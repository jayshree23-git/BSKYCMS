package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.TreatmentHistoryPerUrnBean;
import com.project.bsky.bean.Treatmenthistorybypackagecode;
import com.project.bsky.model.Txnclaimapplication;


public interface TreatmentHistoryService {

	List<Txnclaimapplication> listTreatmentHistoryData();

	List<Treatmenthistorybypackagecode> gettrtmenthistry(String urnno, String packagecode);

	List<TreatmentHistoryPerUrnBean> getTreatmentHistory(String urnno);
    
	String getTreatmentHistorySna(String urnno,Long userId);
	
	String getOldTreatmentHistorySna(String urnno,Long userId);

	String getOnGoingTreatmenthistorylist(String urnno, Long userId);

	List<Object> patienttreatmnetlog(String urnno, Long userId, Long txnid);
}
