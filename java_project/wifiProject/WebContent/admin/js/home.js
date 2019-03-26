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
 * AJAX测试
 * 
 * @returns
 */
function testAJAX() {
	var req = getXMLHttpRequest();
	req.onreadystatechange = function() {
		if (req.readyState == 4) {// 请求成功
			if (req.status == 200) {// 服务器响应成功
				alert(req.responseText);
			}
		}
	}
	req.open("get", "${pageContext.request.contextPath }/servlet");// 建立一个链接
	req.send(null);// 发送请求
}

function removeNode(url, userid) {
	layui.use('layer', function() {
		var layer = layui.layer;
		layer.open({
			area : [ 'auto', '400px' ],
			btnAlign : 'c',
			resize : false,
			content : url + "?userid=" + userid,
			closeBtn : 1,
			type : 2,
			cancel : function() {
				// 右上角关闭回调
				location.reload();
				// return false 开启该代码可禁止点击该按钮关闭
			}
		});
	});
}

function broadcastControl(url, userid) {
	layui.use('layer', function() {
		var layer = layui.layer;
		layer.open({
			area : [ 'auto', '400px' ],
			btnAlign : 'c',
			resize : false,
			content : url + "?userid=" + userid,
			closeBtn : 1,
			type : 2,
			cancel : function() {
				// 右上角关闭回调
				location.reload();
				// return false 开启该代码可禁止点击该按钮关闭
			}
		});
	});
}

function addNode(url, userid) {
	layui.use('layer', function() {
		var layer = layui.layer;
		layer.open({
			area : [ 'auto', '200px' ],
			btnAlign : 'c',
			resize : false,
			content : url + "?userid=" + userid,
			closeBtn : 1,
			type : 2,
			cancel : function() {
				// 右上角关闭回调
				location.reload();
				// return false 开启该代码可禁止点击该按钮关闭
			}
		});
	});
}

// /**
// * 左侧导航栏点击集控器名称
// *
// * @param id
// * 集控器id
// * @returns
// */
// function deviceOnclick(url, id) {
// document.getElementById('body-div').innerHTML = "<iframe style='min-height:
// 500px' name='fname' frameborder='0' scrolling='yes' width='100%'
// src='"+url+"?deviceid="+id+"' class='body-frame'></iframe>";
// }

