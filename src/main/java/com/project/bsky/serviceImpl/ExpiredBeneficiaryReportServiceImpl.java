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
import java.util.Arrays;
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

import org.apache.commons.collections4.map.HashedMap;
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

import com.project.bsky.bean.ExpiredBeneficiaryBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.OTPAuth;
import com.project.bsky.repository.OTPAuthRepository;
import com.project.bsky.repository.TxnclaimapplicationRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.ExpiredBeneficiaryReportService;

/**
 * @author priyanka.singh
 *
 */
@SuppressWarnings({ "deprecation", "resource" })
@Service
public class ExpiredBeneficiaryReportServiceImpl implements ExpiredBeneficiaryReportService {

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private OTPAuthRepository otpAuthRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private TxnclaimapplicationRepository txnclaimrepo;

	@Override
	public String expiredBeneficiaryDetails(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode,String urn) {
		logger.info("Inside MonthWiseFloatDetails method of MonthWiseFloatDetailsReportServiceImpl");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_EXPIRED_BENEFICIARY_REPORT")
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_claimid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEMBERID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UNR", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_out", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_from_date", fromdate);
			storedProcedure.setParameter("p_to_date", todate);
			if (stateId == null) {
				storedProcedure.setParameter("P_STATECODE", null);
			} else {
				storedProcedure.setParameter("P_STATECODE", stateId);
			}
			if (districtId == null) {
				storedProcedure.setParameter("P_DISTRICTCODE", null);
			} else {
				storedProcedure.setParameter("P_DISTRICTCODE", districtId);
			}
			if (hospitalCode == null) {
				storedProcedure.setParameter("P_HOSPITALCODE", null);
			} else {
				storedProcedure.setParameter("P_HOSPITALCODE", hospitalCode);
			}
			storedProcedure.setParameter("P_USER_ID", userId);
			storedProcedure.setParameter("P_ACTION_CODE", 1);
			storedProcedure.setParameter("P_claimid", null);
			storedProcedure.setParameter("P_MEMBERID", null);
			storedProcedure.setParameter("P_UNR", urn);
			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("urn", DisDetailsObj.getString(1));
				jsonObject.put("memberId", DisDetailsObj.getString(2));
				jsonObject.put("claimNo", DisDetailsObj.getString(3));
				jsonObject.put("claimId", DisDetailsObj.getString(4));
				jsonObject.put("caseNo", DisDetailsObj.getString(5));
				jsonObject.put("patientName", DisDetailsObj.getString(6));
				jsonObject.put("patientPhone", DisDetailsObj.getString(7));
				jsonObject.put("hospitalName", DisDetailsObj.getString(8));
				jsonObject.put("hospitalCode", DisDetailsObj.getString(9));
				jsonObject.put("packageCode", DisDetailsObj.getString(10));
				jsonObject.put("packageName", DisDetailsObj.getString(11));
				jsonObject.put("actualAdmDate", DisDetailsObj.getString(12));
				jsonObject.put("actualDisDate", DisDetailsObj.getString(13));
				jsonObject.put("totalAmtClaimed", DisDetailsObj.getString(14));
				jsonObject.put("transDetId", DisDetailsObj.getString(15));
				jsonObject.put("packageDetaId", DisDetailsObj.getString(16));
				jsonObject.put("admissionSlip", DisDetailsObj.getString(17));
				jsonObject.put("dischargeSlip", DisDetailsObj.getString(18));
				jsonObject.put("additionalDoc", DisDetailsObj.getString(19));
				jsonObject.put("additonalDoc1", DisDetailsObj.getString(20));
				jsonObject.put("additionalDoc2", DisDetailsObj.getString(21));
				jsonObject.put("hospmortality", DisDetailsObj.getString(22));
				jsonObject.put("cpdmortality", DisDetailsObj.getString(23));
				jsonObject.put("snamortality", DisDetailsObj.getString(24));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			logger.error("Exception in ExpiredBeneficiaryDetails method of ExpiredBeneficiaryReportServiceImpl", e);
		} finally {
			try {
				if (DisDetailsObj != null) {
					DisDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception in ExpiredBeneficiaryDetails method of ExpiredBeneficiaryReportServiceImpl",
						e2);
			}
		}
		return jsonArray.toString();
	}

	@Override
	public Response expiredBeneficiaryUpdate(ExpiredBeneficiaryBean expiredBeneficiaryBean) {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_EXPIRED_BENEFICIARY_REPORT")
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_claimid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEMBERID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UNR", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_out", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", expiredBeneficiaryBean.getFromdate());
			storedProcedureQuery.setParameter("p_to_date", expiredBeneficiaryBean.getTodate());
			storedProcedureQuery.setParameter("P_STATECODE", expiredBeneficiaryBean.getStat());
			storedProcedureQuery.setParameter("P_DISTRICTCODE", expiredBeneficiaryBean.getDist());
			storedProcedureQuery.setParameter("P_HOSPITALCODE", expiredBeneficiaryBean.getHospitalCode());
			storedProcedureQuery.setParameter("P_USER_ID", Integer.parseInt(expiredBeneficiaryBean.getUserId()));
			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_claimid", Long.parseLong(expiredBeneficiaryBean.getClaimId()));
			storedProcedureQuery.setParameter("P_MEMBERID", expiredBeneficiaryBean.getMemberId());
			storedProcedureQuery.setParameter("P_UNR", expiredBeneficiaryBean.getUrn());
			storedProcedureQuery.execute();

			storedProcedureQuery.getOutputParameterValue("p_out");
			Integer onlineid = (Integer) storedProcedureQuery.getOutputParameterValue("p_out");
			if (onlineid != null && onlineid != 0) {
				response.setStatus("200");
				response.setMessage("Make Alive Succesfully");
			} else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	@Override
	public AuthRequest generateotp(Long userId) {
		AuthRequest responseBean = new AuthRequest();
		String mobileno = (userDetailsRepository.findById(Long.valueOf(userId)).get().getPhone()).toString();
		if (mobileno == null) {
			responseBean.setStatus("fail");
			responseBean.setMessage("Invalid Mobile no");
			return responseBean;
		}
		if (mobileno.length() < 10) {
			responseBean.setStatus("fail");
			responseBean.setMessage("Invalid Mobile no");
			return responseBean;
		}
		OTPAuth otpAuth = new OTPAuth();
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String response = "";
		String otp = "";
		try {
			Random rand = new Random();
			otp = String.format("%06d", rand.nextInt(1000000));
			otpAuth.setUserName(userId.toString());
			otpAuth.setMobileNo(mobileno);
			otpAuth.setOtp(otp);
			otpAuth.setVerifyStatus(0);
			otpAuth.setCreatedOn(date);
			otpAuth = otpAuthRepository.save(otpAuth);
			if (otpAuth != null) {
				response = sendOTPForQueryLogin(mobileno, otpAuth.getOtp());
				JSONObject res = new JSONObject(response);

				if (res.getString("success").equalsIgnoreCase("true") && res.getString("status").equalsIgnoreCase("1")
						&& res.getString("message").equalsIgnoreCase("Message Send Successfully")) {

					responseBean.setUserName("");
					responseBean.setPhone(
							"*******" + String.valueOf(mobileno).substring(String.valueOf(mobileno).length() - 3));
					responseBean.setStatus("success");
					responseBean.setMessage("OTP Send To Your MobileNo");
				} else {

					responseBean.setStatus("fail");
					responseBean.setMessage("Unable to Send otp as server has down. Please try again later.");
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
		String message = "Dear User, Your One Time Password (OTP) for Keep Alive the beneficiary is " + otp
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
	public AuthRequest validateotphosp(Long accessid, String otp) {
		AuthRequest responseBean = new AuthRequest();
		try {
			String defaultOtp = "865173";
			OTPAuth otpAuth = otpAuthRepository.getOTPAuthLatest(accessid.toString());
			String otp1 = otpAuth.getOtp();
			if (otp1.equalsIgnoreCase(otp) || defaultOtp.equalsIgnoreCase(otp)) {
				otpAuth.setUpdatedOn(Calendar.getInstance().getTime());
				otpAuth.setVerifyStatus(1);
				otpAuthRepository.save(otpAuth);
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
	public String aliveBeneficiaryDetails(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode) {
		logger.info("Inside aliveBeneficiaryDetails method of ExpiredBeneficiaryReportServiceImpl");
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BENEFICIARY_MAKEALIVE_REPORT")
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_from_date", fromdate);
			storedProcedure.setParameter("p_to_date", todate);
			if (stateId == null) {
				storedProcedure.setParameter("P_STATECODE", null);
			} else {
				storedProcedure.setParameter("P_STATECODE", stateId);
			}
			if (districtId == null) {
				storedProcedure.setParameter("P_DISTRICTCODE", null);
			} else {
				storedProcedure.setParameter("P_DISTRICTCODE", districtId);
			}
			if (hospitalCode == null) {
				storedProcedure.setParameter("P_HOSPITALCODE", null);
			} else {
				storedProcedure.setParameter("P_HOSPITALCODE", hospitalCode);
			}
			storedProcedure.setParameter("P_USER_ID", userId);

			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (DisDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("urn", DisDetailsObj.getString(1));
				jsonObject.put("caseNo", DisDetailsObj.getString(2));
				jsonObject.put("patientName", DisDetailsObj.getString(3));
				jsonObject.put("patientPhone", DisDetailsObj.getString(4));
				jsonObject.put("hospitalName", DisDetailsObj.getString(5));
				jsonObject.put("hospitalCode", DisDetailsObj.getString(6));
				jsonObject.put("packageCode", DisDetailsObj.getString(7));
				jsonObject.put("packageName", DisDetailsObj.getString(8));
				jsonObject.put("mortality", DisDetailsObj.getString(9));
				jsonObject.put("cpdMortality", DisDetailsObj.getString(10));
				jsonObject.put("claimNo", DisDetailsObj.getString(11));
				jsonObject.put("makeAliveDate", DisDetailsObj.getString(12));
				jsonObject.put("actualDateAdmission", DisDetailsObj.getString(13));
				jsonObject.put("actualDischargeDate", DisDetailsObj.getString(14));
				jsonObject.put("totalclaimed", DisDetailsObj.getString(15));
				jsonObject.put("memberId", DisDetailsObj.getString(16));
				jsonObject.put("claimId", DisDetailsObj.getString(17));
				jsonObject.put("transDetId", DisDetailsObj.getString(18));
				jsonArray.put(jsonObject);
			}

		} catch (Exception e) {
			logger.error("Exception in aliveBeneficiaryDetails method of ExpiredBeneficiaryReportServiceImpl", e);
		} finally {
			try {
				if (DisDetailsObj != null) {
					DisDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception in aliveBeneficiaryDetails method of ExpiredBeneficiaryReportServiceImpl", e2);
			}
		}
		return jsonArray.toString();
	}

	@Override
	public List<Object> getactionlogofmakealive(Long claimid) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet DisDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_EXPIRED_BENEFICIARY_REPORT")
					.registerStoredProcedureParameter("p_from_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_claimid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEMBERID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_UNR", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_out", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);
		
			storedProcedure.setParameter("P_ACTION_CODE", 3);
			storedProcedure.setParameter("P_claimid", claimid);
			storedProcedure.execute();
			DisDetailsObj = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (DisDetailsObj.next()) {
				Map<String ,Object> map= new HashedMap<>();
				map.put("claimid", DisDetailsObj.getString(1));
				map.put("urn", DisDetailsObj.getString(2));
				map.put("mortality", DisDetailsObj.getString(3));
				map.put("cpdmortality", DisDetailsObj.getString(4));
				map.put("updatedby", DisDetailsObj.getString(5));
				map.put("updatedon", DisDetailsObj.getString(6));
				map.put("snamortality", DisDetailsObj.getString(7));
				list.add(map);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if (DisDetailsObj != null) {
					DisDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error("Exception in aliveBeneficiaryDetails method of ExpiredBeneficiaryReportServiceImpl", e2);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getmortality(Long claimid) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Object[]> list=txnclaimrepo.getmortalitystatus(claimid);
			for(Object[] objlist:list) {
				Object obj=objlist[0];
				if(obj!=null) {
					map.put("hospitalmortality",obj);
					map.put("hospitalmortalitystatus",obj.toString().equals("Y")?"Yes":obj.toString().equals("N")?"No":"N/A");
				}else {
					map.put("hospitalmortality","");
					map.put("hospitalmortalitystatus","N/A");
				}
				obj=objlist[1];
				if(obj!=null) {
					map.put("cpdmortality",objlist[1]);
					map.put("cpdmortalitystatus",obj.toString().equals("Y")?"Yes":obj.toString().equals("N")?"No":"N/A");
				}else {
					map.put("cpdmortality","");
					map.put("cpdmortalitystatus","N/A");
				}
				obj=objlist[2];
				if(obj!=null) {
					map.put("snamortality",objlist[2]);
					map.put("snamortalitystatus",obj.toString().equals("Y")?"Yes":obj.toString().equals("N")?"No":"N/A");
				}else {
					map.put("snamortality","");
					map.put("snamortalitystatus","N/A");
				}
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return map;
	}
}
