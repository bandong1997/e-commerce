package cn.e3mall.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
  * @author bandong
  * @version 创建时间：2019年12月13日 下午4:15:05
  */
public class TestPublish {

	@Test
	public void publishService() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		/*while(true) {
			Thread.sleep(1000);
		}*/
		//键盘敲入
		System.out.println("服务已经开启....");
		System.in.read();//输入
		System.out.println("服务已经关闭....");
	}
}
