package cn.dw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.dw.dao.specification.SpecificationOptionDao;
import cn.dw.dao.template.TypeTemplateDao;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.specification.SpecificationOption;
import cn.dw.pojo.specification.SpecificationOptionQuery;
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
	@Autowired
	private SpecificationOptionDao specificationOptionDao;

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
	//根据模板ID获得规格的列表的数据：
	@Override
	public List<Map> findBySpecList(Long id) {
		// 1、根据模板id查询模板对象
		TypeTemplate typeTemplate = templateDao.selectByPrimaryKey(id);
		// 2、从模板对象中获取 规格 集合数据，获取的是json格式的字符串	//[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
		String specIds = typeTemplate.getSpecIds();
		// 3、将json格式的字符串，解析成java中的list集合对象
		List<Map> listMap = JSON.parseArray(specIds, Map.class);
		// 4、遍历集合对象
		if(listMap != null) {
			for (Map map : listMap) {
				// 5、遍历的过程中根据规格id查询规格选项集合数据
				Long specId = Long.parseLong(String.valueOf(map.get("id")));
				
				SpecificationOptionQuery example = new SpecificationOptionQuery();
				cn.dw.pojo.specification.SpecificationOptionQuery.Criteria criteria = example.createCriteria();
				criteria.andSpecIdEqualTo(specId);
				List<SpecificationOption> list = specificationOptionDao.selectByExample(example);
				// 6、将规格选项再封装到规格数据中，一起返回
				map.put("options", list);
			}
		}
		
		return listMap;
	}
}
