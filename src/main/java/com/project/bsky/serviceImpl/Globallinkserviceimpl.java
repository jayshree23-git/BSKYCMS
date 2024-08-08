/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.repository.GlobalLinkRepository;
import com.project.bsky.repository.UserMenuMappingRepository;
import com.project.bsky.service.Globallinkservice;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class Globallinkserviceimpl implements Globallinkservice {
	
	@Autowired
	private GlobalLinkRepository globallinkrepo;
	
	@Autowired
	private UserMenuMappingRepository usermenumapping;
	
	@Autowired
	private Logger logger;
	
	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response savegloballink(GlobalLink globallink) {
		Response response=new Response();
		try {
			Integer count=globallinkrepo.cheakduplicate(globallink.getGlobalLinkName());
			Integer countorder=globallinkrepo.cheakduplicateorder(globallink.getOrder());
			////System.out.println(count);
			if(count==0 && countorder==0) {
				globallink.setBitStatus(0);
				globallink.setCreatedOn(date);			
				globallinkrepo.save(globallink);
				response.setMessage("Global Link Added");
				response.setStatus("Success");
			}else if(count!=0){
				response.setMessage("Global Link Name Already Exist");
				response.setStatus("Failed");
			}else {
				response.setMessage("Order No Already Exist");
				response.setStatus("Failed");
			}
			
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		
		return response;
	}

	@Override
	public List<GlobalLink> getgloballink() {
		// TODO Auto-generated method stub
		return globallinkrepo.getalldata();
	}

	@Override
	public Response deletefunctionmaster(Long userid) {
		Response response=new Response();
		try {
			GlobalLink globallink=globallinkrepo.findById(userid).get();
			globallink.setBitStatus(1);
			globallinkrepo.save(globallink);
			response.setMessage("Record Successfully In-Active");
			response.setStatus("Success");
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public GlobalLink getbyId(Long userid) {
		GlobalLink globallink=null;
		try {
			globallink=globallinkrepo.findById(userid).get();
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return globallink;
	}

	@Override
	public Response updategloballink(GlobalLink globallink) {
		////System.out.println(globallink);
		Response response=new Response();
		try {
			Integer count=globallinkrepo.cheakduplicate(globallink.getGlobalLinkName());
			Integer countorder=globallinkrepo.cheakduplicateorder(globallink.getOrder());
			GlobalLink globallink1=globallinkrepo.findByglobalLinkName(globallink.getGlobalLinkName());
			GlobalLink globallink2=globallinkrepo.findByorder(globallink.getOrder());
			////System.out.println(globallink2);
			if(count==0 && countorder==0) {
//				globallink.setBitStatus(0);
			globallink.setUpdatedOn(date);
			if(globallink.getBitStatus()==1) {
				Integer globalLinkId=globallink.getGlobalLinkId().intValue();
				Integer countassignpmlink=usermenumapping.countassigngllink(globalLinkId);
				if(countassignpmlink>0) {
					response.setMessage("Global Link Assigned to Someone");
					response.setStatus("Failed");
					return response;
				}
			}
			globallinkrepo.save(globallink);
			response.setMessage("Global Link Update");
			response.setStatus("Success");
			}else if(count!=0 && countorder==0) {
				if(globallink.getGlobalLinkId().equals(globallink1.getGlobalLinkId()) && globallink.getGlobalLinkName().equals(globallink1.getGlobalLinkName())) {
//				globallink.setBitStatus(0);
				globallink.setUpdatedOn(date);	
					if(globallink.getBitStatus()==1) {
						Integer globalLinkId=globallink.getGlobalLinkId().intValue();
						Integer countassignpmlink=usermenumapping.countassigngllink(globalLinkId);
						if(countassignpmlink>0) {
							response.setMessage("Global Link Assigned to Someone");
							response.setStatus("Failed");
							return response;
						}
					}
				globallinkrepo.save(globallink);
				response.setMessage("Global Link Update");
				response.setStatus("Success");
				}else 
					{
						response.setMessage("Global Link Name Already Exist");
						response.setStatus("Failed");
					}
			}else if(count==0 && countorder!=0) {
				if(globallink.getGlobalLinkId().equals(globallink2.getGlobalLinkId()) && globallink.getOrder().equals(globallink2.getOrder())) {
					globallink.setUpdatedOn(date);	
					if(globallink.getBitStatus()==1) {
						Integer globalLinkId=globallink.getGlobalLinkId().intValue();
						Integer countassignpmlink=usermenumapping.countassigngllink(globalLinkId);
						if(countassignpmlink>0) {
							response.setMessage("Global Link Assigned to Someone");
							response.setStatus("Failed");
							return response;
						}
					}
				globallinkrepo.save(globallink);
				response.setMessage("Global Link Update");
				response.setStatus("Success");
				}else {
					response.setMessage("Order No Already Exist");
					response.setStatus("Failed");
				}
			}else {
				if(globallink.getGlobalLinkId().equals(globallink1.getGlobalLinkId()) &&
					globallink.getGlobalLinkName().equals(globallink1.getGlobalLinkName())&&
					globallink.getGlobalLinkId().equals(globallink2.getGlobalLinkId()) &&
							globallink.getOrder().equals(globallink2.getOrder())
						) {
					globallink.setUpdatedOn(date);	
					if(globallink.getBitStatus()==1) {
						Integer globalLinkId=globallink.getGlobalLinkId().intValue();
						Integer countassignpmlink=usermenumapping.countassigngllink(globalLinkId);
						if(countassignpmlink>0) {
							response.setMessage("Global Link Assigned to Someone");
							response.setStatus("Failed");
							return response;
						}
					}
				globallinkrepo.save(globallink);
				response.setMessage("Global Link Update");
				response.setStatus("Success");
					
				}else {
				response.setMessage(" Order no Already Exist");
				response.setStatus("Failed");
				}
			}
			
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		
		return response;
	}

}
