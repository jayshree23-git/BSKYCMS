package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CPDMappingBean;
import com.project.bsky.service.CPDMappingReportService;

@Service
public class CPDMappingReportServiceImpl implements CPDMappingReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getHospitalByStateCode(String stateCode) {
		ResultSet hospitalObj = null;
		List<Object> hospitallist = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_MAPPING_REPORT")
					.registerStoredProcedureParameter("p_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 2);
			storedProcedureQuery.setParameter("p_STATECODE", stateCode);
			storedProcedureQuery.setParameter("p_HOSPITALCODE", null);
			storedProcedureQuery.execute();
			hospitalObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (hospitalObj.next()) {
				CPDMappingBean cPDMappingBean = new CPDMappingBean();
				cPDMappingBean.setHospitalName(hospitalObj.getString(1));
				cPDMappingBean.setHospitalCode(hospitalObj.getString(2));
				hospitallist.add(cPDMappingBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (hospitalObj != null) {
					hospitalObj.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return hospitallist;
	}

	@Override
	public List<Object> search(String stateCode, String hospitalCode) {
		ResultSet hospitalInfoObj = null;
		List<Object> hospitallist1 = new ArrayList<Object>();
		try {

			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_MAPPING_REPORT")
					.registerStoredProcedureParameter("p_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);
			storedProcedureQuery.setParameter("p_STATECODE", stateCode);
			storedProcedureQuery.setParameter("p_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.execute();
			hospitalInfoObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (hospitalInfoObj.next()) {
				CPDMappingBean cPDMappingBean = new CPDMappingBean();
				cPDMappingBean.setFullname(hospitalInfoObj.getString(1));
				cPDMappingBean.setUserName(hospitalInfoObj.getString(2));
				cPDMappingBean.setHospitalCode(hospitalInfoObj.getString(3));
				cPDMappingBean.setHospitalName(hospitalInfoObj.getString(4));
				hospitallist1.add(cPDMappingBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (hospitalInfoObj != null) {
					hospitalInfoObj.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return hospitallist1;
	}

	@Override
	public List<Object> getsnamappingreport(String stateCode, String distCode, String hospitalCode, Integer snastatus) throws Exception {
		List<Object> object = new ArrayList<>();
		ResultSet hospitalInfoObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_TAGGED_HOSPITAL_LIST")
					.registerStoredProcedureParameter("P_STAE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STAE_CODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", distCode);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.setParameter("P_SNA_STATUS", snastatus);
			storedProcedureQuery.execute();
			hospitalInfoObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (hospitalInfoObj.next()) {
				Map<String ,Object> map=new HashedMap<>();
				map.put("stateName", hospitalInfoObj.getString(1));
				map.put("distName", hospitalInfoObj.getString(2));
				map.put("hospitalCode", hospitalInfoObj.getString(3));
				map.put("hospitalName", hospitalInfoObj.getString(4));
				map.put("snaName", hospitalInfoObj.getString(5));
				object.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if (hospitalInfoObj != null) {
					hospitalInfoObj.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return object;
	}
}
