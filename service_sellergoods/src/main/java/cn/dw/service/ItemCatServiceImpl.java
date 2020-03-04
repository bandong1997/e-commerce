package cn.dw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;

import cn.dw.dao.item.ItemCatDao;
import cn.dw.pojo.ad.ContentCategoryQuery.Criteria;
import cn.dw.pojo.item.ItemCat;
import cn.dw.pojo.item.ItemCatQuery;


/**
 *  商品模块1的service层接口实现类
 * @author INS
 *
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

	
	//加载商品木块1 的dao层接口
		@Autowired
		private ItemCatDao itemCatDao;
		
		//查询,根据父id查询分类
		@Override
		public List<ItemCat> findByParentId(Long parentId) {
			//设置条件
			ItemCatQuery example = new ItemCatQuery();
			cn.dw.pojo.item.ItemCatQuery.Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(parentId);
			
			return itemCatDao.selectByExample(example);
		}
		//根据分类id查询模板id
		@Override
		public ItemCat getOneItem(Long id) {
			return itemCatDao.selectByPrimaryKey(id);
		}
		//查询所有分类信息
		@Override
		public List<ItemCat> getAll() {
			List<ItemCat> list = itemCatDao.selectByExample(null);
			return list;
		}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
