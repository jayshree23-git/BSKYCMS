/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.BeneficiaryDischargedataBean;
import com.project.bsky.bean.YearWiseGenderDataReportBean;
import com.project.bsky.service.BeneficiaryDistrictwisedataService;

/**
 * @author rajendra.sahoo Dt-20/11/2023
 *
 */
@Service
public class BeneficiaryDistrictwisedataServicesimpl implements BeneficiaryDistrictwisedataService {

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object> getbenificiaryblockwisedata(Long age, String ageconditions, String distcode, Long userid)
			throws Exception {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BENEFICIARY_DISTRICT_WISE_DATA_RPT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "B");
			storedProcedure.setParameter("P_DISTRICTID", distcode.trim());
			storedProcedure.setParameter("P_BLOCKID_ULBID", null);
			storedProcedure.setParameter("P_GPID_WARDID", null);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", null);
			storedProcedure.setParameter("P_AGE", age);
			storedProcedure.setParameter("P_AGE_CONDITION", ageconditions.trim());
			storedProcedure.setParameter("P_USERID", userid);

			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setDistrictId(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setDistrictName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setBlockId(list.getString(3) == null ? "0" : list.getString(3));
				details.setBlockName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setBenificiary(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setTotalFamilyMember(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setUniqueBenefCasetreded(list.getString(7) == null ? "N/A" : list.getString(7));
				details.setTotalClaimTreatedcase(list.getString(8) == null ? "N/A" : list.getString(8));
				details.setTotalTreatedAmount(list.getString(9) == null ? "N/A" : list.getString(9));
				details.setTreatedMale(list.getString(10) == null ? "0" : list.getString(10));
				details.setTreatedMaleAmt(list.getString(11) == null ? "0" : list.getString(11));
				details.setTreatedFeMale(list.getString(12) == null ? "0" : list.getString(12));
				details.setTreatedFemaleAmt(list.getString(13) == null ? "0" : list.getString(13));
				details.setTreatedOther(list.getString(14) == null ? "0" : list.getString(14));
				details.setTreatedOtherAmt(list.getString(15) == null ? "0" : list.getString(15));
				details.setMale(list.getString(16) == null ? "0" : list.getString(16));
				details.setFemale(list.getString(17) == null ? "0" : list.getString(17));
				details.setOther(list.getString(18) == null ? "0" : list.getString(18));
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
	public List<Object> getbenificiarygpwisedata(Long age, String ageconditions, String distcode, String blockcode,
			Long userid) {

		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {

			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BENEFICIARY_DISTRICT_WISE_DATA_RPT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "GP");
			storedProcedure.setParameter("P_DISTRICTID", distcode.trim());
			storedProcedure.setParameter("P_BLOCKID_ULBID", blockcode);
			storedProcedure.setParameter("P_GPID_WARDID", null);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", null);
			storedProcedure.setParameter("P_AGE", age);
			storedProcedure.setParameter("P_AGE_CONDITION", ageconditions.trim());
			storedProcedure.setParameter("P_USERID", userid);

			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setDistrictId(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setDistrictName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setBlockId(list.getString(3) == null ? "0" : list.getString(3));
				details.setBlockName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setGramId(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setGramName(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setBenificiary(list.getString(7) != null ? list.getString(7) : "N/A");
				details.setTotalFamilyMember(list.getString(8) == null ? "N/A" : list.getString(8));
				details.setUniqueBenefCasetreded(list.getString(9) == null ? "N/A" : list.getString(9));
				details.setTotalClaimTreatedcase(list.getString(10) == null ? "N/A" : list.getString(10));
				details.setTotalTreatedAmount(list.getString(11) == null ? "N/A" : list.getString(11));
				details.setTreatedMale(list.getString(12) == null ? "0" : list.getString(12));
				details.setTreatedMaleAmt(list.getString(13) == null ? "0" : list.getString(13));
				details.setTreatedFeMale(list.getString(14) == null ? "0" : list.getString(14));
				details.setTreatedFemaleAmt(list.getString(15) == null ? "0" : list.getString(15));
				details.setTreatedOther(list.getString(16) == null ? "0" : list.getString(16));
				details.setTreatedOtherAmt(list.getString(17) == null ? "0" : list.getString(17));
				details.setMale(list.getString(18) == null ? "0" : list.getString(18));
				details.setFemale(list.getString(19) == null ? "0" : list.getString(19));
				details.setOther(list.getString(20) == null ? "0" : list.getString(20));
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
	public List<Object> getbenificiaryvillagewisedata(Long age, String ageconditions, String distcode, String blockcode,
			String gpcode, Long userid) throws Exception {
		List<Object> object = new ArrayList<Object>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_BENEFICIARY_DISTRICT_WISE_DATA_RPT")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCKID_ULBID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GPID_WARDID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_LOCALITYID_VILLAGEID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_AGE_CONDITION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("P_ACTION", "V");
			storedProcedure.setParameter("P_DISTRICTID", distcode.trim());
			storedProcedure.setParameter("P_BLOCKID_ULBID", blockcode);
			storedProcedure.setParameter("P_GPID_WARDID", gpcode);
			storedProcedure.setParameter("P_LOCALITYID_VILLAGEID", null);
			storedProcedure.setParameter("P_AGE", age);
			storedProcedure.setParameter("P_AGE_CONDITION", ageconditions.trim());
			storedProcedure.setParameter("P_USERID", userid);

			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("P_OUT");
			while (list.next()) {
				YearWiseGenderDataReportBean details = new YearWiseGenderDataReportBean();
				details.setDistrictId(list.getString(1) == null ? "N/A" : list.getString(1));
				details.setDistrictName(list.getString(2) == null ? "N/A" : list.getString(2));
				details.setBlockId(list.getString(3) == null ? "0" : list.getString(3));
				details.setBlockName(list.getString(4) == null ? "N/A" : list.getString(4));
				details.setGramId(list.getString(5) == null ? "N/A" : list.getString(5));
				details.setGramName(list.getString(6) == null ? "N/A" : list.getString(6));
				details.setVillageId(list.getString(7) == null ? "N/A" : list.getString(7));
				details.setVillageName(list.getString(8) == null ? "N/A" : list.getString(8));
				details.setMale(list.getString(9) == null ? "0" : list.getString(9));
				details.setFemale(list.getString(10) == null ? "0" : list.getString(10));
				details.setOther(list.getString(11) == null ? "0" : list.getString(11));
				details.setBenificiary(list.getString(12) != null ? list.getString(12) : "N/A");
				details.setTreatedMale(list.getString(13) != null ? list.getString(13) : "0");
				details.setTreatedFeMale(list.getString(14) != null ? list.getString(14) : "0");
				details.setTreatedOther(list.getString(15) != null ? list.getString(15) : "0");
				details.setTreatedMaleAmount(list.getString(16) != null ? list.getString(16) : "0");
				details.setTreatedFeMaleAmount(list.getString(17) != null ? list.getString(17) : "0");
				details.setTreatedOtherAmount(list.getString(18) != null ? list.getString(18) : "0");
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
	public List<Object> getstatedashboarddata(Date formdate, Date todate) throws Exception {
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_BSKY_TRANSACTION_DATA_CONSOLIDATION")
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<>();
				map.put("createon", rs.getString(1));
				map.put("totaldischarge", rs.getString(2));
				map.put("totalamount", rs.getString(3));
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
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
		return list;
	}

}
