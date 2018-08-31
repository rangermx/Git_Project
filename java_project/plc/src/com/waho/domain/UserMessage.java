package com.waho.domain;

public class UserMessage extends SocketCommand {
	private int id;
	private int userid;
	private String deviceMac;
	private boolean executed = false;
	
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
