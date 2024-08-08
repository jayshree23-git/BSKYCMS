package com.project.bsky.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
/**
 * @author preetam.mishra
 *
 */
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.ClaimsQueriedToHospitalBySNOService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.DaysBetweenDates;
import com.project.bsky.util.DocUtill;

@SuppressWarnings("unused")
@Service
public class ClaimsQueriedToHospitalBySNOServiceImpl implements ClaimsQueriedToHospitalBySNOService {

	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");
	private static ResourceBundle bskyResourcesBundel1 = ResourceBundle.getBundle("fileConfiguration");

	@Value("${file.path.Additionaldoc1}")
	private String Additionaldoc1;

	@Value("${file.path.Additionaldoc2}")
	private String Additionaldoc2;

	@PersistenceContext
	private EntityManager entityManager;

	private final Logger logger;

	@Autowired
	public ClaimsQueriedToHospitalBySNOServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Object> getQueriedClaimsList(String hospitalCode, String fromDate, String toDate, String package1,
			String packagecode, String URN,String schemeid, String schemecategoryid) {
		if (package1.equals("")) {
			package1 = "";
		}
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Long.parseLong(schemecategoryid);
		} else {
			schemecatId = null;
		}
		List<Object> claimRaiseList = new ArrayList<>();
		ResultSet claimListObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("Usp_Claim_Sno_Querytohospital")
					.registerStoredProcedureParameter("p_ActionCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_ActionCode", "V");
			storedProcedureQuery.setParameter("p_hospitalcode", hospitalCode.trim());
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_packagecode", package1.trim());
			storedProcedureQuery.setParameter("p_packagename", packagecode.trim());
			storedProcedureQuery.setParameter("p_urn", URN.trim());
			storedProcedureQuery.setParameter("P_SCHEME_ID", Long.parseLong(schemeid));
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_P_MSGOUT");
			while (claimListObj.next()) {
				ClaimsQueriedToHospitalBySNOBean resBean = new ClaimsQueriedToHospitalBySNOBean();
				resBean.setClaimID(claimListObj.getLong(1));
				resBean.setURN(claimListObj.getString(2));
				resBean.setClaimStatus(claimListObj.getString(3).toString());
				resBean.setPendingAt(claimListObj.getString(4).toString());
				resBean.setUpdateon(claimListObj.getString(5).toString());
				resBean.setSnoapprovedamount(claimListObj.getString(6));
				resBean.setPatientName(claimListObj.getString(7).toString());
				resBean.setPackageCode(claimListObj.getString(8).toString().substring(4));
				resBean.setPackageName(claimListObj.getString(9).toString());
				String dateofadmissionString = claimListObj.getString(10);
				String s1 = dateofadmissionString.substring(0, 2);
				String s2 = dateofadmissionString.substring(2, 4);
				String s3 = dateofadmissionString.substring(4, 8);
				String s4 = s1 + "/" + s2 + "/" + s3;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf1.format(date11);
				resBean.setDateofadmission(d);
				resBean.setuPDATEON(claimListObj.getString(11));
				String getdat = claimListObj.getString(11).substring(0, 10);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(getdat, formatter);
				Date date1 = java.util.Date
						.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = sdf.format(date1);
				int days = DaysBetweenDates.daysCountBetweenDates(sdf.parse(strDate));
				resBean.setGetDaysleft(String.valueOf(days) + "days left");
				resBean.setClaim_no(claimListObj.getString(12).toString());
				resBean.setRemarksDes(claimListObj.getString(13).toString());
				resBean.setRemark(claimListObj.getString(14).toString());
				resBean.setInvoiceno(claimListObj.getString(15));
				String Actualdateofadmission = claimListObj.getString(16);
				String s11 = Actualdateofadmission.substring(0, 2);
				String s21 = Actualdateofadmission.substring(2, 4);
				String s31 = Actualdateofadmission.substring(4, 8);
				String s41 = s11 + "/" + s21 + "/" + s31;
				Date date111 = new SimpleDateFormat("dd/MM/yyyy").parse(s41);
				SimpleDateFormat sdf11 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf11.format(date111);
				resBean.setActualdateofadmission(d1);
				String Dateofdischarge = claimListObj.getString(17);
				String q11 = Dateofdischarge.substring(0, 2);
				String q21 = Dateofdischarge.substring(2, 4);
				String q31 = Dateofdischarge.substring(4, 8);
				String q41 = q11 + "/" + q21 + "/" + q31;
				Date datevalue = new SimpleDateFormat("dd/MM/yyyy").parse(q41);
				SimpleDateFormat sdate = new SimpleDateFormat("dd-MMM-yyyy");
				String data = sdate.format(datevalue);
				resBean.setDateofdischarge(data);
				String Actualdateofdischarge = claimListObj.getString(18);
				String q111 = Actualdateofdischarge.substring(0, 2);
				String q211 = Actualdateofdischarge.substring(2, 4);
				String q311 = Actualdateofdischarge.substring(4, 8);
				String q411 = q111 + "/" + q211 + "/" + q311;
				Date datevalue1 = new SimpleDateFormat("dd/MM/yyyy").parse(q411);
				SimpleDateFormat sdate1 = new SimpleDateFormat("dd-MMM-yyyy");
				String data1 = sdate1.format(datevalue1);
				resBean.setActualdateofdischarge(data1);
				resBean.setCaseno(claimListObj.getString(19));
				resBean.setSnaquerydate(claimListObj.getString(20));
				String snaquerydate = claimListObj.getString(20);
				Date snadate = new Date(snaquerydate);
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				String strDate1 = formatter1.format(snadate);
				int snaquerydays = DaysBetweenDates.daysCountBetweenDates(formatter1.parse(strDate1));
				resBean.setRemainingdayssnaquerydate(String.valueOf(snaquerydays) + "days left");
				claimRaiseList.add(resBean);
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in getQueriedClaimsList method of ClaimsQueriedToHospitalBySNOServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimRaiseList;
	}

