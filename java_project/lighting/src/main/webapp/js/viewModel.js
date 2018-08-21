var loadVM = function() {
    // get username and password from cookie.
    // get the data of user by ajax post function.
    var jsonObj = {
        "devArr": [{
            "devMac": "02AB1205004B1200",
            "devName": "02AB1205004B1200",
            "devNet": 0,
            "gprsPhone": "13116708817",
            "userid": 100000
        }],
        "devAttrArr": [{
            "devMac": "02AB1205004B1200",
            "zigbeeFinder": 0
        }],
        "groupArr": [{
            "groupName": "group1",
            "groupid": 1003,
            "userid": 100000
        }, {
            "groupName": "group2",
            "groupid": 1004,
            "userid": 100000
        }],
        "groupPairArr": [{
            "groupid": 1003,
            "id": 1,
            "userid": 100000,
            "zigbeeMac": "240FCE0A004B1200"
        }],
        "ployArr": [{
            "bindData": "1003",
            "bindType": 1,
            "id": 2,
            "ployName": "策略A",
            "status": 0,
            "timeZone": -540,
            "userid": 100000
        }, {
            "bindData": "1004",
            "bindType": 1,
            "id": 3,
            "ployName": "celueb",
            "status": 0,
            "timeZone": -480,
            "userid": 100000
        }],
        "ployOperateArr": [{
            "hours": 23,
            "id": 1,
            "minutes": 0,
            "operateParam": 70,
            "operateType": 2,
            "ployid": 2
        }, {
            "hours": 0,
            "id": 2,
            "minutes": 0,
            "operateParam": 0,
            "operateType": 1,
            "ployid": 2
        }, {
            "hours": 12,
            "id": 3,
            "minutes": 0,
            "operateParam": 1,
            "operateType": 1,
            "ployid": 2
        }, {
            "hours": 13,
            "id": 4,
            "minutes": 0,
            "operateParam": 100,
            "operateType": 2,
            "ployid": 2
        }],
        "user": {
            "email": "admin@xx.com",
            "id": 100000,
            "password": "admin",
            "phone": "xxx-xxxx-xxxx",
            "username": "admin"
        },
        "zigbeeArr": [{
            "devMac": "02AB1205004B1200",
            "zigbeeBright": 100,
            "zigbeeMac": "240FCE0A004B1200",
            "zigbeeName": "240FCE0A004B1200",
            "zigbeeNet": 0,
            "zigbeeSaddr": "CF1D",
            "zigbeeStatus": 1
        }, {
            "devMac": "02AB1205004B1200",
            "zigbeeBright": 100,
            "zigbeeMac": "4817CE0A004B1200",
            "zigbeeName": "4817CE0A004B1200",
            "zigbeeNet": 0,
            "zigbeeSaddr": "0AE4",
            "zigbeeStatus": 1
        }],
        "zigbeeAttrArr": [{
            "humidity": 75,
            "minPower": 50,
            "power": 1000,
            "temperature": 25,
            "type": 1,
            "version": "1.0",
            "zigbeeMac": "240FCE0A004B1200"
        }, {
            "humidity": 6000,
            "power": 1000,
            "temperature": 3652,
            "type": 1,
            "version": "01",
            "zigbeeMac": "4817CE0A004B1200"
        }]
    };
    var user = new User();
    userDataRefresh(user, jsonObj);// refresh all data except ploy page.
    ployDataRefresh(user, jsonObj);// refresh ploy page data.
    alert(JSON.stringify(user));
    var btnRange = new Array();
    for (var i = 100; i > 0; i--) {
        btnRange.push(i);
    }
    var devTabVue = new Vue({
        el: '#devTab',
        data: {
            devSet: user.devSet,
            zigbeeSet: user.zigbeeSet,
            btnRange: btnRange,
        },
        methods: {
            showZigbeeDetail: function (zigbee) {
                alert(JSON.stringify(zigbee));
            },
        },
    });
}

export {loadVM}