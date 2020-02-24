package cn.dw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.dw.dao.template.TypeTemplateDao;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.template.TypeTemplate;
import cn.dw.pojo.template.TypeTemplateQuery;
import cn.dw.pojo.template.TypeTemplateQuery.Criteria;

/**
 * 	模板管理service层接口实现类
 * @author INS
 *
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {
	//注入dao接口
	@Autowired
	private TypeTemplateDao templateDao;

	//分页条件查询
	@Override
	public PageResult searchTypeTemplate(TypeTemplate typeTemplate, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		TypeTemplateQuery example = new TypeTemplateQuery();
		Criteria criteria = example.createCriteria();
		if(typeTemplate != null) {
			if(typeTemplate.getName() != null && !"".equals(typeTemplate.getName())) {
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
		}
		Page<TypeTemplate> list = (Page<TypeTemplate>) templateDao.selectByExample(example);
		return new PageResult(list.getTotal(), list.getResult());
	}
	//添加模板
	@Override
	public void save(TypeTemplate template) {
		templateDao.insertSelective(template);
	}
	//修改前根据id查询
	@Override
	public TypeTemplate getOne(Long id) {
		return templateDao.selectByPrimaryKey(id);
	}
	//修改
	@Override
	public void updateTypetemlate(TypeTemplate template) {
		templateDao.updateByPrimaryKeySelective(template);
	}
	//批删
	@Override
	public void del(Long[] ids) {
		if(ids != null) {
			for (Long id : ids) {
				templateDao.deleteByPrimaryKey(id);
			}
		}
	}
}
