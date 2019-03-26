package com.waho.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.waho.service.UserService;
import com.waho.service.impl.UserServiceImpl;

/**
 * Servlet implementation class WifiResetServlet
 */
@WebServlet("/wifiResetServlet")
public class WifiResetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WifiResetServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		UserService us = new UserServiceImpl();
		// 获取表单数据
		String useridStr = request.getParameter("userid");
		String ssid = request.getParameter("ssid");
		String password = request.getParameter("password");

		if (useridStr != null && "".equals(useridStr) == false && ssid != null && "".equals(ssid) == false
				&& password != null && "".equals(password) == false) {
			int userid = Integer.parseInt(useridStr);
			Map<String, String[]> map = request.getParameterMap();
			ArrayList<Integer> idList = new ArrayList<Integer>();
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				if (entry.getKey().startsWith("id") && "on".equals(entry.getValue()[0])) {
					idList.add(Integer.parseInt(entry.getKey().substring(2)));
				}
			}
			// 调用业务逻辑
			if (idList.size() > 0) {// 数据有效
				// 调用业务逻辑
				int result = us.userWriteWifiResetCmd(userid, ssid, password, idList);
				if (result > 0) {
					// 分发转向
					response.getWriter().write("成功发送了" + result + "条指令! 节点将在下一次断电重启后，加入新的WiFi网络。");
				} else {
					response.getWriter().write("发送失败！");
				}
				return;
			}
			return;
		}
		response.getWriter().write("发送失败!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
