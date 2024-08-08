/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.CpdActionReportBean;
import com.project.bsky.bean.Cpdwiseunprocessedbean;
import com.project.bsky.bean.NonComplianceBean;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.service.SNAwisependingreportService;
import com.project.bsky.util.DateFormat;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class SNAwisependingreportServiceimpl implements SNAwisependingreportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object> getsnawisependingreport(NonComplianceBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_WISE_PENDING_REPORT")
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_user_id", null);
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_actioncode", "A");

			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			while (snoDetailsObj.next()) {
				if (snoDetailsObj.getString(2) != null) {
					CpdActionReportBean bean = new CpdActionReportBean();
					bean.setAssigned(snoDetailsObj.getString(1));
					bean.setUsername(snoDetailsObj.getString(2));
					bean.setPatcpdfess(snoDetailsObj.getString(3));
					bean.setPatcpdresettelment(snoDetailsObj.getString(4));
					bean.setPendingatcpd(snoDetailsObj.getString(5));
					bean.setCpdquery7(snoDetailsObj.getString(6));
					SnoclaimRaiseDetailsList.add(bean);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public List<Object> getsnawisependingreportdetails(NonComplianceBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_WISE_PENDING_REPORT")
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_user_id", requestBean.getSnoid());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_actioncode", requestBean.getActionId());

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			while (rs.next()) {
				ReportCountBeanDetails rcBean = new ReportCountBeanDetails();
				rcBean.setClaimId(rs.getLong(1));
				rcBean.setClaimNo(rs.getString(2));
				rcBean.setUrn(rs.getString(3));
				rcBean.setPackageName(rs.getString(4));
				rcBean.setHospitalName(rs.getString(5));
				rcBean.setActDateOfAdm(DateFormat.formatDateFun(rs.getString(6)));
				rcBean.setActDateOfDschrg(DateFormat.formatDateFun(rs.getString(7)));
				rcBean.setAuthorizedCode(rs.getString(8));
				rcBean.setHospitalCode(rs.getString(9));
				rcBean.setCpdName(rs.getString(10));
				rcBean.setAlloteddate(rs.getString(11));
				if (rs.getString(12) != null) {
					rcBean.setInvoiceno(rs.getString(12));
				} else {
					rcBean.setInvoiceno("N/A");
				}
				rcBean.setPatentname(rs.getString(14));
				rcBean.setPackagecode(rs.getString(13));
				SnoclaimRaiseDetailsList.add(rcBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public List<Object> getcpdwiseunprocesseddata(Cpdwiseunprocessedbean requestData) {
		List<Object> cpdwiseunprocesseddetails = new ArrayList<Object>();
		ResultSet cpdwiseunprocessed = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_WISE_PENDING_REPORT")
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_statecode", requestData.getStateCode());
			storedProcedureQuery.setParameter("p_user_id", requestData.getUserId());
			storedProcedureQuery.setParameter("p_districtcode", requestData.getDistrictCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestData.getHospitalCode());
			storedProcedureQuery.setParameter("p_from_date", requestData.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestData.getToDate());
			storedProcedureQuery.setParameter("p_actioncode", "A");
			cpdwiseunprocessed = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			storedProcedureQuery.execute();
			while (cpdwiseunprocessed.next()) {
				if (cpdwiseunprocessed.getString(2) != null) {
					CpdActionReportBean bean = new CpdActionReportBean();
					bean.setAssigned(cpdwiseunprocessed.getString(1));
					bean.setUsername(cpdwiseunprocessed.getString(2));
					bean.setPatcpdfess(cpdwiseunprocessed.getString(3));
					bean.setPatcpdresettelment(cpdwiseunprocessed.getString(4));
					bean.setCpdquery7(cpdwiseunprocessed.getString(5));
					cpdwiseunprocesseddetails.add(bean);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (cpdwiseunprocessed != null) {
					cpdwiseunprocessed.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return cpdwiseunprocesseddetails;
	}

	@Override
	public List<Object> getcpdwisependingreportdetails(Cpdwiseunprocessedbean requestBean) {
		List<Object> cpdwiseunprocesseddetailsdetails = new ArrayList<Object>();
		ResultSet cpdwiseunprocesseddetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_WISE_PENDING_REPORT")
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_actioncode", requestBean.getActionId());
			cpdwiseunprocesseddetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			storedProcedureQuery.execute();
			while (cpdwiseunprocesseddetails.next()) {
				ReportCountBeanDetails rcBean = new ReportCountBeanDetails();
				rcBean.setClaimId(cpdwiseunprocesseddetails.getLong(1));
				rcBean.setClaimNo(cpdwiseunprocesseddetails.getString(2));
				rcBean.setUrn(cpdwiseunprocesseddetails.getString(3));
				rcBean.setPackageName(cpdwiseunprocesseddetails.getString(4));
				rcBean.setHospitalName(cpdwiseunprocesseddetails.getString(5));
				rcBean.setActDateOfAdm(DateFormat.formatDateFun(cpdwiseunprocesseddetails.getString(6)));
				rcBean.setActDateOfDschrg(DateFormat.formatDateFun(cpdwiseunprocesseddetails.getString(7)));
				rcBean.setAuthorizedCode(cpdwiseunprocesseddetails.getString(8));
				rcBean.setHospitalCode(cpdwiseunprocesseddetails.getString(9));
				rcBean.setCpdName(cpdwiseunprocesseddetails.getString(10));
				rcBean.setAlloteddate(cpdwiseunprocesseddetails.getString(11));
				if (cpdwiseunprocesseddetails.getString(12) != null) {
					rcBean.setInvoiceno(cpdwiseunprocesseddetails.getString(12));
				} else {
					rcBean.setInvoiceno("N/A");
				}
				rcBean.setPackagecode(cpdwiseunprocesseddetails.getString(13));
				rcBean.setPatentname(cpdwiseunprocesseddetails.getString(14));
				cpdwiseunprocesseddetailsdetails.add(rcBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (cpdwiseunprocesseddetails != null) {
					cpdwiseunprocesseddetails.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return cpdwiseunprocesseddetailsdetails;
	}
}
