package cn.e3mall.common.pojo;

import java.io.Serializable;

/**
  * @author bandong
  * @version 创建时间：2019年12月17日 下午8:21:56
  *  搜索商品需要的数据属性的实体
  */
public class SearchItem implements Serializable{
	private String id;
	private String title;
	private String sell_point;
	private long price;
	private String image;
	private String category_name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSell_point() {
		return sell_point;
	}
	public void setSell_point(String sell_point) {
		this.sell_point = sell_point;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	//取一张图片方法
	public String[] getImages() {
		if(image != null && !"".equals(image)) {
			//使用逗号将其切开
			String[] split = image.split(",");
			return split;
		}
		return null;
	}
	
	
}
