<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" import="java.util.*,com.test.domain.*"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>浙江雷培德zigbee灯光智能控制系统</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/jsp/bootstrap/css/bootstrap.css">
	<link rel="shortcut icon" type="image/x-icon" href="../jsp/favicon.ico" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/bootstrap/js/bootstrap.js"></script>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/Vue.js"></script>
	<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/jsp/login.js"></script> --%>
	<style>
		html{
			background: url('<%=request.getContextPath()%>/jsp/loginbg.jpg') no-repeat center center fixed;
			-webkit-background-size: cover;
			-moz-background-size: cover;
			-o-background-size: cover;
			background-size: cover;
		}
	</style>
</head>
<body>
	
	<div style="padding: 15em" class="col-sm-12" id="mainDiv">
		<form class="form-horizontal" role="form" name="mainform" action="<%=request.getContextPath()%>/user/login.do" method="post">
			<div class="form-group">
				<label class="col-sm-offset-4 col-sm-4" style="color:red" v-if="flags.error != 'null'">{{flags.error}}</label>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label">用户名</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" placeholder="username" required="required" name="username" onblur="test()" onkeyup="test()">
					<span style="color:red; display:none" id="usernameSpan">请输入用户名</span>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label">密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" placeholder="password" required="required" name="password" onblur="test()" onkeyup="test()">
					<span style="color:red; display:none" id="passwordSpan">请输入密码</span>
				</div>
			</div>
			<div class="form-group" v-if="!flags.hidden">
				<label class="col-sm-4 control-label">确认密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" placeholder="password" required="required" name="passwordRepeat" onblur="test()" onkeyup="test()">
					<span style="color:red; display:none" id="passwordRepeatSpan">两次输入密码不一致</span>
				</div>
			</div>
			<div class="form-group" v-if="!flags.hidden">
				<label class="col-sm-4 control-label">邮箱</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" placeholder="email" required="required" name="email" onblur="test()" onkeyup="test()">
				</div>
				<span style="color:red; display:none" id="emailSpan">邮箱格式不正确</span>
			</div>
			<div class="form-group" v-if="!flags.hidden">
				<label class="col-sm-4 control-label">手机号码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" placeholder="phone" required="required" name="phone">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-4 col-sm-2">
					<button type="button" class="btn btn-default" @click="regist()">注册</button>
				</div>
				<div class="col-sm-offset-0 col-sm-2">
					<button type="submit" class="btn btn-default" v-if="flags.hidden">登录</button>
					<button type="button" class="btn btn-default" @click="login()" v-if="!flags.hidden">登录</button>
				</div>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
	var localhost = "<%=request.getContextPath()%>";
	var emailReg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+/;//判断邮箱地址合法正则
	var flags = {};
	flags.hidden = true;
	flags.password = mainform.password.value;
	flags.passwordRepeat = mainform.passwordRepeat.value;

	flags.error = null;

	var jsonStr = '<%=request.getAttribute("responseJson")%>';
	// alert(jsonStr);
	if (jsonStr != 'null') {
		// alert(jsonStr);
		var responseJson = JSON.parse(jsonStr);
		// alert(responseJson.error);
		flags.error = responseJson.error;
	}
	
	var mainDivVue = new Vue({
		el: '#mainDiv',
		data: {
			flags: flags,
		},
		methods: {
			regist: function(event) {
				// alert(mainform.username.value + "\n" + mainform.password.value);
				if (flags.hidden == true) {
					flags.hidden = false;
				} else if (mainform.password.value != mainform.passwordRepeat.value) {
					// alert("两次输入密码不一致");
				} else if (mainform.password.value == "" || mainform.password.value == null) {
					// alert("密码不能为空");
				} else if (mainform.username.value == "" || mainform.username.value == null) {
					// alert("请输入用户名");
				} else if (!emailReg.test(mainform.email.value)) {

				} else {
					$.ajax({
						type:"post",
						url:localhost + "/user/regist.do",
						data:{
							username:mainform.username.value,
							password:mainform.password.value,
							email:mainform.email.value,
							phone:mainform.phone.value
						},
						async : true,
						datatype: "json",
						success:function(datasource, textStatus, jqXHR) {
							var data = eval('(' + datasource + ')');
							if (data.error == null || data.error == "" || data.error == undefined) {//未出错
								alert("注册成功!");
								flags.hidden = true;
								mainform.password.value = "";
								mainform.passwordRepeat.value = "";
								mainform.email.value = "";
								mainform.phone.value = "";
							} else {
								alert(data.error);
							}
						},
						error: function() {
							alert("出错了");
						}
					});
				}
			},
			login: function(event) {
				if (flags.hidden == false) {
					flags.hidden = true;
				}
			}
		}
	});
	function test() {
		if (mainform.password.value == mainform.passwordRepeat.value) {
			passwordRepeatSpan.setAttribute('style', 'display: none');
		} else {
			passwordRepeatSpan.setAttribute('style', 'display: inline; color: red');
		}
		if (mainform.password.value == "" || mainform.password.value == null) {
			passwordSpan.setAttribute('style', 'display: inline; color: red');
		} else {
			passwordSpan.setAttribute('style', 'display: none');
		}
		if (mainform.username.value == "" || mainform.username.value == null) {
			usernameSpan.setAttribute('style', 'display: inline; color: red');
		} else {
			usernameSpan.setAttribute('style', 'display: none');
		}
		if (!emailReg.test(mainform.email.value)) {
			emailSpan.setAttribute('style', 'display: inline; color: red');
		} else {
			emailSpan.setAttribute('style', 'display: none');
		}
	}
</script>
</html>