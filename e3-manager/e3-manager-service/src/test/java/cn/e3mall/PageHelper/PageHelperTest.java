package cn.e3mall.PageHelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
	
	@Test
	public void testPageHelper() throws Exception{
		
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获取mapper对象
		TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
		//执行语句前设置分页使用pageHelper的startPage方法
		PageHelper.startPage(1, 20);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = mapper.selectByExample(example);
		//取分页信息PageInfo,
		PageInfo<TbItem>pageInfo = new PageInfo<>(list);
		System.out.println("总记录数： "+pageInfo.getTotal());
		System.out.println("总页数： "+pageInfo.getPages());
		System.out.println("当前页数据： "+list.size());
		System.out.println("当前页： "+pageInfo.getPageNum());
		
	}
	
}
