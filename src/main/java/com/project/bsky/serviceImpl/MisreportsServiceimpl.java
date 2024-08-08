/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.project.bsky.bean.Authenticationbean;
import com.project.bsky.bean.CceSummaryReport;
import com.project.bsky.bean.Cpdperformancebean;
import com.project.bsky.bean.Disthospbean;
import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Hospitalwiseongoringtrtmntbean;
import com.project.bsky.bean.MisreportsBean;
import com.project.bsky.bean.Nonconnect;
import com.project.bsky.bean.ReportCountBeanDetails;
import com.project.bsky.bean.SnaunprocessedBean;
import com.project.bsky.bean.Snawiseunprocessedbean;
import com.project.bsky.bean.Treatmenthistorybean;
import com.project.bsky.bean.Treatmenthistorybeandetails;
import com.project.bsky.bean.hospitalauthbean;
import com.project.bsky.model.IcdSearchLog;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.IcdSearchLogRepository;
import com.project.bsky.service.MisreportsService;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class MisreportsServiceimpl implements MisreportsService {

	private final Logger logger;

	@Autowired
	private DistrictMasterRepository districtrepo;

	@Autowired
	private HospitalInformationRepository hospitalrepo;

	@Autowired
	private IcdSearchLogRepository icdsearchlogrepo;

	@Autowired
	public MisreportsServiceimpl(Logger logger) {
		this.logger = logger;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Map<String, Object> treatmenthistory(String actioncode, Long treatmentid, Long packageid) {
		Treatmenthistorybeandetails treatmenthistory1 = new Treatmenthistorybeandetails();
		List<Treatmenthistorybeandetails> trtmentlist = new ArrayList<Treatmenthistorybeandetails>();
		Map<String, Object> list = new LinkedHashMap<>();
		ResultSet rs = null;
		ResultSet rs1 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1;
		if (actioncode.equals("A")) {
			sdf1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		} else {
			sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_TREATMENT_HISTORY_DETAILS_RPT")
					.registerStoredProcedureParameter("ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("TREATMENTID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("PACKAGEID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_P_MSGOUT1", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("ACTIONCODE", actioncode);
			storedProcedureQuery.setParameter("TREATMENTID", treatmentid);
			storedProcedureQuery.setParameter("PACKAGEID", packageid);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT1");
			if (rs.next()) {
				treatmenthistory1.setUrn(rs.getString(1));
				treatmenthistory1.setPatient(rs.getString(2));
				treatmenthistory1.setPphoneno(rs.getString(3));
				if (rs.getString(4).equals("M")) {
					treatmenthistory1.setGender("Male");
				} else if (rs.getString(4).equals("F")) {
					treatmenthistory1.setGender("FeMale");
				} else {
					treatmenthistory1.setGender("Other");
				}
				treatmenthistory1.setAge(rs.getString(5));
				treatmenthistory1.setPstate(rs.getString(6));
				treatmenthistory1.setPdist(rs.getString(7));
				treatmenthistory1.setBlock(rs.getString(8));
				treatmenthistory1.setPanchayat(rs.getString(9));
				treatmenthistory1.setVillage(rs.getString(10));
				treatmenthistory1.setHname(rs.getString(11));
				treatmenthistory1.setHcode(rs.getString(12));
				treatmenthistory1.setHstate(rs.getString(13));
				treatmenthistory1.setHdist(rs.getString(14));
				treatmenthistory1.setHcatgory(rs.getString(15));
				treatmenthistory1.setHemail(rs.getString(16));
				treatmenthistory1.setHphone(rs.getString(17));
				treatmenthistory1.setPcode(rs.getString(18) != null ? rs.getString(18) : "N/A");
				treatmenthistory1.setPname(rs.getString(19) != null ? rs.getString(19) : "N/A");
				treatmenthistory1.setPscode(rs.getString(20) != null ? rs.getString(20) : "N/A");
				treatmenthistory1.setPsname(rs.getString(21) != null ? rs.getString(21) : "N/A");
				treatmenthistory1.setPhcode(rs.getString(22) != null ? rs.getString(22) : "N/A");
				treatmenthistory1.setPhname(rs.getString(23) != null ? rs.getString(23) : "N/A");
				treatmenthistory1.setPcost(rs.getString(24) != null ? rs.getString(24) : "N/A");
				treatmenthistory1.setAmountblocked(rs.getString(25) != null ? rs.getString(25) : "N/A");
				if (rs.getString(27) != null) {
					treatmenthistory1.setDateadd(sdf2.format(sdf.parse(rs.getString(27))));
				} else {
					treatmenthistory1.setDateadd("N/A");
				}
				if (rs.getString(26) != null) {
					treatmenthistory1.setAdateadd(sdf1.format(sdf.parse(rs.getString(26))));
				} else {
					treatmenthistory1.setAdateadd("N/A");
				}
				if (rs.getString(29) != null) {
					treatmenthistory1.setDatedis(sdf2.format(sdf.parse(rs.getString(29))));
				} else {
					treatmenthistory1.setDatedis("N/A");
				}
				if (rs.getString(28) != null) {
					treatmenthistory1.setAdatedis(sdf1.format(sdf.parse(rs.getString(28))));
				} else {
					treatmenthistory1.setAdatedis("N/A");
				}
				if (rs.getString(30).equals("1")) {
					treatmenthistory1.setAuthmode("POS");
				} else if (rs.getString(30).equals("2")) {
					treatmenthistory1.setAuthmode("IRIS");
				} else if (rs.getString(30).equals("3")) {
					treatmenthistory1.setAuthmode("OTP");
				} else if (rs.getString(30).equals("4")) {
					treatmenthistory1.setAuthmode("OVERRIDE");
				} else {
					treatmenthistory1.setAuthmode("N/A");
				}
				treatmenthistory1.setMobverify(rs.getString(31).equals("0") ? "VERIFIED" : "NOT VERIFIED");
				treatmenthistory1.setPackagedetailsid(rs.getLong(32));
				treatmenthistory1.setCaseno(rs.getString(33));
			}
			while (rs1.next()) {

				Treatmenthistorybeandetails treatmenthistory = new Treatmenthistorybeandetails();
				treatmenthistory.setHupdate(rs1.getString(1) != null ? rs1.getString(1) : "N/A");
				treatmenthistory.setHdescription(rs1.getString(2) != null ? rs1.getString(2) : "N/A");
				treatmenthistory.setPappdate(rs1.getString(3) != null ? rs1.getString(3) : "N/A");
				treatmenthistory.setPsnaremark(rs1.getString(4) != null ? rs1.getString(4) : "N/A");
				treatmenthistory.setPsnadescription(rs1.getString(6) != null ? rs1.getString(6) : "N/A");
				treatmenthistory.setPsappamount(rs1.getString(5) != null ? rs1.getString(5) : "N/A");
				treatmenthistory.setPreauthdoc1(rs1.getString(7));
				treatmenthistory.setPreauthdoc2(rs1.getString(8));
				treatmenthistory.setPreauthdoc3(rs1.getString(9));
				trtmentlist.add(treatmenthistory);
			}
			list.put("treatmenthistory", treatmenthistory1);
			list.put("preauth", trtmentlist);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		/**
		 * Reference page :- CPDClaimRevertServiceImpl Reference method :-
		 * getPackageDetailsInfoList() Rajendra Prasad sahoo 23-05-2023
		 */
		List<Map<String, Object>> highEndDrugInfoList = new ArrayList<>();
		Map<String, Object> highEndDrugInfo;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		ResultSet hedInfoResultSet = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_CODE_WISE_DETAILS_CMS")
					.registerStoredProcedureParameter("P_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HEDINFO", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_IMPLANTINFO", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_WARDINFO", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILID", treatmenthistory1.getPackagedetailsid());// 410
			storedProcedureQuery.execute();
			hedInfoResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_HEDINFO");
			while (hedInfoResultSet.next()) {
				highEndDrugInfo = new LinkedHashMap<>();
				highEndDrugInfo.put("code",
						hedInfoResultSet.getString(1) != null ? hedInfoResultSet.getString(1) : "N/A");
				highEndDrugInfo.put("name",
						hedInfoResultSet.getString(2) != null ? hedInfoResultSet.getString(2) : "N/A");
				highEndDrugInfo.put("unitPrice",
						hedInfoResultSet.getString(3) != null ? hedInfoResultSet.getDouble(3) : "N/A");
				highEndDrugInfo.put("unit",
						hedInfoResultSet.getString(4) != null ? hedInfoResultSet.getLong(4) : "N/A");
				highEndDrugInfo.put("totalPrice",
						hedInfoResultSet.getString(5) != null ? hedInfoResultSet.getDouble(5) : "N/A");
				highEndDrugInfo.put("recommendedDose",
						hedInfoResultSet.getString(6) != null ? hedInfoResultSet.getString(6) : "N/A");
				highEndDrugInfo.put("preAuthRequired",
						hedInfoResultSet.getString(7) != null ? hedInfoResultSet.getString(7) : "N/A");
				highEndDrugInfo.put("activityDoneOn",
						hedInfoResultSet.getDate(8) != null ? simpleDateFormat.format(hedInfoResultSet.getDate(8))
								: "N/A");
				highEndDrugInfoList.add(highEndDrugInfo);
			}
		} catch (Exception e) {
			logger.error("Exception in getPackageCodeWiseDetailsCMS() of PackageCodeWiseDetailsDaoImpl : ", e);
		} finally {
			try {
				if (hedInfoResultSet != null) {
					hedInfoResultSet.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		list.put("highEndDrugInfoList", highEndDrugInfoList);
		return list;
	}

	@Override
	public Cpdperformancebean getcpdwiseperformace(String actioncode, Date fromdate, Date todate, Long cpdid,
			Long userid) {
		ResultSet rs = null;
		Cpdperformancebean bean = new Cpdperformancebean();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPDWISE_PERFORM_RPT")
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", actioncode);
			storedProcedureQuery.setParameter("p_cpd_id", cpdid);
			storedProcedureQuery.setParameter("p_user_id", userid);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", todate);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			Long totalcodapprv = 0l;
			Long totalcodrej = 0l;
			Double d;
			if (rs.next()) {

				bean.setCpdapprv(rs.getString(1));
				totalcodapprv = rs.getLong(1);
				bean.setCpdreject(rs.getString(2));
				totalcodrej = rs.getLong(2);
				bean.setCpdrejwthoutqury(rs.getString(3));

				bean.setCpdapprvsnaappr(rs.getString(4));
				d = (Double) (rs.getLong(4) * 100.0 / totalcodapprv);
				bean.setPcpdapprvsnaappr(String.format("%.2f", d));

				bean.setCpdapprvsnarej(rs.getString(5));
				d = (Double) (rs.getLong(5) * 100.0 / totalcodapprv);
				bean.setPcpdapprvsnarej(String.format("%.2f", d));

				bean.setCpdapprvsnaquery(rs.getString(6));
				d = (Double) (rs.getLong(6) * 100.0 / totalcodapprv);
				bean.setPcpdapprvsnaquery(String.format("%.2f", d));

				bean.setCpdapprvsnainvestigate(rs.getString(7));
				d = (Double) (rs.getLong(7) * 100.0 / totalcodapprv);
				bean.setPcpdapprvsnainvestigate(String.format("%.2f", d));

				bean.setCpdapprvsnarevert(rs.getString(8));
				d = (Double) (rs.getLong(8) * 100.0 / totalcodapprv);
				bean.setPcpdapprvsnarevert(String.format("%.2f", d));

				bean.setCpdapprvsnahold(rs.getString(9));
				d = (Double) (rs.getLong(9) * 100.0 / totalcodapprv);
				bean.setPcpdapprvsnahold(String.format("%.2f", d));

				bean.setCpdrejsnaappr(rs.getString(10));
				d = (Double) (rs.getLong(10) * 100.0 / totalcodrej);
				bean.setPcpdrejsnaappr(String.format("%.2f", d));

				bean.setCpdrejsnarej(rs.getString(11));
				d = (Double) (rs.getLong(11) * 100.0 / totalcodrej);
				bean.setPcpdrejsnarej(String.format("%.2f", d));

				bean.setCpdrejsnaquery(rs.getString(12));
				d = (Double) (rs.getLong(12) * 100.0 / totalcodrej);
				bean.setPcpdrejsnaquery(String.format("%.2f", d));

				bean.setCpdrejsnainvestigate(rs.getString(13));
				d = (Double) (rs.getLong(13) * 100.0 / totalcodrej);
				bean.setPcpdrejsnainvestigate(String.format("%.2f", d));

				bean.setCpdrejsnarevert(rs.getString(14));
				d = (Double) (rs.getLong(14) * 100.0 / totalcodrej);
				bean.setPcpdrejsnarevert(String.format("%.2f", d));

				bean.setCpdrejsnahold(rs.getString(15));
				d = (Double) (rs.getLong(15) * 100.0 / totalcodrej);
				bean.setPcpdrejsnahold(String.format("%.2f", d));
			}
		} catch (Exception e) {
			logger.error("Exception in getcpdwiseperformace() of misreportsserviceimpl : ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return bean;
	}

	@Override
	public List<Object> getcpdwiseperformacedetails(Integer actioncode, String fromdate, String todate, Long cpdid,
			Long userid, Integer serchtype) {
		List<Object> object = new ArrayList<Object>();
		ResultSet report = null;
		if (cpdid == null) {
			cpdid = 0l;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_cpdwise_sna_performance_report_modified")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_datetype", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userid);
			storedProcedureQuery.setParameter("p_cpd_id", cpdid);
			storedProcedureQuery.setParameter("p_datetype", serchtype);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", todate);
			storedProcedureQuery.setParameter("p_actioncode", actioncode);

			storedProcedureQuery.execute();
			report = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_out");
			while (report.next()) {
				ReportCountBeanDetails bean = new ReportCountBeanDetails();
				bean.setClaimNo(report.getString(1));
				bean.setUrn(report.getString(2));
				bean.setPatentname(report.getString(3));
				bean.setPackageName(report.getString(4));
				bean.setPackagecode(report.getString(5));
				bean.setActDateOfAdm(report.getString(6));
				bean.setActDateOfDschrg(report.getString(7));
				bean.setClaimamount(report.getString(8));
				bean.setApprovedamount(report.getString(9));
				bean.setCpdapproveamount(report.getString(10));
				bean.setSnaapproveamount(report.getString(11));
				bean.setCpdactiontye(report.getString(12));
				bean.setSnaactiontype(report.getString(13));
				bean.setCpdactiondate(report.getString(14));
				bean.setSnaactiondate(report.getString(15));
				bean.setCpdAllotedDate(report.getString(16));
				object.add(bean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (report != null) {
					report.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return object;
	}

	@Override
	public List<Object> getSnawiseunprocessedcountdetails(Snawiseunprocessedbean requestBean) {
		List<Object> runsnawisecountdetatails = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNAWISE_UNPROCESSEDCLAIMLIST_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Action_Type", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", requestBean.getSnoid());
			storedProcedureQuery.setParameter("P_FROMDATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", requestBean.getDistrictCode());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_Action_Type", requestBean.getSearchby());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (snoDetailsObj.next()) {
				SnaunprocessedBean resBean = new SnaunprocessedBean();
				resBean.setClaimid(snoDetailsObj.getLong(1));
				resBean.setUrnnumber(snoDetailsObj.getString(2));
				resBean.setClaimnumber(snoDetailsObj.getString(3));
				resBean.setPatientname(snoDetailsObj.getString(4));
				resBean.setHospitalname(snoDetailsObj.getString(5));
				resBean.setHospitalcode(snoDetailsObj.getString(6));
				resBean.setCaseno(snoDetailsObj.getString(7));
				resBean.setPackagecode(snoDetailsObj.getString(8));
				resBean.setPackagename(snoDetailsObj.getString(9));
				resBean.setActualdateofadmission(snoDetailsObj.getString(10));
				resBean.setActialdateofdischarge(snoDetailsObj.getString(11));
				resBean.setUnprocessdate(snoDetailsObj.getString(12));
				resBean.setUnprocessedby(snoDetailsObj.getString(13));
				runsnawisecountdetatails.add(resBean);
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in saveNonComplianceDateExtension method of CpdSystemRejectedListServiceImpl :",
					e);
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
		return runsnawisecountdetatails;
	}

	@Override
	public List<Object> getDistrictListByStateIddcid(Long dcid, String stateid) {
		List<Object> list = new ArrayList<Object>();
		try {
			List<Object[]> obj = districtrepo.getDistrictListByStateIddcid(dcid, stateid);
			for (Object[] ob : obj) {
				Disthospbean bean = new Disthospbean();
				bean.setDistrictcode((String) ob[2]);
				bean.setDistrictname((String) ob[3]);
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<Object> getHospitalbyDistrictIddcid(Long dcid, String stateid, String distid) {
		List<Object> list = new ArrayList<Object>();
		try {
			List<Object[]> obj = hospitalrepo.getHospitalbyDistrictIddcid(dcid, stateid, distid);
			for (Object[] ob : obj) {
				Disthospbean bean = new Disthospbean();
				bean.setHospitalName((String) ob[1]);
				bean.setHospitalCode((String) ob[2]);
				bean.setHospName((String) ob[3]);
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Map<String, Object> gethospongingtreatmentdtls(Date formdate, Date toDate, Integer flag, String hospital,
			Long userId) {
		Map<String, Object> details = new HashMap<>();
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null, rs1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_WISE_ONGOING_TREATMENT_DTLS")
					.registerStoredProcedureParameter("P_ACTION_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_HOSPITAL", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_DETAILS", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospital);
			storedProcedureQuery.setParameter("P_ACTION_FLAG", flag);
			storedProcedureQuery.setParameter("P_FROM_DATE", formdate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_HOSPITAL");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_DETAILS");
			HospitalBean bean = new HospitalBean();
			while (rs.next()) {
				bean.setStateName(rs.getString(1));
				bean.setDistrictName(rs.getString(2));
				bean.setHospitalName(rs.getString(3));
				bean.setHospitalCode(rs.getString(4));
				bean.setAssignsna(rs.getInt(5));
				bean.setSnaname(rs.getString(6) != null ? rs.getString(6) : "N/A");
				bean.setAssigndc(rs.getLong(7));
				bean.setDcname(rs.getString(8) != null ? rs.getString(8) : "N/A");
				bean.setMobileNo(rs.getString(9));
				bean.setHospitalType(rs.getString(10));
				bean.setEmailId(rs.getString(11));
			}
			while (rs1.next()) {
				Hospitalwiseongoringtrtmntbean ongoingbean = new Hospitalwiseongoringtrtmntbean();
				ongoingbean.setUrn(rs1.getString(1));
				ongoingbean.setPatient(rs1.getString(2));
				ongoingbean.setAge(rs1.getString(3));
				ongoingbean.setGender(rs1.getString(4));
				ongoingbean.setUid(rs1.getString(5));
				ongoingbean.setPphoneno(rs1.getString(6));
				ongoingbean.setActualdateofadmission(rs1.getString(7));
				ongoingbean.setDateofadmission(rs1.getString(8));
				ongoingbean.setProcedurename(rs1.getString(9));
				ongoingbean.setPackagename(rs1.getString(10));
				ongoingbean.setAuthenticatemode(rs1.getString(13));
				ongoingbean.setWardname(rs1.getString(14));
				ongoingbean.setAmountblock(rs1.getString(15));
				ongoingbean.setPreauthstatus(rs1.getString(17));
				ongoingbean.setPreauthrqstdate(rs1.getString(18));
				ongoingbean.setImplantavail(rs1.getString(19));
				ongoingbean.setHighenddrug(rs1.getString(20));
				list.add(ongoingbean);
			}
			details.put("hospital", bean);
			details.put("list", list);
			details.put("status", 200);
			details.put("msg", "Api Called Successfully");
		} catch (Exception e) {
			details.put("status", 400);
			details.put("msg", "Something went Wrong");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return details;
	}

	@Override
	public Map<String, Object> getauthlivestatus(Date formdate, String state, String dist, String hospital,
			Long userId) {
		Map<String, Object> details = new HashMap<>();
		List<Object> otplist = new ArrayList<Object>();
		List<Object> irislist = new ArrayList<Object>();
		List<Object> poslist = new ArrayList<Object>();
		List<Object> overridelist = new ArrayList<Object>();
		List<Object> faceList = new ArrayList<Object>();
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_AUTHDETAILSREPORT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("CUR_POS", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("CUR_IRIS", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("CUR_OTP", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("CUR_OVERRIDE", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("CUR_FACE", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_STATECODE", state);
			storedProcedureQuery.setParameter("P_DISTRICTCODE", dist);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospital);
			storedProcedureQuery.setParameter("P_DATE", formdate);
			storedProcedureQuery.setParameter("P_ACTION", "A");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_OTP");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_IRIS");
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_POS");
			rs3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_OVERRIDE");
			rs4 = (ResultSet) storedProcedureQuery.getOutputParameterValue("CUR_FACE");
			Authenticationbean bean;
			while (rs.next()) {
				bean = new Authenticationbean();
				bean.setHospitalname(rs.getString(1));
				bean.setHospitalcode(rs.getString(2));
				bean.setUrn(rs.getString(3));
				bean.setPatient(rs.getString(4));
				bean.setVerifier(rs.getString(5));
				bean.setCreateon(rs.getString(6));
				bean.setPrps(rs.getString(7));
				bean.setVerifystatus(rs.getString(8));
				otplist.add(bean);
			}
			details.put("otp", otplist);
			while (rs1.next()) {
				bean = new Authenticationbean();
				bean.setHospitalname(rs1.getString(1));
				bean.setHospitalcode(rs1.getString(2));
				bean.setUrn(rs1.getString(3));
				bean.setPatient(rs1.getString(4));
				bean.setVerifier(rs1.getString(5));
				bean.setCreateon(rs1.getString(6));
				bean.setPrps(rs1.getString(7));
				bean.setVerifystatus(rs1.getString(8));
				irislist.add(bean);
			}
			details.put("iris", irislist);
			while (rs2.next()) {
				bean = new Authenticationbean();
				bean.setHospitalname(rs2.getString(1));
				bean.setHospitalcode(rs2.getString(2));
				bean.setUrn(rs2.getString(3));
				bean.setPatient(rs2.getString(4));
				bean.setVerifier(rs2.getString(5));
				bean.setCreateon(rs2.getString(6));
				bean.setPrps(rs2.getString(7));
				bean.setVerifystatus(rs2.getString(8));
				poslist.add(bean);
			}
			details.put("pos", poslist);
			while (rs3.next()) {
				bean = new Authenticationbean();
				bean.setHospitalname(rs3.getString(1));
				bean.setHospitalcode(rs3.getString(2));
				bean.setUrn(rs3.getString(3));
				bean.setPatient(rs3.getString(4));
				bean.setVerifier(rs3.getString(5));
				bean.setCreateon(rs3.getString(6));
				bean.setPrps(rs3.getString(7));
				bean.setVerifystatus(rs3.getString(8));
				overridelist.add(bean);
			}
			details.put("override", overridelist);
			while (rs4.next()) {
				bean = new Authenticationbean();
				bean.setHospitalname(rs4.getString(1));
				bean.setHospitalcode(rs4.getString(2));
				bean.setUrn(rs4.getString(3));
				bean.setPatient(rs4.getString(4));
				bean.setVerifier(rs4.getString(5));
				bean.setCreateon(rs4.getString(6));
				bean.setPrps(rs4.getString(7));
				bean.setVerifystatus(rs4.getString(8));
				faceList.add(bean);
			}
			details.put("faceList", faceList);
			details.put("status", 200);
			details.put("msg", "api Called Successfully");
		} catch (Exception e) {
			details.put("status", 400);
			details.put("msg", "Something Went Wrong");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (rs3 != null) {
					rs3.close();
				}
				if (rs4 != null) {
					rs4.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return details;
	}

	@Override
	public List<Object> gethospitalauthdetails(Date formdate, Date todate, String flag, Integer type, String hospital) {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_AUTHDETLS_REPORTADMIN")
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PURPOSE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VERIFICATIONMODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_PURPOSE", flag);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospital);
			storedProcedureQuery.setParameter("P_VERIFICATIONMODE", type);
			storedProcedureQuery.setParameter("p_action", "A");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_out");
			Authenticationbean bean;
			while (rs.next()) {
				bean = new Authenticationbean();
				bean.setHospitalname(rs.getString(3));
				bean.setUrn(rs.getString(4) != null ? rs.getString(4) : "N/A");
				bean.setPatient(rs.getString(5) != null ? rs.getString(5) : "N/A");
				bean.setVerifier(rs.getString(7) != null ? rs.getString(7) : "N/A");
				bean.setCreateon(rs.getString(10) != null ? rs.getString(10) : "N/A");
				bean.setPrps(rs.getString(11));
				bean.setVerifystatus("Verified");
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return list;
	}

	@Override
	public Treatmenthistorybeandetails blockedcaselogdetailsof1(Long txnid, Long pkgid, Long userid) {
		Treatmenthistorybeandetails treatmenthistory = new Treatmenthistorybeandetails();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_BLOCKED_CASE_LOG_REPORT_DTLS")
					.registerStoredProcedureParameter("P_TRANSACTIONID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TXNPACKAGEDETAILID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_TRANSACTIONID", txnid);
			storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILID", pkgid);
			storedProcedureQuery.setParameter("p_user_id", userid);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				treatmenthistory.setPstate(rs.getString(1));
				treatmenthistory.setPdist(rs.getString(2));
				treatmenthistory.setHname(rs.getString(3));
				treatmenthistory.setUrn(rs.getString(4));
				treatmenthistory.setPatient(rs.getString(5));
				treatmenthistory.setPphoneno(rs.getString(6));
				treatmenthistory.setGender(rs.getString(7));
				treatmenthistory.setPhcode(rs.getString(8));
				treatmenthistory.setPhname(rs.getString(9));
				treatmenthistory.setPscode(rs.getString(10));
				treatmenthistory.setPsname(rs.getString(11));
				treatmenthistory.setPcode(rs.getString(12));
				treatmenthistory.setPname(rs.getString(13));
				treatmenthistory.setAmountblocked(rs.getString(14));
				treatmenthistory.setAdateadd(rs.getString(15));
				treatmenthistory.setDateadd(rs.getString(16));
				treatmenthistory.setStatus(rs.getString(18));
				treatmenthistory.setAdatedis(rs.getString(19));
				treatmenthistory.setDatedis(rs.getString(20));
				treatmenthistory.setClaimedamount(rs.getString(21));
				treatmenthistory.setHcode(rs.getString(22));
				treatmenthistory.setAge(rs.getString(23));
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return treatmenthistory;
	}

	@Override
	public void saveicdsearchlog(IcdSearchLog icdsearchlog) {
		icdsearchlog.setStatusflag(0);
		icdsearchlog.setCreatedOn(Calendar.getInstance().getTime());
		icdsearchlogrepo.save(icdsearchlog);
	}

}
