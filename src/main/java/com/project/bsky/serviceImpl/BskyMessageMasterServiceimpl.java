/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyMessageMaster;
import com.project.bsky.repository.BskyMessageMasterRepository;
import com.project.bsky.service.BskyMessageMasterService;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class BskyMessageMasterServiceimpl implements BskyMessageMasterService {

	@Autowired
	private BskyMessageMasterRepository messagemasterrepo;

	@Autowired
	private Logger logger;

	@Override
	public Response savemessage(BskyMessageMaster messagemaster) {
		Response response = new Response();
		try {
			messagemaster.setStatusflag(0);
			messagemaster.setCreatedon(Calendar.getInstance().getTime());
			BskyMessageMaster messagemaster1 = messagemasterrepo.save(messagemaster);
			if (messagemaster1 != null) {
				response.setMessage("Message Insert Successfully");
				response.setStatus("Success");
			} else {
				response.setMessage("Message Not Insert! Try Later");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<BskyMessageMaster> getalldata() {
		List<BskyMessageMaster> list = new ArrayList<BskyMessageMaster>();
		try {
			list=messagemasterrepo.findAll();
			list.sort((x,y)->(int)(y.getMessageid()-x.getMessageid()));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response updatemessage(BskyMessageMaster messagemaster) {
		Response response = new Response();
		try {
			messagemaster.setUpdatedon(Calendar.getInstance().getTime());
			BskyMessageMaster messagemaster1 = messagemasterrepo.save(messagemaster);
			if (messagemaster1 != null) {
				response.setMessage("Message Updated Successfully");
				response.setStatus("Success");
			} else {
				response.setMessage("Message Not Update! Try Later");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("Failed");
		}
		return response;
	}

}
