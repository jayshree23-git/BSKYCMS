/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.FoBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.financiladetailsbean;
import com.project.bsky.service.FinancialOfficerService;

/**
 * @author hrusikesh.mohanty
 *
 */
@Service
public class FinancialOfficerServiceImpl implements FinancialOfficerService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private Logger logger;

	@Override
	public List<Object> getfinaciladetails(Date fromDate, Date toDate, String finacialno) {
		List<Object> finacilakst = new ArrayList<Object>();
		ResultSet finacial = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SP_FIANCIALLIST")
					.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fiancialno", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_from_date", fromDate);
			storedProcedureQuery.setParameter("p_to_date", toDate);
			storedProcedureQuery.setParameter("p_fiancialno", finacialno);
			storedProcedureQuery.execute();
			finacial = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (finacial.next()) {
				financiladetailsbean list = new financiladetailsbean();
				list.setFLOAT_ID(finacial.getLong(1));
				list.setFLOAT_NO(finacial.getString(2));
				list.setAMOUNT(finacial.getDouble(3));
				list.setCREATED_BY(finacial.getString(4));
				list.setCREATED_ON(finacial.getString(5));
				list.setSTATUS_FLAG(finacial.getInt(6));
				list.setPENDING_AT(finacial.getInt(7));
				list.setPAYMENT_STATUS(finacial.getInt(8));
				finacilakst.add(list);
				////System.out.println(list);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (finacial != null) {
					finacial.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return finacilakst;
	}

	@Override
	public List<Object> getUSerDetailsDAta(String id) {
		////System.out.println(id);
		List<Object> finacilakstthroughid = new ArrayList<Object>();
		ResultSet finacialdetails = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SP_FINACIALDETAILSID")
					.registerStoredProcedureParameter("p_id", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_id", id);
			storedProcedureQuery.execute();
			finacialdetails = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (finacialdetails.next()) {
				financiladetailsbean listed = new financiladetailsbean();
				listed.setFLOAT_NO(finacialdetails.getString(1));
				listed.setAMOUNT(finacialdetails.getDouble(2));
				listed.setCREATED_BY(finacialdetails.getString(3));
				listed.setCREATED_ON(finacialdetails.getString(4));
				listed.setSTATUS_FLAG(finacialdetails.getInt(5));
				listed.setPENDING_AT(finacialdetails.getInt(6));
				listed.setPAYMENT_STATUS(finacialdetails.getInt(7));
				listed.setFLOAT_ID(finacialdetails.getLong(8));
				listed.setTransactiondetailsid(finacialdetails.getLong(9));
				listed.setClaimid(finacialdetails.getLong(10));
				listed.setURNno(finacialdetails.getString(11));
				listed.setPackageid(finacialdetails.getString(12));
				listed.setPackagename(finacialdetails.getString(13));
				listed.setPatentname(finacialdetails.getString(14));
				listed.setCLaimno(finacialdetails.getString(15));
				listed.setClaimRaisedon(finacialdetails.getString(16));
				listed.setHospitalCode(finacialdetails.getString(17));
				listed.setHospitalname(finacialdetails.getString(18)+" ("+finacialdetails.getString(17)+")");
				listed.setActualdateofAdmission(finacialdetails.getString(19));
				listed.setActualdateofDischarge(finacialdetails.getString(20));
				finacilakstthroughid.add(listed);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (finacialdetails != null) {
					finacialdetails.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return finacilakstthroughid;
	}

	@Override
	public Response insertdata(String remarks, long value, long userid, long amount, long floatid, String floatno,
			String flag) throws Exception {
		InetAddress localhost = InetAddress.getLocalHost();
		String getuseripaddressString = localhost.getHostAddress();
		Integer claimraiseInteger = null;
		Response response = new Response();

		try {
			StoredProcedureQuery saveCpdUserData = this.entityManager
					.createStoredProcedureQuery("USP_FO_INSERT_FODETAILS")
					.registerStoredProcedureParameter("P_FLOAT_ID", long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FLOATNO", String.class, ParameterMode.IN)
//					.registerStoredProcedureParameter("p_AMOUNT", long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_FOACTIONTYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_FOCREATEDBY", long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_USER_IP", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_ASSIGN_AUTHORITY", long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", Integer.class, ParameterMode.OUT);

			saveCpdUserData.setParameter("P_FLOAT_ID", floatid);
			saveCpdUserData.setParameter("P_FLOATNO", floatno);
//					saveCpdUserData.setParameter("p_AMOUNT", amount);
			saveCpdUserData.setParameter("p_FOACTIONTYPE", flag);
			saveCpdUserData.setParameter("p_FOCREATEDBY", userid);
			saveCpdUserData.setParameter("p_REMARKS", remarks);
			saveCpdUserData.setParameter("p_USER_IP", getuseripaddressString);
			saveCpdUserData.setParameter("p_ASSIGN_AUTHORITY", value);
			saveCpdUserData.execute();
			claimraiseInteger = (Integer) saveCpdUserData.getOutputParameterValue("p_p_msgout");
			if (claimraiseInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Forwarded Successfully");
			} else if (claimraiseInteger == 2) {
				response.setStatus("Failed");
				response.setMessage("OOPS Something Went Worng,please Try Agian..");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	@Override
	public Response updatedvalue(List<FoBean> foBeanList, String approvedAmount, String userid, String remarks) {
		String amount =null;
		int sumvalue=0;
		for (FoBean num : foBeanList) {
			try {
				String id = num.getId();
				 amount = num.getAmount();
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			 sumvalue +=Integer.parseInt(amount);
			 ////System.out.println(sumvalue);
		}
		int sum = 0;
		try {
			int[] arr = Arrays.stream(approvedAmount.substring(1, approvedAmount.length() - 1).split(","))
					.map(String::trim).mapToInt(Integer::parseInt).toArray();
			for (int i : arr) {
				sum += i;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}