package com.waho.socket.util;

import com.waho.dao.DeviceDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;

/**
 * 接收到上报mac地址后的处理
 * @author mingxin
 *
 */
public class CmdReadMacHandler extends SocketDataHandler {

	private static volatile CmdReadMacHandler instance;

	public static CmdReadMacHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdReadMacHandler.class) {
				if (instance == null) {
					instance = new CmdReadMacHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdReadMacHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_READ_MAC);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			// 1、返回回复指令
			SocketCommand rep = new SocketCommand();
			rep.setCommand(SocketCommand.CMD_READ_MAC_REP);
//			rep.setData(null);
//			rep.setLen((byte)0x00 + SocketCommand.LENGTH_WITHOUT_DATA);
			// 2、查询数据库，是否有mac地址相同的集控器，如果有，就修改状态，如果没有，就添加
			DeviceDao deviceDao = new DeviceDaoImpl();
			String deviceMac = SocketCommand.parseBytesToHexString(sc.getData(), sc.getLen() - SocketCommand.LENGTH_WITHOUT_DATA);
			try {
				Device temp = deviceDao.selectDeviceByDeviceMac(deviceMac);
				if (temp != null) {
					temp.setOnline(true);
					deviceDao.updateDeviceOnline(temp);
				} else {
					temp = new Device();
					temp.setDeviceMac(deviceMac);
					temp.setDeviceName(deviceMac);
					temp.setOnline(true);
					temp.setUserid(1000);
					temp.setMaxNodes(0);
					temp.setCurrentNodes(0);
					deviceDao.insert(temp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rep;
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
