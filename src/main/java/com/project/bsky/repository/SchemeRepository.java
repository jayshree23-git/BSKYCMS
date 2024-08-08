package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.Scheme;

public interface SchemeRepository extends JpaRepository<Scheme, Integer>{
     
	List<Scheme> findByStatusFlagOrderBySchemeName(Integer statusFlag);
}
