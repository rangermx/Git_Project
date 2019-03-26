package com.waho.domain;

public class Message {
	private String msg;
	private String cmd;
	private String mac;
	private int type;
	private int power;
	private int precentage;
	private int switchState;
	private String ssid;
	private String pw;
	private float humidity;
	private float temperature;
	private int err;

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
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

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	@Override
	public String toString() {
		return "Message [msg=" + msg + ", cmd=" + cmd + ", mac=" + mac + ", type=" + type + ", power=" + power
				+ ", precentage=" + precentage + ", switchState=" + switchState + ", ssid=" + ssid + ", pw=" + pw
				+ ", humidity=" + humidity + ", temperature=" + temperature + ", err=" + err + "]";
	}
}
