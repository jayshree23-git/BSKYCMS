package com.project.bsky.service;

import java.util.List;

public interface CPDMappingReportService {

	List<Object> getHospitalByStateCode(String stateCode);

	List<Object> search(String stateCode, String hospitalCode);

	List<Object> getsnamappingreport(String stateCode, String distCode, String hospitalCode, Integer snastatus) throws Exception;

}
