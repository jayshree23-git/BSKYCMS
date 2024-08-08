/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UnprocessedConfiguration;
import com.project.bsky.service.UnprocessedConfigurationService;

/**
 * @author priyanka.singh
 *
 */

@RestController
@RequestMapping(value = "/api")
public class UnprocessedConfigurationController {

	@Autowired
	private UnprocessedConfigurationService unprocessedConfigurationService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveUnprocessedMasterData")
	public Response saveUnprocessedData(@RequestBody UnprocessedConfiguration unprocessedConfiguration) {
		Response returnObj = null;

		try {
			returnObj = unprocessedConfigurationService.saveUnprocessedMasterData(unprocessedConfiguration);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@GetMapping(value = "/getUnprocessedClaimDetails")
	public String getUnprocessedList() {

		JSONArray getUnprocessedDetails = null;
		try {
			getUnprocessedDetails = unprocessedConfigurationService.getDetails();
			return getUnprocessedDetails.toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getUnprocessedDetails.toString();
	}

	@ResponseBody
	@GetMapping(value = "/getUnprocessedDtaByUnproceesdId")
	public UnprocessedConfiguration getById(
			@RequestParam(value = "unprocessedId", required = false) Long unprocessedId) {
		UnprocessedConfiguration unprocessedConfiguration = unprocessedConfigurationService
				.getUnproceesedById(unprocessedId);
		return unprocessedConfiguration;
	}

	@PostMapping(value = "/updateUnprocessedAllDetails")
	public Response UnprocessedConfiguration(@RequestBody UnprocessedConfiguration unprocessedConfiguration) {
		Response returnObj = null;
		try {
			returnObj = unprocessedConfigurationService.updateUnprocessed(unprocessedConfiguration);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@GetMapping(value = "/getUnprocessConfigFliterData")
	@ResponseBody
	public List<UnprocessedConfiguration> getAllUnprocessFilterList(
			@RequestParam(value = "years", required = false) Long years,
			@RequestParam(value = "months", required = false) Long months) {
		return unprocessedConfigurationService.getAllUnprocessFilterData(years, months);
	}

}
