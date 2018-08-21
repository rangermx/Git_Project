package com.test.service.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

/**
 * socket 线程类
 * 
 * @author zhangzhongwen
 * 
 */
public class SocketThread extends Thread {
	private ServerSocket serverSocket = null;
	private Timer timer = null;

	public SocketThread(ServerSocket serverScoket) {
		try {
			if (null == serverSocket) {
				this.serverSocket = new ServerSocket(7001);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		timer = new Timer();
		timer.schedule(new PloyThread(), 10, 20 * 1000);
		while (!this.isInterrupted()) {
			try {
				DeviceSocket devSocket = new DeviceSocket();
				Socket socket = serverSocket.accept();
				
				if (socket != null) {
					devSocket.setSocket(socket);
				}

				if (null != devSocket.getSocket() && !devSocket.getSocket().isClosed()) {
					// 处理接受的数据
					new SocketOperate(devSocket).start();
					SocketTool.socketList.add(devSocket);
					System.out.println("socket start......");
				}
				devSocket.getSocket().setSoTimeout(60 * 1000 * 1);//链接超时时长，单位毫秒，0为死等
//				devSocket.getSocket().setSoTimeout(0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void closeSocketServer() {
		try {
			if (null != serverSocket && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
