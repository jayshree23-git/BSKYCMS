package com.project.bsky.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimRaiseBean;
import com.project.bsky.bean.ClaimRaiseSearch;
import com.project.bsky.bean.PaymentFreezeBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Searchhos;
import com.project.bsky.model.ClaimFileErrorLog;
import com.project.bsky.repository.ClaimFileErrorLogRepository;
import com.project.bsky.repository.TxnclaimapplicationRepository;
import com.project.bsky.service.ClaimraiseDetailsService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.DaysBetweenDates;
import com.project.bsky.util.RandomNumber;

@SuppressWarnings({ "deprecation", "unused" })
@Service
@PropertySource("classpath:fileConfiguration.properties")
public class ClaimraiseDetailsServiceImpl implements ClaimraiseDetailsService {

	private static ResourceBundle bskyResourcesBundel = ResourceBundle.getBundle("fileConfiguration");

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TxnclaimapplicationRepository txnclaimapplicationRepository;

	@Autowired
	private ClaimFileErrorLogRepository claimFileErrorLogRepository;

	@Value("${file.path.DischargeSlip}")
	private String DischargeSlip;

	@Value("${file.path.presurgery}")
	private String preSurgFilePath;

	@Value("${file.path.postsurgery}")
	private String postSurgFilePath;;

	@Value("${file.path.AdditionalDoc}")
	private String AdditionalDoc;

	@Value("${file.path.IntraSurgery}")
	private String IntraSurgeryPic;

	@Value("${file.path.SpecimenRemoval}")
	private String SpecimenRemovalPic;

	@Value("${file.path.PatientPic}")
	private String PatientPic;

	private final Logger logger;

