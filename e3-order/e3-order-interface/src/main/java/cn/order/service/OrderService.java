package cn.order.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月27日 上午9:50:47
  * 0订单信息service层接口
  */

import cn.e3mall.common.util.E3mallResult;
import cn.order.pojo.OrderInfo;

public interface OrderService {
	// 提交订单
	E3mallResult createOrder(OrderInfo orderInfo);
}
