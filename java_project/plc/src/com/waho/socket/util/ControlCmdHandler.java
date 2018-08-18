package com.waho.socket.util;

import com.waho.domain.SocketCommand;

public class ControlCmdHandler extends SocketDataHandler {
	
	static {
		ControlCmdHandler.cmdType = SocketCommand.CMD_CONTROL;
	}
	
	public ControlCmdHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
	}

	@Override
	public byte[] socketCommandHandle(SocketCommand sc) {
		if (sc.getCommand() == ControlCmdHandler.cmdType) {
			
			
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc);
		}
		
		return null;
	}


}
