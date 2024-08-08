package com.project.bsky.serviceImpl;

import com.google.gson.Gson;
import com.project.bsky.model.MSTMailService;
import com.project.bsky.model.MSTMailServiceConfig;
import com.project.bsky.model.MailServiceErrorLog;
import com.project.bsky.model.MailServiceLog;
import com.project.bsky.repository.MSTMailServiceConfigRepository;
import com.project.bsky.repository.MSTMailServiceRepository;
import com.project.bsky.repository.MailServiceErrorLogRepository;
import com.project.bsky.repository.MailServiceLogRepository;
import com.project.bsky.service.MailingService;
import com.project.bsky.util.JwtUtil;
import com.project.bsky.util.MailUtils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 03/07/2023 - 2:46 PM
 */
@Service
public class MailingServiceImpl implements MailingService {

	@Autowired
	private Logger logger;

	@Autowired
	private JwtUtil util;

	@Autowired
	private MSTMailServiceRepository mstMailServiceRepository;
	@Autowired
	private MSTMailServiceConfigRepository mstMailServiceConfigRepository;

	@Autowired
	private MailServiceLogRepository mailServiceLogRepository;

	@Autowired
	private MailServiceErrorLogRepository mailServiceErrorLogRepository;

	@Override
	public List<Map<String, Object>> getMailServiceList() {
		// logger.info("Inside getMailServiceList of MailingServiceImpl");
		try {
			return mstMailServiceRepository.findAllActiveMailService().stream().map(MSTMailService::toMap)
					.collect(Collectors.toList());
		} catch (Exception e) {
			// logger.info("Exception Occurred in getMailServiceList of MailingServiceImpl"
			// + e.getMessage());
			throw e;
		}
	}

	@Override
	public int saveMailServiceData(Map<String, Object> request) throws Exception {
		// logger.info("Inside saveMailServiceData of MailingServiceImpl");

		try {
			int statusFlag = Integer.parseInt(request.getOrDefault("activeStatus", 0).toString());

			if (statusFlag == 1) {
				int mailServiceId = Integer.parseInt(request.getOrDefault("id", "0").toString());
				MSTMailServiceConfig mstMailServiceConfig = mstMailServiceConfigRepository
						.findByMailServiceConfigById(mailServiceId).orElse(null);

				if (mstMailServiceConfig != null) {
					throw new Exception("Mail service is already in use and cannot be deleted!");
				}
			}

			MSTMailService mstMailService;

			if (request.containsKey("id") && request.get("id") != null) {
				int mailServiceId = Integer.parseInt(request.get("id").toString());
				mstMailService = mstMailServiceRepository.findById(mailServiceId)
						.orElseThrow(() -> new Exception("Mail service not found"));

				mstMailService.setUpdatedBy("System");
				mstMailService.setUpdatedOn(new Date());
			} else {
				mstMailService = new MSTMailService();
				mstMailService.setCreatedBy("System");
				mstMailService.setCreatedOn(new Date());
			}

			mstMailService.setStatusFlag(statusFlag);
			mstMailService.setMailServiceName(Objects.toString(request.get("mailServiceName"), null));
			mstMailService.setMailDescription(Objects.toString(request.get("mailServiceDesc"), null));

			return mstMailServiceRepository.save(mstMailService).getId();
		} catch (Exception e) {
			// logger.info("Exception occurred in saveMailServiceData of MailingServiceImpl:
			// " + e.getMessage());
			throw e;
		}
	}

	@Override
	public Map<String, Object> getMailServiceDataById(int id) throws Exception {
		// logger.info("Inside getMailServiceDataById of MailingServiceImpl");
		try {
			return mstMailServiceRepository.findById(id).map(MSTMailService::toMap).orElse(null);
		} catch (Exception e) {
			// logger.info("Exception Occurred in getMailServiceDataById of
			// MailingServiceImpl" + e.getMessage());
			throw e;
		}
	}

	@Override
	public List<Map<String, Object>> getMailServiceNameList() {
		// logger.info("Inside getMailServiceNameList of MailingServiceImpl");
		try {
			return mstMailServiceRepository.getMailServiceNameList().stream().map(MSTMailService::toMap)
					.collect(Collectors.toList());
		} catch (Exception e) {
			// logger.info("Exception Occurred in getMailServiceNameList of
			// MailingServiceImpl" + e.getMessage());
			throw e;
		}
	}

