package cn.e3mall.service;
/**
 * 
 * @author INS
 * 商品service接口 定义方法
 */

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {
	//根据Id来查询一条商品信息数据
	TbItem getItemById(Long itemId);
	//根据id来查询一条商品描述
	TbItemDesc getItemDescById(Long itemId);
	//分页
	EasyUIDataGridResult getItemList(int page,int rows);
	//添加商品信息
	E3mallResult addItem(TbItem item,String desc);
	//页面回显商品描述
	TbItemDesc getDescByItemId(Long itemId);
	//修改商品信息
	E3mallResult updateItem(TbItem item,String desc);
	//删除
	E3mallResult delete(String ids);
	//上架
	E3mallResult instock(String ids);
	//下架
	E3mallResult reshelf(String ids);
}
