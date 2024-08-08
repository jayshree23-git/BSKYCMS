/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserDetailsProfileBean;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.HospitalOperator;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.hospitalOperatorLog;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.HospitalOperatorLogRepository;
import com.project.bsky.repository.HospitalOperatorRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.AdminConsoleService;
import com.project.bsky.service.HospitalOperatorService;

/**
 * @author rajendra.sahoo
 * 
 * main table updatedby == log table updatedby
 * main table createdby == who create the record
 * log table created == whe update the record or create the log record
 *
 */
@Service
public class HospitalOperatorServiceImpl implements HospitalOperatorService {
	
	@Autowired
	private AdminConsoleService adminService;

	@Autowired
	private UserDetailsRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private GroupTypeRepository groupRepo;
	
	@Autowired
	private Environment env;
	
	@Autowired
	HospitalOperatorRepository hospitaloperatorrepo;
	
	
	@Autowired
	HospitalOperatorLogRepository hospitaloperatorlogrepo;
	
	@Autowired
	private Logger logger;	
	
	@Override
	@Transactional
	public Response savehospitaloperator(UserDetailsProfileBean bean) throws Exception {	
		Response response=new Response();
		try {
			Integer count=hospitaloperatorrepo.checkduplicateuser(bean.getUserName().toLowerCase());
			Integer namecount=hospitaloperatorrepo.checkduplicateuserbynameandmobile(bean.getFullName().trim(),bean.getMobileNo(),bean.getHospitalCode());
			if(count==0 && namecount==0) {
			HospitalOperator hospitaloperator=new HospitalOperator();
			hospitaloperator.setHospitalCode(bean.getHospitalCode());
			hospitaloperator.setUserName(bean.getUserName().trim().toLowerCase());
			hospitaloperator.setFullName(bean.getFullName());
			hospitaloperator.setEmail(bean.getEmailId());
			hospitaloperator.setMobileNo(bean.getMobileNo());
			hospitaloperator.setStateCode(bean.getStateCode());
			hospitaloperator.setDistCode(bean.getDistrictCode());
			hospitaloperator.setAddress(bean.getAddress());
			hospitaloperator.setStatusFlag(2);
			hospitaloperator.setCreatedBy(Long.parseLong(bean.getCreatedUserName()));
			hospitaloperator.setCreateOn(Calendar.getInstance().getTime());
			hospitaloperatorrepo.save(hospitaloperator);
			response.setStatus("200");
			response.setMessage("Record Inserted Successfully");
			}else {
				response.setStatus("400");
				if(count!=0) {response.setMessage("UserName Already Exist");}
				else {response.setMessage("User Already Exist");}				
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<HospitalOperator> findbyhospitalcode(String hospitalcode) {
		List<HospitalOperator> list=new ArrayList<HospitalOperator>();
		List<Object[]> objlist=hospitaloperatorrepo.findbycode(hospitalcode);	
		BigDecimal b;
		for(Object[] obj:objlist) {
			HospitalOperator hosp=new HospitalOperator();
			hosp.setFullName((String) obj[0]);
			hosp.setUserName((String) obj[1]);
			b=(BigDecimal) obj[2];
			hosp.setMobileNo(b.toString());
			hosp.setEmail((String) obj[3]);
			hosp.setStateCode((String) obj[4]);
			hosp.setDistCode((String) obj[5]);
			b=(BigDecimal) obj[6];
			String s="";
			if(b.intValue()==0) {
				s="Approved";
			}else if(b.intValue()==1) {
				s="In-Active";
			}else if(b.intValue()==2) {
				s="Applied";
			}else if(b.intValue()==3) {
				s="Rejected";
			}
			hosp.setStatusFlag(b.intValue());
			hosp.setStatus(s);
			b=(BigDecimal) obj[7];
			hosp.setOperatorId(b.longValue());
			list.add(hosp);
		}
		return list;
	}

	@Override
	public List<HospitalOperator> getAllHospitaloperatorforapprove(Integer groupid, Long userid) throws Exception {
		List<HospitalOperator> list=new ArrayList<HospitalOperator>();
		List<Object[]> objlist=new ArrayList<Object[]>();
		if(groupid==4) {
			objlist=hospitaloperatorrepo.hospitalapplylistsna(userid);
		}else {
			objlist=hospitaloperatorrepo.hospitalapplylistadmin();
		}		
		try {
			BigDecimal b;
			for(Object[] obj:objlist) {
				HospitalOperator hosp=new HospitalOperator();
				hosp.setHospitalCode((String) obj[0]);
				hosp.setHospitalname((String) obj[1]+" ("+(String) obj[0]+")");
				hosp.setHospmobile((String) obj[2]);
				hosp.setFullName((String) obj[3]);
				b=(BigDecimal) obj[4];
				hosp.setMobileNo(b.toString());
				hosp.setEmail((String) obj[5]);
				hosp.setStateCode((String) obj[6]);
				hosp.setDistCode((String) obj[7]);
				hosp.setApplydate((String) obj[8]);
				b=(BigDecimal) obj[9];
				hosp.setOperatorId(b.longValue());
				hosp.setUserName((String) obj[10]);
				list.add(hosp);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public HospitalOperator gethospoperatorbyid(Long operatorid) throws Exception {
		HospitalOperator deatis = new HospitalOperator();
		try {
			deatis=hospitaloperatorrepo.findById(operatorid).get();
		}catch (Exception e) {
			throw new Exception(e);
		}
		return deatis;
	}

	@Override
	public Response takeactiononhospitaloperatorlist(Long operatorid, Integer action,Long createdby) throws Exception{
		Response response=new Response();
		try {
			HospitalOperator hosp=hospitaloperatorrepo.findById(operatorid).get();
			if(hosp!=null) {				
					UserDetails userDetails=new UserDetails();
					userDetails.setCreatedUserName(hosp.getCreatedBy().toString());
					userDetails.setEmail(hosp.getEmail());
					userDetails.setPhone(Long.parseLong(hosp.getMobileNo()));
					GroupTypeDetails gr = new GroupTypeDetails();
					gr = groupRepo.findByTypeId(16);
					userDetails.setGroupId(gr);
					userDetails.setStatus(0);
					userDetails.setCreateDateTime(Calendar.getInstance().getTime());
					userDetails.setUserName(hosp.getUserName().toLowerCase());
					userDetails.setFullname(hosp.getFullName());
					userDetails.setTmsLoginStatus(0);
					String password = passwordEncoder.encode(env.getProperty("configKey"));
					userDetails.setPassword(password);
					userDetails.setIsOtpAllowed(1);
					hospitalOperatorLog hospoprlog=new hospitalOperatorLog();
					hospoprlog.setOperatorId(hosp.getOperatorId());
					hospoprlog.setHospitalCode(hosp.getHospitalCode());
					hospoprlog.setCreatedBy(createdby);
					hospoprlog.setCreateOn(Calendar.getInstance().getTime());
					hospoprlog.setStatusFlag(hosp.getStatusFlag());
					hospoprlog.setUserId(hosp.getUserId());
					hospoprlog.setUserName(hosp.getUserName());
					hospoprlog.setFullName(hosp.getFullName());
					hospoprlog.setMobileNo(hosp.getMobileNo());
					hospoprlog.setEmail(hosp.getEmail());
					hospoprlog.setStateCode(hosp.getStateCode());
					hospoprlog.setDistCode(hosp.getDistCode());
					hospoprlog.setUpdatedBy(hosp.getUpdatedBy());
					hospoprlog.setUpdateOn(hosp.getUpdateOn());					
					if(action==1) {
						hosp.setStatusFlag(0);
						Integer count = userRepo.checkusername(hosp.getUserName().toLowerCase());
						if(count==0) {
						hosp.setUserId(userDetails);
						}else {
							response.setStatus("400");
							response.setMessage("Already Action Taken ! Please Refresh The Page");
							return response;
						}
					}else {
						hosp.setStatusFlag(3);
					}				
					hosp.setUpdatedBy(createdby);
					hosp.setUpdateOn(Calendar.getInstance().getTime());		
					hospitaloperatorlogrepo.save(hospoprlog);
					HospitalOperator hosp1=hospitaloperatorrepo.save(hosp);
					if(hosp1!=null) {
						if(hosp1.getStatusFlag()==0) {
						int userId = hosp1.getUserId().getUserId().intValue();
						int groupId = hosp1.getUserId().getGroupId().getTypeId();
						adminService.copyPrimaryLinksForUser(userId, createdby.intValue(), groupId);
						}
					}
					response.setStatus("200");
					response.setMessage("Approved Successfully");				
			}else {
				throw new Exception("error");
			}			
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			throw new Exception(e);
		}
		return response;
	}

	private Integer updatemethodforoperator(UserDetailsProfileBean bean) throws Exception {
		Integer response=0;
		try {
			HospitalOperator hosp=hospitaloperatorrepo.findById(bean.getuId().longValue()).get();
			//log
			hospitalOperatorLog hospoprlog=new hospitalOperatorLog();
			hospoprlog.setOperatorId(hosp.getOperatorId());
			hospoprlog.setHospitalCode(hosp.getHospitalCode());
			hospoprlog.setCreatedBy(Long.parseLong(bean.getCreatedUserName()));
			hospoprlog.setCreateOn(Calendar.getInstance().getTime());
			hospoprlog.setStatusFlag(hosp.getStatusFlag());
			hospoprlog.setUserId(hosp.getUserId());
			hospoprlog.setUserName(hosp.getUserName());
			hospoprlog.setFullName(hosp.getFullName());
			hospoprlog.setMobileNo(hosp.getMobileNo());
			hospoprlog.setEmail(hosp.getEmail());
			hospoprlog.setStateCode(hosp.getStateCode());
			hospoprlog.setDistCode(hosp.getDistCode());
			hospoprlog.setUpdatedBy(hosp.getUpdatedBy());
			hospoprlog.setUpdateOn(hosp.getUpdateOn());
			UserDetails user=hosp.getUserId();
			if(user!=null) {
				user.setFullname(bean.getFullName());
				user.setEmail(bean.getEmailId());
				user.setPhone(Long.parseLong(bean.getMobileNo()));
				hosp.setUserId(user);
			}			
			hosp.setFullName(bean.getFullName());
			hosp.setMobileNo(bean.getMobileNo());
			hosp.setEmail(bean.getEmailId());
			hosp.setStateCode(bean.getStateCode());
			hosp.setDistCode(bean.getDistrictCode());
			hosp.setAddress(bean.getAddress());			
			hosp.setUpdatedBy(Long.parseLong(bean.getCreatedUserName()));
			hosp.setUpdateOn(Calendar.getInstance().getTime());
			hospitaloperatorlogrepo.save(hospoprlog);
			hospitaloperatorrepo.save(hosp);
			response=1;
		}catch (Exception e) {
			response=0;
			throw new Exception(e);
		}
		return response;
	}
	
	@Override
	public Integer updatehospitaloperator(UserDetailsProfileBean bean) throws Exception {
		Integer response=0;
		try {
			HospitalOperator hosp=hospitaloperatorrepo.findById(bean.getuId().longValue()).get();
			Integer namecount=hospitaloperatorrepo.checkduplicateuserbynameandmobile(bean.getFullName().trim(),bean.getMobileNo(),hosp.getHospitalCode());
			if(namecount==0) {
				response=updatemethodforoperator(bean);
			}else if(namecount==1) {
				HospitalOperator ho=hospitaloperatorrepo.checkduplicateuserbynameandmobiledata(bean.getFullName().trim(),bean.getMobileNo(),hosp.getHospitalCode());
				if(ho.getOperatorId().equals(bean.getuId().longValue())) {
					response=updatemethodforoperator(bean);
				}else {
					response=2;
				}
			}else {
				response=2;
			}
		}catch (Exception e) {
			response=0;
			throw new Exception(e);
		}
		return response;	
	}

	@Override
	public List<Object> gethospwiseoperatorcount(String state, String dist, String hospital, Long userid) throws Exception {
		List<Object> list=new ArrayList<Object>();
		try {
			List<Object[]> objlist=hospitaloperatorrepo.gethospwiseoperatorcount(state,dist,hospital,userid);
			for(Object[] obj:objlist) {
				HospitalBean bean=new HospitalBean();
				bean.setStateName((String) obj[0]);
				bean.setDistrictName((String) obj[1]);
				bean.setHospitalCode((String) obj[2]);
				bean.setHospitalName((String) obj[3]+" ("+(String) obj[2]+")");
				BigDecimal b=(BigDecimal) obj[4];
				bean.setCount(b.intValue());
				list.add(bean);
			}	
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public Long getoperatorid(Long userId) {
		HospitalOperator hospitaloper=hospitaloperatorrepo.getbyuserid(userId);
		return hospitaloper.getOperatorId();
	}

	@Override
	public List<Object> gethospwiseoperatorlistreport(String state, String dist, String hospital, Long userid)
			throws Exception {
		List<Object> list=new ArrayList<Object>();
		try {
			List<Object[]> objlist=hospitaloperatorrepo.gethospwiseoperatorlistreport(state,dist,hospital,userid);
			for(Object[] obj:objlist) {
				HospitalOperator bean=new HospitalOperator();
				bean.setStateCode((String) obj[0]);
				bean.setDistCode((String) obj[1]);
				bean.setHospitalCode((String) obj[2]);
				bean.setHospitalname((String) obj[3]+" ("+(String) obj[2]+")");
				bean.setHospmobile((String) obj[4]);
				bean.setFullName((String) obj[5]);
				BigDecimal b=(BigDecimal) obj[6];
				bean.setMobileNo(b.toString());
				list.add(bean);
			}	
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

}
