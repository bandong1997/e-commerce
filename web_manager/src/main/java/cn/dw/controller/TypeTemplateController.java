package cn.dw.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.Result;
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
	
	//分页条件查询
	@RequestMapping("/search")
	public PageResult search(@RequestBody TypeTemplate typeTemplate,Integer page,Integer rows) {
		return typeService.searchTypeTemplate(typeTemplate,page,rows);
	}
	//添加模板
	@RequestMapping("/add")
	public Result add(@RequestBody TypeTemplate template) {
		try {
			typeService.save(template);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	//修改前根据id查询
	@RequestMapping("/findOne")
	public TypeTemplate findOne(Long id) {
		return typeService.getOne(id);
	}
	//修改
	@RequestMapping("/update")
	public Result update(@RequestBody TypeTemplate template) {
		try {
			typeService.updateTypetemlate(template);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	
	//批删
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			typeService.del(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
