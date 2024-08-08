package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.DCConfigurationBean;
import com.project.bsky.bean.HospObj;
import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalUserSaveBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.DistrictMaster;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.service.HospitalUserSaveService;

@Service
public class HospitalUserSaveServiceImpl implements HospitalUserSaveService {

	@Autowired
	private UserDetailsProfileRepository userRepo;

	@Autowired
	private HospitalInformationRepository hospitalUserSaveRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private GroupTypeRepository groupRepo;

	@Autowired
	private DistrictMasterRepository distRepo;

	@Autowired
	private Environment env;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<HospitalInformation> getDetails() {
		return hospitalUserSaveRepository.findAllDetails();

	}

	@Override
	public HospitalInformation saveDetails(HospitalUserSaveBean hospitalUserSaveBean) {
		HospitalInformation hospitalUserSave = new HospitalInformation();
		UserDetails userDetails = new UserDetails();
		Calendar calendar = Calendar.getInstance();
		try {
			hospitalUserSave.setHospitalCode(hospitalUserSaveBean.getHospitalCode().toUpperCase());
			hospitalUserSave.setHospitalName(hospitalUserSaveBean.getHospitalName());
			hospitalUserSave.setHospitalId(null);
			DistrictMaster d = distRepo.getdistrict(hospitalUserSaveBean.getStateId(),
					hospitalUserSaveBean.getDistrictId());
			hospitalUserSave.setDistrictcode(d);
			hospitalUserSave.setCpdApprovalRequired(Integer.parseInt(hospitalUserSaveBean.getCpdApprovalRequired()));
			hospitalUserSave.setMobile(hospitalUserSaveBean.getMobile());
			hospitalUserSave.setEmailId(hospitalUserSaveBean.getEmailId());
			hospitalUserSave.setStatus(0);
			hospitalUserSave.setBackdateadmissiondate(0);
			hospitalUserSave.setBackdatedischargedate(0);
			hospitalUserSave.setPatientOtpRequired(0);
			hospitalUserSave.setMouStatus(0);
			hospitalUserSave.setEmpanelmentstatus(0);
			hospitalUserSave.setIsBlockActive(0);
			hospitalUserSave.setPreauthapprovalrequired(0);
			hospitalUserSave.setAssigned_dc(hospitalUserSaveBean.getAssignedDc());
			hospitalUserSave.setCreatedBy(Long.valueOf(hospitalUserSaveBean.getCreatedBy()));
			hospitalUserSave.setCreatedOn(calendar.getTime());
			hospitalUserSave.setUpdatedBy(null);
			hospitalUserSave.setUpdatedOn(null);
			hospitalUserSave.setLatitude(hospitalUserSaveBean.getLatitude());
			hospitalUserSave.setLongitude(hospitalUserSaveBean.getLongitude());
			hospitalUserSave.setHospitalCategoryid(hospitalUserSaveBean.getHospitalType());
			userDetails.setCompanyCode(null);
			userDetails.setCreatedUserName(hospitalUserSaveBean.getCreatedBy().toString());
			userDetails.setEmail(hospitalUserSaveBean.getEmailId());
			userDetails.setPhone(Long.parseLong(hospitalUserSaveBean.getMobile()));
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(5);
			userDetails.setGroupId(gr);
			userDetails.setStatus(0);
			userDetails.setTPACode(null);
			userDetails.setCreateDateTime(calendar.getTime());
			userDetails.setNonCompBtnFlg(1);
			userDetails.setNonUploadBtnFlg(1);
			userDetails.setUserName(hospitalUserSaveBean.getHospitalCode().toUpperCase());
			userDetails.setFullname(hospitalUserSaveBean.getHospitalName());
			userDetails.setTmsLoginStatus(0);
			String password = passwordEncoder.encode(env.getProperty("configKey"));
			userDetails.setPassword(password);
			userDetails.setIsOtpAllowed(1);
			hospitalUserSave.setUserId(userDetails);
			hospitalUserSave = hospitalUserSaveRepository.save(hospitalUserSave);
			return hospitalUserSave;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public Integer checkHospitalByCode(String hospitalCode) {
		Integer checkhsp = null;
		try {
			checkhsp = hospitalUserSaveRepository.checkHospitalByHospitalCode(hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkhsp;
	}

	@Override
	public void deleteById(Integer hospitalId) {
		try {
			hospitalUserSaveRepository.deleteById(hospitalId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public HospitalInformation update(HospitalUserSaveBean hospitalUserSaveBean) {
		Calendar calendar = Calendar.getInstance();
		try {
			HospitalInformation hospitalUserSave = hospitalUserSaveRepository
					.findById(Integer.parseInt(hospitalUserSaveBean.getHospitalId())).get();
			UserDetails userDetails = hospitalUserSave.getUserId();
			hospitalUserSave.setHospitalName(hospitalUserSaveBean.getHospitalName());
			DistrictMaster d = distRepo.getdistrict(hospitalUserSaveBean.getStateId(),
					hospitalUserSaveBean.getDistrictId());
			hospitalUserSave.setDistrictcode(d);
			hospitalUserSave.setCpdApprovalRequired(Integer.parseInt(hospitalUserSaveBean.getCpdApprovalRequired()));
			hospitalUserSave.setMobile(hospitalUserSaveBean.getMobile());
			hospitalUserSave.setEmailId(hospitalUserSaveBean.getEmailId());
			hospitalUserSave.setAssigned_dc(hospitalUserSaveBean.getAssignedDc());
			hospitalUserSave.setUpdatedBy(Long.valueOf(hospitalUserSaveBean.getUpdatedBy()));
			hospitalUserSave.setUpdatedOn(calendar.getTime());
			hospitalUserSave.setLatitude(hospitalUserSaveBean.getLatitude());
			hospitalUserSave.setLongitude(hospitalUserSaveBean.getLongitude());
			hospitalUserSave.setHospitalCategoryid(hospitalUserSaveBean.getHospitalType());
			userDetails.setCompanyCode(null);
			userDetails.setEmail(hospitalUserSaveBean.getEmailId());
			userDetails.setPhone(Long.parseLong(hospitalUserSaveBean.getMobile()));
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(5);
			userDetails.setGroupId(gr);
			userDetails.setTPACode(null);
			userDetails.setFullname(hospitalUserSaveBean.getHospitalName());
			userDetails.setTmsLoginStatus(hospitalUserSaveBean.getTmsActiveStat());
			hospitalUserSave.setUserId(userDetails);
			hospitalUserSave = hospitalUserSaveRepository.save(hospitalUserSave);
			return hospitalUserSave;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public HospitalInformation getbyid(Integer hospitalId) {
		try {
			return hospitalUserSaveRepository.findById(hospitalId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public List<HospitalInformation> getHospitalFilteredByState(String stateId) {
		return hospitalUserSaveRepository.findAllDetailsByState(stateId);
	}

	@Override
	public List<HospitalInformation> getHospitalFilteredByDistrict(String stateId, String districtId) {
		return hospitalUserSaveRepository.findAllDetailsByDistrict(stateId, districtId);
	}

	@Override
	public List<HospitalBean> getHospitals(String stateId, String districtId, String cpdApprovalRequired,
			String snoTagged, String categoryId, Integer tmsActive) {
		List<HospitalBean> listDetails = new ArrayList<HospitalBean>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_HOSPITAL_LIST")
					.registerStoredProcedureParameter("P_SNO_TAGGED", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPDAPPROVALREQUIRED", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPTYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TMS_ACTIVE_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNO_TAGGED", snoTagged);
			storedProcedureQuery.setParameter("P_STATEID", stateId);
			storedProcedureQuery.setParameter("P_DISTRICTID", districtId);
			storedProcedureQuery.setParameter("P_CPDAPPROVALREQUIRED", cpdApprovalRequired);
			storedProcedureQuery.setParameter("P_HOSPTYPE", categoryId);
			storedProcedureQuery.setParameter("P_TMS_ACTIVE_STATUS", tmsActive);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
			while (rs.next()) {
				HospitalBean sNOConfigurationBean = new HospitalBean();
				sNOConfigurationBean.setHospitalName(rs.getString(1));
				sNOConfigurationBean.setMobileNo(rs.getString(2));
				sNOConfigurationBean.setStatus(rs.getInt(3));
				sNOConfigurationBean.setEmailId(rs.getString(4));
				sNOConfigurationBean.setStateName(rs.getString(5));
				sNOConfigurationBean.setDistrictName(rs.getString(6));
				sNOConfigurationBean.setCpdApprovalRequired(rs.getInt(7));
				sNOConfigurationBean.setHospitalId(rs.getInt(8));
				sNOConfigurationBean.setHospitalCode(rs.getString(9));
				sNOConfigurationBean.setHospitalType(rs.getString(11));
				sNOConfigurationBean.setTmsActiveStat(rs.getString(12));
				sNOConfigurationBean.setMoustartdate(rs.getString(13));
				sNOConfigurationBean.setMouenddate(rs.getString(14));
				sNOConfigurationBean.setHcvalidform(rs.getString(15));
				sNOConfigurationBean.setHcvalidto(rs.getString(16));
				listDetails.add(sNOConfigurationBean);
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
		return listDetails;
	}

	@Override
	public HospitalInformation getbyuserid(Long userId) {
		try {
			return hospitalUserSaveRepository.findByUserId(userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public Response saveDCConfiguration(DCConfigurationBean bean) {
		Response response = new Response();
		try {
			List<HospObj> hospital_List = bean.getHospList();
			for (HospObj hospital : hospital_List) {
				HospitalInformation hospitalInfo = hospitalUserSaveRepository
						.findHospitalByCode(hospital.getHospitalCode());
				hospitalInfo.setAssigned_dc(bean.getDcId());
				hospitalInfo.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				hospitalInfo.setUpdatedBy(Long.valueOf(bean.getCreatedBy()));
				hospitalUserSaveRepository.save(hospitalInfo);
			}
			response.setMessage("DC Configuration saved successfully");
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
	public DCConfigurationBean getDcConfigurationDetailsById(Long dcUserId) {
		DCConfigurationBean dcConfigurationBean = new DCConfigurationBean();
		List<HospObj> hospList = new ArrayList<HospObj>();
		ResultSet rs = null;
		try {
			dcConfigurationBean.setDcId(dcUserId);
			dcConfigurationBean.setDcName(userRepo.getFullName(dcUserId));
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_dc_mapping_data")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_assigned_dc", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_stateid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_assigned_dc", dcUserId);
			storedProcedureQuery.setParameter("p_flag", 2);
			storedProcedureQuery.setParameter("p_stateid", null);
			storedProcedureQuery.setParameter("p_districtid", null);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				HospObj hosp = new HospObj();
				hosp.setDistrictCode(rs.getString(7));
				hosp.setStateCode(rs.getString(6));
				hosp.setDistrictName(rs.getString(4));
				hosp.setStateName(rs.getString(3));
				hosp.setHospitalName(rs.getString(2));
				hosp.setHospitalCode(rs.getString(1));
				hospList.add(hosp);
			}
			dcConfigurationBean.setHospList(hospList);
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
		return dcConfigurationBean;
	}

	@Override
	public Integer checkDCHospitalName(String hospitalCode, Long dcId) {
		return hospitalUserSaveRepository.checkDcNameDulicacy(hospitalCode, dcId);
	}

	@Override
	public Response updateDCConfiguration(DCConfigurationBean bean) {
		Response response = new Response();
		try {
			hospitalUserSaveRepository.inActivateDC(bean.getDcId(), Long.valueOf(bean.getUpdatedBy()));
			List<HospObj> hospital_List = bean.getHospList();
			for (HospObj hospital : hospital_List) {
				HospitalInformation hospitalInfo = hospitalUserSaveRepository
						.findHospitalByCode(hospital.getHospitalCode());
				hospitalInfo.setAssigned_dc(bean.getDcId());
				hospitalInfo.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				hospitalInfo.setUpdatedBy(Long.valueOf(bean.getUpdatedBy()));
				hospitalUserSaveRepository.save(hospitalInfo);
			}
			response.setMessage("DC Configuration updated successfully");
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
	public Integer checkHospitalNameForOther(String hospitalCode, Long assignedDc) {
		return hospitalUserSaveRepository.checkOtherDcNameDulicacy(hospitalCode, assignedDc);
	}

	@Override
	public JSONObject getHospDetails(Integer hospitalId) {
		JSONObject details = null;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_hospital_details")
					.registerStoredProcedureParameter("p_hospitalId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_hospitalId", hospitalId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				details = new JSONObject();
				details.put("hospitalName", rs.getString(1));
				details.put("hospitalCode", rs.getString(2));
				details.put("mobile", rs.getLong(3));
				details.put("email", rs.getString(4));
				details.put("assignedDc", rs.getLong(5));
				details.put("assignedDcName", rs.getString(6));
				details.put("assignedSno", rs.getLong(7));
				details.put("assignedSnoName", rs.getString(8));
				details.put("hospitalType", rs.getString(9));
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
		return details;
	}

	@Override
	public List<DCConfigurationBean> getAllDcConfigurationDetails(String stateId, String districtId) {
		if (stateId.equalsIgnoreCase("null")) {
			stateId = null;
		}
		if (districtId.equalsIgnoreCase("null")) {
			districtId = null;
		}
		List<DCConfigurationBean> listDetails = new ArrayList<DCConfigurationBean>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_DC_MAPPING")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", null);
			storedProcedureQuery.setParameter("P_STATEID", stateId);
			storedProcedureQuery.setParameter("P_DISTRICTID", districtId);
			storedProcedureQuery.setParameter("P_HOSPCODE", null);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
			while (rs.next()) {
				DCConfigurationBean dcConfigurationBean = new DCConfigurationBean();
				dcConfigurationBean.setDcId(rs.getLong(1));
				dcConfigurationBean.setStatus(rs.getInt(2));
				dcConfigurationBean.setHospitalName(rs.getString(3));
				dcConfigurationBean.setStateName(rs.getString(4));
				dcConfigurationBean.setDistrictName(rs.getString(5));
				dcConfigurationBean.setDcName(rs.getString(6));
				dcConfigurationBean.setHospCode(rs.getString(7));
				listDetails.add(dcConfigurationBean);
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
		return listDetails;
	}

	@Override
	public JSONArray getDistinctDC(Long userId) {
		ResultSet rs = null;
		JSONArray distinctDc = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_dc_mapping_data")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_assigned_dc", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_stateid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_assigned_dc", userId);
			storedProcedureQuery.setParameter("p_stateid", null);
			storedProcedureQuery.setParameter("p_districtid", null);
			storedProcedureQuery.setParameter("p_flag", 0);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject dc = new JSONObject();
				dc.put("dcUserId", rs.getInt(1));
				dc.put("fullname", rs.getString(2));
				dc.put("count", rs.getInt(3));
				distinctDc.put(dc);
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
		return distinctDc;
	}

	@Override
	public JSONArray getDCConfigDetails(Long userId, String stateId, String districtId) {
		ResultSet rs = null;
		JSONArray dcDetails = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_dc_mapping_data")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_assigned_dc", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_stateid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_assigned_dc", userId);
			storedProcedureQuery.setParameter("p_stateid", stateId);
			storedProcedureQuery.setParameter("p_districtid", districtId);
			storedProcedureQuery.setParameter("p_flag", 1);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
			while (rs.next()) {
				JSONObject dc = new JSONObject();
				dc.put("hospitalName", rs.getString(2));
				dc.put("hospitalCode", rs.getString(1));
				dc.put("stateName", rs.getString(3));
				dc.put("districtName", rs.getString(4));
				dcDetails.put(dc);
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
		return dcDetails;
	}

	@Override
	public Integer updateProfile(HospitalUserSaveBean hospitalInformation) {
		try {
			Calendar cal = Calendar.getInstance();
			HospitalInformation h = hospitalUserSaveRepository
					.findHospitalByCode(hospitalInformation.getHospitalCode());
			h.setMobile(hospitalInformation.getMobile());
			h.setEmailId(hospitalInformation.getEmailId());
			h.setUpdatedBy(Long.valueOf(hospitalInformation.getUpdatedBy()));
			h.setUpdatedOn(cal.getTime());
			h.setLatitude(hospitalInformation.getLatitude());
			h.setLongitude(hospitalInformation.getLongitude());
			UserDetails u = h.getUserId();
			if (hospitalInformation.getMobile() != null && !hospitalInformation.getMobile().equalsIgnoreCase("null")) {
				u.setPhone(Long.valueOf(hospitalInformation.getMobile()));
			}
			u.setEmail(hospitalInformation.getEmailId());
			u.setCreatedUserName(hospitalInformation.getUpdatedBy().toString());
			u.setCreateDateTime(cal.getTime());
			h.setUserId(u);
			hospitalUserSaveRepository.save(h);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

}
