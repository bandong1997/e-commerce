package cn.search.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月18日 下午7:27:28
  * 索引库搜索商品service接口
  */

import cn.e3mall.common.pojo.SearchResult;

public interface SearchService {
	//搜索
	SearchResult search(String keyword,int page,int rows)throws Exception;
}
