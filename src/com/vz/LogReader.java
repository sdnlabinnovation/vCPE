package com.vz;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class LogReader {
	public static void main(String[] args) {
		/*
		 * String host = "10.76.110.90"; String user = "sdnos"; String password
		 * = "sdnos"; java.util.Properties config = new java.util.Properties();
		 * config.put("StrictHostKeyChecking", "no"); JSch jsch = new JSch();
		 * Channel channel = null;
		 * 
		 * try { Session session = jsch.getSession(user, host, 22);
		 * session.setPassword(password); session.setConfig(config);
		 * session.connect(); System.out.println("Connected through ssh");
		 * 
		 * channel = session.openChannel("exec"); String cmd = "ls";
		 * System.out.println("Command executed: " + cmd); ((ChannelExec)
		 * channel).setCommand(cmd); InputStream outStream =
		 * channel.getInputStream(); channel.connect(); BufferedReader buff =
		 * new BufferedReader(new InputStreamReader(outStream)); String line =
		 * buff.readLine();
		 * 
		 * while (line != null) { System.out.println(line); line =
		 * buff.readLine(); } channel.disconnect();
		 * 
		 * ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
		 * sftp.connect(); String pwd = sftp.pwd(); System.out.println(pwd);
		 * InputStream stream = sftp.get("vcpe_event_track.log"); BufferedReader
		 * br = new BufferedReader(new InputStreamReader(stream)); String line2
		 * = br.readLine(); while (line2 != null) { System.out.println(line2);
		 * line2 = br.readLine(); }
		 * 
		 * session.disconnect();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } finally {
		 * 
		 * }
		 */

		/*String host = "192.168.0.117";
		String user = "ubuntu";
		// String password = "sdnos";
		java.util.Properties config = new java.util.Properties();

		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();

		Channel channel = null;
		StringBuffer logsBuffer = new StringBuffer();
		try {
			jsch.addIdentity("C:\\Users\\TEST\\Desktop\\privateKeyOpenSSH");

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
			sftp.cd("/home/ubuntu/Applications/apache-karaf-3.0.5/data/log");
			pwd = sftp.pwd();
			System.out.println(pwd);
			InputStream stream = sftp.get("verizon.log");
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line2 = br.readLine();
			while (line2 != null) {
				System.out.println(line2);
				logsBuffer.append(line2 + "\n");
				line2 = br.readLine();
			}

			session.disconnect();
			// response.getWriter().append(logsBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}*/
		
		
		/*String username="sweta";
		String host = ConfigReader.getInstanceCreateAppIPValue();
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
					System.out.println("Valid");
					ConfigReader.setIPValue(credArray[1]);
					return;
				}
				line2 = br.readLine();
			}

			session.disconnect();
			System.out.println("Invalid");
			//response.getWriter().append(logsBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}*/
	}

}
