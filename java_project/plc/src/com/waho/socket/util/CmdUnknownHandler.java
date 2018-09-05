package com.waho.socket.util;

import com.waho.domain.Device;
import com.waho.domain.SocketCommand;

public class CmdUnknownHandler extends SocketDataHandler {

	private static volatile CmdUnknownHandler instance;

	public static CmdUnknownHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdUnknownHandler.class) {
				if (instance == null) {
					instance = new CmdUnknownHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdUnknownHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_UNKNOWN);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		System.out.println(Integer.toHexString(sc.getCommand() & 0xFF) + " unknown");
		if (sc.getCommand() == this.getCmdType()) {

		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
