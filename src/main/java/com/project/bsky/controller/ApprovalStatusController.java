package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.service.ApprovalStatusService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class ApprovalStatusController {

	@Autowired
	private ApprovalStatusService approvalstatusservice;

	@ResponseBody
	@GetMapping(value = "/getapprovalstatusllist")
	public List<HospitalSpecialistListBean> approvalreport(
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode) {
		return approvalstatusservice.getapprovalstatusllist(hospitalcode);
	}

}
