package com.project.bsky.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.bsky.bean.Bulkapprovalbean;
import com.project.bsky.bean.Bulkrevertbean;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.MeTriggerDetailsBean;
import com.project.bsky.bean.PatientRequestBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.model.ActionType;
import com.project.bsky.model.EnrollmentRemarks;
import com.project.bsky.model.Mstschemesubcategory;
import com.project.bsky.model.State;

public interface SnoClaimProcessingDetails {

	List<Object> getsnoclaimrasiedata(CPDApproveRequestBean requestBean);

	Map<String, Object> getSNAClaimApprove(CPDApproveRequestBean requestBean);

	Map<String, Object> cpdApprovalCount(CPDApproveRequestBean requestBean);

	String getCountReportBtCPDAprv(CPDApproveRequestBean requestBean);

	String getSnoClaimListById(Integer txnId) throws Exception;

	String claimProcessedDetails(Integer txnId) throws Exception;

	String getURNWiseDetails(Integer txnId) throws Exception;

	String getSystemAdminRejectedDetails(Integer txnId) throws Exception;

	String dischargeTreatment(Integer txnId) throws Exception;

	String getMultiPackageBlock(Integer txnId) throws Exception;

	void downLoadFile(String fileName, String year, String hCode, HttpServletResponse response);

	public Response saveClaimSNODetails(ClaimLogBean logBean) throws Exception;

	public Response saveClaimReApprovalAction(ClaimLogBean logBean) throws Exception;

	public Response saveClaimProcessedDetails(ClaimLogBean logBean) throws Exception;

	public Response saveUrnWiseDetails(ClaimLogBean logBean) throws Exception;

	public Response saveSyatemAdminSnaRejectedDetails(ClaimLogBean logBean) throws Exception;

	ActionRemark getActionRemarkById(Long remarkId);

	List<ActionRemark> getAllActionRemark();

	List<State> getAllState();

	String getPreAuthData(String urn);

	String getDistrictList(String stateCode, Long userId);

	String getHospital(String stateCode, String distCode, Long userId);

	List<ActionType> getActionType();

	public File downLoadMultipleFile(String fileName, String year, String hCode, HttpServletResponse response);

	void filteredFile(JSONArray jsonArray, HttpServletResponse response) throws JSONException, IOException;

	JSONObject getcountdetails(Bulkapprovalbean requestBean);

	Response getsavebulkapproveds(Long user, Long group, String flags, Date fromDate, Date toDate, String stateid,
			String districtid, String hospitalid) throws Exception;

	void downloadDocuments(JSONArray jsonArray, HttpServletResponse response) throws JSONException, IOException;

	List<Object> getdcClaimApproveddata(CPDApproveRequestBean requestBean);

	String getDCClaimAprvById(Integer txnId) throws Exception;

	public Response saveClaimSNOOfDCAprvDetails(ClaimLogBean logBean) throws Exception;

	List<Object> getOldClaimApprovedList(CPDApproveRequestBean requestBean);

	String getOldClaimById(Integer txnId) throws Exception;

	public Response saveoldClaimDetails(ClaimLogBean logBean);

	List<Object> getClaimsOnHoldList(CPDApproveRequestBean requestBean);

	String getTreatmentHistoryoverpackgae(Long txnId, String urnnumber, String hospitalcode, String caseno,
			String uidreferencenumber, Long userid) throws Exception;

	String getremarkdetailsforsna(Long snaid, Date fromdate, Date todate, String hospitalcode, String stateode,
			String distcode);

	String getcountremarkdetailsforsna(Long userid, Date fromdate, Date todate, String statecode, String districtcode,
			String hospitalcode, Long remarkid, String hospitalcodeforremark);

	String getcountremarkdetailspayment(String urn, Long claimid, String floatno);

	String actiondetailsforsna(Long groupId, String statecode, String districtcode, String hospitalcode, Date fromDate,
			Date toDate, Long userId);

	String getactionremarkdetailssforsna(Long snaid, Date fromdate, Date todate, String hospitalcode, String stateode,
			String distcode);

	String getcountremarkdetailsforsnaaction(Long userid, Date fromdate, Date todate, String statecode,
			String districtcode, String hospitalcode, Long remarkid, String hospitalcodeforremark);

	List<EnrollmentRemarks> getEnrollmentAllActionRemark();

	String getAllHistoryAgainstClaimnumber(String claimno) throws Exception;

	public Map<String, Object> getTriggerDetails(MeTriggerDetailsBean triggerDetails) throws Exception;

	List<Object> getbulkapprovallistofdata(String fromDate, String toDate, Long userId);

	Response getsubmitbulkapprovalrevertRecord(Bulkrevertbean logBean);

	String getSpecialtyReuquest(CPDApproveRequestBean requestBean);

	String getSpecialtyReuquestDetails(Long requestId) throws Exception;

	String multipackthroughcaseno(String caseno, String date);

	String getfamilyTreatementDetails(String dateofadmission, String memeberid, String urn);

	String getPatientTreatementDetails(String procedureCode, String uidreferencenumber);

	String getSnaFloatClaimDetails(String urn, Long claimid, String floatno) throws Exception;

	String getCPDTriggerdetailsData(String hospitalcode, Date dateofAdmission, Date dateofdischarge, String procedurecode);

	String getsnamortalitystatus(Long claimid);

	List<Mstschemesubcategory> getSchemesubcategory();

}
