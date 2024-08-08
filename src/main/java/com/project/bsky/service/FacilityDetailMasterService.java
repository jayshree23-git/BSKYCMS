/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.FacilityDetailBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.FacilityDetails;

/**
 * @author priyanka.singh
 *
 */
public interface FacilityDetailMasterService {

	Response saveFaciltyData(FacilityDetailBean facilityDetailBean);

	List<FacilityDetails> getAllFacilityDetail();

	FacilityDetails getActionById(Integer facilityDetailId);

	Response updateFacilityDetail(FacilityDetailBean facilityDetailBean);

}
