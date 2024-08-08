package com.project.bsky.serviceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.bean.ReportCountBean;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaPaymentStatusBean;
import com.project.bsky.bean.UnprocessedClaimBean;
import com.project.bsky.service.ClaimReportService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;

@Service
public class ReportServiceImpl implements ClaimReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;

	@Autowired
	private Logger logger;

	@Autowired
	private SnoClaimProcessingDetailsImpl snoClaimProcessingDetailsImpl;

	@Override
	public List<Object> getActionCountDetails(Long userId, Date fromdate, Date toDate, String eventName, String stateId,
			String districtId, String hospitalId) {
		List<Object> details = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_COUNT_DETAILS")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_Name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
			storedProcedureQuery.setParameter("p_event_Name", eventName);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				ReportCountBeanDetails rcBean = new ReportCountBeanDetails();
				rcBean.setClaimId(rs.getLong(1));
				rcBean.setClaimNo(rs.getString(2));
				rcBean.setUrn(rs.getString(3));
				rcBean.setPackageName(rs.getString(4));
				rcBean.setHospitalName(rs.getString(5));
				rcBean.setActDateOfAdm(DateFormat.formatDateFun(rs.getString(6)));
				rcBean.setActDateOfDschrg(DateFormat.formatDateFun(rs.getString(7)));
				rcBean.setDateOfAdm(DateFormat.formatDateFun(rs.getString(8)));
				rcBean.setDateOfDschrg(DateFormat.formatDateFun(rs.getString(9)));
				rcBean.setAuthorizedCode(rs.getString(10));
				rcBean.setHospitalCode(rs.getString(11));
				rcBean.setCpdName(rs.getString(12));
				details.add(rcBean);

			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return details;
	}

	@Override
	public List<Object> getUnprocessedClaim(CPDApproveRequestBean requestBean) {
		List<Object> unProcessedClaimList = new ArrayList<Object>();
		ResultSet rs = null;
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_UNPROCESSED_CLAIM_LIST_TEST")
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
					.registerStoredProcedureParameter("p_SCHEMEID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_SCHEMECATEGORYID", Integer.class, ParameterMode.IN)
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
			storedProcedureQuery.setParameter("p_SCHEMEID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("p_SCHEMECATEGORYID", schemecatId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				UnprocessedClaimBean resBean = new UnprocessedClaimBean();
				resBean.setTransactionDetailsId(rs.getLong(1));
				resBean.setClaimid(rs.getLong(2));
				resBean.setUrn(rs.getString(3));
				resBean.setPatientName(rs.getString(4));
				resBean.setInvoiceNumber(rs.getString(5));
				resBean.setCreatedOn(rs.getString(6));
				resBean.setCpdAlotteddate(rs.getTimestamp(7));
				resBean.setPackageName(rs.getString(8));
				resBean.setRevisedDate(rs.getTimestamp(9));
				resBean.setPackageCode(rs.getString(10));
				resBean.setCurrentTotalAmount(rs.getString(11));
				resBean.setClaimNo(rs.getString(12));
				resBean.setAuthorizedCode(rs.getString(13));
				resBean.setHospitalName(rs.getString(14));
				resBean.setCpdMortality(rs.getString(15));
				resBean.setHosMortality(rs.getString(16));
				resBean.setHospitalcode(rs.getString(17));
//				Date f = new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString(18));
//				String str = new SimpleDateFormat("dd-MMM-yyyy").format(f);
				String str = null;
				if (rs.getString(18) != null) {
					str = DateFormat.formatDateFun(rs.getString(18));
					resBean.setActualdateofaddmission(str);
				} else {
					resBean.setActualdateofaddmission("N/A");
				}
				if (rs.getString(19) != null) {
					str = DateFormat.formatDateFun(rs.getString(19));
					resBean.setActualdateofdischarge(str);
				} else {
					resBean.setActualdateofdischarge("N/A");
				}
				resBean.setPhone(rs.getString(20) == null ? "N/A" : rs.getString(20));
				resBean.setTriggerValue(rs.getLong(22));
				resBean.setTriggerMsg(rs.getString(23));
				unProcessedClaimList.add(resBean);

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return unProcessedClaimList;
	}

	@Override
	public String getclaimRecievedReportCount(Long userId, String year, String month) {
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_RPT_CLM_RCVD_CNT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout1", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_p_msgout2", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_year", year);
			storedProcedureQuery.setParameter("p_month", month);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout1");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout2");

			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("dateOfMonth", rs.getString(1));
				jsonObject.put("totalTransactionCount", rs.getLong(2));
				jsonObject.put("totalTransactionAmount", rs.getLong(3));
				jsonArray.put(jsonObject);
			}
			details.put("tbldata", jsonArray);
			while (rs1.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("dateOfMonth", rs1.getString(1));
				jsonObject1.put("totalDischargedCount", rs1.getLong(2));
				jsonObject1.put("totalDischargeAmount", rs1.getLong(3));
				jsonObject1.put("totalClaimSubmitted", rs1.getLong(4));
				jsonObject1.put("totalClaimSubmittedAmount", rs1.getLong(5));
				jsonObject1.put("totalClaimPending", rs1.getLong(6));
				jsonObject1.put("totalClaimPendingAmount", rs1.getLong(7));
				jsonArray1.put(jsonObject1);

			}
			details.put("txndata", jsonArray1);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}

		return details.toString();
	}

	@Override
	public String getTransactionsCountDetails(Long userId, String years, String months, String days, String eventName) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_txn_details")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_day", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_Name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_year", years);
			storedProcedureQuery.setParameter("p_month", months);
			storedProcedureQuery.setParameter("p_day", days);
			storedProcedureQuery.setParameter("p_event_Name", eventName);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("id", rs.getLong(1));
				jsonObject.put("urn", rs.getString(2));
				jsonObject.put("invoiceno", rs.getString(3));
				jsonObject.put("hospitalcode", rs.getString(4));
				jsonObject.put("packagecode", rs.getString(5));
				jsonObject.put("currentTotalAmount", rs.getString(6));
				jsonObject.put("patientName", rs.getString(7));
				jsonObject.put("hospitalName", rs.getString(8));
				jsonObject.put("packageName", rs.getString(9));
				jsonObject.put("claimRaiseStatus", rs.getString(10));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return jsonArray.toString();
	}

	@Override
	public String getRecievedCountDetails(Long userId, String years, String months, String days, String eventName) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_RPT_CLM_RCVD_CNT_DTLS")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_day", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_Name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_year", years);
			storedProcedureQuery.setParameter("p_month", months);
			storedProcedureQuery.setParameter("p_day", days);
			storedProcedureQuery.setParameter("p_event_Name", eventName);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("id", rs.getLong(1));
				jsonObject.put("urn", rs.getString(2));
				jsonObject.put("invoiceno", rs.getString(3));
				jsonObject.put("hospitalcode", rs.getString(4));
				jsonObject.put("packagecode", rs.getString(5));
				jsonObject.put("currentTotalAmount", rs.getString(6));
				jsonObject.put("patientName", rs.getString(7));
				jsonObject.put("hospitalName", rs.getString(8));
				jsonObject.put("packageName", rs.getString(9));
				jsonObject.put("claimRaiseStatus", rs.getString(10));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return jsonArray.toString();
	}

	@Override
	public Long getSnaApprovedOfCpdApproved(Long userId, Date fromdate, Date toDate) {
		Long count = null;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_APD_OF_CPD_APD")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				count = rs.getLong(1);
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
		return count;
	}

	@Override
	public Response saveUnprocessedClaimSNADetails(ClaimLogBean logBean) throws Exception {
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
					.createStoredProcedureQuery("USP_SNA_UNPROCESSED_CLAIM_ACTN")
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
	public String getUnprocessedClaimDetailsById(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
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
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_UNPROCESSED_CLAIM_DTLS")
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
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			rs3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");

			if (rs.next()) {
				hospitalCode = rs.getString(18);
				authorizedCode = rs.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = rs.getString(1);
				actualDate = rs.getString(2);
				casenumber = rs.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", rs.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(rs.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(rs.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(rs.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(rs.getString(3), ""));
				jsonObject.put("STATENAME", rs.getString(4));
				jsonObject.put("DISTRICTNAME", rs.getString(5));
				jsonObject.put("BLOCKNAME", rs.getString(6));
				jsonObject.put("VILLAGENAME", rs.getString(7));
				jsonObject.put("HOSPITALNAME", rs.getString(8));
				jsonObject.put("PATIENTNAME", rs.getString(9));
				jsonObject.put("GENDER", rs.getString(10));
				jsonObject.put("AGE", rs.getString(11));
				jsonObject.put("PROCEDURENAME", rs.getString(12));
				jsonObject.put("PACKAGENAME", rs.getString(13));
				jsonObject.put("NOOFDAYS", CommonFileUpload.calculateNoOfDays(rs.getString(34), rs.getString(35)));
				jsonObject.put("INVOICENO", rs.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", rs.getString(16));
				jsonObject.put("HOSPITALADDRESS", rs.getString(17));
				jsonObject.put("HOSPITALCODE", rs.getString(18));
				jsonObject.put("PRESURGERYPHOTO", rs.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", rs.getString(20));
				jsonObject.put("ADITIONALDOCS", rs.getString(21));
				jsonObject.put("PACKAGERATE", rs.getString(22));
				jsonObject.put("INVESTIGATIONDOC", rs.getString(23));
				jsonObject.put("TREATMENTSLIP", rs.getString(24));
				jsonObject.put("ADMINSSIONSLIP", rs.getString(25));
				jsonObject.put("DISCHARGESLIP", rs.getString(26));
				jsonObject.put("CLAIMID", rs.getString(27));
				jsonObject.put("REMARKID", rs.getString(28));
				jsonObject.put("REMARKS", rs.getString(29));
				jsonObject.put("ADITIONAL_DOC1", rs.getString(30));
				jsonObject.put("ADITIONAL_DOC2", rs.getString(31));
				jsonObject.put("FAMILYHEADNAME", rs.getString(32));
				jsonObject.put("VERIFIERNAME", rs.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(rs.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(rs.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(rs.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(rs.getString(35), ""));
				jsonObject.put("MORTALITY", rs.getString(36));
				jsonObject.put("REFERRALCODE", rs.getString(37));
				jsonObject.put("AUTHORIZEDCODE", rs.getString(38));
				jsonObject.put("DISTRICTNAME", rs.getString(39));
				jsonObject.put("NABHFlag", rs.getString(40));
				jsonObject.put("Address", rs.getString(41));
				jsonObject.put("Statusflag", rs.getString(42));
				jsonObject.put("claimCaseNo", rs.getString(43));
				jsonObject.put("claimBillNo", rs.getString(44));
				jsonObject.put("PATIENT_PHOTO", rs.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", rs.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", rs.getString(47));
				String mob = rs.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", rs.getString(48));
				jsonObject.put("IMPLANTDATA", rs.getString(49));
				jsonObject.put("pendingat", rs.getLong(51));
				jsonObject.put("claimstatus", rs.getLong(52));
				jsonObject.put("CPDMORTALITY", rs.getString(53));
				jsonObject.put("verification", rs.getString(54));
				jsonObject.put("ispatient", rs.getString(55));
				jsonObject.put("Referalstatus", rs.getString(56));
				jsonObject.put("PackageCode", rs.getString(57));
				jsonObject.put("packageCode1", rs.getString(58) != null ? rs.getString(58) : "NA");
				jsonObject.put("packageName1", rs.getString(59) != null ? rs.getString(59) : "NA");
				jsonObject.put("subPackageCode1", rs.getString(60) != null ? rs.getString(60) : "NA");
				jsonObject.put("subPackageName1", rs.getString(61) != null ? rs.getString(61) : "NA");
				jsonObject.put("procedureCode1", rs.getString(62) != null ? rs.getString(62) : "NA");
				jsonObject.put("procedureName1", rs.getString(63) != null ? rs.getString(63) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", rs.getString(64));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(rs.getString(65), "time"));
				jsonObject.put("MEMBERID", rs.getString(66));
				jsonObject.put("ISEMERGENCY", rs.getString(67));
				jsonObject.put("OVERRIDECODE", rs.getString(68));
				jsonObject.put("TREATMENTDAY", rs.getString(69));
				jsonObject.put("DOCTORNAME", rs.getString(70));
				jsonObject.put("FROMHOSPITALNAME", rs.getString(71));
				jsonObject.put("TOHOSPITAL", rs.getString(72));
				jsonObject.put("DISREMARKS", rs.getString(73));
				jsonObject.put("TRANSACTIONDESCRIPTION", rs.getString(74));
				jsonObject.put("HOSPITALCATEGORYNAME", rs.getString(75));
				jsonObject.put("disverification", rs.getString(76));
				jsonObject.put("txnPackageDetailId", rs.getLong(77));
				jsonObject.put("Packagerate1", rs.getString(78) != null ? rs.getString(78) : "N/A");
				jsonObject.put("surgerydateandtime", rs.getString(79) != null ? rs.getString(79) : "NA");
				jsonObject.put("surgerydoctorname", rs.getString(80) != null ? rs.getString(80) : "NA");
				jsonObject.put("suergerycontactnumber", rs.getString(81) != null ? rs.getString(81) : "NA");
				jsonObject.put("suergeryregnumber", rs.getString(82) != null ? rs.getString(82) : "NA");
				details.put("actionData", jsonObject);
				while (rs1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", rs1.getString(1));
					jsonObject1.put("ACTIONTYPE", rs1.getString(2));
					jsonObject1.put("ACTIONBY", rs1.getString(3));
					jsonObject1.put("DESCRIPTION", rs1.getString(4));
					jsonObject1.put("ACTIONON", rs1.getString(5));
					jsonObject1.put("ACTIONON1", DateFormat.dateConvertor(rs1.getString(5), ""));
					jsonObject1.put("DISCHARGESLIP", rs1.getString(6));
					jsonObject1.put("ADITIONALDOCS", rs1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", rs1.getString(8));
					jsonObject1.put("PRESURGERY", rs1.getString(9));
					jsonObject1.put("POSTSURGERY", rs1.getString(10));
					jsonObject1.put("HOSPITALCODE", rs.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(rs.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(rs.getString(34)));
					jsonObject1.put("REMARKS", rs1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", rs1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", rs1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", rs1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", rs1.getString(16));
					jsonArray.put(jsonObject1);
				}

				while (rs2.next()) {
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", rs2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", rs2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", rs2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", rs2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (rs3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", rs3.getString(1));
					jsonObject3.put("claimNo", rs3.getString(2));
					jsonObject3.put("caseNo", rs3.getString(3));
					jsonObject3.put("patientName", rs3.getString(4));
					jsonObject3.put("phoneNo", rs3.getString(5));
					jsonObject3.put("hospitalName", rs3.getString(6));
					jsonObject3.put("hospitalCode", rs3.getString(7));
					jsonObject3.put("packageCode", rs3.getString(8));
					jsonObject3.put("packageName", rs3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(rs3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(rs3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", rs3.getLong(12));
					jsonObject3.put("reportName", rs3.getString(13));
					jsonObject3.put("claimId", rs3.getLong(14));
					jsonObject3.put("transactionId", rs3.getLong(15));
					jsonObject3.put("txnPackageId", rs3.getLong(16));
					jsonObject3.put("slNo", rs3.getLong(17));
					jsonObject3.put("createdOn", rs3.getDate(18));
					jsonObject3.put("statusFlag", rs3.getString(19));
					jsonObject3.put("doctorRegNo", rs3.getString(20));
					jsonObject3.put("surgeryDate", rs3.getDate(21));
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
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (rs1 != null)
					rs1.close();
				if (rs2 != null)
					rs2.close();
				if (rs3 != null)
					rs3.close();
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
	public List<Object> getclaimcountprogressreportdetails(Long userId, Date fromdate, Date toDate, String eventName,
			String stateId, String districtId, String hospitalId, String groupid) {
		List<Object> detailscountprogressreport = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_COUNT_PROG_DTLS_RPT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_Name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
			storedProcedureQuery.setParameter("p_event_Name", eventName);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				ReportCountBeanDetails rcBean = new ReportCountBeanDetails();
				rcBean.setClaimId(rs.getLong(1));
				rcBean.setClaimNo(rs.getString(2));
				rcBean.setCaseno(rs.getString(3));
				rcBean.setUrn(rs.getString(4));
				rcBean.setPackageName(rs.getString(5));
				rcBean.setPackagecode(rs.getString(6));
				rcBean.setHospitalName(rs.getString(7));
				rcBean.setActDateOfAdm(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(8)));
				rcBean.setActDateOfDschrg(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(9)));
				rcBean.setDateOfAdm(DateFormat.formatDateFun(rs.getString(10)));
				rcBean.setDateOfDschrg(DateFormat.formatDateFun(rs.getString(11)));
				rcBean.setAuthorizedCode(rs.getString(12));
				rcBean.setHospitalCode(rs.getString(13));
				rcBean.setCpdName(rs.getString(14));
				if (eventName.equalsIgnoreCase("CQW") || eventName.equalsIgnoreCase("CQA")
						|| eventName.equalsIgnoreCase("SQW") || eventName.equalsIgnoreCase("SQA")) {
					rcBean.setQueryDate(rs.getString(15));
				}
				if (eventName.equalsIgnoreCase("NID") || eventName.equalsIgnoreCase("NON")) {
					rcBean.setClaimRaisedBy(rs.getString(15));
				}
				if (eventName.equalsIgnoreCase("CPD") || eventName.equalsIgnoreCase("CRS")
						|| eventName.equalsIgnoreCase("CRV")) {
					rcBean.setCpdAllotedDate(rs.getString(15));
				}
				detailscountprogressreport.add(rcBean);
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
		return detailscountprogressreport;
	}

	@Override
	public String getclaimByUrnAndClamiNo(Long userId, String searchBy, String fieldValue) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_CLAIM_BY_URN_CLAIMNO")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_searchBy", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fieldValue", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_searchBy", searchBy);
			storedProcedureQuery.setParameter("p_fieldValue", fieldValue);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("urn", snoDetailsObj.getString(1));
				jsonObject.put("packagecode", snoDetailsObj.getString(2));
				jsonObject.put("hospitalName", snoDetailsObj.getString(3));
				jsonObject.put("totalamountclaimed", snoDetailsObj.getString(4));
				if (snoDetailsObj.getString(5) != null && snoDetailsObj.getString(5) != "") {
					jsonObject.put("dateofadmission", DateFormat.formatDateFun(snoDetailsObj.getString(5)));
				}
				if (snoDetailsObj.getString(6) != null && snoDetailsObj.getString(6) != "") {
					jsonObject.put("dateofdischarge", DateFormat.formatDateFun(snoDetailsObj.getString(6)));
				}

				jsonObject.put("patientname", snoDetailsObj.getString(7));
				jsonObject.put("packagename", snoDetailsObj.getString(8));
				jsonObject.put("actiontype", snoDetailsObj.getString(9));
				jsonObject.put("claimid", snoDetailsObj.getLong(10));
				jsonObject.put("transactiondetailsid", snoDetailsObj.getLong(11));
				jsonObject.put("hospitalclaimedamount", snoDetailsObj.getString(12));
				jsonObject.put("cpdapprovedamount", snoDetailsObj.getString(13));
				jsonObject.put("snoapprovedamount", snoDetailsObj.getString(14));
				jsonObject.put("cpdName", snoDetailsObj.getString(15));
				jsonObject.put("pendingAt", snoDetailsObj.getLong(16));
				jsonObject.put("claimStatus", snoDetailsObj.getLong(17));
				if (snoDetailsObj.getString(18) != null && snoDetailsObj.getString(18) != "") {
					jsonObject.put("actualdateofadmission", DateFormat.formatDateFun(snoDetailsObj.getString(18)));
				}
				if (snoDetailsObj.getString(19) != null && snoDetailsObj.getString(19) != "") {
					jsonObject.put("actualdateofdischarge", DateFormat.formatDateFun(snoDetailsObj.getString(19)));
				}
				jsonObject.put("claimNo", snoDetailsObj.getString(20));
				jsonObject.put("SNAClaimStatus", snoDetailsObj.getString(21));
				jsonObject.put("SNARemarks", snoDetailsObj.getString(22));
				jsonObject.put("CPDClaimStatus", snoDetailsObj.getString(23));
				jsonObject.put("CPDRemarks", snoDetailsObj.getString(24));
				jsonObject.put("txnpackagedetailid", snoDetailsObj.getString(25));
				jsonObject.put("snaapprovedamount", snoDetailsObj.getString(26));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
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
		return jsonArray.toString();
	}

	@Override
	public String getSNAWisePaymentStatusReport(SnaPaymentStatusBean bean) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNAWISE_TOTALPAYMENT_FREZEE_REPORT")
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", bean.getFromdate());
			storedProcedureQuery.setParameter("P_TODATE", bean.getTodate());
			storedProcedureQuery.setParameter("P_HOSPITALCODE", bean.getHospitalCode());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("UserId", snoDetailsObj.getLong(1));
				jsonObject.put("SnaDoctorName", snoDetailsObj.getString(2));
				jsonObject.put("TotalClaim", snoDetailsObj.getLong(3));
				jsonObject.put("HospitalClaimAmount", snoDetailsObj.getDouble(4));
				jsonObject.put("CpdApprovedAmount", snoDetailsObj.getDouble(5));
				jsonObject.put("SnaApprovedAmount", snoDetailsObj.getDouble(6));
				jsonObject.put("SnaRejectedAmount", snoDetailsObj.getDouble(7));
				jsonObject.put("SnaPaymentFreezeAmount", snoDetailsObj.getDouble(8));
				jsonArray.put(jsonObject);
			}
			details.put("SnaWisePaymentStatus", jsonArray);
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
	public List<Map<String, Object>> hospitalWiseTreatment(Map<String, Object> request)
			throws SQLException, ParseException {
		ResultSet resultSet;
		List<Map<String, Object>> responseList = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_WISE_TREATMENT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_COUNT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", Long.parseLong(request.get("userId").toString()));
			storedProcedureQuery.setParameter("P_STATECODE", request.get("stateCode"));
			storedProcedureQuery.setParameter("P_DISTRICTCODE", request.get("distCode"));
			storedProcedureQuery.setParameter("P_HOSPITALCODE", request.get("hospitalCode"));
			storedProcedureQuery.setParameter("P_FROM_DATE",
					new SimpleDateFormat("dd-MMM-yyyy").parse(request.get("fromDate").toString()));
			storedProcedureQuery.setParameter("P_TO_DATE",
					new SimpleDateFormat("dd-MMM-yyyy").parse(request.get("toDate").toString()));
			storedProcedureQuery.setParameter("P_COUNT", Long.parseLong(request.get("orderBy").toString()));
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (resultSet.next()) {
				Map<String, Object> response = new LinkedHashMap<>();
				response.put("hospitalCode", resultSet.getString(1));
				response.put("hospitalName", resultSet.getString(2));
				response.put("treatmentCount", resultSet.getLong(3));
				responseList.add(response);
			}
			return responseList;
		} catch (Exception e) {
			throw e;
		}
	}
}
