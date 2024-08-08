package com.project.bsky.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaPaymentStatusBean;

public interface ClaimReportService {

	List<Object> getActionCountDetails(Long userId, Date fromdate, Date toDate, String eventName, String stateId,
			String districtId, String hospitalId);

	List<Object> getUnprocessedClaim(CPDApproveRequestBean requestBean);

	String getclaimRecievedReportCount(Long userId, String year, String month);

	String getTransactionsCountDetails(Long userId, String years, String months, String days, String eventName);

	String getRecievedCountDetails(Long userId, String years, String months, String days, String eventName);

	Long getSnaApprovedOfCpdApproved(Long userId, Date fromdate, Date toDate);

	String getUnprocessedClaimDetailsById(Integer txnId) throws Exception;

	public Response saveUnprocessedClaimSNADetails(ClaimLogBean logBean) throws Exception;

	List<Object> getclaimcountprogressreportdetails(Long userId, Date fromdate, Date toDate, String eventName,
			String stateId, String districtId, String hospitalId, String groupid);

	String getclaimByUrnAndClamiNo(Long userId, String searchBy, String fieldValue);

	String getSNAWisePaymentStatusReport(SnaPaymentStatusBean bean);

	List<Map<String, Object>> hospitalWiseTreatment(Map<String, Object> request) throws SQLException, ParseException;
}
