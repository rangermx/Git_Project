package com.waho.dao;

import java.util.List;

import com.waho.domain.Node;

public interface NodeDao {
	/**
	 * 根据用户id查询节点信息
	 * @param userid
	 * @return
	 */
	public List<Node> selectNodesByUserid(int userid) throws Exception;
	/**
	 * 根据节点id修改userid
	 * @param userid
	 * @param id
	 * @return
	 */
	public int updateUseridByid(int userid, int id) throws Exception;
	/**
	 * 将节点信息存入数据库
	 * @param node
	 * @return
	 */
	public int insert(Node node) throws Exception;
	/**
	 * 根据mac地址查询节点信息
	 * @param mac
	 * @return
	 */
	public Node selectNodeByMac(String mac) throws Exception;
	/**
	 * 根据mac地址，更新节点的类型、功率、在线状态、当前开关状态、当前功率百分比、wifissid、wifipw
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public int updateByMac(Node node) throws Exception;
	/**
	 * 根据mac地址，更新节点的当前功率百分比，当前开关状态
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public int updatePrecentageAndSwitchStateByMac(Node node) throws Exception;
	/**
	 * 根据id查询节点信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Node selectNodeById(int id) throws Exception;
	/**
	 * 根据id更新节点在线状态
	 * @param b
	 * @param id
	 * @throws Exception
	 */
	public int updateOnlineById(boolean b, int id) throws Exception;
	/**
	 * 根据id更新节点名称
	 * @param id
	 * @param nodeName
	 * @throws Exception
	 */
	public int updateNodeNameById(int id, String nodeName) throws Exception;
	/**
	 * 根据id更新节点用户id
	 * @param userid
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public int[] updateUseridByid(int userid, List<Integer> idList) throws Exception;
	/**
	 * 根据userid查询在线的节点
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public List<Node> selectOnlineNodesByUserid(int userid) throws Exception;
	
}
