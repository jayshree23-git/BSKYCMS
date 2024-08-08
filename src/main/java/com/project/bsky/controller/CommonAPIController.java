package com.project.bsky.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Regd;
import com.project.bsky.bean.VerifyOTP;
import com.project.bsky.config.dto.HospLogin;
import com.project.bsky.entity.TApplicantProfile;
import com.project.bsky.entity.TOnlineServiceApplication;
import com.project.bsky.entity.TOnlineServiceApproval;
import com.project.bsky.repository.ProcessRepository;
import com.project.bsky.repository.TApplicantProfileRepository;
import com.project.bsky.repository.TOnlineServiceApplicationRepository;
import com.project.bsky.repository.TOnlineServiceApprovalRepository;
//import com.service.demo.service.CommonAPIService;
import com.project.bsky.service.DynamicFormConfigurationService;
import com.project.bsky.util.Encryption;
import com.project.bsky.util.OTPGenerator;

@RestController
@CrossOrigin("*")
public class CommonAPIController {

	@Autowired
	private ProcessRepository processRepository;
	@Autowired
	private TApplicantProfileRepository tapplicantProfileRepository;
	@Autowired
	private DynamicFormConfigurationService dynamicFormConfigurationService;
	@Autowired
	private TApplicantProfileRepository tApplicantProfileRepository;
	@Autowired
	private Logger logger;
//	@Autowired
//	private CommonAPIService commonAPIService;
	
	@Autowired
	private TOnlineServiceApplicationRepository tOnlineServiceApplicationRepository;
	
	@Autowired
	private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;

	public static String OTP = "";
	public static String OTP1="";

