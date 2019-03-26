package com.waho.domain;

public class UserMessage extends SocketCommand {
	/**
	 * 指令id 主键
	 */
	private int id;
	/**
	 * 发出该指令的用户id
	 */
	private int userid;
	/**
	 * 接受该指令的设备mac地址
	 */
	private String deviceMac;
	/**
	 * 指令是否已发出
	 */
	private boolean executed = false;
	/**
	 * 信息域，已弃用
	 */
	private String infoDomain;
	
	public String getInfoDomain() {
		return infoDomain;
	}
	public void setInfoDomain(String infoDomain) {
		this.infoDomain = infoDomain;
	}
	public boolean isExecuted() {
		return executed;
	}
	public void setExecuted(boolean executed) {
		this.executed = executed;
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
	public UserMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "UserMessage [id=" + id + ", userid=" + userid + ", deviceMac=" + deviceMac + ", executed=" + executed
				+ "]";
	}
	
}
