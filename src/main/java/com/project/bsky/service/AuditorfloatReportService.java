package com.project.bsky.service;

import java.util.List;

import com.project.bsky.model.TxnclamFloateDetails;

public interface AuditorfloatReportService {

	List<TxnclamFloateDetails> listReportOfAuditor(String fromdate, String todate, String floatno);

}
