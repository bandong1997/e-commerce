package cn.dw.service;

import java.util.List;
import java.util.Map;

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
	//根据模板ID获得规格的列表的数据：
	List<Map> findBySpecList(Long id);

}
