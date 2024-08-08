/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.FunctionMaster;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.PrimaryLink;
import com.project.bsky.model.UserMenuMapping;
import com.project.bsky.repository.GlobalLinkRepository;
import com.project.bsky.service.PrimaryLinkService;
import com.project.bsky.util.ClassHelperUtils;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class PrimaryLinkController {

	@Autowired
	PrimaryLinkService primarylinkservice;

	@Autowired
	private GlobalLinkRepository globallinkrepo;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getgloballinklist")
	@ResponseBody
	public List<GlobalLink> getgloballinklist() {
		return primarylinkservice.getgloballinklist();
	}

	@GetMapping(value = "/getrespmlist")
	@ResponseBody
	public List<PrimaryLink> getrespmlist(@RequestParam(value = "GId", required = false) Long gid) {
		return primarylinkservice.getrespmlist(gid);
	}

	@GetMapping(value = "/getfunctionlist")
	@ResponseBody
	public List<FunctionMaster> getfunctionlist() {
		return primarylinkservice.getgloballink();
	}

	@PostMapping(value = "/saveprimarylink")
	@ResponseBody
	public Response saveprimarylink(@RequestBody PrimaryLinkBean primarylinkbean) {
		return primarylinkservice.save(primarylinkbean);
	}

	@PostMapping(value = "/updateprimarylink")
	@ResponseBody
	public Response updateprimarylink(@RequestBody PrimaryLinkBean primarylinkbean) {
		return primarylinkservice.update(primarylinkbean);
	}

	@GetMapping(value = "/getprimarylinklist")
	@ResponseBody
	public List<PrimaryLink> getprimarylinklist() {
		return primarylinkservice.findall();
	}

	@GetMapping(value = "/deleteprimarylink")
	@ResponseBody
	public Response deleteprimarylink(@RequestParam(value = "userId", required = false) Long userid) {
		return primarylinkservice.deleteprimarylink(userid);
	}

	@GetMapping(value = "/getfilterprimarylink")
	@ResponseBody
	public List<PrimaryLink> getfilterprimarylink(@RequestParam(value = "primaryid", required = false) String primaryid,
			@RequestParam(value = "globalid", required = false) String globalid,
			@RequestParam(value = "functionid", required = false) String functionid) {
		return primarylinkservice.filterdata(globalid, primaryid, functionid);
	}

	@GetMapping(value = "/getprimarylinkbyid")
	@ResponseBody
	public PrimaryLink getprimarylinkbyid(@RequestParam(value = "userId", required = false) Long userid) {
		return primarylinkservice.getprimarylinkbyid(userid);
	}

	@PostMapping("/checkPrimaryLinkAccess")
	public ResponseEntity<Map<String, Object>> checkPrimaryLinkAccess(@RequestBody Map<String, String> mapRequest) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = primarylinkservice.checkPrimaryLinkAccess(mapRequest);
			return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(map, "success"));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.ok(ClassHelperUtils.createErrorResponse(e.getMessage()));
		}
	}

	@GetMapping(value = "/getMISReportLinkList")
	@ResponseBody
	public List<UserMenuMapping> getReportLinkList(@RequestParam(value = "userId", required = false) Integer userid) {
		String globalLinkName = "MIS Report";
		Integer globalId = null;
		GlobalLink findByglobalLinkName = null;
		findByglobalLinkName = globallinkrepo.findByglobalLinkNameAndBitStatus(globalLinkName.trim(), 0);
		if (findByglobalLinkName != null) {
			globalId = findByglobalLinkName.getGlobalLinkId().intValue();
		}
		return primarylinkservice.getMISReportList(userid, globalId);
	}

}
