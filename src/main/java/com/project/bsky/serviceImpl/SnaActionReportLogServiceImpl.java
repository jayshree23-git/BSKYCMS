/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CpdActionReportBean;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.service.SnaActionReportLogService;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class SnaActionReportLogServiceImpl implements SnaActionReportLogService {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object> snaactionreport(Long userId, String year, String month) {
		List<Object> object = new ArrayList<Object>();
		ResultSet report = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_ACTION_TAKEN_LOG_RPRT")

					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_year", year);
			storedProcedureQuery.setParameter("p_month", month);
			storedProcedureQuery.execute();
			report = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			while (report.next()) {
				CpdActionReportBean cpdactionreportbean = new CpdActionReportBean();
				cpdactionreportbean.setDate(report.getString(1));
				cpdactionreportbean.setApprove(report.getDouble(2));
				cpdactionreportbean.setReject(report.getDouble(3));
				cpdactionreportbean.setSnaquery(report.getLong(4));
				cpdactionreportbean.setInvestigate(report.getLong(5));
				cpdactionreportbean.setReverttocpd(report.getLong(6));
				object.add(cpdactionreportbean);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (report != null) {
					report.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return object;
	}

	@Override
	public List<Object> snaactionreportdetails(Long userId, Integer actiontype, String date) {
		List<Object> object = new ArrayList<Object>();
		ResultSet report = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_ACTION_LOG_DTLS_RPRT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action_type", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_action_type", actiontype);
			storedProcedureQuery.setParameter("p_date", date);
			storedProcedureQuery.execute();
			report = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			while (report.next()) {
				ReportCountBeanDetails snaactionreportdetails = new ReportCountBeanDetails();
				snaactionreportdetails.setClaimId(report.getLong(1));
				snaactionreportdetails.setApprovedamount(report.getString(2));
				snaactionreportdetails.setClaimNo(report.getString(3));
				snaactionreportdetails.setUrn(report.getString(4));
				snaactionreportdetails.setPatentname(report.getString(5));
				snaactionreportdetails.setClaimamount(report.getString(6));
				snaactionreportdetails.setPackageName(report.getString(7));
				snaactionreportdetails.setHospitalName(report.getString(8));
				snaactionreportdetails.setActDateOfAdm(report.getString(9));
				snaactionreportdetails.setActDateOfDschrg(report.getString(10));
				snaactionreportdetails.setAuthorizedCode(report.getString(11));
				snaactionreportdetails.setHospitalCode(report.getString(12));
				snaactionreportdetails.setPackagecode(report.getString(13));
				snaactionreportdetails.setInvoiceno(report.getString(14));
				object.add(snaactionreportdetails);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (report != null) {
					report.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return object;
	}

	@Override
	public List<Object> getsnawisepreauthcountdetails(Date fromdate, Date todate, Long snadoctor, Integer type)
			throws Exception {
		List<Object> object = new ArrayList<Object>();
		ResultSet report = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_PREAUTH_ACTN_CNT")
					.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION", 2);
			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_USERID", snadoctor);
			storedProcedureQuery.setParameter("P_TYPE", type);
			storedProcedureQuery.execute();
			report = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (report.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("hospitalName", report.getString(1) + " (" + report.getString(2) + ")");
				map.put("hospitalCode", report.getString(2));
				map.put("patientName", report.getString(3));
				map.put("caseNo", report.getString(4));
				map.put("specialityCode", report.getString(5));
				map.put("specilityName", report.getString(6) + " (" + report.getString(5) + ")");
				map.put("packageName", report.getString(7));
				map.put("procedureCode", report.getString(8));
				map.put("amount", report.getString(9));
				map.put("rqstdate", report.getString(10));
				object.add(map);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (report != null) {
					report.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return object;
	}

}
