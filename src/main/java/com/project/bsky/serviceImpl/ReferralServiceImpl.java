package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Dctaggedhospitalwise;
import com.project.bsky.bean.ReferralBean;
import com.project.bsky.bean.ReferralVitalParametersBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HealthCardSample;
import com.project.bsky.model.Referral;
import com.project.bsky.model.ReferralVitalParameters;
import com.project.bsky.model.Scheme;
import com.project.bsky.model.SchemeCategoryMaster;
import com.project.bsky.repository.ReferralRepository;
import com.project.bsky.repository.ReferralVitalParametersRepository;
import com.project.bsky.repository.SchemeCategoryMasterRepository;
import com.project.bsky.repository.SchemeRepository;
import com.project.bsky.service.ReferralService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.RandomNumber;

@SuppressWarnings({ "unused", "unchecked" })
@Service
public class ReferralServiceImpl implements ReferralService {

	@Autowired
	private ReferralRepository referralRepository;
	
	@Autowired
	private SchemeCategoryMasterRepository schemeCategoryRepository;
	
	@Autowired
	private SchemeRepository schemeRepository;
	
	@Autowired
	private ReferralVitalParametersRepository referralVitalParametersRepository;

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	private static ResourceBundle bskyResourcesBundel3 = ResourceBundle.getBundle("fileConfiguration");

	@Value("${file.path.OverRideCode:}")
	private String file;

