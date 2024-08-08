package com.project.bsky.serviceImpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.project.bsky.bean.Bulkapprovalbean;
import com.project.bsky.bean.Bulkrevertbean;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.ICDDetailsBean;
import com.project.bsky.bean.MeTriggerDetailsBean;
import com.project.bsky.bean.OldClaimListBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnoClaimDetails;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.model.ActionType;
import com.project.bsky.model.EnrollmentRemarks;
import com.project.bsky.model.Mstschemesubcategory;
import com.project.bsky.model.State;
import com.project.bsky.repository.ActionRemarkRepository;
import com.project.bsky.repository.ActionTypeRepository;
import com.project.bsky.repository.EnrollmentActionRemarkRepository;
import com.project.bsky.repository.Mstschemesubcategoryrepository;
import com.project.bsky.repository.StateRepository;
import com.project.bsky.repository.TxnclaimapplicationRepository;
import com.project.bsky.service.SnoClaimProcessingDetails;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.JwtUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Service
public class SnoClaimProcessingDetailsImpl implements SnoClaimProcessingDetails {

	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ActionRemarkRepository actionrepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private Mstschemesubcategoryrepository mstschemesubcategoryrepository;

	@Autowired
	private ActionTypeRepository actionTypeRepo;

	@Autowired
	private CPDClaimProcessingServiceImpl packageBlocking;

	@Autowired
	private EnrollmentActionRemarkRepository enrollmentremarkrepository;

	@Autowired
	private TxnclaimapplicationRepository txnclaimapplicationrepo;

//	@Autowired
//	private MeTriggerDetailsRepository triggerRepo;

	@Value("${adhaartoref.url}")
	private String baseurl;

