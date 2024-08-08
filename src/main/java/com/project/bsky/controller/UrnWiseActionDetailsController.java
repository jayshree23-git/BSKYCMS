/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.UrnWiseDataBean;
import com.project.bsky.service.UrnWiseActionDetailsService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class UrnWiseActionDetailsController {

	@Autowired
	private UrnWiseActionDetailsService urnWiseActionDetailsService;

	@Autowired
	private Logger logger;
	
	@GetMapping("/getUrnWiseActionDetailsRprt")
	@ResponseBody
	public UrnWiseDataBean getUrnWiseDetailsReport(@RequestParam("urnNo") String urnNo,
			@RequestParam("transId") Long transId) {
		//System.out.println(urnNo+"--"+transId);
		UrnWiseDataBean jsonArray = new UrnWiseDataBean();
		try {
			jsonArray = urnWiseActionDetailsService.getUrnWiseDetails(urnNo, transId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return jsonArray;
	}

	@GetMapping("/getDishounrData")
	@ResponseBody
	public List<Object> getDishounerList(@RequestParam("urnNo") String urnNo, @RequestParam("clmId") Long clmId) {
		//System.out.println(clmId+"ss"+urnNo);
		List<Object> getDishounerList = null;
		try {
			getDishounerList = urnWiseActionDetailsService.getDishouner(urnNo, clmId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getDishounerList;
	}

	@GetMapping("/geturnWiseWardDetails")
	@ResponseBody
	public List<Object> geturnWiseWardDetails(@RequestParam("urnNo") String urnNo,
			@RequestParam("transId") Long transId) {
		List<Object> geturnWiseWardDetails = null;
		try {
			geturnWiseWardDetails = urnWiseActionDetailsService.geturnWiseWardData(urnNo, transId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return geturnWiseWardDetails;
	}

	@GetMapping("/getHighEndDrugData")
	@ResponseBody
	public List<Object> getHighEndDrugData(@RequestParam("urnNo") String urnNo, @RequestParam("transId") Long transId) {
		List<Object> getHighEndDrugData = null;
		try {
			getHighEndDrugData = urnWiseActionDetailsService.getHighEndDrugData(urnNo, transId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getHighEndDrugData;
	}

	@GetMapping("/getImplantData")
	@ResponseBody
	public List<Object> getImplantDetails(@RequestParam("urnNo") String urnNo, @RequestParam("transId") Long transId) {
		List<Object> getImplantData = null;
		try {
			getImplantData = urnWiseActionDetailsService.getImplantDetails(urnNo, transId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getImplantData;
	}

}
