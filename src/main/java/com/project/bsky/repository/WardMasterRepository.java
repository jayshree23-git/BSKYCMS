package com.project.bsky.repository;

import com.project.bsky.model.WardMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardMasterRepository extends JpaRepository<WardMaster,Long> {
    @Query("SELECT DISTINCT w.wardName from WardMaster w where w.deletedFlag=0")
    List<String> findByWardNames();
}
