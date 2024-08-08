/**
 * 
 */
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

import com.project.bsky.bean.PaymentFreezesReportBean;
import com.project.bsky.service.PaymentFreezesReportService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class PaymentFreezesReportServiceImpl implements PaymentFreezesReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getpaymentfreezdetails(String fromdate, String todate, Long snoUserId, String hospitalCode) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PAYMENT_FREEZE_REPORT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", snoUserId);
			if (hospitalCode == null) {
				storedProcedure.setParameter("P_HOSPITAL_CODE", null);
			} else {
				storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalCode);
			}
			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			storedProcedure.setParameter("P_FROM_DATE", d);
			Date d1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			storedProcedure.setParameter("P_TO_DATE", d1);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (list.next()) {
				PaymentFreezesReportBean details = new PaymentFreezesReportBean();
				details.setHospitalCode(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setHospitalName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setTotalClaimed(list.getString(3) == null ? "0" : list.getString(3));
				details.setTotlAmountClaimed(list.getString(4) == null ? "0" : list.getString(4));
				details.setTotalFrezzed(list.getString(5) == null ? "0" : list.getString(5));
				details.setTotalFreezAmount(list.getString(6) == null ? "0" : list.getString(6));
				details.setTotalPostPaymntUpdation(list.getString(7) == null ? "N/A" : list.getString(7));
				details.setTotalPostPaymntUpdationAmt(list.getString(8) == null ? "N/A" : list.getString(8));
				object.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return object;
	}
}
