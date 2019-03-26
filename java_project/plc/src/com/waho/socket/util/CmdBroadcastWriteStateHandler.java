package com.waho.socket.util;

import com.waho.dao.NodeDao;
import com.waho.dao.UserMessageDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.dao.impl.UserMessageDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;
import com.waho.domain.UserMessage;
import com.waho.util.Protocol645Handler;
import com.waho.util.SocketCommandHandler;

/**
 * 接收到广播指令回复后的处理
 * @author mingxin
 *
 */
public class CmdBroadcastWriteStateHandler extends SocketDataHandler {

	private static volatile CmdBroadcastWriteStateHandler instance;

	public static CmdBroadcastWriteStateHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdBroadcastWriteStateHandler.class) {
				if (instance == null) {
					instance = new CmdBroadcastWriteStateHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdBroadcastWriteStateHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_BROADCAST_WRITE_STATE_REP);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			
			Node node = SocketCommandHandler.TransformCmdToNode(sc);
			if (node != null) {
				node.setDeviceid(device.getId());
				NodeDao nodeDao = new NodeDaoImpl();
				try {
					nodeDao.updateNodeStateAndPowerByDeviceId(node);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 找到最后一条发送出去的广播指令
//			UserMessageDao umDao = new UserMessageDaoImpl();
//			try {
//				UserMessage um = umDao.selectLastExecutedBroadcastUserMessageByDevice(device);
//				if (um != null) {
//					// 将集控器下所有节点信息更新
//					NodeDao nodeDao = new NodeDaoImpl();
//					// 获取指令信息
//					Node cmdMsg = SocketCommandHandler.TransformCmdToNode(um);
//					cmdMsg.setDeviceid(device.getId());
//					// 更新数据
//					nodeDao.updateNodeStateAndPowerByDeviceId(cmdMsg);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
