package com.project.bsky.repository;

import com.project.bsky.model.MstApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Project : BSKY Backend
 * @Auther : Sambit Kumar Pradhan
 * @Created On : 13/06/2023 - 3:17 PM
 */
public interface MstApiRepository extends JpaRepository<MstApi, Long> {
    @Query("SELECT m FROM MstApi m ORDER BY m.apiId ASC")
    List<MstApi> findAllByOrder();
}
