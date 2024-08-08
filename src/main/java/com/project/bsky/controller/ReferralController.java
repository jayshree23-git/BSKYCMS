package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.ReferralBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HealthCardSample;
import com.project.bsky.model.Scheme;
import com.project.bsky.model.SchemeCategoryMaster;
import com.project.bsky.service.ReferralService;

@CrossOrigin
@Controller
@RequestMapping(value = "/api")
public class ReferralController {

	@Autowired
	private ReferralService referralService;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/savereferal")
	@ResponseBody
	public Response saveReferal(@RequestBody ReferralBean referral) {

		return referralService.saveReferals(referral);
	}

	@ResponseBody
	@PostMapping("/savereferaldoc")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "refId") String refId) {
		String message = "";
		try {
			referralService.saveDoc(file, Long.parseLong(refId));
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	@ResponseBody
	@GetMapping(value = "/getPatientData")
	public List<Object> getPatientData(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "hospitacode", required = false) String hospitacode) throws Exception {
		List<Object> getPatientDetails = null;
		try {
			getPatientDetails = referralService.getPatientDetails(userId, fromDate, toDate, hospitacode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getPatientDetails;

	}

	@ResponseBody
	@GetMapping(value = "/getPatientDataByID")
	public List<Object> getPatientDataByID(@RequestParam(value = "id", required = false) Long id) throws Exception {
		List<Object> getPatientDetailsById = null;
		try {
			getPatientDetailsById = referralService.getPatientDataByID(id);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getPatientDetailsById;

	}

	@ResponseBody
	@GetMapping(value = "/getNameByCardNo")
	public List<HealthCardSample> getNameByCardNo(@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "schemeCategoryId", required = false) Integer schemeCategoryId)
			throws Exception {
		List<HealthCardSample> getPatientName = null;
		try {
			getPatientName = referralService.getNameByCardNo(schemeCategoryId,urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getPatientName;
	}

	@ResponseBody
	@GetMapping(value = "/getAgeByName")
	public HealthCardSample getAgeByName(@RequestParam(value = "name", required = false) String name) throws Exception {
		HealthCardSample sample = null;
		try {
			sample = referralService.getAgeByName(name);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return sample;
	}

	@ResponseBody
	@PostMapping(value = "/updatePatientDetails")
	public ResponseEntity<Response> updatePatientDetails(@RequestBody ReferralBean bean, Response response) {
		try {
			response = referralService.updatePatientDetails(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);

	}

	@GetMapping("/downloadFileForReferral")
	public void downloadFile(@RequestParam("data") String enCodedJsonString, HttpServletResponse response)
			throws JSONException {
		String resp = "";
		String year = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String hCode = json.getString("h");
		String dateOfAdm = json.getString("d");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "File not found";
			} else {
				year = dateOfAdm.substring(0, 4);
				referralService.downloadFileForReferral(fileName, year, hCode, response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@ResponseBody
	@GetMapping("/getHospitallist")
	public List<Object> getHospitallistdropdown(@RequestParam(value = "userid") Long userid) throws Exception {
		List<Object> claiList = null;
		try {
			claiList = referralService.gethospitallistdcwise(userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claiList;
	}
	
	@ResponseBody
	@GetMapping("/getSchemeList")
	public List<Scheme> getSchemeList(){
		List<Scheme> list=null;
		try {
			list = referralService.getSchemeList();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	@ResponseBody
	@GetMapping("/getSchemeCategoryListById")
	public List<SchemeCategoryMaster> getSchemeCategory(@RequestParam(value = "schemeId", required = false) Integer schemeId){
		List<SchemeCategoryMaster> list=null;
		try {
			list = referralService.getSchemeCategoryById(schemeId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
		
	}

}
