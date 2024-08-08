/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HospObj;
import com.project.bsky.bean.Hospital;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SNOConfigurationBean;
import com.project.bsky.bean.SnaExecBean;
import com.project.bsky.service.SNOConfigurationLogService;
import com.project.bsky.service.SNOConfigurationService;

/**
 * @author ronauk
 *
 */
@RestController
@RequestMapping(value = "/snoConfiguration")
public class SNOConfigurationController {

	@Autowired
	private SNOConfigurationService service;

	@Autowired
	private SNOConfigurationLogService logService;
	
	@Autowired
	private Logger logger;

	/**
	 * This method is used for save Sno configuration data.
	 *
	 */
	@PostMapping("/saveSNOConfiguration")
	public ResponseEntity<Response> saveSNOConfiguration(@RequestBody SNOConfigurationBean bean, Response response) {
		try {
			response = service.saveSNOConfiguration(bean);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return ResponseEntity.ok(response);

	}

	@GetMapping("/saveSNOConfigurationLog")
	public ResponseEntity<Response> saveSNOConfigurationLog(
			@RequestParam(required = false, value = "snoUserId") Integer snoUserId,
			@RequestParam(required = false, value = "createdBy") Integer createdBy,
			@RequestParam(required = false, value = "ipAddress") String ipAddress, Response response) {
		try {
			response = logService.saveConfigurationLog(snoUserId, createdBy, ipAddress);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage("Some error happened");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/saveSNOConfigurationLogForHospital")
	public ResponseEntity<Response> saveSNOConfigurationLogForHospital(
			@RequestParam(required = false, value = "hospitalCode") String hospitalCode,
			@RequestParam(required = false, value = "createdBy") Integer createdBy,
			@RequestParam(required = false, value = "ipAddress") String ipAddress, Response response) {
		try {
			response = logService.saveConfigurationLogForHospital(hospitalCode, createdBy, ipAddress);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage("Some error happened");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/checkSNOAssignedToHosp")
	public ResponseEntity<Response> checkSNOAssignedToHosp(@RequestBody SNOConfigurationBean bean, Response response) {
		boolean SNOhospiltalDuplicacy = false;
		String hospname = null;
		try {
			if (bean.getHospList() == null || bean.getHospList().size() == 0) {
				response.setMessage("Please select hospital");
				response.setStatus("Info");
			} else {
				Integer checkHospital = 0;
				for (HospObj h : bean.getHospList()) {
					checkHospital = service.checkHospitalName(h.getHospitalCode(), bean.getSnoId());
					////System.out.println("---" + checkHospital);
					if (checkHospital > 0) {
						hospname = h.getHospitalName();
						SNOhospiltalDuplicacy = true;
						break;
					}
				}
				if (SNOhospiltalDuplicacy == true) {
					response.setMessage("SNA already assigned to " + hospname);
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

	@PostMapping("/checkSNOAssignedToHospForUpdate")
	public ResponseEntity<Response> checkSNOAssignedToHospForUpdate(@RequestBody SNOConfigurationBean bean,
			Response response) {
		boolean SNOhospiltalDuplicacy = false;
		String hospname = null;
		try {
			Integer checkHospital = 0;
			for (HospObj h : bean.getHospList()) {
				checkHospital = service.checkHospitalName(h.getHospitalCode(), bean.getSnoId());
				////System.out.println("---" + checkHospital);
				if (checkHospital > 0) {
					hospname = h.getHospitalName();
					SNOhospiltalDuplicacy = true;
					break;
				}
			}
			if (SNOhospiltalDuplicacy == true) {
				response.setMessage("SNA already assigned to " + hospname);
				response.setStatus("Info");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/checkSNOAssignedToHospForSNA")
	public ResponseEntity<Response> checkSNOAssignedToHospForSNA(@RequestBody SNOConfigurationBean bean,
			Response response) {
		boolean SNOhospiltalDuplicacy = false;
		// Integer checkSno = service.checkSnoName(bean.getSnoId());
		if (bean.getHospitalCode() == null || bean.getHospitalCode().size() == 0) {
			response.setMessage("Please select hospital");
			response.setStatus("Error");
		} else {
			Integer checkHospital = 0;
			Hospital h = null;
			for (int i = 0; i < bean.getHospitalCode().size(); i++) {
				h = bean.getHospitalCode().get(i);
				checkHospital = service.checkHospitalNameForSNA(h.getHospitalCode(), bean.getSnoId());
				////System.out.println("---" + checkHospital);
				if (checkHospital > 0) {
					SNOhospiltalDuplicacy = true;
					break;
				}
			}
			try {
				if (SNOhospiltalDuplicacy == true) {
					response.setMessage("SNA already assigned to " + h.getHospitalName());
					response.setStatus("Info");
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return ResponseEntity.ok(response);
	}

	/**
	 * This method is used for get all Sno configuration data.
	 *
	 */
	@GetMapping(value = "/getSnoConfigurationDetails")
	public List<SNOConfigurationBean> getConfigurationDetails(
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId) {
		return service.getAllSnoConfigurationDetails(stateId, districtId);
	}

	/**
	 * This method is used for get Sno configuration data by Id.
	 *
	 */
	@GetMapping(value = "/getSnoById")
	public SNOConfigurationBean getConfigurationDetailsById(
			@RequestParam(required = false, value = "snoUserId") Integer snoUserId) {
		return service.getSnoConfigurationDetailsById(snoUserId);

	}

	/**
	 * This method is used for update Sno configuration data.
	 *
	 */
	@PostMapping("/updateSnoData")
	public ResponseEntity<Response> updateSnoMasterData(@RequestBody SNOConfigurationBean sNOConfigurationBean,
			Response response) {
		try {
			response = service.updateSnoDetailsData(sNOConfigurationBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getDistinctSNA")
	public String getDistinctSNA(@RequestParam(required = false, value = "userId") Integer userId) {
		return service.getDistinctSNA(userId).toString();
	}

	@GetMapping(value = "/getSnaConfigDetails")
	public String getSNAConfigDetails(@RequestParam(required = false, value = "userId") Integer userId,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId) {
		////System.out.println(stateId + " : " + districtId);
		if (stateId.equalsIgnoreCase("null")) {
			stateId = null;
		}
		if (districtId.equalsIgnoreCase("null")) {
			districtId = null;
		}
		return service.getSNAConfigDetails(userId, stateId, districtId).toString();
	}

	@PostMapping("/saveSNAExecutive")
	public ResponseEntity<Response> saveSNAExecutive(@RequestBody SnaExecBean bean, Response response) {
		try {
			////System.out.println(bean.toString());
			response = service.saveSNAExecutiveConfiguration(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);

	}

	@GetMapping(value = "/getSNAExecutive")
	public String getSNAExecutive(@RequestParam(required = false, value = "userId") Integer userId) {
		return service.getSNAExec(userId).toString();
	}

	@GetMapping(value = "/getSNAExecutiveMapping")
	public String getSNAExecutiveDetails(@RequestParam(required = false, value = "userId") Integer userId) {
		return service.getSNAExecDetails(userId).toString();
	}

	@GetMapping(value = "/getSnaExecById")
	public SnaExecBean getSnaExecById(@RequestParam(required = false, value = "snoUserId") Integer snoUserId) {
		return service.getSnaExecutiveById(snoUserId);

	}

	@PostMapping("/updateSNAExecutive")
	public ResponseEntity<Response> updateSNAExecutive(@RequestBody SnaExecBean bean, Response response) {
		try {
			////System.out.println(bean.toString());
			response = service.updateSnaExecDetails(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getsnalistbyexecutive")
	public ResponseEntity<?> getSnaListByExecutive(@RequestParam(required = false, value = "userId") Integer userId) {
		String snoList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoList = service.getSnaListByExecutive(userId);
			details.put("status", "success");
			details.put("data", snoList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}
	@GetMapping(value = "/getHospitalListByUserId")
	public ResponseEntity<?> getHospitalListByUserId(@RequestParam(required = false, value = "userId") Integer userId) {
		String snoList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoList = service.getHospitalListById(userId);
			details.put("status", "success");
			details.put("data", snoList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}
}
