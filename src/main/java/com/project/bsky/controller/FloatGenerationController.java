package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.FloatExcelBean;
import com.project.bsky.bean.FloatReportBean;
import com.project.bsky.bean.OldFloatBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.oldblocknewdischargebean;
import com.project.bsky.service.FloatGenerationService;
import com.project.bsky.service.OldClaimFloatService;

/**
 * @author ronauk.maharana
 *
 */
@RestController
@RequestMapping(value = "/api")
public class FloatGenerationController {

	@Autowired
	private FloatGenerationService floatService;

	@Autowired
	private OldClaimFloatService oldFloatService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/getFloatReport")
	public List<Object> getFloatReport(@RequestBody FloatReportBean requestBean) {
		List<Object> floatList = new ArrayList<Object>();
		try {
			floatList = floatService.getFloatReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;
	}

	@ResponseBody
	@PostMapping(value = "/getSummary")
	public String getSummary(@RequestBody FloatReportBean requestBean) {
		String summary = null;
		try {
			summary = floatService.getSummary(requestBean).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return summary;
	}

	@PostMapping("/downloadFloat")
	public ResponseEntity<Response> downloadFloat(@RequestBody FloatExcelBean bean) {
		Response response = null;
		try {
			response = floatService.generateExcel(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response = new Response();
			response.setStatus("failed");
			response.setMessage(e.toString());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/downloadFile")
	public String downloadFile(HttpServletResponse response, @RequestParam("file") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		try {
			byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
			String jsonString = new String(bytes, StandardCharsets.UTF_8);
			JSONObject json = new JSONObject(jsonString);
			String file = json.getString("f");
			String userId = json.getString("u");
			floatService.downLoadFile(file, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	@PostMapping("/saveFloatReport")
	public ResponseEntity<Response> saveFloatReport(@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "pdf") MultipartFile pdf,
			@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId,
			@RequestParam(required = false, value = "mortality") String mortality,
			@RequestParam(required = false, value = "createdBy") String createdBy,
			@RequestParam(required = false, value = "searchtype") String searchtype,
			@RequestParam(required = false, value = "schemecategoryid") String schemecategoryid) {
		Response response = new Response();
		FloatExcelBean bean = new FloatExcelBean();
		try {
			bean.setUserId(Long.parseLong(userId));
			bean.setFromDate(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate));
			bean.setToDate(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate));
			bean.setStateId(stateId);
			bean.setDistrictId(districtId);
			bean.setHospitalId(hospitalId);
			bean.setMortality(mortality);
			bean.setCreatedBy(Long.parseLong(createdBy));
			bean.setSearchtype(Long.parseLong(searchtype));
			bean.setSchemecategoryid(schemecategoryid);
			Integer resp = floatService.saveFloatReport(pdf, bean);
			if (resp == 1) {
				response.setStatus("success");
			} else {
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/saveActionWiseFloatGeneration")
	public ResponseEntity<Response> saveActionWiseFloatGeneration(
			@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "pdf") MultipartFile pdf,
			@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId,
			@RequestParam(required = false, value = "mortality") String mortality,
			@RequestParam(required = false, value = "createdBy") String createdBy,
			@RequestParam(required = false, value = "searchtype") String searchtype,
			@RequestParam(required = false, value = "schemecategoryid") String schemecategoryid) {
		Response response = new Response();
		FloatExcelBean bean = new FloatExcelBean();
		try {
			bean.setUserId(Long.parseLong(userId));
			bean.setFromDate(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate));
			bean.setToDate(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate));
			bean.setStateId(stateId);
			bean.setDistrictId(districtId);
			bean.setHospitalId(hospitalId);
			bean.setMortality(mortality);
			bean.setCreatedBy(Long.parseLong(createdBy));
			bean.setActionName(searchtype);
			bean.setSchemecategoryid(schemecategoryid);
			Integer resp = floatService.saveFloatReport(pdf, bean);
			if (resp == 1) {
				response.setStatus("success");
			} else {
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping(value = "/getGeneratedReports")
	public String getGeneratedReports(@RequestBody FloatReportBean requestBean) {
		String floatList = null;
		try {
			floatList = floatService.getGeneratedReports(requestBean).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;
	}

	@ResponseBody
	@PostMapping(value = "/getAbstractFloatReport")
	public String getAbstractFloatReport(@RequestBody FloatReportBean requestBean) {
		String floatList = null;
		try {
			floatList = floatService.getAbstractFloatReport(requestBean).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;
	}

	@ResponseBody
	@PostMapping(value = "/getActionWiseFloatReport")
	public List<Object> getActionWiseFloatReport(@RequestBody FloatReportBean requestBean) {
		List<Object> floatList = new ArrayList<Object>();
		try {
			floatList = floatService.getActionWiseFloatReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;
	}

	@ResponseBody
	@PostMapping(value = "/getOldFloatReport")
	public ResponseEntity<?> getOldFloatReport(@RequestBody OldFloatBean requestBean) {
		Map<String, Object> floatMap = new HashMap<String, Object>();
		try {
			List<Object> floatList = oldFloatService.getFloatReport(requestBean);
			floatMap.put("status", "success");
			floatMap.put("list", floatList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			floatMap.put("status", "failed");
			floatMap.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(floatMap);
	}

	@GetMapping("/downloadOldFile")
	public String downloadOldFile(HttpServletResponse response, @RequestParam("file") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		try {
			byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
			String jsonString = new String(bytes, StandardCharsets.UTF_8);
			JSONObject json = new JSONObject(jsonString);
			String file = json.getString("f");
			String userId = json.getString("u");
			oldFloatService.downLoadFile(file, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	@PostMapping("/saveOldFloatReport")
	public ResponseEntity<Response> saveOldFloatReport(@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "pdf") MultipartFile pdf,
			@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId,
			@RequestParam(required = false, value = "createdBy") String createdBy) {
		Response response = new Response();
		FloatExcelBean bean = new FloatExcelBean();
		try {
			bean.setUserId(Long.parseLong(userId));
			bean.setFromDate(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate));
			bean.setToDate(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate));
			bean.setStateId(stateId);
			bean.setDistrictId(districtId);
			bean.setHospitalId(hospitalId);
			bean.setCreatedBy(Long.parseLong(createdBy));
			Integer resp = oldFloatService.saveFloatReport(pdf, bean);
			if (resp == 1) {
				response.setStatus("success");
			} else {
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping(value = "/getOldGeneratedReports")
	public String getOldGeneratedReports(@RequestBody OldFloatBean requestBean) {
		String floatList = null;
		try {
			floatList = oldFloatService.getGeneratedReports(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;
	}

	@ResponseBody
	@PostMapping(value = "/getoldblocknewdischargelist")
	public Map<String, List<Object>> getoldblocknewdischargelist(@RequestBody oldblocknewdischargebean requestBean) {
		Map<String, List<Object>> resultMap = new HashMap<>();
		try {
			resultMap = floatService.getoldblocknewdischargelist(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resultMap;
	}

}
