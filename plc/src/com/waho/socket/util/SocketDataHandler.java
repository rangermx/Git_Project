package com.waho.socket.util;

import java.io.OutputStream;

import com.waho.dao.UserMessageDao;
import com.waho.dao.impl.UserMessageDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.domain.UserMessage;

public abstract class SocketDataHandler {

	public SocketDataHandler nextHandler;

	public static byte cmdType;

	public SocketDataHandler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(SocketDataHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	/**
	 * 处理从socket端口接收到的集控器上传数据
	 * 
	 * @param bytes
	 *            数据数组
	 * @param len
	 *            数据长度
	 * @return bytes 如果指令解析完成后，根据交互逻辑判断，需要回复集控器，则返回回复数据
	 */
	public byte[] socketDataHandle(byte[] bytes, int len) {
		
		SocketCommand sc = SocketCommand.parseSocketCommand(bytes, len);
		
		if (sc != null) {// 数据有效
			return this.socketCommandHandle(sc);
		}

		return null;
	}

	/**
	 * 解析并处理集控器上传的数据指令 1、获得指令码 2、解析数据 3、修改数据库 4、返回回复指令
	 * 
	 * @param sc
	 * @return
	 */
	public abstract byte[] socketCommandHandle(SocketCommand sc);

	public static void UserMessageHandle(Device device, OutputStream out) {
		UserMessageDao dao = new UserMessageDaoImpl();
		// 1、从数据库读最后一条用户指令
		UserMessage um;
		try {
			um = dao.selectUserLastUserMessageByDevice(device);
			// 2、判断执行状态是否为已执行
			if (um != null && !um.isExecuted()) {
				// 3、执行未执行的指令，并将指令状态设置为已执行
				out.write(um.tobyteArray());
				um.setExecuted(true);// 设置为已执行
				dao.updateUserMessage(um);// 写回数据库

			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public SocketDataHandler(SocketDataHandler nextHandler) {
		super();
		this.nextHandler = nextHandler;
	}
	
	
}
