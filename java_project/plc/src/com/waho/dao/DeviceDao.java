package com.waho.dao;

import java.util.List;

import com.waho.domain.Device;

public interface DeviceDao {
	public List<Device> selectDeviceByUserid(int id) throws Exception;
}
