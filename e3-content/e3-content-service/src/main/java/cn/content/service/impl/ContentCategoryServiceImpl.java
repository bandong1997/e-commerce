package cn.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.content.service.ContentCategoryService;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
import cn.e3mall.pojo.TbContentExample;

/**
  * @author bandong
  * @version 创建时间：2019年12月13日 下午4:48:06
  * 内容分类service接口实现类
  */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	//注入ContentCategory的mapper层
	@Autowired
	private TbContentCategoryMapper contentCategory;
	@Autowired
	private TbContentMapper contentMapper;
	
	//子节点查询商品分类
	@Override
	public List<EasyUITreeNode> getAllContentCategory(long parentId) {
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategory.selectByExample(example);
		
		List<EasyUITreeNode> node = new ArrayList<EasyUITreeNode>();
		//把list集合里的数据写入在EasyUITreeNode列表中
		for (TbContentCategory t : list) {
			EasyUITreeNode n = new EasyUITreeNode();
			//设置属性
			n.setId(t.getId());
			n.setText(t.getName());
			n.setState(t.getIsParent()?"closed":"open");
			//添加到集合
			node.add(n);
		}
		return node;
	}
	/**
	  * 商品分类新增节点
	 */
	@Override
	public E3mallResult addContentCategory(long parentId, String name) {
		// 创建一个tb_content_category表对应的pojo对象
		TbContentCategory c = new TbContentCategory();
		//设置属性
		c.setParentId(parentId);
		c.setName(name);
		//状态1(正常),2(删除)',
		c.setStatus(1);
		//默认排序是1
		c.setSortOrder(1);
		//新增的节点一定是子节点而不是父节点
		c.setIsParent(false);
		c.setCreated(new Date());
		c.setUpdated(new Date());
		//插入到数据库
		contentCategory.insert(c);
		
		//判断父节点IsParent的属性，不是true改成true
		//根据parentId查询父节点
		TbContentCategory parent = contentCategory.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			//更新数据到genjuId
			contentCategory.updateByPrimaryKey(parent);
		}
		//返回结果
		return E3mallResult.ok();
	}
	/**
	 * 重命名
	 */
	@Override
	public E3mallResult updateContentCategory(long id, String name) {
		// 根据Id查询出信息
		TbContentCategory c = contentCategory.selectByPrimaryKey(id);
		c.setName(name);
		contentCategory.updateByPrimaryKey(c);
		return E3mallResult.ok();
	}
	/**
	 * 删除
	 */
	@Override
	public E3mallResult deleteContentCategory(long id) {
		//根据Id查询
		TbContentCategory tbContentCategory = contentCategory.selectByPrimaryKey(id);
		//进行判断
		if(tbContentCategory.getIsParent()) {
			return E3mallResult.build(1, "失败");
		}else {
			//执行删除
			contentCategory.deleteByPrimaryKey(id);
			//删除关联表的数据
			TbContentExample example2 = new TbContentExample();
			cn.e3mall.pojo.TbContentExample.Criteria criteria2 = example2.createCriteria();
			criteria2.andCategoryIdEqualTo(id);
			contentMapper.deleteByExample(example2);
			//设置查询条件
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			//删除节点时需要判断是否有子节点
			//通过查询的实体里面的parentId来获取父节点信息
			criteria.andParentIdEqualTo(tbContentCategory.getParentId());
			//执行查询
			List<TbContentCategory> childs = contentCategory.selectByExample(example);
			//判断
			if(childs.size()==0) {
				 //判断父节点的isParent属性是否为true如果不是就修改为true
				TbContentCategory parent = contentCategory.selectByPrimaryKey(tbContentCategory.getParentId());
				if(parent.getIsParent()) {
					parent.setIsParent(false);
					//更新到数据库
					contentCategory.updateByPrimaryKey(parent);
				}
			}
		}
		//返回结果，包含pojo
		return E3mallResult.ok();
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
