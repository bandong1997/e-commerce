package cn.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.sso.service.RegisterService;

/**
  * @author bandong
  * @version 创建时间：2019年12月24日 下午3:17:59
  * 0用户注册接口实现类
  */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;
	/**
	 * 数据校验 查看要注册的信息在数据库是否存在
	 */
	@Override
	public E3mallResult checkDate(String param, int type) {
		// 根据不同的type执行不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1.用户名  2.手机号 
		if(type == 1) {
			criteria.andUsernameEqualTo(param);
		}else if(type == 2) {
			criteria.andPhoneEqualTo(param);
		}else if(type == 3) {
			criteria.andEmailEqualTo(param);
		}else {
			return E3mallResult.build(400, "信息有误");
		}
		// 执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);
		// 判断结果是否包含数据
		if(list != null && list.size() > 0) {
			// 有数据返回false
			return E3mallResult.ok(false);
		}
		// 没有数据返回true
		return E3mallResult.ok(true);
	}
	/**
	 * 注册
	 */
	@Override
	public E3mallResult register(TbUser user) {
		// 数据有效性校验
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) ||StringUtils.isBlank(user.getPhone())) {
			return E3mallResult.build(400, "注册信息不可为空，注册失败");
		}
		//1.用户名  2.手机号
		E3mallResult result = checkDate(user.getUsername(), 1);
		if(!(boolean) result.getData()) {//判断是否为true
			return E3mallResult.build(4000, "用户已存在");
		}
		result = checkDate(user.getPhone(), 2);
		if(!(boolean) result.getData()) {//判断是否为true
			return E3mallResult.build(4000, "手机号已注册");
		}
		// 补全pojo属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//密码进行md5加密 spring core报下的DigestUtils类
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5DigestAsHex);
		//添加至数据库
		tbUserMapper.insert(user);
		//返回添加成功
		return E3mallResult.ok();
	}

}
