package cn.dw.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.item.ItemCat;
import cn.dw.service.ItemCatService;

/**
 *  商品模块1
 * @author INS
 *
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	//远程注入商品模块1的service接口
	@Reference
	private ItemCatService itemCatService;
	
	
	//查询,根据父id查询分类
	@RequestMapping("/findByParentId")
	public List<ItemCat> findByParentId(Long parentId){
		return itemCatService.findByParentId(parentId);
	}
	
	//根据分类id查询模板id
	@RequestMapping("/findOne")
	public ItemCat findOne(Long id) {
		return itemCatService.getOneItem(id);
	}
	
	//查询所有分类信息
	@RequestMapping("/findAll")
	public List<ItemCat> findAll(){
		List<ItemCat> list = itemCatService.getAll();
		return list;
	}
	
	
	
	
	
	
}
