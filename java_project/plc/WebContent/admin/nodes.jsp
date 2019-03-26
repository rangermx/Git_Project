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
					<th>节点地址</th>
					<th>节点名称</th>
					<th>主灯状态</th>
					<th>主灯亮度</th>
					<th>辅灯状态</th>
					<th>辅灯亮度</th>
					<th>节点功率</th>
					<th>单灯控制</th>
					<th>更新数据</th>
				</tr>
			</thead>
			<tbody id="table_body">
				<%-- <c:forEach items="${nodes }" var="node">
					<tr>
						<td>${node.nodeAddr }</td>
						<td>${node.nodeName }</td>
						<td>${node.light1State == true ? "开" : "关" }</td>
						<td>${node.light1PowerPercent }</td>
						<td>${node.light2State == true ? "开" : "关" }</td>
						<td>${node.light2PowerPercent }</td>
						<td>${node.power }W</td>
						<td><a href="javascript:;" onclick="nodeControl('${pageContext.request.contextPath }/nodeFormServlet', ${node.id }, ${node.light1State }, ${node.light1PowerPercent }, ${node.light2State }, ${node.light2PowerPercent })">单灯控制</a></td>
						<td><a href="javascript:;" onclick="nodeRefresh('${pageContext.request.contextPath }/nodeRefreshServlet?nodeid=${node.id }')">刷新</a></td>
					</tr>
				</c:forEach> --%>
			</tbody>
		</table>
	</form>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/admin/js/nodes.js"></script>
	<script type="text/javascript">
		/**
		 * 获取XMLHttpRequest对象
		 */
		function getXMLHttpRequest() {
			var xmlhttp;
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			return xmlhttp;
		}
		/**
		 * AJAX请求
		 * 
		 * @returns
		 */
		function AJAXRequest() {
			var req = getXMLHttpRequest();
			req.onreadystatechange = function() {
				if (req.readyState == 4) {// 请求成功
					if (req.status == 200) {// 服务器响应成功
						var nodes = JSON.parse(req.responseText);
						var inner = "";
						var node;
						for (var i = 0; i < nodes.length; i++) {
							node = nodes[i];
							inner = inner + "<tr><td>" + node.nodeAddr + "</td>\
								<td>" + node.nodeName + "</td>\
								<td>";
								if (node.light1State == true) {
									inner = inner + "开";
								} else {
									inner = inner + "关";
								}
								inner = inner + "</td>\
								<td>" + node.light1PowerPercent + "</td>\
								<td>";
								if (node.light2State == true) {
									inner = inner + "开";
								} else {
									inner = inner + "关";
								}
								inner = inner + "</td>\
								<td>" + node.light2PowerPercent + "</td>\
								<td>" + node.power + " W</td>\
								<td><a href='javascript:;' onclick=\"nodeControl('/plc/nodeFormServlet', " + node.id +"," + node.light1State + "," + node.light1PowerPercent + "," + node.light2State + "," + node.light2PowerPercent + ")\">单灯控制</a></td>\
								<td><a href='javascript:;' onclick=\"nodeRefresh('/plc/nodeRefreshServlet?nodeid=" + node.id + "')\">刷新</a></td>\
							</tr>";
						}
						document.getElementById("table_body").innerHTML = inner;
					}
				}
			}
			req.open("get","${pageContext.request.contextPath }/nodeTestServlet?deviceid=${deviceid}");// 建立一个链接
			req.send(null);// 发送请求
		}

		window.onload = function() {
			AJAXRequest();
		}
		window.setInterval(AJAXRequest, 3000); 
	</script>
</body>
</html>