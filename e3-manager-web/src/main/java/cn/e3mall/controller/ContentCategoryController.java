package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.content.service.ContentCategoryService;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3mallResult;

/**
  * @author bandong
  * @version 创建时间：2019年12月13日 下午4:58:18
  * 内容分类controller层
  */
@RestController
public class ContentCategoryController {

	//注入内容分类service接口层
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	//子节点查询
	@RequestMapping("/content/category/list")
	public List<EasyUITreeNode> getContentCategoryServices(@RequestParam(value="id",defaultValue="0")long parentId) {
		List<EasyUITreeNode> node = contentCategoryService.getAllContentCategory(parentId);
		return  node;
	}
	/**
	 * 新增商品分类子节点
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	public E3mallResult addContentCategorys(long parentId,String name) {
		E3mallResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	/**
	 * 重命名
	 */
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	public E3mallResult updateContentCategorys(long id,String name) {
		E3mallResult result = contentCategoryService.updateContentCategory(id, name);
		return result;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/content/category/delete/",method=RequestMethod.POST)
	public E3mallResult deleteContentCategory(long id) {
		E3mallResult result = contentCategoryService.deleteContentCategory(id);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
