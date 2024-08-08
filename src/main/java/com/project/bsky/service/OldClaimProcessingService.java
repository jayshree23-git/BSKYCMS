package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.OldFloatBean;
import com.project.bsky.bean.Response;

public interface OldClaimProcessingService {

	List<Object> getOldClaimQueryBySNAList(CPDApproveRequestBean requestBean);

	String getOldQueriedClaimDetails(String claimID);

	Response takeActionOnOldClaimforsnoQuery(String claimID, String hospitalCode, MultipartFile additionaldoc1,
			MultipartFile additionaldoc2, String urnNumber, String dateofAdmission, String transId, String ClaimAmount,
			String actionby) throws Exception;

	List<Object> getOldClaimReApproveList(CPDApproveRequestBean requestBean);

	String getOldClaimReAprvById(Integer txnId) throws Exception;

	public Response saveClaimSNOOfOldReAprvDetails(ClaimLogBean logBean);

	void downLoadOldFile(String fileName, String year, String hCode, HttpServletResponse response);

	String getOldActionList(CPDApproveRequestBean requestBean);

	String getOldClaimTrackingDetailsById(Integer txnId) throws Exception;

	String getOldSNAProcessedList(CPDApproveRequestBean requestBean);

	String getOldReclaimedPendingAtSNAList(CPDApproveRequestBean requestBean);
	
	String getOldClaimCountProgress(OldFloatBean requestBean);
	
	String getOldclaimprogressreportdetails(Long userId, Date fromdate, Date toDate, String eventName,
			String stateId, String districtId, String hospitalId);

	List<Object> oldclaimnoncompliance(Date fromdate, Date toDate, Long userId, 
			String state, String dist, String hospital) throws Exception;
}
