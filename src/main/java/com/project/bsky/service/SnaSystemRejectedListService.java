package com.project.bsky.service;

import java.text.ParseException;
import java.util.List;

import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SystemRejQueryCpdBean;

public interface SnaSystemRejectedListService {

	List<ClaimsQueriedToHospitalBySNOBean> getSnarejectedlistdata(String hospitalcoderejected, String fromDate, String toDate, String package1,
			String packageCodedata, String uRN, String schemeid, String schemecategoryid);
	public Response saveRejectRequestSNA(SystemRejQueryCpdBean rejBean)  throws ParseException;

}
