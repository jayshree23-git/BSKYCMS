package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.ActionRemark;

@Repository
public interface RemarksMasterRepository extends JpaRepository<ActionRemark, Long>{

}
