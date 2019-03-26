package com.waho.socket.util;

import java.util.LinkedList;

import com.waho.dao.DeviceDao;
import com.waho.dao.NodeDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;

/**
 * 接收到新节点上报信息后的处理
 * @author mingxin
 *
 */
public class CmdNewNodeReportHandler extends SocketDataHandler {

	private static volatile CmdNewNodeReportHandler instance;

	public static CmdNewNodeReportHandler getInstance(SocketDataHandler nextHandler) {
		if (instance == null) {
			synchronized (CmdNewNodeReportHandler.class) {
				if (instance == null) {
					instance = new CmdNewNodeReportHandler(nextHandler);
				}
			}
		}
		return instance;
	}

	private CmdNewNodeReportHandler(SocketDataHandler nextHandler) {
		super(nextHandler);
		this.setCmdType(SocketCommand.CMD_NEW_NODE_REPORT);
	}

	@Override
	public SocketCommand socketCommandHandle(SocketCommand sc, Device device) {
		if (sc.getCommand() == this.getCmdType()) {
			SocketCommand result = new SocketCommand();
			dataHandle(sc, device);
			result.setCommand(SocketCommand.CMD_NEW_NODE_REPORT_REP);
//			result.setLen(SocketCommand.LENGTH_WITHOUT_DATA);
			return result;
		} else if (nextHandler != null) {
			return nextHandler.socketCommandHandle(sc, device);
		}
		return null;
	}

	private void dataHandle(SocketCommand sc, Device device) {
		// 获取上报节点数量
		byte nodeNumber = sc.getData()[0];
		// 数据分割
		byte[][] msgArr = new byte[nodeNumber][9];
		for (int i = 0; i < (int) nodeNumber; i++) {
			System.arraycopy(sc.getData(), 1 + i * 9, msgArr[i], 0, 9);
		}
		// 封装到节点对象
		LinkedList<Node> list = new LinkedList<>();
		for (byte[] bs : msgArr) {
			Node node = new Node();
			node.setDeviceid(device.getId());
			node.setDeviceMac(device.getDeviceMac());
			String temp = SocketCommand.parseBytesToHexString(bs, bs.length);
			node.setNodeAddr(temp.substring(0, 6 * 2));
			node.setNodeName(node.getNodeAddr());
			node.setAgreement(Integer.parseInt(temp.substring(12, 14), 16));
			list.add(node);
		}
		// 更新数据库
		NodeDao nodeDao = new NodeDaoImpl();
		// 读取集控器数据，根据集控器节点的数量进行添加
		device.setCurrentNodes(device.getCurrentNodes() + list.size());
		DeviceDao devDao = new DeviceDaoImpl();
		try {
			devDao.updateDeviceCurrentNodes(device);
			nodeDao.insert(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
