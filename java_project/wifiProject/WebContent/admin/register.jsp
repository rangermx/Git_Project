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
<title>测试界面</title>
<style>
.main-form-div {
	margin-top: 100px;
}

.main-form-body {
	padding-top: 40px;
	padding-left: 10%;
	padding-right: 10%;
	padding-bottom: 40px;
}
</style>
</head>
<body class="layui-layout-body">
	<div class="layui-row main-form-div">
		<div class="layui-col-xs1 layui-col-sm3 layui-col-md4">
			<div class="grid-demo layui-bg-red">移动：1/12 | 平板：3/12 | 桌面：4/12</div>
		</div>
		<div class="layui-col-xs10 layui-col-sm6 layui-col-md4">
			<div class="grid-demo layui-bg-gray">
				<div class="main-form-body">
					<form class="layui-form"
						action="${pageContext.request.contextPath }/registerFormServlet"
						method="post">
						<div class="layui-form-item">
							<label class="layui-form-label">用户名</label>
							<div class="layui-input-inline">
								<input type="text" name="username" required
									lay-verify="required" placeholder="请输入用户名" autocomplete="off"
									class="layui-input">
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">密码</label>
							<div class="layui-input-inline">
								<input type="password" name="password" required
									lay-verify="required" placeholder="请输入密码" autocomplete="off"
									class="layui-input">
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">确认密码</label>
							<div class="layui-input-inline">
								<input type="password" name="passwordrep" required
									lay-verify="required" placeholder="请输入密码" autocomplete="off"
									class="layui-input">
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">邮箱</label>
							<div class="layui-input-inline">
								<input type="text" name="email" required lay-verify="required"
									placeholder="请输入邮箱地址" autocomplete="off" class="layui-input">
							</div>
						</div>
						<div class="layui-form-item">
							<div class="layui-input-block">
								<button class="layui-btn layui-btn-primary" lay-submit
									lay-filter="registerFilter">注册</button>
								<!-- <button class="layui-btn layui-btn-primary" lay-submit
									lay-filter="loginFilter">返回登录</button> -->
								<a href="${pageContext.request.contextPath }/loginServlet" class="layui-btn">返回登录</a>
								<!-- <button type="reset" class="layui-btn layui-btn-primary">重置</button> -->
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="layui-col-xs1 layui-col-sm3 layui-col-md4">
			<div class="grid-demo layui-bg-blue">移动：1/12 | 平板：3/12 |
				桌面：4/12</div>
		</div>
	</div>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/admin/js/myLayui.js"></script>
	<script>
		//Demo
		layui.use('form', function() {
			var form = layui.form;

			//监听提交
			/* form.on('submit(registerFilter)', function(data) {
				
			}); */

			/* form.on('submit(loginFilter)', function(data) {
				layer.msg(JSON.stringify(data.field));
				return false;
			}); */
		});
	</script>
</body>
</html>