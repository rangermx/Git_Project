package com.waho.domain;

public class SocketCommand {
	public static final byte[] HEADER = { (byte) 0xFB, (byte) 0xFB };
	public static final byte[] TAIL = { (byte) 0xFE, (byte) 0xFE };

	public static final byte CMD_CONTROL = 0x01;
	public static final byte CMD_COMMUNCATE = 0x02;
	public static final byte CMD_HEARTBEAT_REP = 0x03;
	public static final byte CMD_READ_MAC_REP = 0x04;
	public static final byte CMD_BROADCAST = 0x05;
	public static final byte CMD_MAIN_NODE_MSG_REP = 0x06;
	public static final byte CMD_NODE_MSG_REP = 0x07;
	
	public static final byte CMD_CONTROL_REP = 0x41;
	public static final byte CMD_COMMUNCATE_REP = 0x42;
	public static final byte CMD_HEARTBEAT = 0x43;
	public static final byte CMD_READ_MAC = 0x44;
	public static final byte CMD_BROADCAST_REP = 0x45;
	public static final byte CMD_MAIN_NODE_MSG = 0x46;
	public static final byte CMD_NODE_MSG = 0x47;

	public static final byte CMD_UNKNOWN = 0x00;

	private byte command;
	private int dataLen;
	private byte[] data;
	private byte checkSum;
	/**
	 * 将接收到的客户端指令byte数组，转换为指令对象
	 * @param bytes
	 * @param len
	 * @return
	 */
	public static SocketCommand parseSocketCommand(byte[] bytes, int len) {
		SocketCommand sc = new SocketCommand();
		if (bytes[0] == HEADER[0] && bytes[1] == HEADER[1] && bytes[len - 2] == TAIL[0] && bytes[len - 1] == TAIL[1]) {//头尾正确
			sc.setCommand(bytes[2]);
			sc.setDataLen(bytes[3]);
			byte[] b = new byte[sc.getDataLen()];
			System.arraycopy(bytes, 4, b, 0, sc.getDataLen());
			sc.setData(b);
			if (sc.getCheckSum() == bytes[len - 3]) {
				return sc;
			}
//			System.out.println("校验错");
//			System.out.println(sc.getCheckSum());
//			System.out.println(bytes[len - 3]);
		}
		return null;
	}
	
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
	
	public static byte[] parseHexStringToBytes(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte)(Integer.parseInt(hexString.substring(i*2, i*2+2)) & 0xFF);
		}
		return bytes;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public int getDataLen() {
		return dataLen;
	}

	public void setDataLen(int dataLen) {
		this.dataLen = dataLen;
	}
	
	public void setDataLen(byte dataLen) {
		this.dataLen = Integer.parseInt(Integer.toHexString(dataLen & 0xFF), 16);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte getCheckSum() {
		byte sum = (byte) (HEADER[0] + HEADER[1] + this.getCommand());
		sum = (byte) (sum + (this.getDataLen() & 0xFF));
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
	 * @return
	 */
	public byte[] tobyteArray() {
		byte[] bytes = new byte[this.getDataLen() + 7];
		System.arraycopy(HEADER, 0, bytes, 0, 2);
		bytes[2] = this.getCommand();
		if (this.getDataLen() >= 0 && this.getData() != null) {
			bytes[3] = (byte)(this.getDataLen() & 0xFF);
			System.arraycopy(this.getData(), 0, bytes, 4, this.getDataLen());
		} else {
			bytes[3] = (byte)(this.getDataLen() & 0xFF);
		}
		bytes[bytes.length - 3] = this.getCheckSum();
		System.arraycopy(TAIL, 0, bytes, bytes.length - 2, 2);
		return bytes;
	}

	@Override
	public String toString() {
		return this.tobyteArray().toString();
	}

}
