package cn.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
  * @author bandong
  * @version 创建时间：2019年12月18日 下午7:05:34
  * 索引库商品搜索返回结果dao层
  */
@Repository
public class SearchDAO {
	//注入SolrServer来获取solrQuery对象进行查询
	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) throws Exception {
		//根据query查询索引库
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList documentList = queryResponse.getResults();
		//去查询结果总记录数
		long numFound = documentList.getNumFound();
		SearchResult result = new SearchResult();
		result.setRecourdCount(numFound);//取出来的总记录数加载到封装的实体里
		//取商品列表，高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : documentList) {
			SearchItem item = new SearchItem();
			item.setId((String) solrDocument.get("id"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			
			//取高亮 一层一层取通过id取item_title
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if(list != null && list.size() > 0) {
				title = list.get(0);
			}else {
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			
			//添加到商品列表
			itemList.add(item);
		}
		result.setItemList(itemList);//商品列表
		//返回结果
		return result;
	}
}
