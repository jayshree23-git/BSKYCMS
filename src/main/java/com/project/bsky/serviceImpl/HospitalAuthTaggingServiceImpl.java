package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.ClaimRaiseBean;
import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;
import com.project.bsky.bean.CpdQueryToHospitalBean;
import com.project.bsky.bean.HospitalAuthTaggingBean;
import com.project.bsky.bean.HospitalAuthorityReportBean;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.model.HospitalAuthTagging;
import com.project.bsky.model.TxnClaimActionLog;
import com.project.bsky.repository.HospitalAuthTaggingRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.TxnActionLogrepository;
import com.project.bsky.service.HospitalAuthTaggingService;
import com.project.bsky.util.DaysBetweenDates;

@Service
public class HospitalAuthTaggingServiceImpl implements HospitalAuthTaggingService {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Autowired
	private HospitalAuthTaggingRepository hospitalAuthTaggingRepository;

	@Autowired
	private HospitalInformationRepository hospRepo;

	@Autowired
	private TxnActionLogrepository txnactionlogrepo;

	@Override
	public List<Object> gethospauthrtydetailsreport(Long userId, String hospital, String fromdate, String todate,
			Integer searchtype) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSP_DTLS_RPRT_HOSPAUTHRTY")
					.registerStoredProcedureParameter("p_fomdate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_searchtype", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userId", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospital", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_fomdate", fromdate);
			storedProcedure.setParameter("p_todate", todate);
			storedProcedure.setParameter("p_userId", userId);
			storedProcedure.setParameter("P_searchtype", searchtype);
			storedProcedure.setParameter("P_hospital", hospital);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (list.next()) {
				ReportCountBeanDetails details = new ReportCountBeanDetails();
				details.setUrn(list.getString(1));
				details.setInvoiceno(list.getString(2));
				details.setHospitalName(list.getString(3));
				details.setHospitalCode(list.getString(4));
				details.setClaimNo(list.getString(5));
				details.setClaimamount(list.getString(6));
				details.setClaimId(list.getLong(7));
				details.setPackagecode(list.getString(8));
				details.setPatentname(list.getString(9));
				details.setActDateOfDschrg(list.getString(10));
				details.setPackageName(list.getString(11));
				details.setRemarks(list.getString(12));
				details.setActDateOfAdm(list.getString(13));
				object.add(details);
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
	public TxnClaimActionLog getviewremark(Long claim, Integer type) {
		TxnClaimActionLog txnactionlog = null;
		try {
			txnactionlog = txnactionlogrepo.getviewremark(claim, type);
		} catch (Exception e) {
			txnactionlog.setRemarks("No Data found !!");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return txnactionlog;
	}

	@Override
	public List<Object> getclaimrasiedataForAuthority(String fromDate, String toDate, String type, String hospitalCode,
			Long userId) throws ParseException {
		List<Object> claimRaiseDetailsList = new ArrayList<Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date varDate = dateFormat.parse(fromDate);
		Date varDate1 = dateFormat.parse(toDate);
		String authcode = null;
		ResultSet rsResultSet = null;
		DecimalFormat df = new DecimalFormat("#,###,###,###.00");
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_hospital_auth_claim")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_FROM_DATE", varDate);
			storedProcedureQuery.setParameter("P_TO_DATE", varDate1);
			storedProcedureQuery.setParameter("P_FLAG", type);
			storedProcedureQuery.execute();
			rsResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rsResultSet.next()) {
				ClaimRaiseBean resBean = new ClaimRaiseBean();
				resBean.setClaimRaiseby(rsResultSet.getString(1));
				String getdat = rsResultSet.getString(1);
				Date date5 = new Date(getdat);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = formatter.format(date5);
				int days = DaysBetweenDates.daysCountBetweenDates(formatter.parse(strDate));
				resBean.setID(rsResultSet.getLong(2));
				resBean.setTransactionid(rsResultSet.getLong(3));
				resBean.setURN(rsResultSet.getString(4).trim());
				String dateofadmissionString = rsResultSet.getString(5);
				String q11 = dateofadmissionString.substring(0, 2);
				String q21 = dateofadmissionString.substring(2, 4);
				String q31 = dateofadmissionString.substring(4, 8);
				String q41 = q11 + "/" + q21 + "/" + q31;
				Date conditon1 = new SimpleDateFormat("dd/MM/yyyy").parse(q41);
				SimpleDateFormat qdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String sd11 = qdf1.format(conditon1);
				resBean.setDateofadmission(sd11);
				resBean.setPatientName(rsResultSet.getString(6));
				resBean.setPackageCode(rsResultSet.getString(7));
				resBean.setPackageName(rsResultSet.getString(8));
				resBean.setProcedurename(rsResultSet.getString(9));
				Double moneyString = (double) (rsResultSet.getLong(10));
				String formattedNumberWithComma = df.format(moneyString);
				resBean.setCurrentTotalAmount(String.valueOf(formattedNumberWithComma));
				String s = rsResultSet.getString(11);
				String s1 = s.substring(0, 2);
				String s2 = s.substring(2, 4);
				String s3 = s.substring(4, 8);
				String s4 = s1 + "/" + s2 + "/" + s3;
				Date ddDate = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf.format(date1);
				resBean.setDateOfDischarge(d);// dd/mm/yyyy
				resBean.setUserid(rsResultSet.getInt(12));
				resBean.setHospitalstateCode(rsResultSet.getInt(13));
				resBean.setTransactiondetailsid(rsResultSet.getString(14));
				resBean.setRemainingDateString(String.valueOf(days) + "days left");
				resBean.setHospitalcode(rsResultSet.getString(15));
				resBean.setInvoiceno(rsResultSet.getString(16));
				authcode = rsResultSet.getString(17);
				if (authcode != null) {
					authcode.substring(2);
				}
				resBean.setAuthorizedcode(authcode);
				String actualdatediString = rsResultSet.getString(18);
				String a1 = actualdatediString.substring(0, 2);
				String a2 = actualdatediString.substring(2, 4);
				String a3 = actualdatediString.substring(4, 8);
				String a4 = a1 + "/" + a2 + "/" + a3;
				Date cond = new SimpleDateFormat("dd/MM/yyyy").parse(a4);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String sd = sdf1.format(cond);
				resBean.setActualdateofdischarge(sd);
				String actualaldateodadmisssionString = rsResultSet.getString(19);
				String q1 = actualaldateodadmisssionString.substring(0, 2);
				String q2 = actualaldateodadmisssionString.substring(2, 4);
				String q3 = actualaldateodadmisssionString.substring(4, 8);
				String q4 = q1 + "/" + q2 + "/" + q3;
				Date conditon = new SimpleDateFormat("dd/MM/yyyy").parse(q4);
				SimpleDateFormat qdf = new SimpleDateFormat("dd-MMM-yyyy");
				String sd1 = qdf.format(conditon);
				resBean.setHospitalName(rsResultSet.getString(20));
				resBean.setActualdateofadmission(sd1);
				claimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (rsResultSet != null) {
					rsResultSet.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimRaiseDetailsList;
	}

	@Override
	public List<Object> getclaimQuryByCPDDataForAuthority(String fromDate, String toDate, String type,
			String hospitalCode, Long userId) throws ParseException {
		List<Object> claimRaiseDetailsList = new ArrayList<Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date varDate = dateFormat.parse(fromDate);
		Date varDate1 = dateFormat.parse(toDate);
		ResultSet deptDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_hospital_auth_claim")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_FROM_DATE", varDate);
			storedProcedureQuery.setParameter("P_TO_DATE", varDate1);
			storedProcedureQuery.setParameter("P_FLAG", type);
			storedProcedureQuery.execute();
			deptDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (deptDetailsObj.next()) {
				CpdQueryToHospitalBean resBean = new CpdQueryToHospitalBean();
				resBean.setClaimId(deptDetailsObj.getLong(1));
				resBean.setUrnNo(deptDetailsObj.getString(2));
				resBean.setPatientname(deptDetailsObj.getString(3));
				resBean.setPackageCode(deptDetailsObj.getString(4).substring(4));
				resBean.setPackagename(deptDetailsObj.getString(5));
				// resBean.setDateofadmission(deptDetailsObj.getString(6));
				String dateofadmissionString = deptDetailsObj.getString(6);
				String a = dateofadmissionString.substring(0, 2);
				String b = dateofadmissionString.substring(2, 4);
				String c = dateofadmissionString.substring(4, 8);
				String d = a + "/" + b + "/" + c;
				Date e = new SimpleDateFormat("dd/MM/yyyy").parse(d);
				SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
				String g = f.format(e);
				resBean.setDateofadmission(g);
				resBean.setCREATEDON(deptDetailsObj.getString(7));
				resBean.setCpdapprovedamount(deptDetailsObj.getString(8));
				resBean.setUpdateon(deptDetailsObj.getString(9));
				String getdat = deptDetailsObj.getString(9).substring(0, 10);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(getdat, formatter);
				Date date1 = java.util.Date
						.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = sdf.format(date1);
				//// System.out.println(strDate);
				int days = DaysBetweenDates.DaysBetweenDatesCPD_SNA(sdf.parse(strDate));
				resBean.setCpdDaysleftString(String.valueOf(days) + "days left");
				resBean.setClaim_no(deptDetailsObj.getString(10));
				resBean.setRemarks(deptDetailsObj.getString(11));
				resBean.setRemark(deptDetailsObj.getString(12));
				resBean.setInvoiceno(deptDetailsObj.getString(13));
//			    resBean.setActualdateofadmission(toDate)
				String actualdateofadmissionString = deptDetailsObj.getString(14);
				String q1 = actualdateofadmissionString.substring(0, 2);
				String q2 = actualdateofadmissionString.substring(2, 4);
				String q3 = actualdateofadmissionString.substring(4, 8);
				String q4 = q1 + "/" + q2 + "/" + q3;
				Date condition = new SimpleDateFormat("dd/MM/yyyy").parse(q4);
				SimpleDateFormat ss = new SimpleDateFormat("dd-MMM-yyyy");
				String d11 = ss.format(condition);
				resBean.setActualdateofadmission(d11);
				String actualdateofdischargeString = deptDetailsObj.getString(15);
				String q11 = actualdateofdischargeString.substring(0, 2);
				String q21 = actualdateofdischargeString.substring(2, 4);
				String q31 = actualdateofdischargeString.substring(4, 8);
				String q41 = q11 + "/" + q21 + "/" + q31;
				Date condition1 = new SimpleDateFormat("dd/MM/yyyy").parse(q41);
				SimpleDateFormat ss1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d111 = ss1.format(condition1);
				resBean.setActualdateofdischarge(d111);
				String dateofdischargeString = deptDetailsObj.getString(16);
				String q111 = dateofdischargeString.substring(0, 2);
				String q211 = dateofdischargeString.substring(2, 4);
				String q311 = dateofdischargeString.substring(4, 8);
				String q411 = q111 + "/" + q211 + "/" + q311;
				Date condition11 = new SimpleDateFormat("dd/MM/yyyy").parse(q411);
				SimpleDateFormat ss11 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1111 = ss11.format(condition11);
				resBean.setDateofdischarge(d1111);
				resBean.setHospitalCode(deptDetailsObj.getString(17));
				resBean.setHospitalName(deptDetailsObj.getString(18));
				claimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
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
		return claimRaiseDetailsList;
	}

	@Override
	public List<Object> getclaimQuryBySNADataForAuthority(String fromDate, String toDate, String type,
			String hospitalCode, Long userId) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date varDate = dateFormat.parse(fromDate);
		Date varDate1 = dateFormat.parse(toDate);
		List<Object> claimRaiseList = new ArrayList<>();
		ResultSet claimListObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_hospital_auth_claim")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_FROM_DATE", varDate);
			storedProcedureQuery.setParameter("P_TO_DATE", varDate1);
			storedProcedureQuery.setParameter("P_FLAG", type);
			storedProcedureQuery.execute();
			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (claimListObj.next()) {
				ClaimsQueriedToHospitalBySNOBean resBean = new ClaimsQueriedToHospitalBySNOBean();
				resBean.setClaimID(claimListObj.getLong(1));
				resBean.setURN(claimListObj.getString(2));
				resBean.setClaimStatus(claimListObj.getString(3).toString());
				resBean.setPendingAt(claimListObj.getString(4).toString());
				resBean.setUpdateon(claimListObj.getString(5).toString());
				resBean.setSnoapprovedamount(claimListObj.getString(6));
				resBean.setPatientName(claimListObj.getString(7).toString());
				resBean.setPackageCode(claimListObj.getString(8).toString().substring(4));
				resBean.setPackageName(claimListObj.getString(9).toString());
//				resBean.setDateofadmission(claimListObj.getString(10).toString());
				String dateofadmissionString = claimListObj.getString(10);
				String s1 = dateofadmissionString.substring(0, 2);
				String s2 = dateofadmissionString.substring(2, 4);
				String s3 = dateofadmissionString.substring(4, 8);
				String s4 = s1 + "/" + s2 + "/" + s3;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(s4);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf1.format(date11);
				resBean.setDateofadmission(d);
				resBean.setuPDATEON(claimListObj.getString(11));
				String getdat = claimListObj.getString(11).substring(0, 10);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(getdat, formatter);
				Date date1 = java.util.Date
						.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = sdf.format(date1);
				int days = DaysBetweenDates.DaysBetweenDatesCPD_SNA(sdf.parse(strDate));
				resBean.setGetDaysleft(String.valueOf(days) + "days left");
				resBean.setClaim_no(claimListObj.getString(12).toString());
				resBean.setRemarksDes(claimListObj.getString(13).toString());
				resBean.setRemark(claimListObj.getString(14).toString());
				resBean.setInvoiceno(claimListObj.getString(15));
				// resBean.setActualdateofadmission(claimListObj.getString(16));
				String Actualdateofadmission = claimListObj.getString(16);
				String s11 = Actualdateofadmission.substring(0, 2);
				String s21 = Actualdateofadmission.substring(2, 4);
				String s31 = Actualdateofadmission.substring(4, 8);
				String s41 = s11 + "/" + s21 + "/" + s31;
				Date date111 = new SimpleDateFormat("dd/MM/yyyy").parse(s41);
				SimpleDateFormat sdf11 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf11.format(date111);
				resBean.setActualdateofadmission(d1);
				// resBean.setDateofdischarge(claimListObj.getString(17));
				String Dateofdischarge = claimListObj.getString(17);
				String q11 = Dateofdischarge.substring(0, 2);
				String q21 = Dateofdischarge.substring(2, 4);
				String q31 = Dateofdischarge.substring(4, 8);
				String q41 = q11 + "/" + q21 + "/" + q31;
				Date datevalue = new SimpleDateFormat("dd/MM/yyyy").parse(q41);
				SimpleDateFormat sdate = new SimpleDateFormat("dd-MMM-yyyy");
				String data = sdate.format(datevalue);
				resBean.setDateofdischarge(data);
				// resBean.setActualdateofdischarge(claimListObj.getString(18));
				String Actualdateofdischarge = claimListObj.getString(18);
				String q111 = Actualdateofdischarge.substring(0, 2);
				String q211 = Actualdateofdischarge.substring(2, 4);
				String q311 = Actualdateofdischarge.substring(4, 8);
				String q411 = q111 + "/" + q211 + "/" + q311;
				Date datevalue1 = new SimpleDateFormat("dd/MM/yyyy").parse(q411);
				SimpleDateFormat sdate1 = new SimpleDateFormat("dd-MMM-yyyy");
				String data1 = sdate1.format(datevalue1);
				resBean.setActualdateofdischarge(data1);
				resBean.setHospitalCode(claimListObj.getString(19));
				resBean.setHospitalName(claimListObj.getString(20));
				claimRaiseList.add(resBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimRaiseList;
	}
}
