package cn.dw.service;
/**
 * bandong
 * 2020年3月10日下午9:12:06
 * 	freemaker模板引擎内容管理服务service接口
 */

import java.util.Map;

public interface CmsService {

	//获取静态页面需要的数据
	Map<String, Object> findGoodsData(Long goodsId);
	//生成静态页面
	void creatStaticPage(Long goodsId,Map<String,Object> rootMap) throws Exception;
}
