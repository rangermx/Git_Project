/**
 * 单灯控制方法
 */
function nodeControl(url, nodeid) {
	layui.use('layer', function() {
		var layer = layui.layer;
		layer.open({
			content : nodeid,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				// 按钮【按钮一】的回调
			},
			btn2 : function(index, layero) {
				// 按钮【按钮二】的回调

				// return false 开启该代码可禁止点击该按钮关闭
			},
			cancel : function() {
				// 右上角关闭回调

				// return false 开启该代码可禁止点击该按钮关闭
			}
		});
	});
}
