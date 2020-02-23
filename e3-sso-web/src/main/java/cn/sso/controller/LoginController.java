package cn.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
import cn.sso.service.LoginService;

/**
  * @author bandong
  * @version 创建时间：2019年12月24日 下午6:31:14
  * 0用户登录Controller层
  */
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	/**
	 * 登录
	 * @return
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult login(String username,String password,HttpServletRequest request,HttpServletResponse response) {
		E3mallResult result = loginService.userLogin(username, password);
		//判断是否登录成功
		if(result.getStatus() == 200) {
			String TT_TOKEN = result.getData().toString();
			//登录成功将token写入cookie中
			CookieUtils.setCookie(request, response, TOKEN_KEY, TT_TOKEN);
		}
		return result;
	}
}
