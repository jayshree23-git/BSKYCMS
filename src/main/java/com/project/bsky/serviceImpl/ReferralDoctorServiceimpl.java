/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospObj;
import com.project.bsky.bean.ReferralDoctorBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.HospitalTypeMaster;
import com.project.bsky.model.ReferalDoctor;
import com.project.bsky.model.ReferralDoctorLog;
import com.project.bsky.model.ReferralDoctorMapping;
import com.project.bsky.model.ReferralDoctorMappingLog;
import com.project.bsky.model.ReferralHospital;
import com.project.bsky.model.ReferralHospitalLog;
import com.project.bsky.model.ReferralResonMst;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.HospitalTypeMasterRepository;
import com.project.bsky.repository.ReferralDoctorLogRepository;
import com.project.bsky.repository.ReferralDoctorMappingLogRepository;
import com.project.bsky.repository.ReferralDoctorMappingRepositoty;
import com.project.bsky.repository.ReferralDoctorRepository;
import com.project.bsky.repository.ReferralHospitalLogRepository;
import com.project.bsky.repository.ReferralHospitalRepository;
import com.project.bsky.repository.ReferralResonMstRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.ReferralDoctorService;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class ReferralDoctorServiceimpl implements ReferralDoctorService {
	
	@Autowired
	private ReferralDoctorRepository referaldoctorrepo;
	
	@Autowired
	private ReferralDoctorLogRepository referalDoctorLogrepo;
	
	@Autowired
	private ReferralDoctorMappingRepositoty referaldoctormappingrepo;
	
	@Autowired
	private ReferralDoctorMappingLogRepository ReferralDoctorMappingLogrepo;
	
	@Autowired
	private ReferralResonMstRepository referralresonrepository;
	
	@Autowired
	private UserDetailsRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private GroupTypeRepository groupRepo;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private HospitalTypeMasterRepository hospitaltypemasterrepo;
	
	@Autowired
	private ReferralHospitalRepository referralhospitalrepo;
	
	@Autowired
	private ReferralHospitalLogRepository referralhospitallogrepo;
	
	@Autowired
	private Logger logger;

	@Override
	public Response savereferaldoctor(ReferalDoctor bean) {
		Response response=new Response();
		try {
			//System.out.println(bean);
			bean.setCreateon(Calendar.getInstance().getTime());
			bean.setStatus(0);
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(25);
			UserDetails userDetails=new UserDetails();
			userDetails.setCreateDateTime(Calendar.getInstance().getTime());
			userDetails.setCreatedUserName(bean.getCreateby().toString());
			userDetails.setEmail(bean.getEmailid());
			userDetails.setFullname(bean.getFullname());
			userDetails.setGroupId(gr);
			userDetails.setStatus(0);
			String password = passwordEncoder.encode(env.getProperty("configKey"));
			userDetails.setPassword(password);
			userDetails.setPhone(Long.parseLong(bean.getMobileno()));
			userDetails.setUserName(bean.getUsername().toLowerCase());
			bean.setUserid(userDetails);
			referaldoctorrepo.save(bean);
			response.setStatus("200");
			response.setMessage("User Created Successfully");
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			//System.out.println(e);
		}
		return response;
	}

	@Override
	public List<ReferalDoctor> getreferaldoctor() {	
		List<ReferalDoctor> list=referaldoctorrepo.findAll();
		try {			
			for(ReferalDoctor data:list) {
				data.setLicenseno(data.getLicenseno()!=null?data.getLicenseno():"N/A");
			}
			Collections.sort(list,(b1,b2)->(int)(b2.getDoctorid()-b1.getDoctorid()));
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response updatereferaldoctor(ReferalDoctor bean) {
		Response response=new Response();
		try {
			ReferalDoctor refdoc=referaldoctorrepo.findById(bean.getDoctorid()).get();
				try {
					ReferralDoctorLog doctorlog=new ReferralDoctorLog();
					doctorlog.setDoctorid(refdoc.getDoctorid());
					doctorlog.setCreateby(refdoc.getCreateby());
					doctorlog.setCreateon(refdoc.getCreateon());
					doctorlog.setUpdateby(bean.getUpdateby());
					doctorlog.setUpdateon(Calendar.getInstance().getTime());
					doctorlog.setEmailid(refdoc.getEmailid());
					doctorlog.setFullname(refdoc.getFullname());
					doctorlog.setLicenseno(refdoc.getLicenseno());
					doctorlog.setMobileno(refdoc.getMobileno());
					doctorlog.setUserid(refdoc.getUserid().getUserId());
					doctorlog.setStatus(refdoc.getStatus());
					//System.out.println(doctorlog);
					referalDoctorLogrepo.save(doctorlog);
				}catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			refdoc.setFullname(bean.getFullname());
			refdoc.setMobileno(bean.getMobileno());
			refdoc.setEmailid(bean.getEmailid());
			refdoc.setStatus(bean.getStatus());
			refdoc.setLicenseno(bean.getLicenseno());
			refdoc.setUpdateby(bean.getUpdateby());
			refdoc.setUpdateon(Calendar.getInstance().getTime());
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(25);
			UserDetails userDetails=userRepo.findById(refdoc.getUserid().getUserId()).get();
			userDetails.setEmail(bean.getEmailid());
			userDetails.setFullname(bean.getFullname());
			userDetails.setStatus(bean.getStatus());
			userDetails.setPhone(Long.parseLong(bean.getMobileno()));
			refdoc.setUserid(userDetails);
			referaldoctorrepo.save(refdoc);
			response.setStatus("200");
			response.setMessage("User Updated Successfully");
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public Response saverefdocConfiguration(ReferralDoctorBean referralDoctorBean) {
		Response response=new Response();
		try {
			ReferalDoctor refdoc=referaldoctorrepo.findByuserid(referralDoctorBean.getCpdId());
			for(HospObj list:referralDoctorBean.getHospList()) {
				Integer count=referaldoctormappingrepo.checkduplicate(list.getHospitalCode(),referralDoctorBean.getCpdId());
				ReferralHospital hosp=referralhospitalrepo.findById(Long.parseLong(list.getHospitalCode())).get();
				if(count==0) {
					ReferralDoctorMapping doctormapping=new ReferralDoctorMapping();
					doctormapping.setDoctorid(refdoc.getDoctorid());
					doctormapping.setUserid(referralDoctorBean.getCpdId());
					doctormapping.setStatecode("21");
					doctormapping.setDistcode(list.getDistrictCode());
					doctormapping.setBlockcode(hosp.getBlockcode());
					doctormapping.setHospitalcode(list.getHospitalCode());
					doctormapping.setHospitaltype(hosp.getHospitaltype().toString());
					doctormapping.setStatus(0);
					doctormapping.setCreateby(referralDoctorBean.getCreatedBy().longValue());
					doctormapping.setCreateon(Calendar.getInstance().getTime());
					referaldoctormappingrepo.save(doctormapping);
				}else {
					ReferralDoctorMapping doctormapping=referaldoctormappingrepo.getdataofduplicate(list.getHospitalCode(),referralDoctorBean.getCpdId());
					doctormapping.setStatus(0);
					doctormapping.setUpdateby(referralDoctorBean.getUpdatedBy().longValue());
					doctormapping.setUpdateon(Calendar.getInstance().getTime());
					doctormapping.setBlockcode(list.getBlockCode());
					referaldoctormappingrepo.save(doctormapping);
				}					
			}
			response.setStatus("200");
			response.setMessage("User Mapped Sucessfully");
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public String getdoctortaglist() {
		JSONArray doctortaglist = new JSONArray();
		List<Object[]> list=referaldoctormappingrepo.getdoctortaglist();
		try {
			for(Object[] obj:list) {
				JSONObject hospital = new JSONObject();
				hospital.put("Userid", obj[0]);
				hospital.put("doctorid", obj[1]);
				hospital.put("fullname", obj[2]);
				hospital.put("Hospitalcount", obj[3]);
				doctortaglist.put(hospital);
			}			
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return doctortaglist.toString();
	}

	@Override
	public Response savereferralreson(ReferralResonMst bean) {
		Response response=new Response();
		try {
			Integer count=referralresonrepository.checkduplicate(bean.getReferaldesc());
			if(count==0) {
				bean.setCreateon(Calendar.getInstance().getTime());
				bean.setStatus(0);
				referralresonrepository.save(bean);
				response.setStatus("200");
				response.setMessage("Record Submitted Sucessfully");
			}else {
				response.setStatus("400");
				response.setMessage("Referral Reson Already Exist");
			}
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<ReferralResonMst> getallreferralreson() {			
		return referralresonrepository.getalldata();
	}

	@Override
	public Response updatereferralreson(ReferralResonMst bean) {
		Response response=new Response();
		try {
			Integer count=referralresonrepository.checkduplicate(bean.getReferaldesc());			
			if(count==0) {		
				ReferralResonMst refmst=referralresonrepository.findById(bean.getReferralid()).get();
				refmst.setReferaldesc(bean.getReferaldesc());
				refmst.setStatus(bean.getStatus());
				refmst.setUpdateby(bean.getCreatedby());
				refmst.setUpdateon(Calendar.getInstance().getTime());
				referralresonrepository.save(refmst);
				response.setStatus("200");
				response.setMessage("Record Updated Sucessfully");
			}else if(count==1) {
				ReferralResonMst refmst=referralresonrepository.findByreferaldesc(bean.getReferaldesc());
				if(refmst.getReferralid().equals(bean.getReferralid())) {
					refmst.setReferaldesc(bean.getReferaldesc());
					refmst.setStatus(bean.getStatus());
					refmst.setUpdateby(bean.getCreatedby());
					refmst.setUpdateon(Calendar.getInstance().getTime());
					referralresonrepository.save(refmst);
					response.setStatus("200");
					response.setMessage("Record Updated Sucessfully");
				}else {
					response.setStatus("400");
					response.setMessage("Referral Reson Already Exist");
				}				
			}else {
				response.setStatus("400");
				response.setMessage("Referral Reson Already Exist");
			}
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public Response savereferalhospital(ReferralHospital bean) {
		Response response=new Response();
		try {
			bean.setStatecode("21");
			bean.setStatus(0);
			bean.setCreatedon(Calendar.getInstance().getTime());
			//System.out.println(bean);
			referralhospitalrepo.save(bean);
			response.setStatus("200");
			response.setMessage("Hospital Created Successfully");
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<ReferralHospital> getreferalhospitallist() {
		List<ReferralHospital> list=new ArrayList<ReferralHospital>();
		try {	
			List<Object[]> objlist=new ArrayList<Object[]>();
			objlist=referralhospitalrepo.getreferalhospitallist();
			for(Object[] obj:objlist) {
				BigDecimal b;
				ReferralHospital hosp=new ReferralHospital();
				b=(BigDecimal) obj[0];
				hosp.setHospitalid(b.longValue());
				hosp.setHospitalname((String) obj[1]);
				hosp.setStatecode((String) obj[2]);
				hosp.setDistcode((String) obj[3]);
				hosp.setBlockcode(((String) obj[4])==null?"N/A":(String) obj[4]);
				hosp.setHospitaltypename((String) obj[5]);
				hosp.setCnctperson(((String) obj[6])==null?"N/A":(String) obj[6]);
				hosp.setMobileno(((String) obj[7])==null?"N/A":(String) obj[7]);
				hosp.setAddress(((String) obj[8])==null?"N/A":(String) obj[8]);
				b=(BigDecimal) obj[9];
				hosp.setStatus(b.intValue());
				list.add(hosp);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalTypeMaster> getreferralhospitaltype() {
		List<HospitalTypeMaster> list=new ArrayList<HospitalTypeMaster>();
		try {
			list=hospitaltypemasterrepo.getallbydesc();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public ReferralHospital getreferralhospitalbyid(Long hospitalid) {
		ReferralHospital referralhospital=new ReferralHospital();
		try {
			referralhospital=referralhospitalrepo.findById(hospitalid).get();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return referralhospital;
	}

	@Override
	public Response updatereferalhospital(ReferralHospital bean) {
		Response response=new Response();
		try {
			ReferralHospital referralhospital=referralhospitalrepo.findById(bean.getHospitalid()).get();
				try {
					ReferralHospitalLog log=new ReferralHospitalLog();
					log.setHospitalid(referralhospital.getHospitalid());
					log.setHospitalname(referralhospital.getHospitalname());
					log.setStatecode("21");
					log.setDistcode(referralhospital.getDistcode());
					log.setBlockcode(referralhospital.getBlockcode());
					log.setHospitaltype(referralhospital.getHospitaltype());
					log.setCnctperson(referralhospital.getCnctperson());
					log.setMobileno(referralhospital.getMobileno());
					log.setAddress(referralhospital.getAddress());
					log.setStatus(referralhospital.getStatus());
					log.setCreatedby(referralhospital.getCreatedby());
					log.setCreatedon(referralhospital.getCreatedon());
					log.setUpdatedby(bean.getUpdatedby());
					log.setUpdatedon(Calendar.getInstance().getTime());
					referralhospitallogrepo.save(log);
				}catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
			bean.setStatecode("21");
			bean.setCreatedby(referralhospital.getCreatedby());
			bean.setCreatedon(referralhospital.getCreatedon());
			bean.setUpdatedon(Calendar.getInstance().getTime());
			referralhospitalrepo.save(bean);
			response.setStatus("200");
			response.setMessage("Hospital Updated Successfully");
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<ReferralHospital> getrefHospitalbyDistrictId(String distid) {
		List<ReferralHospital> list=new ArrayList<ReferralHospital>();
		try {
			list=referralhospitalrepo.getbydistid(distid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<ReferralHospital> getdoctortaglistbydoctorid(Long userid) {
		List<ReferralHospital> list=new ArrayList<ReferralHospital>();
		try {
			List<Object[]> objlist=new ArrayList<Object[]>();
			objlist=referaldoctormappingrepo.getdoctortaglistbydoctorid(userid);
			for(Object[] obj:objlist) {
				ReferralHospital hosp=new ReferralHospital();
				hosp.setHospitalid(Long.parseLong((String) obj[0]));
				hosp.setHospitalname((String) obj[1]);
				hosp.setStatecode((String) obj[2]);
				hosp.setDistcode((String) obj[3]);
				hosp.setBlockcode(((String) obj[4])==null?"N/A":(String) obj[4]);
				hosp.setHospitaltypename((String) obj[5]);
				list.add(hosp);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<ReferralHospital> getrefHospitalbyDistrictIdblockid(String distid, String block) {
		List<ReferralHospital> list=new ArrayList<ReferralHospital>();
		try {
			list=referralhospitalrepo.getrefHospitalbyDistrictIdblockid(distid,block);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public void refdocConfigurationlog(Long cpdId,Integer updateby) {
//		ReferralDoctorMappingLog
		try {
			List<ReferralDoctorMapping> list=referaldoctormappingrepo.getalltagged(cpdId);
			for(ReferralDoctorMapping x:list) {
				ReferralDoctorMappingLog doclog=new ReferralDoctorMappingLog();
				doclog.setDoctormappingid(x.getDoctormappingid());
				doclog.setDoctorid(x.getDoctorid());
				doclog.setStatecode(x.getStatecode());
				doclog.setDistcode(x.getDistcode());
				doclog.setBlockcode(x.getBlockcode());
				doclog.setHospitalname(x.getHospitalcode());
				doclog.setHospitaltype(x.getHospitaltype());
				doclog.setCreateon(x.getUpdateon());
				doclog.setCreateby(x.getUpdateby());
				doclog.setUpdateby(updateby.longValue());
				doclog.setUpdateon(Calendar.getInstance().getTime());
				doclog.setStatus(x.getStatus());
				ReferralDoctorMappingLogrepo.save(doclog);
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
	}

	@Override
	public Response updaterefdocConfiguration(ReferralDoctorBean referralDoctorBean) {
		Response response=new Response();
		try {
			referaldoctormappingrepo.inactiveall(referralDoctorBean.getCpdId(),referralDoctorBean.getUpdatedBy().longValue());
			ReferalDoctor refdoc=referaldoctorrepo.findByuserid(referralDoctorBean.getCpdId());
			for(HospObj list:referralDoctorBean.getHospList()) {
				Integer count=referaldoctormappingrepo.checkduplicate(list.getHospitalCode(),referralDoctorBean.getCpdId());
				ReferralHospital hosp=referralhospitalrepo.findById(Long.parseLong(list.getHospitalCode())).get();
				if(count==0) {
					ReferralDoctorMapping doctormapping=new ReferralDoctorMapping();
					doctormapping.setDoctorid(refdoc.getDoctorid());
					doctormapping.setUserid(referralDoctorBean.getCpdId());
					doctormapping.setStatecode("21");
					doctormapping.setDistcode(list.getDistrictCode());
					doctormapping.setBlockcode(hosp.getBlockcode());
					doctormapping.setHospitalcode(list.getHospitalCode());
					doctormapping.setHospitaltype(hosp.getHospitaltype().toString());
					doctormapping.setStatus(0);
					doctormapping.setCreateby(referralDoctorBean.getCreatedBy().longValue());
					doctormapping.setCreateon(Calendar.getInstance().getTime());
					referaldoctormappingrepo.save(doctormapping);
				}else {
					ReferralDoctorMapping doctormapping=referaldoctormappingrepo.getdataofduplicate(list.getHospitalCode(),referralDoctorBean.getCpdId());
					doctormapping.setStatus(0);
					doctormapping.setUpdateby(referralDoctorBean.getUpdatedBy().longValue());
					doctormapping.setUpdateon(Calendar.getInstance().getTime());
					doctormapping.setBlockcode(list.getBlockCode());
					referaldoctormappingrepo.save(doctormapping);
				}					
			}
			response.setStatus("200");
			response.setMessage("User Mapped Sucessfully");
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<HospitalTypeMaster> getallhosptype() {
		List<HospitalTypeMaster> list=new ArrayList<HospitalTypeMaster>();
		try {
			list=hospitaltypemasterrepo.findAll();
			Collections.sort(list,(b1,b2)->(int)(b2.getHospitaltypeid()-b1.getHospitaltypeid()));
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response saverefhospitaltype(HospitalTypeMaster hospitalTypeMaster) {
		Response response=new Response();
		try {
			Integer count=hospitaltypemasterrepo.checkduplicate(hospitalTypeMaster.getHospitaltypename());
			if(count==0) {
				hospitalTypeMaster.setStatus(0);
				hospitalTypeMaster.setCreatedon(Calendar.getInstance().getTime());
				hospitaltypemasterrepo.save(hospitalTypeMaster);
				response.setStatus("200");
				response.setMessage("Hospital Type Created Successfully");
			}else{
				response.setStatus("400");
				response.setMessage("Hospital Type Already Exist");
			}
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public Response updaterefhospitaltype(HospitalTypeMaster hospitalTypeMaster) {
		Response response=new Response();
		try {
			Integer count=hospitaltypemasterrepo.checkduplicate(hospitalTypeMaster.getHospitaltypename());
			if(count==0) {
				HospitalTypeMaster hospitalTypeMaster1=hospitaltypemasterrepo.findById(hospitalTypeMaster.getHospitaltypeid()).get();
				hospitalTypeMaster.setCreatedby(hospitalTypeMaster1.getCreatedby());
				hospitalTypeMaster.setCreatedon(hospitalTypeMaster1.getCreatedon());
				hospitaltypemasterrepo.save(hospitalTypeMaster);
				response.setStatus("200");
				response.setMessage("Hospital Type Updated Successfully");
			}else{
				HospitalTypeMaster hospitalTypeMaster1=hospitaltypemasterrepo.getduplicate(hospitalTypeMaster.getHospitaltypename());
				if(hospitalTypeMaster.getHospitaltypeid().equals(hospitalTypeMaster1.getHospitaltypeid())) {
					hospitalTypeMaster.setCreatedby(hospitalTypeMaster1.getCreatedby());
					hospitalTypeMaster.setCreatedon(hospitalTypeMaster1.getCreatedon());
					hospitaltypemasterrepo.save(hospitalTypeMaster);
					response.setStatus("200");
					response.setMessage("Hospital Type Updated Successfully");
				}else {
					response.setStatus("400");
					response.setMessage("Hospital Type Already Exist");
				}
			}
		}catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public List<ReferralHospital> getrefHospitalbyDistrictIdhospitaltype(String distid, Integer hosptypeid) {
		List<ReferralHospital> list=new ArrayList<ReferralHospital>();
		try {
			list=referralhospitalrepo.getrefHospitalbyDistrictIdhospitaltype(distid,hosptypeid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	
	
}
