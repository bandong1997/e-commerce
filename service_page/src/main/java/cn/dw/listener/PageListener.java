package cn.dw.listener;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.dw.service.CmsServiceImpl;

/**
 * bandong
 * 2020年3月11日下午9:58:59
 */
public class PageListener implements MessageListener {

	@Autowired
	private CmsServiceImpl cmsServiceImpl;
	
	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amtm = (ActiveMQTextMessage) message;
		try {
			String goodsId = amtm.getText();
			//获取静态页面需要的数据
			Map<String, Object> goodsData = cmsServiceImpl.findGoodsData(Long.parseLong(goodsId));
			//生成静态页面
			cmsServiceImpl.creatStaticPage(Long.parseLong(goodsId), goodsData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
