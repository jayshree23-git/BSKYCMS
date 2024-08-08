/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Cpdconfigbean;
import com.project.bsky.bean.Cpdspecialitydocbean;
import com.project.bsky.bean.Cpdspecialitymappingbean;
import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CPDConfiguration;
import com.project.bsky.model.CPDConfigurationLog;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.repository.CPDConfigurationLogRepository;
import com.project.bsky.repository.CPDConfigurationRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.UserDetailsCpdReposiitory;
import com.project.bsky.service.ConfigurationService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.CpdSpecialitydocument;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class ConfigurationServiceimpl implements ConfigurationService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationServiceimpl.class);
	
	@Autowired
	private UserDetailsCpdReposiitory userdetailscpdrepository;
	
	@Autowired
	private CPDConfigurationRepository cpdconfigrepository;
	
	@Autowired
	private HospitalInformationRepository hospitalRepo;
	
	@Autowired
	private CPDConfigurationLogRepository cpdconfiglogRepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Environment env;
	
	private Connection connection = null;
	private CallableStatement statement = null;

	@Override
	public List<Cpdconfigbean> gettaggedhospitalofcpd(Long userid) {
		List<Cpdconfigbean> taggedhospital=new ArrayList<Cpdconfigbean>();
		try {
			UserDetailsCpd userdetailcpd=userdetailscpdrepository.findByuserid(userid);
			if(userdetailcpd!=null) {
				List<CPDConfiguration> configlist=cpdconfigrepository.findBycpduserId(userdetailcpd.getBskyUserId());
				for(CPDConfiguration list:configlist) {
					if(list.getStatus()==0) {
						Cpdconfigbean bean=new Cpdconfigbean();
						bean.setHospital(hospitalRepo.findByhospitalCode(list.getHospitalCode()));
						bean.setStatus(list.getStatus()!=null?list.getStatus().toString():"");
						bean.setBskyuserid(userdetailcpd.getBskyUserId());
						bean.setApplieddate(list.getCreatedOn()!=null?list.getCreatedOn().toString():"N/A");
						taggedhospital.add(bean);
					}
				}
			}
			Collections.sort(taggedhospital,(b1,b2)->(int)(b1.getHospital().getHospitalName().compareTo(b2.getHospital().getHospitalName())));
		}catch (Exception e) {
			logger.error("Error "+e);
		}
		return taggedhospital;
	}

	@Override
	public Response applyforexclusion(String hospitalcode, Integer bskyuserid) {
		Response response=new Response();
		try {
			CPDConfiguration config=cpdconfigrepository.applyforexclusion(hospitalcode,bskyuserid);
			config.setStatus(3);
			config.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			cpdconfigrepository.save(config);
			response.setMessage("Applied Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error("Error "+e);
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public List<Cpdconfigbean> getappliedexclusionlistadmin() {
		List<Cpdconfigbean> appliedlist=new ArrayList<Cpdconfigbean>();
		try {
			List<CPDConfiguration> configlist=cpdconfigrepository.appliedexclusionlistadmin();
			for(CPDConfiguration list:configlist) {
				Cpdconfigbean bean=new Cpdconfigbean();
				bean.setHospital(hospitalRepo.findByhospitalCode(list.getHospitalCode()));
				bean.setBskyuserid(list.getCpdUserId());
				bean.setTaggeddate(list.getCreatedOn()!=null?list.getCreatedOn().toString():"");
				bean.setApplieddate(list.getUpdatedOn()!=null?list.getUpdatedOn().toString():"");
				UserDetailsCpd userdetailcpd=userdetailscpdrepository.findById(list.getCpdUserId()).get();
				bean.setName(userdetailcpd.getFullName());
				bean.setUsername(userdetailcpd.getUserName());
				bean.setMobileno(userdetailcpd.getMobileNo()!=null?userdetailcpd.getMobileNo().toString():"");
				appliedlist.add(bean);
			}
		}catch (Exception e) {
			logger.error("Error "+e);
		}
		return appliedlist;
	}

	@Override
	public List<Cpdconfigbean> getappliedexclusionlistSNA(Long snoid) {
		List<Cpdconfigbean> appliedlist=new ArrayList<Cpdconfigbean>();
		try {
			List<Object[]> configlist=cpdconfigrepository.appliedexclusionlistsna(snoid);
			for(Object[] list:configlist) {
				Cpdconfigbean bean=new Cpdconfigbean();
				bean.setHospital(hospitalRepo.findByhospitalCode((String) list[0]));
				bean.setStatus(((Character)list[1]).toString());
				Number id=(Number)list[2];
				Integer userid=id.intValue();
				bean.setBskyuserid(userid);
				UserDetailsCpd userdetailcpd=userdetailscpdrepository.findById(userid).get();
				bean.setName(userdetailcpd.getFullName());
				bean.setUsername(userdetailcpd.getUserName());
				bean.setMobileno(userdetailcpd.getMobileNo().toString());
				bean.setTaggeddate((String) list[4]);
				bean.setApplieddate((String) list[5]);
				appliedlist.add(bean);
			}
		}catch (Exception e) {
			logger.error("Error "+e);
		}
		return appliedlist;
	}
	
	@Override
	public List<Cpdconfigbean> getappliedinclusionlistadmin() {
		List<Cpdconfigbean> appliedlist=new ArrayList<Cpdconfigbean>();
		try {
			List<CPDConfiguration> configlist=cpdconfigrepository.appliedinclusionlistadmin();
			for(CPDConfiguration list:configlist) {
				Cpdconfigbean bean=new Cpdconfigbean();
				bean.setHospital(hospitalRepo.findByhospitalCode(list.getHospitalCode()));
				bean.setBskyuserid(list.getCpdUserId());
				UserDetailsCpd userdetailcpd=userdetailscpdrepository.findById(list.getCpdUserId()).get();
				bean.setName(userdetailcpd.getFullName());
				bean.setUsername(userdetailcpd.getUserName());
				bean.setApplieddate(list.getUpdatedOn()!=null?list.getUpdatedOn().toString():list.getCreatedOn().toString());
				bean.setMobileno(userdetailcpd.getMobileNo().toString());
				appliedlist.add(bean);
			}
		}catch (Exception e) {
			logger.error("Error "+e);
		}
		return appliedlist;
	}

	@Override
	public List<Cpdconfigbean> getappliedinclusionlistSNA(Long snoid) {
		List<Cpdconfigbean> appliedlist=new ArrayList<Cpdconfigbean>();
		try {
			List<Object[]> configlist=cpdconfigrepository.appliedinclusionlistsna(snoid);
			for(Object[] list:configlist) {
				Cpdconfigbean bean=new Cpdconfigbean();
				bean.setHospital(hospitalRepo.findByhospitalCode((String) list[0]));
				bean.setStatus(((Character)list[1]).toString());
				Number id=(Number)list[2];
				Integer userid=id.intValue();
				bean.setBskyuserid(userid);
				UserDetailsCpd userdetailcpd=userdetailscpdrepository.findById(userid).get();
				bean.setName(userdetailcpd.getFullName());
				bean.setUsername(userdetailcpd.getUserName());
				bean.setMobileno(userdetailcpd.getMobileNo().toString());
				appliedlist.add(bean);
			}
		}catch (Exception e) {
			logger.error("Error "+e);
		}
		return appliedlist;
	}

	@Override
	public Response approveofexclusion(String hospitalcode, Integer bskyuserid,Integer userid, String ipaddress) {
		Response response=new Response();
		try {
			CPDConfiguration config=cpdconfigrepository.applyforexclusion(hospitalcode,bskyuserid);
			CPDConfigurationLog log = new CPDConfigurationLog();
			log.setMappingId(config.getMappingId());
			log.setCpdUserId(bskyuserid);
			log.setHospitalCode(config.getHospitalCode());
			log.setDistrictCode(config.getDistrictCode());
			log.setStateCode(config.getStateCode());
			log.setStatus(config.getStatus());
			log.setUpdatedBy(config.getUpdatedBy());
			log.setUpdatedOn(config.getUpdatedOn());
			log.setCreatedBy(userid);
			log.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			log.setSystemIp(ipaddress);
			cpdconfiglogRepo.save(log);
			config.setStatus(1);
			config.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			config.setUpdatedBy(userid);
			cpdconfigrepository.save(config);
			response.setMessage("Approved Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error("Error "+e);
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public List<HospitalBean> gethospitalforinclusion(Long userid, String state, String dist) {
		List<HospitalBean> list=new ArrayList<HospitalBean>();		
		if(state.equals("null")) {
			state="";
		}
		try {
			List<Object[]> objlist=cpdconfigrepository.getuntaghospitalofcpd(userid,state,dist);
			for(Object[] obj:objlist) {
				HospitalBean hosp=new HospitalBean();
				String hospitalcode=(String)obj[0];
				hosp.setHospitalCode(hospitalcode);
				hosp.setHospitalName((String)obj[1]);
				hosp.setStateName((String)obj[2]);
				hosp.setDistrictName((String)obj[3]);
				list.add(hosp);
			}
			Collections.sort(list,(b1,b2)->(int)(b1.getHospitalName().compareTo(b2.getHospitalName())));
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response applyhospitalforinclusion(String hospitalcode, Integer userid) {
		Response response=new Response();
		try {
			UserDetailsCpd userdetailcpd=userdetailscpdrepository.findByuserid(userid.longValue());
			Integer checkCPD = cpdconfigrepository.checkCPDConfigDulicacy(hospitalcode,userdetailcpd.getBskyUserId());
			if(checkCPD==0) {
				CPDConfiguration cpdConfig = new CPDConfiguration();
				cpdConfig.setCpdUserId(userdetailcpd.getBskyUserId());
				cpdConfig.setHospitalCode(hospitalcode);
				HospitalInformation hospital=hospitalRepo.findByhospitalCode(hospitalcode);
				cpdConfig.setStateCode(hospital.getDistrictcode().getStatecode().getStateCode());
				cpdConfig.setDistrictCode(hospital.getDistrictcode().getDistrictcode());
				cpdConfig.setStatus(2);
				cpdConfig.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				cpdConfig.setCreatedBy(userid);
				cpdconfigrepository.save(cpdConfig);
			}else {
				CPDConfiguration conf = cpdconfigrepository.getthatrrcord(hospitalcode,userdetailcpd.getBskyUserId());
				CPDConfigurationLog log = new CPDConfigurationLog();
				log.setMappingId(conf.getMappingId());
				log.setCpdUserId(userid);
				log.setHospitalCode(conf.getHospitalCode());
				log.setDistrictCode(conf.getDistrictCode());
				log.setStateCode(conf.getStateCode());
				log.setStatus(conf.getStatus());
				log.setUpdatedBy(conf.getUpdatedBy());
				log.setUpdatedOn(conf.getUpdatedOn());
				log.setCreatedBy(userid);
				log.setCreatedOn(new Timestamp(System.currentTimeMillis()));
				cpdconfiglogRepo.save(log);
				conf.setStatus(2);
				conf.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
				conf.setUpdatedBy(userid);
				cpdconfigrepository.save(conf);
			}
			response.setMessage("Applied Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error("Error "+e);
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public Response approveofinclusion(String hospitalcode, Integer bskyuserid, Integer userid, String ipaddress) {
		Response response=new Response();
		try {
			CPDConfiguration config=cpdconfigrepository.applyforinclusion(hospitalcode,bskyuserid);
			CPDConfigurationLog log = new CPDConfigurationLog();
			log.setMappingId(config.getMappingId());
			log.setCpdUserId(bskyuserid);
			log.setHospitalCode(config.getHospitalCode());
			log.setDistrictCode(config.getDistrictCode());
			log.setStateCode(config.getStateCode());
			log.setStatus(config.getStatus());
			log.setUpdatedBy(config.getUpdatedBy());
			log.setUpdatedOn(config.getUpdatedOn());
			log.setCreatedBy(userid);
			log.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			log.setSystemIp(ipaddress);
			cpdconfiglogRepo.save(log);
			config.setStatus(0);
			config.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			config.setUpdatedBy(userid);
			cpdconfigrepository.save(config);
			response.setMessage("Approved Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error("Error "+e);
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
		return response;
	}

	@Override
	public List<HospitalBean> getcpdtagginglog(Long userid) {
		List<HospitalBean> list=new ArrayList<HospitalBean>();		
		try {
			List<Object[]> objlist=cpdconfigrepository.getcpdtagginglog(userid);
			for(Object[] obj:objlist) {
				HospitalBean hosp=new HospitalBean();
				String hospitalcode=(String)obj[0];
				hosp.setHospitalCode(hospitalcode);
				hosp.setHospitalName((String)obj[1]);
				hosp.setStateName((String)obj[2]);
				hosp.setDistrictName((String)obj[3]);
				Number n=(Number)obj[6];
				hosp.setStatus(n.intValue());
				hosp.setCreateon((String)obj[4]);
				hosp.setUpdateon((String)obj[5]);
				list.add(hosp);
			}			
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<HospitalBean> getcpdtaggedhospital(Long userid, Integer type) {
		List<HospitalBean> list=new ArrayList<HospitalBean>();	
		try {
			UserDetailsCpd userdetailcpd=userdetailscpdrepository.findByuserid(userid.longValue());
			List<Object[]> objlist=new ArrayList<Object[]>();
			if(type==1) {
				objlist=cpdconfigrepository.getcpdtagginglist(userdetailcpd.getBskyUserId());
			}else if(type==2){
				objlist=cpdconfigrepository.getcpduntagginglist(userdetailcpd.getBskyUserId());
			}else if(type==3){
				objlist=cpdconfigrepository.getappliedlistbycpd(userdetailcpd.getBskyUserId());
			}else {
				objlist=cpdconfigrepository.getcpdtagginglog(userdetailcpd.getBskyUserId().longValue());
			}
			for(Object[] obj:objlist) {
				HospitalBean hosp=new HospitalBean();
				String hospitalcode=(String)obj[0];
				hosp.setHospitalCode(hospitalcode);
				hosp.setHospitalName((String)obj[1]);
				hosp.setStateName((String)obj[2]);
				hosp.setDistrictName((String)obj[3]);
				if(type==3) {
					Date s=(Date)obj[4];
					if(s!=null) {
						hosp.setCreateon(new SimpleDateFormat("dd-MMM-YYYY, hh:mm:ss a").format(s));
					}else {
						hosp.setCreateon("N/A");
					}
				}else {
					hosp.setCreateon((String)obj[4]);
				}								
				hosp.setUpdateon((String)obj[5]);
				list.add(hosp);
			}	
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Response cancelrequest(String hospitalcode,Long userid) {
		Response response=new Response();
		try {
			UserDetailsCpd userdetailcpd=userdetailscpdrepository.findByuserid(userid);
			CPDConfiguration config=cpdconfigrepository.applyforinclusion(hospitalcode,userdetailcpd.getBskyUserId());
			CPDConfigurationLog log = new CPDConfigurationLog();
			log.setMappingId(config.getMappingId());
			log.setCpdUserId(userdetailcpd.getBskyUserId());
			log.setHospitalCode(config.getHospitalCode());
			log.setDistrictCode(config.getDistrictCode());
			log.setStateCode(config.getStateCode());
			log.setStatus(config.getStatus());
			log.setUpdatedBy(config.getUpdatedBy());
			log.setUpdatedOn(config.getUpdatedOn());
			log.setCreatedBy(userid.intValue());
			log.setCreatedOn(new Timestamp(System.currentTimeMillis()));
			cpdconfiglogRepo.save(log);
			if(config.getStatus()==3) {
				config.setStatus(0);
			}else if(config.getStatus()==2) {
				if(config.getUpdatedOn()!=null) {
					config.setStatus(1);
				}else {
					config.setStatus(4);
				}
			}
			config.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
			config.setUpdatedBy(userid.intValue());
			cpdconfigrepository.save(config);
		response.setMessage("Approved Successfully");
		response.setStatus("200");
		} catch (Exception e) {
			logger.error("Error "+e);
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
	return response;
	}

	@Override
	public List<Object> cpdhospitaltaglist(Long cpdId, Long cpduserid, Integer status,Long userid) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try{
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CPD_HOSPITAL_TAGGING_RPT")
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_USER_ID", userid);
			storedProcedure.setParameter("P_CPD_USER_ID", cpduserid);
			storedProcedure.setParameter("P_CPD_ID", cpdId);
			storedProcedure.setParameter("P_STATUS", status);
			
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			
			while (rs.next()) {
				Map<String,Object> map=new HashMap<>();
				map.put("cpdid", rs.getString(1));
				map.put("cpdname", rs.getString(2));
				map.put("hospital", rs.getString(3));
				map.put("hospitalcode", rs.getString(4));	
				map.put("state", rs.getString(5));	
				map.put("dist", rs.getString(6));	
				map.put("status", rs.getString(7));				
				map.put("periodfrom", rs.getString(8));
				map.put("periodto", rs.getString(9));
				list.add(map);
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return list;
	}
	
	private void getconnection() throws Exception {				
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public List<Object> getcpdmappedPackageList(Long cpdId, Long cpduserid) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;		
		try{
			getconnection();
			Object[] stringArray = new Object[0]; 
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_CPD_SPECIALITY_MAPPING_DETAILS", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, stringArray);
			
			statement = connection.prepareCall("call USP_CPD_SPECIALITY_MAPPING(?,?,?,?,?,?)");
			statement.setInt(1, 1);//P_ACTIONCODE
			statement.setLong(2, 0l);//P_USER_ID
			statement.setLong(3, cpduserid);//P_CPD_USER_ID
			statement.setArray(4, array_to_pass);//P_CPD_SP_MP_LIST
			statement.registerOutParameter(5, Types.REF_CURSOR);//P_MSG_OUT
			statement.registerOutParameter(6, Types.INTEGER);//P_OUT
			statement.execute();
			rs = ((OracleCallableStatement) statement).getCursor(5);
			while (rs.next()) {
				Map<String,Object> map=new HashMap<>();
				map.put("packageid", rs.getString(1));
				map.put("packagecode",rs.getString(2));
				map.put("packagename", rs.getString(3));
				map.put("document", rs.getString(4));
				map.put("status", rs.getString(5));
				map.put("docstatus", rs.getString(5));
				list.add(map);
			}			
		}catch (Exception e) {
			throw new Exception(e);			
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> saveCPDSpecialityMapping(Cpdspecialitymappingbean bean) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {			
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			for(Cpdspecialitydocbean obj:bean.getSpeciality()) {
				if(obj.getFile()!=null) {
					obj.setDocname(CpdSpecialitydocument.createFileforcpdspeciality(year, obj.getFile(), bean.getCpdId(),obj.getPackagename()));
				}
			}
			
			StructDescriptor structDescriptor;	        
	        getconnection();					
			STRUCT[] quesbean = new STRUCT[bean.getSpeciality().size()];
			
			int i=0;
			for(Cpdspecialitydocbean obj:bean.getSpeciality()){
				structDescriptor = new StructDescriptor("CPD_SPECIALITY_MAPPING_T", connection);
				 Object[] ObjArr = {
						 obj.getPackageid(),
						 obj.getDocname(),
						 obj.getStatus(),
		            };
				 quesbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
		            i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_CPD_SPECIALITY_MAPPING_DETAILS", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, quesbean);
			
			statement = connection.prepareCall("call USP_CPD_SPECIALITY_MAPPING(?,?,?,?,?,?)");
			statement.setInt(1, 2);//P_ACTIONCODE
			statement.setLong(2, bean.getUserId());//P_USER_ID
			statement.setLong(3, bean.getCpdId());//P_CPD_USER_ID
			statement.setArray(4, array_to_pass);//P_CPD_SP_MP_LIST
			statement.registerOutParameter(5, Types.REF_CURSOR);//P_MSG_OUT
			statement.registerOutParameter(6, Types.INTEGER);//P_OUT
			statement.execute();
			Integer out = statement.getInt(6);
			if(out==1) {
				map.put("status", 200);
				map.put("message", "Speciality Mapped Successfully");
			}else {
				map.put("error", "Error From Db");
				map.put("status", 400);
				map.put("message", "Something Went Wrong");
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}
		return map;
	}

	@Override
	public void downloadcpdspecdoc(String fileName, String userid, HttpServletResponse response) {
		String folderName = null;
		try {
			CpdSpecialitydocument.downloadcpdspecdoc(fileName, userid,response);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<Object> getcpdtaggedPackageList(Long cpduserid) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;		
		try{
			getconnection();
			Object[] stringArray = new Object[0]; 
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_CPD_SPECIALITY_MAPPING_DETAILS", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, stringArray);
			
			statement = connection.prepareCall("call USP_CPD_SPECIALITY_MAPPING(?,?,?,?,?,?)");
			statement.setInt(1, 3);//P_ACTIONCODE
			statement.setLong(2, 0l);//P_USER_ID
			statement.setLong(3, cpduserid);//P_CPD_USER_ID
			statement.setArray(4, array_to_pass);//P_CPD_SP_MP_LIST
			statement.registerOutParameter(5, Types.REF_CURSOR);//P_MSG_OUT
			statement.registerOutParameter(6, Types.INTEGER);//P_OUT
			statement.execute();
			
			rs = ((OracleCallableStatement) statement).getCursor(5);
			
			while (rs.next()) {
				Map<String,Object> map=new HashMap<>();
				map.put("cpduserid", rs.getString(1));
				map.put("cpdname", rs.getString(2));
				map.put("packageid",rs.getString(3));
				map.put("packagename",rs.getString(4));
				map.put("packagecode", rs.getString(5));
				map.put("mobileNo", rs.getString(6));
				map.put("document", rs.getString(7));
				map.put("createdon", rs.getString(8));
				list.add(map);
			}			
		}catch (Exception e) {
			throw new Exception(e);			
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return list;
	}

	@Override
	public List<Object> getspecilitywisecpdcount(Long userId, String speclty) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;		
		System.out.println(speclty);
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_SPECIALTY_SUMMARY_RPT")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);
			storedProcedureQuery.setParameter("P_PACKAGECODE",speclty);
			storedProcedureQuery.setParameter("P_USERID", userId);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while(rs.next()) {
				Map<String,Object> map=new HashMap<>();
				map.put("packageid", rs.getString(1));
				map.put("packagecode", rs.getString(2));
				map.put("packagename",rs.getString(3));
				map.put("count",rs.getString(4));
				list.add(map);
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
			}catch (Exception e) {
				System.out.println(e);
			}
		}
		return list;
	}

	@Override
	public List<Object> getspecilitywisecpdlist(Long userId, String packageid) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;		
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_SPECIALTY_SUMMARY_RPT")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 2);
			storedProcedureQuery.setParameter("P_PACKAGECODE",packageid);
			storedProcedureQuery.setParameter("P_USERID", userId);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while(rs.next()) {
				Map<String,Object> map=new HashMap<>();
				map.put("packageId", rs.getString(1));
				map.put("cpdUserId", rs.getString(2));
				map.put("cpdName",rs.getString(3));
				map.put("mobileNo",rs.getString(4));
				map.put("document",rs.getString(5));
				map.put("count",rs.getString(6));
				list.add(map);
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
			}catch (Exception e) {
				System.out.println(e);
			}
		}
		return list;
	}	

}
