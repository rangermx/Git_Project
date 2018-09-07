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
	private int signal;
	private int relayLevel;
	private int agreement;
	private int Phase;
	
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public int getRelayLevel() {
		return relayLevel;
	}
	public void setRelayLevel(int relayLevel) {
		this.relayLevel = relayLevel;
	}
	public int getAgreement() {
		return agreement;
	}
	public void setAgreement(int agreement) {
		this.agreement = agreement;
	}
	public int getPhase() {
		return Phase;
	}
	public void setPhase(int phase) {
		Phase = phase;
	}
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
	@Override
	public String toString() {
		return "Node [id=" + id + ", deviceid=" + deviceid + ", deviceMac=" + deviceMac + ", nodeAddr=" + nodeAddr
				+ ", nodeName=" + nodeName + ", light1State=" + light1State + ", light2State=" + light2State
				+ ", light1PowerPercent=" + light1PowerPercent + ", light2PowerPercent=" + light2PowerPercent
				+ ", power=" + power + ", signal=" + signal + ", relayLevel=" + relayLevel + ", agreement=" + agreement
				+ ", Phase=" + Phase + "]";
	}
	
}
