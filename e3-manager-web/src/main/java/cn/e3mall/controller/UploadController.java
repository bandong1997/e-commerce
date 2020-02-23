package cn.e3mall.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.util.FastDFSClient;
import cn.e3mall.common.util.JsonUtils;

/**
 * 图片上传处理Controller层
 * @author INS
 */
@RestController
public class UploadController {

	//取配置文件的值
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8")
	public String updaloFile(MultipartFile uploadFile) {
		try {
			//使用工具类把图片上传到服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/client.conf");
			//取扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String substring = originalFilename.substring(originalFilename.lastIndexOf(".")+1);//因为取出来的包含点所以+1
			//得到一个文件名和地址
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), substring);
			//补全完整的文件地址
			url = IMAGE_SERVER_URL + url;
			//封装到map中
			Map map = new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			Map map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败");
			return JsonUtils.objectToJson(map);
		}
		
	}
	
	
	
	
}
