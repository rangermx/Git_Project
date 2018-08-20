package com.waho.socket.state;

import java.io.OutputStream;

import com.waho.domain.Device;
import com.waho.socket.util.SocketDataHandler;

public interface SocketState {
	/**
	 * 处理socket接收到的客户端数据
	 * @param data
	 * @param length
	 * @param handler
	 */
	public void clientDataHandle(byte[] data, int length, SocketDataHandler handler);
	/**
	 * 处理用户写入到数据库的指令
	 * @param device
	 * @param out
	 */
	public void userMsgHanle(Device device, OutputStream out);
}
