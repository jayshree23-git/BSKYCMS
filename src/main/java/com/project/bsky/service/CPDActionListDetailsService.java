/**
 * 
 */
package com.project.bsky.service;

import com.project.bsky.bean.UrnWiseDataBean;

/**
 * @author priyanka.singh
 *
 */
public interface CPDActionListDetailsService {

	UrnWiseDataBean getCpdActionDetails(String urnNo, Long transId);
	String getDraftActionHistoryClaimNo(Long claimId) throws Exception;

}
