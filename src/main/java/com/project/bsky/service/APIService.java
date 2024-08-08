package com.project.bsky.service;

import com.project.bsky.model.MstApi;

import java.util.List;
import java.util.Map;

/**
 * @Project : BSKY Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 13/06/2023 - 1:06 PM
 */
public interface APIService {
	
	List<MstApi> getMasterAPIServices();

	List<Map<String, Object>> getReportDataList(String request) throws Exception;

	Map<String, Object> getReportDetails(String request) throws Exception;

	Map<String, Object> getOldDataDetails(String request) throws Exception;


}
