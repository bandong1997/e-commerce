package cn.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
  * @author bandong
  * @version 创建时间：2019年12月20日 上午11:57:41
  * 	全局异常处理器  继承springmvc的HandlerExceptionResolver接口来实现全局异常处理
  */
public class GlobaExceptionResolver implements HandlerExceptionResolver {
	//推荐用org.slf4j.Logger;的
	private static final Logger logger = LoggerFactory.getLogger(GlobaExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("测试输出的日志。。。。。。。");
		logger.info("系统发生异常。。。。。。。。");
		logger.error("系统发生异常", ex);
		//发邮件、短息  使用jmail工具包
		//显示一个错误页面  你的网络有问题，请稍后重试！	
		ModelAndView model = new ModelAndView();
		model.setViewName("error/exception");//返回个逻辑视图
		return model;
	}

}
