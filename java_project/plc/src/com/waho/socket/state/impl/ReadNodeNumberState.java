package com.waho.socket.state.impl;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.waho.dao.DeviceDao;
import com.waho.dao.NodeDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.socket.state.SocketState;
import com.waho.socket.util.SocketDataHandler;
/**
 * 集控器注册完成后，跳转到该状态，将查询节点数量的指令主动下发到集控器。
 * 接受到回复后，跳转到查询节点信息状态。
 * @author mingxin
 *
 */
public class ReadNodeNumberState implements SocketState {

	private Logger logger = Logger.getLogger(this.getClass());

	private int count;

	@Override
	public SocketState clientDataHandle(byte[] data, int length, SocketDataHandler handler, Device device,
			OutputStream out) {
		SocketCommand sc = SocketCommand.parseSocketCommand(data, length);
		if (sc != null && (byte) sc.getCommand() == SocketCommand.CMD_GET_NODE_NUM_REP) {// 如果是读节点数量回复
			// 更新数据库
			dataHandler(sc, device);

			return new ReadNodesMsgState();
		} else {// 其他指令一概不处理

		}
		return null;
	}

	private void dataHandler(SocketCommand sc, Device device) {
		
		byte[] num = new byte[2];
		num[0] = sc.getData()[1];
		num[1] = sc.getData()[0];
		int num_int = Integer.parseInt(SocketCommand.parseBytesToHexString(num, num.length));

		byte[] max = new byte[2];
		max[0] = sc.getData()[3];
		max[1] = sc.getData()[2];
		int mx_int = Integer.parseInt(SocketCommand.parseBytesToHexString(max, max.length));

		device.setMaxNodes(mx_int);
		device.setCurrentNodes(num_int);
		// 第二步、清空节点数据
		NodeDao nodeDao = new NodeDaoImpl();
		try {
			nodeDao.deletNodesByDevice(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 第三步、更新数据库的集控器对象
		DeviceDao deviceDao = new DeviceDaoImpl();
		try {
			deviceDao.updateDeviceNodes(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void userMsgHanle(Device device, OutputStream out) {
		if (this.count % 10 == 2) {// 每调用10次执行一次，每次调用间隔一个线程周期（1s）
			SocketCommand cmd = new SocketCommand();
			cmd.setCommand(SocketCommand.CMD_GET_NODE_NUM);
			cmd.setData(SocketCommand.generateGeNodeNumCommandData());
			try {
				out.write(cmd.tobyteArray());
				logger.info("service to " + device.getDeviceMac() + ": "
						+ SocketCommand.parseBytesToHexString(cmd.tobyteArray(), cmd.tobyteArray().length));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.count++;
	}

}
