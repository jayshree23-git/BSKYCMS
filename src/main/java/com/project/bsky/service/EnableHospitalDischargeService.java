package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.EnableHospitalDischargeBean;
import com.project.bsky.bean.HospBean;
import com.project.bsky.bean.Response;


public interface EnableHospitalDischargeService {
	
	List<EnableHospitalDischargeBean> gettaggedhospitallist(Long userid,String state, String dist, String hospital);

	 List<HospBean> gettaggedhospitallistfosna(Long userid);

	 Response submit(EnableHospitalDischargeBean sna);

	 Response disable(EnableHospitalDischargeBean sna);


}
