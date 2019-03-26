/**
 * 广播控制节点
 */
function deviceBroadcastControl(url, deviceid) {
	layui.use('layer', function() {
		var layer = layui.layer;
		layer.open({
			area: ['auto', '350px'],
			btnAlign: 'c',
			resize: false,
			content : url+"?deviceid="+deviceid,
			closeBtn: 1,
			type: 2,
			btn : '关闭',
			yes : function(index, layero) {
				// 按钮【按钮一】的回调
				layer.close(index);
			},
			cancel : function() {
				// 右上角关闭回调

				// return false 开启该代码可禁止点击该按钮关闭
			}
		});
	});
}

/**
 * 设备允许节点主动注册控制
 * @param url 请求表单的servlet地址
 * @param deviceid 设备id
 * @returns
 */
function nodesRegisterControl(url, deviceid) {
	layui.use('layer', function() {
		var layer = layui.layer;
		layer.open({
			area: ['550px', '280px'],
			btnAlign: 'c',
			resize: false,
			content : url+"?deviceid="+deviceid,
			closeBtn: 1,
			type: 2,
			btn : '关闭',
			yes : function(index, layero) {
				// 按钮【按钮一】的回调
				layer.close(index);
			},
			cancel : function() {
				// 右上角关闭回调

				// return false 开启该代码可禁止点击该按钮关闭
			}
		});
	});
}