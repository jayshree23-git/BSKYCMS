/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UnprocessConfigurationLog;
import com.project.bsky.model.UnprocessedConfiguration;
import com.project.bsky.repository.UnprocessConfigurationLogRepository;
import com.project.bsky.repository.UnprocessedConfigurationRepository;
import com.project.bsky.service.UnprocessedConfigurationService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class UnprocessedConfigurationServiceImpl implements UnprocessedConfigurationService {

	@Autowired
	private UnprocessedConfigurationRepository unprocessedConfigurationRepository;
	
	@Autowired
	private UnprocessConfigurationLogRepository  unprocessConfigurationLogRepository;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();



	@Override
	public Response saveUnprocessedMasterData(UnprocessedConfiguration unprocessedConfiguration) {
		Response response = new Response();
		try {
			Integer countMonth = unprocessedConfigurationRepository
					.checkDuplicateMonth(unprocessedConfiguration.getMonths(), unprocessedConfiguration.getYears());
			if (countMonth == 0) {
				unprocessedConfiguration.setCreatedOn(date);
				unprocessedConfiguration.setStatusFlag(0);
				unprocessedConfigurationRepository.save(unprocessedConfiguration);
				response.setStatus("Success");
				response.setMessage("Unprocess Successfully Inserted");
			} else {
				response.setMessage("Unprocess Month is Already Exist");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public JSONArray getDetails() {
		List<UnprocessedConfiguration> list = unprocessedConfigurationRepository.findDetails();
		JSONArray array = new JSONArray();
		try {
			for (UnprocessedConfiguration uc : list) {
				JSONObject json = new JSONObject();
				json.put("unprocessedId", uc.getUnprocessedId());
				json.put("years", uc.getYears());
				json.put("months", uc.getMonths());
				json.put("unprocessDate", uc.getUnprocessDate());
				json.put("statusFlag", uc.getStatusFlag());
				array.put(json);
			}
		} catch (JSONException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return array;
	}

	@Override
	public UnprocessedConfiguration getUnproceesedById(Long unprocessedId) {
		try {
			return unprocessedConfigurationRepository.findById(unprocessedId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}

	}



	@Override
	public Response updateUnprocessed(UnprocessedConfiguration unprocessedConfiguration) {
		Response response = new Response();
		Integer countMonth = unprocessedConfigurationRepository
				.checkDuplicateMonth(unprocessedConfiguration.getMonths(), unprocessedConfiguration.getYears());
		UnprocessedConfiguration unprocess = unprocessedConfigurationRepository.findById(unprocessedConfiguration.getUnprocessedId()).get();
		UnprocessConfigurationLog unprocessConfigurationLog= new UnprocessConfigurationLog();
		unprocessConfigurationLog.setCreatedBy(unprocess.getCreatedBy());
		unprocessConfigurationLog.setCreatedOn(unprocess.getCreatedOn());
		unprocessConfigurationLog.setMonths(unprocess.getMonths());
		unprocessConfigurationLog.setStatusFlag(unprocess.getStatusFlag());
		unprocessConfigurationLog.setYears(unprocess.getYears());
		unprocessConfigurationLog.setUpdatedOn(unprocess.getUpdatedOn());
		unprocessConfigurationLog.setUnprocessDate(unprocess.getUnprocessDate());
		unprocessConfigurationLog.setUnprocessedId(unprocess.getUnprocessedId());
		unprocessConfigurationLog.setUpdateby(unprocess.getUpdatedBy());
		
		
		
		try {

			
			if (countMonth == 0) {
				unprocessedConfiguration.setUpdatedOn(date);
				unprocessedConfiguration.setCreatedOn(unprocess.getCreatedOn());
				unprocessedConfiguration.setCreatedBy(unprocess.getCreatedBy());
				UnprocessedConfiguration u= unprocessedConfigurationRepository.save(unprocessedConfiguration);
				if(u!=null) {
					unprocessConfigurationLogRepository.save(unprocessConfigurationLog);
					response.setStatus("Success");
					response.setMessage("Unprocess Successfully Updated");
				}else {
					response.setStatus("Failed");
					response.setMessage("Something went Wrong");
				}
				
			} else if (unprocess.getMonths() == unprocessedConfiguration.getMonths()
					&& unprocess.getUnprocessedId() == unprocessedConfiguration.getUnprocessedId()) {
				unprocessedConfiguration.setUpdatedOn(date);
				unprocessedConfiguration.setCreatedOn(unprocess.getCreatedOn());
				unprocessedConfiguration.setCreatedBy(unprocess.getCreatedBy());
				UnprocessedConfiguration u= unprocessedConfigurationRepository.save(unprocessedConfiguration);
				if(u!=null) {
					unprocessConfigurationLogRepository.save(unprocessConfigurationLog);
					response.setStatus("Success");
					response.setMessage("Unprocess Successfully Updated");
				}else {
					response.setStatus("Failed");
					response.setMessage("Something went Wrong");
				}
			}else {
				response.setMessage("Unprocess Month is Already Exist");
			response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<UnprocessedConfiguration> getAllUnprocessFilterData(Long years, Long months) {
		List<UnprocessedConfiguration> getcpdListFilterAdmin = null;
		getcpdListFilterAdmin=unprocessedConfigurationRepository.findFilterUnprocess(years,months);
		return getcpdListFilterAdmin;
	}



	

}
