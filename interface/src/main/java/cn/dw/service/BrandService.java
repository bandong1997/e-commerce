package cn.dw.service;
/**
 * 
 * @author INS
 *	品牌service接口
 */

import java.util.List;
import java.util.Map;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.good.Brand;
public interface BrandService {

	//全查
	List<Brand> findAll();

	//分页查询品牌/高级分页查询
	PageResult findPage(Brand brand, Integer page, Integer rows);
	//添加品牌
	void add(Brand brand);
	//根据id修改前查询
	Brand getOne(Long id);
	//根据id修改
	void updateBrandById(Brand brand);
	//批量删除
	void deleteIds(Long[] ids);
	//查询下拉列表数据
	List<Map> selectOptionList();
}
