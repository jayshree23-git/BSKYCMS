/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;
import java.util.Objects;

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

import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SystemRejQueryCpdBean;
import com.project.bsky.service.SnaSystemRejectedListService;

/**
 * @author hrusikesh.mohanty
 *
 */
@RestController
@RequestMapping(value = "/api")
public class SnaSystemRejectedList {

	private final Logger logger;

	@Autowired
	public SnaSystemRejectedList(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private SnaSystemRejectedListService snasystemrejectedlist;

	@ResponseBody
	@GetMapping(value = "/getSnasystemRejectedlist")
	public List<ClaimsQueriedToHospitalBySNOBean> getSnasystemRejectedlist(
			@RequestParam(value = "hospitalcoderejected", required = false) String hospitalcoderejected,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "packageCodedata", required = false) String packageCodedata,
			@RequestParam(value = "URN", required = false) String URN,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		List<ClaimsQueriedToHospitalBySNOBean> snarejectedlist = null;
		try {

			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				snarejectedlist = snasystemrejectedlist.getSnarejectedlistdata(hospitalcoderejected, null, null,
						Package, packageCodedata, URN,schemeid,schemecategoryid);
			} else {
				snarejectedlist = snasystemrejectedlist.getSnarejectedlistdata(hospitalcoderejected, fromDate, toDate,
						Package, packageCodedata, URN,schemeid,schemecategoryid);
			}

		} catch (Exception e) {
			logger.error(
					"Exception occured in getSnasystemRejectedlist method of SnaSystemRejectedList" + e.getMessage());
		}
		return snarejectedlist;

	}

	@PostMapping(value = "/rejectRequestSna")
	public ResponseEntity<Response> saveRejectedRequest(@RequestBody SystemRejQueryCpdBean rejBean) {
		Response response = null;
		try {
			response = snasystemrejectedlist.saveRejectRequestSNA(rejBean);
		} catch (Exception e) {
			logger.error("Exception occured in saveRejectedRequest method of SnaSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

}
