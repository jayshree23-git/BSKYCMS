package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.DCResetPasswordBean;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.service.DCResetPasswordService;

@Service
public class DCResetPasswordServiceImpl implements DCResetPasswordService {

	@Autowired
	private HospitalInformationRepository hospitalInformationRepository;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> listData(Long userId) {

		List<Object> getList = new ArrayList<Object>();
		try {
			List<Object[]> list = hospitalInformationRepository.getHospitalInformationByDcLoginId(userId);
			for (Object[] obj : list) {
				DCResetPasswordBean dcResetPasswordBean = new DCResetPasswordBean();
				dcResetPasswordBean.setHospitalCode((String) obj[0]);
				dcResetPasswordBean.setHospitalName((String) obj[1]);
				dcResetPasswordBean.setStateName((String) obj[2]);
				dcResetPasswordBean.setDistrictName((String) obj[3]);
				dcResetPasswordBean.setStatus(((BigDecimal) obj[4]).toString());
				dcResetPasswordBean.setUserId(((BigDecimal) obj[5]).toString());
				getList.add(dcResetPasswordBean);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getList;
	}

}
