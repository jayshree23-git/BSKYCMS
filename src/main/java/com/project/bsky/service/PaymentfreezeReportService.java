package com.project.bsky.service;

import java.util.Date;
import java.util.List;

public interface PaymentfreezeReportService {

	List<Object> getpaymentfreezereport(Date formdate, Date todate, String stateId, String districtId,
			String hospitalId, String userId);

}
