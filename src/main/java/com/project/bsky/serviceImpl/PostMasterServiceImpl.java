package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.InteenalCommunicationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.DcCdmoMapping;
import com.project.bsky.model.DcCdmoMappingLog;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.PostMasterModel;
import com.project.bsky.service.PostMasterService;
import com.project.bsky.util.MailUtils;


@Service
public class PostMasterServiceImpl  implements PostMasterService {
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	public Response savepostname(PostMasterModel postmastermodel) {
		Response response = new Response();

		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_POSTMASTER_SUBMIT")
					.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POST_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POST_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POST_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1l);
			storedProcedureQuery.setParameter("P_POST_ID", postmastermodel.getPostid());
			storedProcedureQuery.setParameter("P_POST_NAME", postmastermodel.getPostname());
			storedProcedureQuery.setParameter("P_POST_DESCRIPTION", postmastermodel.getPostdescription());
			storedProcedureQuery.setParameter("P_CREATEDBY", postmastermodel.getCreatedBy());
			storedProcedureQuery.setParameter("P_STATUSFLAG", postmastermodel.getBitStatus());

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
	public List<Object> getpostname() throws Exception{
		List<Object> list= new ArrayList<>();
		ResultSet rs=null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_POSTMASTER_VIEW")
					.registerStoredProcedureParameter("P_ACTIONCODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSGOUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 1);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			while (rs.next()) {
				Map<String,Object> map=new HashedMap<>();
				map.put("POST_ID", rs.getString(1));
				map.put("POST_NAME", rs.getString(2));
				map.put("POST_DESCRIPTION", rs.getString(3));
				map.put("CREATEDON", rs.getString(4));
				map.put("CREATED_BY", rs.getString(5));
				map.put("STATUSFLAG", rs.getString(6));
				map.put("STATUS", rs.getString(7));
				map.put("configurationstatus", rs.getString(8));


				list.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}
//	@Override
//	public PostMasterModel getpostnamebyid(Long userid) {
//		PostMasterModel postmaster=null;
//		try {
//			postmaster=globallinkrepo.findById(userid).get();
//		}catch(Exception e) {
//			logger.error(ExceptionUtils.getStackTrace(e));
//		}
//		return postmaster;
//	}
	@Override
	public Response updatepostname(PostMasterModel postmastermodel) {
		Response response=new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_POSTMASTER_SUBMIT")
					.registerStoredProcedureParameter("P_ACTIONCODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POST_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POST_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_POST_DESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CREATEDBY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATUSFLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTIONCODE", 2l);
			storedProcedureQuery.setParameter("P_POST_ID", postmastermodel.getPostid());
              storedProcedureQuery.setParameter("P_POST_NAME", postmastermodel.getPostname());
			storedProcedureQuery.setParameter("P_POST_DESCRIPTION", postmastermodel.getPostdescription());
			storedProcedureQuery.setParameter("P_CREATEDBY", postmastermodel.getUpdatedBy());
			storedProcedureQuery.setParameter("P_STATUSFLAG", postmastermodel.getBitStatus());
            System.out.println(postmastermodel);
			storedProcedureQuery.execute();
			Long rs = (Long) storedProcedureQuery.getOutputParameterValue("P_OUT");
			System.out.println(rs);
			if (rs==0) {
				response.setStatus("200");
				response.setMessage("Data Updated Successfully");
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
		return response;		}

}
