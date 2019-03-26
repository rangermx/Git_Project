package com.waho.dao;

import java.util.Date;
import java.util.List;

import com.waho.domain.Device;

public interface DeviceDao {
	/**
	 * 根据用户id查询该用户使用的所有集控器
	 * @param id 用户id
	 * @return 集控器列表
	 * @throws Exception
	 */
	public List<Device> selectDeviceByUserid(int id) throws Exception;
	/**
	 * 根据设备id查询集控器信息
	 * @param id 设备id
	 * @return Device 集控器对象
	 * @throws Exception
	 */
	public Device selectDeviceById(int id) throws Exception;
	/**
	 * 根据设备mac地址查询集控器信息
	 * @param deviceMac 集控器mac地址
	 * @return Device 对象
	 * @throws Exception
	 */
	public Device selectDeviceByDeviceMac(String deviceMac) throws Exception;
	/**
	 * 更新设备的在线状态
	 * @param device 集控器信息，mac地址和在线状态不能为空
	 * @return 数据库更新行数
	 * @throws Exception
	 */
	public int updateDeviceOnline(Device device) throws Exception;
	/**
	 * 添加集控器
	 * @param device 集控器信息
	 * @return 数据库插入行数
	 * @throws Exception
	 */
	public int insert(Device device) throws Exception;
	/**
	 * 更新集控器最大节点数量，当前节点数量
	 * @param device
	 * @return 数据库更新行数
	 * @throws Exception
	 */
	public int updateDeviceNodes(Device device) throws Exception;
	/**
	 * 更新集控器主动注册状态
	 * @param deviceMac 集控器mac地址
	 * @param date 开启时间
	 * @param minutes 开启时长
	 * @return 数据库更新行数
	 * @throws Exception
	 */
	public int updateDeviceRegister(String deviceMac, Date date, int minutes) throws Exception;
	/**
	 * 更新集控器当前节点数量
	 * @param device
	 * @return 数据库更新行数
	 * @throws Exception
	 */
	public int updateDeviceCurrentNodes(Device device) throws Exception;
}
