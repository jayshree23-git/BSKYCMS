package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.RationCardSchedularReportBean;
import com.project.bsky.service.RationCardSchedularReportService;

@Service
public class RationCardSchedularReportServiceImpl implements RationCardSchedularReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<RationCardSchedularReportBean> list(String year, String month) {
		List<RationCardSchedularReportBean> rationlist = new ArrayList<RationCardSchedularReportBean>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_RATION_CARD_SCHEDULER_RPRT ")
					.registerStoredProcedureParameter("P_YEAR", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("P_YEAR", year);
			storedProcedure.setParameter("P_MONTH", month);

			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (list.next()) {
				RationCardSchedularReportBean hospbean = new RationCardSchedularReportBean();
				hospbean.setId(list.getLong(1));
				hospbean.setSERVICE_STATUS(list.getString(2));
				hospbean.setSTART_DATE(list.getString(3));
				hospbean.setEND_DATE(list.getString(4));
				hospbean.setRECORDS_FETCHED(list.getLong(5));
				hospbean.setRECORDS_INSERTED(list.getLong(6));
				hospbean.setRECORDS_UPDATED(list.getLong(7));
				hospbean.setRECORDS_FAILED(list.getLong(8));
				hospbean.setCREATED_BY(list.getString(9));
				hospbean.setCREATED_ON(list.getString(10));
				rationlist.add(hospbean);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return rationlist;
	}

	@Override
	public Map<String, Object> details(Long userid, String date, String flag, String type) {
		List<RationCardSchedularReportBean> hospitallist = new ArrayList<RationCardSchedularReportBean>();
		ResultSet list = null;
		Map<String, Object> details = new HashMap<String, Object>();
		Date d = null;
		String s = "";
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e1) {
			logger.error(ExceptionUtils.getStackTrace(e1));
		}
		if (type.contains("Basic")) {
			s = "Basic";
			details.put("status", 200);
		} else {
			s = "Member";
			details.put("status", 201);
		}
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_RATION_CARD_SCHEDULAR_DTLS_RPRT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_date", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_event_name", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_type", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", void.class, ParameterMode.REF_CURSOR);
			storedProcedure.setParameter("p_user_id", userid);
			storedProcedure.setParameter("p_date", d);
			storedProcedure.setParameter("p_event_name", flag);
			storedProcedure.setParameter("p_type", s);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			if (s.equals("Member")) {
				while (list.next()) {
					RationCardSchedularReportBean hospbean = new RationCardSchedularReportBean();
					hospbean.setIdentity(list.getString(1));
					hospbean.setHealthslcardno(list.getString(2));
					hospbean.setRATIONCARDNUMBER(list.getString(3));
					hospbean.setMemberid(list.getString(4));
					hospbean.setFullnameenglish(list.getString(5));
					hospbean.setFullnameodiya(list.getString(6));
					hospbean.setAadhaarnumber(list.getString(7));
					hospbean.setGender(list.getString(8));
					hospbean.setDateofbirth(list.getString(9));
					hospbean.setAge(list.getString(10));
					hospbean.setRelationshipwithfam(list.getString(11));
					hospbean.setSchemetype(list.getString(12));
					hospbean.setMobilenumber(list.getString(13));
					hospbean.setStatus(list.getString(14));
					hospbean.setAdditiondeletionstatus(list.getString(15));
					hospbean.setData_status(list.getString(16));
					hospbean.setExportdate(list.getString(17));
					hospbean.setUpdatedate(list.getString(18));
					hospbean.setCREATED_ON(list.getString(19));
					hospbean.setCREATED_BY(list.getString(20));
					hospbean.setUpdated_on(list.getString(21));
					hospbean.setUpdated_by(list.getString(22));
					hospbean.setStatus_flag(list.getString(23));
					hospitallist.add(hospbean);
				}
			} else {
				while (list.next()) {
					RationCardSchedularReportBean hospbean = new RationCardSchedularReportBean();
					hospbean.setIdentity(list.getString(1));
					hospbean.setHealthslno(list.getString(2));
					hospbean.setRATIONCARDNUMBER(list.getString(3));
					hospbean.setRationcardtype(list.getString(4));
					hospbean.setFullnameenglish(list.getString(5));
					hospbean.setFullnameodiya(list.getString(6));
					hospbean.setAadhaarnumber(list.getString(7));
					hospbean.setGender(list.getString(8));
					hospbean.setSpousefullname(list.getString(9));
					hospbean.setFatherfullname(list.getString(10));
					hospbean.setMobilenumber(list.getString(11));
					hospbean.setDistrict(list.getString(12));
					hospbean.setDistrictid(list.getString(13));
					hospbean.setBlock_ulb(list.getString(14));
					hospbean.setBlockid_ulbid(list.getString(15));
					hospbean.setGp_ward(list.getString(16));
					hospbean.setGpid_wardid(list.getString(17));
					hospbean.setLocality_village(list.getString(18));
					hospbean.setLocalityid_villageid(list.getString(19));
					hospbean.setFpsname(list.getString(20));
					hospbean.setSchemetype(list.getString(21));
					hospbean.setStatus(list.getString(22));
					hospbean.setAdditiondeletionstatus(list.getString(23));
					hospbean.setData_status(list.getString(24));
					hospbean.setExportdate(list.getString(25));
					hospbean.setUpdatedate(list.getString(26));
					hospbean.setCREATED_ON(list.getString(27));
					hospbean.setCREATED_BY(list.getString(28));
					hospbean.setUpdated_on(list.getString(29));
					hospbean.setUpdated_by(list.getString(30));
					hospbean.setStatus_flag(list.getString(31));
					hospitallist.add(hospbean);
				}
			}
			details.put("data", hospitallist);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return details;
	}
}
