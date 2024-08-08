package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.service.GroupTypeService;

@RestController
@RequestMapping(value = "/api")
public class GroupTypeController {

	@Autowired
	private GroupTypeService groupTypeService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveGroupTypeData")
	public Integer addGroup(@RequestBody GroupTypeDetails grouptype) {
		try {
			return groupTypeService.saveDetails(grouptype);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@GetMapping(value = "/getGroupTypeDetails")
	public List<GroupTypeDetails> getGroupDetails() {
		List<GroupTypeDetails> groupDetails = null;
		try {
			groupDetails = groupTypeService.getDetails();
			return groupDetails;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return groupDetails;
	}

	@ResponseBody
	@GetMapping(value = "/getDeleteGroup")
	public Integer deleteById(@RequestParam(value = "typeId", required = false) Integer typeId) {
		try {
			groupTypeService.deleteById(typeId);
			return 1;

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return 1;

	}

	@ResponseBody
	@GetMapping(value = "/getGroupTypebyid")
	public GroupTypeDetails groupUpdate(@RequestParam(value = "typeId", required = false) Integer typeId) {
		GroupTypeDetails groupdetails = groupTypeService.getbyid(typeId);
		return groupdetails;

	}

	@ResponseBody
	@PostMapping(value = "/updategroup")
	public Integer Updategroup(@RequestBody GroupTypeDetails groupdetails) {
		Integer I = groupTypeService.update(groupdetails);
		return I;

	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicateGrpData")
	public ResponseEntity<Response> checkDuplicateData(@RequestParam(value = "typeId", required = false) String typeId,
			Response response) {
		try {
			Integer groupIdInteger = groupTypeService.checkGroupTypeId(Integer.parseInt(typeId));
			if (groupIdInteger != 0)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}
}
