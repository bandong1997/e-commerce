package cn.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbUser;
import cn.sso.service.TokenService;

/**
  * @author bandong
  * @version 创建时间：2019年12月25日 下午6:42:31
  * 用户登录处理拦截器
  */
public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 前处理，执行handler之前执行此方法  返回true放行，false拦截  购物车在有没有用户登录的情况下都可以使用
		// 1.从cookie中取TT_TOKEN信息
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 2.如果没有token说明是未登录状态，放行
		if(StringUtils.isBlank(token)) {//判断token是否为空
			return true;
		}
		// 3.取到token，要调sso系统的服务，根据token取用户信息
		E3mallResult result = tokenService.getTbUserByToken(token);
		// 4.没有取到用户信息，登录过期，放行
		if(result.getStatus() != 200 ) {//说明没有取到用户信息
			return true;
		}
		// 5.取到用户信息，登录状态
		TbUser tbUser = (TbUser) result.getData();
		// 6.把用户信息写入request中，只需要在controller层判断request中是否有数据即可，放行
		request.setAttribute("user", tbUser);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后，返回modelAndView之前

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 完成处理，返回modelAndView之后  可以在此处理异常

	}

}
