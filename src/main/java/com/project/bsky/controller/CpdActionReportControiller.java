
package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.CpdActionReportService;

/**
 * @author jayshree.moharana
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class CpdActionReportControiller {

	@Autowired
	private CpdActionReportService cpdactionreport;

	@ResponseBody
	@GetMapping(value = "/cpdactionreport")
	public List<Object> cpdactionreport(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "year") String year,
			@RequestParam(required = false, value = "month") String month) {
		return cpdactionreport.cpdactionreport(userId, year, month);
	}

	@ResponseBody
	@GetMapping(value = "/cpdactiontakenreport")
	public List<Object> cpdactiontakenreport(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "date") String date) {
		return cpdactionreport.cpdactiontakenreport(userId, date);
	}

	@ResponseBody
	@GetMapping(value = "/getcpdactiontekendetails")
	public List<Object> getcpdactiontekendetails(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "date") String date,
			@RequestParam(required = false, value = "type") Integer type) {
		return cpdactionreport.getcpdactiontekendetails(userId, date, type);
	}

}
