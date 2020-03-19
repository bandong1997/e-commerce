package cn.dw.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import cn.dw.pojo.entity.BuyerCart;
import cn.dw.pojo.entity.Result;
import cn.dw.service.CartService;
import cn.dw.util.Contents;
import cn.dw.util.CookieUtil;

/**
 * bandong
 * 2020年3月16日下午9:35:59
 *  购物车controller层
 */
@RestController
@RequestMapping("/cart")
public class BuyerCartController {
	/**
	 *	跨域问题解决的两种方案
	 * @param itemId
	 * @param num
	 * @return
	 */
	//远程注入购物车service层接口
	@Reference
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	//添加商品到购物车
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:8086",allowCredentials="true")
	public Result addGoodsToCartList(Long itemId,Integer num) {
		try {
			// 1.获取当前登录用户名称
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			// 2.获取购物车列表
			List<BuyerCart> cartList = findCartList();
			// 3.将当前商品加入购物车列表
			cartList = cartService.addItemToCartList(cartList, itemId, num);
			// 4.判断当前用户是否登录，未登陆用户名为 "anonymousUser"
			if("anonymousUser".equals(userName)) {
				// 4.1.未登录，将购物车列表存入cookie中
				CookieUtil.setCookie(request, response, Contents.CART_LIST_COOKIE, 
						JSON.toJSONString(cartList), 60*60*24, "UTF-8");
			}else {
				// 4.2.已登录，将购物车存入redis中
				cartService.setCartListToRedis(userName, cartList);
			}
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
		
	}
	
	//查询购物车列表数据
	@RequestMapping("/findCartList")
	public List<BuyerCart> findCartList(){
		// 1.获取当前登录用户名称
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		// 2.从cookie中获取购物车列表json格式字符串
		String cookieCartListStr = CookieUtil.getCookieValue(request, Contents.CART_LIST_COOKIE, "UTF-8");
		// 3.如果购物车列表json串为空则返回 []
		if(cookieCartListStr == null || "".equals(cookieCartListStr)) {
			cookieCartListStr = "[]";
		}
		// 4.将购物车列表json串转为对象
		List<BuyerCart> cookieCartList = JSON.parseArray(cookieCartListStr, BuyerCart.class);
		// 5.判断当前用户是否登录，未登陆用户名为 "anonymousUser"
		if("anonymousUser".equals(userName)) {
			// 5.1.未登陆，返回cookie中购物车列表
			return cookieCartList;
		}else {
			// 5.2.已登录，从redis中获取购物车列表
			List<BuyerCart> redisCartList = cartService.getCartListFromRedis(userName);
			// 5.3.判断cookie中是否存在购物车列表
			if(cookieCartList.size() > 0) {
				// 5.3.1如果cookie中存在购物车列表则和redis中的购物车列表合并成一个对象
				redisCartList = cartService.mergeCookirCartListToRedisCartList(cookieCartList, redisCartList);
				// 5.3.2删除cookie中的购物车列表
				CookieUtil.deleteCookie(request, response, Contents.CART_LIST_COOKIE);
				// 5.3.3将合并后的购物车列表存入redis中
				cartService.setCartListToRedis(userName, redisCartList);
			}
			// 5.4返回购物车列表对象
			return redisCartList;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
