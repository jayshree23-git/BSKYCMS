package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.HospitalAuthTagging;

@Repository
public interface HospitalAuthTaggingRepository extends JpaRepository<HospitalAuthTagging, Integer> {

}
