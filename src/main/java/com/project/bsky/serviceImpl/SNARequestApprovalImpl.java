package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.project.bsky.bean.BtnVisibilityBean;
import com.project.bsky.bean.HospitalRequestBean;
import com.project.bsky.bean.HospitaldetailsBean;
import com.project.bsky.bean.NonComplianceQryReqBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.SNARequestApproval;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.DocUtill;

@Service
public class SNARequestApprovalImpl implements SNARequestApproval {

	@SuppressWarnings("unused")
	private static ResourceBundle bskyResourcesBundel1 = ResourceBundle.getBundle("fileConfiguration");

	@Value("${file.path.Approveddoc}")
	private String ReqApprovedDoc;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getRequestList(Long userId, Date fromDate, Date toDate, String stateCode, String distCode,
			String hospitalCode, String schemeid, String schemecategoryid) {
		List<Object> requestlist = new ArrayList<Object>();
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Long.parseLong(schemecategoryid);
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_HSPTL_CLM_REQ_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_state_code", stateCode);
			storedProcedureQuery.setParameter("p_dist_code", distCode);
			storedProcedureQuery.setParameter("p_hsptl_code", hospitalCode);
			storedProcedureQuery.setParameter("P_SCHEME_ID", Long.parseLong(schemeid));
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				HospitalRequestBean resBean = new HospitalRequestBean();
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(1));
				resBean.setUrn(snoDetailsObj.getString(2));
				resBean.setRejectionId(snoDetailsObj.getLong(3));
				resBean.setPatientName(snoDetailsObj.getString(4));
				resBean.setCreatedOn(snoDetailsObj.getTimestamp(5));
				resBean.setPackageName(snoDetailsObj.getString(6));
				resBean.setPackageCode(snoDetailsObj.getString(7));
				resBean.setHospitalcode(snoDetailsObj.getString(8));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(9));
				resBean.setAuthorizedcode(snoDetailsObj.getString(10));
				resBean.setClaimRaisedBy(snoDetailsObj.getTimestamp(11));
				requestlist.add(resBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return requestlist;
	}

	@Override
	public String getRequestByDetailId(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		String urn = null;
		String authorizedCode = null;
		String hospitalCode = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_sna_hsptl_clm_req_dtls")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_log_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_out", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_log_msgout");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(21);
				authorizedCode = snoDetailsObj.getString(29).substring(2);
				urn = snoDetailsObj.getString(2);
				jsonObject = new JSONObject();
				jsonObject.put("REJECTIONID", snoDetailsObj.getLong(1));
				jsonObject.put("URN", snoDetailsObj.getString(2));
				jsonObject.put("DESCRIPTION", snoDetailsObj.getString(3));
				jsonObject.put("CLAIMRAISEDBY", snoDetailsObj.getTimestamp(4));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(5)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(6)));
				jsonObject.put("STATENAME", snoDetailsObj.getString(7));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(8));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(9));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(10));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(11));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(12));
				jsonObject.put("GENDER", snoDetailsObj.getString(13));
				jsonObject.put("AGE", snoDetailsObj.getString(14));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(15));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(16));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(25), snoDetailsObj.getString(26)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(18));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(19));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(20));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(23));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(24));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(25)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(26)));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(27));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(28));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(29));
				jsonObject.put("HOSPITALDISTRICTNAME", snoDetailsObj.getString(30));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(31));
				jsonObject.put("Address", snoDetailsObj.getString(32));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(33));
				String mob = snoDetailsObj.getString(34);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("TRANSACTIONDETAILSID", snoDetailsObj.getLong(35));
				jsonObject.put("verification", snoDetailsObj.getString(36));
				jsonObject.put("ispatient", snoDetailsObj.getString(37));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(38));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(39));
				jsonObject.put("categoryName", snoDetailsObj.getString(40));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(2));
					jsonObject1.put("SNAREMARK", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("CLAIMRAISEDBY", snoDetailsObj1.getString(5));
					jsonArray.put(jsonObject1);
				}
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				details.put("actionLog", jsonArray);
				details.put("preAuthHist", preAuthHist);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public List<Object> getNonComplianceRequestList(Long userId, Date fromDate, Date toDate, String stateCode,
			String distCode, String hospitalCode, String flag,String schemeid, String schemecategoryid) {
		List<Object> requestlist = new ArrayList<Object>();
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Long.parseLong(schemecategoryid);
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_sna_non_compliance_req_lst")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_state_code", stateCode);
			storedProcedureQuery.setParameter("p_dist_code", distCode);
			storedProcedureQuery.setParameter("p_hsptl_code", hospitalCode);
			storedProcedureQuery.setParameter("p_flag", flag);
			storedProcedureQuery.setParameter("P_SCHEME_ID", Long.parseLong(schemeid));
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				NonComplianceQryReqBean resBean = new NonComplianceQryReqBean();
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(1));
				resBean.setUrn(snoDetailsObj.getString(2));
				resBean.setRejectionId(snoDetailsObj.getLong(3));
				resBean.setPatientName(snoDetailsObj.getString(4));
				resBean.setCreatedOn(snoDetailsObj.getTimestamp(5));
				resBean.setPackageName(snoDetailsObj.getString(6));
				resBean.setPackageCode(snoDetailsObj.getString(7));
				resBean.setHospitalcode(snoDetailsObj.getString(8));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(9));
				resBean.setAuthorizedcode(snoDetailsObj.getString(10));
				resBean.setLastQueryOn(snoDetailsObj.getTimestamp(11));
				resBean.setClaimNo(snoDetailsObj.getString(12));
				requestlist.add(resBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return requestlist;
	}

	@Override
	public String getNonComplianceRequestByDetailId(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		String urn = null;
		String authorizedCode = null;
		String hospitalCode = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_sna_non_compliance_req_dtl")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_log_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_out", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_log_msgout");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(21);
				authorizedCode = snoDetailsObj.getString(29).substring(2);
				urn = snoDetailsObj.getString(2);
				jsonObject = new JSONObject();
				jsonObject.put("REJECTIONID", snoDetailsObj.getLong(1));
				jsonObject.put("URN", snoDetailsObj.getString(2));
				jsonObject.put("DESCRIPTION", snoDetailsObj.getString(3));
				jsonObject.put("CLAIMRAISEDBY", snoDetailsObj.getTimestamp(4));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(5)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(6)));
				jsonObject.put("STATENAME", snoDetailsObj.getString(7));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(8));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(9));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(10));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(11));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(12));
				jsonObject.put("GENDER", snoDetailsObj.getString(13));
				jsonObject.put("AGE", snoDetailsObj.getString(14));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(15));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(16));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(25), snoDetailsObj.getString(26)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(18));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(19));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(20));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(23));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(24));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(25)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(26)));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(27));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(28));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(29));
				jsonObject.put("HOSPITALDISTRICTNAME", snoDetailsObj.getString(30));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(31));
				jsonObject.put("Address", snoDetailsObj.getString(32));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(33));
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(36));
				jsonObject.put("CLAIMBILLNO", snoDetailsObj.getString(37));
				jsonObject.put("CLAIMCASENO", snoDetailsObj.getString(38));
				String mob = snoDetailsObj.getString(34);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("TRANSACTIONDETAILSID", snoDetailsObj.getLong(35));
				jsonObject.put("verification", snoDetailsObj.getString(39));
				jsonObject.put("ispatient", snoDetailsObj.getString(40));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(41));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(42));
				jsonObject.put("CREATEON", snoDetailsObj.getString(43));
				jsonObject.put("categoryName", snoDetailsObj.getString(44));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(2));
					jsonObject1.put("SNAREMARK", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("LASTQUERYON", snoDetailsObj1.getTimestamp(5));
					jsonArray.put(jsonObject1);
				}
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				details.put("actionLog", jsonArray);
				details.put("preAuthHist", preAuthHist);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public Response saveNonComplianceDetails(Long rejectionId, Long transactionDetailsId, Integer statusflag,
			String claimBy, String snaRemark, Integer sysRejStatus, MultipartFile ApproveDoc, String hospitalCode,
			String dateOfAdm, String urn) throws ParseException {
		String year = dateOfAdm.substring(6, 10);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date varDate = dateFormat.parse(claimBy);
		Map<String, String> filePath = DocUtill.saveFile(urn.trim(), year.trim(), hospitalCode.trim(), ApproveDoc);
		Response response = new Response();
		try {
			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_NON_COMPL_REQ_ACTN")
					.registerStoredProcedureParameter("p_rejectionid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_transactiondetailsid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statusflag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sysrejstatus", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_snaremark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_updatedon", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ApprovedDoc", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_rejectionid", rejectionId);
			storedProcedureQuery.setParameter("p_transactiondetailsid", transactionDetailsId);
			storedProcedureQuery.setParameter("p_statusflag", statusflag);
			storedProcedureQuery.setParameter("p_sysrejstatus", sysRejStatus);
			storedProcedureQuery.setParameter("p_claim_by", varDate);
			storedProcedureQuery.setParameter("p_snaremark", snaRemark);
			storedProcedureQuery.setParameter("p_updatedon", new Date());
			for (Map.Entry<String, String> entry : filePath.entrySet()) {
				if (ReqApprovedDoc.contains(entry.getKey()))
					storedProcedureQuery.setParameter("P_ApprovedDoc", entry.getValue());
			}
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (claimsnoInteger == 1 && statusflag == 1) {
				response.setStatus("Success");
				response.setMessage("Approved Successfully");
			} else if (claimsnoInteger == 1 && statusflag == 2) {
				response.setStatus("Success");
				response.setMessage("Rejected Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public Response saveRejectDetails(Long rejectionId, Long transactionDetailsId, Integer statusflag, String claimBy,
			String snaRemark, Integer sysRejStatus, MultipartFile ApproveDoc, String hospitalCode, String dateOfAdm,
			String urn) throws ParseException {
		String year = dateOfAdm.substring(6, 10);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date varDate = dateFormat.parse(claimBy);
		Map<String, String> filePath = DocUtill.saveFile(urn.trim(), year.trim(), hospitalCode.trim(), ApproveDoc);
		Response response = new Response();
		try {

			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_sna_hsptl_clm_req_actn")
					.registerStoredProcedureParameter("p_rejectionid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_transactiondetailsid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statusflag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sysrejstatus", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_snaremark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_updatedon", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ApprovedDoc", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_rejectionid", rejectionId);
			storedProcedureQuery.setParameter("p_transactiondetailsid", transactionDetailsId);
			storedProcedureQuery.setParameter("p_statusflag", statusflag);
			storedProcedureQuery.setParameter("p_sysrejstatus", sysRejStatus);
			storedProcedureQuery.setParameter("p_claim_by", varDate);
			storedProcedureQuery.setParameter("p_snaremark", snaRemark);
			storedProcedureQuery.setParameter("p_updatedon", new Date());
			for (Map.Entry<String, String> entry : filePath.entrySet()) {
				if (ReqApprovedDoc.contains(entry.getKey()))
					storedProcedureQuery.setParameter("P_ApprovedDoc", entry.getValue());
			}
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (claimsnoInteger == 1 && statusflag == 1) {
				response.setStatus("Success");
				response.setMessage("Approved Successfully");
			} else if (claimsnoInteger == 1 && statusflag == 2) {
				response.setStatus("Success");
				response.setMessage("Rejected Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<Object> getNonUploadingListToSna(BtnVisibilityBean requestBean) {
		List<Object> requestlist = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_NON_UPLOADING_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				HospitaldetailsBean resBean = new HospitaldetailsBean();
				resBean.setUserId(snoDetailsObj.getLong(1));
				resBean.setHospitalName(snoDetailsObj.getString(2));
				resBean.setHospitalCode(snoDetailsObj.getString(3));
				resBean.setStateName(snoDetailsObj.getString(4));
				resBean.setDistrictName(snoDetailsObj.getString(5));
				resBean.setNonUploadingFlag(snoDetailsObj.getString(6));
				resBean.setNonComplianceFlag(snoDetailsObj.getString(7));
				requestlist.add(resBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return requestlist;
	}

	@Override
	public Response savePermissionDetails(Long snaUserId, Long hosUserId, String hospitalCode, String claimBy,
			Integer nonUploadingStatus, Integer nonComplianceStatus) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date varDate = dateFormat.parse(claimBy);
		Response response = new Response();
		try {

			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_HOS_PERMISSION_ACT")
					.registerStoredProcedureParameter("p_snaUserId", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosUserId", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_nonUploadingstatus", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_nonCompliancestatus", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_createdon", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_snaUserId", snaUserId);
			storedProcedureQuery.setParameter("p_hosUserId", hosUserId);
			storedProcedureQuery.setParameter("p_nonUploadingstatus", nonUploadingStatus);
			storedProcedureQuery.setParameter("p_nonCompliancestatus", nonComplianceStatus);
			storedProcedureQuery.setParameter("p_claim_by", varDate);
			storedProcedureQuery.setParameter("p_hospitalCode", hospitalCode.trim());
			storedProcedureQuery.setParameter("p_createdon", new Date());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Permission given Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

}
