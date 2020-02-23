package cn.content.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月16日 上午11:00:54
  * 内容管理接口
  */

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	
	//根据Id分页查询
	EasyUIDataGridResult getContentAllById(Long categoryId,int page,int rows);
	//添加
	E3mallResult addContent(TbContent content);
	//批量删除
	E3mallResult deleteContent(String ids);
	//修改
	E3mallResult updateContent(TbContent content);
	//前端首页轮播查询
	List<TbContent>queryAllContentByCategoryId(long categoryId);
}
