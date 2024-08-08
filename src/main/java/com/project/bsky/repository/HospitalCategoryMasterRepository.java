package com.project.bsky.repository;

import com.project.bsky.model.HospitalCategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalCategoryMasterRepository extends JpaRepository<HospitalCategoryMaster, Long> {

}