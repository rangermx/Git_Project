package com.waho.dao;

import com.waho.domain.User;

public interface UserDao {
	public User selectUserByUsernameAndPassword(String username, String password) throws Exception;
}
