/**
 *
 */
package com.project.bsky.daoImpl;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.FloatRequestBean;
import com.project.bsky.bean.PaymentFreezeBean;
import com.project.bsky.bean.PaymentFreezeDetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.dao.PaymentFreezeDao;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.JwtUtil;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 * @author santanu.barad
 *
 */
@Repository
public class PaymentFreezeDaoImpl implements PaymentFreezeDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Autowired
	private Environment env;

	@Autowired
	private JwtUtil util;

	@Override
	public List<Object> getpaymentfreezedata(CPDApproveRequestBean requestBean) {
		List<Object> PaymentfreezeList = new ArrayList<Object>();
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet claimListObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_SNA_PAYMENT_FREEZE_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_status_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_status_code", requestBean.getFlag());
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", 0);
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			// According the requirement added P_TRIGGERTYPE. by Rajendra With Gyana Sir
			// Dt-13-Nov-2023
			storedProcedureQuery.execute();

			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (claimListObj.next()) {
				PaymentFreezeBean resBean = new PaymentFreezeBean();
				resBean.setTransactionDetailsId(claimListObj.getLong(1));
				resBean.setClaimid(claimListObj.getLong(2));
				resBean.setURN(claimListObj.getString(3));
				resBean.setPatientName(claimListObj.getString(4));
				resBean.setInvoiceNumber(claimListObj.getString(5));
				resBean.setCreatedOn(new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(claimListObj.getDate(6)));
				resBean.setCpdAlotteddate(claimListObj.getTimestamp(7));
				resBean.setPackageName(claimListObj.getString(8));
				resBean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(claimListObj.getDate(9)));
				resBean.setPackageCode(claimListObj.getString(10));
				resBean.setSnaApprovedAmount(claimListObj.getDouble(11));
				resBean.setClaimNo(claimListObj.getString(12));
				resBean.setHospitalcode(claimListObj.getString(13));
				resBean.setAuthorizedcode(claimListObj.getString(14));
				resBean.setClaimStatus(claimListObj.getInt(15));
				resBean.setStatename(claimListObj.getString(16));
				resBean.setDistrictname(claimListObj.getString(17));
				resBean.setHospitalname(claimListObj.getString(18));
				resBean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(claimListObj.getDate(19)));
				resBean.setTotalAmountBlocked(claimListObj.getDouble(20));
				resBean.setWalletRefundStatus(claimListObj.getInt(21));
				resBean.setTxnpackagedetailid(claimListObj.getLong(22));
				resBean.setAmountDifference(claimListObj.getDouble(23));
				PaymentfreezeList.add(resBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return PaymentfreezeList;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Response savePaymentfreeze(Long userId, Date fromDate, Date toDate, String stateCodeList,
			String distCodeList, String hospitalCodeList, Long snoUserId, Double snaAmount, String floatFile,
			String floatNumber, Integer searchtype, String schemecategoryid) throws Exception {
		Response response = new Response();
		Date date = new Date();
		int year = date.getYear() + 1900;
		Integer claimsnoInteger = null;
		Connection con = null;
		CallableStatement st = null;
//		int schemeCategoryId;
//		if (schemecategoryid != null && !schemecategoryid.equals("")) {
//			schemeCategoryId = Integer.parseInt(schemecategoryid);
//		} else {
//			schemeCategoryId = 0;
//		}
		Long userId1 = util.getCurrentUser();
		String[] stringArray = null;
//		List<Map<String, Object>> listState = (List<Map<String, Object>>) stateCodeList;
		JSONArray jsonArray = new JSONArray(stateCodeList);
		List<Map<String, Object>> listState = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			// Convert each JSONObject to a Map
			Map<String, Object> stateMap = new HashMap<>();
			stateMap.put("stateCode", jsonObject.getString("stateCode"));
			stateMap.put("stateName", jsonObject.getString("stateName"));
			listState.add(stateMap);
		}
//		List<Map<String, Object>> listDist = (List<Map<String, Object>>) distCodeList;
		JSONArray jsonArray1 = new JSONArray(distCodeList);
		List<Map<String, Object>> listDist = new ArrayList<>();
		for (int i = 0; i < jsonArray1.length(); i++) {
			JSONObject jsonObject = jsonArray1.getJSONObject(i);
			// Convert each JSONObject to a Map
			Map<String, Object> distMap = new HashMap<>();
			distMap.put("districtCode", jsonObject.getString("districtCode"));
			distMap.put("stateCode", jsonObject.getString("stateCode"));
			listDist.add(distMap);
		}
//		List<Map<String, Object>> listHospital = (List<Map<String, Object>>) hospitalCodeList;
		JSONArray jsonArray2 = new JSONArray(hospitalCodeList);
		List<Map<String, Object>> listHospital = new ArrayList<>();
		for (int i = 0; i < jsonArray2.length(); i++) {
			JSONObject jsonObject = jsonArray2.getJSONObject(i);
			Map<String, Object> hospitalMap = new HashMap<>();
			hospitalMap.put("hospitalCode", jsonObject.getString("hospitalCode"));
			listHospital.add(hospitalMap);
		}

		if (!listHospital.isEmpty()) {
			stringArray = new String[listHospital.size()];
			for (int i = 0; i < listHospital.size(); i++) {
				stringArray[i] = "0#" + "0#" + listHospital.get(i).get("hospitalCode");
			}
		} else if (!listDist.isEmpty()) {
			stringArray = new String[listDist.size()];
			for (int i = 0; i < listDist.size(); i++) {
				stringArray[i] = listDist.get(i).get("stateCode") + "#" + listDist.get(i).get("districtCode") + "#0";
			}
		} else if (!listState.isEmpty()) {
			stringArray = new String[listState.size()];
			for (int i = 0; i < listState.size(); i++) {
				stringArray[i] = listState.get(i).get("stateCode") + "#0" + "#0";
			}
		} else {
			stringArray = new String[1];
			stringArray[0] = "0#0#0";
		}
		try {
//			StoredProcedureQuery storedProcedureQuery = this.entityManager
//					.createStoredProcedureQuery("USP_CLAIM_GENERATE_FLOAT")
//					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_state", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_district", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_hospital", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_float_number", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_sna_userid", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_amount", Double.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);
//
//			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
//			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
//			storedProcedureQuery.setParameter("P_state", requestBean.getStateCode());
//			storedProcedureQuery.setParameter("P_district", requestBean.getDistCode());
//			storedProcedureQuery.setParameter("P_hospital", requestBean.getHospitalCode());
//			storedProcedureQuery.setParameter("p_float_number", floatNumber);
//			storedProcedureQuery.setParameter("p_userid", requestBean.getUserId());
//			storedProcedureQuery.setParameter("p_sna_userid", requestBean.getSnoUserId());
//			storedProcedureQuery.setParameter("p_amount", requestBean.getSnaAmount());
//
//			storedProcedureQuery.execute(); 

			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");

			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("STATE_DIST_CODE_HOS_TYPE", con);
			ARRAY arrayToPass = new ARRAY(des, con, stringArray);
			st = con.prepareCall("call USP_CLAIM_GENERATE_FLOAT(?,?,?,?,?,?,?,?,?)");
			st.setDate(1, new java.sql.Date(fromDate.getTime()));
			st.setDate(2, new java.sql.Date(toDate.getTime()));
			st.setArray(3, arrayToPass);
			st.setString(4, floatNumber);
			st.setLong(5, userId);
			st.setLong(6, snoUserId);
			st.setDouble(7, snaAmount);
			st.setString(8, floatFile);
//			st.setString(9, snaCertificate);
//			st.setString(10, meCertificate);
//			st.setString(11, otherCertificate);
//			st.setString(12, description);
//			st.setInt(13, searchtype);
//			st.setInt(14, schemeCategoryId);
			st.registerOutParameter(9, Types.INTEGER);
			st.execute();
			claimsnoInteger = ((OracleCallableStatement) st).getInt(9);
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Float generated Successfully with this " + floatNumber + " Float number.");
			} else if (claimsnoInteger == 2) {
				response.setStatus("Failed");
				response.setMessage("Float generation failed, Please Try Agian..");
			} else if (claimsnoInteger == 3) {
				response.setStatus("record");
				response.setMessage("Float already Generated.");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}

		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFloatNumber(Long snaUserId, String month, String year) {
		Query query = null;
		List<Object[]> floatObj = null;
		List<String> floatNumberList = new ArrayList<>();
		String floatNumber = null;
		try {
			query = this.entityManager.createNativeQuery(
					"select FLOAT_NO from txnclaim_float_details f where f.status_flag=0 and f.sna_id=" + snaUserId
							+ "  and TO_CHAR(f.act_discharge_date_from,'MON')=" + month
							+ " and TO_CHAR(f.act_discharge_date_from,'YYYY')=" + year + "order by f.FLOAT_ID desc");
			floatObj = query.getResultList();
			if (floatObj.size() > 0) {
				for (Object objects : floatObj) {
					floatNumber = (String) objects;
					floatNumberList.add(floatNumber);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return floatNumberList;
	}

	@Override
	public List<Object> getFloatDetails(String floatNumber) {
		List<Object> floatDetailsList = new ArrayList<Object>();
		ResultSet resultSet = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOATNO_DETAILS")
					.registerStoredProcedureParameter("p_float_no", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosp_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_float_no", floatNumber);
			storedProcedureQuery.setParameter("p_hosp_code", null);
			storedProcedureQuery.execute();

			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (resultSet.next()) {
				PaymentFreezeBean resBean = new PaymentFreezeBean();
				resBean.setFloatNumber(resultSet.getString(1));
				resBean.setClaimNo(resultSet.getString(2));
				resBean.setClaimid(resultSet.getLong(3));
				resBean.setURN(resultSet.getString(4));
				resBean.setPatientName(resultSet.getString(5));
				resBean.setSnaApprovedAmount(resultSet.getDouble(6));
				resBean.setInvoiceNumber(resultSet.getString(7));
				resBean.setPackageCode(resultSet.getString(8));
				resBean.setPackageName(resultSet.getString(9));
				resBean.setCurrentTotalAmount(resultSet.getDouble(10));
				resBean.setCpdApprovedAmount(resultSet.getDouble(11));
				resBean.setCreatedByFullName(resultSet.getString(12));
				resBean.setActualDateOfAdmission(DateFormat.FormatToDateString(resultSet.getString(14)));
				resBean.setActualDateOfDischarge(DateFormat.FormatToDateString(resultSet.getString(15)));
				resBean.setHospitalname(resultSet.getString(16));
				resBean.setHospitalcode(resultSet.getString(17));
				resBean.setCaseno(resultSet.getString(18));
				resBean.setForemarks(resultSet.getString(19));
				resBean.setIsBulkApproved(resultSet.getString(20));
				resBean.setSnaDoctorName(resultSet.getString(21));
				resBean.setPendingAt(resultSet.getInt(22));
				resBean.setClaimStatus(resultSet.getInt(23));
				floatDetailsList.add(resBean);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return floatDetailsList;
	}

//	@SuppressWarnings("null")
	@Override
//	@Transactional
	public Response remarkUpdate(ClaimLogBean bean) {
//		String query = "UPDATE TXNCLAIM_APPLICATION SET FO_REMARKS = '" + bean.getRemarks() + "' WHERE CLAIMID="
//				+ bean.getClaimId();
//		Integer result = null;
		Response response = null;
		Integer resOut = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_ADD_REMARKS_UPDATE")
					.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remark_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", bean.getClaimId());
			storedProcedureQuery.setParameter("P_REMARKS", bean.getRemarks());
			storedProcedureQuery.setParameter("p_user_id", bean.getUserId());
			storedProcedureQuery.setParameter("p_remark_id", bean.getRemarkSelect());
			storedProcedureQuery.execute();

			resOut = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			response = new Response();
			if (resOut == 1) {
				response.setStatus("success");
				response.setMessage("Updated successfully");
			} else {
				response.setStatus("fail");
				response.setMessage("Updation failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return response;
	}

	@Override
	@Transactional
	public Response verifyFloat(String floatNumber, Long actionBy, String remark, String filePath) {
		String query = "UPDATE TXNCLAIM_FLOAT_DETAILS SET IS_VERIFIED = 1, PENDING_AT = 7, REMARKS='" + remark
				+ "', UPDATED_ON = SYSTIMESTAMP, UPDATED_BY =" + actionBy + ", FLOAT_DOC = '" + filePath
				+ "' WHERE FLOAT_NO = '" + floatNumber.trim() + "'";
		Integer result = null, result2 = null;
		Response response = null;
		try {
			result = this.entityManager.createNativeQuery(query).executeUpdate();
			result2 = saveFloatLog(floatNumber, actionBy, remark, filePath);
			response = new Response();
			if (result == 1 && result2 == 1) {
				response.setStatus("success");
				response.setMessage("Verified successfully");
			} else {
				response.setStatus("fail");
				response.setMessage("Verification failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return response;
	}

	@Override
	public List<Object> getSnaApprovedList(CPDApproveRequestBean requestBean) {
		List<Object> PaymentfreezeList = new ArrayList<Object>();
		ResultSet claimListObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_APPROVED_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_status_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_status_code", requestBean.getFlag());

			storedProcedureQuery.execute();

			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (claimListObj.next()) {
				PaymentFreezeBean resBean = new PaymentFreezeBean();
				resBean.setTransactionDetailsId(claimListObj.getLong(1));
				resBean.setClaimid(claimListObj.getLong(2));
				resBean.setURN(claimListObj.getString(3));
				resBean.setPatientName(claimListObj.getString(4));
				resBean.setInvoiceNumber(claimListObj.getString(5));
				resBean.setCreatedOn(claimListObj.getString(6));
				resBean.setCpdAlotteddate(claimListObj.getTimestamp(7));
				resBean.setPackageName(claimListObj.getString(8));
				resBean.setActualDateOfDischarge(claimListObj.getString(9));
				resBean.setPackageCode(claimListObj.getString(10));
				resBean.setSnaApprovedAmount(claimListObj.getDouble(11));
				resBean.setClaimNo(claimListObj.getString(12));
				resBean.setHospitalcode(claimListObj.getString(13));
				resBean.setAuthorizedcode(claimListObj.getString(14));
				resBean.setClaimStatus(claimListObj.getInt(15));
				resBean.setStatename(claimListObj.getString(16));
				resBean.setDistrictname(claimListObj.getString(17));
				resBean.setHospitalname(claimListObj.getString(18));
				resBean.setCurrentTotalAmount(claimListObj.getDouble(19));
				resBean.setCpdApprovedAmount(claimListObj.getDouble(20));
				resBean.setHospitalname(claimListObj.getString(21));
				resBean.setHospitalcode(claimListObj.getString(22));
				resBean.setCaseno(claimListObj.getString(23));
				PaymentfreezeList.add(resBean);
			}

		} catch (Exception e) {
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
		return PaymentfreezeList;
	}

	@Override
	public Response freezePayment(Long userId, String claimList) {
		Response response = new Response();
		String returnObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_payment_freeze")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn_CLAIM_NO_list", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSG_OUT1", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_urn_CLAIM_NO_list", claimList);
			storedProcedureQuery.execute();
			String returnValue = (String) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			returnObj = (String) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT1");

			if (returnValue.equalsIgnoreCase("1")) {
				response.setStatus("success");
				response.setMessage("Payment Freezed Successfully");
			} else if (returnValue.equalsIgnoreCase("2")) {
				if (returnObj.equalsIgnoreCase("exists")) {
					response.setMessage("Payment already freezed");
					response.setStatus("Failed");
				} else {
					response.setMessage("Some error happen");
					response.setStatus("Failed");
				}
			} else {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		}
		return response;
	}

	@Override
	@Transactional
	public Response forwardToSNA(String floatNumber, Long actionBy, String remark, String filePath1) {
		String query = "UPDATE TXNCLAIM_FLOAT_DETAILS SET PENDING_AT = 3, REMARKS='" + remark
				+ "', UPDATED_ON = SYSTIMESTAMP, UPDATED_BY =" + actionBy + ", FLOAT_DOC = '" + filePath1
				+ "'WHERE FLOAT_NO = '" + floatNumber + "'";
		Integer result = null, result2 = null;
		Response response = null;
		try {
			result = this.entityManager.createNativeQuery(query).executeUpdate();
			result2 = saveFloatLog(floatNumber, actionBy, remark, filePath1);
			response = new Response();
			if (result == 1 && result2 == 1) {
				response.setStatus("success");
				response.setMessage("Submitted successfully");
			} else {
				response.setStatus("fail");
				response.setMessage("Process failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return response;
	}

	@Override
	@Transactional
	public Response forwardfloat(String header, Long userId, String remarks, Long pendingAT) {
		Response response = new Response();
//		String query = "UPDATE TXNCLAIM_FLOAT_DETAILS SET PENDING_AT = " + pendingAT + ",REMARKS='" + remarks
//				+ "',UPDATED_ON=SYSTIMESTAMP,UPDATED_BY=" + userId + " WHERE  FLOAT_NO = '" + header + "'";
//		Integer results = null;
//		Response response = null;
//		try {
//			results = this.entityManager.createNativeQuery(query).executeUpdate();
//			Integer res = saveFloatLog(header, userId, remarks, null);
//			response = new Response();
//			if (results == 1 && res == 1) {
//				response.setStatus("success");
//				response.setMessage("Forward  successfully");
//			} else {
//				response.setStatus("fail");
//				response.setMessage("Forward  failed");
//			}
//		} catch (Exception e) {
//			logger.error(ExceptionUtils.getStackTrace(e));
//			throw e;
//		}
//		return response;

		// procedure starts here
		if (header == null || header.trim().isEmpty()) {
			response.setMessage("Float Number Cannot Be Null");
			response.setStatus("failed");
			return response;
		}

		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("USP_FLOAT_FORWARD");
			query.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_PENDING_AT", Long.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_ASSIGNED_AUTHORITY", Long.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_FLOAT_DOC", String.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_UPDATED_BY", Long.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			query.setParameter("P_FLOAT_NO", header.trim());
			query.setParameter("P_REMARKS", remarks);
			query.setParameter("P_PENDING_AT", pendingAT);
			query.setParameter("P_ASSIGNED_AUTHORITY", null);
			query.setParameter("P_FLOAT_DOC", null);
			query.setParameter("P_UPDATED_BY", userId);
			query.execute();

			Integer data = (Integer) query.getOutputParameterValue("P_OUT");
			if (data == 1) {
				response.setStatus("success");
				response.setMessage("Float forwarded successfully");
			} else {
				response.setStatus("failed");
				response.setMessage("Some error happened");
			}
		} catch (Exception e) {
			logger.error("Error forwarding float: ", e);
			response.setStatus("fail");
			response.setMessage("Failed to forward float");
			throw e;
		}

		return response;
	}

	@Override
	public List<Object> getFloatDetailsByHospital(String floatNumber, String hospitalCode) {
		// TODO Auto-generated method stub
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOATNO_DETAILS")
					.registerStoredProcedureParameter("p_float_no", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosp_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_float_no", floatNumber);
			storedProcedureQuery.setParameter("p_hosp_code", hospitalCode);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				PaymentFreezeDetailsBean bean = new PaymentFreezeDetailsBean();
				bean.setClaimId(rs.getInt(1));
				bean.setHospitalCode(rs.getString(2));
				bean.setHospitalName(rs.getString(3));
				bean.setDistrictName(rs.getString(4));
				bean.setUrn(rs.getString(5));
				bean.setInvoiceNo(rs.getString(6));
				bean.setClaimNo(rs.getString(7));
				bean.setCaseNo(rs.getString(8));
				bean.setPatientName(rs.getString(9));
				bean.setGender(rs.getString(10));
				bean.setPackageCode(rs.getString(11));
				bean.setPackageName(rs.getString(12));
				bean.setPackageCost(rs.getString(13));
				bean.setProcedureName(rs.getString(14));
				bean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(15)));
				bean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(16)));
				bean.setMortality(rs.getString(17));
				bean.setCpdMortality(rs.getString(18));
				bean.setTotalAmountClaimed(rs.getString(19));
				bean.setImplantData(rs.getString(20));
				bean.setCpdClaimStatus(rs.getString(21));
				bean.setCpdRemarks(rs.getString(22));
				bean.setCpdApprovedAmount(rs.getString(23));
				bean.setSnaClaimStatus(rs.getString(24));
				bean.setSnaRemarks(rs.getString(25));
				bean.setSnoApprovedAmount(rs.getString(26));
				bean.setJointCeoRemarks(rs.getString(27));
				bean.setFoRemarks(rs.getString(28));
				bean.setJointCeoRemarksRevert(rs.getString(29));
				bean.setNoRemarks(rs.getString(30));
				bean.setNoApprovedAmount(rs.getString(31));
				bean.setJointCeoRemarksVerify(rs.getString(32));
				bean.setFinalFoRemarks(rs.getString(33));
				bean.setAudRemarks(rs.getString(34));
				bean.setDyceoRemarks(rs.getString(35));
				bean.setJointCeoRemarksFinal(rs.getString(36));
				bean.setNoRemarksFinal(rs.getString(37));
				bean.setSnaName(rs.getString(38));
				bean.setCreatedBy(rs.getString(39));
				bean.setPendingAt(rs.getInt(40));
				bean.setIsReverted(rs.getInt(41));
				bean.setIncenticeStatus(rs.getString(42));
				bean.setFloatNumber(rs.getString(43));
//				bean.setPendingAt(8);
//				bean.setIsReverted(1);
				list.add(bean);
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
		return list;
	}

	@Transactional
	@Override
	public Integer saveFloatLog(String floatNumber, Long actionBy, String remark, String filePath1) {
		String fileName = null;
		if (filePath1 != null) {
			fileName = filePath1;
		} else {
			fileName = "";
		}
		String query = "INSERT INTO txnclaim_float_action_log (action_log_id,float_id,float_no,amount,created_by,created_on,status_flag,pending_at,"
				+ "payment_status,updated_by,updated_on,remarks,assigned_authority,is_verified,sna_id,actionby,actionon,float_doc)"
				+ "SELECT seq_txnclaim_float_action_log_id.NEXTVAL,float_id,float_no,amount,created_by,created_on,status_flag,pending_at,payment_status,"
				+ "updated_by,updated_on,'" + remark + "',assigned_authority,is_verified,sna_id,'" + actionBy
				+ "',sysdate,'" + fileName + "' FROM TXNCLAIM_FLOAT_DETAILS " + "WHERE FLOAT_NO = '"
				+ floatNumber.trim() + "'";
		Integer result = null;
		try {
			result = this.entityManager.createNativeQuery(query).executeUpdate();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public Response forwardDraftFloat(String floatNumber, Long snoUserId, String otherCertificate,
			String snaCertificate, String meCertificate, String description) throws Exception {
		Response response = new Response();
		String returnObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DRAFT_DOCUMENT_UPDATE")
					.registerStoredProcedureParameter("p_float_number", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CERTIFICATION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSE_CERTIFICATION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OTHERFILE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_float_number", floatNumber);
			storedProcedureQuery.setParameter("P_CERTIFICATION", snaCertificate);
			storedProcedureQuery.setParameter("P_MSE_CERTIFICATION", meCertificate);
			storedProcedureQuery.setParameter("P_OTHERFILE", otherCertificate);
			storedProcedureQuery.setParameter("P_REMARKS", description);
			storedProcedureQuery.setParameter("p_userid", util.getCurrentUser());
			storedProcedureQuery.execute();
			Integer returnValue = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (returnValue == 1) {
				response.setStatus("success");
				response.setMessage("Float Forwarded Successfully");
			} else {
				response.setMessage("Some error happen");
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		}
		return response;
	}
}
