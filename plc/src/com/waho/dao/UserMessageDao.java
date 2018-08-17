package com.waho.dao;

import com.waho.domain.Device;
import com.waho.domain.UserMessage;

public interface UserMessageDao {
	/**
	 * 查询用户对该设备发出的最后一条控制指令
	 * @param device
	 * @return
	 */
	public UserMessage selectUserLastUserMessageByDevice(Device device) throws Exception;
	
	/**
	 * 更新用户指令，返回更新行数
	 * @param um
	 * @return
	 */
	public int updateUserMessage(UserMessage um) throws Exception;
	
}
