package com.project.bsky.repository;

import com.project.bsky.model.HighEndDrugs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HighEndDrugsRepository extends JpaRepository<HighEndDrugs,Long> {
    @Query("SELECT h.id FROM HighEndDrugs h WHERE h.hedCode=:drugCode")
    Long getHedCodeById(String drugCode);
    @Query("SELECT count(*) FROM HighEndDrugs g WHERE g.hedCode=:hedCode")
    Integer countRowForCheckDuplicateDrugCode(String hedCode);

    HighEndDrugs findByHedCode(String hedCode);
//    @Query("SELECT h.id FROM HighEndDrugs h WHERE h.hedCode=:drugCode AND h.implantCode=:implantCode")
//    Long getImplantNameByHedCode(String drugCode, String implantCode);
}
