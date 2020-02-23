package cn.cart.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月25日 下午7:19:56
  * 添加服务端购物层service层
  */

import java.util.List;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItem;

public interface CartService {

	//添加购物车
	E3mallResult addCartInRedis(Long userId,Long itemId,int num);
	//合并购物车
	E3mallResult mergeCart(Long userId,List<TbItem>itemList);
	//取购物车列表
	List<TbItem> getCartList(Long userId);
	//更新购物车数量
	E3mallResult  updateCartNum(Long userId,Long itemId,int num);
	//删除购物车信息
	E3mallResult  deleteCart(Long userId,Long itemId);
	//清空购物车
	E3mallResult clearCart(Long userId);
	
}
