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

import com.project.bsky.bean.TransactionclaimDumpBean;
import com.project.bsky.service.TransactionclaimDumpService;
@Service
public class TransactionclaimDumpServiceImpl implements TransactionclaimDumpService{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;
	
	@Override
	public List<Object> dischargereport(Long userId, String formdate, String todate,String stateId, String districtId,
			String hospitalId) {
		List<Object> object=new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_TRANSACTION_CLAIM_DUMP")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date",String.class, ParameterMode.IN)
				   .registerStoredProcedureParameter("p_to_date",String.class, ParameterMode.IN)
				   .registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout",void.class, ParameterMode.REF_CURSOR);
			
		storedProcedure.setParameter("p_user_id", userId);
		storedProcedure.setParameter("p_from_date",formdate);
		storedProcedure.setParameter("p_to_date",todate);
		storedProcedure.setParameter("p_hosptlcode", hospitalId);
		storedProcedure.setParameter("p_statecode", stateId);
		storedProcedure.setParameter("p_districtcode", districtId);
			
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while(list.next()) {
				TransactionclaimDumpBean hos=new TransactionclaimDumpBean();
				hos.setTOTAL_SUBMITTED(list.getString(1));
				hos.setTOTAL_NOTSUBMITTED(list.getString(2));
				hos.setTOATL_DISCHAGED(list.getString(3));
				hos.setTOTAL_UNPROCESSED(list.getString(4));
				object.add(hos);
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}

	
	
