package cn.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.search.service.SearchService;

/**
  * @author bandong
  * @version 创建时间：2019年12月18日 下午7:55:31
  * 索引库商品搜索controller层
  */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	@Value("${ROWS}")
	private Integer rows;
	/**
	 * 查询商品列表
	 * @param keyword
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/search")
	public String searchItemList(String keyword, @RequestParam(defaultValue="1")Integer page,Model model) throws Exception {
		//解决乱码
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		
		SearchResult searchResult = searchService.search(keyword, page, rows);
		//结果传给页面(看看页面都是需要什么属性)
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecourdCount());
		model.addAttribute("itemList", searchResult.getItemList());
		//异常测试
		//int a = 1/0;
		//返回逻辑视图
		return "search";
	}
}
