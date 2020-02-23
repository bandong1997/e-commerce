package cn.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
  * @author bandong
  * @version 创建时间：2019年12月20日 下午2:52:40
  * 	MessageListener实现类
  */
public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		//取消息内容
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println(textMessage.getText());
			System.out.println("e3-search-service-cn.search.messge-MessageListener实现类");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
