package cn.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.sso.service.LoginService;

/**
  * @author bandong
  * @version 创建时间：2019年12月24日 下午7:11:31
  * 用户登录接口的实现类
  */
@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	/**
	 * 登录
	 */
	@Override
	public E3mallResult userLogin(String username, String password) {
		 // 1.判断用户名和密码是否正确
		//设置查询条件
		 TbUserExample example = new TbUserExample();
		 Criteria criteria = example.createCriteria();
		 criteria.andUsernameEqualTo(username);
		 //执行查询
		 List<TbUser> list = tbUserMapper.selectByExample(example);
		 //进行判断
		 if(list == null || list.size()==0) {
			 return E3mallResult.build(400, "用户名或密码错误");
		 }
		 //取用户信息
		 TbUser user = list.get(0);
		 //判断密码
		 if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			 // 2.如果不正确，返回登录失败
			 return E3mallResult.build(400, "用户名或密码错误");
		 }
		 // 3.如果正确生成token(token是我们自己创建的cookie的id放值session重复)
		 String TT_TOKEN = UUID.randomUUID().toString();
		 // 4.将用户信息写入redsi中，key是token  value是用户信息
		 user.setPassword(null);
		 jedisClient.set("SESSION:"+TT_TOKEN, JsonUtils.objectToJson(user));
		 // 5.设置session的过期时间
		 jedisClient.expire("SESSION"+TT_TOKEN, SESSION_EXPIRE);
		 // 6.把token返回
		return E3mallResult.ok(TT_TOKEN);
	}

}
