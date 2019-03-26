package com.waho.socket.state.impl;

import java.io.OutputStream;

import com.waho.dao.DeviceDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.socket.state.SocketState;
import com.waho.socket.util.SocketDataHandler;
/**
 * 未注册状态，该状态只接受用户读mac地址上报指令，其他指令不予处理。
 * 接收到mac地址后，将socket的device对象赋值，将socket线程与该集控器绑定。更新数据库中集控器的状态，跳转数据同步状态（读节点数量，读节点信息）。
 * @author mingxin
 *
 */
public class RegisterState implements SocketState {

	private static volatile RegisterState instance;

	public static RegisterState getInstance() {
		if (instance == null) {
			synchronized (RegisterState.class) {
				if (instance == null) {
					instance = new RegisterState();
				}
			}
		}
		return instance;
	}

	private RegisterState() {
		super();
	}

	@Override
	public SocketState clientDataHandle(byte[] data, int length, SocketDataHandler handler, Device device,
			OutputStream out) {
		SocketCommand sc = SocketCommand.parseSocketCommand(data, length);
		if (sc != null && (byte)sc.getCommand() == SocketCommand.CMD_READ_MAC) {// 如果是上报mac地址
//			System.out.println("进入mac地址处理");
			SocketCommand result = handler.socketDataHandle(data, length, device);// 取处理结果
			if (result != null && result.getCommand() == SocketCommand.CMD_READ_MAC_REP) {
				// 1、给socket的deivce赋值
				String deviceMac = SocketCommand.parseBytesToHexString(sc.getData(), sc.getLen() - SocketCommand.LENGTH_WITHOUT_DATA);
				DeviceDao deviceDao = new DeviceDaoImpl();
				try {
					Device dbDevice = deviceDao.selectDeviceByDeviceMac(deviceMac);
					device.setId(dbDevice.getId());
					device.setDeviceMac(dbDevice.getDeviceMac());
					device.setDeviceName(dbDevice.getDeviceName());
					device.setCurrentNodes(dbDevice.getCurrentNodes());
					device.setOnline(dbDevice.isOnline());
					device.setMaxNodes(dbDevice.getMaxNodes());
					device.setUserid(dbDevice.getUserid());
//					System.out.println(device.toString());
					if (device != null) {
						// 2、回复指令
//						System.out.println("进入回复");
						out.write(result.tobyteArray());
						// 3、将状态切换到idle状态
//						return IdleState.getInstance();
						return new ReadNodeNumberState();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {// 其他指令一概不处理

		}
		return null;
	}

	@Override
	public void userMsgHanle(Device device, OutputStream out) {

	}

}
