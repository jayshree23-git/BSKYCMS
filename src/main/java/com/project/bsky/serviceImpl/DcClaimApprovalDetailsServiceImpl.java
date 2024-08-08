package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.ResultSet;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Actiontakenhistorybean;
import com.project.bsky.bean.DCDashboardBean;
import com.project.bsky.bean.EnrollmentActionbean;
import com.project.bsky.bean.Enrollmentapprovalbean;
import com.project.bsky.bean.Enrollmentbean;
import com.project.bsky.bean.GODashboardBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.DcClaimApprovalDetailsService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;

@Service
public class DcClaimApprovalDetailsServiceImpl implements DcClaimApprovalDetailsService {

	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;

	@Autowired
	private Logger logger;

	@Override
	public String getDcClaimList(Long userId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DC_CLAIM_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("transactionDetailsId", snoDetailsObj.getLong(1));
				jsonObject.put("claimid", snoDetailsObj.getLong(2));
				jsonObject.put("URN", snoDetailsObj.getString(3));
				jsonObject.put("PatientName", snoDetailsObj.getString(4));
				jsonObject.put("PackageCode", snoDetailsObj.getString(5));
				jsonObject.put("CreatedOn", snoDetailsObj.getString(6));
				jsonObject.put("CurrentTotalAmount", snoDetailsObj.getLong(7));
				jsonObject.put("PackageName", snoDetailsObj.getString(8));
				jsonObject.put("actualdateofdischarge", DateFormat.FormatToDateString(snoDetailsObj.getString(9)));
				jsonObject.put("actualdateofadmission", DateFormat.FormatToDateString(snoDetailsObj.getString(10)));
				jsonObject.put("claim_no", snoDetailsObj.getString(11));
				jsonObject.put("hospitalname", snoDetailsObj.getString(12));
				jsonObject.put("hospitalcode", snoDetailsObj.getString(13));
				jsonObject.put("invoiceno", snoDetailsObj.getString(14));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArray.toString();
	}

