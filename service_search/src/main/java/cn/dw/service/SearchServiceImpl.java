package cn.dw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;

import cn.dw.pojo.item.Item;

/**
 * bandong
 * 2020年3月4日下午8:37:58
 * 搜索服务service层接口实现类
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {

	//注入SolrTemplate模板进行CRUD
	@Autowired
	private SolrTemplate solrTemplate;
	//简单商品搜索
	@Override
	public Map<String, Object> search(Map searchMap) {
		
		//获取关键字
		String  keywords = String.valueOf(searchMap.get("keywords"));
		//获取当前页
		Integer pageNo = Integer.parseInt(String.valueOf(searchMap.get("pageNo")));
		//获取每页查多少
		Integer pageSize = Integer.parseInt(String.valueOf(searchMap.get("pageSize")));
		
		//创建查询对象
		//Query query = new SimpleQuery();
		//创建高亮查询对象
		HighlightQuery query = new SimpleHighlightQuery();
		//查询条件  
		/**
		 * contains:相当于数据库中like模糊查询的方式将查询关键字当做一个整体进行模糊查询，
		 * is:将查询关键字使用这个域的分词器进行切分词，然后将切分出来的每个词进行查询
		 */
		Criteria criteria = new Criteria("item_keywords").is(keywords);
		//将查询条件放到查询对象中
		query.addCriteria(criteria);
		
		//判断传来的pageNo是否等于null或者小于0，给他一个默认值
		if(pageNo == null || pageNo <=0) {
			pageNo = 1;
		}
		//设置从哪里开始查
		query.setOffset((pageNo-1)*pageSize);
		//设置每页查多少
		query.setRows(pageSize);
		
		/**设置高亮显示选项对象*/
		HighlightOptions options = new HighlightOptions();
		//设置那个域需要高亮显示
		options.addField("item_title");
		//设置高亮显示前缀
		options.setSimplePrefix("<em style = 'color:red'>");
		//设置高亮显示后缀
		options.setSimplePostfix("</em>");
		//将高亮选项设置到查询对象中
		query.setHighlightOptions(options);
		
		
		
		//普通查询数据
		//ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
		//高亮数据查询
		HighlightPage<Item> items = solrTemplate.queryForHighlightPage(query, Item.class);
		//获取带高亮显示的集合  包含查询的数据和高亮显示的数据	
		List<HighlightEntry<Item>> highlightEntries = items.getHighlighted();
		ArrayList<Item> list = new ArrayList<>();
		//遍历高亮集合
		for (HighlightEntry<Item> highlightEntry : highlightEntries) {
			//获取不带高亮的实体对象
			Item item = highlightEntry.getEntity();
			//获取highlights集合
			List<Highlight> highlights = highlightEntry.getHighlights();
			if(highlights != null && highlights.size() > 0) {
				//snipplets集合里面包含着高亮数据
				List<String> snipplets = highlights.get(0).getSnipplets();
				if(snipplets != null && snipplets.size() > 0) {
					//取到高亮显示标题
					String title = snipplets.get(0);
					//赋值
					item.setTitle(title);
				}
			}
			//循环添加数据
			list.add(item);
		}
		
		//返回数据至页面
		Map<String,Object> map = new HashMap<String, Object>();
		//map.put("rows", items.getContent());//数据
		map.put("rows", list);//数据
		map.put("totalPages", items.getTotalPages()); //页数
		map.put("total", items.getTotalElements());//条数
		
		return map;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
