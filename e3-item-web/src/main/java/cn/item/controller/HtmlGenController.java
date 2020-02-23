package cn.item.controller;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
  * @author bandong
  * @version 创建时间：2019年12月23日 下午6:27:06
  * 生成静态页面测试
  */
@RestController
public class HtmlGenController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genHtml")
	public String genHtml()throws Exception {
		//通过freeMarkerConfigurer接口创建configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建数据集
		Map map = new HashMap<>();
		map.put("hello", 123456789);
		//指定输出路径
		FileWriter writer = new FileWriter("D:/JavaEE/Freemarker/hello2.html");
		//输出文件
		template.process(map, writer);
		//关闭流
		writer.close();
		
		return "OK";
	}
}
