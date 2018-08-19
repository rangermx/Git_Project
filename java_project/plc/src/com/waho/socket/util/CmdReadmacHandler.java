package com.waho.socket.util;

import com.waho.domain.SocketCommand;

public class CmdReadmacHandler extends SocketDataHandler {

	private static volatile CmdReadmacHandler instance;

	public static CmdReadmacHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdReadmacHandler.class) {
				if (instance == null) {
					instance = new CmdReadmacHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdReadmacHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_READ_MAC);
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
