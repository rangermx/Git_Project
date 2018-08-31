package com.waho.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.waho.service.UserService;
import com.waho.service.impl.UserServiceImpl;


/**
 * 接受用户提交的节点控制指令
 */
@WebServlet("/nodeServlet")
public class NodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NodeServlet() {
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
		String nodeid = request.getParameter("nodeid");
		String light1State = request.getParameter("light1State");
		String light2State = request.getParameter("light2State");
		String light1PowerPercent = request.getParameter("light1PowerPercent");
		String light2PowerPercent = request.getParameter("light2PowerPercent");
		if (nodeid != null) {// 数据有效
			// 调用业务逻辑
			us.userWriteNodeCmd(Integer.parseInt(nodeid), light1State, light2State, light1PowerPercent, light2PowerPercent);
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
