package cn.dw.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.template.TypeTemplate;
import cn.dw.service.TypeService;

/**
 *  模板管理controller层
 * @author INS
 *
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
	
	//远程注入service接口
	@Reference
	private TypeService typeService;

	//修改前根据id查询
	@RequestMapping("/findOne")
	public TypeTemplate findOne(Long id) {
		return typeService.getOne(id);
	}
	// 根据模板ID获得规格的列表的数据：
	//数据格式:[
	//	  		{"id":27,"text":"网络",options:[{"id":1,"option_name":"3G"},{"id":2,"option_name":"4G"}]},
	//	  		{"id":32,"text":"机身内存",options:[{"id":1,"option_name":"128G"},{"id":2,"option_name":"256G"}]}
	//   	 ] 集合里面放的map
	@RequestMapping("/findBySpecList")
	public List<Map> findBySpecList(Long id) {
		return typeService.findBySpecList(id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
