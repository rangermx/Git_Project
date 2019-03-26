package com.waho.dao;

import java.util.List;

import com.waho.domain.Device;
import com.waho.domain.UserMessage;

public interface UserMessageDao {
	/**
	 * 查询用户对该设备发出的最后一条控制指令
	 * @param device
	 * @return UserMessage
	 */
	public UserMessage selectUserLastUserMessageByDevice(Device device) throws Exception;
	
	/**
	 * 更新用户指令，返回更新行数
	 * @param um
	 * @return
	 */
	public int updateUserMessage(UserMessage um) throws Exception;
	
	/**
	 * 插入新的用户指令
	 * @param um
	 * @return
	 * @throws Exception
	 */
	public int insertUserMessage(UserMessage um) throws Exception;

	/**
	 * 根据集控器查找最后一条已执行的广播指令
	 * @param device
	 * @return
	 */
	public UserMessage selectLastExecutedBroadcastUserMessageByDevice(Device device) throws Exception;

	public List<UserMessage> selectUserMessageByDeviceMacAndInfoDomainAndCommand(String deviceMac, String infoDomain, byte command) throws Exception;
	
}
