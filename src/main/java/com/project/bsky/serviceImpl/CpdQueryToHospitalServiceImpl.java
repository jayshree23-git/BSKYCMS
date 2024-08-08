package com.project.bsky.serviceImpl;

import java.io.File;
import java.net.InetAddress;
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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CpdQueryToHospitalBean;
import com.project.bsky.bean.Response;
import com.project.bsky.repository.ActionRemarkRepository;
import com.project.bsky.service.CpdQueryToHospitalService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.DaysBetweenDates;
import com.project.bsky.util.DocUtill;

@SuppressWarnings("unused")
@Service
public class CpdQueryToHospitalServiceImpl implements CpdQueryToHospitalService {

	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	private static ResourceBundle bskyResourcesBundel2 = ResourceBundle.getBundle("fileConfiguration");

	private static ResourceBundle bskyResourcesBundel1 = ResourceBundle.getBundle("fileConfiguration");

	@Value("${file.path.Additionaldoc1}")
	private String Additionaldoc1;

	@Value("${file.path.Additionaldoc2}")
	private String Additionaldoc;

	private final Logger logger;

	@Autowired
	public CpdQueryToHospitalServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ActionRemarkRepository actionrepository;

	@SuppressWarnings("deprecation")
	@Override
	public List<Object> getAllData(String hospitalCode, String fromDate, String toDate, String package1,
			String packageCodedata, String uRN, String schemeid, String schemecategoryid) {
		List<Object> claimRaiseDetailsList = new ArrayList<Object>();
		if (package1.equals("")) {
			package1 = "";
		}
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			try {
				schemecatId = Long.parseLong(schemecategoryid);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			schemecatId = null;
		}
		ResultSet deptDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_cpd_querytohospital")
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", "V");
			storedProcedureQuery.setParameter("P_hospitalcode", hospitalCode.trim());
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_packagecode", package1.trim());
			storedProcedureQuery.setParameter("p_packagename", packageCodedata.trim());
			storedProcedureQuery.setParameter("p_urn", uRN.trim());
			storedProcedureQuery.setParameter("P_SCHEME_ID", Long.parseLong(schemeid));
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			deptDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (deptDetailsObj.next()) {
				CpdQueryToHospitalBean resBean = new CpdQueryToHospitalBean();
				resBean.setClaimId(deptDetailsObj.getLong(1));
				resBean.setUrnNo(deptDetailsObj.getString(2));
				resBean.setPatientname(deptDetailsObj.getString(3));
				resBean.setPackageCode(deptDetailsObj.getString(4).substring(4));
				resBean.setPackagename(deptDetailsObj.getString(5));
				String dateofadmissionString = deptDetailsObj.getString(6);
				String a = dateofadmissionString.substring(0, 2);
				String b = dateofadmissionString.substring(2, 4);
				String c = dateofadmissionString.substring(4, 8);
				String d = a + "/" + b + "/" + c;
				Date e = new SimpleDateFormat("dd/MM/yyyy").parse(d);
				SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
				String g = f.format(e);
				resBean.setDateofadmission(g);
				resBean.setCREATEDON(deptDetailsObj.getString(7));
				resBean.setCpdapprovedamount(deptDetailsObj.getString(8));
				resBean.setUpdateon(deptDetailsObj.getString(9));
				String getdat = deptDetailsObj.getString(9).substring(0, 10);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(getdat, formatter);
				Date date1 = java.util.Date
						.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = sdf.format(date1);
				int days = DaysBetweenDates.daysCountBetweenDates(sdf.parse(strDate));
				resBean.setCpdDaysleftString(String.valueOf(days) + "days left");
				resBean.setClaim_no(deptDetailsObj.getString(10));
				resBean.setRemarks(deptDetailsObj.getString(11));
				resBean.setRemark(deptDetailsObj.getString(12));
				resBean.setInvoiceno(deptDetailsObj.getString(13));
				String actualdateofadmissionString = deptDetailsObj.getString(14);
				String q1 = actualdateofadmissionString.substring(0, 2);
				String q2 = actualdateofadmissionString.substring(2, 4);
				String q3 = actualdateofadmissionString.substring(4, 8);
				String q4 = q1 + "/" + q2 + "/" + q3;
				Date condition = new SimpleDateFormat("dd/MM/yyyy").parse(q4);
				SimpleDateFormat ss = new SimpleDateFormat("dd-MMM-yyyy");
				String d11 = ss.format(condition);
				resBean.setActualdateofadmission(d11);
				String actualdateofdischargeString = deptDetailsObj.getString(15);
				String q11 = actualdateofdischargeString.substring(0, 2);
				String q21 = actualdateofdischargeString.substring(2, 4);
				String q31 = actualdateofdischargeString.substring(4, 8);
				String q41 = q11 + "/" + q21 + "/" + q31;
				Date condition1 = new SimpleDateFormat("dd/MM/yyyy").parse(q41);
				SimpleDateFormat ss1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d111 = ss1.format(condition1);
				resBean.setActualdateofdischarge(d111);
				String dateofdischargeString = deptDetailsObj.getString(16);
				String q111 = dateofdischargeString.substring(0, 2);
				String q211 = dateofdischargeString.substring(2, 4);
				String q311 = dateofdischargeString.substring(4, 8);
				String q411 = q111 + "/" + q211 + "/" + q311;
				Date condition11 = new SimpleDateFormat("dd/MM/yyyy").parse(q411);
				SimpleDateFormat ss11 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1111 = ss11.format(condition11);
				resBean.setDateofdischarge(d1111);
				resBean.setCaseno(deptDetailsObj.getString(17));
				resBean.setCpdquerydateString(deptDetailsObj.getString(18));
				String cpdquerydate = deptDetailsObj.getString(18);
				Date date5 = new Date(cpdquerydate);
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
				String strDate1 = formatter1.format(date5);
				int cpdquerydays = DaysBetweenDates.daysCountBetweenDates(formatter1.parse(strDate1));
				resBean.setRemainingdayscpdquery(String.valueOf(cpdquerydays) + "days left");
				claimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getAllData method of CpdQueryToHospitalServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (deptDetailsObj != null) {
					deptDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimRaiseDetailsList;
	}

	@Override
	public String getQueriedClaimDetails(String claimID) {
		JSONArray jsonArrays = new JSONArray();
		JSONObject jsonObjectw;
		JSONObject jsonObject1;
		ResultSet claimListObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_cpd_querytohosp_dtls")
					.registerStoredProcedureParameter("P_Action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_Action", "ok");
			storedProcedureQuery.setParameter("p_claimID", claimID);
			storedProcedureQuery.execute();
			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_P_MSGOUT");
			if (claimListObj.next()) {
				jsonObjectw = new JSONObject();
				jsonObjectw.put("urn", claimListObj.getString(1));
				jsonObjectw.put("invoiceno", claimListObj.getString(2));
				jsonObjectw.put("packageid", claimListObj.getString(3));
				jsonObjectw.put("totalamountblocked", claimListObj.getString(4));
				jsonObjectw.put("totalamountclaimed", claimListObj.getString(5));
				jsonObjectw.put("packagecode", claimListObj.getString(6));
				jsonObjectw.put("packagename", claimListObj.getString(7));
				jsonObjectw.put("procedurename", claimListObj.getString(8));
				jsonObjectw.put("packagecategorycode", claimListObj.getString(9));
				jsonObjectw.put("hospitalname", claimListObj.getString(10));
				jsonObjectw.put("currenttotalamount", claimListObj.getString(11));
				jsonObjectw.put("gender", claimListObj.getString(12));
				jsonObjectw.put("age", claimListObj.getString(13));
				jsonObjectw.put("patientname", claimListObj.getString(14));
				jsonObjectw.put("treatmentslip", claimListObj.getString(15));
				jsonObjectw.put("addtional_doc", claimListObj.getString(16));
				jsonObjectw.put("adminssionslip", claimListObj.getString(17));
				jsonObjectw.put("investigationdoc", claimListObj.getString(18));
				jsonObjectw.put("presurgeryphoto", claimListObj.getString(19));
				jsonObjectw.put("postsurgeryphoto", claimListObj.getString(20));
				jsonObjectw.put("createdby", claimListObj.getString(21));
				jsonObjectw.put("createdon", claimListObj.getString(22));
				jsonObjectw.put("claimid", claimListObj.getString(23));
				jsonObjectw.put("blockname", claimListObj.getString(24));
				jsonObjectw.put("districtname", claimListObj.getString(25));
				jsonObjectw.put("statename", claimListObj.getString(26));
				jsonObjectw.put("hospitalcode", claimListObj.getString(27));
				jsonObjectw.put("dateofadmission", DateFormat.FormatToDateString(claimListObj.getString(28)));
				jsonObjectw.put("remarks", claimListObj.getString(29));
				jsonObjectw.put("dateofdischarge", DateFormat.FormatToDateString(claimListObj.getString(30)));
				jsonObjectw.put("dischargeslip", claimListObj.getString(31));
				jsonObjectw.put("transactiondetailsid", claimListObj.getString(32));
				jsonObjectw.put("CLAIM_CASE_NO", claimListObj.getString(33));
				jsonObjectw.put("CLAIM_BILL_NO", claimListObj.getString(34));
				jsonObjectw.put("claim_no", claimListObj.getString(35));
				jsonObjectw.put("intra_surgery_photo", claimListObj.getString(36));
				jsonObjectw.put("specimen_removal_photo", claimListObj.getString(37));
				jsonObjectw.put("patient_photo", claimListObj.getString(38));
				jsonObjectw.put("patientphoneno", claimListObj.getString(39));
				jsonObjectw.put("actualdateofadmission", DateFormat.FormatToDateString(claimListObj.getString(40)));
				jsonObjectw.put("actualdateofdischarge", DateFormat.FormatToDateString(claimListObj.getString(41)));
				jsonObjectw.put("verification", claimListObj.getString(42));
				jsonObjectw.put("ispatient", claimListObj.getString(43));
				jsonObjectw.put("Referalstatus", claimListObj.getString(44));
				jsonObjectw.put("packageCode1", claimListObj.getString(45) != null ? claimListObj.getString(45) : "NA");
				jsonObjectw.put("packageName1", claimListObj.getString(46) != null ? claimListObj.getString(46) : "NA");
				jsonObjectw.put("subPackageCode1",
						claimListObj.getString(47) != null ? claimListObj.getString(47) : "NA");
				jsonObjectw.put("subPackageName1",
						claimListObj.getString(48) != null ? claimListObj.getString(48) : "NA");
				jsonObjectw.put("procedureCode1",
						claimListObj.getString(49) != null ? claimListObj.getString(49) : "NA");
				jsonObjectw.put("procedureName1",
						claimListObj.getString(50) != null ? claimListObj.getString(50) : "NA");
				jsonObjectw.put("NOOFDAYSADDMITTEDHOSPITAL",
						CommonFileUpload.totalDaysBetweenDates(claimListObj.getString(28), claimListObj.getString(30)));
				jsonObjectw.put("categoryName", claimListObj.getString(51) != null ? claimListObj.getString(51) : "NA");
				jsonArrays.put(jsonObjectw);
				try {
					StoredProcedureQuery storedProcedureQuery1 = this.entityManager
							.createStoredProcedureQuery("USP_SNO_CLAIM_LOG_DETAILS")
							.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
					storedProcedureQuery1.setParameter("cid", Integer.parseInt(claimListObj.getString(23)));
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
						jsonObject1.put("dateofadmission", DateFormat.FormatToDateString(claimListObj.getString(28)));
						jsonObject1.put("REMARK_ID", snoDetailsObj1.getLong(11));
						jsonObject1.put("Remark", snoDetailsObj1.getString(12));
						jsonObject1.put("additionaldoc2", snoDetailsObj1.getString(13));
						jsonObject1.put("approvedamount", snoDetailsObj1.getString(14));
						jsonObject1.put("intra_surgery_photo", snoDetailsObj1.getString(15));
						jsonObject1.put("specimen_removal_photo", snoDetailsObj1.getString(16));
						jsonObject1.put("patient_photo", snoDetailsObj1.getString(17));
						jsonArrays.put(jsonObject1);
					}
				} catch (Exception e) {
					logger.error(
							"Exception occured in getQueriedClaimDetails method of CpdQueryToHospitalServiceImpl :", e);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured in getQueriedClaimDetails method of CpdQueryToHospitalServiceImpl :", e);
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArrays.toString();
	}

	@Override
	public Response takeActionOnClaimforCPDQuery(String transactiondetailsid, String actualdateofdiscahrge,
			String hospitalCode, MultipartFile additionaldoc1, MultipartFile additionaldoc2, String urnno,
			String dateofadmissio, String ClaimId, String ClaimAmount, String actionby, String dischrageslip,
			String additionaldocs, String PreSurgerypic, String PostSurgerypic, String intrasurgerypic,
			String specimenremoval, String patientpic) throws Exception {
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		String year = dateofadmissio.substring(6, 10);
		Response response = new Response();
		Integer claimraiseInteger = null;
		Map<String, String> filePath = DocUtill.createFile1(urnno.trim(), year.trim(), hospitalCode.trim(),
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
					else if (k.equalsIgnoreCase("Additionaldoc"))
						throw new RuntimeException(
								additionaldoc2.getOriginalFilename() + " Additional Slip2 Failed To Save in Server!");
				}

			}
		});
		try {
			StoredProcedureQuery saveCpdUserData = this.entityManager
					.createStoredProcedureQuery("usp_claim_cpd_query_to_hospact")
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
					.registerStoredProcedureParameter("P_presuergerySlip", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_postsurgerySlip", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IntraSurgery", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SpecimanSurgery", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PatientPic", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTUALDATEOFDISCHARGE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);
			saveCpdUserData.setParameter("p_action", "R");
			saveCpdUserData.setParameter("P_TRANSACTIONDETAILSID", transactiondetailsid);
			for (Map.Entry<String, String> entry : filePath.entrySet()) {
				if (Additionaldoc1.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_AdditonalDoc1", entry.getValue());

				else if (Additionaldoc.contains(entry.getKey()))
					saveCpdUserData.setParameter("p_AdittionalDoc2", entry.getValue());
			}
			saveCpdUserData.setParameter("P_CLAIMID", ClaimId);
			saveCpdUserData.setParameter("P_URN", urnno.trim());
			saveCpdUserData.setParameter("p_USER_IP", getuseripaddressString);
			saveCpdUserData.setParameter("P_ClaimAmount", ClaimAmount);
			saveCpdUserData.setParameter("P_ActionType", "5");
			saveCpdUserData.setParameter("P_ActionBy", actionby);
			saveCpdUserData.setParameter("P_CreatedBy", actionby);
			saveCpdUserData.setParameter("P_StatusFlag", "0");
			saveCpdUserData.setParameter("P_DischargeSlip", dischrageslip);
			saveCpdUserData.setParameter("P_additionaldoc", additionaldocs);
			saveCpdUserData.setParameter("P_presuergerySlip", PreSurgerypic);
			saveCpdUserData.setParameter("P_postsurgerySlip", PostSurgerypic);
			saveCpdUserData.setParameter("P_IntraSurgery", intrasurgerypic);
			saveCpdUserData.setParameter("P_SpecimanSurgery", specimenremoval);
			saveCpdUserData.setParameter("P_PatientPic", patientpic);
			saveCpdUserData.setParameter("P_ACTUALDATEOFDISCHARGE", actualdateofdiscahrge);
			claimraiseInteger = (Integer) saveCpdUserData.getOutputParameterValue("p_msgout");
			if (claimraiseInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Re-Claim Raised Successfully");
			} else if (claimraiseInteger == 2) {
				response.setStatus("Failed");
				response.setMessage("OOPS Something Went Worng,please Try Agian..");
			}
		} catch (Exception e) {
			logger.error("Exception occured in takeActionOnClaimforCPDQuery method of CpdQueryToHospitalServiceImpl :",
					e);
		}
		return response;
	}

}
