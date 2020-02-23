package cn.sso.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月25日 上午8:58:09
  * 0根据Token查询用户信息
  */

import cn.e3mall.common.util.E3mallResult;

public interface TokenService {
	//根据Token查询用户信息
	E3mallResult getTbUserByToken(String TT_TOKEN);
}
