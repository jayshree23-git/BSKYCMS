package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.ActionType;

public interface ActionTypeRepository extends JpaRepository<ActionType, Long>{

}
