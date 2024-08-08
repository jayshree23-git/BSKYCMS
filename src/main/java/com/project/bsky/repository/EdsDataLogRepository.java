package com.project.bsky.repository;


import com.project.bsky.model.EdsDataLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EdsDataLogRepository extends JpaRepository<EdsDataLog, Integer> {

    @Query(value = "SELECT\n" +
            "    EDL.ID,\n" +
            "    EDL.API_ID,\n" +
            "    EDL.START_TIME,\n" +
            "    EDL.END_TIME,\n" +
            "    EDL.RECORDS_FETCH,\n" +
            "    EDL.RECORDS_INSERTED,\n" +
            "    EDL.RECORDS_UPDATED,\n" +
            "    EDL.RECORDS_FAILED,\n" +
            "    EDL.CREATED_ON,\n" +
            "    EDL.CREATED_BY\n" +
            "FROM EDS_DATA_LOG EDL\n" +
            "WHERE EDL.API_ID =:apiId\n" +
            "AND EXTRACT(YEAR FROM EDL.CREATED_ON) =:year\n" +
            "AND EXTRACT(MONTH FROM EDL.CREATED_ON) =:month\n" +
            "AND STATUS_FLAG = 0\n" +
            "ORDER BY EDL.CREATED_ON DESC", nativeQuery = true)
    List<Object[]> getReportList(@Param(value = "apiId") Long apiId, @Param(value = "year") int year, @Param(value = "month") int month);
    @Query(value = "FROM EdsDataLog EDL WHERE EDL.apiId =:apiId AND EDL.id =:reportId AND EDL.statusFlag = 0")
    EdsDataLog getReportDetails(@Param(value = "apiId") int id, @Param(value = "reportId")  int reportId);
}