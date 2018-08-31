package com.waho.dao;

import java.util.List;

import com.waho.domain.Node;

public interface NodeDao {
	public List<Node> selectNodesByDeviceid(int deviceid) throws Exception;
	public Node selectNodeById(int id) throws Exception;
}
