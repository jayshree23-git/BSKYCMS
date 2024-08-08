/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import org.json.JSONArray;

import com.project.bsky.model.PaymentMode;

/**
 * @author priyanka.singh
 *
 */
public interface GetPaymentModeService {

	List<PaymentMode> getDetails();

//	JSONArray getPayment();

}
