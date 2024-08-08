package com.project.bsky.controller;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.service.CpdQueryToHospitalService;

@Controller
@RequestMapping(value = "/api")
public class CpdQueryToHospitalController {

	private final Logger logger;

	@Autowired
	public CpdQueryToHospitalController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private CpdQueryToHospitalService cpdQueryToHospitalService;

	@ResponseBody
	@GetMapping(value = "/getclaimDetailsAfterQueryFromCpd")
	public List<Object> getallDataForCpdQueryToHospital(
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "packageCodedata", required = false) String packageCodedata,
			@RequestParam(value = "URN", required = false) String URN,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		List<Object> claiList = null;
		try {
			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				claiList = cpdQueryToHospitalService.getAllData(hospitalCode, null, null, Package, packageCodedata,
						URN,schemeid,schemecategoryid);
			} else {
				claiList = cpdQueryToHospitalService.getAllData(hospitalCode, fromDate, toDate, Package,
						packageCodedata, URN,schemeid,schemecategoryid);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getallDataForCpdQueryToHospital method of CpdQueryToHospitalController"
					+ e.getMessage());
		}
		return claiList;
	}

	@ResponseBody
	@RequestMapping(value = "/getQueriedClaimDetailsFromCpd", method = RequestMethod.GET)
	public String getQueriedClaimDetails(@RequestParam(value = "claimID") String claimID) {
		String CpdClaim = null;
		try {
			CpdClaim = cpdQueryToHospitalService.getQueriedClaimDetails(claimID);
		} catch (Exception e) {
			logger.error("Exception occured in getQueriedClaimDetails method of CpdQueryToHospitalController"
					+ e.getMessage());
		}
		return CpdClaim;
	}

	@ResponseBody
	@PostMapping(value = "/takeActionOnQueryCpd", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Response> saveQuerysnohospital(
			@RequestParam(value = "transactiondetailsid", required = false) String transactiondetailsid,
			@RequestParam(value = "actualdateofdiscahrge", required = false) String actualdateofdiscahrge,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "investicationdoc1", required = false) MultipartFile additionaldoc1,
			@RequestParam(value = "investicationdocforcpd2", required = false) MultipartFile additionaldoc2,
			@RequestParam(value = "urnno", required = false) String urnno,
			@RequestParam(value = "dateofadmissio", required = false) String dateofadmissio,
			@RequestParam(value = "ClaimId", required = false) String ClaimId,
			@RequestParam(value = "ClaimAmount", required = false) String ClaimAmount,
			@RequestParam(value = "actionby", required = false) String actionby,
			@RequestParam(value = "dischrageslip", required = false) String dischrageslip,
			@RequestParam(value = "additionaldocs", required = false) String additionaldocs,
			@RequestParam(value = "PreSurgerypic", required = false) String PreSurgerypic,
			@RequestParam(value = "PostSurgerypic", required = false) String PostSurgerypic,
			@RequestParam(value = "intrasurgerypic", required = false) String intrasurgerypic,
			@RequestParam(value = "specimenremoval", required = false) String specimenremoval,
			@RequestParam(value = "patientpic", required = false) String patientpic) {
		Response response = new Response();
		try {
			response = cpdQueryToHospitalService.takeActionOnClaimforCPDQuery(transactiondetailsid,
					actualdateofdiscahrge, hospitalCode, additionaldoc1, additionaldoc2, urnno, dateofadmissio, ClaimId,
					ClaimAmount, actionby, dischrageslip, additionaldocs, PreSurgerypic, PostSurgerypic,
					intrasurgerypic, specimenremoval, patientpic);
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
			logger.error("Exception occured in takeActionOnClaimforCPDQuery method of CpdQueryToHospitalController"
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

}
