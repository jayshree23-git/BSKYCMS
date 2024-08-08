package com.project.bsky.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.CPDConfiguration;
import com.project.bsky.model.CPDConfigurationLog;
import com.project.bsky.repository.CPDConfigurationLogRepository;
import com.project.bsky.repository.CPDConfigurationRepository;
import com.project.bsky.service.CPDConfigurationLogService;

/**
 * @author ronauk
 *
 */
@Service
public class CPDConfigurationLogServiceImpl implements CPDConfigurationLogService {

	@Autowired
	private CPDConfigurationRepository confRepo;

	@Autowired
	private CPDConfigurationLogRepository logRepo;

	@Autowired
	private Logger logger;

	@Override
	public Response saveConfigurationLog(Integer userId, Integer createdBy, String ipAddress) {
		Response response = new Response();
		try {
			List<CPDConfiguration> list = confRepo.getAllCpdConfig(userId);
			if (list.size() > 0) {
				for (CPDConfiguration conf : list) {
					CPDConfigurationLog log = new CPDConfigurationLog();
					log.setMappingId(conf.getMappingId());
					log.setCpdUserId(userId);
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

}
