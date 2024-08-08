/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.project.bsky.bean.BulkDateExtensionBean;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.CpdQueryToHospitalBean;
import com.project.bsky.bean.DateExtensionBean;
import com.project.bsky.bean.Dischargereportbean;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.bean.NonComplianceBean;
import com.project.bsky.bean.NonComplianceCount;
import com.project.bsky.bean.NonComplianceData;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaunprocessedBean;
import com.project.bsky.bean.Snawiserununprocessedupdate;
import com.project.bsky.bean.Snawiseunprocessedbean;
import com.project.bsky.bean.SnoClaimDetails;
import com.project.bsky.bean.SystemRejQueryCpdBean;
import com.project.bsky.bean.Treatmenthistorybeandetails;
import com.project.bsky.bean.UnprocessedCountData;
import com.project.bsky.service.CpdSystemRejectedListService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.DaysBetweenDates;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 * @author hrusikesh.mohanty
 *
 */
@SuppressWarnings({ "deprecation", "unused" })
@Service
public class CpdSystemRejectedListServiceImpl implements CpdSystemRejectedListService {

	private final Logger logger;

	@Autowired
	public CpdSystemRejectedListServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Environment env;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;

	@Autowired
	private SnoClaimProcessingDetailsImpl snoClaimProcessingDetailsImpl;

