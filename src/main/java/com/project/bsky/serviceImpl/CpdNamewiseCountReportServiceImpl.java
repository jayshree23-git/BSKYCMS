package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.project.bsky.service.CpdNamewiseCountreportService;

@Service
public class CpdNamewiseCountReportServiceImpl implements CpdNamewiseCountreportService {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public String list(String formdate, String todate) {
		JSONObject preAuthObject = new JSONObject();
		JSONArray preauthArray = new JSONArray();
		Date formdate1 = null;
		Date todate1 = null;
		try {
			formdate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(formdate);
			todate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
		} catch (ParseException e1) {
			logger.error(ExceptionUtils.getStackTrace(e1));
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_NAMEWISE_COUNT_RPRT")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", formdate1);
			storedProcedureQuery.setParameter("p_to_date", todate1);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			while (snoDetailsObj.next()) {

				preAuthObject = new JSONObject();
				preAuthObject.put("ACTIONBY", snoDetailsObj.getString(1));
				preAuthObject.put("FULL_NAME", snoDetailsObj.getString(2));
				preAuthObject.put("CLAIM", snoDetailsObj.getString(3));
				preAuthObject.put("APPROVE", snoDetailsObj.getString(4));
				preAuthObject.put("REJECT", snoDetailsObj.getString(5));
				preAuthObject.put("QUERY", snoDetailsObj.getString(6));
				preauthArray.put(preAuthObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			}

			catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return preauthArray.toString();
	}

	@Override
	public String details(Long userId, String formdate, String todate) {
		JSONObject preAuthObject = new JSONObject();
		JSONArray preauthArray = new JSONArray();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_NAMEWISE_DTLS_RPRT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", formdate);
			storedProcedureQuery.setParameter("p_to_date", todate);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg");
			while (snoDetailsObj.next()) {
				preAuthObject = new JSONObject();
				preAuthObject.put("date", snoDetailsObj.getString(1));
				preAuthObject.put("APPROVE", snoDetailsObj.getString(3));
				preAuthObject.put("REJECT", snoDetailsObj.getString(4));
				preAuthObject.put("QUERY", snoDetailsObj.getString(5));
				preAuthObject.put("total", snoDetailsObj.getLong(2));
				preauthArray.put(preAuthObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			}

			catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return preauthArray.toString();
	}
}