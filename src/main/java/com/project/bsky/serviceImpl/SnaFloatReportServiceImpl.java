package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.SnaFloatReportBean;
import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.repository.TxnclaimFloatdetailsrepository;
import com.project.bsky.service.SnaFloatReportService;

@Service
public class SnaFloatReportServiceImpl implements SnaFloatReportService{
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private TxnclaimFloatdetailsrepository txnclaimreportrepo;
	@Override
	public List<TxnclamFloateDetails> getfloatdetails(Long userId,Date fromDate, Date toDate, String floatno) {

		List<TxnclamFloateDetails> floatedetailslist=new ArrayList<TxnclamFloateDetails>();;
		List<TxnclamFloateDetails> floatedetailslist1=null;
		//			Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
		//			Date todate1=new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
		////System.out.println(floatno);
		if(floatno!="") {
			floatedetailslist1=txnclaimreportrepo.findBycreateonBetween(fromDate,toDate);
			for(TxnclamFloateDetails x:floatedetailslist1) {
//				floatedetailslist=new ArrayList<TxnclamFloateDetails>();
				if(x.getFloateno().equals(floatno) && x.getCreateby().getUserId().equals(userId)) {
					////System.out.println("inn");
					x.setScreateon(x.getCreateon().toString());						
					floatedetailslist.add(x);
				}					
			}		
		}else {
			floatedetailslist1=txnclaimreportrepo.findBycreateonBetween(fromDate,toDate);	
			for(TxnclamFloateDetails x:floatedetailslist1) {
				floatedetailslist=new ArrayList<TxnclamFloateDetails>();
				////System.out.println(x.getCreateby().getUserId());
				////System.out.println(userId);
				if(x.getCreateby().getUserId().equals(userId))
				{
					////System.out.println("inn");
					x.setScreateon(x.getCreateon().toString());
					floatedetailslist.add(x);
				}
				
			}
		}		
		
		return floatedetailslist;
	}
		
		}
