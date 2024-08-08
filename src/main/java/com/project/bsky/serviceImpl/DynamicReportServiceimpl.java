/**
 * 
 */
package com.project.bsky.serviceImpl;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.DynamicReportConfiguration;
import com.project.bsky.model.DynamicReportDetailsmodel;
import com.project.bsky.model.TblMeAction;
import com.project.bsky.model.TblMeActionLog;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.DynamicReportConfigRepository;
import com.project.bsky.repository.TblMeActionLogReository;
import com.project.bsky.repository.TblMeActionRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.DynamicReportService;

import net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy.Simple;

/**
 * @author rajendra.sahoo
 *
 */
@SuppressWarnings("unused")
@Service
public class DynamicReportServiceimpl implements DynamicReportService {

	@Autowired
	private DynamicReportConfigRepository dynamicreportconfigrepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TblMeActionRepository meactionlogrepo;

	@Autowired
	private TblMeActionLogReository actionlogrepo;

	@Autowired
	private UserDetailsRepository userrepo;

	@Autowired
	private Logger logger;

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @author rajendra.sahoo This method is user for Submit Dynamic Configuration
	 *         Report
	 *
	 */
	@Override
	public Response SubmitdunamicConfiguration(DynamicReportConfiguration dynamicReport) {
		Response response = new Response();
		try {
			String s = dynamicReport.getPackagecode();
			if (s != null && s != "") {
				dynamicReport.setPackagecode(s.substring(0, s.length() - 1));
			}
			if (dynamicReport.getAgecondition() == "") {
				dynamicReport.setAgecondition(">");
			}
			dynamicReport.setStatus(0);
			dynamicReport.setCreatedOn(Calendar.getInstance().getTime());
			dynamicreportconfigrepo.save(dynamicReport);
			response.setStatus("200");
			response.setMessage("Record Submitted Sucessfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get Dynamic Configuration
	 *         Report List
	 *
	 */
	@Override
	public List<DynamicReportConfiguration> getdynamicconfigurationlist() {
		List<DynamicReportConfiguration> list = new ArrayList<DynamicReportConfiguration>();
		try {
			list = dynamicreportconfigrepo.getalldata();
			for (DynamicReportConfiguration data : list) {
				data.setPackagename(data.getPackagecode() != null ? data.getPackagename() : "N/A");
				data.setPackagecode(data.getPackagecode() != null ? data.getPackagecode() : "N/A");
				data.setAgecondition(data.getAgecondition() != null ? data.getAgecondition() : "N/A");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get Dynamic Configuration
	 *         Report by Id
	 *
	 */
	@Override
	public DynamicReportConfiguration getdynamicbyid(Long slno) {
		DynamicReportConfiguration dynamicconfig = new DynamicReportConfiguration();
		try {
			dynamicconfig = dynamicreportconfigrepo.findById(slno).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return dynamicconfig;
	}

	/**
	 * @author rajendra.sahoo This method is user for Update Dynamic Configuration
	 *         Report
	 *
	 */
	@Override
	public Response updatedunamicConfiguration(DynamicReportConfiguration dynamicReport) {
		Response response = new Response();
		try {
			DynamicReportConfiguration dynamicconfig2 = dynamicreportconfigrepo.findById(dynamicReport.getSlno()).get();
			String s = dynamicReport.getPackagecode();
			if (s != null && s != "") {
				dynamicReport.setPackagecode(s.substring(0, s.length() - 1));
			} else {
				dynamicReport.setPackagecode(s);
			}
			if (dynamicReport.getAgecondition() == "") {
				dynamicReport.setAgecondition(">");
			}
			dynamicReport.setCreatedOn(dynamicconfig2.getCreatedOn());
			dynamicReport.setCreatedBy(dynamicconfig2.getCreatedBy());
			dynamicReport.setUpdatedOn(Calendar.getInstance().getTime());
			dynamicreportconfigrepo.save(dynamicReport);
			response.setStatus("200");
			response.setMessage("Record Updated Sucessfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get Dynamic Report List by
	 *         Procedure
	 *
	 */
	@Override
	public List<Object> getdynamicreport(String fromdate, String todate, Integer trigger) {
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DYNAMIC_REPORT")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SLNO", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_SLNO", trigger);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (rs.next()) {
				DynamicReportConfiguration bean = new DynamicReportConfiguration();
				bean.setSlno(rs.getLong(1));
				bean.setReportname(rs.getString(2) != null ? rs.getString(2) : "N/A");
				bean.setTotalnumber(rs.getString(3) != null ? rs.getString(3) : "0");
				list.add(bean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
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

	/**
	 * @author rajendra.sahoo This method is user for Get Dynamic Report SUB Details
	 *
	 */
	@Override
	public Map<String, Object> getdynamicreportsubdetails(String fromdate, String todate, Integer flag, String report) {
		Map<String, Object> list = new HashedMap<>();
		List<Object> objlist = new ArrayList<>();
		List<String> header = new ArrayList<>();
		List<String> value = null;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DYNAMIC_REPORT_SUB_DETAILS")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SLNO", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_SLNO", flag);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (rs != null) {
				ResultSetMetaData columns = rs.getMetaData();
				int i = 0;
				header.add("SL NO.");
				while (i < columns.getColumnCount() - 1) {
					i++;
					header.add(columns.getColumnName(i));
				}
				Integer k = 0;
				while (rs.next()) {
					value = new ArrayList<>();
					k++;
					value.add(k.toString());
					for (int j = 1; j <= columns.getColumnCount() - 1; j++) {
						value.add(rs.getString(j) != null ? rs.getString(j) : "N/A");
					}
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("status", rs.getString(columns.getColumnCount()));
					switch (flag) {
					case 1:
						object.put("searchvalue", rs.getString(2));
						break;
					case 3:
						object.put("searchvalue", rs.getString(2));
						break;
					case 8:
						object.put("searchvalue", rs.getString(2));
						break;
					case 9:
						object.put("searchvalue", rs.getString(4));
						break;
					case 10:
						object.put("searchvalue", rs.getString(4));
						break;
					case 12:
						object.put("searchvalue", rs.getString(4));
						break;
					case 13:
						object.put("searchvalue", rs.getString(4));
						break;
					default:
						object.put("searchvalue", "");
						break;
					}
					objlist.add(object);
				}
			}
			list.put("heading", header);
			list.put("data", objlist);
			list.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			list.put("status", 400);
		}
		return list;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get Dynamic Report Details
	 *
	 */
	@Override
	public Map<String, Object> getdynamicreportdetails(String fromdate, String todate, Integer flag, String report,
			String txtvalue) {
		Map<String, Object> list = new HashedMap<>();
		List<Object> objlist = new ArrayList<Object>();
		ResultSet rs = null;
		String[] header;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DYNAMIC_REPORT_DETAILS")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SLNO", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_VALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_SLNO", flag);
			storedProcedureQuery.setParameter("P_VALUE", txtvalue);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			Integer i = 0;
			switch (flag) {
			case 1:
				header = new String[] { "Sl No", "URN", "Phone No", "Claim No", "Patient Name", "Hospital Name",
						"Hospital Code", "Hospital Dist", "Package Code", "Package Name", "Actual Date of Admission",
						"Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 3:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Gender", "Phone No", "Claim No",
						"Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 4:
				header = new String[] { "Sl No", "URN", "Member Id", "Patient Name", "Patient Gender", "Phone No",
						"Claim No", "Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("MEMBER_ID"));
					value.add(rs.getString("PATIENTNAME"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 5:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender", "Phone No",
						"Claim No", "Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 6:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender", "Phone No",
						"Claim No", "Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 7:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender", "Phone No",
						"Claim No", "Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 8:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender", "Phone No",
						"Claim No", "Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 9:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient District", "Phone No", "Claim No",
						"Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount",
						"Treatment Doctor Name","Doctor Surgery Datetime" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_DISTRICT"));
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					value.add(rs.getString("TREATED_DOCTORNAME"));
					Date d=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(rs.getString("SURGERY_DATETIME"));
					value.add(new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(d));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 10:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender",
						"Patient District", "Phone No", "Claim No", "Hospital Name", "Hospital Code", "Hospital Dist",
						"Package Code", "Package Name", "Actual Date of Admission", "Actual Date of Discharge",
						"Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PATIENT_DISTRICT"));
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 11:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender",
						"Patient District", "Phone No", "Claim No", "Hospital Name", "Hospital Code", "Hospital Dist",
						"Package Code", "Package Name", "Actual Date of Admission", "Actual Date of Discharge",
						"Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PATIENT_DISTRICT"));
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 12:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender", "Phone No",
						"Claim No", "Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			case 13:
				header = new String[] { "Sl No", "URN", "Patient Name", "Patient Age", "Patient Gender", "Phone No",
						"Claim No", "Hospital Name", "Hospital Code", "Hospital Dist", "Package Code", "Package Name",
						"Actual Date of Admission", "Actual Date of Discharge", "Hospital Claim Amount" };
				while (rs.next()) {
					List<String> value = new ArrayList<String>();
					value.add((++i).toString());
					value.add(rs.getString("URN"));
					value.add(rs.getString("PATIENTNAME"));
					value.add(rs.getString("PATIENT_AGE"));
					String s = rs.getString("PATIENT_GENDER");
					value.add(s.equals("M") ? "Male" : s.equals("F") ? "Female" : "Other");
					value.add(rs.getString("PHONENO") != null ? rs.getString("PHONENO") : "N/A");
					value.add(rs.getString("CLAIMNO") != null ? rs.getString("CLAIMNO") : "N/A");
					value.add(rs.getString("HOSPITALNAME"));
					value.add(rs.getString("HOSPITALCODE"));
					value.add(rs.getString("HOSPITAL_DIST"));
					value.add(rs.getString("PACKAGECODE"));
					value.add(rs.getString("PACKAGENAME"));
					value.add(rs.getString("ACTUALDATEOFADMISSION") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFADMISSION")))
							: "N/A");
					value.add(rs.getString("ACTUALDATEOFDISCHARGE") != null
							? sdf.format(sdf1.parse(rs.getString("ACTUALDATEOFDISCHARGE")))
							: "N/A");
					value.add(rs.getString("HOSPITALCLAIMAMOUNT"));
					Map<String, Object> object = new HashedMap<>();
					object.put("data", value);
					object.put("txnid", rs.getString("TRANSACTIONDETAILSID"));
					object.put("packagecode", rs.getString("PACKAGECODE"));
					object.put("txnpackageid", rs.getString("TXNPACKAGEDETAILSID"));
					object.put("claimid", rs.getString("CLAIMID"));
					object.put("urn", rs.getString("URN"));
					object.put("remarkstatus", rs.getString("ME_REMARKID") != null ? 1 : 0);
					objlist.add(object);
				}
				break;
			default:
				header = new String[] { "No Data Found" };
				break;
			}

			list.put("heading", header);
			list.put("data", objlist);
			list.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			list.put("status", 400);
		}
		return list;
	}

	/**
	 * @author rajendra.sahoo This method is user for get Treatment History Over
	 *         Package For All
	 *
	 */
	@Override
	public String getmeTreatmentHistoryoverpackgae(Long txnId, String urnnumber, String hospitalcode, String caseno,
			String uidreferencenumber, Long userid) throws Exception {
		JSONArray treatmentlog = new JSONArray();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ME_TREATMENT_HISTORY_DTLS")
					.registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_urn", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_uidreferencenumber", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_hosptlcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_p_msgout", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("p_user_id", userid);
			storedProcedureQuery.setParameter("p_urn", urnnumber.trim());
			storedProcedureQuery.setParameter("p_uidreferencenumber", uidreferencenumber.trim());
			storedProcedureQuery.setParameter("p_hosptlcode", hospitalcode.trim());
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("p_p_msgout");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("totalnumberofpackage", rs.getString(1) != null ? rs.getString(1) : "N/A");
				json.put("totalnumberofmember", rs.getString(2) != null ? rs.getString(2) : "N/A");
				json.put("totalnumberofpackageforhospital", rs.getString(3) != null ? rs.getString(3) : "N/A");
				json.put("totalnumberofmemberforhospital", rs.getString(4) != null ? rs.getString(4) : "N/A");
				json.put("totalNoOfBloackedamount", rs.getString(5) != null ? rs.getString(5) : "N/A");
				json.put("packageblockedforpatient", rs.getString(6) != null ? rs.getString(6) : "N/A");
				json.put("sumofpackageblockedamount", rs.getString(7) != null ? rs.getString(7) : "N/A");
				treatmentlog.put(json);
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
		return treatmentlog.toString();
	}

	/**
	 * @author rajendra.sahoo This method is user for Submit Remark For Claim
	 *
	 */
	@Transactional
	@Override
	public Response sumbitmeremark(Long txnId, String remark, Long userid, String urn, Long claimid, Integer flag,
			Integer year, Integer month) {
		Response response = new Response();
		try {			
			if (flag == 1) {
				List<Object[]> list = dynamicreportconfigrepo.getdynamicreportthroughmobileno(txnId, flag);
				for (Object[] obj : list) {
					BigDecimal b = (BigDecimal) obj[0];
					BigDecimal b1 = (BigDecimal) obj[1];
					Long claimid1 = b1 != null ? b1.longValue() : null;
					response = submitremarksaveinlog(b.longValue(), remark, userid, urn, claimid1, flag, year, month);
				}
			} else if (flag == 4) {
				List<Object[]> list = dynamicreportconfigrepo.getdynamicreportthroughmemberid(txnId, flag);
				for (Object[] obj : list) {
					BigDecimal b = (BigDecimal) obj[0];
					BigDecimal b1 = (BigDecimal) obj[1];
					Long claimid1 = b1 != null ? b1.longValue() : null;
					response = submitremarksaveinlog(b.longValue(), remark, userid, urn, claimid1, flag, year, month);
				}
			} else {
				List<Object[]> list = dynamicreportconfigrepo.getdynamicreportthroughurn(urn, flag);
				if(list.isEmpty()) {
					response = submitremarksaveinlog(txnId, remark, userid, urn, claimid, flag, year, month);
				}else {
					for (Object[] obj : list) {
						BigDecimal b = (BigDecimal) obj[0];
						BigDecimal b1 = (BigDecimal) obj[1];
						Long claimid1 = b1 != null ? b1.longValue() : null;
						response = submitremarksaveinlog(b.longValue(), remark, userid, urn, claimid1, flag, year, month);
					}
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	private Response submitremarksaveinlog(Long txnId, String remark, Long userid, String urn, Long claimid,
			Integer flag, Integer year, Integer month) {
		Response response = new Response();
		try {
			if (claimid != null) {
				StoredProcedureQuery storedProcedureQuery = this.entityManager
						.createStoredProcedureQuery("USP_CLAIM_ME_ACTION")
						.registerStoredProcedureParameter("P_TRANSACTIONDETAILSID", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_CLAIMID", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_REMARK", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_URN", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_USERIP", String.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
						.registerStoredProcedureParameter("P_OUT", Long.class, ParameterMode.OUT);
				storedProcedureQuery.setParameter("P_TRANSACTIONDETAILSID", txnId);
				storedProcedureQuery.setParameter("P_CLAIMID", claimid);
				storedProcedureQuery.setParameter("P_REMARK", remark);
				storedProcedureQuery.setParameter("P_URN", urn);
				storedProcedureQuery.setParameter("P_USERIP", (InetAddress.getLocalHost()).getHostAddress());
				storedProcedureQuery.setParameter("P_USERID", userid);
				storedProcedureQuery.execute();

				Long status = (Long) storedProcedureQuery.getOutputParameterValue("P_OUT");
			}
			Integer count = meactionlogrepo.checkduplicatetxnid(txnId,flag);
			if (count == 0) {
				// for insert in tbl_me_action
				response = saveinTblMeActionlog(txnId, remark, userid, urn, claimid, flag, year, month);
			} else {
				response = updateinTblMeActionlog(txnId, remark, userid, urn, claimid, flag, year, month);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	private Response saveinTblMeActionlog(Long txnId, String remark, Long userid, String urn, Long claimid,
			Integer flag, Integer year, Integer month) {
		Response response = new Response();
		Date date = Calendar.getInstance().getTime();
		try {
			TblMeAction meactionlog = new TblMeAction();
			meactionlog.setActionby(userid);
			meactionlog.setActionon(date);
			meactionlog.setRemark(remark);
			meactionlog.setUrn(urn);
			meactionlog.setClaimid(claimid);
			if (claimid != null) {
				meactionlog.setClaimraisestatus(1);
			} else {
				meactionlog.setClaimraisestatus(0);
			}
			meactionlog.setClaimremarkcount(1);
			meactionlog.setTransactionid(txnId);
			meactionlog.setCreateby(userid);
			meactionlog.setCreateon(date);
			meactionlog.setMeTriggerId(flag);
			meactionlog.setStatus(0);
			if (year == null) {
				meactionlog.setAgeremark(null);
			} else {
				meactionlog.setAgeremark(year.toString());
			}
			meactionlogrepo.save(meactionlog);
			response.setStatus("200");
			response.setMessage("Remark Submitted Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	private Response updateinTblMeActionlog(Long txnId, String remark, Long userid, String urn, Long claimid,
			Integer flag, Integer year, Integer month) {
		Response response = new Response();
		Date date = Calendar.getInstance().getTime();
		try {
			TblMeAction meaction = meactionlogrepo.getduplicatetxnid(txnId,flag);
			// for log insert
			TblMeActionLog actionlog = new TblMeActionLog();
			actionlog.setActionby(meaction.getActionby());
			actionlog.setActionon(meaction.getActionon());
			actionlog.setRemark(meaction.getRemark());
			actionlog.setUrn(meaction.getUrn());
			actionlog.setClaimid(meaction.getClaimid());
			actionlog.setTransactionid(meaction.getTransactionid());
			actionlog.setMeTriggerId(flag);
			actionlog.setCreateby(userid);
			actionlog.setCreateon(date);
			actionlog.setStatus(meaction.getStatus());
			actionlog.setClaimraisestatus(meaction.getClaimraisestatus());
			actionlog.setActionid(meaction.getActionlogid());
			actionlog.setAgeremark(meaction.getAgeremark());
			actionlogrepo.save(actionlog);
			// for update
			meaction.setRemark(remark);
			meaction.setActionby(userid);
			meaction.setActionon(date);
			meaction.setUpdateby(userid);
			meaction.setUpdateon(date);
			meaction.setMeTriggerId(flag);
			meaction.setClaimremarkcount(meaction.getClaimremarkcount() + 1);
			if (year == null) {
				meaction.setAgeremark(null);
			} else {
				meaction.setAgeremark(year.toString());
			}
			meactionlogrepo.save(meaction);
			response.setStatus("200");
			response.setMessage("Remark Updated Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get List Of M & E Action Taken
	 *         List
	 *
	 */
	@Override
	public List<DynamicReportDetailsmodel> getmeactiontakendetails(Long userid, Date fromdate, Date todate,
			Integer trigger) {
		List<DynamicReportDetailsmodel> list = new ArrayList<DynamicReportDetailsmodel>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ME_ACTION_TAKEN_LIST_RPT")
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGER", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_USERID", userid);
			storedProcedureQuery.setParameter("P_TRIGGER", trigger);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");

			while (rs.next()) {
				DynamicReportDetailsmodel omodelbean = new DynamicReportDetailsmodel();
				omodelbean.setRemark(rs.getString(1));
				omodelbean.setUrn(rs.getString(2));
				omodelbean.setClaimno(rs.getString(3) != null ? rs.getString(3) : "N/A");
				omodelbean.setCaseno(rs.getString(4) != null ? rs.getString(4) : "N/A");
				omodelbean.setPatientname(rs.getString(5));
				omodelbean.setPhoneno(rs.getString(6));
				omodelbean.setHospitalname(rs.getString(7));
				omodelbean.setHospitalcode(rs.getString(8));
				omodelbean.setPackagecode(rs.getString(9));
				omodelbean.setPackagename(rs.getString(10));
				Date d = rs.getDate(11);
				omodelbean.setActualdateofadmission(d != null ? sdf.format(d) : "N/A");
				d = rs.getDate(12);
				omodelbean.setActualdateofdischarge(d != null ? sdf.format(d) : "N/A");
				omodelbean.setClaimamount(rs.getString(13) == null ? "N/A" : rs.getString(13));
				omodelbean.setClaimid(rs.getString(14));
				omodelbean.setTxnid(rs.getString(15) == null ? "N/A" : rs.getString(15));
				omodelbean.setTxnpackageid(rs.getString(16) == null ? "N/A" : rs.getString(16));
				omodelbean.setActionon(rs.getString(17));
				omodelbean.setHospitaldistrictcode(rs.getString(18) == null ? "N/A" : rs.getString(18));
				omodelbean.setHospitaldistrictname(rs.getString(19) == null ? "N/A" : rs.getString(19));
				omodelbean.setClaimsubmitornot(rs.getString(20));
				omodelbean.setAgeRemark(rs.getString(21));
				omodelbean.setSnaName(rs.getString(22));
				omodelbean.setReportname(rs.getString(23));
				list.add(omodelbean);
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

		return list;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get M & E Remark Of A Claim
	 *
	 */
	@Override
	public TblMeAction getmanderemark(Long txnid) {
		TblMeAction log = new TblMeAction();
		try {
			log = meactionlogrepo.findBytransactionid(txnid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get SNA Name through txnid
	 *
	 */
	@Override
	public List<Object[]> getsnoidbytxnid(Long txnid) {
		List<Object[]> list = new ArrayList<>();
		try {
			list = meactionlogrepo.getsnoidbytxnid(txnid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get M & E Action log on a
	 *         claim
	 *
	 */
	@Override
	public List<Object> getmeactionlog(Long txnid) {
		List<Object> list = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		try {
			TblMeAction log = meactionlogrepo.findBytransactionid(txnid);
			if (log != null) {
				TblMeActionLog omodelbean = new TblMeActionLog();
				UserDetails user = userrepo.findById(log.getActionby()).get();
				omodelbean.setActionby1(user.getFullname());
				omodelbean.setActionon1(sdf.format(log.getActionon()));
				omodelbean.setRemark(log.getRemark());
				omodelbean.setActiontype("M & E Section Remark");
				list.add(omodelbean);
			}
			List<Object[]> objlist = actionlogrepo.findBytxnid(txnid);
			for (Object[] obj : objlist) {
				TblMeActionLog omodelbean = new TblMeActionLog();
				omodelbean.setActionby1((String) obj[0]);
				omodelbean.setActionon1(sdf.format((Date) obj[1]));
				omodelbean.setRemark((String) obj[2]);
				omodelbean.setActiontype("M & E Section Remark");
				list.add(omodelbean);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	/**
	 * @author rajendra.sahoo This method is user for Get Case Specific Remark for M
	 *         & E Remark
	 *
	 */
	@Override
	public List<Object> getspecificcaseremarklist(Integer searchby, String fieldvalue, Long userId) {
		List<Object> list = null;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ME_CASE_SPECIFIC_REMARK")
					.registerStoredProcedureParameter("P_SEARCHBY", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FIELDVALUE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_SEARCHBY", searchby);
			storedProcedureQuery.setParameter("P_FIELDVALUE", fieldvalue.trim());
			storedProcedureQuery.setParameter("P_USERID", userId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			list = new ArrayList<Object>();
			while (rs.next()) {
				DynamicReportDetailsmodel omodelbean = new DynamicReportDetailsmodel();
				omodelbean.setRemarkstatus(rs.getString(1) != null ? 1 : 0);
				omodelbean.setUrn(rs.getString(2));
				omodelbean.setClaimno(rs.getString(3) != null ? rs.getString(3) : "N/A");
				omodelbean.setCaseno(rs.getString(4) != null ? rs.getString(4) : "N/A");
				omodelbean.setPatientname(rs.getString(5));
				omodelbean.setPhoneno(rs.getString(6));
				omodelbean.setHospitalname(rs.getString(7));
				omodelbean.setHospitalcode(rs.getString(8));
				omodelbean.setPackagecode(rs.getString(9));
				omodelbean.setPackagename(rs.getString(10));
				Date d = rs.getDate(11);
				omodelbean.setActualdateofadmission(d != null ? sdf.format(d) : "N/A");
				d = rs.getDate(12);
				omodelbean.setActualdateofdischarge(d != null ? sdf.format(d) : "N/A");
				omodelbean.setClaimamount(rs.getString(13) == null ? "N/A" : rs.getString(13));
				omodelbean.setClaimid(rs.getString(14));
				omodelbean.setTxnid(rs.getString(15) == null ? "N/A" : rs.getString(15));
				omodelbean.setTxnpackageid(rs.getString(16) == null ? "N/A" : rs.getString(16));
				omodelbean.setActionon(rs.getString(17));
				omodelbean.setHospitaldistrictcode(rs.getString(18) != null ? rs.getString(18) : "N/A");
				omodelbean.setHospitaldistrictname(rs.getString(19) != null ? rs.getString(19) : "N/A");
				list.add(omodelbean);
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
		return list;
	}

	@Override
	public List<DynamicReportConfiguration> findAllActiveTrigger() {
		List<DynamicReportConfiguration> list = new ArrayList<DynamicReportConfiguration>();
		try {
			list = dynamicreportconfigrepo.findAllActiveTrigger();
			for (DynamicReportConfiguration data : list) {
				data.setPackagename(data.getPackagecode() != null ? data.getPackagename() : "N/A");
				data.setPackagecode(data.getPackagecode() != null ? data.getPackagecode() : "N/A");
				data.setAgecondition(data.getAgecondition() != null ? data.getAgecondition() : "N/A");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public List<Object> getmeabstractreport(String formdate, String todate, Integer trigger) throws Exception {
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ME_ABSTRACT_RPT")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TRIGGER", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_TRIGGER", trigger);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			while (rs.next()) {
				Map<String, Object> map = new HashedMap<String, Object>();
				map.put("slno", rs.getString(1));
				map.put("triggerName", rs.getString(2));
				map.put("totalcase", rs.getString(3));
				map.put("actionTaken", rs.getString(4));
				map.put("pendingClaim", rs.getString(5));
				list.add(map);
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new Exception(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				logger.error(ExceptionUtils.getStackTrace(e2));
			}
		}
		return list;
	}

	@Override
	public List<DynamicReportDetailsmodel> getMeTriggerGrievancedetails(Long snaUserId, Date fromdate, Date todate,
			Integer trigger, String stateCode, String districtCode, String hospitalCode) {
		List<DynamicReportDetailsmodel> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_ME_TRIGGER_GRIEVANCE")
					.registerStoredProcedureParameter("P_TRIGGER", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_FROMDATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", Date.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICTCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_TRIGGER", trigger);
			storedProcedureQuery.setParameter("P_FROMDATE", fromdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_HOSPITALCODE", hospitalCode);
			storedProcedureQuery.setParameter("P_STATECODE", stateCode);
			storedProcedureQuery.setParameter("P_DISTRICTCODE", districtCode);
			storedProcedureQuery.setParameter("P_USERID", snaUserId);
			storedProcedureQuery.execute();

			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");

			while (rs.next()) {
				DynamicReportDetailsmodel omodelbean = new DynamicReportDetailsmodel();
				omodelbean.setRemark(rs.getString(1));
				omodelbean.setUrn(rs.getString(2));
				omodelbean.setClaimno(rs.getString(3) != null ? rs.getString(3) : "N/A");
				omodelbean.setCaseno(rs.getString(4) != null ? rs.getString(4) : "N/A");
				omodelbean.setPatientname(rs.getString(5));
				omodelbean.setPhoneno(rs.getString(6));
				omodelbean.setHospitalname(rs.getString(7));
				omodelbean.setHospitalcode(rs.getString(8));
				omodelbean.setPackagecode(rs.getString(9));
				omodelbean.setPackagename(rs.getString(10));
				Date d = rs.getDate(11);
				omodelbean.setActualdateofadmission(d != null ? sdf.format(d) : "N/A");
				d = rs.getDate(12);
				omodelbean.setActualdateofdischarge(d != null ? sdf.format(d) : "N/A");
				omodelbean.setClaimamount(rs.getString(13) == null ? "N/A" : rs.getString(13));
				omodelbean.setClaimid(rs.getString(14));
				omodelbean.setTxnid(rs.getString(15) == null ? "N/A" : rs.getString(15));
				omodelbean.setTxnpackageid(rs.getString(16) == null ? "N/A" : rs.getString(16));
				omodelbean.setActionon(rs.getString(17));
				omodelbean.setHospitaldistrictcode(rs.getString(18) == null ? "N/A" : rs.getString(18));
				omodelbean.setHospitaldistrictname(rs.getString(19) == null ? "N/A" : rs.getString(19));
				omodelbean.setClaimsubmitornot(rs.getString(20));
				omodelbean.setAgeRemark(rs.getString(21));
				omodelbean.setSnaName(rs.getString(22));
				omodelbean.setReportname(rs.getString(23));
				list.add(omodelbean);
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

		return list;
	}

	@Override
	public Map<String, Object> getdynamicreportforexceldownload(String formdate, String todate, Integer trigger)
			throws Exception {
		Map<String, Object> list = new HashedMap<>();
		List<Object> objlist = new ArrayList<>();
		List<String> header = new ArrayList<>();
		List<String> value = null;
		ResultSet rs = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_DYNAMIC_REPORT_FOR_EXCEL")
					.registerStoredProcedureParameter("P_FROMDATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_TODATE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SLNO", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_FROMDATE", formdate);
			storedProcedureQuery.setParameter("P_TODATE", todate);
			storedProcedureQuery.setParameter("P_SLNO", trigger);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if (rs != null) {
				ResultSetMetaData columns = rs.getMetaData();
				int i = 0;
				header.add("SL NO.");
				while (i < columns.getColumnCount()) {
					i++;
					header.add(columns.getColumnName(i));
				}
				Integer k = 0;
				while (rs.next()) {
					value = new ArrayList<>();
					k++;
					value.add(k.toString());
					for (int j = 1; j <= columns.getColumnCount(); j++) {
						value.add(rs.getString(j) != null ? rs.getString(j) : "N/A");
					}
					objlist.add(value);
				}
			}
			list.put("heading", header);
			list.put("data", objlist);
			list.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			list.put("status", 400);
		}
		return list;
	}

}
