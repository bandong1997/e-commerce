package cn.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.sso.service.TokenService;

/**
  * @author bandong
  * @version 创建时间：2019年12月25日 上午8:59:39
  * 根据Token查询用户信息service层接口实现类
  */
@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	
	/**
	 * 从redis中取token来查询用户信息
	 */
	@Override
	public E3mallResult getTbUserByToken(String TT_TOKEN) {
		// 根据token查询redis来获取用户信息
		String json = jedisClient.get("SESSION:"+TT_TOKEN);
		//判断是否获取到用户信息，如果获取不到则登录过期
		if(StringUtils.isBlank(json)) {
			return E3mallResult.build(201, "登录过期,请重新登录");
		}
		//更新token的过期时间,获取用户信息
		jedisClient.expire("SESSION:"+TT_TOKEN, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		//返回结果，包好user对象
		return E3mallResult.ok(user);
	}

}
