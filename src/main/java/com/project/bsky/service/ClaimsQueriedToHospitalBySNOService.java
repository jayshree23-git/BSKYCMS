package com.project.bsky.service;

/**
 * @author preetam.mishra
 *
 */
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.Response;

@Service
public interface ClaimsQueriedToHospitalBySNOService {

	String getQueriedClaimDetails(String claimID);

	void downLoadFileforforSnoQueriedthospital(String fileName, String year, String hCode,
			HttpServletResponse response);

	Response takeActionOnClaimforsnoQuery(String claimID, String hospitalCode, MultipartFile additionaldoc1,
			MultipartFile additionaldoc2, String urnNumber, String dateofAdmission,String ClaimId,String ClaimAmount,String actionby,String dischargeSlip,
			String Additionalslip,String presuergrypic,String postsuergrypic,String intrasurgerypic,String SpecimenPic,String patientpic) throws Exception;

	List<Object> getQueriedClaimsList(String hospitalCode, String fromDate, String toDate,     String package1,
			String packagecode, String URN, String schemeid, String schemecategoryid);
}
