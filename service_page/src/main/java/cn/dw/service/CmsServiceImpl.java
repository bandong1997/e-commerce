package cn.dw.service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.dubbo.config.annotation.Service;

import cn.dw.dao.good.GoodsDao;
import cn.dw.dao.good.GoodsDescDao;
import cn.dw.dao.item.ItemCatDao;
import cn.dw.dao.item.ItemDao;
import cn.dw.pojo.good.Goods;
import cn.dw.pojo.good.GoodsDesc;
import cn.dw.pojo.item.Item;
import cn.dw.pojo.item.ItemCat;
import cn.dw.pojo.item.ItemQuery;
import cn.dw.pojo.item.ItemQuery.Criteria;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * bandong
 * 2020年3月10日下午9:13:09
 * freemaker模板引擎内容管理服务service接口实现类
 */
@Service
public class CmsServiceImpl implements CmsService,ServletContextAware {

	@Autowired
	private GoodsDao goodsDao;//商品
	@Autowired
	private GoodsDescDao goodsDescDao;//商品详情
	@Autowired
	private ItemDao itemDao;//库存
	@Autowired
	private ItemCatDao itemCatDao;//商品对应的分类
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;//注入freemaker模板引擎
	
	//加载ServletContext接口来获取生成静态页面的位置，
	private ServletContext servletContext;
	
	//获取静态页面需要的数据
	@Override
	public Map<String, Object> findGoodsData(Long goodsId) {
		Map<String, Object>map = new HashMap<String, Object>();
		// 1、获取商品数据
		Goods goods = goodsDao.selectByPrimaryKey(goodsId);
		// 2、获取商品详情数据
		GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(goodsId);
		// 3、获取库存集合数据
		ItemQuery example = new ItemQuery();
		Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goodsId);
		List<Item> itemList = itemDao.selectByExample(example);
		// 4、获取商品对应的分类数据
		if(goods != null) {
			ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(goods.getCategory1Id());//对应的一级分类
			ItemCat itemCat2 = itemCatDao.selectByPrimaryKey(goods.getCategory2Id());//对应的二级分类
			ItemCat itemCat3 = itemCatDao.selectByPrimaryKey(goods.getCategory3Id());//对应的三级分类
			map.put("itemCat1", itemCat1);
			map.put("itemCat2", itemCat2);
			map.put("itemCat3", itemCat3);
			
		}
		// 5、将商品所有的数据放入map集合里
		map.put("goods", goods);
		map.put("goodsDesc", goodsDesc);
		map.put("itemList", itemList);
		return map;
	}

	//生成静态页面
	@Override
	public void creatStaticPage(Long goodsId, Map<String, Object> rootMap) throws Exception {
		// 1、获取模板初始化对象
		Configuration configuration = freeMarkerConfig.getConfiguration();
		// 2、获取模板对象
		Template template = configuration.getTemplate("item.ftl");
		// 3、创建输出流，指定生成静态页面的位置和名称
		String path = goodsId+".html";//文件名称
		String realPath = getPath(path);
		Writer out = new FileWriter(new File(realPath));
		// 4、生成
		template.process(rootMap, out);
		// 5、关闭流
		out.close();
	}

	//把相对路径转化为绝对路径 
	public String getPath(String path) {
		String realPath = servletContext.getRealPath(path);//相对转绝对
		System.out.println(realPath);
		return realPath;
	}
	
	//继承ServletContextAware实现方法来获取servletContext
	//获得servletContext对象
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
