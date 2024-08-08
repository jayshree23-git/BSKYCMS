/**
 * 
 */
package com.project.bsky.service;

import com.project.bsky.bean.Response;

/**
 * @author hrusikesh.mohanty
 *
 */
public interface Cardbalanceupdateservice {

	String getCardBalanceDetails(String urn);

	Response refundAmount(Long userid, String memberId, String urn, Double balanceAmount, Long claimId, String remarks);

}
