package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
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

import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.service.PaymentfreezeReportService;
import com.project.bsky.util.DateFormat;

@Service
public class PaymentfreezeReportServiceImpl implements PaymentfreezeReportService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public List<Object> getpaymentfreezereport(Date formdate, Date todate, String stateId, String districtId,
			String hospitalId, String userId) {
		List<Object> detailscountprogressreport = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PAYMENT_FREEZE_RPT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_event_Name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("p_user_id",Long.parseLong(userId));
			storedProcedureQuery.setParameter("p_from_date", formdate);
			storedProcedureQuery.setParameter("p_to_date", todate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
//			storedProcedureQuery.setParameter("p_event_Name", eventName);
			
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (rs.next()) {
				ReportCountBeanDetails rcBean = new ReportCountBeanDetails();
//				////System.out.println("hi");
				rcBean.setClaimId(rs.getLong(1));
				rcBean.setClaimNo(rs.getString(2));
				rcBean.setUrn(rs.getString(3));
				rcBean.setPackageName(rs.getString(4));
				rcBean.setActDateOfAdm(DateFormat.formatDateFun(rs.getString(5)));
				rcBean.setActDateOfDschrg(DateFormat.formatDateFun(rs.getString(6)));
				rcBean.setAuthorizedCode(rs.getString(7));
				rcBean.setHospitalCode(rs.getString(8));
				rcBean.setInvoiceno(rs.getString(9));
				rcBean.setPackagecode(rs.getString(10));
				rcBean.setPatentname(rs.getString(11));
				rcBean.setClaimamount(rs.getString(12));
				rcBean.setHospitalName(rs.getString(13));				
				detailscountprogressreport.add(rcBean);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return detailscountprogressreport;
	}

}
