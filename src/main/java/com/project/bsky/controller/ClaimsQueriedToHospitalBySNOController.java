package com.project.bsky.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;
import com.project.bsky.service.ClaimsQueriedToHospitalBySNOService;

@RequestMapping(value = "/api")
@RestController
public class ClaimsQueriedToHospitalBySNOController {

	private final Logger logger;

	@Autowired
	public ClaimsQueriedToHospitalBySNOController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private ClaimsQueriedToHospitalBySNOService claimsQueriedToHospitalBySNOService;

	@ResponseBody
	@RequestMapping(value = "/getQueriedClaimsList", method = RequestMethod.GET)
	public List<Object> getQueriedClaimsList(
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "packagecode", required = false) String packagecode,
			@RequestParam(value = "URN", required = false) String URN,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		List<Object> claimsList = null;
		try {
			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				claimsList = claimsQueriedToHospitalBySNOService.getQueriedClaimsList(hospitalCode, null, null, Package,
						packagecode, URN,schemeid,schemecategoryid);
			} else {
				claimsList = claimsQueriedToHospitalBySNOService.getQueriedClaimsList(hospitalCode, fromDate, toDate,
						Package, packagecode, URN,schemeid,schemecategoryid);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getQueriedClaimsList method of ClaimsQueriedToHospitalBySNOController"
					+ e.getMessage());
			e.printStackTrace();
		}
		return claimsList;
	}

	@ResponseBody
	@RequestMapping(value = "/getQueriedClaimDetails", method = RequestMethod.GET)
	public String getQueriedClaimDetails(@RequestParam(value = "claimID") String claimID) {
		String SnoClaim = null;
		try {
			SnoClaim = claimsQueriedToHospitalBySNOService.getQueriedClaimDetails(claimID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured in getQueriedClaimDetails method of ClaimsQueriedToHospitalBySNOController"
					+ e.getMessage());
		}
		return SnoClaim;
	}

	@ResponseBody
	@PostMapping(value = "/takeActionOnQuery", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Response> saveQuerysnohospital(
			@RequestParam(value = "claimID", required = false) String claimID,
			@RequestParam(value = "userName", required = false) String hospitalCode,
			@RequestParam(value = "Additionaldoc1", required = false) MultipartFile Additionaldoc1,
			@RequestParam(value = "Additionaldoc2", required = false) MultipartFile Additionaldoc2,
			@RequestParam(value = "UrnNumber", required = false) String UrnNumber,
			@RequestParam(value = "dateofAdmission", required = false) String dateofAdmission,
			@RequestParam(value = "ClaimId", required = false) String ClaimId,
			@RequestParam(value = "ClaimAmount", required = false) String ClaimAmount,
			@RequestParam(value = "actionby", required = false) String actionby,
			@RequestParam(value = "dischargeSlip", required = false) String dischargeSlip,
			@RequestParam(value = "Additionalslip", required = false) String Additionalslip,
			@RequestParam(value = "presuergrypic", required = false) String presuergrypic,
			@RequestParam(value = "postsuergrypic", required = false) String postsuergrypic,
			@RequestParam(value = "intrasurgerypic", required = false) String intrasurgerypic,
			@RequestParam(value = "SpecimenPic", required = false) String SpecimenPic,
			@RequestParam(value = "patientpic", required = false) String patientpic) {
		Response response = new Response();
		try {
			response = claimsQueriedToHospitalBySNOService.takeActionOnClaimforsnoQuery(claimID, hospitalCode,
					Additionaldoc1, Additionaldoc2, UrnNumber, dateofAdmission, ClaimId, ClaimAmount, actionby,
					dischargeSlip, Additionalslip, presuergrypic, postsuergrypic, intrasurgerypic, SpecimenPic,
					patientpic);
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
			logger.error(
					"Exception occured in takeActionOnClaimforsnoQuery method of ClaimsQueriedToHospitalBySNOController"
							+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/downLoadActionforSnoQueriedthospital")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("fileName") String fileName,
			@RequestParam("hCode") String hCode, @RequestParam("dateOfAdm") String dateOfAdm) {
		try {
			String year = dateOfAdm.substring(6);
			claimsQueriedToHospitalBySNOService.downLoadFileforforSnoQueriedthospital(fileName, year, hCode, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
