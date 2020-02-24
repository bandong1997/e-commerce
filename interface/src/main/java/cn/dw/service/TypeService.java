package cn.dw.service;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.template.TypeTemplate;

/**
 *  模板管理service层接口
 * @author INS
 *
 */
public interface TypeService {
	//分页条件查询
	PageResult searchTypeTemplate(TypeTemplate typeTemplate, Integer page, Integer rows);
	//添加模板
	void save(TypeTemplate template);
	//修改前根据id查询
	TypeTemplate getOne(Long id);
	//修改
	void updateTypetemlate(TypeTemplate template);
	//批删
	void del(Long[] ids);

}
