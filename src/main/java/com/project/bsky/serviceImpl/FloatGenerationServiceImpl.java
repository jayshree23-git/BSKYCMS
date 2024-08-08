package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.FloatExcelBean;
import com.project.bsky.bean.FloatReportBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.oldblocknewdischargebean;
import com.project.bsky.service.FloatGenerationService;
import com.project.bsky.util.FileUtil;

/**
 * @author ronauk.maharana
 *
 */
@Service
public class FloatGenerationServiceImpl implements FloatGenerationService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Override
	public List<Object> getFloatReport(FloatReportBean requestBean) {
		List<Object> floatList = new ArrayList<Object>();
		DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		ResultSet rs = null;
		ResultSet rs1 = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		if (requestBean.getSearchtype() != null && requestBean.getSearchtype() == 1) {
			try {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("usp_float_get_details")
						.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
				storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
				storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
				storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
				storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
				storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
				storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
				storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
				storedProcedureQuery.execute();
				rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
				int i = 1;
				while (rs.next()) {
					List<Object> floatObj = new ArrayList<Object>();
					floatObj.add(String.valueOf(i++));
					floatObj.add(rs.getString(2) != null ? rs.getString(2) : "-NA-");
					floatObj.add(rs.getString(3) != null ? rs.getString(3) : "-NA-");
					floatObj.add(rs.getString(4) != null ? rs.getString(4) : "-NA-");
					floatObj.add(rs.getString(5) != null ? rs.getString(5) : "-NA-");
					floatObj.add(rs.getString(6) != null ? rs.getString(6) : "-NA-");
					floatObj.add(rs.getString(7) != null ? rs.getString(7) : "-NA-");
					floatObj.add(rs.getString(26) != null ? rs.getString(26) : "-NA-");
					floatObj.add(rs.getString(8) != null ? rs.getString(8) : "-NA-");
					if (rs.getString(9).equalsIgnoreCase("M")) {
						floatObj.add("Male");
					} else if (rs.getString(9).equalsIgnoreCase("F")) {
						floatObj.add("Female");
					} else if (rs.getString(9).equalsIgnoreCase("O")) {
						floatObj.add("Others");
					} else {
						floatObj.add("-NA-");
					}
					floatObj.add(rs.getString(10) != null ? rs.getString(10) : "-NA-");
					floatObj.add(rs.getString(11) != null ? rs.getString(11) : "-NA-");
					floatObj.add(rs.getString(24) != null ? rs.getString(24).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs.getString(24))) : "-NA-");

					floatObj.add(rs.getString(12) != null ? rs.getString(12) : "-NA-");
					Timestamp ACTUALDATEOFADMISSION = rs.getTimestamp(13);
					if (ACTUALDATEOFADMISSION != null) {
						floatObj.add(f.format(new Date(ACTUALDATEOFADMISSION.getTime())));
					}
					Timestamp ACTUALDATEOFDISCHARGE = rs.getTimestamp(14);
					if (ACTUALDATEOFDISCHARGE != null) {
						floatObj.add(f.format(new Date(ACTUALDATEOFDISCHARGE.getTime())));
					}
					floatObj.add(rs.getString(15) != null ? rs.getString(15) : "-NA-");
					floatObj.add(rs.getString(25) != null ? rs.getString(25) : "-NA-");
					floatObj.add(rs.getString(16) != null ? rs.getString(16).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs.getString(16))) : "-NA-");
					floatObj.add(rs.getString(17) == null || rs.getString(17).equalsIgnoreCase("")
							|| rs.getString(17).equalsIgnoreCase(" ") || rs.getString(17).contains(" ") ? "-NA-"
									: rs.getString(17));

					floatObj.add(rs.getString(18) != null ? rs.getString(18) : "-NA-");
					floatObj.add(rs.getString(19) != null ? rs.getString(19) : "-NA-");
					floatObj.add(rs.getString(20) != null ? rs.getString(20).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs.getString(20))) : "-NA-");
					floatObj.add(rs.getString(21) != null ? rs.getString(21) : "-NA-");
					floatObj.add(rs.getString(22) != null ? rs.getString(22) : "-NA-");
					floatObj.add(rs.getString(23) != null ? rs.getString(23).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs.getString(23))) : "-NA-");
					floatList.add(floatObj);
				}

			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e2) {
					logger.error(ExceptionUtils.getStackTrace(e2));
				}
			}
			// 1.0 BLOCK DATA FLOAT GENERATION DETAILS(MODIFICATION IN 01-AUG-2023)
		} else if (requestBean.getSearchtype() != null && requestBean.getSearchtype() == 2) {
			// System.out.println("INSIDE THE 1.0 BLOCKDATA FLOAT GENERATION DETAILS");
			try {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("usp_float_get_details_OLD")
						.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
				storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
				storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
				storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
				storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
				storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
				storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
				storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
				storedProcedureQuery.execute();

				rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
				int i = 1;
				while (rs1.next()) {
					List<Object> floatObj = new ArrayList<Object>();
					floatObj.add(String.valueOf(i++));
					floatObj.add(rs1.getString(2) != null ? rs1.getString(2) : "-NA-");
					floatObj.add(rs1.getString(3) != null ? rs1.getString(3) : "-NA-");
					floatObj.add(rs1.getString(4) != null ? rs1.getString(4) : "-NA-");
					floatObj.add(rs1.getString(5) != null ? rs1.getString(5) : "-NA-");
					floatObj.add(rs1.getString(6) != null ? rs1.getString(6) : "-NA-");
					floatObj.add(rs1.getString(7) != null ? rs1.getString(7) : "-NA-");
					floatObj.add(rs1.getString(26) != null ? rs1.getString(26) : "-NA-");
					floatObj.add(rs1.getString(8) != null ? rs1.getString(8) : "-NA-");
					if (rs1.getString(9).equalsIgnoreCase("M")) {
						floatObj.add("Male");
					} else if (rs1.getString(9).equalsIgnoreCase("F")) {
						floatObj.add("Female");
					} else if (rs1.getString(9).equalsIgnoreCase("O")) {
						floatObj.add("Others");
					} else {
						floatObj.add("-NA-");
					}
					floatObj.add(rs1.getString(10) != null ? rs1.getString(10) : "-NA-");
					floatObj.add(rs1.getString(11) != null ? rs1.getString(11) : "-NA-");
					floatObj.add(rs1.getString(24) != null ? rs1.getString(24).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs1.getString(24))) : "-NA-");

					floatObj.add(rs1.getString(12) != null ? rs1.getString(12) : "-NA-");
					Timestamp ACTUALDATEOFADMISSION = rs1.getTimestamp(13);
					if (ACTUALDATEOFADMISSION != null) {
						floatObj.add(f.format(new Date(ACTUALDATEOFADMISSION.getTime())));
					}
					Timestamp ACTUALDATEOFDISCHARGE = rs1.getTimestamp(14);
					if (ACTUALDATEOFDISCHARGE != null) {
						floatObj.add(f.format(new Date(ACTUALDATEOFDISCHARGE.getTime())));
					}
					floatObj.add(rs1.getString(15) != null ? rs1.getString(15) : "-NA-");
					floatObj.add(rs1.getString(25) != null ? rs1.getString(25) : "-NA-");
					floatObj.add(rs1.getString(16) != null ? rs1.getString(16).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs1.getString(16))) : "-NA-");
					floatObj.add(rs1.getString(17) == null || rs1.getString(17).equalsIgnoreCase("")
							|| rs1.getString(17).equalsIgnoreCase(" ") || rs1.getString(17).contains(" ") ? "-NA-"
									: rs1.getString(17));

					floatObj.add(rs1.getString(18) != null ? rs1.getString(18) : "-NA-");
					floatObj.add(rs1.getString(19) != null ? rs1.getString(19) : "-NA-");
					floatObj.add(rs1.getString(20) != null ? rs1.getString(20).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs1.getString(20))) : "-NA-");
					floatObj.add(rs1.getString(21) != null ? rs1.getString(21) : "-NA-");
					floatObj.add(rs1.getString(22) != null ? rs1.getString(22) : "-NA-");
					floatObj.add(rs1.getString(23) != null ? rs1.getString(23).equalsIgnoreCase("0") ? "0.00"
							: formatter.format(Double.parseDouble(rs1.getString(23))) : "-NA-");
					floatList.add(floatObj);
				}

			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (rs1 != null) {
						rs1.close();
					}
				} catch (Exception e2) {
					logger.error(ExceptionUtils.getStackTrace(e2));
				}
			}
		}
		return floatList;
	}

	@Override
	public JSONObject getSummary(FloatReportBean requestBean) {

		JSONObject floatObj = new JSONObject();
		ResultSet rs = null, rs1 = null;
		ResultSet rS2 = null, rs3 = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		if (requestBean.getSearchtype() != null && requestBean.getSearchtype() == 1) {
			try {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_RPT_CLAIM_COUNT")
						.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
						.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
				storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
				storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
				storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalId());
				storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
				storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
				storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
				storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
				storedProcedureQuery.execute();

				rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
				while (rs.next()) {
					floatObj.put("pendatsna", rs.getInt(1));
					floatObj.put("pendatcpd", rs.getInt(2));
					floatObj.put("pendathsptl", rs.getInt(3));
					floatObj.put("pendatdc", rs.getInt(4));
					floatObj.put("TotalDischarged", rs.getInt(5));
					floatObj.put("totalclaimrasied", rs.getInt(6));
					floatObj.put("nonUploading", rs.getInt(7));
					floatObj.put("snaapproved", rs.getInt(8));
					floatObj.put("snarejected", rs.getInt(9));
					floatObj.put("cpdQuery", rs.getInt(10));
					floatObj.put("pendatcpdRstl", rs.getInt(11));
					floatObj.put("pendatcpdRvrt", rs.getInt(12));
					floatObj.put("pendathsptlwithin7", rs.getInt(13));
					floatObj.put("pendathsptlafter7", rs.getInt(14));
					floatObj.put("cpdQuerywithin7", rs.getInt(15));
					floatObj.put("cpdQueryafter7", rs.getInt(16));
					floatObj.put("nonUploadingInit", rs.getInt(17));
					floatObj.put("paymentUnFreezed", rs.getInt(18));
					floatObj.put("paymentFreezed", rs.getInt(19));
				}

				rs1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
				while (rs1.next()) {
					floatObj.put("tcpdApproved", rs1.getInt(1));
					floatObj.put("snaActionOfCpdAprvd", rs1.getInt(2));
					String percent1 = rs1.getString(3);
					if (percent1 != null && percent1.contains(" .00")) {
						percent1 = "0";
					}
					if (percent1.startsWith(".")) {
						percent1 = "0" + percent1;
					}
					floatObj.put("percent1", percent1);
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (rs1 != null) {
						rs1.close();
					}
				} catch (Exception e2) {
					logger.error(ExceptionUtils.getStackTrace(e2));
				}
			}
			// 1.0 BLOCK DATA FLOAT GENERATION DETAILS(MODIFICATION IN 01-AUG-2023)
		} else if (requestBean.getSearchtype() != null && requestBean.getSearchtype() == 2) {
			// System.out.println("INSIDE THE 1.0 BLOCKDATA COUNT DETAILS");
			try {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_RPT_CLAIM_COUNT_old")
						.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_from_date", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_to_date", Date.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR)
						.registerStoredProcedureParameter("p_result_set", void.class, ParameterMode.REF_CURSOR);

				storedProcedureQuery.setParameter("p_user_id", requestBean.getUserId());
				storedProcedureQuery.setParameter("p_from_date", requestBean.getFromDate());
				storedProcedureQuery.setParameter("p_to_date", requestBean.getToDate());
				storedProcedureQuery.setParameter("p_hosptlcode", requestBean.getHospitalId());
				storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
				storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
				storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
				storedProcedureQuery.execute();

				rS2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
				while (rS2.next()) {
					floatObj.put("pendatsna", rS2.getInt(1));
					floatObj.put("pendatcpd", rS2.getInt(2));
					floatObj.put("pendathsptl", rS2.getInt(3));
					floatObj.put("pendatdc", rS2.getInt(4));
					floatObj.put("TotalDischarged", rS2.getInt(5));
					floatObj.put("totalclaimrasied", rS2.getInt(6));
					floatObj.put("nonUploading", rS2.getInt(7));
					floatObj.put("snaapproved", rS2.getInt(8));
					floatObj.put("snarejected", rS2.getInt(9));
					floatObj.put("cpdQuery", rS2.getInt(10));
					floatObj.put("pendatcpdRstl", rS2.getInt(11));
					floatObj.put("pendatcpdRvrt", rS2.getInt(12));
					floatObj.put("pendathsptlwithin7", rS2.getInt(13));
					floatObj.put("pendathsptlafter7", rS2.getInt(14));
					floatObj.put("cpdQuerywithin7", rS2.getInt(15));
					floatObj.put("cpdQueryafter7", rS2.getInt(16));
					floatObj.put("nonUploadingInit", rS2.getInt(17));
					floatObj.put("paymentUnFreezed", rS2.getInt(18));
					floatObj.put("paymentFreezed", rS2.getInt(19));
				}

				rs3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_result_set");
				while (rs3.next()) {
					floatObj.put("tcpdApproved", rs3.getInt(1));
					floatObj.put("snaActionOfCpdAprvd", rs3.getInt(2));
					String percent1 = rs3.getString(3);
					if (percent1 != null && percent1.contains(" .00")) {
						percent1 = "0";
					}
					if (percent1.startsWith(".")) {
						percent1 = "0" + percent1;
					}
					floatObj.put("percent1", percent1);
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (rS2 != null) {
						rS2.close();
					}
					if (rs3 != null) {
						rs3.close();
					}
				} catch (Exception e2) {
					logger.error(ExceptionUtils.getStackTrace(e2));
				}
			}
		}

		return floatObj;
	}

	@Override
	public Response generateExcel(FloatExcelBean bean) {

		Response response = new Response();
		try {
			String date = new SimpleDateFormat("yyyyMMddhhmmssa").format(new Date());

			String filename = "BSKY_Float_Generation_Report_" + date + ".xlsx";
			String out = FileUtil.generateExcel(bean, filename);
			if (out != null) {
				bean.setFilename(filename);
				Integer res = saveReport(bean);
				if (res == 1) {
					response.setStatus("success");
					response.setMessage(filename);
				} else {
					response.setStatus("failed");
					response.setMessage(out);
				}
			} else {
				response.setStatus("failed");
				response.setMessage("Some error occured");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage(e.toString());
		}
		return response;
	}

	@Override
	public void downLoadFile(String fileCode, String userId, HttpServletResponse response) {

		try {
			FileUtil.getFloatReportFile(fileCode, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Integer saveFloatReport(MultipartFile pdf, FloatExcelBean bean) {

		try {
			String filename = FileUtil.savePdf(pdf, bean.getUserId().toString());
			bean.setFilename(filename);
			return saveReport(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public Integer saveReport(FloatExcelBean requestBean) {
		Integer result = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_LOG_DETAILS")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_created_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_search_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 0);
			storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_filename", requestBean.getFilename());
			storedProcedureQuery.setParameter("p_created_by", requestBean.getCreatedBy());
			storedProcedureQuery.setParameter("p_search_by", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();
			result = (Integer) storedProcedureQuery.getOutputParameterValue("p_msgout");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public JSONArray getGeneratedReports(FloatReportBean requestBean) {
		JSONArray floatList = new JSONArray();
		ResultSet rs = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_LOG_DETAILS")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_filename", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_created_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_search_by", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_msgout", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", 1);
			storedProcedureQuery.setParameter("p_sno", requestBean.getSnaUserId());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("p_filename", null);
			storedProcedureQuery.setParameter("p_created_by", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_search_by", null);
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("logId", rs.getInt(1));
				json.put("userId", rs.getLong(2));
				String fileName = rs.getString(3);
				json.put("fileName", fileName);
				json.put("actualDateOfDischargeFrom",
						new SimpleDateFormat("dd-MMM-yyyy").format(new Date(rs.getDate(4).getTime())));
				json.put("actualDateOfDischargeTo",
						new SimpleDateFormat("dd-MMM-yyyy").format(new Date(rs.getDate(5).getTime())));
				json.put("cpdMortality", rs.getString(6));
				json.put("stateName", rs.getString(7));
				json.put("districtName", rs.getString(8));
				json.put("hospitalName", rs.getString(9));
				json.put("createdOn",
						new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(new Date(rs.getTimestamp(10).getTime())));
				String extn = null;
				if (fileName != null) {
					extn = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
				}
				json.put("extn", extn);
				json.put("snoName", rs.getString(12));
				floatList.put(json);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return floatList;
	}

	@Override
	public JSONArray getAbstractFloatReport(FloatReportBean requestBean) {
		JSONArray floatList = new JSONArray();
		ResultSet rs = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_ABSTRACT_RPT")
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_bloceddata", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("P_bloceddata", requestBean.getSearchtype());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			while (rs.next()) {
				JSONObject floatObj = new JSONObject();
				floatObj.put("hospitalCode", rs.getString(1));
				floatObj.put("hospitalName", rs.getString(2));
				floatObj.put("stateName", rs.getString(3));
				floatObj.put("stateCode", rs.getString(4));
				floatObj.put("districtName", rs.getString(5));
				floatObj.put("districtCode", rs.getString(6));
				floatObj.put("discharged", rs.getInt(7));
				floatObj.put("claimraised", rs.getInt(8));
				floatObj.put("claimamount", rs.getDouble(9));
				floatObj.put("approved", rs.getInt(10));
				floatObj.put("snoamount", rs.getDouble(11));
				floatObj.put("rejected", rs.getInt(12));
				floatObj.put("count", rs.getInt(13));
				floatList.put(floatObj);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return floatList;
	}

	@Override
	public List<Object> getActionWiseFloatReport(FloatReportBean requestBean) {
		List<Object> floatList = new ArrayList<Object>();
		DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_FLOAT_ACTN_WS_RPT")
					.registerStoredProcedureParameter("p_flag", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_sno", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_fromdate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_todate", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hsptl_code", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_statecode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_districtcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_mortality", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY_ID", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_resultset", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_flag", requestBean.getAction());
			storedProcedureQuery.setParameter("p_sno", requestBean.getUserId());
			storedProcedureQuery.setParameter("p_fromdate", requestBean.getFromDate());
			storedProcedureQuery.setParameter("p_todate", requestBean.getToDate());
			storedProcedureQuery.setParameter("p_hsptl_code", requestBean.getHospitalId());
			storedProcedureQuery.setParameter("p_statecode", requestBean.getStateId());
			storedProcedureQuery.setParameter("p_districtcode", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("p_mortality", requestBean.getMortality());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY_ID", schemeCategoryId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_resultset");
			int i = 1;
			while (rs.next()) {
				List<Object> floatObj = new ArrayList<Object>();
				floatObj.add(String.valueOf(i++));
				floatObj.add(rs.getString(2) != null ? rs.getString(2) : "-NA-");
				floatObj.add(rs.getString(3) != null ? rs.getString(3) : "-NA-");
				floatObj.add(rs.getString(4) != null ? rs.getString(4) : "-NA-");
				floatObj.add(rs.getString(5) != null ? rs.getString(5) : "-NA-");
				floatObj.add(rs.getString(6) != null ? rs.getString(6) : "-NA-");
				floatObj.add(rs.getString(7) != null ? rs.getString(7) : "-NA-");
				floatObj.add(rs.getString(26) != null ? rs.getString(26) : "-NA-");
				floatObj.add(rs.getString(8) != null ? rs.getString(8) : "-NA-");
				if (rs.getString(9).equalsIgnoreCase("M")) {
					floatObj.add("Male");
				} else if (rs.getString(9).equalsIgnoreCase("F")) {
					floatObj.add("Female");
				} else if (rs.getString(9).equalsIgnoreCase("O")) {
					floatObj.add("Others");
				} else {
					floatObj.add("-NA-");
				}
				floatObj.add(rs.getString(10) != null ? rs.getString(10) : "-NA-");
				floatObj.add(rs.getString(11) != null ? rs.getString(11) : "-NA-");
				floatObj.add(rs.getString(24) != null ? rs.getString(24).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(24))) : "-NA-");

				floatObj.add(rs.getString(12) != null ? rs.getString(12) : "-NA-");
				Timestamp ACTUALDATEOFADMISSION = rs.getTimestamp(13);
				if (ACTUALDATEOFADMISSION != null) {
					floatObj.add(f.format(new Date(ACTUALDATEOFADMISSION.getTime())));
				}
				Timestamp ACTUALDATEOFDISCHARGE = rs.getTimestamp(14);
				if (ACTUALDATEOFDISCHARGE != null) {
					floatObj.add(f.format(new Date(ACTUALDATEOFDISCHARGE.getTime())));
				}
				floatObj.add(rs.getString(15) != null ? rs.getString(15) : "-NA-");
				floatObj.add(rs.getString(25) != null ? rs.getString(25) : "-NA-");
				floatObj.add(rs.getString(16) != null ? rs.getString(16).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(16))) : "-NA-");
				floatObj.add(rs.getString(17) == null || rs.getString(17).equalsIgnoreCase("")
						|| rs.getString(17).equalsIgnoreCase(" ") || rs.getString(17).contains(" ") ? "-NA-"
								: rs.getString(17));

				floatObj.add(rs.getString(18) != null ? rs.getString(18) : "-NA-");
				floatObj.add(rs.getString(19) != null ? rs.getString(19) : "-NA-");
				floatObj.add(rs.getString(20) != null ? rs.getString(20).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(20))) : "-NA-");
				floatObj.add(rs.getString(21) != null ? rs.getString(21) : "-NA-");
				floatObj.add(rs.getString(22) != null ? rs.getString(22) : "-NA-");
				floatObj.add(rs.getString(23) != null ? rs.getString(23).equalsIgnoreCase("0") ? "0.00"
						: formatter.format(Double.parseDouble(rs.getString(23))) : "-NA-");
				floatList.add(floatObj);
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return floatList;
	}

	@Override
	public Map<String, List<Object>> getoldblocknewdischargelist(oldblocknewdischargebean requestBean) {
		List<Object> oldnewList = new ArrayList<Object>();
		List<Object> oldnewList1 = new ArrayList<Object>();
	    Map<String, List<Object>> resultMap = new HashMap<>();
		ResultSet rsultset = null;
		ResultSet rsultset1 = null;
		int schemeCategoryId;
		if (requestBean.getSchemecategoryid() != null && !requestBean.getSchemecategoryid().equals("")) {
			schemeCategoryId = Integer.parseInt(requestBean.getSchemecategoryid());
		} else {
			schemeCategoryId = 0;
		}
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_GET_OLD_BLOCK_NEW_DISC_RPT")
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CPDMORTALITY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
			        .registerStoredProcedureParameter("P_MSG_OUT1", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", requestBean.getFromDate());
			storedProcedureQuery.setParameter("P_TODATE", requestBean.getToDate());
			storedProcedureQuery.setParameter("P_CPDMORTALITY", requestBean.getMortality());
			storedProcedureQuery.setParameter("P_STATE", requestBean.getStateId());
			storedProcedureQuery.setParameter("P_DISTRICT", requestBean.getDistrictId());
			storedProcedureQuery.setParameter("P_HOSPITAL", requestBean.getHospitalId());
			storedProcedureQuery.execute();
			rsultset = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			rsultset1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT1");
			while (rsultset.next()) {
				List<Object> listObj = new ArrayList<>();
				int columnCount = rsultset.getMetaData().getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String value = rsultset.getString(i);
					listObj.add(value != null ? value : "-NA-");
				}
				oldnewList.add(listObj);
			}
			rsultset.close();
			while (rsultset1.next()) {
				List<Object> listObj1 = new ArrayList<>();
				int columnCount1 = rsultset1.getMetaData().getColumnCount();
				for (int i = 1; i <= columnCount1; i++) {
					String value = rsultset1.getString(i);
					listObj1.add(value != null ? value : "-NA-");
				}
				oldnewList1.add(listObj1);
			}
			rsultset1.close();
		    resultMap.put("oldnewList", oldnewList);
		    resultMap.put("oldnewList1", oldnewList1);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (rsultset != null) {
					rsultset.close();
				}
				if (rsultset1 != null) {
					rsultset1.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return resultMap;
	}

}
