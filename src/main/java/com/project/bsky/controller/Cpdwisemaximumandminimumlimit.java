/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.service.CpdwisemaximumandminimumlimtSrvice;

/**
 * @author hrusikesh.mohanty
 *
 */
@RestController
@RequestMapping(value = "/api")
public class Cpdwisemaximumandminimumlimit {

	private final Logger logger;

	@Autowired
	public Cpdwisemaximumandminimumlimit(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private CpdwisemaximumandminimumlimtSrvice cpdwisemaximumandminimumlimt;

	@GetMapping(value = "/cpdwisemaximumandminimumlimit")
	public ResponseEntity<Response> savecpdwisemaximumminimumlimit(
			@RequestParam(value = "cpdid", required = false) Long cpdid,
			@RequestParam(value = "maxlimit", required = false) Long maxlimit,
			@RequestParam(value = "puserid", required = false) Long puserid,
			@RequestParam(value = "Assigneduptodate", required = false) String Assigneduptodate) {
		Response response = null;
		try {
			response = cpdwisemaximumandminimumlimt.savecpdwisemaximumandminimumlimt(cpdid, maxlimit, puserid,
					Assigneduptodate);
		} catch (Exception e) {
			logger.error("Exception occured in Cpdwisemaximumandminimumlimit method of savecpdwisemaximumandminimumlimt"
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/cpdwisemaximumandminimumlimitview")
	public List<Object> Viewcpdwisemaximumminimumlimit(@RequestParam(value = "cpdid", required = false) String cpdid) {
		List<Object> claiList = null;
		try {
			claiList = cpdwisemaximumandminimumlimt.cpdwisemaximumandminimumlimtview(cpdid);
		} catch (Exception e) {
			logger.error(
					"Exception occured in Viewcpdwisemaximumminimumlimit method of cpdwisemaximumandminimumlimtview"
							+ e.getMessage());
		}
		return claiList;
	}

	@GetMapping(value = "/cpdwisemaximumandminimumlimitupdate")
	public Map<String, Object> getUpdatedatacpdwisemaximumminimumlimit(
			@RequestParam(value = "cpduserid", required = false) String cpduserid) {
		Map<String, Object> claiList = null;
		try {
			claiList = cpdwisemaximumandminimumlimt.cpdwisemaximumandminimumlimttogetupdatedata(cpduserid);
		} catch (Exception e) {
			logger.error(
					"Exception occured in Updatecpdwisemaximumminimumlimit method of cpdwisemaximumandminimumlimttogetupdatedata"
							+ e.getMessage());
		}
		return claiList;
	}

	@GetMapping(value = "/cpdwisemaximumandminimumlimitupdaterecord")
	public ResponseEntity<Response> updaterecordcpdwisemaximumminimumlimit(
			@RequestParam(value = "cpdid", required = false) Long cpdid,
			@RequestParam(value = "maxlimit", required = false) Long maxlimit,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "updatedassigneduptodate", required = false) String updatedassigneduptodate) {
		Response response = null;
		try {
			response = cpdwisemaximumandminimumlimt.cpdwisemaximumandminimumlimtupdaterecord(cpdid, maxlimit, userid,
					updatedassigneduptodate);
		} catch (Exception e) {
			logger.error(
					"Exception occured in updaterecordcpdwisemaximumminimumlimit method of cpdwisemaximumandminimumlimtupdaterecord"
							+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
}
