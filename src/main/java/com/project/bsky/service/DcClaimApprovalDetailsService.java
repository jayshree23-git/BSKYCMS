package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.DCDashboardBean;
import com.project.bsky.bean.Enrollmentapprovalbean;
import com.project.bsky.bean.GODashboardBean;
import com.project.bsky.bean.Response;

public interface DcClaimApprovalDetailsService {
	
	public String getDcClaimList(Long userId) throws Exception;

	String getDcClaimListById(Integer txnId) throws Exception;

	Integer dcClaimSubmit(Long userId, Long txnId, String URN, String hospitalCode, Long claimId, Double claimAmount,
			String additionalDoc, String additionalDoc1, String additionalDoc2, String dischargeSlip, String preSurgery,
			String postSurgery, String intraSurgery, String specimenRemoval, String patientPhoto,
			MultipartFile investigation1Doc, MultipartFile investigation2Doc, String dateOfAdmission, String remarks);

	String getDCInvestigationCount(DCDashboardBean bean);

	String getDCOverRideCount(DCDashboardBean bean);

	String getDCGrievanceCount(DCDashboardBean bean);

	String getDCGrievanceResolveCount(DCDashboardBean bean);

	String getDCGrievanceModeCount(DCDashboardBean bean);

	String getCCECountReport(DCDashboardBean bean);

	String getEmpCountReport(DCDashboardBean bean);

	String getGOCountReport(GODashboardBean bean);

	String getGOMediumCount(GODashboardBean bean);

	String getGODistrictWiseCount(GODashboardBean bean);

	String getGOCCEWiseCount(GODashboardBean bean);

	String getGOCCEDistrictWiseCount(GODashboardBean bean);

	public List<Object> getlist(Date fromDate, Date toDate, Long userId, String urn);

	public String getenrollmentdetailsthroughid(Date fromDate, Date toDate, Long userId, Long depgrid,
			String acknowledgementno) throws Exception;

	public Response getaction(Enrollmentapprovalbean requestBean) throws Exception;

	public String getactiontakenhistorydetails(Date fromDate, Date toDate, Long userId, Long depregid,
			String acknowledgementnumber) throws Exception;

	String getGOCountReportDetails(GODashboardBean bean);

	public List<Object> getcomplylist(Date fromDate, Date toDate, String urn, Long userId);

	void downLoadFile(String fileName, String year, String hCode, String Statecode, String districtcode,
			String blockcode, String enrollmentfolder, HttpServletResponse response);

	List<Object> getactiontakenhistorylist(Long enggid, String acknowledgementno);

	public List<Object> gethospitalenrollmentlistactiontakenDetails(String urn, Date fromDate, Date toDate, Long userId,
			String username, Long searchdata, String state, String dist, String hospital);
}
