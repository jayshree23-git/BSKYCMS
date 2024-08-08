package com.project.bsky.repository;


import com.project.bsky.model.MoSarkarLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoSarkarLogRepository extends JpaRepository<MoSarkarLog, Integer> {

    @Query(value = "SELECT\n" +
            "    MSL.API_ID,\n" +
            "    MSL.API_NAME,\n" +
            "    MSL.START_TIME,\n" +
            "    MSL.END_TIME,\n" +
            "    MSL.DATA_SIZE,\n" +
            "    MSL.SUCCESS_DATA_SIZE,\n" +
            "    MSL.FAILED_DATA_SIZE,\n" +
            "    MSL.CREATED_ON,\n" +
            "    MSL.CREATED_BY,\n" +
            "    MSL.ID\n" +
            "FROM MO_SARKAR_LOG MSL\n" +
            "WHERE MSL.API_ID =:apiId\n" +
            "AND EXTRACT(YEAR FROM MSL.CREATED_ON) =:year\n" +
            "AND EXTRACT(MONTH FROM MSL.CREATED_ON) =:month\n" +
            "AND STATUS_FLAG = 0\n" +
            "ORDER BY MSL.CREATED_ON DESC", nativeQuery = true)
    List<Object[]> getReportList(@Param(value = "apiId") Long apiId, @Param(value = "year") int year, @Param(value = "month") int month);
    @Query(value = "FROM MoSarkarLog MSL WHERE MSL.apiId =:apiId AND MSL.id =:reportId AND MSL.statusFlag = 0")
    MoSarkarLog getReportDetails(@Param(value = "apiId") int id, @Param(value = "reportId")  int reportId);
}