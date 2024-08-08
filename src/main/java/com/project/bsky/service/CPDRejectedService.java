package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Cpdlogbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;

public interface CPDRejectedService {

	List<Object> getcpdrejectedlist(CPDApproveRequestBean requestBean);

	String getCpdrejecteddetailsdata(Integer claimid) throws Exception;

	public Response saveRejectedDetails(Cpdlogbean logBean) throws Exception;

	ActionRemark getActionRemarkByid(Long remarkId);

	List<ActionRemark> getAllActionRemarks();

}
