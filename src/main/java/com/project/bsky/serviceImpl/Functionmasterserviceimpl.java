/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.FunctionMaster;
import com.project.bsky.model.UnlinkedFunctionMaster;
import com.project.bsky.repository.FunctionMasterRepository;
import com.project.bsky.repository.UnlinkedFunctionMasterRepository;
import com.project.bsky.service.FunctionmasterService;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class Functionmasterserviceimpl implements FunctionmasterService {

	@Autowired
	private FunctionMasterRepository functionMasterRepository;

	@Autowired
	private UnlinkedFunctionMasterRepository unlinkedFunctionMasterRepository;

	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response savefunctionmaster(FunctionMaster functionmaster) {
		Response response = new Response();
		try {
			Integer count = functionMasterRepository.cheakduplicate(functionmaster.getFileName());
			if (count == 0) {
				functionmaster.setBitStatus(0);
				functionmaster.setCreatedOn(date);
				//// System.out.println(functionmaster);
				functionMasterRepository.save(functionmaster);
				response.setMessage("Function Master Added");
				response.setStatus("Success");
			} else {
				response.setMessage("Function URL Already Exist");
				response.setStatus("Failed");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<FunctionMaster> getfunctionmaster() {

		return functionMasterRepository.findAlldata();
	}

	@Override
	public Response deletefunctionmaster(Long userid, Long fnid) {
		Response response = new Response();
		try {
			FunctionMaster functionmaste = functionMasterRepository.findById(fnid).get();
			functionmaste.setBitStatus(1);
			functionMasterRepository.save(functionmaste);
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
	public FunctionMaster getbyfunctionmaster(Long userid) {
		FunctionMaster functionmaste = null;
		try {
			functionmaste = functionMasterRepository.findById(userid).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return functionmaste;
	}

	@Override
	public Response update(FunctionMaster functionmaster) {
		Response response = new Response();
		try {
			Integer count = functionMasterRepository.cheakduplicate(functionmaster.getFileName());
			FunctionMaster functionmaster1 = functionMasterRepository.findByfileName(functionmaster.getFileName());
			if (count == 0) {
//				functionmaster.setBitStatus(0);
//				if(functionmaster.getBitStatus()==1) {
////					Integer globalLinkId=globallink.getGlobalLinkId().intValue();
////					Integer countassignpmlink=usermenumapping.countassigngllink(globalLinkId);
//					if(countassignpmlink>0) {
//						response.setMessage("Global Link Assigned to Someone");
//						response.setStatus("Failed");
//						return response;
//					}
//				}
				functionmaster.setUpdatedOn(date);
				functionMasterRepository.save(functionmaster);
				response.setMessage("Function Master Update");
				response.setStatus("Success");
			} else if (functionmaster.getFunctionId().equals(functionmaster1.getFunctionId())
					&& functionmaster.getFileName().equals(functionmaster1.getFileName())) {
//				functionmaster.setBitStatus(0);
				functionmaster.setUpdatedOn(date);
				functionMasterRepository.save(functionmaster);
				response.setMessage("Function Master Update");
				response.setStatus("Success");
			} else {
				response.setMessage("Function URL Already Exist");
				response.setStatus("Failed");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Response saveunlinkedfunctionmaster(UnlinkedFunctionMaster unlinkedFunctionMaster) {
		Response response = new Response();
		try {
			Integer count = unlinkedFunctionMasterRepository.cheakUnlinkedDuplicateFunctionMaster(
					unlinkedFunctionMaster.getFileName(), unlinkedFunctionMaster.getUserId());
			if (count == 0) {
				unlinkedFunctionMaster.setBitStatus(0);
				unlinkedFunctionMaster.setCreatedOn(date);
				unlinkedFunctionMasterRepository.save(unlinkedFunctionMaster);
				response.setMessage("Function Master Added");
				response.setStatus("Success");
			} else {
				response.setMessage("The function URL is already assigned to this user.");
				response.setStatus("Failed");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<UnlinkedFunctionMaster> getUnlinkedFunctionMaster() {
		return unlinkedFunctionMasterRepository.findAlldata();
	}

	@Override
	public Response removeUnlinkedFunctionMaster(Long userid, Long fnid) {
		Response response = new Response();
		UnlinkedFunctionMaster unlinkedFunctionMaster = new UnlinkedFunctionMaster();
		try {
			Optional<UnlinkedFunctionMaster> optional = unlinkedFunctionMasterRepository.findById(fnid);
			if (optional.isPresent()) {
				unlinkedFunctionMaster = optional.get();
				unlinkedFunctionMaster.setBitStatus(1);
				unlinkedFunctionMasterRepository.save(unlinkedFunctionMaster);
				response.setMessage("Record Successfully In-Active");
				response.setStatus("Success");
			} else {
				response.setMessage("No Data Found");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public UnlinkedFunctionMaster getUnlinkedFunctionById(Long functionId) {
		UnlinkedFunctionMaster unlinkedFunctionMaster = null;
		try {
			Optional<UnlinkedFunctionMaster> optional = unlinkedFunctionMasterRepository.findById(functionId);
			if (optional.isPresent()) {
				unlinkedFunctionMaster = new UnlinkedFunctionMaster();
				unlinkedFunctionMaster = optional.get();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return unlinkedFunctionMaster;
	}

	@Override
	public Response updateUnlinkedFunctionMaster(UnlinkedFunctionMaster unlinkedFunctionMaster) {
		Response response = new Response();
		try {
			Integer count = unlinkedFunctionMasterRepository.cheakUnlinkedDuplicateFunctionMaster(
					unlinkedFunctionMaster.getFileName(), unlinkedFunctionMaster.getUserId());
			UnlinkedFunctionMaster unlinkedFunctionMaster2 = unlinkedFunctionMasterRepository
					.findByfileName(unlinkedFunctionMaster.getFileName());

			if (count == 0 || (unlinkedFunctionMaster.getFunctionId().equals(unlinkedFunctionMaster2.getFunctionId())
					&& unlinkedFunctionMaster.getFileName().equals(unlinkedFunctionMaster2.getFileName()))) {
				unlinkedFunctionMaster.setUpdatedOn(date);
				unlinkedFunctionMasterRepository.save(unlinkedFunctionMaster);
				response.setMessage("Function Master Updated Successfully");
				response.setStatus("Success");
			} else {
				response.setMessage("The function URL is already assigned to this user.");
				response.setStatus("Failed");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

}
