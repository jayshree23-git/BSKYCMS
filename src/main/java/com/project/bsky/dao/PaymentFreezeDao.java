package com.project.bsky.dao;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.FloatRequestBean;
import com.project.bsky.bean.Response;

/**
 * @author santanu.barad
 *
 */
public interface PaymentFreezeDao {

	List<Object> getpaymentfreezedata(CPDApproveRequestBean requestBean);

	public Response savePaymentfreeze(Long userId, Date fromDate, Date toDate, String stateCodeList,
			String distCodeList, String hospitalCodeList, Long snoUserId, Double snaAmount, String floatFile,
			String floatNumber, Integer searchtype, String schemecategoryid) throws Exception;

	public List<String> getFloatNumber(Long snaUserId, String month, String year);

	List<Object> getFloatDetails(String floatNumber);

	List<Object> getFloatDetailsByHospital(String floatNumber, String hospitalCode);

	public Response remarkUpdate(ClaimLogBean bean);

	public Response verifyFloat(String floatNumber, Long actionBy, String remark, String filePath);

	List<Object> getSnaApprovedList(CPDApproveRequestBean requestBean);

	public Response freezePayment(Long userId, String claimList);

	public Response forwardToSNA(String floatNumber, Long actionBy, String remark, String filePath1);

	Response forwardfloat(String header, Long userId, String remarks, Long pendingAt);

	Integer saveFloatLog(String floatNumber, Long actionBy, String remark, String filePath1);

	public Response forwardDraftFloat(String floatNumber, Long snoUserId, String otherCertificate,
			String snaCertificate, String meCertificate, String description) throws Exception;

}
