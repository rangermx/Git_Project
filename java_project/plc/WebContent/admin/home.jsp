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
<script type="text/javascript"
	src="${pageContext.request.contextPath }/admin/js/home.js"></script>
<title>测试界面</title>
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">雷培德PLC灯控系统</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-left">
				<li class="layui-nav-item"><a href="">测试</a></li>
				<li class="layui-nav-item"><a href="javascript:;">测试</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="">测试</a>
						</dd>
						<dd>
							<a href="">测试</a>
						</dd>
						<dd>
							<a href="">测试</a>
						</dd>
					</dl></li>
			</ul>
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item"><a href="javascript:;"> <img
						src="http://t.cn/RCzsdCq" class="layui-nav-img">
						${result.user.username }
				</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="">基本资料</a>
						</dd>
						<dd>
							<a href="">安全设置</a>
						</dd>
					</dl></li>
				<li class="layui-nav-item"><a href="">退出登录</a></li>
			</ul>
		</div>

		<div class="layui-side layui-bg-black">
			<div class="layui-side-scroll">
				<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<ul class="layui-nav layui-nav-tree" lay-filter="test">
					<li class="layui-nav-item layui-nav-itemed"><a class=""
						href="javascript:;">所有集控器</a>
						<dl class="layui-nav-child">
							<c:forEach items="${result.devices }" var="device">
								<dd>
									<a href="javascript:;" onclick="deviceOnclick(${device.id })">${device.deviceName }</a>
								</dd>
							</c:forEach>
						</dl></li>
					<li class="layui-nav-item"><a href="javascript:;">添加集控器</a></li>
				</ul>
			</div>
		</div>

		<div class="layui-body">
			<!-- 内容主体区域 -->
			<div style="padding: 15px;">
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
								<th>节点详情</th>
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
									<td><a href="${pageContext.request.contextPath }/servlet">节点详情</a></td>
									<td><a href="${pageContext.request.contextPath }/servlet">广播控制</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form>
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
			</div>
		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			© 雷培德PLC灯控系统
		</div>
	</div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/admin/js/myLayui.js"></script>
</body>
</html>