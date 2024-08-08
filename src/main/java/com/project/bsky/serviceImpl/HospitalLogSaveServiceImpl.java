package com.project.bsky.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalCategoryMaster;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.HospitalInformationLog;
import com.project.bsky.repository.HospitalInformationLogRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.HospitalCategoryService;
import com.project.bsky.service.HospitalLogSaveService;


/**
 * @author ronauk
 *
 */
@Service
public class HospitalLogSaveServiceImpl implements HospitalLogSaveService {

	@Autowired
	private HospitalInformationLogRepository hospitalUserLogRepository;

	@Autowired
	private HospitalInformationRepository hospitalRepo;
	
	@Autowired
	private HospitalCategoryService categoryService;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private Logger logger;

	@Override
	public Response saveHospLog(Long userId, Integer createdBy) {
		// TODO Auto-generated method stub
		Response response = new Response();
		HospitalInformationLog hospitalUserSave = new HospitalInformationLog();
		try {
			HospitalInformation hospital = hospitalRepo.findByUserId(userId);
			////System.out.println(hospital.toString());
			hospitalUserSave.setHospitalCode(hospital.getHospitalCode());
			hospitalUserSave.setHospitalName(hospital.getHospitalName());
			hospitalUserSave.setHospitalId(hospital.getHospitalId());
			hospitalUserSave.setStateCode(hospital.getDistrictcode().getStatecode().getStateCode());
			hospitalUserSave.setDistrictcode(hospital.getDistrictcode().getDistrictcode());
			hospitalUserSave.setCpdApprovalRequired(hospital.getCpdApprovalRequired());
			hospitalUserSave.setMobile(hospital.getMobile());
			hospitalUserSave.setEmailId(hospital.getEmailId());
			hospitalUserSave.setUserId(hospital.getUserId().getUserId().intValue());
			hospitalUserSave.setCreatedBy(createdBy);
			hospitalUserSave.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			hospitalUserSave.setAssigned_dc(hospital.getAssigned_dc());
			hospitalUserSave.setStatus(hospital.getStatus());
			hospitalUserSave.setLatitude(hospital.getLatitude());
			hospitalUserSave.setLongitude(hospital.getLongitude());
			hospitalUserSave.setHospitalCategoryid(hospital.getHospitalCategoryid());
			hospitalUserLogRepository.save(hospitalUserSave);
			response.setMessage("Log Created Successfully");
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
	public Response saveDcConfigurationLog(Long userId, Integer createdBy, String ipAddress) {
		// TODO Auto-generated method stub
		Response response = new Response();
		try {
			List<HospitalInformation> list = hospitalRepo.getAllDcConfig(userId);
			////System.out.println("size: " + list.size());
			if (list.size() > 0) {
				for (HospitalInformation hospital : list) {
					HospitalInformationLog hospitalUserSave = new HospitalInformationLog();
					hospitalUserSave.setHospitalCode(hospital.getHospitalCode());
					hospitalUserSave.setHospitalName(hospital.getHospitalName());
					hospitalUserSave.setHospitalId(hospital.getHospitalId());
					hospitalUserSave.setStateCode(hospital.getDistrictcode().getStatecode().getStateCode());
					hospitalUserSave.setDistrictcode(hospital.getDistrictcode().getDistrictcode());
					hospitalUserSave.setCpdApprovalRequired(hospital.getCpdApprovalRequired());
					hospitalUserSave.setMobile(hospital.getMobile());
					hospitalUserSave.setEmailId(hospital.getEmailId());
					hospitalUserSave.setUserId(hospital.getUserId()!=null?hospital.getUserId().getUserId().intValue():null);
					hospitalUserSave.setCreatedBy(createdBy);
					hospitalUserSave.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					hospitalUserSave.setAssigned_dc(userId);
					hospitalUserSave.setStatus(hospital.getStatus());
					hospitalUserSave.setLatitude(hospital.getLatitude());
					hospitalUserSave.setLongitude(hospital.getLongitude());
					hospitalUserSave.setHospitalCategoryid(hospital.getHospitalCategoryid());
					hospitalUserLogRepository.save(hospitalUserSave);
				}
			}
			response.setStatus("success");
			response.setMessage("Log created successfully");
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage("Some error happened");
			return response;
		}
	}

	@Override
	public List<HospitalInformationLog> getgethostlogdata(Integer hospid) {
		List<HospitalInformationLog> list=new ArrayList<HospitalInformationLog>();
		try {
			list=hospitalUserLogRepository.findByhospitalId(hospid);
			for(HospitalInformationLog data:list) {
				if(data.getAssigned_dc()!=null) {
				data.setDcname(userDetailsRepository.findById(data.getAssigned_dc()).get().getFullname());
				}else {
					data.setDcname("N/A");
				}
				if(data.getCreatedBy()!=null) {
					data.setCreatename(userDetailsRepository.findById(Long.valueOf(data.getCreatedBy())).get().getFullname());
					}else {
						data.setCreatename("N/A");
					}
				if (data.getHospitalCategoryid() != null) {
					HospitalCategoryMaster category=categoryService.findCategoryNameById(Long.valueOf(data.getHospitalCategoryid()));
					data.setTypename(category.getHospitalCategoryName());
					}else {
						data.setTypename("N/A");
					}
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalBean> getincentivelogdata(Integer hospid) {
		List<HospitalBean> list=new ArrayList<HospitalBean>();
		try {
			List<Object[]> objlist=hospitalUserLogRepository.getincentivelogdata(hospid);
			for(Object[] obj:objlist) {
				HospitalBean log=new HospitalBean();
				log.setHcvalidform((String) obj[0]);
				log.setHcvalidto((String) obj[1]);
				log.setCreateon((String) obj[2]);
				log.setHospitalType((String) obj[3]);
				list.add(log);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

}
