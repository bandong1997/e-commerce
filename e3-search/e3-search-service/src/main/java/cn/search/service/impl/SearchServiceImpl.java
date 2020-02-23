package cn.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.search.dao.SearchDAO;
import cn.search.service.SearchService;

/**
  * @author bandong
  * @version 创建时间：2019年12月18日 下午7:33:07
  * 索引库搜索商品service接口实现类
  */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDAO searchDAO;
	/**
	 * 搜索商品
	 * @throws Exception 
	 */
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		//创建solrquery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery(keyword);	
		//设置分页条件
		if(page <= 0) page = 1;
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_title");
		//开启高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//调dao执行查询
		SearchResult search = searchDAO.search(query);
		//计算总页数
		long recourdCount = search.getRecourdCount();
		int totalPages = (int) (recourdCount / rows);
		if(recourdCount % rows > 0 ) {
			totalPages ++;
		}
		//添加返回结果
		search.setTotalPages(totalPages);
		//返回结果
		return search;
	}

	
	
	
	
	
	
	
	
}
