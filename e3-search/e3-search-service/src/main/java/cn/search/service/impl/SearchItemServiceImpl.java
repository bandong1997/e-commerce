package cn.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.util.E3mallResult;
import cn.search.mapper.ItemMapper;
import cn.search.service.SearchItemService;

/**
  * @author bandong
  * @version 创建时间：2019年12月18日 上午9:08:40
  * 	索引库维护service接口的实现类
  */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	//查询注入
	@Override
	public E3mallResult importAllItems() {
		try {
			//查询商品列表
			List<SearchItem> list = itemMapper.getAllItemList();
			//遍历商品理解
			for (SearchItem item : list) {
				//创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				//向文档对象添加域
				document.addField("id", item.getId());
				document.addField("item_title", item.getTitle());
				document.addField("item_sell_point", item.getSell_point());
				document.addField("item_price", item.getPrice());
				document.addField("item_image", item.getImage());
				document.addField("item_category_name", item.getCategory_name());
				//把文档对象写入索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			//返回导入成功
			return E3mallResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3mallResult.build(500, "数据注入索引库失败");
		}
	}

}
