package cn.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
  * @author bandong
  * @version 创建时间：2019年12月25日 下午3:20:34
  * 购物车Controller层ceshi
  */
@Controller
public class CartController {
	//注入service接口
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	
	@Value("${CART_COOKIE_EXPIRE}")
	private Integer CART_COOKIE_EXPIRE;
	
	
	/**
	 * 从cookie中取购物车列表处理
	 * HttpServletRequest request ：取cookie
	 * HttpServletResponse response ：写入cookie
	 * 
	 */
	private List<TbItem>getCartListFromCookie(HttpServletRequest request){
		//取cookie信息
		String json = CookieUtils.getCookieValue(request, "cart", true);
		//判断json是否为空
		if(StringUtils.isBlank(json)) {
			return new ArrayList<>(); //如果取出的json为空则返回一个ArrayList集合，放值空指针
		}
		//将json串转为集合
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 添加购物车
	 * 返回成功页面
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String showTbItemCart(@PathVariable Long itemId,@RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否登陆
		TbUser user = (TbUser) request.getAttribute("user");
		//判断是否为登录状态，如果是登陆状态写入redis
		if(user != null) {
			//写入服务端
			cartService.addCartInRedis(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		
		//如果未登陆写入cookie执行以下操作 ↓
		//从cookie中取购物车列表 
		List<TbItem> list = getCartListFromCookie(request);
		//判断商品在商品列表是否存在
		boolean flag = false;
		for (TbItem tbItem : list) {
			//包装的数据类型对象不能直接==比较，要使用longValue得到一个简单的数据类型
			if(tbItem.getId() == itemId.longValue()) {
				flag = true;
				//如果存在数量相加
				tbItem.setNum(tbItem.getNum()+num);
				//跳出循环
				break;
			}
		}
		//因为我们并只知道它进没进循环所以立个flag 
		//判断flag是否为false如果为false就说明不存在
		if(!flag) {
			//根据商品Id查询商品信息，得到一个TbItem对象
			TbItem tbItem = itemService.getItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			//把商品添加到商品列表
			list.add(tbItem); 
		}
		
		//写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_COOKIE_EXPIRE, true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	/**
	 * 购物车结算页面跳转
	 */
	@RequestMapping("/cart/cart")
	public String showCatList(HttpServletRequest request,HttpServletResponse response) {
		//从cookie中取购物车列表
		List<TbItem> list = getCartListFromCookie(request);
		// 判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果是登录状态
		if(user != null) {
			// 从cookie取购物车列表
			// 如果不为空，把cookie中的购物车商品和服务端的购物车商品合并
			cartService.mergeCart(user.getId(), list);
			// 把cookie中的购物车移除
			CookieUtils.deleteCookie(request, response, "cart");
			// 从服务端取购物车信息
			list = cartService.getCartList(user.getId());
		}
		
		//未登陆状态执行 ↓
		//从cookie中取购物车列表
		//将数据响应给页面
		request.setAttribute("cartList", list);
		//返回逻辑视图
		return "cart";
	}
	
	/**
	 * 更新数据库商品数量
	 */
	@RequestMapping(value="/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3mallResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否未登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			//更新数据库商品数量
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3mallResult.ok();
		}
		//从cookie中取购物车列表
		List<TbItem> list = getCartListFromCookie(request);
		//循环商品列表找到对应的商品
		for (TbItem tbItem : list) {
			if(tbItem.getId().longValue() == itemId) {
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//购物车信息写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_COOKIE_EXPIRE, true);
		//返回结果
		return E3mallResult.ok();
	}
	
	/**
	 * 购物车删除商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartById(@PathVariable long itemId,HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否未登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null) {
			//更新数据库商品数量
			cartService.deleteCart(user.getId(), itemId);
			return "redirect:/cart/cart";
		}
		
		//从cookie获取购物车列表
		List<TbItem> list = getCartListFromCookie(request);
		//循环遍历找寻要删除的商品
		for (TbItem tbItem : list) {
			if(tbItem.getId().longValue() == itemId) {
				//删除商品
				list.remove(tbItem);
				//跳出循环
				break;
			}
		}
		//重新写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), CART_COOKIE_EXPIRE, true);
		//重定向返回逻辑视图
		return "redirect:/cart/cart";
	}
	
	public void Test() {
		System.out.println("直接刪除此测试方法即可");
	}
	
	
}
