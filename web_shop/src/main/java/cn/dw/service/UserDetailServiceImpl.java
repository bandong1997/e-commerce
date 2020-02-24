package cn.dw.service;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.dw.pojo.seller.Seller;
/**
 *  安全框架 
 * @author INS
 *
 */
public class UserDetailServiceImpl implements UserDetailsService{

	private SellerService sellerService;
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//角色集合
		ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		//添加角色
		list.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		
		// 1、判断用户名是否为空，为空直接返回null
		if(username == null) {
			return null;
		}
		// 2、根据用户名取数据库查用户对象
		Seller one = sellerService.getOne(username);
		// 3、判断用户对象是否为空，为空直接返回null
		if(one != null) {
			// 4、如果用户存在，判断用户是否已审核
			if("1".equals(one.getStatus())) {
				// 5、返回springsecurity的User对象，包含用户名，密码和角色列表
				return new User(username, one.getPassword(), list);
			}
		}
		return null;
	}

}
