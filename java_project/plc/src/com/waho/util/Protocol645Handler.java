package com.waho.util;

import java.util.Arrays;

import com.waho.domain.Node;
import com.waho.domain.SocketCommand;

public class Protocol645Handler {
	private static final byte HEADER = 0x68;
	private static final byte FOOTER = 0x16;

	private static final byte CMD_WRITE_DATA = 0x14;
	private static final byte CMD_WRITE_DATA_REP = (byte) 0x94;
	private static final byte CMD_READ_DATA = 0x11;
	private static final byte CMD_READ_DATA_REP = (byte) 0x91;

	private static final byte[] WRITE_DOUBLE_STATE = { 0x34, 0x34, 0x23, 0x43 };
	private static final byte[] READ_DOUBLE_STATE = { 0x34, 0x33, (byte) 0xFF, 0x34 };

	private static final byte LIGHT_STATE_ON = 0x34;
	private static final byte LIGHT_STATE_OFF = 0x33;

	/**
	 * 生成广播写地址指令
	 * 
	 * @param nodeAddr
	 * @return
	 */
	public static byte[] GenerateBroadcastWriteAddr645Cmd(String nodeAddr) {
		// byte[] buffer = new byte[20];

		return null;
	}

	/**
	 * 生成节点单灯控制指令
	 * 
	 * @param nodeAddr
	 *            节点地址
	 * @param light1State
	 *            主灯状态
	 * @param light2State
	 *            辅灯状态
	 * @param light1PowerPercent
	 *            主灯功率
	 * @param light2PowerPercent
	 *            辅灯功率
	 * @return
	 */
	public static byte[] GenerateNodeControl645Cmd(String nodeAddr, String light1State, String light2State,
			String light1PowerPercent, String light2PowerPercent) {
		byte[] buffer = new byte[20];
		buffer[0] = HEADER;
		byte[] addr = SocketCommand.parseHexStringToBytes(nodeAddr);
		System.arraycopy(addr, 0, buffer, 1, addr.length);// addr
		buffer[7] = HEADER;
		buffer[8] = CMD_WRITE_DATA;// cmd
		buffer[9] = 0x08;// length
		System.arraycopy(WRITE_DOUBLE_STATE, 0, buffer, 10, 4);// data_flag
		if ("on".equals(light1State)) {
			buffer[14] = LIGHT_STATE_ON;
		} else {
			buffer[14] = LIGHT_STATE_OFF;
		}
		buffer[15] = (byte) (Integer.parseInt(light1PowerPercent) + 0x33);
		if ("on".equals(light2State)) {
			buffer[16] = LIGHT_STATE_ON;
		} else {
			buffer[16] = LIGHT_STATE_OFF;
		}
		buffer[17] = (byte) (Integer.parseInt(light2PowerPercent) + 0x33);
		buffer[18] = GetCS(buffer, 0, 18);
		buffer[19] = FOOTER;
		return buffer;
	}

	/**
	 * 生成刷新节点信息指令（读节点状态、功率）
	 * 
	 * @param nodeAddr
	 * @return
	 */
	public static byte[] GenerateNodeRefresh645Cmd(String nodeAddr) {
		byte[] buffer = new byte[16];
		buffer[0] = HEADER;
		byte[] addr = SocketCommand.parseHexStringToBytes(nodeAddr);
		System.arraycopy(addr, 0, buffer, 1, addr.length);
		buffer[7] = HEADER;
		buffer[8] = CMD_READ_DATA;
		buffer[9] = 0x04;
		System.arraycopy(READ_DOUBLE_STATE, 0, buffer, 10, 4);
		buffer[14] = GetCS(buffer, 0, 14);
		buffer[15] = FOOTER;
		return buffer;
	}

