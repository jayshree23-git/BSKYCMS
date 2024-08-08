package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import com.project.bsky.bean.HospitalPaidClaimBean;

public interface HospitalPaidClaimService {

	public List<HospitalPaidClaimBean> gethospitalwisesummaryreportcountresult(Date formdate, Date todate,
			String userId, Integer searchtype);

	public List<Object> gethospitaldetailsinnerpage(Date formdate, Date todate, Integer searchtype, String userId);

}
