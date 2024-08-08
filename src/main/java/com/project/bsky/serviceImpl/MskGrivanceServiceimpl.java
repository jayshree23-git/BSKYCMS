/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.project.bsky.bean.MskGrivanceBean;
import com.project.bsky.bean.Response;
import com.project.bsky.entity.GrievanceOtpAuth;
import com.project.bsky.model.Grivancetype;
import com.project.bsky.repository.GrievanceOtpAuthRepository;
import com.project.bsky.repository.GrivancetypeRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.service.MskGrivanceService;
import com.project.bsky.util.GrievanceDoc;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class MskGrivanceServiceimpl implements MskGrivanceService {
	
	@Autowired
	private GrivancetypeRepository grievancetyperepo;
	
	@Autowired
	private Environment env;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	HospitalInformationRepository hospitalinfo;
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private GrievanceOtpAuthRepository grievanceOtpAuthRepository;
	
	@Value("${whatsAppServerhost.url}")
	private String whatsAppServerUrl;
	
	private Random rand = new Random();

	@Override
	public List<Grivancetype> getactivegrivancetype() {
		return grievancetyperepo.getactivegrivancetype();
	}

	@Override
	public Response savemskgriv(MskGrivanceBean mskgrivbean,
						MultipartFile docfile1, MultipartFile vdofile1,
						MultipartFile docfile2, MultipartFile vdofile2,
						MultipartFile docfile3, MultipartFile vdofile3) {
		Response response=new Response();
		String grivid=null;
		String docpath=env.getProperty("pdf.documentPathForCommon");
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d =mskgrivbean.getDob()!=null && !"".equals(mskgrivbean.getDob())? sdf.parse(mskgrivbean.getDob()):null;
			if(d!=null) {
			if(d.compareTo(new Date())==1) {
				response.setStatus("400");
				response.setMessage("Date of Birth should not be Greater Then Current Date");
				return response;
			}
			}else {
				d=null;
			}
		} catch (ParseException e1) {
			logger.error(ExceptionUtils.getStackTrace(e1));
			response.setStatus("400");
			response.setMessage("InValid Date");
			return response;
		}
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (mskgrivbean.getEmail() != null && !"".equals(mskgrivbean.getEmail())) {
            if(!Pattern.matches(emailRegex, mskgrivbean.getEmail())) {
            	response.setStatus("400");
    			response.setMessage("InValid Email");
    			return response;
            }
        }
        mskgrivbean.setName(mskgrivbean.getName()!=null?mskgrivbean.getName().trim():mskgrivbean.getName());
        String nameRegex = "^[A-Za-z\\s-]+$";
        if(!Pattern.matches(nameRegex, mskgrivbean.getName())) {
        	response.setStatus("400");
			response.setMessage("please provide valid Name");
			return response;
        }
        if(mskgrivbean.getGrvMedium().equalsIgnoreCase("2") && ((mskgrivbean.getHospital().equalsIgnoreCase("") || mskgrivbean.getHospital().equalsIgnoreCase("0")) && mskgrivbean.getDcName()==null)) {
        		 response.setStatus("400");
     			response.setMessage("please provide Select Hospital Name Or DC Name");
     			return response; 
        	 	
        }
        if(!mskgrivbean.getGrvMedium().equalsIgnoreCase("2") && (mskgrivbean.getHospital().equalsIgnoreCase("") || mskgrivbean.getHospital().equalsIgnoreCase("0"))) {
       		    response.setStatus("400");
    			response.setMessage("please provide Select Hospital Name");
    			return response; 
       	 	
       }
		if (mskgrivbean.getGrvMedium().equalsIgnoreCase("10")) {
			if(Strings.isNullOrEmpty(mskgrivbean.getCitizenfeedback())) {
				response.setStatus("400");
    			response.setMessage("please provide Citizen feedback");
    			return response;
			}
			if(Strings.isNullOrEmpty(mskgrivbean.getServicedate())) {
				response.setStatus("400");
    			response.setMessage("please provide Service Taken Date");
    			return response;
			}
		}
        if(mskgrivbean.getHospital()!=null && !"".equals(mskgrivbean.getHospital()) && !"0".equals(mskgrivbean.getHospital())) {
        	Integer hospcount=hospitalinfo.checkhospitalexistornot(mskgrivbean.getHospstate(),mskgrivbean.getHospdist(),mskgrivbean.getHospital());
            if(hospcount==0) {
            	response.setStatus("400");
    			response.setMessage("Hospital is Not Exist");
    			return response;
            }
        }
        
        
		try {
			boolean stop=true;
			while(stop) {
				Integer randomPIN =(int) ((Math.random() * 90000) + 10000);
				String val = "BSKY" + randomPIN;
				Integer count=grievancetyperepo.checkduplicategrivid(val);
				if(count==0){
					stop=false;
					grivid=val;
				}
			}
			Response re=null;			
			String file="";
				if(GrievanceDoc.supportfiledoc(docfile1)==0) {
					re=new GrievanceDoc().savemskgrivdoc(docfile1,docpath);
					file=re.getMessage()+",";
				}else {
					response.setStatus("400");
					response.setMessage("File Not Supported");
					return response;
				}
				if(vdofile1!=null) {
					if(GrievanceDoc.supportfilevdo(vdofile1)==0) {
						re=new GrievanceDoc().savemskgrivdoc(vdofile1,docpath);
						file=file+re.getMessage();
					}else {
						response.setStatus("400");
						response.setMessage("File Not Supported");
						return response;
					}
				}else {
					file=file+"No";
				}
				if(docfile2!=null) {
					if(GrievanceDoc.supportfiledoc(docfile2)==0) {
						re=new GrievanceDoc().savemskgrivdoc(docfile2,docpath);
						file=file+"#"+re.getMessage()+",";
					}else {
						response.setStatus("400");
						response.setMessage("File Not Supported");
						return response;
					}
					if(vdofile2!=null) {
						if(GrievanceDoc.supportfilevdo(vdofile2)==0) {
						re=new GrievanceDoc().savemskgrivdoc(vdofile2,docpath);
						file=file+re.getMessage();
						}else {
							response.setStatus("400");
							response.setMessage("File Not Supported");
							return response;
						}
					}else {
						file=file+"No";
					}
					if(docfile3!=null) {
						if(GrievanceDoc.supportfiledoc(docfile3)==0) {
							re=new GrievanceDoc().savemskgrivdoc(docfile3,docpath);
							file=file+"#"+re.getMessage()+",";
						}else {
							response.setStatus("400");
							response.setMessage("File Not Supported");
							return response;
						}
						if(vdofile3!=null) {
							if(GrievanceDoc.supportfilevdo(vdofile3)==0) {
								re=new GrievanceDoc().savemskgrivdoc(vdofile3,docpath);
								file=file+re.getMessage();
							}else {
								response.setStatus("400");
								response.setMessage("File Not Supported");
								return response;
							}
						}else {
							file=file+"No";
						}
					}
				}
				String jsonoptiontextdetails=jsondata(mskgrivbean.getHospname(),mskgrivbean.getCasetypedata(),mskgrivbean.getHstatename(),
						mskgrivbean.getBlockname(),mskgrivbean.getDistname(),mskgrivbean.getGender(),mskgrivbean.getGrivtypename(),mskgrivbean.getHdistname(),mskgrivbean.getGrvMediumName(),mskgrivbean.getStateName(),mskgrivbean.getPriorityType());
				StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MOSARKAR_GRIVANCE_INSERT")
					.registerStoredProcedureParameter("P_CASETYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOB", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MOBILE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GRIVANCETYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPSTATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPDIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CITIZENFEEDBACK", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SERVICEDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_APPLICATIONID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GENDER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMAIL", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_JSONDATA", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FILE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OTP_VERIFY_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GRIVANCEMEDIUM", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRIORITY_TYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DCNAME", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FORWARDSTATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_out", Long.class, ParameterMode.OUT);
			
			storedProcedureQuery.setParameter("P_CASETYPE", mskgrivbean.getCasetype());
			storedProcedureQuery.setParameter("P_NAME", mskgrivbean.getName());
			storedProcedureQuery.setParameter("P_DOB",d!=null?sdf1.format(d):null);
			storedProcedureQuery.setParameter("P_MOBILE", mskgrivbean.getMobile());
			storedProcedureQuery.setParameter("P_DISTRICTID", mskgrivbean.getDistrictId());
			storedProcedureQuery.setParameter("P_BLOCKID", mskgrivbean.getBlockId());
			storedProcedureQuery.setParameter("P_GRIVANCETYPE", mskgrivbean.getGrivtype());
			storedProcedureQuery.setParameter("P_HOSPSTATE", mskgrivbean.getHospstate());
			storedProcedureQuery.setParameter("P_HOSPDIST", mskgrivbean.getHospdist());
			storedProcedureQuery.setParameter("P_HOSPITAL", mskgrivbean.getHospital());
			storedProcedureQuery.setParameter("P_SERVICEDATE", mskgrivbean.getServicedate());
			storedProcedureQuery.setParameter("P_APPLICATIONID", grivid);	
			storedProcedureQuery.setParameter("P_GENDER", mskgrivbean.getGender());	
			storedProcedureQuery.setParameter("P_EMAIL", mskgrivbean.getEmail());	
			storedProcedureQuery.setParameter("P_USERID", mskgrivbean.getUserid());
			storedProcedureQuery.setParameter("P_DESCRIPTION",mskgrivbean.getDesc());
			storedProcedureQuery.setParameter("P_JSONDATA", jsonoptiontextdetails);
			storedProcedureQuery.setParameter("P_FILE", file);
			storedProcedureQuery.setParameter("P_OTP_VERIFY_STATUS", mskgrivbean.getOtpverifyStatus());
			storedProcedureQuery.setParameter("P_CITIZENFEEDBACK", mskgrivbean.getCitizenfeedback());
			storedProcedureQuery.setParameter("P_GRIVANCEMEDIUM", mskgrivbean.getGrvMedium());
			storedProcedureQuery.setParameter("P_STATE", mskgrivbean.getState());
			storedProcedureQuery.setParameter("P_PRIORITY_TYPE", mskgrivbean.getPriorityType());
			storedProcedureQuery.setParameter("P_DCNAME", mskgrivbean.getDcName());
			storedProcedureQuery.setParameter("P_FORWARDSTATUS", mskgrivbean.getForwardStatus());	
			
			storedProcedureQuery.execute();	
			Long onlineid=(Long) storedProcedureQuery.getOutputParameterValue("p_out");
			if(onlineid!=null && onlineid!=0) {
				sendWhatsAppMsgForGrievance(onlineid, grivid);
				response.setStatus("200");
				response.setMessage("Grivance Requested generated Under Registration ID : "+grivid);
			}else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong");
			}			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}
	
	private String jsondata(String hospitalname,String casetype,String hstatename,String blockname,
							String distname,String gender,String grivtypeid,String hdistname,String grvMedium,String state,String priority) {
		String s=null;
		JSONObject option1 = new JSONObject();
		try {
			option1.put("GRIEVANCE_MEDIUM_ID",grvMedium);
			option1.put("HOSPITAL_NAME",hospitalname);
			option1.put("DECLARATION","I hereby state that the facts mentioned above are true to best of my knowledge and belief.");
			option1.put("CASE_TYPE",casetype);
			option1.put("STATECODE",state);			
			if(!blockname.equals("") && !blockname.equals(null)){
				option1.put("BLOCKCODE",blockname);
			}
			option1.put("GENDER",gender);
			option1.put("GRIEVANCETYPE_ID",grivtypeid);
			option1.put("DISTRICTCODE",distname);
			option1.put("GRIEVANCEBY_ID","Others");
			option1.put("PRIORITY_TYPE", priority);
			option1.put("H_STATE_CODE", hstatename);
			option1.put("H_DISTRICT_CODE", hdistname);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}		
		s=option1.toString();
		return s;
	}

	@Override
	public List<Object> mskrecordview(String statecode, String distcode, String hospcode) {
		List<Object> list=new ArrayList<Object>();
		ResultSet rs=null;
		Map<String, Object> details;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MOSARKAR_GRIVANCE_VIEW")
					.registerStoredProcedureParameter("P_ONLINESERVICE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_ONLINESERVICE_ID", null);
			storedProcedureQuery.setParameter("P_ACTION_CODE", 1);
			storedProcedureQuery.setParameter("P_STATE_CODE", statecode);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", distcode);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospcode);
			
			storedProcedureQuery.execute();	
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");			
			while(rs.next()) {
				details = new HashMap<>();
				details.put("applicationno", rs.getString(1));
				details.put("casetype", rs.getString(2));
				details.put("priority", rs.getString(3));
				details.put("benificiary", rs.getString(4));
				details.put("gender", rs.getString(5));
				details.put("dateofbirth", rs.getString(6));
				details.put("contactno", rs.getString(7));
				details.put("state", rs.getString(8));
				details.put("dist", rs.getString(9));
				details.put("block", rs.getString(10));
				details.put("email", rs.getString(11));
				details.put("hospitalname", rs.getString(12));
				details.put("hospitalsate", rs.getString(13));
				details.put("hospitaldist", rs.getString(14));
				details.put("cityzen", rs.getString(15));
				details.put("grivtype", rs.getString(16));
				details.put("grivdesc", rs.getString(17));
				details.put("createon", rs.getString(18));
				details.put("serviceid", rs.getString(19));
				details.put("grievanceMedium", rs.getString(20));
				list.add(details);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return list;
	}

	@Override
	public List<Object> docdetailsbyserviceid(Long serviceid) {
		List<Object> list=new ArrayList<Object>();
		ResultSet rs=null;
		Map<String, Object> details;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MOSARKAR_GRIVANCE_VIEW")
					.registerStoredProcedureParameter("P_ONLINESERVICE_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_ONLINESERVICE_ID", serviceid);
			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_STATE_CODE", null);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", null);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", null);
			storedProcedureQuery.execute();	
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");			
			while(rs.next()) {
				details = new HashMap<>();
				details.put("docname", rs.getString(1));
				details.put("medianame", rs.getString(2));
				list.add(details);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return list;
	}

	@Override
	public void downgrivdoc(HttpServletResponse response, String fileName) {
		String folderName = null;
		String docpath=env.getProperty("pdf.documentPathForCommon");
		try {
			GrievanceDoc.downgrivdoc(fileName, response, docpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public void sendWhatsAppMsgForGrievance(Long intOnlineServiceId, String vchApplicationNo) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("text/plain");
			RequestBody body = RequestBody.create(mediaType, "");
			String url = String.format("%ssendMoSarkarGrievance?intOnlineServiceId=%s&mediaNumber=%s", whatsAppServerUrl,
					intOnlineServiceId, vchApplicationNo);
			Request request = new Request.Builder().url(url).method("POST", body).build();
			okhttp3.Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				System.out.println("Request successful. Response: " + response.body().string());
			} else {
				System.out.println("Request unsuccessful. Response: " + response.body().string());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public JSONObject sendOtpForGrv(String mobile) {
		JSONObject jsonObject = new JSONObject();
		try {
			  if(mobile.length()==10) {
				  Map<String, String> map = sendOTP(mobile.trim());
					if (map.get("Status").equals("Success")) {
						jsonObject.put("status", "success");
						jsonObject.put("phone",
								"*******" + String.valueOf(mobile).substring(String.valueOf(mobile).length() - 3));
						jsonObject.put("rePhone", mobile.trim());
					} else {
						jsonObject.put("status", "fail");
						jsonObject.put("message", "Unable to Send otp as server has down. Please try again later.");
					}
			  }else {
				    jsonObject.put("status", "fail");
					jsonObject.put("message", "Mobile Number is invalid");
			  }
				
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return jsonObject;
	}
	public Map<String, String> sendOTP(String mobileNo) {
		Map<String, String> map = new HashMap<>();
			String otp = otpGenertor();
			String responseString = null;
			String message = "Dear User, Your One Time Password (OTP) for Grievance Registration is " + otp
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
				}
				Map<String, String> otpCheck = storeOTP(otp, mobileNo);
				if(otpCheck.get("status").equals("success")) {
					map.put("Status", "Success");
				}else {
					map.put("Status", "Failed");
				}
			} catch (UnsupportedEncodingException e) {
				map.put("Status", "Failed");
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				map.put("Status", "Failed");
				e.printStackTrace();
			} catch (IOException e) {
				map.put("Status", "Failed");
				e.printStackTrace();
			} catch (Exception e) {
				map.put("Status", "Failed");
				e.printStackTrace();
			}
			return map;
	}
	public Map<String, String> storeOTP(String otp, String mobileNo) {
		Map<String, String> response=new HashMap<>();
		try {
			GrievanceOtpAuth otpAuth = new GrievanceOtpAuth();
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
				if (String.valueOf(mobileNo).length() == 10) {
					otpAuth.setCreatedOn(date);
					otpAuth.setMobileNo(Long.parseLong(mobileNo));
					otpAuth.setOtp(Integer.parseInt(otp));
					otpAuth.setOtpVarifierStatus('0');
					otpAuth.setStatusFlag('0');
					grievanceOtpAuthRepository.save(otpAuth);
					response.put("status", "success");
				}
						
		} catch (Exception e) {
			response.put("status", "failed");
			e.printStackTrace();
		}
		return response;
	}
	private String otpGenertor() {
		Integer otp = 100_000 + this.rand.nextInt(900_000);
		return String.valueOf(otp);
	}

	@Override
	public String validateotpgrievance(Long mobileNo, Integer otp) throws JSONException {
		JSONObject responseBean = new JSONObject();
		try {
			GrievanceOtpAuth otpAuth = grievanceOtpAuthRepository.getOTPAuthLatest(mobileNo);
			Integer otp1 = otpAuth.getOtp();
			if (otp1.equals(otp)) {
				responseBean.put("statusFlag", "1");
				responseBean.put("status", "success");
				responseBean.put("message", "OTP validated Successful");
			} else {
				responseBean.put("statusFlag", "0");
				responseBean.put("status", "fail");
				responseBean.put("message", "Invalid OTP");
			}
		} catch (Exception e) {
			responseBean.put("statusFlag", "0");
			responseBean.put("status", "fail");
			responseBean.put("message", "Some error happen");
		}
		return responseBean.toString();
	}
}
