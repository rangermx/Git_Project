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
<title>Insert title here</title>
</head>
<body>
	<form class="layui-form" action="${pageContext.request.contextPath }/broadcastServlet" method="post">
		<input type="hidden" name="deviceid" value="${deviceid }">
		<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
		<div class="layui-form-item">
			<label class="layui-form-label">主灯开关</label>
			<div class="layui-input-block">
				<input type="checkbox" name="light1State" lay-skin="switch">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">主灯功率</label>
			<div class="layui-input-block" style="width: 50px;">
				<input type="text" name="light1PowerPercent" required
					lay-verify="required" value="${deviceid }" autocomplete="off"
					class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">辅灯开关</label>
			<div class="layui-input-block">
				<input type="checkbox" name="light2State" lay-skin="switch">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">辅灯功率</label>
			<div class="layui-input-block" style="width: 50px;">
				<input type="text" name="light2PowerPercent" required
					lay-verify="required" value="80" autocomplete="off"
					class="layui-input">
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit lay-filter="*">立即提交</button>
			</div>
		</div>
		<!-- 更多表单结构排版请移步文档左侧【页面元素-表单】一项阅览 -->
	</form>
	<script>
		layui.use('form', function() {
			var form = layui.form;

		});
	</script>
</body>
</html>