	@PostMapping(value = "/verifyRegd")
	public ResponseEntity<?> verifyRegd(@RequestBody Regd regd) {
		JSONObject jsonObject = new JSONObject();
		try {
			TApplicantProfile tApplicantProfile = tApplicantProfileRepository.getTApplicantProfileByMobile(regd.getMobile());
			if (tApplicantProfile == null) {
				String otp = OTPGenerator.otpGenerator();
				jsonObject.put("status", HttpStatus.OK.value());
				jsonObject.put("msg", "OTP Created and Send Successfully.");
				jsonObject.put("otp", otp);
				jsonObject.put("encText", Encryption.otpEncryption(otp) + "." + Encryption.otpEncryption(regd.getMobile()));
				OTP =  Encryption.otpEncryption(otp);
				
				TApplicantProfile applicantProfile = new TApplicantProfile();
				applicantProfile.setVCHMOBILENO(regd.getMobile());
				tapplicantProfileRepository.save(applicantProfile);
			}else {
				jsonObject.put("status", HttpStatus.NOT_FOUND.value());
				jsonObject.put("msg", "Mobile No Already Exist.");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e);
		}
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

	@PostMapping(value = "/verifyOTP")
	public ResponseEntity<?> verifyOTP(@RequestBody VerifyOTP verifyOTP) throws Exception {
		JSONObject jsonObject = new JSONObject();
		TApplicantProfile tApplicantProfile = null;
		List<TApplicantProfile> obj1 = new ArrayList<>();
		
		try {
			if (Encryption.otpEncryption(verifyOTP.getOtp()).equals(verifyOTP.getEncText().split("\\.")[0]) &&
					Encryption.otpEncryption(verifyOTP.getMobile()).equals(verifyOTP.getEncText().split("\\.")[1])) {
				jsonObject.put("status", HttpStatus.OK.value());
				jsonObject.put("msg", "OTP Verified Successfully.");
				if(verifyOTP.getFlag()==0) {
					 tApplicantProfile = tApplicantProfileRepository.getTApplicantProfileByMobile(verifyOTP.getMobile());
					tApplicantProfile.setVCHAPPLICANTNAME(verifyOTP.getHosName());
					tApplicantProfile.setVCHMOBILENO(verifyOTP.getMobile());
					tApplicantProfile = tApplicantProfileRepository.save(tApplicantProfile);
					jsonObject.put("applicationserviceid",0);
					jsonObject.put("applicationStatus", "0");
					jsonObject.put("approvalStatus", "0");
					jsonObject.put("approvalserviceid",0);
					
				}
				else if(verifyOTP.getFlag()==1){
				 tApplicantProfile = tApplicantProfileRepository.getTApplicantProfileByMobile(verifyOTP.getMobile());
//				tApplicantProfile.setVCHAPPLICANTNAME(null);
//				tApplicantProfile.setVCHMOBILENO(verifyOTP.getMobile());
				tApplicantProfile = tApplicantProfileRepository.save(tApplicantProfile);
//  			tAtApplicantProfileRepository.getDataByProfileId(tApplicantProfile.getINTPROFILEID());
				
				TOnlineServiceApplication application = tOnlineServiceApplicationRepository.getByIntProfileId2(tApplicantProfile.getINTPROFILEID());
				if(application!=null) {
					jsonObject.put("applicationStatus", "1");
					jsonObject.put("applicationserviceid", application.getIntOnlineServiceId());
				}else {
					jsonObject.put("applicationStatus", "0");
					jsonObject.put("applicationserviceid",0);
				}
//				
				TOnlineServiceApproval approval = tOnlineServiceApprovalRepository.getByIntProfileId(tApplicantProfile.getINTPROFILEID());
				if(approval!=null) {
					jsonObject.put("approvalStatus", "1");
					jsonObject.put("approvalserviceid",approval.getIntOnlineServiceId());
				}else {
					jsonObject.put("approvalStatus", "0");
					jsonObject.put("approvalserviceid",0);
				}
				
				
				
				
				
//				obj1 = tApplicantProfileRepository.findAll();
//				for(TApplicantProfile test : obj1) {
//				Integer	id = test.getINTPROFILEID();
//					TOnlineServiceApplication application1 = applicationRepository.getByIntProfileId(test.getINTPROFILEID());
//					////System.out.println("Data : " + application1);
//					if(application1 == null) {
//						jsonObject.put("msg1", "Profile ID Not Found in TOnlineServiceApplication");
//					}else {
//						jsonObject.put("msg1", "0");
//					}
//					
//					TOnlineServiceApproval approval = approvalRepository.getByIntProfileId(test.getINTPROFILEID());
//					if(approval == null) {
//						jsonObject.put("msg2", "Profile ID Not Found in TOnlineServiceApproval");
//					}else {
//						jsonObject.put("msg1", "1");
//					}
//					
//				}
				}
				////System.out.println(tApplicantProfile);
				
				jsonObject.put("profileId", tApplicantProfile.getINTPROFILEID());
				jsonObject.put("hospitalName", tApplicantProfile.getVCHAPPLICANTNAME());
				jsonObject.put("mobileNumber", tApplicantProfile.getVCHMOBILENO());
				
//				jsonObject.put("mobileNumber", tApplicantProfile.getVCHMOBILENO());
//				jsonObject.put("hospitalName",  tApplicantProfile.getVCHAPPLICANTNAME());
			}else {
				jsonObject.put("status", HttpStatus.NOT_FOUND.value());
				jsonObject.put("msg", "OTP Not Verified.");
			}
		
		}catch (NoSuchAlgorithmException ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			throw new RuntimeException(ex);
		}
		return ResponseEntity.ok(jsonObject.toString());
	}
	@PostMapping(value="/hospLogin")
	public ResponseEntity<?> hospLogin(@RequestBody HospLogin hospLogin){
		JSONObject jsonObject = new JSONObject();
		////System.out.println(hospLogin);
		try {
			TApplicantProfile tApplicantProfile = tApplicantProfileRepository.getTApplicantProfileByMobile(hospLogin.getMobile());
			////System.out.println("TApplicantProfile : "+tApplicantProfile);
			if(tApplicantProfile != null) {
				String otp = OTPGenerator.otpGenerator();
				//Send OTP Method
				////System.out.println("OTP Send to Mobile Number : "+otp);
				jsonObject.put("status", HttpStatus.OK.value());
				jsonObject.put("msg", "OTP Created and Send Successfully.");
				jsonObject.put("otp", otp);
				jsonObject.put("encText", Encryption.otpEncryption(otp) + "." + Encryption.otpEncryption(hospLogin.getMobile()));
//				jsonObject.put("profileId", tApplicantProfile.getINTPROFILEID());
//				jsonObject.put("applicantName", tApplicantProfile.getVCHAPPLICANTNAME());
//				jsonObject.put("mobileNumber", tApplicantProfile.getVCHMOBILENO());
				jsonObject.put("hospitalName",  tApplicantProfile.getVCHAPPLICANTNAME());
			}else {
				jsonObject.put("status", HttpStatus.NOT_FOUND.value());
				jsonObject.put("msg", "Mobile No Not Exist.");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e);
		}
		return ResponseEntity.ok(jsonObject.toString());
	}
}