package com.project.bsky.repository;

import com.project.bsky.model.HealthDepartmentMemberDetailsAadharaauthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface HealthDepartmentMemberDetailsAadharaauthLogRepository extends JpaRepository<HealthDepartmentMemberDetailsAadharaauthLog, Long> {

    @Query(value = "FROM HealthDepartmentMemberDetailsAadharaauthLog HDMDAL WHERE HDMDAL.createdOn BETWEEN :startDate AND :endDate")
    List<HealthDepartmentMemberDetailsAadharaauthLog> getReportDetails(@Param(value = "startDate") Date startDate, @Param(value = "endDate")Date endDate);

    @Query(value = "FROM HealthDepartmentMemberDetailsAadharaauthLog HDMDAL WHERE HDMDAL.createdOn BETWEEN :startDate AND :endDate and HDMDAL.dataStatus = :dataStatus")
    List<HealthDepartmentMemberDetailsAadharaauthLog> getReportDetails(@Param(value = "startDate")Date startDate, @Param(value = "endDate")Date endDate, @Param(value = "dataStatus") int dataStatus);
}