	@Override
	public int saveMailServiceConfigData(Map<String, Object> request) throws Exception {
		// logger.info("Inside saveMailServiceConfigData of MailingServiceImpl");
		try {
			int statusFlag = Integer.parseInt(request.getOrDefault("activeStatus", 0).toString());
			int mailServiceConfigId = Integer.parseInt(request.getOrDefault("id", "0").toString());
			MSTMailServiceConfig mstMailServiceConfig = null;

			if (mailServiceConfigId != 0) {
				mstMailServiceConfig = mstMailServiceConfigRepository.getMailServiceConfigDataById(mailServiceConfigId)
						.orElse(null);
			}

			if (mstMailServiceConfig == null) {
				mstMailServiceConfig = new MSTMailServiceConfig();
				mstMailServiceConfig.setCreatedOn(new Date());
				mstMailServiceConfig.setCreatedBy(Integer.parseInt(request.get("createdBy").toString()));
			}

			mstMailServiceConfig.setStatusFlag(statusFlag);
			mstMailServiceConfig.setMstMailService(mstMailServiceRepository
					.findById(Integer.parseInt(request.get("mailServiceId").toString())).orElse(null));
			mstMailServiceConfig.setMailSubject(Objects.toString(request.get("mailSubject"), null));
			mstMailServiceConfig.setMailBody(Objects.toString(request.get("mailBody"), null));
			mstMailServiceConfig.setMailCcRecipient(new Gson().toJson(request.get("mailCCRecipient")));
			mstMailServiceConfig.setMailBccRecipient(new Gson().toJson(request.get("mailBCCRecipient")));
			mstMailServiceConfig.setMailFrequency(Objects.toString(request.get("mailServiceFrequency"), null));

			int mailServiceFrequency = Integer.parseInt(request.get("mailServiceFrequency").toString());
			if (mailServiceFrequency == 1)
				mstMailServiceConfig.setMailTime(Objects.toString(request.get("mailServiceStartTime"), null));
			else if (mailServiceFrequency == 5) {
				mstMailServiceConfig.setMailFrequencyFrom((Date) request.get("mailServiceFrequencyFrom"));
				mstMailServiceConfig.setMailFrequencyTo((Date) request.get("mailServiceFrequencyTo"));
			}

			mstMailServiceConfig.setUpdatedBy(Integer.parseInt(request.get("createdBy").toString()));
			mstMailServiceConfig.setUpdatedOn(new Date());

			return mstMailServiceConfigRepository.save(mstMailServiceConfig).getId();
		} catch (Exception e) {
			// logger.info("Exception Occurred in saveMailServiceConfigData of
			// MailingServiceImpl" + e.getMessage());
			throw e;
		}
	}

	@Override
	public List<Map<String, Object>> getMailServiceConfigList() {
		// logger.info("Inside getMailServiceConfigList of MailingServiceImpl");
		try {
			return mstMailServiceConfigRepository.getAllMailServiceConfig().stream().map(MSTMailServiceConfig::toMap)
					.collect(Collectors.toList());
		} catch (Exception e) {
			// logger.info("Exception Occurred in getMailReportList of MailingServiceImpl" +
			// e.getMessage());
			throw e;
		}
	}

	@Override
	public Map<String, Object> getMailServiceConfigDataById(int id) throws Exception {
		// logger.info("Inside getMailServiceById of MailingServiceImpl");
		try {
			MSTMailServiceConfig mstMailServiceConfig = mstMailServiceConfigRepository.getMailServiceConfigDataById(id)
					.orElseThrow(() -> new Exception("Mail service config not found"));

			return mstMailServiceConfig.toMap();
		} catch (Exception e) {
			// logger.info("Exception Occurred in getMailServiceById of MailingServiceImpl"
			// + e.getMessage());
			throw e;
		}
	}

	@Override
	public void sendInterViewMail(String subject, String mailId, String emailBody) throws IOException {
		try {

			MailServiceLog mailServiceLog = new MailServiceLog();
			mailServiceLog.setMailServiceId(1);
			mailServiceLog.setMailServiceName(subject);
			mailServiceLog.setUserId(null);
			mailServiceLog.setMailDate(new Date());
			mailServiceLog.setCratedBy(String.valueOf(util.getCurrentUser()));
			mailServiceLog.setCreatedOn(new Date());

			sendInterViewMails(mailServiceLog, mailId, emailBody, null);
		} catch (

		Exception e) {
			logger.error("Exception Occurred while sending mail to CPD User in MailingServiceImpl : ", e);
//			throw e;
		}
	}

	public void sendInterViewMails(MailServiceLog mailServiceLog, String mailId, String emailBody, String path) {
		try {
			Map<String, Object> response = MailUtils.sendMail(null, emailBody, mailId, null, path);
			if ((boolean) response.get("status"))
				mailServiceLog.setMailStatus(0);
			else {
				mailServiceLog.setMailStatus(1);
				mailServiceLog.setMailStatus(1);
				MailServiceErrorLog mailServiceErrorLog = new MailServiceErrorLog();
				mailServiceErrorLog.setMailServiceId(1);
				mailServiceErrorLog.setErrorLogDate(new Date());
				mailServiceErrorLog.setCreatedBy(String.valueOf(util.getCurrentUser()));
				mailServiceErrorLog.setCreatedOn(new Date());
				mailServiceErrorLog.setStatusFlag(0);
				mailServiceErrorLog.setErrorLogMessage((String) response.get("errorMessage"));
				mailServiceLog.setErrorLogId(mailServiceErrorLogRepository.save(mailServiceErrorLog).getId());
			}
		} catch (Exception e) {
			logger.error("Exception Occurred while sending mail to CPD User in MailingServiceImpl : ", e);
		} finally {
			mailServiceLogRepository.save(mailServiceLog);
		}
	}

}
