package com.project.bsky.serviceImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.EmpanelBean;
import com.project.bsky.bean.EmpanelmentDetailUpdation;
import com.project.bsky.bean.Response;
import com.project.bsky.entity.TApplicantProfile;
import com.project.bsky.entity.TOnlineServiceApplication;
import com.project.bsky.entity.TOnlineServiceApproval;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.TApplicantProfileRepository;
import com.project.bsky.repository.TOnlineServiceApplicationRepository;
import com.project.bsky.repository.TOnlineServiceApprovalRepository;
import com.project.bsky.service.EmpanelmentDetailUpdationService;


@Service
public class EmpanelmentDetailUpdationServiceImpl implements EmpanelmentDetailUpdationService {

@Autowired
private HospitalInformationRepository hospitalInformationRepository;
@Autowired
private TApplicantProfileRepository tAplicantprofileRepo;
@Autowired
private TOnlineServiceApplicationRepository tOnlineServiceApplicationRepository;

@Autowired
private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;
@PersistenceContext
private EntityManager entityManager;
@Autowired
private Logger logger;
@Override
public List<Object> hospList(String hospitalCode) {

//System.out.println("Inside getHospitalList() method");
List<Object> listHosp = new ArrayList<>();
try {
DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
// hospitalInformationRepository.getListforEmpanelmentDetailUpdation(hospitalCode);
hospitalInformationRepository.getListforEmpanelmentDetailUpdation(hospitalCode).stream().map(hospitalArr -> {
EmpanelmentDetailUpdation empanelmentDetailUpdation = new EmpanelmentDetailUpdation();
empanelmentDetailUpdation.setHospitalName(hospitalArr[0] != null ? hospitalArr[0].toString() : "NA");
empanelmentDetailUpdation.setHospitalCode(hospitalArr[1] != null ? hospitalArr[1].toString() : "NA");
empanelmentDetailUpdation.setStateName(hospitalArr[2] != null ? hospitalArr[2].toString() : "NA");
empanelmentDetailUpdation.setDistrictName(hospitalArr[3] != null ? hospitalArr[3].toString() : "NA");
empanelmentDetailUpdation.setMobile(hospitalArr[4] != null ? hospitalArr[4].toString() : "NA");
empanelmentDetailUpdation.setEmailId(hospitalArr[5] != null ? hospitalArr[5].toString() : "NA");
empanelmentDetailUpdation.setHospitalCategoryName(hospitalArr[6] != null ? hospitalArr[6].toString() : "NA");
empanelmentDetailUpdation.setIntprofileId(hospitalArr[7] != null ? hospitalArr[7].toString() : null);
empanelmentDetailUpdation.setIntonlineserviceId(hospitalArr[8] !=null ? hospitalArr[8].toString() : null);
empanelmentDetailUpdation.setHospitalId(hospitalArr[9] !=null ? hospitalArr[9].toString() : null);
//empanelmentDetailUpdation.setIsBlockActive(hospitalArr[8] !=null ? hospitalArr[15].toString() : "NA");
return empanelmentDetailUpdation;
}).forEach(listHosp::add);
} catch (Exception e) {
logger.error(ExceptionUtils.getStackTrace(e));
}
//System.out.println("List Hospital : " + listHosp);
return listHosp;
//return null;
}

@Override
public Response updateEmpanelHospitalData(EmpanelBean logBean) {
Response response = new Response();
Integer claimsnoInteger = null;
String actionCode;
Timestamp startTime = new Timestamp(System.currentTimeMillis());
String ss = String.valueOf(startTime);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
java.util.Date date=null;
try {
date = sdf.parse(ss);
} catch (ParseException e1) {
e1.printStackTrace();
}
long fetch = date.getTime();
long before = fetch + (10 * 60 * 1000);// 10 mins
Timestamp endTime = new Timestamp(before);

if(logBean.getIntprofileId()!=null) {
//System.out.println("Profile Id is available");
actionCode="B";
}else {
//System.out.println("Profile Id is not available");
actionCode="A";
}
try {
StoredProcedureQuery storedProcedureQuery = this.entityManager
.createStoredProcedureQuery("usp_Emp_hospital_profile_update")
.registerStoredProcedureParameter("P_PROFILE_ID", Long.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_HOSPITAL_ID", Long.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_VCHAPPLICANT_NAME", String.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_VCHMOBILENO", String.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_VCHOTP", String.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_DTMSTARTTIME", Timestamp.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_DTMLASTTIME", Timestamp.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_ACTION_CDE", String.class, ParameterMode.IN)
.registerStoredProcedureParameter("P_MSG_OUT", Integer.class, ParameterMode.OUT);

storedProcedureQuery.setParameter("P_PROFILE_ID", logBean.getIntprofileId());
storedProcedureQuery.setParameter("P_HOSPITAL_ID", logBean.getHospitalId());
storedProcedureQuery.setParameter("P_VCHAPPLICANT_NAME", logBean.getHospitalName());
storedProcedureQuery.setParameter("P_VCHMOBILENO", logBean.getMobile().trim());
storedProcedureQuery.setParameter("P_VCHOTP", logBean.getOtpValue().trim());
storedProcedureQuery.setParameter("P_DTMSTARTTIME", startTime);
storedProcedureQuery.setParameter("P_DTMLASTTIME", endTime);
storedProcedureQuery.setParameter("P_ACTION_CDE", actionCode);
storedProcedureQuery.execute();
claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
////System.out.println(claimsnoInteger);
if (claimsnoInteger == 1 ) {
response.setStatus("Success");
response.setMessage("Ok");
} else {
response.setStatus("Failed");
response.setMessage("Something Went Wrong");
}
response.setStatus("Success");
response.setMessage("Ok");
} catch (Exception e) {
logger.error(ExceptionUtils.getStackTrace(e));
}

return response;
}

@Override
public Response checkDuplicateMobile(String mobile,String profileId) {
Response response = new Response();
TApplicantProfile tApplicantProfileByMobile=null;
if(profileId!=null && profileId!="" && !profileId.equalsIgnoreCase("null")) {
tApplicantProfileByMobile=tAplicantprofileRepo.getTApplicantProfileByProfileIdAndMobile(mobile,Integer.parseInt(profileId));
}else {
tApplicantProfileByMobile = tAplicantprofileRepo.getTApplicantProfileByMobile(mobile);
}
if(tApplicantProfileByMobile!=null) {
response.setStatus("Success");
response.setMessage("Mobile number "+mobile+" is already Registered !");
}
return response;
}

@Override
public JSONObject sendOtpForEMP(String mobile) {
JSONObject jsonObject = new JSONObject();
try {
TApplicantProfile tApplicantProfile = tAplicantprofileRepo
.getTApplicantProfileByMobile(mobile);
if (tApplicantProfile != null) {
Map<String, String> map = this.sendOTP(mobile.trim());
if (map.get("Status").equals("Success")) {
jsonObject.put("status", "success");
jsonObject.put("phone", "*******"+ String.valueOf(mobile).substring(String.valueOf(mobile).length() - 3));
jsonObject.put("userName", tApplicantProfile.getVCHAPPLICANTNAME());
jsonObject.put("rePhone", mobile.trim());
} else {
jsonObject.put("status","fail");
jsonObject.put("message", "Unable to Send otp as server has down. Please try again later.");
}
} else {
jsonObject.put("status", "fail");
jsonObject.put("msg", "Mobile No Not Exist.");
}
} catch (Exception e) {
throw new RuntimeException(e);
}
return jsonObject;
}
public Map<String, String> sendOTP(String mobileNo) {
Map<String, String> map = new HashMap();
if (this.checksendOTP(mobileNo).equals("success")) {
String otp = otpGenertor();
String responseString = null;
String message = "Dear User, Your One Time Password (OTP) for Hospital Registration is " + otp
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
responseString = responseString + line;
// //logger.info(responseString);
}
storeOTP(otp, mobileNo);
map.put("Status", "Success");
} catch (UnsupportedEncodingException e) {
// Logger.error(e.getMessage() + " :: " + ExceptionUtils.getStackTrace(e));
map.put("Status", "Failed");
logger.error(ExceptionUtils.getStackTrace(e));
} catch (ClientProtocolException e) {
// Logger.error(e.getMessage() + " :: " + ExceptionUtils.getStackTrace(e));
map.put("Status", "Failed");
logger.error(ExceptionUtils.getStackTrace(e));
} catch (IOException e) {
// Logger.error(e.getMessage() + " :: " + ExceptionUtils.getStackTrace(e));
map.put("Status", "Failed");
logger.error(ExceptionUtils.getStackTrace(e));
} catch (Exception e) {
// Logger.error(e.getMessage() + " :: " + ExceptionUtils.getStackTrace(e));
map.put("Status", "Failed");
logger.error(ExceptionUtils.getStackTrace(e));
}
return map;
} else {
map.put("Status", "Not Allowed");
return map;
}

}
public void storeOTP(String otp, String MobileNo) {
try {

TApplicantProfile tApplicantProfile = tAplicantprofileRepo.getTApplicantProfileByMobile(MobileNo);
if (tApplicantProfile != null) {
String stDate = String.valueOf(tApplicantProfile.getDTMSTARTTIME());
SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
long starttime = sd.parse(stDate).getTime();

long currenttime = new Date().getTime();
long time = (TimeUnit.MILLISECONDS.toMinutes(currenttime - starttime));
if (time > 10) {
tApplicantProfile.setINTCOUNTOTP(0);
}
tApplicantProfile.setVCHOTP(otp);
tApplicantProfile.setDTMLASTACTIVITYTIME(new Timestamp(System.currentTimeMillis()));
tApplicantProfile.setDTMSTARTTIME(new Timestamp(System.currentTimeMillis()));
tApplicantProfile.setINTCOUNT(0);
String ss = String.valueOf(tApplicantProfile.getDTMSTARTTIME());
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
java.util.Date date = sdf.parse(ss);
long fetch = date.getTime();
long before = fetch + (10 * 60 * 1000);// 10 mins
tApplicantProfile.setDTMLASTTIME(new Timestamp(before));
tApplicantProfile = tAplicantprofileRepo.save(tApplicantProfile);
} else {
tApplicantProfile = new TApplicantProfile();
tApplicantProfile.setVCHMOBILENO(MobileNo);
tApplicantProfile.setVCHOTP(otp);
tApplicantProfile.setDTMLASTACTIVITYTIME(new Timestamp(System.currentTimeMillis()));
tApplicantProfile.setDTMSTARTTIME(new Timestamp(System.currentTimeMillis()));
tApplicantProfile.setINTCOUNT(0);
tApplicantProfile.setINTCOUNTOTP(0);
String ss = String.valueOf(tApplicantProfile.getDTMSTARTTIME());
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
java.util.Date date = sdf.parse(ss);
long fetch = date.getTime();
long before = fetch + (10 * 60 * 1000);// 10 mins
tApplicantProfile.setDTMLASTTIME(new Timestamp(before));
tApplicantProfile = tAplicantprofileRepo.save(tApplicantProfile);
}

} catch (Exception e) {
logger.error(ExceptionUtils.getStackTrace(e));
}
}

