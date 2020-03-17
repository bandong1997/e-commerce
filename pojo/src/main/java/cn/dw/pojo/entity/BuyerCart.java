package cn.dw.pojo.entity;

import java.io.Serializable;
import java.util.List;

import cn.dw.pojo.order.OrderItem;

/**
 * bandong
 * 2020年3月16日下午9:05:16
 * 	自定义购物车集合
 */
public class BuyerCart implements Serializable{
	
	private String sellerId;//商家id
	private String sellerName;//商家名称
	private List<OrderItem>orderItemList;//购物项集合
	
	
	
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
}
