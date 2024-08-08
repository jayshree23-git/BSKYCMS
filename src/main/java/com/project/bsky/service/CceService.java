package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.CceGroupBean;
import com.project.bsky.bean.Response;

public interface CceService {

	List<Object> gettransactionInformation(Long userId, String action);

	Response saveCce(CceGroupBean cce);

	List<Object> getNotConnected(Long userId, String action);

	Response saveNotConnectedCce(CceGroupBean cceNot);

	List<Object> getAllCce(Long userId, String action);

	Response saveReAssignedCce(CceGroupBean cceNot);

	Map<Long, List<Object>> getallCceData(String formDate, String toDate, String stateCode, String distCode, String hospitalCode, String actionBy,
			String pendingAt, String action, String status, Integer pageIn, Integer pageEnd);

	Response addGoRemark(Long id, String goRemarks, Integer action, Long goUserId);

	Map<Long, List<Object>> getallCceDataView(String formDate, String toDate, String stateCode, String distCode, String hospitalCode, String actionBy, String pendingAt, String action, String status, Integer pageIn, Integer pageEnd);
    
	Map<Long, List<Object>> getCceReSettlement(String formDate, String toDate, String stateCode, String distCode, String hospitalCode,String action);
	
	Map<Long, List<Object>> getGOInitialTakeActionData(Long userId,String formDate, String toDate, String stateCode, String distCode, String hospitalCode,String action);
	
	Map<Long, List<Object>> getallCceDataForSHASCEO(String formDate, String toDate, String stateCode, String distCode, String hospitalCode, Integer pageIn, Integer pageEnd);

	Map<String, Object> getassemblyConstituencyLgdCode();
}
