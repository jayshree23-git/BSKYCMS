/**
 * 
 */
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

import com.project.bsky.bean.UrnWiseDataBean;
import com.project.bsky.bean.UrnWiseDetailsBean;
import com.project.bsky.service.UrnWiseActionDetailsService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class UrnWiseActionDetailsServiceImpl implements UrnWiseActionDetailsService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public UrnWiseDataBean getUrnWiseDetails(String urnNo, Long transId) {
		//System.out.println(urnNo+"--"+transId);
		UrnWiseDataBean checkData = new UrnWiseDataBean();
		List<Object> actionList = new ArrayList<Object>();
		List<Object> treatmentList = new ArrayList<Object>();
		List<Object> multipleList = new ArrayList<Object>();
		List<Object> preAuthList = new ArrayList<Object>();
		List<Object> noncomplianceList = new ArrayList<Object>();

		ResultSet basicinformation = null;
		ResultSet actionTakenHistory = null;
		ResultSet treatmentHistory = null;
		ResultSet multiplePackageBlocking = null;
		ResultSet preAuthLog = null;
		ResultSet noncompliance = null;

		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_URN_WISE_ACTION_DTLS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_transactiondetailsid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_Action_Taken_History", void.class,ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_Treatment_History", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_Multiple_Package_Blocking", void.class,ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_Pre_Auth_Log", void.class, ParameterMode.REF_CURSOR)
			        .registerStoredProcedureParameter("P_MSG_Non_Compliance_Log", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_URN", urnNo);
			storedProcedure.setParameter("P_transactiondetailsid", transId);
			storedProcedure.execute();
			basicinformation = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG");
			actionTakenHistory = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Action_Taken_History");
			treatmentHistory = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Treatment_History");
			multiplePackageBlocking = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Multiple_Package_Blocking");
			preAuthLog = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Pre_Auth_Log");
			noncompliance = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Non_Compliance_Log");
			UrnWiseDetailsBean UrnWiseDetailsBean = null;
			while (basicinformation.next()) {
				UrnWiseDetailsBean = new UrnWiseDetailsBean();
				String hospName = basicinformation.getString(1);
				if (hospName != null) {
					UrnWiseDetailsBean.setHospitalName(hospName);
				} else {
					UrnWiseDetailsBean.setHospitalName("N/A");
				}
				UrnWiseDetailsBean.setHospitalCode(basicinformation.getString(2));
				UrnWiseDetailsBean.setUrn(basicinformation.getString(3));
				UrnWiseDetailsBean.setInvoiceNumber(basicinformation.getString(4));
				UrnWiseDetailsBean.setClaimNo(basicinformation.getString(5));
				String patientname = basicinformation.getString(6);
				if (patientname != null) {
					UrnWiseDetailsBean.setPatientName(patientname);
				} else {
					UrnWiseDetailsBean.setPatientName("N/A");
				}
				String patientPhone = basicinformation.getString(7);
				if (patientPhone != null) {
					UrnWiseDetailsBean.setPatirntPhone(patientPhone);
				} else {
					UrnWiseDetailsBean.setPatirntPhone("N/A");
				}
				UrnWiseDetailsBean.setAddress(basicinformation.getString(8));
				UrnWiseDetailsBean.setDateAdmission(basicinformation.getString(9));
				UrnWiseDetailsBean.setDateDischarge(basicinformation.getString(10));
				UrnWiseDetailsBean.setActualDateAdm(basicinformation.getString(11));
				UrnWiseDetailsBean.setActualDischarge(basicinformation.getString(12));
//				UrnWiseDetailsBean.setPackRate(basicinformation.getString(13));
				UrnWiseDetailsBean.setTotalAmt(basicinformation.getString(13));
				UrnWiseDetailsBean.setClaimCaseNo(basicinformation.getString(14));
				UrnWiseDetailsBean.setHospMortality(basicinformation.getString(15));
				UrnWiseDetailsBean.setCpdMortality(basicinformation.getString(16));
				UrnWiseDetailsBean.setNABHFlag(basicinformation.getString(17));
				UrnWiseDetailsBean.setImplantData(basicinformation.getString(18));
				UrnWiseDetailsBean.setDischargeDoc(basicinformation.getString(19));
				UrnWiseDetailsBean.setAdditDocs(basicinformation.getString(20));
				UrnWiseDetailsBean.setAdditDocs1(basicinformation.getString(21));
				UrnWiseDetailsBean.setAdditionalDocs2(basicinformation.getString(22));
				UrnWiseDetailsBean.setPreSurgery(basicinformation.getString(23));
				UrnWiseDetailsBean.setPostSurgery(basicinformation.getString(24));
				UrnWiseDetailsBean.setPatientPhoto(basicinformation.getString(25));
				UrnWiseDetailsBean.setIntraSurgery(basicinformation.getString(26));
				UrnWiseDetailsBean.setCaseno(basicinformation.getString(27)== null ? "N/A":basicinformation.getString(27));
				UrnWiseDetailsBean.setPackageCode(basicinformation.getString(28)==null? "N/A":basicinformation.getString(28));
				UrnWiseDetailsBean.setPackageName(basicinformation.getString(29)==null? "N/A": basicinformation.getString(29));
				UrnWiseDetailsBean.setSubpackageCode(basicinformation.getString(30)== null ? "N/A":basicinformation.getString(30));
				UrnWiseDetailsBean.setSubpakageName(basicinformation.getString(31)==null ? "N/A":basicinformation.getString(31));
				UrnWiseDetailsBean.setProcedureCode(basicinformation.getString(32)==null ? "N/A": basicinformation.getString(32));
				UrnWiseDetailsBean.setProcedureName(basicinformation.getString(33)== null? "N/A":basicinformation.getString(33));
				UrnWiseDetailsBean.setSpecimanPhoto(basicinformation.getString(34));
				UrnWiseDetailsBean.setClaimSubmittedOn(basicinformation.getString(35));

				
			}
			checkData.setBasicinformationObj(UrnWiseDetailsBean);

			while (actionTakenHistory.next()) {
				UrnWiseDetailsBean = new UrnWiseDetailsBean();
				UrnWiseDetailsBean.setActionBy(actionTakenHistory.getString(1));
				UrnWiseDetailsBean.setActionOn(actionTakenHistory.getString(2));
				UrnWiseDetailsBean.setActionType(actionTakenHistory.getString(3));
				UrnWiseDetailsBean.setActionAmount(actionTakenHistory.getString(4));
				UrnWiseDetailsBean.setRemark(actionTakenHistory.getString(5));
				UrnWiseDetailsBean.setDescription(actionTakenHistory.getString(6));
				UrnWiseDetailsBean.setDischargeSlip(actionTakenHistory.getString(7));
				UrnWiseDetailsBean.setAdditDocs(actionTakenHistory.getString(8));
				UrnWiseDetailsBean.setAdditDocs1(actionTakenHistory.getString(9));
				UrnWiseDetailsBean.setPreSurgerypic(actionTakenHistory.getString(10));
				UrnWiseDetailsBean.setPostSurgery(actionTakenHistory.getString(11));
				UrnWiseDetailsBean.setIntSurPhoto(actionTakenHistory.getString(12));
				actionList.add(UrnWiseDetailsBean);
			}
			checkData.setActionTakenHistory(actionList);
			while (treatmentHistory.next()) {
				UrnWiseDetailsBean = new UrnWiseDetailsBean();
				UrnWiseDetailsBean.setClaimNo(treatmentHistory.getString(1));
				UrnWiseDetailsBean.setUrn(treatmentHistory.getString(2));
				UrnWiseDetailsBean.setPackageCode(treatmentHistory.getString(3));
				UrnWiseDetailsBean.setPatientName(treatmentHistory.getString(4));
				UrnWiseDetailsBean.setHospitalName(treatmentHistory.getString(5));
				UrnWiseDetailsBean.setDateAdmission(treatmentHistory.getString(6));
				UrnWiseDetailsBean.setActualDateAdm(treatmentHistory.getString(7));
				UrnWiseDetailsBean.setDateDischarge(treatmentHistory.getString(8));
				UrnWiseDetailsBean.setActualDischarge(treatmentHistory.getString(9));
				UrnWiseDetailsBean.setTotalAmtClmed(treatmentHistory.getString(10));
				UrnWiseDetailsBean.setCPDAppAmount(treatmentHistory.getString(11));
				UrnWiseDetailsBean.setSNAApprovemount(treatmentHistory.getString(12));
				UrnWiseDetailsBean.setStatus(treatmentHistory.getString(13));
				UrnWiseDetailsBean.setCPDName(treatmentHistory.getString(14));
				treatmentList.add(UrnWiseDetailsBean);
			}
			checkData.setTreatmentHistory(treatmentList);
			while (multiplePackageBlocking.next()) {
				UrnWiseDetailsBean = new UrnWiseDetailsBean();
				UrnWiseDetailsBean.setNoOfPackageBlock(multiplePackageBlocking.getString(1));
				UrnWiseDetailsBean.setPatientName(multiplePackageBlocking.getString(2));
				UrnWiseDetailsBean.setDateAdmission(multiplePackageBlocking.getString(3));
				UrnWiseDetailsBean.setDateDischarge(multiplePackageBlocking.getString(4));
				UrnWiseDetailsBean.setPackageName(multiplePackageBlocking.getString(5));
				UrnWiseDetailsBean.setClaimRaised(multiplePackageBlocking.getString(6));
				multipleList.add(UrnWiseDetailsBean);

			}
			checkData.setMultiplePackageBlocking(multipleList);
			while (preAuthLog.next()) {
				UrnWiseDetailsBean = new UrnWiseDetailsBean();
				UrnWiseDetailsBean.setAuthorityCode(preAuthLog.getString(1));
				UrnWiseDetailsBean.setApprovedAmount(preAuthLog.getString(2));
				UrnWiseDetailsBean.setHospUpDate(preAuthLog.getString(3));
				UrnWiseDetailsBean.setApprovedDate(preAuthLog.getString(4));
				UrnWiseDetailsBean.setSnaRemark(preAuthLog.getString(5));
				UrnWiseDetailsBean.setAdditDocs1(preAuthLog.getString(6));
				UrnWiseDetailsBean.setAdditionalDocs2(preAuthLog.getString(7));
				UrnWiseDetailsBean.setAdditionalDocs3(preAuthLog.getString(8));
				preAuthList.add(UrnWiseDetailsBean);
			}
			
			
			checkData.setNoncompliance(noncomplianceList);
			while (noncompliance.next()) {
				UrnWiseDetailsBean = new UrnWiseDetailsBean();
				UrnWiseDetailsBean.setCREATEDBY(noncompliance.getString(1));
				UrnWiseDetailsBean.setCURRENTCLAIMBY(noncompliance.getString(2));
				UrnWiseDetailsBean.setLASTCLAIMBY(noncompliance.getString(3));
				UrnWiseDetailsBean.setUrn(noncompliance.getString(4));
			
				noncomplianceList.add(UrnWiseDetailsBean);
			}
			
			checkData.setPreAuthLog(preAuthList);
		} 
		
		catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {

				if (basicinformation != null)
					basicinformation.close();
				if (actionTakenHistory != null)
					actionTakenHistory.close();
				if (treatmentHistory != null)
					treatmentHistory.close();
				if (multiplePackageBlocking != null)
					multiplePackageBlocking.close();
				if (preAuthLog != null)
					preAuthLog.close();
				if (noncompliance != null)
					noncompliance.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return checkData;
	}

	@Override
	public List<Object> getDishouner(String urnNo, Long clmId) {
//		//System.out.println(urnNo+"--"+clmId);

		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;

		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_URN_WISE_DISHONOR_DTLS")
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_urn", urnNo);
			storedProcedure.setParameter("P_CLAIMID", clmId);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg");
			while (list.next()) {
				UrnWiseDetailsBean urnWiseDetailsBean = new UrnWiseDetailsBean();
				urnWiseDetailsBean.setDisclaimSubmitedBy(list.getString(1));
				urnWiseDetailsBean.setDiClmStatus(list.getString(2));
				urnWiseDetailsBean.setDishonrDate(list.getString(3));
				urnWiseDetailsBean.setDisAssginedCpd(list.getString(4));
				urnWiseDetailsBean.setDisAssignedSna(list.getString(5));
				urnWiseDetailsBean.setDisPreviusCpd(list.getString(6));
				urnWiseDetailsBean.setDisAllotedDate(list.getString(7));
//				urnWiseDetailsBean.setActivityDon1(list.getString(8));
				object.add(urnWiseDetailsBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
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
	public List<Object> geturnWiseWardData(String urnNo, Long transId) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;

		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_URN_WISE_Ward_DTLS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_transactiondetailsid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_Ward", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_URN", urnNo);
			storedProcedure.setParameter("P_transactiondetailsid", transId);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Ward");
			while (list.next()) {
				UrnWiseDetailsBean urnWiseDetailsBean = new UrnWiseDetailsBean();
				urnWiseDetailsBean.setWardName(list.getString(1));
				urnWiseDetailsBean.setWardAmount(list.getString(2));
				urnWiseDetailsBean.setWardBlockDate(list.getString(3));
				urnWiseDetailsBean.setPreAuthReq(list.getString(4));
				urnWiseDetailsBean.setActivityDon1(list.getString(5));
				object.add(urnWiseDetailsBean);

			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
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
	public List<Object> getHighEndDrugData(String urnNo, Long transId) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_URN_WISE_Highend_Drugs_DTLS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_transactiondetailsid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_Highend_Drugs", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("P_URN", urnNo);
			storedProcedure.setParameter("P_transactiondetailsid", transId);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Highend_Drugs");
			while (list.next()) {
				UrnWiseDetailsBean urnWiseDetailsBean = new UrnWiseDetailsBean();
				urnWiseDetailsBean.setCode(list.getString(1));
				urnWiseDetailsBean.setName(list.getString(2));
				urnWiseDetailsBean.setUnit(list.getString(3));
				urnWiseDetailsBean.setUnit2(list.getString(4));
				urnWiseDetailsBean.setTotalPrice(list.getString(5));
				urnWiseDetailsBean.setRecomidose(list.getString(6));
				urnWiseDetailsBean.setPreAuthReq(list.getString(7));
				urnWiseDetailsBean.setActivityDon1(list.getString(8));
				object.add(urnWiseDetailsBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
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
	public List<Object> getImplantDetails(String urnNo, Long transId) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_URN_WISE_Implant_DTLS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_transactiondetailsid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_Implant", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("P_URN", urnNo);
			storedProcedure.setParameter("P_transactiondetailsid", transId);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_Implant");
			while (list.next()) {
				UrnWiseDetailsBean urnWiseDetailsBean = new UrnWiseDetailsBean();
				urnWiseDetailsBean.setCode(list.getString(1));
				urnWiseDetailsBean.setImplantName(list.getString(2));
				urnWiseDetailsBean.setProcedureCode(list.getString(3));
				urnWiseDetailsBean.setUnitPrice(list.getString(4));
				urnWiseDetailsBean.setUnit(list.getString(5));
				urnWiseDetailsBean.setTotalAmt(list.getString(6));
				urnWiseDetailsBean.setActivityDon1(list.getString(7));
				urnWiseDetailsBean.setProcedure(list.getString(8));
				object.add(urnWiseDetailsBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
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

}
