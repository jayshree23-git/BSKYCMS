package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.SnaDoctorTagBean;
/**
 * @author jayshree.moharana
 *
 */
public interface SnaDoctorTagService {
	
	List<SnaDoctorTagBean> getlist(String userId);

	List<SnaDoctorTagBean> gethospitallist(String userId);


}
