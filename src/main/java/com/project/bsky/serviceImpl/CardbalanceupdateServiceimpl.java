/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.service.Cardbalanceupdateservice;

/**
 * @author hrusikesh.mohanty
 *
 */
@Service
public class CardbalanceupdateServiceimpl implements Cardbalanceupdateservice {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public String getCardBalanceDetails(String urn) {
		// TODO Auto-generated method stub
		JSONArray currentbalanceArray = new JSONArray();
		ResultSet currentbalance = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_CARDBALANCE_UPDATE_LIST")
					.registerStoredProcedureParameter("p_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_Fromdate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_todate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_currentbalance", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_ongoingdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_totalclaimdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_dischargedetails", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_URN", urn);
			storedProcedureQuery.setParameter("p_Fromdate", null);
			storedProcedureQuery.setParameter("P_todate", null);
			storedProcedureQuery.execute();

			currentbalance = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_currentbalance");
			while (currentbalance.next()) {
				JSONObject currentbalanceObject = new JSONObject();
				currentbalanceObject.put("AVAILABLEBALANCE", currentbalance.getString(1));
				currentbalanceObject.put("AMOUNTBLOCKED", currentbalance.getString(2));
				currentbalanceObject.put("CLAIMEDAMOUNT", currentbalance.getString(3));
				currentbalanceObject.put("FEMALEFUND", currentbalance.getString(4));
				currentbalanceObject.put("PolicyStartDate", currentbalance.getString(5));
				currentbalanceObject.put("PolicyEnddate", currentbalance.getString(6));
				currentbalanceObject.put("FF_INSUFFICIENTAMOUNT", currentbalance.getString(7));
				currentbalanceObject.put("INSUFFICIENTAMOUNT", currentbalance.getString(8));
				currentbalanceObject.put("memberid", currentbalance.getString(9));
				currentbalanceObject.put("claimid", currentbalance.getLong(10));
				currentbalanceArray.put(currentbalanceObject);
			}
			// //System.out.println(currentbalanceArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (currentbalance != null)
				try {
					currentbalance.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		return currentbalanceArray.toString();
	}

	@Override
	public Response refundAmount(Long userid, String memberId, String urn, Double balanceAmount, Long claimId,
			String remarks) {
		// TODO Auto-generated method stub
		// //System.out.println(userid+" : "+memberId+" : "+urn+" : "+balanceAmount+" :
		// "+claimId+" : "+remarks);
		Integer cardbalance = null;
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_CSM_SNA_REVERT_AMT_UPDATE_CARD")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_memberid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", Double.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_user_id", userid);
			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.setParameter("p_memberid", memberId);
			storedProcedureQuery.setParameter("p_amount", balanceAmount);
			storedProcedureQuery.setParameter("p_claim_id", claimId);
			storedProcedureQuery.setParameter("p_remarks", remarks);
			storedProcedureQuery.execute();

			cardbalance = (Integer) storedProcedureQuery.getOutputParameterValue("p_msg");
			if (cardbalance == 1) {
				response.setStatus("Success");
				response.setMessage("Card Balance Updated Successfully");
			} else if (cardbalance == 2) {
				response.setStatus("Failed");
				response.setMessage("Claim is Not Under Current Financial Year");
			} else {
				response.setStatus("Failed");
				response.setMessage("Something Went Worng");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Something Went Worng");
		}
		return response;
	}

}
