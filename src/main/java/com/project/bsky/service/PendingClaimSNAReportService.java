/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import org.json.JSONArray;



/**
 * @author priyanka.singh
 *
 */
public interface PendingClaimSNAReportService {

	JSONArray getHospitalListBySNOId(Integer snoUserId);

	List<Object> getPendingSnoClaimDetails(Long snoUserId, String hospitalCode);

}
