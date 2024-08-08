package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.project.bsky.bean.claimtrackingdetails;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.service.ClaimTrackingService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;

/**
 * @author jayshree.moharana
 *
 */
@Service
public class ClaimTrackingServiceImpl implements ClaimTrackingService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private HospitalInformationRepository hospitalrepo;

	@Autowired
	private Logger logger;

	@Override
	public Map<Long, List<Object>> getclaimreport(Date fromdate, Date toDate, String urn, String claimno,
			String hospitalcode, Integer searchby, Integer pageIn, Integer pageEnd) {
		Map<Long, List<Object>> claimtrackingrptadmin = new HashMap<Long, List<Object>>();
		Long size = null;
		ResultSet trackObj = null;
		List<Object> track = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_claimcasetracking_details")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimno", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_search_by", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.setParameter("p_claimno", claimno);
			storedProcedureQuery.setParameter("p_hospitalcode", hospitalcode);
			storedProcedureQuery.setParameter("p_search_by", searchby);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.execute();
			size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
			trackObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (trackObj.next()) {
				claimtrackingdetails rcBean = new claimtrackingdetails();
				rcBean.setUrn(trackObj.getString(1));
				rcBean.setCreatedon(trackObj.getString(2));
				rcBean.setHospitalcode(trackObj.getString(3));
				rcBean.setClaimstatus(trackObj.getLong(4));
				rcBean.setPendingat(trackObj.getLong(5));
				rcBean.setCLAIM_CASE_NO(trackObj.getString(6));
				rcBean.setCLAIM_AMOUNT(trackObj.getLong(7));
				rcBean.setClaimid(trackObj.getLong(8));
				rcBean.setPackagecode(trackObj.getString(9));
				rcBean.setPatientname(trackObj.getString(10));
				if (trackObj.getString(11) != null) {
					rcBean.setActualdateofdischarge(trackObj.getString(11));
					Date f = new SimpleDateFormat("yyyy-MM-dd").parse(trackObj.getString(11).substring(0, 10));
					String str = new SimpleDateFormat("dd-MMM-yyyy").format(f);
					rcBean.setActualdateofdischarge(str);
				} else {
					rcBean.setActualdateofdischarge("N/A");
				}
				if (trackObj.getString(12) != null) {
					Date f = new SimpleDateFormat("yyyy-MM-dd").parse(trackObj.getString(12).substring(0, 10));
					String str = new SimpleDateFormat("dd-MMM-yyyy").format(f);
					rcBean.setActualdateofadmission(str);
				} else {
					rcBean.setActualdateofadmission("N/A");
				}
				rcBean.setPackagename(trackObj.getString(13));
				if (trackObj.getString(14) != null) {
					rcBean.setAuthorizedcode(trackObj.getString(14));
				} else {
					rcBean.setAuthorizedcode("N/A");
				}
				rcBean.setTransactiondetailsid(trackObj.getLong(15));
				rcBean.setHospitalname(trackObj.getString(16));
				rcBean.setInvoiceno(trackObj.getString(17));
				rcBean.setCaseno(trackObj.getString(18) != null ? trackObj.getString(18) : "N/A");
				rcBean.setHospitalbillno(trackObj.getString(19) != null ? trackObj.getString(19) : "N/A");
				rcBean.setHospitalclaimno(trackObj.getString(20) != null ? trackObj.getString(20) : "N/A");
				track.add(rcBean);
			}
			claimtrackingrptadmin.put(size, track);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (trackObj != null)
					trackObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimtrackingrptadmin;
	}

	@Override
	public String getClaimdetails(Integer claimid) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		ResultSet deptDetailsObj = null;
		ResultSet deptDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("sp_claimtracking_history")
					.registerStoredProcedureParameter("p_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_ID", claimid);
			storedProcedure.execute();
			deptDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("P_P_MSGOUT");
			if (deptDetailsObj.next()) {
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
				jsonObject.put("TOTALAMOUNTBLOCKED", deptDetailsObj.getString(16));
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
				jsonObject.put("IMPLANT_DATA", deptDetailsObj.getString(49));
				jsonObject.put("CLAIM_NO", deptDetailsObj.getString(50));
				jsonObject.put("PATIENTPHONENO", deptDetailsObj.getString(51));
				jsonObject.put("CPDMORTALITY", deptDetailsObj.getString(52));
				jsonObject.put("VERIFICATIONMODE", deptDetailsObj.getString(53));
				jsonObject.put("ISPATIENTOTPVERIFIED", deptDetailsObj.getString(54));
				jsonObject.put("REFERRALAUTHSTATUS", deptDetailsObj.getString(55));
				jsonObject.put("TOTALCLAIMAMOUNT", deptDetailsObj.getString(56));
				jsonObject.put("hospitalcasenumber", deptDetailsObj.getString(57));
				jsonObject.put("caseNo", deptDetailsObj.getString(58));
				jsonObject.put("txnpackagedetailid", deptDetailsObj.getString(59));
				jsonObject.put("packageCode1",
						deptDetailsObj.getString(60) != null ? deptDetailsObj.getString(60) : "NA");
				jsonObject.put("packageName1",
						deptDetailsObj.getString(61) != null ? deptDetailsObj.getString(61) : "NA");
				jsonObject.put("subPackageCode1",
						deptDetailsObj.getString(62) != null ? deptDetailsObj.getString(62) : "NA");
				jsonObject.put("subPackageName1",
						deptDetailsObj.getString(63) != null ? deptDetailsObj.getString(63) : "NA");
				jsonObject.put("procedureCode1",
						deptDetailsObj.getString(64) != null ? deptDetailsObj.getString(64) : "NA");
				jsonObject.put("procedureName1",
						deptDetailsObj.getString(65) != null ? deptDetailsObj.getString(65) : "NA");
				jsonObject.put("OVERRIDECODE", deptDetailsObj.getString(66));
				jsonObject.put("TREATMENTDAY", deptDetailsObj.getString(67));
				jsonObject.put("DOCTORNAME", deptDetailsObj.getString(68));
				jsonObject.put("FROMHOSPITALNAME", deptDetailsObj.getString(69));
				jsonObject.put("TOHOSPITAL", deptDetailsObj.getString(70));
				jsonObject.put("DISREMARKS", deptDetailsObj.getString(71));
				jsonObject.put("TRANSACTIONDESCRIPTION", deptDetailsObj.getString(72));
				jsonObject.put("MEMBERID", deptDetailsObj.getString(73));
				jsonArray.put(jsonObject);
				try {
					StoredProcedureQuery storedProcedureQuery1 = this.entityManager
							.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_ACTION_LOG")
							.registerStoredProcedureParameter("P_CLAIMID", Integer.class, ParameterMode.IN)
							.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
					storedProcedureQuery1.setParameter("P_CLAIMID", Integer.parseInt(deptDetailsObj.getString(27)));
					storedProcedureQuery1.execute();
					deptDetailsObj1 = (ResultSet) storedProcedureQuery1.getOutputParameterValue("P_P_MSGOUT");
					while (deptDetailsObj1.next()) {
						jsonObject1 = new JSONObject();
						jsonObject1.put("APPROVEDAMOUNT", deptDetailsObj1.getString(1));
						jsonObject1.put("ACTIONTYPE", deptDetailsObj1.getString(2));
						jsonObject1.put("username", deptDetailsObj1.getString(3));
						jsonObject1.put("remarks", deptDetailsObj1.getString(4));
						jsonObject1.put("actionon", deptDetailsObj1.getString(5));
						jsonObject1.put("actionon1", DateFormat.dateConvertor(deptDetailsObj1.getString(5), "time"));
						jsonObject1.put("discharge_slip", deptDetailsObj1.getString(6));
						jsonObject1.put("additional_doc", deptDetailsObj1.getString(7));
						jsonObject1.put("additionaldoc1", deptDetailsObj1.getString(8));
						jsonObject1.put("presurgeryphoto", deptDetailsObj1.getString(9));
						jsonObject1.put("postsurgeryphoto", deptDetailsObj1.getString(10));
						jsonObject1.put("remark_id", deptDetailsObj1.getString(11));
						jsonObject1.put("HOSPITALCODE", deptDetailsObj.getString(18));
						jsonObject1.put("ACTUALDATEOFADMISSION",
								DateFormat.FormatToDateString(deptDetailsObj.getString(2)));
						jsonObject1.put("remark", deptDetailsObj1.getString(12));
						jsonObject1.put("additionaldoc2", deptDetailsObj1.getString(13));
						jsonObject1.put("patient_photo", deptDetailsObj1.getString(14));
						jsonObject1.put("specimen_removal_photo", deptDetailsObj1.getString(15));
						jsonObject1.put("intra_surgery_photo", deptDetailsObj1.getString(16));
						jsonObject1.put("groupId", deptDetailsObj1.getInt(17));
						jsonArray.put(jsonObject1);
					}
				} catch (Exception e) {
					throw e;
				} finally {
					try {
						if (deptDetailsObj1 != null)
							deptDetailsObj1.close();
					} catch (Exception e2) {
						throw e2;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (deptDetailsObj != null)
					deptDetailsObj.close();
			} catch (Exception e2) {
				throw e2;
			}
		}
		return jsonArray.toString();
	}

	@Override
	public Map<Long, List<Object>> gethospitalclaimTracking(Date fromdate, String userid, Date toDate, String urn,
			Long searchby, String hospitalcode, Integer pageIn, Integer pageEnd) {
		Map<Long, List<Object>> trackingdetailsmap = new HashMap<Long, List<Object>>();
		ResultSet trackObj = null;
		Long size = null;
		List<Object> track = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSP_CLM_TRACKING_RPT")
					.registerStoredProcedureParameter("p_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_search_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_HOSPITALCODE", userid);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_hospital", hospitalcode);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.setParameter("p_search_by", searchby);
			storedProcedureQuery.setParameter("P_PAGE_IN", pageIn);
			storedProcedureQuery.setParameter("P_PAGE_END", pageEnd);
			storedProcedureQuery.execute();
			size = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
			trackObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (trackObj.next()) {
				claimtrackingdetails rcBean = new claimtrackingdetails();
				rcBean.setUrn(trackObj.getString(1));
				rcBean.setCreatedon(trackObj.getString(2));
				rcBean.setHospitalcode(trackObj.getString(3));
				rcBean.setClaimstatus(trackObj.getLong(4));
				rcBean.setPendingat(trackObj.getLong(5));
				rcBean.setCLAIM_CASE_NO(trackObj.getString(6));
				rcBean.setCLAIM_AMOUNT(trackObj.getLong(7));
				rcBean.setClaimid(trackObj.getLong(8));
				rcBean.setPackagecode(trackObj.getString(9));
				rcBean.setPatientname(trackObj.getString(10));
				rcBean.setActualdateofdischarge(trackObj.getString(11));
				rcBean.setActualdateofadmission(trackObj.getString(12));
				rcBean.setPackagename(trackObj.getString(13));
				if (trackObj.getString(14) != null) {
					rcBean.setAuthorizedcode(trackObj.getString(14).substring(2));
				} else {
					rcBean.setAuthorizedcode("N/A");
				}
				rcBean.setTransactiondetailsid(trackObj.getLong(15));
				rcBean.setHospitalname(trackObj.getString(17));
				rcBean.setInvoiceno(trackObj.getString(16));
				rcBean.setCaseno(trackObj.getString(18));
				rcBean.setClaimcaseno(trackObj.getString(19) != null ? trackObj.getString(19) : "N/A");
				rcBean.setClaimbilldate(trackObj.getString(20));
				track.add(rcBean);
			}
			trackingdetailsmap.put(size, track);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (trackObj != null)
					trackObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return trackingdetailsmap;
	}

	@Override
	public List<HospitalInformation> dchospitallist(Long userid) {
		return hospitalrepo.dchospitallist(userid);
	}

	@Override
	public String getvilatparameterdetails(String urn) {
		ResultSet vitalParams = null;
		JSONArray vitalArray = new JSONArray();
		JSONObject jsonObject2 = new JSONObject();
		try {
			StoredProcedureQuery storedProcedureQuery1 = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_VITAL_PRAMETER")
					.registerStoredProcedureParameter("URNNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_VITAL_msgout", Integer.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery1.setParameter("URNNO", urn.trim());
			storedProcedureQuery1.execute();
			vitalParams = (ResultSet) storedProcedureQuery1.getOutputParameterValue("p_VITAL_msgout");
			while (vitalParams.next()) {
				JSONObject j = new JSONObject();
				j.put("ADM_VITALSIGN", vitalParams.getString(1));
				j.put("ADM_VITALVALUE", vitalParams.getString(2));
				j.put("DIS_VITALSIGN", vitalParams.getString(3));
				j.put("DIS_VITALVALUE", vitalParams.getString(4));
				vitalArray.put(j);
			}
			jsonObject2.put("vitalArray", vitalArray);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (vitalParams != null)
					vitalParams.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return jsonObject2.toString();
	}

}