	@Override
	public Response saveReferals(ReferralBean referralBean) {
		Response response = new Response();
		try {
			Referral referral = new Referral();
			Referral refer = referralRepository.findByMemberIdAndIsActiveAndDeletedFlag(referralBean.getMemberId(), 0,
					0);
			if (Objects.nonNull(refer)) {
				refer.setIsActive(2);
				referralRepository.save(refer);
			}
			referral.setCreatedBy(referralBean.getCreatedBy());
			referral.setUpdatedBy(-1);
			referral.setCreatedOn(new Date());
			referral.setUpdatedOn(new Date());
			referral.setDeletedFlag(0);
			referral.setAge(referralBean.getAge());
			referral.setUrn(referralBean.getUrn());
			referral.setPatientName(referralBean.getPatientName());
			referral.setMemberId(referralBean.getMemberId());
			referral.setReferralDate(referralBean.getReferralDate());
			referral.setGender(referralBean.getGender());
			referral.setRegdno(referralBean.getRegdno());
			referral.setFromHospitalName(referralBean.getFromHospitalName());
			referral.setFromDrName(referralBean.getFromDrName());
			referral.setFromDeptName(referralBean.getFromDeptName());
			referral.setFromReferralDate(referralBean.getFromReferralDate());
			referral.setStateId(referralBean.getStateId());
			referral.setDistrictId(referralBean.getDistrictId());
			String str1 = referralBean.getToHospital();
			String[] str2 = str1.split("\\(");
			String name = str2[0];
			referral.setToHospital(name);
			String ans = str1.substring(str1.indexOf("(") + 1, str1.indexOf(")"));
			referral.setToHospitalCode(ans);
			referral.setReasonForRefer(referralBean.getReasonForRefer());
			referral.setToReferralDate(referralBean.getToReferralDate());
			referral.setDiagnosis(referralBean.getDiagnosis());
			referral.setBriefHistory(referralBean.getBriefHistory());
			referral.setTreatmentGiven(referralBean.getTreatmentGiven());
			referral.setInvestigationRemark(referralBean.getInvestigationRemark());
			referral.setTreatmentAdvised(referralBean.getTreatmentAdvised());
			referral.setApprovedBy(referralBean.getApprovedBy());
			referral.setApprovedDate(new Date());
			referral.setReferralCode(RandomNumber.getRandomNumberString());
			referral.setReferralStatus("Y");
			referral.setReferredThrough("AD");
			referral.setIsEmpaneled("S");
			referral.setIsActive(0);
			referral.setSchemeId(referralBean.getSchemeName()==null?1:referralBean.getSchemeName());
			referral.setSchemeCategoryId(referralBean.getCategoryName());
			Referral ref = referralRepository.save(referral);
			if (Objects.nonNull(referral)) {
				List<ReferralVitalParametersBean> param = referralBean.getVitalParam();
				for (ReferralVitalParametersBean referalVital : param) {
					ReferralVitalParameters vital = new ReferralVitalParameters();
					vital.setReferral(referral);
					String vitalName = referalVital.getVital();
					String vitalNm = vitalName.substring(vitalName.indexOf("(") + 1, vitalName.indexOf(")"));
					vital.setVital(Long.parseLong(vitalNm));
					vital.setValue(referalVital.getValue());
					vital.setUploadDate(new Date());
					vital.setCreatedBy(-1);
					vital.setUpdatedBy(-1);
					vital.setCreatedOn(new Date());
					vital.setUpdatedOn(new Date());
					vital.setDeletedFlag(0);
					ReferralVitalParameters dbObject = referralVitalParametersRepository.save(vital);
				}
				response.setMessage(String.valueOf(ref.getRefId()));
				response.setStatus("Success");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public void saveDoc(MultipartFile file, Long refId) {
		Referral referral = null;
		try {
			referral = referralRepository.findById(refId).get();
			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			String strDate = dateFormat.format(date);
			Map<String, String> filePath;
			filePath = CommonFileUpload.saveReferalDoc(strDate.substring(0, 4), referral.getUrn().trim(),
					referral.getToHospitalCode().trim(), file);
			referral.setDocument(filePath.get("file"));
			referralRepository.save(referral);
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<Object> getPatientDetails(Integer userId, Date fromDate, Date toDate, String hospitacode) {
		ResultSet patientObj = null;
		List<Object> patientDetails = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_REFERRALPATIENT")
					.registerStoredProcedureParameter("P_userId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_fromDate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_toDate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_userId", userId);
			storedProcedureQuery.setParameter("P_fromDate", fromDate);
			storedProcedureQuery.setParameter("P_toDate", toDate);
			storedProcedureQuery.setParameter("P_hospitalcode", hospitacode);
			storedProcedureQuery.execute();
			patientObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (patientObj.next()) {
				ReferralBean rcBean = new ReferralBean();
				rcBean.setMemberId(patientObj.getLong(1));
				rcBean.setUrn(patientObj.getString(2));
				rcBean.setReferralDate(patientObj.getDate(3));
				rcBean.setReferralCode(patientObj.getString(4));
				rcBean.setPatientName(patientObj.getString(5));
				rcBean.setRegdno(patientObj.getString(6));
				rcBean.setRefId(patientObj.getLong(7));
				rcBean.setReferredThrough(patientObj.getString(8));
				rcBean.setAuthStatus(patientObj.getString(10));
				patientDetails.add(rcBean);
			}
		} catch (Exception e) {

			throw new RuntimeException(e);
		} finally {
			try {
				if (patientObj != null)
					patientObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return patientDetails;
	}

	@Override
	public List<Object> getPatientDataByID(Long id) {
		ResultSet patientObj = null;
		List<Object> patientDetails = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_REFERRALPATIENT_BYID")
					.registerStoredProcedureParameter("P_Id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_Id", id);
			storedProcedureQuery.execute();
			patientObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (patientObj.next()) {
				ReferralBean rcBean = new ReferralBean();
				rcBean.setMemberId(patientObj.getLong(1));
				rcBean.setUrn(patientObj.getString(2));
				rcBean.setHospitalCode(patientObj.getString(3));
				rcBean.setReferralDate(patientObj.getDate(4));
				rcBean.setReferralCode(patientObj.getString(5));
				rcBean.setPatientName(patientObj.getString(6));
				rcBean.setAge(patientObj.getLong(7));
				rcBean.setGender(patientObj.getString(8));
				rcBean.setRegdno(patientObj.getString(9));
				rcBean.setFromHospitalName(patientObj.getString(10));
				rcBean.setFromDrName(patientObj.getString(11));
				rcBean.setFromDeptName(patientObj.getString(12));
				rcBean.setFromReferralDate(patientObj.getDate(13));
				rcBean.setStateId(patientObj.getString(14));
				rcBean.setDistrictId(patientObj.getString(15));
				rcBean.setToHospital(patientObj.getString(16));
				rcBean.setReasonForRefer(patientObj.getString(17));
				rcBean.setToReferralDate(patientObj.getDate(18));
				rcBean.setDiagnosis(patientObj.getString(19));
				rcBean.setBriefHistory(patientObj.getString(20));
				rcBean.setTreatmentGiven(patientObj.getString(21));
				rcBean.setInvestigationRemark(patientObj.getString(22));
				rcBean.setTreatmentAdvised(patientObj.getString(23));
				rcBean.setPdfName(patientObj.getString(24));
				ArrayList<ReferralVitalParametersBean> listBean = new ArrayList<>();
				ReferralVitalParametersBean bean = new ReferralVitalParametersBean();
				bean.setVital(patientObj.getString(26));
				bean.setValue(patientObj.getString(25));
				listBean.add(bean);
				rcBean.setVitalParam(listBean);
				rcBean.setRefId(patientObj.getLong(27));
				rcBean.setReferredThrough(patientObj.getString(28));
				rcBean.setReferralStatus(patientObj.getString(29));
				rcBean.setCtrOn(patientObj.getString(30));
				rcBean.setAuthStatus(patientObj.getString(31));
				rcBean.setRemarks(patientObj.getString(32));
				rcBean.setInvestigationDoc(patientObj.getString(33));
				patientDetails.add(rcBean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (patientObj != null)
					patientObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return patientDetails;
	}

	@Override
	public List<HealthCardSample> getNameByCardNo(Integer schemeCategoryId,String urn) {
//		Response response = new Response();
//		List<HealthCardSample> cardSample = null;
//		try {
//			Query q = entityManager.createNamedQuery("findByAdhara");
//			q.setParameter(1, urn);
//			List<Object[]> a = q.getResultList();
//			if (a.size() > 0) {
//				for (Object[] obj : a) {
//					Query q1 = entityManager.createNamedQuery("findByUrn");
//					q1.setParameter(1, (String.valueOf(obj[1])));
//					List<Object[]> cardObject = q1.getResultList();
//					List<HealthCardSample> cardList = new ArrayList<>();
//					for (Object[] obj1 : cardObject) {
//						HealthCardSample card = new HealthCardSample();
//						card.setUrn(String.valueOf(obj1[0]));
//						card.setAadharNumber(String.valueOf(obj1[1]));
//						card.setFullNameEnglish(String.valueOf(obj1[2]));
//						card.setMemberId(String.valueOf(obj1[3]));
//						card.setGender(String.valueOf(obj1[4]));
//						card.setAge(Integer.parseInt(String.valueOf(obj1[5])));
//						cardList.add(card);
//					}
//					return cardList;
//				}
//			}
//			Query q1 = entityManager.createNamedQuery("findByUrn");
//			q1.setParameter(1, urn);
//			List<Object[]> cardObject = q1.getResultList();
//			if (cardObject.size() > 0) {
//				cardSample = new ArrayList<>();
//				for (Object[] obj2 : cardObject) {
//					HealthCardSample card = new HealthCardSample();
//					card.setUrn(String.valueOf(obj2[0]));
//					card.setAadharNumber(String.valueOf(obj2[1]));
//					card.setFullNameEnglish(String.valueOf(obj2[2]));
//					card.setMemberId(String.valueOf(obj2[3]));
//					card.setGender(String.valueOf(obj2[4]));
//					card.setAge(Integer.parseInt(String.valueOf(obj2[5])));
//					cardSample.add(card);
//				}
//				return cardSample;
//			}
//		} catch (Exception e) {
//			logger.error(ExceptionUtils.getStackTrace(e));
//		}
//		return cardSample;
		Response response = new Response();
		List<HealthCardSample> cardSample = new ArrayList<>();
		ResultSet patientObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GETURN_INFO_REFERRAL")
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemeCategoryId);
			storedProcedureQuery.setParameter("P_URN", urn);
			storedProcedureQuery.execute();
			patientObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (patientObj.next()) {
				HealthCardSample card = new HealthCardSample();
				card.setUrn(patientObj.getString(1));
				card.setAadharNumber(patientObj.getString(2));
				card.setFullNameEnglish(patientObj.getString(3));
				card.setMemberId(patientObj.getString(4));
				card.setGender(patientObj.getString(5));
				card.setAge(patientObj.getInt(6));
				cardSample.add(card);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}finally {
			try {
				if (patientObj != null)
					patientObj.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return cardSample;
	}

	@Override
	public HealthCardSample getAgeByName(String name) {
		HealthCardSample healthCardSample = null;
		try {
			Query q3 = entityManager.createNamedQuery("findByFullNameEnglish");
			q3.setParameter(1, name);
			List<Object[]> cardObject = q3.getResultList();
			if (cardObject.size() > 0) {
				healthCardSample = new HealthCardSample();
				HealthCardSample card = null;
				for (Object[] obj3 : cardObject) {
					card = new HealthCardSample();
					card.setUrn(String.valueOf(obj3[0]));
					card.setAadharNumber(String.valueOf(obj3[1]));
					card.setFullNameEnglish(String.valueOf(obj3[2]));
					card.setMemberId(String.valueOf(obj3[3]));
					card.setGender(String.valueOf(obj3[4]));
					card.setAge(Integer.parseInt(String.valueOf(obj3[5])));
				}
				return card;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return healthCardSample;
	}

	@Override
	public Response updatePatientDetails(ReferralBean bean) {
		Response response = new Response();
		try {
			Integer referrralnoInteger = null;
			StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("USP_REFERRALDATA_UPDATE")
					.registerStoredProcedureParameter("P_Id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_userId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_action", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_remark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);
			query.setParameter("P_Id", bean.getRefId());
			query.setParameter("P_action", bean.getAction());
			query.setParameter("P_remark", bean.getRemarks());
			query.setParameter("P_userId", bean.getUserId());
			query.execute();
			referrralnoInteger = (Integer) query.getOutputParameterValue("P_MSGOUT");
			if (referrralnoInteger == 1 && bean.getAction() == 1) {
				response.setStatus("Success");
				response.setMessage(" Authenticate Successfully");
			} else if (referrralnoInteger == 2 && bean.getAction() == 2) {
				response.setStatus("Success");
				response.setMessage(" Not Authenticate Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Something went wrong");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public void downloadFileForReferral(String fileName, String year, String hCode, HttpServletResponse response) {
		String folderName = null;
		try {
			folderName = bskyResourcesBundel3.getString("folder.Referralfile");
			CommonFileUpload.downLoadFileForOverride(fileName, response, folderName, year, hCode);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<Object> gethospitallistdcwise(Long userid) {
		ResultSet rs = null;
		List<Object> hospitallistdc = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GETHOSPITALLIST_DCWISE")
					.registerStoredProcedureParameter("P_Userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_Userid", userid);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			while (rs.next()) {
				Dctaggedhospitalwise hospitaltag = new Dctaggedhospitalwise();
				hospitaltag.setHospitalcode(rs.getString(1));
				hospitaltag.setHospitaname(rs.getString(2));
				hospitallistdc.add(hospitaltag);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return hospitallistdc;
	}

	@Override
	public List<SchemeCategoryMaster> getSchemeCategoryById(Integer schemeId) {
		return schemeCategoryRepository.findBySchemeIdAndStatusFlagOrderByCategoryName(schemeId, Integer.valueOf(0));
	}

	@Override
	public List<Scheme> getSchemeList() {
		return schemeRepository.findByStatusFlagOrderBySchemeName(Integer.valueOf(0));
	}
}
