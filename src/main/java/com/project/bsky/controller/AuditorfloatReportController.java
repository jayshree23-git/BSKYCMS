package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.Group;
import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.service.AuditorfloatReportService;

@RestController
@RequestMapping(value = "/api")
public class AuditorfloatReportController {
	
	@Autowired
	private AuditorfloatReportService auditorfloatReportService;
	
	@GetMapping(value="/getFloatReportOfAuditor")
	@ResponseBody
	public List<TxnclamFloateDetails> getFloatReportOfAuditor(@RequestParam(value = "fromdate",required = false) String fromdate,
			@RequestParam(value = "todate",required = false) String todate,
			@RequestParam(value = "floatno",required = false) String floatno)
			{
			return auditorfloatReportService.listReportOfAuditor(fromdate,todate,floatno);
			}
}
