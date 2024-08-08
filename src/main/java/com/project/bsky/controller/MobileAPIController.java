package com.project.bsky.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.MobileAuthResponse;
import com.project.bsky.model.MobileAuthRequest;
import com.project.bsky.service.ClaimsMobileAPIService;

/**
 * @Project : BSKY Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 07/03/2023 - 3:46 PM
 */

@RestController
@RequestMapping(value = "/mobileApi")
public class MobileAPIController {

    @Autowired
    private ClaimsMobileAPIService claimsMobileAPIService;
    
    @Autowired
    private Logger logger;

    @RequestMapping(value = "/requestMobileUserLogin", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> requestLogin(@RequestBody MobileAuthRequest mobileAuthRequest) {
        Map<String, Object> details = new LinkedHashMap<>();
        MobileAuthResponse response = null;
        try {
            response = claimsMobileAPIService.requestLogin(mobileAuthRequest);
            details.put("status", (HttpStatus.OK).value());
            details.put("msg", "Login successful");
            details.put("data", response);

        } catch (Exception ex) {
        	logger.error(ExceptionUtils.getStackTrace(ex));
            details.put("status", (HttpStatus.BAD_REQUEST).value());
            details.put("msg", ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(details);
    }
}
