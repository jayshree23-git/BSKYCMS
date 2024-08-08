package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.OngoingtreatmentReportBean;
import com.project.bsky.service.OngoingtreatmentReportService;

@Service
public class OngointreatmentReportServiceImpl implements OngoingtreatmentReportService {
	
	@PersistenceContext
	private EntityManager entityManager;

	public List<OngoingtreatmentReportBean> urnwise(String urn, String usename) {
		List<OngoingtreatmentReportBean> urnlist = new ArrayList<OngoingtreatmentReportBean>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_ONGOING_TRMNT_URN_RPRT")
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_Username", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_urn", urn);
			storedProcedure.setParameter("p_Username", usename);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (list.next()) {
				OngoingtreatmentReportBean resbean = new OngoingtreatmentReportBean();
				resbean.setPatientname(list.getString(1));
				resbean.setHospitalCode(list.getString(2));
				resbean.setUrn(list.getString(3));
				resbean.setInvoiceno(list.getString(4));
				resbean.setActualDateOfAdmission(list.getString(5));
				resbean.setPackagecode(list.getString(6));
				resbean.setProcedurename(list.getString(7));
				resbean.setPackagename(list.getString(8));
				resbean.setHospitalname(list.getString(9));
				urnlist.add(resbean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return urnlist;
	}

	@Override
	public List<OngoingtreatmentReportBean> hospitalwise(String username, String statecode, String districtcode) {
		List<OngoingtreatmentReportBean> hospitallist = new ArrayList<OngoingtreatmentReportBean>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_ONGOING_TRMNT_HSPTL_RPRT")
					.registerStoredProcedureParameter("p_username", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_username", username);
			storedProcedure.setParameter("p_statecode", statecode);
			storedProcedure.setParameter("p_districtcode", districtcode);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (list.next()) {
				OngoingtreatmentReportBean hospbean = new OngoingtreatmentReportBean();
				hospbean.setTotalhospitalname(list.getString(1));
				hospbean.setTotalpatient(list.getString(2));
				hospitallist.add(hospbean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return hospitallist;

	}
}
