package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;

/**
 * 商品分类controller层
 * @author INS
 *
 */
@Controller
public class ItemCatController {

	//注入商品分类的service层接口
	@Autowired
	private ItemCatService itemCatService;
	
	//子节点查询
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatAll(@RequestParam(value="id",defaultValue="0")Long parentId){
		//调用服务查询子节点列表		
		List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
		return list;
	}
}
