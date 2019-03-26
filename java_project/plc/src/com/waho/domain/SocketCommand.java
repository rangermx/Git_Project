package com.waho.domain;

import java.util.Date;

import com.waho.util.Protocol3762Handler;

public class SocketCommand {
	/**
	 * 通讯协议规定的指令消息头
	 */
	public static final byte HEADER = (byte) 0xFB;
	/**
	 * 通讯协议规定的指令消息尾
	 */
	public static final byte TAIL = (byte) 0xFE;

	/**
	 * 心跳包回复指令码
	 */
	public static final byte CMD_HEARTBEAT_REP = 0x01;
	/**
	 * 读mac地址回复指令码
	 */
	public static final byte CMD_READ_MAC_REP = 0x02;
	/**
	 * 获取节点数量指令码
	 */
	public static final byte CMD_GET_NODE_NUM = 0x03;
	/**
	 * 获取节点信息指令码
	 */
	public static final byte CMD_GET_NODE_MSG = 0x04;
	/**
	 * 开启节点主动注册指令码
	 */
	public static final byte CMD_NODE_REG_OPEN = 0x05;
	/**
	 * 集控器数据区初始化指令码
	 */
	public static final byte CMD_DATA_INIT = 0x06;
	/**
	 * 集控器硬件初始化指令码
	 */
	public static final byte CMD_HARDWARE_INIT = 0x07;
	/**
	 * 写节点状态指令码
	 */
	public static final byte CMD_WRITE_NODE_STATE = 0x08;
	/**
	 * 广播写节点状态指令码
	 */
	public static final byte CMD_BROADCAST_WRITE_STATE = 0x09;
	/**
	 * 读节点状态指令码
	 */
	public static final byte CMD_READ_NODE_STATE = 0x0A;
	/**
	 * 新节点上报回复指令码
	 */
	public static final byte CMD_NEW_NODE_REPORT_REP = 0x0B;

	/**
	 * 心跳包指令码
	 */
	public static final byte CMD_HEARTBEAT = (byte) 0x81;
	/**
	 * 读mac地址指令码
	 */
	public static final byte CMD_READ_MAC = (byte) 0x82;
	/**
	 * 获取节点数量回复指令码
	 */
	public static final byte CMD_GET_NODE_NUM_REP = (byte) 0x83;
	/**
	 * 获取节点信息指令码
	 */
	public static final byte CMD_GET_NODE_MSG_REP = (byte) 0x84;
	/**
	 * 开启节点主动注册回复指令码
	 */
	public static final byte CMD_NODE_REG_OPEN_REP = (byte) 0x85;
	/**
	 * 集控器数据区初始化回复指令码
	 */
	public static final byte CMD_DATA_INIT_REP = (byte) 0x86;
	/**
	 * 集控器硬件初始化回复指令码
	 */
	public static final byte CMD_HARDWARE_INIT_REP = (byte) 0x87;
	/**
	 * 写节点状态回复指令码
	 */
	public static final byte CMD_WRITE_NODE_STATE_REP = (byte) 0x88;
	/**
	 * 广播写节点状态指令码
	 */
	public static final byte CMD_BROADCAST_WRITE_STATE_REP = (byte) 0x89;
	/**
	 * 读节点状态回复指令码
	 */
	public static final byte CMD_READ_NODE_STATE_REP = (byte) 0x8A;
	/**
	 * 新节点上报指令码
	 */
	public static final byte CMD_NEW_NODE_REPORT = (byte) 0x8B;

	/**
	 * 未知指令码
	 */
	public static final byte CMD_UNKNOWN = 0x00;

	/**
	 * 指令对象转换为二进制编码时，除去数据区的长度后剩余的指令长度。
	 */
	public static final int LENGTH_WITHOUT_DATA = 9;

