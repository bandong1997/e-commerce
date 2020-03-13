package cn.dw.service;

import cn.dw.pojo.user.User;

/**
 * bandong
 * 2020年3月13日下午4:12:22 用户service接口
 */
public interface UserService {
	
	//获取验证码
	void sendCodePhone(String phone);
	//校验页面上的和redis中的验证码是否一致
	Boolean checkSmscode(String phone, String smscode);
	// 注册
	void addUser(User user);

}
