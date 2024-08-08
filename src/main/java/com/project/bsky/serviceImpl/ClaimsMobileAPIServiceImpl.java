/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.MobileAuthResponse;
import com.project.bsky.bean.MobileServicePendingClaimBean;
import com.project.bsky.bean.MobileUserAuthClaimHistoryBean;
import com.project.bsky.model.MobileAPiRejectionReasonsResponseBean;
import com.project.bsky.model.MobileAuthRequest;
import com.project.bsky.model.MobileServiceGetClaimList;
import com.project.bsky.model.MobileUserAuthClaimHistory;
import com.project.bsky.model.MobileUserChangePassword;
import com.project.bsky.model.MobileUserForgotPassword;
import com.project.bsky.model.MobileUserModel;
import com.project.bsky.model.MobileUserSubmitRemarks;
import com.project.bsky.repository.MobileUserRepository;
import com.project.bsky.service.ClaimsMobileAPIService;
import com.project.bsky.util.JwtUtil;

/**
 * @author preetam.mishra
 *
 */
@Service
public class ClaimsMobileAPIServiceImpl implements ClaimsMobileAPIService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager = null;

	@Autowired
	private MobileUserRepository repository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public String getToken(String apikey) {
		String token = "";
		try {
			if (!apikey.equals("")) {
				// UUID uuid = UUID.randomUUID();
				token = jwtUtil.generateToken(apikey);
				////System.out.println("token:" + token);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return token;
	}

	@Override
	public String forgotPasswordgetOtp(MobileUserForgotPassword mobileUserForgotPassword) {
		Random random = new Random();
		String otp = "";
		try {
			if (!mobileUserForgotPassword.getUserName().isEmpty()
					&& !mobileUserForgotPassword.getMobileNumber().isEmpty()) {
				otp = String.format("%04d", random.nextInt(10000));
				////System.out.println("otp:" + otp);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return otp;
	}

	@Override
	public MobileAuthResponse requestLogin(MobileAuthRequest mobileAuthRequest) throws Exception {
		Authentication auth = null;
		MobileAuthResponse response = new MobileAuthResponse();
		MobileUserModel user = null;
		try {
			auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					mobileAuthRequest.getUserName(), mobileAuthRequest.getPassWord()));
			////System.out.println(auth);
		} catch (Exception e) {
			throw new Exception("Inavalid username/password");
		}
		if (auth != null) {
			user = repository.findByUserName(mobileAuthRequest.getUserName());
			response.setUserId(user.getUserId());
			response.setUserName(user.getUserName());
			response.setEmail(user.getEmail());
			response.setToken("Bearer " + jwtUtil.generateTokenNoTimeLimit(user.getUserName()));
		}
		return response;
	}

	@Override
	public String changePassword(MobileUserChangePassword mobileUserChangePassword) {
		String response = "";
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("Usp_Claim_Mob_Chnpasswrd")
					.registerStoredProcedureParameter("p_userId", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mobileNo", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_oldPassword", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_newPassword", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_confirmPassword", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_userId", mobileUserChangePassword.getUserId());
			storedProcedureQuery.setParameter("p_mobileNo", mobileUserChangePassword.getMobileNo());
			storedProcedureQuery.setParameter("p_oldPassword", mobileUserChangePassword.getOldPassword());
			storedProcedureQuery.setParameter("p_newPassword", mobileUserChangePassword.getNewPassword());
			storedProcedureQuery.setParameter("p_confirmPassword", mobileUserChangePassword.getConfirmPassword());
			storedProcedureQuery.execute();
			if (!mobileUserChangePassword.getUserId().isEmpty() && !mobileUserChangePassword.getMobileNo().isEmpty()
					&& !mobileUserChangePassword.getOldPassword().isEmpty()
					&& !mobileUserChangePassword.getNewPassword().isEmpty()
					&& !mobileUserChangePassword.getConfirmPassword().isEmpty()
					&& mobileUserChangePassword.getNewPassword().equals(mobileUserChangePassword.getConfirmPassword())
					&& !mobileUserChangePassword.getOldPassword().equals(mobileUserChangePassword.getNewPassword())
			// &&
			// !mobileUserChangePassword.getNewPassword().equals(mobileUserChangePassword.getOldPassword())
			) {
				response = (String) storedProcedureQuery.getOutputParameterValue("p_msgout");
				////System.out.println(response);
			} else {
				throw new Exception(
						"Inavalid or blank username or password or mobileNo,new password and confirm password should be same.");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	@Override
	public List<Map<String, Object>> getClaimsList(MobileServiceGetClaimList mobileServiceGetClaimList) {
		ResultSet claimListObj=null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> appclaimsList = new HashMap<String, Object>();
		Map<String, Object> penclaimsList = new HashMap<String, Object>();
		Map<String, Object> rejclaimsList = new HashMap<String, Object>();
		Map<String, Object> queclaimsList = new HashMap<String, Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("Usp_Claim_Mob_Claimlist")
					.registerStoredProcedureParameter("p_userId", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_userId", mobileServiceGetClaimList.getUserName());
			storedProcedureQuery.execute();
			 claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msgout");

			while (claimListObj.next()) {
				String claimStatus = claimListObj.getString(5);
				// if (claimStatus.equals("0"))
				// {
				MobileServicePendingClaimBean resBean = new MobileServicePendingClaimBean();
				resBean.setPatientName(claimListObj.getString(1));
				resBean.setUrn(claimListObj.getString(2));
				resBean.setClaimDate(new SimpleDateFormat("dd MMM yyyy")
						.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimListObj.getString(3))));
				resBean.setClaimedAmount(claimListObj.getString(4).toString());
				resBean.setClaimStatus(claimListObj.getString(5).toString());
				if (claimStatus.equals("0")) {
					penclaimsList.put("Pending Claim:", resBean);

				} else if (claimStatus.equals("1")) {
					appclaimsList.put("Approved Claim:", resBean);

				}
//				} else if (claimStatus.equals("1")) {
//					MobileServiceApprovedClaimBean resBean = new MobileServiceApprovedClaimBean();
//					resBean.setPatientName(claimListObj.getString(1));
//					resBean.setUrn(claimListObj.getString(2));
//					resBean.setClaimDate(new SimpleDateFormat("dd MMM yyyy")
//							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimListObj.getString(3))));
//					resBean.setClaimedAmount(claimListObj.getString(4).toString());
//					resBean.setClaimStatus(claimListObj.getString(5).toString());
//					appclaimsList.put("Approved Claim:", resBean);
//					list.add(appclaimsList);
//
//				} else if (claimStatus.equals("2")) {
//					MobileServiceRejectedClaimBean resBean = new MobileServiceRejectedClaimBean();
//					resBean.setPatientName(claimListObj.getString(1));
//					resBean.setUrn(claimListObj.getString(2));
//					resBean.setClaimDate(new SimpleDateFormat("dd MMM yyyy")
//							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimListObj.getString(3))));
//					resBean.setClaimedAmount(claimListObj.getString(4).toString());
//					resBean.setClaimStatus(claimListObj.getString(5).toString());
//					rejclaimsList.put("Rejected Claim:", resBean);
//					list.add(rejclaimsList);
//
//				} else {
//					MobileServiceQueriedClaimBean resBean = new MobileServiceQueriedClaimBean();
//					resBean.setPatientName(claimListObj.getString(1));
//					resBean.setUrn(claimListObj.getString(2));
//					resBean.setClaimDate(new SimpleDateFormat("dd MMM yyyy")
//							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimListObj.getString(3))));
//					resBean.setClaimedAmount(claimListObj.getString(4).toString());
//					resBean.setClaimStatus(claimListObj.getString(5).toString());
//					queclaimsList.put("Queried Claim:", resBean);
//					list.add(queclaimsList);
//
				list.add(penclaimsList);
				list.add(appclaimsList);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		

		finally {
			try {
				if (claimListObj != null)
					claimListObj.close();
			} 
			catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
				
			}
		}
		
		
		////System.out.println("Data:" + list.toString());
		return list;
	}

	@Override
	public String changePassword(MobileUserSubmitRemarks mobileUserSubmitRemarks) {
		String response = "";
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("Usp_Claim_mob_Submit_Rmk")
					.registerStoredProcedureParameter("p_userId", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_claimid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_remark", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_approvedAmount", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_actionid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", String.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("p_userId", mobileUserSubmitRemarks.getUserName());
			storedProcedureQuery.setParameter("p_urn", mobileUserSubmitRemarks.getUrn());
			storedProcedureQuery.setParameter("p_claimid", mobileUserSubmitRemarks.getClaimId());
			storedProcedureQuery.setParameter("p_remark", mobileUserSubmitRemarks.getRemarks());
			storedProcedureQuery.setParameter("p_approvedAmount", mobileUserSubmitRemarks.getApprovedAmount());
			storedProcedureQuery.setParameter("p_actionid", mobileUserSubmitRemarks.getActionId());
			storedProcedureQuery.execute();
			if (!mobileUserSubmitRemarks.getUserName().isEmpty() && !mobileUserSubmitRemarks.getUrn().isEmpty()
					&& !mobileUserSubmitRemarks.getClaimId().isEmpty()
					&& !mobileUserSubmitRemarks.getRemarks().isEmpty()
					&& mobileUserSubmitRemarks.getApprovedAmount() >= 100
					&& !mobileUserSubmitRemarks.getActionId().isEmpty()
					&& mobileUserSubmitRemarks.getActionId().equals("0")
					&& mobileUserSubmitRemarks.getRemarks().length() > 7) {
				response = (String) storedProcedureQuery.getOutputParameterValue("p_msgout");
				////System.out.println(response);
			} else {
				throw new Exception(
						"Parameters passed cant be empty,remarks must be of more than 7 characters in length,"
								+ "approved amount must be greater or equal to 100");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	@Override
	public List<MobileUserAuthClaimHistoryBean> getAuthClaimHistory(
			MobileUserAuthClaimHistory mobileUserAuthClaimHistory) {
		ResultSet claimListObj=null;
		List<MobileUserAuthClaimHistoryBean> list = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("Usp_Claim_Mob_PreAuth_Logs")
					.registerStoredProcedureParameter("p_userId", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_userId", mobileUserAuthClaimHistory.getUserName());
			storedProcedureQuery.setParameter("p_urn", mobileUserAuthClaimHistory.getUrn());
			storedProcedureQuery.execute();
			if (!mobileUserAuthClaimHistory.getUserName().isEmpty() && !mobileUserAuthClaimHistory.getUrn().isEmpty()) {
				 claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msgout");
				while (claimListObj.next()) {
					MobileUserAuthClaimHistoryBean resBean = new MobileUserAuthClaimHistoryBean();

					resBean.setActionDate((new SimpleDateFormat("dd MMM yyyy")
							.format(new SimpleDateFormat("ddMMyyyy").parse((String) claimListObj.getString(1)))));
					resBean.setRemarks(claimListObj.getString(2));
					resBean.setStatus(claimListObj.getString(3));
					list.add(resBean);
					////System.out.println(resBean);
				}
			} else {
				throw new Exception("Userid and URN must be valid and cant be empty");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			try {
				if (claimListObj != null)
					claimListObj.close();
			} 
			catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
				
			}
		}
	
		return list;
	}

	public List<MobileAPiRejectionReasonsResponseBean> getRejectionReasons() {
		ResultSet claimListObj=null;
		List<MobileAPiRejectionReasonsResponseBean> dataList = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("Usp_Mob_Claim_Rej_Reasons")
					.registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action", "A");
			storedProcedureQuery.execute();
			 claimListObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msgout");
			while (claimListObj.next()) {
				MobileAPiRejectionReasonsResponseBean resBean = new MobileAPiRejectionReasonsResponseBean();
				resBean.setId(claimListObj.getLong(1));
				resBean.setReason(claimListObj.getString(2));
				dataList.add(resBean);
				////System.out.println(resBean);

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			try {
				if (claimListObj != null)
					claimListObj.close();
			} 
			catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
				
			}
		}
	
		
		return dataList;
	}
}
