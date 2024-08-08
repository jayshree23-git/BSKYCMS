package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
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

import com.project.bsky.service.CPDCountReportService;

@Service
public class CPDCountReportServiceImpl implements CPDCountReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public Map<String, Object> details(Long userid, String date, String flag) {
		Map<String, Object> list = new HashMap<>();
		List<String> header = new ArrayList<String>();
		List<String> value;
		List<List<String>> record = new ArrayList<List<String>>();
		ResultSet rs = null;
		try {
			String alloteddate = date;
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CPD_COUNT_REPORT_DETAILS")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_user_id", userid);
			storedProcedure.setParameter("P_DATE", alloteddate);
			storedProcedure.setParameter("p_event_name", flag);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int count = rsMetaData.getColumnCount();
			header.add("Sl No.");
			for (int i = 1; i <= count; i++) {
				header.add(rsMetaData.getColumnName(i));
			}
			Integer Count = 0;
			while (rs.next()) {
				value = new ArrayList<String>();
				Integer i = ++Count;
				value.add(i.toString());
				value.add(rs.getString(1));
				value.add(rs.getString(2));
				value.add(rs.getString(3));
				value.add(rs.getString(4));
				value.add(rs.getString(5));
				value.add(rs.getString(6));
				value.add(rs.getString(7));
				value.add(rs.getString(8));
				value.add(rs.getString(9));
				value.add(rs.getString(10));
				value.add(rs.getString(11));
				if (flag.equals("Resettlement") || flag.equals("Dishonour")) {
					value.add(rs.getString(12));
				}
				if (flag.equals("Unprocessed") || flag.equals("Pending at Hospital")) {
					value.add(rs.getString(12));
					value.add(rs.getString(13));
				}
				if (flag.equals("Noncompliance")) {
					value.add(rs.getString(12));
					value.add(rs.getString(13));
					value.add(rs.getString(14));
				}
				record.add(value);
			}
			list.put("header", header);
			list.put("value", record);
			list.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			list.put("header", null);
			list.put("vlaue", null);
			list.put("status", 400);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return list;
	}
}