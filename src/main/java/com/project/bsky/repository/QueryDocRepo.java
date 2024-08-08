package com.project.bsky.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.entity.QueryDocEntity;

public interface QueryDocRepo  extends JpaRepository<QueryDocEntity, Integer>{

	@Query(value = "from QueryDocEntity where servId = :serId and  notId=:notingId")
	ArrayList<QueryDocEntity> getQueryDoc(Integer serId,Integer notingId);
	
}
