package com.waho.socket.state.impl;

import java.io.OutputStream;

import com.waho.dao.DeviceDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.socket.state.SocketState;
import com.waho.socket.util.SocketDataHandler;

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
	public void clientDataHandle(byte[] data, int length, SocketDataHandler handler, Device device, OutputStream out, SocketState state) {
		SocketCommand result = handler.socketDataHandle(data, length);// 取处理结果
		if (result.getCommand() == SocketCommand.CMD_READ_MAC_REP) {// 如果是上报mac地址
			// 1、给socket的deivce赋值
			SocketCommand sc = SocketCommand.parseSocketCommand(data, length);
			String deviceMac = new String(sc.getData());
			DeviceDao deviceDao = new DeviceDaoImpl();
			try {
				device = deviceDao.selectDeviceByDeviceMac(deviceMac);
				if (device != null) {
					// 2、将状态切换到idle状态
					// 3、回复指令
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 其他指令一概不处理
			
		}
		System.out.write(data, 0, length);
		System.out.println();
	}

	@Override
	public void userMsgHanle(Device device, OutputStream out) {
		System.out.println(device.getDeviceMac());
	}

}
