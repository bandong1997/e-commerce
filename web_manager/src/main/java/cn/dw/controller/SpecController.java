package cn.dw.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.Result;
import cn.dw.pojo.entity.SpecEntity;
import cn.dw.pojo.specification.Specification;
import cn.dw.service.SpecService;

/**
 *  规格管理controller层
 * @author INS
 *
 */
@RestController
@RequestMapping("/specification")
public class SpecController {

	//远程注入service接口
	@Reference
	private SpecService specService;
	
	//条件分页查询
	@RequestMapping("/search")
	public PageResult search(Integer page,Integer rows,@RequestBody Specification specification) {
		return specService.searchSpec(specification,page, rows);
	}
	//添加规格
	@RequestMapping("/add")
	public Result add(@RequestBody SpecEntity specEntity) {
		try {
			specService.addSpec(specEntity);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(true, "添加失败");
		}
	}
	//修改前根据id查询
	@RequestMapping("/findOne")
	public SpecEntity findOne(Long id) {
		return specService.getOne(id);
	}
	//修改
	@RequestMapping("/update")
	public Result update(@RequestBody SpecEntity specEntity) {
		try {
			specService.updateSpec(specEntity);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(true, "修改失败");
		}
	}
	//批量删除
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			specService.del(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(true, "删除失败");
		}
	}
	//查询规格选项下拉列表
	@RequestMapping("/selectOptionList")
	public List<Map>selectOptionList(){
		return specService.selectOptionList();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
