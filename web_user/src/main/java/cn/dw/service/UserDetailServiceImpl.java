package cn.dw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * bandong
 * 2020年3月16日下午8:26:10 
 * 	自定义验证类: 实现UserDetailsService接口由安全框架提供
 * 	在之前这里负责用户名密码的校验工作,并给当前用户赋予对应的访问权限
 * 	现在cas和springSecurity集成,集成后,用户名密码的校验工作交给cas完成,所以能够进入到
 * 	这里类的方法中的都是已经成功认证的用户,这里只需要给登录过的用户赋予对应的访问权限就可以
 */

 
public class UserDetailServiceImpl implements UserDetailsService{

	//实现类，根据用户名去数据库验证密码是否正确
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/**授权*/
		// 创建权限集合
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// 向权限集合中加入访问权限
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new User(username, "", authorities);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
