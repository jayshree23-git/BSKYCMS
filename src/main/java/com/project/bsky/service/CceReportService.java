package com.project.bsky.service;

import java.util.List;

public interface CceReportService {

//	List<Object> getCceOutBoundData(String userId, String formDate, String toDate, String action, String hospitalCode,
//			Long cceId);

//	List<Object> getCceReport(String userId, String formDate, String toDate, String action, String hospitalCode,
//			Long cceId);

	List<Object> getCceReport(String userId,String formDate, String toDate, String action, String hospitalCode);

	List<Object> getCceTotalCountedDetails(String userId,String fromDate, String toDate, String action, String hospitalCode);

}
