/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.bsky.repository.MosarkarRepository;
import com.project.bsky.util.DateFormat;

/**
 * 
 */
@Service
public class Mosarkarserviceimpl implements MosarkarRepository {
	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String getalldetailsinmosarkar(Date fromDate, Date toDate, Integer serachtype) {
		//logger.info("Inside getSnoClaimListById method of SnoClaimRaiseDetailsDaoImpl");
		JSONArray jsonArray = new JSONArray();
		ResultSet details = null;
		JSONObject jsonObject1;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_MOSARKAR_DATA")
					.registerStoredProcedureParameter("p_from_date",Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT",  void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("P_flag", serachtype);
			storedProcedureQuery.execute();
			details = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (details.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("TRANSACTIONID", details.getString(1));
				jsonObject1.put("DISTRICTCODE", details.getString(2));
				jsonObject1.put("MOSARKARINSTITUTIONID", details.getString(3));
				jsonObject1.put("HOSPITAL_NAME", details.getString(4));
				jsonObject1.put("PATIENTNAME", details.getString(5));
				jsonObject1.put("PATIENTPHONENO", details.getString(6) );
				jsonObject1.put("AGE", details.getString(7));
				jsonObject1.put("GENDER", details.getString(8));
				jsonObject1.put("TOTALAMOUNTCLAIMED", details.getString(9));
				jsonObject1.put("INVOICENO", details.getString(10));
				jsonObject1.put("ACTUALDATEOFDISCHARGE",details.getString(11));
				jsonObject1.put("PACKAGENAME", details.getString(12));
				jsonObject1.put("CLAIM_NO", details.getString(13));
				jsonObject1.put("CLAIMID", details.getString(14));
				jsonObject1.put("CASENO", details.getString(15));
				jsonObject1.put("TRANSACTIONDETAILSID", details.getString(16));
				jsonObject1.put("RN", details.getString(17));
				jsonObject1.put("DISTRICTNAME", details.getString(18));
				jsonArray.put(jsonObject1);
			}

		} catch (Exception e) {
			logger.error("Exception Occurred in getalldetailsinmosarkar() of Mosarkarserviceimpl:", e);
			e.printStackTrace();
		} finally {
			try {
				if (details != null) {
					details.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getalldetailsinmosarkar() of Mosarkarserviceimpl:", e2);
			}
		}
		return jsonArray.toString();
	}

}
