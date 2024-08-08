package com.project.bsky.bean;

/**
 * @Project : CSM Framework Backend
 * @Auther : Truptimayee Sa
 * @Created On : 20/11/2022 - 8:02 PM
 */
public class Regd {
	public String hosName;
	public String mobile;

	public String getHostName() {
		return hosName;
	}

	public void setHostName(String hostName) {
		this.hosName = hostName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "Regd{" +
			   "hostName='" + hosName + '\'' +
			   ", mobile='" + mobile + '\'' +
			   '}';
	}
}
