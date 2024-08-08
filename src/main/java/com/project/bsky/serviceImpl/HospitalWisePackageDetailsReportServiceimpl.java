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

import com.project.bsky.bean.HospitalWisePackageDetailsReportBean;
import com.project.bsky.service.HospitalWisePackageDetailsReportService;
@Service
public class HospitalWisePackageDetailsReportServiceimpl implements HospitalWisePackageDetailsReportService {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;
	
	@Override
	public List<HospitalWisePackageDetailsReportBean> list(Long userid, Date fromdate, Date todate,
			String hospitalcode, String statecode, String districtcode) {
		////System.out.println(userid+" "+fromdate+" "+todate+" "+hospitalcode+" "+statecode+" "+districtcode);
		List<HospitalWisePackageDetailsReportBean> paymentlist = new ArrayList<HospitalWisePackageDetailsReportBean>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITALWISE_PACKAGE_DTLS_RPRT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedure.setParameter("p_user_id", userid);
			storedProcedure.setParameter("P_FROM_DATE", fromdate);
			storedProcedure.setParameter("P_TO_DATE", todate);
			storedProcedure.setParameter("P_STATECODE", statecode);
			storedProcedure.setParameter("P_DISTRICTCODE", districtcode);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospitalcode);
			
            storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
            ////System.out.println(list);
			while (list.next()) {
				HospitalWisePackageDetailsReportBean hospbean = new HospitalWisePackageDetailsReportBean();
				hospbean.setSTATECODE(list.getString(1));
				hospbean.setSTATENAME(list.getString(2));
				hospbean.setDISTRICTNAME(list.getString(3));
				hospbean.setHOSPITAL_CODE(list.getString(4));
				hospbean.setHOSPITAL_NAME(list.getString(5));
				hospbean.setPROCEDURENAME(list.getString(6));
				hospbean.setPACKAGENAME(list.getString(7));
				hospbean.setTotalClaimedunderpackages(list.getString(8));
				hospbean.setTotalClaimedAmountunderpackages(list.getString(9));
                ////System.out.println(hospbean);
				paymentlist.add(hospbean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return paymentlist;
	}

	}


