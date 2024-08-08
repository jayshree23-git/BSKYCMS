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

import com.project.bsky.bean.FeedbackCallingReport;
import com.project.bsky.service.FeedbackCallingReportService;

@Service

public class FeedbackCallingReportServiceImpl implements FeedbackCallingReportService {
	 @PersistenceContext
	    private EntityManager entityManager;
	 
	 @Autowired
	 private Logger logger;

	@Override
	public List<Object> getFeedbackCallingReport(String userId, String formDate, String toDate, String action,
			String hospitalCode) {
		////System.out.println( userId + " " +formDate + " " + toDate + " " + action + " "+ hospitalCode);
        List<Object> count = new ArrayList<Object>();
        ResultSet feedbackCallingReportObj = null;
        try {
        	 StoredProcedureQuery storedProcedureQuery = this.entityManager
                     .createStoredProcedureQuery("USP_DGO_CALL_REPORT")
                     .registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
                     .registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
                     .registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
                     .registerStoredProcedureParameter("P_STATE", String.class, ParameterMode.IN)
                     .registerStoredProcedureParameter("P_DISTRICT", String.class, ParameterMode.IN)
                     .registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
                     .registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
                    
                     .registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
             
             storedProcedureQuery.setParameter("P_USER_ID", userId);
             storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
             storedProcedureQuery.setParameter("P_TO_DATE", toDate);
             storedProcedureQuery.setParameter("P_ACTION", action);
             storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
             storedProcedureQuery.execute();
             feedbackCallingReportObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
                 ////System.out.println(feedbackCallingReportObj);
                 while (feedbackCallingReportObj.next()) {

                	 FeedbackCallingReport rcBean = new FeedbackCallingReport();
                	 
//                     if (feedbackCallingReportObj != null) {
//                         rcBean.setStatus(false);
//                     } else {
//                         rcBean.setStatus(true);
//                         }
                	 
                	 
//                     rcBean.setUrn(statisfiedReportObj.getString(1));
//                     rcBean.setPatientName(statisfiedReportObj.getString(2));
//                     rcBean.setMobileNo(statisfiedReportObj.getString(3));
//                     rcBean.setBlockName(statisfiedReportObj.getString(4));
//                     rcBean.setPanchayatName(statisfiedReportObj.getString(5));
//                     rcBean.setVillageName(statisfiedReportObj.getString(6));
//                     rcBean.setCallResponse(dgoOutBoundDetailsObj.getString(7));
//                     rcBean.setInvoiceNo(dgoOutBoundDetailsObj.getString(8));
//                     rcBean.setDateOfAdm(dgoOutBoundDetailsObj.getString(9));
//                     rcBean.setTotalAmountBlocked(dgoOutBoundDetailsObj.getInt(10));
//                     rcBean.setHospitalDist(dgoOutBoundDetailsObj.getString(11));
//                     rcBean.setHospitalName(dgoOutBoundDetailsObj.getString(12));
//                     rcBean.setProcedureName(dgoOutBoundDetailsObj.getString(13));
//                     rcBean.setPackageName(dgoOutBoundDetailsObj.getString(14));
//                     rcBean.setAlottedDate(dgoOutBoundDetailsObj.getString(15));
//                     rcBean.setAlternativeNo(dgoOutBoundDetailsObj.getString(16));
//                     rcBean.setTransId(dgoOutBoundDetailsObj.getString(17));
//                     rcBean.setQuestion1Response(dgoOutBoundDetailsObj.getString(18));
//                     rcBean.setQuestion2Response(dgoOutBoundDetailsObj.getString(19));
//                     rcBean.setQuestion3Response(dgoOutBoundDetailsObj.getString(20));
//                     rcBean.setQuestion4Response(dgoOutBoundDetailsObj.getString(21));
//                     rcBean.setExecutiveRemarks(dgoOutBoundDetailsObj.getString(22));
//                     rcBean.setCceId(dgoOutBoundDetailsObj.getLong(23));
//                     rcBean.setMobileActiveStatus(dgoOutBoundDetailsObj.getString(24));
//                     rcBean.setDistrictName(dgoOutBoundDetailsObj.getString(25));
//                     rcBean.setHospitalCode(dgoOutBoundDetailsObj.getString(26));
//                     rcBean.setFeedbackLoginId(dgoOutBoundDetailsObj.getString(27));
//                     rcBean.setCreatedOn(dgoOutBoundDetailsObj.getString(28));
//                     rcBean.setDialedCount(dgoOutBoundDetailsObj.getInt(29));
//                     rcBean.setDcRemarks(dgoOutBoundDetailsObj.getString(30));
//                     rcBean.setDcsubmittedDate(dgoOutBoundDetailsObj.getString(31));
//                     rcBean.setDcuserId(dgoOutBoundDetailsObj.getString(32));
//                     rcBean.setDgoRemarks(dgoOutBoundDetailsObj.getString(33));
//                     rcBean. setDgosubmittedDate(dgoOutBoundDetailsObj.getString(34));
//                     count.add(rcBean);
                     ////System.out.println(count);
                 }
             
         } catch (Exception e) {
        	 logger.error(ExceptionUtils.getStackTrace(e));
         } finally {
             try {
                 if (feedbackCallingReportObj != null) {
                	 feedbackCallingReportObj.close();
                 }
             } catch (Exception e2) {
            	 logger.error(ExceptionUtils.getStackTrace(e2));
             }
         }
         return count;
     }
	}
	

