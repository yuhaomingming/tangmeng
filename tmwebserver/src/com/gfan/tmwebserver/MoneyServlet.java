package com.gfan.tmwebserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class MoneyServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(MoneyServlet.class
			.getName());

	/**
	 * Constructor of the object.
	 */
	public MoneyServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		// Enumeration<String> ea = request.getParameterNames();
		response.setContentType("text/plain;charset=UTF-8;pageEncoding=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		String type = request.getParameter("type");
		String orderid = request.getParameter("orderid");
		String gold = request.getParameter("gold");
		String username = request.getParameter("username");
		String rolename = request.getParameter("rolename");
		String serverno = request.getParameter("serverno");
		String keystr = request.getParameter("keystr");
		int result = 0;
		if (type != null && !type.equals("") && orderid != null
				&& !orderid.equals("") && gold != null && !gold.equals("")
				&& username != null && !username.equals("") && rolename != null
				&& !rolename.equals("") && serverno != null
				&& !serverno.equals("") && keystr != null && !keystr.equals("")) {

			String privatekey = C3P0DBConnectionPool.getInstance().getconfig(
					"privatekey");
			String key = MD5Util.MD5(orderid + privatekey);
			if (keystr.equals(key)) {

				try {
					// MoneyDao.reqcharge(orderid, username, rolename, serverno,
					// Integer.parseInt(gold));
					result = MoneyDao.callchargesuccess(orderid, username,
							rolename,serverno, Integer.parseInt(gold));
				} catch (NumberFormatException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				} catch (SQLException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			} else {
				result = 0;
			}

		}
		out.println(result + "");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
