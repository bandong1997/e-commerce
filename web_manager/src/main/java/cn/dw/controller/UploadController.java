package cn.dw.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.dw.pojo.entity.Result;
import cn.dw.util.FastDFSClient;

/**
 *  文件上传
 * @author INS
 *
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
	/**
	 * springmvc提供文件上传的功能
	 * 接收文件的类使用MultipartFile接口
	 * 同时要在xml配置文件解析器
	 * @throws Exception 
	 */
	@Value(value = "${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;
	//上传图片
	@RequestMapping("/uploadFile")
	public Result uploadFile(MultipartFile file) throws Exception {
		try {
			//加载配置文件
			FastDFSClient client = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
			String url = client.uploadFile(file.getBytes(), file.getOriginalFilename(),file.getSize());
			return new Result(true, FILE_SERVER_URL+url);
		} catch (Exception e) {
			return new Result(false, "上传失败");
		}
		
		
	}
}
