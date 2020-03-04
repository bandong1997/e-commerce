package cn.dw.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.dw.dao.ad.ContentCategoryDao;
import cn.dw.pojo.ad.ContentCategory;
import cn.dw.pojo.ad.ContentCategoryQuery;
import cn.dw.pojo.ad.ContentCategoryQuery.Criteria;
import cn.dw.pojo.entity.PageResult;

/**
 *  广告位管理service层接口实现类
 * @author INS
 *
 */
@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {

	//注入广告位管理dao接口
	@Autowired
	private ContentCategoryDao contentCategoryDao;

	//分页查询
	@Override
	public PageResult searchContentCategory(ContentCategory contentCategory, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		ContentCategoryQuery example = new ContentCategoryQuery();
		Criteria criteria = example.createCriteria();
		if(contentCategory != null) {
			if(contentCategory.getName() != null && !"".equals(contentCategory.getName())) {
				criteria.andNameLike("%"+contentCategory.getName()+"%");
			}
		}
		Page<ContentCategory> list = (Page<ContentCategory>) contentCategoryDao.selectByExample(example);
		return new PageResult(list.getTotal(), list.getResult());
	}
	//添加广告
	@Override
	public void save(ContentCategory contentCategory) {
		contentCategoryDao.insertSelective(contentCategory);
	}
	//批删
	@Override
	public void del(Long[] ids) {
		if(ids != null) {
			for (Long id : ids) {
				contentCategoryDao.deleteByPrimaryKey(id);
			}
		}
	}
	//修改前根据id查询
	@Override
	public ContentCategory getOne(Long id) {
		return contentCategoryDao.selectByPrimaryKey(id);
	}
	//修改
	@Override
	public void updateContentCategory(ContentCategory contentCategory) {
		contentCategoryDao.updateByPrimaryKeySelective(contentCategory);
	}
	//查询所有的广告分类
	@Override
	public List<ContentCategory> getAll() {
		return contentCategoryDao.selectByExample(null);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
