package com.project.bsky.serviceImpl;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.repository.MstResponseMessageRepository;
import com.project.bsky.service.CPDClaimRevertService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;

/**
 * @author Debendra Nayak
 *
 */
@SuppressWarnings({ "unused", "unchecked" })
@Service
public class CPDClaimRevertServiceImpl implements CPDClaimRevertService {

	private final Logger logger;

	@Autowired
	public CPDClaimRevertServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	CPDClaimProcessingServiceImpl CPDClaimProcessingServiceImpl;

	@Autowired
	private MstResponseMessageRepository mstResponseMessageRepository;

	@Override
	public String getCPDClaimRevertList(Integer userId, String orderValue, String fromDate, String toDate,
			Integer authMode, Integer trigger, Integer schemeid, String schemecategoryid) {
		List<Object[]> claimRaiseDetailsList;
		JSONArray jsonArray = new JSONArray();
		String authcode = null;
		Integer schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Integer.parseInt(schemecategoryid);
		} else {
			schemecatId = null;
		}
		JSONObject jsonObject;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_REVERT_LIST")
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ORDER_VALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_ORDER_VALUE", orderValue);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_AUTH_MODE", authMode);
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", trigger);
			storedProcedureQuery.setParameter("P_SCHEME_ID", schemeid);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			claimRaiseDetailsList = storedProcedureQuery.getResultList();
			for (Iterator<Object[]> iterator = claimRaiseDetailsList.iterator(); iterator.hasNext();) {
				Object[] claimRaiseDetails = iterator.next();
				jsonObject = new JSONObject();
				jsonObject.put("claimID", claimRaiseDetails[0]);
				jsonObject.put("transactionID", claimRaiseDetails[1]);
				jsonObject.put("URN", claimRaiseDetails[2]);
				jsonObject.put("patientName", claimRaiseDetails[3]);
				jsonObject.put("packageCode", claimRaiseDetails[4]);
				jsonObject.put("packageId", claimRaiseDetails[5]);
				jsonObject.put("packageName", claimRaiseDetails[6]);
				jsonObject.put("currentTotalAmount", claimRaiseDetails[7]);
				jsonObject.put("dateOfDischarge", claimRaiseDetails[8]);
				jsonObject.put("transClaimId", claimRaiseDetails[9]);
				jsonObject.put("revisedDate", claimRaiseDetails[10]);
				jsonObject.put("invoiceNo", claimRaiseDetails[11]);
				jsonObject.put("allotedDate", claimRaiseDetails[12]);
				if (claimRaiseDetails[13] != null) {
					authcode = claimRaiseDetails[13].toString().substring(2);
				}
				jsonObject.put("authorizedcode", authcode);
				jsonObject.put("hospitalcode", claimRaiseDetails[14]);
				jsonObject.put("actualDate", claimRaiseDetails[15]);
				jsonObject.put("claimNo", claimRaiseDetails[16]);
				jsonObject.put("takenDate", claimRaiseDetails[17]);
				jsonObject.put("remainingDate", claimRaiseDetails[18].toString() + " days left");
				jsonObject.put("actualDateOfDischarge", claimRaiseDetails[19]);
				jsonObject.put("caseNo", claimRaiseDetails[20] != null ? claimRaiseDetails[20] : "N/A");
				jsonObject.put("triggerValue", claimRaiseDetails[21]);
				jsonObject.put("triggerMessage", claimRaiseDetails[22]);
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getCPDClaimRevertList() method of CPDClaimRevertServiceImpl", e);
			throw new RuntimeException(e);
		}
		return jsonArray.toString();
	}

	@Override
	public String getCPDClaimRevertDetails(String transaction_id, String urn, String transClaimId,
			String authorizedCode, String hospitalCode, String actualDate, String caseNo, Long userId,
			Date actionClickTime, String claimNo) {
		List<Object[]> claimRaiseDetailsList;
		Integer timingLogId = null;
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();
		JSONObject jsonObject3 = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray2 = new JSONArray();
		JSONObject jsonObject1 = new JSONObject();
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		String title = "";
		ResultSet logDetails = null;
		ResultSet resultSet = null;
		ResultSet meTrigger = null;
		ResultSet claimdetails = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery1 = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_ACTION_TIMING")
					.registerStoredProcedureParameter("P_METHOD", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CASE_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIM_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIM_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CLICK_TIME", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_TAKEN_TIME", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_TYPE_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TIMING_LOG_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery1.setParameter("P_METHOD", 0);
			storedProcedureQuery1.setParameter("P_CPD_USER_ID", userId);
			storedProcedureQuery1.setParameter("P_URN", urn);
			storedProcedureQuery1.setParameter("P_CASE_NO", caseNo);
			storedProcedureQuery1.setParameter("P_CLAIM_NO", claimNo);
			storedProcedureQuery1.setParameter("P_CLAIM_ID", Integer.parseInt(transClaimId));
			storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", Integer.parseInt(transaction_id));
			storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", actionClickTime);
			storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", null);
			storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID", null);
			storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", null);
			storedProcedureQuery1.execute();
			resultSet = (ResultSet) storedProcedureQuery1.getOutputParameterValue("P_MSG_OUT");
			while (resultSet != null && resultSet.next()) {
				timingLogId = resultSet.getInt(1);
			}
			JSONArray preAuthLogJsonArray = getPreAuthHistory(urn, authorizedCode, hospitalCode);
			jsonObject2.put("preAuthLogList", preAuthLogJsonArray);
			JSONArray reasonJsonArray = getReasonMaster();
			jsonObject2.put("reasonList", reasonJsonArray);
			JSONArray multiPackageArray = CPDClaimProcessingServiceImpl.getMultiplePackageBlocking(urn, actualDate);
			jsonObject2.put("multiPackList", multiPackageArray);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_REVERT_DTLS")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimed_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_cpd_log_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("tranId", Integer.parseInt(transaction_id));
			storedProcedureQuery.setParameter("tranCode", "0303");
			storedProcedureQuery.execute();
			claimRaiseDetailsList = storedProcedureQuery.getResultList();
			claimdetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_claimed_details");
			logDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_cpd_log_details");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			meTrigger = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			if (claimdetails.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("hospitalName", claimdetails.getString(1));
				jsonObject.put("hospitalCode", claimdetails.getString(2));
				jsonObject.put("invoiceNo", claimdetails.getString(3));
				if (claimdetails.getString(4).equalsIgnoreCase("F"))
					title = "Mrs.";
				else
					title = "Mr";
				jsonObject.put("gender", claimdetails.getString(4));
				jsonObject.put("patientName", title + " " + claimdetails.getString(5));
				if (claimdetails.getString(6) != null && claimdetails.getString(7) != null) {
					jsonObject.put("dateOfAdmission", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimdetails.getString(6))));
					jsonObject.put("dateOfDischarge", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimdetails.getString(7))));
				} else if (claimdetails.getString(6) != null && claimdetails.getString(7) == null) {
					jsonObject.put("dateOfAdmission", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimdetails.getString(6))));
					jsonObject.put("dateOfDischarge", "");
				} else if (claimdetails.getString(6) == null && claimdetails.getString(7) != null) {
					jsonObject.put("dateOfAdmission", "");
					jsonObject.put("dateOfDischarge", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimdetails.getString(7))));
				}
				jsonObject.put("noOfDays", CommonFileUpload.calculateNoOfDays((String) claimdetails.getString(6),
						(String) claimdetails.getString(7)));
				jsonObject.put("procedureName", claimdetails.getString(8));
				jsonObject.put("packageName", claimdetails.getString(9));
				jsonObject.put("patientAddress", claimdetails.getString(10));
				jsonObject.put("hospitalClaimedAmount", claimdetails.getString(11));
				jsonObject.put("packageCost", claimdetails.getString(12));
				jsonObject.put("age", claimdetails.getString(13));
				jsonObject.put("hospitalAddress", claimdetails.getString(14));
				jsonObject.put("admissionSlip", claimdetails.getString(15));
				jsonObject.put("additinalSlip", claimdetails.getString(17));
				jsonObject.put("dischargeSlip", claimdetails.getString(18));
				jsonObject.put("preSurgerySlip", claimdetails.getString(19));
				jsonObject.put("postSurgerySlip", claimdetails.getString(20));
				jsonObject.put("packageCode", claimdetails.getString(21));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(claimdetails.getString(22)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(claimdetails.getString(23)));
				jsonObject.put("STATENAME", claimdetails.getString(24));
				jsonObject.put("DISTRICTNAME", claimdetails.getString(25));
				jsonObject.put("BLOCKNAME", claimdetails.getString(26));
				jsonObject.put("VILLAGENAME", claimdetails.getString(27));
				jsonObject.put("FAMILYHEADNAME", claimdetails.getString(28));
				jsonObject.put("VERIFIERNAME", claimdetails.getString(29));
				if (claimdetails.getString(30) == null)
					jsonObject.put("MORTALITY", "NA");
				else if (claimdetails.getString(30).equalsIgnoreCase("N"))
					jsonObject.put("MORTALITY", "NO");
				else
					jsonObject.put("MORTALITY", "YES");
				jsonObject.put("AUTHORIZEDCODE", claimdetails.getString(32));
				jsonObject.put("REFERRALCODE", claimdetails.getString(31));
				jsonObject.put("NABHFlag", claimdetails.getString(33));
				jsonObject.put("claimCaseNo", claimdetails.getString(34));
				jsonObject.put("claimBillNo", claimdetails.getString(35));
				jsonObject.put("intraSurgery", claimdetails.getString(36));
				jsonObject.put("specimenPhoto", claimdetails.getString(37));
				jsonObject.put("patientPhoto", claimdetails.getString(38));
				jsonObject.put("claimNo", claimdetails.getString(39));
				jsonObject.put("implantData", claimdetails.getString(40));
				jsonObject.put("URN", urn);
				jsonObject.put("patientPhono", claimdetails.getString(41));
				jsonObject.put("queryCount", claimdetails.getString(42));
				jsonObject.put("verificationMode",
						claimdetails.getString(44) != null ? claimdetails.getString(44) : "N/A");
				jsonObject.put("isPatientVerified",
						claimdetails.getString(45) != null ? claimdetails.getString(45) : "N/A");
				jsonObject.put("referralStatus",
						claimdetails.getString(46) != null ? claimdetails.getString(46) : "N/A");
				jsonObject.put("txnPackageDetailsId",
						claimdetails.getString(47) != null ? claimdetails.getLong(47) : null);
				jsonObject.put("packageCode1", claimdetails.getString(48) != null ? claimdetails.getString(48) : "N/A");
				jsonObject.put("packageName1", claimdetails.getString(49) != null ? claimdetails.getString(49) : "N/A");
				jsonObject.put("subPackageCode1",
						claimdetails.getString(50) != null ? claimdetails.getString(50) : "N/A");
				jsonObject.put("subPackageName1",
						claimdetails.getString(51) != null ? claimdetails.getString(51) : "N/A");
				jsonObject.put("procedureCode1",
						claimdetails.getString(52) != null ? claimdetails.getString(52) : "N/A");
				jsonObject.put("procedureName1",
						claimdetails.getString(53) != null ? claimdetails.getString(53) : "N/A");
				jsonObject.put("packageCost1", claimdetails.getString(54) != null ? claimdetails.getString(54) : "N/A");
				jsonObject.put("totalamountblocked",
						claimdetails.getString(55) != null ? claimdetails.getString(55) : "N/A");
				jsonObject.put("createdon", claimdetails.getString(56) != null ? claimdetails.getString(56) : "N/A");
				jsonObject.put("MEMBERID", claimdetails.getString(57) != null ? claimdetails.getString(57) : "N/A");
				jsonObject.put("ISEMERGENCY", claimdetails.getString(58) != null ? claimdetails.getString(58) : "N/A");
				jsonObject.put("OVERRIDECODE", claimdetails.getString(59));
				jsonObject.put("TREATMENTDAY", claimdetails.getString(60));
				jsonObject.put("DOCTORNAME", claimdetails.getString(61));
				jsonObject.put("FROMHOSPITALNAME", claimdetails.getString(62));
				jsonObject.put("TOHOSPITAL", claimdetails.getString(63));
				jsonObject.put("DISREMARKS", claimdetails.getString(64));
				jsonObject.put("TRANSACTIONDESCRIPTION", claimdetails.getString(65));
				jsonObject.put("HOSPITALCATEGORYNAME",
						claimdetails.getString(66) != null ? claimdetails.getString(66) : "N/A");
				jsonObject.put("verification_DISCHARGE", claimdetails.getString(67));
				jsonObject.put("snaFullName", claimdetails.getString(68) != null ? claimdetails.getString(68) : "NA");
				jsonObject.put("snaPhone", claimdetails.getString(69) != null ? claimdetails.getString(69) : "NA");
				jsonObject.put("snaUserId", claimdetails.getLong(70));
				jsonObject.put("mortalitydocument", claimdetails.getString(71));
				jsonObject.put("categoryName", claimdetails.getString(72));
			}

			while (logDetails.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("APPROVEDAMOUNT", logDetails.getString(1));
				jsonObject1.put("ACTIONTYPE", logDetails.getString(2));
				jsonObject1.put("ACTIONBY", logDetails.getString(3));
				jsonObject1.put("DESCRIPTION", logDetails.getString(4));
				jsonObject1.put("ACTIONON", logDetails.getString(5));
				jsonObject1.put("DISCHARGESLIP", logDetails.getString(6));
				jsonObject1.put("ADITIONALDOCS", logDetails.getString(7));
				jsonObject1.put("ADDITIONALDOC1", logDetails.getString(8));
				jsonObject1.put("PRESURGERY", logDetails.getString(9));
				jsonObject1.put("POSTSURGERY", logDetails.getString(10));
				jsonObject1.put("REMARKS", logDetails.getString(12));
				jsonObject1.put("ADDITIONALDOC2", logDetails.getString(13));
				jsonObject1.put("IntraSurgery", logDetails.getString(14));
				jsonObject1.put("SpecimanPhoto", logDetails.getString(15));
				jsonObject1.put("PatientPhoto", logDetails.getString(16));
				jsonArray.put(jsonObject1);
			}
			while (snoDetailsObj2.next()) {
				jsonObject3 = new JSONObject();
				jsonObject3.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
				jsonObject3.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
				jsonObject3.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
				jsonObject3.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
				jsonArray1.put(jsonObject3);
			}

			while (meTrigger.next()) {
				JSONObject jsonObject4 = new JSONObject();
				jsonObject4.put("urn", meTrigger.getString(1));
				jsonObject4.put("claimNo", meTrigger.getString(2));
				jsonObject4.put("caseNo", meTrigger.getString(3));
				jsonObject4.put("patientName", meTrigger.getString(4));
				jsonObject4.put("phoneNo", meTrigger.getString(5));
				jsonObject4.put("hospitalName", meTrigger.getString(6));
				jsonObject4.put("hospitalCode", meTrigger.getString(7));
				jsonObject4.put("packageCode", meTrigger.getString(8));
				jsonObject4.put("packageName", meTrigger.getString(9));
				jsonObject4.put("actualDateOfAdmission", DateFormat.formatDate(meTrigger.getDate(10)));
				jsonObject4.put("actualDateOfDischarge", DateFormat.formatDate(meTrigger.getDate(11)));
				jsonObject4.put("hospitalClaimAmount", meTrigger.getLong(12));
				jsonObject4.put("reportName", meTrigger.getString(13));
				jsonObject4.put("claimId", meTrigger.getLong(14));
				jsonObject4.put("transactionId", meTrigger.getLong(15));
				jsonObject4.put("txnPackageId", meTrigger.getLong(16));
				jsonObject4.put("slNo", meTrigger.getLong(17));
				jsonObject4.put("createdOn", meTrigger.getDate(18));
				jsonObject4.put("statusFlag", meTrigger.getString(19));
				jsonObject4.put("doctorRegNo", meTrigger.getString(20));
				jsonObject4.put("surgeryDate", meTrigger.getDate(21));
				jsonArray2.put(jsonObject4);
			}
			while (ictDetails.next()) {
				ictDetailsObject = new JSONObject();
				ictDetailsObject.put("icdInfoId", ictDetails.getLong(1));
				ictDetailsObject.put("txnPackageDetailsId", ictDetails.getLong(2));
				ictDetailsObject.put("icdMode", ictDetails.getString(3));
				ictDetailsObject.put("icdCode", ictDetails.getString(4));
				ictDetailsObject.put("icdName", ictDetails.getString(5));
				ictDetailsObject.put("icdModeTxt", ictDetails.getString(6));
				ictDetailsObject.put("byGroupId", ictDetails.getLong(7));
				ictDetailsArray.put(ictDetailsObject);
			}
			while (ictSubDetails.next()) {
				ictSubDetailsObject = new JSONObject();
				ictSubDetailsObject.put("icdDtlsId", ictSubDetails.getLong(1));
				ictSubDetailsObject.put("icdInfoId", ictSubDetails.getLong(2));
				ictSubDetailsObject.put("icdSubCode", ictSubDetails.getString(3));
				ictSubDetailsObject.put("icdSubName", ictSubDetails.getString(4));
				ictSubDetailsArray.put(ictSubDetailsObject);
			}
			preAuthLog = getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
			jsonObject2.put("result", jsonObject);
			jsonObject2.put("approvalList", jsonArray);
			jsonObject2.put("timingLogId", timingLogId);
			jsonObject2.put("vitalArray", jsonArray1);
			jsonObject2.put("preAuthLogList", preAuthLog);
			jsonObject2.put("meTrigger", jsonArray2);
			jsonObject2.put("ictDetailsArray", ictDetailsArray);
			jsonObject2.put("ictSubDetailsArray", ictSubDetailsArray);

		} catch (Exception e) {
			logger.error("Exception in getClaimDetailsByTransactionId method of CPDClaimRevertServiceImpl:", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (logDetails != null)
					logDetails.close();
				if (claimdetails != null)
					claimdetails.close();
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();

			} catch (Exception e2) {
				logger.error("Exception in getClaimDetailsByTransactionId method of CPDClaimRevertServiceImpl:", e2);
			}
		}
		return jsonObject2.toString();
	}

	private JSONArray getApprovalHistory(String transaction_id, String urn, Integer transClaimId)
			throws ParseException, SQLException {
		ResultSet snoDetailsObj1 = null;
		List<Object[]> approvalDetailsList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject1;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_RA_HISTORY")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("v_claim_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_claims_details", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("v_claim_id", transClaimId);
			storedProcedureQuery.execute();
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_cpd_claims_details");
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
				jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
				jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
				jsonObject1.put("IntraSurgery", snoDetailsObj1.getString(14));
				jsonObject1.put("SpecimanPhoto", snoDetailsObj1.getString(15));
				jsonObject1.put("PatientPhoto", snoDetailsObj1.getString(16));
				jsonArray.put(jsonObject1);
			}
		} catch (JSONException e) {
			logger.error("Exception in getApprovalHistory method of CPDClaimRevertServiceImpl:", e);
		} finally {
			try {
				if (snoDetailsObj1 != null)
					snoDetailsObj1.close();
			} catch (Exception e2) {
				logger.error("Exception in getApprovalHistory method of CPDClaimRevertServiceImpl:", e2);
				throw e2;
			}
		}
		return jsonArray;
	}

	private JSONArray getPreAuthHistory(String urn, String authorizedCode, String hospitalCode) throws ParseException {
		List<Object[]> preAuthLogList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PRE_AUTH_LOG_DTS")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("authorizedCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_preauth_details", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("authorizedCode", authorizedCode);
			storedProcedureQuery.setParameter("hospitalCode", hospitalCode);
			storedProcedureQuery.execute();
			preAuthLogList = storedProcedureQuery.getResultList();
			for (Iterator<Object[]> iterator = preAuthLogList.iterator(); iterator.hasNext();) {
				Object[] preAuthDetails = iterator.next();
				jsonObject = new JSONObject();
				jsonObject.put("urnNo", preAuthDetails[0]);
				jsonObject.put("memberName", preAuthDetails[1]);
				jsonObject.put("preAuthRemarks", preAuthDetails[2]);
				jsonObject.put("approvedAmount", preAuthDetails[3]);
				jsonObject.put("approvedDate", preAuthDetails[4]);
				jsonObject.put("preAuthFile", preAuthDetails[5]);
				jsonObject.put("hospitalCode", preAuthDetails[6]);
				jsonObject.put("claimedDate", preAuthDetails[7]);
				jsonObject.put("preauthCode", preAuthDetails[8]);
				jsonObject.put("claimedAmount", preAuthDetails[9]);
				jsonArray.put(jsonObject);
			}
		} catch (JSONException e) {
			logger.error("Exception in getPreAuthHistory method of CPDClaimRevertServiceImpl:", e);
		}
		return jsonArray;
	}

	private JSONArray getReasonMaster() throws ParseException, SQLException {
		ResultSet stateObj = null;
		List<Object[]> reasonMasterList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "REASON");
			storedProcedureQuery.execute();
			stateObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (stateObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("reasonId", stateObj.getInt(1));
				jsonObject.put("reasonName", stateObj.getString(2));
				jsonArray.put(jsonObject);
			}
		} catch (JSONException e) {
			logger.error("Exception in getReasonMaster method of CPDClaimRevertServiceImpl:", e);
		} finally {
			try {
				if (stateObj != null)
					stateObj.close();
			} catch (Exception e2) {
				logger.error("Exception in getReasonMaster method of CPDClaimRevertServiceImpl:", e2);
				throw e2;
			}
		}
		return jsonArray;
	}

	@Transactional
	@Override
	public String saveCpdClaimAction(Integer claimID, String action, String userId, String remarks, String reasonId,
			String cpdApprovedAmt, String urn) {
		String result = "";
		try {
			saveTransactIonLog(claimID, action, userId, remarks, reasonId, cpdApprovedAmt);
			if (action.equalsIgnoreCase("Approve") || action.equalsIgnoreCase("Reject")) {
				CPDClaimProcessingServiceImpl.saveCPDActionLog(claimID, action, userId, urn);
			}
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_APPOVAL_ACT")
					.registerStoredProcedureParameter("p_claimID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarsId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_approved_amnt", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_claimID", claimID);
			storedProcedureQuery.setParameter("p_action", action);
			storedProcedureQuery.setParameter("p_userId", Integer.parseInt(userId));
			storedProcedureQuery.setParameter("p_remarsId", Integer.parseInt(reasonId));
			storedProcedureQuery.setParameter("p_remarks", remarks.trim());
			storedProcedureQuery.setParameter("p_cpd_approved_amnt", cpdApprovedAmt);
			storedProcedureQuery.execute();
			result = (String) storedProcedureQuery.getOutputParameterValue("p_msgout");

		} catch (Exception e) {
			logger.error("Exception in saveCpdClaimAction method of CPDClaimRevertServiceImpl:", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	public void saveTransactIonLog(Integer claimID, String action, String userId, String remarks, String reasonId,
			String cpdApprovedAmt) throws Exception {
		Integer result = 0;
		List<Object[]> claimRaiseDetailsList;
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		int actionType = 0;
		try {

			StoredProcedureQuery claimdProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_CPD_CLMDTLS_BY_CLAIMID")
					.registerStoredProcedureParameter("p_claim_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_claims_details", void.class, ParameterMode.REF_CURSOR);

			claimdProcedureQuery.setParameter("p_claim_id", claimID);
			claimdProcedureQuery.execute();
			claimRaiseDetailsList = claimdProcedureQuery.getResultList();

			Map<String, Object> claimdetails = new HashMap<String, Object>();
			for (Iterator<Object[]> iterator = claimRaiseDetailsList.iterator(); iterator.hasNext();) {
				Object[] claimRaiseDetails = iterator.next();
				claimdetails.put("urn", claimRaiseDetails[0]);
				claimdetails.put("claimAmount", claimRaiseDetails[1]);
				claimdetails.put("adtitinalSlip", claimRaiseDetails[2]);
				claimdetails.put("dischargeSlip", claimRaiseDetails[3]);
				claimdetails.put("investigationOne", claimRaiseDetails[4]);
				claimdetails.put("investigationTwo", claimRaiseDetails[5]);
				claimdetails.put("presurgery", claimRaiseDetails[6]);
				claimdetails.put("postsurgery", claimRaiseDetails[7]);
				claimdetails.put("intraSurgery", claimRaiseDetails[8]);
				claimdetails.put("specimanPhoto", claimRaiseDetails[9]);
				claimdetails.put("patientPhoto", claimRaiseDetails[10]);
			}

			Map<String, Integer> claimMap = CommonFileUpload.createClaimStatusMap();
			for (Map.Entry<String, Integer> entry : claimMap.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(action)) {
					actionType = entry.getValue();
				}
			}
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_Claim_TxClaAct_Log_Insert")
					.registerStoredProcedureParameter("Action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("claimId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("discharegeSlip", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("aditionalSlip", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("investigationOne", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("investigationTwo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("presurgeryphoto", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("postsurgeryphoto", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("claimAmount", Double.class, ParameterMode.IN)
					.registerStoredProcedureParameter("intraSurgery", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("specimanPhoto", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("patientPhoto", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("approvedAmount", Double.class, ParameterMode.IN)
					.registerStoredProcedureParameter("actionType", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("actionBy", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("actionOn", Timestamp.class, ParameterMode.IN)
					.registerStoredProcedureParameter("createdBy", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("statusFlag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("createdon", Timestamp.class, ParameterMode.IN)
					.registerStoredProcedureParameter("remarksId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("userIp", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("Action", "I");
			storedProcedureQuery.setParameter("claimId", claimID);
			for (Map.Entry<String, Object> entry : claimdetails.entrySet()) {
				if (entry.getKey().equalsIgnoreCase("urn"))
					storedProcedureQuery.setParameter("urnNo", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("dischargeSlip"))
					storedProcedureQuery.setParameter("discharegeSlip", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("adtitinalSlip"))
					storedProcedureQuery.setParameter("aditionalSlip", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("investigationOne"))
					storedProcedureQuery.setParameter("investigationOne", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("investigationTwo"))
					storedProcedureQuery.setParameter("investigationTwo", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("presurgery"))
					storedProcedureQuery.setParameter("presurgeryphoto", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("postsurgery"))
					storedProcedureQuery.setParameter("postsurgeryphoto", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("claimAmount")) {
					storedProcedureQuery.setParameter("claimAmount",
							Double.parseDouble(String.valueOf(entry.getValue())));
				}
				if (entry.getKey().equalsIgnoreCase("intraSurgery"))
					storedProcedureQuery.setParameter("intraSurgery", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("specimanPhoto"))
					storedProcedureQuery.setParameter("specimanPhoto", (String) entry.getValue());
				if (entry.getKey().equalsIgnoreCase("patientPhoto"))
					storedProcedureQuery.setParameter("patientPhoto", (String) entry.getValue());
			}

			if (cpdApprovedAmt.contains(",")) {
				storedProcedureQuery.setParameter("approvedAmount",
						Double.parseDouble(String.valueOf(cpdApprovedAmt.replace(",", ""))));
			} else {
				storedProcedureQuery.setParameter("approvedAmount", Double.parseDouble(String.valueOf(cpdApprovedAmt)));
			}
			storedProcedureQuery.setParameter("actionType", actionType);
			storedProcedureQuery.setParameter("actionBy", Integer.parseInt(userId));
			storedProcedureQuery.setParameter("actionOn", new Timestamp(System.currentTimeMillis()));
			storedProcedureQuery.setParameter("createdBy", Integer.parseInt(userId));
			storedProcedureQuery.setParameter("statusFlag", 0);
			storedProcedureQuery.setParameter("remarks", remarks.trim());
			storedProcedureQuery.setParameter("createdon", new Timestamp(System.currentTimeMillis()));
			storedProcedureQuery.setParameter("remarksId", Integer.parseInt(reasonId));
			storedProcedureQuery.setParameter("userIp", getuseripaddressString);
			storedProcedureQuery.execute();
			result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		} catch (Exception e) {
			logger.error(
					"Exception occurred in saveCPDClaimRevert method of CPDClaimRevertServiceImpl " + e.getMessage());
		}
	}

	@Override
	public Map<String, Object> getCPDClaimRevertListCount(String userId, String orderValue, String fromDate,
			String toDate, Integer authMode, Integer trigger, Integer schemeid, String schemecategoryid) {
		int revertClaimCount = 0;
		ResultSet resultSet;
		Integer schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Integer.parseInt(schemecategoryid);
		} else {
			schemecatId = null;
		}
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_REVERT_LIST_COUNT")
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ORDER_VALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", Integer.parseInt(userId))
					.setParameter("P_ORDER_VALUE", orderValue).setParameter("P_FROM_DATE", fromDate)
					.setParameter("P_TO_DATE", toDate).setParameter("P_AUTH_MODE", authMode)
					.setParameter("P_TRIGGERTYPE", trigger).setParameter("P_SCHEME_ID", schemeid)
					.setParameter("P_SCHEMECATEGORY_ID", schemecatId);

			storedProcedureQuery.execute();
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (resultSet != null && resultSet.next()) {
				revertClaimCount = resultSet.getInt("REVERT_CLAIM_COUNT");
			}
			response.put("revertClaimCount", revertClaimCount);
		} catch (Exception e) {
			logger.error("Exception occurred in getCPDClaimRevertListCount method of CPDClaimRevertServiceImpl "
					+ e.getMessage());
			throw new RuntimeException(e);
		}
		return response;
	}

	@Override
	public Map<String, Object> getPackageDetailsInfoList(Long txnPackageDetailsId) {
		Map<String, Object> responseMap = new LinkedHashMap<>(), highEndDrugInfo, implantInfo, wardInfo;
		List<Map<String, Object>> highEndDrugInfoList = new ArrayList<>(), implantInfoList = new ArrayList<>(),
				wardInfoList = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
		ResultSet hedInfoResultSet, implantInfoResultSet, wardInfoResultSet;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_CODE_WISE_DETAILS_CMS")
					.registerStoredProcedureParameter("P_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HEDINFO", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_IMPLANTINFO", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_WARDINFO", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILID", txnPackageDetailsId);// 410
			storedProcedureQuery.execute();
			hedInfoResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_HEDINFO");
			if (hedInfoResultSet != null) {
				while (hedInfoResultSet.next()) {
					highEndDrugInfo = new LinkedHashMap<>();
					highEndDrugInfo.put("code",
							hedInfoResultSet.getString(1) != null ? hedInfoResultSet.getString(1) : "NA");
					highEndDrugInfo.put("name",
							hedInfoResultSet.getString(2) != null ? hedInfoResultSet.getString(2) : "NA");
					highEndDrugInfo.put("unitPrice",
							hedInfoResultSet.getString(3) != null ? hedInfoResultSet.getDouble(3) : "NA");
					highEndDrugInfo.put("unit",
							hedInfoResultSet.getString(4) != null ? hedInfoResultSet.getLong(4) : "NA");
					highEndDrugInfo.put("totalPrice",
							hedInfoResultSet.getString(5) != null ? hedInfoResultSet.getDouble(5) : "NA");
					highEndDrugInfo.put("recommendedDose",
							hedInfoResultSet.getString(6) != null ? hedInfoResultSet.getString(6) : "NA");
					highEndDrugInfo.put("preAuthRequired",
							hedInfoResultSet.getString(7) != null ? hedInfoResultSet.getString(7) : "NA");
					highEndDrugInfo.put("activityDoneOn",
							hedInfoResultSet.getDate(8) != null ? simpleDateFormat.format(hedInfoResultSet.getDate(8))
									: "NA");
					highEndDrugInfoList.add(highEndDrugInfo);
				}
				responseMap.put("highEndDrugList", highEndDrugInfoList);
				responseMap.put("highEndDrugTotalPrice",
						highEndDrugInfoList.stream().mapToDouble(map -> (double) map.get("totalPrice")).sum());
			} else
				responseMap.put("highEndDrugList", null);

			implantInfoResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_IMPLANTINFO");
			if (implantInfoResultSet != null) {
				while (implantInfoResultSet.next()) {
					implantInfo = new LinkedHashMap<>();
					implantInfo.put("code",
							implantInfoResultSet.getString(1) != null ? implantInfoResultSet.getString(1) : "NA");
					implantInfo.put("implantName",
							implantInfoResultSet.getString(2) != null ? implantInfoResultSet.getString(2) : "NA");
					implantInfo.put("procedureCode",
							implantInfoResultSet.getString(3) != null ? implantInfoResultSet.getString(3) : "NA");
					implantInfo.put("unitPrice",
							implantInfoResultSet.getString(4) != null ? implantInfoResultSet.getDouble(4) : "NA");
					implantInfo.put("unit",
							implantInfoResultSet.getString(5) != null ? implantInfoResultSet.getLong(5) : "NA");
					implantInfo.put("totalAmount",
							implantInfoResultSet.getString(6) != null ? implantInfoResultSet.getDouble(6) : "NA");
					implantInfo.put("activityOn",
							implantInfoResultSet.getDate(7) != null
									? simpleDateFormat.format(implantInfoResultSet.getDate(7))
									: "NA");
					implantInfo.put("procedureName",
							implantInfoResultSet.getString(8) != null ? implantInfoResultSet.getString(8) : "NA");
					implantInfoList.add(implantInfo);
				}
				responseMap.put("implantDataList", implantInfoList);
				responseMap.put("implantTotalPrice",
						implantInfoList.stream().mapToDouble(map -> (double) map.get("totalAmount")).sum());
				responseMap.put("implantTotalUnitPrice",
						implantInfoList.stream().mapToDouble(map -> (double) map.get("unitPrice")).sum());
				responseMap.put("implantTotalUnit",
						implantInfoList.stream().mapToDouble(map -> (long) map.get("unit")).sum());
			} else
				responseMap.put("implantDataList", null);
			wardInfoResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_WARDINFO");
			if (wardInfoResultSet != null) {
				while (wardInfoResultSet.next()) {
					wardInfo = new LinkedHashMap<>();
					wardInfo.put("wardName",
							wardInfoResultSet.getString(1) != null ? wardInfoResultSet.getString(1) : "NA");
					wardInfo.put("wardAmount",
							wardInfoResultSet.getString(2) != null ? wardInfoResultSet.getDouble(2) : "NA");
					wardInfo.put("wardBlockDate",wardInfoResultSet.getDate(3) != null ? simpleDateFormat1.format(wardInfoResultSet.getDate(3))
									: "NA");
					wardInfo.put("preAuthRequired",
							wardInfoResultSet.getString(4) != null ? wardInfoResultSet.getString(4) : "NA");
					wardInfo.put("activityDoneOn",
							wardInfoResultSet.getString(5) != null ? wardInfoResultSet.getString(5) : "NA");
					wardInfoList.add(wardInfo);
				}
				responseMap.put("wardDataList", wardInfoList);
			} else
				responseMap.put("wardDataList", null);
		} catch (Exception e) {
			logger.error("Exception in getPackageCodeWiseDetailsCMS() of PackageCodeWiseDetailsServiceImpl : ", e);
			// e.printStackTrace();
		}
		return responseMap;
	}

	public JSONArray getPreAuthLogHistory(String urn, String authorizedCode, String hospitalCode) {
		JSONArray jsonArray = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_PREAUTH_LOG_DTLS")
					.registerStoredProcedureParameter("p_urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("authorizedCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_urnNo", urn);
			storedProcedureQuery.setParameter("authorizedCode", authorizedCode);
			storedProcedureQuery.setParameter("hospitalCode", hospitalCode);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("HOSPITALREQUESTDESCRIPTION", rs.getString(1));
				jsonObject.put("REQUESTDATE", rs.getString(2));
				jsonObject.put("REQUESTAMOUNT", rs.getString(3));
				jsonObject.put("DOC1", rs.getString(4));
				jsonObject.put("SNAQUERY1REMARK", rs.getString(5));
				jsonObject.put("SNAQUERY1DESCRIPTION", rs.getString(6));
				jsonObject.put("QUERY1DATE", rs.getString(7));
				jsonObject.put("QUERYREPLAYBYHOS1", rs.getString(8));
				jsonObject.put("QUERYREPLAYBYHOSDATE1", rs.getString(9));
				jsonObject.put("DOC2", rs.getString(10));
				jsonObject.put("SNAQUERY2REMARK", rs.getString(11));
				jsonObject.put("MOREDOCSDESCRIPTION2", rs.getString(12));
				jsonObject.put("QUERY2DATE", rs.getString(13));
				jsonObject.put("QUERYREPLAYBYHOS2", rs.getString(14));
				jsonObject.put("QUERYREPLAYBYHOSDATE2", rs.getString(15));
				jsonObject.put("DOC3", rs.getString(16));
				jsonObject.put("SNAREMARK", rs.getString(17));
				jsonObject.put("SNADESCRIPTION", rs.getString(18));
				jsonObject.put("APPROVEDDATE", rs.getString(19));
				jsonObject.put("APPROVEDAMOUNT", rs.getString(20));
				jsonObject.put("ACTIONBY", rs.getString(21));
				jsonObject.put("sdate", rs.getDate(22));
				jsonObject.put("hospitaluploaddate", rs.getString(23));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Error in getPreAuthHistory method of CPDClaimProcessingServiceImpl", e);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getClaimsOnHoldList of ClaimDetailsDaoImpl :", e2);
			}
		}
		return jsonArray;
	}

	@Transactional
	@Override
	public Map<String, Object> cpdRevertClaimAction(Integer claimID, String action, String userId, String remarks,
			String reasonId, String cpdApprovedAmt, String urn, String mortality, int timingLogId, Date actionTakenTime,
			Long icdFlag, List<ICDDetailsBean> icdFinalData) throws Exception {
		String result = "";
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : icdFinalData) {
				subListData.add(details.getSubList());
				details.setSubList(null);
				icdData.add(details);
			}
			detailsICD = ow.writeValueAsString(icdData);
			subDetailsICD = ow.writeValueAsString(subListData);
		} catch (Exception e) {
			throw e;
		}
		try {
			if (action.equalsIgnoreCase("Query") || action.equalsIgnoreCase("Reject"))
				cpdApprovedAmt = "0";
			if (action.equalsIgnoreCase("Query"))
				mortality = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_wip_cpd_revert_action")
					.registerStoredProcedureParameter("p_claimID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarsId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_approved_amnt", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_claimID", claimID);
			storedProcedureQuery.setParameter("p_action", action);
			storedProcedureQuery.setParameter("p_userId", Integer.parseInt(userId));
			storedProcedureQuery.setParameter("p_remarsId", Integer.parseInt(reasonId));
			storedProcedureQuery.setParameter("p_remarks", remarks.trim());
			storedProcedureQuery.setParameter("p_cpd_approved_amnt", cpdApprovedAmt);
			storedProcedureQuery.setParameter("p_mortality", mortality);
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", icdFlag);
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.execute();
			result = (String) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (Integer.parseInt(result) >= 1 && Integer.parseInt(result) <= 3)
				saveTransactIonLog(claimID, action, userId, remarks, reasonId, cpdApprovedAmt);
			if (action.equalsIgnoreCase("Approve") || action.equalsIgnoreCase("Reject"))
				CPDClaimProcessingServiceImpl.saveCPDActionLog(claimID, action, userId, urn);
			StoredProcedureQuery storedProcedureQuery1 = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_ACTION_TIMING")
					.registerStoredProcedureParameter("P_METHOD", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CASE_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIM_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIM_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CLICK_TIME", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_TAKEN_TIME", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_TYPE_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TIMING_LOG_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery1.setParameter("P_METHOD", 1);
			storedProcedureQuery1.setParameter("P_CPD_USER_ID", Long.parseLong(userId));
			storedProcedureQuery1.setParameter("P_URN", urn);
			storedProcedureQuery1.setParameter("P_CASE_NO", null);
			storedProcedureQuery1.setParameter("P_CLAIM_NO", null);
			storedProcedureQuery1.setParameter("P_CLAIM_ID", null);
			storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", null);
			storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", null);
			storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", actionTakenTime);
			storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID",
					action.equalsIgnoreCase("Approve") ? 1 : action.equalsIgnoreCase("Query") ? 3 : 2);
			storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", timingLogId);
			storedProcedureQuery1.execute();
		} catch (Exception e) {
			logger.error("Exception raised in saveCpdClaimAction method of CPDClaimReapprovalServiceImpl class :", e);
			throw new RuntimeException(e);
		}
		String finalResult = result;
		return new HashMap<String, Object>() {
			{
				put("responseId", finalResult);
				put("response", mstResponseMessageRepository.getReferenceById(Integer.parseInt(finalResult))
						.getResponseMessage());
			}
		};
	}
}
