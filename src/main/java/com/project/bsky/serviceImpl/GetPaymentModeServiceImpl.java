/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.PaymentMode;
import com.project.bsky.repository.GetPaymentModeRepository;
import com.project.bsky.service.GetPaymentModeService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class GetPaymentModeServiceImpl implements GetPaymentModeService {
	
	@Autowired
	private GetPaymentModeRepository  getPaymentModeRepository;

	@Autowired
	private Logger logger;
	
	@Override
	public List<PaymentMode>  getDetails() {
		JSONArray jsonArray = new JSONArray();
		List<PaymentMode> getListPayment=null;
		try {
		 getListPayment = getPaymentModeRepository.getDetails();
		for(PaymentMode paymentMode: getListPayment) {
			JSONObject json = new JSONObject();
			json.put("paymentModeId",paymentMode.getPaymentModeId());
			json.put("paymentType",paymentMode.getPaymentType());
			jsonArray.put(json);
		}
		}catch(JSONException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getListPayment;
	}
	

}
