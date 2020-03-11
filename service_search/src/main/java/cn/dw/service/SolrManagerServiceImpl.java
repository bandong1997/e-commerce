package cn.dw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import cn.dw.dao.item.ItemDao;
import cn.dw.pojo.item.Item;
import cn.dw.pojo.item.ItemQuery;
import cn.dw.pojo.item.ItemQuery.Criteria;

/**
 * bandong
 * 2020年3月9日下午4:34:15
 *  商品上下架根据商品id获取库存数据的service层接口实现类
 */
@Service
@Transactional
public class SolrManagerServiceImpl implements SolrManagerService {

	@Autowired
	private ItemDao itemDao;
	@Autowired
	private SolrTemplate solrTemplate;
	
	//审核通过的商品，根据商品id获取库存数据，放入到solr索引库中
	@Override
	public void saveItemSolr(Long id) {
		//查询item表数据
		ItemQuery example = new ItemQuery();
		Criteria criteria = example.createCriteria();
		//查询指定的商品库存数据
		criteria.andGoodsIdEqualTo(id);
		List<Item> list = itemDao.selectByExample(example);
		
		if(list != null) {
			//如果查询不为空的话存储数据
			for (Item item : list) {
				//存储规格数据
				String spec = item.getSpec();//得到一个string的json字符串
				Map<String,String> map = JSON.parseObject(spec, Map.class); //将json字符串转换成map类型
				System.out.println(map);
				item.setSpecMap(map);
			}
			//保存
			solrTemplate.saveBeans(list);
			//提交
			solrTemplate.commit();
		}
	}

	//根据商品id到solr索引库中删除对用的数据
	@Override
	public void delItemToSolr(Long id) {
		//创建query查询对象
		Query query = new SimpleQuery();
		//创建条件对象
		org.springframework.data.solr.core.query.Criteria criteria = 
				new org.springframework.data.solr.core.query.Criteria("item_goodsid").is(id);
		//条件对象放入查询对象中
		query.addCriteria(criteria);
		//删除
		solrTemplate.delete(query);
		//提交
		solrTemplate.commit();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
