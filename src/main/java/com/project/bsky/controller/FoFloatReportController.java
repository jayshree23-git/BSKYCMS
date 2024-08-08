/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.service.FoFloatReportService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class FoFloatReportController {
	@Autowired
	private FoFloatReportService foFloatReportService;

	@GetMapping(value="/getFilterFoReport")
	public List<TxnclamFloateDetails> getFilterDetailsForFloatReport(
			@RequestParam(value = "floateno", required = false)String floateno,
			@RequestParam(value = "formdate", required = false) String formdate,
			@RequestParam(value = "todate", required = false) String todate){
		////System.out.println("Data come from frontend+++++ "+floateno+","+formdate+","+todate);
		return foFloatReportService.getFilterAllDataForFo(floateno,formdate,todate);
		
	}
	

}
