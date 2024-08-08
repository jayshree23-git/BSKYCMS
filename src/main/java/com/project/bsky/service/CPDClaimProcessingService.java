package com.project.bsky.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Cpdapprovalbean;
import com.project.bsky.bean.DocumentclickStatus;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageDetails;
import org.json.JSONArray;

import javax.servlet.http.HttpServletResponse;

public interface CPDClaimProcessingService {

	String getAllClaimRaised(Integer userId, String orderValue, String fromDate, String toDate, Integer authMode,
			Integer trigger, Integer schemeid, String schemecategoryid) throws SQLException;

	String getCPDDraftCliamDetails(Integer userId, Integer schemeid, String schemecategoryid) throws SQLException;

	String getClaimDetails(String transaction_id, String urn, String transClaimId, String authorizedCode,
			String hospitalCode, String actualDate, String caseNo, Long userId, Date actionClickTime, String claimNo)
			throws SQLException;

	String getIndividualDraftClaimDetails(String transaction_id, String urn, String transClaimId, String authorizedCode,
			String hospitalCode, String actualDate, String caseNo, Long userId, Date actionClickTime, String claimNo)
			throws SQLException;

	Map<String, Object> saveCpdClaimAction(Integer claimID, String action, String userId, String remarks,
			String reasonId, String cpdApprovedAmt, String urn, String mortality, int timingLogId, Date actionTakenTime,
			Long icdFlag, List<ICDDetailsBean> icdFinalData) throws Exception;

	String getPreAuthHistoryDetails(String urn, String authorizedCode, String hospitalCode);

	String getMultiPackDtls(String transaction_id, String urn, String authorizedCode, String hospitalCode);

	PackageDetails getPackageDetails(String packageId, String procedureCode);

	void generatePDF(JSONArray reports, JSONArray header, HttpServletResponse httpServletResponse);

	Map<String, Object> getCPDApprovalListCount(String userId, String orderValue, String fromDate, String toDate,
			Integer authMode, Integer trigger, Integer schemeid, String schemecategoryid);

	List<Map<String, Object>> getOldTreatmentHistoryCPD(String urn);

	Response getDocumnetinsert(DocumentclickStatus documnetstatus);

	Response saveCpdClaimActionnew(Cpdapprovalbean requestBean) throws Exception;

	Response saveCpdClaimDraftAction(Cpdapprovalbean requestBean) throws Exception;

}