	@Autowired
	private JwtUtil util;

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<Object> getsnoclaimrasiedata(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		System.out.println(requestBean);
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_RE_APRV_LST_TEST")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
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

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_remarks", requestBean.getDescription());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_PROCEDURE_CODE", requestBean.getProcedure());
			storedProcedureQuery.setParameter("P_PACKAGE_CODE", requestBean.getPackages());
			storedProcedureQuery.setParameter("P_IMPLANT_CODE", requestBean.getImplant());
			storedProcedureQuery.setParameter("P_HIGH_END_DRUGS_CODE", requestBean.getHighend());
			storedProcedureQuery.setParameter("P_WARDNAME", requestBean.getWard());
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", requestBean.getTrigger());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
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
				resBean.setActualDateOfDischarge1(DateFormat.dateConvertor(snoDetailsObj.getString(16), ""));
				resBean.setActualDateOfAdmission1(DateFormat.dateConvertor(snoDetailsObj.getString(17), ""));
				resBean.setHospitalCode(snoDetailsObj.getString(18));
				resBean.setPhone(snoDetailsObj.getString(19) == null ? "N/A" : snoDetailsObj.getString(19));
				resBean.setVerificationMode(snoDetailsObj.getLong(20));
				resBean.setTxnpackagedetailid(snoDetailsObj.getLong(21));
				resBean.setTriggerValue(snoDetailsObj.getLong(22));
				resBean.setTriggerMsg(snoDetailsObj.getString(23));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception in getsnoclaimrasiedata method of SnoClaimProcessingDetailsImpl", e);
			throw new RuntimeException(e);
		} finally {

			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception in getsnoclaimrasiedata method of SnoClaimProcessingDetailsImpl", e2);
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override

	public Map<String, Object> getSNAClaimApprove(CPDApproveRequestBean requestBean) {
		Map<String, Object> snoclaimRaiseDetailsMap = new HashMap<String, Object>();
		List<Object> snoclaimRaiseDetailsList = new ArrayList<Object>();
		Long total = null;
		ResultSet snoDetailsObj = null;
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_APRV_LIST_DETAILS")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_FLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_IN", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PAGE_END", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IMPLANT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HIGH_END_DRUGS_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_WARDNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FILTER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SEARCHTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_schemesubcategoryid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TOTAL", Long.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_CPD_FLAG", requestBean.getCpdFlag());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_remarks", requestBean.getDescription());
			storedProcedureQuery.setParameter("p_amount", requestBean.getAmountFlag());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_PAGE_IN", requestBean.getPageIn());
			storedProcedureQuery.setParameter("P_PAGE_END", requestBean.getPageEnd());
			storedProcedureQuery.setParameter("P_PROCEDURE_CODE", requestBean.getProcedure());
			storedProcedureQuery.setParameter("P_PACKAGE_CODE", requestBean.getPackages());
			storedProcedureQuery.setParameter("P_IMPLANT_CODE", requestBean.getImplant());
			storedProcedureQuery.setParameter("P_HIGH_END_DRUGS_CODE", requestBean.getHighend());
			storedProcedureQuery.setParameter("P_WARDNAME", requestBean.getWard());
			storedProcedureQuery.setParameter("P_FILTER", requestBean.getFilter());
			storedProcedureQuery.setParameter("P_SEARCHTYPE", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", requestBean.getTrigger());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.setParameter("P_CPD_USERID", requestBean.getCpdUserId());
			storedProcedureQuery.setParameter("P_schemesubcategoryid", requestBean.getSchemesubcategoryid());
			storedProcedureQuery.execute();
			total = (Long) storedProcedureQuery.getOutputParameterValue("P_TOTAL");
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
				resBean.setCpdApprovedAmount(snoDetailsObj.getString(13));
				resBean.setHospitalName(snoDetailsObj.getString(14));
				resBean.setMortality(snoDetailsObj.getString(15));
				resBean.setHospitalMortality(snoDetailsObj.getString(16));
				resBean.setActualDateOfAdmission(snoDetailsObj.getDate(17) != null
						? new SimpleDateFormat("dd-MMM-yyyy").format(snoDetailsObj.getDate(17))
						: "N/A");
				resBean.setActualDateOfDischarge(snoDetailsObj.getDate(18) != null
						? new SimpleDateFormat("dd-MMM-yyyy").format(snoDetailsObj.getDate(18))
						: "N/A");
				resBean.setHospitalCode(snoDetailsObj.getString(19));
				resBean.setPhone(snoDetailsObj.getString(20) == null ? "N/A" : snoDetailsObj.getString(20));
				resBean.setVerificationMode(snoDetailsObj.getLong(21));
				resBean.setTxnpackagedetailid(snoDetailsObj.getLong(22));
				resBean.setNabhFlag(snoDetailsObj.getString(23));
				resBean.setTriggerValue(snoDetailsObj.getLong(24));
				resBean.setTriggerMsg(snoDetailsObj.getString(25));
				resBean.setCategoryName(snoDetailsObj.getString(26));
				snoclaimRaiseDetailsList.add(resBean);
			}
			snoclaimRaiseDetailsMap.put("list", snoclaimRaiseDetailsList);
			snoclaimRaiseDetailsMap.put("size", total);
		} catch (Exception e) {
			logger.error("Exception Occurred in getSnoClaimRaiseDetailsList() of SnoClaimRaiseDetailsDaoImpl:", e);
			e.printStackTrace();
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getSnoClaimRaiseDetailsList() of SnoClaimRaiseDetailsDaoImpl:", e2);
			}
		}
		System.out.println(snoclaimRaiseDetailsMap);
		return snoclaimRaiseDetailsMap;
	}

	@Override
	public String getSnoClaimListById(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray cardBalanceArray = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_APR_DTLS")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				casenumber = snoDetailsObj.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				String mob = snoDetailsObj.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("pendingat", snoDetailsObj.getLong(51));
				jsonObject.put("claimstatus", snoDetailsObj.getLong(52));
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(53));
				jsonObject.put("verification", snoDetailsObj.getString(54));
				jsonObject.put("ispatient", snoDetailsObj.getString(55));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(56));
				jsonObject.put("PackageCode", snoDetailsObj.getString(57));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(58));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(59) != null ? snoDetailsObj.getString(59) : "NA");
				jsonObject.put("packageName1",
						snoDetailsObj.getString(60) != null ? snoDetailsObj.getString(60) : "NA");
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(61) != null ? snoDetailsObj.getString(61) : "NA");
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(62) != null ? snoDetailsObj.getString(62) : "NA");
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(63) != null ? snoDetailsObj.getString(63) : "NA");
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(64) != null ? snoDetailsObj.getString(64) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(65));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(66), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(67));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(68));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(69));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(70));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(71));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(72));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(73));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(74));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(75));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(76));
				jsonObject.put("disverification", snoDetailsObj.getString(77));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "N/A");
				jsonObject.put("uidreferencenumber", snoDetailsObj.getString(79));
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(81) != null ? snoDetailsObj.getString(81) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(82) != null ? snoDetailsObj.getString(82) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(83) != null ? snoDetailsObj.getString(83) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(84));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(85));
				jsonObject.put("categoryname", snoDetailsObj.getString(86));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("ACTIONON1", DateFormat.dateConvertor(snoDetailsObj1.getString(5), ""));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonArray.put(jsonObject1);
				}
				while (snoDetailsObj2.next()) {
					jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
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
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = getMultiplePackageBlockingthroughcaseno(casenumber, actualDate);
				cardBalanceArray = getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceArray);
			}
		} catch (Exception e) {
			logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
				if (snoDetailsObj2 != null) {
					snoDetailsObj2.close();
				}
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();
			} catch (Exception e2) {
				logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e2);
			}

		}
		return details.toString();
	}

	@Override
	public void downLoadFile(String fileName, String year, String hCode, HttpServletResponse response) {
		String folderName = null;
		if (fileName.startsWith(bskyResourcesBundel.getString("file.presurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.presurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.postsurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.postsurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Intrasurgery.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.IntraSurgeryPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Specimen.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.SpecimenRemovalPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Patient.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.PatientPic");
			// for Audit Mortality Audit Report --TMS
		} else if (fileName.startsWith("ADT")) {
			folderName = bskyResourcesBundel.getString("folder.MortalityAuditReport");
			// for Mortality Report --TMS
		} else if (fileName.startsWith("MOT")) {
			folderName = bskyResourcesBundel.getString("folder.MoralityDoc");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.moredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.needmoredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.AdditionalDoc.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.AdditionalDoc");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.DischargeSlip.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.DischargeSlip");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc1.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.investigationDoc2.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.investigation2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc1.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc2.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.cceDOc3.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.cce3");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.dgOfficer.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.dgoDocument");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Declaration.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Declaration");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Identity.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Identity");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Recomply.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Recomply");
		} else if (fileName.startsWith("OC")) {
			folderName = bskyResourcesBundel.getString("folder.Overridefile");
		} else {
			folderName = "PREAUTHDOC";
		}
		try {
			CommonFileUpload.downloadFile(fileName, year, hCode, folderName, response);
		} catch (IOException e) {
			logger.error("Error in downLoadFile method of PreAuthDaoImpl class:", e);
		}
	}

	@Override
	public File downLoadMultipleFile(String fileName, String year, String hCode, HttpServletResponse response) {
		File file = null;
		String folderName = null;
		if (fileName.startsWith(bskyResourcesBundel.getString("file.presurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.presurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.postsurgery-pic.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.postsurg.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Intrasurgery.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.IntraSurgeryPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Specimen.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.patient.SpecimenRemovalPic.photo");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.Patient.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.PatientPic");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.moredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc1");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.needmoredocument.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.Additionaldoc2");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.AdditionalDoc.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.AdditionalDoc");
		} else if (fileName.startsWith(bskyResourcesBundel.getString("file.DischargeSlip.prefix"))) {
			folderName = bskyResourcesBundel.getString("folder.DischargeSlip");
		} else {
			folderName = "PREAUTHDOC";
		}
		try {
			file = CommonFileUpload.downloadMultipleFile(fileName, year, hCode, folderName, response);
		} catch (IOException e) {
			logger.error("Error in downLoadMultipleFile method of PreAuthDaoImpl class:", e);
		}
		return file;
	}

	@Override
	public Response saveClaimSNODetails(ClaimLogBean logBean) throws Exception {
		// logger.info("Inside saveClaimSNODetails method of PreAuthDaoImpl class");
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : logBean.getIcdFinalData()) {
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
			e1.printStackTrace();
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_APR_ACTION")
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
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_MORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", logBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", logBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", logBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", logBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", logBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", logBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", logBean.getIntrasurgery());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", logBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.setParameter("P_SNA_MORTALITY", logBean.getSnamortality());
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
	public ActionRemark getActionRemarkById(Long remarkId) {
		return actionrepo.findById(remarkId).get();
	}

	@Override
	public List<ActionRemark> getAllActionRemark() {
		return actionrepo.findAll();
	}

	// this is for enrollment purpose remark
	@Override
	public List<EnrollmentRemarks> getEnrollmentAllActionRemark() {
		return enrollmentremarkrepository.findAll();
	}

	@Override
	public String getPreAuthData(String urn) {
		// logger.info("Inside getPreAuthData of ClaimLogDaoImpl class");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_PREAUTH_DTLS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_URN", urn);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");

			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("REMARKS", snoDetailsObj.getString(1));
				jsonObject.put("ACTIONDATE", snoDetailsObj.getString(2));
				jsonObject.put("APPROVEDAMOUNT", snoDetailsObj.getString(3));
				jsonObject.put("FILEPATH", snoDetailsObj.getString(4));
				jsonObject.put("STATUS", snoDetailsObj.getString(5));
				jsonObject.put("SDATE", DateFormat.FormatToDateString(snoDetailsObj.getString(6)));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(7));
				jsonObject.put("AUTHORITYCODE", snoDetailsObj.getString(8));
				jsonObject.put("UPLOADDATE", snoDetailsObj.getString(9));
				jsonObject.put("NEEDMOREDOCS", snoDetailsObj.getString(10));
				jsonObject.put("AMOUNT", snoDetailsObj.getString(11));
				jsonObject.put("urnno", snoDetailsObj.getString(12));
				jsonArray.put(jsonObject);

			}
		} catch (Exception e) {
			logger.error("Exception Occured in getPreAuthData of ClaimLogDaoImpl class", e);
			throw new RuntimeException(e);
		} finally {

			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occured in getPreAuthData of ClaimLogDaoImpl class", e2);
			}
		}
		return jsonArray.toString();
	}

	@Override
	public String getDistrictList(String stateCode, Long userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_sna_district_list")
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("DISTRICTID", snoDetailsObj.getLong(1));
				jsonObject.put("DISTRICTCODE", snoDetailsObj.getString(2));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(3));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getDistrictList of ClaimLogDaoImpl class", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getDistrictList of ClaimLogDaoImpl class", e2);
			}
		}
		return jsonArray.toString();
	}

	@Override
	public String getHospital(String stateCode, String distCode, Long userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_HOSPITAL_LIST")
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTCODE", distCode);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("SNAMAPPINGID", snoDetailsObj.getLong(1));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(2));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(3));
				jsonArray.put(jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getHospital of ClaimLogDaoImpl class", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getHospital of ClaimLogDaoImpl class", e2);
			}
		}
		return jsonArray.toString();
	}

	@Override
	public List<State> getAllState() {
		List<State> findAll = stateRepo.findAll();
		return findAll;
	}

	@Override
	public List<ActionType> getActionType() {
		List<ActionType> findAll = actionTypeRepo.findAll();
		return findAll;
	}

	@Override
	public String getMultiPackageBlock(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray cardBalanceArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_multi_package_dtls")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				casenumber = snoDetailsObj.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(50));
				jsonObject.put("verification", snoDetailsObj.getString(51));
				jsonObject.put("ispatient", snoDetailsObj.getString(52));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(53));
				jsonObject.put("PackageCode", snoDetailsObj.getString(54));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(55) == null ? "NA" : snoDetailsObj.getString(55));
				jsonObject.put("packageName1",
						snoDetailsObj.getString(56) == null ? "NA" : snoDetailsObj.getString(56));
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(57) == null ? "NA" : snoDetailsObj.getString(57));
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(58) == null ? "NA" : snoDetailsObj.getString(58));
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(59) == null ? "NA" : snoDetailsObj.getString(59));
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(60) == null ? "NA" : snoDetailsObj.getString(60));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(61));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(62), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(63));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(64));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(65));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(66));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(67));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(68));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(69));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(70));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(71));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(72));
				jsonObject.put("MOBILE", snoDetailsObj.getString(73));
				jsonObject.put("disverification", snoDetailsObj.getString(74));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(75));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(76) != null ? snoDetailsObj.getString(76) : "N/A");
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(77) != null ? snoDetailsObj.getString(77) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(79) != null ? snoDetailsObj.getString(79) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(81));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(82));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("ACTIONON1", DateFormat.dateConvertor(snoDetailsObj1.getString(5), ""));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonArray.put(jsonObject1);
				}
				while (snoDetailsObj2.next()) {
					jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
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
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = getMultiplePackageBlockingthroughcaseno(casenumber, actualDate);
				cardBalanceArray = getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceArray);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getActionDetails method of ActionDaoImpl class :: ", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null)
					snoDetailsObj.close();
				if (snoDetailsObj1 != null)
					snoDetailsObj1.close();
				if (snoDetailsObj2 != null)
					snoDetailsObj2.close();
				if (snoDetailsObj3 != null)
					snoDetailsObj3.close();
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();

			} catch (Exception e2) {
				logger.error("Exception Occurred in getActionDetails method of ActionDaoImpl class :: ", e2);
			}
		}
		return details.toString();
	}

	@Override
	public String dischargeTreatment(Integer txnId) throws Exception {
		;
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_CPD_APR_DTLS")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			if (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				String mob = snoDetailsObj.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				details.put("actionData", jsonObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getActionDetails method of ClaimActionDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				} else if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getActionDetails method of ClaimActionDaoImpl class:", e2);
			}
		}

		return details.toString();
	}

	@Override
	public void filteredFile(JSONArray jsonArray, HttpServletResponse response) throws JSONException, IOException {
		String year = "";
		File file = null;
		PDFMergerUtility pdfMerger = new PDFMergerUtility();
		for (int i = 0; jsonArray.length() > i; i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			String fileName = json.getString("f");
			String hCode = json.getString("h");
			String dateOfAdm = json.getString("d");
			try {
				if (dateOfAdm.length() > 11) {
					String preAuthDate = new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(dateOfAdm));
					year = preAuthDate.substring(6);
				} else {
					year = dateOfAdm.substring(6);
				}
				file = downLoadMultipleFile(fileName, year, hCode, response);
				pdfMerger.addSource(file);
			} catch (Exception e) {
				logger.error("Exception Occurred in filteredFile method of ClaimActionDaoImpl class:", e);
			}
		}
		CommonFileUpload.downloadMultiplemergedFile(pdfMerger, response);
	}

	@Override
	public String getCountReportBtCPDAprv(CPDApproveRequestBean requestBean) {
		Integer totalcpdapproved = null;
		Integer snaappofcpdapp = null;
		Integer snaqryofcpdapp = null;
		JSONObject jsonObject = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_sna_aprv_count")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_cpd_flag", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("totalcpdapproved", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("snaappofcpdapp", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("TOTAL_SNA_QUERY", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_cpd_flag", requestBean.getCpdFlag());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.execute();
			totalcpdapproved = (Integer) storedProcedureQuery.getOutputParameterValue("totalcpdapproved");
			snaappofcpdapp = (Integer) storedProcedureQuery.getOutputParameterValue("snaappofcpdapp");
			snaqryofcpdapp = (Integer) storedProcedureQuery.getOutputParameterValue("TOTAL_SNA_QUERY");
			jsonObject = new JSONObject();
			jsonObject.put("totalcpdapproved", totalcpdapproved);
			jsonObject.put("snaappofcpdapp", snaappofcpdapp);
			jsonObject.put("snaqryofcpdapp", snaqryofcpdapp);
		} catch (Exception e) {
			logger.error("Exception Occurred in getCountReportBtCPDAprv method of ClaimActionDaoImpl class:", e);
			throw new RuntimeException(e);
		}
		return jsonObject.toString();
	}

	@Override
	public JSONObject getcountdetails(Bulkapprovalbean requestBean) {
		JSONObject count = new JSONObject();
		ResultSet snoDetailsObj = null, rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_BULK_APPP_SNA")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKDATA", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateCode1());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistCode1());
			storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_BLOCKDATA", requestBean.getSearchtype());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				count.put("snaapproved", snoDetailsObj.getLong(1));
				count.put("snarejected", snoDetailsObj.getLong(2));
				count.put("cpdApproved", snoDetailsObj.getLong(3));
				count.put("snaQuery", snoDetailsObj.getLong(4));
				count.put("pendatsna", snoDetailsObj.getLong(5));
				count.put("resettlement", snoDetailsObj.getLong(6));
			}
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				count.put("snaActionOfCpdAprvd", rs.getLong(1));
				count.put("snaAprvdOfCpdAprvd", rs.getLong(2));
				count.put("snaActionOfCpdRjctd", rs.getLong(3));
				count.put("snaAprvdOfCpdRjctd", rs.getString(4));
				count.put("snaRjctdOfCpdRjctd", rs.getLong(5));
				count.put("snaQueryOfCpdRjctd", rs.getLong(6));
				count.put("snaInvstgOfCpdRjctd", rs.getLong(7));
				count.put("snaTotalOfCpdAprvd", rs.getLong(8));
				count.put("snaTotalOfSysRjctd", rs.getLong(9));
				count.put("tcpdApproved", rs.getLong(10));
				count.put("snaQueryOfCpdAprvd", rs.getLong(11));
				count.put("snaRjctdOfCpdAprvd", rs.getLong(12));
				count.put("app_persent_flag", rs.getLong(13));
				count.put("percentage", rs.getLong(1) + rs.getLong(12));
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getcountdetails method of ClaimActionDaoImpl class:", e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getcountdetails method of ClaimActionDaoImpl class:", e2);
			}
		}
		return count;
	}

	@Override
	public Response getsavebulkapproveds(Long user, Long group, String flags, Date fromDate, Date toDate,
			String stateid, String districtid, String hospitalid) throws Exception {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_BULK_INSERT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_user_id", user);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_statecode", stateid.trim());
			storedProcedureQuery.setParameter("p_districtcode", districtid.trim());
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalid.trim());
			storedProcedureQuery.execute();
			int bulkapp = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
			if (bulkapp == 1) {
				response.setStatus("Success");
				response.setMessage("Bulk Approved Successfully Done");
			} else {
				response.setStatus("Failed");
				response.setMessage("OOPS Something Went Wrong, please Try Again..");
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getsavebulkapproveds method of ClaimActionDaoImpl class:", e);
			throw new RuntimeException();
		}
		return response;
	}

	@Override
	public void downloadDocuments(JSONArray jsonArray, HttpServletResponse response) {
		String year = "";
		PDDocument mergedPDFDocument = new PDDocument();
		List<PDDocument> pdfDocuments = new ArrayList<>();
		try {
			for (int i = 0; jsonArray.length() > i; i++) {
				String fileName = jsonArray.getJSONObject(i).getString("f");
				String hospitalCode = jsonArray.getJSONObject(i).getString("h");
				String date = jsonArray.getJSONObject(i).getString("d");
				if (date.length() > 11) {
					String preAuthDate = new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
					year = preAuthDate.substring(6).trim();
				} else {
					year = date.substring(6).trim();
				}
				String year1 = String.valueOf(Integer.parseInt(year) - 1);
				String year2 = String.valueOf(Integer.parseInt(year) + 1);
				String fullFilePath = CommonFileUpload.getFullDocumentPath(fileName, year, hospitalCode,
						CommonFileUpload.getFolderName(fileName));
				String fullFilePath1 = CommonFileUpload.getFullDocumentPath(fileName, year1, hospitalCode,
						CommonFileUpload.getFolderName(fileName));
				String fullFilePath2 = CommonFileUpload.getFullDocumentPath(fileName, year2, hospitalCode,
						CommonFileUpload.getFolderName(fileName));
				File filepath = new File(fullFilePath);
				String tempFilepath = fullFilePath;
				if (!filepath.exists()) {
					filepath = new File(fullFilePath1);
					tempFilepath = fullFilePath1;
					if (!filepath.exists()) {
						filepath = new File(fullFilePath2);
						tempFilepath = fullFilePath2;
					}
				}
				if (fileName.endsWith(".pdf")) {
					PDDocument doc = PDDocument.load(new File(tempFilepath));
					pdfDocuments.add(doc);
				} else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".JPG")
						|| fileName.endsWith(".JPEG") || fileName.endsWith(".jfif") || fileName.endsWith(".JFIF")) {
					PDDocument doc = new PDDocument();
					PDPage page = new PDPage();
					doc.addPage(page);
					PDPageContentStream contentStream = new PDPageContentStream(doc, page);
					BufferedImage bufferedImage = ImageIO.read(new File(tempFilepath));
					PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bufferedImage);
					contentStream.beginText();
					contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
					contentStream.newLineAtOffset(250, 700);
					contentStream.showText(CommonFileUpload.getFolderName(fileName)
							.substring(CommonFileUpload.getFolderName(fileName).indexOf("/") + 1));
					contentStream.endText();
					contentStream.drawImage(pdImage, 120, 150, 350, 450);
					contentStream.close();
					pdfDocuments.add(doc);
				}
			}
			for (PDDocument pdfDocument : pdfDocuments) {
				PDPageTree pageTree = pdfDocument.getDocumentCatalog().getPages();
				for (PDPage page : pageTree) {
					mergedPDFDocument.addPage(page);
				}
			}
			OutputStream out = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"mergedDocument.pdf\"");
			mergedPDFDocument.save(out);
			out.flush();
			out.close();
			for (PDDocument pdfDocument : pdfDocuments) {
				pdfDocument.close();
			}
			mergedPDFDocument.close();
		} catch (Exception e) {
			logger.error("Exception Occurred in downloadDocuments method of ClaimActionDaoImpl class:", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Object> getdcClaimApproveddata(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_DC_APPROVED_LST")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_remarks", requestBean.getDescription());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", requestBean.getTrigger());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
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
				resBean.setActualDateOfDischarge(DateFormat.dateConvertor(snoDetailsObj.getString(16), ""));
				resBean.setActualDateOfAdmission(DateFormat.dateConvertor(snoDetailsObj.getString(17), ""));
				resBean.setHospitalCode(snoDetailsObj.getString(18));
				resBean.setPhone(snoDetailsObj.getString(19) == null ? "N/A" : snoDetailsObj.getString(19));
				resBean.setTxnpackagedetailid(snoDetailsObj.getLong(20));
				resBean.setTriggerValue(snoDetailsObj.getLong(21));
				resBean.setTriggerMsg(snoDetailsObj.getString(22));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getdcClaimApproveddata method of ClaimActionDaoImpl class:", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getdcClaimApproveddata method of ClaimActionDaoImpl class:", e2);
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public String getDCClaimAprvById(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray cardBalanceArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		JSONArray multipackagecaeno = new JSONArray();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_DC_APPROVED_CLM_DTLS")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				casenumber = snoDetailsObj.getString(43);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				jsonObject.put("MOBILE", snoDetailsObj.getString(75));
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(50));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(51));
				jsonObject.put("INVESTIGATIONDOC2", snoDetailsObj.getString(52));
				jsonObject.put("verification", snoDetailsObj.getString(53));
				jsonObject.put("ispatient", snoDetailsObj.getString(54));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(55));
				jsonObject.put("packageName", snoDetailsObj.getString(56));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(57) != null ? snoDetailsObj.getString(57) : "NA");
				jsonObject.put("packageName1",
						snoDetailsObj.getString(58) != null ? snoDetailsObj.getString(58) : "NA");
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(59) != null ? snoDetailsObj.getString(59) : "NA");
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(60) != null ? snoDetailsObj.getString(60) : "NA");
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(61) != null ? snoDetailsObj.getString(61) : "NA");
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(62) != null ? snoDetailsObj.getString(62) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(63));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(64), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(65));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(66));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(67));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(68));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(69));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(70));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(71));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(72));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(73));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(74));
				jsonObject.put("disverification", snoDetailsObj.getString(76));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(77));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "N/A");
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(79) != null ? snoDetailsObj.getString(79) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(81) != null ? snoDetailsObj.getString(81) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(82) != null ? snoDetailsObj.getString(82) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(83));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(84));
				jsonObject.put("categoryName", snoDetailsObj.getString(85));
				details.put("actionData", jsonObject);

				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonObject1.put("INVESTIGATION", snoDetailsObj1.getString(17));
					jsonObject1.put("INVESTIGATION2", snoDetailsObj1.getString(18));
					jsonArray.put(jsonObject1);
				}
				while (snoDetailsObj2.next()) {
					JSONObject jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
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
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = getMultiplePackageBlockingthroughcaseno(casenumber, actualDate);
				cardBalanceArray = getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceArray);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null)
					snoDetailsObj.close();
				if (snoDetailsObj1 != null)
					snoDetailsObj1.close();
				if (snoDetailsObj2 != null)
					snoDetailsObj2.close();
				if (snoDetailsObj3 != null)
					snoDetailsObj3.close();
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();

			} catch (Exception e2) {
			}
		}
		return details.toString();
	}

	@Override
	public Response saveClaimSNOOfDCAprvDetails(ClaimLogBean logBean) throws Exception {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : logBean.getIcdFinalData()) {
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
			e1.printStackTrace();
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_DC_APR_ACTION")
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
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_invesigation1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_invesigation2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_MORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", logBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", logBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", logBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", logBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", logBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", logBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", logBean.getIntrasurgery());
			storedProcedureQuery.setParameter("p_invesigation1", logBean.getInvestigationdocs1());
			storedProcedureQuery.setParameter("p_invesigation2", logBean.getInvestigationdocs2());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", logBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.setParameter("P_SNA_MORTALITY", logBean.getSnamortality());
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
			logger.error("Exception raised in saveClaimLog method of ClaimDaoImpl:", e);
		}

		return response;
	}

	@SuppressWarnings("deprecation")
	public String getRefFromAadhaar(String aadhaarNo, String schemeId) throws IOException {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		String jsonBody = String.format("{\n    \"aadhaarNo\":\"%s\",\n    \"schemeId\":\"%s\"\n}", aadhaarNo,
				schemeId);
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonBody);

		Request request = new Request.Builder().url(baseurl + "getRefFromAadhaar").method("POST", body)
				.addHeader("Content-Type", "application/json").build();
		okhttp3.Response execute = client.newCall(request).execute();
		if (!execute.isSuccessful()) {
			throw new IOException("Unexpected code " + execute);
		}
		return execute.body().string();
	}

	@SuppressWarnings("deprecation")
	public String getAadhaarFromRef(String referenceKey, String schemeId) throws IOException {
		String BASE_URL = "http://117.239.112.230/AadhaarVaultEncryption/rest/";
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		String jsonBody = String.format("{\n    \"referenceKey\":\"%s\",\n    \"schemeId\":\"%s\"\n}", referenceKey,
				schemeId);
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonBody);
		Request request = new Request.Builder().url(BASE_URL + "getAadhaarFromRef").method("POST", body)
				.addHeader("Content-Type", "application/json").build();
		okhttp3.Response execute = client.newCall(request).execute();
		if (!execute.isSuccessful()) {
			throw new IOException("Unexpected code " + execute);
		}
		return execute.body().string();
	}

	@Override
	public List<Object> getOldClaimApprovedList(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_OLD_PROCESSED_CLAIM_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claim_status", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_claim_status", requestBean.getAction().trim());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (snoDetailsObj.next()) {
				OldClaimListBean resBean = new OldClaimListBean();
				resBean.setTransid(snoDetailsObj.getLong(1));
				resBean.setURN(snoDetailsObj.getString(2));
				resBean.setClaimstatus(snoDetailsObj.getString(3));
				resBean.setHospitalcode(snoDetailsObj.getString(4));
				resBean.setHospitalname(snoDetailsObj.getString(5));
				resBean.setPatientName(snoDetailsObj.getString(6));
				resBean.setInvoiceNumber(snoDetailsObj.getString(7));
				resBean.setSnaremarks(snoDetailsObj.getString(8));
				resBean.setRemarks(snoDetailsObj.getString(9));
				resBean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(10))));
				resBean.setDateofdischarge(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(11))));
				resBean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(12))));
				resBean.setDateofadmission(new SimpleDateFormat("dd-MMM-yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse(snoDetailsObj.getString(13))));
				resBean.setTransactionDetailsId(snoDetailsObj.getLong(14));
				resBean.setOldClaimNo(snoDetailsObj.getString(15));
				resBean.setApproveduserid(snoDetailsObj.getString(16));
				resBean.setRejecteduserid(snoDetailsObj.getString(17));
				resBean.setInvestigationuserid(snoDetailsObj.getString(18));
				resBean.setSnaapproveduserid(snoDetailsObj.getString(19));
				resBean.setSnarejecteduserid(snoDetailsObj.getString(20));
				resBean.setSnainvestigationuserid(snoDetailsObj.getString(21));
				resBean.setSnafinaldecisionuserid(snoDetailsObj.getString(22));
				resBean.setPaiduserid(snoDetailsObj.getString(23));
				resBean.setTpafinaldecisionuserid(snoDetailsObj.getString(24));
				resBean.setApproveduser(snoDetailsObj.getString(25));
				resBean.setRejecteduser(snoDetailsObj.getString(26));
				resBean.setInvestigationuser(snoDetailsObj.getString(27));
				resBean.setSnaapproveduser(snoDetailsObj.getString(28));
				resBean.setSnarejecteduser(snoDetailsObj.getString(29));
				resBean.setSnainvestigationuser(snoDetailsObj.getString(30));
				resBean.setSnafinaldecisionuser(snoDetailsObj.getString(31));
				resBean.setPaiduser(snoDetailsObj.getString(32));
				resBean.setTpafinaldecisionuser(snoDetailsObj.getString(33));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception raised in getOldClaimApprovedList method of ClaimDaoImpl:", e);
			throw new RuntimeException(e);
		} finally {

			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception raised in getOldClaimApprovedList method of ClaimDaoImpl:", e2);
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	@Override
	public String getOldClaimById(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONObject jsonObject;
		JSONObject details = new JSONObject();
		String urn = null;
		String authorizedCode = null;
		String hospitalCode = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_PROCESSED_CLAIM_DTLS")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(28);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(24), snoDetailsObj.getString(25)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(19));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(21));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(22));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(23));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(24)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(25)));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(26));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(27));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(28));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(29));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(30));
				jsonObject.put("Address", snoDetailsObj.getString(31));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(32));
				jsonObject.put("PackageCode", snoDetailsObj.getString(33));
				jsonObject.put("MOBILE", snoDetailsObj.getString(34));
				jsonObject.put("year", snoDetailsObj.getString(35));
				jsonObject.put("HOSPITALCODE1", snoDetailsObj.getString(36));
				jsonObject.put("FILENAMES", snoDetailsObj.getString(37));
				jsonObject.put("FILENAMES1", snoDetailsObj.getString(38));
				jsonObject.put("FILENAMES2", snoDetailsObj.getString(39));
				jsonObject.put("CLAIMSTATUS", snoDetailsObj.getString(40));
				if (snoDetailsObj.getString(40) != null && !"".equals(snoDetailsObj.getString(40))) {
					if (snoDetailsObj.getString(40).equalsIgnoreCase("SNARejected")
							|| snoDetailsObj.getString(40).equalsIgnoreCase("Approved")) {
						jsonObject.put("BTNSTATUS", "1");
					} else {
						jsonObject.put("BTNSTATUS", "0");
					}
				}
				jsonObject.put("APPROVEDAMOUNT", snoDetailsObj.getString(41));
				jsonObject.put("APPROVEDDATE", snoDetailsObj.getString(42));
				jsonObject.put("PAIDBY", snoDetailsObj.getString(43));
				jsonObject.put("CHECK_DDNO", snoDetailsObj.getString(44));
				jsonObject.put("BANKNAME", snoDetailsObj.getString(45));
				jsonObject.put("PAIDDATE", snoDetailsObj.getString(46));
				jsonObject.put("REMARKS", snoDetailsObj.getString(47));
				jsonObject.put("REJECTEDDATE", snoDetailsObj.getString(48));
				jsonObject.put("INVESTIGATIONDATE", snoDetailsObj.getString(49));
				jsonObject.put("SNACLAIMSTATUS", snoDetailsObj.getString(50));
				jsonObject.put("SNAAPPROVEDAMOUNT", snoDetailsObj.getString(51));
				jsonObject.put("SNAAPPROVEDDATE", snoDetailsObj.getString(52));
				jsonObject.put("SNAREJECTEDDATE", snoDetailsObj.getString(53));
				jsonObject.put("SNAINVESTIGATIONDATE", snoDetailsObj.getString(54));
				jsonObject.put("SNAREMARKS", snoDetailsObj.getString(55));
				jsonObject.put("TPAFINALSTATUS", snoDetailsObj.getString(56));
				jsonObject.put("TPAFINALDECISIONDATE", snoDetailsObj.getString(57));
				jsonObject.put("REVERTREMARKS", snoDetailsObj.getString(58));
				jsonObject.put("SNAFINALSTATUS", snoDetailsObj.getString(59));
				jsonObject.put("SNAFINALDECISIONDATE", snoDetailsObj.getString(60));
				details.put("actionData", jsonObject);

//				while (snoDetailsObj1.next()) {
//					jsonObject1 = new JSONObject();
//					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
//					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
//					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
//					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
//					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
//					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
//					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
//					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
//					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
//					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
//					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
//					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
//					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
//					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
//					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
//					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
//					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
//					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
//					jsonObject1.put("INVESTIGATION", snoDetailsObj1.getString(17));
//					jsonObject1.put("INVESTIGATION2", snoDetailsObj1.getString(18));
//					jsonArray.put(jsonObject1);
//				}
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				details.put("actionLog", jsonArray);
				details.put("preAuthHist", preAuthHist);
			}
		} catch (Exception e) {
			logger.error("Error in getClaimDetails of ClaimDetailsDaoImpl :", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				} else if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getClaimDetails of ClaimDetailsDaoImpl :", e2);
			}
		}
		return details.toString();
	}

	@Override
	public Response saveoldClaimDetails(ClaimLogBean logBean) {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		try {
			localhost = InetAddress.getLocalHost();
			getuseripaddressString = localhost.getHostAddress();
		} catch (UnknownHostException e1) {
			logger.error("Error in saveoldClaimDetails of ClaimDetailsDaoImpl :", e1);
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OLD_PROCESSED_CLAIM_ACTION")
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
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			if (claimsnoInteger == 1 && logBean.getClaimStatus() == 4) {
				response.setStatus("Success");
				response.setMessage("Claim Queried Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error("Error in saveoldClaimDetails of ClaimDetailsDaoImpl :", e);
		}

		return response;
	}

	@Override
	public List<Object> getClaimsOnHoldList(CPDApproveRequestBean requestBean) {
		List<Object> SnoclaimRaiseDetailsList = new ArrayList<Object>();
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_CLAIMS_ON_HOLD")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("P_REMARKS", requestBean.getDescription());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
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
				resBean.setCpdApprovedAmount(snoDetailsObj.getString(13));
				resBean.setHospitalName(snoDetailsObj.getString(14));
				resBean.setMortality(snoDetailsObj.getString(15));
				resBean.setHospitalMortality(snoDetailsObj.getString(16));
				if (snoDetailsObj.getString(17) != null && snoDetailsObj.getString(17) != "") {
					resBean.setActualDateOfAdmission(DateFormat.dateConvertor(snoDetailsObj.getString(17), ""));
				}
				if (snoDetailsObj.getString(18) != null && snoDetailsObj.getString(18) != "") {
					resBean.setActualDateOfDischarge(DateFormat.dateConvertor(snoDetailsObj.getString(18), ""));
				}
				resBean.setHospitalCode(snoDetailsObj.getString(19));
				resBean.setPhone(snoDetailsObj.getString(20) == null ? "N/A" : snoDetailsObj.getString(20));
				resBean.setCaseNo(snoDetailsObj.getString(22));
				SnoclaimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Error in getClaimsOnHoldList of ClaimDetailsDaoImpl :", e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getClaimsOnHoldList of ClaimDetailsDaoImpl :", e2);
			}
		}
		return SnoclaimRaiseDetailsList;
	}

	// @Override
