package cd.e3mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;

/**
  * @author bandong
  * @version 创建时间：2019年12月17日 下午2:47:18
  */
public class TestJedisClient {

	@Test
	public void testJedisClient() {
		//初始化一个spring容器
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获得jedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		//jedisClient.set("jedisClient", "完美");
		String string = jedisClient.get("jedisClient");
		System.out.println(string);
	}
}
