package com.project.bsky.service;

import java.util.List;

import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.model.Txnclaimapplication;

public interface FoFloatReportService {



	List<TxnclamFloateDetails> getFilterAllDataForFo(String floateno, String formdate, String todate);

}
