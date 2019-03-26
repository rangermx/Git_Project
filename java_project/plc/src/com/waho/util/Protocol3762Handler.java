package com.waho.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import com.waho.dao.DeviceDao;
import com.waho.dao.NodeDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;
import com.waho.domain.UserMessage;

public class Protocol3762Handler {
	public static final byte HEADER = 0x68;
	public static final byte TAIL = 0x16;

	public static final byte DIR_MASTERTONODE_SEND = 0x41;
	public static final byte DIR_MASTERTONODE_REP = 0x01;
	public static final byte DIR_NODETOMASTER_SEND = (byte) 0xC1;
	public static final byte DIR_NODETOMASTER_REP = (byte) 0x81;

	public static final byte AFN00 = 0x00;// 确认帧指令码
	public static final byte AFN06 = 0x06;// 主节点主动上报
	public static final byte AFN10 = 0x10;
	public static final byte AFN11 = 0x11;

	public static final byte[] F1 = { 0x01, 0x00 };
	public static final byte[] F2 = { 0x02, 0x00 };
	public static final byte[] F3 = { 0x04, 0x00 };
	public static final byte[] F4 = { 0x08, 0x00 };
	public static final byte[] F5 = { 0x10, 0x00 };
	public static final byte[] F6 = { 0x20, 0x00 };
	public static final byte[] F7 = { 0x40, 0x00 };
	public static final byte[] F8 = { (byte) 0x80, 0x00 };
	public static Protocol3762InfoDomain infoDomain = new Protocol3762InfoDomain();

	/**
	 * 获取376.2指令的AFN码
	 * 
	 * @param sc
	 * @return
	 */
	public static byte GetAFNCode(SocketCommand sc) {
		if (CheckSum(sc.getData())) {
//			if (sc.getCommand() != SocketCommand.CMD_COMMUNCATE
//					&& sc.getCommand() != SocketCommand.CMD_COMMUNCATE_REP) {
//				return sc.getData()[10];
//			} else {
//				return sc.getData()[22];
//			}
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
//			if (sc.getCommand() != SocketCommand.CMD_COMMUNCATE
//					&& sc.getCommand() != SocketCommand.CMD_COMMUNCATE_REP) {
//				System.arraycopy(sc.getData(), 11, result, 0, 2);
//			} else {
//				System.arraycopy(sc.getData(), 23, result, 0, 2);
//			}
			return result;
		}
		return null;
	}

