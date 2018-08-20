package com.waho.socket.state.impl;

import java.io.OutputStream;

import com.waho.domain.Device;
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
	public void clientDataHandle(byte[] data, int length, SocketDataHandler handler) {
		handler.socketDataHandle(data, length);
		System.out.write(data, 0, length);
		System.out.println();
	}

	/**
	 * 注册状态下，判断device是否已上报mac地址，注册完成
	 * 如果注册完成，跳转至数据同步状态
	 */
	@Override
	public void userMsgHanle(Device device, OutputStream out) {
		System.out.println(device.getDeviceMac());
	}

}
