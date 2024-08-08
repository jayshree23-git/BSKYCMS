/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.FoRemark;
import com.project.bsky.model.FoRemarkLog;
import com.project.bsky.repository.FoRemarkLogRepository;
import com.project.bsky.repository.FoRemarkRepository;
import com.project.bsky.service.FoRemarkservice;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class FoRemarkserviceimpl implements FoRemarkservice {
	
	private final Logger logger;
	
	@Autowired
	public FoRemarkserviceimpl(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private FoRemarkRepository foremarkrepo;
	
	@Autowired
	private FoRemarkLogRepository foremarklogrepo;

	@Override
	public Response saveforemark(FoRemark foremark) {
		FoRemark foremark1=null;
		Response response=new Response();
		try {
			Integer count=foremarkrepo.cheakduplicate(foremark.getRemark());
			if(count==0) {
				foremark.setStatus(0);
				foremark.setCreatedOn(Calendar.getInstance().getTime());
				foremark1 = foremarkrepo.save(foremark);
				if(foremark1!=null) {
					response.setMessage("FO Remark Added");
					response.setStatus("200");
				}else {
					response.setMessage("Some Error Happen");
					response.setStatus("400");
				}
			}else {
				response.setMessage("Remark Alread Exist");
				response.setStatus("400");
			}			
		}catch (Exception e) {
			logger.warn("Error "+e);
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public List<FoRemark> getforemark() {
		List<FoRemark> list =new ArrayList<FoRemark>();
		try {
			list=foremarkrepo.getforemark();
//			//System.out.println(list);
		}catch (Exception e) {
			logger.warn("Error "+e);
		}
		return list;
	}

	@Override
	public Response updateforemark(FoRemark foremark) {
		Response response=new Response();
		try {
			Integer count=foremarkrepo.cheakduplicate(foremark.getRemark());
			if(count==0) {
				response=update(foremark);
			}else {
				FoRemark foremark1=foremarkrepo.findByremark(foremark.getRemark());
				if(foremark1.getRemark().equals(foremark.getRemark()) &&
						foremark1.getRemarkid().equals(foremark.getRemarkid())) {
					response=update(foremark);
				}else {
					response.setMessage("Remark Alread Exist");
					response.setStatus("400");
				}
			}			
		}catch (Exception e) {
			logger.warn("Error "+e);
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
		return response;
	}

	private Response update(FoRemark foremark) {	
		Response response=new Response();
		FoRemark nforemark=foremarkrepo.findById(foremark.getRemarkid()).get();
		FoRemarkLog foremarklog=new FoRemarkLog();
		foremarklog.setRemarkid(nforemark.getRemarkid());
		foremarklog.setNewremark(foremark.getRemark());
		foremarklog.setOldremark(nforemark.getRemark());
		foremarklog.setDescription(nforemark.getDescription());
		foremarklog.setStatus(0);
		foremarklog.setUpdatedBy(foremark.getCreatedBy());
		foremarklog.setUpdatedOn(Calendar.getInstance().getTime());
		foremarklogrepo.save(foremarklog);
		nforemark.setRemark(foremark.getRemark());
		nforemark.setDescription(foremark.getDescription());
		nforemark.setStatus(foremark.getStatus());
		nforemark.setUpdatedBy(foremark.getCreatedBy());
		nforemark.setUpdatedOn(Calendar.getInstance().getTime());
		foremarkrepo.save(nforemark);
		response.setMessage("FO Remark Update Successfully");
		response.setStatus("200");
		return response;
	}

	@Override
	public List<FoRemark> getactiveforemark() {
		List<FoRemark> list =new ArrayList<FoRemark>();
		try {
			list=foremarkrepo.getactiveforemark();
//			//System.out.println(list);
		}catch (Exception e) {
			logger.warn("Error "+e);
		}
		return list;
	}
}
