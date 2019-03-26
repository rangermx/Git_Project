package com.waho.dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.waho.dao.UserDao;
import com.waho.domain.User;
import com.waho.util.C3P0Utils;

public class UserDaoImpl implements UserDao {

	@Override
	public User selectUserByUsernameAndPassword(String username, String password) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		User u = qr.query("select * from user where username=? and password=?", new BeanHandler<User>(User.class),
				username, password);
		return u;
	}

	@Override
	public int insert(User user) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update(
				"INSERT INTO `wifi_project`.`user` (`username`,`password`,`email`) VALUES (?, ?, ?)",
				user.getUsername(), user.getPassword(), user.getEmail());
	}

	@Override
	public User selectUserByUsername(String username) throws Exception{
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		User u = qr.query("select * from user where username=?", new BeanHandler<User>(User.class),
				username);
		return u;
	}

	@Override
	public User selectUserById(int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		User u = qr.query("select * from user where id=?", new BeanHandler<User>(User.class),
				id);
		return u;
	}

}
