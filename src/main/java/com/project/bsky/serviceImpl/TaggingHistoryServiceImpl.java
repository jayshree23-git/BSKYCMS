package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.PackageTaggingReportBean;
import com.project.bsky.bean.TaggingHistoryBean;
import com.project.bsky.repository.PackageHeaderRepo;
import com.project.bsky.service.HospitalSpecialityReportService;
import com.project.bsky.service.TaggingHistoryService;
@Service
public class TaggingHistoryServiceImpl implements TaggingHistoryService{
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PackageHeaderRepo packageHeaderRepo;

	@Autowired
	private Logger logger;

	@Override
	public List<TaggingHistoryBean> gettaggedhistory(String state, String dist, String hospital

) {
		List<TaggingHistoryBean> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_TAGGING_HISTORY")
//					.registerStoredProcedureParameter("P_TYPE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

//			storedProcedure.setParameter("P_TYPE", type);
			storedProcedure.setParameter("P_STATE_CODE", state);
			storedProcedure.setParameter("P_DIST_CODE", dist);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospital);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			
			while (rs.next()) {
				TaggingHistoryBean bean = new TaggingHistoryBean();
				
					bean.setLogid(rs.getString(1));
					bean.setStateName(rs.getString(2));
					bean.setDistrictName(rs.getString(3));
					bean.setHospitalCode(rs.getString(4));
					bean.setHospitalName(rs.getString(5));
					bean.setSpecialityType(rs.getString(6));
					bean.setHeaderCode(rs.getString(7));
					bean.setHeaderName(rs.getString(8));
					bean.setSubPackageCode(rs.getString(9));
					bean.setSubPackageName(rs.getString(10));
					bean.setProcedureCode(rs.getString(11));
					bean.setProcedureDescription(rs.getString(12));
					bean.setPackageAmount(rs.getString(13));
					bean.setFullname(rs.getString(14));
					bean.setCreatedon(rs.getString(15));
					bean.setUpdatedby(rs.getString(16));
					bean.setUpdatedon(rs.getString(17));
					bean.setStatus(rs.getString(18));

				
					list.add(bean);
				}
				
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return list;
	}
}
