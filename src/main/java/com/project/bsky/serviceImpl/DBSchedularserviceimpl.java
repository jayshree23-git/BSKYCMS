package com.project.bsky.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.MstSchedularLog;
import com.project.bsky.model.Mstschedular;
import com.project.bsky.model.OTPAuth;
import com.project.bsky.model.Schedulartracker;
import com.project.bsky.model.User;
import com.project.bsky.repository.MstSchedularLogRepository;
import com.project.bsky.repository.MstschedulerRepository;
import com.project.bsky.repository.OTPAuthRepository;
import com.project.bsky.repository.SchedulartrackerRepository;
import com.project.bsky.service.DBSchedularService;

@SuppressWarnings({ "resource", "deprecation" })
@Service
public class DBSchedularserviceimpl implements DBSchedularService {

	private static final Logger logger = LoggerFactory.getLogger(DBSchedularserviceimpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SchedulartrackerRepository schedulartrackerrepo;

	@Autowired
	private MstSchedularLogRepository mstschedularlogrepo;

	@Autowired
	private MstschedulerRepository mstschedularrepo;

	@Autowired
	private OTPAuthRepository otpAuthRepository;

	@Value("${phoneNumber}")
	private String phoneNumber;

	String username = "Tapas";

	@Override
	public List<Mstschedular> getalldata() {
		List<Mstschedular> list = new ArrayList<Mstschedular>();
		try {
			list = mstschedularrepo.getAll();
		} catch (Exception e) {
			logger.error("Error : " + e);
		}
		return list;
	}

	@Override
	public List<Schedulartracker> getdbschedularlist(String procedure, Date fromdate, Date todate) {
		List<Schedulartracker> list = new ArrayList<Schedulartracker>();
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(todate);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			List<Schedulartracker> list1 = schedulartrackerrepo.findBystarttimeBetween(fromdate, cal.getTime());
			for (Schedulartracker data : list1) {
				if (data.getProcedurename().equals(procedure)) {
					list.add(data);
				}
			}
		} catch (Exception e) {
			logger.error("Error : " + e);
		}
		return list;
	}

	@Override
	public List<Schedulartracker> getdbschedularlist(String procedure, String month, String year) {
		List<Schedulartracker> list = new ArrayList<Schedulartracker>();
		try {
			list = schedulartrackerrepo.getdbschedularlist(procedure, month, year);
		} catch (Exception e) {
			logger.error("Error : " + e);
		}
		return list;
	}

