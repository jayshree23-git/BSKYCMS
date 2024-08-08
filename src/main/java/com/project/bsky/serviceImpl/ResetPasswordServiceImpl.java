package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.DistrictMaster;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsPassResetLog;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.UserDetailsPassResetLogRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.ResetPasswordService;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsPassResetLogRepository userDetailsPassResetLogRepository;

	@Autowired
	private Environment env;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DistrictMasterRepository districtMasterRepository;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> listData(Long userId) throws Exception {
		List<Object> listData = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GETLIST_FOR_PASSWORD_RESET")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String,Object> user=new HashedMap<String, Object>();
				user.put("userId", rs.getString(1));
				user.put("userName", rs.getString(2));
				user.put("fullname", rs.getString(3));
				user.put("groupname", rs.getString(4));
				user.put("status", rs.getString(5));
				user.put("phone", rs.getString(6));
				listData.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return listData;
	}

	@Override
	public Response resetpassword(Long userId, Long createdBy) {
		Response response = new Response();
		Calendar calendar = Calendar.getInstance();
		try {
			String password = passwordEncoder.encode(env.getProperty("configKey"));
			UserDetails userDetails = userDetailsRepo.findById(userId).get();
			userDetails.setPassword(password);
			userDetails.setPasswordUpdatedOn(calendar.getTime());
			userDetailsRepo.save(userDetails);
			UserDetailsPassResetLog userDetailsPassResetLog = new UserDetailsPassResetLog();
			UserDetails userDetails1 = userDetailsRepo.findById(createdBy).get();
			userDetailsPassResetLog.setCreatedBy(userDetails1);
			userDetailsPassResetLog.setCreatedOn(calendar.getTime());
			userDetailsPassResetLog.setUserId(userDetails);
			userDetailsPassResetLogRepository.save(userDetailsPassResetLog);
			response.setStatus("Success");
			response.setMessage("Password reset to " + env.getProperty("configKey"));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Some Error Happen");
		}
		return response;
	}

	@Override
	public List<UserDetailsPassResetLog> listview(Long userId) throws Exception {
		List<UserDetailsPassResetLog> listView = new ArrayList<>();
		try {
			UserDetails userDetails = userDetailsRepo.findById(userId).get();
			listView = userDetailsPassResetLogRepository.findByuserId(userDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}
		return listView;
	}

	@Override
	public List<UserDetails> AllUsers(String userName) {
		List<UserDetails> listData = null;
		try {
			if (userName != null && userName != "") {
				listData = userDetailsRepo.getByUserName(userName);
			} else {
				listData = userDetailsRepo.findAll();
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listData;
	}

	@Override
	public List<DistrictMaster> getDistrictListByStateCode(String stateCode) {
		List<DistrictMaster> districtDetailsByStateId = districtMasterRepository.getDistrictDetailsByStateId(stateCode);
		return districtDetailsByStateId;
	}

	@Override
	public String getHospitalMasterData(String stateCode, String distCode) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet snoDetailsObj = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_hospital_master_data")
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_dist_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_state_code", stateCode);
			storedProcedureQuery.setParameter("p_dist_code", distCode);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");

			while (snoDetailsObj.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("STATENAME", snoDetailsObj.getString(1));
				jsonObject.put("DISTRICTNAME", snoDetailsObj.getString(2));
				jsonObject.put("HOSPITALCODE", snoDetailsObj.getString(3));
				jsonObject.put("HOSPITALNAME", snoDetailsObj.getString(4));
				jsonObject.put("ASSIGNEDSNA", snoDetailsObj.getString(5));
				jsonObject.put("UESRNAME", snoDetailsObj.getString(6));
				jsonObject.put("MOBILENO", snoDetailsObj.getString(7));
				jsonObject.put("EMAILID", snoDetailsObj.getString(8));
				jsonObject.put("ADDRESS_INFO", snoDetailsObj.getString(9));
				jsonArray.put(jsonObject);

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return jsonArray.toString();
	}
}