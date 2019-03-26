package com.waho.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.waho.service.UserService;
import com.waho.service.impl.UserServiceImpl;

/**
 * 接受用户请求的开启节点注册指令，将指令写入数据库，返回成功
 */
@WebServlet("/nodesRegisterServlet")
public class NodesRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NodesRegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		UserService us = new UserServiceImpl();
		// 获取表单数据
		String deviceid = request.getParameter("deviceid");
		String month = request.getParameter("month");
		String day = request.getParameter("day");
		String hours = request.getParameter("hours");
		String minutes = request.getParameter("minutes");
		String keepTime = request.getParameter("keepTime");
		Date date = new Date();
		date.setDate(0);
		date.setSeconds(0);
		date.setMonth(Integer.parseInt(month) - 1);
		date.setDate(Integer.parseInt(day));
		date.setHours(Integer.parseInt(hours));
		date.setMinutes(Integer.parseInt(minutes));
		if (deviceid != null) {// 数据有效
			// 调用业务逻辑
			us.userWriteNodesRegisterOpenCmd(Integer.parseInt(deviceid), date, Integer.parseInt(keepTime));
			// 分发转向
			response.getWriter().write("提交完成!");
			return;
		}
		response.getWriter().write("提交失败!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
