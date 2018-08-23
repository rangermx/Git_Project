package com.waho.domain;

public class Node {
	private int id;
	private int deviceid;
	private String deviceMac;
	private String nodeAddr;
	private String nodeName;
	private boolean light1State;
	private boolean light2State;
	private int light1PowerPercent;
	private int light2PowerPercent;
	private int power;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public String getNodeAddr() {
		return nodeAddr;
	}
	public void setNodeAddr(String nodeAddr) {
		this.nodeAddr = nodeAddr;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public boolean isLight1State() {
		return light1State;
	}
	public void setLight1State(boolean light1State) {
		this.light1State = light1State;
	}
	public boolean isLight2State() {
		return light2State;
	}
	public void setLight2State(boolean light2State) {
		this.light2State = light2State;
	}
	public int getLight1PowerPercent() {
		return light1PowerPercent;
	}
	public void setLight1PowerPercent(int light1PowerPercent) {
		this.light1PowerPercent = light1PowerPercent;
	}
	public int getLight2PowerPercent() {
		return light2PowerPercent;
	}
	public void setLight2PowerPercent(int light2PowerPercent) {
		this.light2PowerPercent = light2PowerPercent;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	
}
