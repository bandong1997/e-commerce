package cn.item.pojo;

import cn.e3mall.pojo.TbItem;

/**
  * @author bandong
  * @version 创建时间：2019年12月23日 上午9:35:45
  * 商品详情展示
  */
public class Item extends TbItem {

	/**
	 * 父类的内容复制到子类中
	 * 写一个构造方法，通过父类对象初始化子类
	 * @param item
	 */
	public Item(TbItem tbItem) {
		this.setId(tbItem.getId());
		this.setTitle(tbItem.getTitle());
		this.setSellPoint(tbItem.getSellPoint());
		this.setPrice(tbItem.getPrice());
		this.setNum(tbItem.getNum());
		this.setBarcode(tbItem.getBarcode());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
		this.setStatus(tbItem.getStatus());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
	}
	
	public String[] getImages() {
		String image2 = this.getImage();
		if(image2 != null && !"".equals(image2)) {
			String[] strings = image2.split(",");
			return strings;
		}
		return null;
	}
}
