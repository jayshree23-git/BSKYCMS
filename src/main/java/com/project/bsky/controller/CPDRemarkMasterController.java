/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CPDRemarkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.service.CPDRemarkMasterService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class CPDRemarkMasterController {

	@Autowired
	private CPDRemarkMasterService cpdRemarkMasterService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/savecpdremark")
	public Response saveCpdRemark(@RequestBody CPDRemarkBean cpdRemarkBean) {
		Response returnObj = null;
		try {
			returnObj = cpdRemarkMasterService.saveCpdRemark(cpdRemarkBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@GetMapping(value = "/getallcpdremarksdata")
	@ResponseBody
	public List<ActionRemark> getAllcpdRemarks() {
		return cpdRemarkMasterService.getAllRemarks();
	}

	@ResponseBody
	@GetMapping(value = "/getcpdremarkById")
	public ActionRemark getById(@RequestParam(value = "id", required = false) Long id) {
		ActionRemark actionRemark = cpdRemarkMasterService.getActionById(id);
		return actionRemark;
	}

	@PostMapping(value = "/updateCpdRemarkDetails")
	public Response updateBankMasterData(@RequestBody CPDRemarkBean cpdRemarkBean) {
		Response returnObj = null;
		try {
			returnObj = cpdRemarkMasterService.updateBankMaster(cpdRemarkBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}
}
