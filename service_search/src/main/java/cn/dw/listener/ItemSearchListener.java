package cn.dw.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.dw.service.SolrManagerService;

/**
 * bandong
 * 2020年3月11日下午9:44:19实现MessageListener接口
 */

public class ItemSearchListener implements MessageListener {

	@Autowired
	private SolrManagerService managerService;
	
	@Override
	public void onMessage(Message message) { //message是接收到的消息
		//强转的原因：为了方便获取文本消息，将原生的消息对象转换为activeMQ的文本消息
		ActiveMQTextMessage amtm  = (ActiveMQTextMessage) message; 
		try {
			//text就是商品的id
			String goodsId = amtm.getText();
			//商品id获取库存详细数据，放入到solr索引库中
			managerService.saveItemSolr(Long.parseLong(goodsId));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
