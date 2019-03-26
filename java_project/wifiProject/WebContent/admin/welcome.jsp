<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- <meta http-equiv="refresh" content="5"> -->
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
					<th>节点mac地址</th>
					<th>节点名称</th>
					<th>网络状态</th>
					<th>当前输出百分比</th>
					<th>当前开关状态</th>
					<th>灯光控制</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${result.nodes }" var="node">
					<tr>
						<td>${node.mac }</td>
						<td><a href="javascript:;"
							onclick="nodeRename('${pageContext.request.contextPath }/nodeRenameFromServlet', ${node.id})">${node.nodeName }</a></td>
						<td>${node.online == true ? "online" : "offline" }</td>
						<td>${node.precentage }</td>
						<td>${node.switchState }</td>
						<td><a href="javascript:;"
							onclick="nodeControl('${pageContext.request.contextPath }/nodeFormServlet', ${node.id })">调光</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/admin/js/welcome.js"></script>
</body>
</html>