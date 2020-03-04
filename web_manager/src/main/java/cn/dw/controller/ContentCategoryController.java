package cn.dw.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.ad.ContentCategory;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.Result;
import cn.dw.service.ContentCategoryService;

/**
 *  广告位管理controller
 * @author INS
 *
 */
@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

	//远程注入广告位管理service层接口
	@Reference
	private ContentCategoryService contentCategoryService;
	//分页查询
	@RequestMapping("/search")
	public PageResult search(@RequestBody ContentCategory contentCategory,Integer page,Integer rows) {
		return contentCategoryService.searchContentCategory(contentCategory,page,rows);
	}
	//添加广告
	@RequestMapping("/add")
	public Result add(@RequestBody ContentCategory contentCategory) {
		try {
			contentCategoryService.save(contentCategory);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	//批删
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			contentCategoryService.del(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
	}
	//修改前根据id查询
	@RequestMapping("/findOne")
	public ContentCategory findOne(Long id) {
		return contentCategoryService.getOne(id);
	}
	//修改
	@RequestMapping("/update")
	public Result update(@RequestBody ContentCategory contentCategory) {
		try {
			contentCategoryService.updateContentCategory(contentCategory);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	//查询所有的广告分类
	@RequestMapping("/findAll")
	public List<ContentCategory> findAll(){
		return contentCategoryService.getAll();
	}
}
