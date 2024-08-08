package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.WhatsAppUserConfogurationBean;
import com.project.bsky.model.DcCdmoMapping;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.WhatsAppTemplateModel;
import com.project.bsky.model.WhatsAppUserConfigurationModel;
import com.project.bsky.repository.PrimaryLinkRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.repository.WhatsAppTemplateModelRepository;
import com.project.bsky.repository.WhatsAppUserConfigurationRepository;
import com.project.bsky.service.WhatsAppUserConfigurationService;

@Service
public class WhatsAppUserConfigurationServiceImpl implements WhatsAppUserConfigurationService{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private WhatsAppUserConfigurationRepository whatsappuserconfigurationrepository;

	@Autowired
	private WhatsAppTemplateModelRepository whatsapptempletmodelrepo;

	@Autowired
	private UserDetailsRepository userdetailsrepo;
	
	@Autowired
	private Logger logger;
	@Override
	public List<WhatsAppTemplateModel> getwhatsapptemplatename() {
		List<WhatsAppTemplateModel> list=new ArrayList<>();
		try {
			list=whatsapptempletmodelrepo.alldata();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<Object> getUserNamebyGroupId(String groupid) {
		List<Object> districtlist = new ArrayList();
		Map<String,Object> map=null;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("SP_WHATSAPP_USER_CONFIGURATION")
					.registerStoredProcedureParameter("p_groupid", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_groupid", groupid);
			storedProcedureQuery.setParameter("p_flag", "USER");
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				map = new HashMap<>();
				map.put("FULL_NAME", rs.getString(1));
				map.put("GROUPID", rs.getString(2));
				map.put("USERID", rs.getString(3));
				map.put("USERNAME", rs.getString(4));
				districtlist.add(map);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return districtlist;
	}


	@Override
	public Response savewhatappuserconfig(WhatsAppUserConfogurationBean whatsapptemplatebean) throws Exception {
		Response response =new Response();
		try {
			
			String[] id=null;
			if(whatsapptemplatebean.getUserid()!=null) {
				id=whatsapptemplatebean.getUserid().split(",");
				List<WhatsAppUserConfigurationModel> list=new ArrayList<>();
				for(String s:id) {
					Integer check=whatsappuserconfigurationrepository.getstatus(whatsapptemplatebean.getTemplateId(),Long.parseLong(s));
					if(check==null) {
						UserDetails user= userdetailsrepo.findById(Long.parseLong(s)).get();
						WhatsAppTemplateModel whatsapptemp=whatsapptempletmodelrepo.findById(whatsapptemplatebean.getTemplateId()).get();
						WhatsAppUserConfigurationModel whatsapp=new WhatsAppUserConfigurationModel();
						whatsapp.setTemplateid(whatsapptemplatebean.getTemplateId());
						whatsapp.setWhatsapptemplatename(whatsapptemp.getTemplateName());
						whatsapp.setGroupid(user.getGroupId().getTypeId());
						whatsapp.setUserid(Long.parseLong(s));
						whatsapp.setCreatedby(whatsapptemplatebean.getCreatedby());
						whatsapp.setCreatedon(new Date());
						whatsapp.setStatusflag(0);
						list.add(whatsapp);
					}else if(check==1) {
						WhatsAppUserConfigurationModel whatsapp=whatsappuserconfigurationrepository.getrecord(whatsapptemplatebean.getTemplateId(),Long.parseLong(s));
						whatsapp.setUpdatedby(whatsapptemplatebean.getCreatedby());
						whatsapp.setUpdatedon(new Date());
						whatsapp.setStatusflag(0);
					}else {
						response.setStatus("401");
						response.setMessage("User Already Configured");
						return response;
					}
				}
				whatsappuserconfigurationrepository.saveAll(list);
				response.setStatus("200");
				response.setMessage("User Configured Successfully");
			}else {
				response.setStatus("400");
				response.setMessage("Please Select Atleast One User");
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}


	@Override
	public List<Object> getwhatsappconfigviewlist() throws Exception {
		List<Object> list=new ArrayList<>();
		try {
			List<Object[]> objarr=whatsappuserconfigurationrepository.getwhatsappconfigviewlist();
			for(Object[] obj:objarr) {
				Map<String,Object> response = new HashMap<>();
				response.put("configid", obj[0]);
				response.put("templetid", obj[1]);
				response.put("templetname", obj[2]);
				response.put("groupid", obj[3]);
				response.put("groupname", obj[4]);
				response.put("userid", obj[5]);
				response.put("userfullname", obj[6]);
				response.put("createdby", obj[7]);
				response.put("createdbyname", obj[8]);
				response.put("createdon", obj[9]);
				response.put("statusflag", obj[10]);
				response.put("odishatempid", obj[11]);

				list.add(response);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}


	@Override
	public Response inactiveonwhatsappconfig(Long configid, Integer status,Long updatedby) throws Exception {
		Response response=new Response();
		try {
			WhatsAppUserConfigurationModel whatsapp=whatsappuserconfigurationrepository.findById(configid).get();
			whatsapp.setUpdatedby(updatedby);
			whatsapp.setUpdatedon(new Date());
			whatsapp.setStatusflag(status);
			whatsappuserconfigurationrepository.save(whatsapp);
			response.setStatus("200");
			if(status==0) {
				response.setMessage("Record Active Successfully");
			}else {
				response.setMessage("Record In-Active Successfully");
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}




	

}
