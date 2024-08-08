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

import com.project.bsky.bean.PackageWiseDischargeRptBean;
import com.project.bsky.service.PackageWiseDischargeRptService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class PackageWiseDischargeRptServiceImpl implements PackageWiseDischargeRptService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getpackageWiseDischargedetails(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGEWISE_DISCHARGE_AND_CLAIM")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userId);
			if (stateId == null) {
				storedProcedure.setParameter("P_STATE_CODE", null);
			} else {
				storedProcedure.setParameter("P_STATE_CODE", stateId);
			}

			if (districtId == null) {
				storedProcedure.setParameter("P_DISTRICT_CODE", null);
			} else {
				storedProcedure.setParameter("P_DISTRICT_CODE", districtId);
			}
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
				PackageWiseDischargeRptBean details = new PackageWiseDischargeRptBean();
				details.setPackageHeader(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setNoOfDischrge(list.getString(2) == null ? "0" : list.getString(2));
				details.setDischargeAmt(list.getString(3) == null ? "0" : list.getString(3));
				details.setNoOfClaim(list.getString(4) == null ? "0" : list.getString(4));
				details.setClaimedAmt(list.getString(5) == null ? "0" : list.getString(5));
				details.setPaidAmt(list.getString(6) == null ? "0" : list.getString(6));
				details.setPackageName(list.getString(7) == null ? "N/A" : list.getString(7));
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
	public List<Object> getpackageData(Integer userId, String state, String dist, String hosp, String packageHeader,
			String fromDate, String toDate) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGEWISE_DISCHARGE_AND_CLAIM_DTLS")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_packageheadercode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userId);
			storedProcedure.setParameter("P_packageheadercode", packageHeader);
			if (state == null) {
				storedProcedure.setParameter("P_STATE_CODE", null);
			} else {
				storedProcedure.setParameter("P_STATE_CODE", state);
			}

			if (dist == null) {
				storedProcedure.setParameter("P_DISTRICT_CODE", null);
			} else {
				storedProcedure.setParameter("P_DISTRICT_CODE", dist);
			}
			if (hosp == null) {
				storedProcedure.setParameter("P_HOSPITAL_CODE", null);
			} else {
				storedProcedure.setParameter("P_HOSPITAL_CODE", hosp);
			}

			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
			storedProcedure.setParameter("P_FROM_DATE", d);
			Date d1 = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
			storedProcedure.setParameter("P_TO_DATE", d1);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (list.next()) {
				PackageWiseDischargeRptBean details = new PackageWiseDischargeRptBean();
				details.setPackageCod(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setPackageName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setNoOfDischrge(list.getString(3) == null ? "0" : list.getString(3));
				details.setDischargeAmt(list.getString(4) == null ? "0" : list.getString(4));
				details.setNoOfClaim(list.getString(5) == null ? "0" : list.getString(5));
				details.setClaimedAmt(list.getString(6) == null ? "0" : list.getString(6));
				details.setPaidAmt(list.getString(7) == null ? "0" : list.getString(7));
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
	public List<Object> getpackgebenificiarydata(Integer userId, String state, String district, String hospital,
			String fromDate, String toDate, String packageCode) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGEWISE_DISCHARGE_AND_CLAIM_BENEFICIARY_DTLS")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userId);
			storedProcedure.setParameter("P_PACKAGECODE", packageCode);
			if (state == null) {
				storedProcedure.setParameter("P_STATE_CODE", null);
			} else {
				storedProcedure.setParameter("P_STATE_CODE", state);
			}

			if (district == null) {
				storedProcedure.setParameter("P_DISTRICT_CODE", null);
			} else {
				storedProcedure.setParameter("P_DISTRICT_CODE", district);
			}
			if (hospital == null) {
				storedProcedure.setParameter("P_HOSPITAL_CODE", null);
			} else {
				storedProcedure.setParameter("P_HOSPITAL_CODE", hospital);
			}

			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
			storedProcedure.setParameter("P_FROM_DATE", d);
			Date d1 = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
			storedProcedure.setParameter("P_TO_DATE", d1);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (list.next()) {
				PackageWiseDischargeRptBean details = new PackageWiseDischargeRptBean();
				details.setStateCode(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setStateName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setDistCode(list.getString(3) == null ? "N/A" : list.getString(3));
				details.setDistrictName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setHospCode(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setHospitalName(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setUrn(list.getString(7) == null ? "N/A" : list.getString(7));
				details.setPatientName(list.getString(8) == null ? "N/A" : list.getString(8));
				details.setPatientPhone(list.getString(9) == null ? "N/A" : list.getString(9));
				details.setDateOfAdm(list.getString(10) == null ? "N/A" : list.getString(10));
				details.setActlDateAdm(list.getString(11) == null ? "N/A" : list.getString(11));
				details.setDateOfDischrge(list.getString(12) == null ? "N/A" : list.getString(12));
				details.setActlDateDischarge(list.getString(13) == null ? "N/A" : list.getString(13));
				details.setDischargeAmt(list.getString(14) == null ? "0" : list.getString(14));
				details.setClaimedAmt(list.getString(15) == null ? "0" : list.getString(15));
				details.setPaidAmt(list.getString(16) == null ? "0" : list.getString(16));
				details.setPackageCategCode(list.getString(17) == null ? "N/A" : list.getString(17));
				details.setPackageCod(list.getString(18) == null ? "N/A" : list.getString(18));
				details.setPackageName(list.getString(19) == null ? "N/A" : list.getString(19));
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
