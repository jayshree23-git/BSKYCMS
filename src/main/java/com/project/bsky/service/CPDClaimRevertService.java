package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.ICDDetailsBean;

public interface CPDClaimRevertService {

	String getCPDClaimRevertList(Integer userId, String orderValue, String fromDate, String toDate, Integer authMode,
			Integer trigger, Integer schemeid, String schemecategoryid);

	String getCPDClaimRevertDetails(String transaction_id, String urn, String transClaimId, String authorizedCode,
			String hospitalCode, String actualDate, String caseNo, Long userId, Date actionClickTime, String claimNo);

	String saveCpdClaimAction(Integer claimID, String action, String userId, String remarks, String reasonId,
			String cpdApprovedAmt, String urn);

	Map<String, Object> getCPDClaimRevertListCount(String userId, String orderValue, String fromDate, String toDate,
			Integer authMode, Integer trigger, Integer schemeid, String schemecategoryid);

	Map<String, Object> getPackageDetailsInfoList(Long txnPackageDetailsId);

	Map<String, Object> cpdRevertClaimAction(Integer claimID, String action, String userId, String remarks,
			String reasonId, String cpdApprovedAmt, String urn, String mortality, int timingLogId, Date actionTakenTime,
			Long icdFlag, List<ICDDetailsBean> icdFinalData) throws Exception;

}
