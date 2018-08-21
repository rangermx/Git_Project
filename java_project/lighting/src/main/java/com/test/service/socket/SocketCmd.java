package com.test.service.socket;

import java.util.Timer;

public class SocketCmd {
	
	private String cmdString = "";
	private boolean running = false;
//	private String zigbeeNwkAddr;
	private String start = "FE";
	private String length = "";
	private String cmd0 = "";
	private String cmd1 = "";
	private String addr_mode = "";
	private String addr_type = "";
	private String dst_ep = "0A";
	private String cluster_id = "";
	private String cmd_type = "";
	private String cmd_id = "";
	private String payload = "";
	private Timer timer = new Timer();
	
	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void createCmdString() {
		cmdString = start + length + cmd0 + cmd1 + addr_mode + addr_type + dst_ep + cluster_id + cmd_type + cmd_id + payload;
	}
	
	public SocketCmd() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCmdString() {
		return cmdString;
	}
	public void setCmdString(String cmdString) {
		this.cmdString = cmdString;
	}
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getCmd0() {
		return cmd0;
	}
	public void setCmd0(String cmd0) {
		this.cmd0 = cmd0;
	}
	public String getCmd1() {
		return cmd1;
	}
	public void setCmd1(String cmd1) {
		this.cmd1 = cmd1;
	}
	public String getAddr_mode() {
		return addr_mode;
	}
	public void setAddr_mode(String addr_mode) {
		this.addr_mode = addr_mode;
	}
	public String getAddr_type() {
		return addr_type;
	}
	public void setAddr_type(String addr_type) {
		this.addr_type = addr_type;
	}
	public String getDst_ep() {
		return dst_ep;
	}
	public void setDst_ep(String dst_ep) {
		this.dst_ep = dst_ep;
	}
	public String getCluster_id() {
		return cluster_id;
	}
	public void setCluster_id(String cluster_id) {
		this.cluster_id = cluster_id;
	}
	public String getCmd_type() {
		return cmd_type;
	}
	public void setCmd_type(String cmd_type) {
		this.cmd_type = cmd_type;
	}
	public String getCmd_id() {
		return cmd_id;
	}
	public void setCmd_id(String cmd_id) {
		this.cmd_id = cmd_id;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	@Override
	public String toString() {
		return "SocketCmd [cmdString=" + cmdString + ", running=" + running + ", start=" + start + ", length=" + length + ", cmd0="
				+ cmd0 + ", cmd1=" + cmd1 + ", addr_mode=" + addr_mode + ", addr_type=" + addr_type + ", dst_ep="
				+ dst_ep + ", cluster_id=" + cluster_id + ", cmd_type=" + cmd_type + ", cmd_id=" + cmd_id + ", payload="
				+ payload + "]";
	}
	
}
