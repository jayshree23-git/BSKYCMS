package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import com.project.bsky.model.HospitalInformation;

public interface HospitalWiseClaimReportService {

	List<HospitalInformation> getDetails();

	List<Object> search(Long userId, Date fromDate, Date toDate, String hospitalId);

	List<Object> details(Long userId, Date fromDate, Date toDate, String hospitalId, String serchby);



}
