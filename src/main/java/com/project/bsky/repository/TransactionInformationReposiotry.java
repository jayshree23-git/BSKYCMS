package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.TransactionInformation;

@Repository

public interface TransactionInformationReposiotry extends JpaRepository<TransactionInformation, Long> {

	

}
