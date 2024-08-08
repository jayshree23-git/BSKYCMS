package com.project.bsky.service;

import org.json.JSONObject;

import com.project.bsky.bean.DashboardBean;

/**
 * @author ronauk
 *
 */
public interface DashboardService {
	
	JSONObject getSnaDashboardReport(DashboardBean bean);
	
	JSONObject getHospitalDashboardReport(DashboardBean bean);
	
	JSONObject getCpdDashboardReport(DashboardBean bean);

}
