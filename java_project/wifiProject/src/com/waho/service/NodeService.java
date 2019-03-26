package com.waho.service;

public interface NodeService {
	/**
	 * 根据节点id，将节点的在线状态更新为离线
	 * @param id
	 */
	void setNodeOfflineById(int id);
}
