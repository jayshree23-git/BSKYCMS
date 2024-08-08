package com.project.bsky.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MedicalInfraCategory;

@Repository
public interface MedicalInfraCategoryRepository extends JpaRepository<MedicalInfraCategory, Integer> {

	@Query("select count(*) from MedicalInfraCategory where medInfraCatName=:medInfraCatName")
	Integer checkduplicateCategoryname(String medInfraCatName);

	@Query("FROM MedicalInfraCategory order by medInfracatId desc")
	List<MedicalInfraCategory> getDetails();

	MedicalInfraCategory findBymedInfraCatName(String medInfraCatName);

	@Query("FROM MedicalInfraCategory where statusFlag=0 order by medInfracatId desc")
	List<MedicalInfraCategory> getActiveCategoryList();

	@Query("select medInfraCatName FROM MedicalInfraCategory where statusFlag=0 and medInfracatId=:medInfracatId")
	String findBycatId(Integer medInfracatId);

	@Query("select medInfracatId as categoryId, medInfraCatName as categoryName, "
			+ " replace(TRIM(medInfraCatName),' ','') as checkBoxIdName, isMandatory as isMandatory "
			+ " FROM MedicalInfraCategory where statusFlag=0 order by medInfracatId")
	List<Map<String, Object>> getMedicalInfraCategoryList();

}
