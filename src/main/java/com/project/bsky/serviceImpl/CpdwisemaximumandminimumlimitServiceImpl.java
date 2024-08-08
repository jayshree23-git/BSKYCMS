package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Cpdwisemaximumminimumlimitset;
import com.project.bsky.bean.Response;
import com.project.bsky.service.CpdwisemaximumandminimumlimtSrvice;

@SuppressWarnings("unchecked")
@Service
public class CpdwisemaximumandminimumlimitServiceImpl implements CpdwisemaximumandminimumlimtSrvice {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public Response savecpdwisemaximumandminimumlimt(Long cpdid, Long maxlimit, Long puserid, String Assigneduptodate) {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_MAX_MIN_SETLIMIT")
					.registerStoredProcedureParameter("P_cpd_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_max_limit", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ASSIGNEDUPTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_cpd_userid", cpdid);
			storedProcedureQuery.setParameter("P_max_limit", maxlimit);
			storedProcedureQuery.setParameter("P_USERID", puserid);
			storedProcedureQuery.setParameter("P_ASSIGNEDUPTO", Assigneduptodate);
			storedProcedureQuery.execute();
			int returnValue = (int) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (returnValue == 1) {
				response.setStatus("success");
				response.setMessage("Your Maximum Limit Inserted Successfully");
			} else if (returnValue == 2) {
				response.setStatus("error");
				response.setMessage("Cpd  Maximum Limit Is Already set");
			}
		} catch (Exception e) {
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		}
		return response;
	}

	@Override
	public List<Object> cpdwisemaximumandminimumlimtview(String cpdid) {
		List<Object> claimRaiseDetailsList = new ArrayList<Object>();
		ResultSet rsResultSet = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_MAX_MIN_SETLIMIT_VIEW")
					.registerStoredProcedureParameter("P_USERID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", cpdid);
			storedProcedureQuery.execute();
			rsResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rsResultSet.next()) {
				Cpdwisemaximumminimumlimitset data = new Cpdwisemaximumminimumlimitset();
				data.setMaxminconfigid(rsResultSet.getLong(1));
				data.setCpduserid(rsResultSet.getLong(2));
				data.setMaxlimt(rsResultSet.getLong(3));
				data.setMinlimit(rsResultSet.getLong(4));
				data.setCreatedon(rsResultSet.getString(5) != null ? rsResultSet.getString(5) : "N/A");
				data.setCreatedby(rsResultSet.getLong(6));
				data.setUpdatedby(rsResultSet.getLong(7));
				data.setUpdatedon(rsResultSet.getString(8) != null ? rsResultSet.getString(8) : "N/A");
				data.setFullname(rsResultSet.getString(9));
				data.setAssignedupto(rsResultSet.getString(10));
				claimRaiseDetailsList.add(data);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rsResultSet != null) {
					rsResultSet.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimRaiseDetailsList;
	}

	@Override
	public Map<String, Object> cpdwisemaximumandminimumlimttogetupdatedata(String cpduserid) {
		Map<String, Object> RaiseDetailsMap = new HashMap<String, Object>();
		List<Object> claimRaiseDetailsList = new ArrayList<Object>();
		try {
			Query query = this.entityManager.createNativeQuery(
					"select a.MAX_LIMIT,a.MIN_LIMIT,b.fullname,a.CPD_USERID,a.ASSIGNEDUPTO  FROM tbl_cpd_max_min_limit_config a\r\n"
							+ "inner join user_details_cpd b on a.CPD_USERID=b.BSKYUSERID\r\n"
							+ "WHERE a.cpd_userid = ?1 and a.STATUSFLAG=0");
			query.setParameter(1, cpduserid);
			List<Object[]> data = query.getResultList();
			for (Object[] result : data) {
				claimRaiseDetailsList.add((BigDecimal) result[0]);
				claimRaiseDetailsList.add((BigDecimal) result[1]);
				claimRaiseDetailsList.add((String) result[2]);
				claimRaiseDetailsList.add((BigDecimal) result[3]);
				claimRaiseDetailsList.add((Date) result[4]);
			}
			RaiseDetailsMap.put("Data", claimRaiseDetailsList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return RaiseDetailsMap;
	}

	@Override
	public Response cpdwisemaximumandminimumlimtupdaterecord(Long cpdid, Long maxlimit, Long userid,
			String updatedassigneduptodate) {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_MAX_MIN_UPDATE")
					.registerStoredProcedureParameter("P_cpd_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_max_limit", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ASSIGNEDUPTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_cpd_userid", cpdid);
			storedProcedureQuery.setParameter("P_max_limit", maxlimit);
			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_ASSIGNEDUPTO", updatedassigneduptodate);
			storedProcedureQuery.execute();
			int returnValue = (int) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (returnValue == 1) {
				response.setStatus("success");
				response.setMessage("Your Record Updated Successfully");
			} else if (returnValue == 2) {
				response.setStatus("error");
				response.setMessage("Some error happen");
			}
		} catch (Exception e) {
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		}
		return response;
	}
}