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

import com.project.bsky.service.SNAMasterService;

/**
 * @author ronauk
 *
 */
@Service
public class SNAMasterServiceImpl implements SNAMasterService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public String getStateMasterDetails(String snoId) {
		JSONArray statelist = new JSONArray();
		ResultSet rs = null;
		try {
			if (snoId != null && !snoId.equalsIgnoreCase("")) {
				Long snoUserId = Long.parseLong(snoId);
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_SNA_MASTER_DETAILS")
						.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_flag", 1);
				storedProcedureQuery.setParameter("p_sno", snoUserId);
				storedProcedureQuery.setParameter("p_statecode", null);
				storedProcedureQuery.setParameter("p_districtcode", null);
				storedProcedureQuery.execute();

				rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");

				while (rs.next()) {
					JSONObject state = new JSONObject();
					state.put("stateId", rs.getInt(1));
					state.put("stateCode", rs.getString(2));
					state.put("stateName", rs.getString(3));
					statelist.put(state);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return statelist.toString();
	}

	@Override
	public String getDistrictDetailsByStateId(String snoId, String stateCode) {
		// ////System.out.println("indise getDistrictDetailsByStateId");
		JSONArray districtlist = new JSONArray();
		ResultSet rs = null;
		try {
			if (snoId != null && !snoId.equalsIgnoreCase("")) {
				Long snoUserId = Long.parseLong(snoId);
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_SNA_MASTER_DETAILS")
						.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_flag", 2);
				storedProcedureQuery.setParameter("p_sno", snoUserId);
				storedProcedureQuery.setParameter("p_statecode", stateCode);
				storedProcedureQuery.setParameter("p_districtcode", null);
				storedProcedureQuery.execute();

				rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");

				while (rs.next()) {
					JSONObject district = new JSONObject();
					district.put("DistrictId", rs.getInt(1));
					district.put("statecode", rs.getString(2));
					district.put("districtcode", rs.getString(3));
					district.put("districtname", rs.getString(4));
					districtlist.put(district);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return districtlist.toString();
	}

	@Override
	public String getHospitalByDistrictId(String snoId, String districtId, String stateId) {
		// ////System.out.println("indise getDistrictDetailsByStateId");
		ResultSet rs = null;
		JSONArray hospitallist = new JSONArray();
		try {
			if (snoId != null && !snoId.equalsIgnoreCase("")) {
				Long snoUserId = Long.parseLong(snoId);
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_SNA_MASTER_DETAILS")
						.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_flag", 3);
				storedProcedureQuery.setParameter("p_sno", snoUserId);
				storedProcedureQuery.setParameter("p_statecode", stateId);
				storedProcedureQuery.setParameter("p_districtcode", districtId);
				storedProcedureQuery.execute();

				rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");

				while (rs.next()) {
					JSONObject hospital = new JSONObject();
					hospital.put("hospitalId", rs.getInt(1));
					hospital.put("hospitalName", rs.getString(2));
					hospital.put("hospitalCode", rs.getString(3));
					hospital.put("hospName", rs.getString(4));
					hospitallist.put(hospital);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return hospitallist.toString();
	}

}
