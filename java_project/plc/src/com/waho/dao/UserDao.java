package com.waho.dao;

import com.waho.domain.User;

public interface UserDao {
	/**
	 * 根据用户名和密码查询用户信息
	 * @param username
	 * @param password
	 * @return user对象
	 * @throws Exception
	 */
	public User selectUserByUsernameAndPassword(String username, String password) throws Exception;
}
