package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.SnaDoctorTagBean;
import com.project.bsky.service.SnaDoctorTagService;

/**
 * @author jayshree.moharana
 *
 */
@Service
public class SnaDoctorTagServiceimpl implements SnaDoctorTagService {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<SnaDoctorTagBean> getlist(String userId) {
		List<SnaDoctorTagBean> count = new ArrayList<SnaDoctorTagBean>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_DOCTOR_TAG")
					.registerStoredProcedureParameter("P_SNO_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNO_USER_ID", userId);
			storedProcedureQuery.setParameter("p_action", 0);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				SnaDoctorTagBean stbean = new SnaDoctorTagBean();
				stbean.setUserid(snoDetailsObj.getLong(1));
				stbean.setMobile(snoDetailsObj.getString(3));
				stbean.setEmail(snoDetailsObj.getString(4));
				stbean.setNoofhospital(snoDetailsObj.getInt(5));
				stbean.setSnaname(snoDetailsObj.getString(2));
				count.add(stbean);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return count;
	}

	@Override
	public List<SnaDoctorTagBean> gethospitallist(String userId) {
		List<SnaDoctorTagBean> count = new ArrayList<SnaDoctorTagBean>();
		ResultSet snoDetailsObj = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_SNA_DOCTOR_TAG")
					.registerStoredProcedureParameter("P_SNO_USER_ID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SNO_USER_ID", userId);
			storedProcedureQuery.setParameter("p_action", 1);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (snoDetailsObj.next()) {
				SnaDoctorTagBean stbean = new SnaDoctorTagBean();
				stbean.setUserid(snoDetailsObj.getLong(1));
				stbean.setHospitalcode(snoDetailsObj.getString(2));
				stbean.setHospitalname(snoDetailsObj.getString(3));
				stbean.setMobile(snoDetailsObj.getString(5));
				stbean.setEmail(snoDetailsObj.getString(4));
				stbean.setState(snoDetailsObj.getString(6));
				stbean.setDistrict(snoDetailsObj.getString(7));
				stbean.setAssigndc(snoDetailsObj.getString(8));
				stbean.setHcvalidfrm(snoDetailsObj.getString(9));
				stbean.setHcvalidto(snoDetailsObj.getString(10));
				stbean.setMoustart(snoDetailsObj.getString(11));
				stbean.setMouend(snoDetailsObj.getString(12));
				stbean.setCpdapproval(snoDetailsObj.getString(13));
				stbean.setHospcatogory(snoDetailsObj.getString(14));
				stbean.setTmsactivestatus(snoDetailsObj.getString(15));
				count.add(stbean);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return count;
	}
}
