package com.project.bsky.repository;

import com.project.bsky.model.HealthDepartmentBasicDetailsAadharaauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HealthDepartmentBasicDetailsAadharaauthRepository extends JpaRepository<HealthDepartmentBasicDetailsAadharaauth, Long> {
    @Query("SELECT COUNT(e) FROM HealthDepartmentBasicDetailsAadharaauth e WHERE e.healthslno = :healthSlNo")
    int checkHealthDepartmentBasicDetailsAadharaauthByHealthSlNo(@Param(value = "healthSlNo") Long healthSlNo);
}