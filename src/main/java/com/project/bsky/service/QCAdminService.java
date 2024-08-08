package com.project.bsky.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.TemporyOverrideCodeBean;
import com.project.bsky.bean.Uidauthmodetagbean;
import com.project.bsky.bean.UpdateEmpanelHospData;
import com.project.bsky.model.HospitalInformation;

public interface QCAdminService {

	HospitalInformation listview(String hospitalId);

	Response updateEmpanelHospitalData(UpdateEmpanelHospData updateEmpanelHospData, MultipartFile form);

	List<HospitalInformation> getDetails();

	List<Object> hospList();

	Map<String, Object> submituidauthconfig(Uidauthmodetagbean bean) throws Exception;

	Map<String, Object> temporyOverrideCode(TemporyOverrideCodeBean bean) throws Exception;

	Map<String, Object> getMappedAuthDetails(Map<String, Object> response) throws Exception;

	Map<String, Object> getMappedAuthDetailslog(String hospitalCode) throws Exception;

	List<Map<String, Object>> temporyOverrideCodeView(TemporyOverrideCodeBean bean) throws Exception;

	Map<String, Object> removeTemporyOverrideCode(TemporyOverrideCodeBean bean) throws Exception;

	Map<String, Object> saveHospitalDeactivation(String hospitalCode, String remark, Integer action, Long userId,
			MultipartFile file, MultipartFile adddoc1, MultipartFile adddoc2) throws Exception;

	Map<String, Object> getHospitalDetailsfordeactive(String hospitalCode) throws Exception;

	List<Object> getHospitalDeactivionview(String statecode, String distcode, String hospitalCode, Integer action) throws Exception;

	List<Object> getHospitalDeactivionlog(String hospitalCode) throws Exception;

	void downLoaddeempanelDoc(String fileName, HttpServletResponse response) throws Exception;

}
