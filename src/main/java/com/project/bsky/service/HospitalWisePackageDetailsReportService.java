package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import com.project.bsky.bean.HospitalWisePackageDetailsReportBean;

public interface HospitalWisePackageDetailsReportService {

	public List<HospitalWisePackageDetailsReportBean> list(Long userid, Date fromdate, Date todate, String hospitalcode,
			String statecode, String districtcode);

}
