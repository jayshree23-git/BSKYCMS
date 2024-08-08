package com.project.bsky.util;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @Auther: Arabinda Guin
 * @Date: 27-Feb-2024 : 11:41 AM
 */

public class AadhaarVaultUtils {

	private static ResourceBundle bskyAppResourcesBundel = ResourceBundle.getBundle("application");
	
	public static final String AADHAAR_VAULT_URL = bskyAppResourcesBundel.getString("aadhaarVaultEncryption.url");
	public static final String AADHAAR_FROM_REF_URL = bskyAppResourcesBundel.getString("aadhaarFromVault.url");

	
    public enum ServiceType {
        GET_REFERENCE_NUMBER,
        GET_AADHAAR_FROM_REFERENCE
    }

    /**
     * @Purpose: This method is used to process the document
     * @Auther: Arabinda Guin
     * @Date: 27-Feb-2024 : 11:41 AM
     * @throws IOException, JSONException
     */
    @SuppressWarnings("deprecation")
	public static String callAadhaarService(String aadhaarKey, String schemeId, AadhaarVaultUtils.ServiceType serviceType)
            throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String requestBodyString;
        String url;

        if (serviceType == AadhaarVaultUtils.ServiceType.GET_REFERENCE_NUMBER) {
            requestBodyString = "{\"aadhaarNo\":\"" + aadhaarKey + "\",\"schemeId\":\"" + schemeId + "\"}";
            url = AADHAAR_VAULT_URL;
        } else if (serviceType == AadhaarVaultUtils.ServiceType.GET_AADHAAR_FROM_REFERENCE) {
            requestBodyString = "{\"referenceKey\":\"" + aadhaarKey + "\",\"schemeId\":\"" + schemeId + "\"}";
            url = AADHAAR_FROM_REF_URL;
        } else
            throw new IllegalArgumentException("Unsupported service type");

        RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);

        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful() && response.body() != null) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            if (jsonObject.has("aadhaarDetails")) {
                if (serviceType == AadhaarVaultUtils.ServiceType.GET_REFERENCE_NUMBER)
                    return jsonObject.getJSONObject("aadhaarDetails").getString("referenceNo");
                else
                    return jsonObject.getJSONObject("aadhaarDetails").getString("aadhaarNo");
            } else
                throw new JSONException("Invalid response from Aadhaar Vault");
        }
        throw new IOException("Unexpected code " + response);
    }
    //for masking Aadhar number
    public static String maskAadharNumber(String aadharNumber) {
    	String aadhar=null;
        if (aadharNumber == null || "".equalsIgnoreCase(aadharNumber)) {
            return aadhar;
        }
        else if(aadharNumber.length() != 12) {
        	throw new IllegalArgumentException("Invalid Aadhaar number");
        }
        else {
        	StringBuilder maskedAadhar = new StringBuilder(aadharNumber);
            for (int i = 0; i < 8; i++) {
                maskedAadhar.setCharAt(i, 'X');
            }
            aadhar=maskedAadhar.substring(0, 4) + " " + maskedAadhar.substring(4,8)+ " " + maskedAadhar.substring(8);
        }
        return aadhar;
    }
    
    
    
    public String getReferenceNumberUsingAadhaar(String aadhaarNo, String schemeId) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String body = "{\"aadhaarNo\":\"" + aadhaarNo + "\",\"schemeId\":\"" + schemeId + "\"}";
        RequestBody requestBody = okhttp3.RequestBody.create(mediaType, body);

        Request request = new Request.Builder()
                .url(AADHAAR_VAULT_URL)
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() != null) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getJSONObject("aadhaarDetails").getString("referenceNo");
        }else
            throw new IOException("Unexpected code " + response);
    }
}
