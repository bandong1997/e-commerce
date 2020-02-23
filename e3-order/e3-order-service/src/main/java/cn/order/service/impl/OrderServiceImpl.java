package cn.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import cn.order.pojo.OrderInfo;
import cn.order.service.OrderService;

/**
  * 订单处理服务
  * @author bandong
  * @version 创建时间：2019年12月27日 上午9:54:18
  * 0订单service层 实现类
  */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${ORDER_ITEM_GEN_KEY}")
	private String ORDER_ITEM_GEN_KEY;
	/**
	 * 提交订单
	 */
	@Override
	public E3mallResult createOrder(OrderInfo orderInfo) {
		// 生成订单编号  使用redis的incr生成
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);	
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		// 补全OrderInfo属性
		orderInfo.setOrderId(orderId);
		//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		// 插向订单表
		orderMapper.insert(orderInfo);
		// 向订单明细表插入数据
		List<TbOrderItem> orderItem = orderInfo.getOrderItems();
		for (TbOrderItem t : orderItem) {
			//生成商品明细Id
			String orderItemId = jedisClient.incr(ORDER_ITEM_GEN_KEY).toString();
			//补全pojo属性
			t.setId(orderItemId);
			t.setOrderId(orderId);
			//向明细表插入数据
			orderItemMapper.insert(t);
		}
		// 向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		// 返回e3，包含订单号
		return E3mallResult.ok(orderId);
	}

}
