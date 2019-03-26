package com.waho.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.waho.dao.DeviceDao;
import com.waho.dao.impl.DeviceDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.SocketCommand;
import com.waho.socket.state.SocketState;
import com.waho.socket.state.impl.RegisterState;
import com.waho.socket.util.CmdBroadcastWriteStateHandler;
import com.waho.socket.util.CmdWriteNodeStateHandler;
import com.waho.socket.util.CmdNodeRegOpenHandler;
import com.waho.socket.util.CmdHeartbeatHandler;
import com.waho.socket.util.CmdNewNodeReportHandler;
import com.waho.socket.util.CmdReadNodeStateHandler;
import com.waho.socket.util.CmdReadMacHandler;
import com.waho.socket.util.CmdUnknownHandler;

/**
 * 多线程处理socket接收的数据
 * 
 * @author zhangzhongwen
 * 
 */
public class SocketOperate extends Thread {

	/**
	 * 未知指令处理
	 */
	private static CmdUnknownHandler CUH;
	/**
	 * 读节点状态指令处理
	 */
	private static CmdReadNodeStateHandler CRNSH;
	/**
	 * 新节点上报指令处理
	 */
	private static CmdNewNodeReportHandler CNNRH;
	/**
	 * 广播写节点状态指令处理
	 */
	private static CmdBroadcastWriteStateHandler CBWSH;
	/**
	 * 读节点mac地址指令处理
	 */
	private static CmdReadMacHandler CRMH;
	/**
	 * 心跳包指令处理
	 */
	private static CmdHeartbeatHandler CHH;
	/**
	 * 写节点状态指令处理
	 */
	private static CmdWriteNodeStateHandler CWNSH;
	/**
	 * 开启节点注册指令处理
	 */
	private static CmdNodeRegOpenHandler CNROH;
	private static DeviceDao SocketDeviceDao;

	/**
	 * socekt长连接无数据保持时间，超时则主动断开连接
	 */
	private static final int KEEP_ONLINE_TIME = 30;
	/**
	 * 线程执行周期
	 */
	private static final int THREAD_SLEEP_TIME = 1;

	private Socket socket;

	/**
	 * socket连接的集控器信息，作为socket的唯一标识
	 */
	private Device device;

	/**
	 * 读数据线程
	 */
	private Thread readThread;

	/**
	 * 长连接无数据时长计时
	 */
	private int timeCount = 0;

	/**
	 * log4j日志对象
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 接收到的指令，经过粘包切割处理后的数据对象列表
	 */
	private LinkedList<byte[]> cmdList = new LinkedList<>();

	/**
	 * 将所有数据处理方法，以责任链方式，首位相接连接起来
	 */
	static {
		// 责任链设计模式
		CUH = CmdUnknownHandler.getInstance(null);// 处理未知指令
		CRNSH = CmdReadNodeStateHandler.getInstance(CUH);// 处理读节点信息回复指令
		CNNRH = CmdNewNodeReportHandler.getInstance(CRNSH);// 处理发现从节点上报指令
		CBWSH = CmdBroadcastWriteStateHandler.getInstance(CNNRH);// 处理广播写状态回复指令
		CRMH = CmdReadMacHandler.getInstance(CBWSH);// 处理上报mac地址指令
		CHH = CmdHeartbeatHandler.getInstance(CRMH);// 处理心跳包指令
		CWNSH = CmdWriteNodeStateHandler.getInstance(CHH);// 处理写节点状态回复指令
		CNROH = CmdNodeRegOpenHandler.getInstance(CWNSH);// 处理开启节点主动注册回复指令
		SocketDeviceDao = new DeviceDaoImpl();
	}

	public SocketOperate(Socket socket) {
		this.socket = socket;
		this.device = new Device();
	}
	
	/**
	 * 数据粘包处理，由于接受数据时，很可能多帧数据合并为一帧收到，所以要将多帧数据切割开
	 * @param list 接受切割后数据的链表
	 * @param data 数据
	 * @param length 数据长度
	 */
	private void dataCut(LinkedList<byte[]> list, byte[] data, int length) {
		if (data[0] == SocketCommand.HEADER) {
			int len = (data[1] & 0xFF);
			if (len <= length) {
				list.addLast(new byte[len]);
				System.arraycopy(data, 0, cmdList.getLast(), 0, len);
				logger.info(device.getDeviceMac()+ ": " + SocketCommand.parseBytesToHexString(data, length));
				if (len < length) {
					int newDataLen = length - len;
					byte[] newData = new byte[newDataLen];
					System.arraycopy(data, len, newData, 0, newDataLen);
					dataCut(list, newData, newDataLen);
				}
				return;
			} else {
				logger.info("error length: " + SocketCommand.parseBytesToHexString(data, length));
			}
		} else {
			logger.info("error header: " + SocketCommand.parseBytesToHexString(data, length));
		}
	}

	public void run() {
//		System.out.println("socket start!");
		logger.info("socket start!");
		OutputStream out = null;
		SocketState state = RegisterState.getInstance();
		try {
			out = socket.getOutputStream();
			this.readThread = new Thread() {
				public void run() {
					byte[] temp = new byte[1024];
					int length = 0;
					InputStream in = null;
					try {
						in = socket.getInputStream();
						while (!socket.isClosed()) {
							// 读取客户端发送的信息
							if ((length = in.read(temp)) != -1) {
								timeCount = 0;
								// 添加数据粘包处理
								dataCut(cmdList, temp, length);
//								cmdList.addLast(new byte[length]);
//								System.arraycopy(temp, 0, cmdList.getLast(), 0, length);
//								logger.info(device.getDeviceMac()+ ": " + SocketCommand.parseBytesToHexString(temp, length));
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				}

			};
			this.readThread.start();
			byte[] temp;
			SocketState conversion;
			while (!socket.isClosed()) {

				while (!cmdList.isEmpty()) {
					temp = cmdList.getFirst();
					if (temp != null) {
						// 读设备发送的指令
						conversion = state.clientDataHandle(temp, temp.length, CNROH, device, out);
						if (conversion != null) {
							state = conversion;
							conversion = null;
						}
						// 处理完数据后从列表中移除
						cmdList.removeFirst();
						temp = null;
					}
				}

				// 读用户发送的指令
				// SocketDataHandler.UserMessageHandle(device, out);
				state.userMsgHanle(device, out);

				try {
					sleep(1000 * THREAD_SLEEP_TIME);
					if (timeCount++ > KEEP_ONLINE_TIME) {
						socket.close();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 将设备状态更新至离线
			if (device.getDeviceMac() != null) {
				device.setOnline(false);
				try {
					SocketDeviceDao.updateDeviceOnline(device);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			System.out.println("socket stop!");
			logger.info("socket stop!");
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.readThread.stop();
		}
	}
}