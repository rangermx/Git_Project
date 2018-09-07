package com.waho.socket.util;

import com.waho.dao.NodeDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.util.Protocol645Handler;
import com.waho.domain.Node;

public class CmdCommuncateHandler extends SocketDataHandler {

	private static volatile CmdCommuncateHandler instance;

	public static CmdCommuncateHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdCommuncateHandler.class) {
				if (instance == null) {
					instance = new CmdCommuncateHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdCommuncateHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_COMMUNCATE_REP);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			Node node = Protocol645Handler.Transform645CmdToNode(sc.getData());
			if (node != null) {
				NodeDao nodeDao = new NodeDaoImpl();
				try {
					nodeDao.updateNodeStateAndPower(node);
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
