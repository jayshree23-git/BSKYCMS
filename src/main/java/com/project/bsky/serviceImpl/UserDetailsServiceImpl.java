package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bsky.bean.IntGrvUserBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserDetailsBean;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.OtpConfigLog;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.repository.Bankdetailsrepository;
import com.project.bsky.repository.CPDConfigurationRepository;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.SNOConfigurationRepository;
import com.project.bsky.repository.UserConfigLogRepository;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.HospitalLogSaveService;
import com.project.bsky.service.UserDetailsCpdLogService;
import com.project.bsky.service.UserDetailsService;
import com.project.bsky.service.UserProfileLogSaveService;
import com.project.bsky.util.JwtUtil;

@SuppressWarnings("unchecked")
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private GroupTypeRepository groupRepo;

	@Autowired
	private UserDetailsCpdReposiitory cpdRepo;

	@Autowired
	private UserDetailsProfileRepository profileRepo;

	@Autowired
	private HospitalInformationRepository hospRepo;

	@Autowired
	private CPDConfigurationRepository cpdConfRepo;

	@Autowired
	private SNOConfigurationRepository snoConfRepo;

	@Autowired
	private Bankdetailsrepository bankRepo;

	@Autowired
	private UserDetailsCpdLogService cpdLog;

	@Autowired
	private UserProfileLogSaveService userLog;

	@Autowired
	private HospitalLogSaveService hospLog;

	@Autowired
	private Logger logger;

	@Autowired
	private UserConfigLogRepository otpLogRepository;

	@Autowired
	private JwtUtil jwtUtil;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Integer checkUserDetailsByuserName(String userName) {
		Integer checkUserDetailsuserName = null;
		try {
			checkUserDetailsuserName = userDetailsRepository.checkusername(userName);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkUserDetailsuserName;
	}

	@Override
	public Integer checkUserDetailsByuserNameForHosp(String userName) {
		Integer checkUserDetailsuserName = null;
		try {
			checkUserDetailsuserName = userDetailsRepository.checkusernameforhosp(userName);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkUserDetailsuserName;
	}

	@Override
	public Integer saveUserDetails(String fullName, String userName, String mobileNo, String emailId, String groupId,
			String createon) {
		UserDetails userdetails = new UserDetails();
		String password = passwordEncoder.encode("Admin@123");
		userdetails.setUserName(userName);
		userdetails.setCompanyCode(null);
		userdetails.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
		userdetails.setEmail(emailId);
		GroupTypeDetails gr = new GroupTypeDetails();
		gr = groupRepo.findById(Integer.parseInt(groupId)).get();
		userdetails.setGroupId(gr);
		userdetails.setCreatedUserName(createon);
		userdetails.setStatus(0);
		userdetails.setPassword(password);
		if (mobileNo != null && mobileNo != "" && !mobileNo.equalsIgnoreCase("")
				&& !mobileNo.equalsIgnoreCase("null")) {
			userdetails.setPhone(Long.parseLong(mobileNo));
		} else {
			userdetails.setPhone(null);
		}
		userdetails.setTPACode(null);
		userdetails.setFullname(fullName);
		try {
			userDetailsRepository.save(userdetails);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public List<UserDetails> findAll() {
		return userDetailsRepository.findAll(Sort.by(Sort.Direction.ASC, "fullname"));
	}

	@Override
	public List<UserDetails> getDetailsFilteredByGroup(String groupId) {
		return userDetailsRepository.findAllUserForFilterByGroup(Integer.parseInt(groupId));
	}

	@Override
	public UserDetailsBean getbyid(Long id) {
		UserDetailsBean bean = new UserDetailsBean();
		UserDetails user = userDetailsRepository.findById(id).get();
		bean.setUserDetails(user);
		return bean;
	}

	@Override
	public Integer updateUserDetails(String fullName, String userName, String mobileNo, String emailId, String groupId,
			String createon, String userid) {
		UserDetails userdetails = userDetailsRepository.findById(Long.parseLong(userid)).get();
		userdetails.setUserName(userName);
		userdetails.setCompanyCode(null);
		userdetails.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
		userdetails.setEmail(emailId);
		GroupTypeDetails gr = new GroupTypeDetails();
		gr = groupRepo.findById(Integer.parseInt(groupId)).get();
		userdetails.setGroupId(gr);
		userdetails.setCreatedUserName(createon);
		if (mobileNo != null && mobileNo != "" && !mobileNo.equalsIgnoreCase("")
				&& !mobileNo.equalsIgnoreCase("null")) {
			userdetails.setPhone(Long.parseLong(mobileNo));
		} else {
			userdetails.setPhone(null);
		}
		userdetails.setTPACode(null);
		userdetails.setFullname(fullName);
		try {
			userDetailsRepository.save(userdetails);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public List<UserDetails> findAllUsers(String userId) {
		List<UserDetails> list = new ArrayList<UserDetails>();
		try {
			if (userId == null || userId == "" || userId.equalsIgnoreCase("undefined")) {
				list = userDetailsRepository.findAll(Sort.by(Sort.Direction.ASC, "fullname"));
			} else {
				Long id = Long.parseLong(userId);
				list.add(userDetailsRepository.findById(id).get());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response statusChange(Long userId, Integer status, Long updatedBy) {
		Response response = new Response();
		try {
			Integer groupId = userDetailsRepository.findById(userId).get().getGroupId().getTypeId();
			if (status == 0) {
				userDetailsRepository.inactivate(userId);
				if (groupId == 3) {
					cpdLog.saveCpdLog(userId, updatedBy.intValue());
					cpdRepo.inactivate(userId, updatedBy.intValue());
					bankRepo.inactivate(userId, updatedBy.intValue());
				} else if (groupId == 5) {
					hospLog.saveHospLog(userId, updatedBy.intValue());
					hospRepo.inactivate(userId, updatedBy);
				} else {
					userLog.saveProfileLog(userId, updatedBy);
					profileRepo.inactivate(userId, updatedBy);
				}
				response.setStatus("Success");
				response.setMessage("User InActivated Successfully");
			} else if (status == 1) {
				userDetailsRepository.activate(userId);
				if (groupId == 3) {
					cpdLog.saveCpdLog(userId, updatedBy.intValue());
					cpdRepo.activate(userId, updatedBy.intValue());
					bankRepo.activate(userId, updatedBy.intValue());
				} else if (groupId == 5) {
					hospLog.saveHospLog(userId, updatedBy.intValue());
					hospRepo.activate(userId, updatedBy);
				} else {
					userLog.saveProfileLog(userId, updatedBy);
					profileRepo.activate(userId, updatedBy);
				}
				response.setStatus("Success");
				response.setMessage("User Activated Successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Failed to change Status");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Some Error Happened");
		}
		return response;
	}

	@Override
	public Response checkStatus(Long userId) {
		Response response = new Response();
		try {
			UserDetails userDetails = userDetailsRepository.findById(userId).get();
			Integer groupId = userDetails.getGroupId().getTypeId();
			if (groupId == 3) {
				UserDetailsCpd cpd = cpdRepo.findByuserid(userId);
				if (cpd != null) {
					Integer bskyUserId = cpd.getBskyUserId();
					int count = cpdConfRepo.checkCpdNameDulicacy(bskyUserId);
					if (count > 0) {
						response.setStatus("Info");
						response.setMessage("CPD restricted to Hospital, cannot be made Inactive!!");
					}
				}
			} else if (groupId == 4) {
				Integer count = snoConfRepo.checkSnoConf(userId.intValue());
				if (count > 0) {
					response.setStatus("Info");
					response.setMessage("SNA tagged to Hospital, cannot be made Inactive!!");
				}
			} else if (groupId == 5) {
				HospitalInformation hosp = hospRepo.findByUserId(userId);
				if (hosp != null) {
					String hospCode = hosp.getHospitalCode();
					Integer count = snoConfRepo.checkSnoConfFromHospCode(hospCode);
					if (count > 0) {
						response.setStatus("Info");
						response.setMessage("Hospital tagged to SNA, cannot be made Inactive!!");
					}
				}
			} else if (groupId == 6) {
				Integer count = hospRepo.checkDCConfig(userId);
				if (count > 0) {
					response.setStatus("Info");
					response.setMessage("DC assigned to Hospital, cannot be made Inactive!!");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Some Error Happened");
		}
		return response;
	}

	@Override
	public Integer updateUserLoginOtp(Map<String, Object> map) {
		List<IntGrvUserBean> user = (List<IntGrvUserBean>) map.get("selectedUser");
		ObjectMapper mapper = new ObjectMapper(); // or inject it as a dependency
		List<IntGrvUserBean> pojos = mapper.convertValue(user, new TypeReference<List<IntGrvUserBean>>() {
		});
		Integer returnValue = null;
		OtpConfigLog otpLog = null;
		Long currentUser = jwtUtil.getCurrentUser();
		try {
			for (IntGrvUserBean updatedEntity : pojos) {
				otpLog = new OtpConfigLog();
				UserDetails existingEntity = userDetailsRepository.findById(Long.parseLong(updatedEntity.getUserid()))
						.orElse(null);
				if (existingEntity != null) {
					existingEntity.setIsOtpAllowed(updatedEntity.getIsOtp());
					otpLog.setUserId(Long.parseLong(updatedEntity.getUserid()));
					otpLog.setIsOtp(updatedEntity.getIsOtp());
					otpLog.setStatusFlag(0l);
					otpLog.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					otpLog.setCreatedBy(currentUser.intValue());
					userDetailsRepository.save(existingEntity);
					try {
						otpLogRepository.save(otpLog);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			returnValue = 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Updation failed");
		}
		return returnValue;
	}

	@Override
	public List<OtpConfigLog> userLoginOtpLog(IntGrvUserBean configLog) {
		return otpLogRepository.findAllByUserIdAndStatusFlag(Long.parseLong(configLog.getUserid()), 0l);
	}

	@Override
	public Map<String, Object> getuserlistformobilenoupdate(Long userId, Integer grp) throws Exception {
		Map<String,Object> response=new HashMap<>();
		List<Object> objlist=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_USERDETAILS_EDIT_LIST")
					.registerStoredProcedureParameter("P_GROUPID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_GROUPID", grp);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG");
			while (rs.next()) {
				Map<String,Object> response1=new HashMap<>();
				response1.put("userId",rs.getString(1));
				response1.put("userName",rs.getString(2));
				response1.put("fullName",rs.getString(3));
				response1.put("groupTypeName",rs.getString(4));
				response1.put("mobileNo",rs.getString(5));
				response1.put("emailId",rs.getString(6));
				response1.put("stateName",rs.getString(7));
				response1.put("districtname",rs.getString(8));
				response1.put("address",rs.getString(9));
				objlist.add(response1);
			}
			response.put("status",HttpStatus.OK.value());
			response.put("message","Success");
			response.put("data",objlist);
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}
		return response;
	}

	@Override
	public Map<String, Object> updatemobilenoofuser(Long userid, String mobile, String email, String rqst,
			String description,Long createby) throws Exception {
		Map<String,Object> response=new HashMap<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_USERDETAILS_PROFILE_UPDATE")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMAILID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MOBILE_NUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REQUESTED_THROUGH", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);
			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_EMAILID", email);			
			storedProcedureQuery.setParameter("P_MOBILE_NUMBER", mobile);
			storedProcedureQuery.setParameter("P_REQUESTED_THROUGH", rqst);
			storedProcedureQuery.setParameter("P_DESCRIPTION", description);
			storedProcedureQuery.setParameter("P_CREATEDBY", createby);
			storedProcedureQuery.execute();

			Integer rs = (Integer) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if(rs==1) {
				response.put("status",HttpStatus.OK.value());
				response.put("message","Success");
			}else {
				response.put("status",HttpStatus.BAD_REQUEST.value());
				response.put("message","Error");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public Map<String, Object> getmobilenoupdateloglist(Long userId) throws Exception {
		Map<String,Object> response=new HashMap<>();
		List<Object> objlist=new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_USERDETAILS_PROFILE_UPDATE")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMAILID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MOBILE_NUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REQUESTED_THROUGH", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 2);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String,Object> response1=new HashMap<>();
				response1.put("userId",rs.getString(1));
				response1.put("userName",rs.getString(2));
				response1.put("fullName",rs.getString(3));
				response1.put("groupTypeName",rs.getString(4));
				response1.put("mobileNo",rs.getString(5));
				response1.put("emailId",rs.getString(6));
				response1.put("stateName",rs.getString(7));
				response1.put("districtname",rs.getString(8));
				response1.put("address",rs.getString(9));
				response1.put("rqstthrough",rs.getString(10));
				response1.put("description",rs.getString(11));
				response1.put("updatedby",rs.getString(12));
				response1.put("updatedon",rs.getString(13));
				objlist.add(response1);
			}
			response.put("status",HttpStatus.OK.value());
			response.put("message","Success");
			response.put("data",objlist);
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}
		return response;
	}

}