	/**
	 * AFN06主动上报：F1上报从节点信息 该指定为设置AFN11F5指令后收到的主节点主动上报的信息 返回值为回复信息
	 * 
	 * @param sc
	 * @param device
	 */
	public static byte[] AFN06F1DataHandle(SocketCommand sc, Device device) {
		// 获取上报节点数量
		byte nodeNumber = sc.getData()[13];
		// 数据分割
		byte[][] msgArr = new byte[nodeNumber][9];
		for (int i = 0; i < (int) nodeNumber; i++) {
			System.arraycopy(sc.getData(), 14 + i * 9, msgArr[i], 0, 9);
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
//			node.setSignal(Integer.parseInt(temp.substring(12, 13), 16));
//			node.setRelayLevel(Integer.parseInt(temp.substring(13, 14), 16));
			node.setAgreement(Integer.parseInt(temp.substring(12, 14), 16));
//			node.setPhase(Integer.parseInt(temp.substring(15, 16), 16));
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
		// 回复信息为确认帧，确认帧信息域与sc信息域相同
		byte[] result = new byte[0x15];
		result[0] = HEADER;
		result[1] = 0x15;
		result[2] = 0x00;
		result[3] = DIR_MASTERTONODE_REP;
		System.arraycopy(sc.getData(), 4, result, 4, 6);// 信息域相同
		result[10] = AFN00;// 确认帧
		System.arraycopy(F1, 0, result, 11, 2);// 确认
		result[13] = (byte)0xFF;
		result[14] = (byte)0xFF;
		result[15] = (byte)0xFF;
		result[16] = (byte)0xFF;
		result[17] = 0x00;
		result[18] = 0x00;
		result[19] = getByteSum(result, 3, 0x15 - 5);
		result[20] = TAIL;
		return result;
	}

	/**
	 * AFN10路由查询：F1从节点数量 回复信息处理
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
	 * AFN10路由查询：F2从节点信息 回复信息处理
	 * 
	 * @param sc
	 */
	public static void AFN10F2DataHandle(SocketCommand sc, Device device) {
		// 第一步、将数据切割
		byte node_num = sc.getData()[13];
		byte max_num = sc.getData()[15];
		byte min = node_num > max_num ? max_num : node_num;
		byte[][] msg = new byte[min][8];
		for (int i = 0; i < min; i++) {
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
			node.setNodeAddr(temp.substring(0, 6 * 2));
			node.setNodeName(node.getNodeAddr());
			node.setSignal(Integer.parseInt(temp.substring(12, 13), 16));
			node.setRelayLevel(Integer.parseInt(temp.substring(13, 14), 16));
			node.setAgreement(Integer.parseInt(temp.substring(14, 15), 16));
			node.setPhase(Integer.parseInt(temp.substring(15, 16), 16));
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
	 * AFN11路由设置：F5激活从节点主动注册 接收到回复后，调用该函数，将指令信息更新到设备状态
	 * 
	 * @param um
	 */
	public static void AFN11F5DataHandle(UserMessage um) {
		byte[] dateByte = new byte[6];
		byte[] minutesByte = new byte[2];
		System.arraycopy(um.getData(), 13, dateByte, 0, 6);
		System.arraycopy(um.getData(), 19, minutesByte, 0, 2);
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
			devDao.updateDeviceRegister(um.getDeviceMac(), date, minutes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成开启节点主动注册的376.2命令AFN11F5 startTime 为开始时间 keepMinutes 为持续性时间，单位分钟
	 * 
	 * @param startTime
	 * @param keepMinutes
	 * @return
	 */
	public static byte[] GenerateNodesRegisterOpen3762Cmd(Date startTime, int keepMinutes) {
		byte bufferLen = 0x19;
		byte[] buffer = new byte[bufferLen];
		buffer[0] = HEADER;
		buffer[1] = bufferLen;
		buffer[2] = 0x00;
		buffer[3] = DIR_MASTERTONODE_SEND;
		System.arraycopy(infoDomain.nextInfoDomain(), 0, buffer, 4, 6);// 信息域填充
		buffer[10] = AFN11;
		System.arraycopy(F5, 0, buffer, 11, 2);
		byte[] date = new byte[6];
		date[0] = parseByteTo8421Code((byte) (startTime.getYear() - 100));
		date[1] = parseByteTo8421Code((byte) (startTime.getMonth() + 1));
		date[2] = parseByteTo8421Code((byte) startTime.getDate());
		date[3] = parseByteTo8421Code((byte) startTime.getHours());
		date[4] = parseByteTo8421Code((byte) startTime.getMinutes());
		date[5] = parseByteTo8421Code((byte) startTime.getSeconds());
		byte[] minutes = parseIntegerTo8421Code(keepMinutes, 2, false);
		System.arraycopy(date, 0, buffer, 13, 6);
		System.arraycopy(minutes, 0, buffer, 19, 2);
		buffer[21] = 0x01;
		buffer[22] = 0x01;
		buffer[23] = getByteSum(buffer, 3, bufferLen - 5);
		buffer[24] = TAIL;
		return buffer;
	}
	
	/**
	 * 生成广播控制的376.2命令AFN05F3，data_645为需要广播发送的645指令
	 * @param data_645
	 * @return
	 */
	public static byte[] GenerateBroadcast3762Cmd(byte[] data_645) {
		int bufferLen = 0;
		byte[] buffer = new byte[bufferLen];
		return buffer;
	}

	/**
	 * 获取信息域
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] getInfoDomain(byte[] data) {
		byte[] result = new byte[6];
		System.arraycopy(data, 4, result, 0, 6);
		return result;
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

	/**
	 * 计算byte数组从index开始长度为length的所有数的和
	 * 
	 * @param src
	 * @param index
	 * @param length
	 * @return
	 */
	public static byte getByteSum(byte[] src, int index, int length) {
		if (src != null && src.length >= index + length) {
			byte sum = 0x00;
			for (int i = index; i < length + index; i++) {
				sum += src[i];
			}
			return sum;
		}
		return 0x00;
	}

	/**
	 * 将byte数转换成8421码对应的数
	 * 
	 * @param src
	 * @return
	 */
	public static byte parseByteTo8421Code(byte src) {
		int height = src / 10;
		int low = src % 10;
		return (byte) (((height & 0x0F) << 4) + (low & 0x0F));
	}

	/**
	 * 将int数转为8421码的byte数组 dir为true时，方向为高位在前低位在后 dir为false时，方向为低位在前高位在后
	 * 
	 * @param src
	 * @param byteArrLen
	 * @param dir
	 * @return
	 */
	public static byte[] parseIntegerTo8421Code(int src, int byteArrLen, boolean dir) {
		byte[] result = new byte[byteArrLen];
		String srcStr = Integer.toString(src);
		int strlen = srcStr.length();
		if (byteArrLen * 2 < strlen) {
			srcStr = srcStr.substring(byteArrLen * 2 - strlen);
		}
		int height;
		int low;
		for (int i = 0; i < byteArrLen; i++) {
			if (i * 2 < srcStr.length()) {
				height = Integer.parseInt(srcStr.substring(i * 2, i * 2 + 1));
			} else {
				height = 0;
			}
			if (i * 2 + 1 < srcStr.length()) {
				low = Integer.parseInt(srcStr.substring(i * 2 + 1, i * 2 + 2));
			} else {
				low = 0;
			}
			result[i] = (byte) (((height & 0x0F) << 4) + (low & 0x0F));
		}
		if (dir == true) {
			return result;
		} else {
			byte temp;
			for (int start = 0, end = result.length - 1; start < end; start++, end--) {
				temp = result[end];
				result[end] = result[start];
				result[start] = temp;
			}
			return result;
		}
	}

	public static boolean CheckSum(byte[] bytes) {
		if (!(bytes != null && bytes.length >= 3))
			return false;// 长度不足
		if (bytes[0] != HEADER || bytes[bytes.length - 1] != TAIL)
			return false;// 头尾错
		if ((byte) bytes.length != bytes[1])
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
