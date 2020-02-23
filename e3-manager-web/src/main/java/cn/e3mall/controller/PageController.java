package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * 页面跳转Controller层
 * @author INS
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class PageController {
	
	//跳转首页
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	//根据参数页面跳转
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
}
