package cn.dw.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.dw.pojo.entity.Result;
import cn.dw.pojo.user.User;
import cn.dw.service.UserService;
import cn.dw.util.PhoneFormatCheckUtils;

/**
 * bandong
 * 2020年3月13日下午4:09:00  用户controller表现层
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Reference
	private UserService userService;

	//获取验证码
	@RequestMapping("/sendCode")
	public Result sendCode(String phone) {
		try {
			//手机号为空的话返回失败
			if(phone == null && "".equals(phone)) {
				return new Result(false, "发送失败");
			}
			//手机号校验
			if(!PhoneFormatCheckUtils.isChinaPhoneLegal(phone)) {
				return new Result(false, "手机号码格式不正确");
			}
			//执行方法
			userService.sendCodePhone(phone);
			//返回结果
			return new Result(true, "发送成功");
		} catch (Exception e) {
			return new Result(false, "发送失败");
		}
	}
	
	//用户注册
	@RequestMapping("/add")
	public Result add(@RequestBody User user,String smscode) {
		try {
			//校验页面上的和redis中的验证码是否一致
			Boolean isCheck = userService.checkSmscode(user.getPhone(),smscode);
			if(!isCheck) {
				return new Result(false, "验证码有误，请重新输入");
			}
			//添加前完善表结构
			user.setStatus("Y");//使用状态（Y正常 N非正常）
			user.setCreated(new Date());
			user.setUpdated(new Date());
			user.setSourceType("1");//会员来源：1:PC，2：H5，3：Android，4：IOS，5：WeChat
			// 注册
			userService.addUser(user);
			return new Result(true, "注册成功");
		} catch (Exception e) {
			return new Result(false, "注册失败");
		}
	}
	
	
	public void testGit() {
		System.out.println("删除");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
