package com.test.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.test.domain.DataObject;
import com.test.domain.Device;
import com.test.domain.Group;
import com.test.domain.Ploy;
import com.test.domain.PloyOperate;
import com.test.domain.User;
import com.test.domain.Zigbee;
import com.test.service.IUserService;
import com.test.service.socket.DeviceSocket;
import com.test.service.socket.SocketCmd;
import com.test.service.socket.SocketTool;

@Controller
@RequestMapping("/user")
public class UserController {

	private Integer cmdDelay = 15000;// 毫秒
	private Logger log = Logger.getLogger("D");

	public UserController() {
		// System.out.println("UserController");
	}

	private void myprint(DeviceSocket ds, String cmd) throws IOException {
		PrintWriter out = new PrintWriter(ds.getSocket().getOutputStream());// 得到输出流
		out.print(cmd);
		out.flush();
//		System.out.println("server to " + ds.getDevice().getDevMac() + ": " + cmd);
		log.info("server to " + ds.getDevice().getDevMac() + ": " + cmd);
	}

	@Resource
	private IUserService userService;

	@RequestMapping("/socketTest")
	public void socketTest(HttpServletRequest request, HttpServletResponse response) {
		String cmd = request.getParameter("command");
		DataObject data = new DataObject();
		if (SocketTool.socketList != null) {

			for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
				try {
					myprint(ds, cmd);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码
		try {
			response.getWriter().write(JSONObject.toJSONString(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/updateZigbeeParam")// 更新zigbee节点信息
	public void updateZigbeeParam(HttpServletRequest request, HttpServletResponse response) {
		// 获取zigbee节点的mac地址，集控器mac地址
		DataObject data = new DataObject();
		String zigbeeMac = request.getParameter("zigbeeMac");
		Zigbee zb = userService.getZigbeeByMac(zigbeeMac);
		if (zb != null && zb.getDevMac() != null) {
			if (SocketTool.socketList != null) {
				for (DeviceSocket ds : SocketTool.socketList) {
					if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(zb.getDevMac())) {
						// 向集控器发送获取zigbee节点信息指令
						
					}
				}
			}
		}
		// 将指令添加进指令池，返回reponse 指令发送成功
		// zigbee节点查询时，注意是否为空
		// 集控器mac地址查询时，判断集控器是否在线
	}

	@RequestMapping("/turnHeartBeat") // 开关心跳包
	public void turnHeartBeat(HttpServletRequest request, HttpServletResponse response) {
		DataObject data = new DataObject();
		SocketTool.HeartBeatReport = !SocketTool.HeartBeatReport;
		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码
		try {
			response.getWriter().write(JSONObject.toJSONString(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/LOGIN") //新登录界面
	public String LOGIN(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");

		String password = request.getParameter("password");

		if (username != null && password != null) {
			User user = userService.getUserByName(username);

			if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {

				DataObject data = userService.getDataByUser(user);

				response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码
				try {
					response.getWriter().write(JSONObject.toJSONString(data));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				 return "/WEB-INF/index.html";
			} else {

				DataObject data = new DataObject();

				data.setError("用户名或密码错误!");

				response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码
				try {
					response.getWriter().write(JSONObject.toJSONString(data));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return "/jsp/login.jsp";
			}
		}
		return null;
	}

	@RequestMapping("/login") // 登录
	public String login(HttpServletRequest request, Model model) {

		String username = request.getParameter("username");

		String password = request.getParameter("password");

		if (username != null && password != null) {
			User user = userService.getUserByName(username);

			if (user != null && username.equals(user.getUsername()) && password.equals(user.getPassword())) {

				DataObject data = userService.getDataByUser(user);

				String myJson = JSONObject.toJSONString(data);

				model.addAttribute("responseJson", myJson);

				// return "/WEB-INF/index.html";
				return "/jsp/index.jsp";
			} else {

				DataObject data = new DataObject();

				data.setError("用户名或密码错误!");

				String myJson = JSONObject.toJSONString(data);

				model.addAttribute("responseJson", myJson);

				return "/jsp/login.jsp";
			}
		}
		return null;
	}

	@RequestMapping("/regist") // 注册
	public void regist(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		DataObject data = userService.regist(username, password, email, phone);
		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码
		try {
			response.getWriter().write(JSONObject.toJSONString(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/changePassword") // 修改密码
	public void changePassword(HttpServletRequest request, HttpServletResponse response) {
		int userid = Integer.parseInt(request.getParameter("userid"));
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		DataObject data = new DataObject();
		User user = userService.getUserById(userid);
		if (user != null) {
			if (user.getPassword().equals(oldPassword)) {
				if (userService.changePassword(user, newPassword) > 0) {// 密码修改成功

				} else {
//					data.setError("密码修改失败");
					data.setError("Failed to change password");
				}
			} else {
//				data.setError("密码输入错误");
				data.setError("Incorrect password");
			}
		} else {
//			data.setError("用户不存在");
			data.setError("User does not exist");
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码
		try {
			response.getWriter().write(JSONObject.toJSONString(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/addDev") // 用户添加集控器
	// 如果有该设备,返回所有用户数据（相当于刷新）
	public void addDev(HttpServletRequest request, HttpServletResponse response) {

		String devMac = request.getParameter("devMac");

		Integer userid = Integer.parseInt(request.getParameter("userid"));

		DataObject data = userService.addDevToUser(userid, devMac);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 判断集控器是否在线，如果在线，将socketList中的devcieSocket绑定的数据更新
		Device dev = userService.selectDeviceByDevMac(devMac);
		if (dev != null && dev.getDevNet() == 1) {//不为空且在线
			for(DeviceSocket ds : SocketTool.socketList) {
				if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(devMac)) {
					ds.setDevice(dev);
				}
			}
		}
	}

	@RequestMapping("/removeDev") // 用户删除集控器
	// 如果修改成功，返回所有用户数据（相当于刷新）
	public void removeDev(HttpServletRequest request, HttpServletResponse response) {

		String devMac = request.getParameter("devMac");

		Integer userid = Integer.parseInt(request.getParameter("userid"));

		DataObject data = userService.removeDevFromUser(userid, devMac);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/renameDev") // 集控器重命名
	// 如果修改成功，返回空
	public void renameDev(HttpServletRequest request, HttpServletResponse response) {

		String devMac = request.getParameter("devMac");

		String devNewName = request.getParameter("devNewName");

		DataObject data = userService.renameDev(devMac, devNewName);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/turnZigbeeNetFinder") // 开启节点发现
	public void trunZigbeeNetFinder(HttpServletRequest request, HttpServletResponse response) {
		String devMac = request.getParameter("devMac");// mac地址用于查找对应的socket
		int timeCmd = Integer.parseInt(request.getParameter("timeCmd"));// timeCmd用于设定开启时长，或关闭
		// System.out.println(devMac + " " + timeCmd);
		String timeString;
		if (timeCmd > 15) {
			timeString = Integer.toHexString(timeCmd);
		} else {
			timeString = "0" + Integer.toHexString(timeCmd);
		}
		timeString = timeString.toUpperCase();

		DataObject data = new DataObject();

//		data.setError("该设备离线或您尚未添加该设备!");
		data.setError("The device is offline or you haven't added the device yet!");

		if (SocketTool.socketList != null) {

			// 查找socket端口
			for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
				if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(devMac)) {// 找到设备对应的scoket线程
					SocketCmd socketcmd = new SocketCmd();
					socketcmd.setCmdString("FE0969030200000A00CA0500" + timeString);// 设置指令
					// 查看socket端口上是否有正在执行的节点开启或关闭指令
					for (SocketCmd sc : ds.getCmdPool()) {
						if (sc.getCmdString().substring(0, 24).equals("FE0969030200000A00CA0500")) {// 如果有，说明上一条指令未收到回复，删除该条指令，发送新指令
							ds.getCmdPool().remove(sc);
							break;
						}
					}
					if (data.getError().equals("The device is offline or you haven't added the device yet!")) {// 如果没有，直接发送新指令
						socketcmd.getTimer().schedule(new TimerTask() {
							private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
							private SocketCmd thisCmd = socketcmd;

							public void run() {
								this.cancel();
								if (cmdPool.contains(thisCmd)) {
									cmdPool.remove(thisCmd);
								}
							}
						}, cmdDelay);// 毫秒
						ds.getCmdPool().add(socketcmd);
						// System.out.println("指令已发出");
						try {
							myprint(ds, socketcmd.getCmdString());

							data.setError("");// response返回结果,指令成功发出
							break;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping("/renameZigbee") // zigbee节点重命名
	// 如果修改成功, 返回空
	public void renameZigbee(HttpServletRequest request, HttpServletResponse response) {

		String zigbeeMac = request.getParameter("zigbeeMac");

		String newName = request.getParameter("newName");

		DataObject data = userService.renameZigbee(zigbeeMac, newName);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/removeZigbee") // 删除离线zigbee节点
	
	public void removeZigbee(HttpServletRequest request, HttpServletResponse response) {
		
		DataObject data = new DataObject();
		
		String zigbeeMac = request.getParameter("zigbeeMac");
		
		Integer userid = Integer.parseInt(request.getParameter("userid"));
		
		System.out.println(zigbeeMac + "..." + userid);
		
		User user = userService.getUserById(userid);
		
		if (user != null && zigbeeMac != null) {
			data = userService.removeZigbee(zigbeeMac, user);
		} else {
//			data.setError("参数错误!");
			data.setError("Parameter error!");
		}
		
		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/refresh") // 刷新
	// 1,接受用户id
	// 2,调用service方法重组数据返回
	public void refresh(HttpServletRequest request, HttpServletResponse response) {

		Integer userid = Integer.parseInt(request.getParameter("userid"));

		DataObject data = userService.getDataByUser(userService.getUserById(userid));

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/switchByDev") // 控制集控器下所有zigbee节点
	// 1,接受集控器的mac地址或短地址
	// 2,向socket发送消息，发送开关指令
	public void switchByDev(HttpServletRequest request, HttpServletResponse response) {
		String devMac = request.getParameter("devMac");
		String cmd = request.getParameter("cmd");

		DataObject data = new DataObject();

//		data.setError("该设备离线或您尚未添加该设备!");
		data.setError("The device is offline or you haven't added the device yet!");
		if (SocketTool.socketList != null) {

			for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
				if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(devMac)) {// 找到设备对应的scoket线程
					SocketCmd socketcmd = new SocketCmd();
					socketcmd.setLength("08");
					socketcmd.setCmd0("29");
					socketcmd.setCmd1("02");
					socketcmd.setAddr_mode("02");
					socketcmd.setAddr_type("FFFF");
					socketcmd.setDst_ep("0A");
					socketcmd.setCluster_id("0006");
					socketcmd.setCmd_type("01");
					if (cmd.equals("打开")) {
						socketcmd.setCmd_id("01");
					} else if (cmd.equals("关闭")) {
						socketcmd.setCmd_id("00");
					}
					socketcmd.createCmdString();
					for (SocketCmd sc : ds.getCmdPool()) {
						if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
							// data.setError("已有相同指令正在执行!");
							try {
								myprint(ds, socketcmd.getCmdString());

								data.setError("");// response返回结果,指令成功发出
								break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}
					if (data.getError().equals("The device is offline or you haven't added the device yet!")) {
						socketcmd.getTimer().schedule(new TimerTask() {
							private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
							private SocketCmd thisCmd = socketcmd;

							public void run() {
								this.cancel();
								if (cmdPool.contains(thisCmd)) {
									cmdPool.remove(thisCmd);
								}
							}
						}, cmdDelay);// 毫秒
						ds.getCmdPool().add(socketcmd);
						// System.out.println("指令已发出");
						try {
							myprint(ds, socketcmd.getCmdString());

							data.setError("");// response返回结果,指令成功发出
							break;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/setBrightnessByDev") // 设置集控器下所有zigbee节点亮度
	public void setBrightnessByDev(HttpServletRequest request, HttpServletResponse response) {
		String devMac = request.getParameter("devMac");
		String brightness = request.getParameter("brightness");
		int bright = Integer.parseInt(brightness);
		if (bright > 15) {
			brightness = Integer.toHexString(bright);
		} else {
			brightness = "0" + Integer.toHexString(bright);
		}
		brightness = brightness.toUpperCase();

		DataObject data = new DataObject();

//		data.setError("该设备离线或您尚未添加该设备!");
		data.setError("The device is offline or you haven't added the device yet!");
		if (SocketTool.socketList != null) {

			for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
				if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(devMac)) {// 找到设备对应的scoket线程
					SocketCmd socketcmd = new SocketCmd();
					socketcmd.setLength("0C");
					socketcmd.setCmd0("29");
					socketcmd.setCmd1("02");
					socketcmd.setAddr_mode("02");
					socketcmd.setAddr_type("FFFF");
					socketcmd.setDst_ep("0A");
					socketcmd.setCluster_id("0008");
					socketcmd.setCmd_type("01");
					socketcmd.setCmd_id("00");
					socketcmd.setPayload(brightness + "000000");
					socketcmd.createCmdString();
					for (SocketCmd sc : ds.getCmdPool()) {
						if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
							// data.setError("已有相同指令正在执行!");
							try {
								myprint(ds, socketcmd.getCmdString());

								data.setError("");// response返回结果,指令成功发出
								break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}
					if (data.getError().equals("The device is offline or you haven't added the device yet!")) {
						socketcmd.getTimer().schedule(new TimerTask() {
							private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
							private SocketCmd thisCmd = socketcmd;

							public void run() {
								this.cancel();
								if (cmdPool.contains(thisCmd)) {
									cmdPool.remove(thisCmd);
								}
							}
						}, cmdDelay);// 毫秒
						ds.getCmdPool().add(socketcmd);
						// System.out.println("指令已发出");
						try {
							myprint(ds, socketcmd.getCmdString());

							data.setError("");// response返回结果,指令成功发出
							break;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/setBrightness") // 设置zigbee节点亮度
	public void setBrightness(HttpServletRequest request, HttpServletResponse response) {
		String zigbeeMac = request.getParameter("zigbeeMac");
		String brightness = request.getParameter("brightness");
		int bright = Integer.parseInt(brightness);
		if (bright > 15) {
			brightness = Integer.toHexString(bright);
		} else {
			brightness = "0" + Integer.toHexString(bright);
		}
		brightness = brightness.toUpperCase();

		Zigbee zb = userService.getZigbeeByMac(zigbeeMac);

		DataObject data = new DataObject();

		if (zb != null) {
//			data.setError("无法连接到该节点所在集控器!");
			data.setError("Unable to connect to the controller where the node is located!");
			if (SocketTool.socketList != null) {

				for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
					if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(zb.getDevMac())) {// 找到设备对应的scoket线程
						SocketCmd socketcmd = new SocketCmd();
						socketcmd.setLength("0C");
						socketcmd.setCmd0("29");
						socketcmd.setCmd1("02");
						socketcmd.setAddr_mode("02");
						socketcmd.setAddr_type(zb.getZigbeeSaddr());
						socketcmd.setDst_ep("0A");
						socketcmd.setCluster_id("0008");
						socketcmd.setCmd_type("01");
						socketcmd.setCmd_id("00");
						socketcmd.setPayload(brightness + "000000");
						socketcmd.createCmdString();
						for (SocketCmd sc : ds.getCmdPool()) {
							if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
								// data.setError("已有相同指令正在执行!");
								try {
									myprint(ds, socketcmd.getCmdString());

									data.setError("");// response返回结果,指令成功发出
									break;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
							if (sc.getAddr_type().equals(socketcmd.getAddr_type())) {
//								data.setError("已有相同地址的指令正在执行!");
								data.setError("Instructions that already have the same address are being executed");
								break;
							}
						}
						if (data.getError().equals("Unable to connect to the controller where the node is located!")) {
							socketcmd.getTimer().schedule(new TimerTask() {
								private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
								private SocketCmd thisCmd = socketcmd;

								public void run() {
									this.cancel();
									if (cmdPool.contains(thisCmd)) {
										cmdPool.remove(thisCmd);
									}
								}
							}, cmdDelay);// 毫秒
							ds.getCmdPool().add(socketcmd);
							// System.out.println("指令已发出");
							try {
								myprint(ds, socketcmd.getCmdString());

								data.setError("");// response返回结果,指令成功发出
								break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
//			data.setError("节点不存在!");
			data.setError("Node does not exist!");
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/switchZigbee") // 控制zigbee节点
	public void switchZigbee(HttpServletRequest request, HttpServletResponse response) {
		String zigbeeMac = request.getParameter("zigbeeMac");
		String cmd = request.getParameter("cmd");

		Zigbee zb = userService.getZigbeeByMac(zigbeeMac);

		DataObject data = new DataObject();

		if (zb != null) {
//			data.setError("无法连接到该节点所在集控器!");
			data.setError("Unable to connect to the controller where the node is located!");
			if (SocketTool.socketList != null) {

				for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
					if (ds.getDevice().getDevMac() != null && ds.getDevice().getDevMac().equals(zb.getDevMac())) {// 找到设备对应的scoket线程
						SocketCmd socketcmd = new SocketCmd();
						socketcmd.setLength("08");
						socketcmd.setCmd0("29");
						socketcmd.setCmd1("02");
						socketcmd.setAddr_mode("02");
						socketcmd.setAddr_type(zb.getZigbeeSaddr());
						socketcmd.setDst_ep("0A");
						socketcmd.setCluster_id("0006");
						socketcmd.setCmd_type("01");
						if (cmd.equals("打开")) {
							socketcmd.setCmd_id("01");
						} else if (cmd.equals("关闭")) {
							socketcmd.setCmd_id("00");
						}
						socketcmd.createCmdString();
						for (SocketCmd sc : ds.getCmdPool()) {
							if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
								// data.setError("已有相同指令正在执行!");
								try {
									myprint(ds, socketcmd.getCmdString());

									data.setError("");// response返回结果,指令成功发出
									break;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
						if (data.getError().equals("Unable to connect to the controller where the node is located!")) {
							socketcmd.getTimer().schedule(new TimerTask() {
								private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
								private SocketCmd thisCmd = socketcmd;

								public void run() {
									this.cancel();
									if (cmdPool.contains(thisCmd)) {
										cmdPool.remove(thisCmd);
									}
								}
							}, cmdDelay);// 毫秒
							ds.getCmdPool().add(socketcmd);
							// System.out.println("指令已发出");
							try {
								myprint(ds, socketcmd.getCmdString());

								data.setError("");// response返回结果,指令成功发出
								break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
//			data.setError("节点不存在!");
			data.setError("Node does not exist!");
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/addGroup") // 为用户添加新的控制组
	public void addGroup(HttpServletRequest request, HttpServletResponse response) {

		String groupName = request.getParameter("groupName");

		Integer userid = Integer.parseInt(request.getParameter("userid"));

		DataObject data = userService.addGroupToUser(userid, groupName);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/removeGroup") // 删除分组
	public void removeGroup(HttpServletRequest request, HttpServletResponse response) {

		Integer groupid = Integer.parseInt(request.getParameter("groupid"));

		Integer userid = Integer.parseInt(request.getParameter("userid"));

		DataObject data = userService.removeGroupFromUser(userid, groupid);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/renameGroup") // 组重命名
	public void renameGroup(HttpServletRequest request, HttpServletResponse response) {

		Integer groupid = Integer.parseInt(request.getParameter("groupid"));

		String groupNewName = request.getParameter("groupNewName");

		DataObject data = userService.renameGroup(groupid, groupNewName);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/addZigbeeToGroup")
	public void addZigbeeToGroup(HttpServletRequest request, HttpServletResponse response) {

		String zigbeeListString = request.getParameter("zigbeeList");
		Integer groupid = Integer.parseInt(request.getParameter("groupid"));

		String[] zigbeeMacList = zigbeeListString.split(",");
		ArrayList<Zigbee> zigbeeList = new ArrayList<>();
		Zigbee temp;
		for (int i = 0; i < zigbeeMacList.length; i++) {
			temp = userService.getZigbeeByMac(zigbeeMacList[i]);
			if (temp != null) {
				zigbeeList.add(temp);
			}
		}

		Group group = userService.getGroupById(groupid);
		DataObject data = new DataObject();

		if (group != null) {
			String groupaddr = groupidToAddr(group.getGroupid());

//			data.setError("无法连接离线集控器!");
			data.setError("Unable to connect offline controller!");
			if (SocketTool.socketList != null) {

				for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
					for (Zigbee zb : zigbeeList) {

						if (ds.getDevice().getDevMac() != null && zb.getDevMac().equals(ds.getDevice().getDevMac())
								&& ds.getDevice().getUserid().equals(group.getUserid())) {// 集控器用户id与组用户id要一致
							SocketCmd socketcmd = new SocketCmd();
							socketcmd.setLength("10");
							socketcmd.setCmd0("29");
							socketcmd.setCmd1("02");
							socketcmd.setAddr_mode("02");
							socketcmd.setAddr_type(zb.getZigbeeSaddr());// 短地址
							socketcmd.setDst_ep("0A");
							socketcmd.setCluster_id("0004");
							socketcmd.setCmd_type("01");
							socketcmd.setCmd_id("00");
							socketcmd.setPayload(groupaddr + "05" + "0000000000");
							socketcmd.createCmdString();

							for (SocketCmd sc : ds.getCmdPool()) {
								if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
									// data.setError("已有相同指令正在执行!");
									try {
										myprint(ds, socketcmd.getCmdString());

										//data.setError("");// response返回结果,指令成功发出
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;
								}
							}
							if (data.getError().equals("Unable to connect offline controller!")) {
								socketcmd.getTimer().schedule(new TimerTask() {
									private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
									private SocketCmd thisCmd = socketcmd;

									public void run() {
										this.cancel();
										if (cmdPool.contains(thisCmd)) {
											cmdPool.remove(thisCmd);
										}
									}
								}, cmdDelay);// 毫秒
								ds.getCmdPool().add(socketcmd);
								try {
									myprint(ds, socketcmd.getCmdString());

									//data.setError("");// response返回结果,指令成功发出
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}

		} else {
//			data.setError("分组不存在！");
			data.setError("Group does not exist!");
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {
			if (data.getError().equals("Unable to connect offline controller!")) {
				data.setError("");
			}
			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/refreshGroupMsgOfZigbee")
	public void refreshGroupMsgOfZigbee(HttpServletRequest request, HttpServletResponse response) {
		String zigbeeMac = request.getParameter("zigbeeMac");
		DataObject data = new DataObject();
		//根据zigbee的mac地址从数据库读出zigbee的节点信息
		Zigbee zigbee = userService.getZigbeeByMac(zigbeeMac);
		if (zigbee != null) {
			//获取集控器地址和网络地址
			if (zigbee.getDevMac() != null && zigbee.getZigbeeSaddr() != null) {
				//编辑指令，发送，加入指令池
//				data.setError("无法连接离线集控器!");
				data.setError("Unable to connect offline controller!");
				if (SocketTool.socketList != null) {//集控器列表不为空
					for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
						if (ds.getDevice().getDevMac() != null && zigbee.getDevMac().equals(ds.getDevice().getDevMac())) {//匹配mac地址
							SocketCmd socketcmd = new SocketCmd();
							socketcmd.setLength("09");
							socketcmd.setCmd0("29");
							socketcmd.setCmd1("02");
							socketcmd.setAddr_mode("02");
							socketcmd.setAddr_type(zigbee.getZigbeeSaddr());// 短地址
							socketcmd.setDst_ep("0A");
							socketcmd.setCluster_id("0004");
							socketcmd.setCmd_type("01");
							socketcmd.setCmd_id("02");
							socketcmd.setPayload("00");
							socketcmd.createCmdString();

							for (SocketCmd sc : ds.getCmdPool()) {
								if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
									// data.setError("已有相同指令正在执行!");
									try {
										myprint(ds, socketcmd.getCmdString());

										data.setError("");// response返回结果,指令成功发出
										break;
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;
								}
							}
							if (data.getError().equals("Unable to connect offline controller!")) {
								socketcmd.getTimer().schedule(new TimerTask() {
									private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
									private SocketCmd thisCmd = socketcmd;

									public void run() {
										this.cancel();
										if (cmdPool.contains(thisCmd)) {
											cmdPool.remove(thisCmd);
										}
									}
								}, cmdDelay);// 毫秒
								ds.getCmdPool().add(socketcmd);
								// System.out.println("指令已发出");
								try {
									myprint(ds, socketcmd.getCmdString());

									data.setError("");// response返回结果,指令成功发出
									break;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		} else {//zigbee不存在，或参数错误
//			data.setError("节点不存在");
			data.setError("Node does not exist!");
		}
	}

	@RequestMapping("/removeZigbeeFromGroup")
	public void removeZigbeeFromGroup(HttpServletRequest request, HttpServletResponse response) {

		String zigbeeMac = request.getParameter("zigbeeMac");
		Integer groupid = Integer.parseInt(request.getParameter("groupid"));

		// System.out.println(zigbeeMac + "-----" + groupid.toString());
		Zigbee zb = userService.getZigbeeByMac(zigbeeMac);
		Group group = userService.getGroupById(groupid);

		DataObject data = new DataObject();
		if (zb != null && group != null) {
			String groupaddr = groupidToAddr(group.getGroupid());

//			data.setError("无法连接离线集控器!");
			data.setError("Unable to connect offline controller!");
			if (SocketTool.socketList != null) {

				for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
					if (ds.getDevice().getDevMac() != null && zb.getDevMac().equals(ds.getDevice().getDevMac())
							&& ds.getDevice().getUserid().equals(group.getUserid())) {// 集控器用户id与组用户id要一致
						SocketCmd socketcmd = new SocketCmd();
						socketcmd.setLength("0A");
						socketcmd.setCmd0("29");
						socketcmd.setCmd1("02");
						socketcmd.setAddr_mode("02");
						socketcmd.setAddr_type(zb.getZigbeeSaddr());// 短地址
						socketcmd.setDst_ep("0A");
						socketcmd.setCluster_id("0004");
						socketcmd.setCmd_type("01");
						socketcmd.setCmd_id("03");
						socketcmd.setPayload(groupaddr);
						socketcmd.createCmdString();

						for (SocketCmd sc : ds.getCmdPool()) {
							if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
								// data.setError("已有相同指令正在执行!");
								try {
									myprint(ds, socketcmd.getCmdString());

									data.setError("");// response返回结果,指令成功发出
									break;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
						if (data.getError().equals("Unable to connect offline controller!")) {
							socketcmd.getTimer().schedule(new TimerTask() {
								private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
								private SocketCmd thisCmd = socketcmd;

								public void run() {
									this.cancel();
									if (cmdPool.contains(thisCmd)) {
										cmdPool.remove(thisCmd);
									}
								}
							}, cmdDelay);// 毫秒
							ds.getCmdPool().add(socketcmd);
							// System.out.println("指令已发出");
							try {
								myprint(ds, socketcmd.getCmdString());

								data.setError("");// response返回结果,指令成功发出
								// break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
//			data.setError("节点或分组不存在！");
			data.setError("Node or group does not exist!");
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping("/switchByGroup") // 通过组控制zigbee开关
	public void switchByGroup(HttpServletRequest request, HttpServletResponse response) {
		int groupid = Integer.parseInt(request.getParameter("groupid"));
		String cmd = request.getParameter("cmd");

		Group group = userService.getGroupById(groupid);

		DataObject data = new DataObject();

		if (group != null) {

			String groupaddr = groupidToAddr(group.getGroupid());

//			data.setError("无法连接离线集控器!");
			data.setError("Unable to connect offline controller!");
			if (SocketTool.socketList != null) {

				for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
					if (ds.getDevice().getDevMac() != null && ds.getDevice().getUserid().equals(group.getUserid())) {// 集控器用户id与组用户id要一致
						SocketCmd socketcmd = new SocketCmd();
						socketcmd.setLength("08");
						socketcmd.setCmd0("29");
						socketcmd.setCmd1("02");
						socketcmd.setAddr_mode("01");// 地址模式为：组地址模式
						socketcmd.setAddr_type(groupaddr);// 组地址
						socketcmd.setDst_ep("0A");
						socketcmd.setCluster_id("0006");
						socketcmd.setCmd_type("01");
						if (cmd.equals("打开")) {
							socketcmd.setCmd_id("01");
						} else if (cmd.equals("关闭")) {
							socketcmd.setCmd_id("00");
						}
						socketcmd.createCmdString();
						for (SocketCmd sc : ds.getCmdPool()) {
							if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
								// data.setError("已有相同指令正在执行!");
								try {
									myprint(ds, socketcmd.getCmdString());

									//data.setError("");// response返回结果,指令成功发出
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
						if (data.getError().equals("Unable to connect offline controller!")) {
							socketcmd.getTimer().schedule(new TimerTask() {
								private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
								private SocketCmd thisCmd = socketcmd;

								public void run() {
									this.cancel();
									if (cmdPool.contains(thisCmd)) {
										cmdPool.remove(thisCmd);
									}
								}
							}, cmdDelay);// 毫秒
							ds.getCmdPool().add(socketcmd);
							// System.out.println("指令已发出");
							try {
								myprint(ds, socketcmd.getCmdString());

								//data.setError("");// response返回结果,指令成功发出
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
//			data.setError("分组不存在!");
			data.setError("Group does not exist!");
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {
			if (data.getError().equals("Unable to connect offline controller!")) {
				data.setError("");
			}
			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/setBrightnessByGroup") // 通过组地址设置zigbee节点亮度
	public void setBrightnessByGroup(HttpServletRequest request, HttpServletResponse response) {
		int groupid = Integer.parseInt(request.getParameter("groupid"));
		String brightness = request.getParameter("brightness");
		int bright = Integer.parseInt(brightness);
		if (bright > 15) {
			brightness = Integer.toHexString(bright);
		} else {
			brightness = "0" + Integer.toHexString(bright);
		}
		brightness = brightness.toUpperCase();

		Group group = userService.getGroupById(groupid);

		DataObject data = new DataObject();

		if (group != null) {

			String groupaddr = groupidToAddr(group.getGroupid());

//			data.setError("无法连接离线集控器!");
			data.setError("Unable to connect offline controller!");
			if (SocketTool.socketList != null) {

				for (DeviceSocket ds : SocketTool.socketList) {// 获取socket列表内是否有该设备
					if (ds.getDevice().getUserid().equals(group.getUserid())) {// 找到设备对应的scoket线程
						SocketCmd socketcmd = new SocketCmd();
						socketcmd.setLength("0C");
						socketcmd.setCmd0("29");
						socketcmd.setCmd1("02");
						socketcmd.setAddr_mode("01");// 地址模式为：组地址模式
						socketcmd.setAddr_type(groupaddr);// 组地址
						socketcmd.setDst_ep("0A");
						socketcmd.setCluster_id("0008");
						socketcmd.setCmd_type("01");
						socketcmd.setCmd_id("00");
						socketcmd.setPayload(brightness + "000000");
						socketcmd.createCmdString();
						for (SocketCmd sc : ds.getCmdPool()) {
							if (sc.getCmdString().equals(socketcmd.getCmdString())) {// 有重复指令正在执行
								// data.setError("已有相同指令正在执行!");
								try {
									myprint(ds, socketcmd.getCmdString());

									//data.setError("");// response返回结果,指令成功发出
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
						if (data.getError().equals("Unable to connect offline controller!")) {
							socketcmd.getTimer().schedule(new TimerTask() {
								private ArrayList<SocketCmd> cmdPool = ds.getCmdPool();
								private SocketCmd thisCmd = socketcmd;

								public void run() {
									this.cancel();
									if (cmdPool.contains(thisCmd)) {
										cmdPool.remove(thisCmd);
									}
								}
							}, cmdDelay);// 毫秒
							ds.getCmdPool().add(socketcmd);
							// System.out.println("指令已发出");
							try {
								myprint(ds, socketcmd.getCmdString());

								//data.setError("");// response返回结果,指令成功发出
								// break;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
//			data.setError("分组不存在!");
			data.setError("Group does not exist!");
		}

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {
			if (data.getError().equals("Unable to connect offline controller!")) {
				data.setError("");
			}
			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/addPloy") // 为用户添加新的策略
	public void addPloy(HttpServletRequest request, HttpServletResponse response) {

		String ployName = request.getParameter("ployName");

		Integer bindType = Integer.parseInt(request.getParameter("bindType"));

		String bindData = request.getParameter("bindData");

		Integer userid = Integer.parseInt(request.getParameter("userid"));

		Integer timeZone = Integer.parseInt(request.getParameter("timeZone"));

		Ploy ploy = new Ploy();
		ploy.setPloyName(ployName);
		ploy.setBindType(bindType);
		ploy.setBindData(bindData);
		ploy.setUserid(userid);
		ploy.setTimeZone(timeZone);
		ploy.setStatus(0);

		DataObject data = userService.addPloyToUser(ploy);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/removePloy") // 删除策略
	public void removePloy(HttpServletRequest request, HttpServletResponse response) {

		Integer id = Integer.parseInt(request.getParameter("id"));

		DataObject data = userService.removePloyById(id);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/renamePloy") // 策略重命名
	public void renamePloy(HttpServletRequest request, HttpServletResponse response) {

		Integer id = Integer.parseInt(request.getParameter("id"));

		String newName = request.getParameter("newName");

		DataObject data = userService.renamePloyById(id, newName);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/ployRefresh") // 刷新
	public void ployRefresh(HttpServletRequest request, HttpServletResponse response) {

		Integer userid = Integer.parseInt(request.getParameter("userid"));

		DataObject data = userService.ployRefresh(userid);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {
			// System.out.println(data.toString());
			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/ploySwitch") // 打开、关闭策略
	public void ploySwitch(HttpServletRequest request, HttpServletResponse response) {

		Integer id = Integer.parseInt(request.getParameter("id"));

		Integer status = Integer.parseInt(request.getParameter("status"));

		DataObject data = userService.switchPloyById(id, status);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/changePloyBind") // 更改策略的设备、分组、节点绑定
	public void changePloyBind(HttpServletRequest request, HttpServletResponse response) {

		Integer id = Integer.parseInt(request.getParameter("id"));

		Integer bindType = Integer.parseInt(request.getParameter("bindType"));

		String bindData = request.getParameter("bindData");

		DataObject data = userService.changePloyBindById(id, bindType, bindData);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/savePloyEdit") // 保存编辑
	public void savePloyEdit(HttpServletRequest request, HttpServletResponse response) {

		ArrayList<PloyOperate> list = (ArrayList<PloyOperate>) JSONObject.parseArray(request.getParameter("operateArr"),
				PloyOperate.class);
		Integer ployid = Integer.parseInt(request.getParameter("ployid"));

		DataObject data = userService.savePloyEditById(ployid, list);

		response.setContentType("text/html;charset=utf-8");// 响应到前台为utf-8,防止出现中文乱码

		try {

			response.getWriter().write(JSONObject.toJSONString(data));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
