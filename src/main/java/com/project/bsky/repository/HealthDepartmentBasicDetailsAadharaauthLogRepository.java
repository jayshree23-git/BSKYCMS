package com.project.bsky.repository;

import com.project.bsky.model.HealthDepartmentBasicDetailsAadharaauthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface HealthDepartmentBasicDetailsAadharaauthLogRepository extends JpaRepository<HealthDepartmentBasicDetailsAadharaauthLog, Long> {
    @Query(value = "FROM HealthDepartmentBasicDetailsAadharaauthLog HDBDAL WHERE HDBDAL.createdOn BETWEEN :startDate AND :endDate")
    List<HealthDepartmentBasicDetailsAadharaauthLog> getReportDetails(@Param(value = "startDate")Date startDate, @Param(value = "endDate")Date endDate);

    @Query(value = "FROM HealthDepartmentBasicDetailsAadharaauthLog HDBDAL WHERE HDBDAL.createdOn BETWEEN :startDate AND :endDate and HDBDAL.dataStatus = :dataStatus")
    List<HealthDepartmentBasicDetailsAadharaauthLog> getReportDetails(@Param(value = "startDate")Date startDate, @Param(value = "endDate")Date endDate, @Param(value = "dataStatus") int dataStatus);
}