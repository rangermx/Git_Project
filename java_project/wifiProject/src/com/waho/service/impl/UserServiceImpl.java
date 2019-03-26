package com.waho.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.waho.dao.NodeDao;
import com.waho.dao.UserDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.dao.impl.UserDaoImpl;
import com.waho.domain.Message;
import com.waho.domain.Node;
import com.waho.domain.User;
import com.waho.service.UserService;
import com.waho.util.MD5Utils;
import com.waho.websocket.WebSocketServlet;

public class UserServiceImpl implements UserService {

	@Override
	public Map<String, Object> login(String username, String password) {
		UserDao userDao = new UserDaoImpl();
		Map<String, Object> resultMap = null;
		User user;
		try {
			if (null != username && null != password && "".equals(username) == false && "".equals(password) == false) {
				user = userDao.selectUserByUsernameAndPassword(username, MD5Utils.MD5Encode(password, "utf-8"));
				if (user != null) {
					resultMap = new HashMap<String, Object>();
					resultMap.put("user", user);
					// 查询用户相关的设备数据
					NodeDao nodeDao = new NodeDaoImpl();
					List<Node> nodes = nodeDao.selectNodesByUserid(user.getId());
					resultMap.put("nodes", nodes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Node getNodeByIdString(String nodeid) {
		int id;
		try {
			id = Integer.parseInt(nodeid);
			NodeDao nodeDao = new NodeDaoImpl();
			return nodeDao.selectNodeById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean userWriteNodeCmd(String nodeid, String switchState, String percentage) {
		int id;
		try {
			id = Integer.parseInt(nodeid);
			NodeDao nodeDao = new NodeDaoImpl();
			Node node = nodeDao.selectNodeById(id);
			if (node == null || node.isOnline() == false) {
				return false;
			} else {// 设备存在，并且设备在线
				// 调用websocket发送指令接口
				for (WebSocketServlet socket : WebSocketServlet.webSocketSet) {
					if (socket.getId() == node.getId()) {
						Message cmd = new Message();
						cmd.setMsg("request");
						cmd.setCmd("write");
						cmd.setPrecentage(Integer.parseInt(percentage));
						if (switchState != null && switchState.equals("on")) {
							cmd.setSwitchState(1);
						} else {
							cmd.setSwitchState(0);
						}
						String cmdStr = JSON.toJSONString(cmd);
						socket.sendMessage(cmdStr);
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean userRegister(String username, String password, String email) {
		if (username != null && "".equals(username) == false && password != null && "".equals(password) == false
				&& email != null && "".equals(email) == false) {
			UserDao userDao = new UserDaoImpl();

			User user;
			try {
				user = userDao.selectUserByUsername(username);
				if (user == null) {

					user = new User();
					user.setUsername(username);
					// user.setPassword(password);
					user.setPassword(MD5Utils.MD5Encode(password, "utf-8"));
					user.setEmail(email);

					int result = userDao.insert(user);
					if (result > 0) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> login(int id) {
		UserDao userDao = new UserDaoImpl();
		Map<String, Object> resultMap = null;
		User user;
		try {
			user = userDao.selectUserById(id);
			if (user != null) {
				resultMap = new HashMap<String, Object>();
				resultMap.put("user", user);
				// 查询用户相关的设备数据
				NodeDao nodeDao = new NodeDaoImpl();
				List<Node> nodes = nodeDao.selectNodesByUserid(user.getId());
				resultMap.put("nodes", nodes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Boolean userRenameNode(String nodeid, String nodeName) {
		int id;
		try {
			id = Integer.parseInt(nodeid);
			NodeDao nodeDao = new NodeDaoImpl();
			Node node = nodeDao.selectNodeById(id);
			if (node != null) {
				int result = nodeDao.updateNodeNameById(id, nodeName);
				if (result > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Node> getNodeListByUserId(int id) {
		try {
			NodeDao nodeDao = new NodeDaoImpl();
			List<Node> nodeList = nodeDao.selectNodesByUserid(id);
			return nodeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int removeNodeById(List<Integer> idList, int userid) {
		NodeDao nodeDao = new NodeDaoImpl();
		try {
			List<Node> nodeList = nodeDao.selectNodesByUserid(userid);
			List<Integer> newIdList = new ArrayList<Integer>();
			for (int id : idList) {
				for (Node node : nodeList) {
					if (id == node.getId()) {
						newIdList.add(id);
						break;
					}
				}
			}
			if (newIdList.size() > 0) {
				int[] result = nodeDao.updateUseridByid(0, newIdList);
				int count = 0;
				for (int i : result) {
					if (i > 0) {
						count++;
					}
				}
				return count;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int userWriteWifiResetCmd(int userid, String ssid, String password, ArrayList<Integer> idList) {
		try {
			NodeDao nodeDao = new NodeDaoImpl();
			List<Node> nodeList = nodeDao.selectOnlineNodesByUserid(userid);
			if (nodeList == null || nodeList.size() == 0) {
				return 0;
			} else {// 设备存在，并且设备在线
				int count = 0;
				// 调用websocket发送指令接口
				for (WebSocketServlet socket : WebSocketServlet.webSocketSet) {
					for (Node node : nodeList) {
						if (socket.getId() == node.getId()) {
							Message cmd = new Message();
							cmd.setMsg("request");
							cmd.setCmd("write");
							cmd.setSsid(ssid);
							cmd.setPw(password);
							String cmdStr = JSON.toJSONString(cmd);
							socket.sendMessage(cmdStr);
							count++;
						}
					}
				}
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int broadcastControl(ArrayList<Integer> idList, int userid, boolean switchState, int percentage) {
		try {
			NodeDao nodeDao = new NodeDaoImpl();
			List<Node> nodeList = nodeDao.selectOnlineNodesByUserid(userid);
			if (nodeList == null || nodeList.size() == 0) {
				return 0;
			} else {// 设备存在，并且设备在线
				int count = 0;
				// 调用websocket发送指令接口
				for (WebSocketServlet socket : WebSocketServlet.webSocketSet) {
					for (Node node : nodeList) {
						if (socket.getId() == node.getId()) {
							Message cmd = new Message();
							cmd.setMsg("request");
							cmd.setCmd("write");
							cmd.setPrecentage(percentage);
							if (switchState == true) {
								cmd.setSwitchState(1);
							} else {
								cmd.setSwitchState(0);
							}
							String cmdStr = JSON.toJSONString(cmd);
							socket.sendMessage(cmdStr);
							count++;
						}
					}
				}
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int addNodeToUser(String nodeMac, int userid) {
		NodeDao nodeDao = new NodeDaoImpl();
		try {
			Node node = nodeDao.selectNodeByMac(nodeMac);
			if (node == null) {
				return 0;
			} else {
				return nodeDao.updateUseridByid(userid, node.getId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

}
