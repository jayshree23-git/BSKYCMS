package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Claimsummarydetails;
import com.project.bsky.bean.PackageScheme;
import com.project.bsky.bean.Searchhos;
import com.project.bsky.model.User;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.repository.UserRepository;
import com.project.bsky.service.MasterService;

/**
 * @author ronauk
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class MasterServiceImpl implements MasterService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private DistrictMasterRepository districtrepo;

	@Autowired
	private UserDetailsProfileRepository userdeailsRepo;

	@Autowired
	private Logger logger;

	@Override
	public String getStateMasterDetails() {
		JSONArray statelist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "STATE");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject state = new JSONObject();
				state.put("stateId", rs.getInt(1));
				state.put("stateCode", rs.getString(2));
				state.put("stateName", rs.getString(3));
				statelist.put(state);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return statelist.toString();
	}

	@Override
	public String getDistrictDetailsByStateId(String stateCode) {
		JSONArray districtlist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", stateCode);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "DISTRICT");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject district = new JSONObject();
				district.put("DistrictId", rs.getInt(1));
				district.put("statecode", rs.getString(2));
				district.put("districtcode", rs.getString(3));
				district.put("districtname", rs.getString(4));
				districtlist.put(district);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return districtlist.toString();
	}

	@Override
	public String getSNODetails() {
		JSONArray snolist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "SNO");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject snoUser = new JSONObject();
				snoUser.put("bskyUserId", rs.getInt(1));
				snoUser.put("uesrName", rs.getString(2));
				snoUser.put("userId", rs.getInt(3));
				snoUser.put("fullName", rs.getString(4));
				snolist.put(snoUser);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return snolist.toString();
	}

	@Override
	public String getSNAList() {
		JSONArray snolist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "SNA");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject snoUser = new JSONObject();
				snoUser.put("bskyUserId", rs.getInt(1));
				snoUser.put("uesrName", rs.getString(2));
				snoUser.put("userId", rs.getInt(3));
				snoUser.put("fullName", rs.getString(4));
				snolist.put(snoUser);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return snolist.toString();
	}

	@Override
	public String getHospitalByDistrictId(String districtId, String stateId) {
		ResultSet rs = null;
		JSONArray hospitallist = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", stateId);
			storedProcedureQuery.setParameter("p_districtCode", districtId);
			storedProcedureQuery.setParameter("p_flag", "HOSPITAL");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject hospital = new JSONObject();
				hospital.put("hospitalId", rs.getInt(1));
				hospital.put("hospitalName", rs.getString(2));
				hospital.put("hospitalCode", rs.getString(3));
				hospital.put("hospName", rs.getString(4));
				hospitallist.put(hospital);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return hospitallist.toString();
	}

	@Override
	public String getCPDDetails() {
		ResultSet rs = null;
		JSONArray cpdlist = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "CPD");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject cpdUser = new JSONObject();
				cpdUser.put("bskyUserId", rs.getInt(1));
				cpdUser.put("userid", rs.getLong(2));
				cpdUser.put("fullName", rs.getString(3));
				cpdlist.put(cpdUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return cpdlist.toString();
	}

	@Override
	public String getDistrictDetailsByStateAndDistrictCode(String stateCode, String distCode) {
		ResultSet rs = null;
		JSONObject district = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", stateCode);
			storedProcedureQuery.setParameter("p_districtCode", distCode);
			storedProcedureQuery.setParameter("p_flag", "DISTRICTBYCODE");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				district = new JSONObject();
				district.put("DistrictId", rs.getInt(1));
				district.put("statecode", rs.getString(2));
				district.put("districtcode", rs.getString(3));
				district.put("districtname", rs.getString(4));
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return district.toString();
	}

	@Override
	public String getUserDetails() {
		ResultSet rs = null;
		JSONArray userlist = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "User");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject user = new JSONObject();
				user.put("userId", rs.getLong(1));
				user.put("UserName", rs.getString(2));
				user.put("fullname", rs.getString(3));
				userlist.put(user);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return userlist.toString();
	}

	@Override
	public String getDCDetails() {
		ResultSet rs = null;
		JSONArray dclist = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "DC");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject dcUser = new JSONObject();
				dcUser.put("bskyUserId", rs.getInt(1));
				dcUser.put("userName", rs.getString(2));
				dcUser.put("userId", rs.getLong(3));
				dcUser.put("fullName", rs.getString(4));
				dclist.put(dcUser);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return dclist.toString();
	}

	@Override
	public JSONArray getGroups() {
		JSONArray groupList = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "Group");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject group = new JSONObject();
				group.put("typeId", rs.getInt(1));
				group.put("groupTypeName", rs.getString(2));
				group.put("status", rs.getInt(3));
				groupList.put(group);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return groupList;
	}

	@Override
	public String getSNAEXDetails() {
		JSONArray snolist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "SNAEX");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject snoUser = new JSONObject();
				snoUser.put("bskyUserId", rs.getInt(1));
				snoUser.put("uesrName", rs.getString(2));
				snoUser.put("userId", rs.getInt(3));
				snoUser.put("fullName", rs.getString(4));
				snolist.put(snoUser);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return snolist.toString();
	}

	@Override
	public String getCDMODetails() {
		ResultSet rs = null;
		JSONArray cdmolist = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "CDMO");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject CDMOUser = new JSONObject();
				CDMOUser.put("bskyUserId", rs.getInt(1));
				CDMOUser.put("userName", rs.getString(2));
				CDMOUser.put("userId", rs.getLong(3));
				CDMOUser.put("fullName", rs.getString(4));
				cdmolist.put(CDMOUser);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return cdmolist.toString();
	}

	@Override
	public String getMonths() {
		JSONArray monthList = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "Month");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject month = new JSONObject();
				month.put("index", rs.getInt(1));
				month.put("monthId", rs.getString(2));
				month.put("monthName", rs.getString(3));
				monthList.put(month);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return monthList.toString();
	}

	@Override
	public String getYears() {
		List<String> yearList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "Year");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				yearList.add(rs.getString(1));
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return yearList.toString();
	}

	@Override
	public List<User> getDClist() {
		List<User> user = userrepo.getdclist();
		return user;
	}

	@Override
	public String getHospCatMaster() {
		JSONArray categorylist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "HCM");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject category = new JSONObject();
				category.put("categoryId", rs.getInt(1));
				category.put("categoryName", rs.getString(2));
				categorylist.put(category);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return categorylist.toString();
	}

	@Override
	public List<Map<String, String>> getDistrictByStateCode(List<String> statecode) throws Exception {
		List<Map<String, String>> districtMaps = new ArrayList();
		try {
			Query query = this.entityManager.createNativeQuery("SELECT\n" + "    S.STATECODE,\n" + "    S.STATENAME,\n"
					+ "    D.DISTRICTCODE,\n" + "    D.DISTRICTNAME\n" + "FROM STATE S\n"
					+ "INNER JOIN DISTRICT D ON D.STATECODE = S.STATECODE\n"
					+ "WHERE S.STATECODE IN (?1) order by D.DISTRICTNAME");
			query.setParameter(1, statecode);
			List<Object[]> results = query.getResultList();
			results.forEach(result -> {
				Map<String, String> districtMap = new LinkedHashMap<>();
				districtMap.put("StateCode", (String) result[0]);
				districtMap.put("StateName", (String) result[1]);
				districtMap.put("DistrictCode", (String) result[2]);
				districtMap.put("DistrictName", (String) result[3]);
				districtMaps.add(districtMap);
			});

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return districtMaps;
	}

	@Override
	public List<Map<String, String>> getHospitalByDistrictCode(List<String> Statecodeforhospitallist,
			List<String> districtcode) throws Exception {
		List<Map<String, String>> hosoitaltMaps = new ArrayList();
		try {
			Query query = this.entityManager.createNativeQuery(
					"SELECT S.STATECODE, S.STATENAME, D.DISTRICTCODE, D.DISTRICTNAME, H.HOSPITAL_NAME, H.hospital_code "
							+ "FROM HOSPITAL_INFO H " + "LEFT JOIN STATE S ON H.STATE_CODE = S.STATECODE "
							+ "LEFT JOIN DISTRICT D ON S.STATECODE = D.STATECODE AND H.DISTRICT_CODE = D.DISTRICTCODE "
							+ "WHERE H.STATE_CODE IN (?1) " + "AND H.DISTRICT_CODE IN (?2) " + "AND STATUS_FLAG=0 "
							+ "ORDER BY H.HOSPITAL_NAME");
			query.setParameter(1, Statecodeforhospitallist);
			query.setParameter(2, districtcode);
			List<Object[]> results = query.getResultList();
			results.forEach(result -> {
				Map<String, String> hospitalMap = new LinkedHashMap<>();
				hospitalMap.put("StateCode", (String) result[0]);
				hospitalMap.put("StateName", (String) result[1]);
				hospitalMap.put("DistrictCode", (String) result[2]);
				hospitalMap.put("DistrictName", (String) result[3]);
				hospitalMap.put("HospitalName", (String) result[4]);
				hospitalMap.put("HospitalCode", (String) result[5]);
				hosoitaltMaps.add(hospitalMap);

			});
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return hosoitaltMaps;
	}

	@Override
	public List<Map<String, String>> getdcwiseHospitaldetailsformulticheckbox(List<String> Statecodeforhospitallist,
			List<String> districtcode, Long userid) throws Exception {
		List<Map<String, String>> hosoitaltMaps = new ArrayList();
		try {
			Query query = this.entityManager.createNativeQuery(
					"SELECT S.STATECODE, S.STATENAME, D.DISTRICTCODE, D.DISTRICTNAME, H.HOSPITAL_NAME, H.hospital_code "
							+ "FROM HOSPITAL_INFO H " + "LEFT JOIN STATE S ON H.STATE_CODE = S.STATECODE "
							+ "LEFT JOIN DISTRICT D ON S.STATECODE = D.STATECODE AND H.DISTRICT_CODE = D.DISTRICTCODE "
							+ "WHERE H.STATE_CODE IN ?1 " + "AND H.DISTRICT_CODE IN ?2 " + "AND H.ASSIGNED_DC = ?3 "
							+ "AND STATUS_FLAG=0 " + "ORDER BY H.HOSPITAL_NAME");
			query.setParameter(1, Statecodeforhospitallist);
			query.setParameter(2, districtcode);
			query.setParameter(3, userid);
			List<Object[]> results = query.getResultList();
			results.forEach(result -> {
				Map<String, String> hospitalMap = new LinkedHashMap<>();
				hospitalMap.put("StateCode", (String) result[0]);
				hospitalMap.put("StateName", (String) result[1]);
				hospitalMap.put("DistrictCode", (String) result[2]);
				hospitalMap.put("DistrictName", (String) result[3]);
				hospitalMap.put("HospitalName", (String) result[4]);
				hospitalMap.put("HospitalCode", (String) result[5]);
				hosoitaltMaps.add(hospitalMap);

			});
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hosoitaltMaps;
	}

	@Override
	public Map<String, Object> getDischargeandClaimsummarydetailsinnerpage(Long userid, String month, String years,
			Integer searcby, List<String> satedata, List<String> districtdata, List<String> hospitaldata)
			throws Exception {
		Map<String, Object> summarypage = new HashMap<String, Object>();
		List<Object> summaryinnerpage = new ArrayList<Object>();
		StringBuffer statelistfrday = new StringBuffer();
		StringBuffer districtlistday = new StringBuffer();
		StringBuffer hospitallistday = new StringBuffer();
		String statedataday = null;
		String Districtdataday = null;
		String hospitaldataday = null;
		try {

			if (satedata.size() != 0) {
				for (String element : satedata) {
					statelistfrday.append(element.toString() + ",");
				}
				statedataday = statelistfrday.substring(0, statelistfrday.length() - 1);
			} else {
				statedataday = null;
			}

			if (districtdata.size() != 0) {
				for (String element : districtdata) {
					districtlistday.append(element.toString() + ",");
				}
				Districtdataday = districtlistday.substring(0, districtlistday.length() - 1);
			} else {
				Districtdataday = null;
			}

			if (hospitaldata.size() != 0) {
				for (String element : hospitaldata) {
					hospitallistday.append(element.toString() + ",");
				}
				hospitaldataday = hospitallistday.substring(0, hospitallistday.length() - 1);
			} else {
				hospitaldataday = null;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DAYWISE_DISCHARGE_CLAIM_RPT")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SEARCH_BY", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_list", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_district_list", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_list", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USER_ID", userid);
			storedProcedureQuery.setParameter("P_MONTH", month);
			storedProcedureQuery.setParameter("p_year", years);
			storedProcedureQuery.setParameter("P_SEARCH_BY", searcby);
			storedProcedureQuery.setParameter("p_state_list", statedataday);
			storedProcedureQuery.setParameter("p_district_list", Districtdataday);
			storedProcedureQuery.setParameter("p_hospital_list", hospitaldataday);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (rs.next()) {
				Claimsummarydetails details = new Claimsummarydetails();
				details.setDates(rs.getString(1));
				details.setUniquefamilytreated(rs.getString(2));
				details.setUniquepatienttreated(rs.getString(3));
				details.setTotalpackagedischarged(rs.getString(4));
				details.setTotaldischargeamount(rs.getString(5));
				details.setTotalclaimsubmitted(rs.getString(6));
				details.setTotalclaimamount(rs.getString(7));
				details.setTotal_pendingforclaim_raise(rs.getString(8));
				details.setTotalpendingraiseamount(rs.getString(9));
				details.setClaimnotsubmittedafter7daysofdischarge(rs.getString(10));
				details.setCnsamountafter7daysofdischarge(rs.getString(11));
				summaryinnerpage.add(details);
			}
			summarypage.put("details", summaryinnerpage);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return summarypage;
	}

	@Override
	public String getblockByDistrictId(String districtCode, String stateCode) {
		JSONArray districtlist = new JSONArray();
		try {
			List<Object[]> list = districtrepo.getblockByDistrictId(districtCode, stateCode);
			for (Object[] obj : list) {
				JSONObject district = new JSONObject();
				district.put("statecode", obj[0]);
				district.put("districtcode", obj[1]);
				district.put("blockcode", obj[2]);
				district.put("blockname", obj[3]);
				districtlist.put(district);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtlist.toString();
	}

	@Override
	public List<Object> getDClistCDMO(Long userId) {
		List<Object> listHosp = new ArrayList<>();
		try {
			userdeailsRepo.FindDCList(userId).stream().map(hospitalArr -> {
				Map<String, String> snoUser = new HashMap<String, String>();
				snoUser.put("bskyUserId", hospitalArr[0].toString());
				snoUser.put("uesrName", hospitalArr[1].toString());
				snoUser.put("userId", hospitalArr[2].toString());
				snoUser.put("fullName", hospitalArr[3].toString());
				return snoUser;
			}).forEach(listHosp::add);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listHosp;
	}

	@Override
	public List<Object> getDClistByStateAndDist(String stateId, String distId) {
		List<Object> listHosp = new ArrayList<>();
		try {
			userdeailsRepo.FindDCListByStateAndDist(stateId, distId).stream().map(hospitalArr -> {
				Map<String, String> snoUser = new HashMap<String, String>();
				snoUser.put("bskyUserId", hospitalArr[0].toString());
				snoUser.put("uesrName", hospitalArr[1].toString());
				snoUser.put("userId", hospitalArr[2].toString());
				snoUser.put("fullName", hospitalArr[3].toString());
				return snoUser;
			}).forEach(listHosp::add);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listHosp;
	}

	@Override
	public List<Map<String, String>> getHospitalByDCUserId(List<Integer> list) throws Exception {
		List<Map<String, String>> districtMaps = new ArrayList();
		try {
			Query query = this.entityManager.createNativeQuery(
					"SELECT hospital_id,(hospital_name||' ('||hospital_code||')') as hospname,hospital_code,hospital_name\r\n"
							+ " FROM hospital_info WHERE ASSIGNED_DC in (?1) AND STATUS_FLAG=0\r\n"
							+ " order by hospital_name");
			query.setParameter(1, list);
			List<Object[]> results = query.getResultList();
			results.forEach(result -> {
				Map<String, String> districtMap = new LinkedHashMap<>();
				districtMap.put("hospitalId", result[0].toString());
				districtMap.put("hospitalName", result[1].toString());
				districtMap.put("hospitalCode", result[2].toString());
				districtMap.put("hospName", result[3].toString());
				districtMaps.add(districtMap);
			});

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return districtMaps;
	}

	@Override
	public String getDistrictDetailsByNFSAData() {
		JSONArray districtlistNFSA = new JSONArray();
		ResultSet rsNFSA = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("sp_get_master_data")
					.registerStoredProcedureParameter("p_stateCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtCode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_stateCode", null);
			storedProcedureQuery.setParameter("p_districtCode", null);
			storedProcedureQuery.setParameter("p_flag", "DISTRICTNFSA");
			storedProcedureQuery.execute();
			rsNFSA = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rsNFSA.next()) {
				JSONObject district = new JSONObject();
				district.put("districtid", rsNFSA.getInt(1));
				district.put("districtname", rsNFSA.getString(2));
				district.put("lgddistcode", rsNFSA.getString(3));
				districtlistNFSA.put(district);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rsNFSA != null) {
					rsNFSA.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return districtlistNFSA.toString();
	}

	@Override
	public List<Map<String, Object>> getSchemeDetails(Map<String, Object> request) {
		List<Map<String, Object>> response = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("USP_GET_SCHEME_BSKY")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMEID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION", request.get("action").toString());
			storedProcedureQuery.setParameter("P_SCHEMEID",
					request.get("schemeId") != null && !request.get("schemeId").equals("")
							? Long.parseLong(request.get("schemeId").toString())
							: null);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID",
					request.get("schemeCategoryId") != null && !request.get("schemeCategoryId").equals("")
							? Long.parseLong(request.get("schemeCategoryId").toString())
							: null);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");

			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> data = new HashedMap<>();
					// Scheme
					if (request.get("action").equals("A")) {
						data.put("schemeId", rs.getString("SCHEMEID"));
						data.put("schemeName", rs.getString("SCHEMENAME"));
					} else if (request.get("action").equals("B")) {
						// Scheme Category
						data.put("schemeCategoryId", rs.getString("SCHEMECATEGORYID"));
						data.put("categoryName", rs.getString("CATEGORYNAME"));
					} else if (request.get("action").equals("C")) {
						// Sub scheme category
						data.put("schemeSubCategoryId", rs.getString("SCHEMESUBCATEGORYID"));
						data.put("subCategoryName", rs.getString("SUBCATEGORYNAME"));
					}
					response.add(data);
				}
			}

		} catch (Exception e) {
			logger.error("Exception Occurred in getSchemeDetails method of PatientReferralServiceImpl", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
		}
		return response;
	}

	@Override
	public List<Object> getHospitalPackageSchemeWiseDataForPackageNameData(String schemeId, String schemeCategoryId) {
		List<Object> packagedetails = new ArrayList<Object>();
		ResultSet rs = null;
		Long schemecatId = null;
		if (schemeCategoryId != null && !schemeCategoryId.equals("")) {
			schemecatId = Long.parseLong(schemeCategoryId);
		} else {
			schemecatId = null;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_HOS_SCHEME_INCLUSIONOFSEARCHING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", Long.parseLong("1"));
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemecatId);
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", null);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				PackageScheme searcdata = new PackageScheme();
				searcdata.setPackageheader(rs.getString(1));
				packagedetails.add(searcdata);
			}

		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getSchemeDetails method of getHospitalPackageSchemeWiseDataForPackageNameData",
					e);
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
		}
		return packagedetails;
	}

	@Override
	public List<Object> getHospitalPackageHeadercode(String schemeId, String schemeCategoryId, String acronym) {
		List<Object> packageNamedetails = new ArrayList<Object>();
		ResultSet rs = null;
		Long schemecatId = null;
		if (schemeCategoryId != null && !schemeCategoryId.equals("")) {
			schemecatId = Long.parseLong(schemeCategoryId);
		} else {
			schemecatId = null;
		}

		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_HOS_SCHEME_INCLUSIONOFSEARCHING")
					.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", Long.parseLong("2"));
			storedProcedureQuery.setParameter("P_SCHEMECATEGORYID", schemecatId);
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", acronym.trim());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				PackageScheme searcdata = new PackageScheme();
				searcdata.setPackagename(rs.getString(1));
				packageNamedetails.add(searcdata);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in getSchemeDetails method of getHospitalPackageHeadercode", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
		}
		return packageNamedetails;
	}

	@Override
	public Map<String, Object> getMappedAuthDetails(Map<String, Object> response) throws Exception {
		List<Map<String, Object>> responseList = new ArrayList<>();
		Map<String, Object> mapdata=new HashMap<>();
		ResultSet resultSet;
		try {
		StoredProcedureQuery storedProcedureQuery = entityManager
				.createStoredProcedureQuery("USP_HOSPITAL_UID_AUTH_CONFIGURATION")
				.registerStoredProcedureParameter("P_ACTION_CODE", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

		storedProcedureQuery.setParameter("P_ACTION_CODE", 1L);
		storedProcedureQuery.setParameter("P_STATE_CODE", response.get("stateCode"));
		storedProcedureQuery.setParameter("P_DISTRICT_CODE", response.get("districtCode").toString().isEmpty() ? null : response.get("districtCode").toString());
		storedProcedureQuery.setParameter("P_HOSPITAL_CODE", response.get("hospitalCode").toString().isEmpty() ? null : response.get("hospitalCode").toString());

		storedProcedureQuery.execute();
		resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");

		Integer postatus=0;
		Integer iristatus=0;
		Integer otptatus=0;
		Integer facetatus=0;
		while (resultSet.next()) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("hospitalCode", resultSet.getString(1));
			map.put("hospitalName", resultSet.getString(2));
			map.put("pos", resultSet.getInt(3));
			if(resultSet.getInt(3)==1) {
				postatus=1;
			}
			map.put("iris", resultSet.getInt(4));
			if(resultSet.getInt(4)==1) {
				iristatus=1;
			}
			map.put("face", resultSet.getInt(5));
			if(resultSet.getInt(5)==1) {
				facetatus=1;
			}
			map.put("otp", resultSet.getInt(6));
			if(resultSet.getInt(6)==1) {
				otptatus=1;
			}
			map.put("posid", resultSet.getInt(7));
			map.put("irisid", resultSet.getInt(8));
			map.put("faceid", resultSet.getInt(9));
			map.put("otpid", resultSet.getInt(10));
			map.put("hospitalid", resultSet.getInt(11));
			responseList.add(map);
		}
		mapdata.put("data", responseList);
		mapdata.put("postatus", postatus);
		mapdata.put("iristatus", iristatus);
		mapdata.put("facetatus", facetatus);
		mapdata.put("otptatus", otptatus);
		mapdata.put("status", 200);
		}catch (Exception e) {
			throw new Exception(e);
		}		
		return mapdata;
	}

}
