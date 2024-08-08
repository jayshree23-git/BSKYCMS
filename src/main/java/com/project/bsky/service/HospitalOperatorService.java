/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserDetailsProfileBean;
import com.project.bsky.model.HospitalOperator;

/**
 * @author rajendra.sahoo
 *
 */
public interface HospitalOperatorService {

	Response savehospitaloperator(UserDetailsProfileBean bean) throws Exception;

	List<HospitalOperator> findbyhospitalcode(String hospitalcode);

	List<HospitalOperator> getAllHospitaloperatorforapprove(Integer groupid, Long userid) throws Exception;

	HospitalOperator gethospoperatorbyid(Long operatorid) throws Exception;

	Response takeactiononhospitaloperatorlist(Long operatorid, Integer action, Long createby) throws Exception;

	Integer updatehospitaloperator(UserDetailsProfileBean bean) throws Exception;

	List<Object> gethospwiseoperatorcount(String state, String dist, String hospital, Long userid) throws Exception;

	Long getoperatorid(Long userId);

	List<Object> gethospwiseoperatorlistreport(String state, String dist, String hospital, Long userid)
			throws Exception;
}
