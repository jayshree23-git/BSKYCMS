package com.project.bsky.bean;

import java.util.List;

public class ClaimCountBean {

	private List<Object> reportList;
	private Long SnaApprovedOfCpd;

	public List<Object> getReportList() {
		return reportList;
	}

	public void setReportList(List<Object> reportList) {
		this.reportList = reportList;
	}

	public Long getSnaApprovedOfCpd() {
		return SnaApprovedOfCpd;
	}

	public void setSnaApprovedOfCpd(Long snaApprovedOfCpd) {
		SnaApprovedOfCpd = snaApprovedOfCpd;
	}

	@Override
	public String toString() {
		return "ClaimCountBean [reportList=" + reportList + ", SnaApprovedOfCpd=" + SnaApprovedOfCpd + "]";
	}

}
