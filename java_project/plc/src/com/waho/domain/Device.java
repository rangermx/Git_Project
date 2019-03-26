package com.waho.domain;

import java.util.Date;

public class Device {
	private int id;
	/**
	 * 用户id
	 */
	private int userid;
	/**
	 * 设备mac地址
	 */
	private String deviceMac;
	/**
	 * 设备名称
	 */
	private String deviceName;
	/**
	 * 在线状态
	 */
	private boolean online;
	/**
	 * 当前从节点数量
	 */
	private int currentNodes;
	/**
	 * 最大从节点数量
	 */
	private int maxNodes;
	/**
	 * 是否允许节点主动注册
	 */
	private boolean nodesRegister = false;
	/**
	 * 允许节点主动注册时间长度
	 */
	private int registerMinutes;
	/**
	 * 允许节点主动注册开启时间点
	 */
	private Date registDate;
	
	
	public boolean isNodesRegister() {
		return nodesRegister;
	}
	public void setNodesRegister(boolean nodesRegister) {
		this.nodesRegister = nodesRegister;
	}
	public int getRegisterMinutes() {
		return registerMinutes;
	}
	public void setRegisterMinutes(int registerMinutes) {
		this.registerMinutes = registerMinutes;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
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
