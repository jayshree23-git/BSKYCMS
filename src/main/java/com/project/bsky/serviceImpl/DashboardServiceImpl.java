package com.project.bsky.serviceImpl;

import java.sql.ResultSet;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.DashboardBean;
import com.project.bsky.service.DashboardService;

/**
 * @author ronauk
 *
 */
@Service
public class DashboardServiceImpl implements DashboardService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public JSONObject getSnaDashboardReport(DashboardBean bean) {
		JSONObject count = new JSONObject();
		ResultSet rs = null, rs1 = null, rs2 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_DASH_RPT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_output_ref", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", bean.getUserId());
			storedProcedureQuery.setParameter("p_month", bean.getMonth());
			storedProcedureQuery.setParameter("p_year", bean.getYear());
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				count.put("pendingatSNA", rs.getLong(1));
				count.put("pendingatCPD", rs.getLong(2));
				count.put("pendingatHsptl", rs.getLong(3));
				count.put("snaapproved", rs.getLong(4));
				count.put("totalclaimRasied", rs.getLong(5));
				count.put("snarejected", rs.getLong(6));
				count.put("pendingatDC", rs.getLong(7));
				count.put("totalDischarge", rs.getLong(8));
				count.put("nonUploading", rs.getLong(9));
				count.put("cpdapproved", rs.getLong(10));
				count.put("cpdrejected", rs.getLong(11));
				count.put("nonUploadingInit", rs.getLong(12));
				count.put("snaQuery", rs.getLong(13));
				count.put("pendatcpdRstl", rs.getLong(14));
				count.put("pendatcpdRvrt", rs.getLong(15));
				count.put("systemRejected", rs.getLong(16));
				count.put("unprocessed", rs.getLong(17));
				count.put("resettlement", rs.getLong(18));
				count.put("cpdQuerywithin7", rs.getLong(19));
				count.put("cpdQueryafter7", rs.getLong(20));
				count.put("snaQuerywithin7", rs.getLong(21));
				count.put("snaQueryafter7", rs.getLong(22));
				count.put("tpendingatCPD", rs.getLong(23));
				count.put("snaActionTaken", rs.getLong(24));
				count.put("dcCompliance", rs.getLong(25));
				count.put("onHold", rs.getLong(26));
				count.put("sysadminrej", rs.getLong(27));
				count.put("mortality", rs.getLong(28));
			}

			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs1.next()) {
				count.put("tcpdApproved", rs1.getLong(1));
				count.put("snaActionOfCpdAprvd", rs1.getLong(2));
				count.put("snaAprvdOfCpdAprvd", rs1.getLong(3));
				count.put("snaRjctdOfCpdAprvd", rs1.getLong(4));
				String percent1 = rs1.getString(5);
				if (percent1 != null && percent1.contains(" .00")) {
					percent1 = "0";
				}
				if (percent1 != null && percent1.startsWith(".")) {
					percent1 = "0" + percent1;
				}
				count.put("percent1", percent1);
			}
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_output_ref");
			while (rs2.next()) {
				count.put("statusFlag", false);
				count.put("fresh", rs2.getLong(1));
				count.put("querycomplied", rs2.getLong(2));
				count.put("query", rs2.getLong(3));
				count.put("approve", rs2.getLong(4));
				count.put("reject", rs2.getLong(5));
				count.put("autoapprove", rs2.getLong(6));
				count.put("autoreject", rs2.getLong(7));
				count.put("expired", rs2.getLong(8));
				count.put("cancelled", rs2.getLong(9));
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return count;
	}

	@Override
	public JSONObject getHospitalDashboardReport(DashboardBean bean) {
		JSONObject count = new JSONObject();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSP_DASH_RPT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", bean.getUserId());
			storedProcedureQuery.setParameter("p_month", bean.getMonth());
			storedProcedureQuery.setParameter("p_year", bean.getYear());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				count.put("pendingatHsptl", rs.getLong(1));
				count.put("totalclaimRasied", rs.getLong(2));
				count.put("totalDischarge", rs.getLong(3));
				count.put("nonUploading", rs.getLong(4));
				count.put("nonUploadingInit", rs.getLong(5));
				count.put("cpdQuerywithin7", rs.getLong(6));
				count.put("cpdQueryafter7", rs.getLong(7));
				count.put("snaQuerywithin7", rs.getLong(8));
				count.put("snaQueryafter7", rs.getLong(9));
				count.put("systemRejected", rs.getLong(10));
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
	public JSONObject getCpdDashboardReport(DashboardBean bean) {
		JSONObject count = new JSONObject();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_DASH_RPT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", bean.getUserId());
			storedProcedureQuery.setParameter("p_month", bean.getMonth());
			storedProcedureQuery.setParameter("p_year", bean.getYear());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				count.put("totalclaimRasied", rs.getLong(1));
				count.put("freshCount", rs.getLong(2));
				count.put("tcpdApproved", rs.getLong(3));
				count.put("tcpdRejected", rs.getLong(4));
				count.put("pendatcpdRstl", rs.getLong(5));
				count.put("tcpdQueried", rs.getLong(6));
				count.put("pendingatCPD", rs.getLong(7));
				count.put("dishonorCount", rs.getLong(8));
				count.put("tdishonored", rs.getLong(9));
				count.put("pendatcpdRvrt", rs.getLong(10));
				count.put("pendingatHsptl", rs.getLong(11));
				count.put("cpdQueryafter7", rs.getLong(12));
				count.put("cpdQuerywithin7", rs.getLong(13));
				count.put("tcpdAction", rs.getLong(14));
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

}
