package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.google.gson.JsonObject;
import com.project.bsky.bean.PackageDetailsMasterBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageDetailsMaster;
import com.project.bsky.model.PackageSchemeMapping;
import com.project.bsky.model.PackageSubCategory;
import com.project.bsky.repository.PackageDetailsMasterRepository;
import com.project.bsky.repository.PackageSchemeMappingRepository;
import com.project.bsky.repository.PackageSubCategoryRepository;
import com.project.bsky.service.PackageDetailsService;

import oracle.jdbc.driver.json.binary.JsonpOsonArray;

/**
 * @author Bhasha
 *
 */
@Service
public class PackageDetailsServiceImpl implements PackageDetailsService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PackageSubCategoryRepository packageSubCategoryRepository;

	@Autowired
	private PackageDetailsMasterRepository packageDetailsMasterRepository;

	@Autowired
	private Logger logger;

	@Autowired
	private PackageSchemeMappingRepository schemeMappingRepo;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response savePackageDetails(PackageDetailsMasterBean packageDetailsMasterBean) {
		Integer claimsnoInteger = null;
		Response response = new Response();
//		try {
//			PackageDetailsMaster packageDetailsMaster = new PackageDetailsMaster();
//			packageDetailsMaster.setPackageHeaderCode(packageDetailsMasterBean.getPackageHeaderCode());
//			PackageSubCategory packageSubCategory = packageSubCategoryRepository
//					.findById(Long.parseLong(packageDetailsMasterBean.getPackageSubcatagoryId())).get();
//			packageDetailsMaster.setPackageSubcatagory(packageSubCategory);
//			packageDetailsMaster.setPackageSubcode(packageDetailsMasterBean.getPackageSubcode());
//			packageDetailsMaster.setProcedureCode(packageDetailsMasterBean.getProcedureCode());
//			packageDetailsMaster.setProcedureDescription(packageDetailsMasterBean.getProcedureDescription());
//			packageDetailsMaster.setMandatoryPreauth(packageDetailsMasterBean.getMandatoryPreauth());
//			packageDetailsMaster.setPackageCatagoryType(packageDetailsMasterBean.getPackageCatagoryType());
//			packageDetailsMaster.setMaximumDays(packageDetailsMasterBean.getMaximumDays());
//			packageDetailsMaster.setDayCare(packageDetailsMasterBean.getDayCare());
//			packageDetailsMaster.setStayType(packageDetailsMasterBean.getStayType());
//			packageDetailsMaster.setPackageExtention(packageDetailsMasterBean.getPackageExtention());
//			packageDetailsMaster.setStayType(packageDetailsMasterBean.getStayType());
//			packageDetailsMaster.setPriceEditable(packageDetailsMasterBean.getPriceEditable());
//			packageDetailsMaster.setIsPackageException(packageDetailsMasterBean.getIsPackageException());
//			packageDetailsMaster.setClaimProcessDocs(packageDetailsMasterBean.getClaimProcessDocs());
//			packageDetailsMaster.setPreauthDocs(packageDetailsMasterBean.getPreauthDocs());
//			packageDetailsMaster.setCreatedBy(packageDetailsMasterBean.getCreatedBy());
//			packageDetailsMaster.setUpdatedBy(-1);
//			packageDetailsMaster.setCreatedOn(date);
//			packageDetailsMaster.setUpdatedOn(date);
//			packageDetailsMaster.setDeletedFlag(0);
//			packageDetailsMasterRepository.save(packageDetailsMaster);
//			response.setMessage("PackageDetails Added");
//			response.setStatus("Success");
//		} catch (Exception e) {
//			logger.error(ExceptionUtils.getStackTrace(e));
//			response.setMessage("Some error happen");
//			response.setStatus("Failed");
//		}
//		return response;
		String claimList = null;
		StringBuffer bufferlist = new StringBuffer();
		List<Map<String, Object>> mapObj = packageDetailsMasterBean.getScheme();
		if (!mapObj.isEmpty()) {
			for (Map<String, Object> element : mapObj) {
				bufferlist.append(element.get("schemeCategoryId") + ",");
			}
			claimList = bufferlist.substring(0, bufferlist.length() - 1);
		}
		try {
			PackageSubCategory packageSubCategory = packageSubCategoryRepository
					.findById(Long.parseLong(packageDetailsMasterBean.getPackageSubcatagoryId())).get();
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_T_PACKAGEMASTER")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCATAGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDUREDESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECATAGORYTYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MAXIMUMDAYS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MULTIPROCEDURE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MANDATORYPREAUTH", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STAYTYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DAYCARE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PREAUTHDOCS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMPROCESSDOCS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEMODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEEXCEPTIONFLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEEXTENTION", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRICEEDITABLE", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ISSURGICAL", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEDETAILS_ID", Long.class, ParameterMode.IN) 
					.registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTION", "A");
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", packageDetailsMasterBean.getPackageHeaderCode());
			storedProcedureQuery.setParameter("P_PACKAGESUBCATAGORYID", packageSubCategory.getSubcategoryId());
			storedProcedureQuery.setParameter("P_PACKAGESUBCODE", packageDetailsMasterBean.getPackageSubcode());
			storedProcedureQuery.setParameter("P_PROCEDURECODE", packageDetailsMasterBean.getProcedureCode());
			storedProcedureQuery.setParameter("P_PROCEDUREDESCRIPTION",
					packageDetailsMasterBean.getProcedureDescription());
			storedProcedureQuery.setParameter("P_PACKAGECATAGORYTYPE",
					packageDetailsMasterBean.getPackageCatagoryType());
			storedProcedureQuery.setParameter("P_MAXIMUMDAYS", packageDetailsMasterBean.getMaximumDays());
			storedProcedureQuery.setParameter("P_MULTIPROCEDURE", packageDetailsMasterBean.getMultiProcedure());
			storedProcedureQuery.setParameter("P_MANDATORYPREAUTH", packageDetailsMasterBean.getMandatoryPreauth());
			storedProcedureQuery.setParameter("P_STAYTYPE", packageDetailsMasterBean.getStayType());
			storedProcedureQuery.setParameter("P_DAYCARE", packageDetailsMasterBean.getDayCare());
			storedProcedureQuery.setParameter("P_PREAUTHDOCS", packageDetailsMasterBean.getPreauthDocs());
			storedProcedureQuery.setParameter("P_CLAIMPROCESSDOCS", packageDetailsMasterBean.getClaimProcessDocs());
			storedProcedureQuery.setParameter("P_PACKAGEMODE", null);
			storedProcedureQuery.setParameter("P_PACKAGEEXCEPTIONFLAG",
					packageDetailsMasterBean.getIsPackageException());
			storedProcedureQuery.setParameter("P_PACKAGEEXTENTION",
					packageDetailsMasterBean.getPackageExtention().charAt(0));
			storedProcedureQuery.setParameter("P_PRICEEDITABLE", packageDetailsMasterBean.getPriceEditable().charAt(0));
			storedProcedureQuery.setParameter("P_ISSURGICAL", packageDetailsMasterBean.getIsSurgical());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY", claimList);
			storedProcedureQuery.setParameter("P_USERID", packageDetailsMasterBean.getCreatedBy());
			storedProcedureQuery.setParameter("P_PACKAGEDETAILS_ID", null); 
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Package Details added successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error("Exception Occured in savePackageDetails of PackageDetailsServiceImpl class", e);
		}
		return response;
	}

	@Override
	public List<PackageDetailsMaster> getPackageDetails() {
		List<PackageDetailsMaster> response = new ArrayList<>();
		List<PackageDetailsMaster> packageDetailsMaster = packageDetailsMasterRepository
				.findAll(Sort.by(Sort.Direction.DESC, "id"));
		if (packageDetailsMaster != null) {
			for (PackageDetailsMaster packageDetails : packageDetailsMaster) {
				if (packageDetails != null && packageDetails.getDeletedFlag() == 0) {
					response.add(packageDetails);
				}
			}
		}
		return response;
	}

	@Override
	public String getByPackageDetailsIds(Long id) {
		PackageDetailsMaster packageDetailsMaster = null;
		List<PackageSchemeMapping> schemMapping = null;
		JSONObject packageDetailData = null;
		JSONArray schemeArray = new JSONArray();
		try {
			packageDetailData = new JSONObject();
			packageDetailsMaster = packageDetailsMasterRepository.findById(id).get();
			schemMapping = schemeMappingRepo.findAllByProcedureCode(packageDetailsMaster.getProcedureCode());
			packageDetailData.put("packageDetailsMaster", new JSONObject(packageDetailsMaster));
			if (!schemMapping.isEmpty()) {
				for (PackageSchemeMapping packageSchemeMapping : schemMapping) {
					schemeArray.put(new JSONObject(packageSchemeMapping));
				}
			}
			packageDetailData.put("schemMapping", schemeArray);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packageDetailData.toString();
	}

	@Override
	public Response deletePackageDetails(Long id) {
		Response response = new Response();
		try {
			PackageDetailsMaster packageDetailsMaster = this.getByPackageDetailsId(id);
			packageDetailsMaster.setDeletedFlag(1);
			packageDetailsMasterRepository.save(packageDetailsMaster);
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
	public Response updatePackageDetails(Long id, PackageDetailsMasterBean packageDetailsMasterBean) {
		Integer claimsnoInteger = null;
		Response response = new Response();
//		try {
//			PackageDetailsMaster packageDetailsMaster = this.getByPackageDetailsId(id);
//			if (Objects.isNull(packageDetailsMaster)) {
//				response.setMessage("Package Details Already Exist");
//			}
//			packageDetailsMaster.setId(id);
//			packageDetailsMaster.setPackageHeaderCode(packageDetailsMasterBean.getPackageHeaderCode());
//			PackageSubCategory packageSubCategory = packageSubCategoryRepository
//					.findById(Long.parseLong(packageDetailsMasterBean.getPackageSubcatagoryId())).get();
//			packageDetailsMaster.setPackageSubcatagory(packageSubCategory);
//			packageDetailsMaster.setPackageSubcode(packageDetailsMasterBean.getPackageSubcode());
//			packageDetailsMaster.setProcedureCode(packageDetailsMasterBean.getProcedureCode());
//			packageDetailsMaster.setProcedureDescription(packageDetailsMasterBean.getProcedureDescription());
//			packageDetailsMaster.setPackageCatagoryType(packageDetailsMasterBean.getPackageCatagoryType());
//			packageDetailsMaster.setMaximumDays(packageDetailsMasterBean.getMaximumDays());
//			packageDetailsMaster.setMandatoryPreauth(packageDetailsMasterBean.getMandatoryPreauth());
//			packageDetailsMaster.setDayCare(packageDetailsMasterBean.getDayCare());
//			packageDetailsMaster.setStayType(packageDetailsMasterBean.getStayType());
//			packageDetailsMaster.setPreauthDocs(packageDetailsMasterBean.getPreauthDocs());
//			packageDetailsMaster.setPackageExtention(packageDetailsMasterBean.getPackageExtention());
//			packageDetailsMaster.setClaimProcessDocs(packageDetailsMasterBean.getClaimProcessDocs());
//			packageDetailsMaster.setPriceEditable(packageDetailsMasterBean.getPriceEditable());
//			packageDetailsMaster.setIspackageException(packageDetailsMasterBean.getIsPackageException());
//			packageDetailsMaster.setCreatedBy(packageDetailsMaster.getCreatedBy());
//			packageDetailsMaster.setUpdatedBy(packageDetailsMasterBean.getUpdatedBy());
//			packageDetailsMaster.setCreatedOn(packageDetailsMaster.getCreatedOn());
//			packageDetailsMaster.setUpdatedOn(date);
//			packageDetailsMaster.setDeletedFlag(0);
//			packageDetailsMasterRepository.save(packageDetailsMaster);
//			response.setMessage("Package Details Updated");
//			response.setStatus("Success");
//
//		} catch (Exception e) {
//			logger.error(ExceptionUtils.getStackTrace(e));
//			response.setMessage("Some error happen");
//			response.setStatus("Failed");
//		}
//		return response;
		
		String claimList = null;
		StringBuffer bufferlist = new StringBuffer();
		List<Map<String, Object>> mapObj = packageDetailsMasterBean.getScheme();
		if (!mapObj.isEmpty()) {
			for (Map<String, Object> element : mapObj) {
				bufferlist.append(element.get("schemeCategoryId") + ",");
			}
			claimList = bufferlist.substring(0, bufferlist.length() - 1);
		}
		try {
			PackageSubCategory packageSubCategory = packageSubCategoryRepository
					.findById(Long.parseLong(packageDetailsMasterBean.getPackageSubcatagoryId())).get();
			StoredProcedureQuery storedProcedureQuery = this.entityManager
					.createStoredProcedureQuery("USP_T_PACKAGEMASTER")
					.registerStoredProcedureParameter("P_ACTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEHEADERCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCATAGORYID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGESUBCODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDURECODE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PROCEDUREDESCRIPTION", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGECATAGORYTYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MAXIMUMDAYS", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MULTIPROCEDURE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_MANDATORYPREAUTH", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_STAYTYPE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_DAYCARE", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PREAUTHDOCS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_CLAIMPROCESSDOCS", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEMODE", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEEXCEPTIONFLAG", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEEXTENTION", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PRICEEDITABLE", Character.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_ISSURGICAL", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_SCHEMECATEGORY", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_USERID", Long.class, ParameterMode.IN)
					.registerStoredProcedureParameter("P_PACKAGEDETAILS_ID", Long.class, ParameterMode.IN)
				    .registerStoredProcedureParameter("P_MSGOUT", Integer.class, ParameterMode.OUT);

			storedProcedureQuery.setParameter("P_ACTION", "B");
			storedProcedureQuery.setParameter("P_PACKAGEHEADERCODE", packageDetailsMasterBean.getPackageHeaderCode());
			storedProcedureQuery.setParameter("P_PACKAGESUBCATAGORYID", packageSubCategory.getSubcategoryId());
			storedProcedureQuery.setParameter("P_PACKAGESUBCODE", packageDetailsMasterBean.getPackageSubcode());
			storedProcedureQuery.setParameter("P_PROCEDURECODE", packageDetailsMasterBean.getProcedureCode());
			storedProcedureQuery.setParameter("P_PROCEDUREDESCRIPTION",
					packageDetailsMasterBean.getProcedureDescription());
			storedProcedureQuery.setParameter("P_PACKAGECATAGORYTYPE",
					packageDetailsMasterBean.getPackageCatagoryType());
			storedProcedureQuery.setParameter("P_MAXIMUMDAYS", packageDetailsMasterBean.getMaximumDays());
			storedProcedureQuery.setParameter("P_MULTIPROCEDURE", packageDetailsMasterBean.getMultiProcedure());
			storedProcedureQuery.setParameter("P_MANDATORYPREAUTH", packageDetailsMasterBean.getMandatoryPreauth());
			storedProcedureQuery.setParameter("P_STAYTYPE", packageDetailsMasterBean.getStayType());
			storedProcedureQuery.setParameter("P_DAYCARE", packageDetailsMasterBean.getDayCare());
			storedProcedureQuery.setParameter("P_PREAUTHDOCS", packageDetailsMasterBean.getPreauthDocs());
			storedProcedureQuery.setParameter("P_CLAIMPROCESSDOCS", packageDetailsMasterBean.getClaimProcessDocs());
			storedProcedureQuery.setParameter("P_PACKAGEMODE", null);
			storedProcedureQuery.setParameter("P_PACKAGEEXCEPTIONFLAG",
					packageDetailsMasterBean.getIsPackageException());
			storedProcedureQuery.setParameter("P_PACKAGEEXTENTION",
					packageDetailsMasterBean.getPackageExtention().charAt(0));
			storedProcedureQuery.setParameter("P_PRICEEDITABLE", packageDetailsMasterBean.getPriceEditable().charAt(0));
			storedProcedureQuery.setParameter("P_ISSURGICAL", packageDetailsMasterBean.getIsSurgical());
			storedProcedureQuery.setParameter("P_SCHEMECATEGORY", claimList);
			storedProcedureQuery.setParameter("P_USERID", packageDetailsMasterBean.getCreatedBy());
			storedProcedureQuery.setParameter("P_PACKAGEDETAILS_ID", id); 
			storedProcedureQuery.execute();
			claimsnoInteger = (Integer) storedProcedureQuery.getOutputParameterValue("P_MSGOUT");
			if (claimsnoInteger == 1) {
				response.setStatus("Success");
				response.setMessage("Package Details updated successfully");
			} else {
				response.setStatus("Failed");
				response.setMessage("Action taken Can Not Processed");
			}
		} catch (Exception e) {
			logger.error("Exception Occured in savePackageDetails of PackageDetailsServiceImpl class", e);
		}
		return response;
	}

	@Override
	public PackageDetailsMaster getByPackageDetailsId(Long id) {
		PackageDetailsMaster packageDetailsMaster = null;
		try {
			packageDetailsMaster = packageDetailsMasterRepository.findById(id).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packageDetailsMaster;
	}

}
