package com.project.bsky.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.project.bsky.service.WhatsAppMessagingService;
import com.project.bsky.util.ClassHelperUtils;

import java.util.Map;

@RestController
@RequestMapping(value = "/whatsApp")
public class WhatsAppMessagingController {

	private final Logger logger;

	private final WhatsAppMessagingService whatsAppMessagingService;

	@Autowired
	public WhatsAppMessagingController(Logger logger, WhatsAppMessagingService whatsAppMessagingService) {
		this.logger = logger;
		this.whatsAppMessagingService = whatsAppMessagingService;
	}

	@GetMapping(value = "/testWhatsAppMessage")
	public ResponseEntity<Map<String, Object>> testWhatsAppMessage() {
		logger.info("Inside testWhatsAppMessage of WhatsAppMessagingController");
		Map<String, Object> response;
		try {
			Integer messageStatus = whatsAppMessagingService.testWhatsAppMessage();
			if (messageStatus != null && messageStatus == 200)
				response = ClassHelperUtils.createSuccessMessageResponse("WhatsApp Message Send Successfully.");
			else
				response = ClassHelperUtils.createErrorResponse("Failed to Send WhatsApp Message!");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.info("Exception Occurred in testWhatsAppMessage method of WhatsAppMessagingController :", e);
			return ResponseEntity.ok(ClassHelperUtils.createErrorResponse(e.getMessage()));
		}
	}

}
