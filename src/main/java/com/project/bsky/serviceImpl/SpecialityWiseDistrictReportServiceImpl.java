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

import com.project.bsky.service.SpecialityWiseDistrictReportService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class SpecialityWiseDistrictReportServiceImpl implements SpecialityWiseDistrictReportService {

	@PersistenceContext
	private EntityManager entitiManager;
	
	@Autowired
	private Logger logger;

	@Override
	public String getSpecialityWiseData(String stateId, String districtId, String specialityCode) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet resultSetObject = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entitiManager
					.createStoredProcedureQuery("USP_SPECIALITYWISE_DISTRICTWISE_HOSPITALS_REPORT")
					.registerStoredProcedureParameter("P_STATECODE",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIALITYCODE",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT",void.class,ParameterMode.REF_CURSOR);
			if (stateId == null) {
				storedProcedureQuery.setParameter("P_STATECODE", null);
			} else {
				storedProcedureQuery.setParameter("P_STATECODE", stateId);
			}
			if (districtId == null) {
				storedProcedureQuery.setParameter("P_DISTRICTCODE", null);
			} else {
				storedProcedureQuery.setParameter("P_DISTRICTCODE", districtId);
			}
			if (specialityCode == null ) {
				storedProcedureQuery.setParameter("P_SPECIALITYCODE", null);
			} else {
				storedProcedureQuery.setParameter("P_SPECIALITYCODE",specialityCode);
			}
			storedProcedureQuery.execute();
			resultSetObject = (ResultSet)storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (resultSetObject.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("stateName",resultSetObject.getString(1));
				jsonObject.put("districtName",resultSetObject.getString(2));
				jsonObject.put("specialityName",resultSetObject.getString(3));
				jsonObject.put("specialityCode",resultSetObject.getString(4));
				jsonObject.put("hospitalCode",resultSetObject.getString(5));
				jsonObject.put("hospitalName",resultSetObject.getString(6));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (resultSetObject != null) {
					resultSetObject.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonArray.toString();
	}

}
