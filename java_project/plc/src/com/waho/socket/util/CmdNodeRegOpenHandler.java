package com.waho.socket.util;

import java.util.Date;

import com.waho.dao.DeviceDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;

/**
 * 接收到开启节点主动注册回复后的处理
 * @author mingxin
 *
 */
public class CmdNodeRegOpenHandler extends SocketDataHandler {

	private static volatile CmdNodeRegOpenHandler instance;

	public static CmdNodeRegOpenHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdNodeRegOpenHandler.class) {
				if (instance == null) {
					instance = new CmdNodeRegOpenHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdNodeRegOpenHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_NODE_REG_OPEN_REP);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {

			updateDeviceBySocketCommand(sc, device);

		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

	private void updateDeviceBySocketCommand(SocketCommand sc, Device device) {
		byte[] dateByte = new byte[6];
		byte[] minutesByte = new byte[2];
		System.arraycopy(sc.getData(), 0, dateByte, 0, 6);
		System.arraycopy(sc.getData(), 6, minutesByte, 0, 2);
		String dateStr = SocketCommand.parseBytesToHexString(dateByte, 6);// 日期字符串
		byte temp = minutesByte[0];
		minutesByte[0] = minutesByte[1];
		minutesByte[1] = temp;
		String minutesStr = SocketCommand.parseBytesToHexString(minutesByte, 2);// 分钟字符串
		Date date = new Date();
		date.setDate(0);
		date.setYear(Integer.parseInt(dateStr.substring(0, 2)) + 100);
		date.setMonth(Integer.parseInt(dateStr.substring(2, 4)) - 1);
		date.setDate(Integer.parseInt(dateStr.substring(4, 6)));
		date.setHours(Integer.parseInt(dateStr.substring(6, 8)));
		date.setMinutes(Integer.parseInt(dateStr.substring(8, 10)));
		date.setSeconds(0);
		int minutes = Integer.parseInt(minutesStr);
		DeviceDao devDao = new DeviceDaoImpl();
		try {
			devDao.updateDeviceRegister(device.getDeviceMac(), date, minutes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
