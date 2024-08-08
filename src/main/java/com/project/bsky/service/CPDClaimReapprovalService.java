package com.project.bsky.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.ICDDetailsBean;

public interface CPDClaimReapprovalService {

	String getAllClaimRaised(Integer userId, String orderValue, String fromDate, String toDate, Integer authMode,
			Integer trigger, Integer schemeid, String schemecategoryid);

	String getClaimDetails(String transaction_id, String urn, String transClaimId, String authorizedCode,
			String hospitalCode, String actualDate, String caseNo, Long userId, Date actionClickTime, String claimNo)
			throws SQLException;

	Map<String, Object> saveCpdClaimAction(Integer claimID, String action, String userId, String remarks,
			String reasonId, String cpdApprovedAmt, String urn, String mortality, int timingLogId, Date actionTakenTime,
			Long icdFlag, List<ICDDetailsBean> icdFinalData) throws Exception;

	Map<String, Object> getCPDReApprovalClaimListCount(String userId, String orderValue, String fromDate, String toDate,
			Integer authMode, Integer trigger, Integer schemeid, String schemecategoryid);



}
