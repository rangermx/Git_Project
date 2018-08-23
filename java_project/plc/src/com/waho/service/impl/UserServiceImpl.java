package com.waho.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.waho.dao.DeviceDao;
import com.waho.dao.NodeDao;
import com.waho.dao.UserDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.dao.impl.UserDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.User;
import com.waho.service.UserService;

public class UserServiceImpl implements UserService {

	@Override
	public Map<String, Object> login(String username, String password) {
		UserDao userDao = new UserDaoImpl();
		Map<String, Object> resultMap = null;
		User user;
		try {
			user = userDao.selectUserByUsernameAndPassword(username, password);
			if (user != null) {
				resultMap = new HashMap<String, Object>();
				resultMap.put("user", user);
				// 查询用户相关的设备数据
				DeviceDao deviceDao = new DeviceDaoImpl();
				List<Device> devices = deviceDao.selectDeviceByUserid(user.getId());
				resultMap.put("devices", devices);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}

	@Override
	public List<Node> getNodesByDeviceid(int deviceid) {
		NodeDao nodeDao = new NodeDaoImpl();
		List<Node> list = null;
		try {
			list = nodeDao.selectNodesByDeviceid(deviceid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
