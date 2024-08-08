package com.project.bsky.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CardPolicyBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CardPolicy;
import com.project.bsky.service.CardPolicyService;

@RestController
@RequestMapping(value = "/api")
public class CardPolicyController {

	@Autowired
	private CardPolicyService cardPolicyService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getCardPolicyDate")
	@ResponseBody
	public CardPolicy getCardPolicyDate() {
		return cardPolicyService.getCardPolicyDate();
	}

	@ResponseBody
	@PostMapping(value = "/updateCardPolicy")
	public String updateCardPolicy(@RequestBody CardPolicyBean requestBean, Response response) {
		String policyList = null;
		try {
			policyList = cardPolicyService.updatePolicy(requestBean).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return policyList;
	}
}
