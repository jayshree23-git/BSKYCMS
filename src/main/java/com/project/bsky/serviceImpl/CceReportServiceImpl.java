package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

import com.project.bsky.bean.CceReportDto;
import com.project.bsky.service.CceReportService;

@Service

public class CceReportServiceImpl implements CceReportService {

	 @PersistenceContext
	    private EntityManager entityManager;
	 
	 @Autowired
	 private Logger logger;
	 
	@Override
	public List<Object> getCceReport(String userId,String formDate, String toDate, String action, String hospitalCode) {
		 ////System.out.println(userId + " " + formDate + " " + toDate + " " + action + " "+hospitalCode);
		List<Object> CceReport = new ArrayList<Object>();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        ResultSet trackObj = null;
		DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CCE_OUTBOUNDCALL_REPORT")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			   storedProcedureQuery.setParameter("P_USER_ID", userId);
			   storedProcedureQuery.setParameter("P_ACTION", action);
	            storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
	            storedProcedureQuery.setParameter("P_TO_DATE", toDate);
	            storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
            storedProcedureQuery.execute();
			trackObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
            while (trackObj.next()) {
            	CceReportDto resBean = new CceReportDto();
                ////System.out.println("Inside While");
                ////System.out.println(trackObj.getString(1));
                resBean.setTotalCompleted(trackObj.getString(1));
                resBean.setTotalConnectedCall(trackObj.getString(2));
                resBean.setTotalNotConnectedCall(trackObj.getString(4));
                resBean.setTotalNoCall(trackObj.getString(3));
                resBean.setTotalCount(trackObj.getString(5));
				////System.out.println(resBean);
				CceReport.add(resBean);

			}

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		finally {
			try {
				if (trackObj != null)
					trackObj.close();
			}
			catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));

			}
		}
		return CceReport;
	}

	@Override
	public List<Object> getCceTotalCountedDetails(String userId,String formDate, String toDate, String action, String hospitalCode) {
		////System.out.println(formDate+toDate+userId+action+hospitalCode);
		List<Object> TotalCountedList = new ArrayList<Object>();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        ResultSet trackObj = null;
		DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CCE_OUTBOUNDCALL_REPORT")
					.registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			   storedProcedureQuery.setParameter("P_USER_ID", userId);
			   storedProcedureQuery.setParameter("P_ACTION", action);
	            storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
	            storedProcedureQuery.setParameter("P_TO_DATE", toDate);
	            storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
            storedProcedureQuery.execute();
			trackObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			 while (trackObj.next()) {
	               ////System.out.println("hiii");
	               CceReportDto resBean = new CceReportDto();
	               resBean.setQuestion1(trackObj.getString(1));
	               resBean.setQuestion1n(trackObj.getString(2));
	               resBean.setQuestion2(trackObj.getString(3));
	               resBean.setQuestion2n(trackObj.getString(4));
	               resBean.setQuestion3(trackObj.getString(5));
	               resBean.setQuestion3n(trackObj.getString(6));
	               resBean.setQuestion4(trackObj.getString(7));
	               resBean.setQuestion4n(trackObj.getString(8));
//	               resBean.setDate(trackObj.getString(5));
	               ////System.out.println(resBean);
	               TotalCountedList.add(resBean);
			 }
			 } 
		catch (Exception e) {

					throw new RuntimeException(e);
				}
				finally {
					try {
						if (trackObj != null)
							trackObj.close();
					}
					catch (Exception e2) {
						logger.error(ExceptionUtils.getStackTrace(e2));

					}
				}
				return TotalCountedList;
				
			}

}
