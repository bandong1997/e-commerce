package cn.e3mall.solrj;

import java.util.Collection;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
  * @author bandong
  * @version 创建时间：2019年12月19日 下午5:56:59
  * 测试solr集群
  */
public class TestSolrCloud {

	/**
	 * 香向集群中添加测试数据
	 * @throws Exception
	 */
	@Test
	public void testSolrCloudAdd() throws Exception{
		//创建一个集群的连接，使用CloudSolrServer创建;参数：zkHost:是zookeeper的地址列表
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.77:2182,192.168.25.77:2183,192.168.25.77:2184");
		//设置一个默认的defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档添加域
		document.addField("id", "dous2");
		document.addField("item_title", "测试商品02");
		document.addField("item_price", 10001);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	@Test
	public void testSolrCloudupdate() throws Exception{
		//创建一个集群的连接，使用CloudSolrServer创建;参数：zkHost:是zookeeper的地址列表
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.77:2182,192.168.25.77:2183,192.168.25.77:2184");
		//设置一个默认的defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档添加域 set 和 add 的区别 add:知识添加 ，set：id存在时，执行修改，不存在时执行添加
		document.setField("id", "dous2");
		document.setField("item_title", "测试商品02.1");
		document.setField("item_price", 10001);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	/**
	 * 删除
	 */
	@Test
	public void testSolrCloudDelete() throws Exception{
		//创建一个集群的连接，使用CloudSolrServer创建;参数：zkHost:是zookeeper的地址列表
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.77:2182,192.168.25.77:2183,192.168.25.77:2184");
		//设置一个默认的defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		//进行删除
		//solrServer.deleteById("dous2");
		//设置条件删除
		solrServer.deleteByQuery("id:dous1");
		//提交
		solrServer.commit();
	}
	/**
	 * 查询
	 */
	@Test
	public void queryAll() throws Exception {
		//创建一个集群的连接，使用CloudSolrServer创建;参数：zkHost:是zookeeper的地址列表
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.77:2182,192.168.25.77:2183,192.168.25.77:2184");
		//设置默认的的defaultCollection对象
		solrServer.setDefaultCollection("collection2");
		//创建SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置条件
		query.set("q", "*:*");
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//取文档列表信息
		SolrDocumentList results = queryResponse.getResults();
		System.out.println("总记录数："+ results.getNumFound());
		//获取域里的内容
		for (SolrDocument solrDocument : results) {
			System.out.println("编号="+solrDocument.get("id"));
			System.out.println("商品名称="+solrDocument.get("item_title"));
			System.out.println("商品价格="+solrDocument.get("item_price"));
			System.out.println("--------------------------------");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
