package com.project.bsky.service;

import java.util.Date;
import java.util.List;
//import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.FloatExcelBean;
import com.project.bsky.bean.OldClaimPymntBean;
import com.project.bsky.bean.OldFloatBean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.Response;

/**
 * @author ronauk
 *
 */
public interface OldClaimFloatService {
	
	List<Object> getFloatReport(OldFloatBean requestBean);
	
	void downLoadFile(String fileCode, String userId, HttpServletResponse response);
	
	Integer saveFloatReport(MultipartFile pdf, FloatExcelBean bean);
	
	Integer saveReport(FloatExcelBean requestBean);
	
	String getGeneratedReports(OldFloatBean requestBean);
	
	String getPaymentFreezeReport(OldFloatBean requestBean);

	Integer paymentFreezeAction(Long id, Date fdate, Date tdate, String stateId, String districtId, String hospitalId);

	Integer savePaymentFreezeRecord(MultipartFile file, Long id, Date fdate, Date tdate, String stateId,
			String districtId, String hospitalId);
	
	Integer saveReport(String file, Long id, Date fdate, Date tdate, String stateId,
			String districtId, String hospitalId);

	String paymentFreezeView(OldFloatBean requestBean);
	
	void downloadPfzFile(String fileCode, String userId, HttpServletResponse response);
	
	List<OldClaimPymntBean> getPostPaymentList(OldFloatBean requestBean);
	
	Response updatePostPayment(PostPaymentRequest requestBean);
	
	String paymentFreezeClaimDetails(Date fromDate, Date toDate, String stateId, String districtId, String hospitalId);

	String pendingmortality(String userid, Date fromdate, Date todate, String statecode, String districtcode,
			String hospitalcode)throws Exception;

}
