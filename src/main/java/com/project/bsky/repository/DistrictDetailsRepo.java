package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.DistrictDetails;

public interface DistrictDetailsRepo extends JpaRepository<DistrictDetails, Integer> {

}
