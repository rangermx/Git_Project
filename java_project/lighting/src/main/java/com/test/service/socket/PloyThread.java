package com.test.service.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.test.domain.Ploy;
import com.test.domain.PloyOperate;

public class PloyThread  extends TimerTask{
	
	private Date currentTime;
	private SocketCmd sc;
	private Logger log = Logger.getLogger("D");
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 0、获取当前UTC时间、时、分
		currentTime = new Date();
		long times = currentTime.getTime();
//		times = currentTime.getTime() - ((long)p.getTimeZone() * 60 * 1000);// 获取时区偏移之后的时间
		long hours = times /1000 / 60 / 60 % 24;
		long minutes = times /1000 / 60 % 60;
		// 1、查询数据库ploy_table，获取正在运行的策略
		ArrayList<Ploy> pList = SocketTool.selectPloyByStatus(1);
		// 2、根据正在运行的策略id，查询playOperate_table，获取到达操作时间的指令
		ArrayList<PloyOperate> operateList;
		for (Ploy p : pList) {
			operateList = SocketTool.selectPloyOperateByPloyid(p.getId());
			if (operateList != null) {
				for (PloyOperate operate : operateList) {
					if (operate.getHours() == hours && operate.getMinutes() == minutes) {// operate的定时时间到了
						System.out.println(hours + " : " + minutes);
						// 3、根据策略绑定的设备或分组，寻找到对应的socket端口号，发送指令。
						if (p.getBindType() == 2) {// 3.1、如果bind_type == 2,绑定的是集控器mac地址
							for (DeviceSocket ds : SocketTool.socketList) {
								if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(p.getBindData())) {//找到对应的socket线程
									sendCmd(ds, operate, p);//发送指令
								}
							}
						} else if (p.getBindType() == 1) {// 3.2、如果bind_type == 1,绑定的是分组id
							for (DeviceSocket ds : SocketTool.socketList) {
								if (ds.getDevice().getUserid() != null && ds.getDevice().getUserid().equals(p.getUserid())) {//找到对应用户的所有socket线程
									sendCmd(ds, operate, p);//发送指令
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void sendCmd(DeviceSocket ds, PloyOperate operate, Ploy ploy) {//根据operate和ploy编辑指令通过ds.socket发送
		sc = new SocketCmd();
		if (ploy.getBindType() == 2) {//集控器绑定，发送广播指令
			if (operate.getOperateType() == 1) {// 开关
				sc.setLength("08");
				sc.setCmd0("29");
				sc.setCmd1("02");
				sc.setAddr_mode("02");
				sc.setAddr_type("FFFF");
				sc.setDst_ep("0A");
				sc.setCluster_id("0006");
				sc.setCmd_type("01");
				if (operate.getOperateParam() == 1) {// 开指令
					sc.setCmd_id("01");
				} else if (operate.getOperateParam() == 0) {// 关指令
					sc.setCmd_id("00");
				}
				sc.createCmdString();
			} else if (operate.getOperateType() == 2) {// 调光
				String brightness = Integer.toHexString(operate.getOperateParam());
				brightness = brightness.toUpperCase();
				sc.setLength("0C");
				sc.setCmd0("29");
				sc.setCmd1("02");
				sc.setAddr_mode("02");
				sc.setAddr_type("FFFF");
				sc.setDst_ep("0A");
				sc.setCluster_id("0008");
				sc.setCmd_type("01");
				sc.setCmd_id("00");
				sc.setPayload(brightness + "000000");
				sc.createCmdString();
			}
			
		} else if (ploy.getBindType() == 1) {//分组绑定，发送组地址指令
			if (operate.getOperateType() == 1) {// 开关
				sc.setLength("08");
				sc.setCmd0("29");
				sc.setCmd1("02");
				sc.setAddr_mode("01");// 地址模式为：组地址模式
				sc.setAddr_type(groupidToAddr(Integer.parseInt(ploy.getBindData())));// 组地址
				sc.setDst_ep("0A");
				sc.setCluster_id("0006");
				sc.setCmd_type("01");
				if (operate.getOperateParam() == 1) {
					sc.setCmd_id("01");
				} else if (operate.getOperateParam() == 0) {
					sc.setCmd_id("00");
				}
				sc.createCmdString();
			} else if (operate.getOperateType() == 2) {// 调光
				sc.setLength("0C");
				sc.setCmd0("29");
				sc.setCmd1("02");
				sc.setAddr_mode("01");// 地址模式为：组地址模式
				sc.setAddr_type(groupidToAddr(Integer.parseInt(ploy.getBindData())));// 组地址
				sc.setDst_ep("0A");
				sc.setCluster_id("0008");
				sc.setCmd_type("01");
				sc.setCmd_id("00");
				sc.setPayload(Integer.toHexString(operate.getOperateParam()).toUpperCase() + "000000");
				sc.createCmdString();
			}
			
		} else if (ploy.getBindType() == 3) {//节点绑定，发送节点地址指令
			if (operate.getOperateType() == 1) {// 开关
				sc.setLength("08");
				sc.setCmd0("29");
				sc.setCmd1("02");
				sc.setAddr_mode("02");
				sc.setAddr_type(ploy.getBindData());
				sc.setDst_ep("0A");
				sc.setCluster_id("0006");
				sc.setCmd_type("01");
				if (operate.getOperateParam() == 1) {
					sc.setCmd_id("01");
				} else if (operate.getOperateParam() == 0) {
					sc.setCmd_id("00");
				}
			} else if (operate.getOperateType() == 2) {// 调光
				sc.setLength("0C");
				sc.setCmd0("29");
				sc.setCmd1("02");
				sc.setAddr_mode("02");
				sc.setAddr_type(ploy.getBindData());
				sc.setDst_ep("0A");
				sc.setCluster_id("0008");
				sc.setCmd_type("01");
				sc.setCmd_id("00");
				sc.setPayload(Integer.toHexString(operate.getOperateParam()).toUpperCase() + "000000");
				sc.createCmdString();
			}
		}
		
		for (SocketCmd temp : ds.getCmdPool()) {
			if (temp.getCmdString().equals(sc.getCmdString())) {// 有重复指令正在执行
				try {
					myprint(ds, sc.getCmdString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
		
		//无重复指令
		sc.getTimer().schedule(new TimerTask() {
			private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
			private SocketCmd thisCmd = sc;

			public void run() {
				this.cancel();
				if (cmdPool.contains(thisCmd)) {
					cmdPool.remove(thisCmd);
				}
			}
		}, 80 * 1000);// 毫秒
		ds.getCmdPool().add(sc);
		try {
			myprint(ds, sc.getCmdString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void myprint(DeviceSocket ds, String cmd) throws IOException {
		PrintWriter out = new PrintWriter(ds.getSocket().getOutputStream());// 得到输出流
		out.print(cmd);
		out.flush();
//		System.out.println("serverPloy to " + ds.getDevice().getDevMac() + ": " + cmd);
		log.info("serverPloy to " + ds.getDevice().getDevMac() + ": " + cmd);
	}
	
	private String groupidToAddr(Integer groupid) {
		String groupaddr = groupid.toString();
		if (groupaddr.length() >= 4) {
			groupaddr = groupaddr.substring(groupaddr.length() - 4);
		} else {
			String temp = "";
			for (int i = 0; i < 4 - groupaddr.length(); i++) {
				temp = "0" + temp;
			}
			groupaddr = temp + groupaddr;
		}
		return groupaddr;
	}

}
