
package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.Group;
import com.project.bsky.model.Subgroup;
import com.project.bsky.repository.GroupRepository;
import com.project.bsky.repository.Subgrouprepository;
import com.project.bsky.service.Subgroupservice;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class Subgroupserviceimpl implements Subgroupservice {
	
	@Autowired
	Subgrouprepository subgrouprepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private Logger logger;
	
	Calendar cal = Calendar. getInstance(); 
	Date date=cal. getTime(); 
	
	@Override
	public List<Group> getgroupname() {
		return groupRepository.findAllactivedata();
	}

	@Override
	public List<Subgroup> getalldata() {
		
		return subgrouprepository.findAllbyorder();
	}

	@Override
	public Integer savesubgroup(long groupid, String subgroupname, String createdby) {
		try {
			Long count;
			count=subgrouprepository.findsubgroupname(subgroupname);
			////System.out.println(subgroupname.length());
			////System.out.println(subgroupname);
			if(subgroupname.length()==0||subgroupname.equalsIgnoreCase("null")){
				return 3;
			}else {
				if(count==0) {
					Subgroup subgroup=new Subgroup();
					subgroup.setSubgroupname(subgroupname);
					subgroup.setCreatedby(createdby);
					Group group=new Group();
					group=groupRepository.findById((int) groupid).get();
					subgroup.setGroupid(group);
					subgroup.setCreatedate(date);
					subgroup.setUpdatedate(date);
					subgroup.setUpdateby("");
					subgroup.setStatus(0);
					////System.out.println(subgroup);
					Subgroup s=subgrouprepository.save(subgroup);
					return 1;
				}else {
					return 2;
				}
			}
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
		
	}

	@Override
	public Integer delete(long subgroupid) {
		try {
			subgrouprepository.delete(subgroupid);
			return 1;
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
		
	}

	@Override
	public Subgroup getbyid(long subgroupid) {
		try {
			Subgroup subgroup=subgrouprepository.findById(subgroupid).get();
			return subgroup;
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
		 
	}  
	
	public Integer updatecode(String subgroup, long groupid, String updateby, long subgroupid, Integer status)
	{
		Subgroup sgroup=subgrouprepository.findById(subgroupid).get();
		Group group=new Group();
		group=groupRepository.findById((int) groupid).get();
		sgroup.setGroupid(group);
		sgroup.setSubgroupname(subgroup);
		sgroup.setUpdateby(updateby);
		sgroup.setStatus(status);
		subgrouprepository.save(sgroup);
		return 1;
	}

	@Override
	public Integer update(long groupid, String subgroup, String updateby, long subgroupid, Integer status) {
		try {
			Subgroup sl=subgrouprepository.findBysubgroupname(subgroup);
			Long count=subgrouprepository.findsubgroupname(subgroup);
			if(count==0) {
			return updatecode(subgroup,groupid,updateby,subgroupid,status);
			}else if(sl.getSubgroupid().equals(subgroupid) && sl.getSubgroupname().equals(subgroup)){
				return updatecode(subgroup,groupid,updateby,subgroupid,status);
			}else {
				return 2;
			}
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

}
