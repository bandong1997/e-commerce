package cn.dw.pojo.entity;
/**
 *  添加商品自定义实体
 *  商品对象、商品详情对象、库存集合对象
 * @author INS
 *
 */

import java.io.Serializable;
import java.util.List;

import cn.dw.pojo.good.Goods;
import cn.dw.pojo.good.GoodsDesc;
import cn.dw.pojo.item.Item;

public class GoodsEntity implements Serializable{

	// 商品对象
	private Goods goods;
	// 商品详情
	private GoodsDesc goodsDesc;
	// 库存集合对象
	private List<Item> itemList;
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public GoodsDesc getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(GoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	
	
}
