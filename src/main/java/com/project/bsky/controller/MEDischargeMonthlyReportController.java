/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.MEDischargeReportBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.MEDischargeMonthlyReportService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class MEDischargeMonthlyReportController {

	@Autowired
	private MEDischargeMonthlyReportService medischargeMonthlyReportService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getMonthWiseDischargeMeDetail")
	public MEDischargeReportBean MEDischargeMonthlyData(
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode,
			@RequestParam(value = "serchtype", required = false) Integer serchtype) {
		MEDischargeReportBean bean = new MEDischargeReportBean();
		try {
			bean = medischargeMonthlyReportService.MEDischargeMonthly(userId, fromdate, todate, stateId, districtId,
					hospitalCode, serchtype);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return bean;

	}

	@ResponseBody
	@GetMapping(value = "/getMonthWiseDischargeDetaiMe")
	public String getMonthWiseDischargeDetaiMe(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode,
			@RequestParam(value = "serchtype", required = false) Integer serchtype,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "packageName", required = false) String packageName) {
		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = medischargeMonthlyReportService.getMonthWiseDischargeDetaiMe(userId, fromdate, todate,
					stateId, districtId, hospitalCode, serchtype, Package, packageName);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;
	}

	@GetMapping(value = "/getAdmissionBlockedDetails")
	@ResponseBody
	public String getAdmissionBlockedDetails(@RequestParam(value = "txnid", required = false) String txnid,
			@RequestParam(value = "pkgid", required = false) String pkgid,
			@RequestParam(value = "userId", required = false) String userid) {
		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = medischargeMonthlyReportService.getAdmissionBlockedDetails(txnid, pkgid, userid);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;
	}

	@PostMapping("/saveDischargeReports")
	@ResponseBody
	public Response saveDischargeReport(@RequestParam(required = false, value = "pdf") MultipartFile pdf) {
		Response response = null;
		System.out.println(pdf);
		try {
			response = medischargeMonthlyReportService.saveDischargeReport(pdf);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@GetMapping("/downloadDischargeRpts")
	public String downloadDischargeRpts(HttpServletResponse response, @RequestParam("file") String enCodedJsonString)
			throws JSONException {
		System.out.println(enCodedJsonString + "comes");
		String resp = "";
		try {
			byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
			String jsonString = new String(bytes, StandardCharsets.UTF_8);
			JSONObject json = new JSONObject(jsonString);
			String fileCode = json.getString("f");
			medischargeMonthlyReportService.downloadDischargeRpts(fileCode, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

}
