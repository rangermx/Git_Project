package com.waho.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.waho.dao.DeviceDao;
import com.waho.dao.NodeDao;
import com.waho.dao.UserDao;
import com.waho.dao.UserMessageDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.dao.impl.UserDaoImpl;
import com.waho.dao.impl.UserMessageDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;
import com.waho.domain.User;
import com.waho.domain.UserMessage;
import com.waho.service.UserService;
import com.waho.util.Protocol645Handler;

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

	@Override
	public void userWriteNodeCmd(int nodeid, String light1State, String light2State, String light1PowerPercent,
			String light2PowerPercent) {
		// 根据nodeid获取设备信息
		NodeDao nodeDao = new NodeDaoImpl();
		DeviceDao deviceDao = new DeviceDaoImpl();
		UserMessageDao userMDao = new UserMessageDaoImpl();
		try {
			Node node = nodeDao.selectNodeById(nodeid);
			Device device = deviceDao.selectDeviceById(node.getDeviceid());
			UserMessage um = new UserMessage();
			um.setUserid(device.getUserid());
			um.setDeviceMac(device.getDeviceMac());
			um.setExecuted(false);
			um.setCommand(UserMessage.CMD_COMMUNCATE);
			// 1、将指令信息封装成指令对象
			um.setData(Protocol645Handler.GenerateNodeControl645Cmd(node.getNodeAddr(), light1State, light2State,
					light1PowerPercent, light2PowerPercent));
			um.setDataLen((byte) um.getData().length);
			// 2、写入数据库
			userMDao.insertUserMessage(um);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void userWriteBroadcastCmd(int deviceid, String light1State, String light2State, String light1PowerPercent,
			String light2PowerPercent) {
		DeviceDao deviceDao = new DeviceDaoImpl();
		UserMessageDao userMDao = new UserMessageDaoImpl();
		try {
			Device device = deviceDao.selectDeviceById(deviceid);
			UserMessage um = new UserMessage();
			um.setUserid(device.getUserid());
			um.setDeviceMac(device.getDeviceMac());
			um.setExecuted(false);
			um.setCommand(UserMessage.CMD_BROADCAST);
			// 1、将指令信息封装成指令对象
			um.setData(Protocol645Handler.GenerateBroadcastControl645Cmd(light1State, light2State, light1PowerPercent,
					light2PowerPercent));
			um.setDataLen((byte) um.getData().length);
			// 2、写入数据库
			userMDao.insertUserMessage(um);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void refreshNodeDataById(int nodeid) {
		// 查询节点信息，获取集控器mac地址，拼接查询指令，写入数据库
		NodeDao nodeDao = new NodeDaoImpl();
		try {
			Node node = nodeDao.selectNodeById(nodeid);
			if (node != null) {
				DeviceDao deviceDao = new DeviceDaoImpl();
				Device device = deviceDao.selectDeviceById(node.getDeviceid());
				if (device != null) {
					UserMessage um = new UserMessage();
					um.setCommand(SocketCommand.CMD_COMMUNCATE);
					um.setDeviceMac(device.getDeviceMac());
					um.setUserid(device.getUserid());
					um.setExecuted(false);
					um.setData(Protocol645Handler.GenerateNodeRefresh645Cmd(node.getNodeAddr()));
					if (um.getData() != null) {
						um.setDataLen(um.getData().length);
					}
					UserMessageDao umDao = new UserMessageDaoImpl();
					umDao.insertUserMessage(um);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
