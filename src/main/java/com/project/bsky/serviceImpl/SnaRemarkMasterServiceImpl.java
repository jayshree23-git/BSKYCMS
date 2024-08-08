/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaRemarkBean;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.repository.ActionRemarkRepository;
import com.project.bsky.service.SnaRemarkMasterService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class SnaRemarkMasterServiceImpl implements SnaRemarkMasterService {

	@Autowired
	private ActionRemarkRepository actionRemarkRepository;

	@Autowired
	private Logger logger;

	@Override
	public Response savesnaRemark(SnaRemarkBean snaRemarkBean) {
		Response response = new Response();
		try {
			ActionRemark actionRemark = new ActionRemark();
			Integer countRemark = actionRemarkRepository.checkRemarkSNA(snaRemarkBean.getRemarks());
			if (countRemark == 0) {
				actionRemark.setRemarks(snaRemarkBean.getRemarks());
				actionRemark.setStatusFlag(2);
				actionRemarkRepository.save(actionRemark);
				response.setMessage("SNA  Remark Added");
				response.setStatus("Success");
			} else {
				response.setStatus("Failed");
				response.setMessage("Remark Already Exist");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<ActionRemark> getAllsnaRemarks() {
		List<ActionRemark> getCpdRemark = actionRemarkRepository.findsnaRemarkByStatus();
		return getCpdRemark;
	}

	@Override
	public ActionRemark getActionById(Long id) {
		try {
			return actionRemarkRepository.findById(id).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public Response updatesnaremarkMaster(SnaRemarkBean snaRemarkBean) {
		Response response = new Response();
		try {
			ActionRemark actionrmrk = actionRemarkRepository.findById(snaRemarkBean.getId()).get();
			Integer count = actionRemarkRepository.checkRemarkSNA(snaRemarkBean.getRemarks());
			if (count == 0) {
				actionrmrk.setStatusFlag(Integer.parseInt(snaRemarkBean.getStatusFlag()));
				actionrmrk.setRemarks(snaRemarkBean.getRemarks());
				actionRemarkRepository.save(actionrmrk);
				response.setStatus("Success");
				response.setMessage("SNA Remark Successfully Updated");
			} else if (actionrmrk.getRemarks().equals(snaRemarkBean.getRemarks())
					&& actionrmrk.getId() == snaRemarkBean.getId()) {
				actionrmrk.setStatusFlag(Integer.parseInt(snaRemarkBean.getStatusFlag()));
				actionrmrk.setRemarks(snaRemarkBean.getRemarks());
				actionRemarkRepository.save(actionrmrk);
				response.setStatus("Success");
				response.setMessage("SNA Remark Successfully Updated");
			} else {
				response.setStatus("Failed");
				response.setMessage("SNA Remark Already Exist");
			}
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

}
