package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.CDMOConfigurationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CDMOConfiguration;



public interface CDMOConfigurationService {

	Response saveCDMOConfiguration(CDMOConfigurationBean cdmoConfigurationBean);

	List<CDMOConfiguration>getDetails();

	Response updateCDMOConfiguration(CDMOConfigurationBean cdmoConfigurationBean);
}
