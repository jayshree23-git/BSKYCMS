/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

/**
 * @author priyanka.singh
 *
 */
public interface PaymentFreezesReportService {

	List<Object> getpaymentfreezdetails(String fromdate, String todate, Long snoUserId, String hospitalCode);

}
