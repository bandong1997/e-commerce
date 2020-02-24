package cn.dw.service;

import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.seller.Seller;

/**
 *  商品入驻接口
 * @author INS
 *
 */
public interface SellerService {
	//商家入驻申请
	void save(Seller seller);
	//查询商家申请入住列表，未审核
	PageResult findSeller(Seller seller, Integer page, Integer rows);
	//根据id查看详情
	Seller getOne(String id);
	//审核即根据id修改
	void updateStatus(String sellerId, String status);

}
