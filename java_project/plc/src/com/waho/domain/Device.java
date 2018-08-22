package com.waho.domain;

public class Device {
	private int id;
	private int userid;// 用户id
	private String deviceMac;// 设备mac地址
	private String deviceName;// 设备名称
	private boolean online;// 在线状态
	private int currentNodes;// 当前从节点数量
	private int maxNodes;// 最大从节点数量
	
	
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public int getCurrentNodes() {
		return currentNodes;
	}
	public void setCurrentNodes(int currentNodes) {
		this.currentNodes = currentNodes;
	}
	public int getMaxNodes() {
		return maxNodes;
	}
	public void setMaxNodes(int maxNodes) {
		this.maxNodes = maxNodes;
	}
	
}
