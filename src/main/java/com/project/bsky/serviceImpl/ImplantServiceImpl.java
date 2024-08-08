package com.project.bsky.serviceImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Surveygroupmapping;
import com.project.bsky.bean.mappingsurveygroupbean;
import com.project.bsky.model.Implant;
import com.project.bsky.repository.ImplantRepository;
import com.project.bsky.service.ImplantService;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Service
public class ImplantServiceImpl implements ImplantService {

	@Autowired
	private ImplantRepository implantRepository;

	@Autowired
	private Logger logger;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Connection connection = null;
	private CallableStatement statement = null;
	
	@Autowired
	private Environment env;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response saveImplant(Implant implant) {
		Response response = new Response();
		try {
			Integer count = implantRepository.countRowForCheckDuplicateImplantCode(implant.getImplantCode());
			if (count == 0) {
				implant.setCreatedBy(implant.getCreatedBy());
				implant.setUpdatedBy(-1);
				implant.setCreatedOn(date);
				implant.setUpdatedOn(date);
				implant.setDeletedFlag(0);
				implantRepository.save(implant);
				response.setMessage("Implant Master Added");
				response.setStatus("Success");

			} else {
				response.setStatus("Failed");
				response.setMessage("Implant Code Already Exist");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<Implant> getImplant() {
		List<Implant> implantrResponse = new ArrayList<>();
		List<Implant> findAll = implantRepository.findAll(Sort.by(Sort.Direction.ASC, "implantName"));

		if (findAll != null) {
			for (Implant implant : findAll) {
				if (implant != null && implant.getDeletedFlag() == 0) {
					implantrResponse.add(implant);
				}
			}
		}
		return implantrResponse;
	}

	@Override
	public Response deleteImplant(Long implantId) {
		Response response = new Response();
		try {
			Implant implant = implantRepository.findById(implantId).get();
			implant.setDeletedFlag(1);
			implantRepository.save(implant);
			response.setMessage("Record Successfully In-Active");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Implant getbyimplantId(Long implantId) {
		Implant implant = null;
		try {
			implant = implantRepository.findById(implantId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return implant;
	}

	@Override
	public Response update(Long implantId, Implant implant) {
		Response response = new Response();
		Implant implantResp = null;
		try {
			Integer count = implantRepository.countRowForCheckDuplicateImplantCode(implant.getImplantCode());
			if (count == 0) {
				Implant implantResponse = implantRepository.findById(implantId).get();
				implant.setImplantId(implantId);
				implant.setUpdatedOn(date);
				implant.setCreatedOn(implantResponse.getCreatedOn());
				implant.setCreatedBy(implantResponse.getCreatedBy());
				implant.setUpdatedBy(implant.getUpdatedBy());
				implant.setDeletedFlag(implantResponse.getDeletedFlag());
				implantRepository.save(implant);
				response.setMessage("Implant Updated");
				response.setStatus("Success");

			} else {
				implantResp = implantRepository.findByImplantCode(implant.getImplantCode());
				if (implantResp.getImplantId().equals(implantId)) {
					implant.setImplantId(implantId);
					implant.setUpdatedOn(date);
					implant.setCreatedOn(implantResp.getCreatedOn());
					implant.setCreatedBy(implantResp.getCreatedBy());
					implant.setUpdatedBy(implant.getUpdatedBy());
					implant.setDeletedFlag(implantResp.getDeletedFlag());
					implantRepository.save(implant);
					response.setMessage("Implant Updated");
					response.setStatus("Success");
				}

				else {
					response.setMessage("Implant  Code Already Exist");
					response.setStatus("Failed");
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<Object> getpackageicddetails(String procedurecode) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			Map<String,Object> data=null;			
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_MAPPED_IMPLANT_PACKAGE")
					.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION", 1);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", procedurecode);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while(rs.next()) {
				data=new HashMap<>();
				data.put("implantcode", rs.getString(1));
				data.put("implantname", rs.getString(2));
				data.put("maximumunit", rs.getString(3));
				data.put("unitprice", rs.getString(4));
				data.put("priceeditble", rs.getString(5));
				data.put("uniteditble", rs.getString(6));
				data.put("status",rs.getString(7));
				data.put("mappingid",rs.getString(8));
				list.add(data);
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
			}
		}
		return list;
	}
	
	@Override
	public List<Object> implantproceduremappeddata(String procedurecode, String packageheadercode) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {
			Map<String,Object> data=null;			
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_MAPPED_IMPLANT_PACKAGE")
					.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION", 2);
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", packageheadercode);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", procedurecode);
			
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while(rs.next()) {
				data=new HashMap<>();
				data.put("speciality", rs.getString(1));
				data.put("package", rs.getString(2));
				data.put("implant", rs.getString(3));
				data.put("maximumunit", rs.getString(4));
				data.put("unitprice", rs.getString(5));
				data.put("priceeditble", rs.getString(6));
				data.put("uniteditble", rs.getString(7));
				list.add(data);
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}finally {
			if(rs!=null) {
				rs.close();
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
	public Map<String, Object> getpackageicddetails(Surveygroupmapping implantconfigdata) throws Exception {
		Map<String,Object> data=new HashMap<>();
		try {
			System.out.println(implantconfigdata);
			StructDescriptor structDescriptor;
	        
	        getconnection();		
			
			STRUCT[] quesbean = new STRUCT[implantconfigdata.getSelectlist().size()];
			int i=0;
			for(mappingsurveygroupbean bean:implantconfigdata.getSelectlist()){
				structDescriptor = new StructDescriptor("TYPE_PACKAGE_IMPLANT_T", connection);
				 Object[] ObjArr = {
						 bean.getImplantcode(),
						 implantconfigdata.getProcedure(),
						 bean.getId(),
						 bean.getStatus()
		            };
				 quesbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
		            i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_PACKAGE_IMPLANT", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, quesbean);
			
			statement = connection.prepareCall("call USP_T_IMPLANT_PACKAGE_MAPPING(?,?,?,?)");
			statement.setInt(1, 1);//P_ACTION_CODE
			statement.setLong(2, implantconfigdata.getCreatedby());//P_USERID
			statement.setArray(3, array_to_pass);//P_TYPE_PACKAGE_IMPLANT
			statement.registerOutParameter(4, Types.INTEGER);//P_MSG_OUT
			statement.execute();
			Integer out = statement.getInt(4);
			if(out==1) {
				data.put("status",200);
				data.put("message","Form Submitted Successfully");
			}else {
				data.put("status",400);
				data.put("message","Something Went Wrong");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			if(connection!=null) {
				connection.close();
			}
		}
		return data;
	}	
}
