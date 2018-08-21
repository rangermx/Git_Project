function Dev(devName, macAddr, devNet) {//集控器
    this.name = devName;
    this.mac = macAddr;
    if (devNet == 1) {
        this.online = true;
    } else {
        this.online = false;
    }
}

function Zigbee(zigbeeName, mac, devMac, zigbeeBright, zigbeeNet, zigbeeStatus,
    version, type, temperature, humidity, minPower) {//zigbee节点
    this.name = zigbeeName;
    if (zigbeeNet == 1) {
        this.online = true;
    } else {
        this.online = false;
    }
    this.brightness = zigbeeBright;
    if (zigbeeStatus == 1) {
        this.workStatus = true;
    } else {
        this.workStatus = false;
    }
    this.mac = mac;
    this.devMac = devMac;
    this.groupAddrs = new Array();
    this.version = version;
    this.type = type;
    this.temperature = temperature;
    this.humidity = humidity;
    this.minPower = minPower;
}
function Group(groupName, groupAddr) {//控制组
    this.name = groupName;
    this.addr = groupAddr;
    this.zigbeeOnlineSet = new Array();
    this.zigbeeOfflineSet = new Array();
}
function PloyOperate(id, hours, minutes, operateType, operateParam, ployid) {//策略项
    this.id = id;
    this.hours = hours;
    this.minutes = minutes;
    this.operateType = operateType;//操作类型(1,开关、2,调光)
    this.operateParam = operateParam;//参数(1开，0关)
    this.ployid = ployid;
}
function Ploy(ployName, ployId, status, userid, bindType, bindParam, timeZone) {//控制策略
    this.name = ployName;//策略名
    this.id = ployId;//策略id
    this.status = status;//是否正在运行
    this.userid = userid;//用户id
    this.bindType = bindType;//绑定类型(1组、2集控器、3节点)
    this.bindParam = bindParam;//绑定参数(组id、集控器mac地址、节点mac地址)
    this.timeZone = timeZone;//时区(时间差值，单位分钟)
    this.currentOperate = new PloyOperate(0, 0, 0, 1, 0);
    this.operateArray = new Array();
    this.operateBackUp;
    this.editStarted = false;
}
function User() {//用户数据
    this.username = String();
    this.password = String();
    this.email = String();
    // this.id = String();
    this.devSet = new Array();
    this.groupSet = new Array();
    this.zigbeeSet = new Array();
    this.ployArray = new Array();
}
/*********************对象构造方法*********************/

//数据处理方法
function userDataRefresh(user, jsonObj) {
    var count;
    user.id = jsonObj.user.id;
    user.username = jsonObj.user.username;
    user.password = jsonObj.user.password;
    user.email = jsonObj.user.email;
    user.phone = jsonObj.user.phone;

    user.devSet.splice(0, user.devSet.length);
    user.groupSet.splice(0, user.groupSet.length);
    user.zigbeeSet.splice(0, user.zigbeeSet.length);
    // user.ployArray.splice(0,user.ployArray.length);

    for (var i in jsonObj.devArr) {
        user.devSet.push(new Dev(jsonObj.devArr[i].devName, jsonObj.devArr[i].devMac, jsonObj.devArr[i].devNet));
        for (var index in jsonObj.devAttrArr) {
            if (jsonObj.devAttrArr[index].devMac == jsonObj.devArr[i].devMac) {
                count = user.devSet.length - 1;
                user.devSet[count].zigbeeFinder = jsonObj.devAttrArr[index].zigbeeFinder == null || undefined ? 0 : jsonObj.devAttrArr[index].zigbeeFinder;
            }
        }
    }

    for (var i in jsonObj.groupArr) {
        user.groupSet.push(new Group(jsonObj.groupArr[i].groupName, jsonObj.groupArr[i].groupid));
    }

    for (var i in jsonObj.zigbeeArr) {
        user.zigbeeSet.push(new Zigbee(
            jsonObj.zigbeeArr[i].zigbeeName,
            jsonObj.zigbeeArr[i].zigbeeMac,
            jsonObj.zigbeeArr[i].devMac,
            jsonObj.zigbeeArr[i].zigbeeBright,
            jsonObj.zigbeeArr[i].zigbeeNet,
            jsonObj.zigbeeArr[i].zigbeeStatus
        ));
        for (var index in jsonObj.zigbeeAttrArr) {
            if (jsonObj.zigbeeAttrArr[index].zigbeeMac == jsonObj.zigbeeArr[i].zigbeeMac) {
                count = user.zigbeeSet.length - 1;
                user.zigbeeSet[count].temperature = jsonObj.zigbeeAttrArr[index].temperature / 100;
                user.zigbeeSet[count].humidity = jsonObj.zigbeeAttrArr[index].humidity / 100;
            }
        }
    }

    for (var i in jsonObj.groupPairArr) {
        for (var j in user.zigbeeSet) {
            if (user.zigbeeSet[j].mac == jsonObj.groupPairArr[i].zigbeeMac) {
                user.zigbeeSet[j].groupAddrs.push(jsonObj.groupPairArr[i].groupid);
            }
        }
    }

}

