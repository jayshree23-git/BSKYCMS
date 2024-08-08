package com.project.bsky.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.SNOConfiguration;
import com.project.bsky.model.SNOConfigurationLog;
import com.project.bsky.repository.SNOConfigurationLogRepository;
import com.project.bsky.repository.SNOConfigurationRepository;
import com.project.bsky.service.SNOConfigurationLogService;

/**
 * @author ronauk
 *
 */
@Service
public class SNOConfigurationLogServiceImpl implements SNOConfigurationLogService {

	@Autowired
	private SNOConfigurationRepository confRepo;

	@Autowired
	private SNOConfigurationLogRepository logRepo;
	
	@Autowired
	private Logger logger;

	@Override
	public Response saveConfigurationLog(Integer userId, Integer createdBy, String ipAddress) {
		Response response = new Response();
		try {
			List<SNOConfiguration> list = confRepo.getAllSnoConfig(userId);
			if (list.size() > 0) {
				for (SNOConfiguration conf : list) {
					SNOConfigurationLog log = new SNOConfigurationLog();
					log.setMappingId(conf.getMappingId());
					log.setSnoUserId(userId);
					log.setHospitalCode(conf.getHospitalCode());
					log.setDistrictCode(conf.getDistrictCode());
					log.setStateCode(conf.getStateCode());
					log.setStatus(conf.getStatus());
					log.setUpdatedBy(conf.getUpdatedBy());
					log.setUpdatedOn(conf.getUpdatedOn());
					log.setCreatedBy(createdBy);
					log.setCreatedOn(new Timestamp(System.currentTimeMillis()));
					log.setSystemIp(ipAddress);
					logRepo.save(log);
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
	public Response saveConfigurationLogForHospital(String hospitalCode, Integer createdBy, String ipAddress) {
		Response response = new Response();
		try {
			SNOConfiguration conf = confRepo.getSnoConfFromHospCode(hospitalCode);
			if (conf != null) {
				SNOConfigurationLog log = new SNOConfigurationLog();
				log.setMappingId(conf.getMappingId());
				log.setSnoUserId(conf.getSnoUserId());
				log.setHospitalCode(hospitalCode);
				log.setDistrictCode(conf.getDistrictCode());
				log.setStateCode(conf.getStateCode());
				log.setStatus(conf.getStatus());
				log.setUpdatedBy(conf.getUpdatedBy());
				log.setUpdatedOn(conf.getUpdatedOn());
				log.setCreatedBy(createdBy);
				log.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				log.setSystemIp(ipAddress);
				logRepo.save(log);
				response.setStatus("success");
				response.setMessage("Log created successfully");
			} else {
				response.setStatus("success");
				response.setMessage("SNA not tagged to hospital");
			}
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage("Some error happened");
			return response;
		}
	}

}
