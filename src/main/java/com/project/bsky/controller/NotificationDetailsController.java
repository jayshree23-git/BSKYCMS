package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.model.NotificationDetails;
import com.project.bsky.service.NotificationDetailsService;
import com.project.bsky.util.ClassHelperUtils;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class NotificationDetailsController {

	@Autowired
	private NotificationDetailsService notification;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/savenotification")
	public Response addGroup(NotificationDetails notificationdetails,
			@RequestParam(required = false, value = "file2") MultipartFile form) {
		Response response = null;
		response = notification.save(notificationdetails, form);
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/getnotification")
	public List<NotificationDetails> getnotification() {
		List<NotificationDetails> list = notification.findall();
		return list;
	}

	@ResponseBody
	@PostMapping(value = "/updatenotification")
	public Response updatenotification(NotificationDetails notificationdetails,
			@RequestParam(required = false, value = "file2") MultipartFile form) {
		Response response = null;
		response = notification.update(notificationdetails, form);
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/getdataforNotificationDetails")
	public List<NotificationDetails> getdataforNotificationDetails(
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate) {
		return notification.getdataforNotificationDetails(fromdate, todate);
	}

	@ResponseBody
	@GetMapping(value = "/getNotificationDetailsOnSearch")
	public List<NotificationDetails> getdataforNotificationDetails(
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "groupId", required = false) String groupId) {
		return notification.getdataforNotificationDetailsOns(fromdate, todate, groupId);
	}

	@PostMapping(value = "/getnotificationshow")
	public ResponseEntity<Map<String, Object>> getnotificationshow(@RequestBody Map<String, String> mapRequest) {
		JSONObject json = new JSONObject();
		Map<String, Object> map=new HashedMap<>();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			map.put("status", "success");
			map.put("data", notification.getnotificationshow(json.has("groupid") ? json.getInt("groupid") : 0));
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			logger.error("Error occures in getnotificationshow() method of NotificationDetailsController Class.");
			map.put("status", "fail");
			map.put("error", e.getMessage());
			return ResponseEntity.ok(map);
		}
	}

	@ResponseBody
	@GetMapping(value = "/downLoadnotificatonDoc")
	public String commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		System.out.println(enCodedJsonString);
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		System.out.println("name: " + fileName);
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "Passbook not found";
			} else {
				String year = fileName.substring(7, 11);
				String month = fileName.substring(12, 15);
				notification.downLoadPassbook(fileName, year, response, month);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	/*
	 * @Auther : Sambit Kumar Pradhan
	 * 
	 * @Date : 24-08-2023
	 * 
	 * @Purpose : SNA Dashboard Pop up for Pending claims not verrified by sna since
	 * last 7 days
	 */
	@GetMapping(value = "/getHospitalListClaimsNotVerified")
	public ResponseEntity<?> getHospitalListClaimsNotVerified(@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "actionCode") int actionCode, @RequestParam(value = "days") Integer days) {
		Map<String, Object> response;
		try {
			List<Map<String, Object>> responseList = notification.getHospitalListClaimsNotVerified(userId, actionCode,
					days);
			if (responseList != null && !responseList.isEmpty())
				response = ClassHelperUtils.createSuccessResponse(responseList,
						"Hospital List Whose Claims Not Verified Fetched Successfully.");
			else
				response = ClassHelperUtils
						.createNoContentResponse("No Hospital List Found Whose Claims Not Verified!");
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.badRequest().body(ClassHelperUtils.createErrorResponse(e.getMessage()));
		}
	}

	/*
	 * @Auther : Sambit Kumar Pradhan
	 * 
	 * @Date : 24-08-2023
	 * 
	 * @Purpose : SNA Dashboard Pending claims List not verrified by sna since last
	 * 7 days
	 */
	@GetMapping(value = "/getPendingHospitalClaims")
	public ResponseEntity<?> getPendingHospitalClaims(@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "hospitalCode") String hospitalCode,
			@RequestParam(value = "actionCode") int actionCode, @RequestParam(value = "days") Integer days) {
		Map<String, Object> response;
		try {
			List<Map<String, Object>> responseList = notification.getHospitalClaimsNotVerified(userId, hospitalCode,
					actionCode, days);
			if (responseList != null && !responseList.isEmpty())
				response = ClassHelperUtils.createSuccessResponse(responseList,
						"Hospital Claims Not Verified Fetched Successfully.");
			else
				response = ClassHelperUtils.createNoContentResponse("No Hospital Claims Not Found!");
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.badRequest().body(ClassHelperUtils.createErrorResponse(e.getMessage()));
		}
	}
}
