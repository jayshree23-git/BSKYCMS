package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.Ward;
import com.project.bsky.model.WardDetails;
import com.project.bsky.repository.WardDetailsRepository;
import com.project.bsky.service.WardDetailsService;

@Service

public class WardDetailsServiceImpl implements WardDetailsService {

	@Autowired
	private WardDetailsRepository wardDetailsRepository;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response saveWardDetails(WardDetails wardDetails) {
		Response response = new Response();
		try {
			wardDetails.setCreatedBy(wardDetails.getCreatedBy());
			wardDetails.setUpdatedBy(-1);
			wardDetails.setCreatedOn(date);
			wardDetails.setUpdatedOn(date);
			wardDetails.setDeletedFlag(0);
			wardDetailsRepository.save(wardDetails);
			response.setMessage("Ward Details Master Added");
			response.setStatus("Success");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<WardDetails> getwarddetails() {
		List<WardDetails> warddetailsResponse = new ArrayList<>();
		List<WardDetails> findAll = wardDetailsRepository.findAll(Sort.by(Sort.Direction.ASC, "wardCode"));

		if (findAll != null) {
			for (WardDetails wardDetails : findAll) {
				if (wardDetails != null && wardDetails.getDeletedFlag() == 0) {
					warddetailsResponse.add(wardDetails);
				}
			}
		}
		return warddetailsResponse;
	}

	@Override
	public List<Ward> getAllward(Long wardMasterId) {
		List<Ward> wardResponse = new ArrayList<>();
		return wardResponse;
	}

}
