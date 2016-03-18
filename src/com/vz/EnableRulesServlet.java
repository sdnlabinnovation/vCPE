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
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Servlet implementation class EnableRulesServlet
 */
@WebServlet("/EnableRulesServlet")
public class EnableRulesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnableRulesServlet() {
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
	
	private  String getRuleIds(String ruleName) {
		String host = "192.168.0.68";
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
							listRules.append(line.substring(ruleNostart, ruleNoEnd) );
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

			return listRules.toString();

			// response.getWriter().write(listRules.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/*public static void main(String args[]){
		System.out.println(getRuleIds("Block-Mediaplayer"));
	}*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String rulesEnabledValues=request.getParameter("rulesEnabledValues");
		System.out.println("Input is:"+rulesEnabledValues);
		if(rulesEnabledValues.split(":")[0].equals("Media")){
			String ruleId=getRuleIds("Block-Mediaplayer");
			rulesEnabledValues=ruleId+":"+rulesEnabledValues.split(":")[1];
			
		}
		if(rulesEnabledValues.split(":")[0].equals("Edu")){
			String ruleId=getRuleIds("Block-Education");
			rulesEnabledValues=ruleId+":"+rulesEnabledValues.split(":")[1];
		}
		String host = "192.168.0.68";
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		String rulesArray[]=rulesEnabledValues.split(";");
		try {
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			for(String rule: rulesArray){
				channel = session.openChannel("exec");
				String map[]=rule.split(":");
				String id=map[0];
				String value=map[1];
				String cmd="";
				if(value.equals("enable")){
					cmd="uci set firewall.@rule["+id+"].enabled='1'";
				}else{
					cmd="uci set firewall.@rule["+id+"].enabled='0'";
				}
				System.out.println(cmd);
				((ChannelExec) channel).setCommand(cmd);
				channel.connect();	
				channel.disconnect();
			}
			
			channel = session.openChannel("exec");
			String cmd="uci commit firewall";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			channel = session.openChannel("exec");
			cmd="/etc/init.d/firewall restart";
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			
			session.disconnect();
			
			
			
			
				
			
			
			
			
			
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
