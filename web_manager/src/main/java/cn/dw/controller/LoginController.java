package cn.dw.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  用户controller
 * @author INS
 *
 */	
@RestController
@RequestMapping("/login")
public class LoginController {
	
	//获取当前登陆用户名
	@RequestMapping("/showName")
	public Map showName() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap<>();
		map.put("username", name);
		return map;
	}
}
