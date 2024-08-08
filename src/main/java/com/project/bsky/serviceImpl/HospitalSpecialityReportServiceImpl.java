package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospitalProcedurePackageBean;
import com.project.bsky.bean.HospitalProcedureTagging;
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.bean.PackageTaggingReportBean;
import com.project.bsky.bean.Response;
import com.project.bsky.repository.PackageHeaderRepo;
import com.project.bsky.service.HospitalSpecialityReportService;

@Service
public class HospitalSpecialityReportServiceImpl implements HospitalSpecialityReportService {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PackageHeaderRepo packageHeaderRepo;

	@Autowired
	private Logger logger;

	@Override
	public List<HospitalSpecialistListBean> gethospitalinfo(Long userid, Long actioncode, String state, String dist,
			String hospital) {
		List<HospitalSpecialistListBean> list = new ArrayList<HospitalSpecialistListBean>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_SPECIALITY_TAGGED_REPORT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_distcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action_code", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userid);
			storedProcedure.setParameter("p_statecode", state);
			storedProcedure.setParameter("p_distcode", dist);
			storedProcedure.setParameter("p_hospitalcode", hospital);
			storedProcedure.setParameter("p_action_code", actioncode);
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalSpecialistListBean bean = new HospitalSpecialistListBean();
				bean.setHospitalcode(rs.getString(1));
				bean.setHospitalname(rs.getString(2));
				if (actioncode == 1) {
					bean.setPackagecode(rs.getString(3));
					bean.setPackagename(rs.getString(4));
					bean.setHospitalTypeId(rs.getLong(5));
					bean.setHospitalTypeName(rs.getString(6));
					bean.setStateName(rs.getString(7));
					bean.setDistName(rs.getString(8));
				}
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

	@Override
	public List<HospitalProcedureTagging> procedureTaggingDetails(HospitalProcedurePackageBean packageBean)
			throws Exception {
		List<HospitalProcedureTagging> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PROCEDURE_TAGGING")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURE_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HEADER_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGE_HEADER_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION_CODE", packageBean.getActionCode());
			storedProcedure.setParameter("P_USERID", packageBean.getUserId());
			storedProcedure.setParameter("P_PROCEDURE_ID", packageBean.getProcedureId());
			storedProcedure.setParameter("P_HEADER_ID", packageBean.getHeaderId());
			storedProcedure.setParameter("P_PROCEDURECODE", packageBean.getProcedureCode());
			storedProcedure.setParameter("P_PACKAGE_HEADER_CODE", packageBean.getPackageHeaderCode());
			storedProcedure.execute();

			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			while (rs.next()) {
				HospitalProcedureTagging bean = new HospitalProcedureTagging();

				bean.setStatus("success");

				bean.setPackageHeaderCode(rs.getString(1));
				bean.setPackageHeaderName(rs.getString(2));

				if (Objects.equals(packageBean.getActionCode(), 1)) {
					bean.setSubPackageCode(rs.getString(3));
					bean.setSubPackageName(rs.getString(4));
					bean.setProcedureCode(rs.getString(5));
					bean.setProcedureDescription(rs.getString(6));
					bean.setProcedureId(rs.getInt(7));
					bean.setHeaderId(rs.getInt(8));
					bean.setIsSurgical(rs.getString(9));
				} else {
					bean.setHeaderId(rs.getInt(3));
					bean.setMasterStatus(rs.getInt(4));
					bean.setInsertStatus(rs.getInt(5));
					if (packageBean.getViewStatus().equals("view"))
						bean.setViewStatus(true);
				}
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
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

	@Override
	public List<Map<String, Object>> getPackageHeaderCode() {
		return packageHeaderRepo.getPackageHeaderCode();
	}

	@Override
	public Response submitTaggedProcedure(HospitalProcedurePackageBean packageBean) throws Exception {
		Integer output = null;
		Response respnse = new Response();
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_PROCEDURECODE_TAG_TO_HEADERCODE")
					.registerStoredProcedureParameter("P_USERID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURE_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_INPUT_STRINGS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);

			storedProcedure.setParameter("P_USERID", packageBean.getUserId());
			storedProcedure.setParameter("P_PROCEDURE_ID", packageBean.getProcedureId());
			storedProcedure.setParameter("P_PROCEDURECODE", packageBean.getProcedureCode());
			storedProcedure.setParameter("P_INPUT_STRINGS", packageBean.getPackageHeaderCode());

			storedProcedure.execute();

			output = (Integer) storedProcedure.getOutputParameterValue("P_OUT");
			if (output == 1) {
				respnse.setStatus("success");
			} else {
				throw new Exception("Exception in procedure");
			}

		} catch (Exception e) {
			throw e;
		}
		return respnse;
	}
	
	@Override
	public List<PackageTaggingReportBean> packageTaggingReport(String state, String dist, String hospital, Long type) {
		List<PackageTaggingReportBean> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_SPECIALITY_TAGGED_TO_PACKAGE")
					.registerStoredProcedureParameter("P_TYPE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_TYPE", type);
			storedProcedure.setParameter("P_STATE_CODE", state);
			storedProcedure.setParameter("P_DIST_CODE", dist);
			storedProcedure.setParameter("P_HOSPITAL_CODE", hospital);
			
			storedProcedure.execute();
			rs = (ResultSet) storedProcedure.getOutputParameterValue("P_MSG_OUT");
			
			while (rs.next()) {
				PackageTaggingReportBean bean = new PackageTaggingReportBean();
				
					bean.setStateName(rs.getString(1));
					bean.setDistrictName(rs.getString(2));
					bean.setHospitalCode(rs.getString(3));
					bean.setHospitalName(rs.getString(4));
					bean.setSpecialityType(rs.getString(5));
					bean.setHeaderCode(rs.getString(6));
					bean.setHeaderName(rs.getString(7));
					bean.setSubPackageCode(rs.getString(8));
					bean.setSubPackageName(rs.getString(9));
					bean.setProcedureCode(rs.getString(10));
					bean.setProcedureDescription(rs.getString(11));
					bean.setPackageAmount(rs.getString(12));
					bean.setTaggedType(rs.getString(13));
				
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
