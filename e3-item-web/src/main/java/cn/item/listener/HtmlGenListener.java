package cn.item.listener;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.store.journal.JournalTransactionStore.Tx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import cn.item.pojo.Item;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
  * @author bandong
  * @version 创建时间：2019年12月23日 下午7:38:04
  * 监听商品添加消息，生成对应的静态页面
  * 生成静态页面，接收消息队列 实现MessageListener接口
  */
public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	@Override
	public void onMessage(Message message) {
		try {
			// 创建一个模板 参考jsp
			// 从消息中取商品Id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			// 根据商品Id来查询商品信息，商品信息和商品描述
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getDescByItemId(itemId);
			// 创建数据集，把商品数据封装
			Map map = new HashMap<>();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			// 加载模板对象
			Configuration configuration = freeMarkerConfigurer.createConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			// 创建输出流指定输出目录
			FileWriter writer = new FileWriter(HTML_GEN_PATH+itemId+".html");
			//生成静态页面
			template.process(map, writer);
			//关闭流
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
