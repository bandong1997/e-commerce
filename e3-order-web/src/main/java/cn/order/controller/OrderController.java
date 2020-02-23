package cn.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.cart.service.CartService;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.order.pojo.OrderInfo;
import cn.order.service.OrderService;

/**
  * @author bandong
  * @version 创建时间：2019年12月26日 下午2:50:49
  *  0订单管理
  */
@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrder(HttpServletRequest request) {
		// 取用户id
		TbUser user = (TbUser) request.getAttribute("user");
		// 根据用户id取收货地址列表
		// 使用静态数据。。。。
		// 根据用户id取支付方式
		// 使用静态数据。。。。
		// 根据用户id查询购物车列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		// 把购物车列表响应页面
		request.setAttribute("cartList", cartList);
		// 返回页面
		return "order-cart";
	}
	
	/**
	 * 提交订单
	 */
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String tjOrder(OrderInfo orderInfo,HttpServletRequest request) {
		//取	用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//把用户添加到orderinfo中
		orderInfo.setUserId(user.getId());
 		orderInfo.setBuyerNick(user.getUsername());
		//调用服务生成订单
		E3mallResult result = orderService.createOrder(orderInfo);
		System.out.println(orderInfo);
		//订单提交成功，删除购物车
		if(result.getStatus() == 200) {
			//清空购物车
			cartService.clearCart(user.getId());
		}
		//订单号传给页面
		request.setAttribute("orderId", result.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		//返回逻辑视图
		return "success";
	}
	
	
	
	
	
	
	
	
	
}
