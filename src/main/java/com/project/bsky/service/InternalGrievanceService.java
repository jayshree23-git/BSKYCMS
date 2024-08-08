/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.IntGrvUserBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.InternalGrievance;

/**
 * @author priyanka.singh
 *
 */
public interface InternalGrievanceService {

	Response saveinternalGrievanceDetails(InternalGrievance internalGrievance, MultipartFile filepath);

	List<InternalGrievance> getGrievanceDetails();

	void downLoadPassbook(String fileName, String year, HttpServletResponse response, String month);

	InternalGrievance getInternalGrievanceById(Long grievanceId);

	List<IntGrvUserBean> getgrvuserdatabytypeid(Integer typeid);

	List<Object> getGrievnceFilterDetails(String categoryType, String priority, String status,Date fromDate,Date toDate);

	Response update(String assign, String closedate, Integer status, Long id,Long updatedBy,String closingDescription);

	

}
