package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospitalGroupAuthBean;
import com.project.bsky.bean.HospitalGroupBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.claimtrackingdetails;
import com.project.bsky.model.HospitalAuthTagging;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.repository.HospitalAuthTaggingRepo;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.service.HospitalAuthorityService;

@Service
public class HospitalAuthServiceImpl implements HospitalAuthorityService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private HospitalAuthTaggingRepo hosAuthTaggingRepo;
	
	@Autowired
	private UserDetailsProfileRepository userDetails;
	
	@Autowired
	private HospitalInformationRepository hospRepo;

	@Autowired
	private Logger logger;
	
	@Override
	public List<UserDetailsProfile> getHospitalAuthDetails() {
		
		return userDetails.findAllHospitalAuthority();
	}

	@Override
	public List<HospitalInformation> getHospitalUserDetails() {
		
		List<HospitalInformation> list=hospRepo.findHospitalUser();
		List<HospitalInformation> list2=new ArrayList<HospitalInformation>();
		for (HospitalInformation x:list) {			
			x.setHospitalName(x.getHospitalName()+ " ("+x.getHospitalCode()+")");
			list2.add(x);
		}		
		return list2;
	}
	
	@Override
	public Response saveConfiguration(HospitalGroupAuthBean hospitalGroupBean) {
		HospitalAuthTagging authTagging = null;
		Response response = new Response();
		try {
			List<HospitalGroupBean> hospital_List = hospitalGroupBean.getGroup();
			for (HospitalGroupBean hospital : hospital_List) {
				Integer checkHosp = hosAuthTaggingRepo.checkHospitalConfigDuplicate(hospital.getHosCode(), hospital.getAuthId());
				if(checkHosp==0) {
					authTagging = new HospitalAuthTagging();
					authTagging.setGroupId(hospital.getType());
					authTagging.setUserId(hospital.getAuthId());
					authTagging.setTagHospitalCode(hospital.getHosCode());
					authTagging.setCreatedBy(hospital.getCreatedUser());
					authTagging.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					authTagging.setStatus(0);
					hosAuthTaggingRepo.save(authTagging);
					if(checkHosp>1) {
						Integer status = hosAuthTaggingRepo.checkStatus(hospital.getHosCode(), hospital.getAuthId());
						if (status == 1) {
							authTagging = new HospitalAuthTagging();
							authTagging.setGroupId(hospital.getType());
							authTagging.setUserId(hospital.getAuthId());
							authTagging.setTagHospitalCode(hospital.getHosCode());
							authTagging.setCreatedBy(hospital.getCreatedUser());
							authTagging.setCreatedOn(new Timestamp(System.currentTimeMillis()));
							authTagging.setStatus(0);
							hosAuthTaggingRepo.save(authTagging);
						}
					}
				}
			}
			response.setMessage("Hospital Authority Mapping saved successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
			return response;
		}
	}

	@Transactional
	@Override
	public Response updateConfiguration(HospitalGroupAuthBean hospitalGroupBean) {
		Response response = new Response();
		try {
			List<HospitalGroupBean> hospital_List = hospitalGroupBean.getGroup();
			HospitalGroupBean authTaging = hospital_List.get(0);
			hosAuthTaggingRepo.inActivate(authTaging.getAuthId(), authTaging.getCreatedUser());
			for (HospitalGroupBean hospital : hospital_List) {
				Integer checkHosp = hosAuthTaggingRepo.checkUpdateHospitalConfigDuplicate(hospital.getHosCode(), hospital.getAuthId());
				if (checkHosp > 0) {
					Integer status = hosAuthTaggingRepo.checkStatus(hospital.getHosCode(), hospital.getAuthId());
					if (status == 1) {
						HospitalAuthTagging authTagging = hosAuthTaggingRepo.getHosConfig(hospital.getHosCode(), hospital.getAuthId()).get(0);
						authTagging.setGroupId(hospital.getType());
						authTagging.setUserId(hospital.getAuthId());
						authTagging.setTagHospitalCode(hospital.getHosCode());
						authTagging.setUpdatedBy(hospital.getCreatedUser());
						authTagging.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
						authTagging.setStatus(0);
						hosAuthTaggingRepo.save(authTagging);
					}
				}else {
					HospitalAuthTagging	authTagging = new HospitalAuthTagging();
					authTagging.setGroupId(hospital.getType());
					authTagging.setUserId(hospital.getAuthId());
					authTagging.setTagHospitalCode(hospital.getHosCode());
					authTagging.setCreatedBy(hospital.getCreatedUser());
					authTagging.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					authTagging.setStatus(0);
					hosAuthTaggingRepo.save(authTagging);
				}
			}
			response.setMessage("Hospital Authority Mapping saved successfully");
			response.setStatus("Success");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
			return response;
		}
	}

	@Override
	public Integer checkHospitalName(String hospitalCode) {
		
		return hosAuthTaggingRepo.findDuplicate(hospitalCode);
	}

	@Override
	public String getHospitalAuthData(Long userId) {		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_GET_HSPTL_AUTH_TAGGING")
					.registerStoredProcedureParameter("P_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_ID", userId);
			storedProcedureQuery.setParameter("P_FLAG", 0);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");

			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("AuthUserId", rs.getInt(1));
				jsonObject.put("fullname", rs.getString(2));
				jsonObject.put("count", rs.getInt(3));
				jsonArray.put(jsonObject);

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return jsonArray.toString();
	}

	@Override
	public String getConfigurationDetailsById(Integer userId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;
		ResultSet rs = null;

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_GET_HSPTL_AUTH_TAGGING")
					.registerStoredProcedureParameter("P_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);
			
			storedProcedureQuery.setParameter("P_ID", userId);
			storedProcedureQuery.setParameter("P_FLAG", 1);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");

			while (rs.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("hospitalCode", rs.getString(1));
				jsonObject.put("fullname", rs.getString(2));
				jsonObject.put("hospitalName", rs.getString(3));
				jsonObject.put("authId", rs.getInt(4));
				jsonObject.put("taggingId", rs.getInt(5));
				jsonObject.put("status", rs.getInt(6));
				jsonObject.put("type", rs.getInt(7));
				jsonArray.put(jsonObject);

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return jsonArray.toString();
	}

	@Override
	public List<Object> getadminclaimTracking(Integer bskyUserId, String hospitalCode,Date fromDate, Date toDate,String urn ) {
		
		DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		if(hospitalCode.trim().equalsIgnoreCase("undefined")) {
			hospitalCode = null;
		}
		ResultSet trackObj = null;
		List<Object> track = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ADMIN_CLM_TRACKING_RPT")
					.registerStoredProcedureParameter("p_bskyUserId", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospital_code",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_bskyUserId",bskyUserId );
			storedProcedureQuery.setParameter("p_hospital_code",hospitalCode);
			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date",toDate);
			storedProcedureQuery.setParameter("p_urn", urn);
			storedProcedureQuery.execute();
			trackObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_P_MSGOUT");
			while (trackObj.next()) {
				claimtrackingdetails rcBean=new claimtrackingdetails();
				rcBean.setClaim_no(trackObj.getString(1));
				rcBean.setUrn(trackObj.getString(2));
				rcBean.setPatientname(trackObj.getString(3));
				if(trackObj.getString(4)!=null) {
					rcBean.setActualdateofadmission(f.format(new Date(trackObj.getTimestamp(4).getTime())));
				}
				if(trackObj.getString(5)!=null) {
					rcBean.setActualdateofdischarge(f.format(new Date(trackObj.getTimestamp(5).getTime())));
				}
				rcBean.setPackagecode("0500"+trackObj.getString(6));
				rcBean.setCreatedon(trackObj.getString(7));
				rcBean.setTotalamountclaimed(trackObj.getString(8));
				rcBean.setClaimid(trackObj.getLong(9));
				rcBean.setAuthorizedcode(trackObj.getString(10).substring(2));
				rcBean.setHospitalcode(trackObj.getString(11));
				rcBean.setPackagename(trackObj.getString(13));
				
				track.add(rcBean);

			}

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		finally {
			try {
				if (trackObj != null)
					trackObj.close();
			}
			catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));

			}
		}
		return track;
	}

}
