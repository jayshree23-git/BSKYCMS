package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Cce;

import java.util.List;

@Repository
public interface CceRepository extends JpaRepository<Cce, Long> {

    List<Cce> findByTransactionIdAndUrnAndInvoice(Long transactionId, String urn, String invoice);

    List<Cce> findFirstByOrderByAttemptCountDesc();

}
