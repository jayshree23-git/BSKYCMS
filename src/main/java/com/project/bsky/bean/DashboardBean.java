package com.project.bsky.bean;

public class DashboardBean {

	private Long userId;
	private String month;
	private String year;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "SnaDashboardBean [userId=" + userId + ", month=" + month + ", year=" + year + "]";
	}

}
