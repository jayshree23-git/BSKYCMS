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

import com.project.bsky.bean.HospitalPaidClaimBean;
import com.project.bsky.service.HospitalPaidClaimService;
import com.project.bsky.util.CurrencyConverter;
import com.project.bsky.util.DateFormat;

@Service
public class HospitalPaidClaimServiceImpl implements HospitalPaidClaimService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<HospitalPaidClaimBean> gethospitalwisesummaryreportcountresult(Date formdate, Date todate,
			String userId, Integer searchtype) {
		List<HospitalPaidClaimBean> hospitalwiseclaimlist = new ArrayList<HospitalPaidClaimBean>();
		ResultSet data = null;

		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAWISE_PAYMENTSUMM_RPT")
					.registerStoredProcedureParameter("p_user_id", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_dropdownvalue", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_user_id", userId);
			storedProcedure.setParameter("p_from_date", formdate);
			storedProcedure.setParameter("p_to_date", todate);
			storedProcedure.setParameter("P_dropdownvalue", searchtype);
			storedProcedure.execute();
			data = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			if (data.next()) {
				HospitalPaidClaimBean resbean = new HospitalPaidClaimBean();
				resbean.setTotalcase(data.getString(1));
				if (data.getString(2) == null) {
					resbean.setTotalamount("N/A");
				} else {
					resbean.setTotalamount(CurrencyConverter.indianCurrencyFormat(data.getString(2)));
				}
				hospitalwiseclaimlist.add(resbean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (data != null) {
					data.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}

		return hospitalwiseclaimlist;
	}

	@Override
	public List<Object> gethospitaldetailsinnerpage(Date formdate, Date todate, Integer searchtype, String userId) {
		List<Object> listofdata = new ArrayList<Object>();
		ResultSet hospitalsummaryreport = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITALWISE_PAYMENTSUMMRY_INNERPAGE")
					.registerStoredProcedureParameter("p_user_id", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_dropdownvalue", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_user_id", userId);
			storedProcedure.setParameter("p_from_date", formdate);
			storedProcedure.setParameter("p_to_date", todate);
			storedProcedure.setParameter("P_dropdownvalue", searchtype);
			storedProcedure.execute();
			hospitalsummaryreport = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (hospitalsummaryreport.next()) {
				HospitalPaidClaimBean resbean = new HospitalPaidClaimBean();
				resbean.setUrn(hospitalsummaryreport.getString(1) != null ? hospitalsummaryreport.getString(1) : "N/A");
				resbean.setPatientname(
						hospitalsummaryreport.getString(2) != null ? hospitalsummaryreport.getString(2) : "N/A");
				resbean.setPackagecode(
						hospitalsummaryreport.getString(3) != null ? hospitalsummaryreport.getString(3) : "N/A");
				resbean.setPackagename(
						hospitalsummaryreport.getString(4) != null ? hospitalsummaryreport.getString(4) : "N/A");
				resbean.setTotalamountclaimed(
						hospitalsummaryreport.getString(5) != null ? hospitalsummaryreport.getString(5) : "N/A");
				resbean.setClaim_no(
						hospitalsummaryreport.getString(6) != null ? hospitalsummaryreport.getString(6) : "N/A");
				resbean.setPAYMENTFREEZESTATUS(userId);
				if (hospitalsummaryreport.getInt(7) == 1) {
					resbean.setPAYMENTFREEZESTATUS("Freezed");
				} else if (hospitalsummaryreport.getInt(7) == 2) {
					resbean.setPAYMENTFREEZESTATUS("Paid");
				} else if (hospitalsummaryreport.getInt(7) == 3) {
					resbean.setPAYMENTFREEZESTATUS("SNA Rejected");
				} else {
					resbean.setPAYMENTFREEZESTATUS("N/A");
				}
				resbean.setActualDateOfAdmission(DateFormat.FormatToDateString(hospitalsummaryreport.getString(9)));
				resbean.setActualDateOfDischarge(DateFormat.FormatToDateString(hospitalsummaryreport.getString(8)));
				resbean.setCaseno(
						hospitalsummaryreport.getString(10) != null ? hospitalsummaryreport.getString(10) : "N/A");
				listofdata.add(resbean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (hospitalsummaryreport != null) {
					hospitalsummaryreport.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return listofdata;
	}

}
