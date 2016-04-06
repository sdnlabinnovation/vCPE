package com.vz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class HostsFileManager {

	/*public static void main(String[] args) {
		
		  String urlString="https://www.facebook.com/"; try { InetAddress
		  address= InetAddress.getByName(new URL(urlString).getHost());
		  addHost(address) ;
		  
		  } catch (UnknownHostException e) { // TODO Auto-generated catch block
		  e.printStackTrace(); } catch (MalformedURLException e) { 
			  // TODO   Auto-generated catch block 
			  e.printStackTrace(); }
		
		

		// String host = "192.168.0.68";
		String host = ConfigReader.getIPValue();
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;

		try {
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			String firstRuleId = "16";
			
				channel = session.openChannel("exec");
				String cmd = "uci get firewall.@rule[" + firstRuleId + "].dest_ip";
				System.out.println(cmd);
				((ChannelExec) channel).setCommand(cmd);
				InputStream outStream = channel.getInputStream();
				channel.connect();
				BufferedReader buff = new BufferedReader(new InputStreamReader(outStream));
				String address = buff.readLine();
				System.out.println("Address found:"+address);
				InetAddress siteDeleted = InetAddress.getByName(address);
				String hostname=siteDeleted.getCanonicalHostName();
				System.out.println("Host name "+hostname );
				HostsFileManager.removeHost(siteDeleted);
				channel.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		String address = "31.13.78.35";
		System.out.println("Address found:"+address);
		InetAddress siteDeleted=null;
		try {
			siteDeleted = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String hostname=siteDeleted.getCanonicalHostName();
		System.out.println("Host name "+hostname );
		HostsFileManager.removeHost(siteDeleted);
	}
*/
	public static void removeHost(InetAddress address) {
		String host = ConfigReader.getIPValue();
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		StringBuffer output = new StringBuffer();

		String urlString = address.getHostName();
		String IPaddress = address.getHostAddress();

		try {
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");

			ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			sftp.cd("/etc");
			String pwd = sftp.pwd();
			System.out.println(pwd);
			InputStream stream = sftp.get("hosts");

			File newHosts = new File(System.getenv().get("CUSTOMIZER") + "\\newHosts");
			// if file doesnt exists, then create it
			if (!newHosts.exists()) {
				newHosts.createNewFile();
			}
			FileWriter fw = new FileWriter(newHosts.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = br.readLine();
			while (line != null) {
				System.out.println(line);
				if (!(line.contains(IPaddress))) {
					output.append(line + "\n");
				}

				line = br.readLine();
			}

			// output.append(IPaddress+" "+urlString+"\n");
			System.out.println(output.toString());
			bw.write(output.toString());
			bw.close();

			sftp.put(newHosts.getAbsolutePath(), pwd + "/hosts");
			sftp.disconnect();
			channel = session.openChannel("exec");
			String cmd="/etc/init.d/dnsmasq restart";
			((ChannelExec) channel).setCommand(cmd);			
			channel.connect();			
			channel.disconnect();
			
			
			session.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public static void addHost(InetAddress address) {

		String host = ConfigReader.getIPValue();
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		StringBuffer output = new StringBuffer();

		String urlString = address.getHostName();
		String IPaddress = address.getHostAddress();

		try {
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");

			ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			sftp.cd("/etc");
			String pwd = sftp.pwd();
			System.out.println(pwd);
			InputStream stream = sftp.get("hosts");

			File newHosts = new File(System.getenv().get("CUSTOMIZER") + "\\newHosts");
			// if file doesnt exists, then create it
			if (!newHosts.exists()) {
				newHosts.createNewFile();
			}
			FileWriter fw = new FileWriter(newHosts.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = br.readLine();
			while (line != null) {
				System.out.println(line);
				if (!(line.contains(urlString))) {
					output.append(line + "\n");
				}

				line = br.readLine();
			}

			output.append(IPaddress + " " + urlString + "\n");
			System.out.println(output.toString());
			bw.write(output.toString());
			bw.close();

			sftp.put(newHosts.getAbsolutePath(), pwd + "/hosts");
			sftp.disconnect();
			channel = session.openChannel("exec");
			String cmd="/etc/init.d/dnsmasq restart";
			((ChannelExec) channel).setCommand(cmd);			
			channel.connect();			
			channel.disconnect();
			
			session.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

}