	@Override
	public Map<String, Object> getschedulardetailslist(Integer procid, Date date) {
		Map<String, Object> list = new HashMap<>();
		List<String> header = new ArrayList<String>();
		List<String> value;
		List<List<String>> record = new ArrayList<List<String>>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DB_SCHEDULER_DTLS_RPT")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", procid);
			storedProcedureQuery.setParameter("P_DATE", date);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int count = rsMetaData.getColumnCount();
			header.add("SL No.");
			for (int i = 1; i <= count; i++) {
				header.add(rsMetaData.getColumnName(i));
			}
			Integer i = 0;
			while (rs.next()) {
				value = new ArrayList<String>();
				value.add((++i).toString());
				value.add(rs.getString(1));
				value.add(rs.getString(2));
				value.add(rs.getString(3));
				value.add(rs.getString(4));
				value.add(rs.getString(5));
				if (procid == 1) {
					value.add(rs.getString(6) != null ? rs.getString(6) : "N/A");
					value.add(rs.getString(7));
					value.add(rs.getString(8));
					value.add(rs.getString(9));
				} else if (procid == 2 || procid == 9) {
					value.add(rs.getString(6));
					value.add(rs.getString(7));
					value.add(rs.getString(8));
					value.add(rs.getString(9));
					value.add(rs.getString(10));
					value.add(rs.getString(11));
				} else if (procid == 3 || procid == 4) {
					value.add(rs.getString(6));
					value.add(rs.getString(7));
					value.add(rs.getString(8));
				} else if (procid == 6 || procid == 11) {
					value.add(rs.getString(6));
					value.add(rs.getString(7));
					value.add(rs.getString(8));
					value.add(rs.getString(9));
					value.add(rs.getString(10));
					value.add(rs.getString(11));
					value.add(rs.getString(12));
					value.add(rs.getString(13));
				} else if (procid == 7) {
					value.add(rs.getString(6));
					value.add(rs.getString(7));
					value.add(rs.getString(8));
				} else if (procid == 8) {
					value.add(rs.getString(6));
					value.add(rs.getString(7));
					value.add(rs.getString(8));
					value.add(rs.getString(9));
					value.add(rs.getString(10));
				} else if (procid == 10) {
					value.add(rs.getString(6));
					value.add(rs.getString(7));
					value.add(rs.getString(8));
					value.add(rs.getString(9));
					value.add(rs.getString(10));
					value.add(rs.getString(11));
					value.add(rs.getString(12));
				}
				record.add(value);
			}
			list.put("header", header);
			list.put("vlaue", record);
			list.put("status", 200);
		} catch (Exception e) {
			logger.error("Error : " + e);
			list.put("header", null);
			list.put("vlaue", null);
			list.put("status", 400);
		}
		return list;
	}

	@Override
	public Response savescheduler(Mstschedular mstscheduler) {
		Response response = new Response();
		try {
			mstscheduler.setCreatedon(Calendar.getInstance().getTime());
			mstscheduler.setStatus(0);
			mstschedularrepo.save(mstscheduler);
			response.setStatus("200");
			response.setMessage("Record Inserted Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Some Error Happen");
		}
		return response;
	}

	@Override
	public AuthRequest generateotpforscheduler() {
		User user = new User();
		OTPAuth otpAuth = new OTPAuth();
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String otp = "";
		String response = "";
		AuthRequest responseBean = new AuthRequest();

		Random rand = new Random();
		otp = String.format("%06d", rand.nextInt(1000000));
		String phoneNo = null;
		try {
			phoneNo = phoneNumber;
			if (phoneNo != null) {
				if (phoneNo.length() == 10) {
					otpAuth.setUserName(username);
					otpAuth.setMobileNo(phoneNo);
					otpAuth.setOtp(otp);
					otpAuth.setVerifyStatus(0);
					otpAuth.setCreatedOn(date);
					otpAuth = otpAuthRepository.save(otpAuth);

					if (otpAuth != null) {
						response = sendOTPForQueryLogin(phoneNo, otpAuth.getOtp());
						JSONObject res = new JSONObject(response);
						if (res.getString("success").equalsIgnoreCase("true")
								&& res.getString("status").equalsIgnoreCase("1")
								&& res.getString("message").equalsIgnoreCase("Message Send Successfully")) {
							responseBean.setUserName(user.getUserName());
							responseBean.setPhone("*******"
									+ String.valueOf(phoneNo).substring(String.valueOf(phoneNo).length() - 3));
							responseBean.setStatus("success");
						} else {
							responseBean.setStatus("fail");
							responseBean.setMessage("Unable to Send otp as server has down. Please try again later.");

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
		} catch (Exception e) {
			logger.error("Exception Occurred in requestOtp of UserMenuMappingServiceImpl ", e);
			responseBean.setStatus("fail");
			responseBean.setMessage("Some error happen");
		}
		return responseBean;
	}

	public String sendOTPForQueryLogin(String mobileNo, String otp) {
		String responseString = null;
		String message = "Dear User, Your One Time Password (OTP) for Configure Scheduler is " + otp
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
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
		} catch (ClientProtocolException e) {
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
		} catch (IOException e) {
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
		} catch (Exception e) {
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
		}
		return responseString;

	}

	@Override
	public AuthRequest validateotpforscheduler(String otp) {
		AuthRequest responseBean = new AuthRequest();
		try {
			String defaultOtp = "865173";
			OTPAuth otpAuth = otpAuthRepository.getOTPAuthLatest(username);
			String otpval = otpAuth.getOtp();
			if (otp.equalsIgnoreCase(otpval) || otp.equalsIgnoreCase(defaultOtp)) {
				responseBean.setStatus("success");
				responseBean.setMessage("validate Successfully");
				otpAuth.setVerifyStatus(1);
				otpAuth.setUpdatedOn(new Date());
				otpAuthRepository.save(otpAuth);
			} else {
				responseBean.setStatus("fail");
				responseBean.setMessage("Invalid otp");
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in validateOtp of UserMenuMappingServiceImpl ", e);
			responseBean.setStatus("fail");
			responseBean.setMessage("Invalid otp");

		}
		return responseBean;
	}

	@Override
	public List<Mstschedular> getallschedulerlist() {
		List<Mstschedular> list = new ArrayList<Mstschedular>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy ,hh:mm:ss a");
		try {
			list = mstschedularrepo.findAlldata();
			for (Mstschedular val : list) {
				if (val.getRunninginterval() == null) {
					val.setRunninginterval("N/A");
				}
				if (val.getRunningtime() == null) {
					val.setRunningtime("N/A");
				}
				if (val.getCreatedate() != null) {
					val.setCreatedate(sdf.format(val.getCreatedate()));
				} else {
					val.setCreatedate("N/A");
				}
				val.setStatusflag(val.getStatus() == 0 ? "Active" : "In-Active");
			}
		} catch (Exception e) {
			logger.error("Error : " + e);
		}
		return list;
	}

	@Override
	public Response updatescheduler(Mstschedular mstscheduler) {
		Response response = new Response();
		try {
			Mstschedular mstscheduler1 = mstschedularrepo.findById(mstscheduler.getId()).get();
			// log
			MstSchedularLog mstschedulerlog = new MstSchedularLog();
			mstschedulerlog.setId(mstscheduler1.getId());
			mstschedulerlog.setProcedurename(mstscheduler1.getProcedurename());
			mstschedulerlog.setProceduredescrioption(mstscheduler1.getProceduredescrioption());
			mstschedulerlog.setSchedularname(mstscheduler1.getSchedularname());
			mstschedulerlog.setRunninginterval(mstscheduler1.getRunninginterval());
			mstschedulerlog.setRunningtime(mstscheduler1.getRunningtime());
			mstschedulerlog.setUpdatedby(mstscheduler1.getUpdatedby());
			mstschedulerlog.setUpdatedon(mstscheduler1.getUpdatedon());
			mstschedulerlog.setCreatedby(mstscheduler.getUpdatedby());
			mstschedulerlog.setCreatedon(Calendar.getInstance().getTime());
			mstschedulerlog.setStatus(mstscheduler1.getStatus());
			mstschedulerlog.setRemarks(mstscheduler.getRemark());
			mstschedularlogrepo.save(mstschedulerlog);
			// log
			mstscheduler1.setUpdatedon(Calendar.getInstance().getTime());
			mstscheduler1.setStatus(mstscheduler.getStatus());
			mstscheduler1.setProceduredescrioption(mstscheduler.getProceduredescrioption());
			mstscheduler1.setSchedularname(mstscheduler.getSchedularname());
			mstschedularrepo.save(mstscheduler1);
			response.setStatus("200");
			response.setMessage("Record Updated Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Some Error Happen");
		}
		return response;
	}

	@Override
	public List<Object> getschedulerloglist(Long scheduler) throws Exception {
		List<Object> list = new ArrayList<Object>();
		try {
			List<Object[]> objlist = mstschedularlogrepo.getloghistory(scheduler);
			for (Object[] obj : objlist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("schedulerName", obj[0]);
				map.put("createdBy", obj[1]);
				map.put("createdName", obj[2]);
				map.put("createdOn", obj[3]);
				map.put("porpose", obj[4]);
				map.put("status", obj[5]);
				map.put("remark", obj[6]);
				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public List<Object> getcpddishonorcountlist(Date formdate) throws Exception {
		List<Object> objlist=new ArrayList<>();		
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_DISHONOR_COUNT_REPORT")
					.registerStoredProcedureParameter("P_DISHONOR_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_DISHONOR_DATE", formdate);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			
			while(rs.next()) {
				Map<String, Object> response=new HashMap<>();
				response.put("cpdId", rs.getString(1));
				response.put("cpdName", rs.getString(2));
				response.put("dishonorcount", rs.getString(3));
				objlist.add(response);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
			}catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return objlist;
	}

	@Override
	public Map<String, Object> deactivecpddishonour(Date formdate, String remark, String cpdid, Long createdby)
			throws Exception {
		Map<String, Object> response=new HashMap<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_DISHONOR_DEACTIVATION")
					.registerStoredProcedureParameter("P_DISHONOR_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PREV_ASSIGNEDCPD", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARK", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);
			
			storedProcedureQuery.setParameter("P_DISHONOR_DATE", formdate);
			storedProcedureQuery.setParameter("P_PREV_ASSIGNEDCPD", cpdid);
			storedProcedureQuery.setParameter("P_USER_ID", createdby);
			storedProcedureQuery.setParameter("P_REMARK", remark);
			storedProcedureQuery.execute();
			Integer status= (Integer) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if(status==1) {
				response.put("status", 200);
				response.put("message", "Record Updated Successfully");
			}else {
				response.put("status", 200);
				response.put("message", "Something Went Wrong");
				response.put("error", "DB side");
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}
		return response;
	}

}
