<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/layui/css/layui.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/layui/layui.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/admin/js/home.js"></script>
<title>Insert title here</title>
</head>
<body>
	<form method="get" action="${pageContext.request.contextPath }/servlet">
		<table class="layui-table">
			<colgroup>
				<col width="150">
				<col>
			</colgroup>
			<thead>
				<tr>
					<th>集控器地址</th>
					<th>集控器名称</th>
					<th>网络状态</th>
					<th>节点数量</th>
					<th>节点详情</th>
					<th>广播控制</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>mac</td>
					<td>集控器1</td>
					<td>online</td>
					<td>1</td>
					<td><a href="${pageContext.request.contextPath }/servlet">节点详情</a></td>
					<td><a href="${pageContext.request.contextPath }/servlet">广播控制</a></td>
				</tr>

			</tbody>
		</table>
	</form>
	<form method="get" action="${pageContext.request.contextPath }/servlet">
		<table class="layui-table">
			<colgroup>
				<col width="150">
				<col>
			</colgroup>
			<thead>
				<tr>
					<th>节点地址</th>
					<th>节点名称</th>
					<th>主灯状态</th>
					<th>主灯亮度</th>
					<th>辅灯状态</th>
					<th>辅灯亮度</th>
					<th>单灯控制</th>
					<th>手动刷新</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>1</td>
					<td>灯1</td>
					<td>开</td>
					<td>80</td>
					<td>关</td>
					<td>80</td>
					<td><a href="${pageContext.request.contextPath }/servlet">单灯控制</a></td>
					<td><a href="${pageContext.request.contextPath }/servlet">刷新</a></td>
				</tr>

			</tbody>
		</table>
	</form>
</body>
</html>