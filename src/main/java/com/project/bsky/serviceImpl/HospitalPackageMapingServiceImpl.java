package com.project.bsky.serviceImpl;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.HospitalMappingBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.HospitalPackageMapping;
import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.PackageHeader;
import com.project.bsky.model.PackageSubCategory;
import com.project.bsky.repository.HospitalInformationRepository;
import com.project.bsky.repository.HospitalPackageMappingTmsRepo;
import com.project.bsky.repository.PackageDetailsHospitalRepository;
import com.project.bsky.repository.PackageHeaderRepo;
import com.project.bsky.repository.PackageSubCategoryRepository;
import com.project.bsky.service.HospitalPackageMappingService;
import com.project.bsky.util.DateFormat;

@Service
public class HospitalPackageMapingServiceImpl implements HospitalPackageMappingService {

	@Autowired
	private Logger logger;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PackageHeaderRepo hospitalPackageMappingRepository;

	@Autowired
	private PackageDetailsHospitalRepository packageDetailsHospitalRepository;
	@Autowired
	private HospitalPackageMappingTmsRepo hospitalPackageMappingTmsRepo;

	@Autowired
	private HospitalInformationRepository hospitalInformationRepository;
	@Autowired
	private PackageSubCategoryRepository packageSubCategoryRepository;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public List<PackageDetailsHospital> getpackageDetailsDescrition(Long packageSubcategoryId,
			Integer hospitalcategoryid) {
		List<PackageDetailsHospital> headerResponse = new ArrayList<>();
		List<PackageDetailsHospital> findAll = packageDetailsHospitalRepository.findDetails(packageSubcategoryId,
				hospitalcategoryid);
		if (findAll != null) {
			for (PackageDetailsHospital packageDetailsHospital : findAll) {
				if (packageDetailsHospital != null && packageDetailsHospital.getDeletedFlag() == 0) {
					headerResponse.add(packageDetailsHospital);
				}
			}
		}
		return headerResponse;
	}

