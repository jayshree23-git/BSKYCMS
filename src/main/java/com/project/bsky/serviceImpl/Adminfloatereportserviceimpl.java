/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.stereotype.Service;

import com.project.bsky.bean.BeneficiaryDischargedataBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UrnWiseActionBean;
import com.project.bsky.bean.UrnWiseDetailsBean;
import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.repository.TxnclaimFloatdetailsrepository;
import com.project.bsky.service.Adminfloatereportservice;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class Adminfloatereportserviceimpl implements Adminfloatereportservice {

	@Autowired
	private TxnclaimFloatdetailsrepository txnclaimreportrepo;

	@Autowired
	private Logger logger;

	@Autowired
	private Environment env;

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

	@PersistenceContext
	private EntityManager entityManager;

	private Connection con = null;
	private CallableStatement st = null;

	@Override
	public List<Object> snaFloatDataforrevert(Date formdate, Date todate, Long userid, String floateno)
			throws Exception {
		List<Object> floatedetailslist = new ArrayList<Object>();
		System.out.println(formdate + " " + todate + " " + userid + floateno);
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_FLAOT_REVERSAL_TO_PREVIOUS_STAGE")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_FROMDATE", formdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_FLOAT_NO", floateno);
			storedProcedureQuery.setParameter("P_ACTION", 1);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (rs.next()) {
				UrnWiseDetailsBean bean = new UrnWiseDetailsBean();
				bean.setClaimNo(rs.getString(2));
				bean.setCaseno(rs.getString(3));
				bean.setHospitalCode(rs.getString(4));
				bean.setHospitalName(rs.getString(5) + " (" + rs.getString(4) + ")");
				bean.setActualDischarge(rs.getString(6));
				bean.setActualDateAdm(rs.getString(7));
				bean.setCPDAppAmount(rs.getString(8));
				bean.setCpdActionDate(rs.getString(9));
				bean.setSNAApprovemount(rs.getString(10));
				bean.setRemark(rs.getString(11));
				floatedetailslist.add(bean);
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e.getMessage());
			}
		}
		return floatedetailslist;
	}

	@Override
	public Response snaFloatrevert(Date formdate, Date todate, Long userid, String floateno) throws Exception {
		Response response = new Response();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_FLAOT_REVERSAL_TO_PREVIOUS_STAGE")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_FROMDATE", formdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_FLOAT_NO", floateno);
			storedProcedureQuery.setParameter("P_ACTION", 2);

			storedProcedureQuery.execute();
			String status = (String) storedProcedureQuery.getOutputParameterValue("P_MSG");
			if (status.equals("Sucess")) {
				response.setStatus("200");
				response.setMessage("Reverted Successfully");
			} else {
				response.setStatus("400");
				response.setMessage("Some Thing Went Wrong");
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public List<Object> snaFloatrevertData(Date formdate, Date todate, Long userid) throws Exception {
		List<Object> floatedetailslist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_FLAOT_REVERSAL_TO_PREVIOUS_STAGE")
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_FROMDATE", formdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_ACTION", 3);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (rs.next()) {
				TxnclamFloateDetails bean = new TxnclamFloateDetails();
				bean.setFloateno(rs.getString(1));
				bean.setAmount(rs.getDouble(2));
//				bean.setCreateby(rs.getString(3));
				bean.setScreateon(rs.getString(4));
				bean.setClaimcount(rs.getString(5));
				floatedetailslist.add(bean);
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e.getMessage());
			}
		}
		return floatedetailslist;
	}

	@Override
	public List<Object> getsnafreezelist(Date formdate, Date todate, Long userid, String state, String dist, String hospital) throws Exception {
		List<Object> floatedetailslist = new ArrayList<Object>();
		ResultSet rs = null;
		try {

			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");

			String[] stringArray = new String[0];
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("CLAIMIDLIST", con);
			ARRAY array_to_pass = new ARRAY(des, con, stringArray);

			st = con.prepareCall("call USP_SNA_UNFREEZE_FLOAT(?,?,?,?,?,?,?,?,?,?)");
			st.setDate(1, new java.sql.Date(formdate.getTime()));
			st.setDate(2, new java.sql.Date(todate.getTime()));
			st.setLong(3, (Long) userid);
			st.setInt(4, (Integer) 1);
			st.setArray(5, array_to_pass);
			st.setString(6, state);
			st.setString(7, dist);
			st.setString(8, hospital);
			st.registerOutParameter(9, Types.INTEGER);
			st.registerOutParameter(10, Types.REF_CURSOR);
			st.execute();
			rs = ((OracleCallableStatement) st).getCursor(10);
			while (rs.next()) {
				UrnWiseDetailsBean bean = new UrnWiseDetailsBean();
				bean.setClaimid(rs.getString(1));
				bean.setClaimNo(rs.getString(2));
				bean.setCaseno(rs.getString(3));
				bean.setHospitalCode(rs.getString(4));
				bean.setHospitalName(rs.getString(5) + " (" + rs.getString(4) + ")");
				bean.setActualDischarge(rs.getString(6));
				bean.setActualDateAdm(rs.getString(7));
				bean.setCPDAppAmount(rs.getString(8));
				bean.setSNAApprovemount(rs.getString(9));
				bean.setRemark(rs.getString(10));
				bean.setUrn(rs.getString(11));
				floatedetailslist.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e.getMessage());
			}
		}
		return floatedetailslist;
	}

	@Override
	public Response applyforunfreeze(Date formdate, Date todate, Long userid, String claimid) throws Exception {
		Response response = new Response();
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");

			String[] stringArray = claimid.split(",");
			System.out.println(Arrays.toString(stringArray));
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("CLAIMIDLIST", con);
			ARRAY array_to_pass = new ARRAY(des, con, stringArray);

			st = con.prepareCall("call USP_SNA_UNFREEZE_FLOAT(?,?,?,?,?,?,?,?,?,?)");
			st.setDate(1, new java.sql.Date(formdate.getTime()));
			st.setDate(2, new java.sql.Date(todate.getTime()));
			st.setLong(3, (Long) userid);
			st.setInt(4, (Integer) 2);
			st.setArray(5, array_to_pass);
			st.setString(6, null);
			st.setString(7, null);
			st.setString(8, null);
			st.registerOutParameter(9, Types.INTEGER);
			st.registerOutParameter(10, Types.REF_CURSOR);
			st.execute();
			Integer out = st.getInt(9);

			if (out == 1) {
				response.setStatus("200");
				response.setMessage("Record Updated Sucessfully");
			} else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong Db Side");
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public List<Object> getsnafreezelistforapprove(Date fromdate, Date todate, Long userid, String state, String dist, String hospital) throws Exception {
		List<Object> floatedetailslist = new ArrayList<Object>();
		ResultSet rs = null;
		try {

			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");

			String[] stringArray = new String[0];
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("CLAIMIDLIST", con);
			ARRAY array_to_pass = new ARRAY(des, con, stringArray);

			st = con.prepareCall("call USP_SNA_UNFREEZE_FLOAT(?,?,?,?,?,?,?,?,?,?)");
			st.setDate(1, new java.sql.Date(fromdate.getTime()));
			st.setDate(2, new java.sql.Date(todate.getTime()));
			st.setLong(3, userid);
			st.setInt(4, (Integer) 3);
			st.setArray(5, array_to_pass);
			st.setString(6, state);
			st.setString(7, dist);
			st.setString(8, hospital);
			st.registerOutParameter(9, Types.INTEGER);
			st.registerOutParameter(10, Types.REF_CURSOR);
			st.execute();
			rs = ((OracleCallableStatement) st).getCursor(10);

			while (rs.next()) {
				UrnWiseDetailsBean bean = new UrnWiseDetailsBean();
				bean.setClaimid(rs.getString(1));
				bean.setClaimNo(rs.getString(2));
				bean.setCaseno(rs.getString(3));
				bean.setHospitalCode(rs.getString(4));
				bean.setHospitalName(rs.getString(5) + " (" + rs.getString(4) + ")");
				bean.setActualDischarge(rs.getString(6));
				bean.setActualDateAdm(rs.getString(7));
				bean.setActionBy(rs.getString(8));
				bean.setActionOn(rs.getString(9));
				bean.setRemark(rs.getString(10));
				bean.setUrn(rs.getString(11));
				floatedetailslist.add(bean);
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				throw new Exception(e.getMessage());
			}
		}
		return floatedetailslist;
	}

	@Override
	public Response approveforunfreeze(Long userid, String claimid) throws Exception {
		Response response = new Response();
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");

			String[] stringArray = claimid.split(",");
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("CLAIMIDLIST", con);
			ARRAY array_to_pass = new ARRAY(des, con, stringArray);

			st = con.prepareCall("call USP_SNA_UNFREEZE_FLOAT(?,?,?,?,?,?,?,?,?,?)");
			st.setDate(1, null);
			st.setDate(2, null);
			st.setLong(3, (Long) userid);
			st.setInt(4, (Integer) 4);
			st.setArray(5, array_to_pass);
			st.setString(6, null);
			st.setString(7, null);
			st.setString(8, null);
			st.registerOutParameter(9, Types.INTEGER);
			st.registerOutParameter(10, Types.REF_CURSOR);
			st.execute();
			Integer out = st.getInt(9);
			if (out == 1) {
				response.setStatus("200");
				response.setMessage("Record Updated Sucessfully");
			} else {
				response.setStatus("400");
				response.setMessage("Something Went Wrong Db Side");
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new Exception(e.getMessage());
		}
		return response;
	}

	@Override
	public List<Object> getspecialfloatereport(Date todate, Date formdate, Long userId, Long snaid) throws Exception {
		List<Object> floatedetailslist = new ArrayList<Object>();
		System.out.println(formdate + " " + todate + " " + userId + " " + snaid);
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SPECIAL_FLOAT_VERIFY_RPT")
					.registerStoredProcedureParameter("P_SNA_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNA_ID", snaid);
			storedProcedureQuery.setParameter("P_FROM_DATE", formdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				Map<String, Object> bean = new HashMap<>();
				bean.put("splflaote", rs.getString(1));
				bean.put("snaId", rs.getString(2));
				bean.put("snaName", rs.getString(3));
				bean.put("doc", rs.getString(4));
				bean.put("floateId", rs.getString(5));
				floatedetailslist.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return floatedetailslist;
	}

	@Override
	public List<Object> gethospwisependingclaimdetails(String floatNumber, String hospcode) throws Exception {
		List<Object> hospwisependinglist = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_REST_CLAIM_STATUS")
					.registerStoredProcedureParameter("P_FLOAT_NO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REST_CLAIM_DTLS", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FLOAT_NO", floatNumber);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospcode);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_REST_CLAIM_DTLS");
			while (rs.next()) {
				Map<String, Object> bean = new HashMap<>();
				bean.put("urn", rs.getString(1));
				bean.put("claimNo", rs.getString(2));
				bean.put("patientName", rs.getString(3));
				bean.put("actdateofadm", rs.getString(4));
				bean.put("actdateofdis", rs.getString(5));
				bean.put("packageCode", rs.getString(6));
				bean.put("packageName", rs.getString(7));
				bean.put("claimstatus", rs.getString(8));
				hospwisependinglist.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return hospwisependinglist;
	}

}
