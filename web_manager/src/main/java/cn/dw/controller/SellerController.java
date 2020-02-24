package cn.dw.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.entity.Result;
import cn.dw.pojo.seller.Seller;
import cn.dw.service.SellerService;

/**
 *  审核商家入住申请
 * @author INS
 * web_shop,也是这样只不多端口号不一样
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	//查询商家申请入住列表，未审核
	@RequestMapping("/search")
	public PageResult search(Integer page,Integer rows,@RequestBody Seller seller) {
		return sellerService.findSeller(seller,page,rows);
	}
	//根据id查看详情
	@RequestMapping("/findOne")
	public Seller findOne(String id) {
		return sellerService.getOne(id);
	}
	//审核即根据id修改
	@RequestMapping("/updateStatus")
	public Result updateStatus(String sellerId,String status) {
		try {
			sellerService.updateStatus(sellerId,status);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
