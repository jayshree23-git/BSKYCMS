package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import com.project.bsky.bean.FoBean;
import com.project.bsky.bean.Response;

public interface FinancialOfficerService {

	List<Object> getfinaciladetails(Date fromDate, Date toDate, String finacialno);

	List<Object> getUSerDetailsDAta(String id);

//	Response insertdata(String remarks, String value,String userid);

	Response insertdata(String remarks, long value, long userid, long amount, long floatid, String floatno,String flag)throws Exception;
	Response updatedvalue(List<FoBean> foBeanList, String approvedAmount, String userid, String remarks);

}
