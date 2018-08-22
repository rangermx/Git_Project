package com.waho.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
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

}
