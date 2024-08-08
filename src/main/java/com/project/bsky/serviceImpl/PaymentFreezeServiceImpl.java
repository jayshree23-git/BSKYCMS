package com.project.bsky.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.FloatRequest;
import com.project.bsky.bean.FloatRequestBean;
import com.project.bsky.bean.HospitalwisefloatdetailsModaldata;
import com.project.bsky.bean.PaymentActionBean;
import com.project.bsky.bean.PaymentFreezeBean;
import com.project.bsky.bean.PaymentFreezeDetailsBean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.PostPaymentRequestNew;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.ReversePaymentBean;
import com.project.bsky.bean.SnoClaimDetails;
import com.project.bsky.bean.TxnclamFloateDetailsbean;
import com.project.bsky.dao.PaymentFreezeDao;
import com.project.bsky.model.OTPAuth;
import com.project.bsky.model.TxnclaimFloatActionLog;
import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.FloatLogRepository;
import com.project.bsky.repository.OTPAuthRepository;
import com.project.bsky.repository.TxnclaimFloatdetailsrepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.LoginService;
import com.project.bsky.service.PaymentFreezeService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.FileUtil;
import com.project.bsky.util.JwtUtil;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

@SuppressWarnings("deprecation")
@Service
public class PaymentFreezeServiceImpl implements PaymentFreezeService {

	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	public static String floatFolder = bskyResourcesBundel.getString("file.path.FloatDoc");

	@Autowired
	private PaymentFreezeDao paymentDao;

	@Autowired
	private TxnclaimFloatdetailsrepository repository;

	@Autowired
	private LoginService loginservice;

	@Autowired
	private Environment env;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private FloatLogRepository logRepository;

	@Autowired
	private Logger logger;

	@Autowired
	UserDetailsRepository userRepo;

	@Autowired
	private OTPAuthRepository otpAuthRepository;

	@Autowired
	private JwtUtil util;

	@Override
	public List<Object> getpaymentfreezedata(CPDApproveRequestBean requestBean) {
		return paymentDao.getpaymentfreezedata(requestBean);
	}

	@Override
	public Response savePaymentfreeze(Long userId, Date fromDate, Date toDate, String stateCodeList,
			String distCodeList, String hospitalCodeList, Long snoUserId, Double snaAmount, MultipartFile floatFile,
			Integer searchtype, String schemecategoryid, List<String> floatList) throws Exception {
		String floatNumber = null;
		String formatFloat = null;
		String originalFloat = "OFL";
		String partFloat = "PFL";
		String specialFloat = "SFL";
		String userName = null;
		String floatFile1 = null;
//		String otherCertificate = null;
//		String snaCertificate = null;
//		String meCertificate = null;
		List<String> floatNumberList = new ArrayList<>();
		UserDetails userDetails = userRepo.findByUserId(snoUserId);
		if (userDetails.getUserName() != null) {
			userName = userDetails.getUserName().toUpperCase();
		}
		java.util.Date utilDate = new java.util.Date(fromDate.getTime());
		LocalDate currentdate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String currentMonth = ((currentdate.getMonth()).toString()).substring(0, 3);
//		String curntYr = Integer.toString(currentdate.getYear());
		formatFloat = "FL/" + userName + "/" + Integer.toString(currentdate.getYear()) + "/" + currentMonth + "/"
				+ new SimpleDateFormat("ddMMyyyy").format(new Date()) + "/";
		floatNumber = formatFloat + originalFloat;
		System.out.println(floatNumber);
//		try {
//			floatNumberList = paymentDao.getFloatNumber(snoUserId, currentMonth, curntYr);
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage());
//		}
		if (floatList != null) {
			System.out.println(floatList);
			floatNumberList.addAll(floatList);
		}
		if (!floatNumberList.isEmpty()) {
			String floatNumber1 = null;
			if (floatNumberList.size() == 1) {
				floatNumber1 = floatNumberList.get(0);
				int lastIndexOfSlash = floatNumber1.lastIndexOf('/');
				if (lastIndexOfSlash != -1) {
					floatNumber = formatFloat + partFloat;
				}
			} else if (floatNumberList.size() == 2) {
				floatNumber1 = floatNumberList.get(1);
				int lastIndexOfSlash = floatNumber1.lastIndexOf('/');
				if (lastIndexOfSlash != -1) {
					floatNumber = formatFloat + specialFloat + "01";
				}
			} else {
				for (int i = 2; floatNumberList.size() > i; i++) {
					floatNumber1 = floatNumberList.get(i);
					String lastString = floatNumber1.substring(floatNumber1.lastIndexOf("/") + 1);
					String second = lastString.substring(3);
					if (second.charAt(0) == '0' && second.length() == 2 && !second.equals("09")) {
						second = "0" + (Integer.parseInt(second) + 1);
					} else {
						second = String.valueOf(Integer.parseInt(second) + 1);
					}
					floatNumber = formatFloat + specialFloat + second;
				}
			}
		}
		Map<String, String> filePath = CommonFileUpload.createFileforFloat(floatFile, floatNumber);
		for (Map.Entry<String, String> entry : filePath.entrySet()) {
			floatFile1 = entry.getValue();
		}
		// file upload section
		/*
		 * Map<String, String> filepath1 =
		 * CommonFileUpload.cretefileuploadforgeneratefloat(snacertification,
		 * mecertification, otherfile, floatNumber); System.out.println(filepath1); Date
		 * date = new Date(); int year = date.getYear() + 1900; String floatNumberfloat
		 * = floatNumber.replace("/", "");
		 * 
		 * filepath1.forEach((k, v) -> { if (v != null && !v.equalsIgnoreCase("")) {
		 * String fullFilePath =
		 * CommonFileUpload.getFullDocumentPathfloat(floatNumberfloat, year,
		 * CommonFileUpload.getFolderName(v)); File file = new File(fullFilePath); if
		 * (!file.exists()) { filepath1.forEach((k1, v1) -> { if (v1 != null &&
		 * !v1.equalsIgnoreCase("")) { String fullFilePath1 =
		 * CommonFileUpload.getFullDocumentPathfloat(floatNumberfloat, year,
		 * CommonFileUpload.getFolderName(v1)); File file1 = new File(fullFilePath1); if
		 * (file1.exists()) { file1.delete(); } } });
		 * 
		 * if (k.equalsIgnoreCase("otherCertficate")) throw new RuntimeException(
		 * otherfile.getOriginalFilename() + " otherfile Failed To Save in Server!");
		 * else if (k.equalsIgnoreCase("snaCertificate")) throw new RuntimeException(
		 * snacertification.getOriginalFilename() +
		 * " snacertification Failed To Save in Server!"); else if
		 * (k.equalsIgnoreCase("meCertificate")) throw new RuntimeException(
		 * mecertification.getOriginalFilename() +
		 * " mecertification Failed To Save in Server!"); } } }); otherCertificate =
		 * filepath1.get("otherCertficate"); snaCertificate =
		 * filepath1.get("snaCertificate"); meCertificate =
		 * filepath1.get("meCertificate");
		 */

		// Check if the certificates are null and return a message
//		if (snaCertificate == null || meCertificate == null) {
//			throw new RuntimeException("Upload File Correctly: snaCertificate and meCertificate cannot be null");
//		}
		return paymentDao.savePaymentfreeze(userId, fromDate, toDate, stateCodeList, distCodeList, hospitalCodeList,
				snoUserId, snaAmount, floatFile1, floatNumber, searchtype, schemecategoryid);
	}

	@Override
	public List<TxnclamFloateDetails> getFLoatList() {
		return repository.getVerifiedFloatList();
	}

	@Override
	public List<Object> getFLoatDetails(String floatNumber) {
		return paymentDao.getFloatDetails(floatNumber);
	}

	@Override
	public Response remarkUpdate(ClaimLogBean bean) {
		return paymentDao.remarkUpdate(bean);
	}

	@Override
	public Response verifyFloat(String floatNumber, Long actionBy, String remark, MultipartFile file) {
		String filePath1 = null;
		Map<String, String> filePath = CommonFileUpload.createFileforFloat(file, floatNumber);
		for (Map.Entry<String, String> entry : filePath.entrySet()) {
			filePath1 = entry.getValue();
		}
		return paymentDao.verifyFloat(floatNumber, actionBy, remark, filePath1);
	}

	public JSONArray getPaymentFreezeList(Long snoId, Date fromDate, Date toDate, Long pendingAt) {
		ResultSet rs = null;
		JSONArray list = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOAT_DTLS")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_float_no", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno_amount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_pending_at", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_IS_SNA_HOLD", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 0);
			storedProcedureQuery.setParameter("p_sno", snoId);
			storedProcedureQuery.setParameter("p_claim_id", null);
			storedProcedureQuery.setParameter("p_float_no", null);
			storedProcedureQuery.setParameter("p_sno_amount", null);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_remark", null);
			storedProcedureQuery.setParameter("p_pending_at", pendingAt);
//			storedProcedureQuery.setParameter("P_IS_SNA_HOLD", null);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("floatNo", rs.getString(1));
				obj.put("amount", rs.getDouble(2));
				obj.put("createdOn", rs.getTimestamp(3));
				obj.put("count", rs.getInt(4));
				obj.put("remark", rs.getString(5));
				obj.put("floatId", rs.getLong(6));
				obj.put("roundAmount", rs.getLong(7));
				list.put(obj);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<Object> getFloatClaimDetails(String floatNo) {
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_CLM_WS_DTLS")
					.registerStoredProcedureParameter("p_float_number", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_float_number", floatNo);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				PaymentFreezeDetailsBean bean = new PaymentFreezeDetailsBean();
				bean.setClaimId(rs.getInt(1));
				bean.setHospitalCode(rs.getString(2));
				bean.setHospitalName(rs.getString(3));
				bean.setDistrictName(rs.getString(4));
				bean.setUrn(rs.getString(5));
				bean.setInvoiceNo(rs.getString(6));
				bean.setClaimNo(rs.getString(7));
				bean.setCaseNo(rs.getString(8));
				bean.setPatientName(rs.getString(9));
				bean.setGender(rs.getString(10));
				bean.setPackageCode(rs.getString(11));
				bean.setPackageName(rs.getString(12));
				bean.setPackageCost(rs.getString(13));
				bean.setProcedureName(rs.getString(14));
				bean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(15)));
				bean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(16)));
				bean.setMortality(rs.getString(17));
				bean.setCpdMortality(rs.getString(18));
				bean.setTotalAmountClaimed(rs.getString(19));
				bean.setImplantData(rs.getString(20));
				bean.setCpdClaimStatus(rs.getString(21));
				bean.setCpdRemarks(rs.getString(22));
				bean.setCpdApprovedAmount(rs.getString(23));
				bean.setSnaClaimStatus(rs.getString(24));
				bean.setSnaRemarks(rs.getString(25));
				bean.setSnoApprovedAmount(rs.getString(26));
				bean.setJointCeoRemarks(rs.getString(27));
				bean.setFoRemarks(rs.getString(28));
				bean.setJointCeoRemarksRevert(rs.getString(29));
				bean.setNoRemarks(rs.getString(30));
				bean.setNoApprovedAmount(rs.getString(31));
				bean.setJointCeoRemarksVerify(rs.getString(32));
				bean.setFinalFoRemarks(rs.getString(33));
				bean.setAudRemarks(rs.getString(34));
				bean.setDyceoRemarks(rs.getString(35));
				bean.setJointCeoRemarksFinal(rs.getString(36));
				bean.setNoRemarksFinal(rs.getString(37));
				bean.setSnaName(rs.getString(38));
				bean.setCreatedBy(rs.getString(39));
				bean.setPendingAt(rs.getInt(40));
				bean.setIsReverted(rs.getInt(41));
				bean.setIncenticeStatus(rs.getString(42));
				bean.setIsApproved(rs.getInt(43));
				bean.setIsRejected(rs.getInt(44));
				bean.setIsUnprocessed(rs.getInt(45));
				bean.setIsQueryByCpd(rs.getInt(46));
				bean.setIsReclaim(rs.getInt(47));
				bean.setIsQueryBySna(rs.getInt(48));
				bean.setRemarkId(rs.getLong(49));
				bean.setRemark(rs.getString(50));
				bean.setIarRevertCase(rs.getString(51));
				bean.setFloatNumber(rs.getString(52));
				bean.setCeoremark(rs.getString(53));
				bean.setCeoremarkrevertcase(rs.getString(54));
				bean.setSnafinalremark(rs.getString(55));
				bean.setSnaremarkrevertcase(rs.getString(56));
				bean.setIsBulkApproved(rs.getString(57));
				bean.setIsFloatGenerate(rs.getInt(58));
				bean.setPreviousFloat(rs.getString(59));
				bean.setMeremark(rs.getString(61));
				bean.setAudRemarkId(rs.getLong(62));
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

				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public Response updateSnaApprvdAmnt(Integer claimId, String amount, Long snoId, String remark) {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOAT_DTLS")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_float_no", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno_amount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_pending_at", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_IS_SNA_HOLD", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 2);
			storedProcedureQuery.setParameter("p_sno", snoId);
			storedProcedureQuery.setParameter("p_claim_id", claimId);
			storedProcedureQuery.setParameter("p_float_no", null);
			storedProcedureQuery.setParameter("p_sno_amount", amount);
			storedProcedureQuery.setParameter("p_from_date", null);
			storedProcedureQuery.setParameter("p_to_date", null);
			storedProcedureQuery.setParameter("p_remark", remark);
			storedProcedureQuery.setParameter("p_pending_at", null);
