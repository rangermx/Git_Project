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
	src="${pageContext.request.contextPath }/admin/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/admin/js/home.js"></script>
<title>测试界面</title>
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">雷培德WiFi灯控系统</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-left">
				<li class="layui-nav-item"><a href="javascript:void(0)"
					onclick="location.reload()">首页</a></li>
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
					<li class="layui-nav-item"><a href="javascript:;" onclick="addNode('${pageContext.request.contextPath}/addNodeFormServlet', ${result.user.id })">添加节点</a></li>
					<li class="layui-nav-item"><a href="javascript:;" onclick="removeNode('${pageContext.request.contextPath}/removeNodeFormServlet', ${result.user.id })">删除节点</a></li>
					<li class="layui-nav-item"><a href="javascript:;" onclick="broadcastControl('${pageContext.request.contextPath}/broadcastControlFormServlet', ${result.user.id })">广播控制</a></li>
					<li class="layui-nav-item"><a href="javascript:;" onclick="removeNode('${pageContext.request.contextPath}/wifiResetFormServlet', ${result.user.id })">更改WiFi网络</a></li>
				</ul>
			</div>
		</div>

		<div class="layui-body">
			<!-- 内容主体区域 -->
			<div style="padding: 15px;" id="body-div">
				<iframe style="min-height: 500px" name="fname" frameborder="0"
					scrolling="no" width="100%"
					src="${pageContext.request.contextPath }/welcomeServlet?userid=${result.user.id }"
					class="body-frame"></iframe>
			</div>
		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			© 雷培德WiFi灯控系统
		</div>
	</div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/admin/js/myLayui.js"></script>
</body>
</html>