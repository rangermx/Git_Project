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
<script type="text/javascript">
	function writeCurrentTimeToForm() {
		var date = new Date();
		var month = document.getElementsByName("month")[0];
		var day = document.getElementsByName("day")[0];
		var hours = document.getElementsByName("hours")[0];
		var minutes = document.getElementsByName("minutes")[0];
		var keepTime = document.getElementsByName("keepTime")[0];
		month.value = date.getMonth() + 1;
		day.value = date.getDate();
		hours.value = date.getHours();
		minutes.value = date.getMinutes();
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<form class="layui-form"
		action="${pageContext.request.contextPath }/nodesRegisterServlet"
		method="post">
		<input type="hidden" name="deviceid" value="${deviceid }">
		<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label" style="width: 150px;">节点主动注册开启时间</label>
				<div class="layui-input-inline" style="width: 40px;">
					<input type="text" name="month" required
						lay-verify="required|number|month" placeholder="MM" autocomplete="off"
						class="layui-input">
				</div>
				<div class="layui-form-mid">-</div>
				<div class="layui-input-inline" style="width: 40px;">
					<input type="text" name="day" required lay-verify="required|number|date"
						placeholder="dd" autocomplete="off" class="layui-input">
				</div>
				<div class="layui-form-mid">-</div>
				<div class="layui-input-inline" style="width: 40px;">
					<input type="text" name="hours" required
						lay-verify="required|number|hours" placeholder="HH" autocomplete="off"
						class="layui-input">
				</div>
				<div class="layui-form-mid">:</div>
				<div class="layui-input-inline" style="width: 40px;">
					<input type="text" name="minutes" required
						lay-verify="required|number|minutes" placeholder="mm" autocomplete="off"
						class="layui-input">
				</div>
				<div class="layui-input-inline" style="width: 40px;">
					<a href="javascript:;" class="layui-btn"
						onclick="writeCurrentTimeToForm()">当前时间</a>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label" style="width: 150px;">节点主动注册开启时长</label>
				<div class="layui-input-inline" style="width: 50px;">
					<input type="text" name="keepTime" required
						lay-verify="required|number|time" value="1440" autocomplete="off"
						class="layui-input">
				</div>
				<div class="layui-form-mid">分钟（10-1440）</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block" style="margin-left: 225px;">
				<button class="layui-btn" lay-submit lay-filter="*">立即提交</button>
			</div>
		</div>
		<!-- 更多表单结构排版请移步文档左侧【页面元素-表单】一项阅览 -->
	</form>
	<script>
		layui.use('form', function() {
			var form = layui.form;
			var dateMin = 1;
			var dateMax = 31;
			form.verify({
				username : function(value, item) { //value：表单的值、item：表单的DOM对象
					if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$")
							.test(value)) {
						return '用户名不能有特殊字符';
					}
					if (/(^\_)|(\__)|(\_+$)/.test(value)) {
						return '用户名首尾不能出现下划线\'_\'';
					}
					if (/^\d+\d+\d$/.test(value)) {
						return '用户名不能全为数字';
					}
				}

				//我们既支持上述函数式的方式，也支持下述数组的形式
				//数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
				,
				password : [ /^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格' ],
				month : function(value, item) { //value：表单的值、item：表单的DOM对象
					if (value > 12 || value < 1) {
						return '请输入正确的月份';
					}
					if (value == 2) {
						var year = new Date().getFullYear();
						if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
							dateMax = 29;
						} else {
							dateMax = 28;
						}
					}
					if (value == 4 || value == 6 || value == 9 || value == 11) {
						dateMax = 30;
					}
					if (value == 1 || value == 3 || value == 5 || value == 7 || value == 8 || value == 10 || value == 12) {
						dateMax = 31;
					}
				},
				date : function(value, item) { //value：表单的值、item：表单的DOM对象
					if (value > dateMax || value < dateMin) {
						return '请输入正确的日期';
					}
				},
				hours : function(value, item) { //value：表单的值、item：表单的DOM对象
					if (value > 24 || value < 0) {
						return '请输入正确的小时';
					}
				},
				minutes : function(value, item) { //value：表单的值、item：表单的DOM对象
					if (value > 59 || value < 0) {
						return '请输入正确的分钟';
					}
				},
				time : function(value, item) { //value：表单的值、item：表单的DOM对象
					if (value > 1440 || value < 10) {
						return '请输入10 - 1440';
					}
				},
			});
		});
	</script>
</body>
</html>