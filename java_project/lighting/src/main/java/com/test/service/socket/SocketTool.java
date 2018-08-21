package com.test.service.socket;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.dao.DeviceAttrMapper;
import com.test.dao.DeviceMapper;
import com.test.dao.GroupMapper;
import com.test.dao.GroupPairMapper;
import com.test.dao.PloyMapper;
import com.test.dao.PloyOperateMapper;
import com.test.dao.ZigbeeAttrMapper;
import com.test.dao.ZigbeeMapper;
import com.test.domain.Device;
import com.test.domain.DeviceAttr;
import com.test.domain.Group;
import com.test.domain.GroupPair;
import com.test.domain.Ploy;
import com.test.domain.PloyOperate;
import com.test.domain.Zigbee;
import com.test.domain.ZigbeeAttr;
import com.test.service.IDeviceService;

@Component
public class SocketTool {

	static public ArrayList<DeviceSocket> socketList = new ArrayList<>();
	
	static public Boolean HeartBeatReport = true;

	@Autowired
	private IDeviceService devService;
	@Autowired
	private DeviceMapper devDao;
	@Autowired
	private ZigbeeMapper zigbeeDao;
	@Autowired
	private GroupMapper groupDao;
	@Autowired
	private GroupPairMapper groupPairDao;
	@Autowired
	private PloyMapper ployDao;
	@Autowired
	private PloyOperateMapper ployOperateDao;
	@Autowired
	private ZigbeeAttrMapper zigbeeAttrDao;
	@Autowired
	private DeviceAttrMapper devAttrDao;
	
	public static SocketTool testUtils;

	@PostConstruct
	public void init() {
		testUtils = this;
	}

	public static void test(Device record) {
		System.out.println(testUtils.devDao);
	}
	
	public static boolean updateDeviceData(Device dev) {
		return testUtils.devService.updateDeviceData(dev);
	}
	
	public static ArrayList<Zigbee> selectZigbeeByDevMac(String devMac) {
		return testUtils.zigbeeDao.selectBydevMac(devMac);
	}
	
	public static int updateZigbeeByPrimaryKeySelective(Zigbee record) {
		
		record = testUtils.zigbeeBrightnessTransform(record);
		
		return testUtils.zigbeeDao.updateByPrimaryKeySelective(record);
	}
	
	public static int updateZigbeeBydevMacSelectiveWhereOnline(Zigbee record) {
		
//		record = testUtils.zigbeeBrightnessTransform(record);
		ArrayList<Zigbee> list = testUtils.zigbeeDao.selectBydevMac(record.getDevMac());
		if (list != null) {
			int count = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getZigbeeNet() == 0) {//离线
					list.remove(i);//离线节点删除不做处理
				}
			}
			if (record.getZigbeeStatus() != null) {//非空，即设置状态
				for (Zigbee zb : list) {
					zb.setZigbeeStatus(record.getZigbeeStatus());
				}
			}
			if (record.getZigbeeBright() != null) {//非空，即设置亮度
				for (Zigbee zb : list) {
					zb.setZigbeeBright(record.getZigbeeBright());
					zb = testUtils.zigbeeBrightnessTransform(zb);
				}
			}
			for (Zigbee zb : list) {
				if (testUtils.zigbeeDao.updateByPrimaryKeySelective(zb) >= 0) {
					count++;
				}
			}
			return count;
		} else {
			return 0;
		}
		
//		return testUtils.zigbeeDao.updateBydevMacSelectiveWhereOnline(record);
	}
	
	public static Zigbee selectZigbeeBySAddrAndDevMac(String sAddr, String devMac) {
		return testUtils.zigbeeDao.selectBySAddrAndDevMac(sAddr, devMac);
	}
	
	public static Device selectDeviceByPrimaryKey(String devMac) {
		return testUtils.devDao.selectByPrimaryKey(devMac);
	}
	
	public static Zigbee selectZigbeeByPrimaryKey(String zigbeeMac) {
		return testUtils.zigbeeDao.selectByPrimaryKey(zigbeeMac);
	}
	
	public static ArrayList<Zigbee> selectzigbeeByGroup (Group group) {
		
		ArrayList<GroupPair> pairlist = testUtils.groupPairDao.selectByUserid(group.getUserid());
		
		ArrayList<Zigbee> zblist = new ArrayList<>();
		
		Zigbee zb;
		if (pairlist != null) {
			for (GroupPair pair : pairlist) {
				if (pair.getGroupid().equals(group.getGroupid())) {
					zb = testUtils.zigbeeDao.selectByPrimaryKey(pair.getZigbeeMac());
					if (zb != null) {
						zblist.add(zb);
					}
				}
			}
		}
		return zblist;
	}
	
	public static int insertZigbee(Zigbee zb) {
		return testUtils.zigbeeDao.insert(zb);
	}
	
	public static ArrayList<Group> selectGroupByUserid(Integer userid) {
		return testUtils.groupDao.selectByUserid(userid);
	}
	
	public static int insertGroupPair(GroupPair gp) {
		return testUtils.groupPairDao.insertSelective(gp);
	}
	
	public static GroupPair selectGroupPairByUseridAndGroupidAndZigbeeMac(int userid, int groupid, String zigbeeMac) {
		ArrayList<GroupPair> list = testUtils.groupPairDao.selectByUserid(userid);
		for (GroupPair pair : list) {
			if (pair.getGroupid() == groupid && pair.getZigbeeMac().equals(zigbeeMac)) {
				return pair;
			}
		}
		return null;
	}
	
	public static int removeGroupPairByid(int id) {
		return testUtils.groupPairDao.deleteByPrimaryKey(id);
	}
	
	public static ArrayList<Ploy> selectPloyByStatus(int status) {
		return testUtils.ployDao.selectByStatus(status);
	}
	
	public static ArrayList<PloyOperate> selectPloyOperateByPloyid(int ployid) {
		return testUtils.ployOperateDao.selectByPloyid(ployid);
	}
	
	public static ZigbeeAttr selectZigbeeAttrByPrimaryKey(String zigbeeMac) {
		return testUtils.zigbeeAttrDao.selectByPrimaryKey(zigbeeMac);
	}
	
	public static int updateZigbeeAttrByPrimaryKeySelective(ZigbeeAttr record) {
		return testUtils.zigbeeAttrDao.updateByPrimaryKeySelective(record);
	}
	
	public static int insertZigbeeAttrSelective(ZigbeeAttr record) {
		return testUtils.zigbeeAttrDao.insertSelective(record);
	}
	
	public static DeviceAttr selectDeviceAttrByDevMac(String devMac) {
		return testUtils.devAttrDao.selectByPrimaryKey(devMac);
	}
	
	public static int updateDeviceAttrByPrimaryKeySelective(DeviceAttr record) {
		return testUtils.devAttrDao.updateByPrimaryKeySelective(record);
	}
	
	private Zigbee zigbeeBrightnessTransform(Zigbee record) {
		//在此处进行亮度转换
		//读取数据库判断节点类型
		ZigbeeAttr zba = testUtils.zigbeeAttrDao.selectByPrimaryKey(record.getZigbeeMac());
		if (zba != null && zba.getType() != null) {
			if (zba.getType() == 1 || zba.getType() == 2 || zba.getType() == 17 || zba.getType() == 18) {//读取节点设备类型
				if (record.getZigbeeBright() < 50) {//如果是金卤灯，钠灯，调为50
					record.setZigbeeBright(50);
//					System.out.println("已做处理");
				}
				//如果收到的回复是50-100不做处理
			}//如果是led灯，跳过判断，直接进行数据库读写操作
		}
		return record;
	}
}
