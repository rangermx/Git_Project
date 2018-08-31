package com.waho.util;

import java.util.Map;

import com.waho.domain.SocketCommand;

public class Protocol3762Handler {
	/**
	 * 获取376.2指令的AFN码
	 * @param sc
	 * @return
	 */
	public static byte getAFNCode(SocketCommand sc) {
		
		return 0;
	}
	/**
	 * 获取376.2指令的F码
	 * @param sc
	 * @return
	 */
	public static byte getFCode(SocketCommand sc) {
		return 0;
	}
	/**
	 * 解析数据，返回处理结果
	 * @param sc
	 * @return
	 */
	public static Map<String, String> DataHandle(SocketCommand sc) {
		return null;
	}
}
