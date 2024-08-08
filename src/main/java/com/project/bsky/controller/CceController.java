package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CceGroupBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.CceService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")

public class CceController {

	@Autowired
	private CceService cceservice;
	
	@Autowired
	private Logger logger;

	@GetMapping(value = "/gettransactionInformation")
	@ResponseBody
	public ResponseEntity<?> gettransactionInformation(@RequestParam(value = "userId", required = false) Long userId,
													   @RequestParam(value = "action", required = false) String action) throws Exception {
		List<Object> getTransactionInformation = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			getTransactionInformation = cceservice.gettransactionInformation(userId, action);
			details.put("status", "success");
			details.put("data", getTransactionInformation);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/savecce")
	public Response saveCce(@RequestBody CceGroupBean cce) {
//		//System.out.println(cce);
		return cceservice.saveCce(cce);
	}

	@GetMapping(value = "/getallCceNotConnectedStatus")
	@ResponseBody
	public ResponseEntity<?> getallCceNotConnectedStatus(@RequestParam(value = "userId", required = false) Long userId,
														 @RequestParam(value = "action", required = false) String action) throws Exception {
		List<Object> getNotConnected = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			getNotConnected = cceservice.getNotConnected(userId, action);
			details.put("status", "success");
			details.put("data", getNotConnected);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/savenotconnectedcce")
	public Response saveNotConnectedCce(@RequestBody CceGroupBean cceNot) {
		//System.out.println(cceNot);
		return cceservice.saveNotConnectedCce(cceNot);
	}

	@GetMapping(value = "/getallcce")
	@ResponseBody
	public ResponseEntity<?> getallCce(@RequestParam(value = "userId", required = false) Long userId,
									   @RequestParam(value = "action", required = false) String action) throws Exception {
		List<Object> getAllCce = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			getAllCce = cceservice.getAllCce(userId, action);
			details.put("status", "success");
			details.put("data", getAllCce);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/savereassignedcce")
	public Response saveReAssignedCce(@RequestBody CceGroupBean cceNot) {
		//System.out.println(cceNot);
		return cceservice.saveReAssignedCce(cceNot);
	}

	@GetMapping(value = "/getAllCcedata")
	@ResponseBody
	public ResponseEntity<?> getAllCceData(@RequestParam(value = "formDate", required = false) String formDate,
										   @RequestParam(value = "toDate", required = false) String toDate,
										   @RequestParam(value = "stateCode", required = false) String stateCode,
										   @RequestParam(value = "distCode", required = false) String distCode,
										   @RequestParam(value = "hospitalCode", required = false) String hospitalCode,
										   @RequestParam(value = "actionBy", required = false) String actionBy,
										   @RequestParam(value = "pendingAt", required = false) String pendingAt,
										   @RequestParam(value = "action", required = false) String action,
										   @RequestParam(value = "status", required = false) String status,
										   @RequestParam(value = "pageIn", required = false) Integer pageIn,
										   @RequestParam(value = "pageEnd", required = false) Integer pageEnd) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceservice.getallCceData(formDate, toDate, stateCode, distCode, hospitalCode, actionBy, pendingAt, action, status, pageIn, pageEnd);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				json.put("list", entry.getValue());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

	@GetMapping(value = "/getGoActionCceData")
	@ResponseBody
	public ResponseEntity<?> getAllCceDataView(@RequestParam(value = "formDate", required = false) String formDate,
										   @RequestParam(value = "toDate", required = false) String toDate,
										   @RequestParam(value = "stateCode", required = false) String stateCode,
										   @RequestParam(value = "distCode", required = false) String distCode,
										   @RequestParam(value = "hospitalCode", required = false) String hospitalCode,
										   @RequestParam(value = "actionBy", required = false) String actionBy,
										   @RequestParam(value = "pendingAt", required = false) String pendingAt,
										   @RequestParam(value = "action", required = false) String action,
										   @RequestParam(value = "status", required = false) String status,
										   @RequestParam(value = "pageIn", required = false) Integer pageIn,
										   @RequestParam(value = "pageEnd", required = false) Integer pageEnd) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceservice.getallCceDataView(formDate, toDate, stateCode, distCode, hospitalCode, actionBy, pendingAt, action, status, pageIn, pageEnd);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				json.put("list", entry.getValue());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

	@GetMapping(value = "/addgoremark")
	public Response addGoRemark(@RequestParam(value = "id") Long id,
								@RequestParam(value = "remark") String goRemarks,
								@RequestParam(value = "action") Integer action,
								@RequestParam(value = "userId") Long goUserId) {
		Response response=null;
		response=cceservice.addGoRemark(id, goRemarks, action, goUserId);
		return response;
	}
	
	@GetMapping(value = "/getCceResettlementdata")
	@ResponseBody
	public ResponseEntity<?> getCceResettlementdata(@RequestParam(value = "formDate", required = false) String formDate,
										   @RequestParam(value = "toDate", required = false) String toDate,
										   @RequestParam(value = "stateCode", required = false) String stateCode,
										   @RequestParam(value = "distCode", required = false) String distCode,
										   @RequestParam(value = "hospitalCode", required = false) String hospitalCode,
										   @RequestParam(value = "action", required = false) String action) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceservice.getCceReSettlement(formDate, toDate, stateCode, distCode, hospitalCode,action);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				json.put("list", entry.getValue());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}
	@GetMapping(value = "/getGOITAdata")
	@ResponseBody
	public ResponseEntity<?> getGOITAdata(@RequestParam(value = "userId", required = false) Long userId,
			                               @RequestParam(value = "formDate", required = false) String formDate,
										   @RequestParam(value = "toDate", required = false) String toDate,
										   @RequestParam(value = "stateCode", required = false) String stateCode,
										   @RequestParam(value = "distCode", required = false) String distCode,
										   @RequestParam(value = "hospitalCode", required = false) String hospitalCode,
										   @RequestParam(value = "action", required = false) String action) {
		Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			map = cceservice.getGOInitialTakeActionData(userId,formDate, toDate, stateCode, distCode, hospitalCode,action);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				json.put("list", entry.getValue());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}
	
	@GetMapping(value = "/getCceDataForShasCEO")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getCceDataForShasCEO(@RequestParam(value = "formDate", required = false) String formDate,
										   @RequestParam(value = "toDate", required = false) String toDate,
										   @RequestParam(value = "stateCode", required = false) String stateCode,
										   @RequestParam(value = "distCode", required = false) String distCode,
										   @RequestParam(value = "hospitalCode", required = false) String hospitalCode,
										   @RequestParam(value = "pageIn", required = false) Integer pageIn,
										   @RequestParam(value = "pageEnd", required = false) Integer pageEnd) {
		Map<Long, List<Object>> map = new HashMap<>();
		Map<String, Object> json = new HashMap<>();
		try {
			map = cceservice.getallCceDataForSHASCEO(formDate, toDate, stateCode, distCode, hospitalCode, pageIn, pageEnd);
			for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				json.put("list", entry.getValue());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}

}
