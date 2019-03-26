package com.waho.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.waho.domain.Node;
import com.waho.service.UserService;
import com.waho.service.impl.UserServiceImpl;

/**
 * Servlet implementation class NodeFormServlet
 */
@WebServlet("/nodeFormServlet")
public class NodeFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NodeFormServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取参数
		request.setAttribute("nodeid", request.getParameter("nodeid"));
		// 调用逻辑
		UserService us = new UserServiceImpl();
		Node node = us.getNodeByIdString(request.getParameter("nodeid"));
		// 分发转向
		if (node != null) {
			if (node.getSwitchState() == 1) {
				request.setAttribute("switchState", true);
			} else {
				request.setAttribute("switchState", false);
			}
			request.setAttribute("percentage", node.getPrecentage());
		}
		

		request.getRequestDispatcher("/admin/nodeForm.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
