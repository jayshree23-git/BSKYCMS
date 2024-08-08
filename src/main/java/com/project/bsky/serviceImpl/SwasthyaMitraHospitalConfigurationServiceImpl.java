/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SwasthyaMitraHospitalConfigurationBean;
import com.project.bsky.model.SwasthyamitraMapping;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.SwasthyamitraMappingRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.SwasthyaMitraHospitalConfigurationService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class SwasthyaMitraHospitalConfigurationServiceImpl implements SwasthyaMitraHospitalConfigurationService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private SwasthyamitraMappingRepository swasthyamitraMappingRepository;
	
	@Autowired
	private Logger logger;

	@Override
	public List<UserDetails> getSwasthyaMitra() {
		List<UserDetails> list=new ArrayList<UserDetails>();
		try {
		list=userDetailsRepository.finddetailsSwasthyaMitra();
			for(UserDetails x:list) {
				x.setFullname(x.getFullname()+" ("+x.getUserName()+")");
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response saveSwasthyaMitraDetails(
			SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean) {
		

		Response response = new Response();
		try {

			SwasthyamitraMapping swasthyamitraMapping = new SwasthyamitraMapping();

			Long countUserid = swasthyamitraMappingRepository
					.countRowForCheckDuplicateType((swasthyaMitraHospitalConfigurationBean.getSwasthyaId()));
			////System.out.println(countUserid);
			
			Integer countHospital=swasthyamitraMappingRepository.checkduplicateHospital(swasthyaMitraHospitalConfigurationBean.getHospitalCode());
			////System.out.println(countHospital);
			if (countUserid == 0 && countHospital==0 ) {
				Calendar calendar = Calendar.getInstance();
				UserDetails userdetails1 = userDetailsRepository.findById(Long.parseLong(swasthyaMitraHospitalConfigurationBean.getCreatedBy())).get();
				swasthyamitraMapping.setHospitalcode(swasthyaMitraHospitalConfigurationBean.getHospitalCode());
				swasthyamitraMapping.setStatusflag(0);
				swasthyamitraMapping.setCreateon(calendar.getTime());
				swasthyamitraMapping.setCreateby(Integer.parseInt(swasthyaMitraHospitalConfigurationBean.getCreatedBy()));
				swasthyamitraMapping.setUpdateon(null);
				swasthyamitraMapping.setUpdateby(null);
				UserDetails userdetails2 = userDetailsRepository.findById(swasthyaMitraHospitalConfigurationBean.getSwasthyaId()).get();
				swasthyamitraMapping.setUserdetails(userdetails2);
				swasthyamitraMappingRepository.save(swasthyamitraMapping);
				response.setStatus("Success");
				response.setMessage("Swasthya Mitra Mapped Successfully");

			}else {
				
				response.setStatus("Failed");
				response.setMessage(" Already Exist");
			}
			
			
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
			
		}
		return response;

	}


	@Override
	public List<SwasthyaMitraHospitalConfigurationBean> getDetails() {
		
		List<SwasthyaMitraHospitalConfigurationBean> list = new ArrayList<SwasthyaMitraHospitalConfigurationBean>();
		try {
			List<Object[]> list1 = swasthyamitraMappingRepository.findDetails();
			for (Object[] x : list1) {
				SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean = new SwasthyaMitraHospitalConfigurationBean();
				swasthyaMitraHospitalConfigurationBean.setMappingId((Long) x[0]);
				swasthyaMitraHospitalConfigurationBean.setHospitalCode((String) x[1]);
				swasthyaMitraHospitalConfigurationBean.setUseId(String.valueOf(x[2]));
				swasthyaMitraHospitalConfigurationBean.setStateFlg(String.valueOf(x[3]));
				swasthyaMitraHospitalConfigurationBean.setFullName((String) x[4]);
				swasthyaMitraHospitalConfigurationBean.setHospitalName((String) x[5]);
				swasthyaMitraHospitalConfigurationBean.setDistCode((String) x[6]);

				swasthyaMitraHospitalConfigurationBean.setStatCode((String) x[7]);
				swasthyaMitraHospitalConfigurationBean.setStateName((String) x[8]);
				swasthyaMitraHospitalConfigurationBean.setDistrictName((String) x[9]);

				list.add(swasthyaMitraHospitalConfigurationBean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;

	}

	@Override
	public Response updateSwasthyaMitra(SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean) {
		
		Response response = new Response();

		try {
			Long countUserid = swasthyamitraMappingRepository
					.countRowForCheckDuplicateType(Long.parseLong(swasthyaMitraHospitalConfigurationBean.getUseId()));
			Integer countHospital = swasthyamitraMappingRepository
					.checkduplicateHospital(swasthyaMitraHospitalConfigurationBean.getHospitalCode());
			if (countUserid == 0 && countHospital == 0) {
				// allow
				response = update(swasthyaMitraHospitalConfigurationBean);
			} else if (countUserid == 0 && countHospital != 0) {
				SwasthyamitraMapping swas1 = swasthyamitraMappingRepository
						.checkduplicateHospitala(swasthyaMitraHospitalConfigurationBean.getHospitalCode());
				if (swas1.getHospitalcode().equals(swasthyaMitraHospitalConfigurationBean.getHospitalCode())
						&& swas1.getMappingId() == swasthyaMitraHospitalConfigurationBean.getSwasthyaId()) {
					response = update(swasthyaMitraHospitalConfigurationBean);
				} else {
					response.setStatus("Failed");
					response.setMessage(" Hospital Already Assigned To a SwasthyaMitra");
				}

			} else if (countUserid != 0 && countHospital == 0) {
				SwasthyamitraMapping swas2 = swasthyamitraMappingRepository.countRowForCheckDuplicateTypea(
						Long.parseLong(swasthyaMitraHospitalConfigurationBean.getUseId()));
				if (swas2.getUserdetails().getUserId() == Long
						.parseLong(swasthyaMitraHospitalConfigurationBean.getUseId())
						&& swas2.getMappingId() == swasthyaMitraHospitalConfigurationBean.getSwasthyaId()) {
					response = update(swasthyaMitraHospitalConfigurationBean);
				} else {
					response.setStatus("Failed");
					response.setMessage("SwasthyaMitra Already Assigned To a Hospital");
				}
			} else {
				SwasthyamitraMapping swas1 = swasthyamitraMappingRepository
						.checkduplicateHospitala(swasthyaMitraHospitalConfigurationBean.getHospitalCode());
				SwasthyamitraMapping swas2 = swasthyamitraMappingRepository.countRowForCheckDuplicateTypea(
						Long.parseLong(swasthyaMitraHospitalConfigurationBean.getUseId()));

				if (swas1.getHospitalcode().equals(swasthyaMitraHospitalConfigurationBean.getHospitalCode())
						&& swas1.getMappingId() == swasthyaMitraHospitalConfigurationBean.getSwasthyaId()
						&& swas2.getUserdetails().getUserId() == Long
								.parseLong(swasthyaMitraHospitalConfigurationBean.getUseId())
						&& swas2.getMappingId() == swasthyaMitraHospitalConfigurationBean.getSwasthyaId()) {
					response = update(swasthyaMitraHospitalConfigurationBean);
				} else {
					response.setStatus("Failed");
					response.setMessage("Both Are Already Assigned");
				}

			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

	Response update(SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean) {
		Response response = new Response();
		try {
			SwasthyamitraMapping swasthyamitraMapping = swasthyamitraMappingRepository
					.findById(swasthyaMitraHospitalConfigurationBean.getSwasthyaId()).get();
			UserDetails userdetails1 = userDetailsRepository
					.findById(Long.parseLong(swasthyaMitraHospitalConfigurationBean.getUseId())).get();
			swasthyamitraMapping.setHospitalcode(swasthyaMitraHospitalConfigurationBean.getHospitalCode());
			swasthyamitraMapping.setUpdateby(Integer.parseInt(swasthyaMitraHospitalConfigurationBean.getUpdatedBy()));
			Calendar calendar = Calendar.getInstance();
			swasthyamitraMapping.setUpdateon(calendar.getTime());
			swasthyamitraMapping.setStatusflag(Integer.parseInt(swasthyaMitraHospitalConfigurationBean.getStateFlg()));
			swasthyamitraMapping.setUserdetails(userdetails1);
			////System.out.println(swasthyaMitraHospitalConfigurationBean);
			swasthyamitraMappingRepository.save(swasthyamitraMapping);
			response.setStatus("Success");
			response.setMessage("Swasthya Mitra Mapped Successfully");
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

}
