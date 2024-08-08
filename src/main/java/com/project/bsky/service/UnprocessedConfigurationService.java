/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import org.json.JSONArray;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UnprocessedConfiguration;

/**
 * @author priyanka.singh
 *
 */
public interface UnprocessedConfigurationService {

	Response saveUnprocessedMasterData(UnprocessedConfiguration unprocessedConfiguration);

	JSONArray getDetails();

	UnprocessedConfiguration getUnproceesedById(Long unprocessedId);

	Response updateUnprocessed(UnprocessedConfiguration unprocessedConfiguration);

	List<UnprocessedConfiguration> getAllUnprocessFilterData(Long years, Long months);


}
