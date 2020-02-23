package cn.e3mall.service;
/**
 * 商品分类service接口 定义方法
 * @author INS
 */

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	//子节点查询
	List<EasyUITreeNode>getItemCatList(long parentId);
}
