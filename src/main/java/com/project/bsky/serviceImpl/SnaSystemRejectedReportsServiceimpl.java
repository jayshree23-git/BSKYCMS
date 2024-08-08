/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.SnaSysRejectedList;
import com.project.bsky.repository.SNOConfigurationRepository;
import com.project.bsky.service.SystemRejectedReportsService;

/**
 * @author rajendra.sahoo
 *
 */
@Service
public class SnaSystemRejectedReportsServiceimpl implements SystemRejectedReportsService {
	
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<SnaSysRejectedList> sysrejreports(String formdate, String todate, String state, String dist, String hospital,
			String userid) {
		List<SnaSysRejectedList> snasysrejectlist=new ArrayList<SnaSysRejectedList>();
		ResultSet sysrejectedlist = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_SNA_SYS_REJ_LIST")
					.registerStoredProcedureParameter("P_SNO_USER_ID",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_from_date",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_to_date",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_STATECODE",String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE",String.class, ParameterMode.IN)					
					.registerStoredProcedureParameter("p_msgout",void.class, ParameterMode.REF_CURSOR);
			
			storedProcedure.setParameter("P_SNO_USER_ID",userid);
			storedProcedure.setParameter("p_hospitalcode",hospital);
			storedProcedure.setParameter("p_from_date",formdate);
			storedProcedure.setParameter("p_to_date",todate);
			storedProcedure.setParameter("p_STATECODE",state);
			storedProcedure.setParameter("P_DISTRICTCODE",dist);
			storedProcedure.execute();
			sysrejectedlist = (ResultSet) storedProcedure.getOutputParameterValue("p_msgout");
			while(sysrejectedlist.next()) {
				SnaSysRejectedList resbean=new SnaSysRejectedList();
				resbean.setUrn(sysrejectedlist.getString(1));
				resbean.setSnouserid(sysrejectedlist.getString(2));
				resbean.setHospitalcode(sysrejectedlist.getString(3));
				resbean.setHospitalname(sysrejectedlist.getString(4));
				resbean.setPatientname(sysrejectedlist.getString(5));
				resbean.setPackagename(sysrejectedlist.getString(6));
				resbean.setPackagecode(sysrejectedlist.getString(7));
				resbean.setAmountclaimed(sysrejectedlist.getString(8));
				resbean.setClaim_raised_by(sysrejectedlist.getString(9));
				////System.out.println(resbean);
				snasysrejectlist.add(resbean);
			}
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (sysrejectedlist != null) {
					sysrejectedlist.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return snasysrejectlist;
	}

}
