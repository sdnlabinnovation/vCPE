package com.vz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChangeParentalStatusServlet
 */
@WebServlet("/CheckParentalStatusServlet")
public class CheckParentalStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckParentalStatusServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer responseString = new StringBuffer();
		System.out.println("Checking Parental Status");
		if (ConfigReader.getParentalControlEnableValue().equals("Enabled")) {
			System.out.println("Found parental status enabled");
			responseString.append("Parental:Enabled;");
			String category = "SocialMedia";
			if (ConfigReader.getSocialMediaEnableValue().equals("Enabled")) {
				System.out.println("Found social media enabled");
				responseString.append("SocialMedia:Enabled;");
			} else {
				System.out.println("Found social media disabled");
				responseString.append("SocialMedia:Disabled;");
			}

			category = "AdultContent";
			if (ConfigReader.getAdultContentEnableValue().equals("Enabled")) {

				responseString.append("AdultContent:Enabled;");
			} else {

				responseString.append("AdultContent:Disabled;");
			}

			category = "Blogs";
			if (ConfigReader.getBlogsEnableValue().equals("Enabled")) {

				responseString.append("Blogs:Enabled;");

			} else {

				responseString.append("Blogs:Disabled;");
			}
			response.getWriter().write(responseString.toString());
			return;
		}

		// If ParentalControl Not Enabled
		System.out.println("Found parental status disabled");
		response.getWriter().write("Parental:Disabled;SocialMedia:Disabled;AdultContent:Disabled;Blogs:Disabled;");

	}

}
