package com.project.bsky.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.BtnVisibilityBean;
import com.project.bsky.bean.Response;

public interface SNARequestApproval {

	List<Object> getRequestList(Long userId, Date fromDate, Date toDate, String stateCode, String distCode,
			String hospitalCode, String schemeid, String schemecategoryid);

	String getRequestByDetailId(Integer txnId) throws Exception;

	public Response saveRejectDetails(Long rejectionId, Long transactionDetailsId, Integer statusflag, String claimBy,
			String snaRemark, Integer sysRejStatus, MultipartFile ApproveDoc, String hospitalCode, String dateOfAdm,
			String urn) throws ParseException;

	List<Object> getNonComplianceRequestList(Long userId, Date fromDate, Date toDate, String stateCode, String distCode,
			String hospitalCode, String flag, String schemeid, String schemecategoryid);

	String getNonComplianceRequestByDetailId(Integer txnId) throws Exception;

	public Response saveNonComplianceDetails(Long rejectionId, Long transactionDetailsId, Integer statusflag,
			String claimBy, String snaRemark, Integer sysRejStatus, MultipartFile ApproveDoc, String hospitalCode,
			String dateOfAdm, String urn) throws ParseException;

	List<Object> getNonUploadingListToSna(BtnVisibilityBean requestBean);

	public Response savePermissionDetails(Long snaUserId, Long hosUserId, String hospitalCode, String claimBy,
			Integer nonUploadingStatus, Integer nonComplianceStatus) throws ParseException;
}
