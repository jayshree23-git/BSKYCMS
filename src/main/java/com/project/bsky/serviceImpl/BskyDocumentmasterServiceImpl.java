/**
 * 
 */
package com.project.bsky.serviceImpl;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.DocProcedureMstdocbean;
import com.project.bsky.bean.Documentmstbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyDocumentmaster;
import com.project.bsky.model.BskyDocumentmasterLog;
import com.project.bsky.repository.BskyDocumentmasterLogRepository;
import com.project.bsky.repository.BskyDocumentmasterRepository;
import com.project.bsky.service.BskyDocumentmasterService;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 * Rajendra Prasad Sahoo
 */
@Service
public class BskyDocumentmasterServiceImpl implements BskyDocumentmasterService{
	
	@Autowired
	private BskyDocumentmasterRepository bskydocmstrepo;
	
	@Autowired
	private BskyDocumentmasterLogRepository bskydocmstlogrepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Environment env;
	
	private Connection connection = null;
	private CallableStatement statement = null;

	@Override
	public Response savedocumentmst(BskyDocumentmaster bsydocumentmaster) throws Exception {
		Response response=new Response();
		try {
			Integer count=bskydocmstrepo.checkduplicate(bsydocumentmaster.getDocumentname());
			if(count==0) {
				bsydocumentmaster.setCreatedon(new Date());
				bsydocumentmaster.setStatusflag(0);
				bskydocmstrepo.save(bsydocumentmaster);
				response.setStatus("200");
				response.setMessage("Record Added Successfully");
			}else {
				response.setStatus("400");
				response.setMessage("Document Name Already Exist");
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<Object> getdocumentmst() throws Exception {
		List<Object> list=new ArrayList<>();
		try {
			List<Object[]> objarr=bskydocmstrepo.getdocumentmst();
			for(Object[] obj:objarr) {
				Map<String,Object> response = new HashMap<>();
				response.put("documentId", obj[0]);
				response.put("documentname", obj[1]);
				response.put("createbyname", obj[2]);
				response.put("createtime", obj[3]);
				response.put("statusflag", obj[4]);
				list.add(response);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}
	
	@Override
	public List<Object> getdocumentmstname(String procedurecode ) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs = null;
		try {						
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_document_tagging_list")
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action_code", 2);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", procedurecode);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (rs.next()) {
				Map<String,Object> map1=new HashedMap<>();
				map1.put("docid",rs.getString(1));
				map1.put("docname",rs.getString(2));
				map1.put("status",rs.getString(3));
				map1.put("tagstatus",rs.getString(3));
				map1.put("prestatus",rs.getString(4));
				map1.put("claimstatus",rs.getString(5));
				list.add(map1);
			}		

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
			}
		}
		return list;
	}

	@Override
	public Response updatedocumentmst(BskyDocumentmaster bsydocumentmaster) throws Exception {
		Response response=new Response();
		try {
			BskyDocumentmaster bsydocumentmaster1=bskydocmstrepo.getduplicate(bsydocumentmaster.getDocumentname());
			if(bsydocumentmaster1==null) {
				bsydocumentmaster1=bskydocmstrepo.findById(bsydocumentmaster.getDocumentId()).get();
					//log
					BskyDocumentmasterLog log=new BskyDocumentmasterLog();
					log.setDocumentId(bsydocumentmaster1.getDocumentId());
					log.setDocumentname(bsydocumentmaster1.getDocumentname());
					log.setCreatedby(bsydocumentmaster1.getCreatedby());
					log.setCreatedon(bsydocumentmaster1.getCreatedon());
					log.setUpdatedby(bsydocumentmaster1.getUpdatedby());
					log.setUpdatedon(bsydocumentmaster1.getUpdatedon());
					log.setStatusflag(bsydocumentmaster1.getStatusflag());
					log.setLogcreatedby(bsydocumentmaster.getCreatedby());
					log.setLogcreatedon(new Date());
					bskydocmstlogrepo.save(log);					
					//main
				bsydocumentmaster1.setDocumentname(bsydocumentmaster.getDocumentname());
				bsydocumentmaster1.setStatusflag(bsydocumentmaster.getStatusflag());
				bsydocumentmaster1.setUpdatedby(bsydocumentmaster.getCreatedby());
				bsydocumentmaster1.setUpdatedon(new Date());
				bskydocmstrepo.save(bsydocumentmaster1);
				response.setStatus("200");
				response.setMessage("Record Updated Successfully");
			}else {
				if(bsydocumentmaster1.getDocumentId().equals(bsydocumentmaster.getDocumentId())) {
					//log
					BskyDocumentmasterLog log=new BskyDocumentmasterLog();
					log.setDocumentId(bsydocumentmaster1.getDocumentId());
					log.setDocumentname(bsydocumentmaster1.getDocumentname());
					log.setCreatedby(bsydocumentmaster1.getCreatedby());
					log.setCreatedon(bsydocumentmaster1.getCreatedon());
					log.setUpdatedby(bsydocumentmaster1.getUpdatedby());
					log.setUpdatedon(bsydocumentmaster1.getUpdatedon());
					log.setStatusflag(bsydocumentmaster1.getStatusflag());
					log.setLogcreatedby(bsydocumentmaster.getCreatedby());
					log.setLogcreatedon(new Date());
					bskydocmstlogrepo.save(log);					
					//main
					bsydocumentmaster1.setDocumentname(bsydocumentmaster.getDocumentname());
					bsydocumentmaster1.setStatusflag(bsydocumentmaster.getStatusflag());
					bsydocumentmaster1.setUpdatedby(bsydocumentmaster.getCreatedby());
					bsydocumentmaster1.setUpdatedon(new Date());
					bskydocmstrepo.save(bsydocumentmaster1);
					response.setStatus("200");
					response.setMessage("Record Updated Successfully");
				}else {
					response.setStatus("400");
					response.setMessage("Document Name Already Exist");
				}
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<Object> getprocdurethroughheadercode(String headerCode) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_document_tagging_list")
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action_code", 1);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", headerCode);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (rs.next()) {
				Map<String,Object> map=new HashedMap<>();
				map.put("headerId",rs.getString(1));
				map.put("packageHeaderCode",rs.getString(2));
				map.put("packageHeaderName",rs.getString(3));
				map.put("procedureCode",rs.getString(4));
				map.put("procedureDescription",rs.getString(5));
				map.put("procedureId",rs.getString(6));
				map.put("subPackageCode",rs.getString(7));
				map.put("subPackageName",rs.getString(8));
				map.put("subPackageId",rs.getString(9));
				list.add(map);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
			}
		}
		return list;
	}

	@Override
	public Response savedocproceduremapping(DocProcedureMstdocbean bsydocumentmapping) throws Exception {
		Response response=new Response();
		try {
			
			List<Documentmstbean> list=new ArrayList<Documentmstbean>();
			for(Documentmstbean select:bsydocumentmapping.getSelectitemlist()) {
				Documentmstbean bean=new Documentmstbean();
				bean.setDocid(select.getDocid());
				bean.setPreauth(1);
				bean.setStatus(select.getStatus()==1?1:select.getPreauth());
				list.add(bean);
				Documentmstbean bean1=new Documentmstbean();
				bean1.setDocid(select.getDocid());
				bean1.setPreauth(2);
				bean1.setStatus(select.getStatus()==1?1:select.getClaim());
				list.add(bean1);
			}			
			
			StructDescriptor structDescriptor;	        
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);	
			
			
			STRUCT[] quesbean = new STRUCT[list.size()];
			int i=0;
			for(Documentmstbean bean:list){
				structDescriptor = new StructDescriptor("T_TYPE_MDR_DOC_MAPPING", connection);
				 Object[] objArr = {
						 bsydocumentmapping.getHeaderCode(),
						 bsydocumentmapping.getProcedureCode(),
						 bean.getDocid(),
						 bean.getPreauth(),
						 bean.getStatus()
		            };
				 quesbean[i] = new STRUCT(structDescriptor, connection, objArr);
		            i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_MDR_DOC_MAPPING", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, quesbean);
			
			statement = connection.prepareCall("call USP_MDR_DOC_MAPPING(?,?,?)");
			statement.setLong(1, bsydocumentmapping.getCreatedby());//P_USERID
			statement.setArray(2, array_to_pass);//P_MDR_DOC_MAPPING
			statement.registerOutParameter(3, Types.INTEGER);//P_MSG_OUT
			statement.execute();
			Integer out = statement.getInt(3);
			if(out==1) {
				response.setStatus("200");
				response.setMessage("Document Tagged Successfully");
			}else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(connection!=null) {
				connection.close();
			}
		}
		return response;
	}

	@Override
	public List<Object> getdocproctaggedlist(String headerCode) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_document_tagging_list")
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action_code", 6);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", headerCode);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (rs.next()) {
				Map<String,Object> map=new HashedMap<>();
				map.put("packageHeaderCode",rs.getString(1));
				map.put("packageHeaderName",rs.getString(2)+" ("+rs.getString(1)+")");
				map.put("subpackageCode",rs.getString(3));
				map.put("subpackageName",rs.getString(4)+" ("+rs.getString(3)+")");
				map.put("procedureCode",rs.getString(5));
				map.put("procedureDescription",rs.getString(6));
				String[] docname;
				if (rs.getString(7) == null) {
		            docname=new String[] {}; 
		        }else {
		        	docname=rs.getString(7).split("\\$#");
		        }
				map.put("preauth",docname);
				if (rs.getString(8) == null) {
		            docname=new String[] {}; 
		        }else {
		        	docname=rs.getString(8).split("\\$#");
		        }
				map.put("claim",docname);
				list.add(map);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
			}
		}
		return list;
	}
	
	
	
	public List<Object> gettaggeddocumentbyprocedure(String procedureCode,Integer doctype) throws Exception{
		List<Object> doclist=new ArrayList<>();
		try {
			List<Object[]> objarr = bskydocmstrepo.gettaggeddocumentbyprocedure(procedureCode,doctype);
			for(Object[] obj:objarr) {
				Map<String,Object> map=new HashMap<>();
				map.put("docId", obj[0]);
				map.put("docName", obj[1]);
				doclist.add(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}	
		return doclist;
	}
	
	@Override
	public List<Object> getdocproctaggedlistforexcel(String headerCode) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_document_tagging_list")
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action_code", 3);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", headerCode);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (rs.next()) {
				Map<String,Object> map=new HashedMap<>();
				map.put("headerId",rs.getString(1));
				map.put("packageHeaderCode",rs.getString(2));
				map.put("packageHeaderName",rs.getString(3) +" ("+rs.getString(2)+")");
				map.put("procedureCode",rs.getString(4));
				map.put("procedureDescription",rs.getString(5));
				map.put("procedureId",rs.getString(6));
				map.put("subPackageCode",rs.getString(7));
				map.put("subPackageName",rs.getString(8)+" ("+rs.getString(7)+")");
				map.put("subPackageId",rs.getString(9));
				map.put("preauthDoc",rs.getString(10));
				map.put("claimDoc",rs.getString(11));
				list.add(map);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
			}
		}
		return list;
	}

	@Override
	public List<Object> getproceduretagggeddoclist(String procedureCode) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_get_document_tagging_list")
					.registerStoredProcedureParameter("p_action_code", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg_out", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_action_code", 4);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", procedureCode);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_msg_out");

			while (rs.next()) {
				Map<String,Object> map=new HashedMap<>();
				map.put("docid",rs.getString(1));
				map.put("docname",rs.getString(2));
				map.put("preauth",rs.getInt(3)==0?"Requird":"--");
				map.put("claim",rs.getInt(4)==0?"Requird":"--");
				list.add(map);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
			}
		}
		return list;
	}

	

}
