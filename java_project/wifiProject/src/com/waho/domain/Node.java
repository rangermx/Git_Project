package com.waho.domain;

public class Node {
	/**
	 * 节点数据库id
	 */
	private int id;
	/**
	 * 节点mac地址
	 */
	private String mac;
	/**
	 * 设备类型
	 */
	private int type;
	/**
	 * 设备额定满功率
	 */
	private int power;
	/**
	 * 当前功率百分比
	 */
	private int precentage;
	/**
	 * 灯具开关状态
	 */
	private int switchState;
	/**
	 * wifi模块链接的ap网络ssid
	 */
	private String ssid;
	/**
	 * ap网络的密码
	 */
	private String pw;
	/**
	 * 节点名称
	 */
	private String nodeName;
	/**
	 * 节点用户id
	 */
	private int userid;
	/**
	 * 温度
	 * @return
	 */
	private float temperature;
	/**
	 * 湿度
	 * @return
	 */
	private float humidity;
	/**
	 * 在线状态
	 * @return
	 */
	private boolean online;

	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getPrecentage() {
		return precentage;
	}
	public void setPrecentage(int precentage) {
		this.precentage = precentage;
	}
	public int getSwitchState() {
		return switchState;
	}
	public void setSwitchState(int switchState) {
		this.switchState = switchState;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	@Override
	public String toString() {
		return "Node [id=" + id + ", mac=" + mac + ", type=" + type + ", power=" + power + ", precentage=" + precentage
				+ ", switchState=" + switchState + ", ssid=" + ssid + ", pw=" + pw + ", nodeName=" + nodeName
				+ ", userid=" + userid + ", temperature=" + temperature + ", humidity=" + humidity + ", online="
				+ online + "]";
	}
}
