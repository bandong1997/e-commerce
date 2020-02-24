package cn.dw.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *  商家入驻controller
 * @author INS
 *
 */

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.entity.Result;
import cn.dw.pojo.seller.Seller;
import cn.dw.service.SellerService;
@RestController
@RequestMapping("/seller")
public class SellerController {
	
	//远程注入seller的service接口
	@Reference
	private SellerService sellerService;
	
	//商家入驻申请
	@RequestMapping("/add")
	public Result add(@RequestBody Seller seller) {
		try {
			sellerService.save(seller); 
			return new Result(true, "正在申请，等待审核");
		} catch (Exception e) {
			return new Result(false, "申请失败");
		}
	}
}
