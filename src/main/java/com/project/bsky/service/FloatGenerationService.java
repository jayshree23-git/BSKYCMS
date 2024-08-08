package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.FloatExcelBean;
import com.project.bsky.bean.FloatReportBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.oldblocknewdischargebean;

/**
 * @author ronauk
 *
 */
public interface FloatGenerationService {

	List<Object> getFloatReport(FloatReportBean requestBean);
	
	JSONObject getSummary(FloatReportBean requestBean);
	
	Response generateExcel(FloatExcelBean bean);
	
	void downLoadFile(String fileCode, String userId, HttpServletResponse response);
	
	Integer saveFloatReport(MultipartFile pdf, FloatExcelBean bean);
	
	Integer saveReport(FloatExcelBean requestBean);
	
	JSONArray getGeneratedReports(FloatReportBean requestBean);
	
	JSONArray getAbstractFloatReport(FloatReportBean requestBean);

	List<Object> getActionWiseFloatReport(FloatReportBean requestBean);

	Map<String, List<Object>> getoldblocknewdischargelist(oldblocknewdischargebean requestBean);

}
