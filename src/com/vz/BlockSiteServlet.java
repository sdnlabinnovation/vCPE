package com.vz;

import java.io.BufferedReader;
import java.io.IOException;
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

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Servlet implementation class GetPostFW
 */
@WebServlet("/BlockSite")
public class BlockSiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlockSiteServlet() {
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
		//boolean blockYoutube = Boolean.valueOf(request.getParameter("blockYoutube"));
		String urlString=request.getParameter("urlStr");
		String ruleName="Custom-"+request.getParameter("ruleStr");
		System.out.println("url: " + urlString);
		System.out.println("rule name:"+ruleName);
		//String urlString="https://www.google.co.in";
		InetAddress address=null;
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
		System.out.println("Ip address of website is:"+ip);
		
		//String host = "192.168.0.68";
		String host=ConfigReader.getIPValue();
		System.out.println(host);
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
			String cmd="uci add firewall rule";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();			
			BufferedReader buff=new BufferedReader(new InputStreamReader(outStream));
			String ruleId=buff.readLine();
			System.out.println("Firewall rule added is: "+ruleId);
				
			
			channel.disconnect();
			
			channel = session.openChannel("exec");
			cmd="uci set firewall."+ruleId+".port='tcp udp'";
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			
			channel = session.openChannel("exec");
			cmd="uci set firewall."+ruleId+".name='"+ruleName+"'";
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			
			channel = session.openChannel("exec");
			cmd="uci set firewall."+ruleId+".src='lan'";
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			channel = session.openChannel("exec");
			cmd="uci set firewall."+ruleId+".dest='wan'";
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			channel = session.openChannel("exec");
			cmd="uci set firewall."+ruleId+".dest_ip='"+ip+"'";
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			channel = session.openChannel("exec");
			cmd="uci set firewall."+ruleId+".target='REJECT'";
			((ChannelExec) channel).setCommand(cmd);
			outStream = channel.getInputStream();			
			channel.connect();			
			channel.disconnect();
			
			channel = session.openChannel("exec");
			cmd="uci commit";
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
