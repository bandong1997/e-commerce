package cn.e3mall.fast;
/**
 * FastDfs图片上传测试类
 * @author INS
 *
 */

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.util.FastDFSClient;

public class FastDfcTest {

	
	@Test
	public void testUpload() throws Exception {
		//创建一个配置文件，文件名随意，内容是配置tracker服务器地址
		//使用全局对象加载配置文件
		ClientGlobal.init("E:/WorkSpace/EclipsePractice/EclipseWorkSpace-2/e3-manager-web/src/main/resources/config/client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackClient获取Trankservice对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StorageServer对象可以为空
		StorageServer storageServer = null;
		//创建一个StorageClient对象，需要参数是Trankservice和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用StrorageClient上传文件‪D:\\影音\\图片\\乱图\\壁纸\\a.jpg
		String[] strings = storageClient.upload_file("D:/影音/图片/乱图/壁纸/a.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	//使用工具类测试
	@Test
	public void testDastDfsTest() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("E:/WorkSpace/EclipsePractice/EclipseWorkSpace-2/e3-manager-web/src/main/resources/config/client.conf");
		String string = fastDFSClient.uploadFile("D:/影音/图片/乱图/壁纸/b.jpg");
		System.out.println(string);
	}
	
	
	
	
	
	
	
	
	
	
	
}
