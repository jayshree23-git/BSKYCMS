package com.project.bsky.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalUserSaveBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.DCConfigurationBean;
import com.project.bsky.model.HospitalInformation;

public interface HospitalUserSaveService {

	List<HospitalInformation> getDetails();

	List<HospitalBean> getHospitals(String stateId, String districtId, String cpdApprovalRequired, String snoTagged,
			String categoryId, Integer tmsActive);

	HospitalInformation saveDetails(HospitalUserSaveBean hospitalUserSaveBean);

	Integer checkHospitalByCode(String hospitalCode);

	void deleteById(Integer status);

	HospitalInformation update(HospitalUserSaveBean hospitalInformation);

	Integer updateProfile(HospitalUserSaveBean hospitalInformation);

	HospitalInformation getbyid(Integer hospitalId);

	HospitalInformation getbyuserid(Long userId);

	List<HospitalInformation> getHospitalFilteredByState(@RequestParam(value = "stateId") String stateId);

	List<HospitalInformation> getHospitalFilteredByDistrict(@RequestParam(value = "stateId") String stateId,
			@RequestParam(value = "districtId") String districtId);

	Response saveDCConfiguration(DCConfigurationBean bean);

	Response updateDCConfiguration(DCConfigurationBean bean);

	List<DCConfigurationBean> getAllDcConfigurationDetails(@RequestParam(value = "stateId") String stateId,
			@RequestParam(value = "districtId") String districtId);

	DCConfigurationBean getDcConfigurationDetailsById(Long dcUserId);

	Integer checkDCHospitalName(String hospitalCode, Long dcId);

	Integer checkHospitalNameForOther(String hospitalCode, Long assignedDc);

	JSONObject getHospDetails(Integer hospitalId);

	JSONArray getDistinctDC(Long userId);

	JSONArray getDCConfigDetails(Long userId, String stateId, String districtId);

}
