package com.test.service.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.test.domain.DeviceAttr;
import com.test.domain.Group;
import com.test.domain.GroupPair;
import com.test.domain.Zigbee;
import com.test.domain.ZigbeeAttr;
import com.test.util.UnusualUtils;

/**
 * 多线程处理socket接收的数据
 * 
 * @author zhangzhongwen
 * 
 */
public class SocketOperate extends Thread {

	// 指令
	// static private String cmd_setMac = "mac:";
	private final static String READ_STATUS = "FE0969030200000A00CA040000";

	private DeviceSocket deviceSocket;

	private ArrayList<Zigbee> zigbeeList;

	private ArrayList<Zigbee> zigbeePrematureList = new ArrayList<>();

	private Timer timer;
	
	private Logger log = Logger.getLogger("D");

	private Zigbee checkZigbee(String nwkAddr) {
		zigbeeList = SocketTool.selectZigbeeByDevMac(deviceSocket.getDevice().getDevMac());
		for (Zigbee zb : zigbeeList) {
			if (nwkAddr.equals(zb.getZigbeeSaddr())) {// 匹配成功
				return zb;
			}
		}
		return null;
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

	private Group checkGroup(Integer userid, String groupAddr) {
		ArrayList<Group> groupList = SocketTool.selectGroupByUserid(userid);
		for (Group group : groupList) {
			if (groupAddr.equals(groupidToAddr(group.getGroupid()))) {
				return group;
			}
		}
		return null;
	}

	private void myprint(PrintWriter out, String cmd) {//输出到控制台文件
		out.print(cmd);
		out.flush();
		//添加时间戳
//		System.out.println(new Date().toString() + " server to " + this.deviceSocket.getDevice().getDevMac() + ": " + cmd);
		log.info("server to " + this.deviceSocket.getDevice().getDevMac() + ": " + cmd);
	}

	private void readZigbeeReply(Zigbee zb, String strXML) {
		if ("00".equals(strXML.substring(50, 52))) {
			// 更新节点状态
			zb.setZigbeeNet(1);
		} else if ("01".equals(strXML.substring(50, 52))) {
			zb.setZigbeeNet(0);
		}

		zb.setZigbeeSaddr(strXML.substring(26, 30));// 更新短地址

		if ("00".equals(strXML.substring(36, 38))) {// 状态正常
			if ("00".equals(strXML.substring(38, 40))) {// 灯亮
				zb.setZigbeeStatus(1);
			} else if ("01".equals(strXML.substring(38, 40))) {// 灯灭
				zb.setZigbeeStatus(0);
			}

			zb.setZigbeeBright(Integer.valueOf(strXML.substring(40, 42), 16));// 读取亮度

			// update the database
			SocketTool.updateZigbeeByPrimaryKeySelective(zb);// 更新数据库

			// 其他参数操作
			ZigbeeAttr zigbeeAttr = SocketTool.selectZigbeeAttrByPrimaryKey(zb.getZigbeeMac());
			if (zigbeeAttr != null) {
				zigbeeAttr.setTemperature(Integer.valueOf(strXML.substring(42, 46), 16));// get the temperature
				zigbeeAttr.setHumidity(Integer.valueOf(strXML.substring(46, 50), 16));// get the humidity
				// update the database
				SocketTool.updateZigbeeAttrByPrimaryKeySelective(zigbeeAttr);
			} else {
				zigbeeAttr = new ZigbeeAttr();
//				zigbeeAttr.setType(3);
				zigbeeAttr.setZigbeeMac(zb.getZigbeeMac());// set primary key
				zigbeeAttr.setTemperature(Integer.valueOf(strXML.substring(42, 46), 16));// get the temperature
				zigbeeAttr.setHumidity(Integer.valueOf(strXML.substring(46, 50), 16));// get the humidity
				// insert data to the database
				if (SocketTool.insertZigbeeAttrSelective(zigbeeAttr) > 0) {
//					System.out.println("添加成功");
				} else {
//					System.out.println("添加失败");
				}
			}
			UnusualUtils.RecordUnusualTemperatureToDB(zigbeeAttr);

		} else {// 状态异常

		}
	}

	public SocketOperate(DeviceSocket deviceSocket) {
		this.deviceSocket = deviceSocket;
	}

	public void run() {
		try {

			InputStream in = deviceSocket.getSocket().getInputStream();

			PrintWriter out = new PrintWriter(deviceSocket.getSocket().getOutputStream());

			Zigbee zbtest;

			Group grouptemp;

			GroupPair gp;

			ArrayList<Zigbee> zbTempList;
			
			if (timer == null) {
				timer = new Timer();// 实例化Timer类

				new Timer().schedule(new TimerTask() {
					private PrintWriter timerOut = out;
					private Integer count = 0;

					public void run() {
						myprint(this.timerOut, "E0E290202FFFF0A000801002FF000000");
						count++;
						if (count == 3) {
							this.cancel();
						}
					}
				}, 500, 500);// 毫秒

				timer.schedule(new TimerTask() {
					private PrintWriter timerOut = out;

					public void run() {
						myprint(this.timerOut, "FE04250100000000");

					}
				}, 5000, 5000);// 毫秒
			}
			
			while (/* deviceSocket.getSocket().isConnected() */true) {
				// 读取客户端发送的信息
				String strXML = "";
				byte[] temp = new byte[1024];
				int length = 0;
				int dataPoint = 0;
				ArrayList<String> dataFrameList = new ArrayList<String>();
				while ((length = in.read(temp)) != -1) {
					dataPoint = 0;
					strXML = new String(temp, 0, length);
					dataFrameList.clear();
					
					///////// 以下为数据粘包处理，将包含多帧数据的的数据包拆开解析 ////////////
					while (dataPoint < length) {
						if (length >= 52 + dataPoint && strXML.substring(dataPoint, 10 + dataPoint).equals("FE18690202")) {// 查询灯状态应答帧，长度52
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 52));
							dataPoint = 52 + dataPoint;
						} else if (length >= 12 + dataPoint && strXML.substring(dataPoint, 12 + dataPoint).equals("FE0165010065")) {// 未知命令
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 12));
							dataPoint = 12 + dataPoint;
						} else if (length >= 36 + dataPoint && strXML.substring(dataPoint, 10 + dataPoint).equals("FE0D458100")) {// 查询回复mac地址+短地址，长度36
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 36));
							dataPoint = 36 + dataPoint;
						} else if (length >= 36 + dataPoint && strXML.substring(dataPoint, 8 + dataPoint).equals("FE0D45C1")) {// 上报mac地址+短地址，长度36
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 36));
							dataPoint = 36 + dataPoint;
						} else if (length >= 26 + dataPoint && strXML.substring(dataPoint, 2 + dataPoint).equals("FE")
								&& "29030200000A00CA0102".equals(strXML.substring(4 + dataPoint, 24 + dataPoint))) {// 电话号码,长度不定，未发现粘包，不作处理
							dataFrameList.add(strXML.substring(dataPoint));
							dataPoint = length;
						} else if (length >= 66 + dataPoint && strXML.substring(dataPoint, 8 + dataPoint).equals("FE1C4584")) {// 节点上报信息应答帧，长度44
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 66));
							dataPoint = 66 + dataPoint;
						} else if (length >= 26 + dataPoint && strXML.substring(dataPoint, 8 + dataPoint).equals("FE084585")) {// 未知指令，长度26
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 26));
							dataPoint = 26 + dataPoint;
						} else if (length >= 46 + dataPoint && strXML.substring(dataPoint, 8 + dataPoint).equals("FE124584")) {// 未知指令，长度46
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 46));
							dataPoint = 46 + dataPoint;
						} else if (length >= 27 + dataPoint && strXML.substring(dataPoint, 27 + dataPoint).equals("FE0B29030200000A00CA0101XTB")) {// 心跳包，长度27
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 27));
							dataPoint = 27 + dataPoint;
						} else if (length >= 28 && strXML.substring(dataPoint, 8 + dataPoint).equals("FE096902")
								&& "06".equals(strXML.substring(16 + dataPoint, 18 + dataPoint))) {// 开、关命令应答帧，长度28
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 28));
							dataPoint = 28 + dataPoint;
						} else if (length >= 28 && strXML.substring(dataPoint, 8 + dataPoint).equals("FE096902")
								&& "08".equals(strXML.substring(16 + dataPoint, 18 + dataPoint))) {// 调光命令应答帧，长度28
							dataFrameList.add(strXML.substring(dataPoint, dataPoint + 28));
							dataPoint = 28 + dataPoint;
						} else {
							dataFrameList.add(strXML.substring(dataPoint));
							dataPoint = length;
						}
					}
					//////////////////////////////////////////////////////////////////////
					for (int i = 0; i < dataFrameList.size(); i++) {
						strXML = dataFrameList.get(i);
						
						
//						System.out.println(new Date().toString() + " " + deviceSocket.getDevice().getDevMac() + ": " + strXML);
						log.info(deviceSocket.getDevice().getDevMac() + ": " + strXML);
						if (strXML.startsWith("FE0165010065")) {
							strXML = strXML.replace("FE0165010065", "");
						}
						if (strXML.length() > 30 && strXML.startsWith("FE0D458100")) {// 查询回复mac地址+短地址
							if ("0000".equals(strXML.substring(26, 30))) {// 集控器mac地址
								if (deviceSocket.getDevice().getDevMac() == null) {// 该端口第一次绑定设备
									deviceSocket.getDevice().setDevMac(strXML.substring(10, 26));
									deviceSocket.getDevice().setDevNet(1);// 更新设备网络状态
									SocketTool.updateDeviceData(deviceSocket.getDevice());// 更新数据库
//									myprint(out, "更新数据" + deviceSocket.getDevice().toString());
								} else {// 第N次绑定设备
									deviceSocket.getDevice().setDevNet(0);// 先将旧mac地址设备离线
									SocketTool.updateDeviceData(deviceSocket.getDevice());// 更新数据库
									deviceSocket.getDevice().setDevMac(strXML.substring(10, 26));// 读新mac地址
									deviceSocket.getDevice().setDevNet(1);// 上线
									SocketTool.updateDeviceData(deviceSocket.getDevice());// 更新数据库
								}
								deviceSocket.setDevice(
										SocketTool.selectDeviceByPrimaryKey(deviceSocket.getDevice().getDevMac()));// 同步数据，主要是为了获取用户id
								for (Zigbee zb : zigbeePrematureList) {// 将上报mac地址之前连接到集控器的节点更新到数据库
									zb.setDevMac(deviceSocket.getDevice().getDevMac());
									zbtest = SocketTool.selectZigbeeByPrimaryKey(zb.getZigbeeMac());
									if (zbtest != null) {// 数据库中已存在，进行更新操作
										SocketTool.updateZigbeeByPrimaryKeySelective(zb);
										zbtest = null;
									} else {
										SocketTool.insertZigbee(zb);
									}
								}

								if (zigbeePrematureList != null) {
									zigbeePrematureList.clear();
								}
								if (timer != null) {
									timer.cancel();
									timer = null;
								}

								myprint(out, READ_STATUS);

							} else if (strXML.length() > 30) {// zigbee节点mac地址
								Zigbee zb = SocketTool.selectZigbeeByPrimaryKey(strXML.substring(10, 26));// 节点mac地址
								if (zb != null) {
									zb.setZigbeeSaddr(strXML.substring(26, 30));// 节点短地址
									zb.setZigbeeNet(1);// 节点网络状态(上线)
									zb.setDevMac(deviceSocket.getDevice().getDevMac());
									SocketTool.updateZigbeeByPrimaryKeySelective(zb);// 更新数据库
								} else {
									zb = new Zigbee();
									zb.setDevMac(deviceSocket.getDevice().getDevMac());
									zb.setZigbeeMac(strXML.substring(10, 26));
									zb.setZigbeeSaddr(strXML.substring(26, 30));
									zb.setZigbeeName(zb.getZigbeeMac());
									zb.setZigbeeNet(1);
									zb.setZigbeeStatus(1);
									zb.setZigbeeBright(100);
									SocketTool.insertZigbee(zb);// 更新数据库
								}
							}

						} else if (strXML.length() >= 32 && strXML.startsWith("FE0D45C1")) {// 上报mac地址+短地址
							if (deviceSocket.getDevice().getDevMac() != null) {
								zbtest = SocketTool.selectZigbeeByPrimaryKey(strXML.substring(16, 32));// 节点mac地址
								if (zbtest != null) {
									zbtest.setZigbeeSaddr(strXML.substring(12, 16));// 节点短地址
									zbtest.setDevMac(deviceSocket.getDevice().getDevMac());
									zbtest.setZigbeeNet(1);// 节点网络状态(上线)
									SocketTool.updateZigbeeByPrimaryKeySelective(zbtest);// 更新数据库
								} else {
									zbtest = new Zigbee();
									zbtest.setDevMac(deviceSocket.getDevice().getDevMac());
									zbtest.setZigbeeMac(strXML.substring(16, 32));
									zbtest.setZigbeeSaddr(strXML.substring(12, 16));
									zbtest.setZigbeeName(zbtest.getZigbeeMac());
									zbtest.setZigbeeNet(1);
									zbtest.setZigbeeStatus(1);
									zbtest.setZigbeeBright(100);
									SocketTool.insertZigbee(zbtest);// 更新数据库
								}
								zbtest = null;
							} else {// 节点连接在集控器上报mac地址之前
								zbtest = SocketTool.selectZigbeeByPrimaryKey(strXML.substring(16, 32));// 节点mac地址
								if (zbtest != null) {
									zbtest.setZigbeeSaddr(strXML.substring(12, 16));// 节点短地址
									zbtest.setZigbeeNet(1);// 节点网络状态(上线)
								} else {
									zbtest = new Zigbee();
									zbtest.setZigbeeMac(strXML.substring(16, 32));
									zbtest.setZigbeeSaddr(strXML.substring(12, 16));
									zbtest.setZigbeeName(zbtest.getZigbeeMac());
									zbtest.setZigbeeNet(1);
									zbtest.setZigbeeStatus(1);
									zbtest.setZigbeeBright(100);
								}
								zigbeePrematureList.add(zbtest);
								zbtest = null;
							}

						} else if (strXML.length() >= 26 && strXML.startsWith("FE")
								&& "29030200000A00CA0102".equals(strXML.substring(4, 24))) {// 电话号码
							deviceSocket.getDevice().setGprsPhone(strXML.substring(25, strXML.length() - 1));// 设置电话号码

							if (timer == null) {
								timer = new Timer();// 实例化Timer类

								new Timer().schedule(new TimerTask() {
									private PrintWriter timerOut = out;
									private Integer count = 0;

									public void run() {
										myprint(this.timerOut, "E0E290202FFFF0A000801002FF000000");
										count++;
										if (count == 3) {
											this.cancel();
										}
									}
								}, 500, 500);// 毫秒

								timer.schedule(new TimerTask() {
									private PrintWriter timerOut = out;

									public void run() {
										myprint(this.timerOut, "FE04250100000000");

									}
								}, 5000, 5000);// 毫秒
							}

						} else if (strXML.length() >= 52 && strXML.startsWith("FE18690202")
								&& "0ACA00".equals(strXML.substring(30, 36))) {// 查询灯状态应答帧
							// Zigbee zb = SocketTool.selectZigbeeBySAddrAndDevMac(strXML.substring(10, 14),
							// deviceSocket.getDevice().getDevMac());
							Zigbee zb = SocketTool.selectZigbeeByPrimaryKey(strXML.substring(10, 26));
							if (zb != null) {
								readZigbeeReply(zb, strXML);
							} else {
								// 添加新节点
								if (deviceSocket.getDevice() != null && deviceSocket.getDevice().getDevMac() != null) {
									zb = new Zigbee();
									zb.setZigbeeMac(strXML.substring(10, 26));
									zb.setZigbeeName(zb.getZigbeeMac());
									zb.setDevMac(deviceSocket.getDevice().getDevMac());
									zb.setZigbeeSaddr(strXML.substring(26, 30));
									zb.setZigbeeNet(1);
									zb.setZigbeeBright(100);
									zb.setZigbeeStatus(1);
									SocketTool.insertZigbee(zb);
									
									// 更新
									readZigbeeReply(zb, strXML);
									
									// 查询节点信息
								}
								
							}
						} else if (strXML.length() >= 42 && strXML.startsWith("FE1C4584")) {// 节点上报信息应答帧
							Zigbee zb = checkZigbee(strXML.substring(8, 12));
							if (zb != null) {
								// 其他参数操作
								ZigbeeAttr zigbeeAttr = SocketTool.selectZigbeeAttrByPrimaryKey(zb.getZigbeeMac());
								if (zigbeeAttr != null) {
									zigbeeAttr.setVersion(strXML.substring(34, 36));
									zigbeeAttr.setType(Integer.valueOf(strXML.substring(36, 38), 16));// get device-type
									zigbeeAttr.setPower(Integer.valueOf(strXML.substring(38, 42), 16));// get power
									// update the database
									SocketTool.updateZigbeeAttrByPrimaryKeySelective(zigbeeAttr);
								} else {
									zigbeeAttr = new ZigbeeAttr();
									zigbeeAttr.setZigbeeMac(zb.getZigbeeMac());// set primary key
									zigbeeAttr.setVersion(strXML.substring(34, 36));
									zigbeeAttr.setType(Integer.valueOf(strXML.substring(36, 38), 16));// get device-type
									zigbeeAttr.setPower(Integer.valueOf(strXML.substring(38, 42), 16));// get power
									// insert data to the database
									SocketTool.insertZigbeeAttrSelective(zigbeeAttr);
								}
							} else {
								// 添加新节点
							}
						} else if (strXML.startsWith("FE0B29030200000A00CA0101XTB") && SocketTool.HeartBeatReport) {
							// out.print("FE0969030200000A00CA010000");// 心跳包回复
							// out.flush();
							myprint(out, "FE0969030200000A00CA010000");
						} else if (deviceSocket.getCmdPool().isEmpty()) {// 指令队列为空，拦截下面的比对操作
						} else if (strXML.length() >= 26 && strXML.startsWith("FE096902")
								&& "06".equals(strXML.substring(16, 18))) {// 开、关命令应答帧

							for (SocketCmd sc : deviceSocket.getCmdPool()) {
								if (sc == null) {
									continue;
								}
								if ("0006".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
										&& "02".equals(sc.getCmd1())) {
									if (strXML.substring(8, 14).equals(sc.getAddr_mode() + sc.getAddr_type())) {// 匹配地址
										if ("00".equals(strXML.substring(24, 26))) {// 如果操作成功, 将zigbee信息同步，更新到数据库
											if ("02".equals(sc.getAddr_mode())) {// 如果是通过短地址发送指令
												if ("FFFF".equals(sc.getAddr_type())) {// 广播地址
													zbtest = new Zigbee();
													zbtest.setDevMac(deviceSocket.getDevice().getDevMac());
													if ("00".equals(strXML.substring(22, 24))) {// 关指令
														zbtest.setZigbeeStatus(0);
													} else if ("01".equals(strXML.substring(22, 24))) {// 开指令
														zbtest.setZigbeeStatus(1);
													}
													SocketTool.updateZigbeeBydevMacSelectiveWhereOnline(zbtest);// 同一集控器地址的zigbee节点全部修改状态
													deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
													zbtest = null;
													break;// 必须跳出循环，否则数组越界崩溃
												} else {// 非广播地址
													zbtest = checkZigbee(sc.getAddr_type());
													if (zbtest != null) {
														if ("00".equals(strXML.substring(22, 24))) {// 关指令
															zbtest.setZigbeeStatus(0);
														} else if ("01".equals(strXML.substring(22, 24))) {// 开指令
															zbtest.setZigbeeStatus(1);
														}
														SocketTool.updateZigbeeByPrimaryKeySelective(zbtest);// 更新数据库
														deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
														zbtest = null;
														break;// 必须跳出循环，否则数组越界异常
													} else {// 数据库同步失败
													}
												}
											} else if ("01".equals(sc.getAddr_mode())) {// 如果是通过组地址发送指令
												grouptemp = checkGroup(deviceSocket.getDevice().getUserid(),
														sc.getAddr_type());// 获取组信息
												if (grouptemp != null) {
													String tempStr = strXML.substring(22, 24);
													zbTempList = SocketTool.selectzigbeeByGroup(grouptemp);// 获取该分组下所有zigbee节点
													for (Zigbee zb : zbTempList) {
														if (zb.getDevMac().equals(deviceSocket.getDevice().getDevMac())
																&& zb.getZigbeeNet() == 1) {// 如果zigbee节点设备mac地址与集控器mac地址相同且在线
															if ("00".equals(tempStr)) {// 关指令
																zb.setZigbeeStatus(0);
															} else if ("01".equals(tempStr)) {// 开指令
																zb.setZigbeeStatus(1);
															}
															SocketTool.updateZigbeeByPrimaryKeySelective(zb);// 更新数据库
														}
													}
												}
												deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
												zbTempList = null;
												grouptemp = null;
												break;// 必须跳出循环，否则数组越界异常
											}
										} else {// 操作失败
										}
									} else {// 网络地址匹配失败
									}
								}
							}

						} else if (strXML.length() >= 26 && strXML.startsWith("FE09690202")
								&& "08".equals(strXML.substring(16, 18))) {// 调光命令应答帧

							for (SocketCmd sc : deviceSocket.getCmdPool()) {
								if (sc == null) {
									continue;
								}
								if ("0008".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
										&& "02".equals(sc.getCmd1())) {
									if (strXML.substring(8, 14).equals(sc.getAddr_mode() + sc.getAddr_type())) {// 匹配地址
										if ("00".equals(strXML.substring(24, 26))) {// 如果操作成功, 将zigbee信息同步，更新到数据库
											if ("02".equals(sc.getAddr_mode())) {// 如果是通过短地址发送指令
												if ("FFFF".equals(sc.getAddr_type())) {// 广播地址
													zbtest = new Zigbee();
													zbtest.setDevMac(deviceSocket.getDevice().getDevMac());
													int brightness = Integer.valueOf(sc.getPayload().substring(0, 2),
															16);// 读取亮度
													zbtest.setZigbeeBright(brightness);
													SocketTool.updateZigbeeBydevMacSelectiveWhereOnline(zbtest);// 同一集控器地址的zigbee节点全部修改状态
													deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
													zbtest = null;
													break;// 必须跳出循环，否则数组越界崩溃
												} else {// 非广播地址
													zbtest = checkZigbee(sc.getAddr_type());
													if (zbtest != null) {
														int brightness = Integer
																.valueOf(sc.getPayload().substring(0, 2), 16);// 读取亮度
														zbtest.setZigbeeBright(brightness);
														SocketTool.updateZigbeeByPrimaryKeySelective(zbtest);// 更新数据库
														deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
														zbtest = null;
														break;// 必须跳出循环，否则数组越界异常
													} else {// 数据库同步失败
													}
												}
											} else if ("01".equals(sc.getAddr_mode())) {// 如果是通过组地址发送指令
												grouptemp = checkGroup(deviceSocket.getDevice().getUserid(),
														sc.getAddr_type());// 获取组信息
												if (grouptemp != null) {
													int brightness = Integer.valueOf(sc.getPayload().substring(0, 2),
															16);// 读取亮度
													zbTempList = SocketTool.selectzigbeeByGroup(grouptemp);// 获取该分组下所有zigbee节点
													for (Zigbee zb : zbTempList) {
														if (zb.getDevMac().equals(deviceSocket.getDevice().getDevMac())
																&& zb.getZigbeeNet() == 1) {// 如果zigbee节点设备mac地址与集控器mac地址相同且在线，调节亮度
															zb.setZigbeeBright(brightness);
															SocketTool.updateZigbeeByPrimaryKeySelective(zb);// 更新数据库
														}
													}
												}
												deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
												zbTempList = null;
												grouptemp = null;
												break;// 必须跳出循环，否则数组越界异常
											}
										} else {// 操作失败
										}
									} else {// 网络地址匹配失败
									}
								}
							}

						} else if (strXML.length() >= 26 && strXML.startsWith("FE09690301")
								&& strXML.substring(14, 16).equals("0A") && strXML.endsWith("00")) {// 自定义组控制指令应答帧，非标准指令
							if (strXML.substring(16, 20).equals("0006")) {// 分组开关指令
								for (SocketCmd sc : deviceSocket.getCmdPool()) {
									if (sc == null) {
										continue;
									}
									if ("0006".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
											&& "02".equals(sc.getCmd1()) && "01".equals(sc.getAddr_mode())
											&& strXML.substring(10, 14).equals(sc.getAddr_type())) {
										if (strXML.substring(22, 24).equals("00")) {// 成功?
											grouptemp = checkGroup(deviceSocket.getDevice().getUserid(),
													sc.getAddr_type());// 获取组信息
											if (grouptemp != null) {
												String tempStr = strXML.substring(20, 22);
												zbTempList = SocketTool.selectzigbeeByGroup(grouptemp);// 获取该分组下所有zigbee节点
												if (zbTempList != null) {
													for (Zigbee zb : zbTempList) {
														if (zb.getDevMac().equals(deviceSocket.getDevice().getDevMac())
																&& zb.getZigbeeNet() == 1) {// 如果zigbee节点设备mac地址与集控器mac地址相同且在线
															if (tempStr.equals("00")) {// 关指令
																zb.setZigbeeStatus(0);
															} else if (tempStr.equals("01")) {// 开指令
																zb.setZigbeeStatus(1);
															}
															SocketTool.updateZigbeeByPrimaryKeySelective(zb);// 更新数据库
														}
													}
												}
											}
											zbTempList = null;
											grouptemp = null;

										}

										deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
										break;// 必须跳出循环，否则数组越界崩溃
									}
								}
							} else if (strXML.substring(16, 20).equals("0008")) {// 分组调光指令
								for (SocketCmd sc : deviceSocket.getCmdPool()) {
									if (sc == null) {
										continue;
									}
									if ("0008".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
											&& "02".equals(sc.getCmd1()) && "01".equals(sc.getAddr_mode())
											&& strXML.substring(10, 14).equals(sc.getAddr_type())) {
										if (strXML.substring(22, 24).equals("00")) {// 成功?
											grouptemp = checkGroup(deviceSocket.getDevice().getUserid(),
													sc.getAddr_type());// 获取组信息
											if (grouptemp != null) {
//												int brightness = Integer.valueOf(sc.getPayload().substring(0, 2),
//														16);// 读取亮度
												int brightness = Integer.valueOf(strXML.substring(20, 22), 16);
												zbTempList = SocketTool.selectzigbeeByGroup(grouptemp);// 获取该分组下所有zigbee节点
												for (Zigbee zb : zbTempList) {
													if (zb.getDevMac().equals(deviceSocket.getDevice().getDevMac())
															&& zb.getZigbeeNet() == 1) {// 如果zigbee节点设备mac地址与集控器mac地址相同且在线，调节亮度
														zb.setZigbeeBright(brightness);
														SocketTool.updateZigbeeByPrimaryKeySelective(zb);// 更新数据库
													}
												}
											}
											zbTempList = null;
											grouptemp = null;

										}
										deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
										break;// 必须跳出循环，否则数组越界崩溃
									}
								}
							}
						} else if (strXML.length() >= 26 && strXML.startsWith("FE09690302FFFF0A")
								&& strXML.endsWith("00")) {// 自定义广播指令应答帧，非标准指令
							if (strXML.substring(16, 20).equals("0006")) {// 广播开关应答帧
								for (SocketCmd sc : deviceSocket.getCmdPool()) {
									if (sc == null) {
										continue;
									}
									if ("0006".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
											&& "02".equals(sc.getCmd1()) && "02".equals(sc.getAddr_mode())
											&& "FFFF".equals(sc.getAddr_type())) {

										if (strXML.substring(22, 24).equals("00")) {// 成功?
											zbtest = new Zigbee();
											zbtest.setDevMac(deviceSocket.getDevice().getDevMac());
											if (strXML.substring(20, 22).equals("00")) {// 关指令
												zbtest.setZigbeeStatus(0);
											} else if (strXML.substring(20, 22).equals("01")) {// 开指令
												zbtest.setZigbeeStatus(1);
											}
											SocketTool.updateZigbeeBydevMacSelectiveWhereOnline(zbtest);// 同一集控器地址的zigbee节点全部修改状态

											zbtest = null;

										}

										deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
										break;// 必须跳出循环，否则数组越界崩溃
									}
								}
							} else if (strXML.substring(16, 20).equals("0008")) {// 广播调光应答帧
								for (SocketCmd sc : deviceSocket.getCmdPool()) {
									if (sc == null) {
										continue;
									}
									if ("0008".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
											&& "02".equals(sc.getCmd1()) && "02".equals(sc.getAddr_mode())
											&& "FFFF".equals(sc.getAddr_type())) {// 匹配指令
										if (strXML.substring(22, 24).equals("00")) {// 成功?
											zbtest = new Zigbee();
											zbtest.setDevMac(deviceSocket.getDevice().getDevMac());
//											int brightness = Integer.valueOf(sc.getPayload().substring(0, 2), 16);// 读取亮度
											int brightness = Integer.valueOf(strXML.substring(20, 22), 16);
											zbtest.setZigbeeBright(brightness);
											SocketTool.updateZigbeeBydevMacSelectiveWhereOnline(zbtest);// 同一集控器地址的zigbee节点全部修改状态

											zbtest = null;

										}
										deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
										break;// 必须跳出循环，否则数组越界崩溃
									}
								}
							}
						} else if (strXML.length() >= 26 && strXML.startsWith("FE09690202")
								&& strXML.substring(14, 24).equals("0A04000100")) {// 添加分组应答帧
							for (SocketCmd sc : deviceSocket.getCmdPool()) {
								if (sc == null) {
									continue;
								}
								if ("0004".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
										&& "02".equals(sc.getCmd1()) && "00".equals(sc.getCmd_id())) {
									if (strXML.substring(8, 14).equals(sc.getAddr_mode() + sc.getAddr_type())) {// 匹配地址
										if (strXML.substring(24, 26).equals("00")
												|| strXML.substring(24, 26).equals("8A")) {// 如果操作成功, 将zigbee信息同步，更新到数据库
											if ("02".equals(sc.getAddr_mode())) {// 如果是通过短地址发送指令
												if ("FFFF".equals(sc.getAddr_type())) {// 广播地址
												} else {// 非广播地址

													zbtest = checkZigbee(sc.getAddr_type());
													grouptemp = checkGroup(deviceSocket.getDevice().getUserid(),
															sc.getPayload().substring(0, 4));
													if (grouptemp != null && zbtest != null) {
														gp = new GroupPair();
														gp.setGroupid(grouptemp.getGroupid());
														gp.setZigbeeMac(zbtest.getZigbeeMac());
														gp.setUserid(grouptemp.getUserid());
														SocketTool.insertGroupPair(gp);
														deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
														zbtest = null;
														grouptemp = null;
														break;
													} else {// 数据库同步失败
													}
												}
											}
										} else {// 操作失败
										}
									} else {// 网络地址匹配失败
									}
								}
							}
						} else if (strXML.length() >= 26 && strXML.startsWith("FE09690202")
								&& (strXML.substring(14, 24).equals("0A04000103")
										|| strXML.substring(14, 24).equals("0A04000104"))) {// 删除分组应答帧
							for (SocketCmd sc : deviceSocket.getCmdPool()) {
								if (sc == null) {
									continue;
								}
								if ("0004".equals(sc.getCluster_id()) && "29".equals(sc.getCmd0())
										&& "02".equals(sc.getCmd1()) && "03".equals(sc.getCmd_id())) {
									if (strXML.substring(8, 14).equals(sc.getAddr_mode() + sc.getAddr_type())) {// 匹配地址
										if (strXML.substring(24, 26).equals("00")) {// 如果操作成功, 将zigbee信息同步，更新到数据库
											if ("02".equals(sc.getAddr_mode())) {// 如果是通过短地址发送指令
												if ("FFFF".equals(sc.getAddr_type())) {// 广播地址
												} else {// 非广播地址
													zbtest = checkZigbee(sc.getAddr_type());
													grouptemp = checkGroup(deviceSocket.getDevice().getUserid(),
															sc.getPayload().substring(0, 4));
													if (grouptemp != null && zbtest != null) {
														gp = SocketTool.selectGroupPairByUseridAndGroupidAndZigbeeMac(
																grouptemp.getUserid(), grouptemp.getGroupid(),
																zbtest.getZigbeeMac());
														if (gp != null) {
															SocketTool.removeGroupPairByid(gp.getId());
														}
														deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
														zbtest = null;
														grouptemp = null;
														break;
													} else {// 数据库同步失败
													}
												}
											}
										}
									}
								}
							}
						} else if (strXML.length() >= 26 && strXML.startsWith("FE0969030200000A00CA05")) {// 开启节点发现命令帧
							for (SocketCmd sc : deviceSocket.getCmdPool()) {
								if (sc == null) {
									continue;
								}
								if (strXML.equals(sc.getCmdString())) {// 命令匹配成功
									int seconds = Integer.parseInt(strXML.substring(24, 26), 16);
									if (seconds == 0) {// 关闭发现
										// 修改数据库状态为0
										DeviceAttr devAttr = SocketTool
												.selectDeviceAttrByDevMac(deviceSocket.getDevice().getDevMac());
										if (devAttr != null) {
											devAttr.setZigbeeFinder(0);
											SocketTool.updateDeviceAttrByPrimaryKeySelective(devAttr);
										}
									} else {// 开启发现
										// 修改数据库状态为1
										DeviceAttr devAttr = SocketTool
												.selectDeviceAttrByDevMac(deviceSocket.getDevice().getDevMac());
										if (devAttr != null) {
											devAttr.setZigbeeFinder(1);
											SocketTool.updateDeviceAttrByPrimaryKeySelective(devAttr);
										}
										// 开新定时器线程计时，超 seconds 计时后修改数据库状态为0
										new Timer().schedule(new TimerTask() {
											private DeviceAttr myDeviceAttr = devAttr;

											public void run() {
												myDeviceAttr.setZigbeeFinder(0);
												SocketTool.updateDeviceAttrByPrimaryKeySelective(myDeviceAttr);
												this.cancel();
											}
										}, 1000 * seconds);// 毫秒
									}
									deviceSocket.getCmdPool().remove(sc);// 将已执行指令从池中移除
									break;
								} else if (strXML.substring(24, 26).equals(sc.getCmdString().substring(24, 26))) {// 命令接受成功，执行失败

								}
							}
						} else {// 未识别的命令码头，返回错误
							out.print("error: Unrecognized command header");// 命令头无法识别
							out.flush();
						}
					}
				}
				break;

			}

		} catch (IOException ex) {

		} finally {
			try {

				if (timer != null) {
					timer.cancel();// 定时器关闭
				}

				SocketTool.socketList.remove(deviceSocket);// 将socket从list中删除，防判断出错
				if (deviceSocket.getDevice().getDevMac() != null) {
					for (DeviceSocket ds : SocketTool.socketList) {
						if (ds.getDevice().getDevMac() != null
								&& ds.getDevice().getDevMac().equals(deviceSocket.getDevice().getDevMac())) {
							deviceSocket.getSocket().close();
							System.out.println("socket stop......");
							log.info("socket stop......");
							return;
						}
					}
					deviceSocket.getDevice().setDevNet(0);// 更新设备网络状态
					SocketTool.updateDeviceData(deviceSocket.getDevice());// 更新数据库
				}

				deviceSocket.getSocket().close();
				System.out.println("socket stop......");
				log.info("socket stop......");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}