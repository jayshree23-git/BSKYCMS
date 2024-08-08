/**
 * 
 */
package com.project.bsky.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CPDConfigurationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.CPDConfigurationLogService;
import com.project.bsky.service.CPDConfigurationService;

/**
 * @author ronauk
 *
 */
@RestController
@RequestMapping(value = "/cpdConfiguration")
public class CPDConfigurationController {

	@Autowired
	private CPDConfigurationService cpdConfigurationservice;

	@Autowired
	private CPDConfigurationLogService logService;

	@Autowired
	private Logger logger;

	/**
	 * This method is used for save Cpd configuration data.
	 *
	 */
	@ResponseBody
	@PostMapping("/saveCPDConfiguration")
	public ResponseEntity<Response> saveCPDConfiguration(@RequestBody CPDConfigurationBean cpdConfigurationBean,
			Response response) {
		try {
			response = cpdConfigurationservice.saveCPDConfiguration(cpdConfigurationBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return ResponseEntity.ok(response);

	}

	@GetMapping("/saveCPDConfigurationLog")
	public ResponseEntity<Response> saveCPDConfigurationLog(
			@RequestParam(required = false, value = "cpdUserId") Integer cpdUserId,
			@RequestParam(required = false, value = "createdBy") Integer createdBy,
			@RequestParam(required = false, value = "ipAddress") String ipAddress, Response response) {
		try {
			response = logService.saveConfigurationLog(cpdUserId, createdBy, ipAddress);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage("Some error happened");
		}
		return ResponseEntity.ok(response);

	}

	@PostMapping("/checkCPDAssignedClaims")
	public ResponseEntity<Response> checkCPDAssignedClaims(@RequestBody CPDConfigurationBean bean, Response response) {
		try {
			if (bean.getHospList() == null || bean.getHospList().size() == 0) {
				response.setMessage("Please select hospital");
				response.setStatus("Info");
			} else {
				String hospname = cpdConfigurationservice.checkCPDAssignedClaims(bean.getCpdId(), bean.getHospList());
				if (hospname != null) {
					response.setMessage("Claims assigned from " + hospname);
					response.setStatus("Info");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happened");
			response.setStatus("Error");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/checkCPDAssignedClaimsForUpdate")
	public ResponseEntity<Response> checkCPDAssignedClaimsForUpdate(@RequestBody CPDConfigurationBean bean,
			Response response) {
		try {
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happened");
			response.setStatus("Error");
		}
		return ResponseEntity.ok(response);
	}

	/**
	 * This method is used for get Cpd configuration data by Id.
	 *
	 */
	@GetMapping(value = "/getCpdById")
	public CPDConfigurationBean getConfigurationDetailsById(
			@RequestParam(required = false, value = "cpdUserId") Integer cpdUserId) {
		return cpdConfigurationservice.getCpdConfigurationDetailsById(cpdUserId);
	}

	/**
	 * This method is used for update Cpd configuration data.
	 *
	 */
	@PostMapping("/updateCpdData")
	public ResponseEntity<Response> updateCpdMasterData(@RequestBody CPDConfigurationBean cPDConfigurationBean,
			Response response) {
		try {
			response = cpdConfigurationservice.updateCpdDetailsData(cPDConfigurationBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getDistinctCPD")
	public String getDistinctCPD(@RequestParam(required = false, value = "bskyUserId") Integer bskyUserId) {
		JSONObject json = new JSONObject();
		try {
			json.put("confList", cpdConfigurationservice.getDistinctCPD(bskyUserId));
			json.put("count", cpdConfigurationservice.getRestrictedHospitals(bskyUserId).size());
		} catch (JSONException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return json.toString();
	}

	@GetMapping(value = "/getCpdConfigDetails")
	public String getCPDConfigDetails(@RequestParam(required = false, value = "bskyUserId") Integer bskyUserId,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId) {

		if (stateId.equalsIgnoreCase("null")) {
			stateId = null;
		}
		if (districtId.equalsIgnoreCase("null")) {
			districtId = null;
		}
		return cpdConfigurationservice.getCPDConfigDetails(bskyUserId, stateId, districtId).toString();
	}
}
