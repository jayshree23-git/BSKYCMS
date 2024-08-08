package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.TemporyOverrideCodeBean;
import com.project.bsky.bean.Uidauthmodetagbean;
import com.project.bsky.bean.UpdateEmpanelHospData;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.service.QCAdminService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class QCAdminController {

	@Autowired
	private QCAdminService qCAdminService;

	@Autowired
	private Logger logger;

	// hospitallist for select hospital name
	@ResponseBody
	@GetMapping(value = "/getAllHospitalList")
	public List<HospitalInformation> getHospitalListForAdmin() {
		List<HospitalInformation> hospitalList = null;
		try {
			hospitalList = qCAdminService.getDetails();
			return hospitalList;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalList;
	}

	// by hospitalcode get data in update page
	@GetMapping(value = "/getDatabyhos")
	@ResponseBody
	public HospitalInformation getDatabyhosCode(
			@RequestParam(value = "hospitalId", required = false) String hospitalId) {

		HospitalInformation listView = null;
		try {
			listView = qCAdminService.listview(hospitalId);
			return listView;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listView;
	}

//	@PostMapping(value = "/updateEmpanelData")
//	public Response updateEmpanelHospitalData(@RequestBody UpdateEmpanelHospData updateEmpanelHospData) {
//		Response returnObj = null;
//		//System.out.println(updateEmpanelHospData);
//		try {
//			returnObj=qCAdminService.updateEmpanelHospitalData(updateEmpanelHospData);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return returnObj;
//	}

	@PostMapping(value = "/updateEmpanelData")
	public Response updateEmpanelHospitalData(
			@RequestParam(required = false, value = "hosCValidDateTo") String hosCValidDateTo,
			@RequestParam(required = false, value = "hospitalCode") String hospitalCode,
			@RequestParam(required = false, value = "hospitalCategoryid") String hospitalCategoryid,
			@RequestParam(required = false, value = "hosCValidDateFrom") String hosCValidDateFrom,
			@RequestParam(required = false, value = "mou") String mou,
			@RequestParam(required = false, value = "mouStartDt") String mouStartDt,
			@RequestParam(required = false, value = "mouEndDt") String mouEndDt,
			@RequestParam(required = false, value = "isBlockActive") String isBlockActive,
			@RequestParam(required = false, value = "mouStatus") String mouStatus,
			@RequestParam(required = false, value = "empanelmentstatus") String empanelmentstatus,
			@RequestParam(required = false, value = "updatedby") String updatedby,
			@RequestParam(required = false, value = "preauthapprovalrequired") String preauthapprovalrequired,
			@RequestParam(required = false, value = "cpdApprovalRequired") String cpdApprovalRequired,
			@RequestParam(required = false, value = "file2") MultipartFile form) {
		Response returnObj = null;
		// System.out.println(hospitalCode+" "+hospitalCategoryid+"
		// "+hosCValidDateFrom+" mou "+mou+" "+mouStartDt+" "+mouEndDt+" "+mouStatus+"
		// "+empanelmentstatus+" "+updatedby+" "+form+" "+isBlockActive);
		UpdateEmpanelHospData updateEmpanelHospData = new UpdateEmpanelHospData();
		updateEmpanelHospData
				.setHosCValidDateTo(hosCValidDateTo.trim().equalsIgnoreCase("null") ? null : hosCValidDateTo);
		updateEmpanelHospData
				.setHosCValidDateFrom(hosCValidDateFrom.trim().equalsIgnoreCase("null") ? null : hosCValidDateFrom);
		updateEmpanelHospData.setHospitalCode(hospitalCode);
		updateEmpanelHospData.setHospitalCategoryid(hospitalCategoryid);
		updateEmpanelHospData.setMou(mou);
		updateEmpanelHospData.setMouStartDt(mouStartDt);
		updateEmpanelHospData.setMouEndDt(mouEndDt);
		updateEmpanelHospData.setIsBlockActive(isBlockActive);
		updateEmpanelHospData.setMouStatus(mouStatus);
		updateEmpanelHospData.setEmpanelmentstatus(empanelmentstatus);
		updateEmpanelHospData.setUpdatedby(updatedby);
		updateEmpanelHospData.setCpdApprovalRequired(cpdApprovalRequired);
		updateEmpanelHospData.setPreauthapprovalrequired(preauthapprovalrequired);
		try {
			returnObj = qCAdminService.updateEmpanelHospitalData(updateEmpanelHospData, form);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@ResponseBody
	@GetMapping(value = "/getHospListforviewPage")
	public List<Object> getHospitalList() {
		List<Object> hospitalList = new ArrayList<Object>();
		try {
			hospitalList = qCAdminService.hospList();
			return hospitalList;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalList;
	}

	@ResponseBody
	@PostMapping(value = "/submituidauthconfig")
	public Map<String, Object> submituidauthconfig(@RequestBody Uidauthmodetagbean bean) {
		Map<String, Object> map = new HashedMap();
		try {
			map = qCAdminService.submituidauthconfig(bean);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", e.getMessage());
			map.put("status", HttpStatus.BAD_REQUEST.value());
			map.put("message", "Something Went Wrong");
		}
		return map;
	}

	@ResponseBody
	@PostMapping(value = "/temporyoverridecode")
	public Map<String, Object> temporyOverrideCode(@RequestBody TemporyOverrideCodeBean bean) {
		Map<String, Object> map = new HashedMap();
		try {
			map = qCAdminService.temporyOverrideCode(bean);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", e.getMessage());
			map.put("status", HttpStatus.BAD_REQUEST.value());
			map.put("message", "Something Went Wrong");
		}
		return map;
	}

	@ResponseBody
	@PostMapping(value = "/temporyoverridecodeview")
	public Map<String, Object> temporyOverrideCodeview(@RequestBody TemporyOverrideCodeBean bean) {
		Map<String, Object> map = new HashedMap();
		List<Map<String, Object>> list = null;
		try {
			list = qCAdminService.temporyOverrideCodeView(bean);
			map.put("data", list);
			map.put("status", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", e.getMessage());
			map.put("status", HttpStatus.BAD_REQUEST.value());
			map.put("message", "Something Went Wrong");
		}
		return map;
	}
	
	@ResponseBody
	@PostMapping(value = "/removetemporyoverridecode")
	public Map<String, Object> removeTemporyOverrideCode(@RequestBody TemporyOverrideCodeBean bean) {
		Map<String, Object> map = new HashedMap(); 
		try {
			map = qCAdminService.removeTemporyOverrideCode(bean); 
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", e.getMessage());
			map.put("status", HttpStatus.BAD_REQUEST.value());
			map.put("message", "Something Went Wrong");
		}
		return map;
	}
	
	@PostMapping("/getMappedAuthDetailsview")
	public ResponseEntity<?> getMappedAuthDetailsview(@RequestBody Map<String, Object> response) {
		Map<String, Object> responseList = new HashedMap();
		try {
			responseList = qCAdminService.getMappedAuthDetails(response);
		} catch (Exception e) {
			e.printStackTrace();
			responseList.put("status", 400);
			responseList.put("message", e.getMessage());
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@GetMapping("/getMappedAuthDetailslog")
	public ResponseEntity<?> getMappedAuthDetailslog(@RequestParam(required = false, value = "hospcode") String hospitalCode) {
		Map<String, Object> responseList = new HashedMap();
		try {
			responseList = qCAdminService.getMappedAuthDetailslog(hospitalCode);
		} catch (Exception e) {
			e.printStackTrace();
			responseList.put("status", 400);
			responseList.put("message", e.getMessage());
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@PostMapping("/saveHospitalDeactivation")
	public ResponseEntity<?> saveHospitalDeactivation(@RequestParam(required = false, value = "hospitalCode") String hospitalCode,
			@RequestParam(required = false, value = "file") MultipartFile file,
			@RequestParam(required = false, value = "adddoc1") MultipartFile adddoc1,
			@RequestParam(required = false, value = "adddoc2") MultipartFile adddoc2,
			@RequestParam(required = false, value = "remark") String remark,
			@RequestParam(required = false, value = "action") Integer action,
			@RequestParam(required = false, value = "actionBy") Long userId) {
		Map<String, Object> responseList = new HashedMap();
		try {
//			JSONArray jsonarr=new JSONArray(hospitalCode);
			responseList = qCAdminService.saveHospitalDeactivation(hospitalCode,remark,action,userId,file,adddoc1,adddoc2);
		} catch (Exception e) {
			e.printStackTrace();
			responseList.put("status", 400);
			responseList.put("message", e.getMessage());
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@GetMapping("/getHospitalDetailsfordeactive")
	public ResponseEntity<?> getHospitalDetailsfordeactive(@RequestParam(required = false, value = "hospcode") String hospitalCode) {
		Map<String, Object> responseList = new HashedMap();
		try {
			responseList = qCAdminService.getHospitalDetailsfordeactive(hospitalCode);
		} catch (Exception e) {
			e.printStackTrace();
			responseList.put("status", 400);
			responseList.put("message", e.getMessage());
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@GetMapping("/getHospitalDeactivionview")
	public ResponseEntity<?> getHospitalDeactivionview(@RequestParam(required = false, value = "hospitalId") String hospitalCode,
			@RequestParam(required = false, value = "stateCode") String statecode,
			@RequestParam(required = false, value = "distCode") String distcode,
			@RequestParam(required = false, value = "action") Integer action) {
		Map<String, Object> responseList = new HashedMap();
		try {
			responseList.put("status", 200);
			responseList.put("message", "Success");
			responseList.put("data", qCAdminService.getHospitalDeactivionview(statecode,distcode,hospitalCode,action));
		} catch (Exception e) {
			e.printStackTrace();
			responseList.put("status", 400);
			responseList.put("message", e.getMessage());
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@GetMapping("/getHospitalDeactivionlog")
	public ResponseEntity<?> getHospitalDeactivionlog(@RequestParam(required = false, value = "hospitalId") String hospitalCode) {
		Map<String, Object> responseList = new HashedMap();
		try {
			responseList.put("status", 200);
			responseList.put("message", "Success");
			responseList.put("data", qCAdminService.getHospitalDeactivionlog(hospitalCode));
		} catch (Exception e) {
			e.printStackTrace();
			responseList.put("status", 400);
			responseList.put("message", e.getMessage());
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/downLoaddeempanelDoc")
	public String commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "File not found";
			} else {
				qCAdminService.downLoaddeempanelDoc(fileName,response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			resp = "Something Went Wrong"+e.getMessage();
		}
		return resp;
	}

}
