package cn.dw.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.Result;
import cn.dw.pojo.good.Brand;
import cn.dw.service.BrandService;

/**
 * 	品牌表controller接口
 * @author INS
 *
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

	//远程注入service层
	@Reference
	private BrandService brandService;
	
	//全查
	@RequestMapping("/findAll")
	public List<Brand>findAll(){
		return brandService.findAll();
	}
	//分页查询
	@RequestMapping("/findByPage")
	public PageResult findPage(Integer page,Integer rows) {
		return brandService.findPage(null,page,rows);
	}
	//添加品牌
	@RequestMapping("/save")
	public Result save(@RequestBody Brand brand) {
		try {
			brandService.add(brand);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	//根据id修改前查询
	@RequestMapping("/findById")
	public Brand findOne(Long id) {
		return brandService.getOne(id);
	}
	//根据id修改
	@RequestMapping("/update")
	public Result update(@RequestBody Brand brand) {
		try {
			brandService.updateBrandById(brand);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	//批量删除
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			brandService.deleteIds(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}	
	}
	//分页高级查询
	@RequestMapping("/search")
	public PageResult search(Integer page,Integer rows,@RequestBody Brand brand) {
		return brandService.findPage(brand, page, rows); 
	}
	//查询下拉列表数据
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}
	
	
	
	
	
	
	
}
