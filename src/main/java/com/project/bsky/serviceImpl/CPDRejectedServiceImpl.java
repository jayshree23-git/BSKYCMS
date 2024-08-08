package com.project.bsky.serviceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Cpdlogbean;
import com.project.bsky.bean.Cpdrejecteddetailsbean;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.repository.ActionRemarkRepository;
import com.project.bsky.service.CPDRejectedService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;

@Service
public class CPDRejectedServiceImpl implements CPDRejectedService {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private ActionRemarkRepository actionRemarkRepository;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;

	@Autowired
	private SnoClaimProcessingDetailsImpl snoClaimProcessingDetailsImpl;

	@Autowired
	private Logger logger;
	
	@Autowired
	private SnoClaimProcessingDetailsImpl snoServiceImpl;
	
	@Override
	public List<Object> getcpdrejectedlist(CPDApproveRequestBean requestBean) {
		List<Object> cpdRejectList = new ArrayList<Object>();
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet rsData = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_REJCTD_LIST_TEST")
					.registerStoredProcedureParameter("UserID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IMPLANT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HIGH_END_DRUGS_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_WARDNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("UserID", requestBean.getUserId());
			storedProcedure.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedure.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedure.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedure.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedure.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedure.setParameter("p_mortality", requestBean.getMortality());
			storedProcedure.setParameter("p_remarks", requestBean.getDescription());
			storedProcedure.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedure.setParameter("P_PROCEDURE_CODE", requestBean.getProcedure());
			storedProcedure.setParameter("P_PACKAGE_CODE", requestBean.getPackages());
			storedProcedure.setParameter("P_IMPLANT_CODE", requestBean.getImplant());
			storedProcedure.setParameter("P_HIGH_END_DRUGS_CODE", requestBean.getHighend());
			storedProcedure.setParameter("P_WARDNAME", requestBean.getWard());
			storedProcedure.setParameter("P_TRIGGERTYPE", requestBean.getTrigger());
			storedProcedure.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedure.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedure.execute();
			rsData = (ResultSet) storedProcedure.getOutputParameterValue("P_P_MSGOUT");
			while (rsData.next()) {
				Cpdrejecteddetailsbean cpdrejectedbean = new Cpdrejecteddetailsbean();
				cpdrejectedbean.setTRANSACTIONDETAILSID(rsData.getLong(1));
				cpdrejectedbean.setClaimid(rsData.getLong(2));
				cpdrejectedbean.setURN(rsData.getString(3));
				cpdrejectedbean.setPatientName(rsData.getString(4));
				cpdrejectedbean.setPackageCode(rsData.getString(5));
				cpdrejectedbean.setCreatedOn(rsData.getString(6));
				cpdrejectedbean.setAssignedsno(rsData.getInt(7));
				cpdrejectedbean.setCURRENTTOTALAMOUNT(rsData.getString(8));
				cpdrejectedbean.setInvoiceno(rsData.getString(9));
				cpdrejectedbean.setClaimNo(rsData.getString(10));
				cpdrejectedbean.setHospitalName(rsData.getString(12));
				cpdrejectedbean.setPackageName(rsData.getString(13));
				cpdrejectedbean.setMortality(rsData.getString(14));
				cpdrejectedbean.setHospitalMortality(rsData.getString(15));
				cpdrejectedbean.setActualDateOfAdmission(DateFormat.dateConvertor(rsData.getString(16), ""));
				cpdrejectedbean.setActualDateOfDischarge(DateFormat.dateConvertor(rsData.getString(17), ""));
				cpdrejectedbean.setHospital(rsData.getString(18));
				cpdrejectedbean.setPhone(rsData.getString(19) == null ? "N?A" : rsData.getString(19));
				cpdrejectedbean.setVerificationMode(rsData.getLong(20));
				cpdrejectedbean.setTxnpackagedetailid(rsData.getLong(21));
				cpdrejectedbean.setTriggerValue(rsData.getLong(22));
				cpdrejectedbean.setTriggerMsg(rsData.getString(23));
				cpdRejectList.add(cpdrejectedbean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rsData != null)
					rsData.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return cpdRejectList;
	}

	@Override
	public String getCpdrejecteddetailsdata(Integer claimid) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		ResultSet deptDetailsObj = null;
		ResultSet deptDetailsObj1 = null;
		ResultSet deptDetailsObj2 = null;
		ResultSet deptDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		JSONArray packageBlock = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONArray cardBalanceArray = new JSONArray();
		String urn = null;
		String actualDate = null;
		String casenumber = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_REJCTD_DTLS")
					.registerStoredProcedureParameter("p_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_log_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ID", claimid);
			storedProcedure.execute();
			deptDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("P_P_MSGOUT");
			deptDetailsObj3 = (ResultSet) storedProcedure.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedure.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedure.getOutputParameterValue("p_icd_subdetails");

			if (deptDetailsObj.next()) {
				urn = deptDetailsObj.getString(1);
				actualDate = deptDetailsObj.getString(2);
				casenumber = deptDetailsObj.getString(44);
				jsonObject = new JSONObject();
				jsonObject.put("URN", deptDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(deptDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(deptDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(deptDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(deptDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", deptDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", deptDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", deptDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", deptDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", deptDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", deptDetailsObj.getString(9));
				jsonObject.put("GENDER", deptDetailsObj.getString(10));
				jsonObject.put("AGE", deptDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", deptDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", deptDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(deptDetailsObj.getString(35), deptDetailsObj.getString(36)));
				jsonObject.put("INVOICENO", deptDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", deptDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", deptDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", deptDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", deptDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", deptDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", deptDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", deptDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", deptDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", deptDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", deptDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", deptDetailsObj.getString(26));
				jsonObject.put("CLAIMID", deptDetailsObj.getString(27));
				jsonObject.put("REMARKID", deptDetailsObj.getString(28));
				jsonObject.put("REMARKS", deptDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", deptDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", deptDetailsObj.getString(31));
				jsonObject.put("packagecode", deptDetailsObj.getString(32));
				jsonObject.put("FAMILYHEADNAME", deptDetailsObj.getString(33));
				jsonObject.put("VERIFIERNAME", deptDetailsObj.getString(34));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(deptDetailsObj.getString(35)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(deptDetailsObj.getString(36)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(deptDetailsObj.getString(35), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(deptDetailsObj.getString(36), ""));
				jsonObject.put("MORTALITY", deptDetailsObj.getString(37));
				jsonObject.put("REFERRALCODE", deptDetailsObj.getString(38));
				jsonObject.put("AUTHORIZEDCODE", deptDetailsObj.getString(39));
				jsonObject.put("DISTRICTNAME", deptDetailsObj.getString(40));
				jsonObject.put("NABHFlag", deptDetailsObj.getString(41));
				jsonObject.put("Address", deptDetailsObj.getString(42));
				jsonObject.put("Statusflag", deptDetailsObj.getString(43));
				jsonObject.put("claimCaseNo", deptDetailsObj.getString(44));
				jsonObject.put("claimBillNo", deptDetailsObj.getString(45));
				jsonObject.put("PATIENT_PHOTO", deptDetailsObj.getString(46));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", deptDetailsObj.getString(47));
				jsonObject.put("INTRA_SURGERY_PHOTO", deptDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", deptDetailsObj.getString(49));
				jsonObject.put("CLAIM_NO", deptDetailsObj.getString(50));
				String mob = deptDetailsObj.getString(51);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CPDMORTALITY", deptDetailsObj.getString(52));
				jsonObject.put("verification", deptDetailsObj.getString(53));
				jsonObject.put("ispatient", deptDetailsObj.getString(54));
				jsonObject.put("Referalstatus", deptDetailsObj.getString(55));
				jsonObject.put("packageCode1",
						deptDetailsObj.getString(56) != null ? deptDetailsObj.getString(56) : "NA");
				jsonObject.put("packageName1",
						deptDetailsObj.getString(57) != null ? deptDetailsObj.getString(57) : "NA");
				jsonObject.put("subPackageCode1",
						deptDetailsObj.getString(58) != null ? deptDetailsObj.getString(58) : "NA");
				jsonObject.put("subPackageName1",
						deptDetailsObj.getString(59) != null ? deptDetailsObj.getString(59) : "NA");
				jsonObject.put("procedureCode1",
						deptDetailsObj.getString(60) != null ? deptDetailsObj.getString(60) : "NA");
				jsonObject.put("procedureName1",
						deptDetailsObj.getString(61) != null ? deptDetailsObj.getString(61) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", deptDetailsObj.getString(62));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(deptDetailsObj.getString(63), "time"));
				jsonObject.put("MEMBERID", deptDetailsObj.getString(64));
				jsonObject.put("ISEMERGENCY", deptDetailsObj.getString(65));
				jsonObject.put("OVERRIDECODE", deptDetailsObj.getString(66));
				jsonObject.put("TREATMENTDAY", deptDetailsObj.getString(67));
				jsonObject.put("DOCTORNAME", deptDetailsObj.getString(68));
				jsonObject.put("FROMHOSPITALNAME", deptDetailsObj.getString(69));
				jsonObject.put("TOHOSPITAL", deptDetailsObj.getString(70));
				jsonObject.put("DISREMARKS", deptDetailsObj.getString(71));
				jsonObject.put("TRANSACTIONDESCRIPTION", deptDetailsObj.getString(72));
				jsonObject.put("HOSPITALCATEGORYNAME", deptDetailsObj.getString(73));
				jsonObject.put("disverification", deptDetailsObj.getString(74));
				jsonObject.put("txnPackageDetailId", deptDetailsObj.getLong(75));
				jsonObject.put("Packagerate1",
						deptDetailsObj.getString(76) != null ? deptDetailsObj.getString(76) : "N/A");
				jsonObject.put("surgerydateandtime",
						deptDetailsObj.getString(77) != null ? deptDetailsObj.getString(77) : "NA");
				jsonObject.put("surgerydoctorname",
						deptDetailsObj.getString(78) != null ? deptDetailsObj.getString(78) : "NA");
				jsonObject.put("suergerycontactnumber",
						deptDetailsObj.getString(79) != null ? deptDetailsObj.getString(79) : "NA");
				jsonObject.put("suergeryregnumber",
						deptDetailsObj.getString(80) != null ? deptDetailsObj.getString(80) : "NA");
				jsonObject.put("mortalityauditreport", deptDetailsObj.getString(81));
				jsonObject.put("mortalityDoc", deptDetailsObj.getString(82));
				jsonObject.put("categoryName", deptDetailsObj.getString(83));
				details.put("actionData", jsonObject);

				deptDetailsObj1 = (ResultSet) storedProcedure.getOutputParameterValue("p_log_msgout");
				while (deptDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", deptDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", deptDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", deptDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", deptDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", deptDetailsObj1.getString(5));
					jsonObject1.put("DISCHARGESLIP", deptDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", deptDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", deptDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", deptDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", deptDetailsObj1.getString(10));
					jsonObject1.put("REMARKS", deptDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", deptDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", deptDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", deptDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", deptDetailsObj1.getString(16));
					jsonObject1.put("HOSPITALCODE", deptDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION",
							DateFormat.FormatToDateString(deptDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(deptDetailsObj.getString(35)));
					jsonArray.put(jsonObject1);
				}
				deptDetailsObj2 = (ResultSet) storedProcedure.getOutputParameterValue("p_VITAL_msgout");
				while (deptDetailsObj2.next()) {
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", deptDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", deptDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", deptDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", deptDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (deptDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", deptDetailsObj3.getString(1));
					jsonObject3.put("claimNo", deptDetailsObj3.getString(2));
					jsonObject3.put("caseNo", deptDetailsObj3.getString(3));
					jsonObject3.put("patientName", deptDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", deptDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", deptDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", deptDetailsObj3.getString(7));
					jsonObject3.put("packageCode", deptDetailsObj3.getString(8));
					jsonObject3.put("packageName", deptDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(deptDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(deptDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", deptDetailsObj3.getLong(12));
					jsonObject3.put("reportName", deptDetailsObj3.getString(13));
					jsonObject3.put("claimId", deptDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", deptDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", deptDetailsObj3.getLong(16));
					jsonObject3.put("slNo", deptDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", deptDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", deptDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", deptDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", deptDetailsObj3.getDate(21));
					jsonArray2.put(jsonObject3);
				}
				while (ictDetails.next()) {
					ictDetailsObject = new JSONObject();
					ictDetailsObject.put("icdInfoId", ictDetails.getLong(1));
					ictDetailsObject.put("txnPackageDetailsId", ictDetails.getLong(2));
					ictDetailsObject.put("icdMode", ictDetails.getString(3));
					ictDetailsObject.put("icdCode", ictDetails.getString(4));
					ictDetailsObject.put("icdName", ictDetails.getString(5));
					ictDetailsObject.put("icdModeTxt", ictDetails.getString(6));
					ictDetailsObject.put("byGroupId", ictDetails.getLong(7));
					ictDetailsArray.put(ictDetailsObject);
				}
				while (ictSubDetails.next()) {
					ictSubDetailsObject = new JSONObject();
					ictSubDetailsObject.put("icdDtlsId", ictSubDetails.getLong(1));
					ictSubDetailsObject.put("icdInfoId", ictSubDetails.getLong(2));
					ictSubDetailsObject.put("icdSubCode", ictSubDetails.getString(3));
					ictSubDetailsObject.put("icdSubName", ictSubDetails.getString(4));
					ictSubDetailsArray.put(ictSubDetailsObject);
				}
				packageBlock = packageBlocking.getMultiplePackageBlocking(urn, actualDate);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(claimid);
				multipackagecaeno = snoClaimProcessingDetailsImpl.getMultiplePackageBlockingthroughcaseno(casenumber,
						actualDate);
				cardBalanceArray=snoServiceImpl.getCardBalanceDetails(claimid, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceArray);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (deptDetailsObj != null)
					deptDetailsObj.close();
				if (deptDetailsObj1 != null)
					deptDetailsObj1.close();
				if (deptDetailsObj2 != null)
					deptDetailsObj2.close();
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();
			} catch (Exception e2) {
				throw e2;
			}
		}
		return details.toString();
	}

	@Override
	public Response saveRejectedDetails(Cpdlogbean LogBean) throws Exception {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : LogBean.getIcdFinalData()) {
				subListData.add(details.getSubList());
				details.setSubList(null);
				icdData.add(details);
			}
			detailsICD = ow.writeValueAsString(icdData);
			subDetailsICD = ow.writeValueAsString(subListData);
		} catch (Exception e) {
			throw e;
		}
		try {
			localhost = InetAddress.getLocalHost();
			getuseripaddressString = localhost.getHostAddress();
		} catch (UnknownHostException e1) {
			logger.error(ExceptionUtils.getStackTrace(e1));
		}
		try {
			Integer claimsnoInteger = null;
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_REJCTD_ACTN")
					.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PENDINGAT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMSTATUS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONREMARKID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONREMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_MORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", LogBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", LogBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", LogBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", LogBean.getAmount().toString());
			storedProcedureQuery.setParameter("P_REMARKS", LogBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", LogBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", LogBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", LogBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", LogBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", LogBean.getClaimAmount());
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", LogBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", LogBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", LogBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", LogBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", LogBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", LogBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", LogBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", LogBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", LogBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", LogBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", LogBean.getIntrasurgery());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", LogBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.setParameter("P_SNA_MORTALITY", LogBean.getSnamortality());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			if (claimsnoInteger == 1 && LogBean.getClaimStatus() == 1) {
				response.setStatus("Success");
				response.setMessage("Claim Approved Successfully");
			} else if (claimsnoInteger == 1 && LogBean.getClaimStatus() == 2) {
				response.setStatus("Success");
				response.setMessage("Claim Rejected Successfully");
			} else if (claimsnoInteger == 1 && LogBean.getClaimStatus() == 4) {
				response.setStatus("Success");
				response.setMessage("Claim Queried Successfully");
			} else if (claimsnoInteger == 1 && LogBean.getClaimStatus() == 6) {
				response.setStatus("Success");
				response.setMessage("Claim Investigated Successfully");
			} else if (claimsnoInteger == 1 && LogBean.getClaimStatus() == 8) {
				response.setStatus("Success");
				response.setMessage("Claim Reverted Successfully");
			} else if (claimsnoInteger == 1 && LogBean.getClaimStatus() == 13) {
				response.setStatus("Success");
				response.setMessage("Claim On Hold");
			}
			else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public ActionRemark getActionRemarkByid(Long remarkId) {
		return actionRemarkRepository.findById(remarkId).get();
	}

	@Override
	public List<ActionRemark> getAllActionRemarks() {
		return actionRemarkRepository.findAll();
	}

}
