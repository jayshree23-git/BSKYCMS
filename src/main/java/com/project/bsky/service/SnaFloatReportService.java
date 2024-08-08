package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import com.project.bsky.model.TxnclamFloateDetails;

public interface SnaFloatReportService {

	List<TxnclamFloateDetails> getfloatdetails(Long userId,Date fromDate, Date toDate, String floatno);

}
