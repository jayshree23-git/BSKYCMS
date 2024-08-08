package com.project.bsky.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.bsky.bean.CDMOForwardBean;
import com.project.bsky.bean.FeedbackCallingReport;
import com.project.bsky.bean.LoggedUserDetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.config.dto.NotingDto;
import com.project.bsky.config.dto.NotingParameter;
import com.project.bsky.service.WorkflowService;

@RestController
@CrossOrigin(origins = "*")
@EnableTransactionManagement
public class WorkFlowController {

	@Autowired
	private Logger logger;

	@Autowired
	WorkflowService workflowService;

	@Value("${server.port}")
	private String serverport;

	@Value("${pdf.docPathWork}")
	private String docPathWork;

	@GetMapping(value = "/getallOfficersApi", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getallOfficersApi() {

		List<Map<String, Object>> getallOfficers = null;
		Map<String, Object> response = new HashMap<String, Object>();

		try {

			getallOfficers = workflowService.getallOfficersApi();
			response.put("status", 200);
			response.put("msg", "success");
			response.put("result", getallOfficers);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}

	@GetMapping(value = "/getallApprovalAction", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getallApprovalAction() {

		List<Map<String, Object>> getallOfficers = null;
		Map<String, Object> response = new HashMap<String, Object>();

		try {

			getallOfficers = workflowService.getallApprovalAction();
			response.put("status", 200);
			response.put("msg", "success");
			response.put("result", getallOfficers);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}

	@PostMapping(value = "/fillWorkflow", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> fillWorkflow(@RequestParam("arrParam[serviceId]") String request,
			@RequestParam("arrParam[labelId]") String request1) {

		JSONObject getallOfficers = null;
		Map<String, Object> response = new HashMap<String, Object>();

		try {

			getallOfficers = workflowService.fillWorkflow(request, request1);
			response.put("errorFlag", 0);
			response.put("msg", "success");
			response.put("result", getallOfficers.getString("result"));

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}

	@PostMapping("/setWorkflow")
	public ResponseEntity<?> setWorkflow(@RequestBody String setWorkflow) throws Exception {

		String result = workflowService.setWorkflow(setWorkflow);
		JSONObject object = new JSONObject();
		if (result.equals("success")) {
			object.put("errorFlag", 0);
			object.put("msg", "success");
		} else {
			object.put("msg", "error");
			object.put("status", 400);
		}
		return ResponseEntity.ok(object.toString());

	}

	@GetMapping("/getStateId")
	public String getStateId(@RequestParam(value = "serviceId") Long serviceId) {

		String result = "";
		try {
			result = workflowService.getStateId(serviceId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@ResponseBody
	@PostMapping(value = "/getApplicationList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getApplicationList(@RequestBody String request) {

		JSONObject getApplicationList = null;
		JSONObject responseObj = new JSONObject();

		try {

			getApplicationList = workflowService.getApplicationList(request);

			if (getApplicationList.length() > 0) {

				responseObj.put("status", "200");
				responseObj.put("msg", "success");
				responseObj.put("result", getApplicationList);

			} else {

				responseObj.put("status", "400");
				responseObj.put("msg", "error");
				responseObj.put("result", "");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(responseObj.toString());

	}

	@ResponseBody
	@PostMapping(value = "/getApplication", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getApplication(@RequestBody String request) {

		JSONObject getApplication1 = null;
		JSONObject responseObj = new JSONObject();

		try {

			getApplication1 = workflowService.getApplication(request);

			if (getApplication1.length() > 0) {

				responseObj.put("status", "200");
				responseObj.put("msg", "success");
				responseObj.put("result", getApplication1);

			} else {

				responseObj.put("status", "400");
				responseObj.put("msg", "error");
				responseObj.put("result", "");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(responseObj.toString());

	}

	@ResponseBody
	@PostMapping(value = "/getGrievanceReport", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getGrievanceReport(@RequestBody String request) {

		JSONObject getApplication1 = null;
		JSONObject responseObj = new JSONObject();

		try {

			getApplication1 = workflowService.getGrievanceReport(request);

			if (getApplication1.length() > 0) {

				responseObj.put("status", "200");
				responseObj.put("msg", "success");
				responseObj.put("result", getApplication1);

			} else {

				responseObj.put("status", "400");
				responseObj.put("msg", "error");
				responseObj.put("result", "");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(responseObj.toString());

	}

	@PostMapping(value = "/getAuthAction", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAuthAction(@RequestBody String request) {

		JSONObject getAuthAction = null;
		JSONObject responseObj = new JSONObject();

		try {

			getAuthAction = workflowService.getAuthAction(request);
			responseObj.put("status", 200);
			responseObj.put("result", getAuthAction);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(responseObj.toString());

	}

	@PostMapping(value = "/takeAction", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> takeAction(@RequestParam("arrParam[processId]") String processId,
			@RequestParam("arrParam[stageNo]") String stageNo, @RequestParam("arrParam[serviceId]") String serviceId,
			@RequestParam("arrParam[action]") String action, @RequestParam("arrParam[remark]") String remark,
			@RequestParam("arrParam[updatedBy]") String updatedBy,
			@RequestParam("arrParam[updatedByRoleId]") String updatedByRoleId,
			@RequestParam("arrParam[docIds]") String docIds,
			@RequestParam(value = "arrParam[contactNumber]", required = false) String contactNumber,
			@RequestParam(value = "arrParam[priority]", required = false) String priority,
			@RequestParam(value = "arrParam[doc_0]", required = false) MultipartFile multipart,
			@RequestParam(value = "arrParam[doc_1]", required = false) MultipartFile multipartForAudioVideo

	) throws JsonMappingException, JsonProcessingException, Exception {
		JSONObject object = new JSONObject();
		if (multipart != null) {
			File file = new File(docPathWork + multipart.getOriginalFilename());
			BufferedOutputStream bf = null;
			try {
				byte[] bytes = multipart.getBytes();
				bf = new BufferedOutputStream(new FileOutputStream(file));
				bf.write(bytes);
				bf.close();
			} catch (IOException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			if (multipartForAudioVideo != null) {
				File file1 = new File(docPathWork + multipartForAudioVideo.getOriginalFilename());
				BufferedOutputStream bf1 = null;
				try {
					byte[] bytes = multipartForAudioVideo.getBytes();
					bf1 = new BufferedOutputStream(new FileOutputStream(file1));
					bf1.write(bytes);
					bf1.close();
				} catch (IOException e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
			JSONObject requestObj = new JSONObject();
			requestObj.put("processId", processId);
			requestObj.put("serviceId", serviceId);
			requestObj.put("stageNo", stageNo);
			requestObj.put("action", action);
			requestObj.put("remark", remark);
			requestObj.put("updatedBy", updatedBy);
			requestObj.put("updatedByRoleId", updatedByRoleId);
			requestObj.put("multipart", multipart.getOriginalFilename());
			if (multipartForAudioVideo != null) {
				requestObj.put("multipart2", multipartForAudioVideo.getOriginalFilename());
			}
			requestObj.put("contactNumber", contactNumber);
			requestObj.put("priority", priority);
			String result = workflowService.takeAction(requestObj);

			if (result.equals("success")) {
				object.put("errorFlag", 0);
				object.put("msg", "success");
			} else {
				object.put("msg", "error");
				object.put("status", 400);
			}

		} else {
			if (multipartForAudioVideo != null) {
				File file1 = new File(docPathWork + multipartForAudioVideo.getOriginalFilename());
				BufferedOutputStream bf1 = null;
				try {
					byte[] bytes = multipartForAudioVideo.getBytes();
					bf1 = new BufferedOutputStream(new FileOutputStream(file1));
					bf1.write(bytes);
					bf1.close();
				} catch (IOException e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
			JSONObject requestObj = new JSONObject();
			requestObj.put("processId", processId);
			requestObj.put("serviceId", serviceId);
			requestObj.put("stageNo", stageNo);
			requestObj.put("action", action);
			requestObj.put("remark", remark);
			requestObj.put("updatedBy", updatedBy);
			requestObj.put("updatedByRoleId", updatedByRoleId);
			requestObj.put("contactNumber", contactNumber);
			requestObj.put("priority", priority);
			if (multipartForAudioVideo != null) {
				requestObj.put("multipart2", multipartForAudioVideo.getOriginalFilename());
			}
			String result = workflowService.takeAction(requestObj);
			if (result.equals("success")) {
				object.put("errorFlag", 0);
				object.put("msg", "success");
			} else {
				object.put("msg", "error");
				object.put("status", 400);
			}

		}

		return ResponseEntity.ok(object.toString());

	}

	@PostMapping(value = "/getnoting", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getnoting(@RequestBody NotingParameter notingParameter) {
		List<NotingDto> getNotingEntityList = null;
		if (notingParameter.getProcessId() != null && notingParameter.getServiceId() != null) {
			try {
				getNotingEntityList = workflowService.getNotingIds(notingParameter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			notingParameter.setStatus("Required processId and serviceId");
			return ResponseEntity.ok(notingParameter);
		}

		return ResponseEntity.ok(getNotingEntityList);
	}

	@GetMapping("/getDistByUserName")
	public String getDistByUserName(@RequestParam(value = "username") String username) {

		String result = "";
		try {
			result = workflowService.getDistByUserName(username);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@PostMapping(value = "/getUserDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserDetails(@RequestBody LoggedUserDetailsBean bean) {
		JSONObject responseObj = new JSONObject();

		try {
			LoggedUserDetailsBean resp = workflowService.getUserDetails(bean);
			responseObj.put("status", 200);
			responseObj.put("user", resp);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(responseObj.toString());

	}

	@PostMapping(value = "/CDMOActionofForwardGrv")
	public ResponseEntity<Response> CDMOActionofForwardGrv(@RequestBody CDMOForwardBean logBean) {
		Response response = null;
		try {
			response = workflowService.saveCDMOForwardDetails(logBean);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in CDMOActionofForwardGrv Method of Workflow Controller : " + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getCDMOActionDetails")
	@ResponseBody
	public Map<String, String> CDMOActionDetails(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			if (fromDate != "" && fromDate != null) {
				fromDate = fromDate.trim();
			} else if (toDate != "" && toDate != null) {
				toDate = toDate.trim();
			}
			snoClaim = workflowService.getCDMOActionTakenDetails(userId, fromDate, toDate);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in CDMOActionDetails Method of WorkflowController : " + e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@PostMapping(value = "/grvCCEFeedbackReport")
	public ResponseEntity<Map<String, String>> grvCCEFeedbackReport(@RequestBody FeedbackCallingReport logBean) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = workflowService.getCCEFeedbackReportDetails(logBean);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in grvCCEFeedbackReport Method of Workflow Controller : " + e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}
}
