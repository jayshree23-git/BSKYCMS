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
import com.project.bsky.model.Group;
import com.project.bsky.service.GroupService;

@RestController
@RequestMapping(value = "/api")
public class GroupController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/saveGroupData")
	public Integer addGroup(@RequestBody Group group) {
		try {
			return 1;

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}

	}

	@GetMapping(value = "/getGroupData")
	private List<Group> getGroupData() {

		List<Group> listGrp = null;
		try {
			listGrp = groupService.listGroup();
			return listGrp;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listGrp;

	}

	@ResponseBody
	@GetMapping(value = "/delete")
	private Integer delete(@RequestParam(value = "groupId", required = false) Integer groupId) {

		try {
			groupService.delete(groupId);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return 1;

	}

	@ResponseBody
	@PostMapping("/updateGroup")
	public Integer updateGroup(@RequestBody Group group) {

		Integer returnObject = null;
		try {
			returnObject = groupService.updateGroup(group);
			return returnObject;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return returnObject;
		}

	}

	@ResponseBody
	@GetMapping("/updateGroupById")
	public Group getData(@RequestParam(value = "groupId", required = false) Integer groupId) {

		Group returnOb = null;
		try {
			returnOb = groupService.findAllByGroupId(groupId);
			return returnOb;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return returnOb;

	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicateData")
	public ResponseEntity<Response> checkDuplicateData(
			@RequestParam(value = "groupName", required = false) String groupName, Response response) {
		try {
			Integer groupIdInteger = groupService.checkGruopByName(groupName);

			if (groupIdInteger != null)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

}
