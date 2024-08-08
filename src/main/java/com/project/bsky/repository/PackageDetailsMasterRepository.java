package com.project.bsky.repository;

import com.project.bsky.model.PackageDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageDetailsMasterRepository extends JpaRepository<PackageDetailsMaster, Long> {

}