	@Override
	public String getDcClaimListById(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_dc_claim_dtls")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38).substring(2);
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				String mob = snoDetailsObj.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("pendingat", snoDetailsObj.getLong(51));
				jsonObject.put("claimstatus", snoDetailsObj.getLong(52));
				jsonObject.put("transactiondetailsid", snoDetailsObj.getLong(53));
				jsonObject.put("cost", snoDetailsObj.getLong(54));
				jsonObject.put("cpd_mortality", snoDetailsObj.getString(55));
				jsonObject.put("verification", snoDetailsObj.getString(56));
				jsonObject.put("ispatient", snoDetailsObj.getString(57));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(58));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonArray.put(jsonObject1);
				}
				packageBlock = packageBlocking.getMultiplePackageBlocking(urn, actualDate);
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				details.put("actionLog", jsonArray);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				} else if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public Integer dcClaimSubmit(Long userId, Long txnId, String URN, String hospitalCode, Long claimId,
			Double claimAmount, String additionalDoc, String additionalDoc1, String additionalDoc2,
			String dischargeSlip, String preSurgery, String postSurgery, String intraSurgery, String specimenRemoval,
			String patientPhoto, MultipartFile investigation1Doc, MultipartFile investigation2Doc,
			String dateOfAdmission, String remarks) {
		Integer msgOut = null;
		Map<String, String> savedInvestigationFile;
		try {
			savedInvestigationFile = CommonFileUpload.saveInvestigationFile(dateOfAdmission.substring(6, 10).trim(),
					URN.trim(), hospitalCode.trim(), investigation1Doc, investigation2Doc);
			if (savedInvestigationFile != null) {
				StoredProcedureQuery query = entityManager.createStoredProcedureQuery("USP_CLAIM_DCAPPROVAL_SUBMIT");
				query.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", Long.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_CLAIMAMOUNT", Double.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_USER_IP", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ACTIONTYPE", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ACTIONBY", Long.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_CREATEDBY", Long.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_STATUSFLAG", Integer.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ADDITIONALDOC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ADDITONALDOC1", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_ADITTIONALDOC2", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_DISCHARGESLIP", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_PRESUERGRYPIC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_POSTSURGERYPIC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_SPECIMANSURGERY", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_PATIENTPIC", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_INVESTIGATION1", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_INVESTIGATION2", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN);
				query.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

				query.setParameter("P_ACTION", "D");
				query.setParameter("P_TRANSACTIONDETAILSID", txnId);
				query.setParameter("P_CLAIMID", claimId);
				query.setParameter("P_URN", URN);
				query.setParameter("P_CLAIMAMOUNT", claimAmount);
				query.setParameter("P_USER_IP", InetAddress.getLocalHost().getHostAddress());
				query.setParameter("P_ACTIONTYPE", 1);
				query.setParameter("P_ACTIONBY", userId);
				query.setParameter("P_CREATEDBY", userId);
				query.setParameter("P_STATUSFLAG", 0);
				query.setParameter("P_ADDITIONALDOC", additionalDoc);
				query.setParameter("P_ADDITONALDOC1", additionalDoc1);
				query.setParameter("P_ADITTIONALDOC2", additionalDoc2);
				query.setParameter("P_DISCHARGESLIP", dischargeSlip);
				query.setParameter("P_PRESUERGRYPIC", preSurgery);
				query.setParameter("P_POSTSURGERYPIC", postSurgery);
				query.setParameter("P_INTRASURGERY", intraSurgery);
				query.setParameter("P_SPECIMANSURGERY", specimenRemoval);
				query.setParameter("P_PATIENTPIC", patientPhoto);
				query.setParameter("P_INVESTIGATION1", savedInvestigationFile.get("investigationDoc1"));
				query.setParameter("P_INVESTIGATION2", savedInvestigationFile.get("investigationDoc2"));
				query.setParameter("P_REMARKS", remarks);
				query.execute();
				msgOut = (Integer) query.getOutputParameterValue("P_MSGOUT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgOut;
	}

	@Override
	public String getDCInvestigationCount(DCDashboardBean bean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_DC_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSP_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VIEW_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_YEAR", bean.getYear());
			storedProcedureQuery.setParameter("P_MONTH", bean.getMonth());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_HOSP_CODE", bean.getHospitalCode());
			storedProcedureQuery.setParameter("P_VIEW_FLAG", bean.getViewFlag());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT_TOTAL");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("hospitalName", snoDetailsObj.getString(1));
				jsonObject.put("pending", snoDetailsObj.getLong(2));
				jsonObject.put("approved", snoDetailsObj.getLong(3));
				jsonObject.put("total", snoDetailsObj.getLong(4));
				jsonArray.put(jsonObject);
			}
			details.put("HospitalWise", jsonArray);
			if (bean.getViewFlag().equalsIgnoreCase("GR") && bean.getSectionFlag().equalsIgnoreCase("INV")) {
				if (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("pending", snoDetailsObj1.getLong(1));
					jsonObject1.put("approved", snoDetailsObj1.getLong(2));
					jsonObject1.put("total", snoDetailsObj1.getLong(3));
					details.put("TotalWise", jsonObject1);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				} else if (snoDetailsObj1 != null) {

					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getDCOverRideCount(DCDashboardBean bean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_DC_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSP_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VIEW_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_YEAR", bean.getYear());
			storedProcedureQuery.setParameter("P_MONTH", bean.getMonth());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_HOSP_CODE", bean.getHospitalCode());
			storedProcedureQuery.setParameter("P_VIEW_FLAG", bean.getViewFlag());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (bean.getViewFlag().equalsIgnoreCase("TBL") && bean.getSectionFlag().equalsIgnoreCase("OV")) {
				while (snoDetailsObj.next()) {
					jsonObject = new JSONObject();
					jsonObject.put("hospitalName", snoDetailsObj.getString(1));
					jsonObject.put("ovapproved", snoDetailsObj.getLong(2));
					jsonObject.put("ovpending", snoDetailsObj.getLong(3));
					jsonObject.put("rfAuth", snoDetailsObj.getLong(4));
					jsonObject.put("rfNonAuth", snoDetailsObj.getLong(5));
					jsonArray.put(jsonObject);
				}
				details.put("HospitalWise", jsonArray);
			}
			if (bean.getViewFlag().equalsIgnoreCase("GR") && bean.getSectionFlag().equalsIgnoreCase("OV")) {
				if (snoDetailsObj.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("ovapproved", snoDetailsObj.getLong(1));
					jsonObject1.put("ovpending", snoDetailsObj.getLong(2));
					jsonObject1.put("percentageovapproved", snoDetailsObj.getLong(3));
					jsonObject1.put("percentageovpending", snoDetailsObj.getLong(4));
					jsonObject1.put("rfAuth", snoDetailsObj.getLong(5));
					jsonObject1.put("rfNonAuth", snoDetailsObj.getLong(6));
					jsonObject1.put("percentagerfAuth", snoDetailsObj.getLong(7));
					jsonObject1.put("percentagerfNonAuth", snoDetailsObj.getLong(8));
					details.put("TotalWise", jsonObject1);
				}
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				} else if (snoDetailsObj1 != null) {

					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getDCGrievanceCount(DCDashboardBean bean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_DC_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSP_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VIEW_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_YEAR", bean.getYear());
			storedProcedureQuery.setParameter("P_MONTH", bean.getMonth());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_HOSP_CODE", bean.getHospitalCode());
			storedProcedureQuery.setParameter("P_VIEW_FLAG", bean.getViewFlag());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT_TOTAL");
			if (bean.getViewFlag().equalsIgnoreCase("TBL") && bean.getSectionFlag().equalsIgnoreCase("GRV")) {
				while (snoDetailsObj.next()) {
					jsonObject = new JSONObject();
					jsonObject.put("Grievance_Name", snoDetailsObj.getString(1));
					jsonObject.put("Total", snoDetailsObj.getLong(2));
					jsonObject.put("Pending", snoDetailsObj.getLong(3));
					jsonObject.put("Action", snoDetailsObj.getLong(4));
					jsonArray.put(jsonObject);
				}
				details.put("IndividualWise", jsonArray);
			}
			if (bean.getViewFlag().equalsIgnoreCase("GR") && bean.getSectionFlag().equalsIgnoreCase("GRV")) {
				if (snoDetailsObj.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("Total", snoDetailsObj.getLong(1));
					jsonObject1.put("Pending", snoDetailsObj.getLong(2));
					jsonObject1.put("Action", snoDetailsObj.getLong(3));
					details.put("TotalWise", jsonObject1);
				}
				while (snoDetailsObj1.next()) {
					jsonObject = new JSONObject();
					jsonObject.put("GRIEVANCE_NAME", snoDetailsObj1.getString(1));
					jsonObject.put("total", snoDetailsObj1.getLong(2));
					jsonObject.put("pending", snoDetailsObj1.getLong(3));
					jsonObject.put("action", snoDetailsObj1.getLong(4));
					jsonObject.put("pending_percent", snoDetailsObj1.getString(5));
					jsonObject.put("action_percent", snoDetailsObj1.getString(6));
					jsonArray.put(jsonObject);
				}
				details.put("IndividualWise", jsonArray);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				} else if (snoDetailsObj1 != null) {

					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getDCGrievanceResolveCount(DCDashboardBean bean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_DC_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSP_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VIEW_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_YEAR", bean.getYear());
			storedProcedureQuery.setParameter("P_MONTH", bean.getMonth());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_HOSP_CODE", bean.getHospitalCode());
			storedProcedureQuery.setParameter("P_VIEW_FLAG", bean.getViewFlag());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT_TOTAL");
			if (bean.getViewFlag().equalsIgnoreCase("TBL") && bean.getSectionFlag().equalsIgnoreCase("RES")) {
				while (snoDetailsObj.next()) {
					jsonObject = new JSONObject();
					jsonObject.put("hospitalname", snoDetailsObj.getString(1));
					jsonObject.put("Beneficiary", snoDetailsObj.getLong(2));
					jsonObject.put("Hospital", snoDetailsObj.getLong(3));
					jsonObject.put("Mo_Sarkar", snoDetailsObj.getLong(4));
					jsonObject.put("News_Paper", snoDetailsObj.getLong(5));
					jsonObject.put("Email", snoDetailsObj.getLong(6));
					jsonObject.put("Social_Media", snoDetailsObj.getLong(7));
					jsonObject.put("total", snoDetailsObj.getLong(8));
					jsonArray.put(jsonObject);
				}
				details.put("HospitalWise", jsonArray);
			}
			if (bean.getViewFlag().equalsIgnoreCase("GR") && bean.getSectionFlag().equalsIgnoreCase("RES")) {

				while (snoDetailsObj.next()) {
					jsonObject = new JSONObject();
					jsonObject.put("GRIEVANCE_NAME", snoDetailsObj.getString(1));
					jsonObject.put("total", snoDetailsObj.getLong(2));
					jsonObject.put("resolved", snoDetailsObj.getLong(3));
					jsonArray.put(jsonObject);
				}
				details.put("IndividualWise", jsonArray);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				} else if (snoDetailsObj1 != null) {

					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getDCGrievanceModeCount(DCDashboardBean bean) {
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_DC_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSP_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VIEW_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_YEAR", bean.getYear());
			storedProcedureQuery.setParameter("P_MONTH", bean.getMonth());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_HOSP_CODE", bean.getHospitalCode());
			storedProcedureQuery.setParameter("P_VIEW_FLAG", bean.getViewFlag());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("Beneficiary", snoDetailsObj.getLong(1));
				jsonObject.put("Hospital", snoDetailsObj.getLong(2));
				jsonObject.put("Mo_Sarkar", snoDetailsObj.getLong(3));
				jsonObject.put("News_Paper", snoDetailsObj.getLong(4));
				jsonObject.put("Email", snoDetailsObj.getLong(5));
				jsonObject.put("Social_Media", snoDetailsObj.getLong(6));
				jsonObject.put("total", snoDetailsObj.getLong(7));
				details.put("ModeWise", jsonObject);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getCCECountReport(DCDashboardBean bean) {
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_DC_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSP_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VIEW_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_YEAR", bean.getYear());
			storedProcedureQuery.setParameter("P_MONTH", bean.getMonth());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_HOSP_CODE", bean.getHospitalCode());
			storedProcedureQuery.setParameter("P_VIEW_FLAG", bean.getViewFlag());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("Question1Yes", snoDetailsObj.getLong(1));
				jsonObject.put("Question1No", snoDetailsObj.getLong(2));
				jsonObject.put("Question1YesPercent", snoDetailsObj.getLong(3));
				jsonObject.put("Question1NoPercent", snoDetailsObj.getLong(4));
				jsonObject.put("Question2Yes", snoDetailsObj.getLong(5));
				jsonObject.put("Question2No", snoDetailsObj.getLong(6));
				jsonObject.put("Question2YesPercent", snoDetailsObj.getLong(7));
				jsonObject.put("Question2NoPercent", snoDetailsObj.getLong(8));
				jsonObject.put("Question3Yes", snoDetailsObj.getLong(9));
				jsonObject.put("Question3No", snoDetailsObj.getLong(10));
				jsonObject.put("Question3YesPercent", snoDetailsObj.getLong(11));
				jsonObject.put("Question3NoPercent", snoDetailsObj.getLong(12));
				jsonObject.put("Question4Yes", snoDetailsObj.getLong(13));
				jsonObject.put("Question4No", snoDetailsObj.getLong(14));
				jsonObject.put("Question4YesPercent", snoDetailsObj.getLong(15));
				jsonObject.put("Question4NoPercent", snoDetailsObj.getLong(16));
				jsonObject.put("TotalConnect", snoDetailsObj.getLong(17));
				jsonObject.put("TotalYes", snoDetailsObj.getLong(18));
				jsonObject.put("TotalNo", snoDetailsObj.getLong(19));
				jsonObject.put("TotalYesPositive", snoDetailsObj.getLong(20));
				jsonObject.put("TotalYesNegative", snoDetailsObj.getLong(21));
				jsonObject.put("DcTotal", snoDetailsObj.getLong(22));
				jsonObject.put("DcPending", snoDetailsObj.getLong(23));
				jsonObject.put("DcAction", snoDetailsObj.getLong(24));
				jsonObject.put("Support", snoDetailsObj.getLong(25));
				jsonObject.put("Behaviour", snoDetailsObj.getLong(26));
				jsonObject.put("Bribe", snoDetailsObj.getLong(27));
				details.put("CCETotal", jsonObject);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getEmpCountReport(DCDashboardBean bean) {
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_DC_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSP_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VIEW_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_YEAR", bean.getYear());
			storedProcedureQuery.setParameter("P_MONTH", bean.getMonth());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_HOSP_CODE", bean.getHospitalCode());
			storedProcedureQuery.setParameter("P_VIEW_FLAG", bean.getViewFlag());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("AssignedForInv", snoDetailsObj.getLong(1));
				jsonObject.put("Investigated", snoDetailsObj.getLong(2));
				jsonObject.put("PendingForInv", snoDetailsObj.getLong(3));
				jsonObject.put("QueryToBeRespond", snoDetailsObj.getLong(4));
				jsonObject.put("Approved", snoDetailsObj.getLong(5));
				jsonObject.put("Reject", snoDetailsObj.getLong(6));
				jsonObject.put("SentForReSubmit", snoDetailsObj.getLong(7));
				details.put("EmpTotal", jsonObject);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getGOCountReport(GODashboardBean bean) {
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GO_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", bean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", bean.getDistrictCode());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.setParameter("P_FLAG", bean.getInSection());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("beneficiary", snoDetailsObj.getLong(1));
				jsonObject.put("hospital", snoDetailsObj.getLong(2));
				jsonObject.put("mosarkar", snoDetailsObj.getLong(3));
				jsonObject.put("newspaper", snoDetailsObj.getLong(4));
				jsonObject.put("email", snoDetailsObj.getLong(5));
				jsonObject.put("socialmedia", snoDetailsObj.getLong(6));
				jsonObject.put("total", snoDetailsObj.getLong(7));
				details.put("GOTotal", jsonObject);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getGOMediumCount(GODashboardBean bean) {
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GO_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", bean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", bean.getDistrictCode());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.setParameter("P_FLAG", bean.getInSection());
			storedProcedureQuery.execute();
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (snoDetailsObj1.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("DGOResolve", snoDetailsObj1.getLong(1));
				jsonObject1.put("GOResolve", snoDetailsObj1.getLong(2));
				jsonObject1.put("DCForward", snoDetailsObj1.getLong(3));
				jsonObject1.put("DCPending", snoDetailsObj1.getLong(4));
				jsonObject1.put("DGOPending", snoDetailsObj1.getLong(5));
				jsonObject1.put("GOPending", snoDetailsObj1.getLong(6));
				jsonObject1.put("within7days", snoDetailsObj1.getLong(7));
				jsonObject1.put("morethan7days", snoDetailsObj1.getLong(8));
				jsonObject1.put("morethan15days", snoDetailsObj1.getLong(9));
				jsonObject1.put("morethan30days", snoDetailsObj1.getLong(10));
				jsonObject1.put("totalOverdue", snoDetailsObj1.getLong(11));
				jsonObject1.put("totalResolved", snoDetailsObj1.getLong(12));
				jsonObject1.put("totalPending", snoDetailsObj1.getLong(13));
				details.put("InrTotal", jsonObject1);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getGODistrictWiseCount(GODashboardBean bean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GO_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", bean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", bean.getDistrictCode());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.setParameter("P_FLAG", bean.getInSection());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("hospitalName", snoDetailsObj.getString(1));
				jsonObject.put("totalGrievance", snoDetailsObj.getLong(2));
				jsonObject.put("totalResolved", snoDetailsObj.getLong(3));
				jsonObject.put("totalPending", snoDetailsObj.getLong(4));
				jsonObject.put("totalPendingDays", snoDetailsObj.getLong(5));
				jsonObject.put("avgPendingDays", snoDetailsObj.getLong(6));
				jsonArray.put(jsonObject);
			}
			details.put("GOHospitalWise", jsonArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getGOCCEDistrictWiseCount(GODashboardBean bean) {
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		JSONArray jsonArray = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GO_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", bean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", bean.getDistrictCode());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.setParameter("P_FLAG", bean.getInSection());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT_TOTAL");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("totalAssign", snoDetailsObj.getLong(1));
				jsonObject.put("totalActionTaken", snoDetailsObj.getLong(2));
				jsonObject.put("totalYes", snoDetailsObj.getLong(3));
				jsonObject.put("totalNo", snoDetailsObj.getLong(4));
				jsonObject.put("totalNotConnected", snoDetailsObj.getLong(5));
				jsonObject.put("Question1Yes", snoDetailsObj.getLong(6));
				jsonObject.put("Question1No", snoDetailsObj.getLong(7));
				jsonObject.put("Question2Yes", snoDetailsObj.getLong(8));
				jsonObject.put("Question2No", snoDetailsObj.getLong(9));
				jsonObject.put("Question3Yes", snoDetailsObj.getLong(10));
				jsonObject.put("Question3No", snoDetailsObj.getLong(11));
				jsonObject.put("Question4Yes", snoDetailsObj.getLong(12));
				jsonObject.put("Question4No", snoDetailsObj.getLong(13));
				jsonObject.put("DCPending", snoDetailsObj.getLong(14));
				jsonObject.put("DGOPending", snoDetailsObj.getLong(15));
				jsonObject.put("GOPending", snoDetailsObj.getLong(16));
				jsonObject.put("totalPending", snoDetailsObj.getLong(17));
				jsonObject.put("DCResolve", snoDetailsObj.getLong(18));
				jsonObject.put("DGOResolve", snoDetailsObj.getLong(19));
				jsonObject.put("GOResolve", snoDetailsObj.getLong(20));
				jsonObject.put("TotalResolve", snoDetailsObj.getLong(21));
				details.put("CCETotal", jsonObject);
			}
			while (snoDetailsObj1.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("HospitalName", snoDetailsObj1.getString(1));
				jsonObject1.put("TotalGrievance", snoDetailsObj1.getLong(2));
				jsonObject1.put("TotalResolved", snoDetailsObj1.getLong(3));
				jsonObject1.put("TotalPending", snoDetailsObj1.getLong(4));
				jsonObject1.put("totalPendingDays", snoDetailsObj1.getLong(5));
				jsonObject1.put("avgPendingDays", snoDetailsObj1.getLong(6));
				jsonArray.put(jsonObject1);
			}
			details.put("HospitalWiseCCE", jsonArray);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public String getGOCCEWiseCount(GODashboardBean bean) {
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GO_DASHBOARD_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SECTION_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT_TOTAL", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", bean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", bean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", bean.getDistrictCode());
			storedProcedureQuery.setParameter("P_SECTION_FLAG", bean.getSectionFlag());
			storedProcedureQuery.setParameter("P_FLAG", bean.getInSection());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("totalAssign", snoDetailsObj.getLong(1));
				jsonObject.put("totalActionTaken", snoDetailsObj.getLong(2));
				jsonObject.put("totalYes", snoDetailsObj.getLong(3));
				jsonObject.put("totalNo", snoDetailsObj.getLong(4));
				jsonObject.put("totalNotConnected", snoDetailsObj.getLong(5));
				jsonObject.put("Question1Yes", snoDetailsObj.getLong(6));
				jsonObject.put("Question1No", snoDetailsObj.getLong(7));
				jsonObject.put("Question2Yes", snoDetailsObj.getLong(8));
				jsonObject.put("Question2No", snoDetailsObj.getLong(9));
				jsonObject.put("Question3Yes", snoDetailsObj.getLong(10));
				jsonObject.put("Question3No", snoDetailsObj.getLong(11));
				jsonObject.put("Question4Yes", snoDetailsObj.getLong(12));
				jsonObject.put("Question4No", snoDetailsObj.getLong(13));
				jsonObject.put("DCPending", snoDetailsObj.getLong(14));
				jsonObject.put("DGOPending", snoDetailsObj.getLong(15));
				jsonObject.put("GOPending", snoDetailsObj.getLong(16));
				jsonObject.put("totalPending", snoDetailsObj.getLong(17));
				jsonObject.put("DCResolve", snoDetailsObj.getLong(18));
				jsonObject.put("DGOResolve", snoDetailsObj.getLong(19));
				jsonObject.put("GOResolve", snoDetailsObj.getLong(20));
				jsonObject.put("TotalResolve", snoDetailsObj.getLong(21));
				details.put("CCETotal", jsonObject);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public List<Object> getlist(Date fromDate, Date toDate, Long userId, String urn) {
		List<Object> getlistdata = new ArrayList<Object>();
		ResultSet listrs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BES_APPROVAL_VIEW")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DEPREGID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACKNOWLEDGEMENTNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "A");
			storedProcedure.setParameter("P_USERID", userId);
			storedProcedure.setParameter("P_FROMDATE", fromDate);
			storedProcedure.setParameter("P_TODATE", toDate);
			storedProcedure.setParameter("P_DEPREGID", null);
			storedProcedure.setParameter("P_ACKNOWLEDGEMENTNO", null);
			storedProcedure.setParameter("P_URN", urn.trim());
			storedProcedure.execute();
			listrs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSGOUT");
			while (listrs.next()) {
				Enrollmentbean enroll = new Enrollmentbean();
				enroll.setDeprgid(listrs.getString(1));
				enroll.setAcknowledgementno(listrs.getString(2));
				enroll.setUrn(listrs.getString(3));
				enroll.setMemberid(listrs.getString(4));
				enroll.setMembername(listrs.getString(5));
				enroll.setMaskAadharnumber(listrs.getString(6));
				enroll.setGender(listrs.getString(7));
				enroll.setDob(listrs.getString(8));
				enroll.setRegistrationdate(listrs.getString(9));
				enroll.setHospitalcode(listrs.getString(10));
				enroll.setAge(listrs.getString(11));
				enroll.setCurrentstatus(listrs.getString(12));
				enroll.setHospname(listrs.getString(14));
				getlistdata.add(enroll);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (listrs != null) {
					listrs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return getlistdata;
	}

	@Override
	public String getenrollmentdetailsthroughid(Date fromDate, Date toDate, Long userId, Long depgrid,
			String acknowledgementno) throws Exception {
		JSONArray jsonArrayen = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsen = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BES_APPROVAL_VIEW")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DEPREGID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACKNOWLEDGEMENTNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "B");
			storedProcedure.setParameter("P_USERID", userId);
			storedProcedure.setParameter("P_FROMDATE", fromDate);
			storedProcedure.setParameter("P_TODATE", toDate);
			storedProcedure.setParameter("P_DEPREGID", depgrid);
			storedProcedure.setParameter("P_ACKNOWLEDGEMENTNO", acknowledgementno.trim());
			storedProcedure.setParameter("P_URN", null);
			storedProcedure.execute();
			snoDetailsen = (ResultSet) storedProcedure.getOutputParameterValue("P_MSGOUT");
			if (snoDetailsen.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("schemename", snoDetailsen.getString(1));
				jsonObject.put("schemetype", snoDetailsen.getString(2));
				jsonObject.put("acknowledgementno", snoDetailsen.getString(3));
				jsonObject.put("urn", snoDetailsen.getString(4));
				jsonObject.put("meberid", snoDetailsen.getString(5));
				jsonObject.put("membername", snoDetailsen.getString(6));
				jsonObject.put("mskadhaar", snoDetailsen.getString(7));
				jsonObject.put("gender", snoDetailsen.getString(8));
				jsonObject.put("dob", snoDetailsen.getString(9));
				jsonObject.put("age", snoDetailsen.getString(10));
				jsonObject.put("headname", snoDetailsen.getString(11));
				jsonObject.put("relationwithfamilyhead", snoDetailsen.getString(12));
				jsonObject.put("familyheadmobilenumber", snoDetailsen.getString(13));
				jsonObject.put("policystart", snoDetailsen.getString(14));
				jsonObject.put("policyenddate", snoDetailsen.getString(15));
				jsonObject.put("authenticatorname", snoDetailsen.getString(16));
				jsonObject.put("relationwithauthenticator", snoDetailsen.getString(17));
				jsonObject.put("authenticatormobile", snoDetailsen.getString(18));
				jsonObject.put("statename", snoDetailsen.getString(19));
				jsonObject.put("districtname", snoDetailsen.getString(20));
				jsonObject.put("blockname", snoDetailsen.getString(21));
				jsonObject.put("panchayatname", snoDetailsen.getString(22));
				jsonObject.put("villagename", snoDetailsen.getString(23));
				jsonObject.put("identitydoc", snoDetailsen.getString(24));
				jsonObject.put("declarationdoc", snoDetailsen.getString(25));
				jsonObject.put("registrationdate", snoDetailsen.getString(26));
				jsonObject.put("hospitalcode", snoDetailsen.getString(27));
				jsonObject.put("currentstatus", snoDetailsen.getString(28));
				jsonObject.put("enrregid", snoDetailsen.getString(29));
				jsonObject.put("actiontakenby", snoDetailsen.getString(30));
				jsonObject.put("actiontakenon", snoDetailsen.getString(31));
				jsonObject.put("remark", snoDetailsen.getString(32));
				jsonObject.put("remarkdescription", snoDetailsen.getString(33));
				jsonObject.put("compliedremark", snoDetailsen.getString(34));
				jsonObject.put("compliedoc", snoDetailsen.getString(35));
				jsonObject.put("statecode", snoDetailsen.getString(36));
				jsonObject.put("districtcode", snoDetailsen.getString(37));
				jsonObject.put("blockcode", snoDetailsen.getString(38));
				jsonArrayen.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		} finally {
			try {
				if (snoDetailsen != null) {
					snoDetailsen.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArrayen.toString();
	}

	@Override
	public Response getaction(Enrollmentapprovalbean requestBean) throws Exception {
		Response response = new Response();
		Integer claimraiseInteger = null;
		try {
			StoredProcedureQuery saveCpdUserData = this.entityManager.createStoredProcedureQuery("USP_BES_APPROVAL")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ENRREGID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DEPREGID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACKNOWLEDGEMENTNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARK", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			saveCpdUserData.setParameter("P_ACTION", requestBean.getType().trim());
			saveCpdUserData.setParameter("P_USERID", requestBean.getUserid());
			saveCpdUserData.setParameter("P_ENRREGID", requestBean.getEnrregid());
			saveCpdUserData.setParameter("P_DEPREGID", requestBean.getDepregid());
			saveCpdUserData.setParameter("P_ACKNOWLEDGEMENTNO", requestBean.getAcknowledgementno().trim());
			saveCpdUserData.setParameter("P_REMARK", requestBean.getActionRemarkId());
			saveCpdUserData.setParameter("P_DESCRIPTION", requestBean.getRemarks().trim());
			saveCpdUserData.execute();
			claimraiseInteger = (Integer) saveCpdUserData.getOutputParameterValue("P_MSGOUT");
			if (claimraiseInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Approved Successfully");
			} else if (claimraiseInteger == 2) {
				response.setStatus("Error");
				response.setMessage("Record Is Already Submitted..");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public String getactiontakenhistorydetails(Date fromDate, Date toDate, Long userId, Long depregid,
			String acknowledgementnumber) throws Exception {
		JSONArray jsonArrayhistory = new JSONArray();
		JSONObject jsonObject;
		ResultSet historyrs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BES_APPROVAL_VIEW")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DEPREGID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACKNOWLEDGEMENTNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "C");
			storedProcedure.setParameter("P_USERID", userId);
			storedProcedure.setParameter("P_FROMDATE", fromDate);
			storedProcedure.setParameter("P_TODATE", toDate);
			storedProcedure.setParameter("P_DEPREGID", depregid);
			storedProcedure.setParameter("P_ACKNOWLEDGEMENTNO", acknowledgementnumber.trim());
			storedProcedure.execute();
			historyrs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSGOUT");
			while (historyrs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("depegrid", historyrs.getString(1));
				jsonObject.put("acknowledgementno", historyrs.getString(2));
				jsonObject.put("urn", historyrs.getString(3));
				jsonObject.put("meberid", historyrs.getString(4));
				jsonObject.put("membername", historyrs.getString(5));
				jsonObject.put("mskadhaar", historyrs.getString(6));
				jsonObject.put("gender", historyrs.getString(7));
				jsonObject.put("dob", historyrs.getString(8));
				jsonObject.put("registrationdate", historyrs.getString(9));
				jsonObject.put("hospitalcode", historyrs.getString(10));
				jsonObject.put("age", historyrs.getString(11));
				jsonObject.put("currentstatus", historyrs.getString(12));
				jsonObject.put("actiontakenby", historyrs.getString(13));
				jsonObject.put("actiontakenon", historyrs.getString(14));
				jsonArrayhistory.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		} finally {
			try {
				if (historyrs != null) {
					historyrs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArrayhistory.toString();
	}

	@Override
	public String getGOCountReportDetails(GODashboardBean bean) {
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GO_DASHBOARD_RPT_DTLS")
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PENDINGAT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROM_DATE", bean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", bean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", bean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", bean.getDistrictCode());
			storedProcedureQuery.setParameter("P_FLAG", bean.getInSection());
			storedProcedureQuery.setParameter("P_PENDINGAT", bean.getSectionFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");

			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("applicationNo", snoDetailsObj.getString(1));
				jsonObject.put("beneficiaryName", snoDetailsObj.getString(2));
				jsonObject.put("contactNo", snoDetailsObj.getString(3));
				jsonObject.put("stateName", snoDetailsObj.getString(4));
				jsonObject.put("districtName", snoDetailsObj.getString(5));
				jsonObject.put("hospitalDetails", snoDetailsObj.getString(6));
				jsonObject.put("registeredBy", snoDetailsObj.getString(7));
				jsonObject.put("grievanceMedium", snoDetailsObj.getString(8));
				jsonObject.put("appliedDate",
						snoDetailsObj.getDate(9) != null
								? new SimpleDateFormat("dd-MMM-yyyy").format(snoDetailsObj.getDate(9))
								: "");
				jsonObject.put("lastActionBy", snoDetailsObj.getString(10));
				jsonObject.put("pendingAt", snoDetailsObj.getString(11));
				jsonObject.put("grievanceType", snoDetailsObj.getString(12));
				jsonArray.put(jsonObject);
			}
			details.put("grievanceDetails", jsonArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}

		return details.toString();
	}

	public List<Object> getcomplylist(Date fromDate, Date toDate, String urn, Long userId) {
		List<Object> complydata = new ArrayList<Object>();
		ResultSet complyrs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BES_APPROVAL_VIEW")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DEPREGID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACKNOWLEDGEMENTNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "D");
			storedProcedure.setParameter("P_USERID", userId);
			storedProcedure.setParameter("P_FROMDATE", fromDate);
			storedProcedure.setParameter("P_TODATE", toDate);
			storedProcedure.setParameter("P_DEPREGID", null);
			storedProcedure.setParameter("P_ACKNOWLEDGEMENTNO", null);
			storedProcedure.setParameter("P_URN", urn.trim());
			storedProcedure.execute();
			complyrs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSGOUT");
			while (complyrs.next()) {
				Enrollmentbean enroll = new Enrollmentbean();
				enroll.setDeprgid(complyrs.getString(1));
				enroll.setAcknowledgementno(complyrs.getString(2));
				enroll.setUrn(complyrs.getString(3));
				enroll.setMemberid(complyrs.getString(4));
				enroll.setMembername(complyrs.getString(5));
				enroll.setMaskAadharnumber(complyrs.getString(6));
				enroll.setGender(complyrs.getString(7));
				enroll.setDob(complyrs.getString(8));
				enroll.setRegistrationdate(complyrs.getString(9));
				enroll.setHospitalcode(complyrs.getString(10));
				enroll.setAge(complyrs.getString(11));
				enroll.setCurrentstatus(complyrs.getString(12));
				enroll.setHospname(complyrs.getString(14));
				complydata.add(enroll);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (complyrs != null) {
					complyrs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return complydata;
	}

	@Override
	public void downLoadFile(String fileName, String year, String hCode, String Statecode, String districtcode,
			String blockcode, String enrollmentfolder, HttpServletResponse response) {
		String folderName = null;
		if (fileName.startsWith(bskyResourcesBundel.getString("file.presurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.presurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.postsurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.postsurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Intrasurgery.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.IntraSurgeryPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Specimen.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.SpecimenRemovalPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Patient.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.PatientPic");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.moredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.needmoredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.AdditionalDoc.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.AdditionalDoc");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.DischargeSlip.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.DischargeSlip");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc1.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc2.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc1.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc2.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc3.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce3");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.dgOfficer.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.dgoDocument");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Declaration.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Declaration");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Identity.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Identity");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Recomply.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Recomply");
		} else if (fileName.startsWith("OC")) {
			folderName = bskyResourcesBundel.getString("folder.Overridefile");
		} else {
			folderName = "PREAUTHDOC";
		}
		try {
			CommonFileUpload.downloadFileenrollment(fileName, enrollmentfolder.trim(), year.trim(), hCode,
					Statecode.trim(), districtcode.trim(), blockcode.trim(), folderName, response);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<Object> getactiontakenhistorylist(Long enggid, String acknowledgementno) {
		List<Object> actiontakenon = new ArrayList<Object>();
		ResultSet actionrs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BES_ACTIONTAKEN_LOG_RPT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ENRREGID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACKNOWLEDGEMENTNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "A");
			storedProcedure.setParameter("P_ENRREGID", enggid);
			storedProcedure.setParameter("P_ACKNOWLEDGEMENTNO", acknowledgementno.trim());
			storedProcedure.execute();
			actionrs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSGOUT");
			while (actionrs.next()) {
				Actiontakenhistorybean action = new Actiontakenhistorybean();
				action.setIdentitydoc(actionrs.getString(1));
				action.setDeclarationdoc(actionrs.getString(2));
				action.setActiontype(actionrs.getString(3));
				action.setActionby(actionrs.getString(4));
				action.setActiontakenon(actionrs.getString(5));
				action.setRemark(actionrs.getString(6));
				action.setDescription(actionrs.getString(7));
				action.setCompliedon(actionrs.getString(8));
				action.setCompliedremark(actionrs.getString(9));
				action.setComplieddoc(actionrs.getString(10));
				action.setStatecode(actionrs.getString(11));
				action.setDistrictcode(actionrs.getString(12));
				action.setBlockcode(actionrs.getString(13));
				action.setHospitalcode(actionrs.getString(14));
				actiontakenon.add(action);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (actionrs != null) {
					actionrs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return actiontakenon;
	}

	@Override
	public List<Object> gethospitalenrollmentlistactiontakenDetails(String urn, Date fromDate, Date toDate, Long userId,
			String username, Long searchdata, String state, String dist, String hospital) {
		List<Object> hospitallist = new ArrayList<Object>();
		ResultSet hospitals = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BES_ACTION_TAKEN_DETAILS")
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STSTUS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_FROMDATE", fromDate);
			storedProcedure.setParameter("P_TODATE", toDate);
			storedProcedure.setParameter("P_USERID", userId);
			storedProcedure.setParameter("P_URN", urn.trim());
			storedProcedure.setParameter("P_STSTUS", searchdata);
			storedProcedure.setParameter("P_STATECODE", state);
			storedProcedure.setParameter("P_DISTRICTCODE", dist);
			storedProcedure.setParameter("P_HOSPITALCODE", hospital);
			storedProcedure.execute();
			hospitals = (ResultSet) storedProcedure.getOutputParameterValue("P_MSGOUT");
			while (hospitals.next()) {
				EnrollmentActionbean enroll = new EnrollmentActionbean();
				enroll.setDeprgid(hospitals.getString(1));
				enroll.setEnrregid(hospitals.getString(2));
				enroll.setAcknowledgementno(hospitals.getString(3));
				enroll.setUrn(hospitals.getString(4));
				enroll.setMemberid(hospitals.getString(5));
				enroll.setMembername(hospitals.getString(6));
				enroll.setMaskAadharnumber(hospitals.getString(7));
				enroll.setGender(hospitals.getString(8));
				enroll.setDob(hospitals.getString(9));
				enroll.setRegistrationdate(hospitals.getString(10));
				enroll.setHospitalcode(hospitals.getString(11));
				enroll.setAge(hospitals.getString(12));
				enroll.setCurrentstatus(hospitals.getString(13));
				enroll.setActiontakenby(hospitals.getString(14));
				enroll.setActiontakenon(hospitals.getString(15));
				enroll.setRemark(hospitals.getString(16));
				enroll.setDescription(hospitals.getString(17));
				enroll.setHospitalname(hospitals.getString(18));
				hospitallist.add(enroll);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (hospitals != null) {
					hospitals.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return hospitallist;
	}
}
