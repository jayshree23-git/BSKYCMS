package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.OngoingtreatmentReportBean;

public interface OngoingtreatmentReportService {

	public List<OngoingtreatmentReportBean> urnwise(String urn, String usename);

	public List<OngoingtreatmentReportBean> hospitalwise(String username, String statecode, String districtcode);
}
