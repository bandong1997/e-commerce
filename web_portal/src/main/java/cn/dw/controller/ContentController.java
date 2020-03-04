package cn.dw.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.ad.Content;
import cn.dw.service.ContentService;

/**
 *  广告展示controller层
 * @author INS
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {

	//远程注入广告管理service层接口
	@Reference
	private ContentService contentService;
	
	//根据广告分类id查询广告信息
	@RequestMapping("/findByCategoryId")
	public List<Content>findByCategoryId(Long categoryId){
		return contentService.findByCategoryIdFromReids(categoryId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
