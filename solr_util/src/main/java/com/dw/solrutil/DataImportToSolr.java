package com.dw.solrutil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.dw.dao.item.ItemDao;
import cn.dw.pojo.item.Item;
import cn.dw.pojo.item.ItemQuery;
import cn.dw.pojo.item.ItemQuery.Criteria;

/**
 * bandong
 * 2020年3月4日下午5:23:22
 */
@Component
public class DataImportToSolr {

	//注入SolrTemplate模板
	@Autowired
	private SolrTemplate solrTemplate;
	@Autowired
	private ItemDao itemDao; 
	
	//将item表中的数据查询出来放入到solr索引库
	private void dateImportToSolr() {
		//查询item表数据
		ItemQuery example = new ItemQuery();
		Criteria criteria = example.createCriteria();
		//审核过得方可查询放入solr索引库
		criteria.andStatusEqualTo("1");
		List<Item> list = itemDao.selectByExample(example);
		
		if(list != null) {
			//如果查询不为空的话存储数据
			for (Item item : list) {
				//存储规格数据
				String spec = item.getSpec();//得到一个string的json字符串
				Map<String,String> map = JSON.parseObject(spec, Map.class); //将json字符串转换成map类型
				
				/*Map<String,String> newMap = new HashMap();
				Iterator it = map.keySet().iterator();
				while(it.hasNext()) {
					String key = (String) it.next();
					try {
						newMap.put(URLEncoder.encode(key,"UTF-8"),map.get(key));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}*/
				
				System.out.println(map);
				item.setSpecMap(map);
				//item.setSpecMap(newMap);
			}
			//保存
			solrTemplate.saveBeans(list);
			//提交
			solrTemplate.commit();
		}
	}
	
	//执行
	public static void main(String[] args) {
		//加载spring环境
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/application*.xml");
		//调用Id。没有Id直接调用类名首字母小写
		DataImportToSolr bean = (DataImportToSolr) context.getBean("dataImportToSolr");
		//调用方法
		bean.dateImportToSolr();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
