package com.project.bsky.service;

import java.util.List;
import java.util.Map;

public interface DgoReportService {

//	Map<Long,List<Object>> getDgoCallCenterData(String userId, String formDate, String toDate, String action, String hospitalCode,
//			Long cceId, Integer cceUserId,Integer pageIn, Integer pageEnd);

	Map<Long, List<Object>> getDgoCallCenterData(String userId, String formDate, String toDate, String action,
			String state, String district, String hospitalCode, Long cceId, Integer cceUserId, Integer pageIn,
			Integer pageEnd);

//	List<Object> getDgoCallCenterData(String userId, String formDate, String toDate, String action, String hospitalCode,
//			Long cceId, Integer cceUserId, Integer pageIn, Integer pageEnd);

}
