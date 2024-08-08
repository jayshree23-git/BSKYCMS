package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.PrimaryLink;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserMenuMapping;
import com.project.bsky.model.UserMenuMappingLog;
import com.project.bsky.repository.GlobalLinkRepository;
import com.project.bsky.repository.PrimaryLinkRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.repository.UserMenuMappingLogRepository;
import com.project.bsky.repository.UserMenuMappingRepository;
import com.project.bsky.service.AdminConsoleService;

/**
 * @author ronauk
 *
 */

@Service
public class AdminConsoleServiceImpl implements AdminConsoleService {

	@Autowired
	private Environment environment;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private UserMenuMappingRepository userRepo;

	@Autowired
	private PrimaryLinkRepository primaryRepo;

	@Autowired
	private GlobalLinkRepository globalRepo;

	@Autowired
	private UserDetailsRepository userDetailsRepo;

	@Autowired
	private UserMenuMappingLogRepository usermappinglogrepo;

	@Autowired
	private Logger logger;

	@Override
	public JSONArray getGlobalLinks(int userId) {
		JSONArray globalLinks = new JSONArray();
		try {
			List<JSONObject> primaryLinks = getPrimaryLinksFromGlobalLink(userId);
			List<GlobalLink> list = globalRepo.allactiveglobaldata();
			for (GlobalLink gl : list) {
				JSONObject json = new JSONObject();
				json.put("globalLinkId", gl.getGlobalLinkId());
				json.put("globalLinkName", gl.getGlobalLinkName());
				JSONArray plinks = new JSONArray();
				for (JSONObject js : primaryLinks) {
					if (js.getInt("globalLinkId") == gl.getGlobalLinkId().intValue()) {
						plinks.put(js);
					}
				}
				json.put("pLinks", plinks);
				if (plinks.length() > 0) {
					json.put("checkstat", true);
				} else {
					json.put("checkstat", false);
				}
				json.put("status", gl.getBitStatus());
				globalLinks.put(json);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return globalLinks;
	}

	@Override
	public List<JSONObject> getPrimaryLinksFromGlobalLink(int userId) {
		List<JSONObject> primaryLinks = new ArrayList<JSONObject>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_admin_console_data")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_global_link", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.setParameter("p_global_link", null);
			storedProcedureQuery.setParameter("p_flag", 0);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");

			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("userId", rs.getInt("userId"));
				json.put("user_primaryLinkId", rs.getInt("u_pId"));
				if (rs.getInt("stat") == 0) {
					json.put("status", true);
				} else {
					json.put("status", false);
				}
				json.put("primaryLinkId", rs.getInt("p_pId"));
				json.put("primaryLinkName", rs.getString("p_name"));
				json.put("globalLinkId", rs.getInt("gid"));
				primaryLinks.add(json);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return primaryLinks;
	}

	@Override
	public Response setPrimaryLinks(int userId, int createdby, List<PrimaryLinkBean> list) throws Exception {
		Response response = new Response();
		try {
			usermappinglog(userId, createdby);
			userRepo.setAllInactive(userId, createdby);
			for (PrimaryLinkBean p : list) {
				int count = userRepo.checkUserPrimaryLinkMapping(userId, Long.valueOf(p.getPrimaryLinkId()));
				if (count > 0) {
					userRepo.setStatusActive(userId, createdby, Long.valueOf(p.getPrimaryLinkId()));
				} else {
					UserMenuMapping user = new UserMenuMapping();					
					user.setUserId(userId);
					PrimaryLink pl = new PrimaryLink();
					pl = primaryRepo.findById(Long.valueOf(p.getPrimaryLinkId())).get();
					user.setPrimaryLink(pl);
					user.setBitStatus(0);
					user.setCreatedBy(createdby);
					user.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					user.setGlobal_Link_Id(p.getGlobalLinkId().intValue());
					userRepo.save(user);
				}
			}
			response.setMessage("User mapped successfully");
			response.setStatus("Success");
		} catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	private void usermappinglog(int userId, int createdby) {
		try {
			List<UserMenuMappingLog> loglist = new ArrayList<UserMenuMappingLog>();
			List<UserMenuMapping> usermapping = userRepo.getuserspecifcprimarylinkforlog(userId);
			for (UserMenuMapping user : usermapping) {
				UserMenuMappingLog menumappinglog = new UserMenuMappingLog();
				menumappinglog.setMappingid(user.getUserMappingId());
				menumappinglog.setMappingstatus(user.getBitStatus());
				menumappinglog.setUserid(Long.valueOf(userId));
				menumappinglog.setGloballink(user.getGlobal_Link_Id());
				menumappinglog.setPrimarylink(user.getPrimaryLink().getPrimaryLinkId());
				menumappinglog.setUpdatedby(Long.valueOf(createdby));
				menumappinglog.setUpdatedon(Calendar.getInstance().getTime());
				menumappinglog.setStatus(0);
				loglist.add(menumappinglog);
			}
			usermappinglogrepo.saveAll(loglist);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Response setPrimaryLinksForGroup(int groupId, int createdby, List<PrimaryLinkBean> list) {
		Response response = new Response();
		try {
			List<UserDetails> users = userDetailsRepo.findAllActiveUserForFilterByGroup(groupId);
			for (UserDetails u : users) {
				int userId = u.getUserId().intValue();
				userRepo.setAllInactive(userId, createdby);
				for (PrimaryLinkBean p : list) {
					int count = userRepo.checkUserPrimaryLinkMapping(userId, Long.valueOf(p.getPrimaryLinkId()));
					if (count > 0) {
						if (userRepo.getUserStatus(userId, Long.valueOf(p.getPrimaryLinkId())) == 1) {
							userRepo.setStatusActive(userId, createdby, Long.valueOf(p.getPrimaryLinkId()));
						}
					} else {
						UserMenuMapping user = new UserMenuMapping();
						Integer mappingId = userRepo.getMaxMappingId();
						if (mappingId == null) {
							mappingId = 0;
						}
						user.setUserMappingId(mappingId + 1);
						user.setUserId(userId);
						PrimaryLink pl = new PrimaryLink();
						pl = primaryRepo.findById(Long.valueOf(p.getPrimaryLinkId())).get();
						user.setPrimaryLink(pl);
						user.setBitStatus(0);
						user.setCreatedBy(createdby);
						user.setCreatedOn(new Timestamp(System.currentTimeMillis()));
						user.setGlobal_Link_Id(p.getGlobalLinkId().intValue());
						userRepo.save(user);
					}
				}
			}
			response.setMessage("Group mapped successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happened");
			response.setStatus("Failed");
			return response;
		}
	}

	@Override
	public List<PrimaryLinkBean> getPrimaryLinksFromUserId(int userId) {
		List<PrimaryLinkBean> list = new ArrayList<PrimaryLinkBean>();
		try {
			List<UserMenuMapping> userList = userRepo.getPrimaryLinksFromUserId(userId);
			for (UserMenuMapping u : userList) {
				PrimaryLinkBean p = new PrimaryLinkBean();
				p.setPrimaryLinkId(u.getPrimaryLink().getPrimaryLinkId().intValue());
				p.setGlobalLinkId(Long.valueOf(u.getGlobal_Link_Id()));
				list.add(p);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response copyPrimaryLinksForCPD(int userId, int createdby) {
		Response response = null;
		List<PrimaryLinkBean> list = new ArrayList<PrimaryLinkBean>();
		try {
			List<Long> cpdlist = userDetailsRepo.getAllActiveUserIdForFilterByGroup(3);
			for (Long cpd : cpdlist) {
				list = getPrimaryLinksFromUserId(cpd.intValue());
				if (list != null && list.size() != 0) {
					break;
				}
			}
			response = setPrimaryLinks(userId, createdby, list);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public Response copyPrimaryLinksForHosp(int userId, int createdby) {
		Response response = null;
		List<PrimaryLinkBean> list = new ArrayList<PrimaryLinkBean>();
		try {
			List<Long> hosplist = userDetailsRepo.getAllActiveUserIdForFilterByGroup(5);
			for (Long hosp : hosplist) {
				list = getPrimaryLinksFromUserId(hosp.intValue());
				if (list != null && list.size() != 0) {
					break;
				}
			}
			response = setPrimaryLinks(userId, createdby, list);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public Response copyPrimaryLinksForUser(int userId, int createdby, int groupId) {
		Response response = null;
		List<PrimaryLinkBean> list = new ArrayList<PrimaryLinkBean>();
		try {
			List<Long> userlist = userDetailsRepo.getAllActiveUserIdForFilterByGroup(groupId);
			for (Long user : userlist) {
				list = getPrimaryLinksFromUserId(user.intValue());
				if (list != null && list.size() != 0) {
					break;
				}
			}
			response = setPrimaryLinks(userId, createdby, list);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public Integer checkPrimaryLinksFromUserId(int userId) {
		return userRepo.checkPrimaryLinksFromUserId(userId);
	}

	@Override
	public List<UserDetails> getLockedUserList() {
		return userDetailsRepository.getLockedUserList();
	}

	@Override
	public Map<String, Object> unlockUserByUserId(int userId) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			UserDetails userDetails = userDetailsRepository.findByUserId(userId);
			if (userDetails != null) {
				userDetails.setAttemptedStatus(0);
				userDetails.setPassword(bCryptPasswordEncoder.encode(environment.getProperty("configKey")));
				userDetails = userDetailsRepository.save(userDetails);
				if (userDetails.getAttemptedStatus() == 0) {
					response.put("status", HttpStatus.OK.value());
					response.put("message", "User unlocked successfully");
					response.put("data", userDetailsRepository.getLockedUserList());
				}
			}
		} catch (Exception e) {
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("message", "Some error happened");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

}
