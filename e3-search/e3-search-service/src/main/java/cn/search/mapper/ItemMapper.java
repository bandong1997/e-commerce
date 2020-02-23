package cn.search.mapper;
/**
  * @author bandong
  * @version 创建时间：2019年12月17日 下午8:27:26
   *    搜索商品自定义方法的mapper层 一键导入索库
  */

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;

public interface ItemMapper {
	//查询所有商品信息
	List<SearchItem>getAllItemList();
	SearchItem getItemById(long itemId);
}
