package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.WardCategoryMaster;

@Repository

public interface WardCategoryMasterRepository extends JpaRepository<WardCategoryMaster, Long> {

}



