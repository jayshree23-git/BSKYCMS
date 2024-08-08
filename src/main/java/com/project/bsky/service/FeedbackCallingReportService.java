package com.project.bsky.service;

import java.util.List;

public interface FeedbackCallingReportService {

	List<Object> getFeedbackCallingReport(String userId, String formDate, String toDate, String action,
			String hospitalCode);

}
