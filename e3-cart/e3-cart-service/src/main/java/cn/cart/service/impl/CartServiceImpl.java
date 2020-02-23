package cn.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

/**
  * @author bandong
  * @version 创建时间：2019年12月25日 下午7:20:29
  * 添加服务端购物层service层实现类
  */
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CART_REDIS}")
	private String CART_REDIS;
	/**
	 * 添加购物车
	 */
	@Override
	public E3mallResult addCartInRedis(Long userId, Long itemId, int num) {
		// 1.向redi中添加购车
		// 2.数据类型hash key是用户Id field 商品Id  value:商品信息
		// 3.判断商品是否存在
		Boolean hexists = jedisClient.hexists(CART_REDIS + ":" +userId, itemId + "");
		// 4.如果存在数量相加
		if(hexists) {
			String json = jedisClient.hget(CART_REDIS + ":" +userId, itemId + "");
			//将json转成pojo
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			//数量相加
			item.setNum(item.getNum() + num);
			//写回redis
			jedisClient.hset(CART_REDIS + ":" +userId, itemId + "", JsonUtils.objectToJson(item));
			return E3mallResult.ok();
		}
		// 5.如果不存在根据商品Id取商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//设置购物车数据量
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)) {
			item.setImage(image.split(",")[0]);
		}
		// 6.添加到购物车列表
		jedisClient.hset(CART_REDIS + ":" +userId, itemId + "", JsonUtils.objectToJson(item));
		// 7.返回成功
		return E3mallResult.ok();
	}
	/**
	 * 合并购物车
	 */
	@Override
	public E3mallResult mergeCart(Long userId, List<TbItem> itemList) {
		// 遍历商品列表
		// 把列表添加到购物车
		// 判断购物车中是否有此商品
		// 如果有数量相加
		// 如果没有添加新的商品
		for (TbItem tbItem : itemList) {
			addCartInRedis(userId, tbItem.getId(), tbItem.getNum());
		}
		//返回成功
		return E3mallResult.ok();
	}
	/**
	 * 取购物车列表
	 */
	@Override
	public List<TbItem> getCartList(Long userId) {
		// 根据用户Id查询购物车
		List<String> jsonList = jedisClient.hvals(CART_REDIS + ":" +userId);
		List<TbItem>list = new ArrayList<TbItem>();
		for (String string : jsonList) {
			//将json串转为tbItem对象
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			//添加到列表
			list.add(item);
		}
		//返回结果
		return list;
	}
	/**
	 * 更新购物车数量
	 */
	@Override
	public E3mallResult updateCartNum(Long userId, Long itemId, int num) {
		// 从redis中取商品列表
		String json = jedisClient.hget(CART_REDIS + ":" +userId, itemId + "");
		//更新数量
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		//写入redis
		jedisClient.hset(CART_REDIS + ":" +userId, itemId + "", JsonUtils.objectToJson(item));
		return E3mallResult.ok();
	}
	/**
	 * 删除购物车
	 */
	@Override
	public E3mallResult deleteCart(Long userId, Long itemId) {
		// 删除
		jedisClient.hdel(CART_REDIS + ":" +userId, itemId + "");
		return E3mallResult.ok();
	}
	/**
	 * 清空购物车
	 */
	@Override
	public E3mallResult clearCart(Long userId) {
		// 删除
		jedisClient.del(CART_REDIS + ":" +userId);
		return E3mallResult.ok();
	}

}
