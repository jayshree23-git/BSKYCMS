/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;
import com.project.bsky.service.Noncompleincequeryreportservice;

/**
 * @author rajendra.sahoo
 *
 */
@RestController
@RequestMapping(value = "/api")
public class Noncompleincequeryreport {
	
	@Autowired
	private Noncompleincequeryreportservice noncompliencereport;
	

@Autowired
private Logger logger;
	
	@ResponseBody
	@GetMapping(value = "/noncompleincequerysno")
	public List<ClaimsQueriedToHospitalBySNOBean> noncompleincequerysno(@RequestParam(value = "sno", required = false) String  sno,
			@RequestParam(value = "fromDate" ,required = false) String fromDate,
	    	@RequestParam(value = "toDate" ,required = false) String toDate,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "PackageName", required = false) String PackageName,
	        @RequestParam(value = "URN", required = false) String URN)
	{
		List<ClaimsQueriedToHospitalBySNOBean> list=new ArrayList<ClaimsQueriedToHospitalBySNOBean>();
		try {
			////System.out.println();
			list=noncompliencereport.getnoncompleincequerysno(sno,fromDate,toDate,Package,PackageName,URN);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

}
