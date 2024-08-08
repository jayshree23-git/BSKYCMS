/**
 * 
 */
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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.OldBlockDataReportBean;
import com.project.bsky.bean.OldClaimListBean;
import com.project.bsky.service.OldBlockDataReportService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class OldBlockDataReportServiceImpl implements OldBlockDataReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> OldBlockData(Integer userId, String fromdate, String todate, String stateId, String districtId,
			String hospitalCode) {

		List<Object> object = new ArrayList<Object>();

		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_old_block_data_report_count")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_district_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userId);
			Date d = new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			storedProcedure.setParameter("p_from_date", d);
			Date d1 = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			storedProcedure.setParameter("p_to_date", d1);
			if (stateId == null) {
				storedProcedure.setParameter("p_state_code", null);
			} else {
				storedProcedure.setParameter("p_state_code", stateId);
			}
			if (districtId == null) {
				storedProcedure.setParameter("p_district_code", null);
			} else {
				storedProcedure.setParameter("p_district_code", districtId);
			}
			if (hospitalCode == null) {
				storedProcedure.setParameter("p_hospital_code", null);
			} else {
				storedProcedure.setParameter("p_hospital_code", hospitalCode);
			}
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (list.next()) {
				OldBlockDataReportBean bean = new OldBlockDataReportBean();
				bean.setYear(list.getString(1));
				bean.setCaseCount(list.getString(2));
				object.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return object;
	}

	@Override
	public List<Object> OldBlockDataReportList(Integer userId, String reportData, String stateId, String districtId,
			String hospitalCode) {

		List<Object> object = new ArrayList<Object>();

		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_old_block_data_report_list")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_district_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userId);
			storedProcedure.setParameter("p_year", reportData);
			if (stateId == null) {
				storedProcedure.setParameter("p_state_code", null);
			} else {
				storedProcedure.setParameter("p_state_code", stateId);
			}
			if (districtId == null) {
				storedProcedure.setParameter("p_district_code", null);
			} else {
				storedProcedure.setParameter("p_district_code", districtId);
			}
			if (hospitalCode == null) {
				storedProcedure.setParameter("p_hospital_code", null);
			} else {
				storedProcedure.setParameter("p_hospital_code", hospitalCode);
			}
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (list.next()) {
				OldBlockDataReportBean bean = new OldBlockDataReportBean();
				bean.setStateName(list.getString(1));
				bean.setDistrictName(list.getString(2));
				bean.setHospitalName(list.getString(3));
				bean.setHospitalCode(list.getString(4));
				bean.setUrn(list.getString(5));
				bean.setPatientName(list.getString(6));
				bean.setActualAdmissionDate(list.getString(7));
				bean.setAmountBlocked(list.getString(8));
				bean.setInvoiceNo(list.getString(9));
				bean.setLatestStatus(list.getString(10));
				bean.setTxnPackageId(list.getString(11));
				bean.setTransactionId(list.getString(12));
				object.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return object;
	}

	@Override
	public List<Object> getoldblockgenericsearch(String fieldvalue) throws Exception {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_OLDCLAIM_GENERIC_SEARCH_LIST")
					.registerStoredProcedureParameter("P_PNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_PNAME", fieldvalue);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				OldClaimListBean bean = new OldClaimListBean();
				bean.setTransid(rs.getLong(1));
				bean.setURN(rs.getString(2) != null ? rs.getString(2) : "N/A");
				bean.setClaimstatus(rs.getString(3) != null ? rs.getString(3) : "N/A");
				bean.setHospitalcode(rs.getString(4));
				bean.setHospitalname(rs.getString(5) + " (" + rs.getString(4) + ")");
				bean.setPatientName(rs.getString(6));
				bean.setInvoiceNumber(rs.getString(7));
				bean.setSnaremarks(rs.getString(8) != null ? rs.getString(8) : "N/A");
				bean.setRemarks(rs.getString(9) != null ? rs.getString(9) : "N/A");
				bean.setActualDateOfDischarge(rs.getString(10));
				bean.setDateofdischarge(rs.getString(11));
				bean.setActualDateOfAdmission(rs.getString(12));
				bean.setDateofadmission(rs.getString(13));
				bean.setTransactionDetailsId(rs.getLong(14));
				bean.setClaimid(rs.getString(15));
				bean.setApproveduserid(rs.getString(16));
				bean.setRejecteduserid(rs.getString(17));
				bean.setInvestigationuserid(rs.getString(18));
				bean.setSnaapproveduserid(rs.getString(19));
				bean.setSnarejecteduserid(rs.getString(20));
				bean.setSnainvestigationuserid(rs.getString(21));
				bean.setSnafinaldecisionuserid(rs.getString(22));
				bean.setPaiduserid(rs.getString(23));
				bean.setTpafinaldecisionuserid(rs.getString(24));
				bean.setApproveduser(rs.getString(25) != null ? rs.getString(25) : "N/A");
				bean.setRejecteduser(rs.getString(26) != null ? rs.getString(26) : "N/A");
				bean.setInvestigationuser(rs.getString(27) != null ? rs.getString(27) : "N/A");
				bean.setSnaapproveduser(rs.getString(28) != null ? rs.getString(28) : "N/A");
				bean.setSnarejecteduser(rs.getString(29) != null ? rs.getString(29) : "N/A");
				bean.setSnainvestigationuser(rs.getString(30) != null ? rs.getString(30) : "N/A");
				bean.setSnafinaldecisionuser(rs.getString(31) != null ? rs.getString(31) : "N/A");
				bean.setPaiduser(rs.getString(32) != null ? rs.getString(32) : "N/A");
				bean.setTpafinaldecisionuser(rs.getString(33) != null ? rs.getString(33) : "N/A");
				bean.setPhone(rs.getString(34) != null ? rs.getString(34) : "N/A");
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<Object> getoldblockdataviewlist(Date formdate, Date todate, String stetecode, String distcode,
			String hospitalcode) throws Exception {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_OLDCLAIM_DATA_VIEW_LIST")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_from_date", formdate);
			storedProcedure.setParameter("p_to_date", todate);
			storedProcedure.setParameter("p_state_code", stetecode);
			storedProcedure.setParameter("p_dist_code", distcode);
			storedProcedure.setParameter("p_hsptl_code", hospitalcode);

			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (rs.next()) {
				OldClaimListBean bean = new OldClaimListBean();
				bean.setTransid(rs.getLong(1));
				bean.setURN(rs.getString(2) != null ? rs.getString(2) : "N/A");
				bean.setClaimstatus(rs.getString(3) != null ? rs.getString(3) : "N/A");
				bean.setHospitalcode(rs.getString(4));
				bean.setHospitalname(rs.getString(5) + " (" + rs.getString(4) + ")");
				bean.setPatientName(rs.getString(6));
				bean.setInvoiceNumber(rs.getString(7));
				bean.setSnaremarks(rs.getString(8) != null ? rs.getString(8) : "N/A");
				bean.setRemarks(rs.getString(9) != null ? rs.getString(9) : "N/A");
				bean.setActualDateOfDischarge(rs.getString(10));
				bean.setDateofdischarge(rs.getString(11));
				bean.setActualDateOfAdmission(rs.getString(12));
				bean.setDateofadmission(rs.getString(13));
				bean.setTransactionDetailsId(rs.getLong(14));
				bean.setClaimid(rs.getString(15));
				bean.setApproveduserid(rs.getString(16));
				bean.setRejecteduserid(rs.getString(17));
				bean.setInvestigationuserid(rs.getString(18));
				bean.setSnaapproveduserid(rs.getString(19));
				bean.setSnarejecteduserid(rs.getString(20));
				bean.setSnainvestigationuserid(rs.getString(21));
				bean.setSnafinaldecisionuserid(rs.getString(22));
				bean.setPaiduserid(rs.getString(23));
				bean.setTpafinaldecisionuserid(rs.getString(24));
				bean.setApproveduser(rs.getString(25) != null ? rs.getString(25) : "N/A");
				bean.setRejecteduser(rs.getString(26) != null ? rs.getString(26) : "N/A");
				bean.setInvestigationuser(rs.getString(27) != null ? rs.getString(27) : "N/A");
				bean.setSnaapproveduser(rs.getString(28) != null ? rs.getString(28) : "N/A");
				bean.setSnarejecteduser(rs.getString(29) != null ? rs.getString(29) : "N/A");
				bean.setSnainvestigationuser(rs.getString(30) != null ? rs.getString(30) : "N/A");
				bean.setSnafinaldecisionuser(rs.getString(31) != null ? rs.getString(31) : "N/A");
				bean.setPaiduser(rs.getString(32) != null ? rs.getString(32) : "N/A");
				bean.setTpafinaldecisionuser(rs.getString(33) != null ? rs.getString(33) : "N/A");
				bean.setPhone(rs.getString(34) != null ? rs.getString(34) : "N/A");
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public Object getoldblockdataviewdetails(Long txnid) throws Exception {
		Map<String, Object> list = new HashMap<String, Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_OLDCLAIM_DATA_DTLS")
					.registerStoredProcedureParameter("cid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_out", String.class, ParameterMode.OUT);

			storedProcedure.setParameter("cid", txnid);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			if (rs.next()) {
				list.put("urn", rs.getString(1));
				list.put("actualDateOfadmission", rs.getString(2));
				list.put("actualDateOfDischarge", rs.getString(3));
				list.put("stateName", rs.getString(4));
				list.put("districtName", rs.getString(5));
				list.put("blockname", rs.getString(6));
				list.put("villageName", rs.getString(7));
				list.put("hospitalName", rs.getString(8));
				list.put("patientName", rs.getString(9));
				list.put("gender", rs.getString(10));
				list.put("age", rs.getString(11));
				list.put("procedureName", rs.getString(12));
				list.put("PackageName", rs.getString(13));
				list.put("NoofDays", rs.getString(14));
				list.put("invoiceNo", rs.getString(15));
				list.put("currentTotalAmount", rs.getString(16));
				list.put("hospitalAddress", rs.getString(17));
				list.put("hospitalName", rs.getString(18));
				list.put("hospitalCode", rs.getString(19));
				list.put("transid", rs.getString(20));
				list.put("aditionaldoc1", rs.getString(21));
				list.put("aditionaldoc2", rs.getString(22));
				list.put("familyheadName", rs.getString(23));
				list.put("VerifireName", rs.getString(24));
				list.put("DateOfAdmission", rs.getString(25));
				list.put("DateOfDischarge", rs.getString(26));
				list.put("mortality", rs.getString(27));
				list.put("referralCode", rs.getString(28));
				list.put("authorizedCode", rs.getString(29));
				list.put("hospitalDistName", rs.getString(30));
				list.put("nabhFlag", rs.getString(31));
				list.put("patientAddress", rs.getString(32));
				list.put("implantdata", rs.getString(33));
				list.put("packageCode", rs.getString(34));
				list.put("PatientPhoneNo", rs.getString(35));
				list.put("syear", rs.getString(36));
				list.put("shospitalcode", rs.getString(37));
				list.put("file1", rs.getString(38));
				list.put("file2", rs.getString(39));
				list.put("file2", rs.getString(40));
				list.put("claimstatus", rs.getString(41));
				list.put("approvedAmount", rs.getString(42));
				list.put("approveddate", rs.getString(43));
				list.put("paidby", rs.getString(44));
				list.put("checkDDno", rs.getString(45));
				list.put("bankName", rs.getString(46));
				list.put("paidDate", rs.getString(47));
				list.put("remark", rs.getString(48));
				list.put("rejecteduser", rs.getString(49));
				list.put("investigationDate", rs.getString(50));
				list.put("snaclaimStatus", rs.getString(51));
				list.put("snaApprovedAmount", rs.getString(52));
				list.put("snaApprovedDate", rs.getString(53));
				list.put("snaRejectedDate", rs.getString(54));
				list.put("snaInvestigationdate", rs.getString(55));
				list.put("snaremark", rs.getString(56));
				list.put("tpafinalstatus", rs.getString(57));
				list.put("tpafinalDicissionDate", rs.getString(58));
				list.put("revertedDATE", rs.getString(59));
				list.put("snaFinalStatus", rs.getString(60));
				list.put("snaFinalDicissionDate", rs.getString(61));
				System.out.println(list);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

}
