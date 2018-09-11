package com.waho.socket.util;

import com.waho.dao.NodeDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;
import com.waho.util.Protocol645Handler;

public class CmdNodeMsgHandler extends SocketDataHandler {

	private static volatile CmdNodeMsgHandler instance;

	public static CmdNodeMsgHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdNodeMsgHandler.class) {
				if (instance == null) {
					instance = new CmdNodeMsgHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdNodeMsgHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_NODE_MSG);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			Node node = Protocol645Handler.Transform645CmdToNode(sc.getData());
			if (node != null) {
				NodeDao nodeDao = new NodeDaoImpl();
				try {
					nodeDao.updateNodeStateAndPowerByNodeAddr(node);
					SocketCommand rep = new SocketCommand();
					rep.setCommand(SocketCommand.CMD_MAIN_NODE_MSG_REP);
					rep.setDataLen(0);
					return rep;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
