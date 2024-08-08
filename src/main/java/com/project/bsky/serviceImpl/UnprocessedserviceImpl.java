package com.project.bsky.serviceImpl;

import java.sql.ResultSet;

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

import com.project.bsky.service.Unprocessedservice;
import com.project.bsky.util.DateFormat;

@Service
public class UnprocessedserviceImpl implements Unprocessedservice{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;


	@Override
	public String getdetailsunrocessed(Long snoid, String fromdate, String todate, Long userId) throws Exception {
		JSONObject jsonObjecte;
		JSONArray unprocesseddetail = new JSONArray();
		ResultSet unprocessedsnoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_UNPROCESSED_CLIAM_ADMIN")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_snoid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_snoid", snoid);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", todate);
			storedProcedureQuery.execute();
			unprocessedsnoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
		   while(unprocessedsnoDetailsObj.next()) {
			   jsonObjecte = new JSONObject();
			   jsonObjecte.put("transactiondetailsid", unprocessedsnoDetailsObj.getLong(1));
			   jsonObjecte.put("claimid", unprocessedsnoDetailsObj.getLong(2));
			   jsonObjecte.put("urn", unprocessedsnoDetailsObj.getString(3));
			   jsonObjecte.put("patientname", unprocessedsnoDetailsObj.getString(4));
			   jsonObjecte.put("invoiceno", unprocessedsnoDetailsObj.getString(5));
			   jsonObjecte.put("createdon", unprocessedsnoDetailsObj.getString(6));
			   jsonObjecte.put("cpd_alloted_date",unprocessedsnoDetailsObj.getString(7));
			   jsonObjecte.put("packagename", unprocessedsnoDetailsObj.getString(8));
			   jsonObjecte.put("revised_date", unprocessedsnoDetailsObj.getString(9));
			   jsonObjecte.put("packagecode", unprocessedsnoDetailsObj.getString(10));
			   jsonObjecte.put("currenttotalamount", unprocessedsnoDetailsObj.getString(11));
			   jsonObjecte.put("claim_no", unprocessedsnoDetailsObj.getString(12));
			   jsonObjecte.put("authorizedcode", unprocessedsnoDetailsObj.getString(13));
			   jsonObjecte.put("hospital_name", unprocessedsnoDetailsObj.getString(14));
			   jsonObjecte.put("hospitalcode", unprocessedsnoDetailsObj.getString(15));
			   jsonObjecte.put("actualdateofadmission",DateFormat.FormatToDateString(unprocessedsnoDetailsObj.getString(16)));
			   jsonObjecte.put("actualdateofdischarge", DateFormat.FormatToDateString(unprocessedsnoDetailsObj.getString(17)));
			   jsonObjecte.put("mortality", unprocessedsnoDetailsObj.getString(18));
			   unprocesseddetail.put(jsonObjecte);
		}
		} catch (Exception e) {
			throw e;
		}finally {
			try {
				if (unprocessedsnoDetailsObj != null) {
					unprocessedsnoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
	
		return unprocesseddetail.toString();
	}

}
