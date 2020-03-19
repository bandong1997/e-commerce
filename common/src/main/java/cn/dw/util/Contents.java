package cn.dw.util;
/**
 * bandong
 * 2020年2月28日下午9:27:14
 * 常量接口
 */
public interface Contents {
	//设置redis中key键
	public final static String CONTENT_LIST_REDIS="contentList";
	//分类名称为key 
	public final static String CATEGORY_LIST_REDIS="categoryList";
	//品牌缓存数据库的key
	public final static String BRAND_LIST_REDIS="brandList";
	//规格缓存数据库的key
	public final static String SPEC_LIST_REDIS="specList";
	//存在cookie里面的key
	public final static String CART_LIST_COOKIE="pyg_cartList";
	//存在redis里面的key
	public final static String CART_LIST_REDIS="pyg_cartList";
}
