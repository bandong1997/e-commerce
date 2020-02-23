package cn.e3mall.activemq;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
  * @author bandong
  * @version 创建时间：2019年12月20日 下午3:07:56
  */
public class MessageConsumer {
	
	@Test
	public void msgConsumer() throws IOException{
		//初始化一个spring容器
		@SuppressWarnings({ "unused", "resource" })
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//等待
		System.in.read();
	}
}
