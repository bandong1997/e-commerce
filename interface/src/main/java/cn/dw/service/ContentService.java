package cn.dw.service;

import java.util.List;

import cn.dw.pojo.ad.Content;
import cn.dw.pojo.entity.PageResult;

/**
 *  广告管理service层接口
 * @author INS
 *
 */
public interface ContentService {
	//带条件分页查询
	PageResult searchContent(Content content, Integer page, Integer rows);
	//添加广告
	void save(Content content);
	//批删
	void del(Long[] ids);
	//修改前查询
	Content getOne(Long id);
	//修改
	void updateContent(Content content);
	//根据广告分类id查询广告信息
	List<Content> findByCategoryId(Long categoryId);
	//根据广告分类id查询广告信息redis缓存里
	List<Content> findByCategoryIdFromReids(Long categoryId);
}
