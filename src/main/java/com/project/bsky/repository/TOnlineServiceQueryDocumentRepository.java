package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.TOnlineServiceQueryDocument;

@Repository
public interface TOnlineServiceQueryDocumentRepository extends JpaRepository<TOnlineServiceQueryDocument, Integer> {

}
