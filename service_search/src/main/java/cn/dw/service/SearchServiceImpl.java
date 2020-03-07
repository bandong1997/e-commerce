package cn.dw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import cn.dw.pojo.item.Item;
import cn.dw.util.Contents;

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
	@Autowired
	private RedisTemplate redisTemplate;
	
	//简单商品搜索
	@Override
	public Map<String, Object> search(Map searchMap) {
		// 1.根据查询参数，到solr中分页，高亮，排序，过滤查询
		Map<String, Object> resultMap = highlightSearch(searchMap);
		// 2.根据查询参数到solr中获取对应的分类结果集
		List<String> resultList = findGroupCategoryList(searchMap);
		//页面显示
		resultMap.put("categoryList", resultList);
		
		// 3.判断传入的参数中是否有分类名称
		String category = String.valueOf(searchMap.get("category"));
		if(category != null && !"".equals(category)) {
			// 4.如果有分类参数，则根据分类名称查询对应的品牌集合和规格集合
			Map map = findSpecListAndBrandList(category);
			resultMap.putAll(map);//map里面的所有数据放到另一个map中
		}else {
			// 5.如果没有默认根据第一个分类查询对应的品牌集合和规格集合
			Map map = findSpecListAndBrandList(resultList.get(0));
			resultMap.putAll(map);//map里面的所有数据放到另一个map中
		}
		return resultMap;
	}
	
	
	//根据关键字到solr中查询结果，包含分页，高亮，排序，过滤
	public Map<String, Object> highlightSearch(Map searchMap) {

		//获取关键字
		String  keywords = String.valueOf(searchMap.get("keywords"));
		//获取当前页
		Integer pageNo = Integer.parseInt(String.valueOf(searchMap.get("pageNo")));
		//获取每页查多少
		Integer pageSize = Integer.parseInt(String.valueOf(searchMap.get("pageSize")));
		
		/**过滤*/
		//获取页面点击的分类过滤条件
		String category = String.valueOf(searchMap.get("category"));
		//获取页面点击的品牌过滤条件
		String brand = String.valueOf(searchMap.get("brand"));
		//获取页面点击的规格过滤条件
		String spec = String.valueOf(searchMap.get("spec"));
		
		/**获取页面点击的价格过滤条件*/
		String price = String.valueOf(searchMap.get("price"));
		
		
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
		
		/**设置过滤条件*/
		//根据分类过滤查询
		if(category != null && !"".equals(category)) {
			//创建过滤对象
			FilterQuery filterQuery = new SimpleQuery();
			//创建条件对象
			Criteria criteria2 = new Criteria("item_category").is(category);
			//将条件对象放入过滤对象中
			filterQuery.addCriteria(criteria2);
			//将过滤查询对象放入查询对象中
			query.addFilterQuery(filterQuery);
		}
		//根据品牌过滤查询
		if(brand != null && !"".equals(brand)) {
			//创建过滤对象
			FilterQuery filterQuery = new SimpleQuery();
			//创建条件对象
			Criteria criteria2 = new Criteria("item_brand").is(brand);
			//将条件对象放入过滤对象中
			filterQuery.addCriteria(criteria2);
			//将过滤查询对象放入查询对象中
			query.addFilterQuery(filterQuery);
		}
		//根据规格过滤查询,spec中的数据格式{网络:移动4G,内存大小:16G}
		if(spec != null && !"".equals(spec)) {
			//因为查询的spec是个json串所以要将json字符串转成map集合
			Map<String, String> specMap = JSON.parseObject(spec, Map.class);
			if(specMap != null && specMap.size() > 0) {
				Set<Entry<String, String>> entrySet = specMap.entrySet();
				//遍历set集合
				for (Entry<String, String> entry : entrySet) {
					//创建过滤对象
					FilterQuery filterQuery = new SimpleQuery();
					//创建条件对象
					Criteria criteria2 = new Criteria("item_spec_"+entry.getKey()).is(entry.getValue());
					//将条件对象放入过滤对象中
					filterQuery.addCriteria(criteria2);
					//将过滤查询对象放入查询对象中
					query.addFilterQuery(filterQuery);
				}
			}
		}
		//根据价格过滤    0-500 500-1000 1000-1500 ... 3000-*
		if(price != null && !"".equals(price)) {
			//切割 “-”
			String[] split = price.split("-");
			if(split != null && split.length==2) {//说明有数据
				//说明大于等于最小值，如果第一个最小值为0，进入不到这里
				if(!"0".equals(split[0])) {
					//创建过滤对象
					FilterQuery filterQuery = new SimpleQuery();
					//创建条件对象       greaterThanEqual大于等于                          
					Criteria criteria2 = new Criteria("item_price").greaterThanEqual(split[0]);
					//将条件对象放入过滤对象中
					filterQuery.addCriteria(criteria2);
					//将过滤查询对象放入查询对象中
					query.addFilterQuery(filterQuery);
				}
				// *不等于第二个元素，说明小于等于最大值，如果最后的元素也就是最大值为*，进入不到这里
				if(!"*".equals(split[1])) {
					//创建过滤对象
					FilterQuery filterQuery = new SimpleQuery();
					//创建条件对象       greaterThanEqual大于等于              lessThanEqual小于等于          
					Criteria criteria2 = new Criteria("item_price").lessThanEqual(split[1]);
					//将条件对象放入过滤对象中
					filterQuery.addCriteria(criteria2);
					//将过滤查询对象放入查询对象中
					query.addFilterQuery(filterQuery);
				}
			}
		}
		
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
			//循环添加
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
	//根据关键字到solr中查询结果集中对应的分类集合,要分组去重
	public List<String> findGroupCategoryList(Map searchMap){
		
		List<String> list = new ArrayList<>();
		
		//获取关键字
		String  keywords = String.valueOf(searchMap.get("keywords"));
		//创建查询对象
		Query query = new SimpleQuery();
		//设置查询条件
		Criteria criteria = new Criteria("item_keywords").is(keywords);
		//把条件放入查询对象中
		query.addCriteria(criteria);
		
		//创建分组对象
		GroupOptions groupOptions = new GroupOptions();
		//设置根据分分类域进行分组
		groupOptions.addGroupByField("item_category");
		//将分组对象放入查询对象中
		query.setGroupOptions(groupOptions);
		
		//去重查询
		GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);
		
		//获取分类域实体集合
		GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
		// 获取分类域中的实体集合
		Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
		//遍历实体集合
		for (GroupEntry<Item> groupEntry : groupEntries) {
			//取数据
			String string = groupEntry.getGroupValue();
			list.add(string);
		}
		return list;
	}
	//根据分类名称查询品牌集合和规格集合
	public Map findSpecListAndBrandList(String categoryName) {
		// 1.根据分类名称到redis数据库中查询对用的模板id
		Long templateId = (Long) redisTemplate.boundHashOps(Contents.CATEGORY_LIST_REDIS).get(categoryName);
		// 2.根据模板id取redis数据库中查询对用的品牌集合
		List<Map> brandList = (List<Map>) redisTemplate.boundHashOps(Contents.BRAND_LIST_REDIS).get(templateId);
		// 3.根据模板id取redis数据库中查询对用的规格集合
		List<Map> specList = (List<Map>) redisTemplate.boundHashOps(Contents.SPEC_LIST_REDIS).get(templateId);
		// 4.将品牌集合、规格集合封装到map中
		Map map = new HashMap<>();
		map.put("brandList", brandList);
		map.put("specList", specList);
		return map;
	}
}
