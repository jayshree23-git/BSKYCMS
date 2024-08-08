package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.TblBskySnaPostPymntSummary;

/**
 * @author ronauk
 *
 */
@Repository
public interface TblBskySnaPostPymntSmryRepository extends JpaRepository<TblBskySnaPostPymntSummary, Integer> {

}
