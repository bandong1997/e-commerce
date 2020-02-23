package cn.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.search.mapper.ItemMapper;

/**
  * @author bandong
  * @version 创建时间：2019年12月20日 下午3:47:30
  * 监听商品添加消息事件，将对应的消息同步索引库
  */
public class AddItemMessageListener implements MessageListener {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);//从text文本中取出Id
			//等待一会，让事物先提交 等待1秒钟
			Thread.sleep(1000);
			//根据商品Id查询商品信息
			SearchItem item = itemMapper.getItemById(itemId);
			//创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档对象中添加域
			document.addField("id", item.getId());
			document.addField("item_title", item.getTitle());
			document.addField("item_sell_point", item.getSell_point());
			document.addField("item_price", item.getPrice());
			document.addField("item_image", item.getImage());
			document.addField("item_category_name", item.getCategory_name());
			//把文档写入索引库
			solrServer.add(document);
			//提交 
			solrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
