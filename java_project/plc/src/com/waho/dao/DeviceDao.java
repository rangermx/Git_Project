package com.waho.dao;

import java.util.List;

import com.waho.domain.Device;

public interface DeviceDao {
	public List<Device> selectDeviceByUserid(int id) throws Exception;
	public Device selectDeviceById(int id) throws Exception;
	public Device selectDeviceByDeviceMac(String deviceMac) throws Exception;
	public int updateDeviceOnline(Device device) throws Exception;
	public int insert(Device device) throws Exception;
	public int updateDeviceNodes(Device device) throws Exception;
}
