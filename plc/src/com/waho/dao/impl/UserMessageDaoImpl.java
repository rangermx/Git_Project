package com.waho.dao.impl;

import org.apache.commons.dbutils.QueryRunner;

import com.waho.dao.UserMessageDao;
import com.waho.domain.Device;
import com.waho.domain.UserMessage;
import com.waho.util.C3P0Utils;

public class UserMessageDaoImpl implements UserMessageDao {

	@Override
	public UserMessage selectUserLastUserMessageByDevice(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		// 查询用户指令信息表中最后一条对该设备的控制指令
		return null;
	}

	@Override
	public int updateUserMessage(UserMessage um) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
