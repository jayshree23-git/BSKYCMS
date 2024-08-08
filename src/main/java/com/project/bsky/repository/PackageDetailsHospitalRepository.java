package com.project.bsky.repository;

import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.UserDetailsProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageDetailsHospitalRepository extends JpaRepository<PackageDetailsHospital, Long> {
	// @Query(value="select DISTINCT procedureDescription from packagedetails c
	// where c.packageHeaderCode=packageheadercode;",nativeQuery = true)

	@Query("FROM PackageDetailsHospital where packageHeaderCode=:packageheadercode")
	List<PackageDetailsHospital> findDetails(String packageheadercode);

	@Query("FROM PackageDetailsHospital where packageSubcategoryId=:packageSubcategoryId AND hospitalCategoryId=:hospitalcategoryid")
	List<PackageDetailsHospital> findDetails(Long packageSubcategoryId, Integer hospitalcategoryid);

	@Query("FROM PackageDetailsHospital where packageHeaderCode=:packageHeaderCode AND packageSubCode=:packageSubCode AND procedureCode=:procedureCode")
	List<PackageDetailsHospital> findPackageDetails(String packageHeaderCode, String packageSubCode,
			String procedureCode);

}
