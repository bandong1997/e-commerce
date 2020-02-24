package cn.dw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.dw.dao.good.BrandDao;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.good.Brand;
import cn.dw.pojo.good.BrandQuery;
import cn.dw.pojo.good.BrandQuery.Criteria;
/**
 * 品牌service接口实现类
 * @author INS
 *
 */
@Service
@Transactional  //事务
public class BrandServiceImpl implements BrandService {

	//注入dao层接口 spring注入
	@Autowired
	private BrandDao brandDao;
	//全查
	@Override
	public List<Brand> findAll() {
		return brandDao.selectByExample(null);
	}
	//分页查询品牌/条件查询
	@Override
	public PageResult findPage(Brand brand,Integer page, Integer rows) {
		PageHelper.startPage(page,rows); //设置分页参数
		BrandQuery example = new BrandQuery();
		Criteria criteria = example.createCriteria();
		if(brand != null) {
			if(brand.getName() != null && !"".equals(brand.getName())) {
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if(brand.getFirstChar() != null && !"".equals(brand.getFirstChar())) {
				criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
			}
		}
		Page<Brand> list = (Page<Brand>) brandDao.selectByExample(example);
		return new PageResult(list.getTotal(), list.getResult());
	}
	//添加品牌
	@Override
	public void add(Brand brand) {
		//插入数据,插入的时候不会判断传入对象中的属性是否为空,所有字段都参与拼接sql语句执行效率低
		//brandDao.insert(banrd);
		//插入数据,插入的时候会判断传入对象中的属相是否为空,如果为空,不参与拼接sql语句,sql语句会变短执行效率会提高
		brandDao.insertSelective(brand);
	}
	//根据id修改前查询
	@Override
	public Brand getOne(Long id) {
		return brandDao.selectByPrimaryKey(id);
	}
	//根据id修改
	@Override
	public void updateBrandById(Brand brand) {
		brandDao.updateByPrimaryKeySelective(brand);
	}
	//批量删除
	@Override
	public void deleteIds(Long[] ids) {
		if(ids != null) {
			for (Long id : ids) {
				brandDao.deleteByPrimaryKey(id);
			}
		}
	}
	//查询下拉列表数据
	@Override
	public List<Map> selectOptionList() {
		return brandDao.selectOptionList();
	}
	
}
