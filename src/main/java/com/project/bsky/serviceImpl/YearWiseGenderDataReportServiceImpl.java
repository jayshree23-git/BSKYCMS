/**
 * 
 */
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

import com.project.bsky.bean.YearWiseGenderDataReportBean;
import com.project.bsky.service.YearWiseGenderDataReportService;

/**
 * @author priyanka.singh
 *
 */
@Service
public class YearWiseGenderDataReportServiceImpl implements YearWiseGenderDataReportService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getbenificiarygenderdtls(Long age, String ageconditions) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GENDERWISE_NFSA_REPORT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "D");
			storedProcedure.setParameter("P_DISTRICTID", null);
			storedProcedure.setParameter("P_BLOCKID_ULBID", null);
			storedProcedure.setParameter("P_GPID_WARDID", null);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", null);
			storedProcedure.setParameter("P_AGE", age);
			storedProcedure.setParameter("P_AGE_CONDITION", ageconditions.trim());

			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setDistrictName(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setDistrictId(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setBenificiary(list.getString(3) == null ? "N/A" : list.getString(3));
				details.setMale(list.getString(4) == null ? "0" : list.getString(4));
				details.setFemale(list.getString(5) == null ? "0" : list.getString(5));
				details.setOther(list.getString(6) == null ? "0" : list.getString(6));
				object.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return object;
	}

	@Override
	public List<Object> getbenificiarygenderblockdata(String districtId) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;

		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GENDERWISE_NFSA_REPORT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "B");
			storedProcedure.setParameter("P_DISTRICTID", districtId.trim());
			storedProcedure.setParameter("P_BLOCKID_ULBID", null);
			storedProcedure.setParameter("P_GPID_WARDID", null);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", null);
			storedProcedure.setParameter("P_AGE", null);
			storedProcedure.setParameter("P_AGE_CONDITION", null);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setDistrictId(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setDistrictName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setBlockId(list.getString(3) == null ? "0" : list.getString(3));
				details.setBlockName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setMale(list.getString(5) == null ? "0" : list.getString(5));
				details.setFemale(list.getString(6) == null ? "0" : list.getString(6));
				details.setOther(list.getString(7) == null ? "0" : list.getString(7));
				details.setBenificiary(list.getString(8) != null ? list.getString(8) : "N/A");
				object.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return object;
	}

	@Override
	public List<Object> getbenificiarygendergramdetails(String districtId, String blockId) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;

		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GENDERWISE_NFSA_REPORT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "GP");
			storedProcedure.setParameter("P_DISTRICTID", districtId.trim());
			storedProcedure.setParameter("P_BLOCKID_ULBID", blockId);
			storedProcedure.setParameter("P_GPID_WARDID", null);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", null);
			storedProcedure.setParameter("P_AGE", null);
			storedProcedure.setParameter("P_AGE_CONDITION", null);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setDistrictId(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setDistrictName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setMale(list.getString(7) == null ? "0" : list.getString(7));
				details.setFemale(list.getString(8) == null ? "0" : list.getString(8));
				details.setOther(list.getString(9) == null ? "0" : list.getString(9));
				details.setBlockId(list.getString(3) == null ? "0" : list.getString(3));
				details.setBlockName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setGramId(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setGramName(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setBenificiary(list.getString(10) != null ? list.getString(10) : "N/A");
				object.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return object;
	}

	@Override
	public List<Object> getbenificiarygendervillagedata(String districtId, String blockId, String gramId) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;

		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GENDERWISE_NFSA_REPORT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "V");
			storedProcedure.setParameter("P_DISTRICTID", districtId);
			storedProcedure.setParameter("P_BLOCKID_ULBID", blockId);
			storedProcedure.setParameter("P_GPID_WARDID", gramId);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", null);
			storedProcedure.setParameter("P_AGE", null);
			storedProcedure.setParameter("P_AGE_CONDITION", null);

			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setDistrictId(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setDistrictName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setMale(list.getString(9) == null ? "0" : list.getString(9));
				details.setFemale(list.getString(10) == null ? "0" : list.getString(10));
				details.setOther(list.getString(11) == null ? "0" : list.getString(11));
				details.setBlockId(list.getString(3) == null ? "0" : list.getString(3));
				details.setBlockName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setGramId(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setGramName(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setVillageId(list.getString(7) == null ? "N/A" : list.getString(7));
				details.setVillageName(list.getString(8) == null ? "N/A" : list.getString(8));
				details.setBenificiary(list.getString(12) != null ? list.getString(12) : "N/A");
				object.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return object;
	}

	@Override
	public List<Object> getbenificiarydata(String districtId, String blockId, String gramId, String villageId) {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;

		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_GENDERWISE_NFSA_REPORT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "VD");
			storedProcedure.setParameter("P_DISTRICTID", districtId);
			storedProcedure.setParameter("P_BLOCKID_ULBID", blockId);
			storedProcedure.setParameter("P_GPID_WARDID", gramId);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", villageId);
			storedProcedure.setParameter("P_AGE", null);
			storedProcedure.setParameter("P_AGE_CONDITION", null);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setRationCardNo(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setFullNameEng(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setFullNameOdia(list.getString(3) == null ? "N/A" : list.getString(3));
				details.setAdharName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setGender(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setDateOfBirth(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setAge(list.getString(7) == null ? "N/A" : list.getString(7));
				details.setRelationWithFamily(list.getString(8) == null ? "N/A" : list.getString(8));
				object.add(details);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (list != null) {
					list.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}

		}
		return object;
	}

}
