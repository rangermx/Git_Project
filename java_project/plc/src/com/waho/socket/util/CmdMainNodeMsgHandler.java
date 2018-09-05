package com.waho.socket.util;

import java.util.Arrays;

import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.util.Protocol3762Handler;

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
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			SocketCommand result = new SocketCommand();
			// 处理主节点主动上报信息
			byte[] fCode = Protocol3762Handler.GetFCode(sc);
			switch (Protocol3762Handler.GetAFNCode(sc)) {
			case Protocol3762Handler.AFN10:
				if (Arrays.equals(fCode, Protocol3762Handler.F1)) {// 上报集控器最大节点数量和当前节点数量
					Protocol3762Handler.AFN10F1DataHandle(sc, device);
				} else if (Arrays.equals(fCode, Protocol3762Handler.F2)) {// 上报节点信息
					Protocol3762Handler.AFN10F2DataHandle(sc, device);
				}
				break;
			}
			result.setCommand(SocketCommand.CMD_MAIN_NODE_MSG_REP);
			result.setDataLen((byte)0);
			return result;
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
