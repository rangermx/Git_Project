/*********************对象构造方法*********************/
function Dev(devName, macAddr) {//集控器
	this.name = devName;
	this.mac = macAddr;
	this.online = false;
}

function Zigbee(zigbeeName, mac, devMac) {//zigbee节点
	this.name = zigbeeName;
	this.online = true;
	this.brightness = 80;
	this.workStatus = true;
	this.mac = mac;
	this.devMac = devMac;
	this.groupAddrs = new Array();
	this.groupAddrs[0] = "1";
}
function Group(groupName, groupAddr) {//控制组
	this.name = groupName;
	this.addr = groupAddr;
	this.zigbeeOnlineSet = new Array();
	this.zigbeeOfflineSet = new Array();
}
function PloyOperate(hours, minutes, brightness, lightSwitch) {//策略项
	this.hours = hours;
	this.minutes = minutes;
	this.brightness = brightness;//亮度
	this.lightSwitch = lightSwitch;//开关
}
function Ploy(ployName, ployId) {//控制策略
	this.name = ployName;
	this.id = ployId;
	this.started = false;
	this.currentOperate = new PloyOperate(0, 0, 0, false);;
	this.operateArray = new Array();
	this.groupAddrs = new Array();
}
function User() {//用户数据
	this.username = String();
	this.password = String();
	this.email = String();
	this.devSet = new Array();
	this.groupSet = new Array();
	this.zigbeeSet = new Array();
	this.ployArray = new Array();
	//方法
	//主动请求方法
	this.addDev = function(devMac) {
		//进行post或get请求
		//1,服务器数据库中未发现该设备，请检查设备是否正常链接
		//2,添加成功
	}
}
/*********************对象构造方法*********************/

/*********************vue绑定************************/
var user = new User();
var devDivVue = new Vue({
	el: '#devDiv',
	data: {
		user: user,
		items: user.devSet,
		detail: user.zigbeeSet
	},
	methods: {
		addDev: function(event) {
			var devMac = prompt("请输入集控器Mac地址");
			if (devMac != null && devMac != "") {
				for (var index in this.items) {
					if (this.items[index].mac == devMac) {
						alert("您已添加该设备，设备名为: " + this.items[index].name);
						return;
					}
				}
				this.items.push(new Dev(devMac, devMac));
			}
		},
		removeDev: function(dev) {

		}
	},
	created: function() {
		user.username = "username";
		user.password = "password";
		user.email = "email@email.com";
		
		user.devSet[0] = new Dev("集控器A", "111111");
		user.devSet[1] = new Dev("集控器B", "222222");
		user.devSet[2] = new Dev("集控器C", "333333");
		
		user.devSet[0].online = true;
		user.devSet[1].online = true;
		user.devSet[2].online = true;
		
		user.groupSet[0] = new Group("组A", "1");
		user.groupSet[1] = new Group("组B", "2");
		
		user.zigbeeSet.push(new Zigbee("zigbee1", "111", "111111"));
		user.zigbeeSet.push(new Zigbee("zigbee2", "222", "111111"));
		user.zigbeeSet.push(new Zigbee("zigbee3", "333", "111111"));
		user.zigbeeSet.push(new Zigbee("zigbee4", "444", "222222"));
		user.zigbeeSet.push(new Zigbee("zigbee5", "555", "222222"));
		user.zigbeeSet.push(new Zigbee("zigbee6", "666", "333333"));
		user.zigbeeSet[3].online = false;
		user.zigbeeSet[2].brightness = 50;
		
		user.ployArray[0] = new Ploy("策略A", "1");
		user.ployArray[0].operateArray[0] = new PloyOperate(8, 30, 60, true);
		user.ployArray[0].operateArray[1] = new PloyOperate(10, 0, 80, false);
		user.ployArray[0].groupAddrs[0] = "1";
		user.ployArray[0].currentOperate = user.ployArray[0].operateArray[0];
		user.ployArray[0].started = true;
		
		user.ployArray[1] = new Ploy("策略B", "2");
		// user.ployArray[1].operateArray[0] = new PloyOperate(8, 30, 60, true);
		// user.ployArray[1].operateArray[1] = new PloyOperate(10, 0, 80, false);
		// user.ployArray[1].groupAddrs[0] = "2";
		// user.ployArray[1].currentOperate = user.ployArray[0].operateArray[0];
	}
})
var groupDivVue = new Vue({
	el: '#groupDiv',
	data: {
		items: user.groupSet,
		detail: user.zigbeeSet
	}
})
var ployDivVue = new Vue({
	el: '#ployDiv',
	data: {
		items: user.ployArray,
		detail: user.groupSet
	}
})
var userDivVue = new Vue({
	el: "#userDiv",
	data: {
		item: user
	}
})
/*********************vue绑定************************/
//前端model对象创建根据数据结构进行创建，一对多的对应关系，在一中创建数组，将多添加进去
//后台model对象创建根据数据库表进行创建，一对多对应关心，在多中创建属性与一关联