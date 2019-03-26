package com.waho.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.waho.dao.UserMessageDao;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.domain.UserMessage;
import com.waho.util.C3P0Utils;

public class UserMessageDaoImpl implements UserMessageDao {

	@Override
	public UserMessage selectUserLastUserMessageByDevice(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.query("select * from user_message where deviceMac=? order by id DESC limit 1",
				new BeanHandler<UserMessage>(UserMessage.class), device.getDeviceMac());
	}

	@Override
	public int updateUserMessage(UserMessage um) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE user_message SET executed=? WHERE id=?", um.isExecuted(), um.getId());
		
	}

	@Override
	public int insertUserMessage(UserMessage um) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update(
				"INSERT INTO user_message (userid, deviceMac, command, len, info, data, executed, infoDomain) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
				um.getUserid(), um.getDeviceMac(), um.getCommand(), um.getLen(), um.getInfo(), um.getData(), um.isExecuted(), um.getInfoDomain());
	}

	@Override
	public UserMessage selectLastExecutedBroadcastUserMessageByDevice(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.query("select * from user_message where deviceMac=? and command=? and executed=? order by id DESC limit 1",
				new BeanHandler<UserMessage>(UserMessage.class), device.getDeviceMac(), SocketCommand.CMD_BROADCAST_WRITE_STATE, true);
	}

	@Override
	public List<UserMessage> selectUserMessageByDeviceMacAndInfoDomainAndCommand(String deviceMac, String infoDomain,
			byte command) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.query("select * from user_message where deviceMac=? and command=? and infoDomain=? and executed=?",
				new BeanListHandler<UserMessage>(UserMessage.class), deviceMac, command, infoDomain, true);
	}

}