	/**
	 * 广播地址
	 */
	public static final byte[] BROADCAST_ADDR = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
			(byte) 0xAA };

	/**
	 * 灯状态开
	 */
	private static final byte LIGHT_STATE_ON = 0x01;
	/**
	 * 灯状态关
	 */
	private static final byte LIGHT_STATE_OFF = 0x00;

	/**
	 * 信息域生产器
	 */
	private static byte[] InfoGenerater;

	/**
	 * 指令长度
	 */
	private int len;
	/**
	 * 信息域
	 */
	private byte[] info;
	/**
	 * 指令码
	 */
	private byte command;
	/**
	 * 指令数据区
	 */
	private byte[] data;
	/**
	 * 指令校验码
	 */
	private byte checkSum;

	static {
		if (InfoGenerater == null) {
			InfoGenerater = new byte[4];
		}
	}

	/**
	 * 生成获取节点数量指令的数据区
	 * 
	 * @return
	 */
	public static byte[] generateGeNodeNumCommandData() {
		return null;
	}

	/**
	 * 生成获取节点信息指令的数据区
	 * 
	 * @param index
	 *            起始地址
	 * @param num
	 *            查询数量，不能超过15个
	 * @return 节点数据区二进制代码
	 */
	public static byte[] generateGeNodeMsgCommandData(int index, int num) {
		byte[] bytes = new byte[3];
		bytes[0] = (byte) (index & 0xFF);
		bytes[1] = (byte) ((index >> 8) & 0xFF);
		if (num < 0x0F) {
			bytes[2] = (byte) num;
		} else {
			bytes[2] = 0x0F;
		}

		return bytes;
	}

	/**
	 * 生成写节点状态指令的数据区
	 * @param nodeAddr 节点地址
	 * @param light1State	主灯状态
	 * @param light2State	辅灯状态
	 * @param light1PowerPercent	主灯功率百分比
	 * @param light2PowerPercent	辅灯功率百分比
	 * @return	写节点状态指令的数据区二进制代码
	 */
	public static byte[] GenerateWriteNodeStateCommandData(String nodeAddr, String light1State, String light2State,
			String light1PowerPercent, String light2PowerPercent) {
		byte[] bytes = new byte[10];
		byte[] addr = SocketCommand.parseHexStringToBytes(nodeAddr);
		System.arraycopy(addr, 0, bytes, 0, 6);// addr
		if ("on".equals(light1State)) {
			bytes[6] = LIGHT_STATE_ON;
		} else {
			bytes[6] = LIGHT_STATE_OFF;
		}
		bytes[7] = (byte) (Integer.parseInt(light1PowerPercent));
		if ("on".equals(light2State)) {
			bytes[8] = LIGHT_STATE_ON;
		} else {
			bytes[8] = LIGHT_STATE_OFF;
		}
		bytes[9] = (byte) (Integer.parseInt(light2PowerPercent));
		return bytes;
	}

	/**
	 * 生成广播写节点状态指令的数据区
	 * @param light1State	主灯状态
	 * @param light2State	辅灯状态
	 * @param light1PowerPercent	主灯功率百分比
	 * @param light2PowerPercent	辅灯功率百分比
	 * @return	广播写节点状态指令的数据区二进制代码
	 */
	public static byte[] GenerateBroadcastWriteStateCommandData(String light1State, String light2State,
			String light1PowerPercent, String light2PowerPercent) {
		byte[] bytes = new byte[10];
		System.arraycopy(BROADCAST_ADDR, 0, bytes, 0, 6);// broadcast addr
		if ("on".equals(light1State)) {
			bytes[6] = LIGHT_STATE_ON;
		} else {
			bytes[6] = LIGHT_STATE_OFF;
		}
		bytes[7] = (byte) (Integer.parseInt(light1PowerPercent));
		if ("on".equals(light2State)) {
			bytes[8] = LIGHT_STATE_ON;
		} else {
			bytes[8] = LIGHT_STATE_OFF;
		}
		bytes[9] = (byte) (Integer.parseInt(light2PowerPercent));
		return bytes;
	}

	/**
	 * 生成开启节点主动注册指令的数据区
	 * @param startTime 开启时间
	 * @param keepMinutes 持续时长，分钟
	 * @return 开启节点主动注册指令的数据区二进制代码
	 */
	public static byte[] GenerateNodesRegisterOpenCommandData(Date startTime, int keepMinutes) {
		byte[] bytes = new byte[8];
		bytes[0] = Protocol3762Handler.parseByteTo8421Code((byte) (((startTime.getYear() + 1900) % 100) & 0xFF));
		bytes[1] = Protocol3762Handler.parseByteTo8421Code((byte) ((startTime.getMonth() + 1) & 0xFF));
		bytes[2] = Protocol3762Handler.parseByteTo8421Code((byte) (startTime.getDate() & 0xFF));
		bytes[3] = Protocol3762Handler.parseByteTo8421Code((byte) (startTime.getHours() & 0xFF));
		bytes[4] = Protocol3762Handler.parseByteTo8421Code((byte) (startTime.getMinutes() & 0xFF));
		bytes[5] = Protocol3762Handler.parseByteTo8421Code((byte) (startTime.getSeconds() & 0xFF));
		byte[] temp = Protocol3762Handler.parseIntegerTo8421Code(keepMinutes, 2, false);
		bytes[6] = (byte) (temp[0] & 0xFF);
		bytes[7] = (byte) (temp[1] & 0xFF);
		return bytes;
	}
	/**
	 * 生成读节点信息指令的数据区
	 * @param nodeAddr 节点地址
	 * @return 读节点信息指令的数据区二进制代码
	 */
	public static byte[] GenerateReadNodeStateCommandData(String nodeAddr) {
		byte[] bytes = new byte[6];
		byte[] addr = SocketCommand.parseHexStringToBytes(nodeAddr);
		System.arraycopy(addr, 0, bytes, 0, 6);
		return bytes;
	}

	/**
	 * 将二进制的指令封装成SocketCommand对象
	 * @param bytes 二进制指令
	 * @param len 指令长度
	 * @return SocketCommand对象
	 */
	public static SocketCommand parseSocketCommand(byte[] bytes, int len) {
		SocketCommand sc = new SocketCommand();
		int temp = Integer.parseInt(Integer.toHexString(bytes[1] & 0xFF), 16);
		if (bytes[0] == HEADER && bytes[len - 1] == TAIL && len == temp) {// 头尾正确&&长度正确
			// sc.setLen(bytes[1]);
			byte info[] = new byte[4];
			System.arraycopy(bytes, 2, info, 0, 4);
			sc.setInfo(info);
			sc.setCommand(bytes[6]);
			int dataLen = len - LENGTH_WITHOUT_DATA;
			if (dataLen > 0) {
				byte[] b = new byte[dataLen];
				System.arraycopy(bytes, 7, b, 0, dataLen);
				sc.setData(b);
			}
			// System.out.println(SocketCommand.parseBytesToHexString(sc.tobyteArray(),
			// sc.getLen()));
			if (sc.getCheckSum() == bytes[len - 2]) {
				return sc;
			} else {
				System.out.println("校验不正确");
			}
		} else {
			System.out.println("首位或长度不正确");
		}
		return null;
	}

	/**
	 * 将二进制数据转换为hex字符串
	 * @param bytes 二进制数据
	 * @param length 要转换的数据长度
	 * @return hex字符串
	 */
	public static String parseBytesToHexString(byte[] bytes, int length) {
		StringBuffer sb = new StringBuffer();
		String temp;
		for (int i = 0; i < length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				sb.append("0" + temp);
			} else if (temp.length() == 2) {
				sb.append(temp);
			}
		}
		return sb.toString();
	}

	/**
	 * 将二进制数据转换为hex字符串
	 * @param bytes	二进制数据
	 * @param start	起始位置
	 * @param length 要转换的数据长度
	 * @return hex字符串
	 */
	public static String parseBytesToHexString(byte[] bytes, int start, int length) {
		StringBuffer sb = new StringBuffer();
		String temp;
		for (int i = start; i < start + length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				sb.append("0" + temp);
			} else if (temp.length() == 2) {
				sb.append(temp);
			}
		}
		return sb.toString();
	}

	/**
	 * 将hex字符串转换为byte数组
	 * @param hexString hex字符串
	 * @return 二进制byte数组
	 */
	public static byte[] parseHexStringToBytes(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16) & 0xFF);
		}
		return bytes;
	}
	
	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public int getLen() {
		if (this.data == null || (this.data != null && this.data.length == 0)) {
			this.setLen(LENGTH_WITHOUT_DATA);
		} else {
			this.setLen(LENGTH_WITHOUT_DATA + this.data.length);
		}
		return len;
	}

	private void setLen(int len) {
		this.len = len;
	}

	private void setLen(byte len) {
		this.len = Integer.parseInt(Integer.toHexString(len & 0xFF), 16);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte getCheckSum() {
		byte sum = (byte) (HEADER + (this.getLen() & 0xFF));
		if (this.getInfo() != null) {
			for (byte b : this.getInfo()) {
				sum = (byte) (sum + b);
			}
		}
		sum = (byte) (sum + this.getCommand());
		if (this.getData() != null && this.getData().length > 0) {
			for (byte byte1 : this.getData()) {
				sum = (byte) (sum + byte1);
			}
		}
		checkSum = sum;
		return checkSum;
	}

	/**
	 * 将指令对象转换为指令byte数组
	 * 
	 * @return 二进制的指令数组
	 */
	public byte[] tobyteArray() {
		byte[] bytes = new byte[this.getLen()];
		bytes[0] = HEADER;
		bytes[1] = (byte) (this.getLen() & 0xFF);
		System.arraycopy(this.getInfo(), 0, bytes, 2, 4);
		bytes[6] = this.getCommand();
		if (this.getLen() > LENGTH_WITHOUT_DATA && this.getData() != null) {
			System.arraycopy(this.getData(), 0, bytes, 7, this.getLen() - LENGTH_WITHOUT_DATA);
		}
		bytes[bytes.length - 2] = this.getCheckSum();
		bytes[bytes.length - 1] = TAIL;
		return bytes;
	}

	@Override
	public String toString() {
		return SocketCommand.parseBytesToHexString(this.tobyteArray(), this.getLen());
	}

	private byte[] generateNextInfo() {
		InfoGenerater[3] += 0x01;
		if (InfoGenerater[3] == 0x00) {
			InfoGenerater[2] += 0x01;
			if (InfoGenerater[2] == 0x00) {
				InfoGenerater[1] += 0x01;
				if (InfoGenerater[1] == 0x00) {
					InfoGenerater[0] += 0x01;
				}
			}
		}
		byte[] result = new byte[4];
		System.arraycopy(InfoGenerater, 0, result, 0, 4);
		return result;
	}

	public byte[] getInfo() {
		if (this.info == null) {
			this.info = generateNextInfo();
		}
		return info;
	}

	public void setInfo(byte[] info) {
		if (this.info == null) {
			this.info = new byte[4];
		}
		System.arraycopy(info, 0, this.info, 0, 4);
	}

}
