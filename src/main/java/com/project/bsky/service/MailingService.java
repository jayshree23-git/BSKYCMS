package com.project.bsky.service;

import org.apache.catalina.LifecycleState;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 03/07/2023 - 2:46 PM
 */
public interface MailingService {
	List<Map<String, Object>> getMailServiceList();

	int saveMailServiceData(Map<String, Object> request) throws Exception;

	Map<String, Object> getMailServiceDataById(int id) throws Exception;

	List<Map<String, Object>> getMailServiceNameList();

	int saveMailServiceConfigData(Map<String, Object> request) throws Exception;

	List<Map<String, Object>> getMailServiceConfigList();

	Map<String, Object> getMailServiceConfigDataById(int id) throws Exception;

	void sendInterViewMail(String subject, String mailId, String emailBody) throws IOException;
}
