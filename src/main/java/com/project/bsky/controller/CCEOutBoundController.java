package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetails;
import com.project.bsky.service.CCEOutBoundService;

@RestController
@RequestMapping(value = "/api")
public class CCEOutBoundController {

	@Autowired
	private CCEOutBoundService cceOutBoundService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getCceOutBound")
	@ResponseBody
	public ResponseEntity<?> getCceOutBoundData(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "formDate", required = false) String formDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "cceId", required = false) Long cceId,
			@RequestParam(value = "pageIn", required = false) Integer pageIn,
			@RequestParam(value = "pageEnd", required = false) Integer pageEnd,
			@RequestParam(value = "queryStatus", required = false) String queryStatus,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> getCceOutBoundData = new ArrayList<Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceOutBoundService.getCceOutBoundData(userId, formDate, toDate, action, hospitalCode, cceId, pageIn,
					pageEnd, queryStatus, stateCode, distCode);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				getCceOutBoundData = entry.getValue();
				json.put("list", getCceOutBoundData);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = "/updateCCeOutBound")
	@ResponseBody
	public ResponseEntity<?> getCceOutBoundData(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "cceId") Long cceId, @RequestParam(value = "urn") String urn,
			@RequestParam(value = "hospitalCode") String hospitalCode,
			@RequestParam(value = "cceDoc1", required = false) MultipartFile cceDoc1,
			@RequestParam(value = "cceDoc2", required = false) MultipartFile cceDoc2,
			@RequestParam(value = "cceDoc3", required = false) MultipartFile cceDoc3,
			@RequestParam(value = "dateOfAdmission", required = false) String dateOfAdmission,
			@RequestParam(value = "remarks", required = false) String remarks,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "alternateNo", required = false) String alternateNo) {
		Map<String, Object> response = new HashMap<>();
		Integer msgOut;
		try {
			msgOut = cceOutBoundService.updateCCeOutBound(userId, cceId, urn, hospitalCode, cceDoc1, cceDoc2, cceDoc3,
					dateOfAdmission, remarks, action, alternateNo);
			if (msgOut == 1) {
				response.put("statusCode", HttpStatus.OK.value());
				response.put("status", "Success");
				response.put("message", "Record Saved Successfully.");
			} else {
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("status", "Failed");
				response.put("message", "Some error happen.");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			response.put("message", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@SuppressWarnings({"rawtypes","unused"})
	@GetMapping(value = "/getDgoCallCenterData")
	@ResponseBody
	public ResponseEntity<?> getDgoCallCenterData(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "formDate", required = false) String formDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "cceId", required = false) Long cceId,
			@RequestParam(value = "pageIn", required = false) Integer pageIn,
			@RequestParam(value = "pageEnd", required = false) Integer pageEnd,
			@RequestParam(value = "queryStatus", required = false) String queryStatus,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> getDgoCallCenterData = new ArrayList<Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceOutBoundService.getDgoCallCenterData(userId, formDate, toDate, action, hospitalCode, cceId, pageIn,
					pageEnd, queryStatus, stateCode, distCode);
			List ListofKeys = map.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				getDgoCallCenterData = entry.getValue();
				json.put("list", getDgoCallCenterData);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = "/updateDgoCallCenterData")
	@ResponseBody
	public ResponseEntity<?> updateDgoCallCenterData(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "cceId") Long cceId, @RequestParam(value = "urn") String urn,
			@RequestParam(value = "hospitalCode") String hospitalCode,
			@RequestParam(value = "dgoDoc", required = false) MultipartFile dgoDoc,
			@RequestParam(value = "dateOfAdmission", required = false) String dateOfAdmission,
			@RequestParam(value = "remarks") String remarks, @RequestParam(value = "action") Integer action) {
		Map<String, Object> response = new HashMap<>();
		Integer msgOut;
		try {
			msgOut = cceOutBoundService.updateDgoCallCenterData(userId, cceId, urn, hospitalCode, dgoDoc,
					dateOfAdmission, remarks, action);
			if (msgOut == 1) {
				response.put("statusCode", HttpStatus.OK.value());
				response.put("status", "Success");
				response.put("message", "Record Saved Successfully.");
			} else if (msgOut == 2) {
				response.put("statusCode", HttpStatus.OK.value());
				response.put("status", "Success");
				response.put("message", "Query Raised Successfully.");
			} else {
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("status", "Failed");
				response.put("message", "Some error happen.");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			response.put("message", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/getSupervisorCallCenterData")
	@ResponseBody
	public ResponseEntity<?> getSupervisorCallCenterData(
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "formDate", required = false) String formDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "cceId", required = false) Long cceId,
			@RequestParam(value = "cceUserId", required = false) Integer cceUserId,
			@RequestParam(value = "pageIn", required = false) Integer pageIn,
			@RequestParam(value = "pageEnd", required = false) Integer pageEnd,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> getSupervisorCallCenterData = new ArrayList<Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceOutBoundService.getSupervisorCallCenterData(userId, formDate, toDate, action, hospitalCode, cceId,
					cceUserId, pageIn, pageEnd, stateCode, distCode);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				getSupervisorCallCenterData = entry.getValue();
				json.put("list", getSupervisorCallCenterData);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

	@GetMapping(value = "/addReassignremark")
	public Response addGoRemark(@RequestParam(value = "id") Long id,
			@RequestParam(value = "remark") String reAssignRemarks,
			@RequestParam(value = "reAssignFlag") Integer reAssignFlag,
			@RequestParam(value = "reAssignUser") Integer reAssignUser) {
		return cceOutBoundService.addReassignRemark(id, reAssignRemarks, reAssignFlag, reAssignUser);
	}

	@GetMapping(value = "/getUserNameByGroupId")
	@ResponseBody
	public List<UserDetails> getUserNameByGroupId() {
		return cceOutBoundService.getUserNameByGroupId();
	}

	@ResponseBody
	@GetMapping(value = "/getDistrictListByStateDC")
	public String getDistrict(@RequestParam("stateCode") String stateCode, @RequestParam("userId") Long userId) {
		String districtList = null;
		try {
			districtList = cceOutBoundService.getDistrictList(stateCode, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtList;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalListByDc")
	public String getHospital(@RequestParam("stateCode") String stateCode, @RequestParam("distCode") String distCode,
			@RequestParam("userId") Long userId) {
		String hospitalList = null;
		try {
			hospitalList = cceOutBoundService.getHospital(stateCode, distCode, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalList;
	}

	@GetMapping(value = "/getITACceOutBound")
	@ResponseBody
	public ResponseEntity<?> getITACceOutBound(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "formDate", required = false) String formDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> getCceOutBoundData = new ArrayList<Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceOutBoundService.getITACceOutBoundData(userId, formDate, toDate, action, hospitalCode, stateCode,
					distCode);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				getCceOutBoundData = entry.getValue();
				json.put("list", getCceOutBoundData);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

	@GetMapping(value = "/getDgoITACallCenterData")
	@ResponseBody
	public ResponseEntity<?> getDgoITACallCenterData(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "formDate", required = false) String formDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> getDgoCallCenterData = new ArrayList<Object>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceOutBoundService.getITADgoCallCenterData(userId, formDate, toDate, action, hospitalCode, stateCode,
					distCode);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				getDgoCallCenterData = entry.getValue();
				json.put("list", getDgoCallCenterData);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

	@GetMapping(value = "/saveReassignData")
	public Response saveReassignData(@RequestParam(value = "id") Long id,
			@RequestParam(value = "remark") String reAssignRemarks,
			@RequestParam(value = "reAssignUser") Integer reAssignUser,
			@RequestParam(value = "cceUserId") Long cceUserId) {
		return cceOutBoundService.saveReassignRemark(id, reAssignRemarks, reAssignUser, cceUserId);
	}
}
