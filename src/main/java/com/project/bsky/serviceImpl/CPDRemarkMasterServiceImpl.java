/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CPDRemarkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.repository.ActionRemarkRepository;
import com.project.bsky.service.CPDRemarkMasterService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class CPDRemarkMasterServiceImpl implements CPDRemarkMasterService {

	@Autowired
	private ActionRemarkRepository actionRemarkRepository;

	@Autowired
	private Logger logger;

	@Override
	public List<ActionRemark> getAllRemarks() {
		List<ActionRemark> getCpdRemark = actionRemarkRepository.findCpdRemarkByStatus();
		return getCpdRemark;
	}

	@Override
	public Response saveCpdRemark(CPDRemarkBean cpdRemarkBean) {
		Response response = new Response();
		try {
			ActionRemark actionRemark = new ActionRemark();
			Integer countRemark = actionRemarkRepository.checkRemark(cpdRemarkBean.getRemarks());
			if (countRemark == 0) {
				actionRemark.setRemarks(cpdRemarkBean.getRemarks());
				actionRemark.setStatusFlag(1);
				actionRemarkRepository.save(actionRemark);
				response.setMessage(" CPD Remark Added");
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
	public ActionRemark getActionById(Long id) {
		try {
			return actionRemarkRepository.findById(id).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public Response updateBankMaster(CPDRemarkBean cpdRemarkBean) {
		Response response = new Response();
		try {
			ActionRemark actionrmrk = actionRemarkRepository.findById(cpdRemarkBean.getId()).get();
			Integer count = actionRemarkRepository.countRowForCheckDuplicateType(cpdRemarkBean.getRemarks());
			if (count == 0) {
				actionrmrk.setStatusFlag(Integer.parseInt(cpdRemarkBean.getStatusFlag()));
				actionrmrk.setRemarks(cpdRemarkBean.getRemarks());
				actionRemarkRepository.save(actionrmrk);
				response.setStatus("Success");
				response.setMessage("CPD Remark Successfully Updated");
			} else if (actionrmrk.getRemarks().equals(cpdRemarkBean.getRemarks())
					&& actionrmrk.getId() == cpdRemarkBean.getId()) {
				actionrmrk.setStatusFlag(Integer.parseInt(cpdRemarkBean.getStatusFlag()));
				actionrmrk.setRemarks(cpdRemarkBean.getRemarks());
				actionRemarkRepository.save(actionrmrk);
				response.setStatus("Success");
				response.setMessage("CPD Remark Successfully Updated");
			} else {
				response.setStatus("Failed");
				response.setMessage("CPD Remark Already Exist");
			}
			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return response;

	}

}
