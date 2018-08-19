package com.waho.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.waho.domain.Device;
import com.waho.socket.util.CmdBroadcastHandler;
import com.waho.socket.util.CmdCommuncateHandler;
import com.waho.socket.util.CmdControlHandler;
import com.waho.socket.util.CmdHeartbeatHandler;
import com.waho.socket.util.CmdMainNodeMsgHandler;
import com.waho.socket.util.CmdNodeMsgHandler;
import com.waho.socket.util.CmdReadmacHandler;
import com.waho.socket.util.CmdUnknownHandler;
import com.waho.socket.util.SocketDataHandler;

/**
 * 多线程处理socket接收的数据
 * 
 * @author zhangzhongwen
 * 
 */
public class SocketOperate extends Thread {
	
	private static CmdUnknownHandler CUH;
	private static CmdNodeMsgHandler CNH;
	private static CmdMainNodeMsgHandler CNMH;
	private static CmdBroadcastHandler CBH;
	private static CmdReadmacHandler CRH;
	private static CmdHeartbeatHandler CHH;
	private static CmdCommuncateHandler CMH;
	private static CmdControlHandler CCH;

	private Socket socket;

	private Device device;

	static {
		// 责任链设计模式
		CUH = CmdUnknownHandler.getInstance(null);// 处理未知指令
		CNH = CmdNodeMsgHandler.getInstance(CUH);// 处理从节点消息
		CNMH = CmdMainNodeMsgHandler.getInstance(CNH);// 处理主节点消息
		CBH = CmdBroadcastHandler.getInstance(CNMH);// 处理广播回复
		CRH = CmdReadmacHandler.getInstance(CBH);// 处理读mac地址回复
		CHH = CmdHeartbeatHandler.getInstance(CRH);// 处理心跳包
		CMH = CmdCommuncateHandler.getInstance(CHH);// 处理从节点通讯指令
		CCH = CmdControlHandler.getInstance(CMH);// 处理主节点控制指令
	}

	public SocketOperate(Socket socket) {
		this.socket = socket;
		this.device = new Device();
	}

	public void run() {

		InputStream in = null;
		OutputStream out = null;
		int timeCount = 0;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			byte[] temp = new byte[1024];
			int length = 0;
			while (!socket.isClosed()) {
				// 读取客户端发送的信息
				if ((length = in.read(temp)) != -1) {
					timeCount = 0;
					// 解析责任链，任务开始
					CCH.socketDataHandle(temp, length);
					if (length > 0) {
						System.out.write(temp, 0, length);
						out.write(temp, 0, length);
					}
				}
				// 读用户发送的指令
				SocketDataHandler.UserMessageHandle(device, out);

				try {
					sleep(1 * 1000);
					if (timeCount++ > 10) {
						socket.close();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			System.out.println("socket stop!");
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}