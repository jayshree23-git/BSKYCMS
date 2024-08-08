package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Ward;
import com.project.bsky.model.WardDetails;

@Repository
public interface WardDetailsRepository extends JpaRepository<WardDetails, Long> {

	//List<Ward> findDetails(Long wardMasterId);

}
