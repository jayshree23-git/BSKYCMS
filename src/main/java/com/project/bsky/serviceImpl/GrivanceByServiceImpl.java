package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GrievanceBy;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.GrievanceByRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.GrievanceByService;
@Service
public class GrivanceByServiceImpl implements GrievanceByService {
	@Autowired
	private GrievanceByRepository grievancebyRepository;
	@Autowired
	private UserDetailsRepository userdetailsrepo;
	
	@Autowired
	private Logger logger;

	@Override
	public Response saveGrievancebyData(GrievanceBy grievanceby){
		Response response=new Response();
		try {
			
			Integer countPCode=grievancebyRepository.checkduplicateGrivancename(grievanceby.getGrivancename());
			if(countPCode==0){
				UserDetails userdetails=userdetailsrepo.findById(grievanceby.getCreatedby()).get();
				grievanceby.setCreatedby1(userdetails);
				grievanceby.setStatusFlag(0);
				grievanceby.setCreatedon(Calendar.getInstance().getTime());
				grievancebyRepository.save(grievanceby);
				response.setStatus("Success");
				response.setMessage("GrievanceBy Successfully Inserted");	
			}
			else if(countPCode!=0) {
				response.setMessage("GrievanceBy is Already Exist");
				response.setStatus("Failed");
			}
		else  {
				response.setMessage("Something went wrong");
				response.setStatus("Failed");
			}}
			 catch (Exception e) {
				 logger.error(ExceptionUtils.getStackTrace(e));
					response.setStatus("Failed");
					response.setMessage("Already Exist");			
				}
			return response;
	}
	@Override
	public List<GrievanceBy> getDetails() {
		List<GrievanceBy> list=new ArrayList<GrievanceBy>();
		try {
			list=grievancebyRepository.getDetails();
			for(GrievanceBy x:list) {
				x.setScreatedate(x.getCreatedon().toString());
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	@Override
	public GrievanceBy getgrievancebyId(Long userid) {
		GrievanceBy grievanceby=null;
		try {
			grievanceby=grievancebyRepository.findById(userid).get();
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return grievanceby;
	}
	
	@Override
	
	public Response updateGrievanceBy(GrievanceBy grievanceby) {

		Response response=new Response();
		try {
			
			Integer countPCode=grievancebyRepository.checkduplicateGrivancename(grievanceby.getGrivancename());
			
			if(countPCode==0){
				GrievanceBy grievanceby1=grievancebyRepository.findById(grievanceby.getId()).get();
				grievanceby.setCreatedby1(grievanceby1.getCreatedby1());
				grievanceby.setCreatedon(grievanceby1.getCreatedon());
				grievanceby.setUpdatedon(Calendar.getInstance().getTime());
				grievancebyRepository.save(grievanceby);
				response.setStatus("Success");
				response.setMessage("GrievanceBy Successfully Updated");	
			}
			else  {
				GrievanceBy grievanceby1=grievancebyRepository.findByGrivancename(grievanceby.getGrivancename());
				if(grievanceby1.getId()==grievanceby.getId() && grievanceby.getGrivancename().equals(grievanceby1.getGrivancename())) {
					grievanceby.setCreatedby1(grievanceby1.getCreatedby1());
					grievanceby.setCreatedon(grievanceby1.getCreatedon());
					grievanceby.setUpdatedon(Calendar.getInstance().getTime());
					grievancebyRepository.save(grievanceby);
					response.setStatus("Success");
					response.setMessage("GrievanceBy Successfully Updated");		
				}
				else {
					response.setMessage("GrievanceBy is Already Exist");
					response.setStatus("Failed");
				}
			}
				
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
					response.setStatus("Failed");
					response.setMessage("Something went wrong");		
				}
		return response;
			}
}
