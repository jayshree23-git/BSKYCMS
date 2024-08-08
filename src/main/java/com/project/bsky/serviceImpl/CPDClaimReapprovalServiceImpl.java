/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.project.bsky.service.CPDClaimReapprovalService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;

/**
 * @author Debendra Nayak
 *
 */
@SuppressWarnings({ "unused", "unchecked", "serial" })
@Service
public class CPDClaimReapprovalServiceImpl implements CPDClaimReapprovalService {

	private final Logger logger;

	@Autowired
	public CPDClaimReapprovalServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	CPDClaimProcessingServiceImpl CPDClaimProcessingServiceImpl;

	@Autowired
	private MstResponseMessageRepository mstResponseMessageRepository;

	@Override
	public String getAllClaimRaised(Integer userId, String orderValue, String fromDate, String toDate, Integer authMode,
			Integer trigger, Integer schemeid, String schemecategoryid) {
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
					.createStoredProcedureQuery("USP_CLAIM_CPD_REAPPROVAL_LIST")
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
			storedProcedureQuery.setParameter("P_AUTH_MODE", authMode != null ? authMode : 0);
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
					jsonObject.put("authorizedcode", claimRaiseDetails[13].toString().substring(2));
				}
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
			logger.error("Exception in getAllClaimRaised method of CPDClaimReapprovalServiceImpl class" + e);
			throw new RuntimeException(e);
		}
		return jsonArray.toString();
	}

	@Override
	public String getClaimDetails(String transaction_id, String urn, String transClaimId, String authorizedCode,
			String hospitalCode, String actualDate, String caseNo, Long userId, Date actionClickTime, String claimNo)
			throws SQLException {
		List<Object[]> claimRaiseDetailsList;
		Integer timingLogId = null;
		JSONArray jsonArray = new JSONArray();
		JSONArray logDetailsArray = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();
		JSONObject multiObject = new JSONObject();
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject jsonObject;
		String title = "";
		JSONArray preauthArray = new JSONArray();
		JSONObject preAuthObject = new JSONObject();
		JSONArray multiPackArray = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray vitalArray = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		ResultSet logDetails = null;
		ResultSet multiPackList = null;
		ResultSet preAuthLogList = null;
		ResultSet resultSet = null;
		ResultSet vitalParams = null;
		ResultSet meTrigger = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		JSONObject jsonObject1 = new JSONObject();
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

			JSONArray reasonJsonArray = getReasonMaster();
			jsonObject2.put("reasonList", reasonJsonArray);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_REAPPROVAL_DTLS")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("authorizedCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("actualDate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimed_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_preauth_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_multi_pack_dtls", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_cpd_log_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("tranId", Integer.parseInt(transaction_id));
			storedProcedureQuery.setParameter("tranCode", "0303");
			storedProcedureQuery.setParameter("authorizedCode", authorizedCode);
			storedProcedureQuery.setParameter("hospitalCode", hospitalCode);
			storedProcedureQuery.setParameter("actualDate", actualDate);
			storedProcedureQuery.execute();
			claimRaiseDetailsList = storedProcedureQuery.getResultList();
			preAuthLogList = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_preauth_details");
			multiPackList = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_multi_pack_dtls");
			logDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_cpd_log_details");
			vitalParams = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			meTrigger = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			for (Iterator<Object[]> iterator = claimRaiseDetailsList.iterator(); iterator.hasNext();) {
				Object[] claimRaiseDetails = iterator.next();
				jsonObject = new JSONObject();
				jsonObject.put("hospitalName", claimRaiseDetails[0]);
				jsonObject.put("hospitalCode", claimRaiseDetails[1]);
				jsonObject.put("invoiceNo", claimRaiseDetails[2]);
				if (Character.compare((char) claimRaiseDetails[3], 'F') == 0)
					title = "Mrs.";
				else
					title = "Mr";

				jsonObject.put("gender", claimRaiseDetails[3]);
				jsonObject.put("patientName", title + " " + claimRaiseDetails[4]);

				if (claimRaiseDetails[5] != null && claimRaiseDetails[6] != null) {
					jsonObject.put("dateOfAdmission", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimRaiseDetails[5])));
					jsonObject.put("dateOfDischarge", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimRaiseDetails[6])));
				} else if (claimRaiseDetails[5] != null && claimRaiseDetails[6] == null) {
					jsonObject.put("dateOfAdmission", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimRaiseDetails[5])));
					jsonObject.put("dateOfDischarge", "");
				} else if (claimRaiseDetails[5] == null && claimRaiseDetails[6] != null) {
					jsonObject.put("dateOfAdmission", "");
					jsonObject.put("dateOfDischarge", new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimRaiseDetails[6])));
				}
				jsonObject.put("noOfDays", CommonFileUpload.calculateNoOfDays((String) claimRaiseDetails[5],
						(String) claimRaiseDetails[6]));
				jsonObject.put("procedureName", claimRaiseDetails[7]);
				jsonObject.put("packageName", claimRaiseDetails[8]);
				jsonObject.put("patientAddress", claimRaiseDetails[9]);
				jsonObject.put("hospitalClaimedAmount", claimRaiseDetails[10]);
				jsonObject.put("packageCost", claimRaiseDetails[11]);
				jsonObject.put("age", claimRaiseDetails[12]);
				jsonObject.put("hospitalAddress", claimRaiseDetails[13]);
				jsonObject.put("admissionSlip", claimRaiseDetails[14]);
				jsonObject.put("additinalSlip", claimRaiseDetails[16]);
				jsonObject.put("dischargeSlip", claimRaiseDetails[17]);
				jsonObject.put("preSurgerySlip", claimRaiseDetails[18]);
				jsonObject.put("postSurgerySlip", claimRaiseDetails[19]);
				jsonObject.put("packageCode", claimRaiseDetails[20]);
				jsonObject.put("ACTUALDATEOFADMISSION",
						DateFormat.FormatToDateString(claimRaiseDetails[21].toString()));
				jsonObject.put("ACTUALDATEOFDISCHARGE",
						DateFormat.FormatToDateString(claimRaiseDetails[22].toString()));
				jsonObject.put("STATENAME", claimRaiseDetails[23]);
				jsonObject.put("DISTRICTNAME", claimRaiseDetails[24]);
				jsonObject.put("BLOCKNAME", claimRaiseDetails[25]);
				jsonObject.put("VILLAGENAME", claimRaiseDetails[26]);
				jsonObject.put("FAMILYHEADNAME", claimRaiseDetails[27]);
				jsonObject.put("VERIFIERNAME", claimRaiseDetails[28]);
				if (claimRaiseDetails[29] == null)
					jsonObject.put("MORTALITY", "N/A");
				else if (claimRaiseDetails[29].toString().equalsIgnoreCase("N"))
					jsonObject.put("MORTALITY", "NO");
				else
					jsonObject.put("MORTALITY", "YES");
				jsonObject.put("REFERRALCODE", claimRaiseDetails[30]);
				jsonObject.put("AUTHORIZEDCODE", claimRaiseDetails[31]);
				jsonObject.put("NABHFlag", claimRaiseDetails[32]);
				jsonObject.put("otherDocOne", claimRaiseDetails[33]);
				jsonObject.put("otherDocTwo", claimRaiseDetails[34]);
				jsonObject.put("claimCaseNo", claimRaiseDetails[35]);
				jsonObject.put("claimBillNo", claimRaiseDetails[36]);
				jsonObject.put("intraSurgery", claimRaiseDetails[37]);
				jsonObject.put("specimenPhoto", claimRaiseDetails[38]);
				jsonObject.put("patientPhoto", claimRaiseDetails[39]);
				jsonObject.put("claimNo", claimRaiseDetails[40]);
				jsonObject.put("implantData", claimRaiseDetails[41]);
				jsonObject.put("patientPhono", claimRaiseDetails[42]);
				jsonObject.put("packageCatCode", claimRaiseDetails[43]);
				jsonObject.put("verificationMode", claimRaiseDetails[44] != null ? claimRaiseDetails[44] : "N/A");
				jsonObject.put("isPatientVerified", claimRaiseDetails[45] != null ? claimRaiseDetails[45] : "N/A");
				jsonObject.put("referralStatus", claimRaiseDetails[46] != null ? claimRaiseDetails[46] : "N/A");
				jsonObject.put("txnPackageDetailsId", claimRaiseDetails[47] != null ? claimRaiseDetails[47] : null);
				jsonObject.put("packageCode1", claimRaiseDetails[48] != null ? claimRaiseDetails[48] : "N/A");
				jsonObject.put("packageName1", claimRaiseDetails[49] != null ? claimRaiseDetails[49] : "N/A");
				jsonObject.put("subPackageCode1", claimRaiseDetails[50] != null ? claimRaiseDetails[50] : "N/A");
				jsonObject.put("subPackageName1", claimRaiseDetails[51] != null ? claimRaiseDetails[51] : "N/A");
				jsonObject.put("procedureCode1", claimRaiseDetails[52] != null ? claimRaiseDetails[52] : "N/A");
				jsonObject.put("procedureName1", claimRaiseDetails[53] != null ? claimRaiseDetails[53] : "N/A");
				jsonObject.put("packageCost1", claimRaiseDetails[54] != null ? claimRaiseDetails[54] : "N/A");
				jsonObject.put("blockamount", claimRaiseDetails[55]);
				jsonObject.put("CREATEON", claimRaiseDetails[56]);
				jsonObject.put("MEMBERID", claimRaiseDetails[57]);
				jsonObject.put("ISEMERGENCY", claimRaiseDetails[58]);
				jsonObject.put("OVERRIDECODE", claimRaiseDetails[59]);
				jsonObject.put("TREATMENTDAY", claimRaiseDetails[60]);
				jsonObject.put("DOCTORNAME", claimRaiseDetails[61]);
				jsonObject.put("FROMHOSPITALNAME", claimRaiseDetails[62]);
				jsonObject.put("TOHOSPITAL", claimRaiseDetails[63]);
				jsonObject.put("DISREMARKS", claimRaiseDetails[64]);
				jsonObject.put("TRANSACTIONDESCRIPTION", claimRaiseDetails[65]);
				jsonObject.put("HOSPITALCATEGORYNAME", claimRaiseDetails[66]);
				jsonObject.put("disverification", claimRaiseDetails[67]);
				jsonObject.put("snaFullName", claimRaiseDetails[68] != null ? claimRaiseDetails[68] : "NA");
				jsonObject.put("snaPhone", claimRaiseDetails[69] != null ? claimRaiseDetails[69] : "NA");
				jsonObject.put("snaUserId", claimRaiseDetails[70]);
				jsonObject.put("mortalitydocument", claimRaiseDetails[71]);
				jsonObject.put("categoryName", claimRaiseDetails[72]);
				jsonArray.put(jsonObject);
			}

			while (preAuthLogList.next()) {
				preAuthObject = new JSONObject();
				preAuthObject.put("urnNo", preAuthLogList.getString(1));
				preAuthObject.put("memberName", preAuthLogList.getString(2));
				preAuthObject.put("preAuthRemarks", preAuthLogList.getString(3));
				preAuthObject.put("approvedAmount", preAuthLogList.getString(4));
				preAuthObject.put("approvedDate", preAuthLogList.getString(5));
				preAuthObject.put("preAuthFile", preAuthLogList.getString(6));
				preAuthObject.put("hospitalCode", preAuthLogList.getString(7));
				preAuthObject.put("claimedDate", preAuthLogList.getString(8));
				preAuthObject.put("preauthCode", preAuthLogList.getString(9));
				preAuthObject.put("claimedAmount", preAuthLogList.getString(10));
				preAuthObject.put("aditionalDoc1", preAuthLogList.getString(11));
				preAuthObject.put("aditionalDoc2", preAuthLogList.getString(12));
				preAuthObject.put("aditionalDoc3", preAuthLogList.getString(13));
				preAuthObject.put("moreDesc", preAuthLogList.getString(14));
				preAuthObject.put("moreDescDate", preAuthLogList.getString(15));
				preauthArray.put(preAuthObject);
			}
			while (multiPackList.next()) {
				multiObject = new JSONObject();
				multiObject.put("dateofAdmission", multiPackList.getString(1));
				multiObject.put("urn", multiPackList.getString(2));
				multiObject.put("count", multiPackList.getString(3));
				multiObject.put("patientName", multiPackList.getString(4));
				multiObject.put("actualDateof", DateFormat.FormatToDateString((String) multiPackList.getString(5)));
				multiObject.put("actualDischarge", DateFormat.FormatToDateString((String) multiPackList.getString(6)));
				multiObject.put("packageName", multiPackList.getString(7));
				multiObject.put("transctionId", multiPackList.getString(8));
				multiObject.put("hospitalcode", multiPackList.getString(9));
				if (multiPackList.getString(10) != null) {
					multiObject.put("authorizedcode", multiPackList.getString(10).toString().substring(2));
				}
				multiObject.put("claimStatus",
						multiPackList.getString(11).toString().equalsIgnoreCase("0") ? "No" : "YES");
				multiPackArray.put(multiObject);
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
				logDetailsArray.put(jsonObject1);
			}
			while (vitalParams.next()) {
				JSONObject j = new JSONObject();
				j.put("ADM_VITALSIGN", vitalParams.getString(1));
				j.put("ADM_VITALVALUE", vitalParams.getString(2));
				j.put("DIS_VITALSIGN", vitalParams.getString(3));
				j.put("DIS_VITALVALUE", vitalParams.getString(4));
				vitalArray.put(j);
			}

			while (meTrigger.next()) {
				JSONObject jsonObject3 = new JSONObject();
				jsonObject3.put("urn", meTrigger.getString(1));
				jsonObject3.put("claimNo", meTrigger.getString(2));
				jsonObject3.put("caseNo", meTrigger.getString(3));
				jsonObject3.put("patientName", meTrigger.getString(4));
				jsonObject3.put("phoneNo", meTrigger.getString(5));
				jsonObject3.put("hospitalName", meTrigger.getString(6));
				jsonObject3.put("hospitalCode", meTrigger.getString(7));
				jsonObject3.put("packageCode", meTrigger.getString(8));
				jsonObject3.put("packageName", meTrigger.getString(9));
				jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(meTrigger.getDate(10)));
				jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(meTrigger.getDate(11)));
				jsonObject3.put("hospitalClaimAmount", meTrigger.getLong(12));
				jsonObject3.put("reportName", meTrigger.getString(13));
				jsonObject3.put("claimId", meTrigger.getLong(14));
				jsonObject3.put("transactionId", meTrigger.getLong(15));
				jsonObject3.put("txnPackageId", meTrigger.getLong(16));
				jsonObject3.put("slNo", meTrigger.getLong(17));
				jsonObject3.put("createdOn", meTrigger.getDate(18));
				jsonObject3.put("statusFlag", meTrigger.getString(19));
				jsonObject3.put("doctorRegNo", meTrigger.getString(20));
				jsonObject3.put("surgeryDate", meTrigger.getDate(21));
				jsonArray2.put(jsonObject3);
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
			preAuthLog = CPDClaimProcessingServiceImpl.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
			jsonObject2.put("result", jsonArray);
			jsonObject2.put("preAuthLogList", preauthArray);
			jsonObject2.put("preAuthLog", preAuthLog);
			jsonObject2.put("multiPackList", multiPackArray);
			jsonObject2.put("approvalList", logDetailsArray);
			jsonObject2.put("timingLogId", timingLogId);
			jsonObject2.put("vitalArray", vitalArray);
			jsonObject2.put("meTrigger", jsonArray2);
			jsonObject2.put("ictDetailsArray", ictDetailsArray);
			jsonObject2.put("ictSubDetailsArray", ictSubDetailsArray);

		} catch (Exception e) {
			logger.error("Exception raised in getClaimDetails method of CPDClaimReapprovalServiceImpl class :", e);
			throw new RuntimeException(e);
		} finally {
			if (multiPackList != null)
				multiPackList.close();
			if (logDetails != null)
				logDetails.close();
			if (preAuthLogList != null)
				preAuthLogList.close();
			if (vitalParams != null)
				vitalParams.close();
			if (ictDetails != null)
				ictDetails.close();
			if (ictSubDetails != null)
				ictSubDetails.close();
		}
		return jsonObject2.toString();
	}

	private JSONArray getApprovalHistory(String transaction_id, String urn, Integer transClaimId)
			throws ParseException, SQLException {
		List<Object[]> approvalDetailsList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject1;
		ResultSet snoDetailsObj1 = null;
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
			logger.error("Exception raised in getApprovalHistory method of CPDClaimReapprovalServiceImpl class :", e);
		} finally {
			try {
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error("Exception raised in getApprovalHistory method of CPDClaimReapprovalServiceImpl class :",
						e2);
			}
		}
		return jsonArray;
	}

	@SuppressWarnings("unused")
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
			logger.error("Exception raised in getPreAuthHistory method of CPDClaimReapprovalServiceImpl class :", e);
		}
		return jsonArray;
	}

	private JSONArray getReasonMaster() throws ParseException, SQLException {
		List<Object[]> reasonMasterList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet stateObj = null;
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
			logger.error("Exception raised in getReasonMaster method of CPDClaimReapprovalServiceImpl class :", e);
		} finally {
			try {
				if (stateObj != null) {
					stateObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception raised in getReasonMaster method of CPDClaimReapprovalServiceImpl class :", e2);
			}
		}
		return jsonArray;
	}

	@Transactional
	@Override
	public Map<String, Object> saveCpdClaimAction(Integer claimID, String action, String userId, String remarks,
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
					.createStoredProcedureQuery("usp_claim_wip_cpd_appoval_act")
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
				claimdetails.put("mortality", claimRaiseDetails[11]);
			}

			Map<String, Integer> claimMap = CommonFileUpload.createClaimStatusMap();
			for (Map.Entry<String, Integer> entry : claimMap.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(action)) {
					actionType = entry.getValue();
				}
			}

			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_wip_log_insert")
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
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
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

				if (entry.getKey().equalsIgnoreCase("mortality"))
					storedProcedureQuery.setParameter("p_mortality", (String) entry.getValue());
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
			logger.error("Exception raised in saveTransactIonLog method of CPDClaimReapprovalServiceImpl class", e);
		}
	}

	@Override
	public Map<String, Object> getCPDReApprovalClaimListCount(String userId, String orderValue, String fromDate,
			String toDate, Integer authMode, Integer trigger, Integer schemeid, String schemecategoryid) {
		int reClaimCount = 0;
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
					.createStoredProcedureQuery("USP_CLAIM_CPD_REAPPROVAL_LIST_COUNT")
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
				reClaimCount = resultSet.getInt("RE_CLAIM_COUNT");
			}
			response.put("reClaimCount", reClaimCount);
		} catch (Exception e) {
			logger.error(
					"Exception raised in getCPDReApprovalClaimListCount method of CPDClaimReapprovalServiceImpl class",
					e);
			throw new RuntimeException(e);
		}
		return response;
	}

}
