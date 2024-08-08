package com.project.bsky.service;

public interface CpdNamewiseCountreportService {
	
	String details(Long userId, String formdate, String todate);

	String list(String formdate, String todate);

}
