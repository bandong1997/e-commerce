package cn.dw.service;
/**
 * bandong
 * 2020年3月17日下午9:27:42 
 * 	购物车接口
 */

import java.util.List;

import cn.dw.pojo.entity.BuyerCart;

public interface CartService {
	
	//将商品加入到这个人现有的购物车列表中
	public List<BuyerCart> addItemToCartList(List<BuyerCart> cartList,Long itemId,Integer num);
	
	//将购物车列表存储到redis中
	public void setCartListToRedis(String userName,List<BuyerCart> cartList);
	
	//从redis获取购物车列表
	public List<BuyerCart> getCartListFromRedis(String userName);
	
	//将cookie中的数据和redis中的数据进行合并
	public List<BuyerCart> mergeCookirCartListToRedisCartList(List<BuyerCart> cookieCartList,List<BuyerCart> redisCartList);
	
	
	
	
	
	
	
	
	
	
	
	
	
}
