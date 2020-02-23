package cn.order.lnterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.sso.service.TokenService;

/**
  * @author bandong
  * @version 创建时间：2019年12月26日 下午3:31:14
  * 0提交订单前用登录拦截器
  */
public class OrderInterceptor implements HandlerInterceptor {

	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从cookie取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 判断token是否存在不为空
		if(StringUtils.isBlank(token)) {
			// 不存在，说明是未登陆，跳转sso系统的登录页面，登陆成功后跳转到当前请求的url  request.getRequestURI()当前的url
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		// 存在调用sso系统根据token来取用户信息
		E3mallResult e3mallResult = tokenService.getTbUserByToken(token);
		//判断e3mallResult是否取到了用户信息
		if(e3mallResult.getStatus() != 200) {
			// 如果取不到用户信息，说明登录过期，跳转到登录页面
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		// 取到用户信息，是登录状态将其写进request中
		TbUser user = (TbUser) e3mallResult.getData();
		request.setAttribute("user", user);
		// 判断cookie中是否有购物车数据，有的话合并
		String cookieValue = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(cookieValue)) {
			//合并数据库
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cookieValue, TbItem.class));
		}
		// 放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
