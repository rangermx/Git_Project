package com.waho.socket.util;

import com.waho.domain.SocketCommand;

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
	public SocketCommand socketCommandHandle(SocketCommand sc) {
		if (sc.getCommand() == this.getCmdType()) {

		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc);
		}
		return null;
	}

}
