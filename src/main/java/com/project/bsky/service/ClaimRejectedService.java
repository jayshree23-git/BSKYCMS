package com.project.bsky.service;

import java.text.ParseException;
import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SystemRejectedBean;

public interface ClaimRejectedService {

	List<Object> getrejectedlistdata(String hospitalcoderejected, String fromDate, String toDate, String package1,
			String packagecode, String uRN ,String schemeid,String schemecategoryid);

	public Response saveRejectRequest(SystemRejectedBean rejBean) throws ParseException;

	String getByDetailId(Integer txnId) throws Exception;
}
