package cn.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbUser;
import cn.sso.service.RegisterService;

/**
  * @author bandong
  * @version 创建时间：2019年12月24日 下午2:36:34
  *  00注册
  */
@Controller
public class RegisterController {

	@Autowired
	private RegisterService registerService;
	
	/**
	 * 0跳转注册页面
	 * @return
	 */
	@RequestMapping("/")
	public String showRegister() {
		return "register";
	}
	@RequestMapping("/page/register")
	public String showRegister1() {
		return "register";
	}
	
	/**
	 * 数据校验
	 * @return
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3mallResult checkUser(@PathVariable String param,@PathVariable Integer type ) {
		return registerService.checkDate(param, type);
	}
	
	/**
	 * 注册
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult register(TbUser user) {
		return registerService.register(user);
	}
	/**
	 * 注册成功跳转登录页面
	 */
	@RequestMapping(value="/page/login")
	public String login(String redirect,Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	
	
	
	
	
	
	
	
	
	
}