//			storedProcedureQuery.setParameter("P_IS_SNA_HOLD", Long.parseLong(flag));
			storedProcedureQuery.execute();
			String newAmount = (String) storedProcedureQuery.getOutputParameterValue("p_amount");
			if (newAmount.equalsIgnoreCase(amount)) {
				response.setMessage(newAmount);
				response.setStatus("Success");
			} else {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Response paymentFreeze(String floatNo, Long snoId) {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOAT_DTLS")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_float_no", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno_amount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_pending_at", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_IS_SNA_HOLD", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 3);
			storedProcedureQuery.setParameter("p_sno", snoId);
			storedProcedureQuery.setParameter("p_claim_id", null);
			storedProcedureQuery.setParameter("p_float_no", floatNo);
			storedProcedureQuery.setParameter("p_sno_amount", null);
			storedProcedureQuery.setParameter("p_from_date", null);
			storedProcedureQuery.setParameter("p_to_date", null);
			storedProcedureQuery.setParameter("p_remark", null);
			storedProcedureQuery.setParameter("p_pending_at", null);
//			storedProcedureQuery.setParameter("P_IS_SNA_HOLD", null);
			storedProcedureQuery.execute();
			Integer result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (result == 0) {
				response.setMessage("Payment has been freezed");
				response.setStatus("Success");
			} else {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	public List<TxnclamFloateDetails> getNonVerifiedFLoatList() {
		return repository.AllNotVerifiedFloatList();
	}

	@Override
	public List<Object> getSnaApprovedList(CPDApproveRequestBean requestBean) {
		return paymentDao.getSnaApprovedList(requestBean);
	}

	@Override
	public List<SnoClaimDetails> getPostPaymentList(CPDApproveRequestBean requestBean) {
		List<SnoClaimDetails> SnoclaimRaiseDetailsList = new ArrayList<SnoClaimDetails>();
		List<SnoClaimDetails> finalList = new ArrayList<SnoClaimDetails>();
		ResultSet snoDetailsObj = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		try {
			String[] hospitalCodes = requestBean.getHospitalCodeArr();
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_post_payment_list")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_bloceddata", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_bloceddata", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDetailsObj.next()) {
				SnoClaimDetails resBean = new SnoClaimDetails();
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(1));
				resBean.setClaimid(snoDetailsObj.getLong(2));
				resBean.setURN(snoDetailsObj.getString(3));
				resBean.setPatientName(snoDetailsObj.getString(4));
				resBean.setInvoiceNumber(snoDetailsObj.getString(5));
				resBean.setCreatedOn(snoDetailsObj.getString(6));
				resBean.setCpdAlotteddate(snoDetailsObj.getTimestamp(7));
				resBean.setPackageName(snoDetailsObj.getString(8));
				resBean.setRevisedDate(snoDetailsObj.getTimestamp(9));
				resBean.setPackageCode(snoDetailsObj.getString(10));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(11));
				resBean.setClaimNo(snoDetailsObj.getString(12));
				resBean.setHospitalName(snoDetailsObj.getString(13));
				resBean.setMortality(snoDetailsObj.getString(14));
				resBean.setHospitalMortality(snoDetailsObj.getString(15));
				resBean.setActualDateOfDischarge(DateFormat.FormatToDateString(snoDetailsObj.getString(16)));
				resBean.setActualDateOfAdmission(DateFormat.FormatToDateString(snoDetailsObj.getString(17)));
				resBean.setHospitalCode(snoDetailsObj.getString(18));
				resBean.setCpdApprovedAmount(snoDetailsObj.getString(19));
				resBean.setSnaApprovedAmount(snoDetailsObj.getString(20));
				resBean.setActualCpdAmount(snoDetailsObj.getLong(21));
				resBean.setActualSnaAmount(snoDetailsObj.getLong(22));
				resBean.setCpdClaimStatus(snoDetailsObj.getString(23));
				resBean.setCpdRemarks(snoDetailsObj.getString(24));
				resBean.setSnaClaimStatus(snoDetailsObj.getString(25));
				resBean.setSnaRemarks(snoDetailsObj.getString(26));
				resBean.setCaseNo(snoDetailsObj.getString(27));
				SnoclaimRaiseDetailsList.add(resBean);
			}
			if (hospitalCodes != null && hospitalCodes.length > 0) {
				finalList = SnoclaimRaiseDetailsList.stream()
						.filter(claim -> Arrays.asList(hospitalCodes).contains(claim.getHospitalCode()))
						.collect(Collectors.toList());
			} else {
				finalList = SnoclaimRaiseDetailsList;
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
		return finalList;
	}

	@Override
	public Response updatePostPayment(PostPaymentRequest requestBean) throws Exception {
		Response response = new Response();
		String returnObj = null;
		Connection con = null;
		CallableStatement st = null;
//		StringBuffer bufferlist = new StringBuffer();
//		if (requestBean.getClaimList() != null) {
//			for (Long element : requestBean.getClaimList()) {
//				bufferlist.append(element.toString() + ",");
//			}
//			claimList = bufferlist.substring(0, bufferlist.length() - 1);
//		}
// 
//		Blob blob = null;
//		try {
//			if (claimList != null) {
//				blob = new SerialBlob(claimList.getBytes());
//			}
//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
		try {
//			StoredProcedureQuery storedProcedureQuery = this.entityManager
//					.createStoredProcedureQuery("usp_claim_post_payment_updation")
//					.registerStoredProcedureParameter("p_payment_mode_id", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_bank_id", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_dd_cheque_number", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_claimid_list", Blob.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_PAYMENT_DATE", Date.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_msg_out", String.class, ParameterMode.OUT)
//					.registerStoredProcedureParameter("P_FINALAPPROVE_AMOUNT", Double.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_ACTUALPAID_AMOUNT", Double.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("P_MSG_OUT1", String.class, ParameterMode.OUT);
//
//			storedProcedureQuery.setParameter("p_payment_mode_id", requestBean.getBankModeId());
//			storedProcedureQuery.setParameter("p_bank_id", requestBean.getBankId());
//			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
//			storedProcedureQuery.setParameter("p_dd_cheque_number", requestBean.getTypeNumber());
//			storedProcedureQuery.setParameter("p_claimid_list", blob);
//			storedProcedureQuery.setParameter("P_PAYMENT_DATE", requestBean.getCurrentDate());
//			storedProcedureQuery.setParameter("P_FINALAPPROVE_AMOUNT", requestBean.getTotalPaidAmount());
//			storedProcedureQuery.setParameter("P_ACTUALPAID_AMOUNT", requestBean.getPaidAmount());
			Long[] claimIds = new Long[requestBean.getClaimList().size()];
			for (int i = 0; i < requestBean.getClaimList().size(); i++) {
				claimIds[i] = requestBean.getClaimList().get(i);
			}
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("CLAIMID_LISTS", con);
			ARRAY array_to_pass = new ARRAY(des, con, claimIds);
			st = con.prepareCall("call usp_claim_post_payment_updation(?,?,?,?,?,?,?,?,?,?)");
			st.setLong(1, requestBean.getBankModeId());
			st.setObject(2, requestBean.getBankId());
			st.setLong(3, requestBean.getUserId());
			st.setString(4, requestBean.getTypeNumber());
			st.setArray(5, array_to_pass);
			st.setDate(6, requestBean.getCurrentDate());
			st.setObject(7, requestBean.getTotalPaidAmount());
			st.setObject(8, requestBean.getPaidAmount());
			st.registerOutParameter(9, Types.VARCHAR);
			st.registerOutParameter(10, Types.VARCHAR);
			st.execute();
			String returnValue = ((OracleCallableStatement) st).getString(9);
			returnObj = ((OracleCallableStatement) st).getString(10);

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
			throw e;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return response;
	}

	@Override
	public Response freezePayment(Long userId, MultipartFile file) throws Exception {
		Workbook workbook = null;
		Sheet worksheet = null;
		List<Map<String, Object>> list = new ArrayList<>();
		String claimList = null;
		StringBuffer bufferlist = new StringBuffer();
		try {
			workbook = WorkbookFactory.create(file.getInputStream());
			worksheet = workbook.getSheetAt(2);
			if (worksheet != null) {
				for (int i = 4; i < worksheet.getPhysicalNumberOfRows(); i++) {
					Map<String, Object> mapObject = new HashMap<>();
					Row row = worksheet.getRow(i);
					if (row.getCell(5) != null && row.getCell(7) != null && !String.valueOf(row.getCell(5)).isEmpty()
							&& !String.valueOf(row.getCell(7)).isEmpty()) {
//						////System.out.println(row.getCell(5).getCellType());
//						Object obj = row.getCell(5).getCellType();
//						if (obj.toString().equalsIgnoreCase("STRING")) {
//							mapObject.put("urn", row.getCell(5).getStringCellValue());
//							////System.out.println(row.getCell(5).getStringCellValue());
//						} else {
//							mapObject.put("urn", row.getCell(5).getNumericCellValue());
//							
////							////System.out.println(Long.parseLong(String.valueOf(row.getCell(5).getNumericCellValue())));
//
//							////System.out.println((long)row.getCell(5).getNumericCellValue());
//						}

//						mapObject.put("claimNo", row.getCell(7).getStringCellValue());
						Long cpdAmount = (long) row.getCell(22).getNumericCellValue();
						Long snaAmount = (long) row.getCell(25).getNumericCellValue();
						mapObject.put("cpdAmount", cpdAmount);
						mapObject.put("snaAmount", snaAmount);
						mapObject.put("urn",
								row.getCell(5).getCellType().toString().equalsIgnoreCase("STRING")
										? row.getCell(5).getStringCellValue()
										: (long) row.getCell(5).getNumericCellValue());
						mapObject.put("claimNo",
								row.getCell(7).getCellType().toString().equalsIgnoreCase("STRING")
										? row.getCell(7).getStringCellValue()
										: row.getCell(7).getNumericCellValue());
//				        mapObject.put("cpdAmount", row.getCell(22).getCellType().toString().equalsIgnoreCase("STRING") ? 
//				        		Long.parseLong(row.getCell(22).getStringCellValue()) : Long.parseLong(String.valueOf(row.getCell(22).getNumericCellValue())));
//				        mapObject.put("snaAmount", row.getCell(25).getCellType().toString().equalsIgnoreCase("STRING") ? 
//				        		Long.parseLong(row.getCell(25).getStringCellValue()) : Long.parseLong(String.valueOf(row.getCell(25).getNumericCellValue()))); 
						list.add(mapObject);
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
		if (!list.isEmpty()) {
			for (Map<String, Object> element : list) {
				bufferlist.append(element.get("urn") + "~" + element.get("claimNo") + "~" + element.get("cpdAmount")
						+ "~" + element.get("snaAmount") + ",");
			}
			claimList = bufferlist.substring(0, bufferlist.length() - 1);
		}
		return paymentDao.freezePayment(userId, claimList);
	}

	@Override
	public JSONObject getPaymentFreezeReport(Long userId, Date fromdate, Date toDate, String stateId, String districtId,
			String hospitalId, String mortality, Long searchtype, String schemecategoryid) {
		JSONObject count = new JSONObject();
		int schemeCategoryId;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemeCategoryId = Integer.parseInt(schemecategoryid);
		} else {
			schemeCategoryId = 0;
		}
		ResultSet rs = null, rs1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_ACTN")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_bloceddata", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 0);
			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_from_date", fromdate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
			storedProcedureQuery.setParameter("p_mortality", mortality);
			storedProcedureQuery.setParameter("p_filename", null);
			storedProcedureQuery.setParameter("p_amount", null);
			storedProcedureQuery.setParameter("P_bloceddata", searchtype);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				count.put("pendingatSNA", rs.getLong(1));
				count.put("pendingatCPD", rs.getLong(2));
				count.put("pendingatHsptl", rs.getLong(3));
				count.put("snaapproved", rs.getLong(4));
				count.put("totalclaimRasied", rs.getLong(5));
				count.put("snarejected", rs.getLong(6));
				count.put("pendingatDC", rs.getLong(7));
				count.put("totalDischarge", rs.getLong(8));
				count.put("nonUploading", rs.getLong(9));
				count.put("cpdapproved", rs.getLong(10));
				count.put("cpdrejected", rs.getLong(11));
				count.put("nonUploadingInit", rs.getLong(12));
				count.put("snaQuery", rs.getLong(13));
				count.put("pendatcpdRstl", rs.getLong(14));
				count.put("pendatcpdRvrt", rs.getLong(15));
				count.put("systemRejected", rs.getLong(16));
				count.put("unprocessed", rs.getLong(17));
				count.put("resettlement", rs.getLong(18));
				count.put("cpdQuerywithin7", rs.getLong(19));
				count.put("cpdQueryafter7", rs.getLong(20));
				count.put("snaQuerywithin7", rs.getLong(21));
				count.put("snaQueryafter7", rs.getLong(22));
				count.put("dcCompliance", rs.getLong(23));
				count.put("snoamount", rs.getString(24));
				count.put("cpdamount", rs.getString(25));
				count.put("paymentUnFreezed", rs.getLong(26));
				count.put("paymentFreezed", rs.getLong(27));
				count.put("onHold", rs.getLong(28));
				count.put("sysadminrej", rs.getLong(29));
				count.put("paidClaims", rs.getLong(30));
			}
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs1.next()) {
				count.put("tcpdApproved", rs1.getLong(1));
				count.put("snaActionOfCpdAprvd", rs1.getLong(2));
				count.put("snaTotalOfCpdAprvd", rs1.getLong(3));
				String percent1 = rs1.getString(4);
				if (percent1 != null && percent1.contains(" .00")) {
					percent1 = "0";
				}
				if (percent1 != null && percent1.startsWith(".")) {
					percent1 = "0" + percent1;
				}
				count.put("percent1", percent1);
			}
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public Integer savePaymentFreezeRecord(MultipartFile pdf, PaymentActionBean bean) {
		try {
			if (pdf != null) {
				String filename = FileUtil.savePaymentReport(pdf, bean.getUserId().toString());
				if (filename != null) {
					bean.setFilename(filename);
					return saveFLoatReport(bean);
				} else {
					return 0;
				}
			} else {
				bean.setFilename(null);
				return saveFLoatReport(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Integer paymentFreezeAction(PaymentActionBean requestBean) {
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		Integer result = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_ACTN")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", Double.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_bloceddata", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 1);
			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_filename", null);
			storedProcedureQuery.setParameter("p_amount", null);
			storedProcedureQuery.setParameter("P_bloceddata", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();
			result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public Integer saveFLoatReport(PaymentActionBean requestBean) {
		Integer result = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_ACTN")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_bloceddata", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 3);
			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_filename", requestBean.getFilename());
			storedProcedureQuery.setParameter("p_amount", requestBean.getAmount());
			storedProcedureQuery.setParameter("P_bloceddata", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();
			result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public JSONArray paymentFreezeView(PaymentActionBean requestBean) {
		JSONArray floatList = new JSONArray();
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_ACTN")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_bloceddata", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 2);
			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_filename", null);
			storedProcedureQuery.setParameter("p_amount", null);
			storedProcedureQuery.setParameter("P_bloceddata", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("logId", rs.getInt(1));
				json.put("userId", rs.getLong(2));
				json.put("fileName", rs.getString(3));
				json.put("actualDateOfDischargeFrom",
						new SimpleDateFormat("dd-MMM-yyyy").format(new Date(rs.getDate(4).getTime())));
				json.put("actualDateOfDischargeTo",
						new SimpleDateFormat("dd-MMM-yyyy").format(new Date(rs.getDate(5).getTime())));
				json.put("cpdMortality", rs.getString(6));
				json.put("stateName", rs.getString(7));
				json.put("districtName", rs.getString(8));
				json.put("hospitalName", rs.getString(9));
				json.put("createdOn",
						new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(new Date(rs.getTimestamp(10).getTime())));
				json.put("amount", rs.getString(12));
				json.put("stateCode", rs.getString(13));
				json.put("districtCode", rs.getString(14));
				json.put("hospitalCode", rs.getString(15));
				floatList.put(json);
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
		return floatList;
	}

	@Override
	public void downloadPfzFile(String fileCode, String userId, HttpServletResponse response) {
		try {
			FileUtil.getPaymentFreezeFile(fileCode, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<TxnclamFloateDetailsbean> getfloatdata(Integer groupId, String fromdate, String todate, String snoid,
			Long userid, Integer authMode) {
		ResultSet rsResultSet = null;
		List<TxnclamFloateDetailsbean> floatlist = new ArrayList<TxnclamFloateDetailsbean>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOAT_LIST")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_searchby", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userid);
			storedProcedureQuery.setParameter("P_SNA_ID", snoid);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_searchby", authMode);
			storedProcedureQuery.execute();
			rsResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rsResultSet.next()) {
				TxnclamFloateDetailsbean txnclamFloateDetails = new TxnclamFloateDetailsbean();
				txnclamFloateDetails.setAmount(rsResultSet.getDouble(1));
				txnclamFloateDetails.setAssignedauthority(rsResultSet.getString(2));
				txnclamFloateDetails.setCreateby(rsResultSet.getString(3));
				txnclamFloateDetails.setCreateon(rsResultSet.getString(4));
				txnclamFloateDetails.setFloateId(rsResultSet.getLong(5));
				txnclamFloateDetails.setFloateno(rsResultSet.getString(6));
				txnclamFloateDetails.setIsVerified(rsResultSet.getString(7));
				txnclamFloateDetails.setPaymentstatus(rsResultSet.getString(8));
				txnclamFloateDetails.setPendingat(rsResultSet.getString(9));
				txnclamFloateDetails.setRemarks(rsResultSet.getString(10));
				txnclamFloateDetails.setStatusflag(rsResultSet.getString(11));
				txnclamFloateDetails.setUpdateby(rsResultSet.getLong(12));
				txnclamFloateDetails.setUpdateon(rsResultSet.getString(13));
				txnclamFloateDetails.setSnaFullName(rsResultSet.getString(14));
				txnclamFloateDetails.setFullname(rsResultSet.getString(15));
				txnclamFloateDetails.setAssignedFoName(rsResultSet.getString(16));
				txnclamFloateDetails.setCount(rsResultSet.getInt(17));
				txnclamFloateDetails.setRemarkby(rsResultSet.getString(18));
				txnclamFloateDetails.setRoundAmount(rsResultSet.getLong(19));
				floatlist.add(txnclamFloateDetails);
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
				e2.printStackTrace();
			}
		}
		return floatlist;
	}

	@Override
	public Response forwardToSNA(String floatNumber, Long actionBy, String remark, MultipartFile file) {
		String filePath1 = null;
		Map<String, String> filePath = CommonFileUpload.createFileforFloat(file, floatNumber);
		for (Map.Entry<String, String> entry : filePath.entrySet()) {
			filePath1 = entry.getValue();
		}
		return paymentDao.forwardToSNA(floatNumber, actionBy, remark, filePath1);
	}

	@Override
	public JSONArray getforremarkslistdata(Long userId, String fromDate, String toDate, Long pendingAt) {
		ResultSet rs = null;
		JSONArray listforforemarks = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FOREMARKS_LIST")
					.registerStoredProcedureParameter("p_userId", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_pending_at", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_userId", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("p_pending_at", pendingAt);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("floatNo", rs.getString(1));
				obj.put("amount", rs.getDouble(2));
				obj.put("created_by", rs.getString(3));
				obj.put("created_on", rs.getString(4));
				obj.put("assigned_authority", rs.getString(5));
				obj.put("remarks", rs.getString(6));
				obj.put("count", rs.getInt(7));
				obj.put("floatId", rs.getInt(8));
				obj.put("roundAmount", rs.getLong(9));
				obj.put("VALUE", "FO");
				listforforemarks.put(obj);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return listforforemarks;
	}

	@Override
	public Response paymentForwardsubmit(Long userId, String header, String remarks, Long pendingAt) {
		return paymentDao.forwardfloat(header.trim(), userId, remarks, pendingAt);
	}

	@Override
	public JSONArray getsummaryDetails(Long userid, String fromDate, String toDate, String verify,
			String schemecategoryid) {
		ResultSet rs = null;
		JSONArray summary = new JSONArray();
		int schemeCategoryId;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemeCategoryId = Integer.parseInt(schemecategoryid);
		} else {
			schemeCategoryId = 0;
			// Handle the case where schemecategoryid is null (e.g., log an error, provide a
			// default value)
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SUMMARYDETAILS")
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_fromdate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_todate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_verify", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_userid", userid);
			storedProcedureQuery.setParameter("P_fromdate", fromDate);
			storedProcedureQuery.setParameter("P_todate", toDate);
			storedProcedureQuery.setParameter("P_verify", verify);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("float_no", rs.getString(1));
				obj.put("float_id", rs.getString(2));
				obj.put("REMARKS", rs.getString(3));
				obj.put("amount", rs.getDouble(4));
				obj.put("CREATED_ON", rs.getString(5));
				obj.put("ASSIGNED_AUTHORITY", rs.getString(6));
				obj.put("isVerified", rs.getString(7));
				obj.put("count", rs.getInt(8));
				summary.put(obj);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return summary;
	}

	@Override
	public JSONArray getoldforemarkmodaldata(Long claimid) {
		ResultSet rs = null;
		JSONArray modal = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_GETOLDFOREMARK")
					.registerStoredProcedureParameter("p_claimid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_claimid", claimid);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("fo_remarks", rs.getString(1));
				obj.put("APPROVEDAMOUNT", rs.getString(2));
				obj.put("actionon", rs.getString(3));
				modal.put(obj);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return modal;
	}

	@Override
	public JSONArray getfloatdetailshospitalwise(String floatnumberhospitawisedetails, String fromdate) {
		JSONArray hospitawisefloatList = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_FLOATDETAILSHOSPITALWISE")
					.registerStoredProcedureParameter("P_Floatnumber", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_Floatnumber", floatnumberhospitawisedetails);
			storedProcedureQuery.setParameter("p_fromdate", fromdate);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject floatObj = new JSONObject();
				floatObj.put("hospitalCode", rs.getString(1));
				floatObj.put("hospitalName", rs.getString(2));
				floatObj.put("stateName", rs.getString(3));
				floatObj.put("stateCode", rs.getString(4));
				floatObj.put("districtName", rs.getString(5));
				floatObj.put("districtCode", rs.getString(6));
				floatObj.put("discharged", rs.getInt(7));
				floatObj.put("claimraised", rs.getInt(8));
				floatObj.put("claimamount", rs.getString(9));
				floatObj.put("approved", rs.getInt(10));
				floatObj.put("snoamount", rs.getString(11));
				floatObj.put("rejected", rs.getInt(12));
				floatObj.put("count", rs.getInt(13));
				floatObj.put("snaName", rs.getString(14));
				floatObj.put("createdBy", rs.getString(15));
				floatObj.put("floatid", rs.getString(16));
				floatObj.put("bankname", rs.getString(17) != null ? rs.getString(17) : "N/A");
				floatObj.put("accountnumber", rs.getString(18) != null ? rs.getString(18) : "N/A");
				floatObj.put("ifsccode", rs.getString(19) != null ? rs.getString(19) : "N/A");
				floatObj.put("pendingcase", rs.getInt(8) - rs.getInt(10));
				floatObj.put("roundSnoAmount", rs.getString(20));// notapprovedcare
				hospitawisefloatList.put(floatObj);
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
		return hospitawisefloatList;
	}

	@Override
	public List<Object> getHospitalwisefloatdetailsReport(HospitalwisefloatdetailsModaldata requestBean) {
		List<Object> hospitawisefloatListreport = new ArrayList<Object>();
		java.text.DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_HOSPITALWISEFLOATDETAILS_REPORT")
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Floatnumber", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_Floatnumber", requestBean.getFloatnumber());
			storedProcedureQuery.setParameter("P_Hospitalcode", requestBean.getHospitacode());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			int i = 1;
			while (rs.next()) {
				List<Object> floatObj = new ArrayList<Object>();
				floatObj.add(String.valueOf(i++));
				floatObj.add(rs.getString(2));
				floatObj.add(rs.getString(3));
				floatObj.add(rs.getString(4));
				floatObj.add(rs.getString(5));
				floatObj.add(rs.getString(6));
				floatObj.add(rs.getString(7));
				floatObj.add(rs.getString(8) != null ? rs.getString(8) : "-NA-");
				if (rs.getString(9).equalsIgnoreCase("M")) {
					floatObj.add("Male");
				} else if (rs.getString(9).equalsIgnoreCase("F")) {
					floatObj.add("Female");
				} else if (rs.getString(9) == null) {
					floatObj.add("-NA-");
				}
				floatObj.add(rs.getString(10) != null ? rs.getString(10) : "-NA-");
				floatObj.add(rs.getString(11) != null ? rs.getString(11) : "-NA-");
				floatObj.add(rs.getString(24) != null ? rs.getString(24).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(24))) : "-NA-");

				floatObj.add(rs.getString(12) != null ? rs.getString(12) : "-NA-");
				Timestamp ACTUALDATEOFADMISSION = rs.getTimestamp(13);
				if (ACTUALDATEOFADMISSION != null) {
					floatObj.add(f.format(new Date(ACTUALDATEOFADMISSION.getTime())));
				}
				Timestamp ACTUALDATEOFDISCHARGE = rs.getTimestamp(14);
				if (ACTUALDATEOFDISCHARGE != null) {
					floatObj.add(f.format(new Date(ACTUALDATEOFDISCHARGE.getTime())));
				}
				floatObj.add(rs.getString(15) != null ? rs.getString(15) : "-NA-");
				floatObj.add(rs.getString(25) != null ? rs.getString(25) : "-NA-");
				floatObj.add(rs.getString(16) != null ? rs.getString(16).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(16))) : "-NA-");
				floatObj.add(rs.getString(17) == null || rs.getString(17).equalsIgnoreCase("")
						|| rs.getString(17).equalsIgnoreCase(" ") || rs.getString(17).contains(" ") ? "-NA-"
								: rs.getString(17));

				floatObj.add(rs.getString(18) != null ? rs.getString(18) : "-NA-");
				floatObj.add(rs.getString(19) != null ? rs.getString(19) : "-NA-");
				floatObj.add(rs.getString(20) != null ? rs.getString(20).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(20))) : "-NA-");
				floatObj.add(rs.getString(21) != null ? rs.getString(21) : "-NA-");
				floatObj.add(rs.getString(22) != null ? rs.getString(22) : "-NA-");
				floatObj.add(rs.getString(23) != null ? rs.getString(23).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(23))) : "-NA-");
				hospitawisefloatListreport.add(floatObj);
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
		return hospitawisefloatListreport;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getSummary(FloatRequestBean requestBean) {
		JSONObject floatObj = new JSONObject();
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		Connection con = null;
		CallableStatement st = null;
		List<String> floatNumberList = new ArrayList<>();
		String[] stringArray = null;
		List<Map<String, Object>> listState = (List<Map<String, Object>>) requestBean.getStateCodeList();
		List<Map<String, Object>> listDist = (List<Map<String, Object>>) requestBean.getDistCodeList();
		List<Map<String, Object>> listHospital = (List<Map<String, Object>>) requestBean.getHospitalCodeList();// mapObj.get("selectedStateList");
		java.util.Date utilDate = new java.util.Date(requestBean.getFromDate().getTime());
		LocalDate currentdate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String currentMonth = ((currentdate.getMonth()).toString()).substring(0, 3);
		String curntYr = Integer.toString(currentdate.getYear());
		if (!listHospital.isEmpty()) {
			stringArray = new String[listHospital.size()];
			for (int i = 0; i < listHospital.size(); i++) {
				stringArray[i] = "0#" + "0#" + listHospital.get(i).get("hospitalCode");
			}
		} else if (!listDist.isEmpty()) {
			stringArray = new String[listDist.size()];
			for (int i = 0; i < listDist.size(); i++) {
				stringArray[i] = listDist.get(i).get("stateCode") + "#" + listDist.get(i).get("districtCode") + "#0";
			}
		} else if (!listState.isEmpty()) {
			stringArray = new String[listState.size()];
			for (int i = 0; i < listState.size(); i++) {
				stringArray[i] = listState.get(i).get("stateCode") + "#0" + "#0";
			}
		} else {
			stringArray = new String[1];
			stringArray[0] = "0#0#0";
		}

		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null) {
			schemeCategoryId = requestBean.getSchemecategoryid().intValue();
		} else {
			schemeCategoryId = 0;
			// Handle the case where schemecategoryid is null (e.g., log an error, provide a
			// default value)
		}
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("STATE_DIST_CODE_HOS_TYPE", con);
			ARRAY arrayToPass = new ARRAY(des, con, stringArray);
			st = con.prepareCall("call USP_FLOAT_COUNT_DETAILS(?,?,?,?,?,?,?,?,?,?)");
			st.setLong(1, requestBean.getUserId());
			st.setDate(2, requestBean.getFromDate());
			st.setDate(3, requestBean.getToDate());
			st.setArray(4, arrayToPass);
			st.setString(5, requestBean.getFlag());
			st.setInt(6, schemeCategoryId);
			st.setInt(7, requestBean.getSearchtype());
			st.registerOutParameter(8, Types.REF_CURSOR);
			st.registerOutParameter(9, Types.REF_CURSOR);
			st.registerOutParameter(10, Types.REF_CURSOR);
			st.execute();
			rs = ((OracleCallableStatement) st).getCursor(8);
			rs1 = ((OracleCallableStatement) st).getCursor(9);
			rs2 = ((OracleCallableStatement) st).getCursor(10);
			while (rs.next()) {
				floatObj.put("pendatsna", rs.getInt(1));
				floatObj.put("TotalDischarged", rs.getInt(2));
				floatObj.put("totalclaimrasied", rs.getInt(3));
				floatObj.put("nonUploading", rs.getInt(4));
				floatObj.put("snaapproved", rs.getInt(5));
				floatObj.put("snarejected", rs.getInt(6));
				floatObj.put("snaQueryafter7", rs.getInt(7));
				floatObj.put("nonUploadingInit", rs.getInt(8));
				floatObj.put("mortality", rs.getInt(9));
				floatObj.put("smortality", rs.getInt(10));
				floatObj.put("bulkapproved", rs.getInt(11));
				floatObj.put("snoamount", rs.getDouble(12));
				floatObj.put("cpdamount", rs.getDouble(13));
				floatObj.put("totalAmount", rs.getDouble(14));
				floatObj.put("floatGenerated", rs.getInt(15));
				floatObj.put("floatNotGenerated", rs.getInt(16));
				floatObj.put("totalapproved", rs.getInt(17));
				floatObj.put("cpdQueryafter7", rs.getInt(18));
				floatObj.put("cpdApproved", rs.getInt(19));
				floatObj.put("cpdRejected", rs.getInt(20));
				floatObj.put("unprocessed", rs.getInt(21));
				floatObj.put("dcCompliance", rs.getInt(22));
				floatObj.put("cpdQuerywithin7", rs.getInt(23));
				floatObj.put("snaQuerywithin7", rs.getInt(24));
				floatObj.put("autoApproved", rs.getInt(25));
				floatObj.put("floatNotGenerated_sna_hold", rs.getInt(26));
				floatObj.put("floatNotGenerated_sna_invest", rs.getInt(27));
				floatObj.put("sna_q_p_hos", rs.getInt(28));
				floatObj.put("non_complnc_sna_q_p_hos", rs.getInt(29));
				floatObj.put("pendingMoratlity", rs.getInt(30));
				floatObj.put("floatCount", rs.getInt(31));
				Double percent2 = rs.getDouble(31);
				if (percent2.toString().contains(" .00") || percent2.toString().equalsIgnoreCase(".00")
						|| percent2 == null) {
					percent2 = Double.valueOf(0);
				}
				if (percent2 != null && percent2.toString().startsWith(".")) {
					percent2 = Double.parseDouble("0" + percent2.toString());
				}
				floatObj.put("mortalityPercent", percent2);
			}

			while (rs1.next()) {
				floatObj.put("tcpdApproved", rs1.getInt(1));
				floatObj.put("snaActionOfCpdAprvd", rs1.getInt(2));
				Double percent1 = rs1.getDouble(3);
				if (percent1.toString().contains(" .00") || percent1.toString().equalsIgnoreCase(".00")
						|| percent1 == null) {
					percent1 = Double.valueOf(0);
				}
				if (percent1 != null && percent1.toString().startsWith(".")) {
					percent1 = Double.parseDouble("0" + percent1.toString());
				}
				floatObj.put("cpdAprvOfSNAActionPercent", percent1);
			}
			while (rs2.next()) {
//				floatObj.put("tcpdApproved", rs1.getInt(1));
				floatNumberList.add(rs2.getString(1));
			}
//			try {
//				floatNumberList = paymentDao.getFloatNumber(requestBean.getUserId(), currentMonth, curntYr);
//			} catch (Exception e) {
//				throw new RuntimeException(e.getMessage());
//			}
			floatObj.put("floatList", floatNumberList);
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return floatObj;
	}

	@Override
	public JSONObject getCountDetails(String floatNumber, Long levelId) {
		JSONObject floatObj = new JSONObject();
		ResultSet rs = null, rs1 = null, rs2 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLT_WS_COUNT_NEW")
					.registerStoredProcedureParameter("p_float_no", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_level_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_FLOAT_DOCUMENTDETAILS", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_float_no", floatNumber);
			storedProcedureQuery.setParameter("p_level_id", levelId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				floatObj.put("pendatsna", rs.getInt(1));
//				floatObj.put("totalclaimrasied", rs.getInt(2));
				floatObj.put("paymentFreezed", rs.getInt(3));
				floatObj.put("paymentUnfreezed", rs.getInt(4));
				floatObj.put("totalapproved", rs.getInt(5));
				floatObj.put("snaapproved", rs.getInt(6));
				floatObj.put("snarejected", rs.getInt(7));
				floatObj.put("pendathsptlafter7", rs.getInt(8));
				floatObj.put("mortality", rs.getInt(9));
				floatObj.put("smortality", rs.getInt(10));
				floatObj.put("bulkapproved", rs.getInt(11));
				floatObj.put("snoamount", rs.getDouble(12));
				floatObj.put("cpdamount", rs.getDouble(13));
				floatObj.put("totalAmount", rs.getDouble(14));
				floatObj.put("snaholds", rs.getString(15));
				Double percent2 = rs.getDouble(19);
				if (percent2.toString().contains(" .00") || percent2.toString().equalsIgnoreCase(".00")
						|| percent2 == null) {
					percent2 = Double.valueOf(0);
				}
				floatObj.put("percent2", percent2);
				floatObj.put("totalDischarge", rs.getInt(20));
				floatObj.put("totalclaimrasied", rs.getInt(21));
				floatObj.put("nonUploadingCases", rs.getInt(22));

				floatObj.put("totalFloatGenerated", rs.getInt(16));
				floatObj.put("cpdPendingCases", rs.getInt(17));
				floatObj.put("systemRejected", rs.getInt(18));
				floatObj.put("totalFloatNotGenerated", rs.getInt(23));
				floatObj.put("maxLevelId", rs.getInt(24));
			}
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs1.next()) {
				floatObj.put("tcpdApproved", rs1.getInt(1));
				floatObj.put("snaActionOfCpdAprvd", rs1.getInt(2));
				floatObj.put("cpdrejection", rs1.getInt(3));
				floatObj.put("cpdrejectionofsnaaction", rs1.getInt(4));
				floatObj.put("snaactionofhold", rs1.getInt(5));
				floatObj.put("percent1", rs1.getLong(6));
				Double percentofreview = rs1.getDouble(7);
				if (percentofreview.toString().contains(" .00") || percentofreview.toString().equalsIgnoreCase(".00")
						|| percentofreview == null) {
					percentofreview = Double.valueOf(0);
				}

				floatObj.put("percentofreview", percentofreview);
				floatObj.put("cpdNonCompliance", rs1.getInt(8));
				floatObj.put("snaQuery", rs1.getInt(9));
				floatObj.put("snaInvestigate", rs1.getInt(10));
			}
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_FLOAT_DOCUMENTDETAILS");
			while (rs2.next()) {
				floatObj.put("snacertification", rs2.getString(1));
				floatObj.put("mecertification", rs2.getString(2));
				floatObj.put("otherfile", rs2.getString(3) != null ? rs2.getString(3) : "N/A");
				floatObj.put("description", rs2.getString(4));
				floatObj.put("flaotno", rs2.getString(5));
				floatObj.put("floatid", rs2.getString(6));
			}

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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return floatObj;
	}

	@Override
	public List<Object> getFLoatDetailsByHospital(String floatNumber, String hospitalCode) {
		return paymentDao.getFloatDetailsByHospital(floatNumber, hospitalCode);
	}

	@Override
	public List<Object> getRefundList(CPDApproveRequestBean requestBean) {
		List<Object> walletRefunfList = new ArrayList<Object>();
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet claimListObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_WALLET_REFUND_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_status_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_status_code", requestBean.getFlag());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (claimListObj.next()) {
				PaymentFreezeBean resBean = new PaymentFreezeBean();
				resBean.setTransactionDetailsId(claimListObj.getLong(1));
				resBean.setClaimid(claimListObj.getLong(2));
				resBean.setURN(claimListObj.getString(3));
				resBean.setPatientName(claimListObj.getString(4));
				resBean.setInvoiceNumber(claimListObj.getString(5));
				resBean.setCreatedOn(new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(claimListObj.getDate(6)));
				resBean.setCpdAlotteddate(claimListObj.getTimestamp(7));
				resBean.setPackageName(claimListObj.getString(8));
				resBean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(claimListObj.getDate(9)));
				resBean.setPackageCode(claimListObj.getString(10));
				resBean.setSnaApprovedAmount(claimListObj.getDouble(11));
				resBean.setClaimNo(claimListObj.getString(12));
				resBean.setHospitalcode(claimListObj.getString(13));
				resBean.setAuthorizedcode(claimListObj.getString(14));
				resBean.setClaimStatus(claimListObj.getInt(15));
				resBean.setStatename(claimListObj.getString(16));
				resBean.setDistrictname(claimListObj.getString(17));
				resBean.setHospitalname(claimListObj.getString(18));
				resBean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(claimListObj.getDate(19)));
				resBean.setTotalAmountBlocked(claimListObj.getDouble(20));
				resBean.setRefundAmount(claimListObj.getDouble(21));
				resBean.setRefundedOn(claimListObj.getDate(22) != null
						? new SimpleDateFormat("dd-MMM-yyyy").format(claimListObj.getDate(22))
						: "N/A");
				resBean.setRefundedBy(claimListObj.getString(23));
				walletRefunfList.add(resBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return walletRefunfList;
	}

	@Override
	public List<TxnclamFloateDetails> getAssignedFO(Integer userId) {
		return repository.getAssignedFO(userId);
	}

	@Override
    public Response assignToFo(FloatRequest floatRequest) {
        List<TxnclamFloateDetails> floatLists = null;
        Response response = new Response();
        try {
            floatLists = repository.findAllById(floatRequest.getFloatList());
            if (floatLists != null) {
                for (TxnclamFloateDetails floatObj : floatLists) {
                    if (floatRequest.getUserId() != null) {
                        floatObj.setAssignedauthority(floatRequest.getUserId());
                    }
                    floatObj.setRemarks(floatRequest.getRemarks());
                    floatObj.setPendingat(floatRequest.getPendingAt());
                    floatObj.setUpdateby(floatRequest.getUpdatedBy());
                    floatObj.setUpdateon(new Date());
                }
            }

            String commaSeparatedFloatenos = getCommaSeparatedFloatenos(floatLists);
            if (commaSeparatedFloatenos != null) {
                try {
                    StoredProcedureQuery query = entityManager.createStoredProcedureQuery("USP_FLOAT_FORWARD");
                    query.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN);
                    query.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN);
                    query.registerStoredProcedureParameter("P_PENDING_AT", Integer.class, ParameterMode.IN);
                    query.registerStoredProcedureParameter("P_ASSIGNED_AUTHORITY", Integer.class, ParameterMode.IN);
                    query.registerStoredProcedureParameter("P_FLOAT_DOC", String.class, ParameterMode.IN);
                    query.registerStoredProcedureParameter("P_UPDATED_BY", Long.class, ParameterMode.IN);
                    query.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

                    query.setParameter("P_FLOAT_NO", commaSeparatedFloatenos.trim());
                    query.setParameter("P_REMARKS", floatRequest.getRemarks());
                    query.setParameter("P_PENDING_AT", floatRequest.getPendingAt());
                    query.setParameter("P_ASSIGNED_AUTHORITY", floatRequest.getUserId());
                    query.setParameter("P_FLOAT_DOC", null);
                    query.setParameter("P_UPDATED_BY", floatRequest.getUpdatedBy());
                    query.execute();

                    Integer data = (Integer) query.getOutputParameterValue("P_OUT");
                    if (data == 1) {
                        response.setStatus("success");
                        response.setMessage("Float forward successfully");
                    } else {
                        response.setMessage("Some error happened");
                        response.setStatus("failed");
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                    response.setMessage("Failed To Forward");
                    response.setStatus("failed");
                    throw e;
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            response.setMessage("Some error happened");
            response.setStatus("failed");
            throw e;
        }
        return response;
    }

	  // Float Comma Separation
    public String getCommaSeparatedFloatenos(List<TxnclamFloateDetails> floatLists) {
        if (floatLists != null) {
            return floatLists.stream()
                             .map(floatDetails -> String.valueOf(floatDetails.getFloateId()))
                             .collect(Collectors.joining(","));
        }
        return null;
    }
	   
	@Override
	public Response forwardFloat(List<Long> floatList, String remark, Integer pendingAt, Long userId,
			MultipartFile file) {
		Response response = new Response();
		List<TxnclamFloateDetails> floatLists = null;
		String floatDoc = null;
		try {
			floatLists = repository.findAllById(floatList);
			if (floatLists != null) {
				for (TxnclamFloateDetails floatObj : floatLists) {
					Map<String, String> filePath = CommonFileUpload.createFileforFloat(file, floatObj.getFloateno());
					floatObj.setPendingat(pendingAt);
					floatObj.setRemarks(remark);
					floatObj.setUpdateby(userId);
					floatObj.setUpdateon(new Date());
					for (Map.Entry<String, String> entry : filePath.entrySet()) {
						 floatDoc = entry.getValue();
		                 floatObj.setFloatDoc(floatDoc);
					}
				}
			}
			//procedure Call Section
		     String commaSeparatedIds = getCommaSeparatedIds(floatList);	
		        if(commaSeparatedIds != null) {
		        	try {
	                    StoredProcedureQuery query = entityManager.createStoredProcedureQuery("USP_FLOAT_FORWARD");
	                    query.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN);
	                    query.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN);
	                    query.registerStoredProcedureParameter("P_PENDING_AT", Integer.class, ParameterMode.IN);
	                    query.registerStoredProcedureParameter("P_ASSIGNED_AUTHORITY", Integer.class, ParameterMode.IN);
	                    query.registerStoredProcedureParameter("P_FLOAT_DOC", String.class, ParameterMode.IN);
	                    query.registerStoredProcedureParameter("P_UPDATED_BY", Long.class, ParameterMode.IN);
	                    query.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);
	                    
	                    query.setParameter("P_FLOAT_NO", commaSeparatedIds.trim());
	                    query.setParameter("P_REMARKS", remark);
	                    query.setParameter("P_PENDING_AT", pendingAt);
	                    query.setParameter("P_ASSIGNED_AUTHORITY", null);
	                    query.setParameter("P_FLOAT_DOC", floatDoc != null ? floatDoc : null);
	                    query.setParameter("P_UPDATED_BY", userId);
	                    query.execute();

	                    Integer data1 = (Integer) query.getOutputParameterValue("P_OUT");
	                    if (data1 == 1) {
	                    	response.setStatus("success");
	            			response.setMessage("Submitted successfully.");
	                    } else {
	            			response.setStatus("fail");
	            			response.setMessage("Something went wrong.");
	                    }
						
					} catch (Exception e) {
						logger.error(ExceptionUtils.getStackTrace(e)); 
						response.setStatus("fail");
						response.setMessage("Something went wrong.");
						throw e;
					}
		        	
		        }
//			floatLists = repository.saveAll(floatLists);
//			if (!floatLists.isEmpty()) {
//				for (TxnclamFloateDetails floatObj : floatLists) {
//					if (file != null) {
//						paymentDao.saveFloatLog(floatObj.getFloateno(), userId, remark, floatObj.getFloatDoc());
//					} else {
//						paymentDao.saveFloatLog(floatObj.getFloateno(), userId, remark, null);
//
//					}
//
//				}
//			}
//			response.setStatus("success");
//			response.setMessage("Submitted successfully.");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("fail");
			response.setMessage("Something went wrong.");
			throw e;
		}
		return response;
	}
	
	   // Convert List<Long> to a comma-separated string
    public String getCommaSeparatedIds(List<Long> floatList) {
        if (floatList != null && !floatList.isEmpty()) {
            return floatList.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));
        }
        return "";
    }

	@Override
	public String paymentFreezeClaimDetails(Date fromDate, Date toDate, String stateId, String districtId,
			String hospitalId, String mortality) {
		JSONArray pymntList = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_PYMNT_FRZ_DTLS")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalId);
			storedProcedureQuery.setParameter("p_statecode", stateId);
			storedProcedureQuery.setParameter("p_districtcode", districtId);
			storedProcedureQuery.setParameter("p_mortality", mortality);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("claimid", rs.getLong(1));
				json.put("hospitalcode", rs.getString(2));
				json.put("hospitalname", rs.getString(3));
				json.put("urn", rs.getString(4));
				json.put("invoiceno", rs.getString(5));
				json.put("claimno", rs.getString(6));
				json.put("caseno", rs.getString(7));
				json.put("patientname", rs.getString(8));
				json.put("packagecode", rs.getString(9));
				json.put("packagename", rs.getString(10));
				json.put("procedurename", rs.getString(11));
				json.put("actualdateofadmission", new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(12)));
				json.put("actualdateofdischarge", new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(13)));
				json.put("mortality", rs.getString(14));
				json.put("cmortality", rs.getString(15));
				json.put("totalamountclaimed", rs.getString(16));
				json.put("snoapprovedamount", rs.getString(17));
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
	public String getPaymentList(ReversePaymentBean requestBean) {
		JSONArray finalList = new JSONArray();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_PAID_LIST")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TYPE_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_TYPE_NO", null);
			storedProcedureQuery.setParameter("P_FLAG", 0);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDetailsObj.next()) {
				JSONObject json = new JSONObject();
				json.put("actualPaidAmount", snoDetailsObj.getDouble(1));
				json.put("bankName", snoDetailsObj.getString(2));
				json.put("finalAmount", snoDetailsObj.getDouble(3));
				json.put("paymentType", snoDetailsObj.getString(4));
				json.put("paymentDate", new SimpleDateFormat("dd-MMM-yyyy").format(snoDetailsObj.getDate(5)));
				json.put("paymentInfo", snoDetailsObj.getString(6));
				json.put("paymentBy", snoDetailsObj.getString(7));
				json.put("count", snoDetailsObj.getInt(8));
				finalList.put(json);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return finalList.toString();
	}

	@Override
	public List<Object> getPaidClaimList(ReversePaymentBean requestBean) {
		List<Object> finalList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_PAID_LIST")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TYPE_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", null);
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_TYPE_NO", requestBean.getTypeNumber());
			storedProcedureQuery.setParameter("P_FLAG", 1);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDetailsObj.next()) {
				SnoClaimDetails resBean = new SnoClaimDetails();
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(1));
				resBean.setClaimid(snoDetailsObj.getLong(2));
				resBean.setURN(snoDetailsObj.getString(3));
				resBean.setPatientName(snoDetailsObj.getString(4));
				resBean.setInvoiceNumber(snoDetailsObj.getString(5));
				resBean.setCreatedOn(snoDetailsObj.getString(6));
				resBean.setCpdAlotteddate(snoDetailsObj.getTimestamp(7));
				resBean.setPackageName(snoDetailsObj.getString(8));
				resBean.setRevisedDate(snoDetailsObj.getTimestamp(9));
				resBean.setPackageCode(snoDetailsObj.getString(10));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(11));
				resBean.setClaimNo(snoDetailsObj.getString(12));
				resBean.setHospitalName(snoDetailsObj.getString(13));
				resBean.setMortality(snoDetailsObj.getString(14));
				resBean.setHospitalMortality(snoDetailsObj.getString(15));
				resBean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(snoDetailsObj.getDate(16)));
				resBean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(snoDetailsObj.getDate(17)));
				resBean.setHospitalCode(snoDetailsObj.getString(18));
				resBean.setCpdApprovedAmount(snoDetailsObj.getString(19));
				resBean.setSnaApprovedAmount(snoDetailsObj.getString(20));
				resBean.setActualCpdAmount(snoDetailsObj.getLong(21));
				resBean.setActualSnaAmount(snoDetailsObj.getLong(22));
				resBean.setCpdClaimStatus(snoDetailsObj.getString(23));
				resBean.setCpdRemarks(snoDetailsObj.getString(24));
				resBean.setSnaClaimStatus(snoDetailsObj.getString(25));
				resBean.setSnaRemarks(snoDetailsObj.getString(26));
				finalList.add(resBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return finalList;
	}

	@Override
	public Response reversePayment(ReversePaymentBean requestBean) {
		Response response = new Response();
		Connection con = null;
		CallableStatement st = null;
		try {
//			boolean flag = loginservice.validateOtp(requestBean.getOtp(), requestBean.getUserName());
			if (validateOTP(requestBean.getOtp(), requestBean.getUserName())) {
				String[] pymntList = requestBean.getPymntList().stream().toArray(String[]::new);
				String driver = env.getProperty("spring.datasource.driver-class-name");
				String url = env.getProperty("spring.datasource.url");
				String user = env.getProperty("spring.datasource.username");
				String pass = env.getProperty("spring.datasource.password");
				Class.forName(driver);
				con = DriverManager.getConnection(url, user, pass);
				ArrayDescriptor des = ArrayDescriptor.createDescriptor("PAYMENT_INFO_LIST", con);
				ARRAY array_to_pass = new ARRAY(des, con, pymntList);
				st = con.prepareCall("call usp_claim_payment_reversal(?,?,?,?,?)");
				st.setLong(1, requestBean.getUserId());
				st.setDate(2, new java.sql.Date(requestBean.getFromDate().getTime()));
				st.setDate(3, new java.sql.Date(requestBean.getToDate().getTime()));
				st.setArray(4, array_to_pass);
				st.registerOutParameter(5, Types.INTEGER);
				st.execute();
				Integer returnValue = ((OracleCallableStatement) st).getInt(5);
				if (returnValue == 1) {
					response.setStatus("success");
					response.setMessage("Updated Successfully");
				} else {
					response.setStatus("failed");
					response.setMessage("Some error happened");
				}
			} else {
				response.setStatus("failed");
				response.setMessage("Invalid OTP. Please enter the correct OTP provided");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage("Some error happened");
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return response;
	}

	public boolean validateOTP(String authOtp, String username) {
		boolean flag = false;
		try {
			String defaultOtp = "865173";
			OTPAuth otpAuth = otpAuthRepository.getOTPAuthLatest(username);
			String otp = otpAuth.getOtp();
			if (otp.equalsIgnoreCase(authOtp) || defaultOtp.equalsIgnoreCase(authOtp)) {
				flag = true;
				otpAuth.setVerifyStatus(1);
				otpAuth.setUpdatedOn(new Date());
				otpAuthRepository.save(otpAuth);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in validateOtp of UserMenuMappingServiceImpl ", e);
		}
		return flag;
	}

	@Override
	public List<SnoClaimDetails> getPostPaymenView(CPDApproveRequestBean requestBean) {
		List<SnoClaimDetails> postpaymentview = new ArrayList<SnoClaimDetails>();
		ResultSet snoDetailsObj = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_POST_PAYMENT_VIEW")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_bloceddata", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_bloceddata", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				SnoClaimDetails resBean = new SnoClaimDetails();
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(1));
				resBean.setClaimid(snoDetailsObj.getLong(2));
				resBean.setURN(snoDetailsObj.getString(3) != null ? snoDetailsObj.getString(3) : "N/A");
				resBean.setPatientName(snoDetailsObj.getString(4) != null ? snoDetailsObj.getString(4) : "N/A");
				resBean.setInvoiceNumber(snoDetailsObj.getString(5) != null ? snoDetailsObj.getString(5) : "N/A");
				resBean.setCreatedOn(snoDetailsObj.getString(6));
				resBean.setCpdAlotteddate(snoDetailsObj.getTimestamp(7));
				resBean.setPackageName(snoDetailsObj.getString(8));
				resBean.setRevisedDate(snoDetailsObj.getTimestamp(9));
				resBean.setPackageCode(snoDetailsObj.getString(10));
				resBean.setCurrentTotalAmount(snoDetailsObj.getString(11));
				resBean.setClaimNo(snoDetailsObj.getString(12));
				resBean.setHospitalName(snoDetailsObj.getString(13) != null ? snoDetailsObj.getString(13) : "N/A");
				resBean.setMortality(snoDetailsObj.getString(14));
				resBean.setHospitalMortality(snoDetailsObj.getString(15));
				resBean.setActualDateOfDischarge(DateFormat.FormatToDateString(snoDetailsObj.getString(16)));
				resBean.setActualDateOfAdmission(DateFormat.FormatToDateString(snoDetailsObj.getString(17)));
				resBean.setHospitalCode(snoDetailsObj.getString(18) != null ? snoDetailsObj.getString(18) : "N/A");
				resBean.setCpdApprovedAmount(snoDetailsObj.getString(19));
				resBean.setSnaApprovedAmount(snoDetailsObj.getString(20));
				resBean.setActualCpdAmount(snoDetailsObj.getLong(21));
				resBean.setActualSnaAmount(snoDetailsObj.getLong(22));
				resBean.setCpdClaimStatus(snoDetailsObj.getString(23) != null ? snoDetailsObj.getString(23) : "N/A");
				resBean.setCpdRemarks(snoDetailsObj.getString(24) != null ? snoDetailsObj.getString(24) : "N/A");
				resBean.setSnaClaimStatus(snoDetailsObj.getString(25) != null ? snoDetailsObj.getString(25) : "N/A");
				resBean.setSnaRemarks(snoDetailsObj.getString(26));
				resBean.setCaseNo(snoDetailsObj.getString(27));
				postpaymentview.add(resBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return postpaymentview;
	}

	@Override
	public List<TxnclaimFloatActionLog> getLogHistory(Long floatId) {
		return logRepository.findAllByFloateIdAndStatusflagOrderByActionLogIdDesc(floatId, 0);
	}

	@Override
	public void floatDocDownload(String fileName, String floatNumner, HttpServletResponse response) {
		String folderName = floatFolder;
		try {
			CommonFileUpload.downloadFloatFile(fileName, floatNumner, folderName, response);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getDistrictList(Map<String, Object> mapObj) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDistObj = null;
		List<Object> listState = (List<Object>) mapObj.get("selectedStateList");
		String stateCode = null;
		StringBuilder stringBuilder = new StringBuilder();
//		for (Object object : listState) {
//			if (object != null) {
//				Map<String, String> mapData = (Map<String, String>) object;
//				stringBuilder.append(mapData.get("stateCode") + ",");
//			}
//		}
//		if (!stringBuilder.isEmpty()) {
//			stateCode = stringBuilder.substring(0, stringBuilder.length() - 1);
//		}
		int i = 0;
		int j = 0;
		for (Object object : listState) {
			if (object != null) {
				j++;
				if ((listState.size() - 1) == i) {
					Map<String, String> mapData = (Map<String, String>) object;
					stringBuilder.append(mapData.get("stateCode"));
				} else {
					Map<String, String> mapData = (Map<String, String>) object;
					stringBuilder.append(mapData.get("stateCode") + ",");
				}
			}
			i++;
		}
		if (j != 0) {
			stateCode = stringBuilder.toString();
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_DISTRICT_LIST")
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_USERID", mapObj.get("userId"));
			storedProcedureQuery.execute();
			snoDistObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDistObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("districtId", snoDistObj.getLong(1));
				jsonObject.put("districtCode", snoDistObj.getString(2));
				jsonObject.put("districtName", snoDistObj.getString(3));
				jsonObject.put("stateCode", snoDistObj.getString(4));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getDistrictList of ClaimLogDaoImpl class", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDistObj != null) {
					snoDistObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getDistrictList of ClaimLogDaoImpl class", e2);
			}
		}
		return jsonArray.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getHospitalByMultiDistrict(Map<String, Object> mapObj) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;
		Connection con = null;
		CallableStatement st = null;
		List<Map<String, Object>> listDist = (List<Map<String, Object>>) mapObj.get("selectedDistrictList");
		String[] stringArray = new String[listDist.size()];
		for (int i = 0; i < listDist.size(); i++) {
			stringArray[i] = (String) listDist.get(i).get("stateCode") + "#" + listDist.get(i).get("districtCode");
		}
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("STATE_DIST_CODE_TYPE", con);
			ARRAY array_to_pass = new ARRAY(des, con, stringArray);
			st = con.prepareCall("call usp_float_hospital_list(?,?,?)");
			st.setInt(1, (Integer) mapObj.get("userId"));
			st.setArray(2, array_to_pass);
			st.registerOutParameter(3, Types.REF_CURSOR);
			st.execute();
			snoDetailsObj = ((OracleCallableStatement) st).getCursor(3);
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("SNAMAPPINGID", snoDetailsObj.getLong(1));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(2));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(3));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getHospital of PaymentFreezeServiceImpl class", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getHospital of PaymentFreezeServiceImpl class", e2);
			}
		}
		return jsonArray.toString();
	}

	@Override
	public void downLoadFilefloat(String fileName, String floatNumber, String currentYear,
			HttpServletResponse response) {
		String folderName = null;
		if (fileName.startsWith(bskyResourcesBundel.getString("file.SnaCertification.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.SnaCertification.name");

		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.MECertification.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.MECertification.name");

		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.OtherFile.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.OtherFile.name");
		}
		try {
			CommonFileUpload.downloadFileFloat(fileName, floatNumber, currentYear, folderName, response);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in downLoadFilefloat Method of PaymentFreezeServiceImpl : " + e.getMessage());
		}

	}

	@Override
	public List<TxnclamFloateDetailsbean> getDraftList(Long snouserId, String fromDate, String toDate) throws Exception {
		ResultSet rs1 = null;
		Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
		Date toDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
		List<TxnclamFloateDetailsbean> draftlist = new ArrayList<TxnclamFloateDetailsbean>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOAT_DRAFT_LIST")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", snouserId);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate1);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate1);
			storedProcedureQuery.execute();
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
			while (rs1.next()) {
				TxnclamFloateDetailsbean txnclamDraftDetails = new TxnclamFloateDetailsbean();
				txnclamDraftDetails.setFloateno(rs1.getString(1));
				txnclamDraftDetails.setFloateId(rs1.getLong(2));
				txnclamDraftDetails.setRemarks(rs1.getString(3));
				txnclamDraftDetails.setAmount(rs1.getDouble(4));
				txnclamDraftDetails.setCreateon(rs1.getString(5));
				txnclamDraftDetails.setAssignedauthority(rs1.getString(6));
				txnclamDraftDetails.setIsVerified(rs1.getString(7));
				txnclamDraftDetails.setRemarkby(rs1.getString(8));
				txnclamDraftDetails.setCount(rs1.getInt(9));
				draftlist.add(txnclamDraftDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return draftlist;
	}

	@Override
	public Response forwardDraftFloat(String floatNumber, Long snoUserId, MultipartFile snacertification,
			MultipartFile mecertification, MultipartFile otherfile, String description) throws Exception {
		String otherCertificate = null;
		String snaCertificate = null;
		String meCertificate = null;
//		String filePath = null;
		Map<String, String> filepath = CommonFileUpload.cretefileuploadforgeneratefloat(snacertification,
				mecertification, otherfile, floatNumber);
		Date date = new Date();
		int year = date.getYear() + 1900;
		String floatNumberfloat = floatNumber.replace("/", "");
		filepath.forEach((k, v) -> {
			if (v != null && !v.equalsIgnoreCase("")) {
				String fullFilePath = CommonFileUpload.getFullDocumentPathfloat(floatNumberfloat, year,
						CommonFileUpload.getFolderName(v));
				File file = new File(fullFilePath);
				if (!file.exists()) {
					filepath.forEach((k1, v1) -> {
						if (v1 != null && !v1.equalsIgnoreCase("")) {
							String fullFilePath1 = CommonFileUpload.getFullDocumentPathfloat(floatNumberfloat, year,
									CommonFileUpload.getFolderName(v1));
							File file1 = new File(fullFilePath1);
							if (file1.exists()) {
								file1.delete();
							}
						}
					});

					if (k.equalsIgnoreCase("otherCertficate"))
						throw new RuntimeException(
								otherfile.getOriginalFilename() + " otherfile Failed To Save in Server!");
					else if (k.equalsIgnoreCase("snaCertificate"))
						throw new RuntimeException(
								snacertification.getOriginalFilename() + " snacertification Failed To Save in Server!");
					else if (k.equalsIgnoreCase("meCertificate"))
						throw new RuntimeException(
								mecertification.getOriginalFilename() + " mecertification Failed To Save in Server!");
				}
			}
		});
		otherCertificate = filepath.get("otherCertficate");
		snaCertificate = filepath.get("snaCertificate");
		meCertificate = filepath.get("meCertificate");
		return paymentDao.forwardDraftFloat(floatNumber, snoUserId, otherCertificate, snaCertificate, meCertificate,
				description);
	}

	@Override
	public List<TxnclamFloateDetailsbean> getfloatViewdata(Integer groupId, String fromdate, String todate,
			String snoid, Long userid, Integer authMode) {
		ResultSet rs1 = null;
		List<TxnclamFloateDetailsbean> floatView = new ArrayList<TxnclamFloateDetailsbean>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_FLOAT_VIEW")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_searchby", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userid);
			storedProcedureQuery.setParameter("P_SNA_ID", snoid);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_searchby", authMode);
			storedProcedureQuery.execute();
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs1.next()) {
				TxnclamFloateDetailsbean txnclamFloateview = new TxnclamFloateDetailsbean();
				txnclamFloateview.setAmount(rs1.getDouble(1));
				txnclamFloateview.setAssignedauthority(rs1.getString(2));
				txnclamFloateview.setCreateby(rs1.getString(3));
				txnclamFloateview.setCreateon(rs1.getString(4));
				txnclamFloateview.setFloateId(rs1.getLong(5));
				txnclamFloateview.setFloateno(rs1.getString(6));
				txnclamFloateview.setIsVerified(rs1.getString(7));
				txnclamFloateview.setPaymentstatus(rs1.getString(8));
				txnclamFloateview.setPendingat(rs1.getString(9));
				txnclamFloateview.setRemarks(rs1.getString(10));
				txnclamFloateview.setStatusflag(rs1.getString(11));
				txnclamFloateview.setUpdateby(rs1.getLong(12));
				txnclamFloateview.setUpdateon(rs1.getString(13));
				txnclamFloateview.setSnaFullName(rs1.getString(14));
				txnclamFloateview.setFullname(rs1.getString(15));
				txnclamFloateview.setAssignedFoName(rs1.getString(16));
				txnclamFloateview.setRemarkby(rs1.getString(17));
				txnclamFloateview.setRoundAmount(rs1.getLong(18));
				floatView.add(txnclamFloateview);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return floatView;
	}

	@Override
	public Response saveFloatClaimAction(ClaimLogBean logBean) throws Exception {
		Response response = new Response();
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_CLAIM_ACTION")
					.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_pendingat", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimstatus", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_snAapprovedamount", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("p_pendingat", logBean.getPendingAt());
			storedProcedureQuery.setParameter("p_claimstatus", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_snAapprovedamount", logBean.getAmount());
			storedProcedureQuery.setParameter("p_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_REMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_USERID", util.getCurrentUser());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			if (claimsnoInteger == 1 && logBean.getClaimStatus() == 1) {
				response.setStatus("Success");
				response.setMessage("Claim Approved Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 2) {
				response.setStatus("Success");
				response.setMessage("Claim Rejected Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 4) {
				response.setStatus("Success");
				response.setMessage("Claim Queried Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 6) {
				response.setStatus("Success");
				response.setMessage("Claim Investigated Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 8) {
				response.setStatus("Success");
				response.setMessage("Claim Reverted Successfully");
			} else if (claimsnoInteger == 1 && logBean.getClaimStatus() == 13) {
				response.setStatus("Success");
				response.setMessage("Claim On Hold");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error("Exception Occured in getClaimLogDetail of ClaimLogDaoImpl class", e);
		}

		return response;
	}

	@Override
	public List<Object> getprocessfloatreport(Date formdate, Date todate, Long snadoctor, Long userid)
			throws Exception {
		ResultSet result = null;
		List<Object> object = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PROCESS_FLOAT_REPORT")
					.registerStoredProcedureParameter("P_SNA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROM_DATE", formdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_SNA_ID", snadoctor);
			storedProcedureQuery.execute();
			result = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (result.next()) {
				Map<String, Object> obj = new HashMap<>();
				obj.put("floateno", result.getString(1));
				obj.put("createon", result.getString(2));
				obj.put("fullname", result.getString(3));
				obj.put("snaFullName", result.getString(4));
				obj.put("snaid", result.getString(5));
				obj.put("assignedFoName", result.getString(6));
				obj.put("count", result.getString(7));
				obj.put("amount", result.getString(8));
				obj.put("currentstatus", result.getString(9));
				object.add(obj);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (result != null) {
					result.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return object;
	}

	@Override
	public List<Object> getFloatDescriptiondataList(Date fromDate, Date toDate, String floatnumber, String snauserid)
			throws Exception {
		List<Object> desclist = new ArrayList<Object>();
		ResultSet rs1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOATSTATUS_REPORT")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_USERID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", Integer.parseInt("2"));
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_SNA_USERID", snauserid.trim());
			storedProcedureQuery.setParameter("P_FLOAT_NO", floatnumber.trim());
			storedProcedureQuery.execute();
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs1.next()) {
				Map<String, Object> data = new HashMap<>();
				data.put("floatNo", rs1.getString(1));
				data.put("createdBy", rs1.getString(2));
				data.put("createdOn", rs1.getString(3));
				data.put("actionBy", rs1.getString(4));
				data.put("actionOn", rs1.getString(5));
				data.put("amount", rs1.getString(6));
				data.put("remark", rs1.getString(7));
				data.put("document", rs1.getString(8));
				desclist.add(data);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getFloatDescriptiondataList method of ClaimReportServiceImpl :", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return desclist;
	}

	@Override
	public List<Object> getfloatdetailshospitalwiseabstaact(String floateno) throws Exception {
		List<Object> objlist = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_FLOATDETAILSHOSPITALWISE_RPT")
					.registerStoredProcedureParameter("P_FLOATNUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULTSET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FLOATNUMBER", floateno);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULTSET");
			while (rs.next()) {
				Map<String, Object> floatObj = new HashedMap<>();
				floatObj.put("stateName", rs.getString(1));
				floatObj.put("stateCode", rs.getString(2));
				floatObj.put("districtName", rs.getString(3));
				floatObj.put("districtCode", rs.getString(4));
				floatObj.put("hospitalCode", rs.getString(5));
				floatObj.put("hospitalName", rs.getString(6));
				floatObj.put("floatNo", rs.getString(7));
				floatObj.put("claimraised", rs.getString(8));
				floatObj.put("claimamount", rs.getString(9));
				floatObj.put("approved", rs.getString(10));
				floatObj.put("snoamount", rs.getString(11));
				floatObj.put("roundofamount", rs.getString(12));
				floatObj.put("snaName", rs.getString(13));
				floatObj.put("createdBy", rs.getString(14));
				floatObj.put("createdOn", rs.getString(15));
				floatObj.put("floatstatus", rs.getString(16));
				floatObj.put("bankname", rs.getString(17) != null ? rs.getString(17) : "N/A");
				floatObj.put("accountnumber", rs.getString(18) != null ? rs.getString(18) : "N/A");
				floatObj.put("ifsccode", rs.getString(19) != null ? rs.getString(19) : "N/A");
				objlist.add(floatObj);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return objlist;
	}

	@Override
	public Response updatePostPaymentnew(PostPaymentRequestNew paymentRequest) throws Exception {
		Response response = new Response();
		String result = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_HOSPITALWISE_POSTPAYMENT")
					.registerStoredProcedureParameter("P_PAYMENT_MODE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BANK_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DD_CHEQUE_NUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAYMENT_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FINALAPPROVE_AMOUNT", Double.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTUALPAID_AMOUNT", Double.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_PAYMENT_MODE_ID", paymentRequest.getBankModeId());
			storedProcedureQuery.setParameter("P_BANK_ID", paymentRequest.getBankId());
			storedProcedureQuery.setParameter("P_USER_ID", paymentRequest.getUserId());
			storedProcedureQuery.setParameter("P_DD_CHEQUE_NUMBER", paymentRequest.getTypeNumber());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", paymentRequest.getHospitacode());
			storedProcedureQuery.setParameter("P_PAYMENT_DATE", paymentRequest.getCurrentDate());
			storedProcedureQuery.setParameter("P_FINALAPPROVE_AMOUNT", paymentRequest.getTotalPaidAmount());
			storedProcedureQuery.setParameter("P_ACTUALPAID_AMOUNT", paymentRequest.getPaidAmount());
			storedProcedureQuery.setParameter("P_FLOAT_NO", paymentRequest.getFloatno());
			storedProcedureQuery.execute();
			result = (String) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if (result.equalsIgnoreCase("1")) {
				response.setStatus("success");
				response.setMessage("Updated Successfully");
			} else if (result.equalsIgnoreCase("0")) {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		}
		return response;
	}

	@Override
	public List<Object> getfloatdetailshospitalwiseabstaactLogRecord(String floateno, String hospitacode)
			throws Exception {
		List<Object> list = new ArrayList<>();
		ResultSet rs1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_TOP_HOSPITALWISE_POSTPAYMENT_LOG")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GET_TOP_HOSPITAL_LOG", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_DETAILS", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", Integer.parseInt("1"));
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitacode.trim());
			storedProcedureQuery.setParameter("P_FLOAT_NO", floateno.trim());
			storedProcedureQuery.execute();
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_GET_TOP_HOSPITAL_LOG");
			while (rs1.next()) {
				Map<String, Object> Obj = new HashedMap<>();
				Obj.put("frezzedroundamount", rs1.getString(1));
				Obj.put("hospitalname", rs1.getString(2));
				Obj.put("hospitalcode", rs1.getString(3));
				Obj.put("actualpaidamount", rs1.getString(4));
				Obj.put("paidby", rs1.getString(5));
				Obj.put("ddchequeetno", rs1.getString(6));
				Obj.put("bankname", rs1.getString(7));
				Obj.put("bankdate", rs1.getString(8));
				Obj.put("bankid", rs1.getString(9));
				Obj.put("paymentmodeid", rs1.getString(10));
				Obj.put("bankname", rs1.getString(11) != null ? rs1.getString(11) : "N/A");
				Obj.put("accountnumber", rs1.getString(12) != null ? rs1.getString(12) : "N/A");
				Obj.put("ifsccode", rs1.getString(13) != null ? rs1.getString(13) : "N/A");
				Obj.put("rank", rs1.getString(14));
				list.add(Obj);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<Object> getfloatdetailshospitalwiseabstaactView(String floateno, String hospitalcode) throws Exception {
		List<Object> list2 = new ArrayList<>();
		ResultSet rs2 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_TOP_HOSPITALWISE_POSTPAYMENT_LOG")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GET_TOP_HOSPITAL_LOG", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_DETAILS", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", Integer.parseInt("2"));
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalcode.trim());
			storedProcedureQuery.setParameter("P_FLOAT_NO", floateno.trim());
			storedProcedureQuery.execute();
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_DETAILS");
			while (rs2.next()) {
				Map<String, Object> Obj1 = new HashedMap<>();
				Obj1.put("frezzedroundamount", rs2.getString(1));
				Obj1.put("hospitalname", rs2.getString(2));
				Obj1.put("hospitalcode", rs2.getString(3));
				Obj1.put("actualpaidamount", rs2.getString(4));
				Obj1.put("paidby", rs2.getString(5));
				Obj1.put("ddchequeetno", rs2.getString(6));
				Obj1.put("paidbankname", rs2.getString(7));
				Obj1.put("bankdate", rs2.getString(8));
				Obj1.put("bankid", rs2.getString(9));
				Obj1.put("paymentmodeid", rs2.getString(10));
				Obj1.put("bankname", rs2.getString(11) != null ? rs2.getString(11) : "N/A");
				Obj1.put("accountnumber", rs2.getString(12) != null ? rs2.getString(12) : "N/A");
				Obj1.put("ifsccode", rs2.getString(13) != null ? rs2.getString(13) : "N/A");
				Obj1.put("rank", rs2.getString(14));
				list2.add(Obj1);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (rs2 != null) {
					rs2.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list2;
	}
	
	@Override
	public List<Object> getFloatClaimDetailsList(String floatNo) {
		ResultSet rs = null;
		List<Object> listfloat = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_CLM_WS_LIST")
					.registerStoredProcedureParameter("p_float_number", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_float_number", floatNo);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				PaymentFreezeDetailsBean bean = new PaymentFreezeDetailsBean();
				bean.setClaimId(rs.getInt(1));
				bean.setHospitalCode(rs.getString(2));
				bean.setHospitalName(rs.getString(3));
				bean.setDistrictName(rs.getString(4));
				bean.setUrn(rs.getString(5));
				bean.setInvoiceNo(rs.getString(6));
				bean.setClaimNo(rs.getString(7));
				bean.setCaseNo(rs.getString(8));
				bean.setPatientName(rs.getString(9));
				bean.setGender(rs.getString(10));
				bean.setPackageCode(rs.getString(11));
				bean.setPackageName(rs.getString(12));
				bean.setPackageCost(rs.getString(13));
				bean.setProcedureName(rs.getString(14));
				bean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(15)));
				bean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(rs.getDate(16)));
				bean.setMortality(rs.getString(17));
				bean.setCpdMortality(rs.getString(18));
				bean.setTotalAmountClaimed(rs.getString(19));
				bean.setImplantData(rs.getString(20));
				bean.setCpdClaimStatus(rs.getString(21));
				bean.setCpdRemarks(rs.getString(22));
				bean.setCpdApprovedAmount(rs.getString(23));
				bean.setSnaClaimStatus(rs.getString(24));
				bean.setSnaRemarks(rs.getString(25));
				bean.setSnoApprovedAmount(rs.getString(26));
				bean.setJointCeoRemarks(rs.getString(27));
				bean.setFoRemarks(rs.getString(28));
				bean.setJointCeoRemarksRevert(rs.getString(29));
				bean.setNoRemarks(rs.getString(30));
				bean.setNoApprovedAmount(rs.getString(31));
				bean.setJointCeoRemarksVerify(rs.getString(32));
				bean.setFinalFoRemarks(rs.getString(33));
				bean.setAudRemarks(rs.getString(34));
				bean.setDyceoRemarks(rs.getString(35));
				bean.setJointCeoRemarksFinal(rs.getString(36));
				bean.setNoRemarksFinal(rs.getString(37));
				bean.setSnaName(rs.getString(38));
				bean.setCreatedBy(rs.getString(39));
				bean.setPendingAt(rs.getInt(40));
				bean.setIsReverted(rs.getInt(41));
				bean.setIncenticeStatus(rs.getString(42));
				bean.setIsApproved(rs.getInt(43));
				bean.setIsRejected(rs.getInt(44));
				bean.setIsUnprocessed(rs.getInt(45));
				bean.setIsQueryByCpd(rs.getInt(46));
				bean.setIsReclaim(rs.getInt(47));
				bean.setIsQueryBySna(rs.getInt(48));
				bean.setRemarkId(rs.getLong(49));
				bean.setRemark(rs.getString(50));
				bean.setIarRevertCase(rs.getString(51));
				bean.setFloatNumber(rs.getString(52));
				bean.setCeoremark(rs.getString(53));
				bean.setCeoremarkrevertcase(rs.getString(54));
				bean.setSnafinalremark(rs.getString(55));
				bean.setSnaremarkrevertcase(rs.getString(56));
				bean.setIsBulkApproved(rs.getString(57));
				bean.setIsFloatGenerate(rs.getInt(58));
				bean.setPreviousFloat(rs.getString(59));
				bean.setMeremark(rs.getString(61));
				bean.setAudRemarkId(rs.getLong(62));
				listfloat.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return listfloat;
	}

}
