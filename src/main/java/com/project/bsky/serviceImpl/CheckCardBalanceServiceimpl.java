/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CheackCardBalancebean;
import com.project.bsky.bean.CheckcardbalancedataBean;
import com.project.bsky.bean.UrnWiseActionBean;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.OTPAuth;
import com.project.bsky.model.RationCardUser;
import com.project.bsky.model.RationCardUserLog;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.OTPAuthRepository;
import com.project.bsky.repository.RationCardUserLogRepository;
import com.project.bsky.repository.RationCardUserRepository;
import com.project.bsky.service.CheckCardBalanceService;
import com.project.bsky.util.JwtUtil;

/**
 * @author rajendra.sahoo
 *
 */
@SuppressWarnings({ "deprecation", "resource" })
@Service
public class CheckCardBalanceServiceimpl implements CheckCardBalanceService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RationCardUserRepository rationcardrepo;

	@Autowired
	private OTPAuthRepository otpAuthRepository;

	@Autowired
	private RationCardUserLogRepository rationcardlogrepo;

	@Autowired
	private Logger logger;

	@Autowired
	private DistrictMasterRepository districtrepo;

	@Autowired
	private JwtUtil util;

	@Override
	public CheackCardBalancebean checkcardbalance(String urn, String search, Long schemeId, Long schemeCategoryId) {
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<Object> list = new ArrayList<Object>();
		List<Object> list1 = new ArrayList<Object>();
		List<Object> list2 = new ArrayList<Object>();
		CheackCardBalancebean cardbalance = new CheackCardBalancebean();
		String ipAddress = util.getLocalIp();
		Long userId = util.getCurrentUser();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CHECK_CARD_BALANCE_RPT")
					.registerStoredProcedureParameter("p_acode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMEID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSGOUT1", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSGOUT2", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_acode", search);
			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.setParameter("P_SCHEMEID", schemeId);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemeCategoryId);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.setParameter("P_USER_IP", ipAddress);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT1");
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT2");
			while (rs.next()) {
				CheckcardbalancedataBean bean = new CheckcardbalancedataBean();
				bean.setAvailablebalance(rs.getString(1));
				bean.setAmountBlcked(rs.getString(2));
				bean.setClaimamount(rs.getString(3));
				bean.setFemalefond(rs.getString(4));
				bean.setPolicystdate(rs.getString(5));
				bean.setPoliceenddate(rs.getString(6));
				list.add(bean);
			}
			cardbalance.setCardbalance(list);
			while (rs1.next()) {
				CheckcardbalancedataBean bean = new CheckcardbalancedataBean();
				bean.setCardno(rs1.getString(1));
				bean.setFullname(rs1.getString(2));
				bean.setGender(rs1.getString(3));
				bean.setDist(rs1.getString(4));
				bean.setBlock(rs1.getString(5));
				bean.setWord(rs1.getString(6));
				bean.setVillage(rs1.getString(7));
				bean.setScheme(rs1.getString(8));
				list1.add(bean);
			}
			cardbalance.setBenificiary(list1);
			while (rs2.next()) {
				CheckcardbalancedataBean bean = new CheckcardbalancedataBean();
				bean.setCardno(rs2.getString(1));
				bean.setFullname(rs2.getString(2));
				bean.setGender(rs2.getString(3));
				bean.setRelation(rs2.getString(4));
				bean.setAge(rs2.getString(5));
				bean.setMortality(rs2.getString(6));
				list2.add(bean);
			}
			cardbalance.setFamily(list2);
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
				if (rs2 != null) {
					rs2.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return cardbalance;
	}

	@Override
	public CheackCardBalancebean checkbeneficry(String urn, String search, Long accessid, Long schemeId,
			Long schemeCategoryId) {
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<Object> list1 = new ArrayList<Object>();
		List<Object> list2 = new ArrayList<Object>();
		CheackCardBalancebean cardbalance = new CheackCardBalancebean();
		try {
			RationCardUserLog log = new RationCardUserLog();
			RationCardUser user = rationcardrepo.findById(accessid).get();
			log.setAccessid(user.getAccessid());
			log.setAccessusername(user.getAccessusername());
			log.setCardno(urn);
			log.setMobileno(user.getMobileno());
			if (search.equals("A")) {
				log.setSearchtype("Ration Card No./URN");
			} else if (search.equals("B")) {
				log.setSearchtype("Aadhar Card No.");
			} else {
				log.setSearchtype("N/A");
			}
			log.setCreatedon(Calendar.getInstance().getTime());
			rationcardlogrepo.save(log);

			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CHECK_BENEFICIARY_RPT")
					.registerStoredProcedureParameter("p_acode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMEID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT1", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSGOUT2", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_acode", search);
			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.setParameter("P_SCHEMEID", schemeId);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemeCategoryId);
			storedProcedureQuery.execute();
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT1");
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT2");
			while (rs1.next()) {
				CheckcardbalancedataBean bean = new CheckcardbalancedataBean();
				bean.setCardno(rs1.getString(1));
				bean.setFullname(rs1.getString(2));
				bean.setGender(rs1.getString(3));
				bean.setDist(rs1.getString(4));
				bean.setBlock(rs1.getString(5));
				bean.setWord(rs1.getString(6));
				bean.setVillage(rs1.getString(7));
				bean.setCardtype(rs1.getString(8));
				bean.setNameinodia(rs1.getString(9));
				bean.setAdharno(rs1.getString(10));
				bean.setSpfullname(rs1.getString(11));
				bean.setFathername(rs1.getString(12));
				bean.setMobileno(rs1.getString(13));
				bean.setFpsname(rs1.getString(14));
				bean.setSchemetype(rs1.getString(15));
				bean.setExpdate(rs1.getString(16));
				bean.setUpdate(rs1.getString(17));
				list1.add(bean);
			}
			cardbalance.setBenificiary(list1);
			while (rs2.next()) {
				CheckcardbalancedataBean bean = new CheckcardbalancedataBean();
				bean.setCardno(rs2.getString(1));
				bean.setFullname(rs2.getString(2));
				bean.setGender(rs2.getString(3));
				bean.setRelation(rs2.getString(4));
				bean.setAge(rs2.getString(5));
				bean.setNameinodia(rs2.getString(6));
				bean.setAdharno(rs2.getString(7));
				bean.setDob(rs2.getString(8));
				bean.setSchemetype(rs2.getString(9));
				bean.setExpdate(rs2.getString(10));
				bean.setUpdate(rs2.getString(11));
				bean.setMobileno(rs2.getString(12) != null ? rs2.getString(12) : "N/A");
				list2.add(bean);
			}
			cardbalance.setFamily(list2);
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
				if (rs2 != null) {
					rs2.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return cardbalance;
	}

	@Override
	public List<RationCardUser> getaccessuserlist() {
		List<RationCardUser> list = new ArrayList<RationCardUser>();
		try {
			list = rationcardrepo.getall();
		} catch (Exception e) {
		}
		return list;
	}

	@Override
	public AuthRequest generateotp(Long accessid) {
		OTPAuth otpAuth = new OTPAuth();
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		AuthRequest responseBean = new AuthRequest();
		String response = "";
		String otp = "";
		try {
			Random rand = new Random();
			otp = String.format("%06d", rand.nextInt(1000000));

			RationCardUser user = rationcardrepo.findById(accessid).get();
			if (user != null) {
				if (user.getMobileno() != null) {
					if (user.getMobileno().length() == 10) {
						otpAuth.setUserName(user.getAccessusername());
						otpAuth.setMobileNo(user.getMobileno());
						otpAuth.setOtp(otp);
						otpAuth.setVerifyStatus(0);
						otpAuth.setCreatedOn(date);
						otpAuth = otpAuthRepository.save(otpAuth);

						if (otpAuth != null) {

							response = sendOTPForQueryLogin(user.getMobileno(), otpAuth.getOtp());
							JSONObject res = new JSONObject(response);

							if (res.getString("success").equalsIgnoreCase("true")
									&& res.getString("status").equalsIgnoreCase("1")
									&& res.getString("message").equalsIgnoreCase("Message Send Successfully")) {

								responseBean.setUserName(user.getAccessusername());
								responseBean.setPhone("*******" + String.valueOf(user.getMobileno())
										.substring(String.valueOf(user.getMobileno()).length() - 3));
								responseBean.setStatus("success");
								responseBean.setMessage("OTP Send To Your MobileNo");
							} else {

								responseBean.setStatus("fail");
								responseBean
										.setMessage("Unable to Send otp as server has down. Please try again later.");
							}
						}
					} else {
						responseBean.setStatus("fail");
						responseBean.setMessage("Mobile no does not exist");
					}
				} else {
					responseBean.setStatus("fail");
					responseBean.setMessage("Mobile no does not exist");
				}
			}
		} catch (Exception e) {
			responseBean.setStatus("fail");
			responseBean.setMessage("Some error happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseBean;
	}

	public String sendOTPForQueryLogin(String mobileNo, String otp) {
		String responseString = null;
		String message = "Dear User, Your One Time Password (OTP) for Check Beneficiary is " + otp
				+ ". Don't share it with anyone. BSKY, Govt. of Odisha";
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("https://govtsms.odisha.gov.in/api/api.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("action", "singleSMS"));
			nameValuePairs.add(new BasicNameValuePair("department_id", "D006001"));
			nameValuePairs.add(new BasicNameValuePair("template_id", "1007480143063815155"));
			nameValuePairs.add(new BasicNameValuePair("sms_content", message));
			nameValuePairs.add(new BasicNameValuePair("phonenumber", mobileNo));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = bf.readLine()) != null) {
				responseString = line;

			}
			return responseString;
		} catch (UnsupportedEncodingException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (ClientProtocolException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseString;

	}

	@Override
	public AuthRequest validateotpchkbalance(Long accessid, String otp) {
		AuthRequest responseBean = new AuthRequest();
		try {
			RationCardUser user = rationcardrepo.findById(accessid).get();
			OTPAuth otpAuth = otpAuthRepository.getOTPAuthLatest(user.getAccessusername());
			String otp1 = otpAuth.getOtp();
			if (otp1.equalsIgnoreCase(otp)) {
				responseBean.setStatus("success");
				responseBean.setMessage("OTP validate Successful");
			} else {
				responseBean.setStatus("fail");
				responseBean.setMessage("Invalid OTP");
			}
		} catch (Exception e) {
			responseBean.setStatus("fail");
			responseBean.setMessage("Some error happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseBean;
	}

	@Override
	public List<Object> beneficiarysearchbyname(String distid, String searchtype, String textvalue, Integer schemeId,
			String schemeCategoryId) throws Exception {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		Integer schemecatId = null;
		if (schemeCategoryId != null && !schemeCategoryId.equals("")) {
			schemecatId = Integer.parseInt(schemeCategoryId);
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_BENEFICIARY_SEARCH_BY_NAME_RPT")
					.registerStoredProcedureParameter("P_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONCODE2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TEXTVALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMEID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", searchtype);
			storedProcedureQuery.setParameter("P_ACTIONCODE2", "1");
			storedProcedureQuery.setParameter("P_TEXTVALUE", textvalue);
			storedProcedureQuery.setParameter("P_DISTRICT", distid);
			storedProcedureQuery.setParameter("P_SCHEMEID", schemeId);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemecatId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				CheckcardbalancedataBean bean = new CheckcardbalancedataBean();
				bean.setState(rs.getString(1));
				bean.setDist(rs.getString(2));
				bean.setBlock(rs.getString(3));
				bean.setWord(rs.getString(4));
				bean.setVillage(rs.getString(5));
				bean.setCardno(rs.getString(6));
				bean.setFullname(rs.getString(7));
				bean.setMobileno(rs.getString(8) == null ? "N/A" : rs.getString(8));
				bean.setAdharno(rs.getString(9));
				bean.setGender(rs.getString(10));
				bean.setAge(rs.getString(11));
				bean.setHeadName(rs.getString(12));
				bean.setRelation(rs.getString(13));
				bean.setCardtype(rs.getString(14));
				bean.setFpsname(rs.getString(15));
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getDistrictListofnfsa() throws Exception {
//		;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			List<Object[]> objlist = new ArrayList<Object[]>();
			objlist = districtrepo.getDistrictListofnfsa();
			for (Object[] obj : objlist) {
				Map<String, Object> details = new HashMap<>();
				details.put("districtcode", obj[0]);
				details.put("districtname", obj[1]);
				details.put("lgddistrictcode", obj[2]);
				list.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		}
		return list;
	}

	@Override
	public List<Object> getlistthroughurn(String urn, Integer schemeId, String schememCategoryId) throws Exception {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		Integer schemecatId = null;
		if (schememCategoryId != null && !schememCategoryId.equals("") && !schememCategoryId.equals("undefined	")) {
			schemecatId = Integer.parseInt(schememCategoryId);
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_BENEFICIARY_SEARCH_BY_NAME_RPT")
					.registerStoredProcedureParameter("P_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTIONCODE2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TEXTVALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMEID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", null);
			storedProcedureQuery.setParameter("P_ACTIONCODE2", "2");
			storedProcedureQuery.setParameter("P_TEXTVALUE", urn);
			storedProcedureQuery.setParameter("P_DISTRICT", null);
			storedProcedureQuery.setParameter("P_SCHEMEID", schemeId);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemecatId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				UrnWiseActionBean details = new UrnWiseActionBean();
				details.setUrn(rs.getString(1));
				details.setTransactionId(rs.getString(2));
				details.setClaimRaiseStatus(rs.getString(3));
				details.setHospitalName(rs.getString(4));
				details.setPatientName(rs.getString(5));
				details.setClaimStatus(rs.getString(6));
				details.setActualDateDischarge(rs.getString(7));
				details.setActualDateAdmission(rs.getString(8));
				details.setClaimNo(rs.getString(9));
				details.setCaseNo(rs.getString(10));
				details.setInvoiceNumber(rs.getString(11));
				details.setHospitalCode(rs.getString(12));
				details.setClaimId(rs.getString(13));
				details.setCreatedon(rs.getString(14));
				list.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		}
		System.out.println(list.size());
		System.out.println(urn);
		return list;
	}

	@Override
	public Map<String, Object> getcarddetailsthroughurn(String urn, String search, Integer schemeidvalue,
			String schemeCategoryIdValue) throws Exception {
		ResultSet rs1 = null, rs2 = null;
		Map<String, Object> details = new HashMap<>();
		List<CheckcardbalancedataBean> list2 = new ArrayList<CheckcardbalancedataBean>();
		Integer schemecatId = null;
		if (schemeCategoryIdValue != null && !schemeCategoryIdValue.equals("")) {
			schemecatId = Integer.parseInt(schemeCategoryIdValue);
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CHECK_BENEFICIARY_RPT")
					.registerStoredProcedureParameter("p_acode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMEID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT1", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSGOUT2", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_acode", search);
			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.setParameter("P_SCHEMEID", schemeidvalue);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemecatId);
			storedProcedureQuery.execute();
			rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT1");
			rs2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT2");
			CheckcardbalancedataBean bean1 = new CheckcardbalancedataBean();
			while (rs1.next()) {
				bean1.setCardno(rs1.getString(1));
				bean1.setFullname(rs1.getString(2));
				bean1.setGender(rs1.getString(3));
				bean1.setDist(rs1.getString(4));
				bean1.setBlock(rs1.getString(5));
				bean1.setWord(rs1.getString(6));
				bean1.setVillage(rs1.getString(7));
				bean1.setCardtype(rs1.getString(8));
				bean1.setNameinodia(rs1.getString(9));
				bean1.setAdharno(rs1.getString(10));
				bean1.setSpfullname(rs1.getString(11));
				bean1.setFathername(rs1.getString(12));
				bean1.setMobileno(rs1.getString(13));
				bean1.setFpsname(rs1.getString(14));
				bean1.setSchemetype(rs1.getString(15));
				bean1.setExpdate(rs1.getString(16));
				bean1.setUpdate(rs1.getString(17));
			}
			details.put("benificiary", bean1);
			while (rs2.next()) {
				CheckcardbalancedataBean bean = new CheckcardbalancedataBean();
				bean.setCardno(rs2.getString(1));
				bean.setFullname(rs2.getString(2));
				bean.setGender(rs2.getString(3));
				bean.setRelation(rs2.getString(4));
				bean.setAge(rs2.getString(5));
				bean.setNameinodia(rs2.getString(6));
				bean.setAdharno(rs2.getString(7));
				bean.setDob(rs2.getString(8));
				bean.setSchemetype(rs2.getString(9));
				bean.setExpdate(rs2.getString(10));
				bean.setUpdate(rs2.getString(11));
				bean.setMobileno(rs2.getString(12) != null ? rs2.getString(12) : "N/A");
				list2.add(bean);
			}
			details.put("family", list2);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e.getMessage());
			}
		}
		return details;
	}

	@Override
	public String getCardBalanceLog() throws Exception { 
		ResultSet snoDetailsObj = null;
		JSONObject jsonObject2 = null;
		JSONArray jsonArray1 = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CHECK_CARD_BALANCE_RPT_LOG")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);
			storedProcedureQuery.setParameter("P_USERID", util.getCurrentUser());
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (snoDetailsObj.next()) {
				jsonObject2 = new JSONObject();
				jsonObject2.put("schemeCategoryName", snoDetailsObj.getString(1));
				jsonObject2.put("searchType", snoDetailsObj.getString(2));
				jsonObject2.put("searchNo", snoDetailsObj.getString(3));
				jsonObject2.put("searchOn", snoDetailsObj.getString(4));
				jsonObject2.put("fullName", snoDetailsObj.getString(5));
				jsonArray1.put(jsonObject2);
			}
		} catch (Exception e) {
			logger.error("Error in getCardBalanceLog method of PreAuthDaoImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getCardBalanceLog method of PreAuthDaoImpl class:", e2);
			}
		}
		return jsonArray1.toString();
	}
}
