package com.waho.socket.util;

import java.io.OutputStream;

import com.waho.dao.UserMessageDao;
import com.waho.dao.impl.UserMessageDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.domain.UserMessage;

/**
 * 责任链设计模式接口
 * 所有指令处理工具类应实现该接口
 * @author mingxin
 *
 */
public abstract class SocketDataHandler {

	/**
	 * 指令信息与该工具处理的指令不相同时，跳转到下一个工具类
	 */
	public SocketDataHandler nextHandler;

	/**
	 * 处理的信息类型
	 */
	private byte cmdType;

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
	public SocketCommand socketDataHandle(byte[] bytes, int len, Device device) {

		SocketCommand sc = SocketCommand.parseSocketCommand(bytes, len);

		if (sc != null) {// 数据有效
			return this.socketCommandHandle(sc, device);
		}
		System.out.println("数据无效");
		return null;
	}

	/**
	 * 解析并处理集控器上传的数据指令 1、获得指令码 2、解析数据 3、修改数据库 4、返回回复指令
	 * 
	 * @param sc
	 * @return
	 */
	public abstract SocketCommand socketCommandHandle(SocketCommand sc, Device device);

	/**
	 * 处理用户下发给集控器的指令
	 * @param device 集控器信息
	 * @param out 集控器绑定的socket输出流
	 */
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

	protected SocketDataHandler(SocketDataHandler nextHandler) {
		super();
		this.nextHandler = nextHandler;
	}

	public byte getCmdType() {
		return cmdType;
	}

	public void setCmdType(byte cmdType) {
		this.cmdType = cmdType;
	}

}
