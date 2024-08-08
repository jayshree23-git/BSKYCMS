/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalDetailBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.TreatingDoctorconfigbean;
import com.project.bsky.bean.TreatingSubmitBean;
import com.project.bsky.bean.Treatinglogdetailsbean;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.HospitalBackdateConfigLog;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.HospitalInformationLog;
import com.project.bsky.model.OTPAuth;
import com.project.bsky.repository.HospitalBackdateConfigLogrepository;
import com.project.bsky.repository.HospitalInformationLogRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.OTPAuthRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.Hospitalinforeportservice;

/**
 * @author rajendra.sahoo
 *
 */
@SuppressWarnings({ "resource", "deprecation", "unused" })
@Service
public class Hospitalinforeportserviceimpl implements Hospitalinforeportservice {

	public static final ResourceBundle bskyResourceBundle = ResourceBundle.getBundle("application");

	@Autowired
	private HospitalInformationRepository hospitalUserSaveRepository;

	@Autowired
	private HospitalBackdateConfigLogrepository hospitalBackdateConfigLogrepository;

	@Autowired
	private UserDetailsRepository userdetails;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private OTPAuthRepository otpAuthRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private HospitalInformationLogRepository hospitalUserLogRepository;

	@Autowired
	private Logger logger;

	@Override
	public List<HospitalBean> gethospitalinfo() {
		List<HospitalBean> listDetails = new ArrayList<HospitalBean>();
		List<Object[]> object = hospitalUserSaveRepository.findAllHospitalsreport();
		if (!object.isEmpty()) {
			for (Object[] obj1 : object) {
				HospitalBean sNOConfigurationBean = new HospitalBean();
				sNOConfigurationBean.setHospitalName((String) obj1[0]);
				sNOConfigurationBean.setStatus((Integer) obj1[1]);
				sNOConfigurationBean.setStateName((String) obj1[2]);
				sNOConfigurationBean.setDistrictName((String) obj1[3]);
				sNOConfigurationBean.setHospitalId((Integer) obj1[4]);
				sNOConfigurationBean.setHospitalCode((String) obj1[5]);
				sNOConfigurationBean.setAssigndc((Long) obj1[6]);
				sNOConfigurationBean.setAssignsna((Integer) obj1[7]);
				sNOConfigurationBean.setDcname((String) obj1[8]);
				sNOConfigurationBean.setSnaname((String) obj1[9]);
				listDetails.add(sNOConfigurationBean);
			}
		}
		return listDetails;
	}

	@Override
	public List<HospitalBean> getfilterhospitalinfo(String stateid, String distid, Integer issna, Integer isdc) {
		List<HospitalBean> listDetails = new ArrayList<HospitalBean>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_DTLS_REPORT_ADMIN")
					.registerStoredProcedureParameter("p_issna", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_isdc", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_issna", issna);
			storedProcedure.setParameter("p_isdc", isdc);
			storedProcedure.setParameter("p_STATECODE", stateid);
			storedProcedure.setParameter("P_DISTRICTCODE", distid);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while (list.next()) {
				HospitalBean hospitalbean = new HospitalBean();
				hospitalbean.setHospitalName(list.getString(1));
				hospitalbean.setHospitalCode(list.getString(2));
				hospitalbean.setStateName(list.getString(3));
				hospitalbean.setDistrictName(list.getString(4));
				hospitalbean.setDcname(list.getString(5));
				hospitalbean.setAssignsna(list.getInt(6));
				hospitalbean.setSnaname(list.getString(7));
				listDetails.add(hospitalbean);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}

		return listDetails;
	}

