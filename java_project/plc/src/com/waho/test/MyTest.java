package com.waho.test;

import org.junit.Test;

import com.waho.dao.UserMessageDao;
import com.waho.dao.impl.UserMessageDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.UserMessage;

public class MyTest {
	@Test
	public void test1() {
		UserMessageDao dao = new UserMessageDaoImpl();
		Device device = new Device();
		device.setDeviceMac("device1");
		try {
			UserMessage um = dao.selectUserLastUserMessageByDevice(device);
			System.out.println(um);
			System.out.println(um.getCommand());
			System.out.println(um.getDataLen());
			int v = 0;
			for (int i = 0; i < um.getDataLen(); i++) {
				v = um.getData()[i] & 0xFF;
				System.out.print("0x" + Integer.toHexString(v) + " ");
			}
			System.out.println();
			System.out.println(Integer.toHexString(um.getCheckSum() & 0xFF));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
