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
 * 2020年3月19日下午5:27:58
 *  自定义验证类: 实现UserDetailsService接口由安全框架提供
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
