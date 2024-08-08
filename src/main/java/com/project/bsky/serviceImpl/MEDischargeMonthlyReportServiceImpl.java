/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

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

import com.project.bsky.bean.MEDischargeReportBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.MEDischargeMonthlyReportService;
import com.project.bsky.util.METriggerUtil;

/**
 * @author priyanka.singh
 *
 */
@Service
public class MEDischargeMonthlyReportServiceImpl implements MEDischargeMonthlyReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public MEDischargeReportBean MEDischargeMonthly(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode, Integer serchtype) {
		MEDischargeReportBean bean = new MEDischargeReportBean();

		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_MONTH_WISE_DISCHARGE_COUNT_FOR_ME")
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_USERID", userId);
			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			storedProcedure.setParameter("P_FROM_DATE", d);
			Date d1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			storedProcedure.setParameter("P_TO_DATE", d1);
			if (stateId == null) {
				storedProcedure.setParameter("p_statecode", null);
			} else {
				storedProcedure.setParameter("p_statecode", stateId);
			}
			if (districtId == null) {
				storedProcedure.setParameter("p_districtcode", null);
			} else {
				storedProcedure.setParameter("p_districtcode", districtId);
			}
			if (hospitalCode == null) {
				storedProcedure.setParameter("p_hosptlcode", null);
			} else {
				storedProcedure.setParameter("p_hosptlcode", hospitalCode);
			}
			storedProcedure.setParameter("P_ACTION_CODE", serchtype);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (list.next()) {
				Long l = list.getLong(1);
				bean.setCountData(l.toString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return bean;
	}

	@Override
	public String getMonthWiseDischargeDetaiMe(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode, Integer serchtype, String Package, String packageName) {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_MONTH_WISE_DISCHARGE_LIST_FOR_ME")
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECATEGORYCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_USERID", userId);
			storedProcedure.setParameter("P_FROM_DATE", fromdate);
			storedProcedure.setParameter("P_TO_DATE", todate);
			if (stateId == null) {
				storedProcedure.setParameter("p_statecode", null);
			} else {
				storedProcedure.setParameter("p_statecode", stateId);
			}
			if (districtId == null) {
				storedProcedure.setParameter("p_districtcode", null);
			} else {
				storedProcedure.setParameter("p_districtcode", districtId);
			}
			if (hospitalCode == null) {
				storedProcedure.setParameter("p_hosptlcode", null);
			} else {
				storedProcedure.setParameter("p_hosptlcode", hospitalCode);
			}
			if (Package == null) {
				storedProcedure.setParameter("P_PACKAGECATEGORYCODE", null);
			} else {
				storedProcedure.setParameter("P_PACKAGECATEGORYCODE", Package);
			}
			if (packageName == null) {
				storedProcedure.setParameter("P_PACKAGECODE", null);
			} else {
				storedProcedure.setParameter("P_PACKAGECODE", packageName);
			}
			storedProcedure.setParameter("P_ACTION_CODE", serchtype);
			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
				if (serchtype == 1) {
					jsonObject.put("hospitalCode", DisDetailsObj.getString(1));
					jsonObject.put("hospitalName", DisDetailsObj.getString(2));
					jsonObject.put("hosstateName", DisDetailsObj.getString(3));
					jsonObject.put("hosdistrictName", DisDetailsObj.getString(4));
					jsonObject.put("claimid", DisDetailsObj.getString(5));
					jsonObject.put("invoiceNo", DisDetailsObj.getString(6));
					jsonObject.put("urn", DisDetailsObj.getString(7));
					jsonObject.put("patientNo", DisDetailsObj.getString(8));
					jsonObject.put("patientName", DisDetailsObj.getString(9));
					jsonObject.put("gender", DisDetailsObj.getString(10));
					jsonObject.put("age", DisDetailsObj.getString(11));
					jsonObject.put("familyHeader", DisDetailsObj.getString(12));
					jsonObject.put("varyfyName", DisDetailsObj.getString(13));
					jsonObject.put("pStateCode", DisDetailsObj.getString(14));
					jsonObject.put("pdistrictCode", DisDetailsObj.getString(15));
					jsonObject.put("pBlockCode", DisDetailsObj.getString(16));
					jsonObject.put("pPanchaytCode", DisDetailsObj.getString(17));
					jsonObject.put("villageCode", DisDetailsObj.getString(18));
					jsonObject.put("pStateName", DisDetailsObj.getString(19));
					jsonObject.put("pDistName", DisDetailsObj.getString(20));
					jsonObject.put("pBlockName", DisDetailsObj.getString(21));
					jsonObject.put("pPanchytName", DisDetailsObj.getString(22));
					jsonObject.put("pVilName", DisDetailsObj.getString(23));
					jsonObject.put("fVaryfier", DisDetailsObj.getString(24));
					jsonObject.put("discoverCode", DisDetailsObj.getString(25));
					jsonObject.put("fVaryFierName", DisDetailsObj.getString(26));
					jsonObject.put("transactionType", DisDetailsObj.getString(27));
					jsonObject.put("transactionDate", DisDetailsObj.getString(28));
					jsonObject.put("transactionTime", DisDetailsObj.getString(29));
					jsonObject.put("packageCode", DisDetailsObj.getString(30));
					jsonObject.put("fundBalance", DisDetailsObj.getString(31));
					jsonObject.put("noOfDays", DisDetailsObj.getString(32));
					jsonObject.put("dateOfAdmission", DisDetailsObj.getString(33));
					jsonObject.put("dateOfDischarge", DisDetailsObj.getString(34));
					jsonObject.put("mortality", DisDetailsObj.getString(35));
					jsonObject.put("transactionDescription", DisDetailsObj.getString(36));
					jsonObject.put("unblockAmt", DisDetailsObj.getString(37));
					jsonObject.put("policyStartDate", DisDetailsObj.getString(38));
					jsonObject.put("policyEndDate", DisDetailsObj.getString(39));
					jsonObject.put("procedureName", DisDetailsObj.getString(40));
					jsonObject.put("packageName", DisDetailsObj.getString(41));
					jsonObject.put("packageCategCode", DisDetailsObj.getString(42));
					jsonObject.put("packageId", DisDetailsObj.getString(43));
					jsonObject.put("actualDateAdmission", DisDetailsObj.getString(44));
					jsonObject.put("actualDateDischarge", DisDetailsObj.getString(45));
					jsonObject.put("triggerFlg", DisDetailsObj.getString(46));
					jsonObject.put("referalCode", DisDetailsObj.getString(47));
					jsonObject.put("hospitalActdation", DisDetailsObj.getString(48));
					jsonObject.put("claimraisestatus", DisDetailsObj.getString(49));
					jsonObject.put("sysrejStatus", DisDetailsObj.getString(50));
					jsonObject.put("rejStatus", DisDetailsObj.getString(51));
					jsonObject.put("varificationMode", DisDetailsObj.getString(52));
					jsonObject.put("isPatientApproveVarify", DisDetailsObj.getString(53));
					jsonObject.put("referalAuthStatus", DisDetailsObj.getString(54));
					jsonObject.put("cpdClaimStatus", DisDetailsObj.getString(55));
					jsonObject.put("cpdRemark", DisDetailsObj.getString(56));
					jsonObject.put("snaClaimStatus", DisDetailsObj.getString(57));
					jsonObject.put("snaRemark", DisDetailsObj.getString(58));
					jsonObject.put("hospitalClaimAmt", DisDetailsObj.getString(59));
					jsonObject.put("cpdApprovedAmt", DisDetailsObj.getString(60));
					jsonObject.put("snaApprovedAmt", DisDetailsObj.getString(61));
					jsonObject.put("transactionId", DisDetailsObj.getString(62));
					jsonObject.put("authorizedCode", DisDetailsObj.getString(63));
					jsonObject.put("transactionid", DisDetailsObj.getString(64));
					jsonObject.put("txnpackagedetailid", DisDetailsObj.getString(65));
					jsonObject.put("hospitaldistrictcode", DisDetailsObj.getString(66));
					jsonObject.put("hospitaldistrictname", DisDetailsObj.getString(67));
				} else if (serchtype == 2) {
					jsonObject.put("invoiceNo", DisDetailsObj.getString(1));
					jsonObject.put("caseno", DisDetailsObj.getString(2));
					jsonObject.put("urn", DisDetailsObj.getString(3));
					jsonObject.put("patientName", DisDetailsObj.getString(4));
					jsonObject.put("procedureName", DisDetailsObj.getString(5));
					jsonObject.put("packageName", DisDetailsObj.getString(6));
					jsonObject.put("packageCategCode", DisDetailsObj.getString(7));
					jsonObject.put("isPreAuth", DisDetailsObj.getString(8));
					jsonObject.put("actualDateAdmission", DisDetailsObj.getString(9));
					jsonObject.put("dateOfAdmission", DisDetailsObj.getString(10));
					jsonObject.put("totalPackageCost", DisDetailsObj.getString(11));
					jsonObject.put("blockedAmt", DisDetailsObj.getString(12));
					jsonObject.put("surgicalType", DisDetailsObj.getString(13));
					jsonObject.put("varificationMode", DisDetailsObj.getString(14));
					jsonObject.put("stateName", DisDetailsObj.getString(15));
					jsonObject.put("districtName", DisDetailsObj.getString(16));
					jsonObject.put("hospitalName", DisDetailsObj.getString(17));
					jsonObject.put("transactiondetailid", DisDetailsObj.getString(18));
					jsonObject.put("transactionId", DisDetailsObj.getString(19));
					jsonObject.put("txnpackagedetailid", DisDetailsObj.getString(20));
					jsonObject.put("hospitaldistrictcode", DisDetailsObj.getString(21));
					jsonObject.put("hospitaldistrictname", DisDetailsObj.getString(22));
					jsonObject.put("packageCode", DisDetailsObj.getString(23));
					jsonObject.put("packageSubCatg", DisDetailsObj.getString(24));
				}
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (DisDetailsObj != null) {
					DisDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArray.toString();
	}

	@Override
	public String getAdmissionBlockedDetails(String txnid, String pkgid, String userid) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MONTH_WISE_REPORT_DTLS")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRANSACTIONID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", Integer.parseInt(userid));
			storedProcedureQuery.setParameter("P_TRANSACTIONID", Integer.parseInt(txnid));
			storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILID", Long.valueOf((pkgid)));
			storedProcedureQuery.execute();
			DisDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("statename", DisDetailsObj.getString(1));
				jsonObject.put("districtname", DisDetailsObj.getString(2));
				jsonObject.put("hospitalname", DisDetailsObj.getString(3));
				jsonObject.put("urn", DisDetailsObj.getString(4));
				jsonObject.put("patientName", DisDetailsObj.getString(5));
				jsonObject.put("patientNo", DisDetailsObj.getString(6));
				jsonObject.put("patientGender", DisDetailsObj.getString(7));
				jsonObject.put("age", DisDetailsObj.getString(8));
				jsonObject.put("packageheadercode", DisDetailsObj.getString(9));
				jsonObject.put("packageheadername", DisDetailsObj.getString(10));
				jsonObject.put("packageSubCategCode", DisDetailsObj.getString(11));
				jsonObject.put("packageSubCatgName", DisDetailsObj.getString(12));
				jsonObject.put("procedureCode", DisDetailsObj.getString(13));
				jsonObject.put("procedureName", DisDetailsObj.getString(14));
				jsonObject.put("amountBlocked", DisDetailsObj.getString(15));
				jsonObject.put("actualAdmissionDate", DisDetailsObj.getString(16));
				jsonObject.put("admissionDate", DisDetailsObj.getString(17));
				jsonObject.put("invoice", DisDetailsObj.getString(18));
				jsonObject.put("caseno", DisDetailsObj.getString(19));
				jsonObject.put("dischargeDate", DisDetailsObj.getString(20));
				jsonObject.put("actualDischargeDate", DisDetailsObj.getString(21));
				jsonObject.put("claimedAmount", DisDetailsObj.getString(22));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (DisDetailsObj != null) {
					DisDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArray.toString();
	}

	@Override
	public Response saveDischargeReport(MultipartFile pdf) {
		Response response = new Response();
		Year year = Year.now();
		String yeardata = year.toString();
		try {

			if (pdf != null) {
				response = METriggerUtil.METriggerDocument(pdf, yeardata);
			} else {
				response.setMessage("Some Error Happen");
				response.setStatus("400");
				return response;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen");
			response.setStatus("200");
		}
		return response;
	}

	@Override
	public void downloadDischargeRpts(String fileCode, HttpServletResponse response) {
		try {
			METriggerUtil.downloadDischargeDocForMe(fileCode, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
