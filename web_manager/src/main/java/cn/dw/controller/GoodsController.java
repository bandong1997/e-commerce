package cn.dw.controller;


import org.springframework.data.solr.core.SolrTemplate;
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
import cn.dw.service.SolrManagerService;

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
	
	//远程注入商品上下架根据商品id获取库存数据的service层接口
	@Reference
	private SolrManagerService solrManagerService;
	
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
			if(ids != null) {
				for (Long id : ids) {
					//1.根据商品id到数据库中删除
					goodsService.del(id);
					//2.根据商品id到solr索引库中删除对用的数据
					solrManagerService.delItemToSolr(id);
				}
			}
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	//审核商品
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids,String status) {
		try {
			if(ids != null) {
				for (Long id : ids) {
					//商品上架第一步：到数据库中根据商品id改变商品的上架状态
					goodsService.updateStayus(id,status);
					//第二步：对于审核通过的商品，根据商品id获取库存数据，放入到solr中
					if("1".equals(status)) {
						solrManagerService.saveItemSolr(id);
					}
				}
			}
			return new Result(true, "审核成功");
		} catch (Exception e) {
			return new Result(false, "审核失败");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
