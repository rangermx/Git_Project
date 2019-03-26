package com.waho.socket.util;

import com.waho.dao.NodeDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;
import com.waho.util.SocketCommandHandler;

/**
 * 接收到读节点信息回复后的处理
 * @author mingxin
 *
 */
public class CmdReadNodeStateHandler extends SocketDataHandler {

	private static volatile CmdReadNodeStateHandler instance;

	public static CmdReadNodeStateHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdReadNodeStateHandler.class) {
				if (instance == null) {
					instance = new CmdReadNodeStateHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdReadNodeStateHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_READ_NODE_STATE_REP);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			Node node = SocketCommandHandler.TransformCmdToNode(sc);
			if (node != null) {
				node.setDeviceid(device.getId());
				NodeDao nodeDao = new NodeDaoImpl();
				try {
					nodeDao.updateNodeStateAndPowerByNodeAddrAndDeviceid(node);
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
