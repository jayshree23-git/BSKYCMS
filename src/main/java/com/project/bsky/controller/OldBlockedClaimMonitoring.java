package com.project.bsky.controller;

import com.project.bsky.service.OldBlockedClaimMonitoringService;
import com.project.bsky.service.PreAuthService;
import com.project.bsky.util.CommonClassHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 11/07/2023 - 4:07 PM
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OldBlockedClaimMonitoring {

    @Autowired
    private Logger logger;

    @Autowired
    private PreAuthService preAuthService;

    @Autowired
    private OldBlockedClaimMonitoringService oldBlockedClaimMonitoringService;

    @PostMapping(value = "/getOldBlockedClaimList")
    public ResponseEntity<?> getOldBlockedClaimList(@RequestBody Map<String, Object> request) {
        //logger.info("Inside OldBlockedClaimMonitoring getOldBlockedClaimList method");
        Map<String, Object> response;
        try {
            List<Map<String, Object>> oldBlockedClaimList = (List<Map<String, Object>>) oldBlockedClaimMonitoringService.getOldBlockedClaimList(request);
            if (oldBlockedClaimList != null && oldBlockedClaimList.size() > 0)
                response = CommonClassHelper.createSuccessResponse(
                        oldBlockedClaimList,
                        "Old Blocked Claim List Fetched Successfully"
                );
            else
                response = CommonClassHelper.createErrorResponse("Old Blocked Claim List Not Fetched Successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoring getOldBlockedClaimList method : ", e);
            return ResponseEntity.ok(CommonClassHelper.createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping(value = "/getOldBlockedClaimDetails")
    public ResponseEntity<?> getOldBlockedClaimDetails(@RequestBody Map<String, Object> request) {
        //logger.info("Inside OldBlockedClaimMonitoring getOldBlockedClaimDetails method");
        Map<String, Object> response;
        try {
            Map<String, Object> oldBlockedClaimDetails = (Map<String, Object>) oldBlockedClaimMonitoringService.getOldBlockedClaimList(request);
            if (oldBlockedClaimDetails != null && oldBlockedClaimDetails.size() > 0)
                response = CommonClassHelper.createSuccessResponse(
                        oldBlockedClaimDetails,
                        "Old Blocked Claim Details Fetched Successfully"
                );
            else
                response = CommonClassHelper.createErrorResponse("Old Blocked Claim Details Not Fetched Successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoring getOldBlockedClaimDetails method : ", e);
            return ResponseEntity.ok(CommonClassHelper.createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping(value = "/submitOldBlockedActionDetails")
    public ResponseEntity<?> submitOldBlockedActionDetails(@RequestBody Map<String, Object> request) {
        //logger.info("Inside OldBlockedClaimMonitoring submitOldBlockedActionDetails method");
        Map<String, Object> response;
        try {
            Map<String, Object> oldBlockedActionDetails = oldBlockedClaimMonitoringService.submitOldBlockedActionDetails(request);
            if (oldBlockedActionDetails != null && oldBlockedActionDetails.size() > 0)
                response = CommonClassHelper.createSuccessResponse(
                        oldBlockedActionDetails,
                        "Old Blocked Data Action Taken Successfully"
                );
            else
                response = CommonClassHelper.createErrorResponse("Old Blocked Data Action Not Taken Successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoring submitOldBlockedActionDetails method : ", e);
            return ResponseEntity.ok(CommonClassHelper.createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping(value ="getActionTakenBlockedDataList")
    public ResponseEntity<?> getActionTakenBlockedDataList(@RequestBody Map<String, Object> request) {
        //logger.info("Inside OldBlockedClaimMonitoring getActionTakenBlockedDataList method");
        Map<String, Object> response;
        try {
            List<Map<String, Object>> actionTakenBlockedDataList = (List<Map<String, Object>>) oldBlockedClaimMonitoringService.getOldBlockedClaimList(request);
            if (actionTakenBlockedDataList != null && actionTakenBlockedDataList.size() > 0)
                response = CommonClassHelper.createSuccessResponse(
                        actionTakenBlockedDataList,
                        "Action Taken Blocked Data List Fetched Successfully"
                );
            else
                response = CommonClassHelper.createErrorResponse("Action Taken Blocked Data List Not Fetched Successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoring getActionTakenBlockedDataList method : ", e);
            return ResponseEntity.ok(CommonClassHelper.createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/downloadOldBlockedDataFile")
    public String downloadOldBlockedDataFile(@RequestParam("data") String enCodedJsonString, HttpServletResponse response) throws JSONException {
        //logger.info("Inside OldBlockedClaimMonitoring downloadOldBlockedDataFile method");
        String resp = "";
        byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
        String jsonString = new String(bytes, StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(jsonString);
        String fileName = json.getString("f");
        String hospitalCode = json.getString("h");
        String year = json.getString("d");
        try {
            if (fileName == null || fileName.equals("") || fileName.equalsIgnoreCase(""))
                resp = "File not found";
            else
                oldBlockedClaimMonitoringService.downloadOldDataDoc(fileName, year, hospitalCode, response);
        } catch (Exception e) {
            logger.error("Exception occurred in OldBlockedClaimMonitoring downloadOldBlockedDataFile method : ", e);
            resp = "File not found";
        }
        return resp;
    }
    
    @GetMapping(value = "/viewblockeddataactioncount")
    public ResponseEntity<?> viewblockeddataactioncount(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "districtCode", required = false) String districtCode,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "clmstatus", required = false) Integer clmstatus) {
        Map<String, Object> response=new HashMap<>();
        try {
        	response.put("data", oldBlockedClaimMonitoringService.viewblockeddataactioncount(userId,stateCode,districtCode,fromDate,toDate,
        			hospitalCode,flag,clmstatus));
        	response.put("status", HttpStatus.OK.value());
        	response.put("message", "Success");
        } catch (Exception e) {
        	response.put("status", HttpStatus.BAD_REQUEST.value());
        	response.put("message", "Error");
        	response.put("error", e.getMessage());
        }
        return ResponseEntity.ok().body(response);
    }
}
