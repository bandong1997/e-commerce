package cn.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.sso.service.TokenService;

/**
  * @author bandong
  * @version 创建时间：2019年12月25日 上午9:17:41
  * 根据token来查询redis中用户信息的controller层
  */
@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
														//"application/json,charset=utf-8"
	/*@RequestMapping(value="/user/token/{TT_TOKEN}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getTbUserByTokenInRedis(@PathVariable String TT_TOKEN,String callback) {
		E3mallResult result = tokenService.getTbUserByToken(TT_TOKEN);
		//响应结果前，判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)) {
			//把结果封装一个js语句响应
			return callback + "(" + JsonUtils.objectToJson(result) + ");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	@RequestMapping(value="/user/token/{TT_TOKEN}")
	@ResponseBody
	public Object getTbUserByTokenInRedis(@PathVariable String TT_TOKEN,String callback) {
		E3mallResult result = tokenService.getTbUserByToken(TT_TOKEN);
		//响应结果前，判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)) {
			//把结果封装一个js语句响应
			MappingJacksonValue maValue = new MappingJacksonValue(result);
			maValue.setJsonpFunction(callback);
			return maValue;
		}
		return result;
	}
	
}
