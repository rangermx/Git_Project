package com.test.domain;

import java.util.ArrayList;

public class DataObject {
	private User user;
	private ArrayList<Device> devArr;
	private ArrayList<DeviceAttr> devAttrArr;
	private ArrayList<Zigbee> zigbeeArr;
	private ArrayList<ZigbeeAttr> zigbeeAttrArr;
	private ArrayList<Group> groupArr;
	private ArrayList<GroupPair> groupPairArr;
	private ArrayList<Ploy> ployArr;
	private ArrayList<PloyOperate> ployOperateArr;
	private String error;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ArrayList<Device> getDevArr() {
		return devArr;
	}
	public void setDevArr(ArrayList<Device> devArr) {
		this.devArr = devArr;
	}
	public ArrayList<DeviceAttr> getDevAttrArr() {
		return devAttrArr;
	}
	public void setDevAttrArr(ArrayList<DeviceAttr> devAttrArr) {
		this.devAttrArr = devAttrArr;
	}
	public ArrayList<Zigbee> getZigbeeArr() {
		return zigbeeArr;
	}
	public void setZigbeeArr(ArrayList<Zigbee> zigbeeArr) {
		this.zigbeeArr = zigbeeArr;
	}
	public ArrayList<Group> getGroupArr() {
		return groupArr;
	}
	public void setGroupArr(ArrayList<Group> groupArr) {
		this.groupArr = groupArr;
	}
	public ArrayList<GroupPair> getGroupPairArr() {
		return groupPairArr;
	}
	public void setGroupPairArr(ArrayList<GroupPair> groupPairArr) {
		this.groupPairArr = groupPairArr;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public ArrayList<ZigbeeAttr> getZigbeeAttrArr() {
		return zigbeeAttrArr;
	}
	public void setZigbeeAttrArr(ArrayList<ZigbeeAttr> zigbeeAttrArr) {
		this.zigbeeAttrArr = zigbeeAttrArr;
	}
	public ArrayList<Ploy> getPloyArr() {
		return ployArr;
	}
	public void setPloyArr(ArrayList<Ploy> ployArr) {
		this.ployArr = ployArr;
	}
	public ArrayList<PloyOperate> getPloyOperateArr() {
		return ployOperateArr;
	}
	public void setPloyOperateArr(ArrayList<PloyOperate> ployOperateArr) {
		this.ployOperateArr = ployOperateArr;
	}
	@Override
	public String toString() {
		return "DataObject [user=" + user + ", devArr=" + devArr + ", devAttrArr=" + devAttrArr + ", zigbeeArr="
				+ zigbeeArr + ", zigbeeAttrArr=" + zigbeeAttrArr + ", groupArr=" + groupArr + ", groupPairArr="
				+ groupPairArr + ", ployArr=" + ployArr + ", ployOperateArr=" + ployOperateArr + ", error=" + error
				+ "]";
	}
	
	
	
}
