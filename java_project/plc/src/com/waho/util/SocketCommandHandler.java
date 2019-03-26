package com.waho.util;

import com.waho.domain.Node;
import com.waho.domain.SocketCommand;

/**
 * 指令信息处理工具类
 * @author mingxin
 *
 */
public class SocketCommandHandler {

	/**
	 * SocketCommnad对象的数据区数据封装到Node对象对应的属性
	 * @param sc
	 * @return
	 */
	public static Node TransformCmdToNode(SocketCommand sc) {
		Node node = new Node();
		switch (sc.getCommand()) {
		case SocketCommand.CMD_BROADCAST_WRITE_STATE_REP:
		case SocketCommand.CMD_BROADCAST_WRITE_STATE:
			if (sc.getData()[6] == 0x01) {
				node.setLight1State(true);
			} else if (sc.getData()[6] == 0x00) {
				node.setLight1State(false);
			}
			node.setLight1PowerPercent(sc.getData()[7] & 0xFF);
			if (sc.getData()[8] == 0x01) {
				node.setLight2State(true);
			} else if (sc.getData()[8] == 0x01) {
				node.setLight2State(false);
			}
			node.setLight2PowerPercent(sc.getData()[9] & 0xFF);
			break;
		case SocketCommand.CMD_WRITE_NODE_STATE_REP:
		case SocketCommand.CMD_READ_NODE_STATE_REP:
			node.setNodeAddr(SocketCommand.parseBytesToHexString(sc.getData(), 0, 6));
			byte[] power = new byte[2];
			power[0] = (byte) (sc.getData()[7]);
			power[1] = (byte) (sc.getData()[6]);
			int powerInt = Integer.parseInt(SocketCommand.parseBytesToHexString(power, 2), 16);
			node.setPower(powerInt);
			if (sc.getData()[8] == 0x01) {
				node.setLight1State(true);
			} else if (sc.getData()[8] == 0x01) {
				node.setLight1State(false);
			}
			node.setLight1PowerPercent(sc.getData()[9] & 0xFF);
			if (sc.getData()[10] == 0x01) {
				node.setLight2State(true);
			} else if (sc.getData()[10] == 0x01) {
				node.setLight2State(false);
			}
			node.setLight2PowerPercent(sc.getData()[11] & 0xFF);
			break;
		}
		return node;
	}

}
