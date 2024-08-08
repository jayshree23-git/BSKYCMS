package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.ModuleEntity;


@Repository
public interface ModuleRepository extends JpaRepository <ModuleEntity, Integer>{

}
