package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.FloatRequest;
import com.project.bsky.bean.FloatRequestBean;
import com.project.bsky.bean.HospitalwisefloatdetailsModaldata;
import com.project.bsky.bean.PaymentActionBean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.PostPaymentRequestNew;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.ReversePaymentBean;
import com.project.bsky.bean.SnoClaimDetails;
import com.project.bsky.bean.TxnclamFloateDetailsbean;
import com.project.bsky.model.TxnclaimFloatActionLog;
import com.project.bsky.model.TxnclamFloateDetails;

public interface PaymentFreezeService {

	List<Object> getpaymentfreezedata(CPDApproveRequestBean requestBean);

	List<Object> getRefundList(CPDApproveRequestBean requestBean);

	public Response savePaymentfreeze(Long userId, Date fromDate, Date toDate, String stateCodeList,
			String distCodeList, String hospitalCodeList, Long snoUserId, Double snaAmount, MultipartFile floatFile,
			Integer searchtype, String schemecategoryid, List<String> floatList) throws Exception;

	JSONArray getPaymentFreezeList(Long snoId, Date fromDate, Date toDate, Long pendingAt);

	List<Object> getFloatClaimDetails(String floatNo);

	Response updateSnaApprvdAmnt(Integer claimId, String amount, Long snoId, String remark);

	Response paymentFreeze(String floatNo, Long snoId);

	public List<TxnclamFloateDetails> getFLoatList();

	public List<Object> getFLoatDetails(String floatNumber);

	public List<Object> getFLoatDetailsByHospital(String floatNumber, String hospitalCode);

	public Response remarkUpdate(ClaimLogBean bean);

	public Response verifyFloat(String floatNumber, Long actionBy, String remark, MultipartFile file);

	public List<TxnclamFloateDetails> getNonVerifiedFLoatList();

	List<Object> getSnaApprovedList(CPDApproveRequestBean requestBean);

	List<SnoClaimDetails> getPostPaymentList(CPDApproveRequestBean requestBean);

	public Response updatePostPayment(PostPaymentRequest requestBean) throws Exception;

	public Response freezePayment(Long userId, MultipartFile file) throws Exception;

	JSONObject getPaymentFreezeReport(Long userId, Date fromdate, Date toDate, String stateId, String districtId,
			String hospitalId, String mortality, Long searchtype, String schemecategoryid);

	Integer paymentFreezeAction(PaymentActionBean bean);

	Integer savePaymentFreezeRecord(MultipartFile pdf, PaymentActionBean bean);

	Integer saveFLoatReport(PaymentActionBean bean);

	JSONArray paymentFreezeView(PaymentActionBean requestBean);

	void downloadPfzFile(String fileCode, String userId, HttpServletResponse response);

	List<TxnclamFloateDetailsbean> getfloatdata(Integer groupId, String fromdate, String todate, String snoid,
			Long userid, Integer authMode);

	public Response forwardToSNA(String floatNumber, Long actionBy, String remark, MultipartFile file);

//	JSONArray getforremarkslistdata(Long userId);

	Response paymentForwardsubmit(Long userId, String header, String remarks, Long pendingAt);

	JSONArray getoldforemarkmodaldata(Long claimid);

	JSONArray getsummaryDetails(Long userid, String fromDate, String toDate, String verify, String schemecategoryid);

	JSONArray getforremarkslistdata(Long userId, String fromDate, String toDate, Long pendingAt);

	JSONArray getfloatdetailshospitalwise(String floatnumberhospitawisedetails, String fromdate);

	List<Object> getHospitalwisefloatdetailsReport(HospitalwisefloatdetailsModaldata requestBean);

	JSONObject getSummary(FloatRequestBean requestBean);

	JSONObject getCountDetails(String floatNumber, Long levelId);

	List<TxnclamFloateDetails> getAssignedFO(Integer userId);

	Response forwardFloat(List<Long> floatList, String remark, Integer pendingAt, Long userId, MultipartFile file);

	Response assignToFo(FloatRequest floatRequest);

	String paymentFreezeClaimDetails(Date fromDate, Date toDate, String stateId, String districtId, String hospitalId,
			String mortality);

	String getPaymentList(ReversePaymentBean requestBean);

	List<Object> getPaidClaimList(ReversePaymentBean requestBean);

	Response reversePayment(ReversePaymentBean requestBean);

	List<SnoClaimDetails> getPostPaymenView(CPDApproveRequestBean requestBean);

	List<TxnclaimFloatActionLog> getLogHistory(Long floatId);

	void floatDocDownload(String fileName, String floatNumner, HttpServletResponse response);

	String getDistrictList(Map<String, Object> mapObj);

	String getHospitalByMultiDistrict(Map<String, Object> mapObj);

	void downLoadFilefloat(String fileName, String floatNumber, String currentYear, HttpServletResponse response);

	List<TxnclamFloateDetailsbean> getDraftList(Long snouserId, String fromdate, String todate) throws Exception;

	public Response forwardDraftFloat(String floatNumber, Long snoUserId, MultipartFile snacertification,
			MultipartFile mecertification, MultipartFile otherfile, String description) throws Exception;

	List<TxnclamFloateDetailsbean> getfloatViewdata(Integer groupId, String fromdate, String todate, String snoid,
			Long userid, Integer authMode);

	public Response saveFloatClaimAction(ClaimLogBean logBean) throws Exception;

	List<Object> getprocessfloatreport(Date formdate, Date todate, Long snadoctor, Long userid) throws Exception;

	List<Object> getFloatDescriptiondataList(Date fromDate, Date toDate, String floatnumber, String snauserid)
			throws Exception;

	List<Object> getfloatdetailshospitalwiseabstaact(String floateno) throws Exception;

	Response updatePostPaymentnew(PostPaymentRequestNew paymentRequest) throws Exception;

	List<Object> getfloatdetailshospitalwiseabstaactLogRecord(String floateno, String hospitacode) throws Exception;

	List<Object> getfloatdetailshospitalwiseabstaactView(String floateno, String hospitalcode) throws Exception;

	List<Object> getFloatClaimDetailsList(String floatNo);

}
