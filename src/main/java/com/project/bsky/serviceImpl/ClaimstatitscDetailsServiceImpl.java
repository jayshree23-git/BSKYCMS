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

import com.project.bsky.bean.ClaimStatictscsBean;
import com.project.bsky.service.ClaimstatitscDetailsService;

@Service
public class ClaimstatitscDetailsServiceImpl implements ClaimstatitscDetailsService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public List<Object> getclaimStaticStiDetailsReport(String fromDate, String toDate, String stateid,
			String districtvalue, String hospitalcode, String eventName) {
		List<Object> details = new ArrayList<Object>();
		ResultSet cliamStatiscsDetails = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIMSTATITSCDETAILS_RPT")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_stateid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_from_date", new Date(fromDate));
			storedProcedure.setParameter("p_to_date", new Date(toDate));
			storedProcedure.setParameter("p_stateid", stateid.trim());
			storedProcedure.setParameter("p_districtid", districtvalue.trim());
			storedProcedure.setParameter("p_hospitalid", hospitalcode.trim());
			storedProcedure.setParameter("p_event_name", eventName.trim());
			storedProcedure.execute();
			cliamStatiscsDetails = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (cliamStatiscsDetails.next()) {
				ClaimStatictscsBean datavalue = new ClaimStatictscsBean();
				datavalue.setClaimid(cliamStatiscsDetails.getLong(1));
				datavalue.setClaim_no(cliamStatiscsDetails.getString(2));
				datavalue.setUrn(cliamStatiscsDetails.getString(3));
				datavalue.setPackagename(cliamStatiscsDetails.getString(4));
				datavalue.setHospitalname(cliamStatiscsDetails.getString(5));
//				datavalue.setActualdateofadmission(cliamStatiscsDetails.getString(6));
				String Actualdateofadmission = cliamStatiscsDetails.getString(6);
				String s1 = Actualdateofadmission.substring(0, 2);
				String s2 = Actualdateofadmission.substring(2, 4);
				String s3 = Actualdateofadmission.substring(4, 8);
				String ssString = s1 + "/" + s2 + "/" + s3;
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(ssString);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf.format(date1);
				datavalue.setActualdateofadmission(d);
//				datavalue.setActualdateofdischarge(cliamStatiscsDetails.getString(7));
				String Actualdateofdischarge = cliamStatiscsDetails.getString(7);
				String svalue = Actualdateofdischarge.substring(0, 2);
				String svalue1 = Actualdateofdischarge.substring(2, 4);
				String svalue2 = Actualdateofdischarge.substring(4, 8);
				String ssString1 = svalue + "/" + svalue1 + "/" + svalue2;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(ssString1);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf1.format(date11);
				datavalue.setActualdateofdischarge(d1);
//			datavalue.setDateofadmission(cliamStatiscsDetails.getString(8));
				String setDateofadmission = cliamStatiscsDetails.getString(8);
				String sv1 = setDateofadmission.substring(0, 2);
				String sv2 = setDateofadmission.substring(2, 4);
				String sv3 = setDateofadmission.substring(4, 8);
				String ss1 = sv1 + "/" + sv2 + "/" + sv3;
				Date da = new SimpleDateFormat("dd/MM/yyyy").parse(ss1);
				SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
				String date = sd.format(da);
				datavalue.setDateofadmission(date);
//				datavalue.setDateofdischarge(cliamStatiscsDetails.getString(9));
				String setDateofdischarge = cliamStatiscsDetails.getString(9);
				String sv11 = setDateofdischarge.substring(0, 2);
				String sv21 = setDateofdischarge.substring(2, 4);
				String sv31 = setDateofdischarge.substring(4, 8);
				String ss11 = sv11 + "/" + sv21 + "/" + sv31;
				Date da1 = new SimpleDateFormat("dd/MM/yyyy").parse(ss11);
				SimpleDateFormat sd1 = new SimpleDateFormat("dd-MMM-yyyy");
				String datewise1 = sd1.format(da1);
				datavalue.setDateofdischarge(datewise1);
				datavalue.setAuthorizedcode(cliamStatiscsDetails.getString(10));
				datavalue.setHospitalcode(cliamStatiscsDetails.getString(11));
				datavalue.setFullname(cliamStatiscsDetails.getString(12));
				datavalue.setInvoiceno(cliamStatiscsDetails.getString(13));
				details.add(datavalue);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cliamStatiscsDetails != null)
					cliamStatiscsDetails.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}

		// TODO Auto-generated method stub
		return details;
	}

}
