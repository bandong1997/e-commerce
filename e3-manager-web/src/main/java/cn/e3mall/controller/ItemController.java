package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

/**
 * 
 * @author INS
 *	商品controller层
 */
@RestController
public class ItemController {
	//注入ItemService接口层
	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据Id来查询一条数据
	 */
	@RequestMapping("/item/{id}")
	public TbItem getItemById(@PathVariable Long id) {
		//调用接口方法
		TbItem item = itemService.getItemById(id);
		System.out.println("hhhhh");
		return item;
	}
	/**
	 * 查询所有信息，分页查询
	 */
	@RequestMapping("/item/list")
	public EasyUIDataGridResult getItemAll(Integer page,Integer rows) {
		//调用服务查询列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 添加商品信息
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	public E3mallResult addItem(TbItem item,String desc) {
		E3mallResult result = itemService.addItem(item, desc);
		return result;
	}
	/**
	 * 回显商品描述
	 */
	@RequestMapping("/itemDesc/query/{id}")
	public TbItemDesc getDescByItemId(@PathVariable(value="id") long itemId) {
		TbItemDesc desc = itemService.getDescByItemId(itemId);
		System.out.println(desc+"==========================================");
		return desc;
	}
	/**
	 * 回显商品规格/
	 */
	@RequestMapping("/rest/item/param/item/query/{id}")
	public TbItem getAllItemById(@PathVariable(value="id") long itemId) {
		TbItem item = itemService.getItemById(itemId);
		System.out.println(item+"==========================================");
		return item;
	}
	/**
	 * 修改商品
	 */
	@RequestMapping("/rest/item/update")
	public E3mallResult updateItem(TbItem item,String desc) {
		E3mallResult result = itemService.updateItem(item, desc);
		return result;
	}
	/**
	 * 删除
	 */
	@RequestMapping("/rest/item/delete")
	public E3mallResult deleteItem(String ids) {
		E3mallResult result = itemService.delete(ids);
		return result;
	}
	/**
	 * 下架
	 */
	@RequestMapping("/rest/item/instock")
	public E3mallResult instock(String ids) {
		E3mallResult result = itemService.instock(ids);
		return result; 
	}
	
	/**
	 * 上架
	 */
	@RequestMapping("/rest/item/reshelf")
	public E3mallResult reshelf(String ids) {
		E3mallResult result = itemService.reshelf(ids);
		return result; 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
