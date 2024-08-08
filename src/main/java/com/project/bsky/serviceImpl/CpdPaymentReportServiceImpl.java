package com.project.bsky.serviceImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.CpdPaymentDetailsbean;
import com.project.bsky.bean.CpdPaymentReportBean;
import com.project.bsky.bean.MortalityListbean;
import com.project.bsky.bean.OutSideTreatmnetbean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UrnwiseutilizeReportbean;
import com.project.bsky.bean.packagebean;
import com.project.bsky.model.CpdPaymentReportModel;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.CpdPaymentReportRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.CpdPaymentReportService;
import com.project.bsky.util.DateFormat;
import com.project.bsky.util.JwtUtil;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@SuppressWarnings("unused")
@Service
public class CpdPaymentReportServiceImpl implements CpdPaymentReportService {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CpdPaymentReportRepository cpdpaymentreportrepo;
	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private Logger logger;

	@Autowired
	private Environment env;

	@Autowired
	private JwtUtil util;

	@Override
	public CpdPaymentReportModel getNote() {
		CpdPaymentReportModel list = new CpdPaymentReportModel();
		try {
			list = cpdpaymentreportrepo.getNote();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<CpdPaymentReportBean> details(Long userid, Long actiontype, String year, String month,
			String hospitalcode, String statecode, String districtcode, Long flag) {
		List<CpdPaymentReportBean> paymentdetails = new ArrayList<CpdPaymentReportBean>();
		ResultSet list = null;
		try {
			StoredProcedureQuery storedProcedure = this.entityManager
					.createStoredProcedureQuery("USP_CPD_PAYMENT_DETAILS_RPRT")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_action_type", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_year", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_month", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msg", void.class, ParameterMode.REF_CURSOR);

			storedProcedure.setParameter("p_user_id", userid);
			storedProcedure.setParameter("p_action_type", actiontype);
			storedProcedure.setParameter("p_year", year);
			storedProcedure.setParameter("p_month", month);
			storedProcedure.setParameter("p_hosptlcode", hospitalcode);
			storedProcedure.setParameter("p_statecode", statecode);
			storedProcedure.setParameter("p_districtcode", districtcode);
			storedProcedure.setParameter("p_flag", flag);
			storedProcedure.execute();
			list = (ResultSet) storedProcedure.getOutputParameterValue("p_msg");
			while (list.next()) {
				CpdPaymentReportBean cpdbean = new CpdPaymentReportBean();
				cpdbean.setCLAIMID(list.getLong(1));
				cpdbean.setAPPROVEDAMOUNT(list.getString(2));
				cpdbean.setClaimno(list.getString(3));
				cpdbean.setUrn(list.getString(4));
				cpdbean.setPATIENTNAME(list.getString(5));
				cpdbean.setCurrenttotalamount(list.getString(6));
				cpdbean.setPackagename(list.getString(7));
				cpdbean.setHospitalname(list.getString(8));
				cpdbean.setActualdateofadmission(list.getString(9));
				cpdbean.setActualdateofdischarge(list.getString(10));
				cpdbean.setAuthorizedcode(list.getString(11));
				cpdbean.setHospitalcode(list.getString(12));
				cpdbean.setPackagecode(list.getString(13));
				cpdbean.setINVOICENO(list.getString(14));
				cpdbean.setActionon(list.getString(15));
				cpdbean.setCreatedOn(list.getString(16));
				if (actiontype == 4l) {
					cpdbean.setAssignedcpd(list.getLong(17));
					cpdbean.setDishonour(list.getString(18));
					cpdbean.setPreviuousAllotedDate(list.getString(19));
					cpdbean.setCpdalloteddate(list.getString(20));
				}
				paymentdetails.add(cpdbean);
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
		return paymentdetails;
	}

	@Override
	public List<UserDetails> getCPDUserList() {
		return userDetailsRepository.getCPDUserList();
	}

	@Override
	public Map<String, Object> getCPDPaymentCalculationList(Date fromDate, Date toDate, Long userId)
			throws SQLException {
		Map<String, Object> responseMap = new LinkedHashMap<>();
		List<Map<String, Object>> responseList = new ArrayList<>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_PAYMENT_CALCULATION")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ASSIGNEDCPD", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", new SimpleDateFormat("yy-MM-dd").format(fromDate));
			storedProcedureQuery.setParameter("P_TODATE", new SimpleDateFormat("yy-MM-dd").format(toDate));
			storedProcedureQuery.setParameter("P_ASSIGNEDCPD", userId == 0 ? null : userId);
			storedProcedureQuery.execute();
			ResultSet resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (resultSet.next()) {
				Map<String, Object> response = new LinkedHashMap<>();
				response.put("userId", resultSet.getLong(1));
				response.put("fullName", resultSet.getString(2));
				response.put("pendingAt", resultSet.getLong(3));
				response.put("claimStatus", resultSet.getLong(4));
				response.put("claimDesc", resultSet.getString(5));
				response.put("totalClaims", resultSet.getLong(6));
				response.put("totalAmountPaid", resultSet.getDouble(8));
				response.put("actionCode", resultSet.getDouble(9));
				responseList.add(response);
			}
			List<Map<String, Object>> responseMapList;
			responseMapList = responseList.stream().filter(map -> map.get("fullName") != null)
					.sorted(Comparator.comparing(map -> (String) map.get("fullName"))).collect(Collectors.toList());
			responseMap.put("totalClaimsCount",
					responseList.stream().mapToLong(map -> (Long) map.get("totalClaims")).sum());
			responseMap.put("totalAmount",
					responseList.stream().mapToDouble(map -> (Double) map.get("totalAmountPaid")).sum());
			responseMap.put("responseList", responseMapList);
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Map<String, Object> getCPDPaymentDetailsUserId(Date fromDate, Date toDate, Long userId, Integer actionCode)
			throws SQLException {
		List<String> claimsApprovedBySNA = Arrays.asList("claimId", "Claim No", "Pending At", "Claim Status",
				"Claim Description", "Assigned SNA", "SNA Name", "CPD Allotted Date", "Claim Type",
				"Actual Date of Discharge", "CPD Action Date");
		List<Map<String, Object>> responseList = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_PAYMENT_CALCULATION_DETAILS")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ASSIGNEDCPD", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", new SimpleDateFormat("yy-MM-dd").format(fromDate));
			storedProcedureQuery.setParameter("P_TODATE", new SimpleDateFormat("yy-MM-dd").format(toDate));
			storedProcedureQuery.setParameter("P_ASSIGNEDCPD", userId);
			storedProcedureQuery.setParameter("P_ACTION", actionCode);
			storedProcedureQuery.execute();
			ResultSet resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			responseList = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> response = new LinkedHashMap<>();
				response.put("Claim Id", resultSet.getLong(1));
				response.put("Claim No", resultSet.getString(2));
				response.put("Pending At", resultSet.getLong(3));
				response.put("Claim Status", resultSet.getLong(4));
				response.put("Claim Description", resultSet.getString(5));
				response.put("Assigned SNA", resultSet.getLong(6));
				response.put("SNA Name", resultSet.getString(7));
				response.put("CPD Allotted Date", resultSet.getString(8));
				response.put("Claim Type", resultSet.getLong(9));
				response.put("Actual Date of Discharge", resultSet.getString(10));
				response.put("CPD Action Date", resultSet.getString(11));
				responseList.add(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object> getmoratlityDetailsdata(Long userId, Date fromdate, Date todate, String stateCodeList,
			String districtCodeList, String hospitalCodeList) {
		ResultSet resultset = null;
		List<Object> detaisl = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_MORTALITY_REPORT")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USER_ID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_USER_ID", userId);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromdate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_STATE_CODE", stateCodeList.trim());
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", districtCodeList.trim());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCodeList.trim());
			storedProcedureQuery.execute();
			resultset = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (resultset.next()) {
				MortalityListbean datalist = new MortalityListbean();
				datalist.setUrn(resultset.getString(1));
				datalist.setPatientname(resultset.getString(2));
				datalist.setAge(resultset.getString(3));
				datalist.setPackagename(resultset.getString(4));
				datalist.setPackagecode(resultset.getString(5));
				datalist.setActualdateofAdmission(resultset.getString(6));
				datalist.setActualdateofDischarge(resultset.getString(7));
				if (resultset.getString(8).equalsIgnoreCase("N")) {
					datalist.setMoratlity("No");
				} else if (resultset.getString(8).equalsIgnoreCase("Y")) {
					datalist.setMoratlity("Yes");
				}
				datalist.setStateName(resultset.getString(9));
				datalist.setDistrictName(resultset.getString(10));
				datalist.setHospitalName(resultset.getString(11));
				datalist.setHospitalCode(resultset.getString(12));
				datalist.setClaimid(resultset.getString(13));
				datalist.setAuthorizedcode(resultset.getString(14));
				datalist.setCaseno(resultset.getString(15));
				datalist.setTransactiondetailsid(resultset.getString(16));
				datalist.setClaimno(resultset.getString(17));
				datalist.setHospitlcaseno(resultset.getString(18));
				detaisl.add(datalist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultset != null) {
					resultset.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return detaisl;
	}

	@Override
	public List<Object> getUrnWiseBlockDateData(Long distrctCode, Long userid, Date fromDate, Date toDate) {
		ResultSet rsblock = null;
		List<Object> blocklist = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_URNWISE_AMT_UTLIZE")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCK_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GP_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID_LIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_DIST_CODE", distrctCode);
			storedProcedureQuery.setParameter("P_BLOCK_CODE", null);
			storedProcedureQuery.setParameter("P_GP_CODE", null);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", toDate);
			storedProcedureQuery.setParameter("P_DISTRICTID_LIST", null);
			storedProcedureQuery.execute();
			rsblock = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rsblock.next()) {
				UrnwiseutilizeReportbean blockdata = new UrnwiseutilizeReportbean();
				blockdata.setDistrictid(rsblock.getString(1));
				blockdata.setNameofdistrict(rsblock.getString(2));
				blockdata.setBlockid(rsblock.getString(3));
				blockdata.setBlockname(rsblock.getString(4));
				blockdata.setUrn(rsblock.getString(5));
				blockdata.setNumberofmember(rsblock.getString(6));
				blockdata.setPackagename(rsblock.getString(7));
				blockdata.setTotalamount(rsblock.getString(8));
				blocklist.add(blockdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rsblock != null) {
					rsblock.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return blocklist;
	}

	@Override
	public List<Object> getUrnWiseGpDateData(Long districtcode, Long blockcode, Date fromDate, Date todate,
			Long userid) {
		ResultSet rsgp = null;
		List<Object> gplist = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_URNWISE_AMT_UTLIZE")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCK_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GP_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID_LIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 3);
			storedProcedureQuery.setParameter("P_DIST_CODE", districtcode);
			storedProcedureQuery.setParameter("P_BLOCK_CODE", blockcode);
			storedProcedureQuery.setParameter("P_GP_CODE", null);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_DISTRICTID_LIST", null);
			storedProcedureQuery.execute();
			rsgp = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (rsgp.next()) {
				UrnwiseutilizeReportbean blockdata = new UrnwiseutilizeReportbean();
				blockdata.setDistrictid(rsgp.getString(1));
				blockdata.setNameofdistrict(rsgp.getString(2));
				blockdata.setBlockid(rsgp.getString(3));
				blockdata.setBlockname(rsgp.getString(4));
				blockdata.setGpid(rsgp.getString(5));
				blockdata.setGpname(rsgp.getString(6));
				blockdata.setUrn(rsgp.getString(7));
				blockdata.setNumberofmember(rsgp.getString(8));
				blockdata.setPackagename(rsgp.getString(9));
				blockdata.setTotalamount(rsgp.getString(10));
				gplist.add(blockdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
			;
		} finally {
			try {
				if (rsgp != null) {
					rsgp.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return gplist;
	}

	@Override
	public List<Object> getUrnWisevillageData(Long districtcode, Long blockcode, Long gpcode, Date fromDate,
			Date todate, Long userid) {
		ResultSet villagegp = null;
		List<Object> villagelist = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_URNWISE_AMT_UTLIZE")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCK_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GP_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID_LIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 4);
			storedProcedureQuery.setParameter("P_DIST_CODE", districtcode);
			storedProcedureQuery.setParameter("P_BLOCK_CODE", blockcode);
			storedProcedureQuery.setParameter("P_GP_CODE", null);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_DISTRICTID_LIST", null);
			storedProcedureQuery.execute();
			villagegp = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (villagegp.next()) {
				UrnwiseutilizeReportbean villagedata = new UrnwiseutilizeReportbean();
				villagedata.setDistrictid(villagegp.getString(1));
				villagedata.setNameofdistrict(villagegp.getString(2));
				villagedata.setBlockid(villagegp.getString(3));
				villagedata.setBlockname(villagegp.getString(4));
				villagedata.setGpid(villagegp.getString(5));
				villagedata.setGpname(villagegp.getString(6));
				villagedata.setVillageid(villagegp.getString(7));
				villagedata.setVillagename(villagegp.getString(8));
				villagedata.setUrn(villagegp.getString(9));
				villagedata.setNameofhof(villagegp.getString(10));
				villagedata.setMobileno(villagegp.getString(11) != null ? villagegp.getString(11) : "N/A");
				villagedata.setNumberofmember(villagegp.getString(12));
				villagedata.setPackagename(villagegp.getString(13));
				villagedata.setTotalamount(villagegp.getString(14));
				villagelist.add(villagedata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (villagegp != null) {
					villagegp.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return villagelist;
	}

	@Override
	public List<Object> getPatienttreatedinoutsideodishareportforblockData(Long districtcode, Date fromDate,
			Date todate, Long userId) {
		ResultSet outsidedblockistrs = null;
		List<Object> outsidedblockist = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_TREATED_OUTSIDE_ODISHA_REPORT")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCK_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GP_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID_LIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_DIST_CODE", districtcode);
			storedProcedureQuery.setParameter("P_BLOCK_CODE", null);
			storedProcedureQuery.setParameter("P_GP_CODE", null);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_DISTRICTID_LIST", null);
			storedProcedureQuery.execute();
			outsidedblockistrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (outsidedblockistrs.next()) {
				OutSideTreatmnetbean outsideblockdata = new OutSideTreatmnetbean();
				outsideblockdata.setDistrictid(outsidedblockistrs.getString(1));
				outsideblockdata.setDistrictname(outsidedblockistrs.getString(2));
				outsideblockdata.setBlockid(outsidedblockistrs.getString(3));
				outsideblockdata.setBlockname(outsidedblockistrs.getString(4));
				outsideblockdata.setUrn(outsidedblockistrs.getString(5));
				outsideblockdata.setNoofmembers(outsidedblockistrs.getString(6));
				outsideblockdata.setNofopackages(outsidedblockistrs.getString(7));
				outsideblockdata.setAmount(outsidedblockistrs.getString(8));
				outsidedblockist.add(outsideblockdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outsidedblockistrs != null) {
					outsidedblockistrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return outsidedblockist;
	}

	@Override
	public List<Object> getPatienttreatedinoutsideodishareportforPanchayatkData(Long districtcode, Long blockcode,
			Date fromDate, Date todate, Long userId) {
		ResultSet outsidedpanchayatListrs = null;
		List<Object> outsidedpanchayatList = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_TREATED_OUTSIDE_ODISHA_REPORT")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCK_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GP_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID_LIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 3);
			storedProcedureQuery.setParameter("P_DIST_CODE", districtcode);
			storedProcedureQuery.setParameter("P_BLOCK_CODE", blockcode);
			storedProcedureQuery.setParameter("P_GP_CODE", null);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_DISTRICTID_LIST", null);
			storedProcedureQuery.execute();
			outsidedpanchayatListrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (outsidedpanchayatListrs.next()) {
				OutSideTreatmnetbean outsidepanchayatdata = new OutSideTreatmnetbean();
				outsidepanchayatdata.setDistrictid(outsidedpanchayatListrs.getString(1));
				outsidepanchayatdata.setDistrictname(outsidedpanchayatListrs.getString(2));
				outsidepanchayatdata.setBlockid(outsidedpanchayatListrs.getString(3));
				outsidepanchayatdata.setBlockname(outsidedpanchayatListrs.getString(4));
				outsidepanchayatdata.setGpid(outsidedpanchayatListrs.getString(5));
				outsidepanchayatdata.setGpname(outsidedpanchayatListrs.getString(6));
				outsidepanchayatdata.setUrn(outsidedpanchayatListrs.getString(7));
				outsidepanchayatdata.setNoofmembers(outsidedpanchayatListrs.getString(8));
				outsidepanchayatdata.setNofopackages(outsidedpanchayatListrs.getString(9));
				outsidepanchayatdata.setAmount(outsidedpanchayatListrs.getString(10));
				outsidedpanchayatList.add(outsidepanchayatdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outsidedpanchayatListrs != null) {
					outsidedpanchayatListrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return outsidedpanchayatList;
	}

	@Override
	public List<Object> getPatienttreatedinoutsideodishareportforVillageData(Long districtcode, Long blockcode,
			Date fromDate, Date todate, Long userId, Long gpcode) {
		ResultSet outsidedVillageListrs = null;
		List<Object> outsidedVillageList = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PATIENT_TREATED_OUTSIDE_ODISHA_REPORT")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DIST_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_BLOCK_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_GP_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROM_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TO_DATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTID_LIST", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 4);
			storedProcedureQuery.setParameter("P_DIST_CODE", districtcode);
			storedProcedureQuery.setParameter("P_BLOCK_CODE", blockcode);
			storedProcedureQuery.setParameter("P_GP_CODE", gpcode);
			storedProcedureQuery.setParameter("P_FROM_DATE", fromDate);
			storedProcedureQuery.setParameter("P_TO_DATE", todate);
			storedProcedureQuery.setParameter("P_DISTRICTID_LIST", null);
			storedProcedureQuery.execute();
			outsidedVillageListrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (outsidedVillageListrs.next()) {
				OutSideTreatmnetbean outsideVillagedata = new OutSideTreatmnetbean();
				outsideVillagedata.setDistrictid(outsidedVillageListrs.getString(1));
				outsideVillagedata.setDistrictname(outsidedVillageListrs.getString(2));
				outsideVillagedata.setBlockid(outsidedVillageListrs.getString(3));
				outsideVillagedata.setBlockname(outsidedVillageListrs.getString(4));
				outsideVillagedata.setGpid(outsidedVillageListrs.getString(5));
				outsideVillagedata.setGpname(outsidedVillageListrs.getString(6));
				outsideVillagedata.setVillageid(outsidedVillageListrs.getString(7));
				outsideVillagedata.setVillagename(outsidedVillageListrs.getString(8));
				outsideVillagedata.setUrn(outsidedVillageListrs.getString(9));
				outsideVillagedata.setPatientname(outsidedVillageListrs.getString(10));
				outsideVillagedata.setHospitalcode(outsidedVillageListrs.getString(11));
				outsideVillagedata.setHospitalname(outsidedVillageListrs.getString(12));
				outsideVillagedata.setNoofmembers(outsidedVillageListrs.getString(13));
				outsideVillagedata.setNofopackages(outsidedVillageListrs.getString(14));
				outsideVillagedata.setAmount(outsidedVillageListrs.getString(15));
				outsidedVillageList.add(outsideVillagedata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outsidedVillageListrs != null) {
					outsidedVillageListrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return outsidedVillageList;
	}

	@Override
	public List<Object> getgetCPDProcessingPaymentDetailsData(Long userId, Long year, Long month, String hospitalcode,
			String statecode, String districtcode, String status) {
		ResultSet cpdpaymentrs = null;
		List<Object> cpdpaymentrsList = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("usp_cpd_payment_calculation_modified_details")
					.registerStoredProcedureParameter("P_ASSIGNEDCPD", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_YEAR", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPTLCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_flag", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_userid", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ASSIGNEDCPD", userId);
			storedProcedureQuery.setParameter("P_YEAR", year);
			storedProcedureQuery.setParameter("P_MONTH", month);
			storedProcedureQuery.setParameter("P_HOSPTLCODE", hospitalcode);
			storedProcedureQuery.setParameter("P_STATECODE", statecode);
			storedProcedureQuery.setParameter("P_DISTRICTCODE", districtcode);
			storedProcedureQuery.setParameter("p_flag", status);
			storedProcedureQuery.setParameter("p_userid", userId);
			storedProcedureQuery.execute();
			cpdpaymentrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (cpdpaymentrs.next()) {
				CpdPaymentDetailsbean data = new CpdPaymentDetailsbean();
				data.setClaimnumber(cpdpaymentrs.getString(1) != null ? cpdpaymentrs.getString(1) : "N/A");
				data.setInvoicenumber(cpdpaymentrs.getString(2) != null ? cpdpaymentrs.getString(2) : "N/A");
				data.setUrn(cpdpaymentrs.getString(3) != null ? cpdpaymentrs.getString(3) : "N/A");
				data.setPatientname(cpdpaymentrs.getString(4) != null ? cpdpaymentrs.getString(4) : "N/A");
				data.setPackagecode(cpdpaymentrs.getString(5) != null ? cpdpaymentrs.getString(5) : "N/A");
				data.setActualdateofdischarge(DateFormat.FormatToDateString(cpdpaymentrs.getString(6)));
				data.setActualdateofadmission(DateFormat.FormatToDateString(cpdpaymentrs.getString(7)));
				data.setActionon(cpdpaymentrs.getString(8) != null ? cpdpaymentrs.getString(8) : "N/A");
				data.setCreatedon(cpdpaymentrs.getString(9) != null ? cpdpaymentrs.getString(9) : "N/A");
				data.setTotalamountclaimed(cpdpaymentrs.getString(10) != null ? cpdpaymentrs.getString(10) : "N/A");
				data.setApprovedamount(cpdpaymentrs.getString(11) != null ? cpdpaymentrs.getString(11) : "N/A");
				data.setCliamid(cpdpaymentrs.getString(12));
				cpdpaymentrsList.add(data);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cpdpaymentrs != null) {
					cpdpaymentrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return cpdpaymentrsList;
	}

	@Override
	public List<Object> getpackagedetailsDetails() {
		ResultSet packrs = null;
		List<Object> packlist = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_CALCULATOR")
					.registerStoredProcedureParameter("p_actioncode", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", Long.parseLong("1"));
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", null);
			storedProcedureQuery.setParameter("P_HOSPITALCATEGORYID", null);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", null);
			storedProcedureQuery.execute();
			packrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (packrs.next()) {
				packagebean packadata = new packagebean();
				packadata.setId(packrs.getLong(1));
				packadata.setPackageheadercode(packrs.getString(2));
				packadata.setPackageheader(packrs.getString(3));
				packadata.setPackageconcat(packrs.getString(4));
				packlist.add(packadata);
			}
		} catch (Exception e) {
			e.printStackTrace();
			;
		} finally {
			try {
				if (packrs != null) {
					packrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				;
			}
		}
		return packlist;
	}

	@Override
	public List<Object> getpackagedetailsRecordList(String packlist, String userid, String statedata,
			String districtdata, String hospitaldata, String hospitaltype) {
		ResultSet listrs = null;
		List<Object> Data = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_CALCULATOR")
					.registerStoredProcedureParameter("p_actioncode", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", Long.parseLong("2"));
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", packlist);
			storedProcedureQuery.setParameter("P_HOSPITALCATEGORYID", Long.parseLong(hospitaltype));
			storedProcedureQuery.setParameter("P_PROCEDURECODE", null);
			storedProcedureQuery.execute();
			listrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (listrs.next()) {
				packagebean record = new packagebean();
				record.setPackageheadercode(listrs.getString(1));
				record.setPackageheadername(listrs.getString(2));
				record.setProcedurecode(listrs.getString(3));
				record.setAmount(listrs.getString(4));
				record.setImplantexist(listrs.getString(5));
				Data.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (listrs != null) {
					listrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return Data;
	}

	@Override
	public List<Object> getpackagedetailsForImpantCalculationList(String userid, String procedurecode, String statedata,
			String districtdata, String hospitaldata, String hospitaltype) {
		ResultSet implntrs = null;
		List<Object> implantData = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_CALCULATOR")
					.registerStoredProcedureParameter("p_actioncode", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", Long.parseLong("3"));
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", null);
			storedProcedureQuery.setParameter("P_HOSPITALCATEGORYID", null);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", procedurecode);
			storedProcedureQuery.execute();
			implntrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (implntrs.next()) {
				packagebean imprecord = new packagebean();
				imprecord.setProcedurecode(implntrs.getString(1));
				imprecord.setImplantcode(implntrs.getString(2));
				imprecord.setImplantname(implntrs.getString(3));
				imprecord.setMaximumunit(implntrs.getString(4));
				imprecord.setUnitprice(implntrs.getString(5));
				imprecord.setPriceeditable(implntrs.getString(6));
				imprecord.setUniteditable(implntrs.getString(7));
				imprecord.setIsCheckedImplant(false);
				implantData.add(imprecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (implntrs != null) {
					implntrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return implantData;
	}

	@Override
	public List<Object> getpackagedetailsForHedCalculationList() {
		ResultSet hedrs = null;
		List<Object> hedData = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_CALCULATOR")
					.registerStoredProcedureParameter("p_actioncode", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCATEGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_actioncode", Long.parseLong("4"));
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", null);
			storedProcedureQuery.setParameter("P_HOSPITALCATEGORYID", null);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", null);
			storedProcedureQuery.execute();
			hedrs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (hedrs.next()) {
				packagebean hedrecord = new packagebean();
				hedrecord.setHedcode(hedrs.getString(1));
				hedrecord.setHedname(hedrs.getString(2));
				hedrecord.setPrice(hedrs.getLong(3));
				hedrecord.setMaximumunit(hedrs.getString(4));
				hedrecord.setUnit(hedrs.getString(5));
				hedrecord.setRecomendeddose(hedrs.getString(6));
				hedrecord.setPriceeditable(hedrs.getString(7));
				hedrecord.setUniteditable(hedrs.getString(8));
				hedData.add(hedrecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (hedrs != null) {
					hedrs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return hedData;
	}

	@Override
	public String getCPDPaymentList(Long month, Long year) {
		ResultSet allCPDList = null;
		ResultSet paidCPDList = null;
		List<Object> Data = new ArrayList<Object>();
		JSONObject details = new JSONObject();
		JSONObject jsonObject = null;
		JSONObject jsonObject1 = null;
		JSONArray allCPDArray = new JSONArray();
		JSONArray paidCPDArray = new JSONArray();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_POSTPAYMENT_CALCULATION")
					.registerStoredProcedureParameter("P_YEAR", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MONTH", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT1", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_YEAR", year);
			storedProcedureQuery.setParameter("P_MONTH", month);
			storedProcedureQuery.execute();
			allCPDList = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			paidCPDList = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT1");
			while (allCPDList.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("cpdUserId", allCPDList.getLong(1));
				jsonObject.put("cpdName", allCPDList.getString(2));
				jsonObject.put("finalAmount", allCPDList.getLong(5));
				jsonObject.put("accountNum", allCPDList.getString(7));
				jsonObject.put("ifscCode", allCPDList.getString(8));
				jsonObject.put("bankName", allCPDList.getString(9));
				allCPDArray.put(jsonObject);
			}
			while (paidCPDList.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("postPaymentId", paidCPDList.getLong(1));
				jsonObject1.put("cpdUserId", paidCPDList.getLong(2));
				paidCPDArray.put(jsonObject1);
			}
			details.put("allCPDArray", allCPDArray);
			details.put("paidCPDArray", paidCPDArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (allCPDList != null) {
					allCPDList.close();
				}
				if (paidCPDList != null) {
					paidCPDList.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}

	@Override
	public Response updatePostPayment(PostPaymentRequest requestBean) throws Exception {
		Response response = new Response();
		String returnObj = null;
		Connection con = null;
		CallableStatement st = null;
		StructDescriptor structDescriptor = null;
		Long currectUser = util.getCurrentUser();
		try {
//			Map<String, Object>[] arrayFromList = listToArray(requestBean.getCpdList()); 
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);

			STRUCT[] quesbean = new STRUCT[requestBean.getCpdList().size()];
			int i = 0;
			for (Map<String, Object> obj : requestBean.getCpdList()) {
				structDescriptor = new StructDescriptor("CPD_POSTPAYMENT", con);
				Object[] ObjArr = { obj.get("CPD_USERID"), obj.get("FINAL_AMOUNT") };
				quesbean[i] = new STRUCT(structDescriptor, con, ObjArr);
				i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_CPD_POSTPAYMENT_LIST", con);
			ARRAY array_to_pass = new ARRAY(des, con, quesbean);
			st = con.prepareCall("call USP_CPD_POSTPAYMENT_SUBMIT(?,?,?,?,?,?,?,?,?,?)");
			st.setArray(1, array_to_pass);
			st.setLong(2, requestBean.getMonth());
			st.setLong(3, requestBean.getYear());
			st.setDouble(4, requestBean.getTotalPaidAmount());
			st.setLong(5, requestBean.getBankModeId());
			st.setString(6, requestBean.getTypeNumber());
			st.setLong(7, requestBean.getBankId());
			st.setDate(8, requestBean.getCurrentDate());
			st.setLong(9, currectUser);
			st.registerOutParameter(10, Types.INTEGER);
			st.execute();
			Integer returnValue = ((OracleCallableStatement) st).getInt(10);

			if (returnValue == 1) {
				response.setStatus("success");
				response.setMessage("Updated Successfully");
			}else if (returnValue == 0) {
				response.setStatus("exists");
				response.setMessage("Final amount could not be 0");
			} else {
				response.setMessage("Some error happen");
				response.setStatus("Failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("failed");
			throw e;
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return response;
	}

	@Override
	public java.lang.String cpdPostPaymentUpdationView(Long cpdUserId, Long year) {
		ResultSet paymentList = null;
		JSONObject details = new JSONObject();
		JSONObject jsonObject = null;
		JSONArray paymentListArray = new JSONArray();
		Long userId = util.getCurrentUser();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_CPD_POSTPAYMENT_UPDATE_VIEW_RPT")
					.registerStoredProcedureParameter("P_YEAR", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPD_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_YEAR", year);
			storedProcedureQuery.setParameter("P_CPD_USERID", cpdUserId);
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.execute();
			paymentList = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (paymentList.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("cpdUserId", paymentList.getLong(1));
				jsonObject.put("cpdName", paymentList.getString(2));
				jsonObject.put("paymentMonth", paymentList.getString(3));
				jsonObject.put("finalAmount", paymentList.getLong(4));
				jsonObject.put("paymentType", paymentList.getString(5));
				jsonObject.put("paymentDate", paymentList.getString(6));
				jsonObject.put("accountNum", paymentList.getString(7));
				jsonObject.put("ifscCode", paymentList.getString(8));
				jsonObject.put("paymentInfo", paymentList.getString(10));
				jsonObject.put("bankName", paymentList.getString(11));
				paymentListArray.put(jsonObject);
			}

			details.put("paymentListArray", paymentListArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (paymentList != null) {
					paymentList.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return details.toString();
	}
}
