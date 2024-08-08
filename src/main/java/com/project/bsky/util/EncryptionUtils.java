package com.project.bsky.util;

import java.util.Base64;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Project : CMS Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 11/12/2023 - 11:09 AM
 */

public class EncryptionUtils {

	 private EncryptionUtils() {}
	    private static final Random random = new Random();

	    /**
	     * This method is used to encrypt the data
	     *
	     * @Author : Sambit Kumar Pradhan
	     * @param normalPass normal data
	     * @return encrypted data
	     * @Date : 11/12/2023 - 11:09 AM
	     */
	    public static String encrypt(String normalPass) {
	        if (normalPass != null && !normalPass.isEmpty())
	            return makeRandom() + Base64.getEncoder().encodeToString(normalPass.getBytes()) + makeRandom();
	        else
	            return normalPass;
	    }

	    /**
	     * This method is used to make random string
	     *
	     * @Author : Sambit Kumar Pradhan
	     * @return random string
	     * @Date : 11/12/2023 - 11:09 AM
	     */
	    private static String makeRandom() {
	        StringBuilder text = new StringBuilder();
	        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	        for (int i = 0; i < 5; i++) {
	            text.append(possible.charAt(random.nextInt(possible.length())));
	        }
	        return text.toString();
	    }

	    /**
	     * This method is used to decrypt the data
	     *
	     * @Author : Sambit Kumar Pradhan
	     * @param encryptedData encrypted data
	     * @return decrypted data
	     * @Date : 11/12/2023 - 11:09 AM
	     */
	    public static String decryptCode(String encryptedData) {
	        if (encryptedData != null && !encryptedData.isEmpty()) {
	            encryptedData = encryptedData.substring(5, encryptedData.length() - 5);
	            return new String(Base64.getDecoder().decode(encryptedData));
	        } else
	            return null;
	    }

	    /**
	     * This method is used to get decrypted data
	     *
	     * @Author : Sambit Kumar Pradhan
	     * @param encData encrypted data
	     * @return Map decrypted data
	     * @throws JsonProcessingException
	     * @Date : 11/01/2024 - 04:28 PM
	     */
	    public static Map<String, Object> getDecryptedData(String encData) throws JsonProcessingException {
	        return new ObjectMapper()
	                .readValue(
	                        decryptCode(encData),
	                        new TypeReference<Map<String, Object>>(){}
	                );
	    }
}
