package com.test.service;

import java.util.ArrayList;

import com.test.domain.Device;
import com.test.domain.Zigbee;

public interface IDeviceService {
	
	public boolean updateDeviceData(Device dev);
	
	public boolean updateZigbeeData(Zigbee zigbee);
	
	public boolean setZigbeeBrightnessByDevice(Device dev, int brightness);
	
	public boolean setZigbeeBrightness(Zigbee zigbee, int brightness);
	
	public ArrayList<Zigbee> selecetZigbeeByDevMac(String devMac);
	
}
