package com.waho.service;

import java.util.List;
import java.util.Map;

import com.waho.domain.Node;

public interface UserService {
	/**
	 * 用户登录操作,登陆成功：返回用户的设备列表，登录失败：返回null（用户名或密码错误）
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String, Object> login(String username, String password);

	/**
	 * 根据集控器设备id获取节点信息
	 * 
	 * @return
	 */
	public List<Node> getNodesByDeviceid(int deviceid);

	/**
	 * 用户发送节点控制指令（将用户指令写入数据库）
	 * 
	 * @param nodeid
	 * @param light1State
	 * @param light2State
	 * @param light1PowerPercent
	 * @param light2PowerPercent
	 */
	public void userWriteNodeCmd(int nodeid, String light1State, String light2State, String light1PowerPercent,
			String light2PowerPercent);

	/**
	 * 用户发送广播控制指令
	 * 
	 * @param deviceid
	 * @param light1State
	 * @param light2State
	 * @param light1PowerPercent
	 * @param light2PowerPercent
	 */
	public void userWriteBroadcastCmd(int deviceid, String light1State, String light2State, String light1PowerPercent,
			String light2PowerPercent);
	
	/**
	 * 用户手动刷新节点状态信息
	 * @param nodeid
	 */
	public void refreshNodeDataById(int nodeid);
}
