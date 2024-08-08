/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.bsky.bean.Cpdapprovalbean;
import com.project.bsky.bean.DocumentclickStatus;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageDetails;
import com.project.bsky.repository.MstResponseMessageRepository;
import com.project.bsky.repository.PackageDetailsRepository;
import com.project.bsky.service.CPDClaimProcessingService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.JwtUtil;

/**
 * @author ipsita.shaw
 *
 */
@SuppressWarnings({ "unused", "unchecked", "serial" })
@Service
public class CPDClaimProcessingServiceImpl implements CPDClaimProcessingService {

	private final Logger logger;

	@Autowired
	public CPDClaimProcessingServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PackageDetailsRepository packageDetailsRepository;

	@Autowired
	private MstResponseMessageRepository mstResponseMessageRepository;

	@Autowired
	private JwtUtil util;

	@SuppressWarnings("unchecked")
	@Override
	public String getAllClaimRaised(Integer userId, String orderValue, String fromDate, String toDate, Integer authMode,
			Integer trigger, Integer schemeid, String schemecategoryid) {
		List<Object[]> claimRaiseDetailsList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		Integer schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Integer.parseInt(schemecategoryid);
		} else {
			schemecatId = null;
		}
		try {

			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_CPD_HPTL_CLM_RAISED_LT")
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
				jsonObject.put("invoiceNo", claimRaiseDetails[10]);
				jsonObject.put("allotedDate", claimRaiseDetails[11]);
				if (claimRaiseDetails[12] != null) {
					jsonObject.put("authorizedcode", claimRaiseDetails[12].toString().substring(2));
				}
				jsonObject.put("hospitalcode", claimRaiseDetails[13]);
				jsonObject.put("actualDate", claimRaiseDetails[14]);
				jsonObject.put("claimNo", claimRaiseDetails[15]);
				jsonObject.put("takenDate", claimRaiseDetails[16]);
				jsonObject.put("remainingDate", claimRaiseDetails[17].toString() + " days left");
				jsonObject.put("actualDateOfDischarge", claimRaiseDetails[18]);
				jsonObject.put("caseNo", claimRaiseDetails[19] != null ? claimRaiseDetails[19] : "N/A");
				jsonObject.put("triggerValue", claimRaiseDetails[20]);
				jsonObject.put("triggerMessage", claimRaiseDetails[21]);
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getAllClaimRaised method of CPDClaimProcessingServiceImpl : " + e);
			throw new RuntimeException(e);
		}
		return jsonArray.toString();
	}

	@Override
	public String getClaimDetails(String transaction_id, String urn, String transClaimId, String authorizedCode,
			String hospitalCode, String actualDate, String caseNo, Long userId, Date actionClickTime, String claimNo)
			throws SQLException {
		String title = "";
		JSONArray jsonArray2 = new JSONArray();
		Integer timingLogId = null;
		List<Object[]> claimRaiseDetailsList;
		ResultSet multiPackList = null;
		ResultSet multiPackcasenoList = null;
		ResultSet preAuthLogList = null;
		ResultSet resultSet = null;
		ResultSet vitalParams = null;
		ResultSet meTrigger = null;
		JSONObject jsonObject2 = new JSONObject();
		JSONObject multiObject;
		JSONObject jsonObject;
		JSONObject preAuthObject;
		JSONArray jsonArray = new JSONArray();
		JSONArray preauthArray = new JSONArray();
		JSONArray multiPackArray = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray vitalArray = new JSONArray();
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
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
			JSONArray reasonJsonArray = getReasonMaster();
			jsonObject2.put("reasonList", reasonJsonArray);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_CPD_HPTL_CLM_RISED_DTS")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("authorizedCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("actualDate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimed_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_preauth_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MULTI_PACK_DTLS", void.class, ParameterMode.REF_CURSOR)
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
			multiPackList = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MULTI_PACK_DTLS");
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
					jsonObject.put("MORTALITY", "NA");
				else if (claimRaiseDetails[29].toString().equalsIgnoreCase("N"))
					jsonObject.put("MORTALITY", "NO");
				else
					jsonObject.put("MORTALITY", "YES");
				jsonObject.put("REFERRALCODE", claimRaiseDetails[30] != null ? claimRaiseDetails[30] : "NA");
				jsonObject.put("AUTHORIZEDCODE", claimRaiseDetails[31]);
				jsonObject.put("NABHFlag", claimRaiseDetails[32] != null ? claimRaiseDetails[32] : "NA");
				jsonObject.put("claimCaseNo", claimRaiseDetails[33]);
				jsonObject.put("claimBillNo", claimRaiseDetails[34]);
				jsonObject.put("intraSurgery", claimRaiseDetails[35]);
				jsonObject.put("specimenPhoto", claimRaiseDetails[36]);
				jsonObject.put("patientPhoto", claimRaiseDetails[37]);
				jsonObject.put("claimNo", claimRaiseDetails[38]);
				jsonObject.put("implantData", claimRaiseDetails[39]);
				jsonObject.put("patientPhono", claimRaiseDetails[40]);
				jsonObject.put("packageCatCode", claimRaiseDetails[41]);
				jsonObject.put("verificationMode", claimRaiseDetails[42] != null ? claimRaiseDetails[42] : "NA");
				jsonObject.put("isPatientVerified", claimRaiseDetails[43] != null ? claimRaiseDetails[43] : "NA");
				jsonObject.put("referralStatus", claimRaiseDetails[44] != null ? claimRaiseDetails[44] : "NA");
				jsonObject.put("txnPackageDetailsId", claimRaiseDetails[45] != null ? claimRaiseDetails[45] : null);
				jsonObject.put("packageCode1", claimRaiseDetails[46] != null ? claimRaiseDetails[46] : "NA");
				jsonObject.put("packageName1", claimRaiseDetails[47] != null ? claimRaiseDetails[47] : "NA");
				jsonObject.put("subPackageCode1", claimRaiseDetails[48] != null ? claimRaiseDetails[48] : "NA");
				jsonObject.put("subPackageName1", claimRaiseDetails[49] != null ? claimRaiseDetails[49] : "NA");
				jsonObject.put("procedureCode1", claimRaiseDetails[50] != null ? claimRaiseDetails[50] : "NA");
				jsonObject.put("procedureName1", claimRaiseDetails[51] != null ? claimRaiseDetails[51] : "NA");
				jsonObject.put("packageCost1", claimRaiseDetails[52] != null ? claimRaiseDetails[52] : "--");
				jsonObject.put("blockamount", claimRaiseDetails[53]);
				jsonObject.put("CREATEON", claimRaiseDetails[54]);
				jsonObject.put("MEMBERID", claimRaiseDetails[55]);
				jsonObject.put("ISEMERGENCY", claimRaiseDetails[56]);
				jsonObject.put("OVERRIDECODE", claimRaiseDetails[57]);
				jsonObject.put("TREATMENTDAY", claimRaiseDetails[58]);
				jsonObject.put("DOCTORNAME", claimRaiseDetails[59]);
				jsonObject.put("FROMHOSPITALNAME", claimRaiseDetails[60]);
				jsonObject.put("TOHOSPITAL", claimRaiseDetails[61]);
				jsonObject.put("DISREMARKS", claimRaiseDetails[62]);
				jsonObject.put("TRANSACTIONDESCRIPTION", claimRaiseDetails[63]);
				jsonObject.put("HOSPITALCATEGORYNAME", claimRaiseDetails[64]);
				jsonObject.put("disverification", claimRaiseDetails[65]);
				jsonObject.put("snaFullName", claimRaiseDetails[66] != null ? claimRaiseDetails[66] : "NA");
				jsonObject.put("snaPhone", claimRaiseDetails[67] != null ? claimRaiseDetails[67] : "NA");
				jsonObject.put("snaUserId", claimRaiseDetails[68]);
				jsonObject.put("mortalitydocument", claimRaiseDetails[69]);
				jsonObject.put("schemecategoryname", claimRaiseDetails[70]);
				jsonObject.put("uidreferencenumber", claimRaiseDetails[71]);
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
				if (multiPackList.getString(10) != null)
					multiObject.put("authorizedcode", multiPackList.getString(10).toString().substring(2));
				multiObject.put("claimStatus",
						multiPackList.getString(11).toString().equalsIgnoreCase("0") ? "No" : "YES");
				multiPackArray.put(multiObject);
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
			jsonObject2.put("result", jsonArray);
			jsonObject2.put("preAuthLogList", preauthArray);
			jsonObject2.put("preAuthLog", preAuthLog);
			jsonObject2.put("vitalArray", vitalArray);
			jsonObject2.put("multiPackList", multiPackArray);
			jsonObject2.put("timingLogId", timingLogId);
			jsonObject2.put("meTrigger", jsonArray2);
			jsonObject2.put("ictDetailsArray", ictDetailsArray);
			jsonObject2.put("ictSubDetailsArray", ictSubDetailsArray);

		} catch (Exception e) {
			logger.error("Error in getMemberDetails() method of CPDClaimProcessingServiceImpl", e);

			throw new RuntimeException(e);
		} finally {
			if (multiPackList != null)
				multiPackList.close();
			if (preAuthLogList != null)
				preAuthLogList.close();
			if (resultSet != null)
				resultSet.close();
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
			throws ParseException {
		List<Object[]> approvalDetailsList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_Claim_CPD_PA_ApprovDet")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("v_claim_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_claims_details", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("v_claim_id", transClaimId);
			storedProcedureQuery.execute();
			approvalDetailsList = storedProcedureQuery.getResultList();
			for (Iterator<Object[]> iterator = approvalDetailsList.iterator(); iterator.hasNext();) {
				Object[] approvalDetails = iterator.next();
				jsonObject = new JSONObject();
				jsonObject.put("approvedAmount",
						(approvalDetails[1] != null ? approvalDetails[1] : approvalDetails[0]));
				jsonObject.put("description", approvalDetails[2]);
				jsonObject.put("actionOn", approvalDetails[3]);
				jsonObject.put("actionType", approvalDetails[4].toString());
				if (((BigDecimal) approvalDetails[5]).compareTo(new BigDecimal("5")) == 0) {
					Query query = this.entityManager.createNativeQuery(
							"SELECT HF.HOSPITALNAME FROM HOSPITALPROFILE HF INNER JOIN USERDETAILS UD ON HF.HOSPITALCODE = UD.USERNAME AND UD.GROUPID = 5 AND UD.USERNAME = ?");
					query.setParameter(1, approvalDetails[6]);
					String userName = CommonFileUpload.clobToString((Clob) query.getSingleResult());

					jsonObject.put("actionBy", userName);
				} else {
					jsonObject.put("actionBy", approvalDetails[6]);
				}
				jsonObject.put("remarks", approvalDetails[7]);

				jsonObject.put("discharegeSlip", approvalDetails[8]);
				jsonObject.put("additionalSlip", approvalDetails[9]);
				jsonObject.put("otherSlip", approvalDetails[10]);
				jsonObject.put("otherOneSlip", approvalDetails[11]);
				jsonObject.put("preSurgeryPhoto", approvalDetails[12]);
				jsonObject.put("postSurgeryPhoto", approvalDetails[13]);
				jsonObject.put("dateOfAdmission", DateFormat.FormatToDateString(approvalDetails[14].toString()));
				jsonObject.put("hospitalCode", approvalDetails[15].toString());
				jsonArray.put(jsonObject);
			}

		} catch (JSONException e) {
			logger.error("Error in getApprovalHistory method of CPDClaimProcessingServiceImpl", e);
		}
		return jsonArray;
	}

	public JSONArray getPreAuthHistory(String urn, String authorizedCode, String hospitalCode) throws ParseException {
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
				jsonObject.put("approvedDate1", DateFormat.dateConvertor(String.valueOf(preAuthDetails[4]), ""));
				jsonObject.put("approvedDate2", DateFormat.dateConvertor(String.valueOf(preAuthDetails[4]), "time"));
				jsonObject.put("preAuthFile", preAuthDetails[5]);
				jsonObject.put("hospitalCode", preAuthDetails[6]);
				jsonObject.put("claimedDate", preAuthDetails[7]);
				jsonObject.put("claimedDate1", DateFormat.dateConvertor(String.valueOf(preAuthDetails[7]), ""));
				jsonObject.put("claimedDate2", DateFormat.dateConvertor(String.valueOf(preAuthDetails[7]), "time"));
				jsonObject.put("preauthCode", preAuthDetails[8]);
				jsonObject.put("claimedAmount", preAuthDetails[9]);
				jsonObject.put("aditionalDoc1", preAuthDetails[10]);
				jsonObject.put("aditionalDoc2", preAuthDetails[11]);
				jsonObject.put("aditionalDoc3", preAuthDetails[12]);
				jsonObject.put("moreDesc", preAuthDetails[13]);
				jsonObject.put("moreDescDate", preAuthDetails[14]);
				jsonObject.put("moreDescDate1", DateFormat.dateConvertor(String.valueOf(preAuthDetails[14]), "time"));
				jsonArray.put(jsonObject);
			}

		} catch (JSONException e) {
			logger.error("Error in getPreAuthHistory method of CPDClaimProcessingServiceImpl", e);
		}
		return jsonArray;
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
				jsonObject.put("sdate1", DateFormat.dateConvertor(String.valueOf(rs.getDate(22)), ""));
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

	public JSONArray getMultiplePackageBlocking(String urn, String actualDate) throws ParseException {
		List<Object[]> preAuthLogList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MULTI_PACK_DTS")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("actualDate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_multi_pack_dtls", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("actualDate", actualDate);
			storedProcedureQuery.execute();
			preAuthLogList = storedProcedureQuery.getResultList();
			for (Iterator<Object[]> iterator = preAuthLogList.iterator(); iterator.hasNext();) {
				Object[] preAuthDetails = iterator.next();
				jsonObject = new JSONObject();
				jsonObject.put("dateofAdmission", preAuthDetails[0]);
				jsonObject.put("urn", preAuthDetails[1]);
				jsonObject.put("count", preAuthDetails[2]);
				jsonObject.put("patientName", preAuthDetails[3]);
				jsonObject.put("actualDateof", preAuthDetails[4]);
				jsonObject.put("actualDischarge", String.valueOf(preAuthDetails[5]));
				jsonObject.put("actualDateof1", DateFormat.dateConvertor(String.valueOf(preAuthDetails[4]), ""));
				jsonObject.put("actualDischarge1", DateFormat.dateConvertor(String.valueOf(preAuthDetails[5]), ""));
				jsonObject.put("packageName", preAuthDetails[6]);
				jsonObject.put("transctionId", preAuthDetails[7]);
				jsonObject.put("hospitalcode", preAuthDetails[8]);
				if (preAuthDetails[9] != null)
					jsonObject.put("authorizedcode", preAuthDetails[9].toString().substring(2));
				else
					jsonObject.put("authorizedcode", "");
				jsonObject.put("claimStatus", preAuthDetails[10].toString().equalsIgnoreCase("0") ? "No" : "YES");
				jsonArray.put(jsonObject);
			}

		} catch (JSONException e) {
			logger.error("Error in getMultiplePackageBlocking method of CPDClaimProcessingServiceImpl", e);
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
			logger.error("Error in getReasonMaster method of CPDClaimProcessingServiceImpl", e);
		} finally {
			try {
				if (stateObj != null) {
					stateObj.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getReasonMaster method of CPDClaimProcessingServiceImpl", e2);
			}
		}
		return jsonArray;
	}

	@Transactional
	@Override
	public Map<String, Object> saveCpdClaimAction(Integer claimID, String action, String userId, String remarks,
			String reasonId, String cpdApprovedAmt, String urn, String mortality, int timingLogId, Date actionTakenTime,
			Long icdFlag, List<ICDDetailsBean> icdFinalData) throws Exception {

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
		String result = "";
		try {
			if (action.equalsIgnoreCase("Query") || action.equalsIgnoreCase("Reject"))
				cpdApprovedAmt = "0";
			if (action.equalsIgnoreCase("Query"))
				mortality = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_APPOVAL_ACT")
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
				saveTransactIonLog(claimID, action, userId, remarks, reasonId, cpdApprovedAmt); // Action Log Insertion
			if (action.equalsIgnoreCase("Approve") || action.equalsIgnoreCase("Reject"))
				saveCPDActionLog(claimID, action, userId, urn);
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
			logger.error("Error in saveCpdClaimAction method of of CPDClaimProcessingServiceImpl", e);
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

	void saveCPDActionLog(Integer claimID, String action, String userId, String urn) {
		Integer result = 0;
		int actionType = 0;
		try {
			Query query = this.entityManager
					.createNativeQuery("SELECT PAYMENT FROM USER_RULE_CONFIG WHERE GROUP_ID = 3 AND STATUS_FLAG =0");
			BigDecimal cpdActionAmnt = (BigDecimal) query.getSingleResult();
			Map<String, Integer> claimMap = CommonFileUpload.createClaimStatusMap();
			for (Map.Entry<String, Integer> entry : claimMap.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(action)) {
					actionType = entry.getValue();
				}
			}
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_ACTION_LOG")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("claim_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("action_type", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("action_by", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("action_amount", BigDecimal.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("claim_id", claimID);
			storedProcedureQuery.setParameter("action_type", actionType);
			storedProcedureQuery.setParameter("action_by", Integer.parseInt(userId));
			storedProcedureQuery.setParameter("action_amount", cpdActionAmnt);
			storedProcedureQuery.execute();
		} catch (Exception e) {
			logger.error("Error in saveCPDActionLog method of of CPDClaimProcessingServiceImpl", e);
		}
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
			logger.error("Exception in saveClaimDetails method of CPDClaimProcessingServiceImpl", e);
		}
	}

	@Override
	public String getPreAuthHistoryDetails(String urn, String authorizedCode, String hospitalCode) {
		JSONObject jsonObject2 = new JSONObject();
		String authCode = null;
		if (authorizedCode != null) {
			authCode = authorizedCode.substring(2);
		}
		try {
			JSONArray preAuthLogJsonArray = getPreAuthHistory(urn, authCode, hospitalCode);
			JSONArray preAuthLogHistArray = getPreAuthLogHistory(urn, authCode, hospitalCode);
			jsonObject2.put("preAuthLogList", preAuthLogJsonArray);
			jsonObject2.put("preAuthLog", preAuthLogHistArray);
		} catch (Exception e) {
			logger.error("Exception in getPreAuthHistoryDetails method of of CPDClaimProcessingServiceImpl", e);
			throw new RuntimeException(e);
		}
		return jsonObject2.toString();
	}

	@Override
	public String getMultiPackDtls(String transaction_id, String urn, String authorizedCode, String hospitalCode) {
		List<Object[]> claimRaiseDetailsList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject1 = new JSONObject();
		ResultSet logDetails = null;
		ResultSet claimdetails = null;
		String title = "";
		try {
			JSONArray preAuthLogJsonArray = getPreAuthHistory(urn, authorizedCode, hospitalCode);
			jsonObject2.put("preAuthLogList", preAuthLogJsonArray);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_CPD_MULTI_PACK_DTS")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimed_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_cpd_log_details", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("tranId", Integer.parseInt(transaction_id));
			storedProcedureQuery.setParameter("tranCode", "0303");
			storedProcedureQuery.execute();
			claimRaiseDetailsList = storedProcedureQuery.getResultList();
			claimdetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_claimed_details");
			logDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_cpd_log_details");
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
				jsonObject.put("MORTALITY", claimdetails.getString(30).equalsIgnoreCase("N") ? "No" : "YES");
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

			jsonObject2.put("result", jsonObject);
			jsonObject2.put("approvalList", jsonArray);
		} catch (Exception e) {
			logger.error("Exception in getClaimDetails() method of CPDClaimProcessingServiceImpl" + e);

			throw new RuntimeException(e);
		} finally {
			try {
				if (logDetails != null) {
					logDetails.close();
				}
				if (claimdetails != null) {
					claimdetails.close();
				}
			} catch (Exception e2) {
				logger.error("Exception in getClaimDetails() method" + e2);
			}
		}
		return jsonObject2.toString();
	}

	@Override
	public PackageDetails getPackageDetails(String packageId, String procedureCode) {
		PackageDetails response = null;
		try {
			response = packageDetailsRepository.findByPackageIdAndProcedureCode(packageId, procedureCode);
		} catch (Exception e) {
			logger.error("Exception in getPackageDetails() method of CPDClaimProcessingServiceImpl" + e);
			throw e;
		}
		return response;
	}

	/*
	 * @Author : Sambit Kumar Pradhan
	 * 
	 * @Date : 31/01/2023
	 * 
	 * @Description : This method is used Export the data in PDF format
	 */

	@Override
	public void generatePDF(JSONArray reports, JSONArray header, HttpServletResponse httpServletResponse) {
		String[] columns = new String[header.length()];
		try {
			Document myDoc = new Document(PageSize.A4);
			String fileName = "CPDApprovalClaimList.pdf";
			PdfWriter writer = PdfWriter.getInstance(myDoc, httpServletResponse.getOutputStream());
			httpServletResponse.setContentType("application/pdf");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + "\"" + fileName + "\"");
			myDoc.open();
			Paragraph p = new Paragraph("CPD Approval Claim List", FontFactory.getFont("Arial", 14, Font.BOLD));
			p.setAlignment(Element.ALIGN_CENTER);
			myDoc.add(p);
			myDoc.add(new Paragraph(" "));
			Font headingfont = FontFactory.getFont(String.valueOf(FontFactory.getFont("Arial")), 12);
			Font font = FontFactory.getFont(String.valueOf(FontFactory.getFont("Arial")), 10);
			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(110);
			table.setSpacingBefore(0);
			table.setSpacingAfter(0);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			for (int i = 0; i < header.length(); i++) {
				columns[i] = header.getString(i);
			}
			for (String column : columns) {
				PdfPCell cell = new PdfPCell(new Paragraph(column, headingfont));
				cell.setPaddingLeft(4);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}
			int slNO = 1;
			for (int i = 0; i < reports.length(); i++) {
				JSONObject jsonObject = reports.getJSONObject(i);
				for (String column : columns) {
					table.addCell(new Paragraph(jsonObject.getString(column), font));
				}
				slNO++;
			}
			myDoc.add(table);
			myDoc.close();
		} catch (Exception e) {
			logger.error("Exception in generatePDF() method of CPDClaimProcessingServiceImpl" + e);
			throw new RuntimeException(e);
		}
	}

	/*
	 * @Author : Sambit Kumar Pradhan
	 * 
	 * @Date : 01/02/2023
	 * 
	 * @Description : This method is used to get the CPD Approval List Count
	 */
	@Override
	public Map<String, Object> getCPDApprovalListCount(String userId, String orderValue, String fromDate, String toDate,
			Integer authMode, Integer trigger, Integer schemeid, String schemecategoryid) {
		int freshClaimCount = 0;
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
					.createStoredProcedureQuery("USP_CLM_CPD_APPROVAL_LIST_COUNT")
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
				freshClaimCount = resultSet.getInt("FRESH_CLAIM_COUNT");
			}
			response.put("freshClaimCount", freshClaimCount);
		} catch (Exception e) {
			logger.error("Exception in getCPDApprovalListCount() method of CPDClaimProcessingServiceImpl" + e);
			throw new RuntimeException(e);
		}
		return response;
	}

	@Override
	public List<Map<String, Object>> getOldTreatmentHistoryCPD(String urn) {
		List<Map<String, Object>> response = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		StoredProcedureQuery storedProcedureQuery;
		ResultSet resultSet;
		try {
			storedProcedureQuery = entityManager.createStoredProcedureQuery("USP_CLAIM_GET_OLD_TRTMT_HSTRY_CPD")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_URN", urn);
			storedProcedureQuery.execute();
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (resultSet != null && resultSet.next()) {
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("URN", resultSet.getString(1) != null ? resultSet.getString(1) : "N/A");
				map.put("patientName", resultSet.getString(2) != null ? resultSet.getString(2) : "N/A");
				map.put("dateOfAdmission",
						resultSet.getDate(3) != null ? simpleDateFormat.format(resultSet.getDate(3)) : "N/A");
				map.put("actualDateOfAdmission",
						resultSet.getDate(4) != null ? simpleDateFormat.format(resultSet.getDate(4)) : "N/A");
				map.put("dateOfDischarge",
						resultSet.getDate(5) != null ? simpleDateFormat.format(resultSet.getDate(5)) : "N/A");
				map.put("actualDateOfDischarge",
						resultSet.getDate(6) != null ? simpleDateFormat.format(resultSet.getDate(6)) : "N/A");
				map.put("claimStatus", resultSet.getString(7) != null ? resultSet.getString(7) : "N/A");
				map.put("approvedAmount",
						resultSet.getBigDecimal(8) != null
								? NumberFormat.getNumberInstance(Locale.US).format(resultSet.getBigDecimal(8))
								: "N/A");
				map.put("approvedDate",
						resultSet.getDate(9) != null ? simpleDateFormat.format(resultSet.getDate(9)) : "N/A");
				map.put("SNAApprovedDate",
						resultSet.getDate(10) != null ? simpleDateFormat.format(resultSet.getDate(10)) : "N/A");
				map.put("SNAApprovedAmount",
						resultSet.getBigDecimal(11) != null
								? NumberFormat.getNumberInstance(Locale.US).format(resultSet.getBigDecimal(11))
								: "N/A");
				map.put("remark", resultSet.getString(12) != null ? resultSet.getString(12) : "N/A");
				map.put("SNARemark", resultSet.getString(13) != null ? resultSet.getString(13) : "N/A");
				map.put("tpaFinalStatus", resultSet.getString(14) != null ? resultSet.getString(14) : "N/A");
				map.put("tpaFinalDecisionDate",
						resultSet.getDate(15) != null ? simpleDateFormat.format(resultSet.getDate(15)) : "N/A");
				map.put("invoiceNo", resultSet.getBigDecimal(16) != null ? resultSet.getBigDecimal(16) : "N/A");
				map.put("caseNo", resultSet.getString(17) != null ? resultSet.getString(17) : "N/A");
				response.add(map);
			}
		} catch (Exception e) {
			logger.error("Exception in getOldTreatmentHistoryCPD() method of CPDClaimProcessingServiceImpl" + e);
			throw new RuntimeException(e);
		}
		return response;
	}

	public JSONArray getCpdActionTakenLog(Integer txnId) {
		JSONArray cpdActionLog = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_GET_ACTN_TIME_LOG")
					.registerStoredProcedureParameter("p_txn_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_txn_id", txnId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				JSONObject log = new JSONObject();
				log.put("ACTIONBY", rs.getString(1));
				log.put("CLICKEDON", rs.getTimestamp(2));
				log.put("ACTIONON", rs.getTimestamp(3));
				log.put("CLICKEDON1", DateFormat.dateConvertor(String.valueOf(rs.getTimestamp(2)), "time"));
				log.put("ACTIONON1", DateFormat.dateConvertor(String.valueOf(rs.getTimestamp(3)), "time"));
				log.put("ACTIONTYPE", rs.getString(4));
				log.put("TIMEDIFF", rs.getString(5));
				cpdActionLog.put(log);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return cpdActionLog;
	}

	@Override
	public Response getDocumnetinsert(DocumentclickStatus documnetstatus) {
		Response response = new Response();
		String claimList = null;
		Blob blob = null;
		StringBuffer bufferlist = new StringBuffer();
		if (documnetstatus.getDocumnetname() != null) {
			for (String element : documnetstatus.getDocumnetname()) {
				bufferlist.append(element.toString() + ",");
			}
			claimList = bufferlist.substring(0, bufferlist.length() - 1);
		}
		try {
			if (claimList != null) {
				blob = new SerialBlob(claimList.getBytes());
			}
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_document_view_log_dtls")
					.registerStoredProcedureParameter("p_document_name_LIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_grouptype", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action_page_name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCUMENT_STATUS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_document_name_LIST", claimList);
			storedProcedureQuery.setParameter("p_claimid", documnetstatus.getClaimid());
			storedProcedureQuery.setParameter("p_urn", documnetstatus.getUrnumber());
			storedProcedureQuery.setParameter("p_USERID", documnetstatus.getUserid());
			storedProcedureQuery.setParameter("p_grouptype", documnetstatus.getGroupid());
			storedProcedureQuery.setParameter("p_action_page_name", documnetstatus.getPagenameAction());
			storedProcedureQuery.setParameter("P_DOCUMENT_STATUS", documnetstatus.getDocumnetStatus());
			storedProcedureQuery.execute();
			String returnValue = (String) storedProcedureQuery.getOutputParameterValue("P_MSG");
			if (returnValue.equalsIgnoreCase("SUCESS")) {
				response.setStatus("success");
				response.setMessage("Insert Successfully");
			} else if (returnValue.equalsIgnoreCase("FAIL")) {
				response.setStatus("Failed");
				response.setMessage("Some error happen");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		}
		return response;
	}

	@Override
	public Response saveCpdClaimActionnew(Cpdapprovalbean requestBean) throws Exception {
		Response response = new Response();
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : requestBean.getIcdFinalData()) {
				subListData.add(details.getSubList());
				details.setSubList(null);
				icdData.add(details);
			}
			detailsICD = ow.writeValueAsString(icdData);
			subDetailsICD = ow.writeValueAsString(subListData);
		} catch (Exception e) {
			throw e;
		}
		StoredProcedureQuery storedProcedureQuery = this.entityManager
				.createStoredProcedureQuery("usp_claim_cpd_appoval_act_NEW")
				.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_claimid", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_remarsid", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_cpd_approved_amnt", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_discharegeslip", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_aditionalslip", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_investigationone", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_investigationtwo", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_presurgeryphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_postsurgeryphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_claimamount", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_intrasurgery", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_specimanphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_patientphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_actiontype", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_actionon", Timestamp.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_createdby", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_statusflag", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_createdon", Timestamp.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_userip", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

		storedProcedureQuery.setParameter("p_action", requestBean.getActionRemark());
		storedProcedureQuery.setParameter("p_claimid", requestBean.getClaimId());
		storedProcedureQuery.setParameter("p_userid", requestBean.getUserId());
		storedProcedureQuery.setParameter("p_remarsid", requestBean.getActionRemarksId());
		storedProcedureQuery.setParameter("p_remarks", requestBean.getRemarks());
		storedProcedureQuery.setParameter("p_cpd_approved_amnt", requestBean.getAmount());
		storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
		storedProcedureQuery.setParameter("p_urn", requestBean.getUrnNo());
		storedProcedureQuery.setParameter("P_discharegeslip", requestBean.getDischargeSlip().trim());
		storedProcedureQuery.setParameter("P_aditionalslip", requestBean.getAdditinalSlip().trim());
		storedProcedureQuery.setParameter("P_investigationone", null);
		storedProcedureQuery.setParameter("P_investigationtwo", null);
		storedProcedureQuery.setParameter("P_presurgeryphoto", requestBean.getPreSurgerySlip().trim());
		storedProcedureQuery.setParameter("P_postsurgeryphoto", requestBean.getPostSurgerySlip().trim());
		storedProcedureQuery.setParameter("P_claimamount", requestBean.getClaimAmount());
		storedProcedureQuery.setParameter("P_intrasurgery", requestBean.getIntraSurgery().trim());
		storedProcedureQuery.setParameter("P_specimanphoto", requestBean.getSpecimenPhoto().trim());
		storedProcedureQuery.setParameter("P_patientphoto", requestBean.getPatientPhoto().trim());
		storedProcedureQuery.setParameter("P_actiontype", requestBean.getActionType());
		storedProcedureQuery.setParameter("P_actionon", new Timestamp(System.currentTimeMillis()));
		storedProcedureQuery.setParameter("P_createdby", requestBean.getUserId());
		storedProcedureQuery.setParameter("P_statusflag", 0);
		storedProcedureQuery.setParameter("P_createdon", new Timestamp(System.currentTimeMillis()));
		storedProcedureQuery.setParameter("P_userip", getuseripaddressString);
		storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", requestBean.getIcdFlag());
		storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
		storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
		storedProcedureQuery.execute();
		Integer cpdapprovalnew = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		if (cpdapprovalnew == 1) {
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

				storedProcedureQuery1.setParameter("P_METHOD", 1);
				storedProcedureQuery1.setParameter("P_CPD_USER_ID", requestBean.getUserId());
				storedProcedureQuery1.setParameter("P_URN", requestBean.getUrnNo());
				storedProcedureQuery1.setParameter("P_CASE_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_ID", null);
				storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", null);
				storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", null);
				storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", new Date());
				storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID", Integer.parseInt(requestBean.getActionType()));
				storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", requestBean.getTimingLogId());
				storedProcedureQuery1.execute();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			response.setStatus("Approved");
			response.setMessage("Claim Approved Successfully");
		} else if (cpdapprovalnew == 2) {
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

				storedProcedureQuery1.setParameter("P_METHOD", 1);
				storedProcedureQuery1.setParameter("P_CPD_USER_ID", requestBean.getUserId());
				storedProcedureQuery1.setParameter("P_URN", requestBean.getUrnNo());
				storedProcedureQuery1.setParameter("P_CASE_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_ID", null);
				storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", null);
				storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", null);
				storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", new Date());
				storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID", Integer.parseInt(requestBean.getActionType()));
				storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", requestBean.getTimingLogId());
				storedProcedureQuery1.execute();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			response.setStatus("Reject");
			response.setMessage("Claim Rejected Successfully");
		} else if (cpdapprovalnew == 3) {
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

				storedProcedureQuery1.setParameter("P_METHOD", 1);
				storedProcedureQuery1.setParameter("P_CPD_USER_ID", requestBean.getUserId());
				storedProcedureQuery1.setParameter("P_URN", requestBean.getUrnNo());
				storedProcedureQuery1.setParameter("P_CASE_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_ID", null);
				storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", null);
				storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", null);
				storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", new Date());
				storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID", Integer.parseInt(requestBean.getActionType()));
				storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", requestBean.getTimingLogId());
				storedProcedureQuery1.execute();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			response.setStatus("Query");
			response.setMessage("Claim Queried Successfully");
		} else if (cpdapprovalnew == 4 || cpdapprovalnew == 5) {
			response.setStatus("Failed");
			response.setMessage("Something Went Worng");
		} else if (cpdapprovalnew == 6) {
			response.setStatus("Exist");
			response.setMessage("Action Already Taken on This Claim");
		} else {
			response.setStatus("error");
			response.setMessage("Something Went Worng");
		}
		return response;
	}

	@Override
	public String getCPDDraftCliamDetails(Integer userId, Integer schemeid, String schemecategoryid)
			throws SQLException {
		List<Object[]> claimRaiseDetailsList;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		Integer schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Integer.parseInt(schemecategoryid);
		} else {
			schemecatId = null;
		}
		try {

			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_WIP_DRAFT_LIST")
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
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
				jsonObject.put("invoiceNo", claimRaiseDetails[10]);
				jsonObject.put("allotedDate", claimRaiseDetails[11]);
				if (claimRaiseDetails[12] != null) {
					jsonObject.put("authorizedcode", claimRaiseDetails[12].toString().substring(2));
				}
				jsonObject.put("hospitalcode", claimRaiseDetails[13]);
				jsonObject.put("actualDate", claimRaiseDetails[14]);
				jsonObject.put("claimNo", claimRaiseDetails[15]);
				jsonObject.put("takenDate", claimRaiseDetails[16]);
				jsonObject.put("remainingDate", claimRaiseDetails[17].toString() + " days left");
				jsonObject.put("actualDateOfDischarge", claimRaiseDetails[18]);
				jsonObject.put("caseNo", claimRaiseDetails[19] != null ? claimRaiseDetails[19] : "N/A");
				jsonObject.put("triggerValue", claimRaiseDetails[20]);
				jsonObject.put("triggerMessage", claimRaiseDetails[21]);
				jsonObject.put("claimStatus", claimRaiseDetails[22]);
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getAllClaimRaised method of CPDClaimProcessingServiceImpl : " + e);
			throw new RuntimeException(e);
		}
		return jsonArray.toString();
	}

	@Override
	public String getIndividualDraftClaimDetails(String transaction_id, String urn, String transClaimId,
			String authorizedCode, String hospitalCode, String actualDate, String caseNo, Long userId,
			Date actionClickTime, String claimNo) throws SQLException {
		String title = "";
		JSONArray jsonArray2 = new JSONArray();
		Integer timingLogId = null;
		List<Object[]> claimRaiseDetailsList;
		ResultSet multiPackList = null;
		ResultSet preAuthLogList = null;
		ResultSet resultSet = null;
		ResultSet vitalParams = null;
		ResultSet meTrigger = null;
		JSONObject jsonObject2 = new JSONObject();
		JSONObject multiObject;
		JSONObject jsonObject;
		JSONObject preAuthObject;
		JSONArray jsonArray = new JSONArray();
		JSONArray preauthArray = new JSONArray();
		JSONArray multiPackArray = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray vitalArray = new JSONArray();
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject wipLogDetail = null;
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray wipLogDetailArray = new JSONArray();
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		ResultSet wipLogDetailObject = null;
		Long currentUser = null;
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
			currentUser = util.getCurrentUser();
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_WIP_DRAFT_DETAILS")
					.registerStoredProcedureParameter("urnNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tranCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("authorizedCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("hospitalCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("actualDate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimed_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_preauth_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MULTI_PACK_DTLS", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_wip_details", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("urnNo", urn);
			storedProcedureQuery.setParameter("tranId", Integer.parseInt(transaction_id));
			storedProcedureQuery.setParameter("tranCode", "0303");
			storedProcedureQuery.setParameter("authorizedCode", authorizedCode);
			storedProcedureQuery.setParameter("hospitalCode", hospitalCode);
			storedProcedureQuery.setParameter("actualDate", actualDate);
			storedProcedureQuery.setParameter("p_userid", currentUser);
			storedProcedureQuery.execute();
			claimRaiseDetailsList = storedProcedureQuery.getResultList();
			preAuthLogList = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_preauth_details");
			multiPackList = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MULTI_PACK_DTLS");
			vitalParams = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			meTrigger = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			wipLogDetailObject = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_wip_details");
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
					jsonObject.put("MORTALITY", "NA");
				else if (claimRaiseDetails[29].toString().equalsIgnoreCase("N"))
					jsonObject.put("MORTALITY", "NO");
				else
					jsonObject.put("MORTALITY", "YES");
				jsonObject.put("REFERRALCODE", claimRaiseDetails[30] != null ? claimRaiseDetails[30] : "NA");
				jsonObject.put("AUTHORIZEDCODE", claimRaiseDetails[31]);
				jsonObject.put("NABHFlag", claimRaiseDetails[32] != null ? claimRaiseDetails[32] : "NA");
				jsonObject.put("claimCaseNo", claimRaiseDetails[33]);
				jsonObject.put("claimBillNo", claimRaiseDetails[34]);
				jsonObject.put("intraSurgery", claimRaiseDetails[35]);
				jsonObject.put("specimenPhoto", claimRaiseDetails[36]);
				jsonObject.put("patientPhoto", claimRaiseDetails[37]);
				jsonObject.put("claimNo", claimRaiseDetails[38]);
				jsonObject.put("implantData", claimRaiseDetails[39]);
				jsonObject.put("patientPhono", claimRaiseDetails[40]);
				jsonObject.put("packageCatCode", claimRaiseDetails[41]);
				jsonObject.put("verificationMode", claimRaiseDetails[42] != null ? claimRaiseDetails[42] : "NA");
				jsonObject.put("isPatientVerified", claimRaiseDetails[43] != null ? claimRaiseDetails[43] : "NA");
				jsonObject.put("referralStatus", claimRaiseDetails[44] != null ? claimRaiseDetails[44] : "NA");
				jsonObject.put("txnPackageDetailsId", claimRaiseDetails[45] != null ? claimRaiseDetails[45] : null);
				jsonObject.put("packageCode1", claimRaiseDetails[46] != null ? claimRaiseDetails[46] : "NA");
				jsonObject.put("packageName1", claimRaiseDetails[47] != null ? claimRaiseDetails[47] : "NA");
				jsonObject.put("subPackageCode1", claimRaiseDetails[48] != null ? claimRaiseDetails[48] : "NA");
				jsonObject.put("subPackageName1", claimRaiseDetails[49] != null ? claimRaiseDetails[49] : "NA");
				jsonObject.put("procedureCode1", claimRaiseDetails[50] != null ? claimRaiseDetails[50] : "NA");
				jsonObject.put("procedureName1", claimRaiseDetails[51] != null ? claimRaiseDetails[51] : "NA");
				jsonObject.put("packageCost1", claimRaiseDetails[52] != null ? claimRaiseDetails[52] : "--");
				jsonObject.put("blockamount", claimRaiseDetails[53]);
				jsonObject.put("CREATEON", claimRaiseDetails[54]);
				jsonObject.put("MEMBERID", claimRaiseDetails[55]);
				jsonObject.put("ISEMERGENCY", claimRaiseDetails[56]);
				jsonObject.put("OVERRIDECODE", claimRaiseDetails[57]);
				jsonObject.put("TREATMENTDAY", claimRaiseDetails[58]);
				jsonObject.put("DOCTORNAME", claimRaiseDetails[59]);
				jsonObject.put("FROMHOSPITALNAME", claimRaiseDetails[60]);
				jsonObject.put("TOHOSPITAL", claimRaiseDetails[61]);
				jsonObject.put("DISREMARKS", claimRaiseDetails[62]);
				jsonObject.put("TRANSACTIONDESCRIPTION", claimRaiseDetails[63]);
				jsonObject.put("HOSPITALCATEGORYNAME", claimRaiseDetails[64]);
				jsonObject.put("disverification", claimRaiseDetails[65]);
				jsonObject.put("snaFullName", claimRaiseDetails[66] != null ? claimRaiseDetails[66] : "NA");
				jsonObject.put("snaPhone", claimRaiseDetails[67] != null ? claimRaiseDetails[67] : "NA");
				jsonObject.put("snaUserId", claimRaiseDetails[68]);
				jsonObject.put("mortalitydocument", claimRaiseDetails[69]);
				jsonObject.put("cpdApprovedAmount", claimRaiseDetails[70]);
				jsonObject.put("isCPDQueired", claimRaiseDetails[71]);
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
				if (multiPackList.getString(10) != null)
					multiObject.put("authorizedcode", multiPackList.getString(10).toString().substring(2));
				multiObject.put("claimStatus",
						multiPackList.getString(11).toString().equalsIgnoreCase("0") ? "No" : "YES");
				multiPackArray.put(multiObject);
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
			while (wipLogDetailObject.next()) {
				wipLogDetail = new JSONObject();
				wipLogDetail.put("logId", wipLogDetailObject.getLong(1));
				wipLogDetail.put("actionBy", wipLogDetailObject.getLong(2));
				wipLogDetail.put("actionOn", DateFormat.formatDateWithTime(wipLogDetailObject.getDate(3)));
				wipLogDetail.put("actionType", wipLogDetailObject.getString(4));
				wipLogDetail.put("amount", wipLogDetailObject.getString(5));
				wipLogDetail.put("remark", wipLogDetailObject.getString(6));
				wipLogDetail.put("description", wipLogDetailObject.getString(7));
				wipLogDetailArray.put(wipLogDetail);
			}
			preAuthLog = getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
			jsonObject2.put("result", jsonArray);
			jsonObject2.put("preAuthLogList", preauthArray);
			jsonObject2.put("preAuthLog", preAuthLog);
			jsonObject2.put("vitalArray", vitalArray);
			jsonObject2.put("multiPackList", multiPackArray);
			jsonObject2.put("timingLogId", timingLogId);
			jsonObject2.put("meTrigger", jsonArray2);
			jsonObject2.put("ictDetailsArray", ictDetailsArray);
			jsonObject2.put("ictSubDetailsArray", ictSubDetailsArray);
			jsonObject2.put("wipLogDetailArray", wipLogDetailArray);

		} catch (Exception e) {
			logger.error("Error in getMemberDetails() method of CPDClaimProcessingServiceImpl", e);

			throw new RuntimeException(e);
		} finally {
			if (multiPackList != null)
				multiPackList.close();
			if (preAuthLogList != null)
				preAuthLogList.close();
			if (resultSet != null)
				resultSet.close();
			if (vitalParams != null)
				vitalParams.close();
			if (ictDetails != null)
				ictDetails.close();
			if (ictSubDetails != null)
				ictSubDetails.close();
		}
		return jsonObject2.toString();
	}

	@Override
	public Response saveCpdClaimDraftAction(Cpdapprovalbean requestBean) throws Exception {
		Response response = new Response();
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : requestBean.getIcdFinalData()) {
				subListData.add(details.getSubList());
				details.setSubList(null);
				icdData.add(details);
			}
			detailsICD = ow.writeValueAsString(icdData);
			subDetailsICD = ow.writeValueAsString(subListData);
		} catch (Exception e) {
			throw e;
		}
		StoredProcedureQuery storedProcedureQuery = this.entityManager
				.createStoredProcedureQuery("usp_claim_cpd_draft_action")
				.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_claimid", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_remarsid", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_cpd_approved_amnt", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_discharegeslip", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_aditionalslip", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_investigationone", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_investigationtwo", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_presurgeryphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_postsurgeryphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_claimamount", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_intrasurgery", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_specimanphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_patientphoto", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_actiontype", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_actionon", Timestamp.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_createdby", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_statusflag", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_createdon", Timestamp.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_userip", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

		storedProcedureQuery.setParameter("p_action", requestBean.getActionRemark());
		storedProcedureQuery.setParameter("p_claimid", requestBean.getClaimId());
		storedProcedureQuery.setParameter("p_userid", requestBean.getUserId());
		storedProcedureQuery.setParameter("p_remarsid", requestBean.getActionRemarksId());
		storedProcedureQuery.setParameter("p_remarks", requestBean.getRemarks());
		storedProcedureQuery.setParameter("p_cpd_approved_amnt", requestBean.getAmount());
		storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
		storedProcedureQuery.setParameter("p_urn", requestBean.getUrnNo());
		storedProcedureQuery.setParameter("P_discharegeslip", requestBean.getDischargeSlip().trim());
		storedProcedureQuery.setParameter("P_aditionalslip", requestBean.getAdditinalSlip().trim());
		storedProcedureQuery.setParameter("P_investigationone", null);
		storedProcedureQuery.setParameter("P_investigationtwo", null);
		storedProcedureQuery.setParameter("P_presurgeryphoto", requestBean.getPreSurgerySlip().trim());
		storedProcedureQuery.setParameter("P_postsurgeryphoto", requestBean.getPostSurgerySlip().trim());
		storedProcedureQuery.setParameter("P_claimamount", requestBean.getClaimAmount());
		storedProcedureQuery.setParameter("P_intrasurgery", requestBean.getIntraSurgery().trim());
		storedProcedureQuery.setParameter("P_specimanphoto", requestBean.getSpecimenPhoto().trim());
		storedProcedureQuery.setParameter("P_patientphoto", requestBean.getPatientPhoto().trim());
		storedProcedureQuery.setParameter("P_actiontype", requestBean.getActionType());
		storedProcedureQuery.setParameter("P_actionon", new Timestamp(System.currentTimeMillis()));
		storedProcedureQuery.setParameter("P_createdby", requestBean.getUserId());
		storedProcedureQuery.setParameter("P_statusflag", 0);
		storedProcedureQuery.setParameter("P_createdon", new Timestamp(System.currentTimeMillis()));
		storedProcedureQuery.setParameter("P_userip", getuseripaddressString);
		storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", requestBean.getIcdFlag());
		storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
		storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
		storedProcedureQuery.execute();
		Integer cpdapprovalnew = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		if (cpdapprovalnew == 1) {
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

				storedProcedureQuery1.setParameter("P_METHOD", 1);
				storedProcedureQuery1.setParameter("P_CPD_USER_ID", requestBean.getUserId());
				storedProcedureQuery1.setParameter("P_URN", requestBean.getUrnNo());
				storedProcedureQuery1.setParameter("P_CASE_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_ID", null);
				storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", null);
				storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", null);
				storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", new Date());
				storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID", Integer.parseInt(requestBean.getActionType()));
				storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", requestBean.getTimingLogId());
				storedProcedureQuery1.execute();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			response.setStatus("Approved");
			response.setMessage("Claim Approved Successfully");
		} else if (cpdapprovalnew == 2) {
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

				storedProcedureQuery1.setParameter("P_METHOD", 1);
				storedProcedureQuery1.setParameter("P_CPD_USER_ID", requestBean.getUserId());
				storedProcedureQuery1.setParameter("P_URN", requestBean.getUrnNo());
				storedProcedureQuery1.setParameter("P_CASE_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_ID", null);
				storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", null);
				storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", null);
				storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", new Date());
				storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID", Integer.parseInt(requestBean.getActionType()));
				storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", requestBean.getTimingLogId());
				storedProcedureQuery1.execute();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			response.setStatus("Reject");
			response.setMessage("Claim Rejected Successfully");
		} else if (cpdapprovalnew == 3) {
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

				storedProcedureQuery1.setParameter("P_METHOD", 1);
				storedProcedureQuery1.setParameter("P_CPD_USER_ID", requestBean.getUserId());
				storedProcedureQuery1.setParameter("P_URN", requestBean.getUrnNo());
				storedProcedureQuery1.setParameter("P_CASE_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_NO", null);
				storedProcedureQuery1.setParameter("P_CLAIM_ID", null);
				storedProcedureQuery1.setParameter("P_TRANSACTIONDETAILSID", null);
				storedProcedureQuery1.setParameter("P_ACTION_CLICK_TIME", null);
				storedProcedureQuery1.setParameter("P_ACTION_TAKEN_TIME", new Date());
				storedProcedureQuery1.setParameter("P_ACTION_TYPE_ID", Integer.parseInt(requestBean.getActionType()));
				storedProcedureQuery1.setParameter("P_TIMING_LOG_ID", requestBean.getTimingLogId());
				storedProcedureQuery1.execute();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			response.setStatus("Query");
			response.setMessage("Claim Queried Successfully");
		} else if (cpdapprovalnew == 4 || cpdapprovalnew == 5) {
			response.setStatus("Failed");
			response.setMessage("Something Went Worng");
		} else {
			response.setStatus("error");
			response.setMessage("Something Went Worng");
		}
		return response;
	}
}
