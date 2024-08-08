package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
//import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.FloatExcelBean;
import com.project.bsky.bean.OldClaimPymntBean;
import com.project.bsky.bean.OldFloatBean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.Response;
import com.project.bsky.model.OldFloatReportLog;
import com.project.bsky.model.OldPaymentFreezeReportLog;
import com.project.bsky.model.TblBskySnaClaimPostPayment;
import com.project.bsky.model.TblBskySnaPostPymntSummary;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.OldFloatReportLogRepository;
import com.project.bsky.repository.OldPymntFrzReportLogRepository;
import com.project.bsky.repository.StateRepository;
import com.project.bsky.repository.TblBskySnaClmPostPymntRepository;
import com.project.bsky.repository.TblBskySnaPostPymntSmryRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.OldClaimFloatService;
import com.project.bsky.util.FileUtil;

/**
 * @author ronauk
 *
 */
@Service
public class OldFloatServiceImpl implements OldClaimFloatService {

	@Autowired
	private OldFloatReportLogRepository floatRepo;

	@Autowired
	private OldPymntFrzReportLogRepository pymntRepo;

	@Autowired
	private DistrictMasterRepository districtRepo;

	@Autowired
	private HospitalInformationRepository hospRepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private UserDetailsRepository userRepo;

	@Autowired
	private TblBskySnaClmPostPymntRepository postPymntRepo;

	@Autowired
	private TblBskySnaPostPymntSmryRepository summaryRepo;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public List<Object> getFloatReport(OldFloatBean requestBean) {
		// TODO Auto-generated method stub
//		Map<String, Object> floatMap = new HashMap<String, Object>();
		List<Object> floatList = new ArrayList<Object>();
//		String count = null;
		DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		ResultSet rs = null;
//		ResultSet rs1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_OLD_DETAILS")
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.execute();

//			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
//			while (rs1.next()) {
//				JSONObject json = new JSONObject();
//				json.put("TotalDischarged", rs1.getInt(1));
//				json.put("paymentFreeze", rs1.getInt(2));
//				json.put("paymentNotFreeze", rs1.getInt(3));
//				json.put("paid", rs1.getInt(4));
//				json.put("Rejected", rs1.getInt(5));
//				json.put("SNAInvestigation", rs1.getInt(6));
//				json.put("CPDUnprocessed", rs1.getInt(7));
//				json.put("SNAApproved", rs1.getInt(8));
//				json.put("SNARejected", rs1.getInt(9));
//				json.put("Approved", rs1.getInt(10));
//				json.put("Investigation", rs1.getInt(11));
//				count = json.toString();
//			}

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			int i = 1;
			while (rs.next()) {
				List<Object> floatObj = new ArrayList<Object>();
				floatObj.add(String.valueOf(i++));
				floatObj.add(rs.getString(2) != null ? rs.getString(2) : "-NA-");
				floatObj.add(rs.getString(3) != null ? rs.getString(3) : "-NA-");
				floatObj.add(rs.getString(4) != null ? rs.getString(4) : "-NA-");
				floatObj.add(rs.getString(5) != null ? rs.getString(5) : "-NA-");
				floatObj.add(rs.getString(6) != null ? rs.getString(6) : "-NA-");
				floatObj.add(rs.getString(7) != null ? rs.getString(7) : "-NA-");
				floatObj.add(rs.getString(8) != null ? rs.getString(8) : "-NA-");
				if (rs.getString(9).equalsIgnoreCase("M")) {
					floatObj.add("Male");
				} else if (rs.getString(9).equalsIgnoreCase("F")) {
					floatObj.add("Female");
				} else if (rs.getString(9).equalsIgnoreCase("O")) {
					floatObj.add("Others");
				} else {
					floatObj.add("-NA-");
				}
				floatObj.add(rs.getString(10) != null ? rs.getString(10) : "-NA-");
				floatObj.add(rs.getString(11) != null ? rs.getString(11) : "-NA-");
				floatObj.add(rs.getString(12) != null ? rs.getString(12).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(12))) : "-NA-");

