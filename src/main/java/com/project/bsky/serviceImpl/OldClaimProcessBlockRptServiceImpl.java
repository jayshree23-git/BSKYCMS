/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.project.bsky.service.OldClaimProcessBlockRptService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class OldClaimProcessBlockRptServiceImpl implements OldClaimProcessBlockRptService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public String oldclaimprocessblockData(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode) {
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_OLD_CLAIM_PROCESS_BLOCKING_RPT")
					.registerStoredProcedureParameter("p_userid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_userid", userId);
			if (hospitalCode == null) {
				storedProcedure.setParameter("P_HOSPITAL_CODE", null);
			} else {
				storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalCode);
			}
			if (stateId == null) {
				storedProcedure.setParameter("P_STATE_CODE", null);
			} else {
				storedProcedure.setParameter("P_STATE_CODE", stateId);
			}
			if (districtId == null) {
				storedProcedure.setParameter("P_DISTRICT_CODE", null);
			} else {
				storedProcedure.setParameter("P_DISTRICT_CODE", districtId);
			}
			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			storedProcedure.setParameter("P_FROM_DATE", d);
			Date d1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			storedProcedure.setParameter("P_TO_DATE", d1);
			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
					jsonObject.put("urn", DisDetailsObj.getString(1));
					jsonObject.put("invoiceno", DisDetailsObj.getString(2));
					jsonObject.put("hospitalStateName", DisDetailsObj.getString(3));
					jsonObject.put("hospitalDistrictName", DisDetailsObj.getString(4));
					jsonObject.put("hospitalName", DisDetailsObj.getString(5));
					jsonObject.put("hospitalCode", DisDetailsObj.getString(6));
					jsonObject.put("patientName", DisDetailsObj.getString(7));
					jsonObject.put("packageCode", DisDetailsObj.getString(8));
					jsonObject.put("packageCatgCode", DisDetailsObj.getString(9));
					jsonObject.put("procedureName", DisDetailsObj.getString(10));
					jsonObject.put("dateOfBlocking", DisDetailsObj.getString(11));
					jsonObject.put("isPreAuthStatus", DisDetailsObj.getString(12));
					jsonObject.put("totalPackageCost", DisDetailsObj.getString(13));
					jsonObject.put("blockAmount", DisDetailsObj.getString(14));
					jsonObject.put("surgicalType", DisDetailsObj.getString(15));
					jsonObject.put("verificationMode", DisDetailsObj.getString(16));
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
	public String getOldDischargeData(Integer userId, String fromdate, String todate, String stateId, String districtId,
			String hospitalCode) {
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject;
			ResultSet DisDetailsObj = null;
			try {
				StoredProcedureQuery storedProcedure = this.entityManager
						.createStoredProcedureQuery("USP_OLD_CLAIM_PROCESS_DISCHARGE_RPT")
						.registerStoredProcedureParameter("p_userid", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

				storedProcedure.setParameter("p_userid", userId);
				if (hospitalCode == null) {
					storedProcedure.setParameter("P_HOSPITAL_CODE", null);
				} else {
					storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalCode);
				}
				if (stateId == null) {
					storedProcedure.setParameter("P_STATE_CODE", null);
				} else {
					storedProcedure.setParameter("P_STATE_CODE", stateId);
				}
				if (districtId == null) {
					storedProcedure.setParameter("P_DISTRICT_CODE", null);
				} else {
					storedProcedure.setParameter("P_DISTRICT_CODE", districtId);
				}
				Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
				storedProcedure.setParameter("P_FROM_DATE", d);
				Date d1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
				storedProcedure.setParameter("P_TO_DATE", d1);
				storedProcedure.execute();
				DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
				while (DisDetailsObj.next()) {
					jsonObject = new JSONObject();
						jsonObject.put("hospitalCode", DisDetailsObj.getString(1));
						jsonObject.put("hospitalname", DisDetailsObj.getString(2));
						jsonObject.put("hospitalStateName", DisDetailsObj.getString(3));
						jsonObject.put("hospitalDistrictName", DisDetailsObj.getString(4));
						jsonObject.put("claimno", DisDetailsObj.getString(5)!=null? DisDetailsObj.getString(5):"N/A");
						jsonObject.put("invoiceno", DisDetailsObj.getString(6)!=null?DisDetailsObj.getString(6):"N/A");
						jsonObject.put("urn", DisDetailsObj.getString(7));
						jsonObject.put("patientphoneno", DisDetailsObj.getString(8));
						jsonObject.put("patientname", DisDetailsObj.getString(9));
						jsonObject.put("gender", DisDetailsObj.getString(10));
						jsonObject.put("age", DisDetailsObj.getString(11));
						jsonObject.put("mortality", DisDetailsObj.getString(12)!=null? DisDetailsObj.getString(12):"N/A");
						jsonObject.put("cpdMortality", DisDetailsObj.getString(13)!=null? DisDetailsObj.getString(13):"N/A");
						jsonObject.put("packageCode", DisDetailsObj.getString(14));
						jsonObject.put("packageProcedurename", DisDetailsObj.getString(15));
						jsonObject.put("packageCost", DisDetailsObj.getString(16));
						jsonObject.put("actualdateofadmission", DisDetailsObj.getString(17));
						jsonObject.put("actualdateofdischarge", DisDetailsObj.getString(18));
						jsonObject.put("incentiveStatus", DisDetailsObj.getString(19));
						jsonObject.put("implantData", DisDetailsObj.getString(20)!=null? DisDetailsObj.getString(20):"N/A");
						jsonObject.put("cpdclaimstatus", DisDetailsObj.getString(21)!=null? DisDetailsObj.getString(21):"N/A");
						jsonObject.put("cpdremarks", DisDetailsObj.getString(22)!=null? DisDetailsObj.getString(22):"N/A");
						jsonObject.put("snaclaimstatus", DisDetailsObj.getString(23)!=null? DisDetailsObj.getString(23):"N/A");
						jsonObject.put("snaremarks", DisDetailsObj.getString(24)!=null? DisDetailsObj.getString(24):"N/A");
						jsonObject.put("hospitalclaimedamount", DisDetailsObj.getString(25));
						jsonObject.put("cpdApprovedAmount", DisDetailsObj.getString(26));
						jsonObject.put("snaApprovedAmount", DisDetailsObj.getString(27));
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

}
