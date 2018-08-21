package com.test.service.serviceimpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.DeviceMapper;
import com.test.dao.ZigbeeMapper;
import com.test.domain.Device;
import com.test.domain.Zigbee;
import com.test.service.IDeviceService;

@Service()
public class DeviceServiceImpl implements IDeviceService {

	@Autowired
	private DeviceMapper devDao;
	@Autowired
	private ZigbeeMapper zigbeeDao;

	public DeviceServiceImpl() {
//		System.out.println("DeviceServiceImpl");
	}

	public boolean updateDeviceData(Device dev) {
		if (dev.getDevMac() == null) {
			return false;
		}
		Device data = devDao.selectByPrimaryKey(dev.getDevMac());
		if (data == null) {//设备第一次连入数据库
			dev.setDevName(dev.getDevMac());
			dev.setDevNet(1);
			dev.setUserid(100000);
			if (dev.getGprsPhone() == null) {
				dev.setGprsPhone("unknown");
			}
			if (devDao.insert(dev) > 0) {//操作成功
				
			} else {//新设备加入数据库失败
				System.out.println("新设备加入数据库失败!");
				return false;
			}
		} else {//设备在数据库中有影子对象
			data.setDevNet(dev.getDevNet());
			if (dev.getGprsPhone() != null) {
				data.setGprsPhone(dev.getGprsPhone());//电话号码更新
			}
			devDao.updateByPrimaryKeySelective(data);
			if (dev.getDevNet() == 0) {//如果是下线，将所有下辖zigbee节点设置为离线状态
				Zigbee zb = new Zigbee();//创建新的数据模型
				zb.setDevMac(data.getDevMac());
				zb.setZigbeeNet(0);
				zigbeeDao.updateBydevMacSelectiveWhereOnline(zb);//根据数据模型更新数据
			} else if (dev.getDevNet() == 1) {//上线
				
			} else {//其他预留
				
			}
		}
//		System.out.println("调用service方法成功");
		return true;
	}
	
	public boolean updateZigbeeData(Zigbee zigbee) {
		Zigbee data = zigbeeDao.selectByPrimaryKey(zigbee.getZigbeeMac());
		if (data == null) {//尚未添加该节点
			zigbeeDao.insert(zigbee);
		}
		return false;
	}
	
	public boolean setZigbeeBrightnessByDevice(Device dev, int brightness) {
		return false;
	}
	
	public boolean setZigbeeBrightness(Zigbee zigbee, int brightness) {
		return false;
	}
	
	public ArrayList<Zigbee> selecetZigbeeByDevMac(String devMac) {
		return zigbeeDao.selectBydevMac(devMac);
	}
}
