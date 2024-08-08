/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.PaymentMode;
import com.project.bsky.service.GetPaymentModeService;


/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class GetPaymentModeController {
	
	@Autowired
	private GetPaymentModeService getPaymentModeService;
	
	@Autowired
	private Logger logger;
	
	
	@GetMapping(value = "/getPaymentModeData")
	@ResponseBody
	public Map<String, Object> getPaymentDetails() {
		List<PaymentMode> getPaymentData = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			getPaymentData = getPaymentModeService.getDetails();
			
			details.put("status", "success");
			details.put("details", getPaymentData);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

}
