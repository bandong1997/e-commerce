package cn.dw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.dw.dao.specification.SpecificationDao;
import cn.dw.dao.specification.SpecificationOptionDao;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.SpecEntity;
import cn.dw.pojo.specification.Specification;
import cn.dw.pojo.specification.SpecificationOption;
import cn.dw.pojo.specification.SpecificationOptionQuery;
import cn.dw.pojo.specification.SpecificationQuery;
import cn.dw.pojo.specification.SpecificationQuery.Criteria;

/**
 *  规格管理接口实现类
 * @author INS
 *
 */
@Service
@Transactional
public class SpecServiceImpl implements SpecService {

	//加载到层接口
	@Autowired
	private SpecificationDao specificationDao;
	@Autowired
	private SpecificationOptionDao specificationOptionDao;
	
	//条件分页查询
	@Override
	public PageResult searchSpec(Specification specification, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		SpecificationQuery example = new SpecificationQuery();
		Criteria criteria = example.createCriteria();
		if(specification != null) {
			if(specification.getSpecName() != null && !"".equals(specification.getSpecName())) {
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
		}
		Page<Specification> list = (Page<Specification>) specificationDao.selectByExample(example);
		return new PageResult(list.getTotal(), list.getResult());
	}
	//添加规格
	@Override
	public void addSpec(SpecEntity specEntity) {
		// 添加规格
		specificationDao.insertSelective(specEntity.getSpecification());
		// 添加选项
		if(specEntity.getSpecificationOptionList()!=null) {
			for (SpecificationOption specificationOption : specEntity.getSpecificationOptionList()) {
				//设置外键
				specificationOption.setSpecId(specEntity.getSpecification().getId());
				specificationOptionDao.insertSelective(specificationOption);
			}
		}
	}
	//修改前根据id查询
	@Override
	public SpecEntity getOne(Long id) {
		// 根据规格id查询规格对象
		Specification specification = specificationDao.selectByPrimaryKey(id);
		// 根据规格id查询规格选项集合数据
		SpecificationOptionQuery example = new SpecificationOptionQuery();
		cn.dw.pojo.specification.SpecificationOptionQuery.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<SpecificationOption> list = specificationOptionDao.selectByExample(example);
		// 封装规格数据和规格选项集合数据
		SpecEntity s = new SpecEntity();
		s.setSpecification(specification);
		s.setSpecificationOptionList(list);
		return s;
	}
	//修改
	@Override
	public void updateSpec(SpecEntity specEntity) {
		// 根据规格id修个规格数据
		specificationDao.updateByPrimaryKeySelective(specEntity.getSpecification());
		// 根据规格id删除规格选项数据
		SpecificationOptionQuery example = new SpecificationOptionQuery();
		cn.dw.pojo.specification.SpecificationOptionQuery.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specEntity.getSpecification().getId());
		specificationOptionDao.deleteByExample(example);
		//维护新的关系
		if(specEntity.getSpecificationOptionList() != null) {
			for (SpecificationOption specificationOption : specEntity.getSpecificationOptionList()) {
				//设置外键
				specificationOption.setSpecId(specEntity.getSpecification().getId());
				specificationOptionDao.insertSelective(specificationOption);
			}
		}
	}
	//批量删除
	@Override
	public void del(Long[] ids) {
		if(ids != null) {
			for (Long id : ids) {
				//根据id删除规格数据
				specificationDao.deleteByPrimaryKey(id);
				//根据规格id删除规格选项
				SpecificationOptionQuery example = new SpecificationOptionQuery();
				cn.dw.pojo.specification.SpecificationOptionQuery.Criteria criteria = example.createCriteria();
				criteria.andSpecIdEqualTo(id);
				specificationOptionDao.deleteByExample(example);
			}
		}
	}
	//查询规格选项下拉列表
	@Override
	public List<Map> selectOptionList() {
		return specificationDao.selectOptionList();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
