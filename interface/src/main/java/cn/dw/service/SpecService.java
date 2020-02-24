package cn.dw.service;

import java.util.List;
import java.util.Map;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.SpecEntity;
import cn.dw.pojo.specification.Specification;

/**
 *  规格管理service层接口
 * @author INS
 *
 */
public interface SpecService {
	//条件分页查询
	PageResult searchSpec(Specification specification, Integer page, Integer rows);
	//添加规格
	void addSpec(SpecEntity specEntity);
	//修改前根据id查询
	SpecEntity getOne(Long id);
	//修改
	void updateSpec(SpecEntity specEntity);
	//批量删除
	void del(Long[] ids);
	//查询规格选项下拉列表
	List<Map> selectOptionList();

}
