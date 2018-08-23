package com.waho.service;

import java.util.List;
import java.util.Map;

import com.waho.domain.Node;


public interface UserService {
	/**
	 * 用户登录操作,登陆成功：返回用户的设备列表，登录失败：返回null（用户名或密码错误）
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String, Object> login(String username, String password);
	
	/**
	 * 根据集控器设备id获取节点信息
	 * @return
	 */
	public List<Node> getNodesByDeviceid(int deviceid);
}
