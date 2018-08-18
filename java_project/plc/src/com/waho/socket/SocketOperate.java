package com.waho.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.waho.domain.Device;
import com.waho.socket.util.ControlCmdHandler;
import com.waho.socket.util.SocketDataHandler;

/**
 * 多线程处理socket接收的数据
 * 
 * @author zhangzhongwen
 * 
 */
public class SocketOperate extends Thread {

	private Socket socket;
	
	private Device device;

	public SocketOperate(Socket socket) {
		this.socket = socket;
		this.device = new Device();
	}

	public void run() {

		InputStream in = null;
		OutputStream out = null;
		int timeCount = 0;
		// 责任链设计模式
		ControlCmdHandler cch = new ControlCmdHandler(null);
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
					cch.socketDataHandle(temp, length);
					System.out.println(temp.toString());
					out.write(temp);
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