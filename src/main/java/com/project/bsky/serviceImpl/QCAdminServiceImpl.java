package com.project.bsky.serviceImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.AuthmodeUIDBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.TemporyOverrideCodeBean;
import com.project.bsky.bean.Uidauthmodetagbean;
import com.project.bsky.bean.UpdateEmpanelHospData;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.HospitalInformationLog;
import com.project.bsky.repository.HospitalCategoryMasterRepository;
import com.project.bsky.repository.HospitalInformationLogRepository;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.service.QCAdminService;
import com.project.bsky.util.CommonFileUpload;
import com.project.bsky.util.JwtUtil;
import com.project.bsky.util.MouDocumentUpload;

import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Service
public class QCAdminServiceImpl implements QCAdminService {

	@Autowired
	private Environment env;

	@Autowired
	private HospitalInformationRepository hospitalInformationRepository;

	@Autowired
	private HospitalCategoryMasterRepository categoryRepo;

	@Autowired
	private HospitalInformationLogRepository hospitalUserLogRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Logger logger;

	@Autowired
	private JwtUtil util;

	private Connection connection = null;
	private CallableStatement statement = null;

	@Value("${file.path.Mouuploaddoc}")
	private String file;

	@Override
	public HospitalInformation listview(String hospitalId) {
		HospitalInformation listView = new HospitalInformation();
		try {
			DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
			listView = hospitalInformationRepository.finddatabyHospitalCode(hospitalId);

//			listView.setHosCValidDateFrom(listView.getHcValidFromDate()!=null ? f.format(listView.getHcValidFromDate()):"");
//			listView.setHosCValidDateTo(listView.getHcValidToDate()!=null ? f.format(listView.getHcValidToDate()):"");
//			listView.setMouEndDt(listView.getMouEndDate()!=null ? f.format(listView.getMouEndDate()):"");
//			listView.setMouStartDt(listView.getMouStartDate()!=null ? f.format(listView.getMouStartDate()):"");

			if (listView.getHcValidFromDate() != null) {
				listView.setHosCValidDateFrom(f.format(listView.getHcValidFromDate()));
			}
			if (listView.getHcValidToDate() != null) {
				listView.setHosCValidDateTo(f.format(listView.getHcValidToDate()));
			}
			if (listView.getMouEndDate() != null) {
				listView.setMouEndDt(f.format(listView.getMouEndDate()));
			}
			if (listView.getMouStartDate() != null) {
				listView.setMouStartDt(f.format(listView.getMouStartDate()));
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listView;

	}

//	@Override
//	public Response updateEmpanelHospitalData(UpdateEmpanelHospData updateEmpanelHospData,MultipartFile form) {
//
//		Response response = new Response();
//		try {
//			//System.out.println("hi");
//			HospitalInformation hospitalInformation = hospitalInformationRepository
//					.findByhospitalCode(updateEmpanelHospData.getHospitalCode());
//			SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
//
//			hospitalInformation.setHospitalCategoryid(Integer.parseInt(updateEmpanelHospData.getHospitalCategoryid()));
//			//System.out.println("updateEmpanelHospData "+updateEmpanelHospData.getHosCValidDateFrom());
//			if(updateEmpanelHospData.getHosCValidDateFrom()!=null) {
//				hospitalInformation.setHcValidFromDate(f.parse(updateEmpanelHospData.getHosCValidDateFrom()));
//			}
//			if(updateEmpanelHospData.getHosCValidDateTo()!=null) {
//				hospitalInformation.setHcValidToDate(f.parse(updateEmpanelHospData.getHosCValidDateTo()));
//			}
//			
//			hospitalInformation.setMou(updateEmpanelHospData.getMou());
//			hospitalInformation.setMouEndDate(f.parse(updateEmpanelHospData.getMouEndDt()));
//			hospitalInformation.setMouStartDate(f.parse(updateEmpanelHospData.getMouStartDt()));
//			hospitalInformation.setUpdatedBy(Long.parseLong(updateEmpanelHospData.getUpdatedby()));
//			hospitalInformation.setEmpanelmentstatus(Integer.parseInt(updateEmpanelHospData.getEmpanelmentstatus()));
//			hospitalInformation.setMouStatus(Integer.parseInt(updateEmpanelHospData.getMouStatus()));
//			Calendar calendar = Calendar.getInstance();
//			hospitalInformation.setUpdatedOn(calendar.getTime());
//			//System.out.println(hospitalInformation);
//			hospitalInformationRepository.save(hospitalInformation);
//
//			response.setStatus("Success");
//			response.setMessage("Successfully Updated");
//
//			return response;
//		} catch (Exception e) {
//			//System.out.println(e);
//			response.setStatus("Failed");
//			response.setMessage("Something went wrong");
//			return response;
//		}
//
//	}

	@Override
	public Response updateEmpanelHospitalData(UpdateEmpanelHospData updateEmpanelHospData, MultipartFile form) {
		Response response = new Response();
		try {
			// System.out.println("hi");

			HospitalInformation hospitalInformation = hospitalInformationRepository
					.findByhospitalCode(updateEmpanelHospData.getHospitalCode());
			SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
			HospitalInformationLog qclog = new HospitalInformationLog();
			qclog.setHospitalId(hospitalInformation.getHospitalId());
			qclog.setHospitalCode(hospitalInformation.getHospitalCode());
			qclog.setHospitalName(hospitalInformation.getHospitalName());
			qclog.setStateCode(hospitalInformation.getDistrictcode().getStatecode().getStateCode());
			qclog.setDistrictcode(hospitalInformation.getDistrictcode().getDistrictcode());
			qclog.setEmailId(hospitalInformation.getEmailId());
			qclog.setMobile(hospitalInformation.getMobile());

			qclog.setHospitalCategoryid(Integer.parseInt(updateEmpanelHospData.getHospitalCategoryid()));

			if (updateEmpanelHospData.getHosCValidDateFrom() != null
					&& !updateEmpanelHospData.getHosCValidDateFrom().trim().equalsIgnoreCase("null")) {
				qclog.setHcValidFromDate(f.parse(updateEmpanelHospData.getHosCValidDateFrom()));
			}
			if (updateEmpanelHospData.getHosCValidDateTo() != null) {
				qclog.setHcValidToDate(f.parse(updateEmpanelHospData.getHosCValidDateTo()));
			}
			qclog.setMou(updateEmpanelHospData.getMou());
			qclog.setMouEndDate(f.parse(updateEmpanelHospData.getMouEndDt()));
			qclog.setMouStartDate(f.parse(updateEmpanelHospData.getMouStartDt()));
			qclog.setCreatedBy(Integer.parseInt(updateEmpanelHospData.getUpdatedby()));
			qclog.setEmpanelmentstatus(Integer.parseInt(updateEmpanelHospData.getEmpanelmentstatus()));
			qclog.setMouStatus(Integer.parseInt(updateEmpanelHospData.getMouStatus()));
			// qclog.setStatus(Integer.parseInt(updateEmpanelHospData.getStatus()));
			qclog.setIsBlockActive(Integer.parseInt(updateEmpanelHospData.getIsBlockActive()));
			Calendar calendar1 = Calendar.getInstance();
			qclog.setCreatedOn(calendar1.getTime());
			qclog.setCpdApprovalRequired(Integer.parseInt(updateEmpanelHospData.getCpdApprovalRequired()));
			qclog.setPreauthapprovalrequired(Integer.parseInt(updateEmpanelHospData.getPreauthapprovalrequired()));

			if (form != null) {
				String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				String Month = new SimpleDateFormat("MMM").format(new Date());
				String filePath = MouDocumentUpload.createFileformoudoc(year, form, Month);
				qclog.setMouDocUpload(filePath);
			} else {
				qclog.setMouDocUpload(null);
			}

			// log table insert data
			hospitalUserLogRepository.save(qclog);

			hospitalInformation.setHospitalCategoryid(Integer.parseInt(updateEmpanelHospData.getHospitalCategoryid()));
			// System.out.println("updateEmpanelHospData
			// "+updateEmpanelHospData.getHosCValidDateFrom());
			if (updateEmpanelHospData.getHosCValidDateFrom() != null
					&& !updateEmpanelHospData.getHosCValidDateFrom().trim().equalsIgnoreCase("null")) {
				hospitalInformation.setHcValidFromDate(f.parse(updateEmpanelHospData.getHosCValidDateFrom()));
			}
			if (updateEmpanelHospData.getHosCValidDateTo() != null) {
				hospitalInformation.setHcValidToDate(f.parse(updateEmpanelHospData.getHosCValidDateTo()));
			}

			hospitalInformation.setMou(updateEmpanelHospData.getMou());
			hospitalInformation.setMouEndDate(f.parse(updateEmpanelHospData.getMouEndDt()));
			hospitalInformation.setMouStartDate(f.parse(updateEmpanelHospData.getMouStartDt()));
			hospitalInformation.setUpdatedBy(Long.parseLong(updateEmpanelHospData.getUpdatedby()));
			hospitalInformation.setEmpanelmentstatus(Integer.parseInt(updateEmpanelHospData.getEmpanelmentstatus()));
			hospitalInformation.setMouStatus(Integer.parseInt(updateEmpanelHospData.getMouStatus()));
			// hospitalInformation.setStatus(Integer.parseInt(updateEmpanelHospData.getStatus()));
			hospitalInformation.setIsBlockActive(Integer.parseInt(updateEmpanelHospData.getIsBlockActive()));
			Calendar calendar = Calendar.getInstance();
			hospitalInformation.setUpdatedOn(calendar.getTime());
			hospitalInformation
					.setCpdApprovalRequired(Integer.parseInt(updateEmpanelHospData.getCpdApprovalRequired()));
			hospitalInformation
					.setPreauthapprovalrequired(Integer.parseInt(updateEmpanelHospData.getPreauthapprovalrequired()));

			// System.out.println("Service Impl: "+hospitalInformation);

			if (form != null) {
				String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				String Month = new SimpleDateFormat("MMM").format(new Date());
				String filePath = MouDocumentUpload.createFileformoudoc(year, form, Month);
				hospitalInformation.setMouDocUpload(filePath);
			} else {
				hospitalInformation.setMouDocUpload(null);
			}

			hospitalInformationRepository.save(hospitalInformation);

			response.setStatus("Success");
			response.setMessage("Successfully Updated");

			return response;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage("Something went wrong");
			return response;
		}

	}

	@Override
	public List<HospitalInformation> getDetails() {
		List<HospitalInformation> hospdetails = new ArrayList<HospitalInformation>();
		List<Object[]> hospdetail = hospitalInformationRepository.gethospitalList();
		for (Object[] x : hospdetail) {
			HospitalInformation hospitalInformation = new HospitalInformation();
			hospitalInformation.setHospitalName(x[0] + " (" + x[1] + ")");
			hospitalInformation.setHospitalCode(x[1].toString());
			hospdetails.add(hospitalInformation);
		}
		return hospdetails;
	}

//	@Override
//	public List<Object> hospList() {
//		
//		List<Object> listHosp=new ArrayList<Object>();
//		List<Object[]> hospdetail=hospitalInformationRepository.allHospList();
//		for(Object[] x:hospdetail) {
//			UpdateEmpanelHospData uempnelData=new UpdateEmpanelHospData();
//			//System.out.println(x[0].toString()+" "+x[1].toString()+" "+x[2].toString()+" "+x[3].toString()+" "+x[4].toString()+" "+x[5].toString()+" "+x[6].toString());
//			//System.out.println("Value is:[x] "+x[6].toString());
//			uempnelData.setHospitalName(x[0].toString());
//			uempnelData.setHospitalCode(x[1].toString());
//			uempnelData.setStateName(x[2].toString());
//			uempnelData.setDistrictName(x[3].toString());
//			uempnelData.setMobile(x[4].toString());
//			uempnelData.setEmailId(x[5].toString());
//			//System.out.println(x[6]+".................");
//			HospitalCategoryMaster hospcatmas=categoryRepo.findById(((Integer)x[6]).longValue()).get();
//			if(hospcatmas!=null)
//			{
//				uempnelData.setHospitalCategoryid(hospcatmas.getHospitalCategoryName());
//			}
//			
//			uempnelData.setHosCValidDateFrom(x[7] != null ? x[7].toString() : "NA");
//			uempnelData.setHosCValidDateTo(x[8].toString());
//			uempnelData.setMou(x[9].toString());
//			uempnelData.setMouStartDt(x[10].toString());
//			uempnelData.setMouEndDt(x[11].toString());
//			//System.out.println(uempnelData);
//			
//			
//			listHosp.add(uempnelData);
//		}
//		return listHosp;
//		
//	}
//	

	@Override
	public List<Object> hospList() {
		// System.out.println("Inside getHospitalList() method");
		List<Object> listHosp = new ArrayList<>();
		try {
			DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
			hospitalInformationRepository.getAllEmpaneledHospitalList().stream().map(hospitalArr -> {
				UpdateEmpanelHospData updateImpanelHospData = new UpdateEmpanelHospData();
				updateImpanelHospData.setHospitalName(
						hospitalArr[0] != null ? hospitalArr[0].toString() + " (" + hospitalArr[1].toString() + ")"
								: "NA");
				updateImpanelHospData.setHospitalCode(hospitalArr[1] != null ? hospitalArr[1].toString() : "NA");
				updateImpanelHospData.setStateName(hospitalArr[2] != null ? hospitalArr[2].toString() : "NA");
				updateImpanelHospData.setDistrictName(hospitalArr[3] != null ? hospitalArr[3].toString() : "NA");
				updateImpanelHospData.setMobile(hospitalArr[4] != null ? hospitalArr[4].toString() : "NA");
				updateImpanelHospData.setEmailId(hospitalArr[5] != null ? hospitalArr[5].toString() : "NA");
				updateImpanelHospData.setHospitalCategoryid(hospitalArr[6] != null ? hospitalArr[6].toString() : "NA");
				try {
					updateImpanelHospData
							.setHosCValidDateFrom(hospitalArr[7] != null
									? new SimpleDateFormat("dd-MMM-yyyy")
											.format(new SimpleDateFormat("yyyy-MM-dd").parse(hospitalArr[7].toString()))
									: "NA");
					updateImpanelHospData
							.setHosCValidDateTo(hospitalArr[8] != null
									? new SimpleDateFormat("dd-MMM-yyyy")
											.format(new SimpleDateFormat("yyyy-MM-dd").parse(hospitalArr[8].toString()))
									: "NA");

					updateImpanelHospData
							.setMouStartDt(hospitalArr[10] != null
									? new SimpleDateFormat("dd-MMM-yyyy").format(
											new SimpleDateFormat("yyyy-MM-dd").parse(hospitalArr[10].toString()))
									: "NA");

					updateImpanelHospData
							.setMouEndDt(hospitalArr[11] != null
									? new SimpleDateFormat("dd-MMM-yyyy").format(
											new SimpleDateFormat("yyyy-MM-dd").parse(hospitalArr[11].toString()))
									: "NA");

				} catch (ParseException e) {

					e.printStackTrace();
				}

				updateImpanelHospData.setMou(hospitalArr[9] != null ? hospitalArr[9].toString() : "NA");
				updateImpanelHospData.setEmpanelmentstatus(hospitalArr[12] != null ? hospitalArr[12].toString() : "NA");
				updateImpanelHospData.setMouStatus(hospitalArr[13] != null ? hospitalArr[13].toString() : "NA");
				updateImpanelHospData.setMouDocUpload(hospitalArr[14] != null ? hospitalArr[14].toString() : null);
				updateImpanelHospData.setIsBlockActive(hospitalArr[15] != null ? hospitalArr[15].toString() : "NA");
				updateImpanelHospData
						.setCpdApprovalRequired(hospitalArr[16] != null ? hospitalArr[16].toString() : "NA");
				updateImpanelHospData
						.setPreauthapprovalrequired(hospitalArr[17] != null ? hospitalArr[17].toString() : "NA");
				return updateImpanelHospData;
			}).forEach(listHosp::add);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		// System.out.println("List Hospital : " + listHosp);
		return listHosp;
	}

	@Override
	public Map<String, Object> submituidauthconfig(Uidauthmodetagbean bean) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {

			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);

			StructDescriptor structDescriptor;

			STRUCT[] selectbean = new STRUCT[bean.getSelectedlist().size()];
			int i = 0;
			for (AuthmodeUIDBean data : bean.getSelectedlist()) {
				structDescriptor = new StructDescriptor("HOS_UID_AUTH_CONFIG", connection);
				Object[] ObjArr = { data.getHospitalId(), data.getHospitalCode(), data.getVerificationId(),
						data.getFlag() };
				selectbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
				i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("TYPE_HOS_UID_AUTH_CONFIG_LIST", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, selectbean);

			statement = connection.prepareCall("call USP_HOS_UID_AUTH_CONFIG_SUBMIT(?,?,?)");
			statement.setArray(1, array_to_pass);// P_HOS_WISE_VERIFICATIONID
			statement.setLong(2, bean.getActionBy());// P_USERID
			statement.registerOutParameter(3, Types.INTEGER);// P_OUT
			statement.execute();
			Integer out = statement.getInt(3);
			if (out == 1) {
				map.put("status", HttpStatus.OK.value());
				map.put("message", "Successful");
			} else {
				map.put("status", HttpStatus.BAD_REQUEST.value());
				map.put("message", "Something Went Wrong");
				map.put("error", "Error In DB Side");
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> temporyOverrideCode(TemporyOverrideCodeBean bean) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Integer resInteger = null;
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);

			StructDescriptor structDescriptor;

			STRUCT[] selectbean = new STRUCT[bean.getHospitalList().size()];
			int i = 0;
			for (AuthmodeUIDBean data : bean.getHospitalList()) {
				structDescriptor = new StructDescriptor("TYP_HOSPITAL_CODE", connection);
				Object[] ObjArr = { data.getHospitalId(), data.getHospitalCode() };
				selectbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
				i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("T_TYP_HOSPITAL_CODE", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, selectbean);

			statement = connection.prepareCall("call USP_HOSPITAL_OVERRIDE_CODE_INSERT(?,?,?,?,?,?,?,?,?,?)");
			statement.setInt(1, 1);
			statement.setString(2, bean.getStateCode());
			statement.setString(3, bean.getDistCode());
			statement.setString(4, bean.getOverrideCode());
			statement.setTimestamp(5, bean.getFromDate());
			statement.setTimestamp(6, bean.getToDate());
			statement.setLong(7, util.getCurrentUser());
			statement.setArray(8, array_to_pass);
			statement.registerOutParameter(9, Types.INTEGER);
			statement.registerOutParameter(10, Types.REF_CURSOR);
			statement.execute();
			resInteger = (Integer) statement.getInt(9);
			if (resInteger == 0) {
				map.put("status", "success");
				map.put("message", "Successful");
			} else if (resInteger == 1) {
				map.put("status", "duplicate");
				map.put("message", "Duplicate Override Code");
			} else if (resInteger == 2) {
				map.put("status", "fail");
				map.put("message", "Something went wrong");
			}

		} catch (Exception e) {
			logger.error("Exception Occured in getClaimLogDetail of ClaimLogDaoImpl class", e);
			throw e;
		}finally {
			if (connection != null) {
				connection.close();
			}
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> temporyOverrideCodeView(TemporyOverrideCodeBean bean) throws Exception {
		List<Map<String, Object>> list = new ArrayList<>();
		Integer resInteger = null;
		ResultSet resultSet = null;
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);

			StructDescriptor structDescriptor;

			STRUCT[] selectbean = new STRUCT[bean.getHospitalList().size()];
			int i = 0;
			for (AuthmodeUIDBean data : bean.getHospitalList()) {
				structDescriptor = new StructDescriptor("TYP_HOSPITAL_CODE", connection);
				Object[] ObjArr = { data.getHospitalId(), data.getHospitalCode() };
				selectbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
				i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("T_TYP_HOSPITAL_CODE", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, selectbean);

			statement = connection.prepareCall("call USP_HOSPITAL_OVERRIDE_CODE_INSERT(?,?,?,?,?,?,?,?,?,?)");
			statement.setInt(1, 3);
			statement.setString(2, bean.getStateCode());
			statement.setString(3, bean.getDistCode());
			statement.setString(4, bean.getOverrideCode());
			statement.setTimestamp(5, bean.getFromDate());
			statement.setTimestamp(6, bean.getToDate());
			statement.setLong(7, util.getCurrentUser());
			statement.setArray(8, array_to_pass);
			statement.registerOutParameter(9, Types.INTEGER);
			statement.registerOutParameter(10, Types.REF_CURSOR);
			statement.execute();
//			resInteger = (Integer) statement.getInt(9);
			resultSet = ((OracleCallableStatement) statement).getCursor(10);
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("overrideId", resultSet.getInt(1));
				map.put("stateCode", resultSet.getString(2));
				map.put("districtCode", resultSet.getString(3));
				map.put("hospitalId", resultSet.getInt(4));
				map.put("hospitalCode", resultSet.getString(5));
				map.put("hosOverrideCode", resultSet.getString(6));
				map.put("fromDate", resultSet.getString(7));
				map.put("toDate", resultSet.getString(8));
				map.put("activeStatus", resultSet.getString(9));
				map.put("hospitalName", resultSet.getString(10));
				map.put("districtName", resultSet.getString(11));
				map.put("stateName", resultSet.getString(12));
				list.add(map);
			}

		} catch (Exception e) {
			logger.error("Exception Occured in getClaimLogDetail of ClaimLogDaoImpl class", e);
			throw e;
		}finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}

		return list;
	}

	@Override
	public Map<String, Object> removeTemporyOverrideCode(TemporyOverrideCodeBean bean) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Integer resInteger = null;
		try {
			String driver = env.getProperty("spring.datasource.driver-class-name");
			String url = env.getProperty("spring.datasource.url");
			String user = env.getProperty("spring.datasource.username");
			String pass = env.getProperty("spring.datasource.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);

			StructDescriptor structDescriptor;

			STRUCT[] selectbean = new STRUCT[bean.getHospitalList().size()];
			int i = 0;
			for (AuthmodeUIDBean data : bean.getHospitalList()) {
				structDescriptor = new StructDescriptor("TYP_HOSPITAL_CODE", connection);
				Object[] ObjArr = { data.getHospitalId(), data.getHospitalCode() };
				selectbean[i] = new STRUCT(structDescriptor, connection, ObjArr);
				i++;
			}
			ArrayDescriptor des = ArrayDescriptor.createDescriptor("T_TYP_HOSPITAL_CODE", connection);
			ARRAY array_to_pass = new ARRAY(des, connection, selectbean);

			statement = connection.prepareCall("call USP_HOSPITAL_OVERRIDE_CODE_INSERT(?,?,?,?,?,?,?,?,?,?)");
			statement.setInt(1, 2);
			statement.setString(2, bean.getStateCode());
			statement.setString(3, bean.getDistCode());
			statement.setString(4, bean.getOverrideCode());
			statement.setTimestamp(5, bean.getFromDate());
			statement.setTimestamp(6, bean.getToDate());
			statement.setLong(7, util.getCurrentUser());
			statement.setArray(8, array_to_pass);
			statement.registerOutParameter(9, Types.INTEGER);
			statement.registerOutParameter(10, Types.REF_CURSOR);
			statement.execute();
			resInteger = (Integer) statement.getInt(9);
			if (resInteger == 0) {
				map.put("status", "success");
				map.put("message", "Inactive Successfully");
			} else if (resInteger == 2) {
				map.put("status", "fail");
				map.put("message", "Something went wrong");
			}

		} catch (Exception e) {
			logger.error("Exception Occured in getClaimLogDetail of ClaimLogDaoImpl class", e);
			throw e;
		}finally {
			if (connection != null) {
				connection.close();
			}
		}

		return map;
	}

	public Map<String, Object> getMappedAuthDetails(Map<String, Object> response) throws Exception {
		List<Map<String, Object>> responseList = new ArrayList<>();
		Map<String, Object> mapdata = new HashMap<>();
		ResultSet resultSet=null;
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_UID_AUTH_CONFIGURATION")
					.registerStoredProcedureParameter("P_ACTION_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 2L);
			storedProcedureQuery.setParameter("P_STATE_CODE", response.get("stateCode"));
			storedProcedureQuery.setParameter("P_DISTRICT_CODE",
					response.get("districtCode").toString().isEmpty() ? null : response.get("districtCode").toString());
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE",
					response.get("hospitalCode").toString().isEmpty() ? null : response.get("hospitalCode").toString());

			storedProcedureQuery.execute();
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");

			while (resultSet.next()) {
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("hospitalCode", resultSet.getString(1));
				map.put("hospitalName", resultSet.getString(2));
				map.put("pos", resultSet.getString(3));
				map.put("iris", resultSet.getString(4));
				map.put("face", resultSet.getString(5));
				map.put("otp", resultSet.getString(6));
				map.put("hospitalid", resultSet.getString(7));
				responseList.add(map);
			}
			mapdata.put("data", responseList);
			mapdata.put("status", 200);
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		return mapdata;
	}

	@Override
	public Map<String, Object> getMappedAuthDetailslog(String hospitalCode) throws Exception {
		List<Map<String, Object>> responseList = new ArrayList<>();
		Map<String, Object> mapdata = new HashMap<>();
		ResultSet resultSet=null;
		try {
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_HOSPITAL_UID_AUTH_CONFIGURATION")
					.registerStoredProcedureParameter("P_ACTION_CODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_ACTION_CODE", 3L);
			storedProcedureQuery.setParameter("P_STATE_CODE", null);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", null);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);

			storedProcedureQuery.execute();
			resultSet = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");

			while (resultSet.next()) {
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("verificationId", resultSet.getString(1));
				map.put("verificationMode", resultSet.getString(2));
				map.put("logcreatby", resultSet.getString(3));
				map.put("logcreatedon", resultSet.getString(4));
				map.put("allowstatus", resultSet.getString(5));
				responseList.add(map);
			}
			mapdata.put("data", responseList);
			mapdata.put("status", 200);
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		return mapdata;
	}

	@Override
	public Map<String, Object> saveHospitalDeactivation(String hospitalCode, String remark, Integer action, Long userId,
			MultipartFile file, MultipartFile adddoc1, MultipartFile adddoc2) throws Exception {
		Map<String, Object> mapdata = new HashMap<>();
		try {
			String docname=null,adddoc1name=null,adddoc2name=null;
			if(file!=null) {
				docname=CommonFileUpload.uploadsuspendfile(file,action,hospitalCode);
			}
			if(adddoc1!=null) {
				adddoc1name=CommonFileUpload.uploadsuspendfile(adddoc1,3,hospitalCode);
			}
			if(adddoc2!=null) {
				adddoc2name=CommonFileUpload.uploadsuspendfile(adddoc2,3,hospitalCode);
			}
			
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_EMP_DEACTIVATION_PROCESS")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMP_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_BY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCUMENT_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);
			
			storedProcedureQuery.setParameter("P_ACTION_CODE", 1);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.setParameter("P_EMP_STATUS", action);
			storedProcedureQuery.setParameter("P_ACTION_BY", userId);
			storedProcedureQuery.setParameter("P_DOCUMENT_NAME", docname);
			storedProcedureQuery.setParameter("P_REMARKS", remark);
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC1", adddoc1name);
			storedProcedureQuery.setParameter("P_ADDITIONAL_DOC2", adddoc2name);

			storedProcedureQuery.execute();
			Integer out = (Integer) storedProcedureQuery.getOutputParameterValue("P_OUT");
			if(out==1) {
				mapdata.put("message", "Success");
				mapdata.put("status", 200);
			}else {
				mapdata.put("message", "Error in DB");
				mapdata.put("status", 400);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return mapdata;
	}

	@Override
	public Map<String, Object> getHospitalDetailsfordeactive(String hospitalCode) throws Exception {
		Map<String, Object> mapdata = new HashMap<>();
		ResultSet rs=null;
		try {			
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_EMP_DEACTIVATION_PROCESS")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMP_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_BY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCUMENT_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);
			
			storedProcedureQuery.setParameter("P_ACTION_CODE", 2);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			if(rs.next()) {
				mapdata.put("hospitalCode", rs.getString(1));
				mapdata.put("hospitalName", rs.getString(2));
				mapdata.put("stateName", rs.getString(3));
				mapdata.put("distName", rs.getString(4));
				mapdata.put("monileNo", rs.getString(5));
				mapdata.put("email", rs.getString(6));
				mapdata.put("moustartDate", rs.getString(7));
				mapdata.put("mouendDate", rs.getString(8));
				mapdata.put("mouStatus", rs.getString(9));
				mapdata.put("catogory", rs.getString(10));
				mapdata.put("blockingStatus", rs.getString(11));				
				mapdata.put("tmsStatus", rs.getString(12));
				mapdata.put("empanelStatus", rs.getString(13)==null?"N/A":rs.getString(13));
				mapdata.put("empanelDesc", rs.getString(14)==null?0:rs.getString(14));
				mapdata.put("assignedSnaName", rs.getString(15)==null?"N/A":rs.getString(15));
				mapdata.put("asignedSnaMob", rs.getString(16)==null?"N/A":rs.getString(16));
				mapdata.put("asignedDcName", rs.getString(17)==null?"N/A":rs.getString(17));
				mapdata.put("asignedDcMob", rs.getString(18)==null?"N/A":rs.getString(18));
				mapdata.put("totalSM", rs.getString(19)==null?0:rs.getString(19));
				mapdata.put("totalDischargeCount", rs.getString(20));
				mapdata.put("totalDischargeAmount", rs.getString(21));
				mapdata.put("totalClaimCount", rs.getString(22));
				mapdata.put("totalClaimAmount", rs.getString(23));
				mapdata.put("totalcnsCount", rs.getString(24));
				mapdata.put("totalcnsAmount", rs.getString(25));
				mapdata.put("onGoingCount", rs.getString(26)==null?0:rs.getString(26));
				mapdata.put("lastBlockDate", rs.getString(27));
				mapdata.put("lastDischargeDate", rs.getString(28));
			}
			mapdata.put("status", 200);
			mapdata.put("message", "Success");
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		return mapdata;
	}

	@Override
	public List<Object> getHospitalDeactivionview(String statecode, String distcode, String hospitalCode, Integer action)
			throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {			
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_EMP_DEACTIVATION_PROCESS")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMP_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_BY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCUMENT_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);
			
			storedProcedureQuery.setParameter("P_ACTION_CODE", 3);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);
			storedProcedureQuery.setParameter("P_EMP_STATUS", action);
			storedProcedureQuery.setParameter("P_STATE_CODE", statecode);
			storedProcedureQuery.setParameter("P_DISTRICT_CODE", distcode);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while(rs.next()) {
				Map<String, Object> mapdata = new HashMap<>();
				mapdata.put("statName", rs.getString(1));
				mapdata.put("distName", rs.getString(2));
				mapdata.put("hospitalCode", rs.getString(3));
				mapdata.put("hospitalName", rs.getString(4));
				mapdata.put("statusval", rs.getString(5));
				mapdata.put("emastatus", rs.getString(6));
				mapdata.put("docname", rs.getString(7));
				mapdata.put("docname1", rs.getString(8));
				mapdata.put("docname2", rs.getString(9));
				mapdata.put("remark", rs.getString(10));
				mapdata.put("actionBy", rs.getString(11));
				mapdata.put("actionon", rs.getString(12));
				list.add(mapdata);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		return list;
	}

	@Override
	public List<Object> getHospitalDeactivionlog(String hospitalCode) throws Exception {
		List<Object> list=new ArrayList<>();
		ResultSet rs=null;
		try {			
			StoredProcedureQuery storedProcedureQuery = entityManager
					.createStoredProcedureQuery("USP_EMP_DEACTIVATION_PROCESS")
					.registerStoredProcedureParameter("P_ACTION_CODE", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPITAL_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_EMP_STATUS", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ACTION_BY", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DOCUMENT_NAME", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_REMARKS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC1", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ADDITIONAL_DOC2", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STATE_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DISTRICT_CODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_OUT", Integer.class, ParameterMode.OUT);
			
			storedProcedureQuery.setParameter("P_ACTION_CODE", 4);
			storedProcedureQuery.setParameter("P_HOSPITAL_CODE", hospitalCode);

			storedProcedureQuery.execute();
			rs = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while(rs.next()) {
				Map<String, Object> mapdata = new HashMap<>();
				mapdata.put("statName", rs.getString(1));
				mapdata.put("distName", rs.getString(2));
				mapdata.put("hospitalCode", rs.getString(3));
				mapdata.put("hospitalName", rs.getString(4));
				mapdata.put("statusval", rs.getString(5));
				mapdata.put("emastatus", rs.getString(6));
				mapdata.put("docname", rs.getString(7));
				mapdata.put("docname1", rs.getString(8));
				mapdata.put("docname2", rs.getString(9));
				mapdata.put("remark", rs.getString(10));
				mapdata.put("actionBy", rs.getString(11));
				mapdata.put("actionon", rs.getString(12));
				list.add(mapdata);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
			if (rs != null) {
				rs.close();
			}
		}
		return list;
	}

	@Override
	public void downLoaddeempanelDoc(String fileName, HttpServletResponse response) throws Exception {
		CommonFileUpload.downLoaddeempanelDoc(fileName,response);		
	}

}
