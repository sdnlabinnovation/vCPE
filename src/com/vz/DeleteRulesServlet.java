package com.vz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

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
 * Servlet implementation class DeleteRulesServlet
 */
@WebServlet("/DeleteRulesServlet")
public class DeleteRulesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteRulesServlet() {
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

	private String getRuleIds(String ruleName) {
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
						String identifier = ".name='" + ruleName;
						if (line.contains(identifier)) {
							hasName = true;
							int ruleNostart = line.indexOf("[") + 1;
							int ruleNoEnd = line.indexOf("]");
							listRules.append(line.substring(ruleNostart, ruleNoEnd));
							// listRules.append(":" +
							// line.substring(line.indexOf(".name") + 6));
						}

						line = buff.readLine();
					}

				} else {
					line = buff.readLine();
				}
			}
			System.out.println(ruleName + " Rules In Firewall : " + listRules.toString());
			channel.disconnect();
			session.disconnect();
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String rulesEnabledValues = request.getParameter("rulesEnabledValues");
		System.out.println("Input is:" + rulesEnabledValues);
		String tempRulesEnabledValues = "";
		String rulesAr[] = rulesEnabledValues.split(";");
		for (String rule : rulesAr) {
			String ruleMap[] = rule.split(":");
			int nameLen = ruleMap[0].length();
			String ruleName = ruleMap[0].substring(1, nameLen - 1);
			String ruleId = getRuleIds(ruleName);
			tempRulesEnabledValues = tempRulesEnabledValues+ruleId + ":" + ruleMap[1] + ";";
		}
		System.out.println("Temporary values :" + tempRulesEnabledValues);
		rulesEnabledValues = tempRulesEnabledValues;

		String rulesArray[] = rulesEnabledValues.split(";");
		StringBuffer ruleIds = new StringBuffer();
		for (String rule : rulesArray) {
			String map[] = rule.split(":");
			String id = map[0];
			String value = map[1];
			if (value.equals("enable")) {
				ruleIds.append("" + id + ":");
			}
		}
		System.out.println("Deleting rule ids: "+ruleIds.toString());
		deleteRules(ruleIds.toString());

	}

}
