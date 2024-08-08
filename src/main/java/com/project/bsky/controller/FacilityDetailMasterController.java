/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.FacilityDetailBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.FacilityDetails;
import com.project.bsky.service.FacilityDetailMasterService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class FacilityDetailMasterController {

	@Autowired
	private FacilityDetailMasterService facilityDetailMasterService;

	@ResponseBody
	@PostMapping(value = "/saveFacilityData")
	public Response saveFacilityDetail(@RequestBody FacilityDetailBean facilityDetailBean) {
		Response returnObj = null;
		try {
			returnObj = facilityDetailMasterService.saveFaciltyData(facilityDetailBean);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	@GetMapping(value = "/getallFacilityData")
	@ResponseBody
	public List<FacilityDetails> getAllFacilityData() {
		return facilityDetailMasterService.getAllFacilityDetail();
	}

	@ResponseBody
	@GetMapping(value = "/getFacilityDetailsByFacility")
	public FacilityDetails getById(
			@RequestParam(value = "facilityDetailId", required = false) Integer facilityDetailId) {
		return facilityDetailMasterService.getActionById(facilityDetailId);
	}

	@PostMapping(value = "/updateFacilitatyData")
	public Response updateFacilityData(@RequestBody FacilityDetailBean facilityDetailBean) {
		Response returnObj = null;
		try {
			returnObj = facilityDetailMasterService.updateFacilityDetail(facilityDetailBean);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

}
