/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bsky.service.WhatsAppMessagingService;
import com.project.bsky.util.StringUtils;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author santanu.barad
 *
 */
@Service
public class WhatsAppServiceImpl implements WhatsAppMessagingService {
	@Autowired
	private Logger logger;

	@Override
	public Integer testWhatsAppMessage() {
		try {
			return sendRequest("9777389809", "BSKY TEST", "bsky_005");
		} catch (Exception e) {
			logger.error("Exception Occurred in testWhatsAppMessage of WhatsAppMessagingServiceImpl", e);
			throw e;
		}
	}

	public int sendRequest(String phoneNumber, String bodyText, String templateName) {
		logger.info("Inside sendWhatsAppMessage of WhatsAppMessagingServiceImpl.");
		int statusCode = 500;
		OkHttpClient client = new OkHttpClient.Builder().build();

		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("action", StringUtils.WHATSAPP_ACTION).addFormDataPart("phone", "+91" + phoneNumber)
				.addFormDataPart("template_name", templateName).addFormDataPart("body_text", bodyText).build();

		logger.info("Before Request: {}", body);

		Request request = new Request.Builder().url(StringUtils.WHATSAPP_SENDER_URL).method("POST", body).build();
		logger.info("Request: {}", request);

		try (Response response = client.newCall(request).execute()) {
			logger.info("After Request: {}", response);
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				Map<?, ?> responseMap = new ObjectMapper().readValue(responseBody, Map.class);
				logger.info("Response: {}", responseBody);
				statusCode = Integer.parseInt(responseMap.get("status").toString()) == 1 ? 200 : 500;
//				whatsappMessageLog.setResponseMessage(responseMap.get("msg").toString());
			} else {
				logger.info("HTTP Error Code: {}", response.code());
//				handleErrorResponse(response);
			}
		} catch (IOException e) {
//			handleException(e);
			logger.error("Exception Occurred in sendWhatsAppMessage of WhatsAppMessagingServiceImpl", e);
		}
		return statusCode;
	}
}
