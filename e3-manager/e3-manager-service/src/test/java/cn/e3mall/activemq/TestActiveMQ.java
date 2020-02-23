package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

/**
  * @author bandong
  * @version 创建时间：2019年12月19日 下午8:34:05
  * activeMQ同步索引库测试类	
  */
public class TestActiveMQ {

	/**
	 * 两种消息传递的方式
	 * 		1.Topic：相当于广播
	 * 		2.Queue:点到点
	 */
	
	/**
	 * 点到点形式发送消息
	 * @throws JMSException 
	 */
	@Test
	public void testQueue() throws Exception {
		//1.创建一个工厂连接对象，需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.77:61616");
		//2.使用工厂创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用Connection对象的start方法
		connection.start();
		//4.创建一个session对象->两个参数：
		//第一个是否开启事务(activemq的事务)，如果开启事务，第二个参数毫无意义一般不开启false，
		//第二个:应答模式，一般两种模式，自动应答和手动应答。一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用session对象创建一个Destination(目的地)对象：两种形式：queue、topic :现在使用queue
		Queue queue = session.createQueue("test_queue");
		//6.使用session对象创建一个Producer(生产者)对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个消息对ActiveMQTextMessage对象返回TextMessage，可以使用TextMessage
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("Hello ActiveMQ-方式一");*/
		TextMessage textMessage = session.createTextMessage("Hello Active-方式二");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	/**
	 *   接收消息
	 */
	@Test
	public void testQueueConsumer()throws Exception{
		//1.创建一个connectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.101:61616");
		//2.创建连接对象connection
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用connection创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个Destination(目的地)对象：两种形式：queue、topic :现在使用queue
		Queue queue = session.createQueue("spring-queue"); 
		//6。使用session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		//7.接收消息
		consumer.setMessageListener(new MessageListener() {
			//匿名内部类
			@Override
			public void onMessage(Message message) {
				//8,。打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					//String text = textMessage.getText();
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待接口消息
		System.in.read();//敲击键盘执行
		//9.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
		
	/**
	 *   广播形式发送消息Topic
	 */
	@Test
	public void testTopic() throws Exception{
		//1.创建一个工厂连接对象，需要指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.101:61616");
		//2.使用工厂创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用Connection对象的start方法
		connection.start();
		//4.创建一个session对象->两个参数：
		//第一个是否开启事务(activemq的事务)，如果开启事务，第二个参数毫无意义一般不开启false，
		//第二个:应答模式，一般两种模式，自动应答和手动应答。一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用session对象创建一个Destination(目的地)对象：两种形式：queue、topic :现在使用Topic
		Topic topic = session.createTopic("test_Topic");
		//6.使用session对象创建一个Producer(生产者)对象
		MessageProducer producer = session.createProducer(topic);
		//7.创建一个消息对ActiveMQTextMessage对象返回TextMessage，可以使用TextMessage
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("Hello topic-方式一");*/
		TextMessage textMessage = session.createTextMessage("Hello topic-方式二");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	
	
	/**
	 *   接收消息
	 */
	@Test
	public void testTopicConsumer()throws Exception{
		//1.创建一个connectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.101:61616");
		//2.创建连接对象connection
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用connection创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个Destination(目的地)对象：两种形式：queue、topic :现在使用topic
		Topic topic = session.createTopic("test_Topic"); 
		//6。使用session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		//7.接收消息
		consumer.setMessageListener(new MessageListener() {
			//匿名内部类
			@Override
			public void onMessage(Message message) {
				//8,。打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					//String text = textMessage.getText();
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("3号已启动....");
		//等待接口消息
		System.in.read();//敲击键盘执行
		//9.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
