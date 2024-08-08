package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.service.RemarksMasterService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class RemarksMasterController {

	@Autowired
	private RemarksMasterService remarksMasterService;

	@PostMapping(value = "/saveremark")
	public Response saveRemarksMaster(@RequestBody ActionRemark actionRemark) {
		return remarksMasterService.saveRemarksMaster(actionRemark);
	}

	@GetMapping(value = "/getallremarks")
	@ResponseBody
	public List<ActionRemark> getAllRemarks() {
		return remarksMasterService.getAllRemarks();
	}

	@GetMapping(value = "/getbyremark/{id}")
	@ResponseBody
	public ActionRemark getbyremark(@PathVariable(value = "id", required = false) Long id) {
		return remarksMasterService.getbyremark(id);
	}

	@PutMapping(value = "/updateremark/{id}")
	@ResponseBody
	public Response updatremark(@RequestBody ActionRemark actionRemark,
			@PathVariable(value = "id", required = false) Long id) {
		return remarksMasterService.updateremark(id, actionRemark);
	}

}
