package com.waho.util;

import java.util.LinkedList;
import java.util.Map;

import com.waho.dao.DeviceDao;
import com.waho.dao.NodeDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;

public class Protocol3762Handler {
	public static final byte HEADER = 0x68;
	public static final byte TAIL = 0x16;
	public static final byte AFN10 = 0x10;
	public static final byte[] F1 = { 0x01, 0x00 };
	public static final byte[] F2 = { 0x02, 0x00 };

	/**
	 * 获取376.2指令的AFN码
	 * 
	 * @param sc
	 * @return
	 */
	public static byte GetAFNCode(SocketCommand sc) {
		if (CheckSum(sc.getData())) {
			if (sc.getCommand() != SocketCommand.CMD_COMMUNCATE
					&& sc.getCommand() != SocketCommand.CMD_COMMUNCATE_REP) {
				return sc.getData()[10];
			} else {
				return sc.getData()[22];
			}
		}
		return 0;
	}

	/**
	 * 获取376.2指令的F码
	 * 
	 * @param sc
	 * @return
	 */
	public static byte[] GetFCode(SocketCommand sc) {
		if (CheckSum(sc.getData())) {
			byte[] result = new byte[2];
			if (sc.getCommand() != SocketCommand.CMD_COMMUNCATE
					&& sc.getCommand() != SocketCommand.CMD_COMMUNCATE_REP) {
				System.arraycopy(sc.getData(), 11, result, 0, 2);
			} else {
				System.arraycopy(sc.getData(), 23, result, 0, 2);
			}
			return result;
		}
		return null;
	}

	/**
	 * AFN10路由查询：F1从节点数量
	 * 
	 * @param sc
	 */
	public static void AFN10F1DataHandle(SocketCommand sc, Device device) {
		// 第一步、将数据封装到集控器对象
		byte[] num = new byte[2];
		num[0] = sc.getData()[14];
		num[1] = sc.getData()[13];
		int num_int = Integer.parseInt(SocketCommand.parseBytesToHexString(num, num.length));
		
		byte[] max = new byte[2];
		max[0] = sc.getData()[16];
		max[1] = sc.getData()[15];
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

	/**
	 * AFN10路由查询：F2从节点信息
	 * 
	 * @param sc
	 */
	public static void AFN10F2DataHandle(SocketCommand sc, Device device) {
		// 第一步、将数据切割
		byte node_num = sc.getData()[13];
//		byte max_num = sc.getData()[15];
		byte[][] msg = new byte[node_num][8];
		for (int i = 0; i < node_num; i++) {
			System.arraycopy(sc.getData(), 16 + i * 8, msg[i], 0, 8);
		}
		// 第二步、封装到对象
		Node node = null;
		LinkedList<Node> list = new LinkedList<>();
		for (byte[] bs : msg) {
			node = new Node();
			node.setDeviceid(device.getId());
			node.setDeviceMac(device.getDeviceMac());
			String temp = SocketCommand.parseBytesToHexString(bs, bs.length);
			node.setNodeAddr(temp.substring(0,6 * 2));
			node.setNodeName(node.getNodeAddr());
			node.setSignal(Integer.parseInt(temp.substring(12,13), 16));
			node.setRelayLevel(Integer.parseInt(temp.substring(13,14), 16));
			node.setAgreement(Integer.parseInt(temp.substring(14,15), 16));
			node.setPhase(Integer.parseInt(temp.substring(15,16), 16));
			list.add(node);
		}
		// 第三步、写入数据库
		NodeDao nodeDao = new NodeDaoImpl();
		try {
			nodeDao.insert(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析数据，返回处理结果
	 * 
	 * @param sc
	 * @return
	 */
	public static Map<String, String> DataHandle(SocketCommand sc) {

		return null;
	}

	public static boolean CheckSum(byte[] bytes) {
		if (!(bytes != null && bytes.length >= 3))
			return false;// 长度不足
		if (bytes[0] != HEADER || bytes[bytes.length - 1] != TAIL)
			return false;// 头尾错
		if ((byte)bytes.length != bytes[1])
			return false;// 长度错
		int sum = 0;
		for (int i = 3; i < bytes.length - 2; i++) {
			sum = sum + bytes[i];
		}
		byte b = (byte) (sum & 0xFF);
		if (b != bytes[bytes.length - 2])
			return false;// 校验错
		return true;
	}
}
