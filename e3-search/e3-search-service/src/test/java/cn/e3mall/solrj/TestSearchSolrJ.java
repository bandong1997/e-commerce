package cn.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
  * @author bandong
  * @version 创建时间：2019年12月17日 下午8:36:09
  * 	测试Seacher的使用
  */
public class TestSearchSolrJ {

	/**
	 * 添加
	 */
	@Test
	public void addDocument() throws Exception{
		//创建一个solrService抽象类对象，创建一个连接，连接solr的url
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.77:8090/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		//向文档添加域，文档必须包含一个id域，所有的域名称必须在schema。xml中定义
		solrInputDocument.addField("id", "doc01");
		solrInputDocument.addField("item_title", "测试商品01");
		solrInputDocument.addField("item_price", 1000);
		//把文档写入所以库
		solrServer.add(solrInputDocument);
		//提交
		solrServer.commit();
	}
	/**
	 * 更新；即是添加，根据Id进行更新，有则更新，无则添加
	 */
	@Test
	public void updateDocument() throws Exception{
		//创建一个solrService抽象类对象，创建一个连接，连接solr的url
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.77:8090/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		//向文档添加域，文档必须包含一个id域，所有的域名称必须在schema。xml中定义
		solrInputDocument.setField("id", "doc02");
		solrInputDocument.setField("item_title", "测试更新商品02");
		solrInputDocument.setField("item_price", 1000);
		//把文档写入所以库
		solrServer.add(solrInputDocument);
		//提交
		solrServer.commit();
	}
	/**
	 *   删除
	 */
	@Test
	public void deleteDocument()throws Exception{
		//创建一个solrService抽象类对象，创建一个连接，连接solr的url
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.77:8090/solr/collection1");
		//进行删除
		//solrServer.deleteById("doc01");
		//条件删除
		solrServer.deleteByQuery("id:doc02");
		//提交
		solrServer.commit();
	}
	
	/**
	 * 查询
	 */
	@Test
	public void queryAllItem()throws Exception{
		//创建一个solrServer对象
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.77:8090/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		//query.setQuery("*:*");
		query.set("q", "*:*");
		//执行查询，QueryResponse
		QueryResponse queryResponse = solrServer.query(query);
		//取文档列表，取查询结果的总记录数
		SolrDocumentList documentList = queryResponse.getResults();
		System.out.println("总记录数 === "+documentList.getNumFound());
		//遍历文档列表从中获取域的内容
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}
	
	/**
	 * 复杂查询
	 */
	@Test
	public void queryAllItem2() throws Exception{
		//创建一个solrServer对象
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.77:8090/solr/collection1");
		//创建一个查询对象
		SolrQuery query = new SolrQuery();
		//设置条件
		query.setQuery("手机");
		query.setStart(0);//起始数
		query.setRows(20);//每页显示数
		query.set("df", "item_title");//默认搜索域
		query.setHighlight(true);//高亮显示
		query.addHighlightField("item_title"); //高亮显示域
		query.setHighlightSimplePre("<em>"); //高亮显示前缀
		query.setHighlightSimplePost("</em>"); //高亮显示后缀
		
		//执行查询，QueryResponse
		QueryResponse queryResponse = solrServer.query(query);
		//取文档列表，取查询结果的总记录数
		SolrDocumentList documentList = queryResponse.getResults();
		System.out.println("总记录数 === "+documentList.getNumFound());
		//遍历文档列表从中获取域的内容
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮结果，在documentList里是取不出来的，只有在queryResponse里可以
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			//highlighting是一层一层取数据的
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if(list != null && list.size() >0) {
				title = list.get(0);//域里面只可能有一条数据
			}else {//没有高亮取原来的值
				title = (String) solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}
	
	
	
	
	
	
	
	
	
}
