package com.project.bsky.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;

public interface CpdQueryToHospitalService {

	String getQueriedClaimDetails(String claimID);

	Response takeActionOnClaimforCPDQuery(String transactiondetailsid, String actualdateofdiscahrge,
			String hospitalCode, MultipartFile additionaldoc1, MultipartFile additionaldoc2, String urnno,
			String dateofadmissio, String ClaimId, String ClaimAmount, String actionby, String dischrageslip,
			String additionaldocs, String PreSurgerypic, String PostSurgerypic, String intrasurgerypic,
			String specimenremoval, String patientpic) throws Exception;

	List<Object> getAllData(String hospitalCode, String fromDate, String toDate, String package1,
			String packageCodedata, String uRN, String schemeid, String schemecategoryid);
}
