/**
 * 单灯控制方法
 */
function nodeControl(url, nodeid) {
	layui.use('layer', function() {
		var layer = layui.layer;
		layer.open({
			area: ['auto', '350px'],
			btnAlign: 'c',
			resize: false,
			content : url+"?nodeid="+nodeid,
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
