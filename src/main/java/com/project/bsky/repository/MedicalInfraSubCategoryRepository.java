package com.project.bsky.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MedicalInfraSubCategory;

@Repository
public interface MedicalInfraSubCategoryRepository extends JpaRepository<MedicalInfraSubCategory, Integer> {

	@Query("select count(*) from MedicalInfraSubCategory where medInfraSubCatName=:medInfraSubCatName")
	Integer checkduplicateSubCategorynm(String medInfraSubCatName);

	@Query("FROM MedicalInfraSubCategory order by medInfraSubCatId desc")
	List<MedicalInfraSubCategory> getDetails();

	@Query("FROM MedicalInfraSubCategory where medInfraSubCatName=:medInfraSubCatName")
	MedicalInfraSubCategory findbyMedInfraSubCatName(String medInfraSubCatName);

	@Query("select count(*) from MedicalInfraSubCategory where medInfraSubCatName=:medInfraSubCatName and medInfracatId.medInfracatId=:medInfracatId")
	Integer checkduplicateSubCategoryname(String medInfraSubCatName, Integer medInfracatId);

	@Query("FROM MedicalInfraSubCategory where medInfraSubCatName=:medInfraSubCatName and medInfracatId.medInfracatId=:medInfracatId")
	MedicalInfraSubCategory findbyMedInfraSubCatName(String medInfraSubCatName, Integer medInfracatId);

	@Query("select medInfraSubCatId as subCategoryId, medInfraSubCatName as subCategoryName from MedicalInfraSubCategory where medInfracatId=:categoryId order by medInfraSubCatId desc")
	List<Map<String, Object>> getMedicalInfraSubcatListById(Integer categoryId);
	
//	@Query("select medInfracatId as categoryId, medInfraCatName as categoryName, "
//			+ "isMandatory as isMandatory FROM MedicalInfraCategory where statusFlag=0 order by medInfracatId")
	@Query( value = "select sci.MEDINFRA_SUB_CAT_ID,sci.MEDICAL_SUB_CAT_NAME,mci.MEDINFRA_CAT_ID,mci.MEDICAL_INFRA_CAT_NAME\r\n"
			+ "	,CASE WHEN NVL(mi.SUB_CATEGORY_ID,'00') = '00' THEN 1 ELSE 0 END AS MEDICALINFRA\r\n"
			+ "	from EMP_MST_MEDICAL_INFRA_SUB_CATEGORY sci\r\n"
			+ "	left join EMP_MEDICAL_INFRA mi on mi.SUB_CATEGORY_ID=sci.MEDINFRA_SUB_CAT_ID AND mi.HOSPITAL_ID=?1 AND MI.STATUSFLAG=0\r\n"
			+ "	left join EMP_MST_MEDICAL_INFRA_CATEGORY mci on sci.MEDINFRA_CAT_ID=mci.MEDINFRA_CAT_ID\r\n"
			+ "	WHERE SCI.STATUSFLAG=0 order by sci.MEDICAL_SUB_CAT_NAME",nativeQuery =true)		
		List<Object[]> empanelmentdcauthorityrepository(Long id);
		@Query( value = "select sci.MEDINFRA_SUB_CAT_ID,sci.MEDICAL_SUB_CAT_NAME,mci.MEDINFRA_CAT_ID,mci.MEDICAL_INFRA_CAT_NAME\r\n"
				+ "	,CASE WHEN NVL(mi.SUB_CATEGORY_ID,'00') = '00' THEN 1 ELSE 0 END AS MEDICALINFRA\r\n"
				+ "	from EMP_MST_MEDICAL_INFRA_SUB_CATEGORY sci\r\n"
				+ "	left join EMP_MEDICAL_INFRA_WEB mi on mi.SUB_CATEGORY_ID=sci.MEDINFRA_SUB_CAT_ID AND mi.HOSPITAL_ID=?1 AND MI.STATUSFLAG=0\r\n"
				+ "	left join EMP_MST_MEDICAL_INFRA_CATEGORY mci on sci.MEDINFRA_CAT_ID=mci.MEDINFRA_CAT_ID\r\n"
				+ "	WHERE SCI.STATUSFLAG=0 order by sci.MEDICAL_SUB_CAT_NAME",nativeQuery =true)	
	List<Object[]> empanelmentdcauthorityrepositoryweb(Long id);


}
