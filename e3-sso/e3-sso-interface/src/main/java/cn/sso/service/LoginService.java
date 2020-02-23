package cn.sso.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月24日 下午7:07:03
  * 用户登录接口
  */

import cn.e3mall.common.util.E3mallResult;

public interface LoginService {

	//参数:用户名和密码
	/**
	 * 业务逻辑
	 * 1.判断用户名和密码是否正确
	 * 2.如果不正确，返回登录失败
	 * 3.如果正确生成token(token是我们自己创建的cookie的id放值session重复)
	 * 4.将用户信息写入redsi中，key是token  value是用户信息
	 * 5.设置session的过期时间
	 * 6.把token返回
	 */
	//返回值 E3mallResult ,其中包含token信息
	E3mallResult userLogin(String username,String password);
}
