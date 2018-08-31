package com.waho.util;

public class Protocol645Handler {
	private static final byte HEADER = 0x68;
	private static final byte FOOTER = 0x16;

	private static final byte CMD_WRITE_DATA = 0x14;

	private static final byte[] WRITE_DOUBLE_STATE = { 0x34, 0x34, 0x23, 0x43 };

	private static final byte LIGHT_STATE_ON = 0x34;
	private static final byte LIGHT_STATE_OFF = 0x33;
	
	/**
	 * 生成广播写地址指令
	 * @param nodeAddr
	 * @return
	 */
	public static byte[] GenerateBroadcastWriteAddr645Cmd(String nodeAddr) {
		byte[] buffer = new byte[20];
		
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
		byte[] addr = nodeAddr.getBytes();
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
}
