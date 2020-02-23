package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
  * @author bandong
  * @version 创建时间：2019年12月18日 下午6:50:33
  * 	索引库查询返回结果封装
  */
public class SearchResult implements Serializable{
	private long recourdCount;//总记录数
	private int totalPages;
	private List<SearchItem> itemList;//商品列表
	public long getRecourdCount() {
		return recourdCount;
	}
	public void setRecourdCount(long recourdCount) {
		this.recourdCount = recourdCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
}