function getLocalTime(hours, minutes, offset) {
    var hoursOffset = offset / 60;
    var minutesOffset = offset % 60;
    var localhours = hours - hoursOffset;
    var localminutes = minutes - minutesOffset;
    if (localminutes < 0) {
        localminutes += 60;
        localhours -= 1;
    } else if (localminutes >= 60) {
        localminutes -= 60;
        localhours += 1;
    }
    localhours = localhours % 24;
    if (localhours < 0) {
        localhours += 24;
    }
    return {
        "localhours": localhours,
        "localminutes": localminutes
    };
}

function getGMTTime(localhours, localminutes, offset) {
    var hoursOffset = offset / 60;
    var minutesOffset = offset % 60;
    var gmthours = parseInt(localhours, 10) + hoursOffset;
    var gmtminutes = parseInt(localminutes, 10) + minutesOffset;
    if (gmtminutes < 0) {
        gmtminutes += 60;
        gmthours -= 1;
    } else if (gmtminutes >= 60) {
        gmtminutes -= 60;
        gmthours += 1;
    }
    gmthours = gmthours % 24;
    if (gmthours < 0) {
        gmthours += 24;
    }
    return {
        "gmthours": gmthours,
        "gmtminutes": gmtminutes
    };
}

function ployDataRefresh(user, jsonObj) {
    user.ployArray.splice(0, user.ployArray.length);
    var timeZoneOffset = new Date().getTimezoneOffset();
    var currentHours = new Date().getHours();
    var currentMinutes = new Date().getMinutes();
    var localtime;
    for (var i in jsonObj.ployArr) {
        user.ployArray.push(new Ploy(
            jsonObj.ployArr[i].ployName,
            jsonObj.ployArr[i].id,
            jsonObj.ployArr[i].status,
            jsonObj.ployArr[i].userid,
            jsonObj.ployArr[i].bindType,
            jsonObj.ployArr[i].bindData,
            jsonObj.ployArr[i].timeZone));
        for (var j in jsonObj.ployOperateArr) {
            if (jsonObj.ployOperateArr[j].ployid == user.ployArray[i].id) {
                localtime = getLocalTime(jsonObj.ployOperateArr[j].hours, jsonObj.ployOperateArr[j].minutes, timeZoneOffset);
                user.ployArray[i].operateArray.push(new PloyOperate(
                    jsonObj.ployOperateArr[j].id,
                    localtime.localhours,
                    localtime.localminutes,
                    jsonObj.ployOperateArr[j].operateType,
                    jsonObj.ployOperateArr[j].operateParam,
                    jsonObj.ployOperateArr[j].ployid));
            }
        }
    }
    for (var i = 0; i < user.ployArray.length; i++) {
        user.ployArray[i].operateArray.sort(function (a, b) {
            if (a.hours - b.hours != 0) {
                return a.hours - b.hours;
            } else {
                return a.minutes - b.minutes;
            }
        });
    }

    for (var i = 0; i < user.ployArray.length; i++) {
        if (user.ployArray[i].operateArray.length == 0) {

        } else if (user.ployArray[i].operateArray.length == 1) {
            user.ployArray[i].currentOperate = user.ployArray[i].operateArray[0];
        } else {
            if (user.ployArray[i].operateArray[0].hours >= currentHours &&
                user.ployArray[i].operateArray[0].minutes >= currentMinutes) {
                user.ployArray[i].currentOperate = user.ployArray[i].operateArray[0];
            } else if (user.ployArray[i].operateArray[user.ployArray[i].operateArray.length - 1].hours <= currentHours &&
                user.ployArray[i].operateArray[user.ployArray[i].operateArray.length - 1].minutes <= currentMinutes) {
                user.ployArray[i].currentOperate = user.ployArray[i].operateArray[0];
            } else {
                for (var j = 0; j < user.ployArray[i].operateArray.length - 1; j++) {
                    if (parseInt(user.ployArray[i].operateArray[j].hours, 10) * 60 + parseInt(user.ployArray[i].operateArray[j].minutes, 10) < parseInt(currentHours, 10) * 60 + parseInt(currentMinutes, 10) &&
                        parseInt(currentHours, 10) * 60 + parseInt(currentMinutes, 10) <= parseInt(user.ployArray[i].operateArray[j + 1].hours, 10) * 60 + parseInt(user.ployArray[i].operateArray[j + 1].minutes, 10)) {
                        user.ployArray[i].currentOperate = user.ployArray[i].operateArray[j + 1];
                        break;
                    }
                }
            }
        }
    }
}
