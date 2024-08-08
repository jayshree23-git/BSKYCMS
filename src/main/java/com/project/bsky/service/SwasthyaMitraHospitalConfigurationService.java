/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SwasthyaMitraHospitalConfigurationBean;
import com.project.bsky.model.SwasthyamitraMapping;
import com.project.bsky.model.UserDetails;

/**
 * @author priyanka.singh
 *
 */
public interface SwasthyaMitraHospitalConfigurationService {

	List<UserDetails> getSwasthyaMitra();

	Response saveSwasthyaMitraDetails(SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean);

	List<SwasthyaMitraHospitalConfigurationBean> getDetails();

	Response updateSwasthyaMitra(SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean);

}
