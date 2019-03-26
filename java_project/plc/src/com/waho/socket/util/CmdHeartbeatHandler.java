package com.waho.socket.util;

import com.waho.dao.DeviceDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;

/**
 * 接受到心跳包以后的处理
 * @author mingxin
 *
 */
public class CmdHeartbeatHandler extends SocketDataHandler {

	private static volatile CmdHeartbeatHandler instance;

	public static CmdHeartbeatHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdHeartbeatHandler.class) {
				if (instance == null) {
					instance = new CmdHeartbeatHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdHeartbeatHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_HEARTBEAT);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			SocketCommand rep = new SocketCommand();
			rep.setCommand(SocketCommand.CMD_HEARTBEAT_REP);
//			rep.setLen(SocketCommand.LENGTH_WITHOUT_DATA);
			if (device != null && device.getDeviceMac() != null) {
				DeviceDao deviceDao = new DeviceDaoImpl();
				Device temp;
				try {
					temp = deviceDao.selectDeviceByDeviceMac(device.getDeviceMac());
					if (temp.isOnline() == false) {
						temp.setOnline(true);
						deviceDao.updateDeviceOnline(temp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return rep;
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

}
