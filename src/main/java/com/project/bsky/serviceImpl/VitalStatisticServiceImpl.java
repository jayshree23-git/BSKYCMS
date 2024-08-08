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
import com.project.bsky.model.VitalStatistics;
import com.project.bsky.repository.VitalStatisticsRepository;
import com.project.bsky.service.VitalStatisticService;

@Service

public class VitalStatisticServiceImpl implements VitalStatisticService {

	@Autowired
	private VitalStatisticsRepository vitalStatisticsRepository;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response savevitalStatistics(VitalStatistics vitalstatistics) {
		Response response = new Response();
		try {
			vitalstatistics.setCreatedBy(-1);
			vitalstatistics.setUpdatedBy(-1);
			vitalstatistics.setCreatedOn(date);
			vitalstatistics.setUpdatedOn(date);
			vitalstatistics.setDeletedFlag(0);
			vitalStatisticsRepository.save(vitalstatistics);
			response.setMessage("Vital Statistics Added");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<VitalStatistics> getvitalStatistics() {
		List<VitalStatistics> vitalResponse = new ArrayList<>();
		List<VitalStatistics> findAll = vitalStatisticsRepository
				.findAll(Sort.by(Sort.Direction.ASC, "vitalstatisticsname"));
		if (findAll != null) {
			for (VitalStatistics vitalstatistics : findAll) {
				if (vitalstatistics != null && vitalstatistics.getDeletedFlag() == 0) {
					vitalResponse.add(vitalstatistics);
				}
			}
		}
		return vitalResponse;
	}

	@Override
	public Response deletevitalstatistics(Long vitalStatisticsId) {
		Response response = new Response();
		try {
			VitalStatistics vitalstatistics = vitalStatisticsRepository.findById(vitalStatisticsId).get();
			vitalstatistics.setDeletedFlag(1);
			vitalStatisticsRepository.save(vitalstatistics);
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
	public VitalStatistics getVitalstatistics(Long vitalStatisticsId) {
		VitalStatistics vitalstatistics = null;
		try {
			vitalstatistics = vitalStatisticsRepository.findById(vitalStatisticsId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return vitalstatistics;
	}

	@Override
	public Response update(Long vitalStatisticsId, VitalStatistics vitalstatistics) {
		Response response = new Response();
		try {

			VitalStatistics vitalstatisticsResponse = this.getVitalstatistics(vitalStatisticsId);
			if (Objects.isNull(vitalstatisticsResponse)) {
				response.setMessage("Vital Statistics URL Already Exist");
			}

			vitalstatistics.setVitalStatisticsId(vitalStatisticsId);
			vitalstatistics.setUpdatedOn(date);
			vitalstatistics.setCreatedOn(date);
			vitalstatistics.setCreatedBy(-1);
			vitalstatistics.setUpdatedBy(-1);
			vitalstatistics.setDeletedFlag(0);
			vitalStatisticsRepository.save(vitalstatistics);
			response.setMessage("Vital Statistics Updated");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Long checkDuplicateVitalstatisticsName(String vitalstatisticsname) {
		Long checkPkg = null;
		try {
			checkPkg = vitalStatisticsRepository.getVitalStatisticsIdByVitalstatisticsName(vitalstatisticsname);
			return checkPkg;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkPkg;
	}

	@Override
	public Long checkDuplicateVitalstatisticsCode(String vitalstatisticscode) {
		Long checkPkg = null;
		try {
			checkPkg = vitalStatisticsRepository.getVitalStatisticsIdByVitalstatisticsCode(vitalstatisticscode);
			return checkPkg;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkPkg;
	}

}
