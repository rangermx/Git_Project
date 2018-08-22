package com.waho.service;

import java.util.Map;


public interface UserService {
	/**
	 * 用户登录操作,登陆成功：返回用户的设备列表，登录失败：返回null（用户名或密码错误）
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String, Object> login(String username, String password);
}
