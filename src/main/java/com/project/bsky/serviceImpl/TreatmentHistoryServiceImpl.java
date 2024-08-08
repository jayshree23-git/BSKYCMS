package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.TreatmentHistoryPerUrnBean;
import com.project.bsky.bean.Treatmenthistorybypackagecode;
import com.project.bsky.model.Txnclaimapplication;
import com.project.bsky.repository.TreatmentHistoryRepository;
import com.project.bsky.service.TreatmentHistoryService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.CurrencyConverter;
import com.project.bsky.util.DateFormat;

@Service
public class TreatmentHistoryServiceImpl implements TreatmentHistoryService {

	@Autowired
	TreatmentHistoryRepository treatmentHistoryRepository;

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Txnclaimapplication> listTreatmentHistoryData() {

		return treatmentHistoryRepository.findAll();
	}

	@Override
	public List<Treatmenthistorybypackagecode> gettrtmenthistry(String urnno, String packagecode) {
		NumberFormat myFormat = NumberFormat.getInstance();

		List<Treatmenthistorybypackagecode> gettrtmenthistry = new ArrayList<Treatmenthistorybypackagecode>();
		Date date = new Date();
		ResultSet trtmnthistry = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_GET_TRTMT_LST_BY_U_P")
					.registerStoredProcedureParameter("p_ActionCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_Urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_ActionCode", "S");
			storedProcedureQuery.setParameter("p_Urn", urnno);
			storedProcedureQuery.setParameter("p_packagecode", packagecode);
			storedProcedureQuery.execute();
			trtmnthistry = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_P_MSGOUT");
			while (trtmnthistry.next()) {
				Treatmenthistorybypackagecode trtmnthistrybyp = new Treatmenthistorybypackagecode();
				trtmnthistrybyp.setUrnno(trtmnthistry.getString(1));
				trtmnthistrybyp.setPackagecode(trtmnthistry.getString(2));
				trtmnthistrybyp.setHospitalname(trtmnthistry.getString(3));
				trtmnthistrybyp.setTotalamount(myFormat.format(Double.parseDouble(trtmnthistry.getString(4))));
				trtmnthistrybyp.setDateofadmission(CommonFileUpload.dateformate(trtmnthistry.getString(5)));
				trtmnthistrybyp.setDateofdischarge(CommonFileUpload.dateformate(trtmnthistry.getString(6)));
				trtmnthistrybyp.setPatentname(trtmnthistry.getString(7));
				trtmnthistrybyp.setPackagename(trtmnthistry.getString(8));
				trtmnthistrybyp.setStatus(trtmnthistry.getString(9));
				//// System.out.println(trtmnthistrybyp);
				gettrtmenthistry.add(trtmnthistrybyp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (trtmnthistry != null) {
					trtmnthistry.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return gettrtmenthistry;

	}

	@Override
	public List<TreatmentHistoryPerUrnBean> getTreatmentHistory(String urnno) {
		NumberFormat myFormat = NumberFormat.getInstance();
		List<TreatmentHistoryPerUrnBean> getTreatmenthistory = new ArrayList<TreatmentHistoryPerUrnBean>();
		Date date = new Date();
		ResultSet treatmenthistory = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_GET_TRTMT_LST_BY_URN")
					.registerStoredProcedureParameter("p_ActionCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_Urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_ActionCode", "S");
			storedProcedureQuery.setParameter("p_Urn", urnno);
			storedProcedureQuery.execute();
			treatmenthistory = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_P_MSGOUT");
			while (treatmenthistory.next()) {
				TreatmentHistoryPerUrnBean treatmenthistorybyurn = new TreatmentHistoryPerUrnBean();
				treatmenthistorybyurn.setUrnno(treatmenthistory.getString(1));
				treatmenthistorybyurn.setPackagecode(treatmenthistory.getString(2));
				treatmenthistorybyurn.setHospitalname(treatmenthistory.getString(3));
				treatmenthistorybyurn
						.setTotalamount(myFormat.format(Double.parseDouble(treatmenthistory.getString(4))));
				treatmenthistorybyurn.setDateofadmission(CommonFileUpload.dateformate(treatmenthistory.getString(5)));
				treatmenthistorybyurn.setDateofdischarge(CommonFileUpload.dateformate(treatmenthistory.getString(6)));
				treatmenthistorybyurn.setPatientname(treatmenthistory.getString(7));
				treatmenthistorybyurn.setPackagename(treatmenthistory.getString(8));
				treatmenthistorybyurn.setStatus(treatmenthistory.getString(9));
				treatmenthistorybyurn.setClaimId(treatmenthistory.getLong(10));
				treatmenthistorybyurn.setTransctionId(treatmenthistory.getLong(11));
				treatmenthistorybyurn.setHospitalclaimedamount(treatmenthistory.getString(12));
				treatmenthistorybyurn.setSnaapproveamount(treatmenthistory.getString(14));
				treatmenthistorybyurn.setCpdapproveamount(treatmenthistory.getString(13));
				treatmenthistorybyurn.setCpdname(treatmenthistory.getString(15));
				treatmenthistorybyurn
						.setActualDateofadmission(CommonFileUpload.dateformate(treatmenthistory.getString(16)));
				treatmenthistorybyurn
						.setActualDateofdischarge(CommonFileUpload.dateformate(treatmenthistory.getString(17)));
				treatmenthistorybyurn
						.setInvoiceNo(treatmenthistory.getString(18) != null ? treatmenthistory.getString(18) : "N/A");
				treatmenthistorybyurn.setHospitalname(treatmenthistory.getString(19));
				treatmenthistorybyurn
						.setCaseNo(treatmenthistory.getString(21) != null ? treatmenthistory.getString(21) : "N/A");
				getTreatmenthistory.add(treatmenthistorybyurn);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);

		} finally {
			try {
				if (treatmenthistory != null) {
					treatmenthistory.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}

		return getTreatmenthistory;
	}

	@Override
	public String getTreatmentHistorySna(String urnno, Long userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_GET_TRTMT_HSTRY_SNA")
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_userid", userId);
			storedProcedureQuery.setParameter("p_urn", urnno);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();

				jsonObject.put("urn", snoDetailsObj.getString(1));
				jsonObject.put("packagecode", snoDetailsObj.getString(2));
				jsonObject.put("hospitalName", snoDetailsObj.getString(3));
				if (snoDetailsObj.getString(4) != null && snoDetailsObj.getString(4) != "") {
					jsonObject.put("totalamountclaimed",
							CurrencyConverter.indianCurrencyFormat(snoDetailsObj.getString(4)));
				}
				if (snoDetailsObj.getString(5) != null && snoDetailsObj.getString(5) != "") {
					jsonObject.put("dateofadmission", DateFormat.formatDateFun(snoDetailsObj.getString(5)));
				}
				if (snoDetailsObj.getString(6) != null && snoDetailsObj.getString(6) != "") {
					jsonObject.put("dateofdischarge", DateFormat.formatDateFun(snoDetailsObj.getString(6)));
				}
				jsonObject.put("patientname", snoDetailsObj.getString(7));
				jsonObject.put("packagename", snoDetailsObj.getString(8));
				jsonObject.put("actiontype", snoDetailsObj.getString(9));
				jsonObject.put("claimid", snoDetailsObj.getLong(10));
				jsonObject.put("transactiondetailsid", snoDetailsObj.getLong(11));
				if (snoDetailsObj.getString(12) != null && snoDetailsObj.getString(12) != "") {
					jsonObject.put("hospitalclaimedamount",
							CurrencyConverter.indianCurrencyFormat(snoDetailsObj.getString(12)));
				}
				if (snoDetailsObj.getString(13) != null && snoDetailsObj.getString(13) != "") {
					jsonObject.put("cpdapprovedamount",
							CurrencyConverter.indianCurrencyFormat(snoDetailsObj.getString(13)));
				}
				if (snoDetailsObj.getString(14) != null && snoDetailsObj.getString(14) != "") {
					jsonObject.put("snoapprovedamount",
							CurrencyConverter.indianCurrencyFormat(snoDetailsObj.getString(14)));
				}
				jsonObject.put("cpdName", snoDetailsObj.getString(15));
				jsonObject.put("pendingAt", snoDetailsObj.getLong(16));
				jsonObject.put("claimStatus", snoDetailsObj.getLong(17));
				if (snoDetailsObj.getString(18) != null && snoDetailsObj.getString(18) != "") {
					jsonObject.put("actualdateofadmission", DateFormat.formatDateFun(snoDetailsObj.getString(18)));
				}
				if (snoDetailsObj.getString(19) != null && snoDetailsObj.getString(19) != "") {
					jsonObject.put("actualdateofdischarge", DateFormat.formatDateFun(snoDetailsObj.getString(19)));
				}
				jsonObject.put("claimNo", snoDetailsObj.getString(20));
				jsonObject.put("claimRaiseStatus", snoDetailsObj.getString(21));
				jsonObject.put("hospitalCode", snoDetailsObj.getString(22));
				jsonObject.put("txnpackagedetailid", snoDetailsObj.getLong(23));
				jsonObject.put("caseno", snoDetailsObj.getString(24));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return jsonArray.toString();
	}

	@Override
	public String getOldTreatmentHistorySna(String urnno, Long userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_GET_OLD_TRTMT_HSTRY")
//					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

//			storedProcedureQuery.setParameter("p_userid", userId);
			storedProcedureQuery.setParameter("p_urn", urnno);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();

				jsonObject.put("urn", snoDetailsObj.getString(1));
				jsonObject.put("chipno", snoDetailsObj.getString(2));
				jsonObject.put("claimstatus", snoDetailsObj.getString(3));
				if (snoDetailsObj.getString(4) != null && snoDetailsObj.getString(4) != "") {
					jsonObject.put("approvedamount",
							CurrencyConverter.indianCurrencyFormat(snoDetailsObj.getString(4)));
				}
				jsonObject.put("approveddate", snoDetailsObj.getString(5));
				jsonObject.put("snaapproveddate", snoDetailsObj.getString(6));
				jsonObject.put("approveddate1", DateFormat.dateConvertor(snoDetailsObj.getString(5), ""));
				jsonObject.put("snaapproveddate1", DateFormat.dateConvertor(snoDetailsObj.getString(6), ""));
				if (snoDetailsObj.getString(7) != null && snoDetailsObj.getString(7) != "") {
					jsonObject.put("snaapprovedamount",
							CurrencyConverter.indianCurrencyFormat(snoDetailsObj.getString(7)));
				}
				jsonObject.put("remarks", snoDetailsObj.getString(8));
				jsonObject.put("snaremarks", snoDetailsObj.getString(9));
				jsonObject.put("tpafinalstatus", snoDetailsObj.getString(10));
				jsonObject.put("tpafinaldecisiondate", snoDetailsObj.getString(11));
				jsonObject.put("tpafinaldecisiondate1", DateFormat.dateConvertor(snoDetailsObj.getString(11), ""));
				jsonObject.put("invoiceNo", snoDetailsObj.getString(12) != null ? snoDetailsObj.getString(12) : "N/A");
				jsonObject.put("patientName",
						snoDetailsObj.getString(13) != null ? snoDetailsObj.getString(13) : "N/A");
				jsonObject.put("dateOfAdmission",
						snoDetailsObj.getString(14) != null ? snoDetailsObj.getString(14) : "N/A");
				jsonObject.put("actualDateOfAdmission",
						snoDetailsObj.getString(15) != null ? snoDetailsObj.getString(15) : "N/A");
				jsonObject.put("dateOfDischarge",
						snoDetailsObj.getString(16) != null ? snoDetailsObj.getString(16) : "N/A");
				jsonObject.put("actualDateOfDischarge",
						snoDetailsObj.getString(17) != null ? snoDetailsObj.getString(17) : "N/A");
				jsonObject.put("dateOfAdmission1",
						snoDetailsObj.getString(14) != null ? DateFormat.dateConvertor(snoDetailsObj.getString(14), "")
								: "N/A");
				jsonObject.put("actualDateOfAdmission1",
						snoDetailsObj.getString(15) != null ? DateFormat.dateConvertor(snoDetailsObj.getString(15), "")
								: "N/A");
				jsonObject.put("dateOfDischarge1",
						snoDetailsObj.getString(16) != null ? DateFormat.dateConvertor(snoDetailsObj.getString(16), "")
								: "N/A");
				jsonObject.put("actualDateOfDischarge1",
						snoDetailsObj.getString(17) != null ? DateFormat.dateConvertor(snoDetailsObj.getString(17), "")
								: "N/A");
				jsonObject.put("hospitalName",
						snoDetailsObj.getString(18) != null ? snoDetailsObj.getString(18) : "N/A");
				jsonObject.put("hospitalCode",
						snoDetailsObj.getString(19) != null ? snoDetailsObj.getString(19) : "N/A");
				jsonArray.put(jsonObject);
				//// System.out.println("JSON OBJECT : "+jsonObject);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return jsonArray.toString();
	}

	@Override
	public String getOnGoingTreatmenthistorylist(String urnno, Long userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_ONGOING_PATIENT_DTLS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_URN", urnno.trim());
			System.out.println(urnno);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("HOSPITALNAME", rs.getString(1));
				jsonObject.put("HOSPITALCODE", rs.getString(2));
				jsonObject.put("URN", rs.getString(3));
				jsonObject.put("MEMBERID", rs.getString(4));
				jsonObject.put("MEMBERNAME", rs.getString(5));
				jsonObject.put("PATIENTGENDER", rs.getString(6));
				jsonObject.put("AGE", rs.getString(7));
				jsonObject.put("ADMISSIONDATE", rs.getString(8));
				jsonObject.put("CASENO", rs.getString(9));
				jsonObject.put("INVOICE", rs.getString(10));
				jsonObject.put("PATIENTCONTACTNUMBER", rs.getString(11));
				jsonObject.put("SPECIALITY_CODE", rs.getString(12));
				jsonObject.put("SPECIALITY_NAME", rs.getString(13));
				jsonObject.put("PACKAGENAME", rs.getString(14));
				jsonObject.put("PACKAGECODE", rs.getString(15));
				jsonObject.put("BLOCK_DATE_TIME", rs.getString(16));
				jsonObject.put("AMOUNTBLOCKED", rs.getString(17));
				jsonObject.put("PREAUTHSTATUS", rs.getString(18));
				jsonObject.put("ISEMERGENCY", rs.getString(19));
				jsonObject.put("VERIFICATIONMODE", rs.getString(20));
				jsonObject.put("DOCTORNAME", rs.getString(21));
				jsonObject.put("totalamountblocked", rs.getString(22));
				jsonObject.put("hospitaldisamount", rs.getString(23));
				jsonObject.put("claimraisestatus", rs.getString(24));
				jsonObject.put("claimraisedamount", rs.getString(25));
				jsonObject.put("cpdappamount", rs.getString(26));
				jsonObject.put("snaappamount", rs.getString(27));
				jsonObject.put("claimno", rs.getString(28));
				jsonObject.put("dateofdischarge", rs.getString(29));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return jsonArray.toString();
	}

	@Override
	public List<Object> patienttreatmnetlog(String urnno, Long userId,Long txnid) {
		List<Object> patientlist = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_TREATMENT_LOG")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_URN", urnno.trim());
			storedProcedureQuery.setParameter("P_TRANSACTIONDETAILSID", txnid);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String,Object> jsonObject= new HashedMap<>();
				jsonObject.put("urn", rs.getString(1));
				jsonObject.put("claimNo", rs.getString(2));
				jsonObject.put("patientName", rs.getString(3));
				jsonObject.put("dateOfAdmission", rs.getString(4));
				jsonObject.put("actualdateOfAdmission", rs.getString(5));
				jsonObject.put("dateOfdischarge", rs.getString(6));
				jsonObject.put("actualdateOfdischarge", rs.getString(7));
				jsonObject.put("hospitalName", rs.getString(8));
				jsonObject.put("totalAmount", rs.getString(9));
				jsonObject.put("packageCode", rs.getString(10));
				jsonObject.put("packageName", rs.getString(11));
				jsonObject.put("caseNo", rs.getString(12));
				jsonObject.put("totalamountblocked", rs.getString(13));
				jsonObject.put("claimraisestatus", rs.getString(14));
				jsonObject.put("claimraiseamount", rs.getString(15));
				jsonObject.put("cpdappamount", rs.getString(16));
				jsonObject.put("snaappamount", rs.getString(17));
				patientlist.add(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return patientlist;
	}

}
