package com.project.bsky.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.ClaimRaiseBean;
import com.project.bsky.bean.OldClaimListBean;
import com.project.bsky.bean.OldFloatBean;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.bean.Response;
import com.project.bsky.service.OldClaimProcessingService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.DaysBetweenDates;
import com.project.bsky.util.DocUtill;

import ch.qos.logback.classic.Logger;

@SuppressWarnings("unused")
@Service
public class OldClaimProcessingServiceImpl implements OldClaimProcessingService {

	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	private static ResourceBundle bskyResourcesBundel1 = ResourceBundle.getBundle("fileConfiguration");

	@Value("${file.path.Additionaldoc1}")
	private String Additionaldoc1;

	@Value("${file.path.Additionaldoc2}")
	private String Additionaldoc2;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;

	@Override
	public List<Object> getOldClaimQueryBySNAList(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<>();
		ResultSet rsResultSet = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_HPTL_OLD_CLAIM_LIST")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_status", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode().trim());
			storedProcedureQuery.setParameter("p_claim_status", requestBean.getClaimStatus());
			storedProcedureQuery.execute();
			rsResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");
			while (rsResultSet.next()) {
				ClaimRaiseBean resBean = new ClaimRaiseBean();
				resBean.setClaimRaiseby(rsResultSet.getString(1));
				resBean.setID(rsResultSet.getLong(2));
				resBean.setTransactionid(rsResultSet.getLong(3));
				resBean.setURN(rsResultSet.getString(4).trim());
				resBean.setDateofadmission(rsResultSet.getString(5) != null && !"".equals(rsResultSet.getString(5))
						? new SimpleDateFormat("dd-MMM-yyyy")
								.format(new SimpleDateFormat("ddMMyyyy").parse(rsResultSet.getString(5)))
						: rsResultSet.getString(5));
				resBean.setPatientName(rsResultSet.getString(6));
				resBean.setPackageCode(rsResultSet.getString(7));
				resBean.setPackageName(rsResultSet.getString(8));
				resBean.setProcedurename(rsResultSet.getString(9));
				resBean.setCurrentTotalAmount(
						String.valueOf(new DecimalFormat("#,###,###,###.00").format(rsResultSet.getDouble(10))));
				resBean.setDateOfDischarge(rsResultSet.getString(11) != null && !"".equals(rsResultSet.getString(11))
						? new SimpleDateFormat("dd-MMM-yyyy")
								.format(new SimpleDateFormat("ddMMyyyy").parse(rsResultSet.getString(11)))
						: rsResultSet.getString(11));
				resBean.setUserid(rsResultSet.getInt(12));
				resBean.setHospitalstateCode(rsResultSet.getInt(13));
				resBean.setTransactiondetailsid(rsResultSet.getString(14));
				resBean.setRemainingDateString(
						String.valueOf(DaysBetweenDates.daysCountBetweenDates(rsResultSet.getDate(1))) + "days left");
				resBean.setHospitalcode(rsResultSet.getString(15));
				resBean.setInvoiceno(rsResultSet.getString(16));
				resBean.setAuthorizedcode(rsResultSet.getString(17));
				resBean.setActualdateofdischarge(
						rsResultSet.getString(18) != null && !"".equals(rsResultSet.getString(18))
								? new SimpleDateFormat("dd-MMM-yyyy")
										.format(new SimpleDateFormat("ddMMyyyy").parse(rsResultSet.getString(18)))
								: rsResultSet.getString(18));
				resBean.setActualdateofadmission(
						rsResultSet.getString(19) != null && !"".equals(rsResultSet.getString(19))
								? new SimpleDateFormat("dd-MMM-yyyy")
										.format(new SimpleDateFormat("ddMMyyyy").parse(rsResultSet.getString(19)))
								: rsResultSet.getString(19));
				resBean.setCaseno(rsResultSet.getString(20));
				resBean.setOldCLaimStatus(rsResultSet.getString(21));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rsResultSet != null) {
					rsResultSet.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public String getOldQueriedClaimDetails(String claimID) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		ResultSet claimList = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_HPTL_OLD_CLAIM_DTLS")
					.registerStoredProcedureParameter("p_claimID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_claimID", claimID);
			storedProcedureQuery.execute();
			claimList = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			if (claimList.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("URN", claimList.getString(1).trim());
				jsonObject.put("INVOICENO", claimList.getString(2));
				jsonObject.put("PACKAGEID", claimList.getString(3));
				jsonObject.put("UNBLOCKAMOUNT", claimList.getString(4));
				jsonObject.put("TOTALAMOUNTBLOCKED", claimList.getString(5));
				jsonObject.put("TOTALAMOUNTCLAIMED", claimList.getString(6));
				jsonObject.put("PACKAGECODE", claimList.getString(7));
				jsonObject.put("PACKAGENAME", claimList.getString(8));
				jsonObject.put("PROCEDURENAME", claimList.getString(9));
				jsonObject.put("PACKAGECATEGORYCODE", claimList.getString(10));
				jsonObject.put("HOSPITALNAME", claimList.getString(11));
				jsonObject.put("CURRENTTOTALAMOUNT", claimList.getString(12));
				jsonObject.put("GENDER", claimList.getString(13));
				jsonObject.put("AGE", claimList.getString(14));
				jsonObject.put("PATIENTNAME", claimList.getString(15));
				jsonObject.put("CLAIMID", claimList.getString(16));
				jsonObject.put("BLOCKNAME", claimList.getString(17));
				jsonObject.put("DISTRICTNAME", claimList.getString(18));
				jsonObject.put("STATENAME", claimList.getString(19));
				jsonObject.put("dateofadmission", DateFormat.FormatToDateString(claimList.getString(20)));
				jsonObject.put("DateOfDischarge", DateFormat.FormatToDateString(claimList.getString(21)));
				jsonObject.put("hospitalcode", claimList.getString(22));
				jsonObject.put("transactiondetailsid", claimList.getString(23));
				jsonObject.put("patientphoneno", claimList.getString(24));
				jsonObject.put("actualdateofadmission", DateFormat.FormatToDateString(claimList.getString(25)));
				jsonObject.put("actualdateofdischarge", DateFormat.FormatToDateString(claimList.getString(26)));
				jsonObject.put("NOOFDAYSADDMITTEDHOSPITAL",
						CommonFileUpload.totalDaysBetweenDates(claimList.getString(20), claimList.getString(21)));
				jsonObject.put("year", claimList.getString(27));
				jsonObject.put("HOSPITALCODE1", claimList.getString(28));
				jsonObject.put("FILENAMES", claimList.getString(29));
				jsonObject.put("FILENAMES1", claimList.getString(30));
				jsonObject.put("FILENAMES2", claimList.getString(31));
				jsonObject.put("CLAIMSTATUS", claimList.getString(32));
				jsonObject.put("APPROVEDAMOUNT", claimList.getString(33));
				jsonObject.put("APPROVEDDATE", claimList.getString(34));
				jsonObject.put("PAIDBY", claimList.getString(35));
				jsonObject.put("CHECK_DDNO", claimList.getString(36));
				jsonObject.put("BANKNAME", claimList.getString(37));
				jsonObject.put("PAIDDATE", claimList.getString(38));
				jsonObject.put("REMARKS", claimList.getString(39));
				jsonObject.put("REJECTEDDATE", claimList.getString(40));
				jsonObject.put("INVESTIGATIONDATE", claimList.getString(41));
				jsonObject.put("SNACLAIMSTATUS", claimList.getString(42));
				jsonObject.put("SNAAPPROVEDAMOUNT", claimList.getString(43));
				jsonObject.put("SNAAPPROVEDDATE", claimList.getString(44));
				jsonObject.put("SNAREJECTEDDATE", claimList.getString(45));
				jsonObject.put("SNAINVESTIGATIONDATE", claimList.getString(46));
				jsonObject.put("SNAREMARKS", claimList.getString(47));
				jsonObject.put("TPAFINALSTATUS", claimList.getString(48));
				jsonObject.put("TPAFINALDECISIONDATE", claimList.getString(49));
				jsonObject.put("REVERTREMARKS", claimList.getString(50));
				jsonObject.put("SNAFINALSTATUS", claimList.getString(51));
				jsonObject.put("SNAFINALDECISIONDATE", claimList.getString(52));
				jsonArray.put(jsonObject);
				StoredProcedureQuery storedProcedureQuery1 = this.entityManager
						.createStoredProcedureQuery("USP_OLD_CLAIM_LOG_DETAILS")
						.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
				storedProcedureQuery1.setParameter("cid", Integer.parseInt(claimList.getString(16)));
				storedProcedureQuery1.execute();
				snoDetailsObj1 = (ResultSet) storedProcedureQuery1.getOutputParameterValue("P_P_MSGOUT");
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("CLAIMAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("additionaldoc1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("dateofadmission", DateFormat.FormatToDateString(claimList.getString(20)));
					jsonObject1.put("REMARK_ID", snoDetailsObj1.getLong(11));
					jsonObject1.put("Remark", snoDetailsObj1.getString(12));
					jsonObject1.put("additionaldoc2", snoDetailsObj1.getString(13));
					jsonObject1.put("approvedamount", snoDetailsObj1.getString(14));
					jsonObject1.put("intra_surgery_photo", snoDetailsObj1.getString(15));
					jsonObject1.put("specimen_removal_photo", snoDetailsObj1.getString(16));
					jsonObject1.put("patient_photo", snoDetailsObj1.getString(17));
					jsonArray.put(jsonObject1);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (claimList != null) {
					claimList.close();
					if (snoDetailsObj1 != null) {
						snoDetailsObj1.close();
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return jsonArray.toString();
	}

	@Override
	public Response takeActionOnOldClaimforsnoQuery(String claimID, String hospitalCode, MultipartFile additionaldoc1,
			MultipartFile additionaldoc2, String urnNumber, String dateofAdmission, String transId, String ClaimAmount,
			String actionby) throws Exception {
		String year = dateofAdmission.substring(6, 10);
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		Response response = new Response();
		Integer claimraiseInteger = null;
		Map<String, String> filePath = DocUtill.createFile2(urnNumber.trim(), year.trim(), hospitalCode.trim(),
				additionaldoc1, additionaldoc2);
		filePath.forEach((k, v) -> {
			if (v != null && !v.equalsIgnoreCase("")) {
				String fullFilePath = DocUtill.getFullDocumentPath(v, year, hospitalCode, DocUtill.getFolderName(v));
				File file = new File(fullFilePath);
				if (!file.exists()) {
					filePath.forEach((k1, v1) -> {
						if (v1 != null && !v1.equalsIgnoreCase("")) {
							String fullFilePath1 = DocUtill.getFullDocumentPath(v1, year, hospitalCode,
									DocUtill.getFolderName(v1));
							File file1 = new File(fullFilePath1);
							if (file1.exists()) {
								file1.delete();
							}
						}
					});
					if (k.equalsIgnoreCase("Additionaldoc1"))
						throw new RuntimeException(
								additionaldoc1.getOriginalFilename() + " Additional Slip1 Failed To Save in Server!");
					else if (k.equalsIgnoreCase("Additionaldoc2"))
						throw new RuntimeException(
								additionaldoc2.getOriginalFilename() + " Additional Slip2 Failed To Save in Server!");
				}
			}
		});
		try {
			StoredProcedureQuery saveCpdUserData = this.entityManager
					.createStoredProcedureQuery("SP_HPTL_OLD_CLAIM_ACT")
					.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AdditonalDoc1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_AdittionalDoc2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ClaimAmount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ActionType", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ActionBy", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CreatedBy", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_StatusFlag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);
			saveCpdUserData.setParameter("p_action", "W");
			saveCpdUserData.setParameter("P_TRANSACTIONDETAILSID", claimID);
			for (Map.Entry<String, String> entry : filePath.entrySet()) {
				if (Additionaldoc1.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_AdditonalDoc1", entry.getValue());
				else if (Additionaldoc2.contains(entry.getKey()))
					saveCpdUserData.setParameter("p_AdittionalDoc2", entry.getValue());
			}
			saveCpdUserData.setParameter("P_CLAIMID", transId);
			saveCpdUserData.setParameter("P_URN", urnNumber.trim());
			saveCpdUserData.setParameter("p_USER_IP", getuseripaddressString);
			saveCpdUserData.setParameter("P_ClaimAmount", ClaimAmount);
			saveCpdUserData.setParameter("P_ActionType", "5");
			saveCpdUserData.setParameter("P_ActionBy", actionby);
			saveCpdUserData.setParameter("P_CreatedBy", actionby);
			saveCpdUserData.setParameter("P_StatusFlag", "0");
			claimraiseInteger = (Integer) saveCpdUserData.getOutputParameterValue("p_msgout");
			if (claimraiseInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Re-Claim Raised Successfully");
			} else if (claimraiseInteger == 2) {
				response.setStatus("Failed");
				response.setMessage("OOPS Something Went Worng,please Try Agian..");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public List<Object> getOldClaimReApproveList(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_OLD_CLAIM_RE_APRV_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");
			while (snoDetailsObj.next()) {
				OldClaimListBean resBean = new OldClaimListBean();
				resBean.setTransid(snoDetailsObj.getLong(1));
				resBean.setURN(snoDetailsObj.getString(2));
				resBean.setClaimstatus(snoDetailsObj.getString(3));
				resBean.setHospitalcode(snoDetailsObj.getString(4));
				resBean.setHospitalname(snoDetailsObj.getString(5));
				resBean.setPatientName(snoDetailsObj.getString(6));
				resBean.setInvoiceNumber(snoDetailsObj.getString(7));
				resBean.setSnaremarks(snoDetailsObj.getString(8));
				resBean.setRemarks(snoDetailsObj.getString(9));
				resBean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(10))));
				resBean.setDateofdischarge(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(11))));
				resBean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(12))));
				resBean.setDateofadmission(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(13))));
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(14));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(15));
				resBean.setPackageName(snoDetailsObj.getString(16));
				resBean.setPackageCode(snoDetailsObj.getString(17));
				SnoclaimRaiseDetailsList.add(resBean);
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
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public String getOldClaimReAprvById(Integer txnId) throws Exception {
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
					.createStoredProcedureQuery("USP_GET_OLD_CLAIM_RE_APRV_DTLS")
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
				authorizedCode = snoDetailsObj.getString(28);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
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
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(24), snoDetailsObj.getString(25)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(19));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(21));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(22));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(23));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(24)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(25)));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(26));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(27));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(28));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(29));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(30));
				jsonObject.put("Address", snoDetailsObj.getString(31));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(32));
				jsonObject.put("PackageCode", snoDetailsObj.getString(33));
				jsonObject.put("MOBILE", snoDetailsObj.getString(34));
				jsonObject.put("year", snoDetailsObj.getString(35));
				jsonObject.put("HOSPITALCODE1", snoDetailsObj.getString(36));
				jsonObject.put("FILENAMES", snoDetailsObj.getString(37));
				jsonObject.put("FILENAMES1", snoDetailsObj.getString(38));
				jsonObject.put("FILENAMES2", snoDetailsObj.getString(39));
				jsonObject.put("CLAIMSTATUS", snoDetailsObj.getString(40));
				jsonObject.put("APPROVEDAMOUNT", snoDetailsObj.getString(41));
				jsonObject.put("APPROVEDDATE", snoDetailsObj.getString(42));
				jsonObject.put("PAIDBY", snoDetailsObj.getString(43));
				jsonObject.put("CHECK_DDNO", snoDetailsObj.getString(44));
				jsonObject.put("BANKNAME", snoDetailsObj.getString(45));
				jsonObject.put("PAIDDATE", snoDetailsObj.getString(46));
				jsonObject.put("REMARKS", snoDetailsObj.getString(47));
				jsonObject.put("REJECTEDDATE", snoDetailsObj.getString(48));
				jsonObject.put("INVESTIGATIONDATE", snoDetailsObj.getString(49));
				jsonObject.put("SNACLAIMSTATUS", snoDetailsObj.getString(50));
				jsonObject.put("SNAAPPROVEDAMOUNT", snoDetailsObj.getString(51));
				jsonObject.put("SNAAPPROVEDDATE", snoDetailsObj.getString(52));
				jsonObject.put("SNAREJECTEDDATE", snoDetailsObj.getString(53));
				jsonObject.put("SNAINVESTIGATIONDATE", snoDetailsObj.getString(54));
				jsonObject.put("SNAREMARKS", snoDetailsObj.getString(55));
				jsonObject.put("TPAFINALSTATUS", snoDetailsObj.getString(56));
				jsonObject.put("TPAFINALDECISIONDATE", snoDetailsObj.getString(57));
				jsonObject.put("REVERTREMARKS", snoDetailsObj.getString(58));
				jsonObject.put("SNAFINALSTATUS", snoDetailsObj.getString(59));
				jsonObject.put("SNAFINALDECISIONDATE", snoDetailsObj.getString(60));
				jsonObject.put("CLAIMS_TATUS", snoDetailsObj.getString(61));
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
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(24)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonObject1.put("INVESTIGATION", snoDetailsObj1.getString(17));
					jsonObject1.put("INVESTIGATION2", snoDetailsObj1.getString(18));
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
				} else if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public Response saveClaimSNOOfOldReAprvDetails(ClaimLogBean logBean) {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		try {
			localhost = InetAddress.getLocalHost();
			getuseripaddressString = localhost.getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_CLAIM_RE_APRV_ACTION")
					.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PENDINGAT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMSTATUS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONREMARKID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONREMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			if (claimsnoInteger == 1 && logBean.getClaimStatus() == 1) {
				response.setStatus("Success");
				response.setMessage("Claim Approved Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 2) {
				response.setStatus("Success");
				response.setMessage("Claim Rejected Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 4) {
				response.setStatus("Success");
				response.setMessage("Claim Queried Successfully");
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
	public void downLoadOldFile(String fileName, String year, String hCode, HttpServletResponse response) {
		try {
			CommonFileUpload.downloadOldFile(fileName, year, hCode, response);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public String getOldActionList(CPDApproveRequestBean requestBean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_CLAIM_ACTION_LIST")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("TRANSID", snoDetailsObj.getLong(1));
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(2));
				jsonObject.put("URN", snoDetailsObj.getString(3));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(4));
				jsonObject.put("DATEOFADMISSION", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(5))));
				jsonObject.put("DATEOFDISCHARGE", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(6))));
				jsonObject.put("ACTUALDATEOFADMISSION", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(7))));
				jsonObject.put("ACTUALDATEOFDISCHARGE", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(8))));
				jsonObject.put("HOSPITALDETAILS", snoDetailsObj.getString(9));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(10));
				jsonObject.put("PACKAGECODE", snoDetailsObj.getString(11));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(12));
				jsonObject.put("ACTIONON", snoDetailsObj.getString(13));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(14));
				jsonObject.put("CLAIMAMOUNT", snoDetailsObj.getString(15));
				jsonObject.put("CURRENTCLAIMSTATUS", snoDetailsObj.getString(16));
				jsonObject.put("CLAIMSTATUS", snoDetailsObj.getInt(17));
				jsonObject.put("PENDINGAT", snoDetailsObj.getInt(18));
				jsonObject.put("NEWSNAREMARKS", snoDetailsObj.getString(19));
				jsonObject.put("OLDSNAREMARKS", snoDetailsObj.getString(20));
				jsonObject.put("TRANSACTIONDETAILSID", snoDetailsObj.getString(21));
				jsonArray.put(jsonObject);
			}
			details.put("actionData", jsonArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public String getOldClaimTrackingDetailsById(Integer txnId) throws Exception {
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
					.createStoredProcedureQuery("USP_OLD_CLAIM_TRACKING_DTLS")
					.registerStoredProcedureParameter("P_TRANSID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_log_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_TRANSID", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_log_msgout");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(28);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
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
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(24), snoDetailsObj.getString(25)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(19));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(21));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(22));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(23));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(24)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(25)));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(26));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(27));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(28));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(29));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(30));
				jsonObject.put("Address", snoDetailsObj.getString(31));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(32));
				jsonObject.put("PackageCode", snoDetailsObj.getString(33));
				jsonObject.put("MOBILE", snoDetailsObj.getString(34));
				jsonObject.put("year", snoDetailsObj.getString(35));
				jsonObject.put("HOSPITALCODE1", snoDetailsObj.getString(36));
				jsonObject.put("FILENAMES", snoDetailsObj.getString(37));
				jsonObject.put("FILENAMES1", snoDetailsObj.getString(38));
				jsonObject.put("FILENAMES2", snoDetailsObj.getString(39));
				jsonObject.put("CLAIMSTATUS", snoDetailsObj.getString(40));
				jsonObject.put("APPROVEDAMOUNT", snoDetailsObj.getString(41));
				jsonObject.put("APPROVEDDATE", snoDetailsObj.getString(42));
				jsonObject.put("PAIDBY", snoDetailsObj.getString(43));
				jsonObject.put("CHECK_DDNO", snoDetailsObj.getString(44));
				jsonObject.put("BANKNAME", snoDetailsObj.getString(45));
				jsonObject.put("PAIDDATE", snoDetailsObj.getString(46));
				jsonObject.put("REMARKS", snoDetailsObj.getString(47));
				jsonObject.put("REJECTEDDATE", snoDetailsObj.getString(48));
				jsonObject.put("INVESTIGATIONDATE", snoDetailsObj.getString(49));
				jsonObject.put("SNACLAIMSTATUS", snoDetailsObj.getString(50));
				jsonObject.put("SNAAPPROVEDAMOUNT", snoDetailsObj.getString(51));
				jsonObject.put("SNAAPPROVEDDATE", snoDetailsObj.getString(52));
				jsonObject.put("SNAREJECTEDDATE", snoDetailsObj.getString(53));
				jsonObject.put("SNAINVESTIGATIONDATE", snoDetailsObj.getString(54));
				jsonObject.put("SNAREMARKS", snoDetailsObj.getString(55));
				jsonObject.put("TPAFINALSTATUS", snoDetailsObj.getString(56));
				jsonObject.put("TPAFINALDECISIONDATE", snoDetailsObj.getString(57));
				jsonObject.put("REVERTREMARKS", snoDetailsObj.getString(58));
				jsonObject.put("SNAFINALSTATUS", snoDetailsObj.getString(59));
				jsonObject.put("SNAFINALDECISIONDATE", snoDetailsObj.getString(60));
				jsonObject.put("CLAIM_NO", snoDetailsObj.getString(61));
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
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(24)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonObject1.put("INVESTIGATION", snoDetailsObj1.getString(17));
					jsonObject1.put("INVESTIGATION2", snoDetailsObj1.getString(18));
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
				} else if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public String getOldSNAProcessedList(CPDApproveRequestBean requestBean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_CLM_SNA_PROCESSED_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_status", requestBean.getFlag());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("TRANSID", snoDetailsObj.getLong(1));
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(2));
				jsonObject.put("URN", snoDetailsObj.getString(3));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(4));
				jsonObject.put("DATEOFADMISSION", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(5))));
				jsonObject.put("DATEOFDISCHARGE", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(6))));
				jsonObject.put("ACTUALDATEOFADMISSION", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(7))));
				jsonObject.put("ACTUALDATEOFDISCHARGE", new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(8))));
				jsonObject.put("HOSPITALDETAILS", snoDetailsObj.getString(9));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(10));
				jsonObject.put("PACKAGECODE", snoDetailsObj.getString(11));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(12));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(13));
				jsonObject.put("CLAIMAMOUNT", snoDetailsObj.getString(14));
				jsonObject.put("CLAIMSTATUS", snoDetailsObj.getInt(15));
				jsonObject.put("PENDINGAT", snoDetailsObj.getInt(16));
				jsonObject.put("OLDSNAREMARKS", snoDetailsObj.getString(17));
				jsonObject.put("TRANSACTIONDETAILSID", snoDetailsObj.getString(18));
				jsonObject.put("APPROVEDAMOUNT", snoDetailsObj.getString(19));
				jsonObject.put("NEWSNAREMARKS", snoDetailsObj.getString(20));
				jsonObject.put("CURRENTSTATUS", snoDetailsObj.getString(21));
				jsonArray.put(jsonObject);
			}
			details.put("actionData", jsonArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public String getOldReclaimedPendingAtSNAList(CPDApproveRequestBean requestBean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_RECLAIMED_LIST")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_status", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_claim_status", requestBean.getClaimStatus());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(1));
				jsonObject.put("URN", snoDetailsObj.getString(2));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(3));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(4));
				jsonObject.put("TRANSID", snoDetailsObj.getLong(5));
				jsonObject.put("DATEOFADMISSION", snoDetailsObj.getDate(6));
				jsonObject.put("ACTUALDATEOFADMISSION", snoDetailsObj.getDate(7));
				jsonObject.put("DATEOFDISCHARGE", snoDetailsObj.getDate(8));
				jsonObject.put("ACTUALDATEOFDISCHARGE", snoDetailsObj.getDate(9));
				jsonObject.put("ACTIONON", snoDetailsObj.getString(10));
				jsonObject.put("CLAIMAMOUNT", snoDetailsObj.getString(11));
				jsonObject.put("PACKAGECODE", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("OLDCLAIMSTATUS", snoDetailsObj.getString(14));
				jsonArray.put(jsonObject);
			}
			details.put("actionData", jsonArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public String getOldClaimCountProgress(OldFloatBean requestBean) {
				String count = null;
				ResultSet rs = null;
				try {
					StoredProcedureQuery storedProcedureQuery = this.entityManager
							.createStoredProcedureQuery("USP_OLD_CLAIM_PROGRESS_REPORT")
							.registerStoredProcedureParameter("P_SNA_USERID", Long.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

					storedProcedureQuery.setParameter("P_SNA_USERID", requestBean.getUserId());
					storedProcedureQuery.setParameter("P_FROMDATE", requestBean.getFromDate());
					storedProcedureQuery.setParameter("P_TODATE", requestBean.getToDate());
					storedProcedureQuery.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalId());
					storedProcedureQuery.setParameter("P_STATECODE", requestBean.getStateId());
					storedProcedureQuery.setParameter("P_DISTRICTCODE", requestBean.getDistrictId());
					storedProcedureQuery.execute();

					rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
					while (rs.next()) {
						JSONObject json = new JSONObject();
						json.put("totalCaseReopen", rs.getLong(1));
						json.put("totalApprovedReopen", rs.getLong(2));
						json.put("totalSnaRejectedReopen", rs.getLong(3));
						json.put("totalAPSNAApproved", rs.getLong(4));
						json.put("totalAPSNARejected", rs.getLong(5));
						json.put("totalAPQueryPendingAtHospital", rs.getLong(6));
						json.put("totalAPQueryReClaimed", rs.getLong(7));
						json.put("totalSNARejSNAApproved", rs.getLong(8));
						json.put("totalSNARejSNARejected", rs.getLong(9));
						json.put("totalSNARejQueryPendingAtHospital", rs.getLong(10));
						json.put("totalSNARejQueryReClaimed", rs.getLong(11));
						count = json.toString();
					}
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				return count;
	}

	@Override
	public String getOldclaimprogressreportdetails(Long userId, Date fromdate, Date toDate, String eventName,
			String stateId, String districtId, String hospitalId) {
		JSONArray detailscountprogressreport = new JSONArray();
		Integer eventNumber = getEventNumber(eventName);
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_CLAIM_PROGRESS_DETAILS")
					.registerStoredProcedureParameter("P_SNA_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EVENT", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNA_USERID", userId);
			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", toDate);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalId);
			storedProcedureQuery.setParameter("P_STATECODE", stateId);
			storedProcedureQuery.setParameter("P_DISTRICTCODE", districtId);
			storedProcedureQuery.setParameter("P_EVENT", eventNumber);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				JSONObject obj=new JSONObject();
				obj.put("claimNo", rs.getString(1));
				obj.put("urn", rs.getString(2));
				obj.put("hospitalName", rs.getString(3));
				obj.put("hospitalCode", rs.getString(4));
				obj.put("dateOfAdmission",rs.getString(5)!=null && !"".equals(rs.getString(5))? new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("ddMMyyyy").parse(rs.getString(5))):"");
				obj.put("actualDateOfAdmission",rs.getString(6)!=null && !"".equals(rs.getString(6))? new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("ddMMyyyy").parse(rs.getString(6))):"");
				obj.put("dateOfDischarge", rs.getString(7)!=null && !"".equals(rs.getString(7))?new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("ddMMyyyy").parse(rs.getString(7))):"");
				obj.put("actualDateOfDischarge", rs.getString(8)!=null && !"".equals(rs.getString(8))?new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("ddMMyyyy").parse(rs.getString(8))):"");
				obj.put("oldClaimStatus", rs.getString(9));
				obj.put("claimStatus", rs.getInt(10));
				obj.put("claimAmount", rs.getString(11));
				obj.put("invoiceNo", rs.getString(12));
				obj.put("transId", rs.getLong(13));
				obj.put("transactionDetailsId", rs.getLong(14));
				obj.put("pendingAt", rs.getInt(15));
				obj.put("currentStatus", getStatus(rs.getInt(15),rs.getInt(10)));
				detailscountprogressreport.put(obj);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return detailscountprogressreport.toString();
	}
	public Integer getEventNumber(String eventName) {
		Integer val = 0;
		switch (eventName) {
		case "TRO":
			val = 1;
			break;
		case "TROA":
			val = 2;
			break;
		case "TROSR":
			val = 3;
			break;
		case "APSA":
			val = 4;
			break;
		case "ASSR":
			val = 5;
			break;
		case "ASQPH":
			val = 6;
			break;
		case "AHRPS":
			val = 7;
			break;
		case "SRSA":
			val = 8;
			break;
		case "SRSR":
			val = 9;
			break;
		case "SRSQPH":
			val = 10;
			break;
		case "SNHRPS":
			val = 11;
			break;
		default:
			break;
		}
		return val;
		
	}
	public String getStatus(Integer pendingAt,Integer claimStatus) {
		String status="";
		if(pendingAt==3 && claimStatus==1)
			status="SNA Approved";
		else if(pendingAt==3 && claimStatus==2)
			status="SNA Rejected";
		else if(pendingAt==0 && claimStatus==4)
			status="SNA Queried And Pending At Hospital";
		else if(pendingAt==2 && claimStatus==4)
			status="Hospital Reclaimed and Pending At SNA";
		return status;
	}

	@Override
	public List<Object> oldclaimnoncompliance(Date fromdate, Date toDate, Long userId, String state, String dist,
			String hospital) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_CLM_NON_COMPLIANCE_LIST")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNAID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 1);
			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", toDate);
			storedProcedureQuery.setParameter("P_SNAID", userId);
			storedProcedureQuery.setParameter("P_STATECODE", state);
			storedProcedureQuery.setParameter("P_DISTRICTCODE", dist);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospital);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				Map<String,Object> map=new HashMap<>();
				map.put("transactiondetailsId", rs.getString(1));
				map.put("transactionId", rs.getString(2));
				map.put("id", rs.getString(3));
				map.put("urn", rs.getString(4));
				map.put("patientName", rs.getString(5));
				map.put("invoiceNo", rs.getString(6));
				map.put("caseNo", rs.getString(7));
				map.put("dateofAdm", rs.getString(8));
				map.put("dateofDis", rs.getString(9));
				map.put("actDateofAdm", rs.getString(10));
				map.put("actDateofDis", rs.getString(11));
				map.put("pkgCode", rs.getString(12));
				map.put("pkgName", rs.getString(13));
				map.put("procedureName", rs.getString(14));
				map.put("totalAmount", rs.getString(15));
				map.put("hospitalCode", rs.getString(16));
				map.put("hoispitalName", rs.getString(17)+" ("+rs.getString(16)+")");
				map.put("queryOn", rs.getString(18));
				map.put("claimStatus", rs.getString(19));
				map.put("remark", rs.getString(20));
				list.add(map);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return list;
	}
}