//	public void downloadDocuments(JSONArray jsonArray, HttpServletResponse response) {
//		String year = "";
//		Document mergedDocument = new Document(PageSize.A4);
//		mergedDocument.open();
//		try {
//			for (int i = 0; jsonArray.length() > i; i++) {
//				String fileName = jsonArray.getJSONObject(i).getString("f");
//				String hospitalCode = jsonArray.getJSONObject(i).getString("h");
//				String date = jsonArray.getJSONObject(i).getString("d");
//				if (date.length() > 11) {
//					String preAuthDate = new SimpleDateFormat("dd MMM yyyy")
//							.format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
//					year = preAuthDate.substring(6);
//				} else {
//					year = date.substring(6);
//				}
//				String fullFilePath = CommonFileUpload.getFullDocumentPath(fileName, year, hospitalCode, CommonFileUpload.getFolderName(fileName));
//
//				if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
//					Image image = Image.getInstance(fullFilePath);
//					image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
//					image.setAlignment(Image.ALIGN_CENTER);
//					mergedDocument.add(image);
//				}
//			}
//			response.setContentType("application/pdf");
//			response.setHeader("Content-Disposition", "attachment; filename=\"mergedDocument.pdf\"");
//			PdfWriter.getInstance(mergedDocument, response.getOutputStream());
//			OutputStream out = response.getOutputStream();
//
//		}catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
	@SuppressWarnings("unchecked")
	public JSONArray getMultiplePackageBlockingthroughcaseno(String casenumber, String actualDate)
			throws ParseException {
		List<Object[]> preAuthLogListcase;
		JSONArray jsonArray1 = new JSONArray();
		JSONObject jsonObject1;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_multi_pack_dts_caseno")
					.registerStoredProcedureParameter("actualdate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CASENO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_multi_pack_dtls_caseno", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("actualdate", actualDate);
			storedProcedureQuery.setParameter("P_CASENO", casenumber);
			storedProcedureQuery.execute();
			preAuthLogListcase = storedProcedureQuery.getResultList();
			for (Iterator<Object[]> iterator = preAuthLogListcase.iterator(); iterator.hasNext();) {
				Object[] preAuthDetails = iterator.next();
				jsonObject1 = new JSONObject();
				jsonObject1.put("dateofAdmission", preAuthDetails[0]);
				jsonObject1.put("urn", preAuthDetails[1]);
				jsonObject1.put("count", preAuthDetails[2]);
				jsonObject1.put("patientName", preAuthDetails[3]);
				jsonObject1.put("actualDateof", preAuthDetails[4]);
				jsonObject1.put("actualDischarge", preAuthDetails[5]);
				jsonObject1.put("actualDateof1", DateFormat.dateConvertor(String.valueOf(preAuthDetails[4]), ""));
				jsonObject1.put("actualDischarge1", DateFormat.dateConvertor(String.valueOf(preAuthDetails[5]), ""));
				jsonObject1.put("packageName", preAuthDetails[6]);
				jsonObject1.put("transctionId", preAuthDetails[7]);
				jsonObject1.put("hospitalcode", preAuthDetails[8]);
				if (preAuthDetails[9] != null)
					jsonObject1.put("authorizedcode", preAuthDetails[9].toString().substring(2));
				else
					jsonObject1.put("authorizedcode", "");
				jsonObject1.put("claimStatus", preAuthDetails[10].toString().equalsIgnoreCase("0") ? "No" : "YES");
				jsonObject1.put("packageCode", preAuthDetails[11]);
				jsonObject1.put("dischargeAmount", preAuthDetails[12]);
				jsonObject1.put("cpdappamount", preAuthDetails[13]);
				jsonObject1.put("snaappamount", preAuthDetails[14]);
				jsonObject1.put("currentclaimstatus", preAuthDetails[15]);
				jsonObject1.put("calculatedpercentage", preAuthDetails[16]);
				jsonObject1.put("packagecost", preAuthDetails[17]);
				jsonObject1.put("totalimpalantcost", preAuthDetails[18]);
				jsonObject1.put("totalhedcost", preAuthDetails[19]);
				jsonObject1.put("amountblocked", preAuthDetails[20]);
				jsonObject1.put("type", preAuthDetails[21]);
				jsonObject1.put("calculatedclaimamount", preAuthDetails[22]);
				jsonObject1.put("totalamountclaimed", preAuthDetails[23]);
				jsonObject1.put("totalamountclaimedfinal", preAuthDetails[24]);
				jsonObject1.put("claimstatus", preAuthDetails[25]);
				jsonObject1.put("txnpackagedetailid", preAuthDetails[26]);
				jsonObject1.put("pendingat", preAuthDetails[27]);
				jsonObject1.put("claimno", preAuthDetails[28]);
				jsonObject1.put("caseno", preAuthDetails[29]);
				jsonArray1.put(jsonObject1);
			}

		} catch (JSONException e) {
			logger.error("Error in getMultiplePackageBlocking method of CPDClaimProcessingServiceImpl", e);
		}

		return jsonArray1;
	}

	@Override
	public String getTreatmentHistoryoverpackgae(Long txnId, String urnnumber, String hospitalcode, String caseno,
			String uidreferencenumber, Long userid) throws Exception {
		JSONArray treatmentlog = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_TREATMENT_HISTORY_DTLS")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_uidreferencenumber", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userid);
			storedProcedureQuery.setParameter("p_urn", urnnumber.trim());
			storedProcedureQuery.setParameter("p_uidreferencenumber", uidreferencenumber.trim());
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalcode.trim());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("totalnumberofpackage", rs.getString(1) != null ? rs.getString(1) : "N/A");
				json.put("totalnumberofmember", rs.getString(2) != null ? rs.getString(2) : "N/A");
				json.put("totalnumberofpackageforhospital", rs.getString(3) != null ? rs.getString(3) : "N/A");
				json.put("totalnumberofmemberforhospital", rs.getString(4) != null ? rs.getString(4) : "N/A");
				json.put("totalNoOfBloackedamount", rs.getString(5) != null ? rs.getString(5) : 0);
				json.put("packageblockedforpatient", rs.getString(6) != null ? rs.getString(6) : "N/A");
				json.put("sumofpackageblockedamount", rs.getString(7) != null ? rs.getString(7) : 0);
				treatmentlog.put(json);
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
		return treatmentlog.toString();
	}

	@Override
	public String getremarkdetailsforsna(Long snaid, Date fromdate, Date todate, String hospitalcode, String stateode,
			String distcode) {
		JSONArray remarkdetailslog = new JSONArray();
		ResultSet result = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_REJECTION_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_remarkid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalcoderemark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", snaid);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalcode.trim());
			storedProcedureQuery.setParameter("p_statecode", stateode.trim());
			storedProcedureQuery.setParameter("p_districtcode", distcode.trim());
			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_remarkid", null);
			storedProcedureQuery.setParameter("P_hospitalcoderemark", null);
			storedProcedureQuery.execute();
			result = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (result.next()) {
				JSONObject json = new JSONObject();
				json.put("remarkid", result.getString(1) != null ? result.getString(1) : "N/A");
				json.put("remarkname", result.getString(2) != null ? result.getString(2) : "N/A");
				json.put("remarkcount", result.getString(3) != null ? result.getString(3) : "0");
				remarkdetailslog.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null) {
					result.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return remarkdetailslog.toString();
	}

	@Override
	public String getcountremarkdetailsforsna(Long userid, Date fromdate, Date todate, String statecode,
			String districtcode, String hospitalcode, Long remarkid, String hospitalcodeforremark) {
		JSONArray countlog = new JSONArray();
		ResultSet result = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_REJECTION_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_remarkid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalcoderemark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalcode.trim());
			storedProcedureQuery.setParameter("p_statecode", statecode.trim());
			storedProcedureQuery.setParameter("p_districtcode", districtcode.trim());
			storedProcedureQuery.setParameter("P_ACTION_CODE", 3);
			storedProcedureQuery.setParameter("P_remarkid", remarkid);
			storedProcedureQuery.setParameter("P_hospitalcoderemark", hospitalcodeforremark.trim());
			storedProcedureQuery.execute();
			result = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (result.next()) {
				JSONObject json = new JSONObject();
				json.put("remarkid", result.getString(1) != null ? result.getString(1) : "N/A");
				json.put("patientname", result.getString(2) != null ? result.getString(2) : "N/A");
				json.put("urn", result.getString(3) != null ? result.getString(3) : "N/A");
				json.put("claimno", result.getString(4) != null ? result.getString(4) : "N/A");
				json.put("hospitalname", result.getString(5) != null ? result.getString(5) : "N/A");
				json.put("hospitalcode", result.getString(6) != null ? result.getString(6) : "N/A");
				json.put("actualdateofadmission", DateFormat.FormatToDateString(result.getString(7)));
				json.put("actualdateofdischarge", DateFormat.FormatToDateString(result.getString(8)));
				json.put("transactiondetailsid", result.getString(9) != null ? result.getString(9) : "N/A");
				json.put("claimid", result.getString(10) != null ? result.getString(10) : "N/A");
				countlog.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null) {
					result.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return countlog.toString();
	}

	@Override
	public String getcountremarkdetailspayment(String urn, Long claimid, String floatno) {
		JSONArray floatlog = new JSONArray();
		ResultSet rslist = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_CLAIM_DTLS")
					.registerStoredProcedureParameter("P_floatno", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_claimid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_floatno", floatno.trim());
			storedProcedureQuery.setParameter("P_claimid", claimid);
			storedProcedureQuery.execute();
			rslist = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rslist.next()) {
				JSONObject json = new JSONObject();
				json.put("urn", rslist.getString(1) != null ? rslist.getString(1) : "N/A");
				json.put("actualdateofadmission", DateFormat.FormatToDateString(rslist.getString(2)));
				json.put("actualdateofdischarge", DateFormat.FormatToDateString(rslist.getString(3)));
				json.put("statename", rslist.getString(4) != null ? rslist.getString(4) : "N/A");
				json.put("districtname", rslist.getString(5) != null ? rslist.getString(5) : "N/A");
				json.put("blockname", rslist.getString(6) != null ? rslist.getString(6) : "N/A");
				json.put("villagename", rslist.getString(7) != null ? rslist.getString(7) : "N/A");
				json.put("hospitalname", rslist.getString(8) != null ? rslist.getString(8) : "N/A");
				json.put("patientname", rslist.getString(9) != null ? rslist.getString(9) : "N/A");
				json.put("gender", rslist.getString(10) != null ? rslist.getString(10) : "N/A");
				json.put("age", rslist.getString(11) != null ? rslist.getString(11) : "N/A");
				json.put("procedurename", rslist.getString(12) != null ? rslist.getString(12) : "N/A");
				json.put("packagename", rslist.getString(13) != null ? rslist.getString(13) : "N/A");
				json.put("noofdays", rslist.getString(14) != null ? rslist.getString(14) : "N/A");
				json.put("invoiceno", rslist.getString(15) != null ? rslist.getString(15) : "N/A");
				json.put("totalamountblocked", rslist.getString(16) != null ? rslist.getString(16) : "N/A");
				json.put("hospitaladdress", rslist.getString(17) != null ? rslist.getString(17) : "N/A");
				json.put("hospitalcode", rslist.getString(18) != null ? rslist.getString(18) : "N/A");
				json.put("cost", rslist.getString(19) != null ? rslist.getString(19) : "N/A");
				json.put("claimid", rslist.getString(20) != null ? rslist.getString(20) : "N/A");
				json.put("remark_id", rslist.getString(21) != null ? rslist.getString(21) : "N/A");
				json.put("remarks", rslist.getString(22) != null ? rslist.getString(22) : "N/A");
				json.put("packagecode", rslist.getString(23) != null ? rslist.getString(23) : "N/A");
				json.put("familyheadname", rslist.getString(24) != null ? rslist.getString(24) : "N/A");
				json.put("verifiername", rslist.getString(25) != null ? rslist.getString(25) : "N/A");
				json.put("dateofadmission", DateFormat.FormatToDateString(rslist.getString(26)));
				json.put("dateofdischarge", DateFormat.FormatToDateString(rslist.getString(27)));
				json.put("mortality", rslist.getString(28) != null ? rslist.getString(28) : "N/A");
				json.put("referralcode", rslist.getString(29) != null ? rslist.getString(29) : "N/A");
				json.put("authorizedcode", rslist.getString(30) != null ? rslist.getString(30) : "N/A");
				json.put("districtname", rslist.getString(31) != null ? rslist.getString(31) : "N/A");
				json.put("nabhflag", rslist.getString(32) != null ? rslist.getString(32) : "N/A");
				json.put("Address", rslist.getString(33) != null ? rslist.getString(33) : "N/A");
				json.put("statusflag", rslist.getString(34) != null ? rslist.getString(34) : "N/A");
				json.put("claim_case_no", rslist.getString(35) != null ? rslist.getString(35) : "N/A");
				json.put("claim_bill_no", rslist.getString(36) != null ? rslist.getString(36) : "N/A");
				json.put("implant_data", rslist.getString(37) != null ? rslist.getString(37) : "N/A");
				json.put("claim_no", rslist.getString(38) != null ? rslist.getString(38) : "N/A");
				json.put("PATIENTPHONENO", rslist.getString(39) != null ? rslist.getString(39) : "N/A");
				json.put("cpd_mortality", rslist.getString(40) != null ? rslist.getString(40) : "N/A");
				json.put("verification", rslist.getString(41) != null ? rslist.getString(41) : "N/A");
				json.put("ispatient", rslist.getString(42) != null ? rslist.getString(42) : "N/A");
				json.put("Referalstatus", rslist.getString(43) != null ? rslist.getString(43) : "N/A");
				json.put("totalamountclaimed", rslist.getString(44) != null ? rslist.getString(44) : "N/A");
				json.put("claim_case_no", rslist.getString(45) != null ? rslist.getString(45) : "N/A");
				json.put("caseno", rslist.getString(46) != null ? rslist.getString(46) : "N/A");
				json.put("packagerate", rslist.getString(47) != null ? rslist.getString(47) : "N/A");
				json.put("floatno", rslist.getString(48) != null ? rslist.getString(48) : "N/A");
				json.put("floatid", rslist.getString(49) != null ? rslist.getString(49) : "N/A");
				json.put("cpdapprovedamount", rslist.getString(50) != null ? rslist.getString(50) : "N/A");
				json.put("snaapprovedamount", rslist.getString(51) != null ? rslist.getString(51) : "N/A");
				json.put("DISCHARGESLIP", rslist.getString(52) != null ? rslist.getString(52) : "N/A");
				json.put("ADITIONALDOCS", rslist.getString(53) != null ? rslist.getString(53) : "N/A");
				json.put("ADITIONAL_DOC1", rslist.getString(54) != null ? rslist.getString(54) : "N/A");
				json.put("ADITIONAL_DOC2", rslist.getString(55) != null ? rslist.getString(55) : "N/A");
				json.put("mortalityauditreport", rslist.getString(56) != null ? rslist.getString(56) : "N/A");
				json.put("mortalityDoc", rslist.getString(57) != null ? rslist.getString(57) : "N/A");
				json.put("PRESURGERYPHOTO", rslist.getString(58) != null ? rslist.getString(58) : "N/A");
				json.put("POSTSURGERYPHOTO", rslist.getString(59) != null ? rslist.getString(59) : "N/A");
				json.put("PATIENT_PHOTO", rslist.getString(60) != null ? rslist.getString(60) : "N/A");
				json.put("INTRA_SURGERY_PHOTO", rslist.getString(61) != null ? rslist.getString(61) : "N/A");
				json.put("SPECIMEN_REMOVAL_PHOTO", rslist.getString(62) != null ? rslist.getString(62) : "N/A");
				floatlog.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rslist != null) {
					rslist.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return floatlog.toString();
	}

	@Override
	public String actiondetailsforsna(Long groupId, String statecode, String districtcode, String hospitalcode,
			Date fromDate, Date toDate, Long userId) {
		JSONArray actinlog = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_ACTN_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_remarkid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalcoderemark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalcode.trim());
			storedProcedureQuery.setParameter("p_statecode", statecode.trim());
			storedProcedureQuery.setParameter("p_districtcode", districtcode.trim());
			storedProcedureQuery.setParameter("P_ACTION_CODE", 1);
			storedProcedureQuery.setParameter("P_remarkid", null);
			storedProcedureQuery.setParameter("P_hospitalcoderemark", null);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("Hospitalcode", rs.getString(1) != null ? rs.getString(1) : "N/A");
				json.put("hospitalname", rs.getString(2) != null ? rs.getString(2) : "N/A");
				json.put("totaldischarge", rs.getString(3) != null ? rs.getString(3) : "0");
				json.put("claimsubmited", rs.getString(4) != null ? rs.getString(4) : "0");
				json.put("claimnotsubmitted", rs.getString(5) != null ? rs.getString(5) : "0");
				json.put("snaapproved", rs.getString(6) != null ? rs.getString(6) : "0");
				json.put("actionbysna", rs.getString(7) != null ? rs.getString(7) : "0");
				json.put("remarkwisedetails", rs.getString(8) != null ? rs.getString(8) : "0");
				json.put("snaaction", rs.getString(9) != null ? rs.getString(9) : "0");
				json.put("snaactionpercentage", rs.getString(10) != null ? rs.getString(10) : "0");
				json.put("total", rs.getString(11) != null ? rs.getString(11) : "0");
				actinlog.put(json);
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
		return actinlog.toString();
	}

	@Override
	public String getactionremarkdetailssforsna(Long snaid, Date fromdate, Date todate, String hospitalcode,
			String stateode, String distcode) {
		JSONArray remarkdetailslog = new JSONArray();
		ResultSet result = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_ACTN_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_remarkid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalcoderemark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", snaid);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalcode.trim());
			storedProcedureQuery.setParameter("p_statecode", stateode.trim());
			storedProcedureQuery.setParameter("p_districtcode", distcode.trim());
			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_remarkid", null);
			storedProcedureQuery.setParameter("P_hospitalcoderemark", null);
			storedProcedureQuery.execute();
			result = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (result.next()) {
				JSONObject json = new JSONObject();
				json.put("remarkid", result.getString(1) != null ? result.getString(1) : "N/A");
				json.put("remarkname", result.getString(2) != null ? result.getString(2) : "N/A");
				json.put("remarkcount", result.getString(3) != null ? result.getString(3) : "0");
				remarkdetailslog.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null) {
					result.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return remarkdetailslog.toString();
	}

	@Override
	public String getcountremarkdetailsforsnaaction(Long userid, Date fromdate, Date todate, String statecode,
			String districtcode, String hospitalcode, Long remarkid, String hospitalcodeforremark) {
		JSONArray countlogforsnaaction = new JSONArray();
		ResultSet result = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_SNA_ACTN_RPT")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_remarkid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalcoderemark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalcode.trim());
			storedProcedureQuery.setParameter("p_statecode", statecode.trim());
			storedProcedureQuery.setParameter("p_districtcode", districtcode.trim());
			storedProcedureQuery.setParameter("P_ACTION_CODE", 3);
			storedProcedureQuery.setParameter("P_remarkid", remarkid);
			storedProcedureQuery.setParameter("P_hospitalcoderemark", hospitalcodeforremark.trim());
			storedProcedureQuery.execute();
			result = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (result.next()) {
				JSONObject json = new JSONObject();
				json.put("remarkid", result.getString(1) != null ? result.getString(1) : "N/A");
				json.put("patientname", result.getString(2) != null ? result.getString(2) : "N/A");
				json.put("urn", result.getString(3) != null ? result.getString(3) : "N/A");
				json.put("claimno", result.getString(4) != null ? result.getString(4) : "N/A");
				json.put("hospitalname", result.getString(5) != null ? result.getString(5) : "N/A");
				json.put("hospitalcode", result.getString(6) != null ? result.getString(6) : "N/A");
				json.put("actualdateofadmission", DateFormat.FormatToDateString(result.getString(7)));
				json.put("actualdateofdischarge", DateFormat.FormatToDateString(result.getString(8)));
				json.put("transactiondetailsid", result.getString(9) != null ? result.getString(9) : "N/A");
				json.put("claimid", result.getString(10) != null ? result.getString(10) : "N/A");
				countlogforsnaaction.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null) {
					result.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return countlogforsnaaction.toString();
	}

	@Override
	public String getAllHistoryAgainstClaimnumber(String claimno) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CMS_CLNO_HSTRY")
					.registerStoredProcedureParameter("p_CLAIMNUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_CLAIMNUMBER", claimno.trim());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");
			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("claim_no", rs.getString(1) != null ? rs.getString(1) : "N/A");
				jsonObject.put("hospitalcode", rs.getString(2) != null ? rs.getString(2) : "N/A");
				jsonObject.put("actionby", rs.getString(3) != null ? rs.getString(3) : "N/A");
				jsonObject.put("fullname", rs.getString(4) != null ? rs.getString(4) : "N/A");
				jsonObject.put("actiontype", rs.getString(5) != null ? rs.getString(5) : "N/A");
				jsonObject.put("actionon", rs.getString(6) != null ? rs.getString(6) : "N/A");
				jsonObject.put("hospitalname", rs.getString(7) != null ? rs.getString(7) : "N/A");
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
	public Map<String, Object> getTriggerDetails(MeTriggerDetailsBean triggerDetails) throws Exception {
//		List<MeTriggerDetails> triggerList = new ArrayList<>();
//		MeTriggerDetails triggerObject = null;
		String triggerNameString = null;
//		try {
//			if (triggerDetails.getSlNo() == 1) {
//				trigger = triggerRepo.findByPhoneNoAndSlNo(triggerDetails.getPhoneNo(), triggerDetails.getSlNo());
//			} else if (triggerDetails.getSlNo() == 2) {
//				trigger = triggerRepo.findByUrnAndSlNo(triggerDetails.getUrn(), triggerDetails.getSlNo());
//			} else if (triggerDetails.getSlNo() == 9) {
//				trigger = triggerRepo.findByDoctorRegNoAndSurgeryDate(triggerDetails.getDoctorRegNo(),
//						triggerDetails.getSurgeryDate());
//			} else if (triggerDetails.getSlNo() == 11) {
//				trigger = triggerRepo.findByUrnAndCaseNo(triggerDetails.getUrn(), triggerDetails.getCaseNo());
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//		return trigger;
		try {
			if (triggerDetails.getSlNo() == 1) {
				triggerNameString = "No of Mobile Duplicate During Blocking In Different Urn";
			} else if (triggerDetails.getSlNo() == 2) {
				triggerNameString = "No of Mobile Duplicate During Blocking In Same Urn";
			} else if (triggerDetails.getSlNo() == 3) {
				triggerNameString = "Male Patinet Treatment Under Gynacelogy";
			} else if (triggerDetails.getSlNo() == 4) {
				triggerNameString = "Haemodialysis treated more than 13 times";
			} else if (triggerDetails.getSlNo() == 5) {
				triggerNameString = "Caesarean Delivery below 18 years";
			} else if (triggerDetails.getSlNo() == 6) {
				triggerNameString = "URN Getting Treatment On Age 0";
			} else if (triggerDetails.getSlNo() == 7) {
				triggerNameString = "URN Getting Treatment On Age 100 Or Above";
			} else if (triggerDetails.getSlNo() == 8) {
				triggerNameString = "Same patient in multiple hospitals during the same period";
			} else if (triggerDetails.getSlNo() == 9) {
				triggerNameString = "Multiple beneficiaries treated by the same doctor at multiple Locations during the same period\r\n"
						+ "";
			} else if (triggerDetails.getSlNo() == 10) {
				triggerNameString = "Death Cases";
			} else if (triggerDetails.getSlNo() == 11) {
				triggerNameString = "UnBundling Packages Cases";
			} else if (triggerDetails.getSlNo() == 12) {
				triggerNameString = " Cataract surgery less than equal to 40 years";
			} else if (triggerDetails.getSlNo() == 13) {
				triggerNameString = "Hysterectomy less than 40 years";
			} else {
				triggerNameString = "Out of Pocket Expenditure ";
			}
		} catch (Exception e) {
			throw e;
		}
		ResultSet triggerResultSet = null;
		List<String> triggerheader = new ArrayList<String>();
		List<List<String>> record = new ArrayList<List<String>>();
		Map<String, Object> list = new HashMap<>();
		List<String> triggerdata;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ME_TRIGGER_DETAILS_NEW")
					.registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SLNO", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CASENO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENTNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PHONENO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGENAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTUALDATEOFADMISSION", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTUALDATEOFDISCHARGE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCLAIMAMOUNT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REPORT_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TXNPACKAGEDETAILSID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATED_ON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS_FLAG", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCTOR_REGNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SURGERY_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ID", null);
			storedProcedureQuery.setParameter("P_SLNO", triggerDetails.getSlNo());
			storedProcedureQuery.setParameter("P_URN", triggerDetails.getUrn());
			storedProcedureQuery.setParameter("P_CLAIMNO", triggerDetails.getClaimNo());
			storedProcedureQuery.setParameter("P_CASENO", triggerDetails.getCaseNo());
			storedProcedureQuery.setParameter("P_PATIENTNAME", triggerDetails.getPatientName());
			storedProcedureQuery.setParameter("P_PHONENO", triggerDetails.getPhoneNo());
			storedProcedureQuery.setParameter("P_HOSPITALNAME", triggerDetails.getHospitalName());
			storedProcedureQuery.setParameter("P_HOSPITALCODE", triggerDetails.getHospitalCode());
			storedProcedureQuery.setParameter("P_PACKAGECODE", triggerDetails.getPackageCode());
			storedProcedureQuery.setParameter("P_PACKAGENAME", triggerDetails.getPackageName());
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
			storedProcedureQuery.setParameter("P_ACTUALDATEOFADMISSION",
					dateFormat1.parse(triggerDetails.getActualDateOfAdmission()));
			storedProcedureQuery.setParameter("P_ACTUALDATEOFDISCHARGE",
					dateFormat2.parse(triggerDetails.getActualDateOfDischarge()));
			storedProcedureQuery.setParameter("P_HOSPITALCLAIMAMOUNT", triggerDetails.getClaimAmount());
			storedProcedureQuery.setParameter("P_REPORT_NAME", triggerDetails.getReportName());
			storedProcedureQuery.setParameter("P_CLAIMID", triggerDetails.getClaimId());
			storedProcedureQuery.setParameter("P_TRANSACTIONDETAILSID", triggerDetails.getTrnsactionDetailsId());
			storedProcedureQuery.setParameter("P_TXNPACKAGEDETAILSID", triggerDetails.getTxnPkgDetailsId());
			storedProcedureQuery.setParameter("P_CREATED_ON", triggerDetails.getCreatedOn());
			storedProcedureQuery.setParameter("P_STATUS_FLAG", triggerDetails.getStatusFlag());
			storedProcedureQuery.setParameter("P_DOCTOR_REGNO", triggerDetails.getDoctorRegNo());
			storedProcedureQuery.setParameter("P_SURGERY_DATE", triggerDetails.getSurgeryDate());
			storedProcedureQuery.execute();
			triggerResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			ResultSetMetaData rsMetaData = triggerResultSet.getMetaData();
			int count = rsMetaData.getColumnCount();
			triggerheader.add("SL No.");
			for (int i = 1; i <= count; i++) {
				triggerheader.add(rsMetaData.getColumnName(i));
			}
			Integer i = 0;
			while (triggerResultSet.next()) {
				triggerdata = new ArrayList<String>();
				if (triggerDetails.getSlNo() == 1) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(9)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(10)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null ? triggerResultSet.getString(11) : "N/A");
				} else if (triggerDetails.getSlNo() == 2) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(10)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null ? triggerResultSet.getString(12) : "N/A");

				} else if (triggerDetails.getSlNo() == 3) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(10)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null ? triggerResultSet.getString(12) : "N/A");

				} else if (triggerDetails.getSlNo() == 4) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(10)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null ? triggerResultSet.getString(12) : "N/A");

				} else if (triggerDetails.getSlNo() == 5) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null ? triggerResultSet.getString(10) : "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(12)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null ? triggerResultSet.getString(13) : "N/A");

				} else if (triggerDetails.getSlNo() == 6) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null ? triggerResultSet.getString(10) : "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(12)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null ? triggerResultSet.getString(13) : "N/A");

				} else if (triggerDetails.getSlNo() == 7) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null ? triggerResultSet.getString(10) : "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(12)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null ? triggerResultSet.getString(13) : "N/A");

				} else if (triggerDetails.getSlNo() == 8) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null ? triggerResultSet.getString(10) : "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(12)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null ? triggerResultSet.getString(13) : "N/A");
					triggerdata.add(triggerResultSet.getString(14) != null ? triggerResultSet.getString(14) : "N/A");

				} else if (triggerDetails.getSlNo() == 9) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null ? triggerResultSet.getString(10) : "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null ? triggerResultSet.getString(11) : "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(12)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(13)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(14) != null ? triggerResultSet.getString(14) : "N/A");

				} else if (triggerDetails.getSlNo() == 10) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null ? triggerResultSet.getString(10) : "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(12)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null ? triggerResultSet.getString(13) : "N/A");

				} else if (triggerDetails.getSlNo() == 11) {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null ? triggerResultSet.getString(10) : "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(12)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null ? triggerResultSet.getString(13) : "N/A");
				} else {
					triggerdata.add((++i).toString());
					triggerdata.add(triggerResultSet.getString(1) != null ? triggerResultSet.getString(1) : "N/A");
					triggerdata.add(triggerResultSet.getString(2) != null ? triggerResultSet.getString(2) : "N/A");
					triggerdata.add(triggerResultSet.getString(3) != null ? triggerResultSet.getString(3) : "N/A");
					triggerdata.add(triggerResultSet.getString(4) != null ? triggerResultSet.getString(4) : "N/A");
					triggerdata.add(triggerResultSet.getString(5) != null ? triggerResultSet.getString(5) : "N/A");
					triggerdata.add(triggerResultSet.getString(6) != null ? triggerResultSet.getString(6) : "N/A");
					triggerdata.add(triggerResultSet.getString(7) != null ? triggerResultSet.getString(7) : "N/A");
					triggerdata.add(triggerResultSet.getString(8) != null ? triggerResultSet.getString(8) : "N/A");
					triggerdata.add(triggerResultSet.getString(9) != null ? triggerResultSet.getString(9) : "N/A");
					triggerdata.add(triggerResultSet.getString(10) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(10)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(11) != null
							? sdf.format(sdf1.parse(triggerResultSet.getString(11)))
							: "N/A");
					triggerdata.add(triggerResultSet.getString(12) != null ? triggerResultSet.getString(12) : "N/A");
					triggerdata.add(triggerResultSet.getString(13) != null ? triggerResultSet.getString(13) : "N/A");
					triggerdata.add(triggerResultSet.getString(14) != null ? triggerResultSet.getString(14) : "N/A");
					triggerdata.add(triggerResultSet.getString(15) != null ? triggerResultSet.getString(15) : "N/A");
					triggerdata.add(triggerResultSet.getString(16) != null ? triggerResultSet.getString(16) : "N/A");
					triggerdata.add(triggerResultSet.getString(17) != null ? triggerResultSet.getString(17) : "N/A");
					triggerdata.add(triggerResultSet.getString(18) != null ? triggerResultSet.getString(18) : "N/A");
					triggerdata.add(triggerResultSet.getString(19) != null ? triggerResultSet.getString(19) : "N/A");
					triggerdata.add(triggerResultSet.getString(20) != null ? triggerResultSet.getString(20) : "N/A");
				}
				record.add(triggerdata);
			}
			list.put("header", triggerheader);
			list.put("data", record);
			list.put("ReportName", triggerNameString);
			list.put("status", 200);
		} catch (Exception e) {
			logger.error("Error : " + e);
			list.put("header", null);
			list.put("data", null);
			list.put("ReportName", null);
			list.put("status", 400);
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if (triggerResultSet != null) {
					triggerResultSet.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<Object> getbulkapprovallistofdata(String fromDate, String toDate, Long userId) {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_BULK_APPROVAL_REVERT")
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_from_date", fromDate.trim());
			storedProcedureQuery.setParameter("p_to_date", toDate.trim());
			storedProcedureQuery.setParameter("p_userid", userId);
			storedProcedureQuery.setParameter("p_actioncode", "1");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				Bulkrevertbean records = new Bulkrevertbean();
				records.setBulk_aprv_status(rs.getString(1));
				records.setUrn(rs.getString(2));
				records.setPatientname(rs.getString(3) != null ? rs.getString(3) : "N/A");
				records.setPackagename(rs.getString(4) != null ? rs.getString(4) : "N/A");
				records.setProcedurename(rs.getString(5) != null ? rs.getString(5) : "N/A");
				records.setActualdateofdischarge(DateFormat.FormatToDateString(rs.getString(6)));
				records.setActualdateofadmission(DateFormat.FormatToDateString(rs.getString(7)));
				list.add(records);
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
		return list;
	}

	@Override
	public Response getsubmitbulkapprovalrevertRecord(Bulkrevertbean logBean) {
		Response response = new Response();
		Integer bulksubmit = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLM_BULK_APPROVAL_REVERT")
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actioncode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_from_date", logBean.getFormdate().trim());
			storedProcedureQuery.setParameter("p_to_date", logBean.getTodate().trim());
			storedProcedureQuery.setParameter("p_userid", logBean.getUserid());
			storedProcedureQuery.setParameter("p_actioncode", "2");
			storedProcedureQuery.execute();
			bulksubmit = (Integer) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (bulksubmit == 1) {
				response.setStatus("Success");
				response.setMessage("Bulk Approval Reverted Successfully");
			} else if (bulksubmit == 2) {
				response.setStatus("Failed");
				response.setMessage("Bulk Approval Reverted Failed ");
			}
		} catch (Exception e) {
			logger.error("Error in saveoldClaimDetails of ClaimDetailsDaoImpl :", e);
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String claimProcessedDetails(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray cardBalanceArray = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_sna_processed_dtls")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");

			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				casenumber = snoDetailsObj.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				String mob = snoDetailsObj.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("pendingat", snoDetailsObj.getLong(51));
				jsonObject.put("claimstatus", snoDetailsObj.getLong(52));
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(53));
				jsonObject.put("verification", snoDetailsObj.getString(54));
				jsonObject.put("ispatient", snoDetailsObj.getString(55));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(56));
				jsonObject.put("PackageCode", snoDetailsObj.getString(57));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(58));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(59) != null ? snoDetailsObj.getString(59) : "NA");
				jsonObject.put("packageName1",
						snoDetailsObj.getString(60) != null ? snoDetailsObj.getString(60) : "NA");
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(61) != null ? snoDetailsObj.getString(61) : "NA");
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(62) != null ? snoDetailsObj.getString(62) : "NA");
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(63) != null ? snoDetailsObj.getString(63) : "NA");
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(64) != null ? snoDetailsObj.getString(64) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(65));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(66), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(67));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(68));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(69));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(70));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(71));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(72));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(73));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(74));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(75));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(76));
				jsonObject.put("disverification", snoDetailsObj.getString(77));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "N/A");
				jsonObject.put("uidreferencenumber", snoDetailsObj.getString(79));
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(81) != null ? snoDetailsObj.getString(81) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(82) != null ? snoDetailsObj.getString(82) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(83) != null ? snoDetailsObj.getString(83) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(84));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(85));
				jsonObject.put("categoryName", snoDetailsObj.getString(86));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("ACTIONON1", DateFormat.dateConvertor(snoDetailsObj1.getString(5), ""));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonArray.put(jsonObject1);
				}
				while (snoDetailsObj2.next()) {
					jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
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
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = getMultiplePackageBlockingthroughcaseno(casenumber, actualDate);
				cardBalanceArray = getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceArray);
			}
		} catch (Exception e) {
			logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
				if (snoDetailsObj2 != null) {
					snoDetailsObj2.close();
				}
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();
			} catch (Exception e2) {
				logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e2);
			}
		}
		return details.toString();
	}

	@Override
	public Response saveClaimProcessedDetails(ClaimLogBean logBean) throws Exception {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : logBean.getIcdFinalData()) {
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
			e1.printStackTrace();
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_sna_claim_processed_action")
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
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_MORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", logBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", logBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", logBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", logBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", logBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", logBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", logBean.getIntrasurgery());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", logBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.setParameter("P_SNA_MORTALITY", logBean.getSnamortality());
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
	public String getURNWiseDetails(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONArray cardBalanceArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_urn_wise_dtls")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");

			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				casenumber = snoDetailsObj.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				String mob = snoDetailsObj.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("pendingat", snoDetailsObj.getLong(51));
				jsonObject.put("claimstatus", snoDetailsObj.getLong(52));
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(53));
				jsonObject.put("verification", snoDetailsObj.getString(54));
				jsonObject.put("ispatient", snoDetailsObj.getString(55));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(56));
				jsonObject.put("PackageCode", snoDetailsObj.getString(57));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(58));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(59) != null ? snoDetailsObj.getString(59) : "NA");
				jsonObject.put("packageName1",
						snoDetailsObj.getString(60) != null ? snoDetailsObj.getString(60) : "NA");
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(61) != null ? snoDetailsObj.getString(61) : "NA");
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(62) != null ? snoDetailsObj.getString(62) : "NA");
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(63) != null ? snoDetailsObj.getString(63) : "NA");
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(64) != null ? snoDetailsObj.getString(64) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(65));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(66), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(67));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(68));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(69));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(70));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(71));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(72));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(73));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(74));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(75));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(76));
				jsonObject.put("disverification", snoDetailsObj.getString(77));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "N/A");
				jsonObject.put("uidreferencenumber", snoDetailsObj.getString(79));
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(81) != null ? snoDetailsObj.getString(81) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(82) != null ? snoDetailsObj.getString(82) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(83) != null ? snoDetailsObj.getString(83) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(84));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(85));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("ACTIONON1", DateFormat.dateConvertor(snoDetailsObj1.getString(5), ""));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonArray.put(jsonObject1);
				}
				while (snoDetailsObj2.next()) {
					jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
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
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = getMultiplePackageBlockingthroughcaseno(casenumber, actualDate);
				cardBalanceArray = getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceArray);
			}
		} catch (Exception e) {
			logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
				if (snoDetailsObj2 != null) {
					snoDetailsObj2.close();
				}
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();
			} catch (Exception e2) {
				logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e2);
			}
		}
		return details.toString();
	}

	@Override
	public Response saveUrnWiseDetails(ClaimLogBean logBean) throws Exception {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : logBean.getIcdFinalData()) {
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
			e1.printStackTrace();
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_sna_urn_wise_action")
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
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_MORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", logBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", logBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", logBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", logBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", logBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", logBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", logBean.getIntrasurgery());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", logBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.setParameter("P_SNA_MORTALITY", logBean.getSnamortality());
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
	public String getSystemAdminRejectedDetails(Integer txnId) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();
		JSONArray ictDetailsArray = new JSONArray();
		JSONArray ictSubDetailsArray = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONArray cardBalanceDetails = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject ictDetailsObject = null;
		JSONObject ictSubDetailsObject = null;
		JSONObject details = new JSONObject();
		String urn = null;
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet ictDetails = null;
		ResultSet ictSubDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_system_admin_rej_dtls")
					.registerStoredProcedureParameter("cid", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_details", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_icd_subdetails", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("cid", txnId);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			ictDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_details");
			ictSubDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_icd_subdetails");

			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				casenumber = snoDetailsObj.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				String mob = snoDetailsObj.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("pendingat", snoDetailsObj.getLong(51));
				jsonObject.put("claimstatus", snoDetailsObj.getLong(52));
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(53));
				jsonObject.put("verification", snoDetailsObj.getString(54));
				jsonObject.put("ispatient", snoDetailsObj.getString(55));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(56));
				jsonObject.put("PackageCode", snoDetailsObj.getString(57));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(58));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(59) != null ? snoDetailsObj.getString(59) : "NA");
				jsonObject.put("packageName1",
						snoDetailsObj.getString(60) != null ? snoDetailsObj.getString(60) : "NA");
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(61) != null ? snoDetailsObj.getString(61) : "NA");
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(62) != null ? snoDetailsObj.getString(62) : "NA");
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(63) != null ? snoDetailsObj.getString(63) : "NA");
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(64) != null ? snoDetailsObj.getString(64) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(65));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(66), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(67));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(68));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(69));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(70));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(71));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(72));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(73));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(74));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(75));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(76));
				jsonObject.put("disverification", snoDetailsObj.getString(77));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "N/A");
				jsonObject.put("uidreferencenumber", snoDetailsObj.getString(79));
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(81) != null ? snoDetailsObj.getString(81) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(82) != null ? snoDetailsObj.getString(82) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(83) != null ? snoDetailsObj.getString(83) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(84));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(85));
				jsonObject.put("categoryName", snoDetailsObj.getString(86));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("ACTIONON1", DateFormat.dateConvertor(snoDetailsObj1.getString(5), ""));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonArray.put(jsonObject1);
				}
				while (snoDetailsObj2.next()) {
					jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
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
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = getMultiplePackageBlockingthroughcaseno(casenumber, actualDate);
				cardBalanceDetails = getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("ictDetailsArray", ictDetailsArray);
				details.put("ictSubDetailsArray", ictSubDetailsArray);
				details.put("cardBalanceArray", cardBalanceDetails);
			}
		} catch (Exception e) {
			logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
				if (snoDetailsObj2 != null) {
					snoDetailsObj2.close();
				}
				if (ictDetails != null)
					ictDetails.close();
				if (ictSubDetails != null)
					ictSubDetails.close();
			} catch (Exception e2) {
				logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e2);
			}
		}
		return details.toString();
	}

	@Override
	public Response saveSyatemAdminSnaRejectedDetails(ClaimLogBean logBean) throws Exception {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : logBean.getIcdFinalData()) {
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
			e1.printStackTrace();
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_system_admin_rej_action")
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
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", logBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", logBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", logBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", logBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", logBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", logBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", logBean.getIntrasurgery());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", logBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
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
	public Response saveClaimReApprovalAction(ClaimLogBean logBean) throws Exception {
		Response response = new Response();
		InetAddress localhost;
		String getuseripaddressString = null;
		String detailsICD = null;
		String subDetailsICD = null;
		List<Object> icdData = new ArrayList<Object>();
		List<Object> subListData = new ArrayList<Object>();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			for (ICDDetailsBean details : logBean.getIcdFinalData()) {
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
			e1.printStackTrace();
		}
		Integer claimsnoInteger = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_claim_sna_re_apr_action")
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
					.registerStoredProcedureParameter("P_CLAIMAMOUNT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGE_SLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UPDATEDON", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PATIENT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SPECIMEN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INTRASURGERY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IS_ICDMODIFIED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_details_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_icd_subdetails_json", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNA_MORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_CLAIMID", logBean.getClaimId());
			storedProcedureQuery.setParameter("P_PENDINGAT", logBean.getPendingAt());
			storedProcedureQuery.setParameter("P_CLAIMSTATUS", logBean.getClaimStatus());
			storedProcedureQuery.setParameter("P_AMOUNT", logBean.getAmount());
			storedProcedureQuery.setParameter("P_REMARKS", logBean.getRemarks());
			storedProcedureQuery.setParameter("P_ACTIONREMARKID", logBean.getActionRemarksId());
			storedProcedureQuery.setParameter("P_ACTIONREMARKS", logBean.getActionRemark());
			storedProcedureQuery.setParameter("P_URN", logBean.getUrnNo());
			storedProcedureQuery.setParameter("P_USERID", logBean.getUserId());
			storedProcedureQuery.setParameter("P_CREATEDON", new Date());
			storedProcedureQuery.setParameter("P_CLAIMAMOUNT", logBean.getClaimAmount());
			storedProcedureQuery.setParameter("P_DISCHARGE_SLIP", logBean.getDischargeslip());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC", logBean.getAdditionaldocs());
			storedProcedureQuery.setParameter("P_PRESURGERYPHOTO", logBean.getPresurgeryphoto());
			storedProcedureQuery.setParameter("P_POSTSURGERYPHOTO", logBean.getPostsurgeryphoto());
			storedProcedureQuery.setParameter("p_USER_IP", getuseripaddressString);
			storedProcedureQuery.setParameter("P_UPDATEDBY", logBean.getUserId());
			storedProcedureQuery.setParameter("P_UPDATEDON", new Date());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", logBean.getAdditionaldoc1());
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", logBean.getAdditionaldoc2());
			storedProcedureQuery.setParameter("P_STATUSFLAG", logBean.getStatusflag());
			storedProcedureQuery.setParameter("P_PATIENT", logBean.getPatientpic());
			storedProcedureQuery.setParameter("P_SPECIMEN", logBean.getSpecimenpic());
			storedProcedureQuery.setParameter("P_INTRASURGERY", logBean.getIntrasurgery());
			storedProcedureQuery.setParameter("P_IS_ICDMODIFIED", logBean.getIcdFlag());
			storedProcedureQuery.setParameter("p_icd_details_json", detailsICD);
			storedProcedureQuery.setParameter("p_icd_subdetails_json", subDetailsICD);
			storedProcedureQuery.setParameter("P_SNA_MORTALITY", logBean.getSnamortality());
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
	public Map<String, Object> cpdApprovalCount(CPDApproveRequestBean requestBean) {
		Map<String, Object> snoclaimRaiseDetailsMap = new HashMap<String, Object>();
		JSONObject jsonObject = null;
		ResultSet countDtls = null;
		Integer schemecatId = null;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemecatId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CPD_APRV_LIST_COUNT")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HSPTL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_FLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remarks", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_amount", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AUTH_MODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IMPLANT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HIGH_END_DRUGS_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_WARDNAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FILTER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SEARCHTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGERTYPE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", requestBean.getUserId());
			storedProcedureQuery.setParameter("P_FROM_DATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TO_DATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_STATE_CODE", requestBean.getStateCode());
			storedProcedureQuery.setParameter("P_DIST_CODE", requestBean.getDistCode());
			storedProcedureQuery.setParameter("P_HSPTL_CODE", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_CPD_FLAG", requestBean.getCpdFlag());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_remarks", requestBean.getDescription());
			storedProcedureQuery.setParameter("p_amount", requestBean.getAmountFlag());
			storedProcedureQuery.setParameter("P_AUTH_MODE", requestBean.getAuthMode());
			storedProcedureQuery.setParameter("P_PROCEDURE_CODE", requestBean.getProcedure());
			storedProcedureQuery.setParameter("P_PACKAGE_CODE", requestBean.getPackages());
			storedProcedureQuery.setParameter("P_IMPLANT_CODE", requestBean.getImplant());
			storedProcedureQuery.setParameter("P_HIGH_END_DRUGS_CODE", requestBean.getHighend());
			storedProcedureQuery.setParameter("P_WARDNAME", requestBean.getWard());
			storedProcedureQuery.setParameter("P_FILTER", requestBean.getFilter());
			storedProcedureQuery.setParameter("P_SEARCHTYPE", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_TRIGGERTYPE", requestBean.getTrigger());
			storedProcedureQuery.setParameter("P_SCHEME_ID", requestBean.getSchemeid());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.setParameter("P_CPD_USERID", requestBean.getCpdUserId());
			storedProcedureQuery.execute();
			countDtls = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
			while (countDtls.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("totalcpdapproved", countDtls.getLong(1));
				jsonObject.put("snaappofcpdapp", countDtls.getLong(3));
				jsonObject.put("snaqryofcpdapp", countDtls.getLong(2));
				jsonObject.put("percent", countDtls.getLong(4));
			}
			snoclaimRaiseDetailsMap.put("count", jsonObject.toString());
		} catch (Exception e) {
			logger.error("Exception Occurred in getSnoClaimRaiseDetailsList() of SnoClaimRaiseDetailsDaoImpl:", e);
			e.printStackTrace();
		} finally {
			try {
				if (countDtls != null) {
					countDtls.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getSnoClaimRaiseDetailsList() of SnoClaimRaiseDetailsDaoImpl:", e2);
			}
		}
		return snoclaimRaiseDetailsMap;

	}

	public JSONArray getCardBalanceDetails(Integer transactionDetailsId, String urn) throws ParseException {
		JSONArray cardBalanceArray = new JSONArray();
		JSONObject cardBalanceObject = null;
		ResultSet cardBalanceDetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_CARDBALANCEDETAILS")
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_URN", urn);
			storedProcedureQuery.setParameter("P_CID", transactionDetailsId);
			storedProcedureQuery.execute();
			cardBalanceDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (cardBalanceDetails.next()) {
				cardBalanceObject = new JSONObject();
				cardBalanceObject.put("isAvailableBalance", cardBalanceDetails.getString(1));
				cardBalanceObject.put("amountBlocked", cardBalanceDetails.getString(2));
				cardBalanceObject.put("claimAmount", cardBalanceDetails.getString(3));
				cardBalanceObject.put("femailFund", cardBalanceDetails.getString(4));
				cardBalanceObject.put("policyStartDate", cardBalanceDetails.getString(5));
				cardBalanceObject.put("policyEndDate", cardBalanceDetails.getString(6));
				cardBalanceArray.put(cardBalanceObject);
			}

		} catch (Exception e) {
			logger.error("Error in getCardBalanceDetails method of SnoClaimRaiseDetailsDaoImpl", e);
		} finally {
			try {
				if (cardBalanceDetails != null) {
					cardBalanceDetails.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getCardBalanceDetails() of SnoClaimRaiseDetailsDaoImpl:", e2);
			}
		}
		return cardBalanceArray;
	}

	@Override
	public String getSpecialtyReuquest(CPDApproveRequestBean requestBean) {
		JSONArray requestList = new JSONArray();
		JSONObject requestObj = null;
		ResultSet snoDetailsObj = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOS_SPECIALITY_APRV_VIEW")
					.registerStoredProcedureParameter("P_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REQUESTID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", requestBean.getFlag());
			storedProcedureQuery.setParameter("P_USERID", util.getCurrentUser());
			storedProcedureQuery.setParameter("P_STATUS", requestBean.getStatus());
			storedProcedureQuery.setParameter("P_FROMDATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TODATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_REQUESTID", null);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (snoDetailsObj.next()) {
				requestObj = new JSONObject();
				requestObj.put("requestId", snoDetailsObj.getLong(1));
				requestObj.put("hospitalName", snoDetailsObj.getString(2));
				requestObj.put("hospitalCode", snoDetailsObj.getString(3));
				requestObj.put("urn", snoDetailsObj.getString(4));
				requestObj.put("patientName", snoDetailsObj.getString(5));
				requestObj.put("age", snoDetailsObj.getLong(6));
				requestObj.put("gender", snoDetailsObj.getString(7));
				requestObj.put("requestDateTime", snoDetailsObj.getString(8));
				requestObj.put("status", snoDetailsObj.getString(9));
				requestObj.put("hospitalRemark", snoDetailsObj.getString(10));
				requestObj.put("patientPhoto", snoDetailsObj.getString(11));
				requestObj.put("clinicalDoc", snoDetailsObj.getString(12));
				requestObj.put("docDate", snoDetailsObj.getString(13));
				requestObj.put("description", snoDetailsObj.getString(16));
				requestList.put(requestObj);
			}

		} catch (Exception e) {
			logger.error("Exception Occurred in getSpecialtyReuquest() of SnoClaimRaiseDetailsDaoImpl:", e);
			e.printStackTrace();
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception Occurred in getSpecialtyReuquest() of SnoClaimRaiseDetailsDaoImpl:", e2);
			}
		}
		return requestList.toString();
	}

	@Override
	public String getSpecialtyReuquestDetails(Long requestId) throws Exception {
		JSONArray patientHistoryArray = new JSONArray();
		JSONArray onGoingPatntTrtmtArray = new JSONArray();
		JSONArray urnHistoryArray = new JSONArray();
		JSONArray onGoingUrnTrtmtArray = new JSONArray();
		JSONArray actionTakenLogArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject onGoingUrnTrtmtObject = null;
		JSONObject actionTakenLogObject = null;
		JSONObject details = new JSONObject();
		ResultSet requestDetails = null;
		ResultSet patientHistory = null;
		ResultSet onGoingPatntTrtmt = null;
		ResultSet urnHistory = null;
		ResultSet onGoingUrnTrtmt = null;
		ResultSet actionTakenLog = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_HOS_SPECIALITY_APRV_VIEW_DTLS")
					.registerStoredProcedureParameter("P_REQUESTID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_PATIENT_HSTY", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ONGOING_PATIENT_TRTMT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_URN_HSTY", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ONGOING_URN_TRTMT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ACTION_TAKEN_LOG", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_REQUESTID", requestId);
			storedProcedureQuery.execute();
			requestDetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			patientHistory = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_PATIENT_HSTY");
			onGoingPatntTrtmt = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ONGOING_PATIENT_TRTMT");
			urnHistory = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_URN_HSTY");
			onGoingUrnTrtmt = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ONGOING_URN_TRTMT");
			actionTakenLog = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ACTION_TAKEN_LOG");
			if (requestDetails.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("requestId", requestDetails.getLong(1));
				jsonObject.put("urn", requestDetails.getString(2));
				jsonObject.put("patientName", requestDetails.getString(3));
				jsonObject.put("age", requestDetails.getString(4));
				jsonObject.put("gender", requestDetails.getString(5));
				jsonObject.put("uidRef", requestDetails.getString(6));
				jsonObject.put("address", requestDetails.getString(7));
				jsonObject.put("schemeCategoryName", requestDetails.getString(8));
				jsonObject.put("verificationMode", requestDetails.getString(9));
				jsonObject.put("specialityName", requestDetails.getString(10));
				jsonObject.put("specialityCode", requestDetails.getString(11));
				jsonObject.put("hospitalName", requestDetails.getString(12));
				jsonObject.put("hospitalCode", requestDetails.getString(13));
				jsonObject.put("status", requestDetails.getString(14));
				jsonObject.put("requestDateTime", requestDetails.getString(15));
				jsonObject.put("patientPhoto", requestDetails.getString(16));
				jsonObject.put("clinicalDoc", requestDetails.getString(17));
				jsonObject.put("hospitalRemark", requestDetails.getString(18));
				jsonObject.put("docDate", requestDetails.getString(19));
				jsonObject.put("queryCount", requestDetails.getLong(20));
				details.put("requestDetails", jsonObject);
				while (patientHistory.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("urn", patientHistory.getString(1));
					jsonObject1.put("patientName", patientHistory.getString(2));
					jsonObject1.put("hospitalName", patientHistory.getString(3));
					jsonObject1.put("hospitalCode", patientHistory.getString(4));
					jsonObject1.put("caseNo", patientHistory.getString(5));
					jsonObject1.put("packageCode", patientHistory.getString(6));
					jsonObject1.put("packageName", patientHistory.getString(7));
					jsonObject1.put("dateOfAdmission", patientHistory.getString(8));
					jsonObject1.put("dateOfDischarge", patientHistory.getString(9));
					jsonObject1.put("actualDateOfAdmission", patientHistory.getString(10));
					jsonObject1.put("actualDateOfDischarge", patientHistory.getString(11));
					jsonObject1.put("claimAmount", patientHistory.getString(12));
					patientHistoryArray.put(jsonObject1);
				}
				while (onGoingPatntTrtmt.next()) {
					jsonObject2 = new JSONObject();
					jsonObject2.put("urn", onGoingPatntTrtmt.getString(1));
					jsonObject2.put("patientName", onGoingPatntTrtmt.getString(2));
					jsonObject2.put("hospitalName", onGoingPatntTrtmt.getString(3));
					jsonObject2.put("hospitalCode", onGoingPatntTrtmt.getString(4));
					jsonObject2.put("caseNo", onGoingPatntTrtmt.getString(5));
					jsonObject2.put("packageCode", onGoingPatntTrtmt.getString(6));
					jsonObject2.put("packageName", onGoingPatntTrtmt.getString(7));
					jsonObject2.put("dateOfAdmission", onGoingPatntTrtmt.getString(8));
					jsonObject2.put("actualDateOfAdmission", onGoingPatntTrtmt.getString(9));
					jsonObject2.put("amountBlocked", onGoingPatntTrtmt.getString(10));
					onGoingPatntTrtmtArray.put(jsonObject2);
				}
				while (urnHistory.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", urnHistory.getString(1));
					jsonObject3.put("patientName", urnHistory.getString(2));
					jsonObject3.put("age", urnHistory.getString(3));
					jsonObject3.put("gender", urnHistory.getString(4));
					jsonObject3.put("hospitalName", urnHistory.getString(5));
					jsonObject3.put("hospitalCode", urnHistory.getString(6));
					jsonObject3.put("caseNo", urnHistory.getString(7));
					jsonObject3.put("packageCode", urnHistory.getString(8));
					jsonObject3.put("packageName", urnHistory.getString(9));
					jsonObject3.put("dateOfAdmission", urnHistory.getString(10));
					jsonObject3.put("dateOfDischarge", urnHistory.getString(11));
					jsonObject3.put("actualDateOfAdmission", urnHistory.getString(12));
					jsonObject3.put("actualDateOfDischarge", urnHistory.getString(13));
					jsonObject3.put("claimAmount", urnHistory.getString(14));
					urnHistoryArray.put(jsonObject3);
				}
				while (onGoingUrnTrtmt.next()) {
					onGoingUrnTrtmtObject = new JSONObject();
					onGoingUrnTrtmtObject.put("urn", onGoingUrnTrtmt.getString(1));
					onGoingUrnTrtmtObject.put("patientName", onGoingUrnTrtmt.getString(2));
					onGoingUrnTrtmtObject.put("age", onGoingUrnTrtmt.getString(3));
					onGoingUrnTrtmtObject.put("gender", onGoingUrnTrtmt.getString(4));
					onGoingUrnTrtmtObject.put("hospitalName", onGoingUrnTrtmt.getString(5));
					onGoingUrnTrtmtObject.put("hospitalCode", onGoingUrnTrtmt.getString(6));
					onGoingUrnTrtmtObject.put("caseNo", onGoingUrnTrtmt.getString(7));
					onGoingUrnTrtmtObject.put("packageCode", onGoingUrnTrtmt.getString(8));
					onGoingUrnTrtmtObject.put("packageName", onGoingUrnTrtmt.getString(9));
					onGoingUrnTrtmtObject.put("dateOfAdmission", onGoingUrnTrtmt.getString(10));
					onGoingUrnTrtmtObject.put("actualDateOfAdmission", onGoingUrnTrtmt.getString(11));
					onGoingUrnTrtmtObject.put("amountBlocked", onGoingUrnTrtmt.getString(12));
					onGoingUrnTrtmtArray.put(onGoingUrnTrtmtObject);
				}
				while (actionTakenLog.next()) {
					actionTakenLogObject = new JSONObject();
					actionTakenLogObject.put("queryRemark", actionTakenLog.getString(1));
					actionTakenLogObject.put("queryDesc", actionTakenLog.getString(2));
					actionTakenLogObject.put("queryDateTime", actionTakenLog.getString(3));
					actionTakenLogObject.put("queryCompileDate", actionTakenLog.getString(4));
					actionTakenLogObject.put("queryCompilyDoc", actionTakenLog.getString(5));
					actionTakenLogObject.put("queryCompilyremerk", actionTakenLog.getString(6));
//					actionTakenLogObject.put("docDate", actionTakenLog.getString(7));
					actionTakenLogArray.put(actionTakenLogObject);
				}

				details.put("patientHistory", patientHistoryArray);
				details.put("onGoingPatntTrtmt", onGoingPatntTrtmtArray);
				details.put("urnHistory", urnHistoryArray);
				details.put("onGoingUrnTrtmt", onGoingUrnTrtmtArray);
				details.put("actionTakenLog", actionTakenLogArray);
			}
		} catch (Exception e) {
			logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (requestDetails != null) {
					requestDetails.close();
				}
				if (patientHistory != null) {
					patientHistory.close();
				}
				if (onGoingPatntTrtmt != null) {
					onGoingPatntTrtmt.close();
				}
				if (urnHistory != null)
					urnHistory.close();

				if (onGoingUrnTrtmt != null)
					onGoingUrnTrtmt.close();

				if (actionTakenLog != null)
					actionTakenLog.close();
			} catch (Exception e2) {
				logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e2);
			}

		}
		return details.toString();
	}

	@Override
	public String multipackthroughcaseno(String caseno, String date) {
		JSONObject details = new JSONObject();
		JSONArray multipackagecaeno = new JSONArray();
		try {
			multipackagecaeno = getMultiplePackageBlockingthroughcaseno(caseno, date);
			details.put("multipackagecaseno", multipackagecaeno);
		} catch (Exception e) {
			System.out.println(e);
		}
		return details.toString();
	}

	@Override
	public String getfamilyTreatementDetails(String dateofadmission, String memeberid, String urn) {
		JSONObject list = new JSONObject();
		JSONArray familylist = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs2 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_TREATMENT_HISTORY_FOR_URN")
					.registerStoredProcedureParameter("P_DATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEMBERID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_DATE", dateofadmission);
			storedProcedureQuery.setParameter("P_URN", urn.trim());
			storedProcedureQuery.setParameter("P_MEMBERID", memeberid.trim());
			storedProcedureQuery.execute();
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs2.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("hospitalname", rs2.getString(1));
				jsonObject.put("hospitalcode", rs2.getString(2));
				jsonObject.put("urn", rs2.getString(3));
				jsonObject.put("memberid", rs2.getString(4));
				jsonObject.put("membername", rs2.getString(5));
				jsonObject.put("patientgender", rs2.getString(6));
				jsonObject.put("age", rs2.getString(7));
				jsonObject.put("admissionDate", rs2.getString(8));
				jsonObject.put("caseno", rs2.getString(9));
				jsonObject.put("patientcontact", rs2.getString(10));
				jsonObject.put("specialitycode", rs2.getString(11));
				jsonObject.put("specialityName", rs2.getString(12));
				jsonObject.put("packagename", rs2.getString(13));
				jsonObject.put("packagecode", rs2.getString(14));
				jsonObject.put("dateofDischarge", rs2.getString(15));
				jsonObject.put("totalamountclaimed", rs2.getString(16));
				familylist.put(jsonObject);
			}
			list.put("famlylist", familylist);
		} catch (Exception e) {
			logger.error("Error in getfamilyTreatementDetails method of PreAuthDaoImpl class:", e);
		} finally {
			try {
				if (rs2 != null) {
					rs2.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getfamilyTreatementDetails method of PreAuthDaoImpl class:", e2);
			}
		}
		return list.toString();
	}

	@Override
	public String getPatientTreatementDetails(String procedureCode, String uidreferencenumber) {
		JSONObject data = new JSONObject();
		JSONArray patientlist = new JSONArray();
		JSONObject jsonObject1;
		ResultSet rs3 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_TREATMENT_LOG_PROCEDURECODE_WISE")
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UIDREFERENCENUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_PROCEDURECODE", procedureCode.trim());
			storedProcedureQuery.setParameter("P_UIDREFERENCENUMBER", uidreferencenumber);
			storedProcedureQuery.execute();
			rs3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs3.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("hospitalname", rs3.getString(1));
				jsonObject1.put("hospitalcode", rs3.getString(2));
				jsonObject1.put("urn", rs3.getString(3));
				jsonObject1.put("memberid", rs3.getString(4));
				jsonObject1.put("membername", rs3.getString(5));
				jsonObject1.put("patientgender", rs3.getString(6));
				jsonObject1.put("age", rs3.getString(7));
				jsonObject1.put("admissionDate", rs3.getString(8));
				jsonObject1.put("caseno", rs3.getString(9));
				jsonObject1.put("patientcontact", rs3.getString(10));
				jsonObject1.put("specialitycode", rs3.getString(11));
				jsonObject1.put("specialityName", rs3.getString(12));
				jsonObject1.put("packagename", rs3.getString(13));
				jsonObject1.put("packagecode", rs3.getString(14));
				jsonObject1.put("dateofDischarge", rs3.getString(15));
				jsonObject1.put("totalamountclaimed", rs3.getString(16));
				jsonObject1.put("totalamountblocked", rs3.getString(17));
				jsonObject1.put("claimraisestatus", rs3.getString(18));
				jsonObject1.put("claimraisedamount", rs3.getString(19));
				jsonObject1.put("cpdappamount", rs3.getString(20));
				jsonObject1.put("snaappamount", rs3.getString(21));
				jsonObject1.put("claimno", rs3.getString(22));
				patientlist.put(jsonObject1);
			}
			data.put("patientlist", patientlist);
		} catch (Exception e) {
			logger.error("Error in getPatientTreatementDetails method of PreAuthDaoImpl class:", e);
		} finally {
			try {
				if (rs3 != null) {
					rs3.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getPatientTreatementDetails method of PreAuthDaoImpl class:", e2);
			}
		}
		return data.toString();
	}

	@Override
	public String getSnaFloatClaimDetails(String urn, Long claimid, String floatno) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray packageBlock = new JSONArray();
		JSONArray preAuthHist = new JSONArray();
		JSONArray cpdActionLog = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray preAuthLog = new JSONArray();

		JSONArray cardBalanceArray = new JSONArray();
		JSONArray multipackagecaeno = new JSONArray();
		JSONArray floatLogArray = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject floatLogObject;
		JSONObject details = new JSONObject();
		String actualDate = null;
		String authorizedCode = null;
		String hospitalCode = null;
		String casenumber = null;
		Integer txnId = null;
		ResultSet snoDetailsObj = null;
		ResultSet snoDetailsObj1 = null;
		ResultSet snoDetailsObj2 = null;
		ResultSet snoDetailsObj3 = null;
		ResultSet floatLog = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_CLAIM_DETAILS")
					.registerStoredProcedureParameter("P_floatno", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_claimid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_LOG_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("p_VITAL_msgout", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_ME_TRIGGER", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_FLOAT_LOG", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_floatno", floatno);
			storedProcedureQuery.setParameter("P_claimid", claimid);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_LOG_MSGOUT");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_VITAL_msgout");
			snoDetailsObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_ME_TRIGGER");
			floatLog = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_FLOAT_LOG");
			if (snoDetailsObj.next()) {
				hospitalCode = snoDetailsObj.getString(18);
				authorizedCode = snoDetailsObj.getString(38);
				if (authorizedCode != null) {
					authorizedCode = authorizedCode.substring(2);
				}
//				urn = snoDetailsObj.getString(1);
				actualDate = snoDetailsObj.getString(2);
				casenumber = snoDetailsObj.getString(43);
				jsonObject = new JSONObject();
				jsonObject.put("URN", snoDetailsObj.getString(1));
				jsonObject.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
				jsonObject.put("ACTUALDATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(3)));
				jsonObject.put("ACTUALDATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(2), ""));
				jsonObject.put("ACTUALDATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(3), ""));
				jsonObject.put("STATENAME", snoDetailsObj.getString(4));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(5));
				jsonObject.put("BLOCKNAME", snoDetailsObj.getString(6));
				jsonObject.put("VILLAGENAME", snoDetailsObj.getString(7));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(8));
				jsonObject.put("PATIENTNAME", snoDetailsObj.getString(9));
				jsonObject.put("GENDER", snoDetailsObj.getString(10));
				jsonObject.put("AGE", snoDetailsObj.getString(11));
				jsonObject.put("PROCEDURENAME", snoDetailsObj.getString(12));
				jsonObject.put("PACKAGENAME", snoDetailsObj.getString(13));
				jsonObject.put("NOOFDAYS",
						CommonFileUpload.calculateNoOfDays(snoDetailsObj.getString(34), snoDetailsObj.getString(35)));
				jsonObject.put("INVOICENO", snoDetailsObj.getString(15));
				jsonObject.put("TOTALAMOUNTCLAIMED", snoDetailsObj.getString(16));
				jsonObject.put("HOSPITALADDRESS", snoDetailsObj.getString(17));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(18));
				jsonObject.put("PRESURGERYPHOTO", snoDetailsObj.getString(19));
				jsonObject.put("POSTSURGERYPHOTO", snoDetailsObj.getString(20));
				jsonObject.put("ADITIONALDOCS", snoDetailsObj.getString(21));
				jsonObject.put("PACKAGERATE", snoDetailsObj.getString(22));
				jsonObject.put("INVESTIGATIONDOC", snoDetailsObj.getString(23));
				jsonObject.put("TREATMENTSLIP", snoDetailsObj.getString(24));
				jsonObject.put("ADMINSSIONSLIP", snoDetailsObj.getString(25));
				jsonObject.put("DISCHARGESLIP", snoDetailsObj.getString(26));
				jsonObject.put("CLAIMID", snoDetailsObj.getString(27));
				jsonObject.put("REMARKID", snoDetailsObj.getString(28));
				jsonObject.put("REMARKS", snoDetailsObj.getString(29));
				jsonObject.put("ADITIONAL_DOC1", snoDetailsObj.getString(30));
				jsonObject.put("ADITIONAL_DOC2", snoDetailsObj.getString(31));
				jsonObject.put("FAMILYHEADNAME", snoDetailsObj.getString(32));
				jsonObject.put("VERIFIERNAME", snoDetailsObj.getString(33));
				jsonObject.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
				jsonObject.put("DATEOFDISCHARGE", DateFormat.FormatToDateString(snoDetailsObj.getString(35)));
				jsonObject.put("DATEOFADMISSION1", DateFormat.dateConvertor(snoDetailsObj.getString(34), ""));
				jsonObject.put("DATEOFDISCHARGE1", DateFormat.dateConvertor(snoDetailsObj.getString(35), ""));
				jsonObject.put("MORTALITY", snoDetailsObj.getString(36));
				jsonObject.put("REFERRALCODE", snoDetailsObj.getString(37));
				jsonObject.put("AUTHORIZEDCODE", snoDetailsObj.getString(38));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(39));
				jsonObject.put("NABHFlag", snoDetailsObj.getString(40));
				jsonObject.put("Address", snoDetailsObj.getString(41));
				jsonObject.put("Statusflag", snoDetailsObj.getString(42));
				jsonObject.put("claimCaseNo", snoDetailsObj.getString(43));
				jsonObject.put("claimBillNo", snoDetailsObj.getString(44));
				jsonObject.put("PATIENT_PHOTO", snoDetailsObj.getString(45));
				jsonObject.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj.getString(46));
				jsonObject.put("INTRA_SURGERY_PHOTO", snoDetailsObj.getString(47));
				String mob = snoDetailsObj.getString(50);
				if (mob != null) {
					jsonObject.put("MOBILE", mob);
				} else {
					jsonObject.put("MOBILE", "--");
				}
				jsonObject.put("CLAIMNO", snoDetailsObj.getString(48));
				jsonObject.put("IMPLANTDATA", snoDetailsObj.getString(49));
				jsonObject.put("pendingat", snoDetailsObj.getLong(51));
				jsonObject.put("claimstatus", snoDetailsObj.getLong(52));
				jsonObject.put("CPDMORTALITY", snoDetailsObj.getString(53));
				jsonObject.put("verification", snoDetailsObj.getString(54));
				jsonObject.put("ispatient", snoDetailsObj.getString(55));
				jsonObject.put("Referalstatus", snoDetailsObj.getString(56));
				jsonObject.put("PackageCode", snoDetailsObj.getString(57));
				jsonObject.put("txnPackageDetailId", snoDetailsObj.getLong(58));
				jsonObject.put("packageCode1",
						snoDetailsObj.getString(59) != null ? snoDetailsObj.getString(59) : "NA");
				jsonObject.put("packageName1",
						snoDetailsObj.getString(60) != null ? snoDetailsObj.getString(60) : "NA");
				jsonObject.put("subPackageCode1",
						snoDetailsObj.getString(61) != null ? snoDetailsObj.getString(61) : "NA");
				jsonObject.put("subPackageName1",
						snoDetailsObj.getString(62) != null ? snoDetailsObj.getString(62) : "NA");
				jsonObject.put("procedureCode1",
						snoDetailsObj.getString(63) != null ? snoDetailsObj.getString(63) : "NA");
				jsonObject.put("procedureName1",
						snoDetailsObj.getString(64) != null ? snoDetailsObj.getString(64) : "NA");
				jsonObject.put("TOTALAMOUNTBLOCKED", snoDetailsObj.getString(65));
				jsonObject.put("CREATEON", DateFormat.dateConvertor(snoDetailsObj.getString(66), "time"));
				jsonObject.put("MEMBERID", snoDetailsObj.getString(67));
				jsonObject.put("ISEMERGENCY", snoDetailsObj.getString(68));
				jsonObject.put("OVERRIDECODE", snoDetailsObj.getString(69));
				jsonObject.put("TREATMENTDAY", snoDetailsObj.getString(70));
				jsonObject.put("DOCTORNAME", snoDetailsObj.getString(71));
				jsonObject.put("FROMHOSPITALNAME", snoDetailsObj.getString(72));
				jsonObject.put("TOHOSPITAL", snoDetailsObj.getString(73));
				jsonObject.put("DISREMARKS", snoDetailsObj.getString(74));
				jsonObject.put("TRANSACTIONDESCRIPTION", snoDetailsObj.getString(75));
				jsonObject.put("HOSPITALCATEGORYNAME", snoDetailsObj.getString(76));
				jsonObject.put("disverification", snoDetailsObj.getString(77));
				jsonObject.put("Packagerate1",
						snoDetailsObj.getString(78) != null ? snoDetailsObj.getString(78) : "N/A");
				jsonObject.put("uidreferencenumber", snoDetailsObj.getString(79));
				jsonObject.put("surgerydateandtime",
						snoDetailsObj.getString(80) != null ? snoDetailsObj.getString(80) : "NA");
				jsonObject.put("surgerydoctorname",
						snoDetailsObj.getString(81) != null ? snoDetailsObj.getString(81) : "NA");
				jsonObject.put("suergerycontactnumber",
						snoDetailsObj.getString(82) != null ? snoDetailsObj.getString(82) : "NA");
				jsonObject.put("suergeryregnumber",
						snoDetailsObj.getString(83) != null ? snoDetailsObj.getString(83) : "NA");
				jsonObject.put("mortalityauditreport", snoDetailsObj.getString(84));
				jsonObject.put("mortalityDoc", snoDetailsObj.getString(85));
				jsonObject.put("categoryname", snoDetailsObj.getString(86));
				jsonObject.put("transactionId", snoDetailsObj.getLong(87));
				jsonObject.put("floatId", snoDetailsObj.getLong(88));
				details.put("actionData", jsonObject);
				while (snoDetailsObj1.next()) {
					jsonObject1 = new JSONObject();
					jsonObject1.put("APPROVEDAMOUNT", snoDetailsObj1.getString(1));
					jsonObject1.put("ACTIONTYPE", snoDetailsObj1.getString(2));
					jsonObject1.put("ACTIONBY", snoDetailsObj1.getString(3));
					jsonObject1.put("DESCRIPTION", snoDetailsObj1.getString(4));
					jsonObject1.put("ACTIONON", snoDetailsObj1.getString(5));
					jsonObject1.put("ACTIONON1", DateFormat.dateConvertor(snoDetailsObj1.getString(5), ""));
					jsonObject1.put("DISCHARGESLIP", snoDetailsObj1.getString(6));
					jsonObject1.put("ADITIONALDOCS", snoDetailsObj1.getString(7));
					jsonObject1.put("ADDITIONALDOC1", snoDetailsObj1.getString(8));
					jsonObject1.put("PRESURGERY", snoDetailsObj1.getString(9));
					jsonObject1.put("POSTSURGERY", snoDetailsObj1.getString(10));
					jsonObject1.put("HOSPITALCODE", snoDetailsObj.getString(18));
					jsonObject1.put("ACTUALDATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(2)));
					jsonObject1.put("DATEOFADMISSION", DateFormat.FormatToDateString(snoDetailsObj.getString(34)));
					jsonObject1.put("REMARKS", snoDetailsObj1.getString(12));
					jsonObject1.put("ADDITIONALDOC2", snoDetailsObj1.getString(13));
					jsonObject1.put("PATIENT_PHOTO", snoDetailsObj1.getString(14));
					jsonObject1.put("SPECIMEN_REMOVAL_PHOTO", snoDetailsObj1.getString(15));
					jsonObject1.put("INTRA_SURGERY_PHOTO", snoDetailsObj1.getString(16));
					jsonArray.put(jsonObject1);
				}
				while (snoDetailsObj2.next()) {
					jsonObject2 = new JSONObject();
					jsonObject2.put("ADM_VITALSIGN", snoDetailsObj2.getString(1));
					jsonObject2.put("ADM_VITALVALUE", snoDetailsObj2.getString(2));
					jsonObject2.put("DIS_VITALSIGN", snoDetailsObj2.getString(3));
					jsonObject2.put("DIS_VITALVALUE", snoDetailsObj2.getString(4));
					jsonArray1.put(jsonObject2);
				}
				while (snoDetailsObj3.next()) {
					jsonObject3 = new JSONObject();
					jsonObject3.put("urn", snoDetailsObj3.getString(1));
					jsonObject3.put("claimNo", snoDetailsObj3.getString(2));
					jsonObject3.put("caseNo", snoDetailsObj3.getString(3));
					jsonObject3.put("patientName", snoDetailsObj3.getString(4));
					jsonObject3.put("phoneNo", snoDetailsObj3.getString(5));
					jsonObject3.put("hospitalName", snoDetailsObj3.getString(6));
					jsonObject3.put("hospitalCode", snoDetailsObj3.getString(7));
					jsonObject3.put("packageCode", snoDetailsObj3.getString(8));
					jsonObject3.put("packageName", snoDetailsObj3.getString(9));
					jsonObject3.put("actualDateOfAdmission", DateFormat.formatDate(snoDetailsObj3.getDate(10)));
					jsonObject3.put("actualDateOfDischarge", DateFormat.formatDate(snoDetailsObj3.getDate(11)));
					jsonObject3.put("hospitalClaimAmount", snoDetailsObj3.getLong(12));
					jsonObject3.put("reportName", snoDetailsObj3.getString(13));
					jsonObject3.put("claimId", snoDetailsObj3.getLong(14));
					jsonObject3.put("transactionId", snoDetailsObj3.getLong(15));
					txnId = snoDetailsObj3.getInt(15);
					jsonObject3.put("txnPackageId", snoDetailsObj3.getLong(16));
					jsonObject3.put("slNo", snoDetailsObj3.getLong(17));
					jsonObject3.put("createdOn", snoDetailsObj3.getDate(18));
					jsonObject3.put("statusFlag", snoDetailsObj3.getString(19));
					jsonObject3.put("doctorRegNo", snoDetailsObj3.getString(20));
					jsonObject3.put("surgeryDate", snoDetailsObj3.getDate(21));
					jsonArray2.put(jsonObject3);
				}
				while (floatLog.next()) {
					floatLogObject = new JSONObject();
					floatLogObject.put("actionLogId", floatLog.getLong(1));
					floatLogObject.put("actionon", floatLog.getTimestamp(17));
					floatLogObject.put("amount", floatLog.getLong(4));
					floatLogObject.put("remarks", floatLog.getString(12));
					floatLogObject.put("actionByName", floatLog.getString(21));
					floatLogObject.put("actionByGroup", floatLog.getString(22));
					floatLogObject.put("createdOn", floatLog.getDate(6));
					floatLogArray.put(floatLogObject);
				}
				packageBlock = packageBlocking.getMultiplePackageBlocking(urn, actualDate);
				preAuthHist = packageBlocking.getPreAuthHistory(urn, authorizedCode, hospitalCode);
				preAuthLog = packageBlocking.getPreAuthLogHistory(urn, authorizedCode, hospitalCode);
				cpdActionLog = packageBlocking.getCpdActionTakenLog(txnId);
				multipackagecaeno = getMultiplePackageBlockingthroughcaseno(casenumber, actualDate);
				cardBalanceArray = getCardBalanceDetails(txnId, urn);
				details.put("actionLog", jsonArray);
				details.put("cpdActionLog", cpdActionLog);
				details.put("packageBlock", packageBlock);
				details.put("preAuthHist", preAuthHist);
				details.put("preAuthLog", preAuthLog);
				details.put("vitalArray", jsonArray1);
				details.put("multipackagecaseno", multipackagecaeno);
				details.put("meTrigger", jsonArray2);
				details.put("cardBalanceArray", cardBalanceArray);
				details.put("floatdetails", floatLogArray);
			}
		} catch (Exception e) {
			logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
				if (snoDetailsObj2 != null) {
					snoDetailsObj2.close();
				}

			} catch (Exception e2) {
				logger.error("Error in getActionDetails method of PreAuthDaoImpl class:", e2);
			}

		}
		return details.toString();
	}

	@Override
	public String getCPDTriggerdetailsData(String hospitalcode, Date dateofAdmission, Date dateofdischarge,
			String procedurecode) {
		System.out.println();
		JSONObject data = new JSONObject();
		JSONArray cpdtrigerlist = new JSONArray();
		JSONArray cpdtrigerlist1 = new JSONArray();
		JSONObject jsonObject1, jsonObject2;
		ResultSet rs = null, rs1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_CPD_TRG_DTLS")
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_Pattern", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalcode.trim());
			storedProcedureQuery.setParameter("P_PACKAGECODE", procedurecode.trim());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_Pattern");
			if (rs.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("avglos", rs.getString(1));
				jsonObject1.put("avglos1", rs.getString(2));
				jsonObject1.put("avgpackagecount", rs.getString(3));
				jsonObject1.put("avgpackagecount1", rs.getString(4));
				jsonObject1.put("result_Ratio", rs.getString(5));
				cpdtrigerlist.put(jsonObject1);
			}
			while (rs1.next()) {
				jsonObject2 = new JSONObject();
				jsonObject2.put("urn", rs1.getString(1));
				jsonObject2.put("membername", rs1.getString(2));
				jsonObject2.put("procedurename", rs1.getString(3));
				jsonObject2.put("packagename", rs1.getString(4));
				jsonObject2.put("packagecode", rs1.getString(5));
				jsonObject2.put("Actualdateofadmission", rs1.getString(6));
				jsonObject2.put("Actualdateofadischarge", rs1.getString(7));
				jsonObject2.put("totalamount", rs1.getString(8));
				jsonObject2.put("caseno", rs1.getString(9));
				cpdtrigerlist1.put(jsonObject2);
			}
			data.put("cpdtrigerlist", cpdtrigerlist);
			data.put("cpdtrigerlist1", cpdtrigerlist1);
		} catch (Exception e) {
			logger.error("Error in getPatientTreatementDetails method of getCPDTriggerdetailsData class:", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getPatientTreatementDetails method of getCPDTriggerdetailsData class:", e2);
			}
		}
		return data.toString();
	}

	@Override
	public String getsnamortalitystatus(Long claimid) {
		return txnclaimapplicationrepo.getsnamortalitystatus(claimid);
	}

	@Override
	public List<Mstschemesubcategory> getSchemesubcategory() {
		List<Mstschemesubcategory> allrecord = mstschemesubcategoryrepository.findAll();
		return allrecord;
	}

}
