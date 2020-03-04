package cn.dw.service;

import java.util.List;

import cn.dw.pojo.item.ItemCat;

/**
 *  商品模块1的service层接口
 * @author INS
 *
 */
public interface ItemCatService {
	//查询,根据父id查询分类
	List<ItemCat> findByParentId(Long parentId);
	//根据分类id查询模板id
	ItemCat getOneItem(Long id);
	//查询所有分类信息
	List<ItemCat> getAll();

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
