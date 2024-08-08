package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PackageDetails;

@Repository
public interface PackageDetailsRepository extends JpaRepository<PackageDetails, Integer>{

	PackageDetails findByPackageIdAndProcedureCode(String packageId, String procedureCode);

}
