package com.waho.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.waho.domain.Node;

public interface UserService {

	/**
	 * 登录服务，返回用户信息和节点列表 map的数据结构为 { user:user Object nodes:[ node Object ] }
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	Map<String, Object> login(String username, String password);

	/**
	 * 根据节点id的字符串返回节点对象
	 * 
	 * @param nodeid
	 * @return
	 */
	Node getNodeByIdString(String nodeid);

	/**
	 * 根据节点id，对节点的开关状态、功率输出百分比进行控制，返回指令发送成功或失败（不代表执行成功和失败，执行结果需要从数据库查询）
	 * 
	 * @param nodeid
	 * @param switchState
	 * @param percentage
	 */
	Boolean userWriteNodeCmd(String nodeid, String switchState, String percentage);

	/**
	 * 用户注册，注册成功返回true，注册失败返回false
	 * @param username
	 * @param password
	 * @param email
	 * @return
	 */
	Boolean userRegister(String username, String password, String email);

	/**
	 * 登录服务，返回用户信息和节点列表 map的数据结构为 { user:user Object nodes:[ node Object ] }
	 * @param id
	 * @return
	 */
	Map<String, Object> login(int id);

	/**
	 * 节点重命名，成功返回true，失败返回false
	 * @param nodeid 要修改的节点id
	 * @param nodeName 新的节点名称
	 * @return
	 */
	Boolean userRenameNode(String nodeid, String nodeName);

	/**
	 * 根据用户id获取节点信息
	 * @param id
	 * @return
	 */
	List<Node> getNodeListByUserId(int id);

	/**
	 * 根据节点id删除节点的用户id信息
	 * @param idList
	 * @param userid 
	 * @return
	 */
	int removeNodeById(List<Integer> idList, int userid);

	/**
	 * 根据节点id，查找与userid相同的在线节点的webSocket，发送修改wifi名称和密码的指令。返回值为成功发送的指令条数，即成功发送的节点个数。
	 * @param userid
	 * @param ssid
	 * @param password
	 * @param idList
	 * @return
	 */
	int userWriteWifiResetCmd(int userid, String ssid, String password, ArrayList<Integer> idList);

	/**
	 * 广播控制节点开关和输出功率百分比，返回成功发送的指令条数
	 * @param idList
	 * @param userid
	 * @param switchState
	 * @param percentage
	 * @return
	 */
	int broadcastControl(ArrayList<Integer> idList, int userid, boolean switchState, int percentage);

	/**
	 * 将节点添加到用户名下。
	 * @param nodeMac
	 * @param userid
	 * @return
	 */
	int addNodeToUser(String nodeMac, int userid);

}
