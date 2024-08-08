package com.project.bsky.repository;


import com.project.bsky.model.APIServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface APIServiceLogRepository extends JpaRepository<APIServiceLog, Integer> {
    @Query(value = "SELECT\n" +
            "    ASL.API_ID,\n" +
            "    ASL.API_NAME,\n" +
            "    ASL.API_HIT_TIME,\n" +
            "    ASL.API_END_TIME,\n" +
            "    ASL.DATA_SIZE,\n" +
            "    ASL.CREATED_ON,\n" +
            "    ASL.CREATED_BY,\n" +
            "    ASL.ID\n" +
            "    FROM API_SERVICE_LOG ASL\n" +
            "WHERE ASL.API_ID =:apiId\n" +
            "AND EXTRACT(YEAR FROM ASL.CREATED_ON) =:year\n" +
            "AND EXTRACT(MONTH FROM ASL.CREATED_ON) =:month\n" +
            "AND STATUS_FLAG = 0\n" +
            "ORDER BY ASL.CREATED_ON DESC", nativeQuery = true)
    List<Object[]> getReportList(@Param(value = "apiId") Long apiId, @Param(value = "year") int year, @Param(value = "month") int month);

    @Query(value = "SELECT \n" +
            "    ASL.API_NAME,\n" +
            "    ASL.CREATED_ON,\n" +
            "    ASL.API_HIT_TIME,\n" +
            "    ASL.API_END_TIME,\n" +
            "    ASL.INPUT_DATA,\n" +
            "    ASL.OUTPUT_DATA\n" +
            "FROM API_SERVICE_LOG ASL\n" +
            "WHERE ASL.API_ID =:apiId\n" +
            "AND ASL.ID =:reportId\n" +
            "AND STATUS_FLAG = 0", nativeQuery = true)
   List<Object[]> getReportDetails1(@Param(value = "apiId") Long apiId, @Param(value = "reportId") Long reportId);

    @Query(value = "FROM APIServiceLog ASL WHERE ASL.apiId =:apiId AND ASL.id =:reportId AND ASL.statusFlag = 0")
    APIServiceLog getReportDetails(@Param(value = "apiId") int apiId, @Param(value = "reportId") int reportId);

}