				floatObj.add(rs.getString(13) != null ? rs.getString(13) : "-NA-");
				Timestamp ACTUALDATEOFADMISSION = rs.getTimestamp(14);
				if (ACTUALDATEOFADMISSION != null) {
					floatObj.add(f.format(new Date(ACTUALDATEOFADMISSION.getTime())));
				}
				Timestamp ACTUALDATEOFDISCHARGE = rs.getTimestamp(15);
				if (ACTUALDATEOFDISCHARGE != null) {
					floatObj.add(f.format(new Date(ACTUALDATEOFDISCHARGE.getTime())));
				}
				floatObj.add(rs.getString(16) != null ? rs.getString(16) : "-NA-");
				floatObj.add(rs.getString(17) != null ? rs.getString(17).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(17))) : "-NA-");
				floatObj.add(rs.getString(18) == null || rs.getString(18).equalsIgnoreCase("")
						|| rs.getString(18).equalsIgnoreCase(" ") || rs.getString(18).contains(" ") ? "-NA-"
								: rs.getString(18));

				floatObj.add(rs.getString(19) != null ? rs.getString(19) : "-NA-");
				floatObj.add(rs.getString(20) != null ? rs.getString(20) : "-NA-");
				floatObj.add(rs.getString(21) != null ? rs.getString(21).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(21))) : "-NA-");
				floatList.add(floatObj);
			}
