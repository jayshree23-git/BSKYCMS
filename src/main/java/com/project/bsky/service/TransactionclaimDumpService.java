package com.project.bsky.service;

import java.util.List;

public interface TransactionclaimDumpService {
	List<Object> dischargereport(Long userId,String formdate, String todate,String stateId, String districtId, String hospitalId);
	List<Object> getdischargedetails(String formdate, String todate);


}
