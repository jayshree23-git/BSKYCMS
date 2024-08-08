package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.SchemeCategoryMaster;

public interface SchemeCategoryMasterRepository extends JpaRepository<SchemeCategoryMaster, Integer>{
	
	List<SchemeCategoryMaster> findBySchemeIdAndStatusFlagOrderByCategoryName(Integer schemeId,Integer statusFlag);

}