private static String otpGenertor() {
SecureRandom random = new SecureRandom();
int num = random.nextInt(100000);
String formatted = String.format("%06d", num);
return formatted;
}
public String checksendOTP(String mobileNo) {
try {
TApplicantProfile tApplicantProfile = tAplicantprofileRepo.getTApplicantProfileByMobile(mobileNo);
if (tApplicantProfile != null) {
int count = tApplicantProfile.getINTCOUNTOTP();
String ss = String.valueOf(tApplicantProfile.getDTMSTARTTIME());
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
java.util.Date date = sdf.parse(ss);
long starttime = date.getTime();

Date newDate = new Date();
long currenttime = newDate.getTime();

String ss1 = String.valueOf(tApplicantProfile.getDTMLASTTIME());
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
java.util.Date date1 = sdf1.parse(ss1);
long endtime = date1.getTime();
long time = (TimeUnit.MILLISECONDS.toMinutes(currenttime - starttime)); 
if (tApplicantProfile.getVCHMOBILENO().equals(mobileNo)) {
if (tApplicantProfile.getINTCOUNTOTP() < 3) {
tApplicantProfile.setINTCOUNTOTP(++count);
tApplicantProfile = tAplicantprofileRepo.save(tApplicantProfile);
return "success";
} else {
if (time > 10) {
tApplicantProfile.setINTCOUNTOTP(0);
tApplicantProfile = tAplicantprofileRepo.save(tApplicantProfile);
                         if(tApplicantProfile.getINTCOUNTOTP() == 0) {
tApplicantProfile.setINTCOUNTOTP(++count);
//System.out.println(tApplicantProfile);
tApplicantProfile = tAplicantprofileRepo.save(tApplicantProfile);
return "success";
                         }
}
}
return "block";
} else {
return "block";
}
} else {
//System.out.println("NOT FOUND USER");
return "success";
}
} catch (Exception e) {
logger.error(ExceptionUtils.getStackTrace(e));
return "failure";
}
}

