/**
 * 
 */
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
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CPDConfigurationBean;
import com.project.bsky.bean.HospObj;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CPDConfiguration;
import com.project.bsky.repository.CPDConfigurationRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.service.CPDConfigurationService;

/**
 * @author ronauk
 *
 */
@SuppressWarnings("unchecked")
@Service
public class CPDConfigurationServiceImpl implements CPDConfigurationService {

	@Autowired
	private CPDConfigurationRepository cpdConfigrepository;

	@Autowired
	private UserDetailsCpdReposiitory cpdRepo;

	@Autowired
	private HospitalInformationRepository hospitalRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public Response saveCPDConfiguration(CPDConfigurationBean cpdConfigurationBean) {
		Response response = new Response();
		try {
			List<HospObj> hospital_List = cpdConfigurationBean.getHospList();
			for (HospObj hospital : hospital_List) {
				Integer checkCPD = cpdConfigrepository.checkCPDConfigDulicacy(hospital.getHospitalCode(),
						cpdConfigurationBean.getCpdId());
				if (checkCPD > 0) {
					if (checkCPD == 1) {
						Integer status = cpdConfigrepository.checkStatus(hospital.getHospitalCode(),
								cpdConfigurationBean.getCpdId());
						if (status == 1) {
							CPDConfiguration cpdConfig = cpdConfigrepository
									.getCpdConfig(hospital.getHospitalCode(), cpdConfigurationBean.getCpdId()).get(0);
							cpdConfig.setStateCode(hospital.getStateCode());
							cpdConfig.setDistrictCode(hospital.getDistrictCode());
							cpdConfig.setStatus(0);
							cpdConfig.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
							cpdConfig.setUpdatedBy(cpdConfigurationBean.getCreatedBy());
							cpdConfigrepository.save(cpdConfig);
						}
					} else {
						boolean flag = false;
						List<Integer> stats = cpdConfigrepository.getStats(hospital.getHospitalCode(),
								cpdConfigurationBean.getCpdId());
						for (Integer status : stats) {
							if (status == 0) {
								flag = true;
								break;
							}
						}
						if (flag == false) {
							CPDConfiguration cpdConfig = cpdConfigrepository
									.getCpdConfig(hospital.getHospitalCode(), cpdConfigurationBean.getCpdId()).get(0);
							cpdConfig.setStateCode(hospital.getStateCode());
							cpdConfig.setDistrictCode(hospital.getDistrictCode());
							cpdConfig.setStatus(0);
							cpdConfig.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
							cpdConfig.setUpdatedBy(cpdConfigurationBean.getCreatedBy());
							cpdConfigrepository.save(cpdConfig);
						}
					}
				} else {
					CPDConfiguration cpdConfig = new CPDConfiguration();
					cpdConfig.setCpdUserId(cpdConfigurationBean.getCpdId());
					cpdConfig.setHospitalCode(hospital.getHospitalCode());
					cpdConfig.setStateCode(hospital.getStateCode());
					cpdConfig.setDistrictCode(hospital.getDistrictCode());
					cpdConfig.setStatus(0);
					cpdConfig.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					cpdConfig.setCreatedBy(cpdConfigurationBean.getCreatedBy());
					cpdConfigrepository.save(cpdConfig);
				}
			}
			response.setMessage("CPD Configuration saved successfully");
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
	public CPDConfigurationBean getCpdConfigurationDetailsById(Integer cpdUserId) {
		CPDConfigurationBean cPDConfigurationBean = new CPDConfigurationBean();
		List<HospObj> hospList = new ArrayList<HospObj>();
		ResultSet rs = null;
		try {
			cPDConfigurationBean.setCpdId(cpdUserId);
			cPDConfigurationBean.setCpdName(cpdRepo.getFullName(cpdUserId));
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_CPD_MAPPING_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPDID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_CPDID", cpdUserId);
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
			cPDConfigurationBean.setHospList(hospList);
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
		return cPDConfigurationBean;
	}

	@Transactional
	@Override
	public Response updateCpdDetailsData(CPDConfigurationBean cpdConfigurationBean) {
		Response response = new Response();
		try {
			cpdConfigrepository.inActivate(cpdConfigurationBean.getCpdId(), cpdConfigurationBean.getUpdatedBy());
			List<HospObj> hospital_List = cpdConfigurationBean.getHospList();
			for (HospObj hospital : hospital_List) {
				Integer checkCPD = cpdConfigrepository.checkCPDConfigDulicacy(hospital.getHospitalCode(),
						cpdConfigurationBean.getCpdId());
				if (checkCPD > 0) {
					Integer status = cpdConfigrepository.checkStatus(hospital.getHospitalCode(),
							cpdConfigurationBean.getCpdId());
					if (status == 1) {
						CPDConfiguration cpdConfig = cpdConfigrepository
								.getCpdConfig(hospital.getHospitalCode(), cpdConfigurationBean.getCpdId()).get(0);
						cpdConfig.setStateCode(hospital.getStateCode());
						cpdConfig.setDistrictCode(hospital.getDistrictCode());
						cpdConfig.setStatus(0);
						cpdConfig.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
						cpdConfig.setUpdatedBy(cpdConfigurationBean.getUpdatedBy());
						cpdConfigrepository.save(cpdConfig);
					}
				} else {
					CPDConfiguration cpdConfig = new CPDConfiguration();
					cpdConfig.setCpdUserId(cpdConfigurationBean.getCpdId());
					cpdConfig.setHospitalCode(hospital.getHospitalCode());
					cpdConfig.setStateCode(hospital.getStateCode());
					cpdConfig.setDistrictCode(hospital.getDistrictCode());
					cpdConfig.setStatus(0);
					cpdConfig.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					cpdConfig.setCreatedBy(cpdConfigurationBean.getUpdatedBy());
					cpdConfigrepository.save(cpdConfig);
				}
			}
			response.setMessage("CPD Configuration Updated successfully");
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
	public Integer checkCpdName(Integer cpdUserId) {
		return cpdConfigrepository.checkCpdNameDulicacy(cpdUserId);
	}

	@Override
	public Integer checkHospitalName(String hospitalCode) {
		return cpdConfigrepository.checkHospitalNameDulicacy(hospitalCode);
	}

	@Override
	public Integer checkCPDHospitalName(String hospitalCode, Integer cpdId) {
		return cpdConfigrepository.checkCPDHospitalNameDulicacy(hospitalCode, cpdId);
	}

	@Override
	public List<Integer> checkCpdStatus(Integer cpdUserId) {
		return cpdConfigrepository.getStatus(cpdUserId);
	}

	@Override
	public JSONArray getDistinctCPD(Integer bskyUserId) {
		ResultSet rs = null;
		JSONArray distinctCpds = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_CPD_MAPPING_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPDID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_CPDID", bskyUserId);
			storedProcedureQuery.setParameter("P_STATEID", null);
			storedProcedureQuery.setParameter("P_DISTRICTID", null);
			storedProcedureQuery.setParameter("P_FLAG", 0);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
			while (rs.next()) {
				JSONObject cpd = new JSONObject();
				cpd.put("cpdUserId", rs.getInt(1));
				cpd.put("fullname", rs.getString(2));
				cpd.put("mobileNo", rs.getString(3));
				cpd.put("count", rs.getInt(4));
				distinctCpds.put(cpd);
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
		return distinctCpds;
	}

	@Override
	public JSONArray getCPDConfigDetails(Integer bskyUserId, String stateId, String districtId) {
		ResultSet rs = null;
		JSONArray cpdDetails = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_CPD_MAPPING_DATA")
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPDID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_RESULT_SET", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_CPDID", bskyUserId);
			storedProcedureQuery.setParameter("P_STATEID", stateId);
			storedProcedureQuery.setParameter("P_DISTRICTID", districtId);
			storedProcedureQuery.setParameter("P_FLAG", 1);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_RESULT_SET");
			while (rs.next()) {
				JSONObject cpd = new JSONObject();
				cpd.put("hospitalName", rs.getString(1));
				cpd.put("hospitalCode", rs.getString(2));
				cpd.put("stateName", rs.getString(3));
				cpd.put("districtName", rs.getString(4));
				cpd.put("periodFrom", rs.getString(5));
				cpd.put("periodTo", rs.getString(6));
				cpd.put("status", rs.getInt(7));
				cpdDetails.put(cpd);
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
		return cpdDetails;
	}

	@Override
	public String checkCPDAssignedClaims(Integer cpdId, List<HospObj> hospitals) {
		String hospName = null;
		try {
			Long userId = cpdRepo.findById(cpdId).get().getUserid().getUserId();
			List<String> hospitalCodes = hospitals.stream().map(HospObj::getHospitalCode).collect(Collectors.toList());
			String query = "select hospitalcode from txnclaim_application where assignedcpd=:userId and hospitalcode in :hospitalCodes and statusflag = 0";
			Query q = this.entityManager.createNativeQuery(query);
			q.setParameter("userId", userId);
			q.setParameter("hospitalCodes", hospitalCodes);
			List<String> list = q.getResultList();
			if (list != null && list.size() > 0) {
				String hospCode = list.get(0);
				hospName = hospitalRepo.findHospitalNameByCode(hospCode);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospName;
	}

	@Override
	public List<String> getRestrictedHospitals(Integer cpdId) {
		return cpdConfigrepository.getRestrictedHospitals(cpdId);
	}

}
