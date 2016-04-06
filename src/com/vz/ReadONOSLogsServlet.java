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
 * Servlet implementation class ReadONOSLogsServlet
 */
@WebServlet("/ReadONOSLogsServlet")
public class ReadONOSLogsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadONOSLogsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String host = ConfigReader.getONOSIPValue();
		String user = ConfigReader.getONOSUserValue();
		// String password = "sdnos";
		java.util.Properties config = new java.util.Properties();

		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();

		Channel channel = null;
		StringBuffer logsBuffer = new StringBuffer();
		try {
			String keyPath=ConfigReader.getKeyPathValue();
			jsch.addIdentity(keyPath);

			Session session = jsch.getSession(user, host, 22);
			// session.setPassword(password);
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
			String onosLogPath=ConfigReader.getONOSLogPathValue();
			sftp.cd(onosLogPath);
			pwd = sftp.pwd();
			System.out.println(pwd);
			String ONOSLogName=ConfigReader.getONOSLogNameValue();
			InputStream stream = sftp.get(ONOSLogName);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line2 = br.readLine();
			while (line2 != null) {
				System.out.println(line2);
				logsBuffer.append(line2 + "\n");
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
