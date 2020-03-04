package cn.dw.service;

import java.util.Map;

/**
 * bandong
 * 2020年3月4日下午8:36:40
 * 商品搜索service层接口
 */
public interface SearchService {
	//简单商品搜索 
	Map<String, Object> search(Map searchMap);

}
