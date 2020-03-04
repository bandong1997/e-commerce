package cn.dw.service;

import java.util.List;

import org.bouncycastle.jce.provider.asymmetric.ec.Signature.ecCVCDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.dw.dao.ad.ContentDao;
import cn.dw.pojo.ad.Content;
import cn.dw.pojo.ad.ContentQuery;
import cn.dw.pojo.ad.ContentQuery.Criteria;
import cn.dw.pojo.entity.PageResult;
import cn.dw.util.Contents;

/**
 *  广告管理service层接口实现类
 * @author INS
 *
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {
	
	//注入广告管理dao层接口
	@Autowired
	private ContentDao contentDao;
	//注入RedisTemplate模板
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	//带条件分页查询
	@Override
	public PageResult searchContent(Content content, Integer page, Integer rows) {
		PageHelper.startPage(page,rows);
		ContentQuery example = new ContentQuery();
		Criteria criteria = example.createCriteria();
		if(content != null) {
			if(content.getTitle() != null && !"".equals(content.getTitle())) {
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
		}
		Page<Content> list = (Page<Content>) contentDao.selectByExample(example);
		return new PageResult(list.getTotal(), list.getResult());
	}
	//添加广告
	@Override
	public void save(Content content) {
		// 1.将新的广告添加到数据库
		contentDao.insertSelective(content);
		// 2.根据分类的到redis中删除对用的广告数据
		redisTemplate.boundHashOps(Contents.CONTENT_LIST_REDIS).delete(content.getCategoryId());
	}
	//批删
	@Override
	public void del(Long[] ids) {
		if(ids != null) {
			for (Long id : ids) {
				// 1.根据广告id查询广告对象
				Content content = contentDao.selectByPrimaryKey(id);
				// 2.根据广告对象中的分类id,删除redis中对应的广告集合数据
				redisTemplate.boundHashOps(Contents.CONTENT_LIST_REDIS).delete(content.getCategoryId());
				// 3.根据广告id删除数据库中的广告数据
				contentDao.deleteByPrimaryKey(id);
			}
		}
	}
	//修改前查询
	@Override
	public Content getOne(Long id) {
		return contentDao.selectByPrimaryKey(id);
	}
	//修改
	@Override
	public void updateContent(Content content) {
		// 1.根据广告id查询原来的广告对象
		Content oldContent = contentDao.selectByPrimaryKey(content.getId());
		// 2.根据原来的广告对象中的分类id,到redis中删除对应的广告集合数据
		redisTemplate.boundHashOps(Contents.CONTENT_LIST_REDIS).delete(oldContent.getCategoryId());
		// 3.根据传入的最新的广告对象中的分类id,删除redis中对应的广告集合数据
		redisTemplate.boundHashOps(Contents.CONTENT_LIST_REDIS).delete(content.getCategoryId());
		// 4.将新的广告对象更新到数据库中
		contentDao.updateByPrimaryKeySelective(content);
	}
	//根据广告分类id查询广告信息
	@Override
	public List<Content> findByCategoryId(Long categoryId) {
		ContentQuery example = new ContentQuery();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		return contentDao.selectByExample(example);
	}
	//根据广告分类id查询广告信息redis缓存里
	@Override
	public List<Content> findByCategoryIdFromReids(Long categoryId) {
		//redis存储的数据结构是key-value,相当于一个大的map,map中的key是不可以重复的,所以key是稀缺资源
		// 1.根据广告分类id查询广告信息redis中
		List<Content> contentList = (List<Content>) redisTemplate.boundHashOps(Contents.CONTENT_LIST_REDIS).get(categoryId);
		System.out.println("走redis缓存");
		// 2.如果redis缓存里没有从数据库查询
		if(contentList == null) {
			contentList = findByCategoryId(categoryId);
			System.out.println("走数据库，查询出来后写入缓存");
			// 3.将从数据库查询出来的写入redis中
			redisTemplate.boundHashOps(Contents.CONTENT_LIST_REDIS).put(categoryId, contentList);
		}
		return contentList;
	}
}