	@Override
	public Response saveHospitalPackageMapping(HospitalMappingBean hospitalMappingBean) {
		Response response = new Response();
		try {
			HospitalPackageMapping hospitalPackageMapping = new HospitalPackageMapping();
			hospitalPackageMapping.setHospitalState(hospitalMappingBean.getHospitalState());
			hospitalPackageMapping.setHospitalDistrict(hospitalMappingBean.getHospitalDistrict());
			PackageSubCategory p = packageSubCategoryRepository.findById(hospitalMappingBean.getPackageSubcategoryId())
					.get();
			hospitalPackageMapping.setPackageSubcategoryId(p);
			HospitalInformation h = hospitalInformationRepository
					.findHospitalByCode(hospitalMappingBean.getHospitalCode());
			hospitalPackageMapping.setHospitalCode(h);
			PackageDetailsHospital packageDetailsHospital = packageDetailsHospitalRepository
					.findById(Long.parseLong(hospitalMappingBean.getPackageDetailsId())).get();
			hospitalPackageMapping.setPackageDetailsId(packageDetailsHospital);
			PackageHeader packageHeader = hospitalPackageMappingRepository
					.findById(hospitalMappingBean.getPackageHeaderId()).get();
			hospitalPackageMapping.setPackageHeaderId(packageHeader);
			hospitalPackageMapping.setCreatedBy(hospitalMappingBean.getCreatedBy());
			hospitalPackageMapping.setCreatedOn(date);
			hospitalPackageMapping.setDeletedFlag(0);
			hospitalPackageMappingTmsRepo.save(hospitalPackageMapping);
			response.setMessage("Hospital Package Mapping Added");
			response.setStatus("success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<HospitalPackageMapping> getAllHospitalPackageMapping() {
		List<HospitalPackageMapping> response = new ArrayList<>();
		List<HospitalPackageMapping> findAll = hospitalPackageMappingTmsRepo
				.findAll(Sort.by(Sort.Direction.DESC, "id"));
		;
		if (findAll != null) {
			for (HospitalPackageMapping hospitalPackageMapping : findAll) {
				if (hospitalPackageMapping != null && hospitalPackageMapping.getDeletedFlag() == 0) {
					response.add(hospitalPackageMapping);
				}
			}
		}
		return response;
	}

	@Override
	public HospitalPackageMapping getByHospitalpackageMapping(Long id) {
		HospitalPackageMapping hospitalPackageMapping = null;
		try {
			hospitalPackageMapping = hospitalPackageMappingTmsRepo.findById(id).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalPackageMapping;
	}

	@Override
	public Response deleteHospitalMappingById(Long id) {
		Response response = new Response();
		try {
			HospitalPackageMapping hospitalPackageMapping = hospitalPackageMappingTmsRepo.findById(id).get();
			hospitalPackageMapping.setDeletedFlag(1);
			hospitalPackageMappingTmsRepo.save(hospitalPackageMapping);
			response.setMessage("Record Successfully In-Active");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Response updateHospitalMappingById(Long id, HospitalMappingBean hospitalMappingBean) {
		Response response = new Response();
		try {
			HospitalPackageMapping hospitalPackageMappingResponse = this.getByHospitalpackageMapping(id);
			if (Objects.isNull(hospitalPackageMappingResponse)) {
				response.setMessage("HospitalPackageMapping  Already Exist");
			}
			HospitalPackageMapping hospitalPackageMapping = new HospitalPackageMapping();
			hospitalPackageMapping.setHospitalState(hospitalMappingBean.getHospitalState());
			hospitalPackageMapping.setHospitalDistrict(hospitalMappingBean.getHospitalDistrict());
			PackageSubCategory p = packageSubCategoryRepository.findById(hospitalMappingBean.getPackageSubcategoryId())
					.get();
			hospitalPackageMapping.setPackageSubcategoryId(p);
			HospitalInformation h = hospitalInformationRepository
					.findHospitalByCode(hospitalMappingBean.getHospitalCode());
			hospitalPackageMapping.setHospitalCode(h);
			PackageDetailsHospital packageDetailsHospital = packageDetailsHospitalRepository
					.findById(Long.parseLong(hospitalMappingBean.getPackageDetailsId())).get();
			hospitalPackageMapping.setPackageDetailsId(packageDetailsHospital);
			PackageHeader packageHeader = hospitalPackageMappingRepository
					.findById(hospitalMappingBean.getPackageHeaderId()).get();
			hospitalPackageMapping.setPackageHeaderId(packageHeader);
			hospitalPackageMapping.setId(id);
			hospitalPackageMapping.setUpdatedBy(hospitalMappingBean.getUpdatedBy());
			hospitalPackageMapping.setUpdatedOn(date);
			hospitalPackageMapping.setDeletedFlag(0);
			hospitalPackageMapping.setCreatedBy(hospitalPackageMappingResponse.getCreatedBy());
			hospitalPackageMapping.setCreatedOn(hospitalPackageMappingResponse.getCreatedOn());
			hospitalPackageMappingTmsRepo.save(hospitalPackageMapping);
			response.setMessage("Hospital Package Mapping Updated");
			response.setStatus("Success");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<PackageSubCategory> getPackageSubcategory(String packageheadercode) {
		List<PackageSubCategory> headerResponse = new ArrayList<>();
		List<PackageSubCategory> findAll = packageSubCategoryRepository.findDetails(packageheadercode);
		if (findAll != null) {
			for (PackageSubCategory packageSubCategory : findAll) {
				if (packageSubCategory != null && packageSubCategory.getDeletedFlag() == 0) {
					headerResponse.add(packageSubCategory);
				}
			}
		}
		return headerResponse;
	}

	@Override
	public Map<String, Object> getPackageDetailByCode(String packageCode, String subPackageCode, String procedureCode,
			String hospitalCode) throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		Map<String, Object> details = new HashMap<String, Object>();
		ResultSet snoDetailsObj = null, snoDetailsObj1 = null,snoDetailsObj2 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_PACKAGE_INFO_DTLS")
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_HOSPCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_OUT1", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_OUT2", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", packageCode);
			storedProcedureQuery.setParameter("P_PACKAGESUBCODE", subPackageCode);
			storedProcedureQuery.setParameter("P_PROCEDURECODE", procedureCode);
			storedProcedureQuery.setParameter("P_HOSPCODE", hospitalCode);
			storedProcedureQuery.execute();
			snoDetailsObj = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			snoDetailsObj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT1");
			snoDetailsObj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT2");

			while (snoDetailsObj.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("packageCode", snoDetailsObj.getString(1));
				jsonObject.put("packageName", snoDetailsObj.getString(2));
				jsonObject.put("subPackageCode", snoDetailsObj.getString(3));
				jsonObject.put("subCategoryName", snoDetailsObj.getString(4));
				jsonObject.put("procedureCode", snoDetailsObj.getString(5));
				jsonObject.put("procedureName", snoDetailsObj.getString(6));
				jsonObject.put("hospitalCatName", snoDetailsObj.getString(7));
				jsonObject.put("amount", snoDetailsObj.getLong(8));
				jsonObject.put("preAuthDocs", snoDetailsObj.getString(9));
				jsonObject.put("claimProcessDocs", snoDetailsObj.getString(10));
				jsonObject.put("MaximumDays", snoDetailsObj.getString(11));
				jsonObject.put("WardType", snoDetailsObj.getString(12));
				jsonObject.put("DayCare", snoDetailsObj.getString(13));
				jsonObject.put("exceptionType", snoDetailsObj.getString(14));
				jsonObject.put("packageCatType", snoDetailsObj.getString(15));
				jsonArray.put(jsonObject);
			}

			while (snoDetailsObj1.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("packageCode", snoDetailsObj1.getString(1));
				jsonObject.put("packageName", snoDetailsObj1.getString(2));
				jsonObject.put("subPackageCode", snoDetailsObj1.getString(3));
				jsonObject.put("subCategoryName", snoDetailsObj1.getString(4));
				jsonObject.put("procedureCode", snoDetailsObj1.getString(5));
				jsonObject.put("procedureName", snoDetailsObj1.getString(6));
				jsonObject.put("hospitalCatName", snoDetailsObj1.getString(7));
				jsonObject.put("amount", snoDetailsObj1.getLong(8));
				jsonObject.put("preAuthDocs", snoDetailsObj1.getString(9));
				jsonObject.put("claimProcessDocs", snoDetailsObj1.getString(10));
				jsonObject.put("MaximumDays", snoDetailsObj1.getString(11));
				jsonObject.put("WardType", snoDetailsObj1.getString(12));
				jsonObject.put("DayCare", snoDetailsObj1.getString(13));
				jsonObject.put("exceptionType", snoDetailsObj1.getString(14));
				jsonObject.put("packageCatType", snoDetailsObj1.getString(15));
				jsonArray1.put(jsonObject);
			}
			
			while (snoDetailsObj2.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("procedurecode", snoDetailsObj2.getString(1));
				jsonObject.put("hospitalcategoryName", snoDetailsObj2.getString(2));
				jsonObject.put("wardname", snoDetailsObj2.getString(3));
				jsonObject.put("currentamount", snoDetailsObj2.getString(4));
				jsonObject.put("previousAmount", snoDetailsObj2.getString(5));
				jsonObject.put("updatedon", snoDetailsObj2.getString(6));
				jsonObject.put("remark", snoDetailsObj2.getString(7));
				jsonArray2.put(jsonObject);
			}

			details.put("packageInfo", jsonArray.toString());
			details.put("overallInfo", jsonArray1.toString());
			details.put("PackageCostUpdationLog", jsonArray2.toString());

		} catch (Exception e) {
			logger.error("Error in getPackageDetailByCode method of HospitalPackageMapingServiceImpl class:", e);
			throw e;
		} finally {
			try {
				if (snoDetailsObj != null) {
					snoDetailsObj.close();
				}
				if (snoDetailsObj1 != null) {
					snoDetailsObj1.close();
				}
				if (snoDetailsObj2 != null) {
					snoDetailsObj2.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getPackageDetailByCode method of HospitalPackageMapingServiceImpl class:", e2);
			}

		}
		return details;
	}

	@Override
	public String getAuthenticationDetails(String urn, String memberid, Integer flag, String Hospitalcode, String caseno)
			throws Exception {
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray jsonArray3 = new JSONArray();
		JSONObject jsonObject;
		JSONObject jsonObject1;
		JSONObject jsonObject2;
		JSONObject jsonObject3;
		JSONObject details = new JSONObject();
		ResultSet Obj1 = null;
		ResultSet Obj2 = null;
		ResultSet sObj3 = null;
		ResultSet obj4 = null;
		try {

			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_AUTHENTICATION_MODE_DTLS")
					.registerStoredProcedureParameter("P_URNNUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_MEMEBRID", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_FLAG", Integer.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_Hospitalcode", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CASENO", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_OUT1", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_OUT2", void.class, ParameterMode.REF_CURSOR)
					.registerStoredProcedureParameter("P_MSG_OUT3", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_URNNUMBER", urn.trim());
			storedProcedureQuery.setParameter("p_MEMEBRID", memberid.trim());
			storedProcedureQuery.setParameter("p_FLAG", flag);
			storedProcedureQuery.setParameter("P_Hospitalcode", Hospitalcode.trim());
			storedProcedureQuery.setParameter("P_CASENO", caseno);
			storedProcedureQuery.execute();
			Obj1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			Obj2 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT1");
			sObj3 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT2");
			obj4 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT3");
			while (Obj1.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("hospitalcode", Obj1.getString(1));
				jsonObject.put("createdon", Obj1.getString(2));
				jsonObject.put("verifystatus", Obj1.getString(3));
				if (Obj1.getString(4).equalsIgnoreCase("0301")) {
					jsonObject.put("verifiedthrough", "Blocking");
				} else if (Obj1.getString(4).equalsIgnoreCase("0303")) {
					jsonObject.put("verifiedthrough", "Discharge");
				}
				jsonObject.put("POSFLAG", Obj1.getString(5));
				jsonObject.put("hospitalname", Obj1.getString(6));
				jsonObject.put("VERIFYDATE", Obj1.getString(7));
				jsonObject.put("VERIFYDATE1", DateFormat.dateConvertor(Obj1.getString(7), ""));
				jsonObject.put("POSCODE", Obj1.getString(8));
				jsonObject.put("AUTHENTICATIONSTATUS", Obj1.getString(9));
				jsonObject.put("verifiedmembername", Obj1.getString(10));
				jsonArray.put(jsonObject);
			}
			while (Obj2.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("hospitalcode", Obj2.getString(1));
				jsonObject1.put("createdon", Obj2.getString(2));
				jsonObject1.put("verifystatus", Obj2.getString(3));
				if (Obj2.getString(4).equalsIgnoreCase("0301")) {
					jsonObject1.put("verifiedthrough", "Blocking");
				} else if (Obj2.getString(4).equalsIgnoreCase("0303")) {
					jsonObject1.put("verifiedthrough", "Discharge");
				}
				jsonObject1.put("hospitalname", Obj2.getString(5));
				jsonObject1.put("verifiedmembername", Obj2.getString(6));
				jsonArray1.put(jsonObject1);
			}
			while (sObj3.next()) {
				jsonObject2 = new JSONObject();
				jsonObject2.put("hospitalcode", sObj3.getString(1));
				jsonObject2.put("createdon", sObj3.getString(2));
				if (sObj3.getString(3).equalsIgnoreCase("0301")) {
					jsonObject2.put("verifiedthrough", "Blocking");
				} else if (sObj3.getString(3).equalsIgnoreCase("0303")) {
					jsonObject2.put("verifiedthrough", "Discharge");
				}
				jsonObject2.put("hospitalname", sObj3.getString(4));
				jsonObject2.put("verifiedmembername", sObj3.getString(5));
				jsonArray2.put(jsonObject2);
			}
			while (obj4.next()) {
				jsonObject3 = new JSONObject();
				jsonObject3.put("hospitalCode", obj4.getString(1));
				jsonObject3.put("createdOn", obj4.getString(2));
				jsonObject3.put("verifyStatus", obj4.getString(3));
				jsonObject3.put("verifyThrough", obj4.getString(4));
				jsonObject3.put("faceAuthFlag", obj4.getString(5));
				jsonObject3.put("hospitalName", obj4.getString(6));
				jsonObject3.put("verifyDate", obj4.getString(7));
				jsonObject3.put("faceAuthId", obj4.getString(8));
				jsonObject3.put("authenticationStatus", obj4.getString(9));
				jsonObject3.put("verifiedMemberName", obj4.getString(10));
				jsonArray3.put(jsonObject3);
			}
			details.put("posdetails", jsonArray);
			details.put("otpdetaiils", jsonArray1);
			details.put("irisdetails", jsonArray2);
			details.put("facedetails", jsonArray3);

		} catch (Exception e) {
			logger.error("Error in getPackageDetailByCode method of getAuthenticationDetails class:", e);
			throw e;
		} finally {
			try {
				if (Obj1 != null) {
					Obj1.close();
				}
				if (Obj2 != null) {
					Obj2.close();
				}
				if (sObj3 != null) {
					sObj3.close();
				}
				if (obj4 != null) {
					obj4.close();
				}
			} catch (Exception e2) {
				logger.error("Error in getPackageDetailByCode method of getAuthenticationDetails class:", e2);
			}

		}
		return details.toString();
	}

	@Override
	public String getOverridecodeDetails(String overridecode, String urn, Long memberid, String hospitalcode)
			throws Exception {
		JSONArray jsonArray1 = new JSONArray();
		JSONObject jsonObject1;
		JSONObject details = new JSONObject();
		ResultSet object1 = null;
		try {
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_OVERRIDECODE_DTLS")
					.registerStoredProcedureParameter("P_OVERRIDECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_URNNUMBER", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MEBERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("p_HOSPITALCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MSG_OUT", void.class, ParameterMode.REF_CURSOR);

			storedProcedureQuery.setParameter("P_OVERRIDECODE", overridecode.trim());
			storedProcedureQuery.setParameter("P_URNNUMBER", urn.trim());
			storedProcedureQuery.setParameter("P_MEBERID", memberid);
			storedProcedureQuery.setParameter("p_HOSPITALCODE", hospitalcode.trim());
			storedProcedureQuery.execute();
			object1 = (ResultSet) storedProcedureQuery.getOutputParameterValue("P_MSG_OUT");
			while (object1.next()) {
				jsonObject1 = new JSONObject();
				jsonObject1.put("urnnumber", object1.getString(1));
				jsonObject1.put("noofdays", object1.getString(2));
				jsonObject1.put("hospitaldescription", object1.getString(3));
				jsonObject1.put("approvedtypes", object1.getString(4));
				jsonObject1.put("requestedday", object1.getDate(5));
				jsonObject1.put("approvalday", object1.getDate(6));
				jsonObject1.put("requestedday1", DateFormat.dateConvertor(String.valueOf(object1.getDate(5)), ""));
				jsonObject1.put("approvalday1", DateFormat.dateConvertor(String.valueOf(object1.getDate(6)), ""));
				jsonObject1.put("createdon", new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(object1.getDate(7)));
				jsonObject1.put("fullname", object1.getString(8));
				jsonObject1.put("phone", object1.getString(9));
				jsonObject1.put("Dcdcdescription", object1.getString(10));
				jsonObject1.put("pdffile", object1.getString(11) != null ? object1.getString(11) : "N/A");
				jsonObject1.put("hospitalcode", object1.getString(12));
				jsonObject1.put("hospitalname", object1.getString(13));
				jsonArray1.put(jsonObject1);
			}
			details.put("overridecodedetails", jsonArray1);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return details.toString();
	}
}