	@Override
	public List<Object> getdischargedetails(String formdate, String todate) {
		List<Object> dischargelist=new ArrayList<Object>();
		ResultSet deptDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_TRANSACTION_CLM_DTL_RPT")
					.registerStoredProcedureParameter("p_from_date",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date",String.class, ParameterMode.IN)		
			.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_from_date",formdate);
			storedProcedure.setParameter("p_to_date",todate);
			storedProcedure.execute();
		deptDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (deptDetailsObj.next()) {
				TransactionclaimDumpBean resBeanforSEarch = new TransactionclaimDumpBean();
				resBeanforSEarch.setTRANSACTIONID(deptDetailsObj.getString(1));
				resBeanforSEarch.setINVOICENO(deptDetailsObj.getString(2));
		
			resBeanforSEarch.setURN(deptDetailsObj.getString(3));
			
				resBeanforSEarch.setSTATECODE(deptDetailsObj.getString(4));
				resBeanforSEarch.setDISTRICTCODE(deptDetailsObj.getString(5));
				resBeanforSEarch.setMEMBERID(deptDetailsObj.getString(6));
				resBeanforSEarch.setFPVERIFIERID(deptDetailsObj.getString(7));
				resBeanforSEarch.setHOSPITALCODE(deptDetailsObj.getString(8));
				resBeanforSEarch.setHOSPITALAUTHORITYID(deptDetailsObj.getString(9));
				resBeanforSEarch.setTRANSACTIONCODE(deptDetailsObj.getString(10));
				resBeanforSEarch.setTRANSACTIONTYPE(deptDetailsObj.getString(11));
				resBeanforSEarch.setTRANSACTIONDATE(deptDetailsObj.getString(12));
				resBeanforSEarch.setTRANSACTIONTIME(deptDetailsObj.getString(13));
				resBeanforSEarch.setPACKAGECODE(deptDetailsObj.getString(14));
				
				resBeanforSEarch.setTOTALAMOUNTCLAIMED(deptDetailsObj.getString(15));
			    resBeanforSEarch.setTOTALAMOUNTBLOCKED(deptDetailsObj.getString(16));
				resBeanforSEarch.setINSUFFCIENTFUND(deptDetailsObj.getString(17));
				resBeanforSEarch.setINSUFFCIENTAMT(deptDetailsObj.getString(18));
				resBeanforSEarch.setNOOFDAYS(deptDetailsObj.getString(19));
				resBeanforSEarch.setDATEOFADMISSION(deptDetailsObj.getString(20));
				resBeanforSEarch.setDATEOFDISCHARGE(deptDetailsObj.getString(21));
				resBeanforSEarch.setMORTALITY(deptDetailsObj.getString(22));
				resBeanforSEarch.setTRANSACTIONDESCRIPTION(deptDetailsObj.getString(23));
				resBeanforSEarch.setAMOUNTCLAIMED(deptDetailsObj.getString(24));
			    resBeanforSEarch.setTRAVELAMOUNTCLAIMED(deptDetailsObj.getString(25));
			
				resBeanforSEarch.setCURRENTTOTALAMOUNT(deptDetailsObj.getString(26));
				resBeanforSEarch.setREVISED_DATE(deptDetailsObj.getString(27));
				resBeanforSEarch.setID(deptDetailsObj.getString(28));
				resBeanforSEarch.setPATIENTNAME(deptDetailsObj.getString(29));
				resBeanforSEarch.setFAMILYHEADNAME(deptDetailsObj.getString(30));
				resBeanforSEarch.setVERIFIERNAME(deptDetailsObj.getString(31));
				resBeanforSEarch.setTRAVELAMOUNT(deptDetailsObj.getString(32));
				resBeanforSEarch.setGENDER(deptDetailsObj.getString(33));
				resBeanforSEarch.setUPLOADSTATUS(deptDetailsObj.getString(34));
				resBeanforSEarch.setTRANSACTION_DATE(deptDetailsObj.getString(35));
				
				resBeanforSEarch.setUNBLOCKAMOUNT(deptDetailsObj.getString(36));
				resBeanforSEarch.setROUND(deptDetailsObj.getString(37));
				resBeanforSEarch.setBLOCKCODE(deptDetailsObj.getString(38));
				resBeanforSEarch.setPANCHAYATCODE(deptDetailsObj.getString(39));
				resBeanforSEarch.setVILLAGECODE(deptDetailsObj.getString(40));
				resBeanforSEarch.setAGE(deptDetailsObj.getString(41));
				resBeanforSEarch.setSTATENAME(deptDetailsObj.getString(42));
				resBeanforSEarch.setDISTRICTNAME(deptDetailsObj.getString(43));
				resBeanforSEarch.setBLOCKNAME(deptDetailsObj.getString(44));
				resBeanforSEarch.setPANCHAYATNAME(deptDetailsObj.getString(45));
				resBeanforSEarch.setVILLAGENAME(deptDetailsObj.getString(46));
				resBeanforSEarch.setACTUALDATEOFDISCHARGE(deptDetailsObj.getString(47));
				resBeanforSEarch.setREGISTRATIONNO(deptDetailsObj.getString(48));
				resBeanforSEarch.setPOLICYSTARTDATE(deptDetailsObj.getString(49));
				resBeanforSEarch.setPOLICYENDDATE(deptDetailsObj.getString(50));
				resBeanforSEarch.setHOSPITALNAME(deptDetailsObj.getString(51));
				resBeanforSEarch.setPROCEDURENAME(deptDetailsObj.getString(52));
				resBeanforSEarch.setPACKAGENAME(deptDetailsObj.getString(53));
				
				resBeanforSEarch.setPACKAGECATEGORYCODE(deptDetailsObj.getString(54));
				
				resBeanforSEarch.setPACKAGEID(deptDetailsObj.getString(55));
				resBeanforSEarch.setHOSPITALSTATECODE(deptDetailsObj.getString(56));
				resBeanforSEarch.setHOSPITALDISTRICTCODE(deptDetailsObj.getString(57));
			    resBeanforSEarch.setACTUALDATEOFADMISSION(deptDetailsObj.getString(58));
			    resBeanforSEarch.setAUTHORIZEDCODE(deptDetailsObj.getString(59));
			    resBeanforSEarch.setMANUALTRANSACTION(deptDetailsObj.getString(60));
			    resBeanforSEarch.setCLAIMID(deptDetailsObj.getString(61));
			    resBeanforSEarch.setTRIGGERFLAG(deptDetailsObj.getString(62));
			    resBeanforSEarch.setREFERRALCODE(deptDetailsObj.getString(63));
				resBeanforSEarch.setNABH(deptDetailsObj.getString(64));
			    resBeanforSEarch.setHOSPITALCLAIMEDAMOUNT(deptDetailsObj.getString(65));
			    resBeanforSEarch.setCLAIMRAISESTATUS(deptDetailsObj.getString(66));
				resBeanforSEarch.setTRANSACTIONDETAILSID(deptDetailsObj.getString(67));
				resBeanforSEarch.setDISCHARGE_DOC(deptDetailsObj.getString(68));
				resBeanforSEarch.setSTATUSFLAG(deptDetailsObj.getString(69));
				resBeanforSEarch.setCREATEDON(deptDetailsObj.getString(70));
			
				resBeanforSEarch.setCLAIM_ID(deptDetailsObj.getString(71));
				resBeanforSEarch.setIMPLANT_DATA(deptDetailsObj.getString(72));
				resBeanforSEarch.setDATEOFDISCHARGE_TEMP(deptDetailsObj.getString(73));
				resBeanforSEarch.setCLAIM_RAISED_BY(deptDetailsObj.getString(74));
				resBeanforSEarch.setDATEOFADMISSION_TEMP(deptDetailsObj.getString(75));
				resBeanforSEarch.setPATIENTPHONENO(deptDetailsObj.getString(76));
			
				resBeanforSEarch.setSYS_REJ_STATUS(deptDetailsObj.getString(77));
				resBeanforSEarch.setSEQUENCE_ID(deptDetailsObj.getString(78));
				resBeanforSEarch.setTRANID_PK(deptDetailsObj.getString(79));
				resBeanforSEarch.setREC_CR_DT(deptDetailsObj.getString(80));
				resBeanforSEarch.setBATCH_NO(deptDetailsObj.getString(81));
				resBeanforSEarch.setREJECTED_STATUS(deptDetailsObj.getString(82));
			    resBeanforSEarch.setCLAIMSUBMITTED(deptDetailsObj.getString(83));
			    resBeanforSEarch.setCLAIMRAISESTATUS(deptDetailsObj.getString(84));
			dischargelist.add(resBeanforSEarch);
			}}
			catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (deptDetailsObj != null) {
						deptDetailsObj.close();
					}
				} catch (Exception e2) {
					logger.error(ExceptionUtils.getStackTrace(e2));
				}
			}
			return dischargelist;
	}

}
