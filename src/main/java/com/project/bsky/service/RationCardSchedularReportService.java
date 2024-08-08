package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.RationCardSchedularReportBean;

public interface RationCardSchedularReportService {

	public List<RationCardSchedularReportBean> list(String year, String month);

	public Map<String, Object> details(Long userid, String date, String flag,String type);

}
