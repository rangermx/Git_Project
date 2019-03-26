package com.waho.dao;

import com.waho.domain.User;

public interface UserDao {
	/**
	 * 根据用户名和密码查询用户信息
	 * @param username
	 * @param password
	 * @return
	 */
	public User selectUserByUsernameAndPassword(String username, String password) throws Exception;

	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public int insert(User user) throws Exception;

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	public User selectUserByUsername(String username) throws Exception;

	/**
	 * 根据id查询用户
	 * @param id
	 * @return
	 */
	public User selectUserById(int id) throws Exception;
}
