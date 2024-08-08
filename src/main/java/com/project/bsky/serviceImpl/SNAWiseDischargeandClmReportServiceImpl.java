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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.SNAWiseDischargeandClmReportBean;
import com.project.bsky.service.SNAWiseDischargeandClmReportService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class SNAWiseDischargeandClmReportServiceImpl implements SNAWiseDischargeandClmReportService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;
	
	@Override
	public List<Object> getSNADischargeclaimData(Long snoUserId, Long year) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_monthwise_discharge_claim_rprt")
					.registerStoredProcedureParameter("p_sna_id",Long.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_year",Long.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out",void.class,ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_sna_id",snoUserId);
			storedProcedure.setParameter("p_year",year);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (list.next()) {
				SNAWiseDischargeandClmReportBean details = new SNAWiseDischargeandClmReportBean();
				details.setMonthName(list.getString(1)==null?"N/A":list.getString(1));
				details.setTotalDischarge(list.getString(2)==null?"N/A":list.getString(2));
				details.setDischargeAmt(list.getString(3)==null?"N/A":list.getString(3));
				details.setClmSubmitted(list.getString(4)==null?"N/A":list.getString(4));
				details.setClmSubmitAmt(list.getString(5)==null?"N/A":list.getString(5));
				details.setTotalPaid(list.getString(6)==null?"N/A":list.getString(6));
				details.setPaidAmount(list.getString(7)==null?"N/A":list.getString(7));
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}

	@Override
	public List<Object> snamonthwisedischargelist(Long userId, String year, Integer month) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_sna_month_wise_discharge_claim_rpt")
					.registerStoredProcedureParameter("user_id",Long.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_year",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("P_month",Integer.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out",void.class,ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("user_id",userId);
			storedProcedure.setParameter("P_year",year);
			storedProcedure.setParameter("P_month",month);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (list.next()) {
				SNAWiseDischargeandClmReportBean details = new SNAWiseDischargeandClmReportBean();
				details.setSnoid(list.getString(1)==null?"N/A":list.getString(1));
				details.setSnoname(list.getString(2)==null?"N/A":list.getString(2));
				details.setTotalDischarge(list.getString(3)==null?"N/A":list.getString(3));
				details.setDischargeAmt(list.getString(4)==null?"0":list.getString(4));
				details.setClmSubmitted(list.getString(5)==null?"N/A":list.getString(5));
				details.setClmSubmitAmt(list.getString(6)==null?"0":list.getString(6));
				details.setTotalPaid(list.getString(7)==null?"N/A":list.getString(7));
				details.setPaidAmount(list.getString(8)==null?"0":list.getString(8));
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}

	@Override
	public List<Object> hospitalwisedischargelist(Long userId, Integer year, Integer month, String statecode,
			String distcode, String hospcode) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_hospitalwise_discharge_claim_rprt")
					.registerStoredProcedureParameter("p_year",Integer.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_month",Integer.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out",void.class,ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_year",year);
			storedProcedure.setParameter("p_month",month);
			storedProcedure.setParameter("p_hosptlcode",hospcode);
			storedProcedure.setParameter("p_statecode",statecode);
			storedProcedure.setParameter("p_districtcode",distcode);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (list.next()) {
				SNAWiseDischargeandClmReportBean details = new SNAWiseDischargeandClmReportBean();
				details.setHospcode(list.getString(1)==null?"N/A":list.getString(1));
				details.setHospname(list.getString(2)==null?"N/A":list.getString(2));
				details.setTotalDischarge(list.getString(3)==null?"N/A":list.getString(3));
				details.setDischargeAmt(list.getString(4)==null?"0":list.getString(4));
				details.setClmSubmitted(list.getString(5)==null?"N/A":list.getString(5));
				details.setClmSubmitAmt(list.getString(6)==null?"0":list.getString(6));
				details.setTotalPaid(list.getString(7)==null?"N/A":list.getString(7));
				details.setPaidAmount(list.getString(8)==null?"0":list.getString(8));
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
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}

}
