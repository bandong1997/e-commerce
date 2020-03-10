package cn.dw.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.dw.dao.good.BrandDao;
import cn.dw.dao.good.GoodsDao;
import cn.dw.dao.good.GoodsDescDao;
import cn.dw.dao.item.ItemCatDao;
import cn.dw.dao.item.ItemDao;
import cn.dw.dao.seller.SellerDao;
import cn.dw.pojo.entity.GoodsEntity;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.good.Brand;
import cn.dw.pojo.good.Goods;
import cn.dw.pojo.good.GoodsDesc;
import cn.dw.pojo.good.GoodsQuery;
import cn.dw.pojo.good.GoodsQuery.Criteria;
import cn.dw.pojo.item.Item;
import cn.dw.pojo.item.ItemCat;
import cn.dw.pojo.item.ItemQuery;
import cn.dw.pojo.seller.Seller;

/**
 * 添加商品service接口实现类
 * @author INS
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private GoodsDescDao descDao;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private ItemCatDao itemCatDao;
	
	@Autowired
	private BrandDao brandDao;
	
	
	@Autowired
	private SellerDao sellerDao;
	
	
	//添加商品
	@Override
	public void save(GoodsEntity goodsEntity) {
		// 1、保存商品信息
		// 设置商品状态默认为 0 ，代表未审核
		goodsEntity.getGoods().setAuditStatus("0");
		goodsDao.insertSelective(goodsEntity.getGoods());
		// 2、保存商品详情信息
		// 商品主键作为商品详情页面
		goodsEntity.getGoodsDesc().setGoodsId(goodsEntity.getGoods().getId());
		descDao.insertSelective(goodsEntity.getGoodsDesc());
		// 3、保存库存集合数据
		insertItem(goodsEntity);
		
	}
	
	
	
	//使用goodsEntity对象中的数据初始化Item库存对象
	public Item setItemValues(GoodsEntity goodsEntity,Item item) {
		
		// 商品id
		item.setGoodsId(goodsEntity.getGoods().getId());
		// 创建时间
		item.setCreateTime(new Date());
		// 更新时间
		item.setUpdateTime(new Date());
		// 库存状态,默认为 0 代表未审核
		item.setStatus("0");
		// 分类id
		item.setCategoryid(goodsEntity.getGoods().getCategory3Id());
		// 分类名称
		ItemCat itemCat = itemCatDao.selectByPrimaryKey(goodsEntity.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		// 品牌名称
		Brand brand = brandDao.selectByPrimaryKey(goodsEntity.getGoods().getBrandId());
		item.setBrand(brand.getName());
		// 卖家名称
		Seller seller = sellerDao.selectByPrimaryKey(goodsEntity.getGoods().getSellerId());
		item.setSeller(seller.getName());
		// 示例图片
		String images = goodsEntity.getGoodsDesc().getItemImages();
		//格式转换
		List<Map> listMap = JSON.parseArray(images, Map.class);
		if(listMap != null) {
			//取url
			String url = (String) listMap.get(0).get("url");
			item.setImage(url);
		}
		return item;
		
	}
	
	
	//保存库存数据
	public void insertItem(GoodsEntity goodsEntity) {
		if("1".equals(goodsEntity.getGoods().getIsEnableSpec())) {
			//勾选规格复选框，有规格数据
			if(goodsEntity.getItemList() != null) {//集合数据无法添加，需要遍历出来一条一条添加
				for (Item item : goodsEntity.getItemList()) {
					// 商品标题 商品名称+规格组成
					//取商品名称
					String title = goodsEntity.getGoods().getGoodsName();
					//从库存对象中获取前端传入的json格式规格字符串     {"机身内存":"16G","网络":"联通3G"}
					String spec = item.getSpec();
					Map map = JSON.parseObject(spec,Map.class);
					//取map里面的值
					Collection <String>values = map.values();
					//循环拼接
					for (String string : values) {
						title+= ""+string;
					}
					item.setTitle(title);
					//设置Item库存对象数据
					setItemValues(goodsEntity, item);
					//保存到数据库
					itemDao.insertSelective(item);
				}
			}
		}else {
			//没有勾选复选框数据，没有库存数据，但我们要初始化一条数据，不然前端可能报错
			Item item = new Item();
			//设置价格  一刀999
			item.setPrice(new BigDecimal("999"));
			//设置库存量
			item.setNum(0);
			//初始化规格
			item.setSpec("{}");
			//设置库存标题
			item.setTitle(goodsEntity.getGoods().getGoodsName());
			
			setItemValues(goodsEntity, item);
			//保存到数据库
			itemDao.insertSelective(item);
		}
	}


	//分页高级
	@Override
	public PageResult searchGoods(Goods goods, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		//商品查询
		GoodsQuery example = new GoodsQuery();
		Criteria criteria = example.createCriteria();
		if(goods != null) {
			if(goods.getGoodsName() != null && !"".equals(goods.getGoodsName())) {
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus())) {
				criteria.andAuditStatusEqualTo(goods.getAuditStatus());
			}
			if(goods.getSellerId() != null && !"".equals(goods.getSellerId()) && 
					!"admin".equals(goods.getSellerId()) && !"root".equals(goods.getSellerId())) {
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
		}
		Page<Goods> list = (Page<Goods>) goodsDao.selectByExample(example);
		return new PageResult(list.getTotal(), list.getResult());
	}
	//根据商品id查询商品信息页面回显
	@Override
	public GoodsEntity getOne(Long id) {
		// 1、根据商品id查询商品对象
		Goods goods = goodsDao.selectByPrimaryKey(id);
		// 2、根据商品id查询商品详情对象
		GoodsDesc goodsDesc = descDao.selectByPrimaryKey(id);
		// 3、根据商品id查询库存集合对象
		ItemQuery example = new ItemQuery();
		cn.dw.pojo.item.ItemQuery.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<Item> itemList = itemDao.selectByExample(example);
		// 4、将以上查询出来的数据封装到goodsEntity中 
		GoodsEntity goodsEntity = new GoodsEntity();
		goodsEntity.setGoods(goods);
		goodsEntity.setGoodsDesc(goodsDesc);
		goodsEntity.setItemList(itemList);
		return goodsEntity;
	}
	//修改商品
	@Override
	public void updateGoodsEntity(GoodsEntity goodsEntity) {
		// 1、根据商品id修改商品
		goodsDao.updateByPrimaryKeySelective(goodsEntity.getGoods());
		// 2、根据商品id修改商品详情对象
		descDao.updateByPrimaryKeySelective(goodsEntity.getGoodsDesc());
		// 3、根据商品id删除对应的库存数据
		ItemQuery example = new ItemQuery();
		cn.dw.pojo.item.ItemQuery.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goodsEntity.getGoods().getId());
		itemDao.deleteByExample(example);
		// 4、添加库存集合数据
		insertItem(goodsEntity);
	}
	//批删
	@Override
	public void del(Long id) {
		Goods goods = new Goods();
		goods.setId(id);
		goods.setIsDelete("1");//1逻辑删除 0正常】
		goodsDao.updateByPrimaryKeySelective(goods);
	}
	//审核商品
	@Override
	public void updateStayus(Long id, String status) {
		// 1、根据商品id修改商品状态
		Goods goods = new Goods();
		goods.setId(id);
		goods.setAuditStatus(status);
		goodsDao.updateByPrimaryKeySelective(goods);
		// 2、根据商品id修改库存状态
		Item item = new Item();
		item.setStatus(status);
		ItemQuery example = new ItemQuery();
		cn.dw.pojo.item.ItemQuery.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		itemDao.updateByExampleSelective(item, example);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
