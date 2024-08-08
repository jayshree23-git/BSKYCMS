package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.repository.ActionRemarkRepository;
import com.project.bsky.service.RemarksMasterService;

@Service

public class RemarksMasterServiceImpl implements RemarksMasterService {
	
	@Autowired
	private ActionRemarkRepository actionRemarkRepository;

	@Autowired
	private Logger logger;

	@Override
	public Response saveRemarksMaster(ActionRemark actionRemark) {
		Response response = new Response();
		try {
			actionRemarkRepository.save(actionRemark);
			response.setMessage(" Action Remark Added");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<ActionRemark> getAllRemarks() {
		List<ActionRemark> headerResponse = new ArrayList<>();
		List<ActionRemark> findAll = actionRemarkRepository.findAll(Sort.by(Sort.Direction.DESC, "remarks"));
		if (findAll != null) {
			for (ActionRemark actionRemark : findAll) {
				headerResponse.add(actionRemark);
			}

		}
		return headerResponse;

	}

	@Override
	public ActionRemark getbyremark(Long id) {
		ActionRemark actionRemark = null;
		try {
			actionRemark = actionRemarkRepository.findById(id).get();
		} catch (Exception e) {
		}
		return actionRemark;
	}

	@Override
	public Response updateremark(Long id, ActionRemark actionRemark) {
		Response response = new Response();
		try {

			ActionRemark actionRemarkResponse = this.getbyremark(id);
			if (Objects.isNull(actionRemarkResponse)) {
				response.setMessage("Remarks URL Already Exist");
			}
			actionRemark.setId(id);
			actionRemarkRepository.save(actionRemark);
			response.setMessage("Remarks Update");
			response.setStatus("Success");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}
}
