package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.TreatmentHistoryPerUrnBean;
import com.project.bsky.bean.Treatmenthistorybypackagecode;
import com.project.bsky.service.SnoClaimProcessingDetails;
import com.project.bsky.service.TreatmentHistoryService;
import com.project.bsky.serviceImpl.SnoClaimProcessingDetailsImpl;

@RestController
@RequestMapping(value = "/api")
public class TreatmentHistoryController {

	@Autowired
	private TreatmentHistoryService treatmentHistoryService;

	@Autowired
	private SnoClaimProcessingDetails snoClaimProcessing;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getTreatMentHistorybyurnandpackagecode")
	private List<Treatmenthistorybypackagecode> getTreatmentHistoryData(
			@RequestParam(required = false, value = "urnno") String urnno,
			@RequestParam(required = false, value = "packagecode") String packagecode) {
		//// System.out.println(urnno);
		//// System.out.println(packagecode);
		List<Treatmenthistorybypackagecode> trtmtlist = null;
		try {
			trtmtlist = treatmentHistoryService.gettrtmenthistry(urnno, packagecode);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return trtmtlist;

	}

	@GetMapping(value = "/getTreatMentHistorybyUrn")
	private List<TreatmentHistoryPerUrnBean> getTreatMentHistory(
			@RequestParam(required = false, value = "urnno") String urnno) {

		List<TreatmentHistoryPerUrnBean> treatmentList = null;
		try {
			treatmentList = treatmentHistoryService.getTreatmentHistory(urnno);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return treatmentList;
	}

	@GetMapping(value = "/getTreatMentHistorySna")
	@ResponseBody
	private String getTreatMentHistorySna(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "urnno") String urnno) {
		//// System.out.println(userId+"\t"+urnno);
		String treatmentList = null;
		try {
			treatmentList = treatmentHistoryService.getTreatmentHistorySna(urnno, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return treatmentList;
	}

	@GetMapping(value = "/getOldTreatMentHistorySna")
	@ResponseBody
	private String getOldTreatMentHistorySna(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "urnno") String urnno) {
		//// System.out.println(userId+"\t"+urnno);
		String treatmentList = null;
		try {
			treatmentList = treatmentHistoryService.getOldTreatmentHistorySna(urnno, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return treatmentList;
	}

	@GetMapping(value = "/getOnGoingTreatmenthistory")
	@ResponseBody
	private String getOnGoingTreatmenthistory(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "urnno") String urnno) {
		//// System.out.println(userId+"\t"+urnno);
		String ongoingtList = null;
		try {
			ongoingtList = treatmentHistoryService.getOnGoingTreatmenthistorylist(urnno, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ongoingtList;
	}

	@GetMapping(value = "/patienttreatmnetlog")
	@ResponseBody
	private List<Object> patienttreatmnetlog(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "urnno") String urnno,
			@RequestParam(required = false, value = "txnid") Long txnid) {
		//// System.out.println(userId+"\t"+urnno);
		List<Object> ongoingtList = new ArrayList<>();
		try {
			ongoingtList = treatmentHistoryService.patienttreatmnetlog(urnno, userId, txnid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ongoingtList;
	}

	@GetMapping(value = "/multipackthroughcaseno")
	@ResponseBody
	private String multipackthroughcaseno(@RequestParam(required = false, value = "caseno") String caseno) {
		String ongoingtList = null;
		try {
			ongoingtList = snoClaimProcessing.multipackthroughcaseno(caseno, "");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ongoingtList;
	}

	@GetMapping(value = "/getFamilytreatmentlist")
	@ResponseBody
	private String getFamilytreatmentlist(@RequestParam(required = false, value = "dateofadmission") String dateofadmission,
			@RequestParam(required = false, value = "memeberid") String memeberid,
			@RequestParam(required = false, value = "urn") String urn) {
		String familylist = null;
		try {
			familylist = snoClaimProcessing.getfamilyTreatementDetails(dateofadmission,memeberid,urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return familylist;
	}
	
	@GetMapping(value = "/getPatienttreatmentlistbyProcedure")
	@ResponseBody
	private String getPatienttreatmentlistbyProcedure(@RequestParam(required = false, value = "procedureCode") String procedureCode,
			@RequestParam(required = false, value = "uidreferencenumber") String uidreferencenumber) {
		String patientlist = null;
		try {
			patientlist = snoClaimProcessing.getPatientTreatementDetails(procedureCode,uidreferencenumber);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return patientlist;
	}
	
	
	
	@GetMapping(value = "/getCPDTriggerdetails")
	@ResponseBody
	private String getCPDTriggerdetails(@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "dateofAdmission") Date dateofAdmission,
			@RequestParam(required = false, value = "dateofdischarge") Date dateofdischarge,
			@RequestParam(required = false, value = "procedurecode") String procedurecode) {
		String triggerlist = null;
		try {
			triggerlist = snoClaimProcessing.getCPDTriggerdetailsData(hospitalcode,dateofAdmission,dateofdischarge,procedurecode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return triggerlist;
	}
}
