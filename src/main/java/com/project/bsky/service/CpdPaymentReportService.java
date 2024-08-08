package com.project.bsky.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.CpdPaymentReportBean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CpdPaymentReportModel;
import com.project.bsky.model.UserDetails;

public interface CpdPaymentReportService {

	CpdPaymentReportModel getNote();

	public List<CpdPaymentReportBean> details(Long userid, Long actiontype, String year, String month,
			String hospitalcode, String statecode, String districtcode, Long flag);

	public List<UserDetails> getCPDUserList();

	public Map<String, Object> getCPDPaymentCalculationList(Date fromDate, Date toDate, Long userId)
			throws SQLException;

	public Map<String, Object> getCPDPaymentDetailsUserId(Date fromDate, Date toDate, Long userId, Integer actionCode)
			throws SQLException;

	public List<Object> getmoratlityDetailsdata(Long userId, Date fromdate, Date todate, String stateCodeList,
			String districtCodeList, String hospitalCodeList);

	public List<Object> getUrnWiseBlockDateData(Long distrctCode, Long userid, Date fromDate, Date toDate);

	public List<Object> getUrnWiseGpDateData(Long districtcode, Long blockcode, Date fromDate, Date todate,
			Long userid);

	public List<Object> getUrnWisevillageData(Long districtcode, Long blockcode, Long gpcode, Date fromDate,
			Date todate, Long userid);

	public List<Object> getPatienttreatedinoutsideodishareportforblockData(Long districtcode, Date fromDate,
			Date todate, Long userId);

	public List<Object> getPatienttreatedinoutsideodishareportforPanchayatkData(Long districtcode, Long blockcode,
			Date fromDate, Date todate, Long userId);

	public List<Object> getPatienttreatedinoutsideodishareportforVillageData(Long districtcode, Long blockcode,
			Date fromDate, Date todate, Long userId, Long gpcode);

	public List<Object> getgetCPDProcessingPaymentDetailsData(Long userId, Long year, Long month, String hospitalcode,
			String statecode, String districtcode, String status);

	public List<Object> getpackagedetailsDetails();

	public List<Object> getpackagedetailsRecordList(String packlist, String userid, String statedata,
			String districtdata, String hospitaldata, String hospitaltype);

	public List<Object> getpackagedetailsForImpantCalculationList(String userid, String procedurecode, String statedata,
			String districtdata, String hospitaldata, String hospitaltype);

	public String getCPDPaymentList(Long month, Long year);

	public List<Object> getpackagedetailsForHedCalculationList();

	public Response updatePostPayment(PostPaymentRequest requestBean) throws Exception;

	public String cpdPostPaymentUpdationView(Long cpdUserId, Long year);
}
