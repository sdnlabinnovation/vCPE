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
 * Servlet implementation class ReadLogServlet
 */
@WebServlet("/ReadLogServlet")
public class ReadLogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String[] keywords = { "Created Firewall Instance", "Assigned Floating IP",
			"Port Security disabled" ,"Deleted Firewall Instance"};

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReadLogServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Reading instance craetion logs");
		String host = ConfigReader.getInstanceCreateAppIPValue();
		String user = "sdnos";
		String password = "sdnos";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		StringBuffer logsBuffer = new StringBuffer();
		try {
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
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
			InputStream stream = sftp.get("vcpe_event_track.log");
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line2 = br.readLine();
			
			while (line2 != null) {
				System.out.println(line2);
				boolean flag=true;
				for (String keyword : keywords) {
					if (line2.contains(keyword) && line2.trim().endsWith("Successfully")) {
						logsBuffer.append("<font color=\"#00FF00\">" + line2 + "</font>" + "\n");
						flag=false;
						break;
					}/*else{
						logsBuffer.append(line2 + "\n");
					}*/
				}
				if(flag==true){
					logsBuffer.append(line2 + "\n");
				}
				line2 = br.readLine();
			}
			sftp.disconnect();
			session.disconnect();
			response.getWriter().append(logsBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

}
