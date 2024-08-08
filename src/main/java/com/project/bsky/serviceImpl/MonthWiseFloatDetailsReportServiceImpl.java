/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.service.MonthWiseFloatDetailsReportService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class MonthWiseFloatDetailsReportServiceImpl implements MonthWiseFloatDetailsReportService {

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String MonthWiseFloatDetails(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode) {
		logger.info("Inside MonthWiseFloatDetails method of MonthWiseFloatDetailsReportServiceImpl");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_monthwise_float_detail_report")
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_from_date", fromdate);
			storedProcedure.setParameter("p_to_date", todate);
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
			if (hospitalCode == null) {
				storedProcedure.setParameter("P_HOSPITAL_CODE", null);
			} else {
				storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalCode);
			}
			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("slNo", DisDetailsObj.getString(1));
				jsonObject.put("hospitalCode", DisDetailsObj.getString(2));
				jsonObject.put("hospitalName", DisDetailsObj.getString(3));
				jsonObject.put("hospStateName", DisDetailsObj.getString(4));
				jsonObject.put("hospDistrictName", DisDetailsObj.getString(5));
				jsonObject.put("month", DisDetailsObj.getString(6));
				jsonObject.put("year", DisDetailsObj.getString(7));
				jsonObject.put("patientDistName", DisDetailsObj.getString(8));
				jsonObject.put("urn", DisDetailsObj.getString(9));
				jsonObject.put("invoiceNo", DisDetailsObj.getString(10));
				jsonObject.put("claimNo", DisDetailsObj.getString(11));
				jsonObject.put("caseNo", DisDetailsObj.getString(12));
				jsonObject.put("patientName", DisDetailsObj.getString(13));
				jsonObject.put("gender", DisDetailsObj.getString(14));
				jsonObject.put("procedureName", DisDetailsObj.getString(15));
				jsonObject.put("packageName", DisDetailsObj.getString(16));
				jsonObject.put("packageCatgCode", DisDetailsObj.getString(17));
				jsonObject.put("packageId", DisDetailsObj.getString(18));
				jsonObject.put("packageCost", DisDetailsObj.getString(19));
				jsonObject.put("actualDischargeDate", DisDetailsObj.getString(20));
				jsonObject.put("mortalityHospital", DisDetailsObj.getString(21));
				jsonObject.put("mortalityCpd", DisDetailsObj.getString(22));
				jsonObject.put("hospitalClaimAmt", DisDetailsObj.getString(23));
				jsonObject.put("implantData", DisDetailsObj.getString(24));
				jsonObject.put("cpdClaimStatus", DisDetailsObj.getString(25));
				jsonObject.put("cpdRemark", DisDetailsObj.getString(26));
				jsonObject.put("cpdApproveAmt", DisDetailsObj.getString(27));
				jsonObject.put("snaClaimStatus", DisDetailsObj.getString(28));
				jsonObject.put("snaRemark", DisDetailsObj.getString(29));
				jsonObject.put("snaApproveAmt", DisDetailsObj.getString(30));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			logger.error("Exception in getsnoclaimrasiedata method of SnoClaimProcessingDetailsImpl", e);
		} finally {
			try {
				if (DisDetailsObj != null) {
					DisDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception in getsnoclaimrasiedata method of SnoClaimProcessingDetailsImpl", e2);
			}
		}
		return jsonArray.toString();
	}

}
