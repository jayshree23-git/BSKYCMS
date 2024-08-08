package com.project.bsky.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.ClaimraiseDetailsService;

@RestController
@RequestMapping(value = "/api")
public class ClaimMonitoring {

	private final Logger logger;

	@Autowired
	public ClaimMonitoring(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private ClaimraiseDetailsService claimRaiseService;

	@ResponseBody
	@GetMapping(value = "/getclaimDetails")
	public List<Object> getallDataForClaimRequest(
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "packageCode", required = false) String packageCode,
			@RequestParam(value = "URN", required = false) String URN,
			@RequestParam(value = "caseno", required = false) String caseno,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		List<Object> claiList = null;
		try {
			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				claiList = claimRaiseService.getclaimrasiedata(hospitalCode, Package, packageCode, URN, null, null,
						caseno,schemeid,schemecategoryid);
			} else {
				claiList = claimRaiseService.getclaimrasiedata(hospitalCode, Package, packageCode, URN, fromDate,
						toDate, caseno,schemeid,schemecategoryid);
			}
		} catch (Exception e) {
			logger.error("Exception occured in getallDataForClaimRequest method of ClaimMonitoring" + e.getMessage());
		}
		return claiList;
	}

	@ResponseBody
	@GetMapping(value = "/getclaimdetailsid/{check}")
	public List<Object> getclaimdetailsthroughid(@PathVariable("check") String check) {
		List<Object> listInteger = null;
		try {
			listInteger = claimRaiseService.getUSerDetailsDAta(check);

		} catch (Exception e) {
			logger.error("Exception occured in getclaimdetailsthroughid method of ClaimMonitoring" + e.getMessage());
		}
		return listInteger;
	}

	@ResponseBody()
	@PostMapping(value = "/saveClaimRaiseHospital", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Response> saveClaimRaiseHospita(
			@RequestParam(value = "refractionid", required = false) Long refractionid,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "updatedby", required = false) Long updatedby,
			@RequestParam(value = "UrnNumber", required = false) String UrnNumber,
			@RequestParam(value = "invoiceNumber", required = false) BigInteger invoiceNumber,
			@RequestParam(value = "dateofAdmission", required = false) String dateofAdmission,
			@RequestParam(value = "packageCode", required = false) String packageCode,
			@RequestParam(value = "TreatmentDetailsSlip", required = false) MultipartFile TreatmentDetailsSlip,
			@RequestParam(value = "HospitalBill", required = false) MultipartFile HospitalBill,
			@RequestParam(value = "presurgry", required = false) MultipartFile presurgry,
			@RequestParam(value = "postsurgery", required = false) MultipartFile postsurgery,
			@RequestParam(value = "casenumber", required = false) String casenumber,
			@RequestParam(value = "billnumber", required = false) String billnumber,
			@RequestParam(value = "intasurgery", required = false) MultipartFile intasurgery,
			@RequestParam(value = "specimansurgery", required = false) MultipartFile specimansurgery,
			@RequestParam(value = "patientpic", required = false) MultipartFile patientpic,
			@RequestParam(value = "TMSdischargedocumnet", required = false) String tmsdischargedocumnet,
			@RequestParam(value = "claimbilldate", required = false) String claimbilldate,
			@RequestParam(value = "amountValue", required = false) Long amountValue,
			@RequestParam(value = "remarks", required = false) String remarks) {
		Response response = new Response();
		try {
			response = claimRaiseService.saveClaimRaiseHospital(refractionid, hospitalCode, updatedby, UrnNumber,
					invoiceNumber, dateofAdmission, packageCode, TreatmentDetailsSlip, HospitalBill, presurgry,
					postsurgery, casenumber, billnumber, intasurgery, specimansurgery, patientpic, tmsdischargedocumnet,
					claimbilldate,amountValue,remarks);
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
			logger.error("Exception occured in saveClaimRaiseHospita method of ClaimMonitoring" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody()
	@PostMapping(value = "/commonDownloadAction")
	public ResponseEntity<byte[]> commonDownloadMethod(
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "hCode", required = false) String hCode,
			@RequestParam(value = "dateOfAdm", required = false) String dateOfAdm) {
		byte[] downLoadFile = null;
		try {
			String year = dateOfAdm.substring(4);
			downLoadFile = claimRaiseService.downLoadFileClaim(fileName, year, hCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/jpg"))
				.header("Content-Disposition", "inline;filename=" + fileName).body(downLoadFile);
	}

	/**
	 * @Author : Sambit Kumar Pradhan
	 * @Date : 17-01-2023
	 * @Description : For Get Unsaved Claimed Files List Into Database
	 */
	@GetMapping(value = "/UnsavedClaimFiles")
	public void UnsavedClaimFiles() {
		try {
			claimRaiseService.getUnSavedClaimData();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@ResponseBody
	@PostMapping(value = "/getClaimList")
	public ResponseEntity<?> getClaimList(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> claimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			claimList = claimRaiseService.getClaimList(requestBean);
			details.put("status", "success");
			details.put("data", claimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/executeProcess")
	@ResponseBody
	public ResponseEntity<?> executeProcess(@RequestParam("urn") String urn,
			@RequestParam("processId") Integer processId) throws Exception {
		Response response = new Response();
		try {
			response = claimRaiseService.executeProcess(urn, processId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
}
