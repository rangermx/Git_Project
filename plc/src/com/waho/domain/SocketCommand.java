package com.waho.domain;

public class SocketCommand {
	public static final byte[] HEADER = { (byte) 0xFB, (byte) 0xFB };
	public static final byte[] TAIL = { (byte) 0xFE, (byte) 0xFE };

	public static final byte CMD_CONTROL = 0x01;
	public static final byte CMD_COMMUNCATE = 0x02;
	public static final byte CMD_HEARTBEAT = 0x03;
	public static final byte CMD_READ_MAC = 0x04;
	public static final byte CMD_BROADCAST = 0x05;
	public static final byte CMD_MAIN_NODE_MSG = 0x06;
	public static final byte CMD_NODE_MSG = 0x07;

	public static final byte CMD_UNKNOWN = 0x00;

	private byte command;
	private byte dataLen;
	private byte[] data;
	private byte checkSum;

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
		}
		return null;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public byte getDataLen() {
		return dataLen;
	}

	public void setDataLen(byte dataLen) {
		this.dataLen = dataLen;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte getCheckSum() {
		byte sum = (byte) (HEADER[0] + HEADER[1] + this.getCommand() + this.getDataLen());
		for (byte byte1 : this.getData()) {
			sum = (byte) (sum + byte1);
		}
		checkSum = sum;
		return checkSum;
	}

	public byte[] tobyteArray() {
		byte[] bytes = new byte[this.getDataLen() + 7];
		System.arraycopy(HEADER, 0, bytes, 0, 2);
		bytes[2] = this.getCommand();
		bytes[3] = this.getDataLen();
		System.arraycopy(this.getData(), 0, bytes, 4, this.getDataLen());
		bytes[bytes.length - 3] = this.getCheckSum();
		System.arraycopy(TAIL, 0, bytes, bytes.length - 2, 2);
		return bytes;
	}

	@Override
	public String toString() {
		return this.tobyteArray().toString();
	}

}
