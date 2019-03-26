package com.waho.websocket;

import com.alibaba.fastjson.JSON;
import com.waho.dao.NodeDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Message;
import com.waho.domain.Node;

public class WebSocketRequestHandler {
	/**
	 * 处理request指令
	 * 
	 * @param requestStr
	 */
	public static void RequestHandle(String requestStr, WebSocketServlet servlet) {
		Message msg = JSON.parseObject(requestStr, Message.class);
		if (msg.getMsg().equals("request")) {
			loginHandle(msg, servlet);
		}
	}

	/**
	 * 处理登录请求
	 * 
	 * @param msg
	 */
	private static void loginHandle(Message msg, WebSocketServlet servlet) {
		if (msg.getCmd().equals("login")) {
			if (msg.getMac() != null) {
				Node node = new Node();
				node.setMac(msg.getMac());
				node.setUserid(1000);
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
					Node selectResult = nodeDao.selectNodeByMac(node.getMac());
					if (selectResult == null) {
						nodeDao.insert(node);
					} else {
						nodeDao.updateByMac(node);
					}
					selectResult = nodeDao.selectNodeByMac(node.getMac());
					servlet.setId(selectResult.getId());
					for (WebSocketServlet temp : WebSocketServlet.webSocketSet) {
						if (temp.getId() == servlet.getId() && temp.equals(servlet) == false) {
							temp.setId(0);
							break;
						}
					}
					Message rep = new Message();
					rep.setMsg("response");
					rep.setCmd("login");
					rep.setMac(node.getMac());
					rep.setErr(0);
					String repStr = JSON.toJSONString(rep);
					servlet.sendMessage(repStr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// /**
	// * 通过session将字符串发送出去
	// * @param session
	// * @param msg
	// * @throws Exception
	// */
	// private static void sendMessage(Session session, String msg) throws Exception
	// {
	// session.getBasicRemote().sendText(msg);
	// }
}
