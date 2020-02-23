package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.codemodel.internal.JStatement;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
/**
 * 
 * @author INS
 * 员工service层接口方法实现类
 */
@Service
public class ItemServiceImpl implements ItemService {
	//注入ItemMapper层
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;//消息队列
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	
	//取值
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;//前缀
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;//过期时间
	/**
	 * 根据Id来查询一条数据
	 */
	@Override   
	public TbItem getItemById(Long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(json)) {//判断json串是否为空
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				System.out.println("走缓存...");
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有查询数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		System.out.println("查询数据库...");
		if(item!=null) {
			//将从数据库查询出来的数据添加至缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(item));
				//设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return item;
		}
		return null;
	}
	//配置分页
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//设置记录数
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem>pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		//设置总记录数
		result.setTotal(total);
		return result;
	}
	/**
	 * 添加商品信息
	 */
	@Override
	public E3mallResult addItem(TbItem item, String desc) {
		//使用工具类生成Id
		final long itemId = IDUtils.genItemId();
		//补全item属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建商品描述表对应的pojo
		TbItemDesc tbItemDesc = new TbItemDesc();
		//补全商品描述表属性
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(tbItemDesc);
		//发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
		//返回成功
		return E3mallResult.ok();
	}
	/**
	 * 页面回显商品描述
	 */
	@Override
	public TbItemDesc getDescByItemId(Long itemId) {
		TbItemDesc desc = itemDescMapper.selectByPrimaryKey(itemId);
		//设置new E3mallResult(null) ，即设置status=200
		desc.setE3mallResult(new E3mallResult(null));
		return desc;
	}
	/**
	 * 修改商品信息
	 */
	@Override
	public E3mallResult updateItem(TbItem item, String desc) {
		//通过Id进行查询此条数据
		TbItem item2 = itemMapper.selectByPrimaryKey(item.getId());
		//修改商品表属性
		item2.setCid(item.getCid());
		item2.setTitle(item.getTitle());
		item2.setSellPoint(item.getSellPoint());
		item2.setImage(item.getImage());
		item2.setPrice(item.getPrice());
		item2.setNum(item.getNum());
		item2.setBarcode(item.getBarcode());
		itemMapper.updateByPrimaryKey(item2);
		//修改商品描述
		TbItemDesc tbItemDesc = new TbItemDesc();
		//要指定Id判断修改那个
		tbItemDesc.setItemId(item.getId());
		tbItemDesc.setItemDesc(desc);
		
		itemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
		return E3mallResult.ok();
	}
	/**
	 * 批量删除
	 */
	@Override
	public E3mallResult delete(String ids) {
		//获取逗号分隔符
		String[] split = ids.split(",");
		//循环删除
		for (String string : split) {
			//删除商品表
			itemMapper.deleteByPrimaryKey(Long.valueOf(string));//字符串转整型
			//删除商品描述表
			itemDescMapper.deleteByPrimaryKey(Long.valueOf(string));
		}
		return E3mallResult.ok();
	}
	/**
	 * 批量下架
	 */
	@Override
	public E3mallResult instock(String ids) {
		//逗号分割
		String[] split = ids.split(",");
		//遍历一个一个修改
		for (String string : split) {
			//根据ids查询出此条数据
			TbItem item = itemMapper.selectByPrimaryKey(Long.valueOf(string));
			//修改商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte) 2);
			//进行修改操作
			itemMapper.updateByPrimaryKey(item);
		}
		return E3mallResult.ok();
		
	}
	/**
	 * 批量上架
	 */
	@Override
	public E3mallResult reshelf(String ids) {
		//逗号分割
		String[] split = ids.split(",");
		//遍历一个一个修改
		for (String string : split) {
			//根据ids查询出此条数据
			TbItem item = itemMapper.selectByPrimaryKey(Long.valueOf(string));
			//修改商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte) 1);
			//进行修改操作
			itemMapper.updateByPrimaryKey(item);
		}
		return E3mallResult.ok();
	}
	
	/**
	 * 根据id来查询一条商品描述
	 */
	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)) {//判断json串是否为空
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				System.out.println("走缓存...");
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有查询数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		System.out.println("查询数据库...");
		//将从数据库查询出来的数据添加至缓存
		if(itemDesc!=null) {
			try {
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
				//设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return itemDesc;
		}
		return null;
	}
	
}
