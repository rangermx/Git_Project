package com.waho.socket.util;

import com.waho.domain.Device;
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
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			SocketCommand rep = new SocketCommand();
			rep.setCommand(SocketCommand.CMD_HEARTBEAT_REP);
			rep.setDataLen(0);
			return rep;
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
