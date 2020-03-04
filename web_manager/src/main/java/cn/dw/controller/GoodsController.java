package cn.dw.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.entity.GoodsEntity;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.Result;
import cn.dw.pojo.good.Goods;
import cn.dw.service.GoodsService;

/**
 *  添加商品
 * @author INS
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	//远程注入service层接口
	@Reference
	private GoodsService goodsService;
	
	
	//添加商品
	@RequestMapping("/add")
	public Result add(@RequestBody GoodsEntity goodsEntity) {
		try {
			//获取当前登录的用户信息
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			//当前商品是哪个商家添加的
			goodsEntity.getGoods().setSellerId(userName);
			goodsService.save(goodsEntity);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	//分页高级
	@RequestMapping("/search")
	public PageResult search(@RequestBody Goods goods, Integer page,Integer rows) {
		//取当前登录者用户信息
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.setSellerId(userName);
		return goodsService.searchGoods(goods,page,rows);
	}
	//根据商品id查询商品信息页面回显
	@RequestMapping("/findOne")
	public GoodsEntity findOne(Long id) {
		return goodsService.getOne(id);
	}
	//修改商品
	@RequestMapping("/update")
	public Result update(@RequestBody GoodsEntity goodsEntity) {
		try {
			//获取当前登录用户
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			//获取商品的拥有者
			String sellerId = goodsEntity.getGoods().getSellerId();
			if(!userName.equals(sellerId)) {
				return new Result(false, "权限不足");
			}
			goodsService.updateGoodsEntity(goodsEntity);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	//批删
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			goodsService.del(ids);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	//审核商品
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids,String status) {
		try {
			goodsService.updateStayus(ids,status);
			return new Result(true, "审核成功");
		} catch (Exception e) {
			return new Result(false, "审核失败");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
