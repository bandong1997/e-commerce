<html>
	<head>
		<title>学生信息</title>
	<head>
	<body>
		取学生信息<br/>
		<laber>学号=</laber>${student.id}<br/>
		<laber>姓名=</laber>${student.name}<br/>
		<laber>性别=</laber>${student.sex}<br/>
		<laber>年龄=</laber>${student.age}<br/>
		<br/>
		<br/>
		<br/>
		学生列表
		<table  border="1">
		<tr>
			<td align="center">序号</td>
			<td align="center">编号</td>
			<td align="center">姓名</td>
			<td align="center">性别</td>
			<td align="center">年龄</td>
		</tr>
		<#list stuList as stu >
		<#if stu_index % 2 ==0>
			<tr bgcolor="red">
			<#else>
			<tr bgcolor="blue">
		</#if>
			<td align="center">${stu_index}</td>
			<td align="center">${stu.id}</td>
			<td align="center">${stu.name}</td>
			<td align="center">${stu.sex}</td>
			<td align="center">${stu.age}</td>
		</tr>
		</#list >
	</table>
	<br/>
	<!--不同日期格式：可以使用?date，?time，?datetime 和?string(parent)-->
	当前时间1:${date?date}<br/>
	当前时间2:${date?string("yyyy/MM/dd HH:mm:ss")}<br/>
	<!--null值得处理-->
	null的处理L:${val!"val的值为空..."}<br/>
	<br/>
	判断val的值是否为null:
	<#if val??>
		val有内容${val}
		<#else>
		val为null没有内容，
	</#if>
	<br/>
	引用其他模板...<br/>
	<#include "hello.ftl">
	</body>
</html>