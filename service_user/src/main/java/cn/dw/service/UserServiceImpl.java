package cn.dw.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import cn.dw.dao.user.UserDao;
import cn.dw.pojo.user.User;

/**
 * bandong
 * 2020年3月13日下午4:13:23 用户service接口实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ActiveMQQueue smsDestination;
	
	//从common中取模板号和签名
	@Value("${template_code}")
	private String template_code;
	
	@Value("${sign_name}")
	private String sign_name;
	
	//获取验证码
	@Override
	public void sendCodePhone(String phone) {
		// 1、生成一个随机6位数字,作为验证码
		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i < 7; i++) {
			//获取随机数,随机生成0-1之间的数乘于10
			int s = new Random().nextInt(10); 
			buffer.append(s); //追加到buffer中
		}
		// 2、手机号作为key,验证码作为value保存到redis中,生存时间为10分钟
		redisTemplate.boundValueOps(phone).set(buffer.toString(),60*10,TimeUnit.SECONDS);
		// 3、将手机号,短信内容,模板编号,签名封装成Map消息发送给消息服务器
		jmsTemplate.send(smsDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("mobile", phone); //手机号
				mapMessage.setString("template_code", template_code); //模板号
				mapMessage.setString("sign_name", sign_name);//签名
				Map map = new HashMap<>();
				map.put("code", buffer);
				mapMessage.setString("param", JSON.toJSONString(map)); //参数即是短息内容 验证码 ,使用\是将"改成'
				return mapMessage;
			}
		});
		
		
	}

	//校验页面上的和redis中的验证码是否一致
	@Override
	public Boolean checkSmscode(String phone, String smscode) {
		
		if(phone == null || "".equals(phone) || "".equals(smscode) || smscode == null) {
			return false;
		}
		// 1、根据手机号到redis数据库中获取验证码
		String redisSmscode = (String) redisTemplate.boundValueOps(phone).get();
		// 2、判断页面验证码和redis中的验证码是否一致
		if(smscode.equals(redisSmscode)) {
			return true;
		}
		return false;
	}

	// 注册
	@Override
	public void addUser(User user) {
		userDao.insertSelective(user);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
