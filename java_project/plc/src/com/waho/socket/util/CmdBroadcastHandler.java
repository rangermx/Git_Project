package com.waho.socket.util;

import com.waho.domain.SocketCommand;

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
	public SocketCommand socketCommandHandle(SocketCommand sc) {
		if (sc.getCommand() == this.getCmdType()) {

		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc);
		}
		return null;
	}

}
