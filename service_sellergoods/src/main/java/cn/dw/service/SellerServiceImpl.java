package cn.dw.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.tools.classfile.Opcode.Set;

import cn.dw.dao.seller.SellerDao;
import cn.dw.pojo.entity.PageResult;
import cn.dw.pojo.seller.Seller;
import cn.dw.pojo.seller.SellerQuery;
import cn.dw.pojo.seller.SellerQuery.Criteria;

/**
 *  商品入驻service接口实现类
 * @author INS
 *
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {
	
	//加载seller的dao接口
	@Autowired
	private SellerDao sellerDao;

	//商家入驻申请
	@Override
	public void save(Seller seller) {
		seller.setCreateTime(new Date());//申请时间
		seller.setStatus("0"); //状态 0.未审核 1.已审核 2.已关闭
		sellerDao.insertSelective(seller);
	}
	//查询商家申请入住列表，未审核
	@Override
	public PageResult findSeller(Seller seller, Integer page, Integer rows) {
		PageHelper.startPage(page,rows);
		SellerQuery example = new SellerQuery();
		Criteria criteria = example.createCriteria();
		if(seller != null) {
			if(seller.getName() != null && !"".equals(seller.getName())) {
				criteria.andNameLike("%"+seller.getName()+"%");
			}
			if(seller.getNickName() != null && !"".equals(seller.getNickName())) {
				criteria.andNickNameLike("%"+seller.getNickName()+"%");
			}
			if(seller.getStatus() != null && !"".equals(seller.getStatus())) {
				criteria.andStatusEqualTo(seller.getStatus());
			}
		}	
		Page<Seller> list = (Page<Seller>) sellerDao.selectByExample(example);
		return new PageResult(list.getTotal(), list.getResult());
	}
	//根据id查看详情
	@Override
	public Seller getOne(String id) {
		return sellerDao.selectByPrimaryKey(id);
	}
	//审核即根据id修改
	@Override
	public void updateStatus(String sellerId, String status) {
		Seller seller = new Seller();
		seller.setSellerId(sellerId);
		seller.setStatus(status);
		sellerDao.updateByPrimaryKeySelective(seller);
	}
}
