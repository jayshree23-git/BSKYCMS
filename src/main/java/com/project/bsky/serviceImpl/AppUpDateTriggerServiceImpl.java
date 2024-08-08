/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.Date;
import java.sql.ResultSet;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.GetServiceUpdateRecordBean;
import com.project.bsky.bean.GetServiceUpdateRecordReq;
import com.project.bsky.service.AppUpDateTriggerService;

/**
 * @author preetam.mishra
 *
 */
@Service
public class AppUpDateTriggerServiceImpl implements AppUpDateTriggerService {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public GetServiceUpdateRecordBean getAppServiceStatus(Integer onlineServiceId) {
		GetServiceUpdateRecordBean resBean = new GetServiceUpdateRecordBean();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_app_update_trigger")
					.registerStoredProcedureParameter("p_onlineServiceId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_onlineServiceId", onlineServiceId);
			storedProcedureQuery.execute();
			ResultSet claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msgout");
			while (claimListObj.next()) {

				resBean.setNotingId(claimListObj.getString(1));
				resBean.setOnlineServiceApprovalId(claimListObj.getString(2));
				resBean.setTinStatus(claimListObj.getString(3));
				resBean.setOnlineServiceId(claimListObj.getString(4));
				resBean.setBitDeleteFlag(claimListObj.getString(5));
				resBean.setDtmCreatedOn(claimListObj.getString(6).toString());
				resBean.setApplicationStatus(claimListObj.getString(7));
				resBean.setApplyStatus(claimListObj.getString(8));
				resBean.setAtProcessId(claimListObj.getString(9).toString());
				resBean.setProfileId(claimListObj.getString(10));
				resBean.setUpdatedBy(claimListObj.getString(11));
				resBean.setApprovalStatus(claimListObj.getString(12));
				resBean.setQueryStatus(claimListObj.getString(13));
				resBean.setApplicationNo(claimListObj.getString(14));
				resBean.setRemark(claimListObj.getString(15));
				resBean.setCreatedBy(claimListObj.getString(16));
				resBean.setUpdatedOn(claimListObj.getString(17));
				resBean.setReSubmitStatus(claimListObj.getString(18));
				////System.out.println(resBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return resBean;
	}

	@Override
	public String insertAppUpdateInHistoryTbl(GetServiceUpdateRecordBean resBean) {
		String response = "";
		getAppServiceStatus(Integer.valueOf(resBean.getOnlineServiceId()));
		////System.out.println("from insert:" + resBean.toString());

		try {
			if (resBean.getTinStatus().equals("3") && resBean.getOnlineServiceApprovalId() != null) {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("usp_claim_app_update_insert")
						.registerStoredProcedureParameter("notingId", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("onlineServiceApprovalId", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("atProcessId", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("onlineServiceId", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("bitDeleteFlag", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("dtmCreatedOn", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("applicationStatus", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("applyStatus", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("processId", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("profileId", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("updatedBy", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("approvalStatus", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("applicationNo", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("remark", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("historyId", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("queryStatus", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("createdBy", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("updatedOn", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("reSubmitStatus", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_msgout", String.class, ParameterMode.OUT);
				storedProcedureQuery.setParameter("notingId", resBean.getNotingId());
				storedProcedureQuery.setParameter("onlineServiceApprovalId",
						Integer.valueOf(resBean.getOnlineServiceApprovalId()));
				storedProcedureQuery.setParameter("onlineServiceId", Integer.valueOf(resBean.getOnlineServiceId()));
				storedProcedureQuery.setParameter("atProcessId", Integer.valueOf(resBean.getAtProcessId()));
				storedProcedureQuery.setParameter("bitDeleteFlag", Integer.valueOf(resBean.getBitDeleteFlag()));
				storedProcedureQuery.setParameter("dtmCreatedOn", new java.util.Date(resBean.getDtmCreatedOn()));
				storedProcedureQuery.setParameter("applicationStatus", Integer.valueOf(resBean.getApplicationStatus()));
				storedProcedureQuery.setParameter("applyStatus", Integer.valueOf(resBean.getApplyStatus()));
				storedProcedureQuery.setParameter("processId", Integer.valueOf(resBean.getProcessId()));
				storedProcedureQuery.setParameter("profileId", Integer.valueOf(resBean.getProfileId()));
				storedProcedureQuery.setParameter("updatedBy", Integer.valueOf(resBean.getUpdatedBy()));
				storedProcedureQuery.setParameter("approvalStatus", Integer.valueOf(resBean.getApprovalStatus()));
				storedProcedureQuery.setParameter("applicationNo", resBean.getApplicationNo());
				storedProcedureQuery.setParameter("remark", resBean.getRemark());
				storedProcedureQuery.setParameter("historyId", "");
				storedProcedureQuery.setParameter("queryStatus", Integer.valueOf(resBean.getQueryStatus()));
				storedProcedureQuery.setParameter("createdBy", Integer.valueOf(resBean.getCreatedBy()));
				storedProcedureQuery.setParameter("updatedOn", new java.util.Date(resBean.getUpdatedOn()));
				storedProcedureQuery.setParameter("reSubmitStatus", Integer.valueOf(resBean.getReSubmitStatus()));
				storedProcedureQuery.execute();
				response = (String) storedProcedureQuery.getOutputParameterValue("p_msgout");
			} else {
				throw new Exception("Please check the tinstatus or serviceId!!");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
		return response;
	}
}
