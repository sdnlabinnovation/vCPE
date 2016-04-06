package com.vz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(!(username.equals(password))){
			response.getWriter().append("Invalid");
			return;
		}
		
		/*String host = ConfigReader.getInstanceCreateAppIPValue();
		String user = "sdnos";
		String pass = "sdnos";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		
		try {
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(pass);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");

			channel = session.openChannel("exec");
			String cmd = "ls";
			System.out.println("Command executed: " + cmd);
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();
			channel.connect();
			BufferedReader buff = new BufferedReader(new InputStreamReader(outStream));
			String line = buff.readLine();

			while (line != null) {
				System.out.println(line);
				line = buff.readLine();
			}
			channel.disconnect();

			ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			String pwd = sftp.pwd();
			System.out.println(pwd);
			InputStream stream = sftp.get("user_instance.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line2 = br.readLine();
			while (line2 != null) {
				System.out.println(line2);
				String credArray []=line2.split(",");
				String userPresent="VZCus:"+username;
				if(credArray[0].equals(userPresent)){
					System.out.println("Valid Username");
					response.getWriter().append("Valid");
					ConfigReader.setIPValue(credArray[1]);
					session.disconnect();
					return;
				}
				line2 = br.readLine();
			}
			sftp.disconnect();
			session.disconnect();
			System.out.println("Invalid");
			response.getWriter().append("Invalid");
			//response.getWriter().append(logsBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}*/
		if (username.equals("Customer") && password.equals("Customer")) {
			response.getWriter().append("Valid");
		} else {
			response.getWriter().append("Invalid");
		}
	}
	
	
	
}
