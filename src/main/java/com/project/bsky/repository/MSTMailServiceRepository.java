package com.project.bsky.repository;

import com.project.bsky.model.MSTMailService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 05/07/2023 - 11:32 AM
 */
public interface MSTMailServiceRepository extends JpaRepository<MSTMailService, Integer> {
    @Query(value = "FROM MSTMailService MMS")
    List<MSTMailService> findAllActiveMailService();

    @Query(value = "FROM MSTMailService MMS WHERE  MMS.statusFlag = 0")
    List<MSTMailService> getMailServiceNameList();
}
