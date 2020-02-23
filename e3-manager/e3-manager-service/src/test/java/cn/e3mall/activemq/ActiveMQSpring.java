package cn.e3mall.activemq;
/**
  * @author bandong
  * @version 创建时间：2019年12月20日 下午2:28:01
  * 	activemq整合spring测试
  */

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActiveMQSpring {

	@Test
	public void testActiveMQSpring() throws Exception{
		//初始化spring容器
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//从容器获得JmsTemplate对象
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		//从容器中获得一个Destination对象 两个id:queueDestination(一对一):topicDestination(一对多)
		Destination destination = (Destination) applicationContext.getBean("queueDestination");
		//发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("测试-active整合spring");
			}
		});
		
	}
}
