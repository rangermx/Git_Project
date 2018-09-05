package com.waho.socket.state.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.socket.state.SocketState;
import com.waho.socket.util.SocketDataHandler;

public class IdleState implements SocketState {
	
	private static volatile IdleState instance;

	public static IdleState getInstance() {
		if (instance == null) {
			synchronized (IdleState.class) {
				if (instance == null) {
					instance = new IdleState();
				}
			}
		}
		return instance;
	}

	private IdleState() {
		super();
	}

	@Override
	public SocketState clientDataHandle(byte[] data, int length, SocketDataHandler handler, Device device, OutputStream out) {
//		System.out.println(SocketCommand.parseBytesToHexString(data, data.length));
		// 接受非登录操作的集控器数据，并进行相应的数据处理。
		SocketCommand sc = handler.socketDataHandle(data, length, device);
//		System.out.println(handler);
		// 如需要回复，进行回复
		if (sc != null) {
			try {
				out.write(sc.tobyteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void userMsgHanle(Device device, OutputStream out) {
		// 1、取出最后一条未执行的用户操作指令
		// 2、封包发送
		// 3、将指令状态置为已经执行，写回到数据库
	}

}
