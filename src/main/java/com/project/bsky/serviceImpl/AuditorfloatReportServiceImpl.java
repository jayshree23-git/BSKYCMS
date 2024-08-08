package com.project.bsky.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.repository.TxnclaimFloatdetailsrepository;
import com.project.bsky.service.AuditorfloatReportService;

@Service
public class AuditorfloatReportServiceImpl implements AuditorfloatReportService {
	
	@Autowired
	private TxnclaimFloatdetailsrepository txnclaimFloatdetailsrepository;
	
	@Autowired
	private Logger logger;

	@Override
	public List<TxnclamFloateDetails> listReportOfAuditor(String fromdate, String todate, String floatno) {
		List<TxnclamFloateDetails> listOfReport=null;
		try {
			Date fromDate1= new SimpleDateFormat("dd-MMM-yyyy").parse(fromdate);
			Date todate1= new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			if(floatno!="") {
				List<TxnclamFloateDetails> listOfReport1=txnclaimFloatdetailsrepository.findBycreateonBetween(fromDate1, todate1);
				for(TxnclamFloateDetails x:listOfReport1) {
					listOfReport=new ArrayList<TxnclamFloateDetails>();
					if(x.getFloateno().equals(floatno)) {
						x.setScreateon(x.getCreateon().toString());
						listOfReport.add(x);
					}
				}
			}else {
				listOfReport=txnclaimFloatdetailsrepository.findBycreateonBetween(fromDate1, todate1);
				for(TxnclamFloateDetails x:listOfReport) {
					 x.setScreateon(x.getCreateon().toString());
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listOfReport;
	}
}
