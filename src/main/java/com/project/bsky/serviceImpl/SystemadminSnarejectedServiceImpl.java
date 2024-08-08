package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.SnoClaimDetails;
import com.project.bsky.bean.SystemadminSnarejectedBean;
import com.project.bsky.bean.Systemadminsnarejected;
import com.project.bsky.service.SystemadminsnarejectedService;
import com.project.bsky.util.DateFormat;

@Service
public class SystemadminSnarejectedServiceImpl implements SystemadminsnarejectedService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getsystemadminsnarejectedlist(Systemadminsnarejected systemadminrejected) {
		List<Object> list = new ArrayList<Object>();
		ResultSet systemadminsnarejected = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SYS_ADM_SNA_REJCT_LST")
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_hospitalcode", systemadminrejected.getHospitalcode().trim());
			storedProcedureQuery.setParameter("p_from_date", systemadminrejected.getFromdate());
			storedProcedureQuery.setParameter("p_to_date", systemadminrejected.getTodate());
			storedProcedureQuery.setParameter("p_packagecode", systemadminrejected.getPackagecode().trim());
			storedProcedureQuery.setParameter("p_packagename", systemadminrejected.getPackageanme().trim());
			storedProcedureQuery.setParameter("p_urn", systemadminrejected.getUrnnumber().trim());
			storedProcedureQuery.execute();
			systemadminsnarejected = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (systemadminsnarejected.next()) {
				SystemadminSnarejectedBean sysadmlist = new SystemadminSnarejectedBean();
				sysadmlist.setClaimraiseby(
						systemadminsnarejected.getString(1) != null ? systemadminsnarejected.getString(1) : "N/A");
				sysadmlist.setUrn(
						systemadminsnarejected.getString(2) != null ? systemadminsnarejected.getString(2) : "N/A");
				sysadmlist.setDateofadmission(
						systemadminsnarejected.getString(3) != null ? systemadminsnarejected.getString(3) : "N/A");
				sysadmlist.setPatientname(
						systemadminsnarejected.getString(4) != null ? systemadminsnarejected.getString(4) : "N/A");
				sysadmlist.setPackagecode(
						systemadminsnarejected.getString(5) != null ? systemadminsnarejected.getString(5) : "N/A");
				sysadmlist.setProcedurename(
						systemadminsnarejected.getString(6) != null ? systemadminsnarejected.getString(6) : "N/A");
				sysadmlist.setPackagename(
						systemadminsnarejected.getString(7) != null ? systemadminsnarejected.getString(7) : "N/A");
				sysadmlist.setTotalamountclaimed(
						systemadminsnarejected.getString(8) != null ? systemadminsnarejected.getString(8) : "N/A");
				sysadmlist.setDateofdischarge(
						systemadminsnarejected.getString(9) != null ? systemadminsnarejected.getString(9) : "N/A");
				sysadmlist.setUserid(systemadminsnarejected.getLong(10));
				sysadmlist.setHospitalstatecode(
						systemadminsnarejected.getString(11) != null ? systemadminsnarejected.getString(11) : "N/A");
				sysadmlist.setTransactiondetailsid(
						systemadminsnarejected.getString(12) != null ? systemadminsnarejected.getString(12) : "N/A");
				sysadmlist.setHospitalcode(
						systemadminsnarejected.getString(13) != null ? systemadminsnarejected.getString(13) : "N/A");
				sysadmlist.setInvoiceno(
						systemadminsnarejected.getString(14) != null ? systemadminsnarejected.getString(14) : "N/A");
				sysadmlist.setAuthorizedcode(DateFormat.FormatToDateString(systemadminsnarejected.getString(15)));
				sysadmlist
						.setActualdateofdischarge(DateFormat.FormatToDateString(systemadminsnarejected.getString(16)));
				sysadmlist
						.setActualdateofadmission(DateFormat.FormatToDateString(systemadminsnarejected.getString(17)));
				sysadmlist.setCaseno(
						systemadminsnarejected.getString(18) != null ? systemadminsnarejected.getString(18) : "N/A");
				list.add(sysadmlist);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (systemadminsnarejected != null) {
					systemadminsnarejected.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}

		return list;
	}

	@Override
	public Map<Long, List<Object>> getsystemAdminSnadetails(CPDApproveRequestBean requestBean) {
		Map<Long, List<Object>> Systemadminsnarejected = new HashMap<Long, List<Object>>();
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		Long size = null;
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_snawise_systemrejected_claimlist")
					.registerStoredProcedureParameter("p_sna_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sna_userid", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_hospitalcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_MORTALITY", requestBean.getMortality());
			storedProcedureQuery.setParameter("P_PAGE_IN", requestBean.getPageIn());
			storedProcedureQuery.setParameter("P_PAGE_END", requestBean.getPageEnd());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_out");
			while (snoDetailsObj.next()) {
				SnoClaimDetails resBean = new SnoClaimDetails();
				resBean.setInvoiceNumber(snoDetailsObj.getString(1));
				resBean.setURN(snoDetailsObj.getString(2));
				resBean.setCaseNo(snoDetailsObj.getString(3));
				resBean.setClaimNo(snoDetailsObj.getString(4));
				resBean.setPatientName(snoDetailsObj.getString(5));
				resBean.setPhone(snoDetailsObj.getString(6) == null ? "N/A" : snoDetailsObj.getString(6));
				resBean.setHospitalCode(snoDetailsObj.getString(7));
				resBean.setHospitalName(snoDetailsObj.getString(8));
				resBean.setActualDateOfAdmission(DateFormat.FormatToDateString(snoDetailsObj.getString(9)));
				resBean.setActualDateOfDischarge(DateFormat.FormatToDateString(snoDetailsObj.getString(10)));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(11));
				resBean.setHospitalMortality(snoDetailsObj.getString(12));
				resBean.setTxnpackagedetailid(snoDetailsObj.getLong(13));
				resBean.setPackageCode(snoDetailsObj.getString(14));
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(15));
				resBean.setPackageName(snoDetailsObj.getString(16));
				resBean.setClaimraisedby(snoDetailsObj.getString(17));
				SnoclaimRaiseDetailsList.add(resBean);
			}
			Systemadminsnarejected.put(size, SnoclaimRaiseDetailsList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return Systemadminsnarejected;
	}
}
