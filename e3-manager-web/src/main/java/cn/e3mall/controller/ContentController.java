package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import cn.content.service.ContentService;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbContent;

/**
  * @author bandong
  * @version 创建时间：2019年12月16日 上午11:14:11
  *内容管理Con他roller层
  */
@RestController
public class ContentController {

	//注入service层接口
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	public EasyUIDataGridResult getAllContent(Long categoryId,int page, int rows) {
		System.out.println(categoryId+"---------");
		EasyUIDataGridResult result = contentService.getContentAllById(categoryId, page, rows);
		return result;
	}
	
	/**
	 * 添加
	 */
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	public E3mallResult addContentsAll(TbContent content) {
		//调用业务将数据添加到数据库
		E3mallResult result = contentService.addContent(content);
		return result;
	}
	/**
	 * 批量删除
	 */
	@RequestMapping("/content/delete")
	public E3mallResult deleteContents(String ids) {
		E3mallResult result = contentService.deleteContent(ids);
		return result;
	}
	/**
	 * 编辑
	 */
	@RequestMapping(value="/rest/content/edit",method=RequestMethod.POST)
	public E3mallResult updateContent(TbContent content) {
		E3mallResult result = contentService.updateContent(content);
		return result;
	}
	
}
