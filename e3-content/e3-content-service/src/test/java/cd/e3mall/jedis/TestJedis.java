package cd.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
  * @author bandong
  * @version 创建时间：2019年12月17日 下午12:08:14
  */
public class TestJedis {

	/**
	 * 单机版测试
	 */
	@Test
	public void testJedis() {
		//创建一个链接Jedis对象，参数host、port
		Jedis jedis = new Jedis("192.168.25.77", 6379);
		//直接操作jedis操作redis,所有jedis命令都对应一个方法
		jedis.set("wink1", "Hello Redis !");
		String wink1 = jedis.get("wink1");
		System.out.println(wink1);
		//关闭连接
		jedis.close();
	}
	
	/**
	 * 连接池的使用
	 */
	@Test
	public void testJedisPool() {
		//创建一个链接池对象，参数是：host、port
		JedisPool jedisPool = new JedisPool("192.168.25.77", 6379);
		//从连接池获得一个连接就是redis对象
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		String wink1 = jedis.get("wink1");
		System.out.println(wink1+"====");
		//关闭redis连接：注 每次使用完关闭连接，连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	
	
	
	/**
	 * redis集群
	 */    
	@Test
	public void testJedisCluster() throws Exception {
		//创建一个JedisCluster对象，他有一个参数nodes是set类型。set中包含若干个HOSTAndPort
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.77", 7001));
		nodes.add(new HostAndPort("192.168.25.77", 7002));
		nodes.add(new HostAndPort("192.168.25.77", 7003));
		nodes.add(new HostAndPort("192.168.25.77", 7004));
		nodes.add(new HostAndPort("192.168.25.77", 7005));
		nodes.add(new HostAndPort("192.168.25.77", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
	 	//直接使用JedisCluster操作redis
		jedisCluster.set("jc", "测试");
		String string = jedisCluster.get("jc");
		System.out.println(string);
		//关闭JedisCluster对象
		jedisCluster.close();
	}
	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
