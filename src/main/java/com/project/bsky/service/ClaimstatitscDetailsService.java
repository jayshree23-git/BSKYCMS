package com.project.bsky.service;

import java.util.List;

public interface ClaimstatitscDetailsService {

	List<Object> getclaimStaticStiDetailsReport(String fromDate, String toDate, String stateid, String districtvalue,
			String hospitalcode, String eventName);

}
