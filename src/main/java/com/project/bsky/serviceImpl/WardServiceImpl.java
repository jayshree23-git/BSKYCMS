package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.Ward;
import com.project.bsky.repository.WardRepository;
import com.project.bsky.service.WardService;

@Service

public class WardServiceImpl implements WardService {
	@Autowired
	private WardRepository wardRepository;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response saveWard(Ward ward) {
		Response response = new Response();
		try {
			ward.setCreatedBy(ward.getCreatedBy());
			ward.setUpdatedBy(-1);
			ward.setCreatedOn(date);
			ward.setUpdatedOn(date);
			ward.setDeletedFlag(0);
			wardRepository.save(ward);
			response.setMessage("Ward Master Added");
			response.setStatus("Success");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<Ward> getward() {
		List<Ward> wardrResponse = new ArrayList<>();
		List<Ward> findAll = wardRepository.findAll(Sort.by(Sort.Direction.DESC, "wardMasterId"));

		if (findAll != null) {
			for (Ward ward : findAll) {
				if (ward != null && ward.getDeletedFlag() == 0) {
					wardrResponse.add(ward);
				}
			}
		}
		return wardrResponse;
	}

	@Override
	public Response deleteWard(Long wardMasterId) {
		Response response = new Response();
		try {
			Ward ward = wardRepository.findById(wardMasterId).get();
			ward.setDeletedFlag(1);
			wardRepository.save(ward);
			response.setMessage("Record Successfully In-Active");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Ward getbywardMasterId(Long wardMasterId) {
		Ward ward = null;
		try {
			ward = wardRepository.findById(wardMasterId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ward;
	}

	@Override
	public Response update(Long wardMasterId, Ward ward) {
		Response response = new Response();
		try {
			Ward wardResponse = this.getbywardMasterId(wardMasterId);

			if (Objects.isNull(wardResponse)) {
				response.setMessage("Ward URL Already Exist");
			}
			ward.setWardMasterId(wardMasterId);
			ward.setUpdatedOn(date);
			ward.setCreatedOn(wardResponse.getCreatedOn());
			ward.setCreatedBy(wardResponse.getCreatedBy());
			ward.setUpdatedBy(-1);
			ward.setDeletedFlag(wardResponse.getDeletedFlag());
			wardRepository.save(ward);
			response.setMessage("Ward Update Successfully!!");
			response.setStatus("Success");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Long checkDuplicateWardName(String wardName) {
		Long checkwrd = null;
		try {
			checkwrd = wardRepository.getWardMasterIdByWardName(wardName);
			return checkwrd;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkwrd;
	}

	@Override
	public Long checkDuplicateWardCode(String wardCode) {
		Long checkwrd = null;
		try {
			checkwrd = wardRepository.getWardMasterIdByWardCode(wardCode);
			return checkwrd;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkwrd;
	}



	@Override
	public List<PackageDetailsHospital> getpackageDetailsDescrition(Integer hospitalCategoryId) {
		List<PackageDetailsHospital> headerResponse = new ArrayList<>();
		List<PackageDetailsHospital> findAll = wardRepository.findDetails(hospitalCategoryId);
		if (findAll != null) {
			for (PackageDetailsHospital packageDetailsHospital : findAll) {
				if (packageDetailsHospital != null && packageDetailsHospital.getDeletedFlag() == 0) {
					headerResponse.add(packageDetailsHospital);
				}
			}
		}
		return headerResponse;
	}

}
