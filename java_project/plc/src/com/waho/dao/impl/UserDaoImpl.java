package com.waho.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.waho.dao.UserDao;
import com.waho.domain.User;
import com.waho.util.C3P0Utils;

public class UserDaoImpl implements UserDao {

	@Override
	public User selectUserByUsernameAndPassword(String username, String password) throws SQLException{
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		User u = qr.query("select * from users where username=? and password=?", new BeanHandler<User>(User.class),
				username, password);
		return u;
	}

}
