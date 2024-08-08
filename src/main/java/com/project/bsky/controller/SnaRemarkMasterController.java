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

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaRemarkBean;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.service.SnaRemarkMasterService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class SnaRemarkMasterController {

	@Autowired
	private SnaRemarkMasterService snaRemarkMasterService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/savesnaremarkdata")
	public Response savesnaRemark(@RequestBody SnaRemarkBean snaRemarkBean) {
		Response returnObj = null;
		try {
			returnObj = snaRemarkMasterService.savesnaRemark(snaRemarkBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@GetMapping(value = "/getallsnaremark")
	@ResponseBody
	public List<ActionRemark> getAllcpdRemarks() {
		return snaRemarkMasterService.getAllsnaRemarks();
	}

	@ResponseBody
	@GetMapping(value = "/getsnaremarksById")
	public ActionRemark getById(@RequestParam(value = "id", required = false) Long id) {
		ActionRemark actionRemark = snaRemarkMasterService.getActionById(id);
		return actionRemark;
	}

	@PostMapping(value = "/updatesnaRemarkDetails")
	public Response updatesnaMasterData(@RequestBody SnaRemarkBean snaRemarkBean) {
		Response returnObj = null;
		try {
			returnObj = snaRemarkMasterService.updatesnaremarkMaster(snaRemarkBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

}
