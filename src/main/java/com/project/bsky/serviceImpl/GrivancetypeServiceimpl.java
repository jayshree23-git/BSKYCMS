package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.Grivancetype;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.GrivancetypeRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.GrivancetypeService;

@Service
public class GrivancetypeServiceimpl implements GrivancetypeService {
	
	@Autowired
	private GrivancetypeRepository grivancetyperepository;
	
	@Autowired
	private UserDetailsRepository userdetailsrepo;
	
	@Autowired
	private Logger logger;

	@Override
	public Response saveGrivancetypeData(Grivancetype grivancetype) {
		Response response=new Response();
		try {
			Integer chechduplicatetypename=grivancetyperepository.findBytypename(grivancetype.getGrievancetypename());
			if(chechduplicatetypename==0) {
				UserDetails userdetails=userdetailsrepo.findById(grivancetype.getCreatedby()).get();
				grivancetype.setCreatedby1(userdetails);
				grivancetype.setStatusflag(0);
				grivancetype.setCreateon(Calendar.getInstance().getTime());
				Grivancetype grivancetype1=grivancetyperepository.save(grivancetype);
				if(grivancetype1!=null) {
					response.setMessage("Data Insert Successfully");
					response.setStatus("Success");
				}else {
					response.setMessage("Something Went Wrong!! Data Can Not Save");
					response.setStatus("failed");
				}
			}else {
				response.setMessage("Grievancetype  Already Exist");
				response.setStatus("failed");
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("failed");
		}
		return response;
	}

	@Override
	public List<Grivancetype> getGrivancetypeData() {
		List<Grivancetype> list=new ArrayList<Grivancetype>();
		try {
			list=grivancetyperepository.findAll();
			for(Grivancetype x:list) {
				x.setScreatedate(x.getCreateon().toString());
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}		
		return list;
	}

	@Override
	public Response updateGrivancetypeData(Grivancetype grivancetype) {
		Response response=new Response();
		try {
		Integer chechduplicatetypename=grivancetyperepository.findBytypename(grivancetype.getGrievancetypename());
			if(chechduplicatetypename==0) {
				Grivancetype grivancetype1=grivancetyperepository.findById(grivancetype.getGrievancetypeid()).get();
				grivancetype1.setGrievancetypename(grivancetype.getGrievancetypename());
				grivancetype1.setStatusflag(grivancetype.getStatusflag());
				grivancetype1.setUpdateon(Calendar.getInstance().getTime());
				Grivancetype grivancetype2=grivancetyperepository.save(grivancetype1);
				if(grivancetype2!=null) {
					response.setMessage("Data Update Successfully");
					response.setStatus("Success");
				}else {
					response.setMessage("Something Went Wrong!! Data Can Not update");
					response.setStatus("failed");
				}				
			}else {
				Grivancetype grivancetype1=grivancetyperepository.findBygrievancetypename(grivancetype.getGrievancetypename());
				if(grivancetype.getGrievancetypeid()==grivancetype1.getGrievancetypeid()
						&&grivancetype.getGrievancetypename().equalsIgnoreCase(grivancetype1.getGrievancetypename())) {
					grivancetype1.setGrievancetypename(grivancetype.getGrievancetypename());
					grivancetype1.setStatusflag(grivancetype.getStatusflag());
					grivancetype1.setUpdateon(Calendar.getInstance().getTime());
					Grivancetype grivancetype2=grivancetyperepository.save(grivancetype1);
					if(grivancetype2!=null) {
						response.setMessage("Data Update Successfully");
						response.setStatus("Success");
					}else {
						response.setMessage("Something Went Wrong!! Data Can Not update");
						response.setStatus("failed");
					}						
				}else {
					response.setMessage("Grievancetype Name Already Exist");
					response.setStatus("failed");
				}
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("failed");
		}
		return response;
	}
	@Override
	public Grivancetype getgrievancetypeId (Long userid) {
		Grivancetype grivancetype=null;
		try {
			grivancetype=grivancetyperepository.findById(userid).get();
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return grivancetype;
	}


}
