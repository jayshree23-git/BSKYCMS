package com.project.bsky.repository;

import com.project.bsky.model.HealthDepartmentMemberDetailsAadharaauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HealthDepartmentMemberDetailsAadharaauthRepository extends JpaRepository<HealthDepartmentMemberDetailsAadharaauth, Long> {
    @Query("SELECT COUNT(e) FROM HealthDepartmentMemberDetailsAadharaauth e WHERE e.healthmemeberslno = :healthMemeberslno")
    int checkHealthDepartmentMemberDetailsAadharaauthByHealthMemberSlNo(@Param(value = "healthMemeberslno") Long healthMemeberslno);
}