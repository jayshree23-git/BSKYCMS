package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.PaymentFreezeBean;
import com.project.bsky.service.SnaRejectedLIstService;

@Service
public class SnaRejetedlistServiceImpl implements SnaRejectedLIstService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object> snaRejetdList(Long userId, Date fromdate, Date toDate, String stateCode, String distCode,
			String hospitalCode) {
		List<Object> snaRejetdlistList = new ArrayList<Object>();
		ResultSet claimListObj = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_SNA_REJECTED_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_state_code", stateCode);
			storedProcedureQuery.setParameter("p_dist_code", distCode);
			storedProcedureQuery.setParameter("p_hsptl_code", hospitalCode);

			storedProcedureQuery.execute();

			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (claimListObj.next()) {
				PaymentFreezeBean resBean = new PaymentFreezeBean();
				////System.out.println("Inside While");
				////System.out.println(claimListObj.getString(1));
				resBean.setTransactionDetailsId(claimListObj.getLong(1));
				resBean.setClaimid(claimListObj.getLong(2));
				resBean.setURN(claimListObj.getString(3));
				resBean.setPatientName(claimListObj.getString(4));
				resBean.setInvoiceNumber(claimListObj.getString(5));
				resBean.setCreatedOn(claimListObj.getString(6));
				resBean.setCpdAlotteddate(claimListObj.getTimestamp(7));
				resBean.setPackageName(claimListObj.getString(8));
				resBean.setActualDateOfDischarge(claimListObj.getString(9));
				resBean.setPackageCode(claimListObj.getString(10));
				resBean.setCurrentTotalAmount(claimListObj.getDouble(11));
				resBean.setClaimNo(claimListObj.getString(12));
				resBean.setHospitalcode(claimListObj.getString(13));
				resBean.setAuthorizedcode(claimListObj.getString(14));
				snaRejetdlistList.add(resBean);
				////System.out.println(resBean);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return snaRejetdlistList;
	}

}
