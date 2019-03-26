package com.waho.dao;

import java.util.List;

import com.waho.domain.Device;
import com.waho.domain.Node;

public interface NodeDao {
	/**
	 * 根据设备id查询节点信息
	 * @param deviceid
	 * @return 链接在该集控器下的节点信息
	 * @throws Exception
	 */
	public List<Node> selectNodesByDeviceid(int deviceid) throws Exception;
	/**
	 * 根据节点id查询节点信息
	 * @param id
	 * @return Node 对象
	 * @throws Exception
	 */
	public Node selectNodeById(int id) throws Exception;
	/**
	 * 添加新节点
	 * @param node
	 * @return 添加的行数
	 * @throws Exception
	 */
	public int insert(Node node) throws Exception;
	/**
	 * 添加多个新节点
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int insert(List<Node> list) throws Exception;
	/**
	 * 将集控器下链接的所有节点删除
	 * @param device
	 * @return
	 * @throws Exception
	 */
	public int deletNodesByDevice(Device device) throws Exception;
	/**
	 * 根据节点的地址和链接的集控器id，更新节点的状态和功率
	 * @param node
	 * @return 更新的数据库行数
	 * @throws Exception
	 */
	public int updateNodeStateAndPowerByNodeAddrAndDeviceid(Node node) throws Exception;
	/**
	 * 根据集控器id，将集控器下所有的节点状态和功率更新
	 * @param node
	 * @return 数据库更新的行数
	 * @throws Exception
	 */
	public int updateNodeStateAndPowerByDeviceId(Node node) throws Exception;
}