	@Override
	public String getQueriedClaimDetails(String claimID) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		ResultSet claimList = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("Usp_Claim_Sno_querytohsp_Dtls")
					.registerStoredProcedureParameter("P_Action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("P_Action", "Y");
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
				jsonObject.put("addtional_doc", claimList.getString(16));
				jsonObject.put("PRESURGERYPHOTO", claimList.getString(17));
				jsonObject.put("POSTSURGERYPHOTO", claimList.getString(18));
				jsonObject.put("CREATEDON", claimList.getString(19));
				jsonObject.put("CLAIMID", claimList.getString(20));
				jsonObject.put("BLOCKNAME", claimList.getString(21));
				jsonObject.put("DISTRICTNAME", claimList.getString(22));
				jsonObject.put("STATENAME", claimList.getString(23));
				jsonObject.put("dateofadmission", DateFormat.FormatToDateString(claimList.getString(24)));
				jsonObject.put("DateOfDischarge", DateFormat.FormatToDateString(claimList.getString(25)));
				jsonObject.put("dischargeslip", claimList.getString(26));
				jsonObject.put("hospitalcode", claimList.getString(27));
				jsonObject.put("transactiondetailsid", claimList.getString(28));
				jsonObject.put("CLAIM_CASE_NO", claimList.getString(29));
				jsonObject.put("CLAIM_BILL_NO", claimList.getString(30));
				jsonObject.put("claim_no", claimList.getString(31));
				jsonObject.put("intra_surgery_photo", claimList.getString(32));
				jsonObject.put("specimen_removal_photo", claimList.getString(33));
				jsonObject.put("patient_photo", claimList.getString(34));
				jsonObject.put("patientphoneno", claimList.getString(35));
				jsonObject.put("actualdateofadmission", DateFormat.FormatToDateString(claimList.getString(36)));
				jsonObject.put("actualdateofdischarge", DateFormat.FormatToDateString(claimList.getString(37)));
				jsonObject.put("NOOFDAYSADDMITTEDHOSPITAL",
						CommonFileUpload.totalDaysBetweenDates(claimList.getString(24), claimList.getString(25)));
				jsonObject.put("verification", claimList.getString(38));
				jsonObject.put("ispatient", claimList.getString(39));
				jsonObject.put("Referalstatus", claimList.getString(40));
				jsonObject.put("packageCode1", claimList.getString(41) != null ? claimList.getString(41) : "NA");
				jsonObject.put("packageName1", claimList.getString(42) != null ? claimList.getString(42) : "NA");
				jsonObject.put("subPackageCode1", claimList.getString(43) != null ? claimList.getString(43) : "NA");
				jsonObject.put("subPackageName1", claimList.getString(44) != null ? claimList.getString(44) : "NA");
				jsonObject.put("procedureCode1", claimList.getString(45) != null ? claimList.getString(45) : "NA");
				jsonObject.put("procedureName1", claimList.getString(46) != null ? claimList.getString(46) : "NA");
				jsonObject.put("categoryName", claimList.getString(47) != null ? claimList.getString(47) : "NA");
				jsonArray.put(jsonObject);
				StoredProcedureQuery storedProcedureQuery1 = this.entityManager
						.createStoredProcedureQuery("USP_SNO_CLAIM_LOG_DETAILS")
						.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
				storedProcedureQuery1.setParameter("cid", Integer.parseInt(claimList.getString(20)));
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
					jsonObject1.put("dateofadmission", DateFormat.FormatToDateString(claimList.getString(24)));
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
			logger.error(
					"Exception occured in getQueriedClaimDetails method of ClaimsQueriedToHospitalBySNOServiceImpl :",
					e);
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArray.toString();
	}

