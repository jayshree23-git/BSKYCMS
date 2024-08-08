package com.project.bsky.service;

import java.util.List;

import org.json.JSONObject;

import com.project.bsky.bean.EmpanelBean;
import com.project.bsky.bean.Response;

public interface EmpanelmentDetailUpdationService {

	List<Object> hospList(String hospitalCode);

	Response updateEmpanelHospitalData(EmpanelBean logBean);
	
	Response checkDuplicateMobile(String mobile,String profileId);
	
	JSONObject sendOtpForEMP(String mobile);
	
	JSONObject validateOtpForEmp(String mobile,String otp);
}
