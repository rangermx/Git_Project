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
 * Servlet implementation class RemoveNodeServlet
 */
@WebServlet("/removeNodeServlet")
public class RemoveNodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RemoveNodeServlet() {
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
		if (useridStr != null && "".equals(useridStr) == false) {
			int userid = Integer.parseInt(useridStr);
			Map<String, String[]> map = request.getParameterMap();
			ArrayList<Integer> idList = new ArrayList<Integer>();
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				// System.out.println("Key = " + entry.getKey() + ", Value = " +
				// entry.getValue()[0]);
				if (entry.getKey().startsWith("id") && "on".equals(entry.getValue()[0])) {
					idList.add(Integer.parseInt(entry.getKey().substring(2)));
				}
			}
			if (idList.size() > 0) {// 数据有效
				// 调用业务逻辑
				int result = us.removeNodeById(idList, userid);
				if (result > 0) {
					// 分发转向
					response.getWriter().write("成功删除了" + result + "条数据!");
				} else {
					response.getWriter().write("删除失败！");
				}
				return;
			}
		}
		response.getWriter().write("删除失败!");
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
