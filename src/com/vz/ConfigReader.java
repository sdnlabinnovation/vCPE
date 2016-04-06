package com.vz;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.FileOutputStream;

public class ConfigReader {

	/*
	 * public static void main (String args[]){ Scanner in=new
	 * Scanner(System.in); String line=in.next(); while(line!=null &&
	 * !(line.equals("x"))){ setParentalControlEnableValue(line);
	 * line=in.next(); } }
	 */
	/*public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		System.out.println(getSitesList("SocialMedia"));
	}
	
	
*/
	/*public static void main(String[] args) {
		//getAdultContentEnableValue();
		setParentalControlEnableValue("Enabled");
	}*/
	
	public static String getSitesList(String category) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);

			// get the property value
			String propertyName = category + "Sites";
			String sitesList = prop.getProperty(propertyName);

			prop.clear();
			return (sitesList);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}

	public static String getAdultContentEnableValue() {
		/*Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String adultContentEnable = prop.getProperty("adultContentEnable");

			prop.clear();
			return (adultContentEnable);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";*/
		String ruleId=getRuleIds("PC-Adult-Content");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci get firewall.@rule[" + ruleId + "].enabled";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();			
			BufferedReader buff=new BufferedReader(new InputStreamReader(outStream));
			String enabledValue=buff.readLine();
			if(enabledValue==null){
				System.out.println("Enabled");
				return("Enabled");
			}
			System.out.println(enabledValue);
			System.out.println("Enabled Value: "+enabledValue);
			
			if(enabledValue.equals("0")){
				System.out.println("Disbaled");
				return("Disabled");
			}else{
				System.out.println("Enabled");
				return("Enabled");
			}
			
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
		return "";
		
	}
	
	private static String getRuleIds(String ruleName) {
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
	
	public static String getIPValue() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String ip = prop.getProperty("IP");

			prop.clear();
			return (ip);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}
	
	public static String getONOSIPValue() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String ip = prop.getProperty("ONOSIP");

			prop.clear();
			return (ip);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}
	
	public static String getONOSUserValue() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String user = prop.getProperty("ONOSUser");

			prop.clear();
			return (user);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}
	public static String getKeyPathValue() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String keyPath = prop.getProperty("keyPath");

			prop.clear();
			return (keyPath);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}
	
	public static String getONOSLogPathValue() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String ONOSLogPath = prop.getProperty("ONOSLogPath");

			prop.clear();
			return (ONOSLogPath);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}
	
	public static String getONOSLogNameValue() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String ONOSLogName = prop.getProperty("ONOSLogName");

			prop.clear();
			return (ONOSLogName);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}
	
	public static String getInstanceCreateAppIPValue() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			
			
			// get the property value
			String instanceCreateAppIP = prop.getProperty("instanceCreateAppIP");

			prop.clear();
			return (instanceCreateAppIP);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}

	public static void setAdultContentEnableValue(String enableValue) {
		/*Properties prop = new Properties();
		FileOutputStream out = null;
		InputStream input = null;
		try {

			
			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			input.close();
			out = new FileOutputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");
			// set the property value
			prop.setProperty("adultContentEnable", enableValue);
			prop.store(out, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
		String ruleId=getRuleIds("PC-Adult-Content");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		String enableValueNumber="0";
		if(enableValue.equals("Enabled")){
			enableValueNumber="1";
		}
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci set firewall.@rule[" + ruleId + "].enabled='"+enableValueNumber+"'";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();	
					
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
			

	}

	public static String getBlogsEnableValue() {
		/*Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);

			// get the property value
			String blogsEnable = prop.getProperty("blogsEnable");

			prop.clear();
			return (blogsEnable);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";*/
		
		
		
		String ruleId=getRuleIds("PC-Blogs");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci get firewall.@rule[" + ruleId + "].enabled";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();			
			BufferedReader buff=new BufferedReader(new InputStreamReader(outStream));
			String enabledValue=buff.readLine();
			if(enabledValue==null){
				System.out.println("Enabled");
				return("Enabled");
			}
			System.out.println(enabledValue);
			System.out.println("Enabled Value: "+enabledValue);
			
			if(enabledValue.equals("0")){
				System.out.println("Disbaled");
				return("Disabled");
			}else{
				System.out.println("Enabled");
				return("Enabled");
			}
			
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
		return "";
		
	}

	public static void setBlogsEnableValue(String enableValue) {
		/*Properties prop = new Properties();
		FileOutputStream out = null;
		InputStream input = null;
		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			input.close();
			
			out = new FileOutputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// set the property value
			prop.setProperty("blogsEnable", enableValue);
			
			prop.store(out, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
		String ruleId=getRuleIds("PC-Blogs");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		String enableValueNumber="0";
		if(enableValue.equals("Enabled")){
			enableValueNumber="1";
		}
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci set firewall.@rule[" + ruleId + "].enabled='"+enableValueNumber+"'";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();	
					
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
			

	}
	
	public static void setIPValue(String ipAdr) {
		Properties prop = new Properties();
		FileOutputStream out = null;
		InputStream input = null;
		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			input.close();
			
			out = new FileOutputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// set the property value
			prop.setProperty("IP", ipAdr);
			
			prop.store(out, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String getSocialMediaEnableValue() {
		/*Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);

			// get the property value
			String socialMediaEnable = prop.getProperty("socialMediaEnable");

			prop.clear();
			return (socialMediaEnable);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";*/
		
		String ruleId=getRuleIds("PC-Social-Media");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci get firewall.@rule[" + ruleId + "].enabled";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();			
			BufferedReader buff=new BufferedReader(new InputStreamReader(outStream));
			String enabledValue=buff.readLine();
			if(enabledValue==null){
				System.out.println("Enabled");
				return("Enabled");
			}
			System.out.println(enabledValue);
			System.out.println("Enabled Value: "+enabledValue);
			
			if(enabledValue.equals("0")){
				System.out.println("Disbaled");
				return("Disabled");
			}else{
				System.out.println("Enabled");
				return("Enabled");
			}
			
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
		return "";
		
	}

	public static void setSocialMediaEnableValue(String enableValue) {
		/*Properties prop = new Properties();
		FileOutputStream out = null;
		InputStream input = null;
		try {

			
			
			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			input.close();
			out = new FileOutputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");
			// set the property value
			prop.setProperty("socialMediaEnable", enableValue);
			prop.store(out, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
		String ruleId=getRuleIds("PC-Social-Media");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		String enableValueNumber="0";
		if(enableValue.equals("Enabled")){
			enableValueNumber="1";
		}
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci set firewall.@rule[" + ruleId + "].enabled='"+enableValueNumber+"'";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();	
					
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
			

	}

	public static String getParentalControlEnableValue() {
		/*Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);

			// get the property value
			String parentalControlEnable = prop.getProperty("parentalControlEnable");
			
			prop.clear();
			return (parentalControlEnable);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";*/
		
		String ruleId=getRuleIds("Parental-Control");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci get firewall.@rule[" + ruleId + "].enabled";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();			
			BufferedReader buff=new BufferedReader(new InputStreamReader(outStream));
			String enabledValue=buff.readLine();
			if(enabledValue==null){
				System.out.println("Enabled");
				return("Enabled");
			}
			System.out.println(enabledValue);
			System.out.println("Enabled Value: "+enabledValue);
			
			if(enabledValue.equals("0")){
				System.out.println("Disbaled");
				return("Disabled");
			}else{
				System.out.println("Enabled");
				return("Enabled");
			}
			
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
		return "";
		
	}

	public static void setParentalControlEnableValue(String enableValue) {
		/*Properties prop = new Properties();
		FileOutputStream out = null;
		InputStream input = null;
		try {

			input = new FileInputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// load a properties file
			prop.load(input);
			input.close();
			
			out = new FileOutputStream(System.getenv().get("CUSTOMIZER") + "\\config.properties");

			// set the property value
			prop.setProperty("parentalControlEnable", enableValue);
			
			prop.store(out, null);
			

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
		
		
		String ruleId=getRuleIds("Parental-Control");
		System.out.println(ruleId);
		String host=ConfigReader.getIPValue();
		System.out.println(host);
		String user = "root";
		String password = "root";
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		Channel channel = null;
		Session session=null;
		String enableValueNumber="0";
		if(enableValue.equals("Enabled")){
			enableValueNumber="1";
		}
		try {
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected through ssh");
			
			channel = session.openChannel("exec");
			String cmd="uci set firewall.@rule[" + ruleId + "].enabled='"+enableValueNumber+"'";
			((ChannelExec) channel).setCommand(cmd);
			InputStream outStream = channel.getInputStream();			
			channel.connect();	
					
			
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			channel.disconnect();
			session.disconnect();
		}
			

	}

}
