package com.project.bsky.service;

import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 11/07/2023 - 4:09 PM
 */
public interface OldBlockedClaimMonitoringService {
    Object getOldBlockedClaimList(Map<String, Object> request) throws Exception;
    Map<String, Object> submitOldBlockedActionDetails(Map<String, Object> request);
    void downloadOldDataDoc(String fileName, String year, String hospitalCode, HttpServletResponse response);
	
    Map<String,Object> viewblockeddataactioncount(Long userId, String stateCode, String districtCode, Date fromDate, Date toDate,
			String hospitalCode, String flag, Integer clmstatus) throws Exception;

 }
