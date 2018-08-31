package com.waho.socket.util;

import com.waho.domain.SocketCommand;

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
	public SocketCommand socketCommandHandle(SocketCommand sc) {
		if (sc.getCommand() == this.getCmdType()) {

		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc);
		}
		return null;
	}
}
