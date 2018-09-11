package com.waho.dao;

import java.util.List;

import com.waho.domain.Device;
import com.waho.domain.Node;

public interface NodeDao {
	public List<Node> selectNodesByDeviceid(int deviceid) throws Exception;
	public Node selectNodeById(int id) throws Exception;
	public int insert(Node node) throws Exception;
	public int insert(List<Node> list) throws Exception;
	public int deletNodesByDevice(Device device) throws Exception;
	public int updateNodeStateAndPowerByNodeAddr(Node node) throws Exception;
	public int updateNodeStateAndPowerByDeviceId(Node node) throws Exception;
}
