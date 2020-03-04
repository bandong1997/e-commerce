package cn.dw.service;

import java.util.List;

import cn.dw.pojo.ad.ContentCategory;
import cn.dw.pojo.entity.PageResult;

/**
 *  广告位管理service层接口
 * @author INS
 *
 */
public interface ContentCategoryService {
	//分页查询
	PageResult searchContentCategory(ContentCategory contentCategory, Integer page, Integer rows);
	//添加广告
	void save(ContentCategory contentCategory);
	//批删
	void del(Long[] ids);
	//修改前根据id查询
	ContentCategory getOne(Long id);
	//修改
	void updateContentCategory(ContentCategory contentCategory);
	//查询所有的广告分类
	List<ContentCategory> getAll();

}
