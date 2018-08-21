layui.use(['element','form', 'layedit', 'laydate', 'layer'], function () {
    var element = layui.element;
    var form = layui.form;
    var layer = layui.layer;
    var layedit = layui.layedit;
    var laydate = layui.laydate;

    form.on('submit(sendcmd)', function (data) {
        // layer.alert(JSON.stringify(data.field), {
        //     title: '最终的提交信息'
        // });
        form.verify({//自定义验证信息
            // title: function (value) {
            //     if (value.length < 5) {
            //         return '标题至少得5个字符啊';
            //     }
            // }
            // , pass: [/(.+){6,12}$/, '密码必须6到12位']
            // , content: function (value) {
            //     layedit.sync(editIndex);
            // }
        });
        $('#receiveTA').val(function(i, origText){
            return origText + new Date().toLocaleTimeString() + "--" + "send: " + data.field.sendTA + "\n";
        });
        // $('.zigbee-btn-on').val(function(zigbee) {
        //     alert(JSON.stringify(zigbee));
        // });
        return false;
    });
});