package cn.dw.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dw.pojo.entity.BuyerCart;
import cn.dw.pojo.entity.Result;

/**
 * bandong
 * 2020年3月16日下午9:35:59
 *  购物车controller层
 */
@RestController
@RequestMapping("/cart")
public class BuyerCartController {
	/**
	 *	跨域问题解决的两种方案
	 * @param itemId
	 * @param num
	 * @return
	 */
	
	//添加商品到购物车
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:8086",allowCredentials="true")
	public Result addGoodsToCartList(Long itemId,Integer num) {
		try {
			System.out.println("添加成功000");
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
		
	}
	
	//查询购物数据
	@RequestMapping("/findCartList")
	public List<BuyerCart> findCartList(){
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
