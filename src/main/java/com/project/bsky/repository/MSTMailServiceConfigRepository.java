package com.project.bsky.repository;

import com.project.bsky.model.MSTMailServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 07/07/2023 - 4:01 PM
 */
public interface MSTMailServiceConfigRepository extends JpaRepository<MSTMailServiceConfig, Integer> {
    @Query(value = "FROM MSTMailServiceConfig MMSC WHERE MMSC.mstMailService.id = ?1 AND MMSC.statusFlag = 0")
    Optional<MSTMailServiceConfig> findByMailServiceConfigById(int id);

    @Query(value = "FROM MSTMailServiceConfig MMSC WHERE MMSC.id = ?1")
    Optional<MSTMailServiceConfig> getMailServiceConfigDataById(int id);

    @Query(value = "FROM MSTMailServiceConfig MMSC ORDER BY MMSC.createdOn DESC")
    List<MSTMailServiceConfig> getAllMailServiceConfig();
}
