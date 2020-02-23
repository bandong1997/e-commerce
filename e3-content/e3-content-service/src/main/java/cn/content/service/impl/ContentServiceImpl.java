package cn.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.content.service.ContentService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
import sun.tools.jar.resources.jar;

/**
  * @author bandong
  * @version 创建时间：2019年12月16日 上午11:05:07
  * 内容管理service接口实现类
  */
@Service
public class ContentServiceImpl implements ContentService {

	//注入contentmapper层
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	//加载属性文件里的值
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	//根据Id分页查询
	@Override
	public EasyUIDataGridResult getContentAllById(Long categoryId,int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		//添加条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//设置记录数
		result.setRows(list);
		//取分页结果
		PageInfo<TbContent> info = new PageInfo<>(list);
		//去记录总数
		long total = info.getTotal();
		//设置总记录数
		result.setTotal(total);
		return result;
	}
	/**
	 * 添加
	 */
	@Override
	public E3mallResult addContent(TbContent content) {
		//补全content的属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//将数据添加收到数据库
		contentMapper.insert(content);
		//缓存同步，删除缓存中对应的数据，切记删除hash
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3mallResult.ok();
	}
	/**
	 * 批量删除
	 */
	@Override
	public E3mallResult deleteContent(String ids) {
		//获取逗号分割符
		String[] split = ids.split(",");
		//循环删除
		for (String string : split) {
			//先查询出来数据来获取里面的CategoryId属性
			TbContent content = contentMapper.selectByPrimaryKey(Long.valueOf(string));
			//执行删除
			contentMapper.deleteByPrimaryKey(Long.valueOf(string));
			//缓存同步，删除缓存中对应的数据
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		}
		return E3mallResult.ok();
	}
	/**
	 * 修改
	 */
	@Override
	public E3mallResult updateContent(TbContent content) {
		content.setUpdated(new Date());
		//更新到数据库
		contentMapper.updateByPrimaryKeySelective(content);
		//缓存同步，删除缓存中对应的数据
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3mallResult.ok();
	}
	
	/**
	 * 前端首页轮播查询
	 * 根据内容分类的ID查询内容列表
	 * 添加缓存：为了不影响代码正常使用，尽量写在try{}catch(){}里
	 */
	@Override
	public List<TbContent> queryAllContentByCategoryId(long categoryId) {
		//查询缓存
		try {
			//如果缓存中存在，直接响应结果。 
			String json = jedisClient.hget(CONTENT_LIST, categoryId + "");
			if(StringUtils.isNotBlank(json)) {//判断json串是否为空
				//不为空执行 将json转为一个列表
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果查询不到，那就执行查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置条件
		criteria.andCategoryIdEqualTo(categoryId);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		System.out.println("未走缓存.......");
		//将查询出来的数据加载到redis中
		try {
			jedisClient.hset(CONTENT_LIST, categoryId + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
