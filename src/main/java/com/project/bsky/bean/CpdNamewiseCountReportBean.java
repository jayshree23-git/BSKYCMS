package com.project.bsky.bean;

public class CpdNamewiseCountReportBean {
	
	public String getFormdate() {
		return formdate;
	}

	public void setFormdate(String formdate) {
		this.formdate = formdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	

	@Override
	public String toString() {
		return "CpdNamewiseCountReportBean [formdate=" + formdate + ", userId=" + userId + ", todate=" + todate + "]";
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private String formdate;
	private Long userId;
	private String todate;

}