@Override
public JSONObject validateOtpForEmp(String mobile, String otp) {
JSONObject jsonObject = new JSONObject();
TApplicantProfile tApplicantProfile = null;
try {
//    String response = "success";
   String response = this.getOtpDetailsofUser(otp, mobile);
   if (response.equals("success")) {
    jsonObject.put("status", HttpStatus.OK.value());
    jsonObject.put("msg", "OTP Verified Successfully.");
tApplicantProfile = tAplicantprofileRepo.getTApplicantProfileByMobile(mobile);
this.logout(mobile);
TOnlineServiceApplication application = tOnlineServiceApplicationRepository.getByIntProfileId2(tApplicantProfile.getINTPROFILEID());
if (application != null) {
jsonObject.put("applicationStatus", "1");
jsonObject.put("applicationserviceid", application.getIntOnlineServiceId());
} else {
jsonObject.put("applicationStatus", "0");
jsonObject.put("applicationserviceid", 0);
}
TOnlineServiceApproval approval = tOnlineServiceApprovalRepository.getByIntProfileId2(tApplicantProfile.getINTPROFILEID());
if (approval != null) {
jsonObject.put("approvalStatus", "1");
jsonObject.put("approvalserviceid", approval.getIntOnlineServiceId());
} else {
jsonObject.put("approvalStatus", "0");
jsonObject.put("approvalserviceid", 0);
}
//System.out.println(tApplicantProfile);

jsonObject.put("profileId", tApplicantProfile.getINTPROFILEID());
jsonObject.put("hospitalName", tApplicantProfile.getVCHAPPLICANTNAME());
jsonObject.put("mobileNumber", tApplicantProfile.getVCHMOBILENO());

} else if (response.equals("block")) {//response block
jsonObject.put("status", HttpStatus.UNAUTHORIZED.value());
jsonObject.put("msg", "Maximum Limit Excide.");

} else {
jsonObject.put("status", HttpStatus.NOT_FOUND.value());
jsonObject.put("msg", "OTP Not Verified.");
}

} catch (Exception ex) {
throw new RuntimeException(ex);
}
return jsonObject;
}
private String getOtpDetailsofUser(String otp, String MobileNo) throws ParseException {
TApplicantProfile tApplicantProfile = tAplicantprofileRepo.getTApplicantProfileByMobile(MobileNo);
if (tApplicantProfile != null) {
int count = tApplicantProfile.getINTCOUNT();
String ss = String.valueOf(tApplicantProfile.getDTMSTARTTIME());
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
java.util.Date date = sdf.parse(ss);
long starttime = date.getTime();

Date newDate = new Date();
long currenttime = newDate.getTime();
String ss1 = String.valueOf(tApplicantProfile.getDTMLASTTIME());
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
java.util.Date date1 = sdf1.parse(ss1);
long endtime = date1.getTime();
//System.out.println(endtime);
//System.out.println(TimeUnit.MILLISECONDS.toMinutes(currenttime - starttime));
long time = (TimeUnit.MILLISECONDS.toMinutes(currenttime - starttime));
if (time < 10) {
if (tApplicantProfile.getVCHOTP().equals(otp)) {
if (tApplicantProfile.getINTCOUNT() < 4)
return "success";
else
return "block";
} else {

tApplicantProfile.setINTCOUNT(++count);
tApplicantProfile = tAplicantprofileRepo.save(tApplicantProfile);
if (tApplicantProfile.getINTCOUNT() < 4)
return "failure";

else {
return "block";
} 
}
} else {
//System.out.println("TIME LIMIT EXCIDE");
return "failure";
}
} else {
//System.out.println("NOT FOUND USER");
return "failure";
}
}
public void logout(String mobileNo) {
TApplicantProfile tApplicantProfile = tAplicantprofileRepo.getTApplicantProfileByMobile(mobileNo);
if (tApplicantProfile != null) {

tApplicantProfile.setINTCOUNTOTP(0);
tApplicantProfile = tAplicantprofileRepo.save(tApplicantProfile);

}
}

}