	@Autowired
	public ClaimraiseDetailsServiceImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<Object> getUSerDetailsDAta(String check) {
		List<Object> claimRaiseDetailsListforsearch = new ArrayList<Object>();
		ResultSet deptDetailsObj = null;
		String authcode = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_CLAIMRAISE_SEARCH")
					.registerStoredProcedureParameter("p_ActionCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_Id", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_ActionCode", "S");
			storedProcedure.setParameter("p_Id", check);
			storedProcedure.execute();
			deptDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_P_MSGOUT");
			while (deptDetailsObj.next()) {
				ClaimRaiseSearch resBeanforSEarch = new ClaimRaiseSearch();
				resBeanforSEarch.setId(deptDetailsObj.getLong(1));
				resBeanforSEarch.setTransactionid(deptDetailsObj.getLong(2));
				resBeanforSEarch.setInvoiceno(deptDetailsObj.getString(3));
				resBeanforSEarch.setURN(deptDetailsObj.getString(4).trim());
				resBeanforSEarch.setNoofdays(deptDetailsObj.getString(5));
				String Actualdateofadmission = deptDetailsObj.getString(6);
				String s1 = Actualdateofadmission.substring(0, 2);
				String s2 = Actualdateofadmission.substring(2, 4);
				String s3 = Actualdateofadmission.substring(4, Actualdateofadmission.length());
				String ssString = s1 + "/" + s2 + "/" + s3;
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(ssString);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String d = sdf.format(date1);
				resBeanforSEarch.setActualdateofadmission(d);
				String discharge = deptDetailsObj.getString(7);
				String s11 = discharge.substring(0, 2);
				String s21 = discharge.substring(2, 4);
				String s31 = discharge.substring(4, discharge.length());
				String ssString1 = s11 + "/" + s21 + "/" + s31;
				Date date11 = new SimpleDateFormat("dd/MM/yyyy").parse(ssString1);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
				String d1 = sdf1.format(date11);
				resBeanforSEarch.setActualdateofdischarge(d1);
				resBeanforSEarch.setTotalamountclaimed(deptDetailsObj.getString(8));
				resBeanforSEarch.setAge(deptDetailsObj.getInt(9));
				resBeanforSEarch.setGender(deptDetailsObj.getString(10));
				resBeanforSEarch.setPatientName(deptDetailsObj.getString(11));
				resBeanforSEarch.setPackageCode(deptDetailsObj.getString(12));
				resBeanforSEarch.setPackageName(deptDetailsObj.getString(13));
				resBeanforSEarch.setProcedurename(deptDetailsObj.getString(14));
				resBeanforSEarch.setDateofadmission(deptDetailsObj.getString(15));
				resBeanforSEarch.setDateOfDischarge(deptDetailsObj.getString(16));
				resBeanforSEarch.setUserid(deptDetailsObj.getInt(17));
				resBeanforSEarch.setHospitalstateCode(deptDetailsObj.getInt(18));
				resBeanforSEarch.setTransactiondetailsid(deptDetailsObj.getLong(19));
				resBeanforSEarch.setTotalamountblocked(deptDetailsObj.getString(20));
				resBeanforSEarch.setHospitalcode(deptDetailsObj.getString(21));
				resBeanforSEarch.setDownload("download");
				resBeanforSEarch.setStatename(deptDetailsObj.getString(22));
				resBeanforSEarch.setDistrictname(deptDetailsObj.getString(23));
				resBeanforSEarch.setBlockname(deptDetailsObj.getString(24));
				resBeanforSEarch.setPanchayatname(deptDetailsObj.getString(25));
				resBeanforSEarch.setVillagename(deptDetailsObj.getString(26));
				resBeanforSEarch.setStatusflag(deptDetailsObj.getInt(27));
				authcode = deptDetailsObj.getString(28);
				if (authcode != null) {
					authcode.substring(4);
				}
				resBeanforSEarch.setAuthorizedcode(authcode);
				resBeanforSEarch.setPatientphoneno(deptDetailsObj.getString(29));
				long noofStringdayscalculationString = CommonFileUpload.totalDaysBetweenDates(Actualdateofadmission,
						discharge);
				resBeanforSEarch.setNoofdatscalculation(String.valueOf(noofStringdayscalculationString + "days"));
				claimRaiseDetailsListforsearch.add(resBeanforSEarch);
				resBeanforSEarch.setVerificationmode(deptDetailsObj.getString(30));
				resBeanforSEarch.setIspatientotpverifiedString(deptDetailsObj.getString(31));
				resBeanforSEarch.setREFERRALAUTHSTATUS(deptDetailsObj.getString(32));
				resBeanforSEarch.setIntrasurgery(deptDetailsObj.getString(33));
				resBeanforSEarch.setPOSTSURGERY(deptDetailsObj.getString(34));
				resBeanforSEarch.setPRESURGERY(deptDetailsObj.getString(35));
				resBeanforSEarch.setSPECIMENREMOVAL(deptDetailsObj.getString(36));
				resBeanforSEarch.setCaseno(deptDetailsObj.getString(37));
				resBeanforSEarch.setDischarge_doc(deptDetailsObj.getString(38));
				resBeanforSEarch.setPatientphoto(deptDetailsObj.getString(39));
				resBeanforSEarch
						.setPackageCode1(deptDetailsObj.getString(40) != null ? deptDetailsObj.getString(40) : "N/A");
				resBeanforSEarch
						.setPackageName1(deptDetailsObj.getString(41) != null ? deptDetailsObj.getString(41) : "N/A");
				resBeanforSEarch
						.setSubPackageCode(deptDetailsObj.getString(42) != null ? deptDetailsObj.getString(42) : "N/A");
				resBeanforSEarch
						.setSubPackageName(deptDetailsObj.getString(43) != null ? deptDetailsObj.getString(43) : "N/A");
				resBeanforSEarch
						.setProcedureCode(deptDetailsObj.getString(44) != null ? deptDetailsObj.getString(44) : "N/A");
				resBeanforSEarch
						.setProcedureName(deptDetailsObj.getString(45) != null ? deptDetailsObj.getString(45) : "N/A");
				resBeanforSEarch.setPreauthdoc(deptDetailsObj.getString(46));
				resBeanforSEarch.setClaimdoc(deptDetailsObj.getString(47));
				resBeanforSEarch.setCategoryname(deptDetailsObj.getString(48));
			}
		} catch (Exception e) {
			logger.error("Exception occured in getUSerDetailsDAta method of ClaimraiseDetailsServiceImpl :", e);
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
		return claimRaiseDetailsListforsearch;
	}

	@Override
	public List<Object> getclaimrasiedata(String hospitalCode, String Package, String packageCode, String URN,
			String fromDate, String toDate, String caseno, String schemeid, String schemecategoryid) {
		List<Object> claimRaiseDetailsList = new ArrayList<Object>();
		if (Package.equals("")) {
			Package = "";
		}
		Long schemecatId = null;
		if (schemecategoryid != null && !schemecategoryid.equals("")) {
			schemecatId = Long.parseLong(schemecategoryid);
		} else {
			schemecatId = null;
		}
		String authcode = null;
		ResultSet rsResultSet = null;
		DecimalFormat df = new DecimalFormat("#,###,###,###.00");
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_Fresh_Raise")
					.registerStoredProcedureParameter("p_ActionCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_packagename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_caseno", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEME_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_ActionCode", "V");
			storedProcedureQuery.setParameter("P_Hospitalcode", hospitalCode.trim());
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_packagecode", Package.trim());
			storedProcedureQuery.setParameter("p_packagename", packageCode.trim());
			storedProcedureQuery.setParameter("p_urn", URN.trim());
			storedProcedureQuery.setParameter("p_caseno", caseno.trim());
			storedProcedureQuery.setParameter("P_SCHEME_ID", Long.parseLong(schemeid));
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemecatId);
			storedProcedureQuery.execute();
			rsResultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_P_MSGOUT");
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
				resBean.setDateOfDischarge(d);
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
				resBean.setActualdateofadmission(sd1);
				resBean.setCaseno(rsResultSet.getString(20));
				resBean.setPreauthdocs(rsResultSet.getString(21));
				resBean.setClaimdocs(rsResultSet.getString(22));
				claimRaiseDetailsList.add(resBean);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getclaimrasiedata method of ClaimraiseDetailsServiceImpl :", e);
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
	public Response saveClaimRaiseHospital(Long refractionid, String hospitalCode, Long updatedby, String urnNumber,
			BigInteger invoiceNumber, String dateofAdmission, String packageCode, MultipartFile treatmentDetailsSlip,
			MultipartFile hospitalBill, MultipartFile presurgry, MultipartFile postsurgery, String casenumber,
			String billnumber, MultipartFile intasurgery, MultipartFile specimansurgery, MultipartFile patientpic,
			String tmsdischargedocumnet, String claimbilldate, Long amountValue, String remarks) throws Exception {
		String random = RandomNumber.generateRandomClaimNo();
		Query query = this.entityManager
				.createNativeQuery("SELECT COUNT(1) FROM TXNCLAIM_APPLICATION WHERE CLAIM_NO = ?1");
		query.setParameter(1, random);
		int count = ((Number) query.getSingleResult()).intValue();
		while (count > 0) {
			random = RandomNumber.generateRandomClaimNo();
			query.setParameter(1, random);
			count = ((Number) query.getSingleResult()).intValue();
		}
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		String year = dateofAdmission.substring(4, 8);
		Response response = new Response();
		Integer claimraiseInteger = null;
		Map<String, String> filePath = CommonFileUpload.createFile(urnNumber.trim(), presurgry, postsurgery,
				treatmentDetailsSlip, hospitalBill, intasurgery, specimansurgery, patientpic, year.trim(),
				hospitalCode.trim());
		filePath.forEach((k, v) -> {
			if (v != null && !v.equalsIgnoreCase("")) {
				String fullFilePath = CommonFileUpload.getFullDocumentPath(v, year, hospitalCode,
						CommonFileUpload.getFolderName(v));
				File file = new File(fullFilePath);
				if (!file.exists()) {
					filePath.forEach((k1, v1) -> {
						if (v1 != null && !v1.equalsIgnoreCase("")) {
							String fullFilePath1 = CommonFileUpload.getFullDocumentPath(v1, year, hospitalCode,
									CommonFileUpload.getFolderName(v1));
							File file1 = new File(fullFilePath1);
							if (file1.exists()) {
								file1.delete();
							}
						}
					});

					if (k.equalsIgnoreCase("DischargeSlip"))
						throw new RuntimeException(treatmentDetailsSlip.getOriginalFilename()
								+ " Discharge Slip Failed To Save in Server!");
					else if (k.equalsIgnoreCase("presurgery"))
						throw new RuntimeException(
								presurgry.getOriginalFilename() + " Pre Surgery Failed To Save in Server!");
					else if (k.equalsIgnoreCase("postsurgery"))
						throw new RuntimeException(
								postsurgery.getOriginalFilename() + " Post Surgery Failed To Save in Server!");
					else if (k.equalsIgnoreCase("AdditionalDoc"))
						throw new RuntimeException(
								hospitalBill.getOriginalFilename() + " Hospital Bill Failed To Save in Server!");
					else if (k.equalsIgnoreCase("IntraSurgeryPic"))
						throw new RuntimeException(
								intasurgery.getOriginalFilename() + " Inta Surgery Failed To Save in Server!");
					else if (k.equalsIgnoreCase("SpecimenRemovalPic"))
						throw new RuntimeException(specimansurgery.getOriginalFilename()
								+ " Specimen Removal Image Failed To Save in Server!");
					else if (k.equalsIgnoreCase("PatientPic"))
						throw new RuntimeException(
								patientpic.getOriginalFilename() + " Patient Pic Failed To Save in Server!");
				}
			}
		});

		try {
			StoredProcedureQuery saveCpdUserData = this.entityManager
					.createStoredProcedureQuery("USP_CLAIMRAISE_DETAILS_SUBMIT")
					.registerStoredProcedureParameter("Action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INVOICENO", BigInteger.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PENDINGAT", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMSTATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISCHARGESLIP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDTIONAL_DOC", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRESURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POSTSURGERYPHOTO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDON", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REFTRANSACTIONID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_CreatedBy", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_ASSIGNEDCPD", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_ASSIGNEDSNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMCASENO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMBILLNO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_IntraSurgery", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Specimen", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PatientPic", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIM_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_claimbilldate", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIM_AMOUNT", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIM_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT);

			saveCpdUserData.setParameter("Action", "T");
			saveCpdUserData.setParameter("P_TRANSACTIONDETAILSID", refractionid);
			saveCpdUserData.setParameter("P_INVOICENO", invoiceNumber);
			saveCpdUserData.setParameter("P_URN", urnNumber.trim());
			saveCpdUserData.setParameter("P_HOSPITALCODE", hospitalCode);
			saveCpdUserData.setParameter("P_PENDINGAT", 1);
			saveCpdUserData.setParameter("P_CLAIMSTATUS", 0);
			saveCpdUserData.setParameter("P_PACKAGECODE", packageCode.substring(4));
			saveCpdUserData.setParameter("P_REFTRANSACTIONID", 0);
			saveCpdUserData.setParameter("P_STATUSFLAG", 0);
			saveCpdUserData.setParameter("p_USER_IP", getuseripaddressString);
			saveCpdUserData.setParameter("p_CreatedBy", updatedby);
			saveCpdUserData.setParameter("p_ASSIGNEDCPD", null);// "1672"
			saveCpdUserData.setParameter("p_ASSIGNEDSNO", null);// "1914"
			saveCpdUserData.setParameter("P_CLAIMCASENO", casenumber.trim());
			saveCpdUserData.setParameter("P_CLAIMBILLNO", billnumber.trim());
			saveCpdUserData.setParameter("P_CLAIM_NO", random.trim());
			saveCpdUserData.setParameter("P_claimbilldate", claimbilldate.trim());
			saveCpdUserData.setParameter("P_CLAIM_AMOUNT", amountValue);
			saveCpdUserData.setParameter("P_CLAIM_DESCRIPTION", remarks);
			for (Map.Entry<String, String> entry : filePath.entrySet()) {
				if (DischargeSlip.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_DISCHARGESLIP", entry.getValue());
				else if (AdditionalDoc.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_ADDTIONAL_DOC", entry.getValue());
				else if (preSurgFilePath.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_PRESURGERYPHOTO", entry.getValue());
				else if (postSurgFilePath.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_POSTSURGERYPHOTO", entry.getValue());
				else if (IntraSurgeryPic.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_IntraSurgery", entry.getValue());
				else if (SpecimenRemovalPic.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_Specimen", entry.getValue());
				else if (PatientPic.contains(entry.getKey()))
					saveCpdUserData.setParameter("P_PatientPic", entry.getValue());
			}
			saveCpdUserData.setParameter("P_CREATEDON", "");
			saveCpdUserData.execute();
			claimraiseInteger = (Integer) saveCpdUserData.getOutputParameterValue("p_msgout");
			if (claimraiseInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Claim Raised Successfully, (Claim No. : " + random + ")");
			} else if (claimraiseInteger == 2) {
				response.setStatus("Failed");
				response.setMessage("This Claim Is Already Submitted..");
			} else if (claimraiseInteger == 3) {
				response.setStatus("Snanotmapped");
				response.setMessage("Sorry Your SNA Is Not Tagged..Kindly Contact To Your Admin");
			} else if (claimraiseInteger == 4) {
				response.setStatus("DC");
				response.setMessage("Sorry Your DC Is Not Tagged..Kindly Contact To Your Admin");
			} else if (claimraiseInteger == 5) {
				response.setStatus("amount");
				response.setMessage("Sorry You Can Not Approve More Than Claim Amount");
			} else if(claimraiseInteger == 6) {
				response.setStatus("claimBill");
				response.setMessage("Provide Claim Bill Number Already Present For This Case Number");
			}
		} catch (Exception e) {
			logger.error("Exception occured in getclaimrasiedata method of ClaimraiseDetailsServiceImpl :", e);
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public byte[] downLoadFileClaim(String fileName, String year, String hCode) {
		String folderName = bskyResourcesBundel.getString("folder.hospitalBill");
		byte[] downloadFile = null;
		try {
			downloadFile = CommonFileUpload.downLoadFile1(fileName, year, hCode, folderName);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return downloadFile;
	}

	@Override
	public List<Object> getpackdata(String packageName) {
		List<Object> searchpacage = new ArrayList<Object>();
		ResultSet pack = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSP_INCLUSIONOFSEARCHING")
					.registerStoredProcedureParameter("p_PROCEDURE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_PROCEDURE", packageName.trim());
			storedProcedure.execute();
			pack = (ResultSet) storedProcedure.getOutputParameterValue("p_p_msgout");
			while (pack.next()) {
				Searchhos searcdata = new Searchhos();
				searcdata.setPackagename(pack.getString(1));
				searcdata.setPackagecode(pack.getString(2));
				searchpacage.add(searcdata);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (pack != null) {
					pack.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return searchpacage;
	}

	@Override
	public void getUnSavedClaimData() {
		try {
			ClaimFileErrorLog claimFileErrorLog;
			List<Object[]> list = txnclaimapplicationRepository.getCustomTXNClaimApplication();
			for (Object[] obj : list) {
				int fileNotFoundCount = 0;
				claimFileErrorLog = new ClaimFileErrorLog();
				String hospitalCode = "", year = "";
				if (obj[2] != null)
					hospitalCode = obj[2].toString();
				if (obj[4] != null)
					year = obj[4].toString().substring(4, 8);
				claimFileErrorLog.setClaimid(Long.parseLong(obj[0].toString()));
				claimFileErrorLog.setTranctiondetailsid(Long.parseLong(obj[1].toString()));
				claimFileErrorLog.setUrn(obj[3].toString());

//				For Discharge Slip
				if (obj[5] != null) {
					String dischargeSlip = obj[5].toString();
					String dischargeSlipPath = CommonFileUpload.getFullDocumentPath(dischargeSlip, year, hospitalCode,
							CommonFileUpload.getFolderName(dischargeSlip));
					File dischargeSlipFile = new File(dischargeSlipPath);
					if (dischargeSlipFile.exists()) {
						claimFileErrorLog.setDischrage("File Present");
					} else if (!dischargeSlipFile.exists()) {
						String dischargeSlipPath1 = CommonFileUpload.getRootPath() + dischargeSlip;
						dischargeSlipFile = new File(dischargeSlipPath1);
						if (dischargeSlipFile.exists()) {
							claimFileErrorLog.setDischrage("File Present in Root Directory");
							CommonFileUpload.copyFile(dischargeSlipFile, dischargeSlipPath);
							dischargeSlipFile = new File(dischargeSlipPath);
							if (dischargeSlipFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setDischrage("File Absent");
						fileNotFoundCount++;

					}
				} else {
					claimFileErrorLog.setDischrage("File Not Provided");
				}

//				For Additional Doc
				if (obj[6] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String additionalDoc = obj[6].toString();
					String additionalDocPath = CommonFileUpload.getFullDocumentPath(additionalDoc, year, hospitalCode,
							CommonFileUpload.getFolderName(additionalDoc));
					File additionalDocFile = new File(additionalDocPath);
					if (additionalDocFile.exists()) {
						claimFileErrorLog.setAdditionalDoc("File Present");
					} else if (!additionalDocFile.exists()) {
						String additionalDocPath1 = CommonFileUpload.getRootPath() + additionalDoc;
						additionalDocFile = new File(additionalDocPath1);
						if (additionalDocFile.exists()) {
							claimFileErrorLog.setAdditionalDoc("File Present in Root Directory");
							CommonFileUpload.copyFile(additionalDocFile, additionalDocPath);
							additionalDocFile = new File(additionalDocPath);
							if (additionalDocFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setAdditionalDoc("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setAdditionalDoc("File Not Provided");
				}

//				For Additional Doc1
				if (obj[7] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String additionalDoc1 = obj[7].toString();
					String additionalDoc1Path = CommonFileUpload.getFullDocumentPath(additionalDoc1, year, hospitalCode,
							CommonFileUpload.getFolderName(additionalDoc1));
					File additionalDoc1File = new File(additionalDoc1Path);
					if (additionalDoc1File.exists()) {
						claimFileErrorLog.setAdditionalDoc1("File Present");
					} else if (!additionalDoc1File.exists()) {
						String additionalDoc1Path1 = CommonFileUpload.getRootPath() + additionalDoc1;
						additionalDoc1File = new File(additionalDoc1Path1);
						if (additionalDoc1File.exists()) {
							claimFileErrorLog.setAdditionalDoc1("File Present in Root Directory");
							CommonFileUpload.copyFile(additionalDoc1File, additionalDoc1Path);
							additionalDoc1File = new File(additionalDoc1Path);
							if (additionalDoc1File.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setAdditionalDoc1("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setAdditionalDoc1("File Not Provided");
				}

//				For Additional Doc2
				if (obj[8] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String additionalDoc2 = obj[8].toString();
					String additionalDoc2Path = CommonFileUpload.getFullDocumentPath(additionalDoc2, year, hospitalCode,
							CommonFileUpload.getFolderName(additionalDoc2));
					File additionalDoc2File = new File(additionalDoc2Path);
					if (additionalDoc2File.exists()) {
						claimFileErrorLog.setAdditionalDoc2("File Present");
					} else if (!additionalDoc2File.exists()) {
						String additionalDoc2Path1 = CommonFileUpload.getRootPath() + additionalDoc2;
						additionalDoc2File = new File(additionalDoc2Path1);
						if (additionalDoc2File.exists()) {
							claimFileErrorLog.setAdditionalDoc2("File Present in Root Directory");
							CommonFileUpload.copyFile(additionalDoc2File, additionalDoc2Path);
							additionalDoc2File = new File(additionalDoc2Path);
							if (additionalDoc2File.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setAdditionalDoc2("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setAdditionalDoc2("File Not Provided");
				}

//				For Investigation Doc
				if (obj[9] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String investigationDoc = obj[9].toString();
					String investigationDocPath = CommonFileUpload.getFullDocumentPath(investigationDoc, year,
							hospitalCode, CommonFileUpload.getFolderName(investigationDoc));
					File investigationDocFile = new File(investigationDocPath);
					if (investigationDocFile.exists()) {
						claimFileErrorLog.setInvetigation("File Present");
					} else if (!investigationDocFile.exists()) {
						String investigationDocPath1 = CommonFileUpload.getRootPath() + investigationDoc;
						investigationDocFile = new File(investigationDocPath1);
						if (investigationDocFile.exists()) {
							claimFileErrorLog.setInvetigation("File Present in Root Directory");
							CommonFileUpload.copyFile(investigationDocFile, investigationDocPath);
							investigationDocFile = new File(investigationDocPath);
							if (investigationDocFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setInvetigation("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setInvetigation("File Not Provided");
				}

//				For Investigation Doc1
				if (obj[10] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String investigationDoc1 = obj[10].toString();
					String investigationDoc1Path = CommonFileUpload.getFullDocumentPath(investigationDoc1, year,
							hospitalCode, CommonFileUpload.getFolderName(investigationDoc1));
					File investigationDoc1File = new File(investigationDoc1Path);
					if (investigationDoc1File.exists()) {
						claimFileErrorLog.setInvestigation1("File Present");
					} else if (!investigationDoc1File.exists()) {
						String investigationDoc1Path1 = CommonFileUpload.getRootPath() + investigationDoc1;
						investigationDoc1File = new File(investigationDoc1Path1);
						if (investigationDoc1File.exists()) {
							claimFileErrorLog.setInvestigation1("File Present in Root Directory");
							CommonFileUpload.copyFile(investigationDoc1File, investigationDoc1Path);
							investigationDoc1File = new File(investigationDoc1Path);
							if (investigationDoc1File.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setInvestigation1("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setInvestigation1("File Not Provided");
				}

//				For Pre-Surgery
				if (obj[11] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String preSurgery = obj[11].toString();
					String preSurgeryPath = CommonFileUpload.getFullDocumentPath(preSurgery, year, hospitalCode,
							CommonFileUpload.getFolderName(preSurgery));
					File preSurgeryFile = new File(preSurgeryPath);
					if (preSurgeryFile.exists()) {
						claimFileErrorLog.setPreSurgery("File Present");
					} else if (!preSurgeryFile.exists()) {
						String preSurgeryPath1 = CommonFileUpload.getRootPath() + preSurgery;
						preSurgeryFile = new File(preSurgeryPath1);
						if (preSurgeryFile.exists()) {
							claimFileErrorLog.setPreSurgery("File Present in Root Directory");
							CommonFileUpload.copyFile(preSurgeryFile, preSurgeryPath);
							preSurgeryFile = new File(preSurgeryPath);
							if (preSurgeryFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setPreSurgery("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setPreSurgery("File Not Provided");
				}

//				For Post-Surgery
				if (obj[12] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String postSurgery = obj[12].toString();
					String postSurgeryPath = CommonFileUpload.getFullDocumentPath(postSurgery, year, hospitalCode,
							CommonFileUpload.getFolderName(postSurgery));
					File postSurgeryFile = new File(postSurgeryPath);
					if (postSurgeryFile.exists()) {
						claimFileErrorLog.setPostSurgery("File Present");
					} else if (!postSurgeryFile.exists()) {
						String postSurgeryPath1 = CommonFileUpload.getRootPath() + postSurgery;
						postSurgeryFile = new File(postSurgeryPath1);
						if (postSurgeryFile.exists()) {
							claimFileErrorLog.setPostSurgery("File Present in Root Directory");
							CommonFileUpload.copyFile(postSurgeryFile, postSurgeryPath);
							postSurgeryFile = new File(postSurgeryPath);
							if (postSurgeryFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setPostSurgery("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setPostSurgery("File Not Provided");
				}

//				For Intra-Surgery Summary
				if (obj[13] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String intraSurgery = obj[13].toString();
					String intraSurgeryPath = CommonFileUpload.getFullDocumentPath(intraSurgery, year, hospitalCode,
							CommonFileUpload.getFolderName(intraSurgery));
					File intraSurgeryFile = new File(intraSurgeryPath);
					if (intraSurgeryFile.exists()) {
						claimFileErrorLog.setIntraSurgery("File Present");
					} else if (!intraSurgeryFile.exists()) {
						String intraSurgeryPath1 = CommonFileUpload.getRootPath() + intraSurgery;
						intraSurgeryFile = new File(intraSurgeryPath1);
						if (intraSurgeryFile.exists()) {
							claimFileErrorLog.setIntraSurgery("File Present in Root Directory");
							CommonFileUpload.copyFile(intraSurgeryFile, intraSurgeryPath);
							intraSurgeryFile = new File(intraSurgeryPath);
							if (intraSurgeryFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setIntraSurgery("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setIntraSurgery("File Not Provided");
				}

//				For Specimen-Removal
				if (obj[14] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String specimenRemoval = obj[14].toString();
					String specimenRemovalPath = CommonFileUpload.getFullDocumentPath(specimenRemoval, year,
							hospitalCode, CommonFileUpload.getFolderName(specimenRemoval));
					File specimenRemovalFile = new File(specimenRemovalPath);
					if (specimenRemovalFile.exists()) {
						claimFileErrorLog.setSpecimenRemoval("File Present");
					} else if (!specimenRemovalFile.exists()) {
						String specimenRemovalPath1 = CommonFileUpload.getRootPath() + specimenRemoval;
						specimenRemovalFile = new File(specimenRemovalPath1);
						if (specimenRemovalFile.exists()) {
							claimFileErrorLog.setSpecimenRemoval("File Present in Root Directory");
							CommonFileUpload.copyFile(specimenRemovalFile, specimenRemovalPath);
							specimenRemovalFile = new File(specimenRemovalPath);
							if (specimenRemovalFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setSpecimenRemoval("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setSpecimenRemoval("File Not Provided");
				}

//				For Patient-Pic
				if (obj[15] != null && !Objects.equals(hospitalCode, "") && !Objects.equals(year, "")) {
					String patientPic = obj[15].toString();
					String patientPicPath = CommonFileUpload.getFullDocumentPath(patientPic, year, hospitalCode,
							CommonFileUpload.getFolderName(patientPic));
					File patientPicFile = new File(patientPicPath);
					if (patientPicFile.exists()) {
						claimFileErrorLog.setPatient("File Present");
					} else if (!patientPicFile.exists()) {
						String patientPicPath1 = CommonFileUpload.getRootPath() + patientPic;
						patientPicFile = new File(patientPicPath1);
						if (patientPicFile.exists()) {
							claimFileErrorLog.setPatient("File Present in Root Directory");
							CommonFileUpload.copyFile(patientPicFile, patientPicPath);
							patientPicFile = new File(patientPicPath);
							if (patientPicFile.exists()) {
							}
						}
					} else {
						claimFileErrorLog.setPatient("File Absent");
						fileNotFoundCount++;
					}
				} else {
					claimFileErrorLog.setPatient("File Not Provided");
				}

				if (fileNotFoundCount > 0)
					claimFileErrorLogRepository.save(claimFileErrorLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Object> getClaimList(CPDApproveRequestBean requestBean) {
		List<Object> claimList = new ArrayList<Object>();
		ResultSet claimListObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_CLAIM_TESTING_EXEC")
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_process", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_urn", null);
			storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_state_code", requestBean.getStateCode());
			storedProcedureQuery.setParameter("p_dist_code", requestBean.getDistCode());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalCode());
			storedProcedureQuery.setParameter("p_flag", 0);
			storedProcedureQuery.setParameter("p_process", null);
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
				resBean.setPackageName(claimListObj.getString(7));
				resBean.setActualDateOfDischarge(new SimpleDateFormat("dd-MMM-yyyy").format(claimListObj.getDate(8)));
				resBean.setPackageCode(claimListObj.getString(9));
				resBean.setClaimNo(claimListObj.getString(10));
				resBean.setHospitalcode(claimListObj.getString(11));
				resBean.setHospitalname(claimListObj.getString(12));
				resBean.setActualDateOfAdmission(new SimpleDateFormat("dd-MMM-yyyy").format(claimListObj.getDate(13)));
				resBean.setAssignedSna(claimListObj.getString(14));
				resBean.setAssignedCpd(claimListObj.getString(15));
				claimList.add(resBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (claimListObj != null) {
					claimListObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return claimList;
	}

	@Override
	public Response executeProcess(String urn, Integer processId) {
		Integer result = null;
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_CLAIM_TESTING_EXEC")
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_process", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.setParameter("p_from_date", null);
			storedProcedureQuery.setParameter("p_to_date", null);
			storedProcedureQuery.setParameter("p_state_code", null);
			storedProcedureQuery.setParameter("p_dist_code", null);
			storedProcedureQuery.setParameter("p_hsptl_code", null);
			storedProcedureQuery.setParameter("p_flag", 1);
			storedProcedureQuery.setParameter("p_process", processId);
			storedProcedureQuery.execute();
			result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msg");
			if (result == 1) {
				response.setStatus("Success");
				response.setMessage("Process Executed Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Something Went Worng");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Something Went Worng");
		}
		return response;
	}

}
