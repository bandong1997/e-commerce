package cn.dw.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import cn.dw.utils.SmsUtil;

/**
 * bandong
 * 2020年3月13日下午2:40:59
 */
@Component  //将监听交给spring管理
public class SmsListener {
	@Autowired
	private SmsUtil smsUtil;
	
	@JmsListener(destination="sms")
	public void readSms(Map<String,String> map){
		
		try {
			SendSmsResponse sendSms = smsUtil.sendSms(
					//从map获取传递来的参数
					map.get("mobile"),map.get("template_code"),map.get("sign_name"),map.get("param"));
			
			System.out.println(sendSms.getRequestId());
			System.out.println(sendSms.getCode());
			System.out.println(sendSms.getMessage());
			System.out.println(sendSms.getBizId());
			
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

}
