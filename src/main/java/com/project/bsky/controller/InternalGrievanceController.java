/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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

import com.project.bsky.bean.IntGrvUserBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.InternalGrievance;
import com.project.bsky.service.InternalGrievanceService;

/**
 * @author priyanka.singh
 *
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class InternalGrievanceController {

	@Autowired
	private InternalGrievanceService internalGrievanceService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveGrievanceInternalData")
	public Response addInternalGrievance(InternalGrievance internalGrievance,
			@RequestParam(required = false, value = "documentName1") MultipartFile form) {
		Response response = null;
		try {
			response = internalGrievanceService.saveinternalGrievanceDetails(internalGrievance, form);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/getgrvuserdatabytypeid")
	public List<IntGrvUserBean> getgrvuserdatabytypeid(
			@RequestParam(required = false, value = "typeId") Integer typeid) {
		List<IntGrvUserBean> list = new ArrayList<IntGrvUserBean>();
		try {
			list = internalGrievanceService.getgrvuserdatabytypeid(typeid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getAllGrievanceInternal")
	public List<InternalGrievance> getGrievanceData() {
		List<InternalGrievance> getDetails = null;
		try {
			getDetails = internalGrievanceService.getGrievanceDetails();
			return getDetails;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getDetails;
	}

	@GetMapping(value = "/getAllFilterDataGrievnceFilter")
	@ResponseBody
	public List<Object> getDataGrievnceFilter(
			@RequestParam(value = "categoryType", required = false) String categoryType,
			@RequestParam(value = "priority", required = false) String priority,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "statusFlag", required = false) String status) {
		List<Object> getDataGrievnceFilter = null;
		try {
			getDataGrievnceFilter = internalGrievanceService.getGrievnceFilterDetails(categoryType, priority, status,
					fromDate, toDate);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getDataGrievnceFilter;
	}

	@ResponseBody
	@GetMapping(value = "/downLoadinternalgrivanceDoc")
	public String commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "Passbook not found";
			} else {
				String year = fileName.substring(7, 11);
				String month = fileName.substring(12, 15);
				internalGrievanceService.downLoadPassbook(fileName, year, response, month);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	@ResponseBody
	@GetMapping(value = "/getInternalGrievanceDataById")
	public InternalGrievance getById(@RequestParam(value = "grievanceId", required = false) Long grievanceId) {
		InternalGrievance internalGrievance = internalGrievanceService.getInternalGrievanceById(grievanceId);
		return internalGrievance;
	}

	@ResponseBody
	@GetMapping(value = "/updateGrievanceDetailsById")
	public Response updateGrievanceDetails(@RequestParam(required = false, value = "assinedTo") String assign,
			@RequestParam(required = false, value = "closingDate") String closedate,
			@RequestParam(required = false, value = "statusFlag") Integer status,
			@RequestParam(required = false, value = "updatedBy") Long updatedBy,
			@RequestParam(required = false, value = "id") Long id,
			@RequestParam(required = false, value = "closingDescription") String closingDescription) {
		Response response = null;
		try {
			response = internalGrievanceService.update(assign, closedate, status, id, updatedBy, closingDescription);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
}
