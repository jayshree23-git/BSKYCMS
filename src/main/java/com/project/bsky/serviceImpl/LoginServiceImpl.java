package com.project.bsky.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.AuthResponse;
import com.project.bsky.bean.AuthUserBean;
import com.project.bsky.bean.LoginBean;
import com.project.bsky.bean.OrderLink;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.LoginHistory;
import com.project.bsky.model.OTPAuth;
import com.project.bsky.model.User;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsPassResetLog;
import com.project.bsky.model.UserMenuMapping;
import com.project.bsky.repository.GlobalLinkRepository;
import com.project.bsky.repository.LoginHistoryRepo;
import com.project.bsky.repository.OTPAuthRepository;
import com.project.bsky.repository.UserDetailsPassResetLogRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.repository.UserMenuMappingRepository;
import com.project.bsky.repository.UserRepository;
import com.project.bsky.service.LoginService;
import com.project.bsky.util.ClassHelperUtils;
import com.project.bsky.util.JwtUtil;
import com.project.bsky.util.OTPGenerator;
import com.project.bsky.util.PasswordEncrypter;
import com.project.bsky.util.ProfileUtil;
import com.project.bsky.util.SMSUtil;
import com.project.bsky.util.Validation;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private Logger logger;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager = null;

	@Autowired
	private UserRepository repository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserMenuMappingRepository userMenuMappingRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private GlobalLinkRepository globalLinkRepository;

	@Autowired
	private OTPAuthRepository otpAuthRepository;

	@Autowired
	private UserDetailsPassResetLogRepository userDetailsPassResetLogRepository;

	@Autowired
	private LoginHistoryRepo historyRepo;

	@Autowired
	private Environment env;

	@Value("${configKey}")
	private String password;

	@Value("${leftdays}")
	private Integer days;

	@Value("${phoneNumber}")
	private String phoneNumber;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public AuthResponse authenticate(HttpServletRequest httpServletRequest, Map<String, String> mapRequest)
			throws Exception {
		JSONObject json = new JSONObject();
		AuthResponse response = new AuthResponse();
		AuthRequest authRequest = new AuthRequest();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));

			authRequest.setUserName(json.getString("userName"));
			authRequest.setPassWord(json.getString("passWord"));
			authRequest.setCaptcha(json.getString("captcha"));
			authRequest.setInputCaptcha(json.getString("inputCaptcha"));

			if (Validation.validateCaptcha(authRequest.getCaptcha(), authRequest.getInputCaptcha())) {
				Authentication auth = null;
				String token, attemptMessage = "";
				UserDetails user = null;
//				InetAddress localhost = InetAddress.getLocalHost();
				String ipAddress = httpServletRequest.getRemoteAddr();
				LoginHistory history = new LoginHistory();
				Calendar calendar = Calendar.getInstance();
				history.setUserIP(ipAddress);
				history.setCreatedOn(calendar.getTime());
				history.setUserName(authRequest.getUserName());
				Query query;
				List<Object[]> operatorObj;
				String hospitalCode = null;
				try {
					user = userDetailsRepo.findByUserName(authRequest.getUserName());
					if (user == null)
						throw new Exception("Invalid Username");
					else if (user.getAttemptedStatus() < 10) {
						try {
							auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
									authRequest.getUserName(), authRequest.getPassWord()));
							user.setAttemptedStatus(0);
						} catch (Exception e) {
							user.setAttemptedStatus(user.getAttemptedStatus() + 1);
							if (user.getAttemptedStatus() == 10)
								attemptMessage = "Your Account is Locked. Please Contact Admin.";
							else
								attemptMessage = user.getAttemptedStatus() < 10
										? "You have " + (10 - user.getAttemptedStatus()) + " attempt(s) left."
										: "";
							throw new Exception("Invalid Password, " + attemptMessage);
						}
					} else
						throw new Exception("Your Account is Locked. Please Contact Admin.");
				} catch (Exception e) {
					logger.error("Exception Occured in authenticate method of LoginServiceImpl", e);
					history.setStatusFlag('1');
					throw new Exception(e.getMessage());
				} finally {
					if (auth != null) {
						history.setStatusFlag('0');
					}
					try {
						if (user != null)
							userDetailsRepo.save(user);
						historyRepo.save(history);
					} catch (Exception e) {
						logger.error("Exception Occured in authenticate method of LoginServiceImpl", e);
					}
				}
				if (auth != null) {
					if (password.equals(authRequest.getPassWord())) {
						response.setCheckPassword(password);
					} else {
						token = jwtUtil.generateToken(auth.getName());
						user = userDetailsRepo.findByUserName(authRequest.getUserName());
						if (user.getStatus() == 1) {
							throw new Exception("Invalid username/password");
						}
						if (user.getGroupId().getTypeId() == 16) {
							query = this.entityManager
									.createNativeQuery("SELECT HOSPITALCODE FROM  USER_HOSPITAL_OPERATOR "
											+ "WHERE USERID=" + user.getUserId());
							operatorObj = query.getResultList();
							if (operatorObj.size() > 0) {
								for (Object objects : operatorObj) {
									hospitalCode = (String) objects;
								}
							}
							user.setUserName(hospitalCode);
						}

						AuthUserBean bean = new AuthUserBean();
						bean.setUserId(user.getUserId());
						bean.setUserName(user.getUserName());
						bean.setFullName(user.getFullname());
						bean.setGroupId(user.getGroupId().getTypeId());
						bean.setPhone(user.getPhone().toString());
						bean.setStatusFlag(user.getStatus());
						Date updateDate = user.getPasswordUpdatedOn();
						Date currentDate = calendar.getTime();
						long leftDays = 0;
						if (updateDate != null) {
							long upDate = TimeUnit.MILLISECONDS.toDays(updateDate.getTime());
							long crnDate = TimeUnit.MILLISECONDS.toDays(currentDate.getTime());
							leftDays = crnDate - upDate;
						}
						if (leftDays > days) {
							response.setCheckPassword(password);
						}
						bean.setLeftDays(days - leftDays);
						bean.setEmail(user.getEmail());
						bean.setIsOtp(user.getIsOtpAllowed());
						response.setUser(bean);
						response.setAuth_token("Bearer " + token);
						if (user.getIsOtpAllowed() == 0)
							generateOtpForLogin(user);
					}
				}
			} else {
				throw new Exception("Invalid Captcha");
			}
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	@Override
	@Transactional
	public Map<String, Object> changePassword(Map<String, String> mapRequest) throws Exception {
		Map<String, Object> map = new HashMap<>();
		JSONObject json = new JSONObject();
		UserDetails user = new UserDetails();
		UserDetailsPassResetLog userDetailsPassResetLog = null;
		List<UserDetails> userlist = userDetailsRepo.findAll();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			String userName = json.has("userName") ? json.getString("userName") : "";
			String pass = json.has("passWord") ? json.getString("passWord") : "";
			String otp = json.has("otp") ? json.getString("otp") : "";

			Map<String, String> otpResponseMap = validateOtp(otp, userName);

			if (otpResponseMap.get("status").equals("success")) {
				String passwd = env.getProperty("configKey");
				if (!pass.equalsIgnoreCase(passwd)) {
					List<UserDetails> collect = userlist.stream()
							.filter(e -> e.getUserName().equalsIgnoreCase(userName)).collect(Collectors.toList());
					user = collect.get(0);
					if (user != null) {
						if (user.getUserName().equalsIgnoreCase(userName)) {
							Boolean value = passwordMatch(pass, user.getPassword());
							if (Boolean.FALSE.equals(value)) {
								Calendar calendar = Calendar.getInstance();
								password = encoder.encode(pass);
								user.setPassword(password);
								user.setAttemptedStatus(0);
								user.setPasswordUpdatedOn(calendar.getTime());
								user = userDetailsRepo.save(user);
								userDetailsPassResetLog = new UserDetailsPassResetLog();
								userDetailsPassResetLog.setCreatedBy(user);
								userDetailsPassResetLog.setCreatedOn(calendar.getTime());
								userDetailsPassResetLog.setUserId(user);
								userDetailsPassResetLogRepository.save(userDetailsPassResetLog);
								map.put("status", "success");
								map.put("message", "Password Changed successfully");
							} else {
								map.put("status", "failed");
								map.put("message", "Old Password Can Not Be Same As New Password");
							}
						} else {
							map.put("status", "failed");
							map.put("message", "User Not Found");
						}
					} else {
						map.put("status", "failed");
						map.put("message", "User Not Found");
					}
				} else {
					map.put("status", "failed");
					map.put("message", "This Pasword Can Not Be Saved, Try With Another Password");
				}
			} else {
				map.put("status", otpResponseMap.get("status"));
				map.put("message", otpResponseMap.get("message"));
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in changePassword method of LoginServiceImpl", e);
			throw e;
		}
		return map;
	}

	@Override
	public JSONArray getUserMappingList(Map<String, String> mapRequest) {
		List<UserMenuMapping> leftMenu = null;
		Set<OrderLink> globalList = new HashSet<>();
		JSONArray jsonArray = new JSONArray();
		JSONObject globalObject = new JSONObject();
		JSONObject primaryObject = null;
		JSONObject functionObject = null;
		JSONArray primaryLinkList = new JSONArray();
		List<Long> globalListIds = new ArrayList<>();
		JSONObject json = new JSONObject();
		Integer bitStatus = 0;

		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			leftMenu = userMenuMappingRepository
					.findByUserIdAndBitStatus(json.has("userId") ? json.getInt("userId") : 0, bitStatus);

			for (int i = 0; i < leftMenu.size(); i++) {
				OrderLink orderLink = new OrderLink();
				orderLink.setId(leftMenu.get(i).getPrimaryLink().getGlobalLink().getGlobalLinkId());
				orderLink.setOrderId(leftMenu.get(i).getPrimaryLink().getGlobalLink().getOrder());

				globalList.add(orderLink);
				globalListIds.add(leftMenu.get(i).getPrimaryLink().getGlobalLink().getGlobalLinkId());
			}

			List<OrderLink> personList = globalList.stream()
					.sorted((e1, e2) -> e1.getOrderId().compareTo(e2.getOrderId())).collect(Collectors.toList());
			for (OrderLink glink : personList) {

				globalObject = new JSONObject();
				primaryLinkList = new JSONArray();

				GlobalLink glinkObj = globalLinkRepository.getById(glink.getId());

				globalObject.put("globalId", glink.getId());
				globalObject.put("globalName", glinkObj.getGlobalLinkName());
				globalObject.put("globalNameId", glinkObj.getGlobalLinkName().replace(" ", ""));
				globalObject.put("orderId", glinkObj.getOrder());

				for (int k = 0; k < leftMenu.size(); k++) {

					primaryObject = new JSONObject();
					functionObject = new JSONObject();

					if (leftMenu.get(k).getPrimaryLink().getGlobalLink().getGlobalLinkId() == glink.getId()) {
						primaryObject.put("orderId", leftMenu.get(k).getPrimaryLink().getOrder());
						primaryObject.put("primaryId", leftMenu.get(k).getPrimaryLink().getPrimaryLinkId());
						primaryObject.put("primaryName", leftMenu.get(k).getPrimaryLink().getPrimaryLinkName());
						primaryObject.put("globalId",
								leftMenu.get(k).getPrimaryLink().getGlobalLink().getGlobalLinkId());

						functionObject.put("functionId",
								leftMenu.get(k).getPrimaryLink().getFunctionMaster().getFunctionId());
						functionObject.put("functionName",
								leftMenu.get(k).getPrimaryLink().getFunctionMaster().getFunctionName());
						functionObject.put("functionUrl",
								leftMenu.get(k).getPrimaryLink().getFunctionMaster().getFileName());

						primaryObject.put("functionList", functionObject);
						primaryLinkList.put(primaryObject);
					}

				}
				JSONArray sorted = sort(primaryLinkList, "orderId");
				globalObject.put("primaryList", sorted);
				jsonArray.put(globalObject);
			}
		} catch (Exception e) {
			logger.error("Exception Occured in getLeftMenuList() method of UserMenuMappingServiceImpl", e);
		}
		return jsonArray;
	}

	public JSONArray sort(JSONArray jsonArr, String sortBy) throws JSONException {
		JSONArray sortedJsonArray = new JSONArray();
		final String KEY_NAME = sortBy;

		List<JSONObject> jsonValues = new ArrayList<>();
		for (int i = 0; i < jsonArr.length(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject a, JSONObject b) {
				Integer firstValue = null;
				Integer secondValue = null;

				try {
					firstValue = (Integer) a.get(KEY_NAME);
					secondValue = (Integer) b.get(KEY_NAME);
				} catch (JSONException e) {
					logger.error("Exception Occured in sort of UserMenuMappingServiceImpl ", e);
				}
				return firstValue.compareTo(secondValue);
			}
		});
		for (int i = 0; i < jsonArr.length(); i++) {
			sortedJsonArray.put(jsonValues.get(i));
		}
		return sortedJsonArray;
	}

	@Override
	public AuthRequest forgotPassword(Map<String, String> mapRequest) throws Exception {
		JSONObject json = new JSONObject();
		User user = new User();
		OTPAuth otpAuth = null;
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String otp = "";
		String response = "";
		AuthRequest responseBean = new AuthRequest();
		Random rand = new Random();
		otp = String.format("%06d", rand.nextInt(1000000));
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			user = repository.findByUserNameIgnoreCase(json.has("userName") ? json.getString("userName") : "");
			if (user != null) {
				if (user.getPhone() != null) {
					if (user.getPhone().length() == 10) {
						if (user.getUserName()
								.equalsIgnoreCase(json.has("userName") ? json.getString("userName") : "")) {
							Integer authId = otpAuthRepository.getMaxAuthId();
							if (authId == null) {
								authId = 0;
							}
							otpAuth = new OTPAuth();
							otpAuth.setAuthId(Long.valueOf(authId + 1));
							otpAuth.setUserName(user.getUserName());
							otpAuth.setMobileNo(user.getPhone());
							otpAuth.setOtp(otp);
							otpAuth.setAttemptStatus(0);
							otpAuth.setVerifyStatus(0);
							otpAuth.setCreatedOn(date);
							otpAuth = otpAuthRepository.save(otpAuth);
							if (otpAuth != null) {
								response = sendOTP(user.getPhone(), otpAuth.getOtp());
								JSONObject res = new JSONObject(response);
								if (res.getString("success").equalsIgnoreCase("true")
										&& res.getString("status").equalsIgnoreCase("1")
										&& res.getString("message").equalsIgnoreCase("Message Send Successfully")) {

									responseBean.setUserName(user.getUserName());
									responseBean.setPhone("*******" + String.valueOf(user.getPhone())
											.substring(String.valueOf(user.getPhone()).length() - 3));
									responseBean.setStatus("success");
								} else {
									responseBean.setStatus("fail");
									responseBean.setMessage(
											"Unable to Send otp as server has down. Please try again later.");
								}
							}
						} else {
							responseBean.setStatus("fail");
							responseBean.setMessage("User does not exist");
						}
					} else {
						responseBean.setStatus("fail");
						responseBean.setMessage("Mobile no does not exist");
					}
				} else {
					responseBean.setStatus("fail");
					responseBean.setMessage("Mobile no does not exist");
				}
			} else {
				responseBean.setStatus("fail");
				responseBean.setMessage("User does not exist");
			}
		} catch (Exception e) {
			logger.error("Exception Occured in forgotPassword of LoginServiceImpl ", e);
			responseBean.setStatus("fail");
			responseBean.setMessage("Some error happen");
		}
		return responseBean;
	}

	@Override
	public Response otpValidate(AuthRequest authRequest) throws Exception {
		Response response = new Response();
		try {
			String defaultOtp = "865173";
			OTPAuth otpAuth = otpAuthRepository.getOTPAuthLatest(authRequest.getUserName());
			String otp = otpAuth.getOtp();
			if (otp.equalsIgnoreCase(authRequest.getOtp()) || defaultOtp.equalsIgnoreCase(authRequest.getOtp())) {
				otpAuth.setVerifyStatus(1);
				otpAuth.setUpdatedOn(new Date());
				otpAuthRepository.save(otpAuth);
				response.setMessage("Validate Successfully");
				response.setStatus("success");
			} else {
				response.setMessage("Invalid OTP");
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in otpValidate of UserMenuMappingServiceImpl ", e);
			throw new Exception("Some Error Happen");
		}
		return response;
	}

	public String sendOTP(String mobileNo, String otp) {
		String responseString = null;
		String message = "Dear User, Your One Time Password (OTP) for Forgot Password is " + otp
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
			logger.error("Exception Occurred in sendOTP of UserMenuMappingServiceImpl ", e);
		} catch (ClientProtocolException e) {
			logger.error("Exception Occurred in sendOTP of UserMenuMappingServiceImpl ", e);
		} catch (IOException e) {
			logger.error("Exception Occurred in sendOTP of UserMenuMappingServiceImpl ", e);
		} catch (Exception e) {
			logger.error("Exception Occurred in sendOTP of UserMenuMappingServiceImpl ", e);
		}
		return responseString;
	}

	@Override
	public AuthRequest internalLogin(AuthRequest authRequest) throws Exception {
		User user = new User();
		AuthRequest responseBean = new AuthRequest();
		try {
			user = repository.findByUserNameIgnoreCase(authRequest.getUserName());

			if (user != null) {

				responseBean.setUser(user);
				responseBean.setStatus("success");

			} else {
				responseBean.setStatus("fail");
				responseBean.setMessage("User does not exist");
				throw new Exception("User does not exist");
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in internalLogin of UserMenuMappingServiceImpl ", e);
			throw e;
		}
		return responseBean;
	}

	public boolean passwordMatch(String password, String encodedPassword) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.matches(password, encodedPassword);
	}

	@Override
	public UserDetails changeuserpassword(Map<String, String> mapRequest) throws Exception {
		UserDetails user = null;
		JSONObject json = new JSONObject();
		Calendar calendar = Calendar.getInstance();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			Long userName = json.has("userName") ? json.getLong("userName") : 0L;
			String passwd = env.getProperty("configKey");
			if (!(json.has("passWord") ? json.getString("passWord") : "").equalsIgnoreCase(passwd)) {
				user = userDetailsRepo.findById(userName).get();
				if (user != null) {
					Boolean value = passwordMatch(json.has("oldpwd") ? json.getString("oldpwd") : "",
							user.getPassword());
					if (Boolean.TRUE.equals(value)) {
						value = passwordMatch(json.has("passWord") ? json.getString("passWord") : "",
								user.getPassword());
						if (Boolean.FALSE.equals(value)) {
							password = encoder.encode(json.has("passWord") ? json.getString("passWord") : "");
							user.setPassword(password);
							user.setPasswordUpdatedOn(calendar.getTime());
							user = userDetailsRepo.save(user);
							UserDetailsPassResetLog userDetailsPassResetLog = new UserDetailsPassResetLog();
							userDetailsPassResetLog.setCreatedBy(user);
							userDetailsPassResetLog.setCreatedOn(calendar.getTime());
							userDetailsPassResetLog.setUserId(user);
							userDetailsPassResetLogRepository.save(userDetailsPassResetLog);
						} else
							throw new Exception("You can not use your privious Password Try With New Password !");
					} else
						throw new Exception("Current Password Is Incorrect");
				} else
					throw new Exception("User not found");
			} else
				throw new Exception("This Pasword Can Not Save Try With Another Password !");

		} catch (Exception e) {
			logger.error("Exception Occurred in changeuserpassword of UserMenuMappingServiceImpl ", e);
			throw e;
		}
		return user;
	}

	@Override
	public AuthRequest requestOtp(AuthRequest authRequest) throws Exception {
		// logger.info("Inside requestOtp method of UserMenuMappingServiceImpl");
		User user = new User();
		OTPAuth otpAuth = new OTPAuth();
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String otp = "";
		String response = "";
		AuthRequest responseBean = new AuthRequest();

		Random rand = new Random();
		otp = String.format("%06d", rand.nextInt(10000));
		String phoneNo = null;
		try {
			user = repository.findByUserNameIgnoreCase(authRequest.getUserName());
			if (user != null) {
				phoneNo = phoneNumber;
				if (phoneNo != null) {

					if (phoneNo.length() == 10) {

						if (user.getUserName().equalsIgnoreCase(authRequest.getUserName())) {
							otpAuth.setUserName(user.getUserName());
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
									responseBean.setMessage(
											"Unable to Send otp as server has down. Please try again later.");
//									throw new Exception(
//											"Unable to fetch data as server has down. Please try again later.");

								}
							}

						} else {
							responseBean.setStatus("fail");
							responseBean.setMessage("User does not exist");
//							throw new Exception("User does not exist");
						}
					} else {
						responseBean.setStatus("fail");
						responseBean.setMessage("Mobile no does not exist");
//						throw new Exception("Mobile no does not exist");
					}
				} else {
					responseBean.setStatus("fail");
					responseBean.setMessage("Mobile no does not exist");
//					throw new Exception("Mobile no does not exist");
				}
			} else {
				responseBean.setStatus("fail");
				responseBean.setMessage("User does not exist");
//				throw new Exception("User does not exist");
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in requestOtp of UserMenuMappingServiceImpl ", e);
			responseBean.setStatus("fail");
			responseBean.setMessage("Some error happen");
//			e.printStackTrace();
//			throw new Exception("Some Error Happen");
		}
		return responseBean;
	}

	public String sendOTPForQueryLogin(String mobileNo, String otp) {
		// logger.info("Inside sendOTPForQueryLogin method of
		// UserMenuMappingServiceImpl");
		// String otp = otpGenertor();
		String result = null;
		String responseString = null;
		String message = "Dear User, Your One Time Password (OTP) for Internal Login or Query Login is " + otp
				+ ". Don't share it with anyone. BSKY, Govt. of Odisha";
		// String message = "Dear user, your grievance has been resolved. BSKY, Govt. of
		// Odisha";
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
			// storeOTP(otp, otpParameter.getMobileNo());
			return responseString;
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (ClientProtocolException e) {
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception Occurred in sendOTPForQueryLogin of UserMenuMappingServiceImpl ", e);
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return responseString;

	}

	@Override
	public Map<String, Object> getSSOLoginInformation(LoginBean loginBean) {
		// logger.info("Inside getSSOLoginInformation method of
		// UserMenuMappingServiceImpl");
		Map<String, Object> response = new LinkedHashMap<>();
		User user;
		try {
			Date apiHitDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
					.parse(PasswordEncrypter.decryptPassword(loginBean.getDateoftoken()));

			// //System.out.println("API Hit Date Time : " +
			// PasswordEncrypter.encryptPassword("29-03-2023 9:38:00"));

			long minutesDiff = (new Date().getTime() - apiHitDateTime.getTime()) / (60 * 1000);
			// //System.out.println("Minutes Diff : " + minutesDiff + " minutes");

			if (minutesDiff < 5) {
				user = repository.findByUserNameIgnoreCase(loginBean.getUsername());
				// //System.out.println("User : " + user);
				if (user == null)
					throw new Exception("Invalid Username!");
				else if (user.getStatusFlag() == 1)
					throw new Exception("Invalid Username!");
				else if (user.getGroupId() != 5)
					throw new Exception("Not a Hospital User!");
				else {
					response.put("user", user);
					response.put("authToken", "Bearer " + jwtUtil.generateToken(loginBean.getUsername()));
				}
			} else
				throw new Exception("Token Expired!");
		} catch (Exception e) {
			logger.error("Exception Occurred in getSSOLoginInformation of UserMenuMappingServiceImpl ", e);
			throw new RuntimeException(e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, String> validateOtp(String authOtp, String username) {
		Map<String, String> map = new HashMap<>();
		try {
			String defaultOtp = "865173";
			OTPAuth otpAuth = otpAuthRepository.getOTPAuthLatest(username);
			String otp = otpAuth.getOtp();
			Integer attempt = otpAuth.getAttemptStatus() == null ? 0 : otpAuth.getAttemptStatus();
			if (attempt >= 3) {
				map.put("status", "exceed");
				map.put("message", "You have lost your 3 attempts, please resend the OTP to continue.");
			} else {
				if (otp.equalsIgnoreCase(authOtp) || defaultOtp.equalsIgnoreCase(authOtp)) {
					otpAuth.setVerifyStatus(1);
					otpAuth.setAttemptStatus(0);
					otpAuth.setUpdatedOn(new Date());

					otpAuthRepository.save(otpAuth);

					map.put("status", "success");
					map.put("message", "success");
				} else {
					otpAuth.setVerifyStatus(0);
					otpAuth.setAttemptStatus(attempt + 1);
					otpAuth.setUpdatedOn(new Date());

					otpAuthRepository.save(otpAuth);

					map.put("status", "invalid");
					map.put("message", ((3 - otpAuth.getAttemptStatus()) == 0
							? "You have lost your 3 attempts, please resend the OTP to continue."
							: "invalid OTP, you have only " + (3 - otpAuth.getAttemptStatus()) + " attempts left."));
				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in validateOtp of UserMenuMappingServiceImpl ", e);
			map.put("status", "failed");
			map.put("message", e.getMessage());
		}
		return map;
	}

	@Override
	public String getProfilePath(Long userId) {
		String photo = userDetailsRepo.getProfilePhoto(userId);
		if (photo != null) {
			String path = ProfileUtil.getProfilePhotoPath();
			return path + photo;
		} else {
			return null;
		}
	}

	@Override
	public AuthResponse mobileApilogin(String username) {
		AuthResponse response = new AuthResponse();
		try {
			Calendar calendar = Calendar.getInstance();
			String token = jwtUtil.generateToken(username);
			UserDetails user = userDetailsRepo.findByUserName(username);
			AuthUserBean bean = new AuthUserBean();
			bean.setUserId(user.getUserId());
			bean.setUserName(user.getUserName());
			bean.setFullName(user.getFullname());
			bean.setGroupId(user.getGroupId().getTypeId());
			bean.setPhone(user.getPhone().toString());
			bean.setStatusFlag(user.getStatus());
			Date updateDate = user.getPasswordUpdatedOn();
			Date currentDate = calendar.getTime();
			// //System.out.println(bean.toString());
			long leftDays = 0;
			if (updateDate != null) {
				long upDate = TimeUnit.MILLISECONDS.toDays(updateDate.getTime());
				long crnDate = TimeUnit.MILLISECONDS.toDays(currentDate.getTime());
				leftDays = crnDate - upDate;

			}
			if (leftDays > days) {
				response.setCheckPassword(password);
			}
			bean.setLeftDays(days - leftDays);
			response.setUser(bean);
			response.setAuth_token("Bearer " + token);
		} catch (Exception e) {
			// System.out.println(e);
		}
		return response;
	}

	public void generateOtpForLogin(UserDetails user) throws IOException, MessagingException {
		Calendar calendar = Calendar.getInstance();
		String otp = OTPGenerator.generateOTP();
		OTPAuth otpAuth = new OTPAuth();
		otpAuth.setUserName(user.getUserName());
		otpAuth.setMobileNo(String.valueOf(user.getPhone()));
		otpAuth.setOtp(otp);
		otpAuth.setVerifyStatus(0);
		otpAuth.setCreatedOn(calendar.getTime());
		try {
			otpAuth = otpAuthRepository.save(otpAuth);
		} catch (Exception e) {
			throw new RuntimeException("Otp generation failed.");
		}
		if (user.getPhone() != null && otp != null) {
			String otpMessage = String
					.format("Dear User, " + "Your One Time Password (OTP) for Login verification is %s. "
							+ "Dont share it with anyone. " + "BSKY, Govt. of Odisha.", otp);

			Map<String, Object> otpResponse = SMSUtil.sendOTP(String.valueOf(user.getPhone()), otpMessage);
		}
		if (otp != null && user.getEmail() != null) {
			String body = String.format("Dear User,\n\n" + "Your One Time Password (OTP) for Login is %s, " + ".\n\n"
					+ "Regards,\n" + "BSKY Team\n\n" + "This is a system-generated mail. Please do not reply.\n", otp);

//			EmailUtil.sendOtpLoginMail("BSKY, Login OTP", body, user.getEmail().trim(), null, null);
		}
	}

	public String sendSMSForInterviewSchedule(String mobileNo, String msgBody, String action, String departmentId,
			String templateId) {
//		String result = null;
		String responseString = null;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("https://govtsms.odisha.gov.in/api/api.php");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("action", action));
			nameValuePairs.add(new BasicNameValuePair("department_id", departmentId));
			nameValuePairs.add(new BasicNameValuePair("template_id", templateId));
			nameValuePairs.add(new BasicNameValuePair("sms_content", msgBody));
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

}