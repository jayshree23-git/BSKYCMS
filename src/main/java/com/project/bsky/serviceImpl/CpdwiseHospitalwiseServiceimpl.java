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

import com.project.bsky.bean.Cpdwisehospitalwise;
import com.project.bsky.bean.Cpdwisehospitalwisebean;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.service.Cpdwisehospitalwiseaction;
import com.project.bsky.util.DateFormat;

/**
 * @author hrusikesh.mohanty
 *
 */
@Service
public class CpdwiseHospitalwiseServiceimpl implements Cpdwisehospitalwiseaction {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getcpdwisehospitalwiseactiondetails(Cpdwisehospitalwise requestdatacpddetails) {
		List<Object> cpdwisehospitalwiseactionreportdetails = new ArrayList<Object>();
		ResultSet cpdwisehospitalwisedetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPDWISE_HOSPITALWISE_ACTION")
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_Cpduser_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_groupid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_statecode", requestdatacpddetails.getStateCode());
			storedProcedureQuery.setParameter("p_districtcode", requestdatacpddetails.getDistrictCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestdatacpddetails.getHospitalCode());
			storedProcedureQuery.setParameter("p_from_date", requestdatacpddetails.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestdatacpddetails.getToDate());
			storedProcedureQuery.setParameter("p_Cpduser_id", requestdatacpddetails.getCpduserId());
			storedProcedureQuery.setParameter("p_groupid", requestdatacpddetails.getGroupid());
			storedProcedureQuery.setParameter("p_hospitalcode", requestdatacpddetails.getHospitalcodesearch());
			storedProcedureQuery.setParameter("p_actioncode", requestdatacpddetails.getActionId());
			storedProcedureQuery.execute();
			cpdwisehospitalwisedetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			while (cpdwisehospitalwisedetails.next()) {
				ReportCountBeanDetails rcBean = new ReportCountBeanDetails();
				rcBean.setClaimId(cpdwisehospitalwisedetails.getLong(1));
				rcBean.setClaimNo(cpdwisehospitalwisedetails.getString(2));
				rcBean.setUrn(cpdwisehospitalwisedetails.getString(3));
				rcBean.setPackageName(cpdwisehospitalwisedetails.getString(4));
				rcBean.setHospitalName(cpdwisehospitalwisedetails.getString(5));
				rcBean.setActDateOfAdm(DateFormat.formatDateFun(cpdwisehospitalwisedetails.getString(6)));
				rcBean.setActDateOfDschrg(DateFormat.formatDateFun(cpdwisehospitalwisedetails.getString(7)));
				rcBean.setAuthorizedCode(cpdwisehospitalwisedetails.getString(8));
				rcBean.setHospitalCode(cpdwisehospitalwisedetails.getString(9));
				rcBean.setCpdName(cpdwisehospitalwisedetails.getString(10));
				rcBean.setAlloteddate(cpdwisehospitalwisedetails.getString(11));
				if (cpdwisehospitalwisedetails.getString(12) != null) {
					rcBean.setInvoiceno(cpdwisehospitalwisedetails.getString(12));
				} else {
					rcBean.setInvoiceno("N/A");
				}
				rcBean.setPackagecode(cpdwisehospitalwisedetails.getString(13));
				rcBean.setPatentname(cpdwisehospitalwisedetails.getString(14));
				cpdwisehospitalwiseactionreportdetails.add(rcBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (cpdwisehospitalwisedetails != null) {
					cpdwisehospitalwisedetails.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return cpdwisehospitalwiseactionreportdetails;
	}
}
