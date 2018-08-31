package com.waho.socket.util;

import com.waho.domain.SocketCommand;

public class CmdHeartbeatHandler extends SocketDataHandler {

	private static volatile CmdHeartbeatHandler instance;

	public static CmdHeartbeatHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdHeartbeatHandler.class) {
				if (instance == null) {
					instance = new CmdHeartbeatHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdHeartbeatHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_HEARTBEAT);
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
