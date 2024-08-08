package com.project.bsky.service;

import java.text.ParseException;
import java.util.List;

import com.project.bsky.bean.HospitalAuthTaggingBean;
import com.project.bsky.model.HospitalAuthTagging;
import com.project.bsky.model.TxnClaimActionLog;

public interface HospitalAuthTaggingService {

	List<Object> gethospauthrtydetailsreport(Long userId, String hospital, String fromdate, String todate,
			Integer searchtype);

	TxnClaimActionLog getviewremark(Long claim, Integer type);

	List<Object> getclaimrasiedataForAuthority(String fromDate, String toDate, String type, String hospitalCode,
			Long userId) throws ParseException;

	List<Object> getclaimQuryByCPDDataForAuthority(String fromDate, String toDate, String type, String hospitalCode,
			Long userId) throws ParseException;

	List<Object> getclaimQuryBySNADataForAuthority(String fromDate, String toDate, String type, String hospitalCode,
			Long userId) throws ParseException;

}
