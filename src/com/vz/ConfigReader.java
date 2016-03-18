package com.vz;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
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
		Properties prop = new Properties();
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

		return "";
	}

	public static void setAdultContentEnableValue(String enableValue) {
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
		}

	}

	public static String getBlogsEnableValue() {
		Properties prop = new Properties();
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

		return "";
	}

	public static void setBlogsEnableValue(String enableValue) {
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
		}

	}

	public static String getSocialMediaEnableValue() {
		Properties prop = new Properties();
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

		return "";
	}

	public static void setSocialMediaEnableValue(String enableValue) {
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
		}

	}

	public static String getParentalControlEnableValue() {
		Properties prop = new Properties();
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

		return "";
	}

	public static void setParentalControlEnableValue(String enableValue) {
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
		}

	}

}
