package com.project.bsky.repository;

import com.project.bsky.model.CardPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardPolicyRepository extends JpaRepository<CardPolicy,Long> {
    @Query("select distinct startDate,endDate From CardPolicy where blockedAmount=0")
    List<Object[]> findPolicyDate();
}
