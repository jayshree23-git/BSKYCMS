package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.RejectedListBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SystemRejectedBean;
import com.project.bsky.service.ClaimRejectedService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.DaysBetweenDates;

@Service
public class ClaimRejectedServiceImpl implements ClaimRejectedService {

	private final Logger logger;

	@Autowired
	public ClaimRejectedServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Object> getrejectedlistdata(String hospitalcoderejected, String fromDate, String toDate,
			String package1, String packagecode, String uRN,String schemeid,String schemecategoryid) {
		List<Object> claimRejectedList = new ArrayList<Object>();
		if (package1.equals("")) {
			package1 = "";
		}
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Long.parseLong(schemecategoryid);
		} else {
			schemecatId = null;
		}
		ResultSet rejetedlist = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_claim_rejected_list")
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_hospitalcode", hospitalcoderejected.trim());
			storedProcedure.setParameter("p_from_date", fromDate);
			storedProcedure.setParameter("p_to_date", toDate);
			storedProcedure.setParameter("p_packagecode", package1.trim());
			storedProcedure.setParameter("p_packagename", packagecode.trim());
			storedProcedure.setParameter("p_urn", uRN.trim());
			storedProcedure.setParameter("P_SCHEME_ID", Long.parseLong(schemeid));
			storedProcedure.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedure.execute();
			rejetedlist = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (rejetedlist.next()) {
				RejectedListBean rejectedlist = new RejectedListBean();
				rejectedlist.setClaim_raised_by(rejetedlist.getString(1));
				rejectedlist.setId(rejetedlist.getLong(2));
				rejectedlist.setTransactionid(rejetedlist.getString(3));
				rejectedlist.setUrn(rejetedlist.getString(4));
				String dateofadmissionString = rejetedlist.getString(5);
				String q11 = dateofadmissionString.substring(0, 2);
				String q21 = dateofadmissionString.substring(2, 4);
				String q31 = dateofadmissionString.substring(4, 8);
				String q41 = q11 + "/" + q21 + "/" + q31;
				Date condition1 = new SimpleDateFormat("dd/MM/yyyy").parse(q41);
				SimpleDateFormat ss1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d111 = ss1.format(condition1);
				rejectedlist.setDateofadmission(d111);
				rejectedlist.setPatientname(rejetedlist.getString(6));
				rejectedlist.setPackagecode(rejetedlist.getString(7).substring(4));
				rejectedlist.setProcedurename(rejetedlist.getString(8));
				rejectedlist.setPackagename(rejetedlist.getString(9));
				rejectedlist.setCurrenttotalamount(rejetedlist.getString(10));
				String s = rejetedlist.getString(11);
				String s1 = s.substring(0, 2);
				String s2 = s.substring(2, 4);
				String s3 = s.substring(4, 8);
				String s4 = s1 + "/" + s2 + "/" + s3;
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf.format(date1);
				rejectedlist.setDateofdischarge(d);
				rejectedlist.setUserid(rejetedlist.getString(12));
				rejectedlist.setHospitalstatecode(rejetedlist.getString(13));
				rejectedlist.setTransactiondetailsid(rejetedlist.getString(14));
				rejectedlist.setHospitalcode(rejetedlist.getString(15));
				rejectedlist.setInvoiceno(rejetedlist.getString(16));
				rejectedlist.setAuthorizedCode(rejetedlist.getString(17));
				String actualdateofDischar = rejetedlist.getString(18);
				String s11 = actualdateofDischar.substring(0, 2);
				String s21 = actualdateofDischar.substring(2, 4);
				String s31 = actualdateofDischar.substring(4, 8);
				String s41 = s11 + "/" + s21 + "/" + s31;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(s41);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf1.format(date11);
				rejectedlist.setActualdateofdischarge(d1);
				rejectedlist.setNonUploadFlag(rejetedlist.getString(19));
				if (rejetedlist.getString(20) != null && rejetedlist.getString(20) != "") {
					rejectedlist.setBtnVisibleBy(rejetedlist.getString(20));
					String getdat = rejetedlist.getString(20);
					Date date5 = new Date(getdat);
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					String strDate = formatter.format(date5);
					int days = DaysBetweenDates.daysCountBetweenDates(formatter.parse(strDate));
					rejectedlist.setDaysRemaining(String.valueOf(days) + " days left");
					rejectedlist.setDays(String.valueOf(days));
				}
				String actualdateofadmissionString = rejetedlist.getString(21);
				String q1 = actualdateofadmissionString.substring(0, 2);
				String q2 = actualdateofadmissionString.substring(2, 4);
				String q3 = actualdateofadmissionString.substring(4, 8);
				String q4 = q1 + "/" + q2 + "/" + q3;
				Date condition = new SimpleDateFormat("dd/MM/yyyy").parse(q4);
				SimpleDateFormat ss = new SimpleDateFormat("dd-MMM-yyyy");
				String d11 = ss.format(condition);
				rejectedlist.setActualdateofadmission(d11);
				rejectedlist.setCaseno(rejetedlist.getString(22));
				claimRejectedList.add(rejectedlist);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getrejectedlistdata method of ClaimRejectedServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (rejetedlist != null) {
					rejetedlist.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimRejectedList;
	}

	@Override
	public Response saveRejectRequest(SystemRejectedBean rejBean) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date varDate = dateFormat.parse(rejBean.getClaimBy());
		Response response = new Response();
		try {

			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SYSREJ_DISCHARGE_ACT")
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
			storedProcedureQuery.setParameter("p_claim_by", varDate);
			storedProcedureQuery.setParameter("p_claim_status", rejBean.getClaimstatus());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Requested Successfully");
			}
		} catch (Exception e) {
			logger.error("Exception occured in saveRejectRequest method of ClaimRejectedServiceImpl :", e);
		}

		return response;
	}
	
	@Override
	public String getByDetailId(Integer txnId) throws Exception {
		JSONArray preAuthHist = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		String urn = null;
		String authorizedCode = null;
		String hospitalCode = null;
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_rejected_details")
					.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_id", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(15);
				authorizedCode = snoDetailsObj.getString(21).substring(2);
				urn = snoDetailsObj.getString(2);
				jsonObject = new JSONObject();
				jsonObject.put("ID", snoDetailsObj.getLong(1));
				jsonObject.put("URN", snoDetailsObj.getString(2));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(3));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(4)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(5)));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(6));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(7));
				jsonObject.put("PACKAGECODE", snoDetailsObj.getString(8));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(9));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(10));
				jsonObject.put("CURRENTTOTALAMOUNT", snoDetailsObj.getString(11));
				jsonObject.put("GENDER", snoDetailsObj.getString(12));
				jsonObject.put("AGE", snoDetailsObj.getString(13));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(14));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(15));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(16)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(17)));
				jsonObject.put("TRANSACTIONDETAILSID", snoDetailsObj.getLong(18));
				jsonObject.put("ADDRESS", snoDetailsObj.getString(19));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(16), snoDetailsObj.getString(17)));
				String mob = snoDetailsObj.getString(20);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(21));
				jsonObject.put("CATEGORYNAME", snoDetailsObj.getString(22));
				details.put("actionData", jsonObject);
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				details.put("preAuthHist", preAuthHist);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getByDetailId method of ClaimRejectedServiceImpl :", e);
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
		return details.toString();
	}
}
