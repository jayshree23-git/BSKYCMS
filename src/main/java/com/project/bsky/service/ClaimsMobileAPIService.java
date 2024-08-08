/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.MobileAuthResponse;
import com.project.bsky.bean.MobileUserAuthClaimHistoryBean;
import com.project.bsky.model.MobileAPiRejectionReasonsResponseBean;
import com.project.bsky.model.MobileAuthRequest;
import com.project.bsky.model.MobileServiceGetClaimList;
import com.project.bsky.model.MobileUserAuthClaimHistory;
import com.project.bsky.model.MobileUserChangePassword;
import com.project.bsky.model.MobileUserForgotPassword;
import com.project.bsky.model.MobileUserSubmitRemarks;

/**
 * @author preetam.mishra
 *
 */
@Service
public interface ClaimsMobileAPIService {

	/**
	 * @param apiKey
	 * @return
	 */
	String getToken(String apiKey);

	/**
	 * @param userName
	 * @param mobileNumber
	 * @return
	 */
	String forgotPasswordgetOtp(MobileUserForgotPassword mobileUserForgotPassword);

	/**
	 * @param mobileAuthRequest
	 * @return
	 * @throws Exception
	 */
	MobileAuthResponse requestLogin(MobileAuthRequest mobileAuthRequest) throws Exception;

	/**
	 * @param mobileUserChangePassword
	 * @return
	 */
	String changePassword(MobileUserChangePassword mobileUserChangePassword);

	/**
	 * @param mobileServiceGetClaimList
	 * @return
	 */
	List<Map<String, Object>> getClaimsList(MobileServiceGetClaimList mobileServiceGetClaimList);

	/**
	 * @param mobileUserSubmitRemarks
	 * @return
	 */
	String changePassword(MobileUserSubmitRemarks mobileUserSubmitRemarks);

	/**
	 * @param mobileUserAuthClaimHistory
	 * @return
	 */
	List<MobileUserAuthClaimHistoryBean> getAuthClaimHistory(MobileUserAuthClaimHistory mobileUserAuthClaimHistory);

	/**
	 * @return
	 */
	List<MobileAPiRejectionReasonsResponseBean> getRejectionReasons();

}
