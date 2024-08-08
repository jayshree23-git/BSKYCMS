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
import org.json.JSONArray;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.PendingClaimSnaReportBean;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.service.PendingClaimSNAReportService;
import com.project.bsky.service.SNOConfigurationService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class PendingClaimSNAReportServiceImpl implements PendingClaimSNAReportService {

	@Autowired
	private SNOConfigurationService snaconfigurationrepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public JSONArray getHospitalListBySNOId(Integer snoUserId) {
		String stateId = null;
		String districtId = null;
		JSONArray hospital = snaconfigurationrepo.getSNAConfigDetails(snoUserId, stateId, districtId);
		return hospital;

	}

	@Override
	public List<Object> getPendingSnoClaimDetails(Long snoUserId, String hospitalCode) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_CLAIM_PENDING_RPRT")
					.registerStoredProcedureParameter("p_sno_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_sno_user_id", snoUserId);
			storedProcedure.setParameter("p_hospital_code", hospitalCode);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (list.next()) {
				PendingClaimSnaReportBean details = new PendingClaimSnaReportBean();
				details.setHospitalName(list.getString(1));
				details.setClaimNo(list.getString(2));
				String str = null;
				String cpdName = list.getString(3);
				if (cpdName != null) {
					details.setCpdName(cpdName);
				} else {
					details.setCpdName("N/A");
				}
				String alotDate = list.getString(4);
				if (alotDate != null) {
					Date f = new SimpleDateFormat("dd-MM-yyyy").parse(list.getString(4));
					str = new SimpleDateFormat("dd-MMM-yyyy").format(f);
					details.setAllotedDate(str);
				} else {
					details.setAllotedDate("N/A");
				}
				details.setPackageCode(list.getString(5));
				String totalclaimAmout = list.getString(6);
				if (totalclaimAmout != null) {
					details.setTotalAmountClaimed(totalclaimAmout);
				} else {
					details.setTotalAmountClaimed("0");
				}

				details.setAppliedType(list.getString(8));
				details.setClaimId(list.getString(7));
				String packageName = list.getString(9);
				if (packageName != null) {
					details.setPackageName(list.getString(9));
				} else {
					details.setPackageName("N/A");
				}
				String Urn = list.getString(10);
				if (Urn != null) {
					details.setUrn(list.getString(10));
				} else {
					details.setUrn("N/A");
				}
				String authorizedcode = list.getString(11);
				if (authorizedcode != null) {
					details.setAuthorizedcode(list.getString(11));
				} else {
					details.setAuthorizedcode("N/A");
				}
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
