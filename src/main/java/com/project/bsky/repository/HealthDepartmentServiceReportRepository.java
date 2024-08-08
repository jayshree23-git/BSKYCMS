package com.project.bsky.repository;



import com.project.bsky.model.HealthDepartmentServiceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HealthDepartmentServiceReportRepository extends JpaRepository<HealthDepartmentServiceReport, Long> {

    @Query(value = "SELECT\n" +
            "    HDSR.ID,\n" +
            "    HDSR.API_ID,\n" +
            "    HDSR.SERVICE_STATUS,\n" +
            "    HDSR.START_DATE,\n" +
            "    HDSR.END_DATE,\n" +
            "    HDSR.RECORDS_FETCHED,\n" +
            "    HDSR.RECORDS_INSERTED,\n" +
            "    HDSR.RECORDS_UPDATED,\n" +
            "    HDSR.RECORDS_FAILED,\n" +
            "    HDSR.CREATED_ON,\n" +
            "    HDSR.CREATED_BY\n" +
            "FROM HEALTH_DEPARTMENT_SERVICE_REPORT HDSR\n" +
            "WHERE HDSR.API_ID =:apiId\n" +
            "AND EXTRACT(YEAR FROM HDSR.CREATED_ON) =:year\n" +
            "AND EXTRACT(MONTH FROM HDSR.CREATED_ON) =:month\n" +
            "AND HDSR.STATUS_FLAG = 0\n" +
            "ORDER BY HDSR.CREATED_ON DESC", nativeQuery = true)
    List<Object[]> getReportList(@Param(value = "apiId") Long apiId, @Param(value = "year") int year, @Param(value = "month") int month);

    @Query(value = "FROM HealthDepartmentServiceReport HDSR\n" +
            "WHERE HDSR.apiId =:apiId\n" +
            "AND HDSR.id =:reportId\n" +
            "AND HDSR.statusFlag = 0")
    HealthDepartmentServiceReport getReportDetails(@Param(value = "apiId") Long apiId, @Param(value = "reportId") Long reportId);
}