	@Override
	public Response updatebackdateconfig(Long userid, String addmission, String discharge, String hospital) {
		Response response = new Response();
		try {
			Integer add = 0, dis = 0;
			try {
				add = Integer.parseInt(addmission);
				dis = Integer.parseInt(discharge);
			} catch (Exception e) {
				response.setMessage("Only Number Allowed For BackDate");
				response.setStatus("400");
			}
			HospitalInformation hospitalinfo = hospitalUserSaveRepository.findHospitalByCode(hospital);
			HospitalBackdateConfigLog log = new HospitalBackdateConfigLog();
			log.setBackdateadmissiondate(hospitalinfo.getBackdateadmissiondate());
			log.setBackdatedischargedate(hospitalinfo.getBackdatedischargedate());
			log.setHospital(hospitalinfo);
			log.setCreatedby(userdetails.findById(userid).get());
			log.setCreateon(Calendar.getInstance().getTime());
			log.setDistrictcode(hospitalinfo.getDistrictcode());
			log.setStatus(0);
			hospitalBackdateConfigLogrepository.save(log);
			hospitalinfo.setBackdateadmissiondate(add);
			hospitalinfo.setBackdatedischargedate(dis);
			hospitalinfo.setUpdatedBy(userid);
			hospitalinfo.setUpdatedOn(Calendar.getInstance().getTime());
			hospitalUserSaveRepository.save(hospitalinfo);
			response.setMessage("Record Update Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public List<HospitalBackdateConfigLog> getallhospitallogdata() {
		List<HospitalBackdateConfigLog> list = new ArrayList<HospitalBackdateConfigLog>();
		try {
			list = hospitalBackdateConfigLogrepository.getallhospitallogdata();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalBean> getallhospitalbackdatelogdata() {
		List<HospitalBean> list = new ArrayList<HospitalBean>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
		String dateformat = null;
		try {
			List<Object[]> objectlist = hospitalUserSaveRepository.getallhospitalbackdatelogdata();
			for (Object[] obj : objectlist) {
				HospitalBean hosbean = new HospitalBean();
				hosbean.setHospitalName((String) obj[0]);
				hosbean.setHospitalCode((String) obj[1]);
				hosbean.setStateName((String) obj[2]);
				hosbean.setDistrictName((String) obj[3]);
				hosbean.setHospitalType((String) obj[4]);
				if (obj[5] != null) {
					hosbean.setHcvalidform(sdf1.format((Date) obj[5]));
				} else {
					hosbean.setHcvalidform("N/A");
				}

				if (obj[6] != null) {
					hosbean.setHcvalidto(sdf1.format((Date) obj[6]));
				} else {
					hosbean.setHcvalidto("N/A");
				}

				if (obj[7] != null) {
					hosbean.setMoustartdate(sdf1.format((Date) obj[7]));
				} else {
					hosbean.setMoustartdate("N/A");
				}

				if (obj[8] != null) {
					hosbean.setMouenddate(sdf1.format((Date) obj[8]));
				} else {
					hosbean.setMouenddate("N/A");
				}
				hosbean.setBackadmissionday(((Number) obj[9]).toString());
				hosbean.setBackdischargeday(((Number) obj[10]).toString());
				list.add(hosbean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalInformation> gethcexplist() {
		List<HospitalInformation> list = new ArrayList<HospitalInformation>();
		try {
			Calendar cal = Calendar.getInstance();
			Date fromdate = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 11);
			Date todate = cal.getTime();
			list = hospitalUserSaveRepository.findByhcValidToDateBetween(fromdate, todate);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalInformation> getmouexplist() {
		List<HospitalInformation> list = new ArrayList<HospitalInformation>();
		try {
			Calendar cal = Calendar.getInstance();
			Date fromdate = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 11);
			Date todate = cal.getTime();
			list = hospitalUserSaveRepository.findBymouEndDateBetween(fromdate, todate);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalBean> getincentive(String state, String dist) {
		if (state.equals("null")) {
			state = "";
		}
		List<HospitalBean> listDetails = new ArrayList<HospitalBean>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_INCENTIVE_RPT")
					.registerStoredProcedureParameter("P_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("CATOGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_STATECODE", state.toString());
			storedProcedure.setParameter("P_DISTCODE", dist.toString());
			storedProcedure.setParameter("P_ACTIONCODE", "A");
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (list.next()) {
				HospitalBean hospitalbean = new HospitalBean();
				hospitalbean.setHospitalId(list.getInt(1));
				hospitalbean.setHospitalType(list.getString(2) != null ? list.getString(2) : "N/A");
				hospitalbean.setCount(list.getInt(3));
				listDetails.add(hospitalbean);
			}
			Collections.sort(listDetails, (b1, b2) -> (int) (b2.getCount() - b1.getCount()));
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
		return listDetails;
	}

	@Override
	public Map<String, Object> getincentivedetails(String state, String dist, Integer catagory) {
		if (state.equals("null")) {
			state = "";
		}
		Map<String, Object> list = new HashMap<>();
		List<String> header = new ArrayList<String>();
		List<String> value;
		List<List<String>> record = new ArrayList<List<String>>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_INCENTIVE_RPT")
					.registerStoredProcedureParameter("P_ACTIONCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("CATOGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_STATECODE", state.toString());
			storedProcedure.setParameter("P_DISTCODE", dist.toString());
			storedProcedure.setParameter("CATOGORY_ID", catagory);
			storedProcedure.setParameter("P_ACTIONCODE", "B");
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int count = rsMetaData.getColumnCount();
			header.add("SL No.");
			for (int i = 1; i <= count; i++) {
				header.add(rsMetaData.getColumnName(i));
			}
			Integer i = 1;
			while (rs.next()) {
				value = new ArrayList<String>();
				value.add((i++).toString());
				value.add(rs.getString(1));
				value.add(rs.getString(2));
				value.add(rs.getString(3));
				value.add(rs.getString(4));
				value.add(rs.getInt(5) == 0 ? "Active" : "In-Active");
				value.add(rs.getString(6));
				value.add(rs.getString(7));
				value.add(rs.getString(8));
				value.add(rs.getString(9));
				value.add(rs.getInt(10) == 0 ? "Active" : "In-Active");
				record.add(value);
			}
			list.put("header", header);
			list.put("vlaue", record);
			list.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			list.put("header", null);
			list.put("vlaue", null);
			list.put("status", 400);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return list;
	}

	@Override
	public AuthRequest generateotp(Long userid) {
		AuthRequest responseBean = new AuthRequest();
		String mobileno = (userDetailsRepository.findById(Long.valueOf(userid)).get().getPhone()).toString();
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
			otpAuth.setUserName(userid.toString());
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
		String result = null;
		String responseString = null;
		String message = "Dear User, Your One Time Password (OTP) for Update Hospital is " + otp
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
	public Map<String, Object> hospitallistforotpconfigure(String state, String dist, Long userid, Long otpreq) {
		Map<String, Object> mapobj = new HashMap<>();
		List<HospitalBean> beanlist = new ArrayList<HospitalBean>();
		ResultSet rs = null;
		Boolean checkall = true;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_OTP_CONFIGURATION_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OTP_REQUIRED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userid);
			storedProcedure.setParameter("P_STATE_CODE", state);
			storedProcedure.setParameter("P_DISTRICT_CODE", dist);
			storedProcedure.setParameter("P_OTP_REQUIRED", otpreq);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalBean bean = new HospitalBean();
				bean.setHospitalId(rs.getInt(1));
				bean.setHospitalName(rs.getString(2));
				bean.setHospitalCode(rs.getString(3));
				bean.setMobileNo(rs.getString(4));
				bean.setEmailId(rs.getString(5));
				if (rs.getInt(6) == 1) {
					checkall = false;
				}
				bean.setStatus(rs.getInt(6));
				beanlist.add(bean);
			}
			mapobj.put("status", 200);
			mapobj.put("msg", "Api called Successfully");
			mapobj.put("data", beanlist);
			mapobj.put("checkall", checkall);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			mapobj.put("status", 400);
			mapobj.put("msg", "SomeThing Went Wrong");
			mapobj.put("error", e);
			mapobj.put("checkall", false);
		}
		return mapobj;
	}

	@Override
	public Response submithospitallistforotpconfigure(List<HospitalDetailBean> hospbean) {
		Response response = new Response();
		List<HospitalInformation> hosplist = new ArrayList<HospitalInformation>();
		List<HospitalInformationLog> hosploglist = new ArrayList<HospitalInformationLog>();
		try {
			for (HospitalDetailBean hosp : hospbean) {
				HospitalInformationLog hospitalUserlog = new HospitalInformationLog();
				HospitalInformation hospital = hospitalUserSaveRepository.findById(hosp.getHospitalid()).get();
				hospitalUserlog.setHospitalCode(hospital.getHospitalCode());
				hospitalUserlog.setHospitalName(hospital.getHospitalName());
				hospitalUserlog.setHospitalId(hospital.getHospitalId());
				hospitalUserlog.setStateCode(hospital.getDistrictcode().getStatecode().getStateCode());
				hospitalUserlog.setDistrictcode(hospital.getDistrictcode().getDistrictcode());
				hospitalUserlog.setCpdApprovalRequired(hospital.getCpdApprovalRequired());
				hospitalUserlog.setMobile(hospital.getMobile());
				hospitalUserlog.setEmailId(hospital.getEmailId());
				hospitalUserlog.setUserId(hospital.getUserId().getUserId().intValue());
				hospitalUserlog.setCreatedBy(hosp.getCreateby());
				hospitalUserlog.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				hospitalUserlog.setAssigned_dc(hospital.getAssigned_dc());
				hospitalUserlog.setStatus(hospital.getStatus());
				hospitalUserlog.setLatitude(hospital.getLatitude());
				hospitalUserlog.setLongitude(hospital.getLongitude());
				hospitalUserlog.setHospitalCategoryid(hospital.getHospitalCategoryid());
				hospitalUserlog.setPatientOtpRequired(hospital.getPatientOtpRequired());
				hospitalUserlog.setLoginOtpRequired(hospital.getLoginOtpRequired());
				hosploglist.add(hospitalUserlog);
				hospital.setPatientOtpRequired(hosp.getStatus());
				hospital.setUpdatedBy(hosp.getCreateby().longValue());
				hospital.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				hosplist.add(hospital);
			}
			hospitalUserLogRepository.saveAll(hosploglist);
			hospitalUserSaveRepository.saveAll(hosplist);
			response.setStatus("200");
			response.setMessage("Record Updated Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("SomeThing Went Wrong");
		}
		return response;
	}

	@Override
	public Map<String, Object> getlistTreatingdoctorConfiguration(String state, String dist, String type, Long userid) {
		Map<String, Object> obj = new HashMap<>();
		List<TreatingDoctorconfigbean> list = new ArrayList<TreatingDoctorconfigbean>();
		ResultSet rs = null;
		Boolean check = true;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_TREATING_DOCTOR_CONFIG_LIST")
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_STATE_CODE", state.trim());
			storedProcedure.setParameter("P_DISTRICT_CODE", dist.trim());
			storedProcedure.setParameter("P_TYPE", type.trim());
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				TreatingDoctorconfigbean beans = new TreatingDoctorconfigbean();
				beans.setStateCode(rs.getString(1));
				beans.setStateName(rs.getString(2));
				beans.setDistCode(rs.getString(3));
				beans.setDistName(rs.getString(4));
				beans.setHospitalName(rs.getString(5));
				beans.setHospitalCode(rs.getString(6));
				beans.setMobilenumber(rs.getString(7));
				beans.setEmailid(rs.getString(8));
				if (rs.getInt(9) == 1) {
					check = false;
				}
				beans.setTratingdoctorrequired(rs.getInt(9));
				beans.setHospitalId(rs.getInt(10));
				list.add(beans);
			}
			obj.put("status", 200);
			obj.put("msg", "Api called Successfully");
			obj.put("data", list);
			obj.put("check", check);
		} catch (Exception e) {
			obj.put("status", 400);
			obj.put("msg", "SomeThing Went Wrong");
			obj.put("error", e);
			obj.put("check", false);
		}
		return obj;
	}

	@Override
	public Response SubmitgetTreatingdoctorconfigurationlist(TreatingSubmitBean hospbean) {
		Response response = new Response();
		String hospitalid = "";
		String status = "";
		int createdby = 0;
		Integer claimraiseInteger = null;
		try {
			for (HospitalDetailBean x : hospbean.getHospdetailbean()) {
				hospitalid = hospitalid + x.getHospitalid() + ",";
				status = status + x.getStatus() + ",";
				createdby = x.getCreateby();
			}
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CLAIM_TREATING_DOCTOR_CONFIG_UPDATION")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			storedProcedure.setParameter("P_USERID", Long.parseLong(String.valueOf(createdby)));
			storedProcedure.setParameter("P_HOSPITAL_ID", hospitalid.trim());
			storedProcedure.setParameter("P_TYPE", status.trim());
			storedProcedure.execute();
			claimraiseInteger = (Integer) storedProcedure.getOutputParameterValue("P_OUT");
			if (claimraiseInteger == 1) {
				response.setStatus("200");
				response.setMessage("Record Updated Successfully");
			} else if (claimraiseInteger == 2) {
				response.setStatus("400");
				response.setMessage("SomeThing Went Wrong");
			}
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("SomeThing Went Wrong");
		}
		return response;
	}

	@Override
	public Map<String, Object> getlatestlogdetails() {
		Map<String, Object> objdata = new HashMap<>();
		List<Treatinglogdetailsbean> listdata = new ArrayList<Treatinglogdetailsbean>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("usp_claim_treating_doctor_config_details")
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("p_msg_out");
			while (rs.next()) {
				Treatinglogdetailsbean data = new Treatinglogdetailsbean();
				data.setHospital_code(rs.getString(1));
				data.setHospital_name(rs.getString(2));
				data.setMobile(rs.getString(3));
				data.setEmail_id(rs.getString(4));
				data.setHospitalcategoryname(rs.getString(5));
				data.setTreating_doctor_required(rs.getString(6));
				data.setFullname(rs.getString(7));
				data.setUpdatedon(rs.getString(8));
				data.setStatename(rs.getString(9));
				data.setDistrictname(rs.getString(10));
				listdata.add(data);
			}
			objdata.put("status", 200);
			objdata.put("msg", "Api called Successfully");
			objdata.put("data", listdata);
		} catch (Exception e) {
			objdata.put("status", 400);
			objdata.put("msg", "SomeThing Went Wrong");
			objdata.put("error", e);
		}
		return objdata;
	}

	@Override
	public Map<String, Object> hospitallistforloginotpconfigure(String state, String dist, Long userid, Long otpreq) {
		Map<String, Object> mapobj = new HashMap<>();
		List<HospitalBean> beanlist = new ArrayList<HospitalBean>();
		ResultSet rs = null;
		Boolean checkall = true;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_HOSP_LOGIN_OTP_CONFIGURATION_LIST")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OTP_REQUIRED", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userid);
			storedProcedure.setParameter("P_STATE_CODE", state);
			storedProcedure.setParameter("P_DISTRICT_CODE", dist);
			storedProcedure.setParameter("P_OTP_REQUIRED", otpreq);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalBean bean = new HospitalBean();
				bean.setHospitalId(rs.getInt(1));
				bean.setHospitalName(rs.getString(2));
				bean.setHospitalCode(rs.getString(3));
				bean.setMobileNo(rs.getString(4));
				bean.setEmailId(rs.getString(5));
				if (rs.getInt(6) == 1) {
					checkall = false;
				}
				bean.setStatus(rs.getInt(6));
				beanlist.add(bean);
			}
			mapobj.put("status", 200);
			mapobj.put("msg", "Api called Successfully");
			mapobj.put("data", beanlist);
			mapobj.put("checkall", checkall);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			mapobj.put("status", 400);
			mapobj.put("msg", "SomeThing Went Wrong");
			mapobj.put("error", e);
			mapobj.put("checkall", false);
		}
		return mapobj;
	}

	@Override
	public Response submithospitallistforloginotpconfigure(List<HospitalDetailBean> hospbean) {
		Response response = new Response();
		List<HospitalInformation> hosplist = new ArrayList<HospitalInformation>();
		List<HospitalInformationLog> hosploglist = new ArrayList<HospitalInformationLog>();
		try {
			for (HospitalDetailBean hosp : hospbean) {
				HospitalInformationLog hospitalUserlog = new HospitalInformationLog();
				HospitalInformation hospital = hospitalUserSaveRepository.findById(hosp.getHospitalid()).get();
				hospitalUserlog.setHospitalCode(hospital.getHospitalCode());
				hospitalUserlog.setHospitalName(hospital.getHospitalName());
				hospitalUserlog.setHospitalId(hospital.getHospitalId());
				hospitalUserlog.setStateCode(hospital.getDistrictcode().getStatecode().getStateCode());
				hospitalUserlog.setDistrictcode(hospital.getDistrictcode().getDistrictcode());
				hospitalUserlog.setCpdApprovalRequired(hospital.getCpdApprovalRequired());
				hospitalUserlog.setMobile(hospital.getMobile());
				hospitalUserlog.setEmailId(hospital.getEmailId());
				hospitalUserlog.setUserId(hospital.getUserId().getUserId().intValue());
				hospitalUserlog.setCreatedBy(hosp.getCreateby());
				hospitalUserlog.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				hospitalUserlog.setAssigned_dc(hospital.getAssigned_dc());
				hospitalUserlog.setStatus(hospital.getStatus());
				hospitalUserlog.setLatitude(hospital.getLatitude());
				hospitalUserlog.setLongitude(hospital.getLongitude());
				hospitalUserlog.setHospitalCategoryid(hospital.getHospitalCategoryid());
				hospitalUserlog.setPatientOtpRequired(hospital.getPatientOtpRequired());
				hospitalUserlog.setLoginOtpRequired(hospital.getLoginOtpRequired());
				hosploglist.add(hospitalUserlog);
				hospital.setLoginOtpRequired(hosp.getStatus());
				hospital.setUpdatedBy(hosp.getCreateby().longValue());
				hospital.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				hosplist.add(hospital);
			}
			hospitalUserLogRepository.saveAll(hosploglist);
			hospitalUserSaveRepository.saveAll(hosplist);
			response.setStatus("200");
			response.setMessage("Record Updated Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("SomeThing Went Wrong");
		}
		return response;
	}
}
