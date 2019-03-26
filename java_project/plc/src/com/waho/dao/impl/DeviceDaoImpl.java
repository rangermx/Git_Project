package com.waho.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.waho.dao.DeviceDao;
import com.waho.domain.Device;
import com.waho.util.C3P0Utils;

public class DeviceDaoImpl implements DeviceDao {
	
	public void checkNodesRegister(Device device) throws Exception {
		if (device.getRegistDate() != null && device.getRegisterMinutes() != 0) {
			Date date = new Date();
			long check = date.getTime() - device.getRegistDate().getTime();
			if (check > 0 && check <= device.getRegisterMinutes() * 60 * 1000) {
				if (!device.isNodesRegister()) {
					device.setNodesRegister(true);
					updateDeviceRegister(device);
				}
			} else if (device.isNodesRegister()) {
				device.setNodesRegister(false);
				updateDeviceRegister(device);
			}
		}
	}

	@Override
	public List<Device> selectDeviceByUserid(int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		List<Device> result = qr.query("select * from devices where userid=?", new BeanListHandler<>(Device.class), id);
		for (Device device : result) {
			checkNodesRegister(device);
		}
		return result;
	}

	@Override
	public Device selectDeviceById(int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		Device device = qr.query("select * from devices where id=?", new BeanHandler<Device>(Device.class), id);
		checkNodesRegister(device);
		return device;
	}

	@Override
	public Device selectDeviceByDeviceMac(String deviceMac) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		Device device = qr.query("select * from devices where deviceMac=?", new BeanHandler<Device>(Device.class), deviceMac);
		if (device != null) {
			checkNodesRegister(device);
		}
		return device;
	}

	@Override
	public int updateDeviceOnline(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE devices SET online=? WHERE deviceMac=?", device.isOnline(), device.getDeviceMac());
	}

	@Override
	public int insert(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update(
				"INSERT INTO `plc`.`devices` (`deviceMac`, `userid`, `online`, `currentNodes`, `maxNodes`, `deviceName`) VALUES (?, ?, ?, ?, ?, ?)",
				device.getDeviceMac(), device.getUserid(), device.isOnline(), device.getCurrentNodes(),
				device.getMaxNodes(), device.getDeviceName());

	}

	@Override
	public int updateDeviceNodes(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE devices SET maxNodes=?,currentNodes=? WHERE deviceMac=?", device.getMaxNodes(),
				device.getCurrentNodes(), device.getDeviceMac());
	}

	@Override
	public int updateDeviceRegister(String deviceMac, Date date, int minutes) throws Exception {
		Date now = new Date();
		boolean nodesRegister = false;
		long check = now.getTime() - date.getTime();
		if (check > 0 && check <= minutes * 60 * 1000) {
			nodesRegister = true;
		}
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE devices SET registDate=?,registerMinutes=?,nodesRegister=? WHERE deviceMac=?", date, minutes, nodesRegister,
				deviceMac);

	}
	
	private int updateDeviceRegister(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE devices SET registDate=?,registerMinutes=?,nodesRegister=? WHERE deviceMac=?", device.getRegistDate(), device.getRegisterMinutes(), device.isNodesRegister(),
				device.getDeviceMac());
	}

	@Override
	public int updateDeviceCurrentNodes(Device device) throws Exception{
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE devices SET currentNodes=? WHERE deviceMac=?", device.getCurrentNodes(),
				device.getDeviceMac());
	}

}
