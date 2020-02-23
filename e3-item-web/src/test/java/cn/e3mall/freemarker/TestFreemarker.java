package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.e3mall.pojo.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
  * @author bandong
  * @version 创建时间：2019年12月23日 下午1:26:58
  * 测试freemarker做静态页面
  */
public class TestFreemarker {
	@Test
	public void testFreemarker() throws Exception{
		//1.创建模板文件,一般创建在web-inf下，因为看着清晰
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3.设置模板对象保存的目录
		configuration.setDirectoryForTemplateLoading(new File("E:/WorkSpace/EclipsePractice/EclipseWorkSpace-2/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//4.模板文件的编码格式，一般都是utf-8
		configuration.setDefaultEncoding("utf-8");
		//5.使用configuration加载一个模板文件来创建一个模板对象
		//Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		
		Student student = new Student(1,"张三","男",18);
		//6.创建一个数据集。可以是pojo也可以是map。推荐使用map
		Map map = new HashMap<>();
		map.put("hello", "Hello-Freemarker");
		map.put("student", student);
		
		//创建一个学生list集合
		List<Student>stuList= new ArrayList<Student>();
		stuList.add(new Student(1,"天明1","男",18));
		stuList.add(new Student(2,"天明2","男",18));
		stuList.add(new Student(3,"天明3","男",18));
		stuList.add(new Student(4,"天明4","男",18));
		stuList.add(new Student(5,"天明5","男",18));
		stuList.add(new Student(6,"天明6","男",18));
		stuList.add(new Student(7,"天明7","男",18));
		stuList.add(new Student(8,"天明8","男",18));
		stuList.add(new Student(9,"天明9","男",18));
		stuList.add(new Student(10,"天明10","男",18));
		stuList.add(new Student(11,"天明11","男",18));
		map.put("stuList", stuList);
		//指定一个时间
		map.put("date", new Date());
		//空值的处理
		map.put("val", "123");
		//7.创建一个Witer对象指定输出文件路径及文件名
		FileWriter writer = new FileWriter("D:/JavaEE/Freemarker/student.html");
		//8.生成静态页面
		template.process(map, writer);
		//9.关闭流
		writer.close();
	}
}
