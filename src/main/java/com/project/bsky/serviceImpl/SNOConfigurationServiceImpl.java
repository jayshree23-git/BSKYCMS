package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bsky.bean.ExecutiveList;
import com.project.bsky.bean.HospObj;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SNOConfigurationBean;
import com.project.bsky.bean.SnaExecBean;
import com.project.bsky.model.SNAExecutive;
import com.project.bsky.model.SNOConfiguration;
import com.project.bsky.repository.SNAExecutiveRepository;
import com.project.bsky.repository.SNOConfigurationRepository;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.service.SNOConfigurationService;

/**
 * @author ronauk
 *
 */
@Service
public class SNOConfigurationServiceImpl implements SNOConfigurationService {

	@Autowired
	private SNOConfigurationRepository snoConfigrepository;

	@Autowired
	private UserDetailsProfileRepository userRepo;

	@Autowired
	private SNAExecutiveRepository snaExecutiverepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	@Transactional
	public Response saveSNOConfiguration(SNOConfigurationBean snoConfigurationBean) {
		Response response = new Response();
		try {
			// //System.out.println(snoConfigurationBean.toString());
			List<HospObj> hospital_List = snoConfigurationBean.getHospList();
			for (HospObj hospital : hospital_List) {
				Integer checkCPD = snoConfigrepository.checkSNOConfigDulicacy(hospital.getHospitalCode(),
						snoConfigurationBean.getSnoId());
				// //System.out.println(checkCPD);
				if (checkCPD > 0) {
					if (checkCPD == 1) {
						Integer status = snoConfigrepository.checkStatus(hospital.getHospitalCode(),
								snoConfigurationBean.getSnoId());
						if (status == 1) {
							SNOConfiguration snoConfig = snoConfigrepository
									.getSnoConfig(hospital.getHospitalCode(), snoConfigurationBean.getSnoId()).get(0);
							// //System.out.println("to update: " + hospital.getHospitalCode());
							snoConfig.setStateCode(hospital.getStateCode());
							snoConfig.setDistrictCode(hospital.getDistrictCode());
							snoConfig.setStatus(0);
							snoConfig.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
							snoConfig.setUpdatedBy(snoConfigurationBean.getCreatedBy());
							snoConfigrepository.save(snoConfig);
						}
					} else {
						boolean flag = false;
						List<Integer> stats = snoConfigrepository.getStats(hospital.getHospitalCode(),
								snoConfigurationBean.getSnoId());
						for (Integer status : stats) {
							if (status == 0) {
								flag = true;
								break;
							}
						}
						if (flag == false) {
							SNOConfiguration snoConfig = snoConfigrepository
									.getSnoConfig(hospital.getHospitalCode(), snoConfigurationBean.getSnoId()).get(0);
							// //System.out.println("to update: " + hospital.getHospitalCode());
							snoConfig.setStateCode(hospital.getStateCode());
							snoConfig.setDistrictCode(hospital.getDistrictCode());
							snoConfig.setStatus(0);
							snoConfig.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
							snoConfig.setUpdatedBy(snoConfigurationBean.getCreatedBy());
							snoConfigrepository.save(snoConfig);
						}
					}
				} else {
					SNOConfiguration snoConfig = new SNOConfiguration();
					// //System.out.println("to save: " + hospital.getHospitalCode());
					snoConfig.setSnoUserId(snoConfigurationBean.getSnoId());
					snoConfig.setHospitalCode(hospital.getHospitalCode());
					snoConfig.setStateCode(hospital.getStateCode());
					snoConfig.setDistrictCode(hospital.getDistrictCode());
					snoConfig.setStatus(0);
					snoConfig.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					snoConfig.setCreatedBy(snoConfigurationBean.getCreatedBy());
					snoConfigrepository.save(snoConfig);
				}
			}
			List<String> hospitalCodes = hospital_List.stream().map(HospObj::getHospitalCode)
					.collect(Collectors.toList());

			assignClaimsToSna(snoConfigurationBean.getSnoId(), hospitalCodes);
			response.setMessage("SNA Configuration saved successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
			return response;
		}

	}

	@Override
	public List<SNOConfigurationBean> getAllSnoConfigurationDetails(String stateId, String districtId) {
		// //System.out.println(stateId + " : " + districtId);
		if (stateId.equalsIgnoreCase("null")) {
			stateId = null;
		}
		if (districtId.equalsIgnoreCase("null")) {
			districtId = null;
		}
		// //System.out.println(stateId + " : " + districtId);
		List<SNOConfigurationBean> objBean = new ArrayList<SNOConfigurationBean>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_sna_mapping")
					.registerStoredProcedureParameter("p_sno_mapping_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_state_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_district_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno_mapping_id", null);
			storedProcedureQuery.setParameter("p_state_code", stateId);
			storedProcedureQuery.setParameter("p_district_code", districtId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				SNOConfigurationBean sNOConfigurationBean = new SNOConfigurationBean();
				sNOConfigurationBean.setSnoMappingId(rs.getInt(1));
				sNOConfigurationBean.setStatus(rs.getInt(2));
				sNOConfigurationBean.setHospitalName(rs.getString(3));
				sNOConfigurationBean.setStateName(rs.getString(4));
				sNOConfigurationBean.setDistrictName(rs.getString(5));
				sNOConfigurationBean.setSnoName(rs.getString(6));
				sNOConfigurationBean.setHospCode(rs.getString(7));
				objBean.add(sNOConfigurationBean);
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
		return objBean;
	}

	@Override
	public SNOConfigurationBean getSnoConfigurationDetailsById(Integer snoUserId) {
		SNOConfigurationBean sNOConfigurationBean = new SNOConfigurationBean();
		List<HospObj> hospList = new ArrayList<HospObj>();
		ResultSet rs = null;
		try {
			sNOConfigurationBean.setSnoId(snoUserId);
			sNOConfigurationBean.setSnoName(userRepo.getFullName(snoUserId));
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_MAPPING_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNOUSERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNOUSERID", snoUserId);
			storedProcedureQuery.setParameter("P_FLAG", 2);
			storedProcedureQuery.setParameter("P_STATEID", null);
			storedProcedureQuery.setParameter("P_DISTRICTID", null);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
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
			sNOConfigurationBean.setHospList(hospList);
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
		return sNOConfigurationBean;
	}

	@Override
	@Transactional
	public Response updateSnoDetailsData(SNOConfigurationBean sNOConfigurationBean) {
		Response response = new Response();
		try {
			// //System.out.println(sNOConfigurationBean.toString());
			List<String> hospitals = snoConfigrepository.gethostipals(sNOConfigurationBean.getSnoId());
			snoConfigrepository.inActivate(sNOConfigurationBean.getSnoId(), sNOConfigurationBean.getUpdatedBy());
			assignClaimsToSna(null, hospitals);

			List<HospObj> hospital_List = sNOConfigurationBean.getHospList();
			for (HospObj hospital : hospital_List) {
				Integer checkCPD = snoConfigrepository.checkSNOConfigDulicacy(hospital.getHospitalCode(),
						sNOConfigurationBean.getSnoId());
				if (checkCPD > 0) {
					Integer status = snoConfigrepository.checkStatus(hospital.getHospitalCode(),
							sNOConfigurationBean.getSnoId());
					if (status == 1) {
						SNOConfiguration snoConfig = snoConfigrepository
								.getSnoConfig(hospital.getHospitalCode(), sNOConfigurationBean.getSnoId()).get(0);
						// //System.out.println("to update: " + hospital.getHospitalCode());
						snoConfig.setStateCode(hospital.getStateCode());
						snoConfig.setDistrictCode(hospital.getDistrictCode());
						snoConfig.setStatus(0);
						snoConfig.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
						snoConfig.setUpdatedBy(sNOConfigurationBean.getUpdatedBy());
						snoConfigrepository.save(snoConfig);
					}
				} else {
					SNOConfiguration snoConfig = new SNOConfiguration();
					// //System.out.println("to save: " + hospital.getHospitalCode());
					snoConfig.setSnoUserId(sNOConfigurationBean.getSnoId());
					snoConfig.setHospitalCode(hospital.getHospitalCode());
					snoConfig.setStateCode(hospital.getStateCode());
					snoConfig.setDistrictCode(hospital.getDistrictCode());
					snoConfig.setStatus(0);
					snoConfig.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					snoConfig.setCreatedBy(sNOConfigurationBean.getUpdatedBy());
					snoConfigrepository.save(snoConfig);
				}
			}
			List<String> hospitalCodes = hospital_List.stream().map(HospObj::getHospitalCode)
					.collect(Collectors.toList());

			assignClaimsToSna(sNOConfigurationBean.getSnoId(), hospitalCodes);
			response.setMessage("SNA Configuartion Updated successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
			return response;
		}
	}

	@Override
	public Integer checkSnoName(Integer SnoUserId) {
		return snoConfigrepository.checkSnoNameDulicacy(SnoUserId);
	}

	@Override
	public Integer checkHospitalName(String hospitalCode, Integer snoId) {
		return snoConfigrepository.checkHospitalNameDulicacy(hospitalCode, snoId);
	}

	@Override
	public List<Integer> checkSnoStatus(int snoUserId) {
		// TODO Auto-generated method stub
		return snoConfigrepository.getStatus(snoUserId);
	}

	@Override
	public SNOConfiguration getSnoConfFromHospCode(String hospitalCode) {
		// TODO Auto-generated method stub
		return snoConfigrepository.getSnoConfFromHospCode(hospitalCode);
	}

	@Override
	public Integer checkHospitalNameForSNA(String hospitalCode, Integer snoUserId) {
		// TODO Auto-generated method stub
		return snoConfigrepository.checkHospitalNameDulicacyForSNA(hospitalCode, snoUserId);
	}

	@Override
	public JSONArray getDistinctSNA(Integer userId) {
		// TODO Auto-generated method stub
		ResultSet rs = null;
		JSONArray distinctSna = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_MAPPING_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNOUSERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNOUSERID", userId);
			storedProcedureQuery.setParameter("P_STATEID", null);
			storedProcedureQuery.setParameter("P_DISTRICTID", null);
			storedProcedureQuery.setParameter("P_FLAG", 0);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");

			while (rs.next()) {
				JSONObject sna = new JSONObject();
				sna.put("snaUserId", rs.getInt(1));
				sna.put("fullname", rs.getString(2));
				sna.put("count", rs.getInt(3));
				distinctSna.put(sna);
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
		return distinctSna;
	}

	@Override
	public JSONArray getSNAConfigDetails(Integer userId, String stateId, String districtId) {
		// TODO Auto-generated method stub
		// //System.out.println(stateId + " : " + districtId);
		ResultSet rs = null;
		JSONArray snaDetails = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_MAPPING_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNOUSERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNOUSERID", userId);
			storedProcedureQuery.setParameter("P_STATEID", stateId);
			storedProcedureQuery.setParameter("P_DISTRICTID", districtId);
			storedProcedureQuery.setParameter("P_FLAG", 1);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");

			while (rs.next()) {
				JSONObject sna = new JSONObject();
				sna.put("hospitalName", rs.getString(2));
				sna.put("hospitalCode", rs.getString(1));
				sna.put("stateName", rs.getString(3));
				sna.put("districtName", rs.getString(4));
				sna.put("status", rs.getInt(5));
				snaDetails.put(sna);
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
		// //System.out.println(snaDetails);
		return snaDetails;
	}

	@Override
	@Transactional
	public Response updateSnoDetailsDataById(SNOConfigurationBean sNOConfigurationBean, Integer snoMappingId) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
			// //System.out.println(sNOConfigurationBean);
			List<HospObj> hospital_List = sNOConfigurationBean.getHospList();
			HospObj hospital = hospital_List.get(0);
			SNOConfiguration snoConfig = snoConfigrepository.findById(snoMappingId).get();
			// //System.out.println("to update: " + hospital.getHospitalCode());
			snoConfig.setSnoUserId(sNOConfigurationBean.getSnoId());
			snoConfig.setStateCode(hospital.getStateCode());
			snoConfig.setDistrictCode(hospital.getDistrictCode());
			snoConfig.setHospitalCode(hospital.getHospitalCode());
			snoConfig.setStatus(0);
			snoConfig.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			snoConfig.setUpdatedBy(sNOConfigurationBean.getUpdatedBy());
			snoConfigrepository.save(snoConfig);
			List<String> hospitalCodes = hospital_List.stream().map(HospObj::getHospitalCode)
					.collect(Collectors.toList());

			assignClaimsToSna(sNOConfigurationBean.getSnoId(), hospitalCodes);
			response.setMessage("SNA Configuartion Updated successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
			return response;
		}
	}

	@Override
	@Transactional
	public void assignClaimsToSna(Integer snoId, List<String> hospitalCodes) {
		// TODO Auto-generated method stub
		String query = null;
		Query q = null;
		try {
			if (snoId != null) {
				query = "update txnclaim_application set assignedsno=:snoId where pendingat IN (0,1,2) AND statusflag=0 AND hospitalcode in :hospitalCodes";
				q = this.entityManager.createNativeQuery(query);
				q.setParameter("snoId", snoId);
				q.setParameter("hospitalCodes", hospitalCodes);
			} else {
				query = "update txnclaim_application set assignedsno=null where pendingat IN (0,1,2) AND statusflag=0 AND hospitalcode in :hospitalCodes";
				q = this.entityManager.createNativeQuery(query);
				q.setParameter("hospitalCodes", hospitalCodes);
			}
			q.executeUpdate();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Response saveSNAExecutiveConfiguration(SnaExecBean bean) {
		Response response = new Response();
		int count = 0;
		int check = 0;
		for (int i = 0; i < bean.getHospList().size(); i++) {
			SNAExecutive findBySNAID = null;
			findBySNAID = snaExecutiverepository.findBySNAID(bean.getSnoId(), bean.getHospList().get(i).getUserId());
			if (findBySNAID != null) {
				check++;
			}
		}
		if (check == 0) {
			for (int i = 0; i < bean.getHospList().size(); i++) {
				SNAExecutive snaExecutive = new SNAExecutive();
				snaExecutive.setGroupId(Integer.parseInt("18"));
				snaExecutive.setCreatedBy(bean.getUserId());
				snaExecutive.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				snaExecutive.setStatus(0);
				snaExecutive.setSnauserid(bean.getSnoId());
				snaExecutive.setSnaexecutiveId(bean.getHospList().get(i).getUserId());
				snaExecutiverepository.save(snaExecutive);
				count = 1;
			}
			if (count == 1) {
				response.setMessage("SNA Executive Mapping successfully");
				response.setStatus("Success");
			} else {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} else {
			response.setMessage("SNA Executive Already Mapped");
			response.setStatus("Info");
		}

		return response;
	}

	@Override
	public JSONArray getSNAExec(Integer userId) {
		ResultSet rs = null;
		JSONArray distinctSna = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_EXECUTIVE_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNOUSERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNOUSERID", userId);
			storedProcedureQuery.setParameter("P_FLAG", 0);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");

			while (rs.next()) {
				JSONObject sna = new JSONObject();
				sna.put("snaUserId", rs.getInt(1));
				sna.put("fullname", rs.getString(2));
				sna.put("count", rs.getInt(3));
				distinctSna.put(sna);
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
		return distinctSna;
	}

	@Override
	public JSONArray getSNAExecDetails(Integer userId) {
		ResultSet rs = null;
		JSONArray snaDetails = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_EXECUTIVE_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNOUSERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNOUSERID", userId);
			storedProcedureQuery.setParameter("P_FLAG", 1);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");

			while (rs.next()) {
				JSONObject sna = new JSONObject();
				sna.put("executive_name", rs.getString(1));
				sna.put("status", rs.getInt(2));
				snaDetails.put(sna);
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
		// //System.out.println(snaDetails);
		return snaDetails;
	}

	@Override
	public SnaExecBean getSnaExecutiveById(Integer snoMappingId) {
		SnaExecBean sNOConfigurationBean = new SnaExecBean();
		ArrayList<ExecutiveList> hospList = new ArrayList<ExecutiveList>();
		ResultSet rs = null;
		try {
			sNOConfigurationBean.setSnoId(snoMappingId);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_SNA_EXECUTIVE_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SNOUSERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNOUSERID", snoMappingId);
			storedProcedureQuery.setParameter("P_FLAG", 2);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
			while (rs.next()) {
				ExecutiveList hosp = new ExecutiveList();
				hosp.setUserId(rs.getInt(1));
				hosp.setFullName(rs.getString(3));
				hosp.setSnadoctorName(rs.getString(2));
				;
				hospList.add(hosp);
			}
			sNOConfigurationBean.setHospList(hospList);
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
		return sNOConfigurationBean;
	}

	@Override
	public Response updateSnaExecDetails(SnaExecBean bean) {
		Response response = new Response();
		int count = 0;
		List<SNAExecutive> findBySNAID = snaExecutiverepository.findListBySNAID(bean.getSnoId());
		for (int i = 0; i < findBySNAID.size(); i++) {
			SNAExecutive snaExecutive = snaExecutiverepository.findById((findBySNAID.get(i).getExecutivemappingId()))
					.get();
			snaExecutive.setStatus(1);
			snaExecutiverepository.save(snaExecutive);
		}
		for (int i = 0; i < bean.getHospList().size(); i++) {
			List<SNAExecutive> snaExecutive = null;
			SNAExecutive SNAExecutive1 = null;
			snaExecutive = snaExecutiverepository.findBySNAInactiveID(bean.getSnoId(),
					bean.getHospList().get(i).getUserId());
			for (SNAExecutive sNAExecutive : snaExecutive) {
				SNAExecutive1 = sNAExecutive;
				break;
			}
			if (SNAExecutive1 != null) {
				SNAExecutive snaExecutive2 = snaExecutiverepository.findById((SNAExecutive1.getExecutivemappingId()))
						.get();
				snaExecutive2.setStatus(0);
				snaExecutive2.setUpdatedBy(bean.getUserId());
				snaExecutive2.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				snaExecutiverepository.save(snaExecutive2);
			} else {
				SNAExecutive snaExecutive3 = new SNAExecutive();
				snaExecutive3.setGroupId(Integer.parseInt("18"));
				snaExecutive3.setCreatedBy(bean.getUserId());
				snaExecutive3.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				snaExecutive3.setStatus(0);
				snaExecutive3.setSnauserid(bean.getSnoId());
				snaExecutive3.setSnaexecutiveId(bean.getHospList().get(i).getUserId());
				snaExecutive3.setUpdatedBy(bean.getUserId());
				snaExecutive3.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				snaExecutiverepository.save(snaExecutive3);
			}
			count = 1;
		}
		if (count == 1) {
			response.setMessage("SNA Executive Mapping Updated successfully");
			response.setStatus("Success");
		} else {
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}

		return response;
	}

	@Override
	public String getSnaListByExecutive(Integer userId) throws Exception {
		JSONArray snolist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_sna_list_by_executive")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (rs.next()) {
				JSONObject snoUser = new JSONObject();
				snoUser.put("executingMappingId", rs.getInt(1));
				snoUser.put("groupId", rs.getInt(2));
				snoUser.put("snaUserId", rs.getInt(3));
				snoUser.put("snaExecutiveId", rs.getInt(4));
				snoUser.put("fullName", rs.getString(5));
				snolist.put(snoUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
	public String getHospitalListById(Integer userId) throws Exception {
		JSONArray snolist = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_DC_MAPPING_HOSP")
					.registerStoredProcedureParameter("p_user_id", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (rs.next()) {
				JSONObject snoUser = new JSONObject();
				snoUser.put("hospital_id", rs.getInt(1));
				snoUser.put("hospital_code", rs.getString(2));
				snoUser.put("hospital_name", rs.getString(3));
				snolist.put(snoUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
}
