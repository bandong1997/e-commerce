package cn.sso.service;
/**
  * @author bandong
  * @version 创建时间：2019年12月24日 下午3:14:42
  * 0用户注册接口
  */

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {

	//数据校验
	E3mallResult checkDate(String param,int type);
	//注册
	E3mallResult register(TbUser user);
}
