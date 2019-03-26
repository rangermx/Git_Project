<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/layui/css/layui.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/layui/layui.js"></script>
<title>Insert title here</title>
</head>
<body>
	<form method="post"
		action="${pageContext.request.contextPath }/servlet">
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
					<th>当前节点数量</th>
					<th>最大节点数量</th>
					<th>节点主动注册</th>
					<th>广播控制</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${result.devices }" var="device">
					<tr>
						<td>${device.deviceMac }</td>
						<td>${device.deviceName }</td>
						<td>${device.online == true ? "online" : "offline" }</td>
						<td>${device.currentNodes }</td>
						<td>${device.maxNodes }</td>
						<td><a href="javascript:;"
							onclick="nodesRegisterControl('${pageContext.request.contextPath }/nodesRegisterFormServlet', ${device.id })">${device.nodesRegister == true ? "ON" : "OFF" }</a></td>
						<td><a href="javascript:;"
							onclick="deviceBroadcastControl('${pageContext.request.contextPath }/broadcastFormServlet', ${device.id })">广播控制</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/admin/js/welcome.js"></script>
</body>
</html>