	@Override
	public void downLoadFileforforSnoQueriedthospital(String fileName, String year, String hCode,
			HttpServletResponse response) {
		String folderName = null;
		if (fileName.startsWith(bskyResourcesBundel.getString("file.presurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.presurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.postsurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.postsurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.AdditionalDoc.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.AdditionalDoc");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.DischargeSlip.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.DischargeSlip");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigation.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation");
		}
		try {
			CommonFileUpload.downloadFile(fileName, year, hCode, folderName, response);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Response takeActionOnClaimforsnoQuery(String claimID, String hospitalCode, MultipartFile additionaldoc1,
			MultipartFile additionaldoc2, String urnNumber, String dateofAdmission, String ClaimId, String ClaimAmount,
			String actionby, String dischargeSlip, String Additionalslip, String presuergrypic, String postsuergrypic,
			String intrasurgerypic, String SpecimenPic, String patientpic) throws Exception {
		String year = dateofAdmission.substring(6, 10);
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		Response response = new Response();
		Integer claimraiseInteger = null;
		Map<String, String> filePath = DocUtill.createFile1(urnNumber.trim(), year.trim(), hospitalCode.trim(),
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
					.createStoredProcedureQuery("Usp_Claim_Sno_Querytohsp_Act")
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
					.registerStoredProcedureParameter("P_DischargeSlip", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_additionaldoc", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_presuergrypic", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_postsurgerypic", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IntraSurgery", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SpecimanSurgery", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PatientPic", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);
			saveCpdUserData.setParameter("p_action", "W");
			saveCpdUserData.setParameter("P_TRANSACTIONDETAILSID", claimID);
			for (Map.Entry<String, String> entry : filePath.entrySet()) {
				if (Additionaldoc1.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_AdditonalDoc1", entry.getValue());

				else if (Additionaldoc2.contains(entry.getKey()))
					saveCpdUserData.setParameter("p_AdittionalDoc2", entry.getValue());
			}
			saveCpdUserData.setParameter("P_CLAIMID", ClaimId);
			saveCpdUserData.setParameter("P_URN", urnNumber.trim());
			saveCpdUserData.setParameter("p_USER_IP", getuseripaddressString);
			saveCpdUserData.setParameter("P_ClaimAmount", ClaimAmount);
			saveCpdUserData.setParameter("P_ActionType", "5");
			saveCpdUserData.setParameter("P_ActionBy", actionby);
			saveCpdUserData.setParameter("P_CreatedBy", actionby);
			saveCpdUserData.setParameter("P_StatusFlag", "0");
			saveCpdUserData.setParameter("P_DischargeSlip", dischargeSlip);
			saveCpdUserData.setParameter("P_additionaldoc", Additionalslip);
			saveCpdUserData.setParameter("P_presuergrypic", presuergrypic);
			saveCpdUserData.setParameter("P_postsurgerypic", postsuergrypic);
			saveCpdUserData.setParameter("P_IntraSurgery", intrasurgerypic);
			saveCpdUserData.setParameter("P_SpecimanSurgery", SpecimenPic);
			saveCpdUserData.setParameter("P_PatientPic", patientpic);
			claimraiseInteger = (Integer) saveCpdUserData.getOutputParameterValue("p_msgout");
			if (claimraiseInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Re-Claim Raised Successfully");
			} else if (claimraiseInteger == 2) {
				response.setStatus("Failed");
				response.setMessage("OOPS Something Went Worng,please Try Agian..");
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in takeActionOnClaimforsnoQuery method of ClaimsQueriedToHospitalBySNOServiceImpl :",
					e);
			throw new Exception(e.getMessage());
		}
		return response;
	}
}
