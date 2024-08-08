package com.project.bsky.service;

import java.util.List;

public interface DischargeReportHospitalService {

//	List<Object> getdischargwdetails(String urn, String groupId,String hospitalcode,String userID);

	List<Object> getdischargwdetails(Long userId, String searchBy, String fieldValue,String groupId);

}
