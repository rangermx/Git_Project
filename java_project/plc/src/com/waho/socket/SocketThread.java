package com.waho.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * socket 线程类
 * 
 * @author zhangzhongwen
 * 
 */
public class SocketThread extends Thread {
	private ServerSocket serverSocket = null;

	public SocketThread(ServerSocket serverScoket) {
		try {
			if (null == serverSocket) {
				this.serverSocket = new ServerSocket(7002);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		while (!this.isInterrupted()) {
			try {
				
				Socket socket = serverSocket.accept();
				
				if (socket != null && !socket.isClosed()) {
					// 处理接受的数据
					new SocketOperate(socket).start();
//					System.out.println("socket start......");
//					socket.setSoTimeout(10 * 1000 * 1);//链接超时时长，单位毫秒，0为死等
				}

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
