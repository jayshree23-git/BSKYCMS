package com.project.bsky.service;

import java.util.List;
import java.util.Map;

public interface OldandnewclaimDetailsService {

	Map<String, Object> detailsoldclaimandnewclaimforview(String urnnumber, String claimid, String selctedvalue,
			String claimnumber, String transactiondetailsid, String authorizedcode, String hospitalcode);

}
