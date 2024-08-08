/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;

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

import com.project.bsky.service.OneDcTaggedReportService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class OneDcTaggedReportServiceImpl implements OneDcTaggedReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public String getHospitalByDistrictId(Integer userId, String stateId1, String districtId1,Integer dcId) {
	
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager.createStoredProcedureQuery("USP_DC_TAG_TO_MULTI_HOS")
					.registerStoredProcedureParameter("P_USER_ID",Integer.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ASSIGNED_DC",Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			
				storedProcedure.setParameter("P_USER_ID",userId);
			
			if (stateId1 == null) {
				storedProcedure.setParameter("P_STATE_CODE",null);
			} else {
				storedProcedure.setParameter("P_STATE_CODE",stateId1);
			}
			if (districtId1 == null) {
				storedProcedure.setParameter("P_DISTRICT_CODE",null);
			} else {
				storedProcedure.setParameter("P_DISTRICT_CODE",districtId1);
			}
			
			storedProcedure.setParameter("P_ASSIGNED_DC",dcId);
			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("assignDc", DisDetailsObj.getString(3));
				jsonObject.put("hospitalCount", DisDetailsObj.getString(4));
				jsonObject.put("stateName", DisDetailsObj.getString(1));
				jsonObject.put("districtName", DisDetailsObj.getString(2));
				jsonObject.put("statecode", DisDetailsObj.getString(5));
				jsonObject.put("districtcode", DisDetailsObj.getString(6));
				jsonObject.put("fullname", DisDetailsObj.getString(7));
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
	public String getDcDetailsList(Integer userId,Integer assignDc, String stateId1, String districtId1) {
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager.createStoredProcedureQuery("USP_DC_TAG_TO_MULTI_HOS_DTLS")
					.registerStoredProcedureParameter("P_USER_ID",Integer.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ASSIGNED_DC",Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			
				storedProcedure.setParameter("P_USER_ID",userId);
			
			if (stateId1 == null) {
				storedProcedure.setParameter("P_STATE_CODE",null);
			} else {
				storedProcedure.setParameter("P_STATE_CODE",stateId1);
			}
			if (districtId1 == null) {
				storedProcedure.setParameter("P_DISTRICT_CODE",null);
			} else {
				storedProcedure.setParameter("P_DISTRICT_CODE",districtId1);
			}
			
			storedProcedure.setParameter("P_ASSIGNED_DC",assignDc);
			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("stateName", DisDetailsObj.getString(1));
				jsonObject.put("districtName", DisDetailsObj.getString(2));
				jsonObject.put("hospitalCode", DisDetailsObj.getString(3));
				jsonObject.put("hospitalName", DisDetailsObj.getString(4));
				jsonObject.put("fullname", DisDetailsObj.getString(5));
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
