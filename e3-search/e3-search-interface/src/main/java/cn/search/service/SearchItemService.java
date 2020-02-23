package cn.search.service;
/**	
  * @author bandong
  * @version 创建时间：2019年12月18日 上午9:05:10
  *  	索引库维护接口
  */

import cn.e3mall.common.util.E3mallResult;

public interface SearchItemService {
	//查询注入，只需返回成功、失败即可
	E3mallResult importAllItems();
}
