package com.waho.socket.util;

import com.waho.domain.Device;
import com.waho.domain.SocketCommand;

public class CmdControlHandler extends SocketDataHandler {
	
	private static volatile CmdControlHandler instance;

	public static CmdControlHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdControlHandler.class) {
				if (instance == null) {
					instance = new CmdControlHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdControlHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_CONTROL_REP);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {

		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
