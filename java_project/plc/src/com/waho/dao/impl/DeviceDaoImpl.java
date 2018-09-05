package com.waho.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.waho.dao.DeviceDao;
import com.waho.domain.Device;
import com.waho.util.C3P0Utils;

public class DeviceDaoImpl implements DeviceDao {

	@Override
	public List<Device> selectDeviceByUserid(int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		List<Device> result = qr.query("select * from devices where userid=?", new BeanListHandler<>(Device.class), id);
		return result;
	}

	@Override
	public Device selectDeviceById(int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.query("select * from devices where id=?", new BeanHandler<Device>(Device.class), id);
	}

	@Override
	public Device selectDeviceByDeviceMac(String deviceMac) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.query("select * from devices where deviceMac=?", new BeanHandler<Device>(Device.class), deviceMac);
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
		return qr.update("UPDATE devices SET maxNodes=?,currentNodes=? WHERE deviceMac=?", device.getMaxNodes(), device.getCurrentNodes(), device.getDeviceMac());
	}

}
