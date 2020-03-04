package cn.dw.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.service.SearchService;

/**
 * bandong
 * 2020年3月4日下午8:32:07
 *  商品搜索controller层
 */
@RestController
@RequestMapping("/itemsearch")
public class Searchcontroller {
	
	//远程注入商品搜索层接口
	@Reference
	private SearchService searchService;
	
	//简单商品搜索  携带参数：searchMap
	// 返回值：数据集合，总条数，总页数
	@RequestMapping("/search")
	public  Map<String, Object> search(@RequestBody Map searchMap){
		return searchService.search(searchMap);
	}
}
