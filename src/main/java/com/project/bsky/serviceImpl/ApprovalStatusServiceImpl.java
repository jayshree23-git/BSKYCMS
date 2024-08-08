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
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.service.ApprovalStatusService;

@Service
public class ApprovalStatusServiceImpl implements ApprovalStatusService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<HospitalSpecialistListBean> getapprovalstatusllist(String hospitalcode) {
		List<HospitalSpecialistListBean> approvallist = new ArrayList<HospitalSpecialistListBean>();

		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SPECIALITY_APPROVAL_STATUS_RPT")
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_distcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_userid", null);
			storedProcedureQuery.setParameter("p_statecode", null);
			storedProcedureQuery.setParameter("p_distcode", null);
			storedProcedureQuery.setParameter("p_hospitalcode", hospitalcode);
			storedProcedureQuery.setParameter("p_action_code", 4);
			storedProcedureQuery.execute();
			list = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (list.next()) {
				HospitalSpecialistListBean approvalstatus = new HospitalSpecialistListBean();
				approvalstatus.setPackagecode(list.getString(1));
				approvalstatus.setPackagename(list.getString(2));
				approvalstatus.setAppliedon(list.getString(3));
				approvalstatus.setActiontype(list.getInt(4) == 0 ? "Approved" : "Rejected");
				if (list.getInt(4) == 0) {
					approvalstatus.setActiontype("Approved");
				} else if (list.getInt(4) == 2) {
					approvalstatus.setActiontype("Rejected");
				} else if (list.getInt(4) == 5) {
					approvalstatus.setActiontype("Pending");
				}
				approvalstatus.setActionon(list.getString(5));
				approvalstatus.setActionby(list.getString(6));
				approvallist.add(approvalstatus);
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
		return approvallist;
	}
}
