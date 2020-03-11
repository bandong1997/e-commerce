package cn.dw.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.dw.service.SolrManagerService;

/**
 * bandong
 * 2020年3月11日下午9:57:24
 */
public class ItemDeleteListener implements MessageListener{

	@Autowired
	private SolrManagerService solrManagerService; 
	
	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amtm = (ActiveMQTextMessage) message;
		try {
			//商品id
			String goodsId = amtm.getText();
			//根据商品id到solr索引库中删除对用的数据
			solrManagerService.delItemToSolr(Long.parseLong(goodsId));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