	/**
	 * 生成广播控制指令
	 * 
	 * @param light1State
	 * @param light2State
	 * @param light1PowerPercent
	 * @param light2PowerPercent
	 * @return
	 */
	public static byte[] GenerateBroadcastControl645Cmd(String light1State, String light2State,
			String light1PowerPercent, String light2PowerPercent) {
		byte[] buffer = new byte[20];
		buffer[0] = HEADER;
		byte[] addr = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA };
		System.arraycopy(addr, 0, buffer, 1, addr.length);// addr
		buffer[7] = HEADER;
		buffer[8] = CMD_WRITE_DATA;// cmd
		buffer[9] = 0x08;// length
		System.arraycopy(WRITE_DOUBLE_STATE, 0, buffer, 10, 4);// data_flag
		if ("on".equals(light1State)) {
			buffer[14] = LIGHT_STATE_ON;
		} else {
			buffer[14] = LIGHT_STATE_OFF;
		}
		buffer[15] = (byte) (Integer.parseInt(light1PowerPercent) + 0x33);
		if ("on".equals(light2State)) {
			buffer[16] = LIGHT_STATE_ON;
		} else {
			buffer[16] = LIGHT_STATE_OFF;
		}
		buffer[17] = (byte) (Integer.parseInt(light2PowerPercent) + 0x33);
		buffer[18] = GetCS(buffer, 0, 18);
		buffer[19] = FOOTER;
		return buffer;
	}

	public static byte GetCS(byte[] src, int srcPos, int length) {
		byte sum = 0x00;
		for (int i = 0; i < length; i++) {
			sum += src[srcPos + i];
		}
		return sum;
	}

	/**
	 * 将645指令信息解析封装成节点对象
	 * 
	 * @param src
	 * @return
	 */
	public static Node Transform645CmdToNode(byte[] src) {
		
		Node result = null;
		if (src[0] != HEADER || src[src.length - 1] != FOOTER)
			return null;// 头尾错
		if (src[src.length - 2] != GetCS(src, 0, src.length - 2))
			return null;// 校验错
		// 读指令
		byte[] data_flag = new byte[4];
		System.arraycopy(src, 10, data_flag, 0, 4);
		switch (src[8]) {
		case CMD_WRITE_DATA:
			if (Arrays.equals(WRITE_DOUBLE_STATE, data_flag)) {// 写数据
				result = new Node();
				byte temp = (byte) (src[14] - 0x33);
				if (temp == 0x01) {
					result.setLight1State(true);
				} else if (temp == 0x00) {
					result.setLight1State(false);
				}
				temp = (byte) (src[15] - 0x33);
				result.setLight1PowerPercent(temp & 0xFF);

				temp = (byte) (src[16] - 0x33);
				if (temp == 0x01) {
					result.setLight2State(true);
				} else if (temp == 0x00) {
					result.setLight2State(false);
				}
				temp = (byte) (src[17] - 0x33);
				result.setLight2PowerPercent(temp & 0xFF);
			}
			break;
		case CMD_READ_DATA_REP:
			if (Arrays.equals(READ_DOUBLE_STATE, data_flag)) {// 读数据回复
				byte[] power = new byte[2];
				power[0] = (byte) (src[15] - 0x33);
				power[1] = (byte) (src[14] - 0x33);
				int powerInt = Integer.parseInt(SocketCommand.parseBytesToHexString(power, 2), 16);
				result = new Node();
				result.setNodeAddr(SocketCommand.parseBytesToHexString(src, 1, 6));
				result.setPower(powerInt);
				byte temp = (byte) (src[16] - 0x33);
				if (temp == 0x01) {
					result.setLight1State(true);
				} else if (temp == 0x00) {
					result.setLight1State(false);
				}
				temp = (byte) (src[17] - 0x33);
				result.setLight1PowerPercent(temp & 0xFF);

				temp = (byte) (src[18] - 0x33);
				if (temp == 0x01) {
					result.setLight2State(true);
				} else if (temp == 0x00) {
					result.setLight2State(false);
				}
				temp = (byte) (src[19] - 0x33);
				result.setLight2PowerPercent(temp & 0xFF);
			}
			break;
		case CMD_WRITE_DATA_REP:
			byte[] power = new byte[2];
			power[0] = (byte) (src[11] - 0x33);
			power[1] = (byte) (src[10] - 0x33);
			int powerInt = Integer.parseInt(SocketCommand.parseBytesToHexString(power, 2), 16);
			result = new Node();
			result.setNodeAddr(SocketCommand.parseBytesToHexString(src, 1, 6));
			result.setPower(powerInt);
			byte temp = (byte) (src[12] - 0x33);
			if (temp == 0x01) {
				result.setLight1State(true);
			} else if (temp == 0x00) {
				result.setLight1State(false);
			}
			temp = (byte) (src[13] - 0x33);
			result.setLight1PowerPercent(temp & 0xFF);

			temp = (byte) (src[14] - 0x33);
			if (temp == 0x01) {
				result.setLight2State(true);
			} else if (temp == 0x00) {
				result.setLight2State(false);
			}
			temp = (byte) (src[15] - 0x33);
			result.setLight2PowerPercent(temp & 0xFF);
			break;
		default:
			break;
		}
		return result;
	}
}
