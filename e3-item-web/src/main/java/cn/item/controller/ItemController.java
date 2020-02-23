package cn.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import cn.item.pojo.Item;

/**
  * @author bandong
  * @version 创建时间：2019年12月23日 上午10:14:39
  * 商品详情页面展示Controller层
  */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	/**
	 * 获取商品基本信息返回逻辑视图
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	public String showItemAndItemDesc(@PathVariable Long itemId,Model model) {
		//取商品基本信息
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		//获取商品描述
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		//页面展示
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