//			floatMap.put("count", count);
//			floatMap.put("list", floatList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return floatList;
	}

	@Override
	public void downLoadFile(String fileCode, String userId, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			FileUtil.getOldFloatReportFile(fileCode, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Integer saveFloatReport(MultipartFile pdf, FloatExcelBean bean) {
		// TODO Auto-generated method stub
		try {
			String filename = FileUtil.saveOldFloatReportFile(pdf, bean.getUserId().toString());
			bean.setFilename(filename);
			return saveReport(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public Integer saveReport(FloatExcelBean requestBean) {
		// TODO Auto-generated method stub
		OldFloatReportLog log = new OldFloatReportLog();
		try {
			log.setUserId(requestBean.getUserId());
			log.setFileName(requestBean.getFilename());
			log.setActualDateOfDischargeFrom(requestBean.getFromDate());
			log.setActualDateOfDischargeTo(requestBean.getToDate());
			log.setStateCode(requestBean.getStateId());
			log.setDistrictCode(requestBean.getDistrictId());
			log.setHospitalCode(requestBean.getHospitalId());
			log.setCreatedOn(new Date());
			log.setCreatedBy(requestBean.getCreatedBy());
			log.setStatusflag(0);
			floatRepo.save(log);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public String getGeneratedReports(OldFloatBean requestBean) {
		// TODO Auto-generated method stub
		JSONArray list = new JSONArray();
		try {
			// //System.out.println(requestBean.toString());
			List<OldFloatReportLog> logList = floatRepo.getGeneratedReports(requestBean.getSnaUserId(),
					requestBean.getFromDate(), requestBean.getToDate(), requestBean.getHospitalId(),
					requestBean.getDistrictId(), requestBean.getStateId(), requestBean.getUserId());
			for (OldFloatReportLog log : logList) {
				JSONObject json = new JSONObject();
				json.put("logId", log.getLogId());
				json.put("userId", log.getUserId());
				String fileName = log.getFileName();
				json.put("fileName", fileName);
				json.put("actualDateOfDischargeFrom",
						new SimpleDateFormat("dd-MMM-yyyy").format(log.getActualDateOfDischargeFrom()));
				json.put("actualDateOfDischargeTo",
						new SimpleDateFormat("dd-MMM-yyyy").format(log.getActualDateOfDischargeTo()));
				json.put("stateName", log.getStateCode().equalsIgnoreCase("All") ? "All"
						: stateRepo.findStateByCode(log.getStateCode()).getStateName());
				json.put("districtName", log.getDistrictCode().equalsIgnoreCase("All") ? "All"
						: districtRepo.getdistrict(log.getStateCode(), log.getDistrictCode()).getDistrictname());
				json.put("hospitalName",
						log.getHospitalCode().equalsIgnoreCase("All") ? "All"
								: hospRepo.getHospital(log.getHospitalCode()).getHospitalName() + " ("
										+ log.getHospitalCode() + ")");
				json.put("createdOn", new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(log.getCreatedOn()));
				String extn = null;
				if (fileName != null) {
					extn = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
				}
				json.put("extn", extn);
				json.put("snoName", userRepo.findByUserId(log.getUserId()).getFullname());
				list.put(json);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list.toString();
	}

	@Override
	public String getPaymentFreezeReport(OldFloatBean requestBean) {
		// TODO Auto-generated method stub
		String count = null;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_OLD")
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_filename", null);
			storedProcedureQuery.setParameter("p_flag", 0);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("TotalDischarged", rs.getInt(1));
				json.put("paymentFreeze", rs.getInt(2));
				json.put("paid", rs.getInt(3));
				json.put("paymentNotFreeze", rs.getInt(4));
				json.put("snaQuery", rs.getInt(5));
				json.put("resettlement", rs.getInt(6));
				json.put("snaapproved", rs.getInt(7));
				json.put("snarejected", rs.getInt(8));
				count = json.toString();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return count;
	}

	@Override
	@Transactional
	public Integer paymentFreezeAction(Long id, Date fdate, Date tdate, String stateId, String districtId,
			String hospitalId) {
		// TODO Auto-generated method stub
		// //System.out.println(id + " : " + fdate + " : " + tdate + " : " + stateId + " :
		// " + districtId + " : " + hospitalId);
		Integer result = 0;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_OLD")
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno", id);
			storedProcedureQuery.setParameter("p_fromdate", fdate);
			storedProcedureQuery.setParameter("p_todate", tdate);
			storedProcedureQuery.setParameter("p_hsptl_code", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
			storedProcedureQuery.setParameter("p_filename", null);
			storedProcedureQuery.setParameter("p_flag", 1);
			storedProcedureQuery.execute();
			
			result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public Integer savePaymentFreezeRecord(MultipartFile pdf, Long id, Date fdate, Date tdate, String stateId,
			String districtId, String hospitalId) {
		// TODO Auto-generated method stub
		try {
			if (pdf != null) {
				String filename = FileUtil.saveOldPaymentReport(pdf, id.toString());
				if (filename != null) {
					return saveReport(filename, id, fdate, tdate, stateId, districtId, hospitalId);
				} else {
					return 0;
				}
			} else {
				String filename = null;
				return saveReport(filename, id, fdate, tdate, stateId, districtId, hospitalId);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public Integer saveReport(String file, Long id, Date fdate, Date tdate, String stateId, String districtId,
			String hospitalId) {
		// TODO Auto-generated method stub
		Integer result = 0;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_OLD")
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno", id);
			storedProcedureQuery.setParameter("p_fromdate", fdate);
			storedProcedureQuery.setParameter("p_todate", tdate);
			storedProcedureQuery.setParameter("p_hsptl_code", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
			storedProcedureQuery.setParameter("p_filename", file);
			storedProcedureQuery.setParameter("p_flag", 2);
			storedProcedureQuery.execute();
			
			result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public String paymentFreezeView(OldFloatBean requestBean) {
		// TODO Auto-generated method stub
		JSONArray list = new JSONArray();
		try {
			// //System.out.println(requestBean.toString());
			List<OldPaymentFreezeReportLog> logList = pymntRepo.getGeneratedReports(requestBean.getUserId(),
					requestBean.getFromDate(), requestBean.getToDate(), requestBean.getHospitalId(),
					requestBean.getDistrictId(), requestBean.getStateId());
			for (OldPaymentFreezeReportLog log : logList) {
				JSONObject json = new JSONObject();
				json.put("logId", log.getLogId());
				json.put("userId", log.getUserId());
				json.put("fileName", log.getFileName());
				json.put("actualDateOfDischargeFrom",
						new SimpleDateFormat("dd-MMM-yyyy").format(log.getActualDateOfDischargeFrom()));
				json.put("actualDateOfDischargeTo",
						new SimpleDateFormat("dd-MMM-yyyy").format(log.getActualDateOfDischargeTo()));
				json.put("stateName", log.getStateCode().equalsIgnoreCase("All") ? "All"
						: stateRepo.findStateByCode(log.getStateCode()).getStateName());
				json.put("districtName", log.getDistrictCode().equalsIgnoreCase("All") ? "All"
						: districtRepo.getdistrict(log.getStateCode(), log.getDistrictCode()).getDistrictname());
				json.put("hospitalName",
						log.getHospitalCode().equalsIgnoreCase("All") ? "All"
								: hospRepo.getHospital(log.getHospitalCode()).getHospitalName() + " ("
										+ log.getHospitalCode() + ")");
				json.put("createdOn", new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(log.getCreatedOn()));
				json.put("stateCode", log.getStateCode().equalsIgnoreCase("All") ? null : log.getStateCode());
				json.put("districtCode", log.getDistrictCode().equalsIgnoreCase("All") ? null : log.getDistrictCode());
				json.put("hospitalCode", log.getHospitalCode().equalsIgnoreCase("All") ? null : log.getHospitalCode());
				list.put(json);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list.toString();
	}

	@Override
	public void downloadPfzFile(String fileCode, String userId, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			FileUtil.getOldPaymentFreezeFile(fileCode, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<OldClaimPymntBean> getPostPaymentList(OldFloatBean requestBean) {
		// TODO Auto-generated method stub
		List<OldClaimPymntBean> pymntlist = new ArrayList<OldClaimPymntBean>();
		List<OldClaimPymntBean> finalList = new ArrayList<OldClaimPymntBean>();
		ResultSet rs = null;
		// //System.out.println(requestBean.toString());
		try {
			String[] hospitalCodes = requestBean.getHospitalCodeArr();
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_OLD_POST_PYMNT")
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				OldClaimPymntBean claim = new OldClaimPymntBean();
				claim.setTransid(rs.getLong(1));
				claim.setHospitalName(rs.getString(2));
				claim.setHospitalCode(rs.getString(3));
				claim.setUrn(rs.getString(4));
				claim.setInvoiceNumber(rs.getString(5));
				claim.setCaseNo(rs.getString(6));
				claim.setPatientName(rs.getString(7));
				claim.setPackageCode(rs.getString(8));
				claim.setPackageName(rs.getString(9));
				claim.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(10)));
				claim.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(11)));
				claim.setMortality(rs.getString(12));
				claim.setCurrentTotalAmount(rs.getString(13));
				claim.setSnaClaimStatus(rs.getString(14));
				claim.setSnaRemarks(rs.getString(15));
				claim.setSnaApprovedAmount(rs.getString(16));
				pymntlist.add(claim);
			}

			if (hospitalCodes != null && hospitalCodes.length > 0) {
				finalList = pymntlist.stream()
						.filter(claim -> Arrays.asList(hospitalCodes).contains(claim.getHospitalCode()))
						.collect(Collectors.toList());
			} else {
				finalList = pymntlist;
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return finalList;
	}

	@Override
	@Transactional
	public Response updatePostPayment(PostPaymentRequest requestBean) {
		// TODO Auto-generated method stub
		Response response = new Response();
		String returnObj = null;
		// //System.out.println(requestBean.toString());
		try {
			List<Long> transList = requestBean.getClaimList();
			List<TblBskySnaClaimPostPayment> ppList = postPymntRepo.findByTransIdIn(transList);
			if (ppList.size() > 0) {
				returnObj = "exists";
			} else {
				returnObj = "not exists";
			}

			List<TblBskySnaClaimPostPayment> list = new ArrayList<TblBskySnaClaimPostPayment>();
			for (int i = 0; i < transList.size(); i++) {
				Long transId = transList.get(i);
				TblBskySnaClaimPostPayment t = postPymntRepo.findBytransId(transId);
				if (t == null) {
					TblBskySnaClaimPostPayment t1 = new TblBskySnaClaimPostPayment();
					t1.setBankId(requestBean.getBankId());
					t1.setCreatedBy(requestBean.getUserId());
					t1.setCreatedOn(new Date());
					t1.setDdChequeNo(requestBean.getTypeNumber());
					t1.setPaymentDate(requestBean.getCurrentDate());
					t1.setPaymentModeId(requestBean.getBankModeId());
					t1.setStatus(0);
					t1.setTransId(transId);
					list.add(t1);
				}
			}
			postPymntRepo.saveAll(list);

			String query = "UPDATE tbl_bskysnaclaims t SET t.PAYMENT_FREEZE_STATUS=2, t.PAID_ON=sysdate WHERE t.transid in :list";
			Query q = this.entityManager.createNativeQuery(query);
			q.setParameter("list", transList);
			q.executeUpdate();

			TblBskySnaPostPymntSummary summary = new TblBskySnaPostPymntSummary();
			summary.setActualPaidAmount(requestBean.getPaidAmount());
			summary.setBankId(requestBean.getBankId());
			summary.setCreatedBy(requestBean.getUserId());
			summary.setCreatedOn(new Date());
			summary.setFinalApproveAmount(requestBean.getTotalPaidAmount());
			summary.setPaymentDate(requestBean.getCurrentDate());
			summary.setPaymentInfo(requestBean.getTypeNumber());
			summary.setPaymentMode(requestBean.getBankModeId());
			summary.setStatus(0);
			summaryRepo.save(summary);
			String returnValue = "1";

			if (returnValue.equalsIgnoreCase("1")) {
				if (returnObj.equalsIgnoreCase("exists")) {
					response.setStatus("exists");
					response.setMessage("Some record already exists.");
				} else {
					response.setStatus("success");
					response.setMessage("Updated Successfully");
				}
			} else {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("failed");
		}
		return response;
	}

	@Override
	public String paymentFreezeClaimDetails(Date fromDate, Date toDate, String stateId, String districtId,
			String hospitalId) {
		// TODO Auto-generated method stub
		JSONArray pymntList = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_OLD_DTLS")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("transid", rs.getLong(1));
				json.put("hospitalcode", rs.getString(2));
				json.put("hospitalname", rs.getString(3));
				json.put("urn", rs.getString(4));
				json.put("invoiceno", rs.getString(5));
				json.put("caseno", rs.getString(6));
				json.put("patientname", rs.getString(7));
				json.put("packagecode", rs.getString(8));
				json.put("packagename", rs.getString(9));
				json.put("procedurename", rs.getString(10));
				json.put("actualdateofadmission", new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(11)));
				json.put("actualdateofdischarge", new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(12)));
				json.put("mortality", rs.getString(13));
				json.put("totalamountclaimed", rs.getString(14));
				json.put("snoapprovedamount", rs.getString(15));
				pymntList.put(json);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return pymntList.toString();
	}

	@Override
	public String pendingmortality(String userid, Date fromdate, Date todate, String statecode, String districtcode,
			String hospitalcode) throws Exception{
		JSONArray pendinglist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_Pending_Mortality_Dtls")
					.registerStoredProcedureParameter("p_user_id", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userid.trim());
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", todate);
			storedProcedureQuery.setParameter("p_state_code", statecode.trim());
			storedProcedureQuery.setParameter("p_dist_code", districtcode.trim());
			storedProcedureQuery.setParameter("p_hsptl_code", hospitalcode.trim());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("claimno", rs.getString(1)!=null?rs.getString(1):"N/A");
				json.put("patientname", rs.getString(2)!=null?rs.getString(2):"N/A");
				json.put("hospitalcode", rs.getString(3)!=null?rs.getString(3):"N/A");
				json.put("hospitalname", rs.getString(4)!=null?rs.getString(4):"N/A");
				json.put("actualdateofadmission",com.project.bsky.util.DateFormat.FormatToDateString(rs.getString(5)));
				json.put("actualdateofdischarge", com.project.bsky.util.DateFormat.FormatToDateString(rs.getString(6)));
				json.put("packagecode", rs.getString(7)!=null?rs.getString(7):"N/A");
				json.put("packagename", rs.getString(8)!=null?rs.getString(8):"N/A");
				json.put("claimdesc", rs.getString(9)!=null?rs.getString(9):"N/A");
				json.put("urn", rs.getString(10)!=null?rs.getString(10):"N/A");
				pendinglist.put(json);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return pendinglist.toString();
	}

}
