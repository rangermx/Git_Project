package com.waho.socket.state.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.waho.dao.NodeDao;
import com.waho.dao.UserMessageDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.dao.impl.UserMessageDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;
import com.waho.domain.UserMessage;
import com.waho.socket.state.SocketState;
import com.waho.socket.util.SocketDataHandler;

/**
 * 空闲状态，此时设备已注册完成，信息同步也已完成，可以进行正常的控制操作。 该状态查询数据库中所有类型的用户指令，转发给集控器。
 * 同时根据集控器上报的数据，更新数据库。
 * 
 * @author mingxin
 *
 */
public class IdleState implements SocketState {

	private static volatile IdleState instance;

	private static Integer SlowTimes = 5;

	private Logger logger = Logger.getLogger(this.getClass());

	private List<Node> nodeList;

	private Integer pollCount = 0;

	private Integer pollSizeMemory = 0;

	private Integer slowCount = 0;

	private NodeDao nodeDao = new NodeDaoImpl();

	public static IdleState getInstance() {
//		if (instance == null) {
//			synchronized (IdleState.class) {
//				if (instance == null) {
//					instance = new IdleState();
//				}
//			}
//		}
//		return instance;
		return new IdleState();
	}

	private IdleState() {
		super();
	}

	@Override
	public SocketState clientDataHandle(byte[] data, int length, SocketDataHandler handler, Device device,
			OutputStream out) {
		// System.out.println(SocketCommand.parseBytesToHexString(data, data.length));
		// 接受非登录操作的集控器数据，并进行相应的数据处理。
		SocketCommand sc = handler.socketDataHandle(data, length, device);
		// System.out.println(handler);
		// 如需要回复，进行回复
		if (sc != null) {
			try {
				out.write(sc.tobyteArray());
				logger.info("service to " + device.getDeviceMac() + ": "
						+ SocketCommand.parseBytesToHexString(sc.tobyteArray(), sc.tobyteArray().length));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void userMsgHanle(Device device, OutputStream out) {
		// 1、取出最后一条未执行的用户操作指令
		UserMessageDao umDao = new UserMessageDaoImpl();
		try {
			UserMessage um = umDao.selectUserLastUserMessageByDevice(device);
			// 2、封包发送
			if (um != null && !um.isExecuted()) {
				out.write(um.tobyteArray());
				logger.info("service to " + device.getDeviceMac() + ": "
						+ SocketCommand.parseBytesToHexString(um.tobyteArray(), um.tobyteArray().length));
				// 3、将指令状态置为已经执行，写回到数据库
				um.setExecuted(true);
				umDao.updateUserMessage(um);
			} else if (++slowCount >= SlowTimes) { // 未有指令执行，进行轮询操作
				slowCount = 0;// 延时操作，函数三次调用只执行一次

				nodeList = nodeDao.selectNodesByDeviceid(device.getId());
				if (nodeList != null) {
					if (pollSizeMemory != nodeList.size()) {
						pollCount = 0;
						pollSizeMemory = nodeList.size();
					}
					if (pollCount < pollSizeMemory) {
						if (pollCount >= 0) {
							Node node = nodeList.get(pollCount);
							if (node != null) {
								if (device != null) {
									um = new UserMessage();
									um.setCommand(SocketCommand.CMD_READ_NODE_STATE);
									um.setDeviceMac(device.getDeviceMac());
									um.setUserid(device.getUserid());
									um.setExecuted(false);
									um.setData(SocketCommand.GenerateReadNodeStateCommandData(node.getNodeAddr()));
									out.write(um.tobyteArray());
									logger.info("service to " + device.getDeviceMac() + ": " + SocketCommand
											.parseBytesToHexString(um.tobyteArray(), um.tobyteArray().length));
								}
							}
						}
						pollCount++;
					} else {
						pollCount = -1200;
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
