
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CpdActionReportBean;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.service.CpdActionReportService;

/**
 * @author jayshree.moharana
 *
 */

@Service
public class CpdActionReportServiceimpl implements CpdActionReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> cpdactionreport(Long userId, String year, String month) {
		List<Object> object = new ArrayList<Object>();
		ResultSet report = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_ACTION_REPORT")
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
				Double Approve = report.getDouble(2);
				Double Reject = report.getDouble(3);
				cpdactionreportbean.setDate(report.getString(1));
				cpdactionreportbean.setApprove(Approve);
				cpdactionreportbean.setReject(Reject);
				cpdactionreportbean.setQuery(report.getDouble(4));
				Double dishonour = report.getDouble(5);
				cpdactionreportbean.setDishonour(dishonour);
				Double myamount = 75 * (Approve + Reject);
				cpdactionreportbean.setMyamount(myamount);
				Double dishonouramount = (75 * dishonour) / 2;
				cpdactionreportbean.setDishonouramount(dishonouramount);
				cpdactionreportbean.setFinalamount(myamount - dishonouramount);
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}

	@Override
	public List<Object> cpdactiontakenreport(Long userId, String date) {
		List<Object> object = new ArrayList<Object>();
		ResultSet report = null;
		try {
			Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(date);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_ACTION_TAKEN_RPT")
					.registerStoredProcedureParameter("p_action", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action", 1);
			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_date", date1);
			storedProcedureQuery.execute();
			report = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");

			while (report.next()) {
				CpdActionReportBean cpdactionreportbean = new CpdActionReportBean();
				cpdactionreportbean.setUsername(report.getString(1));
				cpdactionreportbean.setUserid(report.getString(2));
				cpdactionreportbean.setAssigned(report.getString(3));
				cpdactionreportbean.setActiontaken(report.getString(4));
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}

	@Override
	public List<Object> getcpdactiontekendetails(Long userId, String date, Integer type) {
		List<Object> object = new ArrayList<Object>();
		ResultSet report = null;
		try {
			Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(date);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_ACTION_TAKEN_RPT")
					.registerStoredProcedureParameter("p_action", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action", type);
			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_date", date1);
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
				snaactionreportdetails.setAlloteddate(report.getString(15));
				if (report.getString(16) != null) {
					if (report.getInt(16) == 1) {
						snaactionreportdetails.setActiontype("Approved");
					} else if (report.getInt(16) == 2) {
						snaactionreportdetails.setActiontype("Rejected");
					} else if (report.getInt(16) == 3) {
						snaactionreportdetails.setActiontype("Query");
					} else {
						snaactionreportdetails.setActiontype(report.getString(16));
					}
				} else {
					snaactionreportdetails.setActiontype(report.getString(16));
				}
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}
}
