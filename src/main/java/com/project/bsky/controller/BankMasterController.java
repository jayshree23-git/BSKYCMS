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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.BankMasterBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.BankMaster;
import com.project.bsky.service.BankMasterService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class BankMasterController {

	@Autowired
	private Logger logger;

	@Autowired
	private BankMasterService bankMasterService;

	// SAVE CPD LEAVE REQUEST ON CPD SCREEN
	@ResponseBody
	@PostMapping(value = "/saveBankDetailsData")
	public Response saveUnprocessedData(@RequestBody BankMasterBean bankMasterBean) {
		Response returnObj = null;
		try {
			returnObj = bankMasterService.saveBankMasterDetails(bankMasterBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@GetMapping(value = "/getAllBankDetails")
	@ResponseBody
	public Map<String, Object> getBankMasterDetails() {
		List<BankMaster> getBankData = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			getBankData = bankMasterService.getBankDetails();

			details.put("status", "success");
			details.put("details", getBankData);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}



	@ResponseBody
	@GetMapping(value = "/getBankDetailsByBankId")
	public BankMaster getById(@RequestParam(value = "bankId", required = false) Integer bankId) {
		BankMaster bankMaster = bankMasterService.getBankById(bankId);
		return bankMaster;
	}

	@PostMapping(value = "/updateBankDataById")
	public Response updateBankMasterData(@RequestBody BankMasterBean bankMasterBean) {
		Response returnObj = null;
		try {
			returnObj = bankMasterService.updateBankMaster(bankMasterBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

}
