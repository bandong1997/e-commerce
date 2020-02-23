package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3mallResult;
import cn.search.service.SearchItemService;

/**
  * @author bandong
  * @version 创建时间：2019年12月18日 上午9:53:43
  * 	索引库controller层
  */
@Controller
public class SearchItemController {

	//注入service接口
	@Autowired
	private SearchItemService itemService;
	
	//注入
	@RequestMapping(value="/index/item/import",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult getAllItems() {
		E3mallResult e3mallResult = itemService.importAllItems();
		return e3mallResult;
	}
}
