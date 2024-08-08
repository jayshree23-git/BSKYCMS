package com.project.bsky.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Response;

public interface ClaimraiseDetailsService {
	
	List<Object> getUSerDetailsDAta(String check);

	byte[] downLoadFileClaim(String fileName, String year, String hCode);

	Response saveClaimRaiseHospital(Long refractionid, String hospitalCode, Long updatedby, String urnNumber,
			BigInteger invoiceNumber, String dateofAdmission, String packageCode, MultipartFile treatmentDetailsSlip,
			MultipartFile hospitalBill, MultipartFile presurgry, MultipartFile postsurgery, String casenumber,
			String billnumber, MultipartFile intasurgery, MultipartFile specimansurgery, MultipartFile patientpic,String tmsdischargedocumnet,String claimbilldate, Long amountValue, String remarks)
			throws Exception;

	List<Object> getclaimrasiedata(String hospitalCode, String package1, String packageCode, String uRN,
			String fromDate, String toDate,String caseno,String schemeid,String schemecategoryid);

	List<Object> getpackdata(String packageName);
	
	void getUnSavedClaimData();
	
	List<Object> getClaimList(CPDApproveRequestBean requestBean);

	Response executeProcess(String urn, Integer processId);
	
}
