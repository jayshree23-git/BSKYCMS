package com.project.bsky.serviceImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.OnlinePostConfigBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.OnlinePostConfigModel;
import com.project.bsky.model.PostMasterModel;
import com.project.bsky.repository.OnlinePostConfigRepo;
import com.project.bsky.repository.PostMasterRepository;
import com.project.bsky.service.OnlinePostConfigService;
import com.project.bsky.util.CommonFileUpload;


@Service
public class OnlinePostConfigServiceImpl implements OnlinePostConfigService {
	
	@Autowired
	private OnlinePostConfigRepo onlinepostconfigrepo;
	
	@Autowired
	private PostMasterRepository postmasterrepo;
	
	@Autowired
	private Logger logger;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private static ResourceBundle bskyResourcesBundel4 = ResourceBundle.getBundle("fileConfiguration");

	@Override
	public List<PostMasterModel> getpostnamebyid() throws Exception {
		List<PostMasterModel> list=new ArrayList<>();
		try {
			list=postmasterrepo.findallactivedata();
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public Response saveonlinepostconfig(OnlinePostConfigBean onlinepostconfigbean) {
		Response response = new Response();
			String docname="";
			SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
		try {
			docname=CommonFileUpload.uploadonlinepostDoc(onlinepostconfigbean.getFilename(),onlinepostconfigbean.getPostid());
			System.out.println(docname);
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ONLINEPOST_CONFIGURATION_SUBMIT")
					.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CONFIGURATIONID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POST_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CURRENT_JOB_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ONLINE_APPLY_FROM", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ONLINE_APPLY_TO", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCUMENT_UPLOAD", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADVERTISEMENT_NUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDBY", Long.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_ADVERTISEMENT_DATE", Date.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("P_NO_OF_VACANCY", Long.class, ParameterMode.IN) 
                    .registerStoredProcedureParameter("P_ONLINE_PUBLISH", Long.class, ParameterMode.IN) 
                    .registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1l);
			storedProcedureQuery.setParameter("P_CONFIGURATIONID",null);
			storedProcedureQuery.setParameter("P_POST_ID", onlinepostconfigbean.getPostid());
			storedProcedureQuery.setParameter("P_CURRENT_JOB_DESCRIPTION", onlinepostconfigbean.getCurrentjobdescription());
			storedProcedureQuery.setParameter("P_ONLINE_APPLY_FROM", sdf.parse(onlinepostconfigbean.getOnlineapplyfrom()));
			storedProcedureQuery.setParameter("P_ONLINE_APPLY_TO", sdf.parse(onlinepostconfigbean.getOnlineapplyto()));
			storedProcedureQuery.setParameter("P_DOCUMENT_UPLOAD", docname);
			storedProcedureQuery.setParameter("P_ADVERTISEMENT_NUMBER", onlinepostconfigbean.getAdvertisementnumb());
			storedProcedureQuery.setParameter("P_CREATEDBY", onlinepostconfigbean.getCreatedBy());
			storedProcedureQuery.setParameter("P_STATUSFLAG", onlinepostconfigbean.getBitStatus());
			storedProcedureQuery.setParameter("P_ADVERTISEMENT_DATE", sdf.parse(onlinepostconfigbean.getAdvertisementdate()));
			storedProcedureQuery.setParameter("P_NO_OF_VACANCY", onlinepostconfigbean.getNoofvaccancy());
			storedProcedureQuery.setParameter("P_ONLINE_PUBLISH", onlinepostconfigbean.getOnlinepublish());

			storedProcedureQuery.execute();
			Long rs = (Long) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (rs==0) {
				response.setStatus("200");
				response.setMessage("Data Saved Successfully");
			}
			else if(rs ==1) {
				response.setStatus("401");
				response.setMessage("Data Already Exist");
			}
			else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong");
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		return response;
	}

	@Override
	public void downLoadonlinepostDoc(String fileName,HttpServletResponse response) {
		try {
			CommonFileUpload.downLoadonlinepostDoc(fileName, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public List<Object> getonlinepostconfiglist() throws Exception {
		List<Object> list= new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ONLINEPOST_CONFIGURATION_VIEW")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String,Object> map=new HashedMap<>();
				map.put("POSTID", rs.getString(1));
				map.put("CONFIGURATIONID", rs.getString(2));
				map.put("POSTNAME", rs.getString(3));
				map.put("POSTDESCRIPTION", rs.getString(4));
				map.put("CURRENTJOBDESCRIPTION", rs.getString(5));
				map.put("ONLINEAPPLYFROM", rs.getString(6));
				map.put("ONLINEAPPLYTO", rs.getString(7));
				map.put("DOCUMENTUPLOAD", rs.getString(8));
				map.put("ADVERTISEMENTNUMBER", rs.getString(9));
				map.put("CREATEDON", rs.getString(10));
				map.put("CREATEDBY", rs.getString(11));	
				map.put("STATUSFLAG", rs.getString(12));
				map.put("ADVERTISEMENTDATE", rs.getString(13));
				map.put("NOOFVACANCY", rs.getString(14));
				map.put("TOTALAPPLIED", rs.getString(15));
				map.put("DRAFT", rs.getString(16));
				map.put("COMPLET", rs.getString(17));
				map.put("ONLINEPUBLISHSTATUS", rs.getString(18));
				map.put("Configurationstatus", rs.getString(19));






				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public Response updateonlinepostconfig(OnlinePostConfigBean onlinepostconfigbean) {
		Response response = new Response();
		String docname="";
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
	try {
		if(onlinepostconfigbean.getFilename()!=null) {
			docname=CommonFileUpload.uploadonlinepostDoc(onlinepostconfigbean.getFilename(),onlinepostconfigbean.getPostid());
		}else {
			docname=onlinepostconfigbean.getFilename1();
		}
		
		StoredProcedureQuery storedProcedureQuery = this.entityManager
				.createStoredProcedureQuery("USP_ONLINEPOST_CONFIGURATION_SUBMIT")
				.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_CONFIGURATIONID", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_POST_ID", Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_CURRENT_JOB_DESCRIPTION", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_ONLINE_APPLY_FROM", Date.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_ONLINE_APPLY_TO", Date.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_DOCUMENT_UPLOAD", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_ADVERTISEMENT_NUMBER", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("P_CREATEDBY", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_ADVERTISEMENT_DATE", Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("P_NO_OF_VACANCY", Long.class, ParameterMode.IN) 
                .registerStoredProcedureParameter("P_ONLINE_PUBLISH", Long.class, ParameterMode.IN) 
                .registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT);

		storedProcedureQuery.setParameter("P_ACTIONCODE", 2l);
		storedProcedureQuery.setParameter("P_CONFIGURATIONID",onlinepostconfigbean.getConfigid());
		storedProcedureQuery.setParameter("P_POST_ID", onlinepostconfigbean.getPostid());
		storedProcedureQuery.setParameter("P_CURRENT_JOB_DESCRIPTION", onlinepostconfigbean.getCurrentjobdescription());
		storedProcedureQuery.setParameter("P_ONLINE_APPLY_FROM", sdf.parse(onlinepostconfigbean.getOnlineapplyfrom()));
		storedProcedureQuery.setParameter("P_ONLINE_APPLY_TO", sdf.parse(onlinepostconfigbean.getOnlineapplyto()));
		storedProcedureQuery.setParameter("P_DOCUMENT_UPLOAD", docname);
		storedProcedureQuery.setParameter("P_ADVERTISEMENT_NUMBER", onlinepostconfigbean.getAdvertisementnumb());
		storedProcedureQuery.setParameter("P_CREATEDBY", onlinepostconfigbean.getCreatedBy());
		storedProcedureQuery.setParameter("P_STATUSFLAG", onlinepostconfigbean.getBitStatus());
		storedProcedureQuery.setParameter("P_ADVERTISEMENT_DATE", sdf.parse(onlinepostconfigbean.getAdvertisementdate()));
		storedProcedureQuery.setParameter("P_NO_OF_VACANCY", onlinepostconfigbean.getNoofvaccancy());
		storedProcedureQuery.setParameter("P_ONLINE_PUBLISH", onlinepostconfigbean.getOnlinepublish());

		storedProcedureQuery.execute();
		Long rs = (Long) storedProcedureQuery.getOutputParameterValue("P_OUT");
		if (rs==0) {
			response.setStatus("200");
			response.setMessage("Data Saved Successfully");
		}
		else if(rs ==1) {
			response.setStatus("401");
			response.setMessage("Data Already Exist");
		}
		else {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}

	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
	
	return response;
	}

	@Override
	public OnlinePostConfigModel getbyId(Long configid) {
		OnlinePostConfigModel onlinepostconfigmodel=new OnlinePostConfigModel();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
		try {
			onlinepostconfigmodel=onlinepostconfigrepo.findById(configid).get();
			if(onlinepostconfigmodel!=null) {
				onlinepostconfigmodel.setAdvertise(sdf.format(onlinepostconfigmodel.getAdvertisementdate()));
				onlinepostconfigmodel.setApplyform(sdf.format(onlinepostconfigmodel.getOnlineapplyfrom()));
				onlinepostconfigmodel.setApplyto(sdf.format(onlinepostconfigmodel.getOnlineapplyto()));
			}
			
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return onlinepostconfigmodel;}
}