	@Override
	public List<CpdQueryToHospitalBean> getRejetedData(String hospitalCode, String fromDate, String toDate,
			String package1, String packageCodedata, String uRN, String schemeid, String schemecategoryid) {
		List<CpdQueryToHospitalBean> claimRaiseDetailsList = new ArrayList<CpdQueryToHospitalBean>();
		if (package1.equals("")) {
			package1 = "";
		}
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Long.parseLong(schemecategoryid);
		} else {
			schemecatId = null;
		}
		ResultSet deptDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPDSYS_REJECTED_LIST")
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
				String a1 = dateofadmissionString.substring(0, 2);
				String b1 = dateofadmissionString.substring(2, 4);
				String c1 = dateofadmissionString.substring(4, 8);
				String d1 = a1 + "/" + b1 + "/" + c1;
				Date e1 = new SimpleDateFormat("dd/MM/yyyy").parse(d1);
				SimpleDateFormat f1 = new SimpleDateFormat("dd-MMM-yyyy");
				String g1 = f1.format(e1);
				resBean.setDateofadmission(g1);
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
				String actualdateofadmissionString = deptDetailsObj.getString(11);
				String a11 = actualdateofadmissionString.substring(0, 2);
				String b11 = actualdateofadmissionString.substring(2, 4);
				String c11 = actualdateofadmissionString.substring(4, 8);
				String d11d = a11 + "/" + b11 + "/" + c11;
				Date e11 = new SimpleDateFormat("dd/MM/yyyy").parse(d11d);
				SimpleDateFormat f11 = new SimpleDateFormat("dd-MMM-yyyy");
				String g11 = f11.format(e11);
				resBean.setActualdateofadmission(g11);
				String Actualdateofdischarge = deptDetailsObj.getString(12);
				String a111 = Actualdateofdischarge.substring(0, 2);
				String b111 = Actualdateofdischarge.substring(2, 4);
				String c111 = Actualdateofdischarge.substring(4, 8);
				String d111 = a111 + "/" + b111 + "/" + c111;
				Date e111 = new SimpleDateFormat("dd/MM/yyyy").parse(d111);
				SimpleDateFormat f111 = new SimpleDateFormat("dd-MMM-yyyy");
				String g111 = f111.format(e111);
				resBean.setActualdateofdischarge(g111);
				resBean.setTransactiondetailsid(deptDetailsObj.getString(13));
				resBean.setInvoiceno(deptDetailsObj.getString(14));
				String dateofdischargeString = deptDetailsObj.getString(15);
				String q1 = dateofdischargeString.substring(0, 2);
				String q2 = dateofdischargeString.substring(2, 4);
				String q3 = dateofdischargeString.substring(4, 8);
				String q4 = q1 + "/" + q2 + "/" + q3;
				Date condition = new SimpleDateFormat("dd/MM/yyyy").parse(q4);
				SimpleDateFormat ss = new SimpleDateFormat("dd-MMM-yyyy");
				String d11 = ss.format(condition);
				resBean.setDateofdischarge(d11);
				resBean.setCaseno(deptDetailsObj.getString(16));
				claimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getRejetedData method of CpdSystemRejectedListServiceImpl :", e);
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
	public String getRequestByDetailId(Integer txnId) throws Exception {
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
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_cpdsys_rejected_dtls")
					.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_log_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_id", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_log_msgout");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(17);
				authorizedCode = snoDetailsObj.getString(26).substring(2);
				urn = snoDetailsObj.getString(2);
				jsonObject = new JSONObject();
				jsonObject.put("CLAIMID", snoDetailsObj.getLong(1));
				jsonObject.put("URN", snoDetailsObj.getString(2));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(3));
				jsonObject.put("PACKAGEID", snoDetailsObj.getString(4));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(5));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(6));
				jsonObject.put("PACKAGECODE", snoDetailsObj.getString(7));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(8));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(9));
				jsonObject.put("PACKAGECATAGORYCODE", snoDetailsObj.getString(10));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(11));
				jsonObject.put("CURRENTTOTALAMOUNT", snoDetailsObj.getString(12));
				jsonObject.put("GENDER", snoDetailsObj.getString(13));
				jsonObject.put("AGE", snoDetailsObj.getString(14));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(15));
				jsonObject.put("CPDACTIONDATE", snoDetailsObj.getTimestamp(16));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(17));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(18)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(19)));
				jsonObject.put("TRANSACTIONDETAILSID", snoDetailsObj.getLong(20));
				jsonObject.put("CLAIM_CASE_NO", snoDetailsObj.getString(21));
				jsonObject.put("CLAIM_BILL_NO", snoDetailsObj.getString(22));
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(23));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(18), snoDetailsObj.getString(19)));
				String mob = snoDetailsObj.getString(24);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("ADDRESS", snoDetailsObj.getString(25));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(26));
				jsonObject.put("verification", snoDetailsObj.getString(27));
				jsonObject.put("ispatient", snoDetailsObj.getString(28));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(29));
				jsonObject.put("categoryName", snoDetailsObj.getString(30));
				details.put("actionData", jsonObject);
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
					jsonObject1.put("dateofadmission", DateFormat.FormatToDateString(snoDetailsObj.getString(18)));
					jsonObject1.put("REMARK_ID", snoDetailsObj1.getLong(11));
					jsonObject1.put("Remark", snoDetailsObj1.getString(12));
					jsonObject1.put("additionaldoc2", snoDetailsObj1.getString(13));
					jsonObject1.put("approvedamount", snoDetailsObj1.getString(14));
					jsonObject1.put("intra_surgery_photo", snoDetailsObj1.getString(15));
					jsonObject1.put("specimen_removal_photo", snoDetailsObj1.getString(16));
					jsonObject1.put("patient_photo", snoDetailsObj1.getString(17));
					jsonArray.put(jsonObject1);
				}
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				details.put("actionLog", jsonArray);
				details.put("preAuthHist", preAuthHist);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getRequestByDetailId method of CpdSystemRejectedListServiceImpl :", e);
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public Response saveRejectRequestCPD(SystemRejQueryCpdBean rejBean) throws ParseException {
		Response response = new Response();
		try {
			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SYSREJ_DISCHARGE_CPD_ACT")
					.registerStoredProcedureParameter("p_transactiondetailsId", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statusflag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_assignedsna", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_createdby", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_createdon", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_by", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_status", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_transactiondetailsId", rejBean.getTransactiondetailsid());
			storedProcedureQuery.setParameter("p_urn", rejBean.getUrnNo().trim());
			storedProcedureQuery.setParameter("p_hospitalcode", rejBean.getHospitalcode());
			storedProcedureQuery.setParameter("p_statusflag", rejBean.getStatusflag());
			storedProcedureQuery.setParameter("p_assignedsna", Integer.parseInt("1914"));
			storedProcedureQuery.setParameter("p_description", rejBean.getDescription());
			storedProcedureQuery.setParameter("p_createdby", rejBean.getUserId());
			storedProcedureQuery.setParameter("p_createdon", new Date());
			storedProcedureQuery.setParameter("p_claim_by", rejBean.getClaimBy());
			storedProcedureQuery.setParameter("p_claim_status", rejBean.getClaimstatus());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Requested Successfully");
			}
		} catch (Exception e) {
			logger.error("Exception occured in saveRejectRequestCPD method of CpdSystemRejectedListServiceImpl :", e);
		}
		return response;
	}

	@Override
	public List<Object> getRejetedDataCPDToSNA(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_non_complqry_cpd_lst_test")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IMPLANT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HIGH_END_DRUGS_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_WARDNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("P_REMARKS", requestBean.getDescription());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_PROCEDURE_CODE", requestBean.getProcedure());
			storedProcedureQuery.setParameter("P_PACKAGE_CODE", requestBean.getPackages());
			storedProcedureQuery.setParameter("P_IMPLANT_CODE", requestBean.getImplant());
			storedProcedureQuery.setParameter("P_HIGH_END_DRUGS_CODE", requestBean.getHighend());
			storedProcedureQuery.setParameter("P_WARDNAME", requestBean.getWard());
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", requestBean.getTrigger());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDetailsObj.next()) {
				SnoClaimDetails resBean = new SnoClaimDetails();
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(1));
				resBean.setClaimid(snoDetailsObj.getLong(2));
				resBean.setURN(snoDetailsObj.getString(3));
				resBean.setPatientName(snoDetailsObj.getString(4));
				resBean.setInvoiceNumber(snoDetailsObj.getString(5));
				resBean.setCreatedOn(DateFormat.dateConvertor(snoDetailsObj.getString(6), "time"));
				resBean.setCpdAlotteddate(snoDetailsObj.getTimestamp(7));
				resBean.setPackageName(snoDetailsObj.getString(8));
				resBean.setRevisedDate(snoDetailsObj.getTimestamp(9));
				resBean.setPackageCode(snoDetailsObj.getString(10));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(11));
				resBean.setClaimNo(snoDetailsObj.getString(12));
				resBean.setCpdApprovedAmount(snoDetailsObj.getString(13));
				resBean.setHospitalName(snoDetailsObj.getString(14));
				resBean.setMortality(snoDetailsObj.getString(15));
				resBean.setHospitalMortality(snoDetailsObj.getString(16));
				if (snoDetailsObj.getString(17) != null && snoDetailsObj.getString(17) != "") {
					resBean.setActualDateOfAdmission(DateFormat.dateConvertor(snoDetailsObj.getString(17), ""));
				}
				if (snoDetailsObj.getString(18) != null && snoDetailsObj.getString(18) != "") {
					resBean.setActualDateOfDischarge(DateFormat.dateConvertor(snoDetailsObj.getString(18), ""));
				}
				resBean.setHospitalCode(snoDetailsObj.getString(19));
				resBean.setPhone(snoDetailsObj.getString(20) == null ? "N/A" : snoDetailsObj.getString(20));
				resBean.setVerificationMode(snoDetailsObj.getLong(21));
				resBean.setTxnpackagedetailid(snoDetailsObj.getLong(22));
				resBean.setTriggerValue(snoDetailsObj.getLong(23));
				resBean.setTriggerMsg(snoDetailsObj.getString(24));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getRejetedDataCPDToSNA method of CpdSystemRejectedListServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public Response saveClaimSNANonComplianceDetails(ClaimLogBean logBean) throws Exception {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : logBean.getIcdFinalData()) {
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
			localhost = InetAddress.getLocalHost();
			getuseripaddressString = localhost.getHostAddress();
		} catch (UnknownHostException e1) {
			logger.error(ExceptionUtils.getStackTrace(e1));
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_NON_COMPLQRY_CPD_ACT")
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
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_MORTALITY", String.class, ParameterMode.IN)
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
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", logBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", logBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", logBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", logBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", logBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", logBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", logBean.getIntrasurgery());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", logBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.setParameter("P_SNA_MORTALITY", logBean.getSnamortality());
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
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 6) {
				response.setStatus("Success");
				response.setMessage("Claim Investigated Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 8) {
				response.setStatus("Success");
				response.setMessage("Claim Reverted Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 13) {
				response.setStatus("Success");
				response.setMessage("Claim On Hold");
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
	public String getNonComplianceClaimListById(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONArray cardBalanceArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_NONCOMPL_QRY_CPD_DTS")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				casenumber = snoDetailsObj.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
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
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
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
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
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
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(53));
				jsonObject.put("verification", snoDetailsObj.getString(54));
				jsonObject.put("ispatient", snoDetailsObj.getString(55));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(56));
				jsonObject.put("PackageCode", snoDetailsObj.getString(57));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(58) != null ? snoDetailsObj.getString(58) : "NA");
				jsonObject.put("packageName1",
						snoDetailsObj.getString(59) != null ? snoDetailsObj.getString(59) : "NA");
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(60) != null ? snoDetailsObj.getString(60) : "NA");
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(61) != null ? snoDetailsObj.getString(61) : "NA");
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(62) != null ? snoDetailsObj.getString(62) : "NA");
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(63) != null ? snoDetailsObj.getString(63) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(64));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(65), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(66));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(67));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(68));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(69));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(70));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(71));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(72));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(73));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(74));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(75));
				jsonObject.put("disverification", snoDetailsObj.getString(76));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(77));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "N/A");
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(79) != null ? snoDetailsObj.getString(79) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(81) != null ? snoDetailsObj.getString(81) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(82) != null ? snoDetailsObj.getString(82) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(83));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(84));
				jsonObject.put("categoryName", snoDetailsObj.getString(85));
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
				while (snoDetailsObj2.next()) {
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
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
				packageBlock = packageBlocking.getMultiplePackageBlocking(urn, actualDate);
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = snoClaimProcessingDetailsImpl.getMultiplePackageBlockingthroughcaseno(casenumber,
						actualDate);
				cardBalanceArray = snoClaimProcessingDetailsImpl.getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceArray);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getUSerDetailsDAta method of CpdSystemRejectedListServiceImpl :", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null)
					snoDetailsObj.close();
				if (snoDetailsObj1 != null)
					snoDetailsObj1.close();
				if (snoDetailsObj2 != null)
					snoDetailsObj2.close();
				if (snoDetailsObj3 != null)
					snoDetailsObj3.close();
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();

			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details.toString();
	}

	@Override
	public List<Object> getNonComplianceExtn(NonComplianceBean requestBean) throws ParseException {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date FromDate = dateFormat.parse(requestBean.getFromDate());
		Date toDate = dateFormat.parse(requestBean.getToDate());
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_NonCompliance_exnt")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", FromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_flag", Integer.parseInt(requestBean.getActionId()));
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				NonComplianceData resBean = new NonComplianceData();
				resBean.setClaimRaisedBy(snoDetailsObj.getTimestamp(1));
				resBean.setLastclaimby(new SimpleDateFormat("dd-MMM-yyyy").format(snoDetailsObj.getDate(1)));
				resBean.setURN(snoDetailsObj.getString(2));
				resBean.setPatientName(snoDetailsObj.getString(3));
				resBean.setPackageCode(snoDetailsObj.getString(4));
				resBean.setPackageName(snoDetailsObj.getString(5));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(6));
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(7));
				resBean.setHospitalCode(snoDetailsObj.getString(8));
				resBean.setActualDateOfDischarge(DateFormat.FormatToDateString(snoDetailsObj.getString(9)));
				resBean.setActualDateOfAdmission(DateFormat.FormatToDateString(snoDetailsObj.getString(10)));
				resBean.setHname(snoDetailsObj.getString(11));
				resBean.setStatename(snoDetailsObj.getString(12));
				resBean.setDistname(snoDetailsObj.getString(13));
				resBean.setClaimno(snoDetailsObj.getString(14));
				resBean.setCheck(false);
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getUSerDetailsDAta method of CpdSystemRejectedListServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public Response saveNonComplianceDateExtension(DateExtensionBean logBean) {
		Response response = new Response();
		Connection con = null;
		CallableStatement st = null;
		Integer claimsnoInteger = null;
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TRANSACTION_DETAILS_ID", con);
			ARRAY array_to_pass = new ARRAY(des, con, logBean.getTransactionDetailsId());
			st = con.prepareCall("call usp_NonCompliance_exnt_update(?,?,?,?,?)");
			st.setArray(1, array_to_pass);
			st.setInt(2, Integer.parseInt(logBean.getActionId()));
			st.setString(3, logBean.getClaimBy());
			st.setInt(4, logBean.getCreatedBy());
			st.registerOutParameter(5, Types.INTEGER);
			st.execute();
			claimsnoInteger = ((OracleCallableStatement) st).getInt(5);
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
			} else {
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error("Exception occured in getUSerDetailsDAta method of CpdSystemRejectedListServiceImpl :", e);
		}
		return response;
	}

	@Override
	public List<Object> getRejetedDataSNAToSNA(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();

		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_NON_COMPLQRY_SNA_LST_TEST")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IMPLANT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HIGH_END_DRUGS_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_WARDNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("P_REMARKS", requestBean.getDescription());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_PROCEDURE_CODE", requestBean.getProcedure());
			storedProcedureQuery.setParameter("P_PACKAGE_CODE", requestBean.getPackages());
			storedProcedureQuery.setParameter("P_IMPLANT_CODE", requestBean.getImplant());
			storedProcedureQuery.setParameter("P_HIGH_END_DRUGS_CODE", requestBean.getHighend());
			storedProcedureQuery.setParameter("P_WARDNAME", requestBean.getWard());
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", requestBean.getTrigger());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDetailsObj.next()) {
				SnoClaimDetails resBean = new SnoClaimDetails();
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(1));
				resBean.setClaimid(snoDetailsObj.getLong(2));
				resBean.setURN(snoDetailsObj.getString(3));
				resBean.setPatientName(snoDetailsObj.getString(4));
				resBean.setInvoiceNumber(snoDetailsObj.getString(5));
				resBean.setCreatedOn(DateFormat.dateConvertor(snoDetailsObj.getString(6), "time"));
				resBean.setCpdAlotteddate(snoDetailsObj.getTimestamp(7));
				resBean.setPackageName(snoDetailsObj.getString(8));
				resBean.setRevisedDate(snoDetailsObj.getTimestamp(9));
				resBean.setPackageCode(snoDetailsObj.getString(10));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(11));
				resBean.setClaimNo(snoDetailsObj.getString(12));
				resBean.setCpdApprovedAmount(snoDetailsObj.getString(13));
				resBean.setHospitalName(snoDetailsObj.getString(14));
				resBean.setMortality(snoDetailsObj.getString(15));
				resBean.setHospitalMortality(snoDetailsObj.getString(16));
				resBean.setActualDateOfAdmission(DateFormat.dateConvertor(snoDetailsObj.getString(17), ""));
				resBean.setActualDateOfDischarge(DateFormat.dateConvertor(snoDetailsObj.getString(18), ""));
				resBean.setHospitalCode(snoDetailsObj.getString(19));
				resBean.setPhone(snoDetailsObj.getString(20) == null ? "N/A" : snoDetailsObj.getString(20));
				resBean.setTriggerValue(snoDetailsObj.getLong(22));
				resBean.setTriggerMsg(snoDetailsObj.getString(23));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getRejetedDataSNAToSNA method of CpdSystemRejectedListServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public List<Object> getUnprocessedCountData(NonComplianceBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITALWISE_PENDING_CLAIM_REPORT")
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_FROMDATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TODATE", requestBean.getToDate());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (snoDetailsObj.next()) {
				UnprocessedCountData resBean = new UnprocessedCountData();
				resBean.setDischarge(snoDetailsObj.getInt(1));
				resBean.setClaim(snoDetailsObj.getInt(2));
				resBean.setFreshCpd(snoDetailsObj.getInt(3));
				resBean.setCpdResettlement(snoDetailsObj.getInt(4));
				resBean.setHosPending(snoDetailsObj.getInt(5));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in saveNonComplianceDateExtension method of CpdSystemRejectedListServiceImpl :",
					e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public Response RunUnprocessed(NonComplianceBean requestBean) throws Exception {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_UNPROCESSED_CLAIM_MANUAL")
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_FROMDATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TODATE", requestBean.getToDate());
			storedProcedureQuery.execute();
			String bulkapp = (String) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (bulkapp.equalsIgnoreCase("SUCESS")) {
				response.setStatus("Success");
				response.setMessage("Unprocessed Run Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("OOPS Something Went Wrong, please Try Again..");
			}
		} catch (Exception e) {
			logger.error("Exception occured in RunUnprocessed method of CpdSystemRejectedListServiceImpl :", e);
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public List<Object> getBulkNonComplianceExtn(NonComplianceBean requestBean) throws ParseException {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date FromDate = dateFormat.parse(requestBean.getFromDate());
		Date toDate = dateFormat.parse(requestBean.getToDate());
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_bulk_noncompliance_exnt")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", FromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_flag", Integer.parseInt(requestBean.getActionId()));
			storedProcedureQuery.setParameter("p_user_id", requestBean.getSnoid());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				NonComplianceCount resBean = new NonComplianceCount();
				resBean.setCount(snoDetailsObj.getLong(1));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getBulkNonComplianceExtn method of CpdSystemRejectedListServiceImpl :",
					e);
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
	public Response saveBulkNonComplianceDateExtension(BulkDateExtensionBean requestBean) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date FromDate = dateFormat.parse(requestBean.getFromDate());
		Date toDate = dateFormat.parse(requestBean.getToDate());
		Response response = new Response();
		try {

			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_bulk_noncompliance_exnt_insert")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statusflag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_by", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_created_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_message", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_from_date", FromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_flag", Integer.parseInt(requestBean.getActionId()));
			storedProcedureQuery.setParameter("p_user_id", requestBean.getSnoid());
			storedProcedureQuery.setParameter("p_statusflag", requestBean.getStatusflag());
			storedProcedureQuery.setParameter("p_claim_by", requestBean.getClaimBy());
			storedProcedureQuery.setParameter("p_created_by", requestBean.getCreatedBy());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_message");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Date Extended Successfully");
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in saveBulkNonComplianceDateExtension method of CpdSystemRejectedListServiceImpl :",
					e);
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<Object> getSnawiseunprocessedcountdetails(Snawiseunprocessedbean requestBean) {
		List<Object> runsnawisecountdetatails = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNAWISE_UNPROCESSEDCLAIM_DETAILS")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Action_Type", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKDATA", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", requestBean.getSnoid());
			storedProcedureQuery.setParameter("P_FROMDATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_Action_Type", requestBean.getSearchby());
			storedProcedureQuery.setParameter("P_BLOCKDATA", requestBean.getSearchtype());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (snoDetailsObj.next()) {
				SnaunprocessedBean resBean = new SnaunprocessedBean();
				resBean.setClaimid(snoDetailsObj.getLong(1));
				resBean.setClaimnumber(snoDetailsObj.getString(2));
				resBean.setUrnnumber(snoDetailsObj.getString(3));
				resBean.setHospitalcode(snoDetailsObj.getString(4));
				resBean.setHospitalname(snoDetailsObj.getString(5));
				resBean.setActialdateofdischarge(DateFormat.FormatToDateString(snoDetailsObj.getString(6)));
				resBean.setActualdateofadmission(DateFormat.FormatToDateString(snoDetailsObj.getString(7)));
				resBean.setCaseno(snoDetailsObj.getString(8) != null ? snoDetailsObj.getString(8) : "N/A");
				resBean.setPackagecode(snoDetailsObj.getString(9));
				resBean.setPackagename(snoDetailsObj.getString(10));
				resBean.setCpdactiondate(snoDetailsObj.getString(11) != null ? snoDetailsObj.getString(11) : "N/A");
				resBean.setPatientname(snoDetailsObj.getString(12));
				resBean.setTransactiondetailsid(snoDetailsObj.getString(13));
				resBean.setClaimraisedby(snoDetailsObj.getString(14));
				runsnawisecountdetatails.add(resBean);
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in saveNonComplianceDateExtension method of CpdSystemRejectedListServiceImpl :",
					e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return runsnawisecountdetatails;
	}

	@Override
	public Response getSnawiseunprocesseupdatet(Snawiserununprocessedupdate snawiseunprocessed) {
		Response response = new Response();
		String claimList = null;
		String returnObj = null;
		Blob blob = null;
		StringBuffer bufferlist = new StringBuffer();
		if (snawiseunprocessed.getClaimid() != null) {
			for (Long element : snawiseunprocessed.getClaimid()) {
				bufferlist.append(element.toString() + ",");
			}
			claimList = bufferlist.substring(0, bufferlist.length() - 1);
		}
		try {
			if (claimList != null) {
				blob = new SerialBlob(claimList.getBytes());
			}
		} catch (Exception e1) {
			logger.error(ExceptionUtils.getStackTrace(e1));
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNAWISEUNPROCESSED_UPDATE")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMID_DETAILS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Actiontype", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_USERID", snawiseunprocessed.getUserId());
			storedProcedureQuery.setParameter("P_CLAIMID_DETAILS", claimList);
			storedProcedureQuery.setParameter("P_Actiontype", snawiseunprocessed.getSearchby());
			storedProcedureQuery.execute();
			String returnValue = (String) storedProcedureQuery.getOutputParameterValue("P_MSG");
			if (returnValue.equalsIgnoreCase("SUCESS")) {
				response.setStatus("success");
				response.setMessage("SNA Wise Unprocessed Claim Run Successfully");
			} else if (returnValue.equalsIgnoreCase("FAIL")) {
				response.setStatus("Failed");
				response.setMessage("Some error happen");
			}
		} catch (Exception e) {
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		}
		return response;
	}

	@Override
	public List<Object> getNonComplianceExtensionview(Integer action, Long userid) {
		List<Object> object = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_NONCOMPLIANCE_EXTN_VIEW_RPT")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_msg_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", action);
			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_msg_OUT");
			while (rs.next()) {
				NonComplianceData resBean = new NonComplianceData();
				resBean.setURN(rs.getString(1));
				resBean.setLastclaimby(rs.getString(2));
				resBean.setCurrentclaimby(rs.getString(3));
				resBean.setExtendedon(rs.getString(4));
				resBean.setExtendedby(rs.getString(5));
				resBean.setPatientName(rs.getString(6));
				resBean.setCurrentTotalAmount(rs.getString(7));
				resBean.setPackageCode(rs.getString(8));
				resBean.setPackageName(rs.getString(9));
				resBean.setActualDateOfDischarge(rs.getString(10));
				resBean.setActualDateOfAdmission(rs.getString(11));
				resBean.setHcode(rs.getString(12));
				resBean.setHname(rs.getString(13) + " (" + rs.getString(12) + ")");
				object.add(resBean);
			}
		} catch (Exception e) {
			logger.error("some error hapen :- " + e);
		}
		return object;
	}

	@Override
	public List<Object> getunprocessedsummarydetails(Date fromdate, Date todate, String state, String state2,
			String dist, String hospital, Integer flag) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_UNPROCESSED_SUMMARY_RPT")
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATE_CODE", state);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", dist);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospital);
			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_ACTIONCODE", flag);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (snoDetailsObj.next()) {
				Treatmenthistorybeandetails bean = new Treatmenthistorybeandetails();
				bean.setUrn(snoDetailsObj.getString(3));
				bean.setCaseno(snoDetailsObj.getString(4));
				bean.setPatient(snoDetailsObj.getString(5));
				bean.setPhcode(snoDetailsObj.getString(6));
				bean.setPhname(snoDetailsObj.getString(7));
				bean.setClaimedamount(snoDetailsObj.getString(8));
				bean.setAdateadd(snoDetailsObj.getString(9));
				bean.setAdatedis(snoDetailsObj.getString(10));
				bean.setHcode(snoDetailsObj.getString(11));
				bean.setHname(snoDetailsObj.getString(12));
				SnoclaimRaiseDetailsList.add(bean);
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in saveNonComplianceDateExtension method of CpdSystemRejectedListServiceImpl :",
					e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return SnoclaimRaiseDetailsList;
	}
}
