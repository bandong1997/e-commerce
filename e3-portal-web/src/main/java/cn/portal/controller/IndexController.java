package cn.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
  * @author bandong
  * @version 创建时间：2019年12月13日 下午2:52:50
  * index  controller层
  */
@Controller
public class IndexController {
	
	//取值
	@Value("${CONTENT_LUNBO_CATEGORYID}")
	private Long categoryId;
	
	//注入内容管理service层接口
	@Autowired
	private ContentService contentService;
	//页面跳转首页
	@RequestMapping("/")
	public String index(Model model) {
		List<TbContent> list = contentService.queryAllContentByCategoryId(categoryId);
		model.addAttribute("ad1List", list);
		return "index";
	}

}
