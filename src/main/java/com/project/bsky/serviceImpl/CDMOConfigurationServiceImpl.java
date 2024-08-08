package com.project.bsky.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CDMOConfigurationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CDMOConfiguration;
import com.project.bsky.repository.CDMOConfigurationRepository;
import com.project.bsky.service.CDMOConfigurationService;

@Service
public class CDMOConfigurationServiceImpl implements CDMOConfigurationService {

	@Autowired
	private CDMOConfigurationRepository cdmoConfigRepository;

	@Autowired
	private Logger logger;

	@Override
	public Response saveCDMOConfiguration(CDMOConfigurationBean cdmoConfigurationBean) {
		Response response = new Response();
		try {
			Integer count = cdmoConfigRepository.checkduplicate(cdmoConfigurationBean.getStateCode(),
					cdmoConfigurationBean.getDistrictCode());
			Integer countorder = cdmoConfigRepository.cheakduplicateorder(cdmoConfigurationBean.getCdmoId());

			if (count == 0 && countorder == 0) {
				CDMOConfiguration cdmoConfig = new CDMOConfiguration();
				cdmoConfig.setCdmoUserId(cdmoConfigurationBean.getCdmoId());
				cdmoConfig.setStateCode(cdmoConfigurationBean.getStateCode());
				cdmoConfig.setDistrictCode(cdmoConfigurationBean.getDistrictCode());
				cdmoConfig.setStatus(0);
				cdmoConfig.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				cdmoConfig.setCreatedBy(cdmoConfigurationBean.getCreatedBy());
				cdmoConfigRepository.save(cdmoConfig);
				response.setMessage("CDMO Assigned  successfully");
				response.setStatus("Success");
			} else if (countorder != 0) {
				response.setMessage("CDMO Is Already Tagged Update Instead?");
				response.setStatus("Failed");
			} else {
				response.setMessage(" CDMO Is Already Tagged To This District Update Instead?");
				response.setStatus("Failed");
			}
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<CDMOConfiguration> getDetails() {
		List<CDMOConfiguration> list = new ArrayList<CDMOConfiguration>();
		try {
			List<Object[]> list1 = cdmoConfigRepository.getDetails();
			for (Object[] x : list1) {
				CDMOConfiguration cdmoconf = new CDMOConfiguration();
				cdmoconf.setMappingId((Integer) x[0]);
				cdmoconf.setStatename((String) x[3]);
				cdmoconf.setDistname((String) x[4]);
				cdmoconf.setName((String) x[1]);
				cdmoconf.setStatus((Integer) x[2]);
				cdmoconf.setCdmoUserId((Integer) x[5]);
				cdmoconf.setStateCode((String) x[6]);
				cdmoconf.setDistrictCode((String) x[7]);
				list.add(cdmoconf);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response updateCDMOConfiguration(CDMOConfigurationBean cdmoConfigurationBean) {
		Response response = new Response();
		try {
			Integer count = cdmoConfigRepository.checkduplicate(cdmoConfigurationBean.getStateCode(),
					cdmoConfigurationBean.getDistrictCode());

			Integer countorder = cdmoConfigRepository.cheakduplicateorder(cdmoConfigurationBean.getCdmoId());

			CDMOConfiguration cdmoConfigurationBean1 = cdmoConfigRepository
					.checkduplicate1(cdmoConfigurationBean.getStateCode(), cdmoConfigurationBean.getDistrictCode());
			CDMOConfiguration cdmoConfigurationBean2 = cdmoConfigRepository
					.cheakduplicateorder1(cdmoConfigurationBean.getCdmoId());

			if (count == 0 && countorder == 0) {
				response = update(cdmoConfigurationBean);
			} else if (count != 0 && countorder == 0) {

				if (cdmoConfigurationBean.getCdmoId() == cdmoConfigurationBean1.getMappingId()
						&& cdmoConfigurationBean1.getDistrictCode().equals(cdmoConfigurationBean1.getDistrictCode())
						&& cdmoConfigurationBean.getStateCode().equals(cdmoConfigurationBean1.getStateCode())) {

					response = update(cdmoConfigurationBean);
				} else {
					response.setMessage("District already exist");
					response.setStatus("Failed");
				}
			} else if (count == 0 && countorder != 0) {

				if (cdmoConfigurationBean.getCdmoMappingId() == cdmoConfigurationBean2.getMappingId()
						&& cdmoConfigurationBean.getCdmoId().equals(cdmoConfigurationBean2.getCdmoUserId())) {

					response = update(cdmoConfigurationBean);
				} else {
					response.setMessage("CDMO Is Already Tagged Update Instead?");
					response.setStatus("Failed");
				}
			} else {

				if (cdmoConfigurationBean.getCdmoMappingId() == cdmoConfigurationBean1.getMappingId()
						&& cdmoConfigurationBean.getDistrictCode().equals(cdmoConfigurationBean1.getDistrictCode())
						&& cdmoConfigurationBean.getStateCode().equals(cdmoConfigurationBean1.getStateCode())
						&& cdmoConfigurationBean.getCdmoMappingId() == cdmoConfigurationBean2.getMappingId()
						&& cdmoConfigurationBean.getCdmoId().equals(cdmoConfigurationBean2.getCdmoUserId())) {
					response = update(cdmoConfigurationBean);
				} else {
					response.setMessage("Another CDMO Is Already Tagged To This District Update Instead?");
					response.setStatus("Failed");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
		}
		return response;
	}

	public Response update(CDMOConfigurationBean cdmoConfigurationBean) {
		Response response = new Response();
		try {

			CDMOConfiguration CDMOC = cdmoConfigRepository.findById(cdmoConfigurationBean.getCdmoMappingId()).get();
			CDMOC.setDistrictCode(cdmoConfigurationBean.getDistrictCode());
			CDMOC.setStateCode(cdmoConfigurationBean.getStateCode());
			CDMOC.setCdmoUserId(cdmoConfigurationBean.getCdmoId());
			CDMOC.setStatus(cdmoConfigurationBean.getStatus());
			CDMOC.setUpdatedBy(cdmoConfigurationBean.getUpdatedBy());
			CDMOC.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			CDMOConfiguration CDMOC1 = cdmoConfigRepository.save(CDMOC);
			if (CDMOC1 != null) {
				response.setMessage("CDMO Assigned  successfully");
				response.setStatus("Success");
			} else {
				response.setMessage("Data Not Saved! Try Again");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
		}
		return response;
	}

}
