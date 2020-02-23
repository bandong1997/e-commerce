package cn.content.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月13日 下午4:46:20
  * 内容分类service接口
  */

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3mallResult;

public interface ContentCategoryService {
	//子节点查询内容分类
	List<EasyUITreeNode>getAllContentCategory(long parentId);
	//商品分类新增节点
	E3mallResult addContentCategory(long parentId,String name);
	//重命名
	E3mallResult updateContentCategory(long id,String name);
	//批量删除
	E3mallResult deleteContentCategory(long id);
}
