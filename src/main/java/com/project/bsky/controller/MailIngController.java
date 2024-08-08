package com.project.bsky.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.MailingService;
import com.project.bsky.util.CommonClassHelper;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 03/07/2023 - 2:44 PM
 */

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MailIngController {

	@Autowired
	private Logger logger;

	@Autowired
	private MailingService mailingService;

	@GetMapping(value = "/getMailServiceList")
	public ResponseEntity<?> getMailServiceList() {

		Map<String, Object> response;
		try {
			List<Map<String, Object>> mailServiceList = mailingService.getMailServiceList();
			if (!mailServiceList.isEmpty())
				response = CommonClassHelper.createSuccessResponse(mailServiceList,
						"Mail Service List Fetched Successfully");
			else
				response = CommonClassHelper.createNoContentResponse("Mail Service List Not Found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ResponseEntity<>(CommonClassHelper.createErrorResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/saveMailServiceData")
	public ResponseEntity<?> saveMailServiceData(@RequestBody Map<String, Object> request) {
		Map<String, Object> response;
		try {
			int savedId = mailingService.saveMailServiceData(request);
			if (savedId > 0)
				response = CommonClassHelper.createSuccessResponse(null,
						request.containsKey("id") ? "Mail Service Data Updated Successfully"
								: "Mail Service Data Saved Successfully");
			else
				response = CommonClassHelper.createNoContentResponse("Mail Service Data Not Saved");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ResponseEntity<>(CommonClassHelper.createErrorResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getMailServiceDataById")
	public ResponseEntity<?> getMailServiceDataById(@RequestBody Map<String, Object> request) {
		Map<String, Object> response;
		try {
			Map<String, Object> data = mailingService.getMailServiceDataById((int) request.get("id"));
			if (!data.isEmpty())
				response = CommonClassHelper.createSuccessResponse(data, "Mail Service Data Fetched Successfully");
			else
				response = CommonClassHelper.createNoContentResponse("Mail Service Data Not Found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ResponseEntity<>(CommonClassHelper.createErrorResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/getMailServiceNameList")
	public ResponseEntity<?> getMailServiceNameList() {
		Map<String, Object> response;
		try {
			List<Map<String, Object>> mailServiceList = mailingService.getMailServiceNameList();
			if (!mailServiceList.isEmpty())
				response = CommonClassHelper.createSuccessResponse(mailServiceList,
						"Mail Service List Fetched Successfully");
			else
				response = CommonClassHelper.createNoContentResponse("Mail Service List Not Found");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ResponseEntity<>(CommonClassHelper.createErrorResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/saveMailServiceConfigData")
	public ResponseEntity<?> saveMailServiceConfigData(@RequestBody Map<String, Object> request) {
		Map<String, Object> response;
		try {
			int mailServiceId = mailingService.saveMailServiceConfigData(request);
			if (mailServiceId > 0)
				response = CommonClassHelper.createSuccessResponse(mailServiceId,
						request.containsKey("id") ? "Mail Service Configuration Data Updated Successfully"
								: "Mail Service Configuration Data Saved Successfully");
			else
				response = CommonClassHelper.createNoContentResponse("Mail Service Config Data Not Saved");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ResponseEntity<>(CommonClassHelper.createErrorResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/getMailServiceConfigList")
	public ResponseEntity<?> getMailServiceConfigList() {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			List<Map<String, Object>> mailReportList = mailingService.getMailServiceConfigList();
			if (mailReportList.size() > 0)
				response = CommonClassHelper.createSuccessResponse(mailReportList,
						"Mail Service Configuration List Fetched Successfully");
			else
				response = CommonClassHelper.createNoContentResponse("Mail Service Configuration List Not Found");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ResponseEntity<>(CommonClassHelper.createErrorResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getMailServiceConfigDataById")
	public ResponseEntity<?> getMailServiceConfigDataById(@RequestBody Map<String, Object> request) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			Map<String, Object> data = mailingService.getMailServiceConfigDataById((int) request.get("id"));
			if (!data.isEmpty())
				response = CommonClassHelper.createSuccessResponse(data,
						"Mail Service Configuration Data Fetched Successfully");
			else
				response = CommonClassHelper.createNoContentResponse("Mail Service Configuration Data Not Found");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ResponseEntity<>(CommonClassHelper.createErrorResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}