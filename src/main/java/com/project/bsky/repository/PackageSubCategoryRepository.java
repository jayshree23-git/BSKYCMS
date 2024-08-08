package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PackageSubCategory;

import java.util.List;

@Repository
public interface PackageSubCategoryRepository extends JpaRepository<PackageSubCategory, Long> {

	PackageSubCategory findBySubcategoryId(Long subcategoryId);

	@Query("From PackageSubCategory where packageheadercode = :packageheadercode")
	List<PackageSubCategory> findDetails(String packageheadercode);
	
	@Query("SELECT g.subcategoryId FROM PackageSubCategory g WHERE g.packagesubcategoryname=:packagesubcategoryname")
	Long getSubCategoryIdBySubcategoryName(String packagesubcategoryname);
	
	@Query("SELECT g.subcategoryId FROM PackageSubCategory g WHERE g.packagesubcategorycode=:packagesubcategorycode")
	Long getSubCategoryIdBySubcategoryCode(String packagesubcategorycode);
	
	@Query("SELECT Count(1) FROM PackageSubCategory g WHERE g.packagesubcategorycode=:packagesubcategorycode")
	Long getSubCategoryIdBySubcategoryCode1(String packagesubcategorycode);

	PackageSubCategory findByPackageheadercodeAndPackagesubcategorynameAndPackagesubcategorycode(
			String packageheadercode, String packagesubcategoryname, String packagesubcategorycode);

	PackageSubCategory findBypackagesubcategorycode(String packagesubcategorycode);
}
