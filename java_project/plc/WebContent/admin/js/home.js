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
	req.open("get", "${pageContext.request.contextPath }/servlet");//建立一个链接
	req.send(null);//发送请求
}
/**
 * 左侧导航栏点击集控器名称
 * @param id 集控器id
 * @returns
 */
function deviceOnclick(id) {
	// 根据设备id获取节点信息
	// 根据节点信息更新表格数据
	alert(id);
}