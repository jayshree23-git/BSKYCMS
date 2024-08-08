package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
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

import com.project.bsky.bean.DgoReportDto;
import com.project.bsky.service.DgoReportService;

@Service

public class DgoReportServiceImpl implements DgoReportService {
	 @PersistenceContext
	    private EntityManager entityManager;
	 
	 @Autowired
	 private Logger logger;

//	@Override
//	public List<Object> getDgoCallCenterData(String userId, String formDate, String toDate, String action,
//			String hospitalCode, Long cceId, Integer cceUserId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	 @Override
	 public Map<Long , List<Object>> getDgoCallCenterData(String userId, String formDate, String toDate, String action,	String state, String district,
				String hospitalCode, Long cceId, Integer cceUserId,Integer pageIn, Integer pageEnd) {
	        ////System.out.println(userId + " " + formDate + " " + toDate + " " + action + " "+hospitalCode+""+cceId+""+cceUserId+""+pageIn+""+pageEnd);
	        Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
			List<Object> count = new ArrayList<Object>();
			Long size = null;
	        ResultSet dgoOutBoundDetailsObj = null;
	        try {
	            StoredProcedureQuery storedProcedureQuery = this.entityManager
	                    .createStoredProcedureQuery("USP_DGO_CALL_REPORT")
	                    .registerStoredProcedureParameter("P_USER_ID", String.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_STATE", String.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_DISTRICT", String.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_CCEUSERID", Integer.class, ParameterMode.IN)
	                    .registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
	                    .registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

	            storedProcedureQuery.setParameter("P_USER_ID", userId);
	            storedProcedureQuery.setParameter("P_FROM_DATE", formDate);
	            storedProcedureQuery.setParameter("P_TO_DATE", toDate);
	            storedProcedureQuery.setParameter("P_ACTION", action);
	            storedProcedureQuery.setParameter("P_STATE", state);
	            storedProcedureQuery.setParameter("P_DISTRICT", district);
	            storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
	            storedProcedureQuery.setParameter("P_ID", cceId);
	            storedProcedureQuery.setParameter("P_CCEUSERID", cceUserId);
	            storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
				storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
	            storedProcedureQuery.execute();
	            dgoOutBoundDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
	                ////System.out.println(dgoOutBoundDetailsObj);
	                size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
	                while (dgoOutBoundDetailsObj.next()) {

	                	DgoReportDto rcBean = new DgoReportDto();
	                    if (dgoOutBoundDetailsObj != null) {
	                    	
	                        rcBean.setStatus(false);
	                    } else {
	                        rcBean.setStatus(true);
	                    }
	                    rcBean.setUrn(dgoOutBoundDetailsObj.getString(1));
	                    rcBean.setPatientName(dgoOutBoundDetailsObj.getString(2));
	                    rcBean.setMobileNo(dgoOutBoundDetailsObj.getString(3));
	                    rcBean.setBlockName(dgoOutBoundDetailsObj.getString(4));
	                    rcBean.setPanchayatName(dgoOutBoundDetailsObj.getString(5));
	                    rcBean.setVillageName(dgoOutBoundDetailsObj.getString(6));
	                    rcBean.setCallResponse(dgoOutBoundDetailsObj.getString(7));
	                    rcBean.setInvoiceNo(dgoOutBoundDetailsObj.getString(8));
	                    rcBean.setDateOfAdm(dgoOutBoundDetailsObj.getString(9));
	                    rcBean.setTotalAmountBlocked(dgoOutBoundDetailsObj.getInt(10));
	                    rcBean.setHospitalDist(dgoOutBoundDetailsObj.getString(11));
	                    rcBean.setHospitalName(dgoOutBoundDetailsObj.getString(12));
	                    rcBean.setProcedureName(dgoOutBoundDetailsObj.getString(13));
	                    rcBean.setPackageName(dgoOutBoundDetailsObj.getString(14));
	                    rcBean.setAlottedDate(dgoOutBoundDetailsObj.getString(15));
	                    rcBean.setAlternativeNo(dgoOutBoundDetailsObj.getString(16));
	                    rcBean.setTransId(dgoOutBoundDetailsObj.getString(17));
	                    rcBean.setQuestion1Response(dgoOutBoundDetailsObj.getString(18));
	                    rcBean.setQuestion2Response(dgoOutBoundDetailsObj.getString(19));
	                    rcBean.setQuestion3Response(dgoOutBoundDetailsObj.getString(20));
	                    rcBean.setQuestion4Response(dgoOutBoundDetailsObj.getString(21));
	                    rcBean.setExecutiveRemarks(dgoOutBoundDetailsObj.getString(22));
	                    rcBean.setCceId(dgoOutBoundDetailsObj.getLong(23));
	                    rcBean.setMobileActiveStatus(dgoOutBoundDetailsObj.getString(24));
	                    rcBean.setDistrictName(dgoOutBoundDetailsObj.getString(25));
	                    rcBean.setHospitalCode(dgoOutBoundDetailsObj.getString(26));
	                    rcBean.setFeedbackLoginId(dgoOutBoundDetailsObj.getString(27));
	                    rcBean.setCreatedOn(dgoOutBoundDetailsObj.getString(28));
	                    rcBean.setDialedCount(dgoOutBoundDetailsObj.getInt(29));
	                    rcBean.setDcRemarks(dgoOutBoundDetailsObj.getString(30));
	                    rcBean.setDcsubmittedDate(dgoOutBoundDetailsObj.getString(31));
	                    rcBean.setDcuserId(dgoOutBoundDetailsObj.getString(32));
	                    rcBean.setDgoRemarks(dgoOutBoundDetailsObj.getString(33));
	                    rcBean. setDgosubmittedDate(dgoOutBoundDetailsObj.getString(34));
	                    count.add(rcBean);
	                    ////System.out.println(count);
	                }
	                map.put(size, count);
	                ////System.out.println(map);
	            
	        } catch (Exception e) {
	        	logger.error(ExceptionUtils.getStackTrace(e));
	        } finally {
	            try {
	                if (dgoOutBoundDetailsObj != null) {
	                    dgoOutBoundDetailsObj.close();
	                }
	            } catch (Exception e2) {
	            	logger.error(ExceptionUtils.getStackTrace(e2));
	            }
	        }
	        return map;
	    }

}
	                
            
        
                   
	

            
                    



 
