package com.project.bsky.serviceImpl;

import java.text.ParseException;
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
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.FoFloatReportService;

@Service
public class FoFloatReportServiceImpl implements FoFloatReportService {
	@Autowired
	private UserDetailsRepository userDetailsRepo;
	
	@Autowired
	private TxnclaimFloatdetailsrepository txnclaimFloatDetailsRepository;
	
	@Autowired
	private Logger logger;
	

	@Override
	public List<TxnclamFloateDetails> getFilterAllDataForFo(String floateno, String formdate, String todate) {
		List<TxnclamFloateDetails> getfilterDatafo=null;
		////System.out.println("Data come from frontend+++++ "+floateno+","+formdate+","+todate);
		try {
			Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(formdate);
			Date todate1=new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
			if(floateno!="") {
			////System.out.println("//////"+floateno);
			List<TxnclamFloateDetails> getfilterDatafo1=txnclaimFloatDetailsRepository.findBycreateonBetween(fromDate1,todate1);	
				for(TxnclamFloateDetails temp: getfilterDatafo1) {
				getfilterDatafo=new ArrayList<TxnclamFloateDetails>();
				if(temp.getFloateno().equals(floateno)){
				temp.setScreateon(temp.getCreateon().toString());						
				getfilterDatafo.add(temp);
				}	
			}
			  }else
				 {
			     getfilterDatafo=txnclaimFloatDetailsRepository.findBycreateonBetween(fromDate1,todate1);
			     for(TxnclamFloateDetails temp: getfilterDatafo) {
				 temp.setScreateon(temp.getCreateon().toString());
			     }
		}
		}catch(ParseException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getfilterDatafo;
	}
}
