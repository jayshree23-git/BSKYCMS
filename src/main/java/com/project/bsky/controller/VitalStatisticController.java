package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.VitalStatistics;
import com.project.bsky.service.VitalStatisticService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")

public class VitalStatisticController {

	@Autowired
	private VitalStatisticService vitalStatisticService;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/savevitalstatistics")
	@ResponseBody
	public Response savevitalStatistics(@RequestBody VitalStatistics vitalstatistics) {
		return vitalStatisticService.savevitalStatistics(vitalstatistics);
	}

	@GetMapping(value = "/getvitalstatistics")
	@ResponseBody
	public List<VitalStatistics> getvitalStatistics() {
		return vitalStatisticService.getvitalStatistics();
	}

	@DeleteMapping(value = "/deletevitalstatistics/{vitalStatisticsId}")
	@ResponseBody
	public Response deletevitalstatistics(
			@PathVariable(value = "vitalStatisticsId", required = false) Long vitalStatisticsId) {
		return vitalStatisticService.deletevitalstatistics(vitalStatisticsId);

	}

	@GetMapping(value = "/getvitalstatistics/{vitalStatisticsId}")
	@ResponseBody
	public VitalStatistics getVitalstatistics(
			@PathVariable(value = "vitalStatisticsId", required = false) Long vitalStatisticsId) {
		return vitalStatisticService.getVitalstatistics(vitalStatisticsId);
	}

	@PutMapping(value = "/updatevitalstatistics/{vitalStatisticsId}")
	@ResponseBody
	public Response updatevitalstatistics(@RequestBody VitalStatistics vitalstatistics,
			@PathVariable(value = "vitalStatisticsId", required = false) Long vitalStatisticsId) {
		return vitalStatisticService.update(vitalStatisticsId, vitalstatistics);
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicatevitalstatisticsname")
	public ResponseEntity<Response> checkDuplicateVitalstatisticsName(
			@RequestParam(value = "vitalstatisticsname", required = false) String vitalstatisticsname,
			Response response) {
		try {
			Long subcategoryIdLong = vitalStatisticService.checkDuplicateVitalstatisticsName(vitalstatisticsname);

			if (subcategoryIdLong != null)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicatevitalstatisticscode")
	public ResponseEntity<Response> checkDuplicateVitalstatisticsCode(
			@RequestParam(value = "vitalstatisticscode", required = false) String vitalstatisticscode,
			Response response) {
		try {
			Long subcategoryIdLong = vitalStatisticService.checkDuplicateVitalstatisticsCode(vitalstatisticscode);

			if (subcategoryIdLong != null)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}
}
