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
 * Servlet implementation class CheckMediaBlockServlet
 */
@WebServlet("/CheckMediaBlockServlet")
public class CheckMediaBlockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckMediaBlockServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
			StringBuffer listRules=new StringBuffer();
			while (line != null) {
				
				if(line.contains("=rule")){
					//Beginning of  a rule
					System.out.println(line);
					line=buff.readLine();
					boolean ruleEnabled=true;
					boolean hasName=false;
					while( line!=null && !(line.contains("=rule")) ){
						//Inside the Rule
						if(line.contains(".name='Block-Mediaplayer'")){
							hasName=true;
							int ruleNostart=line.indexOf("[")+1;
							int ruleNoEnd=line.indexOf("]");
							listRules.append(line.substring(ruleNostart, ruleNoEnd));
							listRules.append(":"+line.substring(line.indexOf(".name")+6));
						}
						if(line.contains(".enabled='0'")){
							ruleEnabled=false;
						}
						
						line = buff.readLine();
					}
					if(hasName==true && ruleEnabled==true){
						listRules.append(":enabled;");
					}
					if(hasName==true && ruleEnabled==false){
						listRules.append(":disabled;");
					}
					
				}else{
					line = buff.readLine();
				}
				
				
			}
			System.out.println("Rules In Firewall : "+ listRules.toString());
			response.getWriter().write(listRules.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
