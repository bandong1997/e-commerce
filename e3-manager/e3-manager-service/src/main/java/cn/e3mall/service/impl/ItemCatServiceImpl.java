package cn.e3mall.service.impl;
import java.util.ArrayList;
/**
 *商品管理service层接口实现类
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	//注入mapper层
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	//根据子节点查询
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		// 根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//创建返回EasyUITreeNode结果的list
		List<EasyUITreeNode> resultList = new ArrayList<>();
		//把list集合里面的列表转换为EasyUITreeNode列表
		for (TbItemCat itemCat : list) {
			//新建EasyUITreeNode
			EasyUITreeNode node = new EasyUITreeNode();
			//设置属性
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent()?"closed":"open");//判断Boolean型的state
			//添加到EasyUITreeNode
			resultList.add(node);
		}
		return resultList;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
