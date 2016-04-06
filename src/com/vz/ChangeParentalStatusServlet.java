package com.vz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Servlet implementation class EnableParentalControlRulesServlet
 */
@WebServlet("/ChangeParentalStatusServlet")
public class ChangeParentalStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeParentalStatusServlet() {
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
	public void addRules(String[] urlStrings, String category) {
		int ruleNo = 0;
		for (String urlString : urlStrings) {
			ruleNo++;
			String ruleName = category + ruleNo;
			System.out.println("url: " + urlString);
			System.out.println("rule name:" + ruleName);
			// String urlString="https://www.google.co.in";
			InetAddress address = null;
			try {
				address = InetAddress.getByName(new URL(urlString).getHost());
				HostsFileManager.addHost(address);

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String ip = address.getHostAddress();
			System.out.println("Ip address of website is:" + ip);

			//String host = "192.168.0.68";
			String host=ConfigReader.getIPValue();
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

				channel = session.openChannel("exec");
				String cmd = "uci add firewall rule";
				((ChannelExec) channel).setCommand(cmd);
				InputStream outStream = channel.getInputStream();
				channel.connect();
				BufferedReader buff = new BufferedReader(new InputStreamReader(outStream));
				String ruleId = buff.readLine();
				System.out.println("Firewall rule added is: " + ruleId);

				channel.disconnect();

				channel = session.openChannel("exec");
				cmd = "uci set firewall." + ruleId + ".port='tcp udp'";
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();
				channel.disconnect();

				channel = session.openChannel("exec");
				cmd = "uci set firewall." + ruleId + ".name='" + ruleName + "'";
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();
				channel.disconnect();

				channel = session.openChannel("exec");
				cmd = "uci set firewall." + ruleId + ".src='lan'";
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();
				channel.disconnect();

				channel = session.openChannel("exec");
				cmd = "uci set firewall." + ruleId + ".dest='wan'";
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();
				channel.disconnect();

				channel = session.openChannel("exec");
				cmd = "uci set firewall." + ruleId + ".dest_ip='" + ip + "'";
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();
				channel.disconnect();

				channel = session.openChannel("exec");
				cmd = "uci set firewall." + ruleId + ".target='REJECT'";
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();
				channel.disconnect();

				channel = session.openChannel("exec");
				cmd = "uci commit firewall";
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();
				channel.disconnect();

				channel = session.openChannel("exec");
				//cmd="/etc/init.d/firewall restart";
				cmd="sh run12_on.sh";
				System.out.println("restarting :"+cmd);
				((ChannelExec) channel).setCommand(cmd);
				outStream = channel.getInputStream();
				channel.connect();		
				buff = new BufferedReader(new InputStreamReader(outStream));
				String line = buff.readLine();
				System.out.println("------Restarting Firewall--------");
				while(line!=null){
					System.out.println(line);
					line = buff.readLine();
				}
				
					
				channel.disconnect();
							

				session.disconnect();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private String getRuleIds(String category) {
		//String host = "192.168.0.68";
		String host=ConfigReader.getIPValue();
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
			channel = session.openChannel("exec");
			String cmd = "uci show firewall";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();
			channel.connect();
			BufferedReader buff = new BufferedReader(new InputStreamReader(outStream));
			String line = buff.readLine();
			StringBuffer listRules = new StringBuffer();
			while (line != null) {

				if (line.contains("=rule")) {
					// Beginning of a rule
					System.out.println(line);
					line = buff.readLine();
					boolean ruleEnabled = true;
					boolean hasName = false;
					while (line != null && !(line.contains("=rule"))) {
						// Inside the Rule
						String identifier = ".name='" + category;
						if (line.contains(identifier)) {
							hasName = true;
							int ruleNostart = line.indexOf("[") + 1;
							int ruleNoEnd = line.indexOf("]");
							listRules.append(line.substring(ruleNostart, ruleNoEnd) + ":");
							// listRules.append(":" +
							// line.substring(line.indexOf(".name") + 6));
						}

						line = buff.readLine();
					}

				} else {
					line = buff.readLine();
				}
			}
			System.out.println(category + " Rules In Firewall : " + listRules.toString());

			return listRules.toString();

			// response.getWriter().write(listRules.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public void deleteRules(String ruleIds) {
		// After deletion rule ids get shifted

		String rulesArray[] = ruleIds.split(":");

		//String host = "192.168.0.68";
		String host=ConfigReader.getIPValue();
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
			String dummyRuleId = "";
			String firstRuleId = rulesArray[0];
			for (String ruleId : rulesArray) {
				
				
			    channel = session.openChannel("exec");
				String cmd = "uci get firewall.@rule[" + firstRuleId + "].dest_ip";
				System.out.println(cmd);
				((ChannelExec) channel).setCommand(cmd);
				InputStream outStream = channel.getInputStream();
				channel.connect();
				BufferedReader buff = new BufferedReader(new InputStreamReader(outStream));
				String address = buff.readLine();
				InetAddress siteDeleted=InetAddress.getByName(address);
				HostsFileManager.removeHost(siteDeleted);
				channel.disconnect();		
						
				channel = session.openChannel("exec");
				cmd = "uci delete firewall.@rule[" + firstRuleId + "]";
				System.out.println(cmd);
				((ChannelExec) channel).setCommand(cmd);
				channel.connect();
				channel.disconnect();

			}
			channel = session.openChannel("exec");
			String cmd = "uci commit firewall";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();
			channel.connect();
			channel.disconnect();

			channel = session.openChannel("exec");
			//cmd="/etc/init.d/firewall restart";
			cmd="sh run12_on.sh";
			System.out.println("restarting :"+cmd);
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();
			channel.connect();		
			BufferedReader buff = new BufferedReader(new InputStreamReader(outStream));
			String line = buff.readLine();
			System.out.println("------Restarting Firewall--------");
			while(line!=null){
				System.out.println(line);
				line = buff.readLine();
			}
			
				
			channel.disconnect();
						

			session.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/*
	 * public static void main(String args[]){
	 * //addRules(getSitesList("SocialMedia"),"SocialMedia");
	 * //deleteRules(getRuleIds("SocialMedia"));
	 * if(getRuleIds("SocialMedia").equals("")){ System.out.println("works"); };
	 * }
	 */

	private String[] getSitesList(String category) {
		String sitesListStr = ConfigReader.getSitesList(category);
		System.out.println(sitesListStr);
		String sitesAr[] = sitesListStr.split(";");
		/*
		 * for(String site: sitesAr){ System.out.println("Site is : "+site); }
		 */
		return sitesAr;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Changing Parental Status :");
		// Change Config File
		String parentalStatus = request.getParameter("Parental");
		ConfigReader.setParentalControlEnableValue(parentalStatus);
		String socialMediaStatus = request.getParameter("SocialMedia");
		ConfigReader.setSocialMediaEnableValue(socialMediaStatus);
		String adultContentStatus = request.getParameter("AdultContent");
		ConfigReader.setAdultContentEnableValue(adultContentStatus);
		String blogsStatus = request.getParameter("Blogs");
		ConfigReader.setBlogsEnableValue(blogsStatus);

		// Change OpenWRT
		StringBuffer responseString = new StringBuffer();
		if (ConfigReader.getParentalControlEnableValue().equals("Enabled")) {
			responseString.append("Parental:Enabled;");
			String category = "SocialMedia";
			if (ConfigReader.getSocialMediaEnableValue().equals("Enabled")) {
				String ruleIds = getRuleIds(category);
				if (ruleIds.equals("")) {
					String[] rulesArray = getSitesList(category);
					addRules(rulesArray, category);
					
				}
				responseString.append("SocialMedia:Enabled;");
			} else {
				String ruleIds = getRuleIds(category);
				if (!ruleIds.equals("")){
					deleteRules(ruleIds);
				}
				responseString.append("SocialMedia:Disabled;");
			}

			category = "AdultContent";
			if (ConfigReader.getAdultContentEnableValue().equals("Enabled")) {
				String ruleIds = getRuleIds(category);
				if (ruleIds.equals("")) {
					String[] rulesArray = getSitesList(category);
					addRules(rulesArray, category);
					
				}
				responseString.append("AdultContent:Enabled;");
			} else {
				String ruleIds = getRuleIds(category);
				if (!ruleIds.equals("")){
					deleteRules(ruleIds);
				}
				responseString.append("AdultContent:Disabled;");
			}

			category = "Blogs";
			if (ConfigReader.getBlogsEnableValue().equals("Enabled")) {
				String ruleIds = getRuleIds(category);
				if (ruleIds.equals("")) {
					String[] rulesArray = getSitesList(category);
					addRules(rulesArray, category);
					
				}
				responseString.append("Blogs:Enabled;");

			} else {
				String ruleIds = getRuleIds(category);
				if (!ruleIds.equals("")){
					deleteRules(ruleIds);
				}
				
				responseString.append("Blogs:Disabled;");
			}
			response.getWriter().write(responseString.toString());
			return;
		}else{
			String ruleIds = getRuleIds("SocialMedia");
			if (!ruleIds.equals("")){
				deleteRules(ruleIds);
			}
			ruleIds = getRuleIds("AdultContent");
			if (!ruleIds.equals("")){
				deleteRules(ruleIds);
			}
			ruleIds = getRuleIds("Blogs");
			if (!ruleIds.equals("")){
				deleteRules(ruleIds);
			}
		}

		// If ParentalControl Not Enabled
		response.getWriter().write("Parental:Disabled;SocialMedia:Disabled;AdultContent:Disabled;Blogs:Disabled;");

	}

}
