package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.project.bsky.service.PaidreportamountService;
import com.project.bsky.util.DateFormat;

@Service
public class PaidreportamountServiceImpl implements PaidreportamountService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override

	public Map<String, Object> getsearchvalue(Long userid, String username, String fromdate, String todate,
			Long groupId, String state, String districtId, String hospitalCode) throws Exception {
		////System.out.println("Inside Get Search Value");
		JSONArray value = new JSONArray();
		JSONObject json;
		ResultSet paid = null;
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			List<?> results = entityManager
					.createNativeQuery("SELECT\n" + "S.STATENAME,\n" + "D.DISTRICTNAME,\n" + "HOSPITAL_NAME\n"
							+ "FROM  HOSPITAL_INFO H\n" + "INNER JOIN STATE S ON H.STATE_CODE = S.STATECODE\n"
							+ "INNER JOIN DISTRICT D ON  H.DISTRICT_CODE = D.DISTRICTCODE\n" + "WHERE H.USER_ID = ?1")
					.setParameter(1, userid).getResultList();
			results.forEach(element -> {
				Object[] row = (Object[]) element;
				response.put("stateName", row[0]);
				response.put("districtName", row[1]);
				response.put("hospitalName", row[2]);
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_PAID_DATA")
					.registerStoredProcedureParameter("P_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Groupid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_District", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospital", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_userid", userid);
			storedProcedureQuery.setParameter("P_Groupid", groupId);
			storedProcedureQuery.setParameter("p_hospitalcode", username);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", todate);
			storedProcedureQuery.setParameter("p_state", state);
			storedProcedureQuery.setParameter("P_District", districtId);
			storedProcedureQuery.setParameter("P_hospital", hospitalCode);
			storedProcedureQuery.execute();
			paid = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (paid != null && paid.next()) {
				json = new JSONObject();
				json.put("actualdateofdischarge", paid.getDate(1) != null ? paid.getDate(1) : "N/A");
//				json.put("payment_date", paid.getDate(2) != null ? paid.getDate(2) : "N/A");
				if(paid.getDate(2)==null) {
					continue;
				}else {
				json.put("payment_date", paid.getDate(2) != null ? paid.getDate(2) : "N/A");
				}
				json.put("total_discharge", paid.getString(3));
				json.put("total_paid_case", paid.getString(4));
				json.put("total_amount", paid.getDouble(5));
				value.put(json);
			}
			response.put("value", value.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (paid != null) {
					paid.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return response;
	}

	@Override
	public Map<String, Object> getsearchvalue(String paymentdate, Integer number, String totaldischarge,
			String Hospitalcode,String groupId) throws Exception {
		JSONArray data = new JSONArray();
		
		Date date = new Date();
		Date date1 = new Date();
		String datepaymnetdate = "";
		String dateactualdateofdischarge = "";
		if (paymentdate.equals("N/A")) {
			datepaymnetdate = "";
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				date1 = formatter.parse(totaldischarge);
				SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMyyyy");
				dateactualdateofdischarge = formatter1.format(date1);

			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}

		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = formatter.parse(paymentdate);
				date1 = formatter.parse(totaldischarge);
				SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMyyyy");
				datepaymnetdate = formatter1.format(date);
				dateactualdateofdischarge = formatter1.format(date1);
				////System.out.println(date);
				////System.out.println(date1);
			} catch (ParseException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}

		JSONObject jsonobject;
		ResultSet resultSet = null;
		Map<String, Object> response = new LinkedHashMap<>();

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_DATA_DETAILS")
					.registerStoredProcedureParameter("P_number", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_paymentdate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Dischargedate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_groupid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_number", number);
			storedProcedureQuery.setParameter("P_paymentdate", datepaymnetdate);
			storedProcedureQuery.setParameter("P_Dischargedate", dateactualdateofdischarge);
			storedProcedureQuery.setParameter("P_Hospitalcode", Hospitalcode);
			storedProcedureQuery.setParameter("P_groupid", groupId);
			storedProcedureQuery.execute();
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (resultSet != null && resultSet.next()) {
				jsonobject = new JSONObject();
				jsonobject.put("actualdateofdischarge", DateFormat.FormatToDateString(resultSet.getString(1)));
				jsonobject.put("payment_date", resultSet.getDate(2)!=null?resultSet.getDate(2):"N/A");
				jsonobject.put("claim_no", resultSet.getString(3));
				jsonobject.put("DD_CHEQUE_NUMBER", resultSet.getString(4));
				jsonobject.put("urn", resultSet.getString(5));
				jsonobject.put("patientname", resultSet.getString(6));
				jsonobject.put("hospitalcode", resultSet.getString(7));
				jsonobject.put("hospitalname", resultSet.getString(8));
				jsonobject.put("invoiceno", resultSet.getString(9));
				jsonobject.put("packagecode", resultSet.getString(10));
				jsonobject.put("packagename", resultSet.getString(11));
				data.put(jsonobject);
			}
			response.put("inside", data.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return response;
	}
}
