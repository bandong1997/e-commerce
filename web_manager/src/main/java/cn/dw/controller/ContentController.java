package cn.dw.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.ad.Content;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.Result;
import cn.dw.service.ContentService;

/**
 *  广告管理controller层
 * @author INS
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {
	//远程注入广告管理service层接口
	@Reference
	private ContentService contentController;
	//带条件分页查询
	@RequestMapping("/search")
	public PageResult search(@RequestBody Content content,Integer page,Integer rows) {
		return contentController.searchContent(content,page,rows);
	}
	//添加广告
	@RequestMapping("/add")
	public Result add(@RequestBody Content content) {
		try {
			contentController.save(content);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	//批删
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			contentController.del(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
	}
	//修改前查询
	@RequestMapping("/findOne")
	public Content findOne(Long id) {
		return contentController.getOne(id);
	}
	//修改
	@RequestMapping("/update")
	public Result update(@RequestBody Content content) {
		try {
			contentController.updateContent(content);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
}
