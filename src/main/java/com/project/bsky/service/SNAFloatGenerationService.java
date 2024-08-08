package com.project.bsky.service;

import java.util.Date;

public interface SNAFloatGenerationService {

	String viewSnaFloatList(Date fromDate, Date toDate, String finacialno,Integer userId);

	String viewIndivisualFloatDetails(String floatNo);

}
