package com.waho.socket.util;

import com.waho.domain.SocketCommand;

public class CmdMainNodeMsgHandler extends SocketDataHandler {

	private static volatile CmdMainNodeMsgHandler instance;

	public static CmdMainNodeMsgHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdMainNodeMsgHandler.class) {
				if (instance == null) {
					instance = new CmdMainNodeMsgHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdMainNodeMsgHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_MAIN_NODE_MSG);
	}

	@Override
	public byte[] socketCommandHandle(SocketCommand sc) {
		if (sc.getCommand() == this.getCmdType()) {

		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc);
		}
		return null;
	}

}
