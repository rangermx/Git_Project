package com.waho.websocket;

import javax.websocket.Session;

import com.alibaba.fastjson.JSON;
import com.waho.dao.NodeDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Message;
import com.waho.domain.Node;

public class WebSocketResponseHandler {

	/**
	 * 处理response指令
	 * @param message
	 * @param session
	 */
	public static void responseHandle(String responseStr, Session session) {
		Message msg = JSON.parseObject(responseStr, Message.class);
		if (msg.getMsg().equals("response")) {
			writeHandle(session, msg);
			readHandle(session, msg);
		}
	}

	private static void readHandle(Session session, Message msg) {
		if (msg.getCmd().equals("read") && msg.getErr() == 0) {
			if (msg.getMac() != null) {
				Node node = new Node();
				node.setMac(msg.getMac());
				node.setType(msg.getType());
				node.setNodeName(node.getMac());
				node.setOnline(true);
				node.setPower(msg.getPower());
				node.setSsid(msg.getSsid());
				node.setPw(msg.getPw());
				node.setPrecentage(msg.getPrecentage());
				node.setSwitchState(msg.getSwitchState());
				NodeDao nodeDao = new NodeDaoImpl();
				try {
					nodeDao.updateByMac(node);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static void writeHandle(Session session, Message msg) {
		if (msg.getCmd().equals("write") && msg.getErr() == 0) {
			Node node = new Node();
			node.setMac(msg.getMac());
			node.setPrecentage(msg.getPrecentage());
			node.setSwitchState(msg.getSwitchState());
			NodeDao nodeDao = new NodeDaoImpl();
			try {
				nodeDao.updatePrecentageAndSwitchStateByMac(node);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	/**
//	 * 通过session将字符串发送出去
//	 * @param session
//	 * @param msg
//	 * @throws Exception
//	 */
//	private static void sendMessage(Session session, String msg) throws Exception {
//		session.getBasicRemote().sendText(msg);
//	}
}
