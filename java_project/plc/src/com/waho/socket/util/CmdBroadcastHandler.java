package com.waho.socket.util;

import com.waho.dao.NodeDao;
import com.waho.dao.UserMessageDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.dao.impl.UserMessageDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.domain.UserMessage;

public class CmdBroadcastHandler extends SocketDataHandler {
	
	private static volatile CmdBroadcastHandler instance;

	public static CmdBroadcastHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdBroadcastHandler.class) {
				if (instance == null) {
					instance = new CmdBroadcastHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdBroadcastHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_BROADCAST_REP);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			// 找到最后一条发送出去的广播指令
			UserMessageDao umDao = new UserMessageDaoImpl();
			try {
				UserMessage um = umDao.selectLastExecutedBroadcastUserMessageByDevice(device);
				if (um != null) {
					// 将集控器下所有节点信息更新
					NodeDao nodeDao = new NodeDaoImpl();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
